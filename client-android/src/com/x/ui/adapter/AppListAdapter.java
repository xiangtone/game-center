/**   
 * @Title: AppListAdapter.java
 * @Package com.mas.amineappstore.adapter
 * @Description: TODO 
 
 * @date 2013-12-23 上午10:26:20
 * @version V1.0   
 */

package com.x.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;
import com.x.ui.activity.appdetail.AppDetailActivity;

/**
 * @ClassName: AppListAdapter
 * @Description: 应用listView适配器
 
 * @date 2013-12-23 上午10:26:20
 * 
 */

public class AppListAdapter extends ArrayListBaseAdapter<AppInfoBean> {

	private int ct, resId;
	private Activity context;
	private String searchKey;

	public AppListAdapter(Activity context) {
		super(context);
		this.context = context;
	}

	public AppListAdapter(Activity context, int ct) {
		super(context);
		this.ct = ct;
		this.context = context;
	}

	public int index = 0;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			if (ct == Constan.Ct.WALLPAPER_LIVE) {
				resId = R.drawable.banner_default_picture;
				convertView = inflater.inflate(R.layout.wallpapper_live_item, null);
			} else {
				resId = R.drawable.ic_screen_default_picture;
				convertView = inflater.inflate(R.layout.home_recommend_app_list_item, null);
			}
		}
		final AppInfoBean appInfoBean = mList.get(position);
		AppListViewHolder holder = new AppListViewHolder(convertView);
		convertView.setTag(appInfoBean.getUrl());//全量包url
		holder.initData(appInfoBean, context, resId);
		holder.setSkinTheme(context, ct);

		holder.appDownloadBtn.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.appPauseView.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.appContinueBtn.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.appInstallBtn.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.appUpgradeBtn.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.appLaunchBtn.setOnClickListener(new MyListener(appInfoBean, holder));

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// check network
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}

				Intent intent = new Intent(context, AppDetailActivity.class);
				intent.putExtra("appInfoBean", appInfoBean);
				intent.putExtra("ct", ct);
				context.startActivity(intent);
			}
		});

		setSkinTheme(convertView);

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		if (ct == Constan.Ct.WALLPAPER_LIVE) {
			View view = convertView.findViewById(R.id.item_listview_ll);
			SkinConfigManager.getInstance().setViewBackground(context, view, SkinConstan.LIST_VIEW_ITEM_BG);
		} else {
			SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
		}
	}

	protected class MyListener implements OnClickListener {

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
					} else {//合并失败，下载增量包
						showReDownloadPatchDialog(downloadBean, appInfoBean, ResourceUtil.getString(context,
								R.string.dialog_redownload_incremental_upgrade, appInfoBean.getAppName()));
					}
				} else if (StorageUtils.isFileExit(downloadBean.getLocalPath())) {
					PackageUtil.installApk(context, downloadBean.getLocalPath());
				} else { //文件不存在，正常重下流程
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
		SharedPrefsUtil.putValue(context, "ct_" + appInfoBean.getApkId(), ct); // 存储ct值，方便下载统计使用	
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

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
}
