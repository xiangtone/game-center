package com.x.ui.adapter.gridview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.dao.UpdateApp;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;
import com.x.ui.adapter.gridview.base.ListAsGridBaseAdapter;

/**
 * @ClassName: AppGridListAdapter
 * @Desciption: appGridListAdapter 适配器
 
 * @Date: 2014-3-1 下午3:14:04
 */

public class AppGridListAdapter extends ListAsGridBaseAdapter {

	private int ct;
	private Context context;
	private List<AppInfoBean> data = new ArrayList<AppInfoBean>();
	private LayoutInflater mInflater;

	public AppGridListAdapter(Context context) {
		super(context, R.layout.gridview_item_layout);
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	public AppGridListAdapter(Context context, int ct) {
		super(context, R.layout.gridview_item_layout);
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.ct = ct;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return getList().size();
	}

	@Override
	protected View getItemView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.gridview_item_layout, null);
		}
		final AppInfoBean appInfoBean = data.get(position);
		AppGridViewHolder holder = new AppGridViewHolder(convertView);
		convertView.setTag(appInfoBean.getUrl());
		holder.initData(appInfoBean, context);
		holder.setSkinTheme(context);

		holder.appDownloadBtn.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.downloadPercentPv.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.appContinueBtn.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.appInstallBtn.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.appUpgradeBtn.setOnClickListener(new MyListener(appInfoBean, holder));
		holder.appLaunchBtn.setOnClickListener(new MyListener(appInfoBean, holder));

		setSkinTheme(convertView);// set skin theme

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		View view = convertView.findViewById(R.id.item_gridview_ll);
		SkinConfigManager.getInstance().setViewBackground(context, view, SkinConstan.GRID_VIEW_ITEM_BG);
	}

	private class MyListener implements OnClickListener {

		private AppInfoBean appInfoBean;
		private AppGridViewHolder viewHolder;

		public MyListener(AppInfoBean appInfoBean, AppGridViewHolder viewHolder) {
			this.appInfoBean = appInfoBean;
			this.viewHolder = viewHolder;
		}

		@Override
		public void onClick(View v) {
			DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
					"" + appInfoBean.getApkId(), "" + appInfoBean.getVersionCode());
			switch (v.getId()) {
			case R.id.gl_app_download_btn:
				addDownload(appInfoBean);
				break;
			case R.id.gl_app_pause_rp:
				if (downloadBean == null)
					return;
				DownloadManager.getInstance().pauseDownload(context, downloadBean.getUrl());
				break;
			case R.id.gl_app_continue_btn:
				if (downloadBean == null)
					return;
				continueDownload(downloadBean.getUrl());
				break;
			case R.id.gl_app_install_btn:

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
			case R.id.gl_app_upgrade_btn:
				addDownload(appInfoBean);
				break;
			case R.id.gl_app_launch_btn:
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
		UpdateApp updateApp = UpdateManage.getInstance(context).getUpdateAppByPackageName(appInfoBean.getPackageName());
		boolean isUpdatePatch = false;
		String downloadUrl = appInfoBean.getUrl();
		if (updateApp != null && !TextUtils.isEmpty(updateApp.getUpdateAttribute())) {
			isUpdatePatch = true;
			downloadUrl = updateApp.getUpdateAttribute();
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

	public void setList(List<AppInfoBean> list) {
		this.data = list;
		notifyDataSetChanged();
	}

	public List<AppInfoBean> getList() {
		return data;
	}

}
