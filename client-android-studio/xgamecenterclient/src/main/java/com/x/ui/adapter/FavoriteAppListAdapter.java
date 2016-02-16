package com.x.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.x.R;
import com.x.business.favorite.FavoriteManage;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.FavoriteAppBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;

public class FavoriteAppListAdapter extends ArrayListBaseAdapter<FavoriteAppBean> {

	int isHave = 0;
	boolean sign = false;
	public static boolean downloading = false;

	public FavoriteAppListAdapter(Activity context) {
		super(context);

	}

	public void removeItem(String url) {
		for (FavoriteAppBean favoriteAppBean : mList) {
			if (favoriteAppBean.getUrl() != null && favoriteAppBean.getUrl().equals(url)) {
				removeItem(favoriteAppBean);
				break;
			}
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.favorite_app_item_layout, null);
			}
			final FavoriteAppBean favoriteAppBean = mList.get(position);
			final FavoriteAppViewHolder holder = new FavoriteAppViewHolder(convertView);
			convertView.setTag(favoriteAppBean.getFavoriteDownloadUrl());//全量包url
			holder.initData(favoriteAppBean, context);

			holder.downloadPauseTv.setOnClickListener(new MyBtnListener(holder, favoriteAppBean));
			holder.downloadContinueTv.setOnClickListener(new MyBtnListener(holder, favoriteAppBean));
			holder.installTv.setOnClickListener(new MyBtnListener(holder, favoriteAppBean));
			holder.upgradeTv.setOnClickListener(new MyBtnListener(holder, favoriteAppBean));
			holder.appIconIv.setOnClickListener(new MyBtnListener(holder, favoriteAppBean));
			holder.detailAppTv.setOnClickListener(new MyBtnListener(holder, favoriteAppBean));
			holder.cancelAppTv.setOnClickListener(new MyBtnListener(holder, favoriteAppBean));
			holder.launchTv.setOnClickListener(new MyBtnListener(holder, favoriteAppBean));
			holder.downloadTv.setOnClickListener(new MyBtnListener(holder, favoriteAppBean));
			convertView.setOnClickListener(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;

	}

	private class MyBtnListener implements OnClickListener {
		FavoriteAppViewHolder viewHolder;
		FavoriteAppBean favoriteAppBean;

		public MyBtnListener(FavoriteAppViewHolder viewHolder, FavoriteAppBean favoriteAppBean) {
			this.viewHolder = viewHolder;
			this.favoriteAppBean = favoriteAppBean;
		}

		@Override
		public void onClick(View v) {
			DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
					"" + favoriteAppBean.getFavoriteResourceId(), "" + favoriteAppBean.getFavoriteVersionCode());
			switch (v.getId()) {
			case R.id.favo_upgrade_tv:
				FavoriteManage.getInstance(context).addDownload(favoriteAppBean, context);
				break;
			case R.id.favo_download_pause_tv:
				if (downloadBean == null)
					return;
				DownloadManager.getInstance().pauseDownload(context, downloadBean.getUrl());
				viewHolder.refreshAppStatus(AppInfoBean.Status.PAUSED, context);
				FavoriteManage.getInstance(context).markFavoriteManualDownloadNetwork(
						favoriteAppBean.getFavoriteResourceId());
				break;
			case R.id.favo_download_continue_tv:
				if (downloadBean != null && downloadBean.getResourceId() > 0) {
					DownloadManager.getInstance().continueDownload(context, downloadBean.getUrl(),
							favoriteAppBean.getResourceId());
				}
				break;
			case R.id.favo_app_download_tv:
				FavoriteManage.getInstance(context).addDownload(favoriteAppBean, context);
				break;
			case R.id.favo_launch_tv:
				launchApp(favoriteAppBean);
				break;
			case R.id.favo_download_install_tv:
				if (downloadBean == null)
					return;
				if (downloadBean.isPatch()) {
					if (UpdateManage.getInstance(context).isNewApkFileExit(downloadBean.getPackageName(),downloadBean.getVersionName())) {
						PackageUtil.installApk(context,
								UpdateManage.getInstance(context).getNewPatchApkPath(downloadBean.getPackageName(),downloadBean.getVersionName()));
					} else if (!Utils.isAppExit(downloadBean.getPackageName(), context)) { // 低版本被卸载，下载全量包
						showReDownloadFulldoseDialog(
								downloadBean,
								ResourceUtil.getString(context, R.string.dialog_redownload_full_dose,
										downloadBean.getName()));
					} else {//合并失败，下载增量包
						showReDownloadPatchDialog(downloadBean, ResourceUtil.getString(context,
								R.string.dialog_redownload_incremental_upgrade, downloadBean.getName()));
					}
				} else if (StorageUtils.isFileExit(downloadBean.getLocalPath())) {
					PackageUtil.installApk(context, downloadBean.getLocalPath());
				} else { //文件不存在，正常重下流程
					showReDownloadDialog(
							downloadBean,
							ResourceUtil.getString(context, R.string.dialog_redownload_file_not_found,
									downloadBean.getName()));
				}
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 启动应用
	 * @param appPackageName
	 */
	public void launchApp(final FavoriteAppBean favoriteAppBean) {
		if (!TextUtils.isEmpty(favoriteAppBean.getFavoritePackageName())) {
			Intent intent = this.context.getPackageManager().getLaunchIntentForPackage(
					favoriteAppBean.getFavoritePackageName());
			if (intent != null) {
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				this.context.startActivity(intent);
			} else {
				showReDownloadDialog(
						favoriteAppBean,
						ResourceUtil.getString(context, R.string.dialog_redownload_app_not_found,
								favoriteAppBean.getFavoriteAppName()));
			}
		}
	}

	private void showReDownloadDialog(final DownloadBean downloadBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getUrl(), downloadBean.getName(),
						downloadBean.getTotalBytes(), 0, downloadBean.getIconUrl(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), downloadBean.getStars(), false,
						downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	/** 
	* @Title: showReDownloadFulldoseDialog 
	* @Description: 重新下载全量包 
	* @param @param downloadBean
	* @param @param tips    
	* @return void    
	*/

	private void showReDownloadFulldoseDialog(final DownloadBean downloadBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getOriginalUrl(), downloadBean.getName(),
						downloadBean.getTotalBytes(), 0, downloadBean.getIconUrl(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), downloadBean.getStars(), false,
						downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	/** 
	* @Title: showReDownloadPatchDialog 
	* @Description: 重下增量包
	* @param @param downloadBean
	* @param @param tips    
	* @return void    
	*/

	private void showReDownloadPatchDialog(final DownloadBean downloadBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getUrl(), downloadBean.getName(),
						downloadBean.getTotalBytes(), 0, downloadBean.getIconUrl(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), downloadBean.getStars(), true,
						downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	/**
	 * 刷新收藏列表
	 */
	private void refreshFavoriteList() {
		Intent updateIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		updateIntent.putExtra(MyIntents.TYPE, MyIntents.INTENT_TYPE_FAVORITE_REFRESH_LIST);
		BroadcastManager.sendBroadcast(new Intent(updateIntent));
	}


}
