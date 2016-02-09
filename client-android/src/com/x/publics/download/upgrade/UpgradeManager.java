package com.x.publics.download.upgrade;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.x.R;
import com.x.business.update.OriginalFileNotFountException;
import com.x.business.update.UpdateManage;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.UpgradeRequest;
import com.x.publics.http.model.UpgradeResponse;
import com.x.publics.http.model.UpgradeRequest.UpgradeRequestData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.PatchUtils;
import com.x.publics.utils.ProgressDialogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.receiver.NotificationBtnClickReceiver;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.activity.home.SlidingPaneMenuFragment;

/**
 * 
 * @ClassName: UpgradeManager
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-6-4 上午11:16:43
 * 
 */
public class UpgradeManager {

	public Context context;
	public ProgressDialog pd;
	public HttpRequest httpRequest;
	public DownloadFile downloadFile;
	public static UpgradeManager upgradeManager;
	public NotificationManager notificationManager = null;

	public static final int HANDLER_DOWNLOAD_ING = 1001; // 正在下载
	public static final int HANDLER_DOWNLOAD_SUCCESS = 1002; // 下载成功
	public static final int HANDLER_DOWNLOAD_FAILURE = 1003; // 下载失败

	public static final int NOTI_DETAIL_BUTTON_ID = 1004; // 详情按钮ID
	public static final int NOTI_INSTALL_BUTTON_ID = 1005;// 安装按钮ID

	public static final int START_IN_BACKGROUND = 1006; // 后台进行下载
	public static final int START_ON_NOTIFICATION = 1007; // 通知栏进行下载

	public static final int DETAIL_REQUEST_CODE = 1008; // 详情requestCode
	public static final int INSTALL_REQUEST_CODE = 1009; // 安装requestCode

	public static final int HANDLER_FULL_DOWNLOAD_INSTALL = 1010; // 全量下载安装
	public static final int HANDLER_PATCH_DOWNLOAD_INSTALL = 1011;// 增量下载，合并安装

	private int actionId;
	private Handler tipsHandler;
	private String lastServerUrl; // 上一次请求时，服务器的Url
	private String currentServerUrl; // 每次请求时，服务器的最新Url
	private boolean isClick = false;
	private static List<Activity> fxList = new LinkedList<Activity>();
	private NotificationCompat.Builder builder; // 兼容包，通知栏

	/**
	 * 构造器
	 * 
	 * @param context
	 */
	public UpgradeManager(Context context) {
		this.context = context;
	}

	/**
	 * 获取单例模式
	 * 
	 * @return
	 */
	public static UpgradeManager getInstence(Context context) {
		if (upgradeManager == null) {
			upgradeManager = new UpgradeManager(context);
		}
		return upgradeManager;
	}

	/**
	 * 监控文件下载
	 */
	DownloadProgress downloadProgress = new DownloadProgress() {

		/**
		 * 文件正在下载
		 */
		@Override
		public void downloading(String url, String savePath, boolean downloadPhone, long downloaded, long total) {
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt("downloaded", (int) downloaded);
			bundle.putInt("total", (int) total);
			msg.setData(bundle);
			msg.what = UpgradeManager.HANDLER_DOWNLOAD_ING;
			updateHandler.sendMessage(msg);
			setDownloading(true);
		}

		/**
		 * 文件下载成功
		 */
		@Override
		public void downloadSucess(String url, String savePath, boolean downloadPhone, long total, int type,
				String reason) {
			Message msg = new Message();
			msg.what = UpgradeManager.HANDLER_DOWNLOAD_SUCCESS;
			updateHandler.sendMessage(msg);
			setDownloading(false);
		}

		/**
		 * 文件开始下载
		 */
		@Override
		public void downloadStart(String url, String savePath, boolean downloadPhone, long downloaded, long total) {
			setDownloading(true);
		}

		/**
		 * 文件准备下载
		 */
		@Override
		public void downloadReady(String url) {
			updateHandler.sendEmptyMessage(HANDLER_DOWNLOAD_ING);
			setDownloading(true);
		}

		/**
		 * 文件下载失败
		 */
		@Override
		public void downloadFail(String url, String savePath, boolean downloadPhone, int type, String reason) {
			Message msg = new Message();
			msg.obj = reason;
			msg.what = UpgradeManager.HANDLER_DOWNLOAD_FAILURE;
			updateHandler.sendMessage(msg);
			setDownloading(false);
		}
	};

	/**
	 * 下载更新
	 * 
	 * @param hrm
	 */
	public void doUpdate(String dowloadUrl) {
		if (TextUtils.isEmpty(dowloadUrl)) {
			return;
		}

		// 初始化带下载进度条通知栏
		Bitmap large = BitmapFactory.decodeResource(context.getResources(), R.drawable.mas_ic_launcher);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(),
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder = UpgradeManager.newBaseNotify(context, R.drawable.tiker_bar_icon,
				ResourceUtil.getString(context, R.string.platform_ticker_text), large, pendingIntent);

		// 启动下载线程
		updateCurrentApp(context, dowloadUrl, null);
	}

	/**
	 * 取消notificationManager显示
	 */
	public void cancelDownloadNotification(int notificationId) {
		notificationManager.cancel(notificationId);
	}

	/**
	 * 下载线程
	 */
	private Thread updateThread;

	/**
	 * 更新APP
	 * 
	 * @param context
	 * @param url
	 * @param md5
	 * @param verCode
	 */
	public void updateCurrentApp(final Context context, final String url, final String md5) {
		if (context != null && url != null && url.length() > 0) {
			if (updateThread == null) {
				updateThread = new Thread() {
					public void run() {
						String fileDir = Environment.getExternalStorageDirectory() + File.separator
								+ DownloadFile.DEFAULT_SAVE_FILE_DIR;
						// 判读全量下载、增量下载，对应不同名称
						String fileName = "";
						if (isPatch() && getUsePatch()) {
							fileName = Tools.getStringHashValue(url) + "_" + getVersionName() + ".apk";
						} else {
							fileName = context.getPackageName() + "_" + getVersionName() + ".apk";
						}
						String savePath = fileDir + File.separator + fileName;
						downloadFile = new DownloadFile(context);

						boolean down = downloadFile.downloadFile(url, fileDir, fileName, downloadProgress);

						if (down) {
							Log.e("UpdateHelper", "首次下载成功！url=" + url + " md5=" + md5 + " verName=" + getVersionName());
							if (md5 != null && md5.length() > 0) {
								String filemd5 = Tools.getFileMD5(new File(fileDir + File.separator + fileName));
								if (!md5.equals(filemd5))// MD5校验失败，则重新下载一次。
								{
									Log.e("UpdateHelper", "MD5校验失败！重新下载");
									down = downloadFile.downloadFile(url, fileDir, fileName, null);
								}
							}
							if (down) {
								Log.e("UpdateHelper", "开始安装");
								// 储存本地文件路径
								setLocalPath(savePath);
								// 判断，调用系统安装
								if (isPatch() && getUsePatch()) {
									// 增量更新
									updateHandler.sendEmptyMessage(HANDLER_PATCH_DOWNLOAD_INSTALL);
								} else {
									// 全量更新
									if (actionId != START_IN_BACKGROUND) {
										updateHandler.sendEmptyMessage(HANDLER_FULL_DOWNLOAD_INSTALL);
									}
								}

							}
						} else {
							Log.e("UpdateHelper", "首次下载失败！");
						}
						updateThread = null;
					}
				};
				try {
					updateThread.start();
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (IllegalThreadStateException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @Title: showCustomView
	 * @Description: 自定义大视体图通知栏 4.1+ 支持
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressLint("NewApi")
	private void showCustomView() {
		RemoteViews remoteViews = null;
		Notification mNotification = null;

		// 自定义布局
		if (actionId == START_IN_BACKGROUND) {
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification_big_view1);
			remoteViews.setTextViewText(R.id.noti_title,
					ResourceUtil.getString(context, R.string.platform_download_complete_title1));
		} else {
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification_big_view2);
			remoteViews.setTextViewText(R.id.noti_title,
					ResourceUtil.getString(context, R.string.platform_download_complete_title2));
		}

		remoteViews.setImageViewResource(R.id.noti_icon, R.drawable.mas_ic_launcher);

		// 判断是否增量更新，选择对应的文件大小
		String size = "";
		if (isPatch() && getUsePatch()) {
			size = Utils.sizeFormat(getPatchSize());
		} else {
			size = Utils.sizeFormat(getFileSize());
		}
		remoteViews.setTextViewText(R.id.noti_size,
				ResourceUtil.getString(context, R.string.platform_download_complete_size) + size);

		// 4.1+ 版本显示拖拽提示
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			remoteViews.setTextViewText(R.id.noti_drag_tips, ResourceUtil.getString(context, R.string.noti_drag_tips));
		}
		remoteViews.setTextViewText(R.id.noti_desc, Html.fromHtml(getUpgradeInfo()));

		remoteViews.setOnClickPendingIntent(R.id.noti_install_btn,
				getActionPendingIntent(NotificationBtnClickReceiver.NOTI_INSTALL_BUTTON_ID, INSTALL_REQUEST_CODE));
		remoteViews.setOnClickPendingIntent(R.id.noti_detail_btn,
				getActionPendingIntent(NotificationBtnClickReceiver.NOTI_DETAIL_BUTTON_ID, DETAIL_REQUEST_CODE));

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mNotification = mBuilder.setContent(remoteViews).setSmallIcon(R.drawable.mas_ic_launcher)
				.setPriority(NotificationCompat.PRIORITY_MAX).build();

		mNotification.contentIntent = getActionPendingIntent(NotificationBtnClickReceiver.NOTI_INSTALL_BUTTON_ID,
				INSTALL_REQUEST_CODE);

		if (android.os.Build.VERSION.SDK_INT >= 16) {
			mNotification.bigContentView = remoteViews; // 可显示多行，只支持4.1+
		} else {
			mNotification.contentView = remoteViews; // 显示单行，高度固定
		}

		notificationManager.notify(HANDLER_DOWNLOAD_SUCCESS, mNotification);
	}

	/**
	 * @Title: getActionPendingIntent
	 * @Description: 根据ID，返回不同的pendingIntent
	 * @param @param actionId
	 * @param @param requestCode
	 * @param @return 设定文件
	 * @return PendingIntent 返回类型
	 * @throws
	 */
	public PendingIntent getActionPendingIntent(int actionId, int requestCode) {
		// Intent actionIntent = new Intent(context, FxActivity.class);
		// actionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// actionIntent.putExtra("ACTION_ID", actionId);
		// actionIntent.putExtra("ACTION_PENDING", true);
		// PendingIntent pendingIntent = PendingIntent.getActivity(context,
		// requestCode, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 点击的事件处理
		Intent buttonIntent = new Intent(NotificationBtnClickReceiver.ACTION_BUTTON);
		buttonIntent.putExtra(NotificationBtnClickReceiver.INTENT_BUTTONID_TAG, actionId);
		// 这里加了广播，所及INTENT的必须用getBroadcast方法
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, buttonIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		return pendingIntent;
	}

	/**
	 * 
	 * @Title: checkVersion
	 * @Description: 版本检查，自动、手动检查
	 * @param @param actId
	 * @param @param isClick 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void checkVersion(int actId, boolean isClick) {
		this.actionId = actId;
		this.isClick = isClick;
		getData();
	}

	/**
	 * 
	 * @Title: checkVersion
	 * @Description: 版本检查，自动、手动检查
	 * @param @param actId
	 * @param @param isClick 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void checkVersion(int actId, Handler handler, boolean isClick) {
		this.actionId = actId;
		this.isClick = isClick;
		this.tipsHandler = handler;
		getData();
	}

	/**
	 * 
	 * @Title: getData
	 * @Description: 发送请求，获取数据
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getData() {
		UpgradeRequest upgradeRequest = new UpgradeRequest();
		UpgradeRequestData data = new UpgradeRequestData();
		data.setAppId(Utils.getMetaDataIntValue(context, "appId"));
		data.setAppPackageName(Utils.getPackageMsg(context).packageName);
		data.setAppVersionCode(Utils.getPackageMsg(context).versionCode);
		data.setAppVersionName(Utils.getPackageMsg(context).versionName);
		data.setApkKey(Utils.getMetaDataStringValue(context, "apkKey"));
		try {
			String str = UpdateManage.getInstance(context).getAppSignatureMD5(context.getPackageName(), context);
			data.setMd5(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		upgradeRequest.setData(data);
		DataFetcher.getInstance().getPlatformUpgrade(upgradeRequest, myResponseListent, myErrorListener);
	}

	/**
	 * @Title: myResponseListent
	 * @Description: 新版本检测，数据响应
	 * @param @param
	 * @return void
	 * @throws
	 */
	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			if (isClick && !ProgressDialogUtil.progressDialog.isShowing()) {
				return;
			}
			ProgressDialogUtil.closeProgressDialog();
			LogUtil.getLogger().d("response==>" + response.toString());
			// 升级响应数据处理
			final UpgradeResponse upgradeResponse = (UpgradeResponse) JsonUtil.jsonToBean(response,
					UpgradeResponse.class);
			if (upgradeResponse != null && upgradeResponse.state.code == 200 && upgradeResponse.isUpgrade) {

				// 新版本提示
				if (tipsHandler != null) {
					tipsHandler.sendEmptyMessage(MainActivity.HANDLER_MENU_ID);
				}

				currentServerUrl = upgradeResponse.data.url;
				lastServerUrl = getUrl();

				// 假如两次Url不相同，存储本地路径为空
				if (!currentServerUrl.equals(lastServerUrl)) {
					setLocalPath(null);
				}

				// 存储更新信息
				setUrl(upgradeResponse.data.url); // 设置全量更新路径url
				setIsUpgrade(upgradeResponse.isUpgrade); // 保存平台是否更新
				setUpgradeInfo(upgradeResponse.data.updateInfo); // 保存平台更新信息a
				setVersionName(upgradeResponse.data.versionName);// 保存更新版本名称
				setPatchSize(upgradeResponse.data.patchSize);// 保存增量文件大小
				setFileSize(upgradeResponse.data.fileSize); // 保存全量文件大小
				setIsPatch(upgradeResponse.data.isPatch);// 保存是否增量更新
				setPatchUrl(upgradeResponse.data.urlPatch);// 保存增量更新路径url
				setUpgradeType(upgradeResponse.data.upgradeType); //保存升级类型：2-普通升级，3强制升级

				switch (actionId) {

				case START_IN_BACKGROUND:
					// 进行文件下载
					startDownload();
					break;

				case START_ON_NOTIFICATION:
					// 弹出对话框
					Intent intent = new Intent(context, FxActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					break;
				}

			} else {
				setUsePatch(true); // 还原使用增量更新
				setIsUpgrade(false); // 设置平台不更新
				setUpgradeType(2); //保存升级类型：2-普通升级，3强制升级
				if (isClick) {
					ToastUtil.show(context, ResourceUtil.getString(context, R.string.new_version_tips),
							Toast.LENGTH_LONG);
				}

				// 平台不更新，删除文件夹下的apk
				// deleteAllFile(StorageUtils.FILE_DOWNLOAD_PLATFORM_UPGRADE_APK_PATH);

			}
		}
	};

	/**
	 * @Title: myResponseListent
	 * @Description: 新版本检测，异常响应
	 * @param @param
	 * @return void
	 * @throws
	 */
	private ErrorListener myErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			ProgressDialogUtil.closeProgressDialog();
			setIsUpgrade(false); // 设置平台不更新
			if (isClick) {
				// 网络错误，提示网络异常
				ToastUtil
						.show(context, ResourceUtil.getString(context, R.string.network_canot_work), Toast.LENGTH_LONG);
			}
		}
	};

	/**
	 * @Title: startDownload
	 * @Description: 下载文件
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void startDownload() {
		notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
		cancelDownloadNotification(HANDLER_DOWNLOAD_SUCCESS);

		if (isNewApkFileExists()) {
			setLocalPath(getNewPatchApkPath());// 设置下载好的文件路径
			if (actionId == START_IN_BACKGROUND) {
				updateHandler.sendEmptyMessage(HANDLER_DOWNLOAD_SUCCESS);
			} else {
				updateHandler.sendEmptyMessage(HANDLER_DOWNLOAD_SUCCESS);
				updateHandler.sendEmptyMessage(HANDLER_FULL_DOWNLOAD_INSTALL);
			}
			return;
		}

		String downloadUrl = null;
		boolean flag = false;
		// 判断是否存在增量更新，选择对应的下载路径
		if (isPatch() && getUsePatch()) {
			downloadUrl = getPatchUrl();
			flag = true;
		} else {
			downloadUrl = getUrl();
			flag = false;
		}

		// 判断存储空间是否可用
		if (hasSpace(flag)) {
			doUpdate(downloadUrl);
		}
	}

	/**
	 * @Title: hasStorage
	 * @Description: 采用合并包，需要预留空间
	 * @param @return
	 * @return boolean
	 */
	private boolean hasSpace(boolean flag) {
		int tempFileSize = 0;
		if (flag) {
			// 差量包 + 原始包 + 合并包
			tempFileSize = Tools.getApkSize(context) + getPatchSize() + getFileSize();
		} else {
			// 全量包
			tempFileSize = getFileSize();
		}

		// SD卡剩余可用空间
		long storage = StorageUtils.getAvailableStorage();

		if (storage < tempFileSize) {
			if (actionId == START_ON_NOTIFICATION) {
				ToastUtil.show(context, ResourceUtil.getString(context, R.string.sdcard_no_memory),
						ToastUtil.LENGTH_SHORT);
			}
			return false;
		}

		return true;
	}

	/**
	 * @Title: startDownload
	 * @Description: 下载文件
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void startDownload(int actId) {
		// TODO Auto-generated method stub
		this.actionId = actId;
		startDownload();
	}

	/**
	 * 更新通知栏进度Handler
	 */
	private Handler updateHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			// 下载中。。。
			case HANDLER_DOWNLOAD_ING:
				Bundle bundle = msg.getData();
				int downloaded = bundle.getInt("downloaded");
				int total = bundle.getInt("total");
				if (actionId == START_ON_NOTIFICATION) {
					toUpdateProgress(downloaded, total);
				}
				break;

			// 下载完成
			case HANDLER_DOWNLOAD_SUCCESS:
				if (isNewApkFileExists()) {
					showCustomView(); // 显示自定义大视图
					cancelDownloadNotification(HANDLER_DOWNLOAD_ING); // 取消下载通知栏
				}
				break;

			// 下载失败
			case HANDLER_DOWNLOAD_FAILURE:
				cancelDownloadNotification(HANDLER_DOWNLOAD_ING); // 取消下载通知栏
				if (actionId == START_ON_NOTIFICATION)
					ToastUtil.show(context, R.string.download_fail, Toast.LENGTH_LONG);
				break;

			// 全量下载安装
			case HANDLER_FULL_DOWNLOAD_INSTALL:
				Tools.installAppCommon(context, getLocalPath());
				break;

			// 增量下载，合并安装
			case HANDLER_PATCH_DOWNLOAD_INSTALL:
				patchUpdate(getLocalPath());
				break;
			}
		}

	};

	// ================================================SharedPrefsUtil============================================//

	public boolean isDownloading() {
		return SharedPrefsUtil.getValue(context, "PLA_IS_DOWNLOAD_ING", false);
	}

	public void setDownloading(boolean isDownloading) {
		SharedPrefsUtil.putValue(context, "PLA_IS_DOWNLOAD_ING", isDownloading);
	}

	/**
	 * 
	 * @Title: isPatch
	 * @Description: 获取判断增量更新
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean isPatch() {
		return SharedPrefsUtil.getValue(context, "PLA_IS_PATCH", false);
	}

	/**
	 * 
	 * @Title: setIsPatch
	 * @Description: 设置是否增量更新
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setIsPatch(boolean isPatch) {
		SharedPrefsUtil.putValue(context, "PLA_IS_PATCH", isPatch);
	}

	/**
	 * 
	 * @Title: getUsePatch
	 * @Description: 是否使用增量更新
	 * @param @return
	 * @return boolean
	 */
	public boolean getUsePatch() {
		return SharedPrefsUtil.getValue(context, "PLA_USE_PATCH", true);
	}

	/**
	 * 
	 * @Title: setUsePatch
	 * @Description: 设置使用增量更新
	 * @param @param usePatch
	 * @return void
	 */
	public void setUsePatch(boolean usePatch) {
		SharedPrefsUtil.putValue(context, "PLA_USE_PATCH", usePatch);
	}

	/**
	 * 
	 * @Title: getPatchUrl
	 * @Description: 获取增量更新Url
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getPatchUrl() {
		return SharedPrefsUtil.getValue(context, "PLA_PATCH_URL", "");
	}

	/**
	 * 
	 * @Title: setPatchUrl
	 * @Description: 设置增量更新Url
	 * @param @param patchUrl 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setPatchUrl(String patchUrl) {
		SharedPrefsUtil.putValue(context, "PLA_PATCH_URL", patchUrl);
	}

	/**
	 * 
	* @Title: setUpgradeType 
	* @Description:设置升级类型，2-普通升级，3-强制升级
	* @param @param upgradeType    
	* @return void
	 */
	public void setUpgradeType(int upgradeType) {
		SharedPrefsUtil.putValue(context, "PLA_UPGRADE_TYPE", upgradeType);
	}

	/**
	 * 
	* @Title: getUpgradeType 
	* @Description: 获取升级类型，2-普通升级，3-强制升级
	* @param @param context
	* @param @return    
	* @return int
	 */
	public int getUpgradeType() {
		return SharedPrefsUtil.getValue(context, "PLA_UPGRADE_TYPE", 2);
	}

	/**
	 * 
	 * @Title: getUrl
	 * @Description: 获取全量更新Url
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getUrl() {
		return SharedPrefsUtil.getValue(context, "PLA_URL", "");
	}

	/**
	 * 
	 * @Title: setUrl
	 * @Description: 设置全量更新Url
	 * @param @param patchUrl 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setUrl(String url) {
		SharedPrefsUtil.putValue(context, "PLA_URL", url);
	}

	/**
	 * 
	 * @Title: getFileSize
	 * @Description: 获取全量下载文件大小
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public int getFileSize() {
		return SharedPrefsUtil.getValue(context, "PLA_FILE_SIZE", 0);
	}

	/**
	 * 
	 * @Title: setFileSize
	 * @Description: 设置全量文件大小
	 * @param @param size 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setFileSize(int size) {
		SharedPrefsUtil.putValue(context, "PLA_FILE_SIZE", size);
	}

	/**
	 * 
	 * @Title: getPatchSize
	 * @Description: 获取增量文件大小
	 * @param @return 设定文件
	 * @return long 返回类型
	 * @throws
	 */
	public int getPatchSize() {
		return SharedPrefsUtil.getValue(context, "PLA_PATCH_SIZE", 0);
	}

	/**
	 * 
	 * @Title: setPatchSize
	 * @Description: 设置增量文件大小
	 * @param @param size 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setPatchSize(int size) {
		SharedPrefsUtil.putValue(context, "PLA_PATCH_SIZE", size);
	}

	/**
	 * 
	 * @Title: getUpgradeInfo
	 * @Description: 获取平台更新信息
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getUpgradeInfo() {
		return SharedPrefsUtil.getValue(context, "PLA_UPDATE_INFO", "");
	}

	/**
	 * 
	 * @Title: setUpgradeInfo
	 * @Description: 设置平台更新信息
	 * @param @param msg 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setUpgradeInfo(String msg) {
		SharedPrefsUtil.putValue(context, "PLA_UPDATE_INFO", msg);
	}

	/**
	 * 
	 * @Title: getVersionName
	 * @Description: 获取更新版本名称
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getVersionName() {
		return SharedPrefsUtil.getValue(context, "PLA_VERSION_NAME", "");
	}

	/**
	 * 
	 * @Title: setVersionName
	 * @Description: 设置更新版本名称
	 * @param @param verName 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setVersionName(String verName) {
		SharedPrefsUtil.putValue(context, "PLA_VERSION_NAME", verName);
	}

	/**
	 * 
	 * @Title: isUpgrade
	 * @Description: 获取判断是否存在更新
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean isUpgrade() {
		return SharedPrefsUtil.getValue(context, "PLA_IS_UPGRADE", false);
	}

	/**
	 * 
	 * @Title: setIsUpgrade
	 * @Description: 设置平台是否更新
	 * @param @param isUpgrade 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setIsUpgrade(boolean isUpgrade) {
		SharedPrefsUtil.putValue(context, "PLA_IS_UPGRADE", isUpgrade);
	}

	/**
	 * 
	 * @Title: getLocalPath
	 * @Description: 获取文件存储路径
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getLocalPath() {
		return SharedPrefsUtil.getValue(context, "PLA_LOCAL_PATH", "");
	}

	/**
	 * 
	 * @Title: saveLocalPath
	 * @Description: 保存文件存储路径
	 * @param @param path 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setLocalPath(String path) {
		SharedPrefsUtil.putValue(context, "PLA_LOCAL_PATH", path);
	}

	/**
	 * 
	 * @Title: getNewPatchApkPath
	 * @Description: 获取新的apk路径
	 * @param @param packageName
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getNewPatchApkPath() {
		return StorageUtils.FILE_DOWNLOAD_PLATFORM_UPGRADE_APK_PATH + context.getPackageName() + "_" + getVersionName()
				+ ".apk";
	}

	/**
	 * 
	 * @Title: isNewApkFileExists
	 * @Description: 判断新apk是否存在
	 * @param @param packageName
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean isNewApkFileExists() {
		String newApkPath = getNewPatchApkPath();
		File file = new File(newApkPath);
		return file.exists();
	}

	/**
	 * 
	 * @Title: deleteAllFile
	 * @Description: 删除文件夹下的所有文件
	 * @param @param path 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void deleteAllFile(String path) {
		File mDir = new File(path);
		if (mDir.exists() && mDir.isDirectory()) {
			File[] file = mDir.listFiles();
			for (int i = 0; i < file.length; i++) {
				file[i].delete();
			}
		}
	}

	/**
	 * @Title: FxActivity
	 * @Description: TODO
	 * @param @param activity
	 * @return void
	 */
	public static void FxActivity(Activity activity) {
		fxList.add(activity);
		if (fxList.size() > 1) {
			fxList.get(0).finish();
			fxList.remove(0);
			System.gc();
		}
	}

	// ================================================PatchApkPath======================================================//

	/**
	 * @Title: patchUpdate
	 * @Description: 合并差异包
	 * @param @param downloadBean
	 * @return void
	 * @throws IOException
	 * @throws OriginalFileNotFountException
	 */

	public boolean patchUpdate(String path) {

		AsyncTask<String, Void, Boolean> mergeTask = new AsyncTask<String, Void, Boolean>() {
			String oldApkPath = "";
			String tempApkPath = "";
			String newApkPath = "";
			String patchApkPath = "";
			String path = "";

			protected void onPreExecute() {
				LogUtil.getLogger().d("patchUpdate", "开始合并差异包");
			};

			@Override
			protected Boolean doInBackground(String... params) {
				path = params[0];
				oldApkPath = ZeroDataResourceHelper.getInstance(context).getZappSourceDir(); // 已安装包（旧包）路径
				tempApkPath = StorageUtils.FILE_DOWNLOAD_PLATFORM_UPGRADE_APK_PATH + context.getPackageName()
						+ "_tmp.apk"; // 临时包路径（复制旧包，指定存放的路径）
				patchApkPath = path; // 已下载增量包 存放路径
				newApkPath = getNewPatchApkPath();// 合并包存放路径（自定义）
				try {
					UpdateManage.getInstance(context).backupApplication(oldApkPath, tempApkPath);
					LogUtil.getLogger().d("patchUpdate", "系统包抽取结束,调用JNI合并包");
					PatchUtils.patch(oldApkPath, newApkPath, patchApkPath);
					LogUtil.getLogger().d("patchUpdate", "合并差异包成功");
					// 设置合并好的新路径
					setLocalPath(newApkPath);
					// 删除临时复制包
					File tempfile = new File(tempApkPath);
					tempfile.delete();
					// 删除增量包
					File patchfile = new File(patchApkPath);
					patchfile.delete();
				} catch (OriginalFileNotFountException e) {
					e.printStackTrace();
					return false;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				File file = new File(getLocalPath());
				if (result) {
					Drawable appIcon = Utils.getAppIconByFile(context, getLocalPath());

					try {
						// 根据appIcon 和 size判断是否合并成功
						result = (appIcon != null && getFileSize() == file.length());
					} catch (Exception e) {
						e.printStackTrace();
						result = false;
					}

					if (result) {
						// 合并成功，通知显示 或 调用系统安装
						updateHandler.sendEmptyMessage(HANDLER_DOWNLOAD_SUCCESS);
						if (actionId == START_ON_NOTIFICATION) {
							updateHandler.sendEmptyMessage(HANDLER_FULL_DOWNLOAD_INSTALL);
						}
					} else {
						// 合并失败，重新下载全量包
						file.delete(); // 删除合并失败的apk文件
						setUsePatch(false); // 取消使用增量更新
						startDownload();
					}

				} else {
					// 全量下载
					file.delete(); // 删除合并失败的apk文件
					setUsePatch(false); // 取消使用增量更新
					startDownload();
				}
			}
		};

		Utils.executeAsyncTask(mergeTask, path);
		return true;
	}

	// ===============================================自定义下载进度条==================================================

	public static NotificationCompat.Builder newBaseNotify(Context ctx, int smallIcon,
			String ticker, Bitmap large, PendingIntent pi) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				ctx).setContentTitle(ResourceUtil.getString(ctx, R.string.platform_downloading))
				.setWhen(System.currentTimeMillis()).setContentText("0K/0MB").setContentInfo("0%")
				.setSmallIcon(smallIcon).setContentIntent(pi).setLargeIcon(large).setTicker(ticker);
		return mBuilder;
	}

	/**
	 * 
	 * @Title: toUpdateProgress
	 * @Description: 更新下载进度条
	 * @param @param downloaded
	 * @param @param total 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void toUpdateProgress(int downloaded, int total) {
		// TODO Auto-generated method stub

		float percent; // 下载百分比
		String content; // 当前下载大小/总大小

		// 分母不能为零
		if (total == 0) {
			percent = 0f;
			total = 100;
			if (isPatch() && getUsePatch()) {
				content = "0K/" + Utils.sizeFormat(getPatchSize());
			} else {
				content = "0K/" + Utils.sizeFormat(getFileSize());
			}
		} else {
			percent = ((float) (downloaded) / (float) total);
			content = Utils.sizeFormat(downloaded) + "/" + Utils.sizeFormat(total);
		}

		builder.setProgress(total, downloaded, false).setContentText(content)
				.setContentInfo(String.format("%.0f", (percent * 100)) + "%");
		notificationManager.notify(HANDLER_DOWNLOAD_ING, builder.build());
	};

}
