package com.x.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.x.R;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;

/**
* @ClassName: LocalAppListAdapter
* @Description: 应用管理适配器 

* @date 2014-1-16 下午04:42:38
* 
*/

public class UpdateAppListAdapter extends ArrayListBaseAdapter<InstallAppBean> {

	boolean sign = false;
	int isHave = 0;
	public static boolean downloading = false;

	public UpdateAppListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.update_app_item_layout, null);
			}
			final InstallAppBean infoBean = mList.get(position);
			final UpdateAppListViewHolder holder = new UpdateAppListViewHolder(convertView);
			convertView.setTag(infoBean.getUrl());//全量包url
			holder.initData(infoBean, context);
			holder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
			holder.downloadPauseTv.setOnClickListener(new MyBtnListener(holder, infoBean));
			holder.downloadContinueTv.setOnClickListener(new MyBtnListener(holder, infoBean));
			holder.installTv.setOnClickListener(new MyBtnListener(holder, infoBean));
			holder.upgradeTv.setOnClickListener(new MyBtnListener(holder, infoBean));
			holder.appIconIv.setOnClickListener(new MyBtnListener(holder, infoBean));
			convertView.setOnClickListener(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;

	}

	private class MyBtnListener implements OnClickListener {
		UpdateAppListViewHolder viewHolder;
		InstallAppBean infoBean;

		public MyBtnListener(UpdateAppListViewHolder viewHolder, InstallAppBean infoBean) {
			this.viewHolder = viewHolder;
			this.infoBean = infoBean;
		}

		@Override
		public void onClick(View v) {
			DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
					"" + infoBean.getApkId(), "" + infoBean.getVersionCode());
			switch (v.getId()) {
			case R.id.uail_upgrade_tv:
				UpdateManage.getInstance(context).addDownload(infoBean, context, false, true);
				break;
			case R.id.uail_download_install_tv:
				// to install
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
			case R.id.uail_download_pause_tv:
				if (downloadBean == null)
					return;
				DownloadManager.getInstance().pauseDownload(context, downloadBean.getUrl());
				UpdateManage.getInstance(context).markManualDownloadNetwork(infoBean.getApkId());
				viewHolder.downloadPauseTv.setVisibility(View.INVISIBLE);
				viewHolder.downloadContinueTv.setVisibility(View.VISIBLE);

				break;
			case R.id.uail_download_continue_tv:
				if (downloadBean == null)
					return;
				DownloadManager.getInstance().continueDownload(context, downloadBean.getUrl(), infoBean.getApkId());
				break;
			}

		}
	}

	/** 
	* @Title: showReDownloadDialog 
	* @Description: 正常重新下载 
	* @param @param downloadBean
	* @param @param tips    
	* @return void    
	*/

	private void showReDownloadDialog(final DownloadBean downloadBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				/*dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				UpdateApp updateApp = UpdateManage.getInstance(context).getUpdateAppByPackageName(
						downloadBean.getPackageName());
				boolean isUpdatePatch = false;
				String downloadUrl = downloadBean.getUrl();
				if (updateApp != null && !TextUtils.isEmpty(updateApp.getUpdateAttribute())) {
					isUpdatePatch = true;
					downloadUrl = updateApp.getUpdateAttribute();
				}
				DownloadBean redownloadBean = new DownloadBean(downloadUrl, downloadBean.getName(), 0, 0,
						downloadBean.getIconUrl(), MediaType.APP, downloadBean.getResourceId(),
						downloadBean.getVersionName(), downloadBean.getPackageName(), DownloadTask.TASK_DOWNLOADING,
						downloadBean.getTotalBytes(), downloadBean.getVersionCode(), downloadBean.getAppId(),
						downloadBean.getCategoryId(), downloadBean.getStars(), isUpdatePatch, downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);*/
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

}
