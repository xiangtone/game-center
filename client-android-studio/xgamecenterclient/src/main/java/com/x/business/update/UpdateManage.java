package com.x.business.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.x.R;
import com.x.business.account.AccountManager;
import com.x.business.country.CountryManager;
import com.x.business.settings.SettingModel;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.FileType;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.db.dao.UpdateApp;
import com.x.db.updateapp.UpdateAppDBHelper;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadNotificationManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.AppsUpgradeRequest;
import com.x.publics.http.model.AppsUpgradeResponse;
import com.x.publics.http.model.AppsUpgradeRequest.RequestInstallBean;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.MyAppsBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.PatchUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SettingsUtils;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;
import com.x.receiver.NotificationBtnClickReceiver;
import com.x.ui.activity.myApps.MyAppsActivity;

/**
 * UpdateManage
 * 
 
 * 
 */
public class UpdateManage {

	public Long UPDATE_REQUEST_INTERVAL = 8 * 60 * 60 * 1000L;/* 2 * 60 * 1000L; */
	private static final long REQUEST_INTERVAL = 5 * 24 * 60 * 60 * 1000L;//5天内只展示一次应用升级的推送
	private static UpdateManage updateManage;
	private NotificationManager notificationManager;
	public Context context;

	public UpdateManage(Context context) {
		this.context = context;
	}

	/**
	 * 构建实例
	 * 
	 
	 * @param context
	 * @return
	 */
	public static UpdateManage getInstance(Context context) {
		if (updateManage == null) {
			updateManage = new UpdateManage(context);
		}
		return updateManage;
	}

	public boolean addUpdateApp(InstallAppBean installAppBean) {

		return insertUpdateApp(installAppBean);
	}

	public int getUpdateAppSize() {
		return UpdateAppDBHelper.getInstance(context).findUpdateAppCount();
	}

	/**
	 * @Title: getAppsUpdate
	 * @Description: 获取更新
	 * @param @param listener
	 * @param @param errorListener
	 * @return void
	 */

	public void getAppsUpdate(final Listener<JSONObject> listener, final ErrorListener errorListener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				AppsUpgradeRequest appsUpgradeRequest = new AppsUpgradeRequest();
				List<InstallAppBean> installAppBeans = LocalAppEntityManager.getInstance().getAllApps();
				if (installAppBeans.isEmpty())
					return;
				List<RequestInstallBean> requestList = new ArrayList<RequestInstallBean>();
				for (InstallAppBean bean : installAppBeans) {
					RequestInstallBean requestInstallBean = new RequestInstallBean();
					requestInstallBean.setAppName(bean.getAppName());
					requestInstallBean.setAppPackageName(bean.getPackageName());
					requestInstallBean.setAppVersionName(bean.getVersionName());
					requestInstallBean.setAppVersionCode(Integer.valueOf(bean.getVersionCode()));
					requestInstallBean.setFileSize(bean.getFileSize());
					try {
						if (bean.getSysFlag() == 0) {
							requestInstallBean.setMd5(getAppSignatureMD5(bean.getPackageName(), context));
						} else {// 预装应用不增量升级
							requestInstallBean.setMd5("");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					requestList.add(requestInstallBean);
				}
				appsUpgradeRequest.setApps(requestList);
				appsUpgradeRequest.setAppNum(requestList.size());
				appsUpgradeRequest.setClientId(AccountManager.getInstance().getClientId(context));
				appsUpgradeRequest.setRaveId(CountryManager.getInstance().getCountryId(context));
				DataFetcher.getInstance().getUpgradeApps(appsUpgradeRequest, listener, errorListener);
			}
		}).start();
	}

	public void showUpdateNotification(List<InstallAppBean> applist) {
		if (canShowUpdateNotification()) {
			// showNotification(Constan.Notification.UPDATE_ID, applist);
			showBigViewNotification(Constan.Notification.UPDATE_ID, applist);
			setLastShowUpdateNotification(System.currentTimeMillis());
		}
	}

	private void showNotification(int id, List<InstallAppBean> applist) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Service.NOTIFICATION_SERVICE);
		int appNum = applist.size();
		String title = ResourceUtil.getString(context, R.string.update_notification_title, "" + appNum);
		String content = "";
		for (InstallAppBean bean : applist) {
			content += bean.getAppName() + ",";
		}
		content = content.subSequence(0, content.length() - 1).toString();
		NotificationCompat.Builder builder = new Builder(context);
		builder.setTicker(title);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.tiker_bar_icon);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.mas_ic_launcher));
		builder.setAutoCancel(true);
		builder.setPriority(NotificationCompat.PRIORITY_HIGH);

		Intent intent = new Intent(context, MyAppsActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		builder.setContentIntent(resultPendingIntent);
		notificationManager.notify(id, builder.build());
	}

	/**
	 * 
	 * @Title: showBigViewNotification
	 * @Description: TODO
	 * @param @param id
	 * @param @param applist
	 * @return void
	 */
	@SuppressLint("NewApi")
	private void showBigViewNotification(int id, List<InstallAppBean> applist) {
		notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
		notificationManager.cancel(Constan.Notification.UPDATE_ID);
		int appNum = applist.size();
		/* Notification title */
		String title = ResourceUtil.getString(context, R.string.update_notification_title, "" + appNum);
		/* Notification content */
		String content = "";
		for (InstallAppBean bean : applist) {
			content += bean.getAppName() + ",";
		}
		content = content.subSequence(0, content.length() - 1).toString();

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_update_view);
		remoteViews.setTextViewText(R.id.noti_title, title);
		remoteViews.setTextViewText(R.id.noti_content, content);
		remoteViews.setTextViewText(R.id.noti_time, Utils.getTimeShort());

		int num = (appNum >= 6) ? 6 : appNum;
		/* 循环获取应用icon */
		for (int i = 0; i < num; i++) {
			remoteViews.setViewVisibility(R.id.appicon_1 + i, View.VISIBLE);
			remoteViews.setImageViewBitmap(R.id.appicon_1 + i,
					Utils.getApplicationIcon(context, applist.get(i).getPackageName()));
		}

		/* setter update all button */
		remoteViews.setOnClickPendingIntent(R.id.btn_noti_update_all, getBtnPendingIntent());

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setTicker(title);
		mBuilder.setContent(remoteViews);
		mBuilder.setSmallIcon(R.drawable.tiker_bar_icon);
		mBuilder.setAutoCancel(true);
		mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
		Notification mNotification = mBuilder.build();

		/* 点击整个通知栏，触发的事件 */
		mNotification.contentIntent = getActionPendingIntent();

		if (android.os.Build.VERSION.SDK_INT >= 16) {
			mNotification.bigContentView = remoteViews; // 可显示多行，只支持4.1+
		} else {
			mNotification.contentView = remoteViews; // 显示单行，高度固定
		}
		notificationManager.notify(id, mNotification);
	}

	/**
	 * @Title: getActionPendingIntent
	 * @Description: 通知栏事件处理方法
	 * @param @return
	 * @return PendingIntent
	 */
	private PendingIntent getActionPendingIntent() {
		// Intent actionIntent = new Intent(context, MyAppsActivity.class);
		// actionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		// actionIntent,
		// PendingIntent.FLAG_UPDATE_CURRENT);

		// 点击的事件处理
		Intent buttonIntent = new Intent(NotificationBtnClickReceiver.ACTION_BUTTON);
		/* '整个通知栏' */
		buttonIntent.putExtra(NotificationBtnClickReceiver.INTENT_BUTTONID_TAG,
				NotificationBtnClickReceiver.BUTTON_ONTIFI_BAR_ID);
		// 这里加了广播，所及INTENT的必须用getBroadcast方法
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2, buttonIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		return pendingIntent;
	}

	/**
	 * 
	 * @Title: getBtnPendingIntent
	 * @Description: 通知栏按钮事件处理方法
	 * @param @return
	 * @return PendingIntent
	 */
	private PendingIntent getBtnPendingIntent() {
		// 点击的事件处理
		Intent buttonIntent = new Intent(NotificationBtnClickReceiver.ACTION_BUTTON);
		/* 'Update All' 按钮 */
		buttonIntent.putExtra(NotificationBtnClickReceiver.INTENT_BUTTONID_TAG,
				NotificationBtnClickReceiver.BUTTON_UPDATE_ALL_ID);
		// 这里加了广播，所及INTENT的必须用getBroadcast方法
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, buttonIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	public boolean canShowUpdateNotification() {
		long lastShow = getLastShowUpdateNotification();
		return lastShow == 0l || Math.abs(System.currentTimeMillis() - lastShow) >= REQUEST_INTERVAL;
	}

	private long getLastShowUpdateNotification() {
		return SharedPrefsUtil.getValue(context, "lastShowUpdateNotification", 0l);
	}

	private void setLastShowUpdateNotification(long time) {
		SharedPrefsUtil.putValue(context, "lastShowUpdateNotification", time);
	}

	public List<InstallAppBean> getSystemInstallApp(Context context) {
		List<InstallAppBean> pList = new ArrayList<InstallAppBean>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> pakList = pm.getInstalledPackages(0);
		LogUtil.getLogger().d(" (int) pakList.size = " + pakList.size());
		InstallAppBean bean = null;
		for (int i = 0; i < pakList.size(); i++) {
			PackageInfo pinfo = (PackageInfo) pakList.get(i);
			/** 判断是系统应用程序还不是用户应用程序 **/
			if ((pinfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// Drawable icon = pm.getApplicationIcon(pinfo.applicationInfo);
				String appName = (String) pm.getApplicationLabel(pinfo.applicationInfo).toString();
				String packageName = pinfo.packageName.toString().trim();
				if (packageName != null && packageName.equals(context.getPackageName()))
					continue;
				String localtion = pinfo.applicationInfo.sourceDir;
				long appSize = Integer.valueOf((int) new File(localtion).length());
				String versionName = pinfo.versionName;
				int versionCode = pinfo.versionCode;
				bean = new InstallAppBean(0, appName, packageName, versionName, versionCode, "" + appSize, localtion,
						1, 0, null);
				LogUtil.getLogger().d(" (InstallAppBean) bean = " + bean.toString());
				pList.add(bean);
			}
		}
		return pList;
	}

	public boolean canSendUpdateRequest(Context context) {
		boolean isNetworkOk = NetworkUtils.isNetworkAvailable(context);
		long lastReuestTime = getLastUpdateRequestSuccessTime();
		boolean lastRequestSuccess = getLastUpdateRequestState();
		if (!isNetworkOk) {
			return false;
		} else if (lastReuestTime == 0l) {
			return true;
		} else if (!lastRequestSuccess) {
			return true;
		} else if (System.currentTimeMillis() - lastReuestTime >= UPDATE_REQUEST_INTERVAL) {
			return true;
		} else {
			return false;
		}
	}

	public long getLastUpdateRequestSuccessTime() {
		return SharedPrefsUtil.getValue(context, "lastUpdateRequestSuccessTime", 0l);
	}

	public void setLastUpdateRequestSuccessTime(Context context) {
		SharedPrefsUtil.putValue(context, "lastUpdateRequestSuccessTime", System.currentTimeMillis());
	}

	public void setLastUpdateRequestSuccess() {
		SharedPrefsUtil.putValue(context, "islastUpdateRequestSuccess", true);
	}

	public void setLastUpdateRequestFail() {
		SharedPrefsUtil.putValue(context, "islastUpdateRequestSuccess", false);
	}

	public boolean getLastUpdateRequestState() {
		return SharedPrefsUtil.getValue(context, "islastUpdateRequestSuccess", true);
	}

	/**
	 * 插入数据
	 * 
	 * @param appInfoSubBean
	 * @return
	 */
	public boolean insertUpdateApp(InstallAppBean installAppBean) {
		if (installAppBean != null) {
			UpdateApp updateApp = new UpdateApp();
			updateApp.setUpdateAppName(installAppBean.getAppName());
			updateApp.setUpdateCategoryId(installAppBean.getCategoryId());
			updateApp.setUpdateCreateTime(System.currentTimeMillis());
			updateApp.setUpdateDownloadUrl(installAppBean.getUrl());
			updateApp.setUpdateIconUrl(installAppBean.getLogo());
			updateApp.setUpdatePackageName(installAppBean.getPackageName());
			updateApp.setUpdateResourceId(installAppBean.getApkId());
			updateApp.setUpdateSize(Long.valueOf(installAppBean.getFileSize()));
			updateApp.setUpdateStarts(installAppBean.getStars());
			updateApp.setUpdateStatus(installAppBean.getStatus());
			updateApp.setUpdateUpdateTime(System.currentTimeMillis());
			updateApp.setUpdateVersionCode(installAppBean.getVersionCode());
			updateApp.setUpdateVersionName(installAppBean.getVersionName());
			updateApp.setUpdateAppId(installAppBean.getAppId());
			updateApp.setUpdateManualDownloadNetwork("-1");
			updateApp.setUpdateAttribute(installAppBean.getUrlPatch());
			updateApp.setUpdateExtAttribute2((int) installAppBean.getPatchSize());
			updateApp.setUpdateExtAttribute1("" + installAppBean.getFileType());
			return UpdateAppDBHelper.getInstance(context).insertUpdateApp(updateApp);
		}
		return false;
	}

	/**
	 * 删除数据
	 * 
	 * @param resId
	 * @return
	 */
	public boolean deleteUpdateApp(int resId) {
		if (resId > 0) {
			return UpdateAppDBHelper.getInstance(context).deleteUpdateAppByResourceId(resId);
		}
		return false;
	}

	/**
	 * 删除数据
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean deleteUpdateAppByPackageName(String packageName) {
		if (!TextUtils.isEmpty(packageName)) {
			return UpdateAppDBHelper.getInstance(context).deleteUpdateAppByPackageName(packageName);
		}
		return false;
	}

	/**
	 * 删除数据
	 * 
	 * @param resId
	 * @return
	 */
	public boolean deleteAllUpdateApp() {
		return UpdateAppDBHelper.getInstance(context).deleteAllUpdateApp();
	}

	/**
	 * 修改状态
	 * 
	 * @param resId
	 * @param status
	 * @return
	 */
	public boolean updateUpdateAppStatus(int resId, int status) {
		return UpdateAppDBHelper.getInstance(context).updateStatusByResourceId(resId, status,
				System.currentTimeMillis());
	}

	/**
	 * 查询AllUpdateApp
	 * 
	 * @return
	 */
	public List<UpdateApp> findAllUpdateApp() {
		return UpdateAppDBHelper.getInstance(context).findAllUpdateApp();
	}

	/**
	 * 查询AllUpdateApp
	 * 
	 * @return
	 */
	public List<InstallAppBean> findAllUpdateInstallApp() {
		List<UpdateApp> updateApps = findAllUpdateApp();
		if (updateApps == null)
			return null;
		List<InstallAppBean> updateList = new ArrayList<InstallAppBean>();
		for (UpdateApp updateApp : updateApps) {
			InstallAppBean installAppBean = new InstallAppBean();
			installAppBean.setAppName(updateApp.getUpdateAppName());
			installAppBean.setCategoryId(updateApp.getUpdateCategoryId());
			installAppBean.setUrl(updateApp.getUpdateDownloadUrl());
			installAppBean.setLogo(updateApp.getUpdateIconUrl());
			installAppBean.setPackageName(updateApp.getUpdatePackageName());
			installAppBean.setApkId(updateApp.getUpdateResourceId());
			installAppBean.setFileSize(updateApp.getUpdateSize());
			installAppBean.setStars(updateApp.getUpdateStarts());
			installAppBean.setStatus(updateApp.getUpdateStatus());
			installAppBean.setVersionCode(updateApp.getUpdateVersionCode());
			installAppBean.setVersionName(updateApp.getUpdateVersionName());
			installAppBean.setAppId(updateApp.getUpdateAppId());
			installAppBean.setIsPatch(TextUtils.isEmpty(updateApp.getUpdateAttribute()) ? false : true);
			installAppBean.setUrlPatch(updateApp.getUpdateAttribute()); // 增量更新url
			installAppBean.setPatchSize(updateApp.getUpdateExtAttribute2()); // 增量包大小

			installAppBean.setFileType(updateApp.getUpdateExtAttribute1() != null ? Integer.valueOf(updateApp
					.getUpdateExtAttribute1()) : 1); // fileType
			updateList.add(installAppBean);
		}
		return updateList;
	}

	/**
	 * 查询新版本号
	 * 
	 * @return
	 */

	public String getNewVersion(String packageName) {
		UpdateApp updateApp = UpdateAppDBHelper.getInstance(context).findUpdateAppByPackageName(packageName);
		if (updateApp != null)
			return updateApp.getUpdateVersionName();
		return null;
	}

	/**
	 * 查询是否UpdateApp
	 * 
	 * @return
	 */
	public boolean getIsUpdateApp(int resId) {
		UpdateApp updateApp = UpdateAppDBHelper.getInstance(context).findUpdateAppByResourceId(resId);
		if (updateApp != null) {
			return true;
		}
		return false;
	}

	/**
	 * 修改手动下载的网络类型
	 * 
	 * @param resourceId
	 * @param networkType
	 * @return
	 */
	public boolean updateManualDownloadNetwork(int resourceId, String networkType) {
		return UpdateAppDBHelper.getInstance(context).updateManualDownloadNetworkByResourceId(resourceId, networkType,
				System.currentTimeMillis());
	}

	/**
	 * wifi自动下载开关
	 */
	public void autoDownloadUpdateAppControl(boolean isNetworkChange) {
		boolean result = SharedPrefsUtil.getValue(this.context, SettingsUtils.AUTO_DOWNLOAD_UPDATE, false);
		if (result && NetworkUtils.isNetworkAvailable(context)
				&& NetworkUtils.NETWORK_TYPE_WIFI.equals(NetworkUtils.getNetworkInfo(context))) {
			List<UpdateApp> updateAppList = UpdateAppDBHelper.getInstance(context).findAllUpdateApp();

			if (updateAppList != null && updateAppList.size() > 0) {
				for (int i = 0; i < updateAppList.size(); i++) {
					UpdateApp updateApp = updateAppList.get(i);
					DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
							"" + updateApp.getUpdateResourceId(), "" + updateApp.getUpdateVersionCode());

					if (!"-1".equals(updateAppList.get(i).getUpdateManualDownloadNetwork()) // -1原始状态，不等于-1即被手动暂停过，不进行下载
							|| (downloadBean != null && downloadBean.getStatus() == DownloadTask.TASK_FINISH)) {
						continue;
					}
					if (downloadBean == null) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.UPGRADE, 0, null, 0, null, null,
								false);
						addDownload(updateAppList.get(i), this.context);
					} else if (downloadBean != null && downloadBean.getStatus() == DownloadTask.TASK_PAUSE) {
						DownloadManager.getInstance().continueAutoDownload(context, downloadBean.getUrl());
					}
				}
			}
		} else {
			if (!isNetworkChange) {
				List<UpdateApp> updateAppList = UpdateAppDBHelper.getInstance(context).findAllUpdateApp();
				if (updateAppList != null && updateAppList.size() > 0) {
					ArrayList<String> urls = new ArrayList<String>();
					for (int i = 0; i < updateAppList.size(); i++) {
						UpdateApp updateApp = updateAppList.get(i);
						DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
								"" + updateApp.getUpdateResourceId(), "" + updateApp.getUpdateVersionCode());
						if ("-1".equals(updateApp.getUpdateManualDownloadNetwork()) && downloadBean != null) {
							urls.add(downloadBean.getUrl());
						}
					}
					if (!urls.isEmpty())
						pauseDownload(urls);

				}
			}

		}

	}

	/**
	 * 初始化UpgradeData
	 */
	private List<InstallAppBean> getAppsUpgradeResponse() {
		AppsUpgradeResponse appsUpgradeResponse;
		String upgradeJson = SharedPrefsUtil.getValue(this.context, "upgrade_data", "");
		if (!upgradeJson.equals("")) {
			JSONObject json = null;
			try {
				json = new JSONObject(upgradeJson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			appsUpgradeResponse = (AppsUpgradeResponse) JsonUtil.jsonToBean(json, AppsUpgradeResponse.class);
			if (appsUpgradeResponse != null) {
				return appsUpgradeResponse.applist;
			}
		}
		return null;
	}

	/**
	 * auto wifi 下载被触发
	 */
	public void onClickAutoWifiDownload() {
		sendAutoWifiDownloadBroadcast();
	}

	/**
	 * 发送广播
	 */
	public void sendAutoWifiDownloadBroadcast() {
		Intent intent = new Intent();
		intent.setAction(MyIntents.INTENT_UPDATE_AUTO_WIFI_ACTION);
		this.context.sendBroadcast(intent);
	}

	/**
	 * 触发下载
	 */
	public void triggerAutoWifiDownload() {
		boolean result = SharedPrefsUtil.getValue(this.context, SettingsUtils.AUTO_DOWNLOAD_UPDATE, false);
		if (result && NetworkUtils.isNetworkAvailable(context)
				&& NetworkUtils.NETWORK_TYPE_WIFI.equals(NetworkUtils.getNetworkInfo(context))) {
			sendAutoWifiDownloadBroadcast();
		}
	}

	/**
	 * 添加下载
	 * 
	 * @param updateApp
	 */
	public void addDownload(UpdateApp updateApp, Context context) {
		long totalBytes = updateApp.getUpdateSize();
		long currentBytes = 0;
		boolean isUpdatePatch = TextUtils.isEmpty(updateApp.getUpdateAttribute()) ? false : true;
		if (isUpdatePatch) {
			totalBytes = updateApp.getUpdateExtAttribute2();
		}
		String downloadUrl = TextUtils.isEmpty(updateApp.getUpdateAttribute()) ? updateApp.getUpdateDownloadUrl()
				: updateApp.getUpdateAttribute();

		String mediaType = MediaType.APP;
		String type = updateApp.getUpdateExtAttribute1();
		try {
			if (type != null) {
				if (FileType.APPS == Integer.valueOf(type)) {
					mediaType = MediaType.APP;
				} else if (FileType.GAME == Integer.valueOf(type)) {
					mediaType = MediaType.GAME;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DownloadBean downloadBeanHistory = DownloadEntityManager.getInstance().getDownloadBeanByResId(
				"" + updateApp.getUpdateResourceId(), "" + updateApp.getUpdateVersionCode());
		if (downloadBeanHistory != null) {
			currentBytes = downloadBeanHistory.getCurrentBytes();
		}

		DownloadBean downloadBean = new DownloadBean(downloadUrl, updateApp.getUpdateAppName(), totalBytes,
				currentBytes, updateApp.getUpdateIconUrl(), mediaType, updateApp.getUpdateResourceId(),
				updateApp.getUpdateVersionName(), updateApp.getUpdatePackageName(), DownloadTask.TASK_DOWNLOADING,
				Long.valueOf(updateApp.getUpdateSize()), updateApp.getUpdateVersionCode(), updateApp.getUpdateAppId(),
				updateApp.getUpdateCategoryId(), updateApp.getUpdateStarts(), isUpdatePatch,
				updateApp.getUpdateDownloadUrl());
		SettingModel settingModel = Utils.getSettingModel(context);
		boolean toInstall = settingModel.isAutoInstall() && settingModel.isSilentInstall();
		downloadBean.setAutoInstall(toInstall);
		DownloadManager.getInstance().addDownload(context, downloadBean);
	}

	/**
	 * 下载所有更新
	 * 
	 * @param context
	 * @param updateList
	 */
	public void downloadAllUpdate(Context context, List<InstallAppBean> updateList) {
		if (updateList == null || !DownloadManager.getInstance().canDownload(context))
			return;
		for (InstallAppBean appBean : updateList) {
			DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
					"" + appBean.getApkId(), "" + appBean.getVersionCode());
			if (downloadBean != null && downloadBean.getStatus() == DownloadTask.TASK_FINISH)
				continue;
			if (downloadBean == null) {
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.UPGRADE, 0, null, 0, null, null, false);
			}
			addDownload(appBean, context, true, true);

		}
	}

	/**
	 * 添加下载
	 * 
	 * @param updateApp
	 */
	public void addDownload(MyAppsBean appsBean, Context context, boolean isBatchDownload, boolean isAutoInstall) {

		boolean isUpdatePatch = false;
		long totalBytes = appsBean.getFileSize();
		String downloadUrl = appsBean.getOriginalUrl();
		if (!TextUtils.isEmpty(appsBean.getUrlPatch())) {
			isUpdatePatch = true;
			downloadUrl = appsBean.getUrlPatch();
			totalBytes = appsBean.getPatchSize();
		}
		String mediaType = MediaType.APP;
		if (appsBean.getFileType() == 1) {
			mediaType = MediaType.APP;
		} else if (appsBean.getFileType() == 2) {
			mediaType = MediaType.GAME;
		}
		DownloadBean downloadBean = new DownloadBean(downloadUrl, appsBean.getAppName(), totalBytes, 0,
				appsBean.getLogo(), mediaType, appsBean.getApkId(), appsBean.getVersionName(),
				appsBean.getPackageName(), DownloadTask.TASK_DOWNLOADING, Long.valueOf(appsBean.getFileSize()),
				appsBean.getVersionCode(), appsBean.getAppId(), appsBean.getCategoryId(), appsBean.getStars(),
				isUpdatePatch, appsBean.getOriginalUrl());
		downloadBean.setAutoInstall(isAutoInstall);
		if (isBatchDownload) {
			DownloadManager.getInstance().addDownloads(context, downloadBean, appsBean.getApkId(), 2);
		} else {
			DownloadManager.getInstance().addDownload(context, downloadBean, appsBean.getApkId(), 2);
		}
	}

	/**
	 * 添加下载
	 * 
	 * @param updateApp
	 */
	public void addDownload(InstallAppBean appInfoBean, Context context, boolean isBatchDownload, boolean isAutoInstall) {

		boolean isUpdatePatch = false;
		String downloadUrl = appInfoBean.getUrl();
		if (!TextUtils.isEmpty(appInfoBean.getUrlPatch())) {
			isUpdatePatch = true;
			downloadUrl = appInfoBean.getUrlPatch();
		}
		String mediaType = MediaType.APP;
		if (appInfoBean.getFileType() == 1) {
			mediaType = MediaType.APP;
		} else if (appInfoBean.getFileType() == 2) {
			mediaType = MediaType.GAME;
		}
		DownloadBean downloadBean = new DownloadBean(downloadUrl, appInfoBean.getAppName(), Long.valueOf(appInfoBean
				.getFileSize()), 0, appInfoBean.getLogo(), mediaType, appInfoBean.getApkId(),
				appInfoBean.getVersionName(), appInfoBean.getPackageName(), DownloadTask.TASK_DOWNLOADING,
				Long.valueOf(appInfoBean.getFileSize()), appInfoBean.getVersionCode(), appInfoBean.getAppId(),
				appInfoBean.getCategoryId(), appInfoBean.getStars(), isUpdatePatch, appInfoBean.getUrl());
		downloadBean.setAutoInstall(isAutoInstall);
		if (isBatchDownload) {
			DownloadManager.getInstance().addDownloads(context, downloadBean, appInfoBean.getApkId(), 2);
		} else {
			DownloadManager.getInstance().addDownload(context, downloadBean, appInfoBean.getApkId(), 2);
		}
	}

	/**
	 * 查询升级
	 * 
	 * @return
	 */
	public UpdateApp getUpdateAppByPackageName(String packageName) {
		UpdateApp updateApp = UpdateAppDBHelper.getInstance(context).findUpdateAppByPackageName(packageName);
		return updateApp;
	}

	/**
	 * 记录UpdateAppApp网络状态
	 * 
	 * @param UpdateAppResourceId
	 */
	public void markManualDownloadNetwork(int resourceId) {
		String netWorkInfo = NetworkUtils.getNetworkInfo(this.context);
		updateManualDownloadNetwork(resourceId, netWorkInfo);
	}

	/**
	 * 移除updateApp网络状态
	 * 
	 * @param favoriteResourceId
	 */
	public void cancelManualDownloadNetwork(int favoriteResourceId) {
		updateManualDownloadNetwork(favoriteResourceId, "-1");
	}

	/**
	 * 暂停下载
	 * 
	 * @param downloadUrl
	 */
	private void pauseDownload(String downloadUrl) {
		DownloadManager.getInstance().pauseDownload(this.context, downloadUrl);
	}

	/**
	 * 暂停下载
	 * 
	 * @param downloadUrl
	 */
	private void pauseDownload(ArrayList<String> downloadUrls) {
		DownloadManager.getInstance().pauseDownload(this.context, downloadUrls);
	}

	public void refreshUpdateByResId(int resId) {
		try {
			List<InstallAppBean> updateList = findAllUpdateInstallApp();
			if (updateList == null)
				return;
			for (InstallAppBean appInfoBean : updateList) {
				if (appInfoBean.getApkId() != 0 && appInfoBean.getApkId() == resId) {
					deleteUpdateApp(resId);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void refreshUpdateByPkgName(String packageName) {
		try {
			List<InstallAppBean> updateList = findAllUpdateInstallApp();
			if (updateList == null)
				return;
			for (InstallAppBean appInfoBean : updateList) {
				if (appInfoBean.getPackageName() != null && appInfoBean.getPackageName().equals(packageName)) {
					deleteUpdateAppByPackageName(packageName);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: patchUpdate
	 * @Description: 合并差异包
	 * @param @param downloadBean
	 * @return void
	 * @throws IOException
	 * @throws OriginalFileNotFountException
	 */

	public boolean patchUpdate(DownloadBean downloadBean) {

		AsyncTask<DownloadBean, Void, Boolean> mergeTask = new AsyncTask<DownloadBean, Void, Boolean>() {
			String oldApkPath = "";
			String tempApkPath = "";
			String newApkPath = "";
			String patchPath = "";
			DownloadBean downloadBean = null;

			protected void onPreExecute() {
				LogUtil.getLogger().d("patchUpdate", "开始合并差异包");

			};

			@Override
			protected Boolean doInBackground(DownloadBean... params) {
				downloadBean = params[0];
				Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
				nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.MERGE_PATCH);
				nofityIntent.putExtra(MyIntents.URL, downloadBean.getOriginalUrl());
				BroadcastManager.sendBroadcast(nofityIntent);

				InstallAppBean installAppBean = LocalAppEntityManager.getInstance().getLocalAppByPackageName(
						downloadBean.getPackageName());
				if (installAppBean != null) {
					tempApkPath = installAppBean.getSourceDir();
				} else {
					tempApkPath = "/data/app/" + downloadBean.getPackageName() + "-1.apk";
				}
				oldApkPath = StorageUtils.FILE_DOWNLOAD_APK_PATH + downloadBean.getPackageName() + "_tmp.apk";
				newApkPath = getNewPatchApkPath(downloadBean.getPackageName(), downloadBean.getVersionName());
				patchPath = downloadBean.getLocalPath();
				try {
					backupApplication(tempApkPath, oldApkPath);
				} catch (OriginalFileNotFountException e) {
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
				LogUtil.getLogger().d("patchUpdate", "系统包抽取结束,调用JNI合并包");
				int patch = PatchUtils.patch(oldApkPath, newApkPath, patchPath);
				LogUtil.getLogger().d("patchUpdate", "合并差异包成功");
				downloadBean.setLocalPath(newApkPath);
				DownloadEntityManager.getInstance().update(downloadBean);
				File oldfile = new File(oldApkPath);
				oldfile.delete();
				File downloadFile = new File(patchPath);
				downloadFile.delete();
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				InstallAppBean updateBean = UpdateManage.getInstance(context).getUpdateAppBean(
						downloadBean.getPackageName(), downloadBean.getVersionCode());
				if (updateBean != null && result) {
					long totalSize = updateBean.getFileSize();
					File mergeFile = new File(downloadBean.getLocalPath());
					Drawable appIcon = Utils.getAppIconByFile(context, downloadBean.getLocalPath());
					try {
						result = (appIcon != null && totalSize == mergeFile.length());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (result) {
						// to install
						if ((MediaType.APP.equals(downloadBean.getMediaType()) || MediaType.GAME.equals(downloadBean
								.getMediaType()))
								&& Utils.getSettingModel(context).isAutoInstall()
								&& downloadBean.isAutoInstall()) {
							PackageUtil.installApk(context, downloadBean.getLocalPath());
						}
						// notify list changed
						sendDownloadCompleteBroadcast(downloadBean);
						DownloadNotificationManager.getInstance(context).showCompleteNotification();
						DownloadNotificationManager.getInstance(context).showDownloadingNotification();
					} else { // 下载全量包
						ToastUtil.show(context, ResourceUtil.getString(context, R.string.upgrade_fail),
								Toast.LENGTH_LONG);
						DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());
						DownloadBean redownloadBean = new DownloadBean(downloadBean.getOriginalUrl(),
								downloadBean.getName(), downloadBean.getTotalBytes(), 0, downloadBean.getIconUrl(),
								downloadBean.getMediaType(), downloadBean.getResourceId(),
								downloadBean.getVersionName(), downloadBean.getPackageName(),
								DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(),
								downloadBean.getVersionCode(), downloadBean.getAppId(), downloadBean.getCategoryId(),
								downloadBean.getStars(), false, downloadBean.getOriginalUrl());
						DownloadManager.getInstance().addDownload(context, redownloadBean);
					}
				} else { // 下载全量包
					ToastUtil.show(context, ResourceUtil.getString(context, R.string.upgrade_fail), Toast.LENGTH_LONG);
					DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());
					DownloadBean redownloadBean = new DownloadBean(downloadBean.getOriginalUrl(),
							downloadBean.getName(), downloadBean.getTotalBytes(), 0, downloadBean.getIconUrl(),
							downloadBean.getMediaType(), downloadBean.getResourceId(), downloadBean.getVersionName(),
							downloadBean.getPackageName(), DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(),
							downloadBean.getVersionCode(), downloadBean.getAppId(), downloadBean.getCategoryId(),
							downloadBean.getStars(), false, downloadBean.getOriginalUrl());
					DownloadManager.getInstance().addDownload(context, redownloadBean);
				}
			}

		};

		Utils.executeAsyncTask(mergeTask, downloadBean);
		return true;
	}

	private void sendDownloadCompleteBroadcast(DownloadBean downloadBean) {
		Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.COMPLETE);
		nofityIntent.putExtra(MyIntents.URL, downloadBean.getOriginalUrl());
		nofityIntent.putExtra(MyIntents.DOWNLOADBEAN, downloadBean);
		BroadcastManager.sendBroadcast(nofityIntent);
	}

	/**
	 * 
	 * @Title: backupApplication
	 * @Description: 拷贝文件到指定目录
	 * @param @param old
	 * @param @param dest
	 * @param @return
	 * @param @throws OriginalFileNotFountException
	 * @param @throws IOException 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean backupApplication(String old, String dest) throws OriginalFileNotFountException, IOException {

		if (old == null || old.length() == 0 || dest == null || dest.length() == 0) {
			throw new OriginalFileNotFountException("illegal parameters");
		}
		// check file /data/app/appId-1.apk exists
		File apkFile = new File(old);
		if (apkFile.exists() == false) {
			throw new OriginalFileNotFountException(old + " doesn't exist!");
		}
		FileInputStream in = null;
		in = new FileInputStream(apkFile);
		// create dest folder if necessary
		int i = dest.lastIndexOf('/');
		if (i != -1) {
			File dirs = new File(dest.substring(0, i));
			dirs.mkdirs();
			dirs = null;
		}
		// do file copy operation
		byte[] c = new byte[1024];
		int slen;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(dest);
			while ((slen = in.read(c, 0, c.length)) != -1)
				out.write(c, 0, slen);
		} finally {
			if (out != null)
				out.close();
			if (in != null) {
				in.close();
			}
		}
		return true;

	}

	/**
	 * 得到任意apk公钥信息的md5字符串
	 * 
	 * @param apkPath
	 * @param context
	 */
	public String getApkSignatureMD5(String apkPath, Context context) throws Exception {
		Class clazz = Class.forName("android.content.pm.PackageParser");
		Method parsePackageMethod = clazz.getMethod("parsePackage", File.class, String.class, DisplayMetrics.class,
				int.class);

		Object packageParser = clazz.getConstructor(String.class).newInstance("");
		Object packag = parsePackageMethod.invoke(packageParser, new File(apkPath), null, context.getResources()
				.getDisplayMetrics(), 0x0004);

		Method collectCertificatesMethod = clazz.getMethod("collectCertificates",
				Class.forName("android.content.pm.PackageParser$Package"), int.class);
		collectCertificatesMethod.invoke(packageParser, packag, PackageManager.GET_SIGNATURES);
		Signature mSignatures[] = (Signature[]) packag.getClass().getField("mSignatures").get(packag);
		if (mSignatures != null && mSignatures.length > 0 && mSignatures[0] != null) {
			// 说明：没有提供md5的具体实现
			return signatureMD5(mSignatures);
		}

		return null;
	}

	public String getAppSignatureMD5(String packageName, Context context) throws Exception {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
		// Log.i("test", String.format("pkg=%s, sig=%s", info.packageName,
		// signatureMD5(info.signatures)));
		return signatureMD5(info.signatures);
	}

	private String signatureMD5(Signature[] signatures) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			if (signatures != null) {
				for (Signature s : signatures)
					digest.update(s.toByteArray());
			}
			return toHexString(digest.digest());
		} catch (Exception e) {
			return "";
		}
	}

	private final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public String getNewPatchApkPath(String packageName, String versionName) {
		return StorageUtils.FILE_DOWNLOAD_APK_PATH + packageName + "_" + versionName + "_zapp_merge.apk";
	}

	public boolean isNewApkFileExit(String packageName, String versionName) {
		String newApkPath = getNewPatchApkPath(packageName, versionName);
		File file = new File(newApkPath);
		return file.exists();
	}

	public String getNewApkPath(String packageName, String versionName) {
		return StorageUtils.FILE_DOWNLOAD_APK_PATH + packageName + "_" + versionName + ".apk";
	}

	public boolean isNewFileExit(String packageName, String versionName) {
		String newApkPath = getNewApkPath(packageName, versionName);
		File file = new File(newApkPath);
		return file.exists();
	}

	/**
	 * 查询AllUpdateApp
	 * 
	 * @return map
	 */
	public HashMap<String, InstallAppBean> findAllUpdateInstallAppMap() {
		List<UpdateApp> updateApps = findAllUpdateApp();
		HashMap<String, InstallAppBean> result = new HashMap<String, InstallAppBean>();
		if (updateApps != null) {
			for (UpdateApp updateApp : updateApps) {
				InstallAppBean installAppBean = new InstallAppBean();
				installAppBean.setAppName(updateApp.getUpdateAppName());
				installAppBean.setCategoryId(updateApp.getUpdateCategoryId());
				installAppBean.setUrl(updateApp.getUpdateDownloadUrl());
				installAppBean.setLogo(updateApp.getUpdateIconUrl());
				installAppBean.setPackageName(updateApp.getUpdatePackageName());
				installAppBean.setApkId(updateApp.getUpdateResourceId());
				installAppBean.setFileSize(updateApp.getUpdateSize());
				installAppBean.setStars(updateApp.getUpdateStarts());
				installAppBean.setStatus(updateApp.getUpdateStatus());
				installAppBean.setVersionCode(updateApp.getUpdateVersionCode());
				installAppBean.setVersionName(updateApp.getUpdateVersionName());
				installAppBean.setAppId(updateApp.getUpdateAppId());
				installAppBean.setIsPatch(TextUtils.isEmpty(updateApp.getUpdateAttribute()) ? false : true);
				installAppBean.setUrlPatch(updateApp.getUpdateAttribute()); // 增量更新url
				installAppBean.setPatchSize(updateApp.getUpdateExtAttribute2()); // 增量包大小
				result.put(updateApp.getUpdatePackageName(), installAppBean);
			}
		}
		return result;
	}

	/**
	 * 查询升级
	 * 
	 * @return
	 */
	public InstallAppBean getUpdateAppBeanByPackageName(String packageName) {
		InstallAppBean installAppBean = null;
		UpdateApp updateApp = UpdateAppDBHelper.getInstance(context).findUpdateAppByPackageName(packageName);
		if (updateApp != null) {
			installAppBean = new InstallAppBean();
			installAppBean.setAppName(updateApp.getUpdateAppName());
			installAppBean.setCategoryId(updateApp.getUpdateCategoryId());
			installAppBean.setUrl(updateApp.getUpdateDownloadUrl());
			installAppBean.setLogo(updateApp.getUpdateIconUrl());
			installAppBean.setPackageName(updateApp.getUpdatePackageName());
			installAppBean.setApkId(updateApp.getUpdateResourceId());
			installAppBean.setFileSize(updateApp.getUpdateSize());
			installAppBean.setStars(updateApp.getUpdateStarts());
			installAppBean.setStatus(updateApp.getUpdateStatus());
			installAppBean.setVersionCode(updateApp.getUpdateVersionCode());
			installAppBean.setVersionName(updateApp.getUpdateVersionName());
			installAppBean.setAppId(updateApp.getUpdateAppId());
			installAppBean.setIsPatch(TextUtils.isEmpty(updateApp.getUpdateAttribute()) ? false : true);
			installAppBean.setUrlPatch(updateApp.getUpdateAttribute()); // 增量更新url
			installAppBean.setPatchSize(updateApp.getUpdateExtAttribute2()); // 增量包大小
		}
		return installAppBean;
	}

	public InstallAppBean getUpdateAppBean(String packageName, int versionCode) {
		InstallAppBean installAppBean = null;
		UpdateApp updateApp = UpdateAppDBHelper.getInstance(context).findUpdateApp(packageName, versionCode);
		if (updateApp != null) {
			installAppBean = new InstallAppBean();
			installAppBean.setAppName(updateApp.getUpdateAppName());
			installAppBean.setCategoryId(updateApp.getUpdateCategoryId());
			installAppBean.setUrl(updateApp.getUpdateDownloadUrl());
			installAppBean.setLogo(updateApp.getUpdateIconUrl());
			installAppBean.setPackageName(updateApp.getUpdatePackageName());
			installAppBean.setApkId(updateApp.getUpdateResourceId());
			installAppBean.setFileSize(updateApp.getUpdateSize());
			installAppBean.setStars(updateApp.getUpdateStarts());
			installAppBean.setStatus(updateApp.getUpdateStatus());
			installAppBean.setVersionCode(updateApp.getUpdateVersionCode());
			installAppBean.setVersionName(updateApp.getUpdateVersionName());
			installAppBean.setAppId(updateApp.getUpdateAppId());
			installAppBean.setIsPatch(TextUtils.isEmpty(updateApp.getUpdateAttribute()) ? false : true);
			installAppBean.setUrlPatch(updateApp.getUpdateAttribute()); // 增量更新url
			installAppBean.setPatchSize(updateApp.getUpdateExtAttribute2()); // 增量包大小
		}
		return installAppBean;
	}
}
