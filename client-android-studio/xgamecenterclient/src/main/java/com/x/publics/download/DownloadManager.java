/**   
* @Title: DownloadUtils.java
* @Package com.x.util
* @Description: TODO 

* @date 2014-1-20 上午09:54:23
* @version V1.0   
*/

package com.x.publics.download;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.x.R;
import com.x.business.favorite.FavoriteManage;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.dao.UpdateApp;
import com.x.db.updateapp.UpdateAppDBHelper;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;

/**
* @ClassName: DownloadUtils
* @Description: TODO 

* @date 2014-1-20 上午09:54:23
* 
*/

public class DownloadManager {

	public static DownloadManager downloadManager;

	public static DownloadManager getInstance() {
		if (downloadManager == null)
			downloadManager = new DownloadManager();
		return downloadManager;
	}

	public boolean canDownload(Context mContext) {
		if (!NetworkUtils.isNetworkAvailable(mContext)) {
			ToastUtil
					.show(mContext, mContext.getResources().getString(R.string.network_canot_work), Toast.LENGTH_SHORT);
			return false;
		}
		if (!StorageUtils.isSDCardPresent()) {
			ToastUtil.show(mContext, mContext.getResources().getString(R.string.sdcard_not_found), Toast.LENGTH_SHORT);
			return false;
		}

		if (!StorageUtils.isSdCardWrittenable()) {
			ToastUtil.show(mContext, mContext.getResources().getString(R.string.sdcard_cannot_use), Toast.LENGTH_SHORT);
			return false;
		}
		if (Utils.getSettingModel(mContext).isOnlyWifiDownload()
				&& !NetworkUtils.getNetworkInfo(mContext).equals(NetworkUtils.NETWORK_TYPE_WIFI)) {
			ToastUtil.show(mContext,ResourceUtil.getString(mContext,R.string.settings_only_download_wifi,ResourceUtil.getString(mContext, R.string.app_name)),
					Toast.LENGTH_SHORT);
			return false;
		}
		return true;
	}

	/** 
	* @Title: addDownloads 
	* @Description: 批量添加使用此方法 
	* @param @param mContext
	* @param @param downloadBean    
	* @return void    
	*/

	public void addDownloads(final Context mContext, final DownloadBean downloadBean) {
		doAddDownload(mContext, downloadBean);
	}

	/** 
	* @Title: addDownload 
	* @Description: 添加下载任务 
	* @param @param mContext
	* @param @param downloadBean     
	* @return void    
	* @throws 
	*/

	public void addDownload(final Context mContext, final DownloadBean downloadBean) {
		if (!canDownload(mContext))
			return;
		if (!NetworkUtils.getNetworkInfo(mContext).equals(NetworkUtils.NETWORK_TYPE_WIFI)
				&& Utils.getSettingModel(mContext).isGprsDownloadPromt()) {
			DialogInterface.OnClickListener positiveListener = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					doAddDownload(mContext, downloadBean);
				}
			};

			DialogInterface.OnClickListener negativeListener = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};
			try {
				Utils.showDialog(mContext, ResourceUtil.getString(mContext, R.string.warm_tips),
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt),
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt_download), positiveListener,
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt_cancle), negativeListener);
			} catch (Exception e) {
				e.printStackTrace();
				doAddDownload(mContext, downloadBean);
			}

		} else {
			doAddDownload(mContext, downloadBean);
		}
	}

	/** 
	* @Title: addDownload 
	* @Description: 添加下载任务 
	* @param @param mContext
	* @param @param downloadBean     
	* @param @param from 1来自收藏 2来自更新
	* @return void    
	* @throws 
	*/
	public void addDownload(final Context mContext, final DownloadBean downloadBean, final int apkId, final int from) {
		if (!canDownload(mContext))
			return;
		if (!NetworkUtils.getNetworkInfo(mContext).equals(NetworkUtils.NETWORK_TYPE_WIFI)
				&& Utils.getSettingModel(mContext).isGprsDownloadPromt()) {
			DialogInterface.OnClickListener positiveListener = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					doAddDownload(mContext, downloadBean);
					FavoriteManage.getInstance(mContext).markFavoriteManualDownloadNetwork(apkId);
					UpdateManage.getInstance(mContext).markManualDownloadNetwork(apkId);
				}
			};

			DialogInterface.OnClickListener negativeListener = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};
			try {
				Utils.showDialog(mContext, ResourceUtil.getString(mContext, R.string.warm_tips),
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt),
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt_download), positiveListener,
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt_cancle), negativeListener);
			} catch (Exception e) {
				e.printStackTrace();
				doAddDownload(mContext, downloadBean);
				FavoriteManage.getInstance(mContext).markFavoriteManualDownloadNetwork(apkId);
				UpdateManage.getInstance(mContext).markManualDownloadNetwork(apkId);
			}

		} else {
			doAddDownload(mContext, downloadBean);
			FavoriteManage.getInstance(mContext).cancelFavoriteManualDownloadNetwork(apkId);
			UpdateManage.getInstance(mContext).cancelManualDownloadNetwork(apkId);
		}
	}

	/** 
	* @Title: addDownloads 
	* @Description: 批量添加使用此方法 
	* @param @param mContext
	* @param @param downloadBean
	* @param @param apkId
	* @param @param from    
	* @return void    
	*/

	public void addDownloads(final Context mContext, final DownloadBean downloadBean, final int apkId, final int from) {
		doAddDownload(mContext, downloadBean);
		FavoriteManage.getInstance(mContext).cancelFavoriteManualDownloadNetwork(apkId);
		UpdateManage.getInstance(mContext).cancelManualDownloadNetwork(apkId);
	}

	/** 
	* @Title: pauseDownload 
	* @Description: 暂停下载 
	* @param @param mContext
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public void pauseDownload(Context mContext, String url) {
		Intent downloadIntent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(MyIntents.TYPE, MyIntents.Types.PAUSE);
		downloadIntent.putExtra(MyIntents.URL, url);
		mContext.startService(downloadIntent);
	}

	/** 
	* @Title: pauseDownload 
	* @Description: 暂停下载 
	* @param @param mContext
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public void pauseDownload(Context mContext, ArrayList<String> urls) {
		Intent downloadIntent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(MyIntents.TYPE, MyIntents.Types.PAUSE_SOME);
		downloadIntent.putStringArrayListExtra(MyIntents.URL, urls);
		mContext.startService(downloadIntent);
	}

	/** 
	* @Title: continueDownload 
	* @Description: 继续下载 
	* @param @param mContext
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public void continueDownload(final Context mContext, final String url) {
		if (!canDownload(mContext))
			return;
		if (!NetworkUtils.getNetworkInfo(mContext).equals(NetworkUtils.NETWORK_TYPE_WIFI)
				&& Utils.getSettingModel(mContext).isGprsDownloadPromt()) {
			DialogInterface.OnClickListener positiveListener = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					doContinueDownload(mContext, url);
				}
			};

			DialogInterface.OnClickListener negativeListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};
			try {
				Utils.showDialog(mContext, ResourceUtil.getString(mContext, R.string.warm_tips),
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt),
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt_download), positiveListener,
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt_cancle), negativeListener);
			} catch (Exception e) {
				e.printStackTrace();
				doContinueDownload(mContext, url);
			}

		} else {
			doContinueDownload(mContext, url);
		}
	}

	/** 
	* @Title: continueDownload 
	* @Description: 收藏/更新手动继续下载 ,处理wifi下收藏/更新自动下载标记
	* @param @param mContext
	* @param @param url
	* @param @param apkId
	* @return void    
	* @throws 
	*/

	public void continueDownload(final Context mContext, final String url, final int apkId) {
		if (!canDownload(mContext))
			return;
		if (!NetworkUtils.getNetworkInfo(mContext).equals(NetworkUtils.NETWORK_TYPE_WIFI)
				&& Utils.getSettingModel(mContext).isGprsDownloadPromt()) {
			DialogInterface.OnClickListener positiveListener = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					doContinueDownload(mContext, url);
					FavoriteManage.getInstance(mContext).markFavoriteManualDownloadNetwork(apkId);
					UpdateManage.getInstance(mContext).markManualDownloadNetwork(apkId);
				}
			};

			DialogInterface.OnClickListener negativeListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};
			try {
				Utils.showDialog(mContext, ResourceUtil.getString(mContext, R.string.warm_tips),
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt),
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt_download), positiveListener,
						ResourceUtil.getString(mContext, R.string.dialog_download_prompt_cancle), negativeListener);
			} catch (Exception e) {
				e.printStackTrace();
				doContinueDownload(mContext, url);
				FavoriteManage.getInstance(mContext).markFavoriteManualDownloadNetwork(apkId);
				UpdateManage.getInstance(mContext).markManualDownloadNetwork(apkId);
			}

		} else {
			doContinueDownload(mContext, url);
			FavoriteManage.getInstance(mContext).cancelFavoriteManualDownloadNetwork(apkId);
			UpdateManage.getInstance(mContext).cancelManualDownloadNetwork(apkId);
		}
	}

	/** 
	* @Title: continueAutoDownload 
	* @Description: 自动下载更新/收藏时的继续下载
	* @param @param mContext
	* @param @param url    
	* @return void    
	*/

	public void continueAutoDownload(final Context mContext, final String url) {
		if (!canDownload(mContext))
			return;
		doContinueAutoDownload(mContext, url);
	}

	private void doAddDownload(final Context mContext, final DownloadBean downloadBean) {
		Intent downloadIntent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(MyIntents.TYPE, MyIntents.Types.ADD);
		Bundle bundle = new Bundle();
		bundle.putSerializable(MyIntents.DOWNLOADBEAN, downloadBean);
		downloadIntent.putExtras(bundle);
		mContext.startService(downloadIntent);
	}

	private void doContinueDownload(final Context mContext, final String url) {
		Intent downloadIntent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(MyIntents.TYPE, MyIntents.Types.CONTINUE);
		downloadIntent.putExtra(MyIntents.URL, url);
		mContext.startService(downloadIntent);
	}

	private void doContinueAutoDownload(final Context mContext, final String url) {
		Intent downloadIntent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(MyIntents.TYPE, MyIntents.Types.AUTO_DOWNLOAD_CONTINUE);
		downloadIntent.putExtra(MyIntents.URL, url);
		mContext.startService(downloadIntent);
	}

	/** 
	* @Title: deleteDownload 
	* @Description: 删除下载 
	* @param @param mContext
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public void deleteDownload(Context mContext, String url) {
		Intent downloadIntent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(MyIntents.TYPE, MyIntents.Types.DELETE);
		downloadIntent.putExtra(MyIntents.URL, url);
		mContext.startService(downloadIntent);
		DownloadEntityManager.getInstance().deleteByUrl(url);
	}

	/** 
	* @Title: deleteAllDownload 
	* @Description: 删除所有下载 
	* @param @param mContext
	* @param @param type     
	* @return void    
	* @throws 
	*/

	public void deleteAllDownload(Context mContext, int type) {
		Intent downloadIntent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(MyIntents.TYPE, type);
		mContext.startService(downloadIntent);
		if (type == MyIntents.Types.DELETE_ALL_DOWNLOADING)
			DownloadEntityManager.getInstance().deleteAllUnFinished();
		else if (type == MyIntents.Types.DELETE_ALL_HISTORY)
			DownloadEntityManager.getInstance().deleteAllFinished();
		else if (type == MyIntents.Types.DELETE_ALL_DOWNLOADING_MEDIA)
			DownloadEntityManager.getInstance().deleteUnfinishedMedia();
	}

	/** 
	* @Title: pauseAllDownload 
	* @Description: 暂停所有下载 
	* @param @param context     
	* @return void    
	* @throws 
	*/
	public void pauseAllDownload(Context context) {
		Intent downloadIntent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
		downloadIntent.putExtra(MyIntents.TYPE, MyIntents.Types.PAUSE_ALL);
		context.startService(downloadIntent);
	}

	public void deleteOldVersionDownloadHistory(DownloadBean downloadBean) {
		if (downloadBean == null)
			return;
		String mediaType = downloadBean.getMediaType();
		if (MediaType.APP.equals(mediaType) || MediaType.GAME.equals(mediaType)) {
			List<DownloadBean> uninstallAppList = DownloadEntityManager.getInstance()
					.getAppDownloadFinishedByPackageName(downloadBean.getPackageName());
			for (DownloadBean historyDownloadBean : uninstallAppList) {
				if (historyDownloadBean.getVersionCode() != downloadBean.getVersionCode()) {
					DownloadEntityManager.getInstance().deleteByUrl(historyDownloadBean.getUrl());
					File file = new File(historyDownloadBean.getLocalPath());
					file.delete();
				}
			}
		}
	}

	public void updateAllApp(Context context) {
		UpdateManage.getInstance(context).downloadAllUpdate(context,
				UpdateManage.getInstance(context).findAllUpdateInstallApp());
	}

	public void pauseAllApp(Context context) {
		ArrayList<String> urls = new ArrayList<String>();
		ArrayList<DownloadBean> downloadList = DownloadEntityManager.getInstance().getAllAppDownloading();
		if (!downloadList.isEmpty()) {
			for (DownloadBean downloadBean : downloadList) {
				urls.add(downloadBean.getUrl());
				UpdateManage.getInstance(context).markManualDownloadNetwork(downloadBean.getResourceId());
				FavoriteManage.getInstance(context).markFavoriteManualDownloadNetwork(downloadBean.getResourceId());
			}
			if (!urls.isEmpty()) {
				pauseDownload(context, urls);
			}
		}
	}

	public void downloadAllApp(Context context) {
		ArrayList<DownloadBean> pauseList = DownloadEntityManager.getInstance().getAllAppPaused();
		for (DownloadBean downloadBean : pauseList) {
			continueDownload(context, downloadBean.getUrl(), downloadBean.getResourceId());
		}

	}

}
