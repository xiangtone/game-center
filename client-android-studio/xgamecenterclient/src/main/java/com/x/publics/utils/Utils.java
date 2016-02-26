package com.x.publics.utils;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.x.R;
import com.x.business.resource.ResourceManage;
import com.x.business.settings.SettingModel;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.db.resource.NativeResourceDBHelper;
import com.x.db.resource.NativeResourceConstant.Category;
import com.x.db.resource.NativeResourceConstant.FileType;
import com.x.db.resource.NativeResourceConstant.Voice;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.model.AppsUpgradeResponse;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.FavoriteAppBean;
import com.x.publics.model.FileBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.MyAppsBean;
import com.x.publics.model.RingtonesBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.Constan.MediaType;
import com.x.ui.activity.feedback.SubmitFeedbackActivity;
import com.x.ui.activity.home.SplashActivity;
import com.x.ui.view.MyAloneDialog;
import com.x.ui.view.MyDialog;

@SuppressLint("NewApi")
public class Utils {

	private static final String SCHEME = "package";
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
	private static final String APP_PKG_NAME_22 = "pkg";
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	private static List<Activity> activityList = new LinkedList<Activity>();
	private static List<Activity> actList = new LinkedList<Activity>();
	private static List<Activity> finishList = new LinkedList<Activity>();

	/**
	 * 获取MAC地址
	 */

	public static String getMAC(Context context) {
		String mac = null;// MAC地址
		if (mac == null) {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			if (info != null) {
				mac = info.getMacAddress();
			}
		}
		return mac;
	}

	/**
	 * 获取手机唯一编号
	 */
	private static String imei;// 手机设备唯一编号

	public static String getIMEI(Context context) {
		if (imei == null && context != null) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();// 手机串号
			if (imei == null || imei.length() == 0) {
				imei = "000000000000000";
			}
		}
		return imei;
	}

	/**
	 * 获取SIM卡唯一编号
	 */
	private static String imsi;// SIM卡唯一编号

	public static String getIMSI(Context context) {
		if (imsi == null && context != null) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imsi = tm.getSubscriberId();
			if (imsi == null || imsi.length() == 0) {
				imsi = "000000000000000";
			}
		}
		return imsi;
	}

	/**
	 * 获取手机型号
	 */
	private static String deviceModel;// 手机型号

	public static String getDeviceModel() {
		if (deviceModel == null) {
			deviceModel = android.os.Build.MODEL;
		}
		return deviceModel;
	}

	/**
	 * 获取手机型号
	 */
	private static String manufacturer;// 设备厂商型号

	public static String getDeviceManufacturer() {
		if (manufacturer == null) {
			manufacturer = android.os.Build.MANUFACTURER;
		}
		return manufacturer;
	}

	/**
	 * 获取Android系统版本
	 */
	private static String androidRelease;// Android系统版本

	public static String getAndroidRelease() {
		if (androidRelease == null) {
			androidRelease = android.os.Build.VERSION.RELEASE;
		}
		return androidRelease;
	}

	/**
	 * 获取Android系统API版本号
	 */
	private static int androidSDKINT;// Android系统API版本号

	public static int getAndroidSDKINT() {
		if (androidSDKINT == 0) {
			androidSDKINT = android.os.Build.VERSION.SDK_INT;
		}
		return androidSDKINT;
	}

	/**
	 * 获取零流量分享用户名
	 */
	public static String getZeroShareNickName() {
		String nickName = android.os.Build.MODEL;
		return nickName;
	}

	/**
	 * 获取手机号码
	 */
	private static String phoneNumber;// 手机号码

	public static String getPhoneNumber(Context context) {
		if (phoneNumber == null) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			phoneNumber = tm.getLine1Number();
		}
		return phoneNumber;
	}

	/**
	 * @Title: showInstalledAppDetails
	 * @Description: 打开系统应用信息页面
	 * @param @param context
	 * @param @param packageName
	 * @return void
	 * @throws
	 */

	public static void showInstalledAppDetails(Context context, String packageName) {
		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) {
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts(SCHEME, packageName, null);
			intent.setData(uri);
		} else {
			final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packageName);
		}
		context.startActivity(intent);
	}

	private static final int CPU_COUNT = 9;
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int KEEP_ALIVE = 1;
	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);
	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};

	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
			KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

	/**
	 * 使用{@link AsyncTask.THREAD_POOL_EXECUTOR} 执行AsyncTask 这样可以避免android
	 * 4.0以上系统 每次只执行一个 asyncTask
	 * 
	 * @param task
	 * @param params
	 */
	public static <Params, Progress, Result> void executeAsyncTask(AsyncTask<Params, Progress, Result> task,
			Params... params) {
		if (VERSION.SDK_INT >= 11) {
			task.executeOnExecutor(THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}

	public static int calculateMemoryCacheSize(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		boolean largeHeap = (context.getApplicationInfo().flags & FLAG_LARGE_HEAP) != 0;
		int memoryClass = am.getMemoryClass();
		if (largeHeap && SDK_INT >= HONEYCOMB) {
			memoryClass = ActivityManagerHoneycomb.getLargeMemoryClass(am);
		}
		// Target ~15% of the available heap.
		LogUtil.getLogger().d("LruCache size ======" + (memoryClass / 7) + "M");
		return 1024 * 1024 * memoryClass / 7;
	}

	@TargetApi(HONEYCOMB)
	private static class ActivityManagerHoneycomb {
		static int getLargeMemoryClass(ActivityManager activityManager) {
			return activityManager.getLargeMemoryClass();
		}
	}

	public static Drawable getPictureDraw(byte[] b) {
		if (b == null)
			return null;
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, null);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		return bitmapDrawable;
	}

	public static String sizeFormat(long size) {
		// DecimalFormat df = new DecimalFormat("###.##");
		// float f;
		// if (size < 1024 * 1024) {
		// f = (float) ((float) size / (float) 1024);
		// return (df.format(Float.valueOf(f).doubleValue() + 0) + "KB");
		// } else {
		// f = (float) ((float) size / (float) (1024 * 1024));
		// return (df.format(Float.valueOf(f).doubleValue()) + "MB");
		// }
		if (size / (1024 * 1024 * 1024) > 0) {
			float tmpSize = (float) (size) / (float) (1024 * 1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "G";
		} else if (size / (1024 * 1024) > 0) {
			float tmpSize = (float) (size) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		} else if (size / 1024 > 0) {
			return "" + (size / (1024)) + "KB";
		} else
			return "" + size + "B";

	}

	public static String sizeFormat2(long size) {
		DecimalFormat df = new DecimalFormat("###");
		float f;
		if (size < 1024 * 1024) {
			f = (float) ((float) size / (float) 1024);
			return (df.format(Float.valueOf(f).doubleValue()) + "KB");
		} else {
			f = (float) ((float) size / (float) (1024 * 1024));
			return (df.format(Float.valueOf(f).doubleValue()) + "MB");
		}
	}

	public static String formatData(long time) {
		String data = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			data = sdf.format(new Date(time));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return data;
	}

	public static String getString(Context context, int id) {
		return context.getResources().getString(id);
	}

	public static void launchAnotherApp(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();
		Intent it = new Intent();
		try {
			it = packageManager.getLaunchIntentForPackage(packageName);
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean launchApp(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();
		Intent it = new Intent();
		try {
			it = packageManager.getLaunchIntentForPackage(packageName);
			context.startActivity(it);
			return true;
		} catch (Exception e) {
		}
		return false;
	}


	/**
	 * 获取当前应用包名
	 */
	public static String getPackageName(Context context) {
		String packageName = "";
		if (context != null) {
			packageName = context.getPackageName();
		}
		return packageName;
	}

	/**
	 * 获取本应用版本名
	 * 
	 * @return
	 */
	public static String getVersionName(Context context) {
		String version = "";
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取本应用版本号
	 * 
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionCode = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * @Title: getAppIcon
	 * @Description: 根据包名获取icon
	 * @param @param context
	 * @param @param packageName
	 * @param @return
	 * @return Drawable
	 * @throws
	 */

	public static Drawable getAppIcon(Context context, String packageName) {
		Drawable drawable = null;
		try {
			drawable = context.getPackageManager().getApplicationIcon(packageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drawable;
	}

	public static Drawable getAppIconByFile(Context context, String localPath) {
		Drawable drawable = null;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pkgInfo = pm.getPackageArchiveInfo(localPath, PackageManager.GET_ACTIVITIES);
			if (pkgInfo != null) {
				ApplicationInfo appInfo = pkgInfo.applicationInfo;
				appInfo.sourceDir = localPath;
				appInfo.publicSourceDir = localPath;
				drawable = pm.getApplicationIcon(appInfo);// 得到图标信息
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drawable;
	}

	/**
	 * @Title: hasInstallShortcut
	 * @Description:
	 * @param @param context
	 * @param @return
	 * @return boolean
	 * @throws
	 */

	public static boolean hasInstallShortcut(Context context) {
		boolean hasInstall = false;
		try {
			final String AUTHORITY = "com.android.launcher.settings";
			Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
			Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[] { "title", "iconResource" },
					"title=?", new String[] { context.getString(R.string.app_name) }, null);
			if (cursor != null && cursor.getCount() > 0) {
				hasInstall = true;
			}
			if (cursor != null) {
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hasInstall;
	}

	/**
	 * @Title: addShortcutToDesktop
	 * @Description: 添加桌面快捷方式
	 * @param @param context
	 * @return void
	 * @throws
	 */

	public static void addShortcutToDesktop(Context context) {
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra("duplicate", false);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(context, R.drawable.mas_ic_launcher));
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
				new Intent(context, SplashActivity.class).setAction(Intent.ACTION_MAIN));
		context.sendBroadcast(shortcut);

	}

	/**
	 * @Title: getSettingModel
	 * @Description: 获取应用设置
	 * @param @param context
	 * @param @return
	 * @return SettingModel
	 * @throws
	 */

	public static SettingModel getSettingModel(Context context) {
		SettingModel settingModel = new SettingModel();
		boolean gprsSavingMode = SettingsUtils.getValue(context, SettingsUtils.GPRS_SAVING_MODE, false);
		boolean gprsDownloadPromt = SettingsUtils.getValue(context, SettingsUtils.GPRS_DOWNLOAD_PROMT, true);
		boolean onlyWifiDownload = SettingsUtils.getValue(context, SettingsUtils.ONLY_WIFI_DOWNLOAD, false);
		boolean deleteApkFile = SettingsUtils.getValue(context, SettingsUtils.DELETE_APK_FILE, true);
		boolean autoInstall = SettingsUtils.getValue(context, SettingsUtils.To_INSTALL, true);
		boolean autoDownloadFavInWifi = SettingsUtils.getValue(context, SettingsUtils.AUTO_DOWNLOAD_FAV, false);
		boolean autoDownloadUpdateInWifi = SettingsUtils.getValue(context, SettingsUtils.AUTO_DOWNLOAD_UPDATE, false);
		boolean isFicheMode = SettingsUtils.getValue(context, SettingsUtils.IS_FICHE_MODE, false);
		boolean isFloatMode = SettingsUtils.getValue(context, SettingsUtils.FLOAT_SETTING, true);
		boolean isSoundMode = SettingsUtils.getValue(context, SettingsUtils.SOUND_SETTING, true);
		boolean isSlientInstall = SettingsUtils.getValue(context, SettingsUtils.SILENT_INSTALL, false);

		settingModel.setGprsSavingMode(gprsSavingMode);
		settingModel.setGprsDownloadPromt(gprsDownloadPromt);
		settingModel.setOnlyWifiDownload(onlyWifiDownload);
		settingModel.setDeleteApkFile(true);
		settingModel.setAutoInstall(autoInstall);
		settingModel.setAutoDownloadFavInWifi(autoDownloadFavInWifi);
		settingModel.setAutoDownloadUpdateInWifi(autoDownloadUpdateInWifi);
		settingModel.setFicheMode(isFicheMode);
		settingModel.setFloatMode(false);// 屏蔽悬浮窗
		settingModel.setSoundEffect(isSoundMode);
		settingModel.setSilentInstall(isSlientInstall);
		return settingModel;
	}

	/**
	 * @Title: showDialog
	 * @Description: 弹出提示对话框
	 * @param @param context
	 * @param @param title
	 * @param @param content
	 * @param @param posiTips
	 * @param @param positiveListener
	 * @param @param negaTips
	 * @param @param negativeListener
	 * @return void
	 * @throws
	 */

	public static Dialog showDialog(Context context, String title, String content, String posiTips,
			DialogInterface.OnClickListener positiveListener, String negaTips,
			DialogInterface.OnClickListener negativeListener) {
		// 创建了对话框的构造器
		MyDialog.Builder builder = new MyDialog.Builder(context); // 自定义dialog
		// 设置更新标题
		builder.setTitle(title);
		// 设置更新提示内容
		builder.setMessage(content);
		// 左边按钮
		builder.setPositiveButton(negaTips, negativeListener);
		// 右边按钮
		builder.setNegativeButton(posiTips, positiveListener);

		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	/**
	 * @Title: showAloneDialog
	 * @Description: 弹出单个按钮对话框
	 * @param @param context
	 * @param @param title
	 * @param @param content
	 * @param @param negaTips
	 * @param @param negativeListener
	 * @return void
	 */

	public static Dialog showAloneDialog(Context context, String title, String content, String negaTips,
			DialogInterface.OnClickListener negativeListener, boolean isShowloading) {
		// 创建了对话框的构造器
		MyAloneDialog.Builder builder = new MyAloneDialog.Builder(context); // 自定义dialog
		// 设置更新标题
		builder.setTitle(title);
		// 设置提示内容
		builder.setMessage(content);
		builder.setShowloading(isShowloading);
		builder.setMessageGravity(Gravity.CENTER_HORIZONTAL);
		// 右边按钮
		builder.setNegativeButton(negaTips, negativeListener);
		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	/**
	 * 铃声设置
	 * 
	 * @param context
	 * @param path
	 */
	public static boolean showRingtonesSettingDialog(final Context context, final String path) {
		File file = new File(path);
		if (!file.exists()) {
			ToastUtil.show(context, R.string.ringtones_set_failure, 1000);
			return false;
		}
		final boolean[] choice_arrays = new boolean[] { false, false /* , false */};
		final String[] title_arrays = context.getResources().getStringArray(R.array.music_settings);

		DialogInterface.OnMultiChoiceClickListener choiceClickListener = new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				choice_arrays[which] = isChecked;
			}
		};

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				boolean temp = false;
				for (int i = 0; i < choice_arrays.length; i++) {
					if (choice_arrays[i]) {
						temp = true;
						switch (i) {
						case 0:
							ResourceManage.getInstance(context).setVoice(Voice.RINGTONES, path);
							break;
						case 1:
							ResourceManage.getInstance(context).setVoice(Voice.NOTIFICATION, path);
							break;
						case 2:// 闹钟已屏蔽
							ResourceManage.getInstance(context).setVoice(Voice.ALARM, path);
							break;
						}
					}
				}
				if (temp) {
					ToastUtil.show(context, context.getResources().getString(R.string.ringtones_set_succ),
							Toast.LENGTH_SHORT);
				}
			}
		};
		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};

		showSettingDialog(context, ResourceUtil.getString(context, R.string.music_dialog_title), title_arrays,
				choice_arrays, choiceClickListener, ResourceUtil.getString(context, R.string.confirm),
				positiveListener, ResourceUtil.getString(context, R.string.cancel), negativeListener);
		return true;
	}

	/**
	 * Music Settings Dialog
	 * 
	 * @param context
	 * @param title
	 * @param title_arrays
	 * @param choice_arrays
	 * @param clickListener
	 * @param posiTips
	 * @param positiveListener
	 * @param negaTips
	 * @param negativeListener
	 */
	public static void showSettingDialog(Context context, String title, String[] title_arrays, boolean[] choice_arrays,
			DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener, String posiTips,
			DialogInterface.OnClickListener positiveListener, String negaTips,
			DialogInterface.OnClickListener negativeListener) {
		// 创建了对话框的构造器
		MyDialog.Settings builder = new MyDialog.Settings(context); // 自定义dialog
		// 设置更新标题
		builder.setTitle(title);
		// 复选框监听事件
		builder.setMultiChoiceItems(multiChoiceClickListener);
		// 选项列表
		builder.setChoiceItem(title_arrays);
		// 选项列表对应选中状态
		builder.setChoiceStatus(choice_arrays);
		// 左边按钮
		builder.setPositiveButton(negaTips, negativeListener);
		// 右边按钮
		builder.setNegativeButton(posiTips, positiveListener);

		builder.create().show();
	}

	/**
	 * @Title: deleteFile
	 * @Description: 删除文件
	 * @param @param filePath
	 * @return void
	 * @throws
	 */
	public static void deleteFile(String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists())
				file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 封装AppInfoBean
	 * 
	 * @param favoriteAppBean
	 * @return
	 */
	public static AppInfoBean getAppInfoBean(FavoriteAppBean favoriteAppBean) {
		if (favoriteAppBean == null) {
			return null;
		}
		AppInfoBean appInfoBean = new AppInfoBean();
		appInfoBean.setApkId(favoriteAppBean.getFavoriteResourceId());
		appInfoBean.setAppId(favoriteAppBean.getFavoriteResourceId());
		appInfoBean.setAppName(favoriteAppBean.getFavoriteAppName());
		appInfoBean.setCategoryId(favoriteAppBean.getFavoriteCategoryId());
		appInfoBean.setFileSize(favoriteAppBean.getFavoriteSize());
		appInfoBean.setLogo(favoriteAppBean.getFavoriteIconUrl());
		appInfoBean.setPackageName(favoriteAppBean.getFavoritePackageName());
		appInfoBean.setStars(favoriteAppBean.getFavoriteStarts());
		appInfoBean.setStatus(favoriteAppBean.getFavoriteStatus());
		appInfoBean.setUrl(favoriteAppBean.getFavoriteDownloadUrl());
		appInfoBean.setVersionCode(favoriteAppBean.getFavoriteVersionCode());
		appInfoBean.setVersionName(favoriteAppBean.getFavoriteVersionName());
		appInfoBean.setAppId(favoriteAppBean.getFavoriteAppId());
		if (!TextUtils.isEmpty(favoriteAppBean.getFavoriteAttribute())) {
			appInfoBean.setFileType(Integer.valueOf(favoriteAppBean.getFavoriteAttribute()));
		}

		return appInfoBean;
	}

	public static MyAppsBean getMyAppBean(FavoriteAppBean favoriteAppBean, MyAppsBean appsBean) {
		if (favoriteAppBean == null || appsBean == null) {
			return null;
		}
		appsBean.setApkId(favoriteAppBean.getFavoriteResourceId());
		appsBean.setAppId(favoriteAppBean.getFavoriteResourceId());
		appsBean.setAppName(favoriteAppBean.getFavoriteAppName());
		appsBean.setCategoryId(favoriteAppBean.getFavoriteCategoryId());
		appsBean.setFileSize(favoriteAppBean.getFavoriteSize());
		appsBean.setLogo(favoriteAppBean.getFavoriteIconUrl());
		appsBean.setPackageName(favoriteAppBean.getFavoritePackageName());
		appsBean.setStars(favoriteAppBean.getFavoriteStarts());
		appsBean.setStatus(favoriteAppBean.getFavoriteStatus());
		appsBean.setOriginalUrl(favoriteAppBean.getFavoriteDownloadUrl());
		appsBean.setUrl(favoriteAppBean.getFavoriteDownloadUrl());
		appsBean.setVersionCode(favoriteAppBean.getFavoriteVersionCode());
		appsBean.setVersionName(favoriteAppBean.getFavoriteVersionName());
		appsBean.setAppId(favoriteAppBean.getFavoriteAppId());
		if (!TextUtils.isEmpty(favoriteAppBean.getFavoriteAttribute())) {
			appsBean.setFileType(Integer.valueOf(favoriteAppBean.getFavoriteAttribute()));
		}

		return appsBean;
	}

	/**
	 * 
	 * @Title: deletePicFile
	 * @Description: 删除图片，更新媒体库
	 * @param @param con
	 * @throws
	 */
	public static void deletePicFile(Context con, String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
				NativeResourceDBHelper.getInstance(con).notifyFileSystemChanged(filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AppInfoBean getAppInfoBean(InstallAppBean installAppBean) {
		AppInfoBean appInfoBean = new AppInfoBean();
		appInfoBean.setAppName(installAppBean.getAppName());
		appInfoBean.setApkId(installAppBean.getApkId());
		appInfoBean.setAppId(installAppBean.getAppId());
		appInfoBean.setPackageName(installAppBean.getPackageName());
		appInfoBean.setVersionName(installAppBean.getVersionName());
		appInfoBean.setVersionCode(installAppBean.getVersionCode());
		appInfoBean.setCategoryId(installAppBean.getCategoryId());
		appInfoBean.setUrl(installAppBean.getUrl());
		appInfoBean.setLogo(installAppBean.getLogo());
		appInfoBean.setStars(installAppBean.getStars());
		appInfoBean.setStatus(convertStatus(installAppBean.getStatus()));
		appInfoBean.setFileSize(Long.valueOf(installAppBean.getFileSize()));
		appInfoBean.setFileType(installAppBean.getFileType());
		return appInfoBean;
	}

	public static AppInfoBean getAppInfoBean(MyAppsBean appsBean) {
		AppInfoBean appInfoBean = new AppInfoBean();
		appInfoBean.setAppName(appsBean.getAppName());
		appInfoBean.setApkId(appsBean.getApkId());
		appInfoBean.setAppId(appsBean.getAppId());
		appInfoBean.setPackageName(appsBean.getPackageName());
		appInfoBean.setVersionName(appsBean.getVersionName());
		appInfoBean.setVersionCode(appsBean.getVersionCode());
		appInfoBean.setCategoryId(appsBean.getCategoryId());
		appInfoBean.setUrl(appsBean.getOriginalUrl());
		appInfoBean.setLogo(appsBean.getLogo());
		appInfoBean.setStars(appsBean.getStars());
		appInfoBean.setStatus(convertStatus(appsBean.getStatus()));
		appInfoBean.setFileSize(Long.valueOf(appsBean.getFileSize()));
		appInfoBean.setFileType(appsBean.getFileType());
		return appInfoBean;
	}

	public static AppInfoBean getAppInfoBean(DownloadBean downloadBean) {
		AppInfoBean appInfoBean = new AppInfoBean();
		appInfoBean.setAppName(downloadBean.getName());
		appInfoBean.setApkId(downloadBean.getResourceId());
		appInfoBean.setAppId(downloadBean.getAppId());
		appInfoBean.setPackageName(downloadBean.getPackageName());
		appInfoBean.setVersionName(downloadBean.getVersionName());
		appInfoBean.setVersionCode(downloadBean.getVersionCode());
		appInfoBean.setCategoryId(downloadBean.getCategoryId());
		appInfoBean.setUrl(downloadBean.getUrl());
		appInfoBean.setLogo(downloadBean.getIconUrl());
		appInfoBean.setStars(downloadBean.getStars());
		appInfoBean.setStatus(convertStatus(downloadBean.getStatus()));
		appInfoBean.setFileSize(downloadBean.getTotalBytes());
		appInfoBean.setFileType(downloadBean.getFileType());
		return appInfoBean;
	}

	public static InstallAppBean getInstallAppBean(DownloadBean downloadBean) {
		InstallAppBean installAppBean = new InstallAppBean();
		installAppBean.setAppName(downloadBean.getName());
		installAppBean.setApkId(downloadBean.getResourceId());
		installAppBean.setAppId(downloadBean.getAppId());
		installAppBean.setPackageName(downloadBean.getPackageName());
		installAppBean.setVersionName(downloadBean.getVersionName());
		installAppBean.setVersionCode(downloadBean.getVersionCode());
		installAppBean.setCategoryId(downloadBean.getCategoryId());
		installAppBean.setUrl(downloadBean.getOriginalUrl());
		if (downloadBean.isPatch()) {
			installAppBean.setUrlPatch(downloadBean.getUrl());
		}
		installAppBean.setLogo(downloadBean.getIconUrl());
		installAppBean.setStars(downloadBean.getStars());
		installAppBean.setStatus(convertStatus(downloadBean.getStatus()));
		installAppBean.setFileSize(downloadBean.getTotalBytes());
		installAppBean.setCurrentBytes(downloadBean.getCurrentBytes());
		installAppBean.setTotalBytes(downloadBean.getTotalBytes());
		installAppBean.setSpeed(downloadBean.getSpeed());
		return installAppBean;
	}

	public static WallpaperBean getWallpaperBean(DownloadBean downloadBean) {
		WallpaperBean wallpaperBean = new WallpaperBean();
		wallpaperBean.setImageId(downloadBean.getResourceId());
		wallpaperBean.setCategoryId(downloadBean.getCategoryId());
		wallpaperBean.setImageName(downloadBean.getName());
		wallpaperBean.setLogo(downloadBean.getIconUrl());
		wallpaperBean.setUrl(downloadBean.getUrl());
		wallpaperBean.setFileSize((int) downloadBean.getTotalBytes());
		return wallpaperBean;
	}

	public static int convertStatus(int status) {
		int result = AppInfoBean.Status.NORMAL;
		switch (status) {
		case DownloadTask.TASK_LAUNCH:
			result = AppInfoBean.Status.CANLAUNCH;
			break;
		case DownloadTask.TASK_PAUSE:
			result = AppInfoBean.Status.PAUSED;
			break;
		case DownloadTask.TASK_DOWNLOADING:
			result = AppInfoBean.Status.DOWNLOADING;
			break;
		case DownloadTask.TASK_WAITING:
			result = AppInfoBean.Status.WAITING;
			break;
		case DownloadTask.TASK_FINISH:
			result = AppInfoBean.Status.CANINSTALL;
			break;
		case DownloadTask.TASK_CONNECTING:
			result = AppInfoBean.Status.CONNECTING;
			break;
		case DownloadTask.TASK_INSTALLING:
			result = AppInfoBean.Status.INSTALLING;
			break;
		}
		return result;
	}

	public static boolean isAppExit(String packageName, Context context) {
		if (packageName == null)
			return false;
		Intent it = context.getPackageManager().getLaunchIntentForPackage(packageName);
		return (it != null);
	}

	public static AppsUpgradeResponse getUpdateData(Context context) {
		String upgradeJson = SharedPrefsUtil.getValue(context, "upgrade_data", "");
		AppsUpgradeResponse upgradeResponse = null;
		JSONObject json = null;
		try {
			json = new JSONObject(upgradeJson);
			upgradeResponse = (AppsUpgradeResponse) JsonUtil.jsonToBean(json, AppsUpgradeResponse.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return upgradeResponse;
	}

	public static void saveUpdataData(AppsUpgradeResponse appsUpgradeResponse, Context context) {
		String newJson;
		if (!appsUpgradeResponse.applist.isEmpty())
			newJson = JsonUtil.objectToJson(appsUpgradeResponse);
		else
			newJson = "";
		SharedPrefsUtil.putValue(context, "upgrade_data", newJson);
	}

	/**
	 * 转换下载数量，显示规则：1,000+ 5,000+ 10,000+ 50,000+ 100,000+ 500,000+ 1,000,000+
	 * 5,000,000+ 10,000,000+ 50,000,000+ 100,000,000+ 500,000,000+
	 * 
	 * @param count
	 * @return
	 */
	public static String changeDownloads(int count) {
		if (count < 5001) {
			return "1,000+";
		} else if (count > 5000 && count < 10001) {
			return "5,000+";
		} else if (count > 10000 && count < 50001) {
			return "10,000+";
		} else if (count > 50000 && count < 100001) {
			return "50,000+";
		} else if (count > 100000 && count < 500001) {
			return "100,000+";
		} else if (count > 500000 && count < 1000001) {
			return "500,000+";
		} else if (count > 1000000 && count < 5000001) {
			return "1,000,000+";
		} else if (count > 5000000 && count < 10000001) {
			return "5,000,000+";
		} else if (count > 10000000 && count < 50000001) {
			return "10,000,000+";
		} else if (count > 50000000 && count < 100000001) {
			return "50,000,000+";
		} else if (count > 100000000 && count < 500000001) {
			return "100,000,000+";
		} else if (count > 500000000 && count < 1000000000) {
			return "500,000,000+";
		} else {
			return "1,000,000,000+";
		}
	}

	/**
	 * 字符串去空格，去除前后空格，保留中间空格
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSpace(String str) {
		str = str.trim();
		while (str.startsWith(" ")) {
			str = str.substring(1, str.length()).trim();
		}
		while (str.endsWith(" ")) {
			str = str.substring(0, str.length() - 1).trim();
		}
		return str;
	}

	/**
	 * 获取mannifest中metaDate的值
	 * 
	 * @param context
	 * @param packageName
	 * @param metaDateKey
	 * @return
	 */
	public static int getMetaDataIntValue(Context context, String metaDateKey) {
		int metaDateValue = 0;
		if (!TextUtils.isEmpty(metaDateKey)) {
			ApplicationInfo appInfo;
			try {
				appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA);
				if (appInfo != null) {
					Bundle metaData = appInfo.metaData;
					if (metaData != null) {
						if (metaData.containsKey(metaDateKey)) {
							int metaDateInt = metaData.getInt(metaDateKey, -1);
							if (metaDateInt != -1) {
								metaDateValue = metaDateInt;
							}
						}
					}
				}
			} catch (NameNotFoundException e) {
			} catch (Exception e) {
			}
		}
		return metaDateValue;
	}

	/**
	 * 获取mannifest中metaDate的值
	 * 
	 * @param context
	 * @param packageName
	 * @param metaDateKey
	 * @return
	 */
	public static String getMetaDataStringValue(Context context, String metaDateKey) {
		String metaDateValueStr = null;
		if (!TextUtils.isEmpty(metaDateKey)) {
			ApplicationInfo appInfo;
			try {
				appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA);
				if (appInfo != null) {
					Bundle metaData = appInfo.metaData;
					if (metaData != null) {
						if (metaData.containsKey(metaDateKey)) {
							metaDateValueStr = metaData.getString(metaDateKey);
						}
					}
				}
			} catch (NameNotFoundException e) {
			} catch (Exception e) {
			}
		}
		return metaDateValueStr;
	}

	/**
	 * 添加activity 到集合中，当子元素超过10个时，去除第一个元素
	 * 
	 * @param context
	 * @param activity
	 * @return
	 */
	public static void addActivity(Activity activity) {
		activityList.add(activity);
		if (activityList.size() > 10) {
			activityList.get(0).finish();
			activityList.remove(0);
			System.gc();
		}
	}

	public static void addAct(Activity activity) {
		actList.add(activity);
	}

	public static void finishAct() {
		for (int i = 0; i < actList.size(); i++) {
			actList.get(i).finish();
		}
	}

	/**
	 * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
	 * 
	 * @param content
	 *            将要生成一维码的内容
	 * @return 返回生成好的一维码bitmap
	 * @throws WriterException
	 *             WriterException异常
	 */
	public static Bitmap CreateBarCode(String content) throws Exception {
		// 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 500, 200);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 将指定的内容生成成二维码
	 * 
	 * @param content
	 *            将要生成二维码的内容
	 * @return 返回生成好的二维码事件
	 * @throws WriterException
	 *             WriterException异常
	 */
	public static Bitmap createQRCode(String content) {
		Bitmap bitmap = null;
		try {
			// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
			BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 600, 600);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			// 二维矩阵转为一维像素数组,也就是一直横着排了
			int[] pixels = new int[width * height]; // 开辟内存空间，有可能导致内存溢出，所以需要捕获
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					}
				}
			}

			bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			// 通过像素数组生成bitmap,具体参考api
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bitmap;
	}

	/***
	 * 合并图片
	 * 
	 * @param src
	 * @param watermark
	 * @return
	 */
	public static Bitmap createBitmap(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();

		// create the new blank bitmap
		Bitmap newBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newBitmap);

		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src

		// 在src的中间画watermark
		cv.drawBitmap(watermark, w / 2 - ww / 2, h / 2 - wh / 2, null);// 设置ic_launcher的位置

		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存

		// store
		cv.restore();// 存储

		return newBitmap;

	}

	/***
	 * 缩放图片
	 * 
	 * @param src
	 * @param destWidth
	 * @param destHeigth
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap src, int destWidth, int destHeigth) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();// 源文件的大小
		int h = src.getHeight();
		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) destWidth) / w;// 宽度缩小比例
		float scaleHeight = ((float) destHeigth) / h;// 高度缩小比例

		Matrix m = new Matrix();// 矩阵
		m.postScale(scaleWidth, scaleHeight);// 设置矩阵比例
		Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, m, true);// 直接按照矩阵的比例把源文件画入进行
		return resizedBitmap;
	}

	/**
	 * @Title: setQRCode
	 * @Description: 设置合并好二维码图片
	 * @param @param view
	 * @param @param resId
	 * @param @param str
	 * @param @param context
	 * @return void
	 */
	public static void setQRCodeBackground(View view, int resId, String msg, Context context) {
		Bitmap mBitmap = createQRCode(msg);
		if (mBitmap == null)
			return;
		Bitmap mBitmap2 = createBitmap(mBitmap,
				zoomBitmap(BitmapFactory.decodeResource(context.getResources(), resId), 80, 80));
		BitmapDrawable bd = new BitmapDrawable(context.getResources(), mBitmap2);
		view.setBackgroundDrawable(bd);
	}

	/**
	 * 递归删除文件
	 * 
	 * @param file
	 
	 */
	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete();
			} else if (file.isDirectory()) { // 如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代，>>> 递归
				}
			}
			file.delete();
		}
	}

	/**
	 * 截取文件名
	 * 
	 * @param filepath
	 * @return
	 
	 */
	public static String getNameFromFilepath(String filepath) {
		int pos = filepath.lastIndexOf('/');
		if (pos != -1) {
			return filepath.substring(pos + 1);
		}
		return "";
	}

	/**
	 * 截取文件名，不显示后缀
	 * 
	 * @param filepath
	 * @return
	 
	 */
	public static String getNameNOFromFilepath(String filepath) {
		int pos = filepath.lastIndexOf('/');
		if (pos != -1) {
			int last = filepath.lastIndexOf('.');
			if (last > (pos + 1)) {
				return filepath.substring(pos + 1, filepath.lastIndexOf("."));
			} else {
				return getNameFromFilepath(filepath);
			}
		}
		return "";
	}

	/**
	 * 截取文件后缀
	 * 
	 * @param filepath
	 * @return
	 
	 */
	public static String getTypeFromFilepath(String filepath) {
		int dotPosition = filepath.lastIndexOf('.');
		String ext = filepath.substring(dotPosition + 1, filepath.length()).toLowerCase();
		return ext;
	}

	/**
	 * 获取文件详细
	 * 
	 * @param filePath
	 * @return
	 
	 */
	public static FileBean GetFileInfo(Category category, String filePath, boolean isSuffix) {
		File file = new File(filePath);
		if (!file.exists())
			return null;
		FileBean fileInfo = new FileBean();

		switch (category) {
		case VIDEO:
			String type = getTypeFromFilepath(filePath);
			if (!MimeUtils.videoToMimeTypeMap.containsKey(type)) {
				return null;
			}
			break;
		}
		if (isSuffix)
			fileInfo.setFileName(getNameFromFilepath(filePath));
		else
			fileInfo.setFileName(getNameNOFromFilepath(filePath));
		fileInfo.setModifiedDate(file.lastModified());
		fileInfo.setFilePath(filePath);
		fileInfo.setFileSize(file.length());
		return fileInfo;
	}

	/**
	 * 获取文件类型
	 * 
	 * @param filePath
	 * @return
	 */
	// public static Category getFileType(String filePath) {
	// String mimeType =
	// MimeUtils.guessMimeTypeFromExtension(getTypeFromFilepath(filePath));
	// int dotPosition = mimeType.indexOf("/");
	// String type = mimeType.substring(0, dotPosition).toLowerCase();
	// if (type.equals("text") || type.equals("application")) {
	// return Category.DOC;
	// } else if (type.equals("audio")) {
	// return Category.MUSIC;
	// } else if (type.equals("image")) {
	// return Category.PICTURE;
	// } else if (type.equals("video")) {
	// return Category.VIDEO;
	// }
	// return Category.UNKNOWN;
	// }

	/**
	 * 获取Doc具体类型
	 * 
	 * @param filePath
	 * @return
	 */
	public static int getFileType(String filePath) {
		String type = getTypeFromFilepath(filePath);
		if (type.equals("txt") || type.equals("text")) {
			return FileType.TXT;
		} else if (type.equals("doc") || type.equals("docx")) {
			return FileType.DOC;
		} else if (type.equals("pdf")) {
			return FileType.PDF;
		} else if (type.equals("ppt")) {
			return FileType.PPT;
		} else if (type.equals("xls") || type.equals("xlsx")) {
			return FileType.XLS;
		} else if (type.equals("apk")) {
			return FileType.APK;
		}
		String mimeType = MimeUtils.guessMimeTypeFromExtension(type);
		if (mimeType != null) {
			int dotPosition = mimeType.indexOf("/");
			String mediaType = mimeType.substring(0, dotPosition).toLowerCase();
			if (mediaType.equals("audio")) {
				return FileType.MUSIC;
			} else if (mediaType.equals("image")) {
				return FileType.PICTURE;
			} else if (mediaType.equals("video")) {
				return FileType.VIDEO;
			}
		}
		return FileType.UNKNOWN;
	}

	/**
	 * 时间格式转换
	 * 
	 * @param ms
	 * @return MM : SS
	 
	 */
	public static String millisTimeToDotFormat(long ms) {
		long sec = 1000;
		long min = sec * 60;
		long hour = min * 60;
		long day = hour * 24;

		long days = ms / day;
		long hours = (ms - days * day) / hour;
		long mins = (ms - days * day - hours * hour) / min;
		long secs = (ms - days * day - hours * hour - mins * min) / sec;

		// String strDay = days < 10 ? "0"+days : ""+days;
		// String strHour = hours < 10 ? "0"+hours : ""+hours;
		String strMin = mins < 10 ? "0" + mins : "" + mins;
		String strSec = secs < 10 ? "0" + secs : "" + secs;

		return (strMin + ":" + strSec);
	}

	/**
	 * 设置壁纸
	 * 
	 
	 */
	public static void setWallpaper(Context context, String path) {
		DataEyeManager.getInstance().module(ModuleName.WALLPAPER_SETWALLPAPAER, true);
		WallpaperManager wallpaperManager = null;
		FileInputStream data = null;
		try {
			data = new FileInputStream(new File(path));
			wallpaperManager = WallpaperManager.getInstance(context);
			wallpaperManager.setStream(data);
			data.close();
			ToastUtil.show(context, context.getResources().getString(R.string.picture_set_wallpaper_succ),
					Toast.LENGTH_SHORT);
			DataEyeManager.getInstance().module(ModuleName.WALLPAPER_SETWALLPAPAER, false);
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.show(context, context.getResources().getString(R.string.picture_set_wallpaper_fail),
					Toast.LENGTH_SHORT);
		}
	}

	/**
	 * 保存Bitmap
	 * 
	 * @param file
	 * @param mBitmap
	 
	 */
	public static void saveBitmapFile(Context context, File file, Bitmap mBitmap) {
		if (StorageUtils.isSDCardPresent()) {
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			try {
				fOut.write(stream.toByteArray());
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// update
			NativeResourceDBHelper.getInstance(context).notifyFileSystemChanged(file.getAbsolutePath());
		}
	}

	public static String getCurrenTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * 获取时间 小时:分 HH:mm
	 * 
	 * @return
	 */
	public static String getTimeShort() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(new Date(System.currentTimeMillis()));
	}

	/**
	 *把时间由long类型转为String类型
	 */
	public static String converCurrentTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(time));
	}

	/**
	 * 获取图片Item的宽度
	 * 
	 * @param mActivity 
	 * @param column
	 * @return
	 */
	public static int getItemWidth(Activity mActivity, int column) {
		int itemWidth;
		int deleteWidth = mActivity.getResources().getInteger(R.integer.waterfall_delete_width);
		Display display = mActivity.getWindowManager().getDefaultDisplay();
		itemWidth = display.getWidth() / column - deleteWidth;// 根据屏幕大小计算每列大小
		return itemWidth;
	}

	/**
	 * 铃声下载
	 * 
	 * @param rtBean
	 * @param context
	 */
	public static void addDownload(RingtonesBean rtBean, Context context) {
		DownloadBean downloadBean = new DownloadBean(rtBean.getUrl(), rtBean.getLogo(), rtBean.getMusicName(),
				rtBean.getFileSize(), rtBean.getCurrentBytes(), MediaType.MUSIC, DownloadTask.TASK_DOWNLOADING,
				rtBean.getFileSize(), rtBean.getMusicId(), rtBean.getCategoryId());
		DownloadManager.getInstance().addDownload(context, downloadBean);
	}

	/**
	 * 用于打开各种文件
	 * 
	 * @param filePath文件路径
	 */
	public static Intent openFile(String filePath) {

		File file = new File(filePath);
		if ((file == null) || !file.exists() || file.isDirectory())
			return null;

		/* 取得扩展名 */
		String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length())
				.toLowerCase();
		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg")
				|| end.equals("wav")) {
			return getAudioFileIntent(filePath);
		} else if (end.equals("3gp") || end.equals("mp4")) {
			return getVideoFileIntent(filePath);
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")
				|| end.equals("bmp")) {
			return getImageFileIntent(filePath);
		} else if (end.equals("apk")) {
			return getApkFileIntent(filePath);
		} else if (end.equals("ppt")) {
			return getPptFileIntent(filePath);
		} else if (end.equals("xls")) {
			return getExcelFileIntent(filePath);
		} else if (end.equals("doc")) {
			return getWordFileIntent(filePath);
		} else if (end.equals("pdf")) {
			return getPdfFileIntent(filePath);
		} else if (end.equals("chm")) {
			return getChmFileIntent(filePath);
		} else if (end.equals("txt")) {
			return getTextFileIntent(filePath, false);
		} else if (end.equals(".html")) {
			return getHtmlFileIntent(filePath);
		} else {
			return getAllIntent(filePath);
		}
	}

	// Android获取一个用于打开APK文件的intent
	private static Intent getAllIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	// Android获取一个用于打开APK文件的intent
	private static Intent getApkFileIntent(String fullPath) {
		fullPath = fullPath.replaceAll(".tmp", "");
		if (fullPath.startsWith("/data/data/")) {
			try {
				Runtime.getRuntime().exec("chmod 644 " + fullPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(fullPath));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	// Android获取一个用于打开VIDEO文件的intent
	private static Intent getVideoFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// Android获取一个用于打开AUDIO文件的intent
	private static Intent getAudioFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// Android获取一个用于打开Html文件的intent
	private static Intent getHtmlFileIntent(String param) {

		Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content")
				.encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// Android获取一个用于打开图片文件的intent
	private static Intent getImageFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// Android获取一个用于打开PPT文件的intent
	private static Intent getPptFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// Android获取一个用于打开Excel文件的intent
	private static Intent getExcelFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// Android获取一个用于打开Word文件的intent
	private static Intent getWordFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// Android获取一个用于打开CHM文件的intent
	private static Intent getChmFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// Android获取一个用于打开文本文件的intent
	private static Intent getTextFileIntent(String param, boolean paramBoolean) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}

	// Android获取一个用于打开PDF文件的intent
	private static Intent getPdfFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

	/**
	 * 获取包资源信息
	 * 
	 * @param context
	 * @return
	 */
	public static PackageInfo getPackageMsg(Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 
	 * @Title: isTablet
	 * @Description: 判断是否属于平板
	 * @param @param context
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * 
	 * @Title: autoScreenAdapter
	 * @Description: 屏幕自动适配、平板、手机
	 * @param @param context 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void autoScreenAdapter(Activity activity) {
		Window window = activity.getWindow();
		WindowManager wm = window.getWindowManager();
		Display display = wm.getDefaultDisplay(); // 获取屏幕宽、高
		WindowManager.LayoutParams params = window.getAttributes();
		// 屏幕宽度
		float screenWidth = display.getWidth();
		// 屏幕高度
		// float screenHeight = display.getHeight();

		// 判断设备是平板、手机
		if (Utils.isTablet(activity)) {
			// Log.i("is Tablet!");
			params.width = (int) (screenWidth * 0.70); // 宽度设置为屏幕的0.70
		} else {
			// Log.i("is phone!");
			params.width = (int) (screenWidth * 0.85); // 宽度设置为屏幕的0.85
		}

		window.setAttributes(params);
	}

	public static void setBackgroundDrawable(View view, Drawable drawable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}

	/**
	 * @Title: getFileSize
	 * @Description: 获取文件大小
	 * @param @param file
	 * @param @return
	 * @param @throws Exception
	 * @return int
	 */
	public static int getFileSize(File file) {
		int size = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			size = fis.available();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static long getFileSize(String path) {
		long size = 0;
		try {
			File f = new File(path);
			size = f.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static void setBackgroundResource(View view, int resId, Context context) {
		if (view == null)
			return;
		try {
			Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId);
			BitmapDrawable bd = new BitmapDrawable(context.getResources(), bm);
			view.setBackgroundDrawable(bd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void recycleBackgroundResource(View view) {
		if (view == null)
			return;
		try {
			BitmapDrawable bd = (BitmapDrawable) view.getBackground();
			view.setBackgroundResource(0);// 别忘了把背景设为null，避免onDraw刷新背景时候出现used a
			// recycled bitmap错误
			bd.setCallback(null);
			bd.getBitmap().recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void recycleImageView(ImageView view) {
		if (view == null)
			return;
		try {
			Drawable bd = view.getDrawable();
			view.setImageBitmap(null);
			view.setBackgroundDrawable(null);
			view.setBackgroundResource(0);//别忘了把背景设为null，避免onDraw刷新背景时候出现used a recycled bitmap错误
			bd.setCallback(null);
			view = null;
			//			bd.getBitmap().recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: drawableToBitmap
	 * @Description: drawable → Bitmap
	 * @param @param drawable
	 * @param @return
	 * @return Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 
	 * @Title: getApplicationIcon
	 * @Description: 根据包名，获取应用icon，bitmap
	 * @param @param context
	 * @param @param packageName
	 * @param @return
	 * @return Bitmap
	 */
	public static Bitmap getApplicationIcon(Context context, String packageName) {
		Bitmap bitmap = null;
		Drawable drawable = null;
		PackageManager pm = context.getPackageManager();
		try {
			drawable = pm.getApplicationIcon(packageName);
			bitmap = drawableToBitmap(drawable);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static boolean isCheckFileExist(String sdkDebugFileName) {
		try {
			File checkDebugFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator
					+ sdkDebugFileName);
			if (checkDebugFile.exists()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public static void showNotification(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Service.NOTIFICATION_SERVICE);
		String content = context.getResources().getString(R.string.feedback_warn_content);
		String tips = context.getResources().getString(R.string.feedback_warn_tips);
		String title = context.getResources().getString(R.string.feedback_warn_title);
		NotificationCompat.Builder builder = new Builder(context);
		builder.setTicker(tips);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.tiker_bar_icon);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.mas_ic_launcher));
		builder.setAutoCancel(true);
		builder.setPriority(NotificationCompat.PRIORITY_HIGH);
		Intent intent = new Intent(context, SubmitFeedbackActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		builder.setContentIntent(resultPendingIntent);
		notificationManager.notify(1, builder.build());
	}

	/**
	* @Title: addFinishAct 
	* @Description: 添加需要finish的activity
	* @param @param activity    
	* @return void
	 */
	public static void addFinishAct(Activity activity) {
		finishList.add(activity);
	}

	/**
	* @Title: exitSystem 
	* @Description: 退出应用系统
	* @param     
	* @return void
	 */
	public static void exitSystem() {
		for (Activity act : finishList) {
			act.finish();
		}
		finishList.clear();
	}

	/**
	* @Title: shareMsg 
	* @Description: TODO 
	* @param @param context
	* @param @param appName
	* @param @param packageName
	* @param @param title    
	* @return void
	 */
	public static void shareMsg(Context context, String appName, String packageName) {
		String shareTips = ResourceUtil.getString(context, R.string.share_tips, appName);

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain"); // shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareTips);

		if (Utils.isAppExit(packageName, context)) {
			shareIntent.putExtra("appDetailActivityUri", packageName);
		} else {
			shareIntent.putExtra("appDetailActivityUri", "appDetailActivityUri");
		}

		shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(shareIntent, ResourceUtil.getString(context, R.string.app_name)));
	}
	
	public static String convertVersionName(String versionName){
		if(versionName != null)
			return versionName.replace("(", "").replace(")", "").replace(" ", "");
		return "_";
	}
	
	/**
	* @Title: collapseStatusBar 
	* @Description:auto collapse status bar
	* @param @param context    
	* @return void
	 */
	public static void collapseStatusBar(Context context) {
		int currentApiVersion = android.os.Build.VERSION.SDK_INT;
		try {
			Object service = context.getSystemService("statusbar");
			Class<?> statusbarManager = Class
					.forName("android.app.StatusBarManager");
			Method collapse = null;
			if (service != null) {
				if (currentApiVersion <= 16) {
					collapse = statusbarManager.getMethod("collapse");
				} else {
					collapse = statusbarManager.getMethod("collapsePanels");
				}
				collapse.setAccessible(true);
				collapse.invoke(service);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
