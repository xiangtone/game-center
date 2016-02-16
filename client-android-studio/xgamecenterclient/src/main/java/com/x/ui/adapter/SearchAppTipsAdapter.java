/**   
 * @Title: SearchTipsAdapter.java
 * @Package com.x.ui.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-7-30 下午2:54:28
 * @version V1.0   
 */

package com.x.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;

/**
 * @ClassName: SearchTipsAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-30 下午2:54:28
 * 
 */

public class SearchAppTipsAdapter extends ArrayListBaseAdapter<AppInfoBean> {

	public SearchAppTipsAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		public TextView tipsName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = null;
		ViewHolder viewHolder;
		AppInfoBean appInfoBean = mList.get(position);
		if (position == 0) {
			// first item
			convertView = inflater.inflate(R.layout.home_recommend_app_list_item, null);
			AppListViewHolder holder = new AppListViewHolder(convertView);
			convertView.setTag(appInfoBean.getUrl());// 全量包url
			holder.initData(appInfoBean, context, R.drawable.ic_screen_default_picture);
			holder.setSkinTheme(context, 0);

			holder.appDownloadBtn.setOnClickListener(new MyListener(appInfoBean, holder));
			holder.appPauseView.setOnClickListener(new MyListener(appInfoBean, holder));
			holder.appContinueBtn.setOnClickListener(new MyListener(appInfoBean, holder));
			holder.appInstallBtn.setOnClickListener(new MyListener(appInfoBean, holder));
			holder.appUpgradeBtn.setOnClickListener(new MyListener(appInfoBean, holder));
			holder.appLaunchBtn.setOnClickListener(new MyListener(appInfoBean, holder));

		} else {
			// other item
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_tips_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tipsName = (TextView) convertView.findViewById(R.id.tv_tips_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tipsName.setText(appInfoBean.getAppName());
		}

		setSkinTheme(convertView);// set skin theme

		return convertView;
	}

	private class MyListener implements OnClickListener {

		private AppInfoBean appInfoBean;
		private AppListViewHolder viewHolder;

		public MyListener(AppInfoBean appInfoBean, AppListViewHolder viewHolder) {
			this.appInfoBean = appInfoBean;
			this.viewHolder = viewHolder;
		}

		@Override
		public void onClick(View v) {
			DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
					"" + appInfoBean.getApkId(), "" + appInfoBean.getVersionCode());
			switch (v.getId()) {
			case R.id.hali_app_download_btn:
				addDownload(appInfoBean);
				break;
			case R.id.hali_app_pause_ll:
				if (downloadBean == null)
					return;
				DownloadManager.getInstance().pauseDownload(context, downloadBean.getUrl());

				break;
			case R.id.hali_app_continue_btn:
				if (downloadBean == null)
					return;
				continueDownload(downloadBean.getUrl());
				break;
			case R.id.hali_app_install_btn:
				if (downloadBean == null)
					return;
				// to install
				if (downloadBean.isPatch()) {
					if (UpdateManage.getInstance(context).isNewApkFileExit(downloadBean.getPackageName(),
							downloadBean.getVersionName())) {
						PackageUtil.installApk(
								context,
								UpdateManage.getInstance(context).getNewPatchApkPath(downloadBean.getPackageName(),
										downloadBean.getVersionName()));
					} else if (!Utils.isAppExit(downloadBean.getPackageName(), context)) { // 低版本被卸载，下载全量包
						showReDownloadFulldoseDialog(
								downloadBean,
								appInfoBean,
								ResourceUtil.getString(context, R.string.dialog_redownload_full_dose,
										appInfoBean.getAppName()));
					} else {// 合并失败，下载增量包
						showReDownloadPatchDialog(downloadBean, appInfoBean, ResourceUtil.getString(context,
								R.string.dialog_redownload_incremental_upgrade, appInfoBean.getAppName()));
					}
				} else if (StorageUtils.isFileExit(downloadBean.getLocalPath())) {
					PackageUtil.installApk(context, downloadBean.getLocalPath());
				} else { // 文件不存在，正常重下流程
					showReDownloadDialog(
							downloadBean,
							appInfoBean,
							ResourceUtil.getString(context, R.string.dialog_redownload_file_not_found,
									appInfoBean.getAppName()));
				}
				break;
			case R.id.hali_app_upgrade_btn:
				addDownload(appInfoBean);
				break;
			case R.id.hali_app_launch_btn:
				// to launch
				Utils.launchAnotherApp(context, appInfoBean.getPackageName());
				break;
			default:
				break;
			}
			viewHolder.refreshAppStatus(appInfoBean.getStatus(), context, null);
		}
	}

	private void addDownload(AppInfoBean appInfoBean) {
		// SharedPrefsUtil.putValue(context, "ct_" + appInfoBean.getApkId(),
		// ct); // 存储ct值，方便下载统计使用
		boolean isUpdatePatch = false;
		String downloadUrl = appInfoBean.getUrl();
		if (!TextUtils.isEmpty(appInfoBean.getPatchUrl())) {
			isUpdatePatch = true;
			downloadUrl = appInfoBean.getPatchUrl();
		}
		String mediaType = MediaType.APP;
		if (appInfoBean.getFileType() == 1) {
			mediaType = MediaType.APP;
		} else if (appInfoBean.getFileType() == 2) {
			mediaType = MediaType.GAME;
		}
		DownloadBean downloadBean = new DownloadBean(downloadUrl, appInfoBean.getAppName(), appInfoBean.getFileSize(),
				0, appInfoBean.getLogo(), mediaType, appInfoBean.getApkId(), appInfoBean.getVersionName(),
				appInfoBean.getPackageName(), DownloadTask.TASK_DOWNLOADING, appInfoBean.getFileSize(),
				appInfoBean.getVersionCode(), appInfoBean.getAppId(), appInfoBean.getCategoryId(),
				appInfoBean.getStars(), isUpdatePatch, appInfoBean.getUrl());
		DownloadManager.getInstance().addDownload(context, downloadBean);
	}

	private void continueDownload(String url) {
		DownloadManager.getInstance().continueDownload(context, url);

	}

	private void showReDownloadDialog(final DownloadBean downloadBean, final AppInfoBean appInfoBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getUrl(), appInfoBean.getAppName(),
						downloadBean.getTotalBytes(), 0, appInfoBean.getLogo(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), appInfoBean.getStars(), false,
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

	private void showReDownloadFulldoseDialog(final DownloadBean downloadBean, final AppInfoBean appInfoBean,
			String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getOriginalUrl(), appInfoBean.getAppName(),
						downloadBean.getTotalBytes(), 0, appInfoBean.getLogo(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), appInfoBean.getStars(), false,
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

	private void showReDownloadPatchDialog(final DownloadBean downloadBean, final AppInfoBean appInfoBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getUrl(), appInfoBean.getAppName(),
						downloadBean.getTotalBytes(), 0, appInfoBean.getLogo(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), appInfoBean.getStars(), true,
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
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param convertView    
	* @return void    
	*/
	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}
}
