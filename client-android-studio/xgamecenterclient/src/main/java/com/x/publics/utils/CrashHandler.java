package com.x.publics.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.x.business.account.AccountManager;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadNotificationManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.MasPlay;
import com.x.publics.http.model.RaveCrashRequest;
import com.x.publics.http.model.RaveCrashResponse;
import com.x.publics.http.model.RaveCrashRequest.RaveCrashRequestData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;

/**
 * @ClassName: CrashHandler
 * @Desciption: UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.  
 
 * @Date: 2014-1-25 上午11:36:06
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	// CrashHandler 实例
	private static CrashHandler INSTANCE = new CrashHandler();

	// 程序的 Context 对象
	private Context mContext;

	// 系统默认的 UncaughtException 处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	public static final String crash_log_file_enable = "zapp_crash.bin";

	/** 保证只有一个 CrashHandler 实例 */
	private CrashHandler() {
	}

	/** 获取 CrashHandler 实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		// 获取系统默认的 UncaughtException 处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

		// 设置该 CrashHandler 为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当 UncaughtException 发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}

			// 退出程序,注释下面的重启启动程序代码
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);

			// 重新启动程序，注释上面的退出程序
			//Intent intent = new Intent();
			//intent.setClass(mContext, MainActivity.class);
			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//mContext.startActivity(intent);
			//android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
	 * 
	 * @param ex
	 * @return true：如果处理了该异常信息；否则返回 false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用 Toast 来显示异常信息
		//		new Thread() {
		//			@Override
		//			public void run() {
		//				Looper.prepare();
		//				ToastUtil.show(mContext, "很抱歉，程序出现异常，即将退出。", Toast.LENGTH_LONG);
		//				Looper.loop();
		//			}
		//		}.start();

		stopDownload();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件（本地 并 上传服务器）
		saveCrashInfo2File(ex);

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			Log.e(TAG, "error : ", e);
		}
		return false;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中*
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private void saveCrashInfo2File(Throwable ex) {
		final StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + " = " + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		String result = writer.toString();
		sb.append(result);

		try {
			if (Utils.isCheckFileExist(crash_log_file_enable)) {
				long timestamp = System.currentTimeMillis();
				String time = formatter.format(new Date());
				String fileName = "crash-" + time + "-" + timestamp + ".log";

				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 文件存储路径
					String path = StorageUtils.FILE_ROOT + "crash/";
					File dir = new File(path);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					FileOutputStream fos = new FileOutputStream(path + fileName);
					fos.write(sb.toString().getBytes());
					fos.close();
				}
			}

			// 发送错误报告
			sendErrorInfo2Server(sb.toString());

		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
	}

	/**
	 * 上传错误信息到服务器 
	 */
	private void sendErrorInfo2Server(String info) {

		RaveCrashRequest request = new RaveCrashRequest();
		RaveCrashRequestData data = new RaveCrashRequestData();
		data.setClientId(AccountManager.getInstance().getClientId(mContext));
		data.setContent(info);
		data.setDeviceModel(android.os.Build.MODEL);
		data.setDeviceVendor(android.os.Build.MANUFACTURER);
		data.setDeviceType(Utils.isTablet(mContext) == false ? 1 : 2);
		data.setOsVersion(String.valueOf(android.os.Build.VERSION.SDK_INT));
		data.setOsVersionName(android.os.Build.VERSION.RELEASE);

		MasPlay masPlay = new MasPlay();
		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), 0);

			masPlay.setMasPackageName(mContext.getPackageName());
			masPlay.setMasVersionCode(packageInfo.versionCode);
			masPlay.setMasVersionName(packageInfo.versionName);

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		request.setData(data);
		request.setMasPlay(masPlay);

		DataFetcher.getInstance().sendCrashReport(request, crashRequestListener, myErrorListener);
	}

	/**
	 * <response_1>数据响应
	 */
	private Listener<JSONObject> crashRequestListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {

			LogUtil.getLogger().d("response==>" + response.toString());

			// 响应数据，进行操作
			RaveCrashResponse crashResponse = (RaveCrashResponse) JsonUtil
					.jsonToBean(response, RaveCrashResponse.class);

			if (crashResponse != null && crashResponse.state.code == 200) {
				//ToastUtil.show(mContext, "success",Toast.LENGTH_LONG);
			} else {
				//ToastUtil.show(mContext, "fail",Toast.LENGTH_LONG);
				System.exit(0);
			}
		}
	};

	/**
	 * 获取异常响应处理
	 */
	private ErrorListener myErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			//ToastUtil.show(mContext, "error",Toast.LENGTH_LONG);
			System.exit(0);
		}
	};

	public void stopDownload() {
		DownloadEntityManager.getInstance().updateAllPause();
		DownloadNotificationManager.getInstance(mContext).cancleAll();
	}
}