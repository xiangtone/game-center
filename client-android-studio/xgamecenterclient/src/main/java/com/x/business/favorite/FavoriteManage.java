package com.x.business.favorite;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;

import com.x.business.settings.SettingModel;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.FileType;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.db.dao.FavoriteApp;
import com.x.db.dao.UpdateApp;
import com.x.db.favorite.FavoriteDBHelper;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.model.AppsUpgradeResponse;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.FavoriteAppBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.CloneClass;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.SettingsUtils;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * FavoriteManage
 
 *
 */
public class FavoriteManage {

	private static FavoriteManage favoriteManage;
	private AppsUpgradeResponse appsUpgradeResponse;
	public Context context;
	public int[] needDownloadStatus = { AppInfoBean.Status.NORMAL, AppInfoBean.Status.PAUSED,
			AppInfoBean.Status.CANUPGRADE, AppInfoBean.Status.DOWNLOADING, AppInfoBean.Status.CONNECTING,
			AppInfoBean.Status.WAITING };
	public int[] needStopDownloadStatus = { AppInfoBean.Status.NORMAL, AppInfoBean.Status.PAUSED,
			AppInfoBean.Status.CANUPGRADE, AppInfoBean.Status.DOWNLOADING, AppInfoBean.Status.CONNECTING,
			AppInfoBean.Status.WAITING };

	public FavoriteManage(Context context) {
		this.context = context;
	}

	/**
	 * 构建实例
	 
	 * @param context
	 * @return
	 */
	public static FavoriteManage getInstance(Context context) {
		if (favoriteManage == null) {
			favoriteManage = new FavoriteManage(context);
		}
		return favoriteManage;
	}

	/**
	 * 同步favoriteApp数据
	 */
	public void initFavoriteAppData() {
		List<FavoriteApp> favoriteAppList = findAllFavoriteApp();
		//		List<InstallAppBean> listAppInfoBean = getAppsUpgradeResponse();
		HashMap<String, InstallAppBean> updateAppMap = getAppsUpgradeMap();
		if (favoriteAppList != null && favoriteAppList.size() > 0) {
			for (int i = 0; i < favoriteAppList.size(); i++) {
				FavoriteApp favoriteApp = favoriteAppList.get(i);
				int favoriteResourceId = favoriteApp.getFavoriteResourceId();
				String appPackageName = favoriteApp.getFavoritePackageName();
				int status = 0;
				InstallAppBean installAppBean = null;

				DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
						String.valueOf(favoriteResourceId), "" + favoriteApp.getFavoriteVersionCode());//获取下载数据
				HashMap<String, InstallAppBean> localAppMap = LocalAppEntityManager.getInstance().getAllAppsMap();//获取本地已安装应用

				if (localAppMap != null) {
					installAppBean = localAppMap.get(appPackageName);
				}

				// 如果本地安装的版本低于于数据版本则提示可升级，否则提示可启动
				if (installAppBean != null && status != AppInfoBean.Status.CANUPGRADE) {
					status = AppInfoBean.Status.CANLAUNCH;
				}
				//是否可升级
				InstallAppBean updateAppBean = updateAppMap.get(appPackageName);
				if (updateAppBean != null) {
					status = AppInfoBean.Status.CANUPGRADE;

					if (downloadBean != null && downloadBean.getVersionCode() == updateAppBean.getVersionCode()) {
						int downloadStatus = downloadBean.getStatus();
						if (downloadStatus == DownloadTask.TASK_DOWNLOADING) {
							status = AppInfoBean.Status.DOWNLOADING;
						} else if (downloadStatus == DownloadTask.TASK_PAUSE) {
							status = AppInfoBean.Status.PAUSED;
						} else if (downloadStatus == DownloadTask.TASK_FINISH) {
							status = AppInfoBean.Status.CANINSTALL;
						} else if (downloadStatus == DownloadTask.TASK_LAUNCH) {
							status = AppInfoBean.Status.CANLAUNCH;
						} else if (downloadStatus == DownloadTask.TASK_WAITING) {
							status = AppInfoBean.Status.WAITING;
						} else if (downloadStatus == DownloadTask.TASK_CONNECTING) {
							status = AppInfoBean.Status.CONNECTING;
						}
					}
				} else if (downloadBean != null) {//如果该应用有下载历史则取下载历史的状态
					int downloadStatus = downloadBean.getStatus();
					if (downloadStatus == DownloadTask.TASK_DOWNLOADING) {
						status = AppInfoBean.Status.DOWNLOADING;
					} else if (downloadStatus == DownloadTask.TASK_PAUSE) {
						status = AppInfoBean.Status.PAUSED;
					} else if (downloadStatus == DownloadTask.TASK_FINISH) {
						status = AppInfoBean.Status.CANINSTALL;
					} else if (downloadStatus == DownloadTask.TASK_LAUNCH) {
						status = AppInfoBean.Status.CANLAUNCH;
					} else if (downloadStatus == DownloadTask.TASK_WAITING) {
						status = AppInfoBean.Status.WAITING;
					} else if (downloadStatus == DownloadTask.TASK_CONNECTING) {
						status = AppInfoBean.Status.CONNECTING;
					}
				}

				updateFavoriteAppStatus(favoriteResourceId, status);
			}
		}
	}

	/**
	 * 添加收藏应用
	 * @param appInfoBean
	 */
	public boolean addFavoriteApp(AppInfoBean appInfoBean) {
		int favoriteAppSize = getFavoriteAppSize();
		boolean result = false;
		if (favoriteAppSize < 100) {
			result = insertFavoriteApp(appInfoBean);
			if (result) {
				triggerAutoWifiDownload();
				sendAddFavoriteBroadcast(appInfoBean.getUrl(), appInfoBean.getPackageName());
			}

		}
		return result;
	}

	/**
	 * 获取收藏应用的数量
	 * @return
	 */
	public int getFavoriteAppSize() {
		return FavoriteDBHelper.getInstance(context).findFavoriteAppCount();
	}

	/**
	 * 取消收藏
	 * @param resId
	 */
	public boolean cancelFavoriteApp(int resId) {
		FavoriteAppBean favoriteAppBean = getFavoriteAppBeanByResId(resId);
		if (favoriteAppBean == null)
			return false;
		sendDeleteFavoriteBroadcast(favoriteAppBean.getFavoriteDownloadUrl(), favoriteAppBean.getFavoritePackageName());
		boolean result = deleteFavoriteApp(resId);
		return result;
	}

	private void sendDeleteFavoriteBroadcast(String url, String packageName) {
		Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.DELETE_FAVORITE);
		nofityIntent.putExtra(MyIntents.PACKAGENAME, packageName);
		nofityIntent.putExtra(MyIntents.URL, url);
		BroadcastManager.sendBroadcast(nofityIntent);
	}

	private void sendAddFavoriteBroadcast(String url, String packageName) {
		Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.ADD_FAVORITE);
		nofityIntent.putExtra(MyIntents.PACKAGENAME, packageName);
		nofityIntent.putExtra(MyIntents.URL, url);
		BroadcastManager.sendBroadcast(nofityIntent);
	}

	/**
	 * 插入数据
	 * @param appInfoSubBean
	 * @return
	 */
	public boolean insertFavoriteApp(AppInfoBean appInfoBean) {
		if (appInfoBean != null) {
			if (FavoriteDBHelper.getInstance(context).findFavoriteAppByResourceId(appInfoBean.getApkId()) != null) {
				return true;
			}
			FavoriteApp favoriteApp = new FavoriteApp();
			favoriteApp.setFavoriteAppName(appInfoBean.getAppName());
			favoriteApp.setFavoriteCategoryId(appInfoBean.getCategoryId());
			favoriteApp.setFavoriteCreateTime(System.currentTimeMillis());
			favoriteApp.setFavoriteDownloadUrl(appInfoBean.getUrl());
			favoriteApp.setFavoriteIconUrl(appInfoBean.getLogo());
			favoriteApp.setFavoritePackageName(appInfoBean.getPackageName());
			favoriteApp.setFavoriteResourceId(appInfoBean.getApkId());
			favoriteApp.setFavoriteSize(appInfoBean.getFileSize());
			favoriteApp.setFavoriteStarts(appInfoBean.getStars());
			favoriteApp.setFavoriteStatus(appInfoBean.getStatus());
			favoriteApp.setFavoriteUpdateTime(System.currentTimeMillis());
			favoriteApp.setFavoriteVersionCode(appInfoBean.getVersionCode());
			favoriteApp.setFavoriteVersionName(appInfoBean.getVersionName());
			favoriteApp.setFavoriteAppId(appInfoBean.getAppId());
			favoriteApp.setFavoriteManualDownloadNetwork("-1");
			favoriteApp.setFavoriteAttribute("" + appInfoBean.getFileType());
			return FavoriteDBHelper.getInstance(context).insertFavoriteApp(favoriteApp);
		}
		return false;
	}

	/**
	 * 删除数据
	 * @param resId
	 * @return
	 */
	public boolean deleteFavoriteApp(int resId) {
		if (resId > 0) {
			return FavoriteDBHelper.getInstance(context).deleteFavoriteAppByResourceId(resId);
		}
		return false;
	}

	/**
	 * 修改状态
	 * @param resId
	 * @param status
	 * @return
	 */
	public boolean updateFavoriteAppStatus(int resId, int status) {
		return FavoriteDBHelper.getInstance(context)
				.updateStatusByResourceId(resId, status, System.currentTimeMillis());
	}

	/**
	 * 修改信息
	 * @param updateAppBean
	 * @return
	 */
	public boolean updateFavoriteAppInfo(InstallAppBean updateAppBean) {
		if (updateAppBean == null)
			return false;
		return FavoriteDBHelper.getInstance(context).updateInfoByPackageName(updateAppBean.getPackageName(),
				updateAppBean.getUrl(), updateAppBean.getVersionName(), updateAppBean.getVersionCode(),
				System.currentTimeMillis());
	}

	/**
	 * 查询AllFavoriteApp
	 * @return
	 */
	public List<FavoriteApp> findAllFavoriteApp() {
		return FavoriteDBHelper.getInstance(context).findAllFavoriteApp();
	}

	/**
	 * 查询是否FavoriteApp
	 * @return
	 */
	public boolean getIsFavoriteApp(int resId) {
		FavoriteApp favoriteApp = FavoriteDBHelper.getInstance(context).findFavoriteAppByResourceId(resId);
		if (favoriteApp != null) {
			return true;
		}
		return false;
	}

	public HashMap<String, FavoriteAppBean> getAllFavoriteAppBeanMap() {
		HashMap<String, FavoriteAppBean> favoriteMap = new HashMap<String, FavoriteAppBean>();
		List<FavoriteAppBean> favoriteAppBeans = getAllFavoriteAppBeanList();
		if (favoriteAppBeans != null) {
			for (FavoriteAppBean favoriteAppBean : favoriteAppBeans) {
				favoriteMap.put(favoriteAppBean.getFavoritePackageName(), favoriteAppBean);
			}
		}

		return favoriteMap;
	}

	public FavoriteAppBean getFavoriteAppBeanByPackageName(String packageName) {
		FavoriteAppBean favoriteAppBean = new FavoriteAppBean();
		try {
			FavoriteApp favoriteApp = FavoriteDBHelper.getInstance(context).findFavoriteAppByPackageName(packageName);
			if (favoriteApp != null)
				return (FavoriteAppBean) CloneClass.clone(favoriteApp, favoriteAppBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public FavoriteAppBean getFavoriteAppBeanByResId(int resId) {
		FavoriteAppBean favoriteAppBean = new FavoriteAppBean();
		try {
			FavoriteApp favoriteApp = FavoriteDBHelper.getInstance(context).findFavoriteAppByResourceId(resId);
			if (favoriteApp != null)
				return (FavoriteAppBean) CloneClass.clone(favoriteApp, favoriteAppBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<FavoriteAppBean> getAllFavoriteAppBeanList() {
		List<FavoriteAppBean> favoriteAppBeanList = null;
		List<FavoriteApp> favoriteAppList = findAllFavoriteApp();

		if (favoriteAppList != null && favoriteAppList.size() > 0) {
			favoriteAppBeanList = new ArrayList<FavoriteAppBean>();
			for (FavoriteApp favoriteApp : favoriteAppList) {
				FavoriteAppBean favoriteAppBean = new FavoriteAppBean();
				try {
					favoriteAppBean = (FavoriteAppBean) CloneClass.clone(favoriteApp, favoriteAppBean);
					favoriteAppBeanList.add(favoriteAppBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return favoriteAppBeanList;
	}

	/**
	 * 修改手动下载的网络类型
	 * @param resourceId
	 * @param networkType
	 * @return
	 */
	public boolean updateManualDownloadNetwork(int resourceId, String networkType) {
		return FavoriteDBHelper.getInstance(context).updateManualDownloadNetworkByResourceId(resourceId, networkType,
				System.currentTimeMillis());
	}

	/**
	 * 适配List<FavoriteAppBean>
	 * @return
	 */
	public List<FavoriteAppBean> adapterFavoriteAppBean() {
		List<FavoriteAppBean> favoriteAppBeanList = null;
		List<FavoriteApp> favoriteAppList = findAllFavoriteApp();

		if (favoriteAppList != null && favoriteAppList.size() > 0) {
			favoriteAppBeanList = new ArrayList<FavoriteAppBean>();
			HashMap<String, InstallAppBean> updateAppMap = UpdateManage.getInstance(context)
					.findAllUpdateInstallAppMap();
			for (FavoriteApp favoriteApp : favoriteAppList) {
				FavoriteAppBean favoriteAppBean = new FavoriteAppBean();
				try {
					DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
							String.valueOf(favoriteApp.getFavoriteResourceId()),
							"" + favoriteApp.getFavoriteVersionCode());

					if (downloadBean != null) {
						favoriteAppBean = (FavoriteAppBean) CloneClass.cloneSuper(downloadBean, favoriteAppBean);
					}

					favoriteAppBean = (FavoriteAppBean) CloneClass.clone(favoriteApp, favoriteAppBean);

					InstallAppBean updateAppBean = updateAppMap.get(favoriteAppBean.getFavoritePackageName());
					if (updateAppBean != null && updateAppBean.getIsPatch()) { // 增量更新
						favoriteAppBean.setPatchUrl(updateAppBean.getUrlPatch());
						favoriteAppBean.setPatchSize(updateAppBean.getPatchSize());
						favoriteAppBean.setPatch(true);
					}
					favoriteAppBeanList.add(favoriteAppBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return favoriteAppBeanList;
	}

	/**
	 * wifi自动下载开关
	 */
	public void autoDownloadFavorteAppControl(boolean isNetworkChange) {
		boolean result = SharedPrefsUtil.getValue(this.context, SettingsUtils.AUTO_DOWNLOAD_FAV, false);
		if (result && NetworkUtils.isNetworkAvailable(context)
				&& NetworkUtils.NETWORK_TYPE_WIFI.equals(NetworkUtils.getNetworkInfo(context))) {
			initFavoriteAppData();
			List<FavoriteApp> favoriteAppList = FavoriteDBHelper.getInstance(context).findFavoriteAppByStatus(
					needDownloadStatus);

			if (favoriteAppList != null && favoriteAppList.size() > 0) {
				for (int i = 0; i < favoriteAppList.size(); i++) {
					FavoriteApp favoriteApp = favoriteAppList.get(i);
					if (!"-1".equals(favoriteAppList.get(i).getFavoriteManualDownloadNetwork())) {//-1原始状态，不等于-1即被手动暂停过，不进行下载
						continue;
					}

					DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
							"" + favoriteApp.getFavoriteResourceId(), "" + favoriteApp.getFavoriteVersionCode());
					if (downloadBean == null) {
						DataEyeManager.getInstance().source(StatisticConstan.SrcName.FAVORITE, 0, null, 0, null, null,
								false);
						addDownload(favoriteAppList.get(i));
					} else if (downloadBean != null && downloadBean.getStatus() == DownloadTask.TASK_PAUSE) {
						DownloadManager.getInstance().continueAutoDownload(context, downloadBean.getUrl());
					}

				}
			}
		} else {
			if (!isNetworkChange) {
				initFavoriteAppData();
				List<FavoriteApp> favoriteAppList = FavoriteDBHelper.getInstance(context).findFavoriteAppByStatus(
						needStopDownloadStatus);
				if (favoriteAppList != null && favoriteAppList.size() > 0) {
					ArrayList<String> urls = new ArrayList<String>();
					for (int i = 0; i < favoriteAppList.size(); i++) {
						FavoriteApp favoriteApp = favoriteAppList.get(i);
						DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
								"" + favoriteApp.getFavoriteResourceId(), "" + favoriteApp.getFavoriteVersionCode());
						if ("-1".equals(favoriteApp.getFavoriteManualDownloadNetwork()) && downloadBean != null) {
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
	private HashMap<String, InstallAppBean> getAppsUpgradeMap() {
		return UpdateManage.getInstance(context).findAllUpdateInstallAppMap();
	}

	/**
	 * 初始化UpgradeData
	 */
	private List<InstallAppBean> getAppsUpgradeResponse() {
		return UpdateManage.getInstance(context).findAllUpdateInstallApp();
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
		intent.setAction(MyIntents.INTENT_FAVORITE_AUTO_WIFI_ACTION);
		this.context.sendBroadcast(intent);
	}

	/**
	 * 触发下载
	 */
	public void triggerAutoWifiDownload() {
		boolean result = SharedPrefsUtil.getValue(this.context, SettingsUtils.AUTO_DOWNLOAD_FAV, false);
		if (result && NetworkUtils.isNetworkAvailable(context)
				&& NetworkUtils.NETWORK_TYPE_WIFI.equals(NetworkUtils.getNetworkInfo(context))) {
			sendAutoWifiDownloadBroadcast();
		}
	}

	/**
	 * 添加下载
	 * @param favoriteApp
	 */
	private void addDownload(FavoriteApp favoriteApp) {

		UpdateApp updateApp = UpdateManage.getInstance(context).getUpdateAppByPackageName(
				favoriteApp.getFavoritePackageName());
		boolean isUpdatePatch = false;
		String downloadUrl = favoriteApp.getFavoriteDownloadUrl();
		long totalBytes = favoriteApp.getFavoriteSize();
		long currentBytes = 0;
		if (updateApp != null && !TextUtils.isEmpty(updateApp.getUpdateAttribute())) {
			isUpdatePatch = true;
			downloadUrl = updateApp.getUpdateAttribute();
			totalBytes = updateApp.getUpdateExtAttribute2();
		}
		String mediaType = MediaType.APP;
		String type = favoriteApp.getFavoriteAttribute();
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
				"" + favoriteApp.getFavoriteResourceId(), "" + favoriteApp.getFavoriteVersionCode());
		if (downloadBeanHistory != null) {
			currentBytes = downloadBeanHistory.getCurrentBytes();
		}
		DownloadBean downloadBean = new DownloadBean(downloadUrl, favoriteApp.getFavoriteAppName(), totalBytes,
				currentBytes, favoriteApp.getFavoriteIconUrl(), mediaType, favoriteApp.getFavoriteResourceId(),
				favoriteApp.getFavoriteVersionName(), favoriteApp.getFavoritePackageName(),
				DownloadTask.TASK_DOWNLOADING, Long.valueOf(favoriteApp.getFavoriteSize()),
				favoriteApp.getFavoriteVersionCode(), favoriteApp.getFavoriteAppId(),
				favoriteApp.getFavoriteCategoryId(), favoriteApp.getFavoriteStarts(), isUpdatePatch,
				favoriteApp.getFavoriteDownloadUrl());
		SettingModel settingModel = Utils.getSettingModel(context);
		boolean toInstall = settingModel.isAutoInstall() && settingModel.isSilentInstall();
		downloadBean.setAutoInstall(toInstall);
		DownloadManager.getInstance().addDownload(this.context, downloadBean);
	}

	/**
	 * 添加下载
	 * @param favoriteApp
	 */
	public void addDownload(FavoriteAppBean favoriteAppBean, Context context) {
		boolean isUpdatePatch = false;
		String downloadUrl = favoriteAppBean.getFavoriteDownloadUrl();
		if (!TextUtils.isEmpty(favoriteAppBean.getPatchUrl())) {
			isUpdatePatch = true;
			downloadUrl = favoriteAppBean.getPatchUrl();
		}
		String mediaType = MediaType.APP;
		String type = favoriteAppBean.getFavoriteAttribute();
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

		DownloadBean downloadBean = new DownloadBean(downloadUrl, favoriteAppBean.getFavoriteAppName(),
				Long.valueOf(favoriteAppBean.getFavoriteSize()), 0, favoriteAppBean.getFavoriteIconUrl(), mediaType,
				favoriteAppBean.getFavoriteResourceId(), favoriteAppBean.getFavoriteVersionName(),
				favoriteAppBean.getFavoritePackageName(), DownloadTask.TASK_DOWNLOADING, Long.valueOf(favoriteAppBean
						.getFavoriteSize()), favoriteAppBean.getFavoriteVersionCode(),
				favoriteAppBean.getFavoriteAppId(), favoriteAppBean.getFavoriteCategoryId(),
				favoriteAppBean.getFavoriteStarts(), isUpdatePatch, favoriteAppBean.getFavoriteDownloadUrl());
		DownloadManager.getInstance().addDownload(context, downloadBean, favoriteAppBean.getFavoriteResourceId(), 1);
	}

	/**
	 * 记录favoriteApp网络状态
	 * @param favoriteResourceId
	 */
	public void markFavoriteManualDownloadNetwork(int favoriteResourceId) {
		String netWorkInfo = NetworkUtils.getNetworkInfo(this.context);
		updateManualDownloadNetwork(favoriteResourceId, netWorkInfo);
	}

	/**
	 * 移除favoriteApp网络状态
	 * @param favoriteResourceId
	 */
	public void cancelFavoriteManualDownloadNetwork(int favoriteResourceId) {
		updateManualDownloadNetwork(favoriteResourceId, "-1");
	}

	/**
	 * 暂停下载
	 * @param downloadUrl
	 */
	private void pauseDownload(String downloadUrl) {
		DownloadManager.getInstance().pauseDownload(this.context, downloadUrl);
	}

	/**
	 * 暂停下载
	 * @param downloadUrl
	 */
	private void pauseDownload(ArrayList<String> downloadUrls) {
		DownloadManager.getInstance().pauseDownload(this.context, downloadUrls);
	}

	public CharSequence getVersionTips(Context context, String packageName) {
		CharSequence tips = null;
		CharSequence localVersion = null;
		CharSequence newVersion = null;
		try {
			newVersion = UpdateManage.getInstance(context).getNewVersion(packageName);
			InstallAppBean installAppBean = LocalAppEntityManager.getInstance().getAllAppsMap().get(packageName);
			if (installAppBean != null)
				localVersion = installAppBean.getVersionName();
			if (newVersion != null && localVersion != null)
				tips = Html.fromHtml("Version:" + localVersion + "<font color=#FF3030>-->" + newVersion + "</font>");
			return tips;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
