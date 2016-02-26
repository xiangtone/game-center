package com.x.ui.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.x.R;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.RingtonesBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;
import com.x.publics.utils.MediaPlayerUtil.onRingtonesListener;
import com.x.ui.activity.wallpaper.WallpaperOpenActivity;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;

/**
 * @ClassName: DownloadListAdapter
 * @Description: 下载管理listview Adapter
 
 * @date 2014-1-10 上午09:21:39
 * 
 */

public class DownloadListAdapter extends ArrayListBaseAdapter<DownloadBean> implements onRingtonesListener {

	int isHave = 0;
	boolean sign = false;
	public static boolean downloading = false;
	private Animation operatingAnim = null;
	private ActionSlideExpandableListView mDownloadLv = null;
	private Activity mActivity;

	public DownloadListAdapter(Activity context) {
		super(context);
		operatingAnim = AnimationUtils.loadAnimation(context, R.anim.ring_play);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		mActivity = context;
	}

	public void setListView(ActionSlideExpandableListView mDownloadLv) {
		this.mDownloadLv = mDownloadLv;
	}

	public void removeItem(String url) {
		for (DownloadBean downloadBean : mList) {
			if (downloadBean.getUrl() != null && downloadBean.getUrl().equals(url)) {
				removeItem(downloadBean);
				break;
			}
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.download_item_layout, null);
			}
			final DownloadBean downloadBean = mList.get(position);
			final DownloadViewHolder holder = new DownloadViewHolder(convertView);
			convertView.setTag(downloadBean.getOriginalUrl());
			holder.initData(downloadBean, context);
			holder.setSkinTheme(context);

			holder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
			holder.downloadPauseTv.setOnClickListener(new MyBtnListener(holder, downloadBean));
			holder.downloadContinueTv.setOnClickListener(new MyBtnListener(holder, downloadBean));
			holder.downloadInstallTv.setOnClickListener(new MyBtnListener(holder, downloadBean));
			holder.launchTv.setOnClickListener(new MyBtnListener(holder, downloadBean));
			holder.openTv.setOnClickListener(new MyBtnListener(holder, downloadBean));
			holder.settingTv.setOnClickListener(new MyBtnListener(holder, downloadBean));
			holder.musicLayout.setOnClickListener(new MyBtnListener(holder, downloadBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;

	}

	private class MyBtnListener implements OnClickListener {
		DownloadViewHolder viewHolder;
		DownloadBean downloadBean;

		public MyBtnListener(DownloadViewHolder viewHolder, DownloadBean downloadBean) {
			this.viewHolder = viewHolder;
			this.downloadBean = downloadBean;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dil_download_pause_tv:
				DownloadManager.getInstance().pauseDownload(context, downloadBean.getUrl());
				viewHolder.downloadPauseTv.setVisibility(View.INVISIBLE);
				viewHolder.downloadContinueTv.setVisibility(View.VISIBLE);
				break;
			case R.id.dil_download_continue_tv:
				DownloadManager.getInstance().continueDownload(context, downloadBean.getUrl());
				break;
			case R.id.dil_download_install_tv:

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
			case R.id.dil_download_launch_tv:
				if (Utils.isAppExit(downloadBean.getPackageName(), context))
					Utils.launchAnotherApp(context, downloadBean.getPackageName());
				else
					showReDownloadDialog(
							downloadBean,
							ResourceUtil.getString(context, R.string.dialog_redownload_app_not_found,
									downloadBean.getName()));
				break;

			case R.id.dil_download_open_tv:
				if (StorageUtils.isFileExit(downloadBean.getLocalPath())) {
					WallpaperBean wallpaperBean = Utils.getWallpaperBean(downloadBean);
					//Intent intent = new Intent(context, WallpaperBigViewActivity.class);
					Intent intent = new Intent(context, WallpaperOpenActivity.class);
					intent.putExtra("wallpaperBean", wallpaperBean);
					intent.putExtra("localPath", downloadBean.getLocalPath());
					context.startActivity(intent);
				} else {
					showReDownloadDialog(
							downloadBean,
							ResourceUtil.getString(context, R.string.dialog_redownload_file_not_found,
									downloadBean.getName()));
				}

				break;

			case R.id.dil_download_setting_tv:
				if (StorageUtils.isFileExit(downloadBean.getLocalPath())) {
					Utils.showRingtonesSettingDialog(context, downloadBean.getLocalPath());
				} else {
					showReDownloadDialog(
							downloadBean,
							ResourceUtil.getString(context, R.string.dialog_redownload_file_not_found,
									downloadBean.getName()));
				}

				break;
			case R.id.mus_disk_bg:
				DownloadBean musicDownloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
						"" + downloadBean.getResourceId());
				if (StorageUtils.isFileExit(downloadBean.getLocalPath())) {
					if (downloadBean.getMediaType().equals(MediaType.MUSIC)) {
						if (mDownloadLv != null && downloadBean.getOriginalUrl() != null) {
							MediaPlayerUtil playerUtil = MediaPlayerUtil.getInstance(mActivity);
							RingtonesBean ringtonesBean = new RingtonesBean();
							ringtonesBean.setUrl(downloadBean.getOriginalUrl());
							ringtonesBean.setFilepath(downloadBean.getLocalPath());
							//							ringtonesBean.setDuration((int)bean.getDuration());
							playerUtil.start(ringtonesBean);
							playerUtil.setRingtonesListener(DownloadListAdapter.this);
						}
					}
				} else if (musicDownloadBean != null && musicDownloadBean.getStatus() == DownloadTask.TASK_FINISH) {
					showReDownloadDialog(musicDownloadBean, ResourceUtil.getString(context,
							R.string.dialog_redownload_file_not_found, musicDownloadBean.getName()));
				}

				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onRingtonesLoading(String mTag) {
		View view = mDownloadLv.findViewWithTag(mTag);
		if (view != null) {
			DownloadViewHolder mHolder = new DownloadViewHolder(view);
			if (mHolder.disk.getAnimation() == null) {
				mHolder.disk.startAnimation(operatingAnim);
			}
			if (mHolder.disk_pic.getAnimation() == null) {
				mHolder.disk_pic.startAnimation(operatingAnim);
			}
		}
	}

	@Override
	public void onRingtonesPlayer(String mTag, int cur, int dur) {
		View view = mDownloadLv.findViewWithTag(mTag);
		if (view != null) {
			DownloadViewHolder mHolder = new DownloadViewHolder(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_pause);
			if (mHolder.disk.getAnimation() == null) {
				mHolder.disk.startAnimation(operatingAnim);
			}
			if (mHolder.disk_pic.getAnimation() == null) {
				mHolder.disk_pic.startAnimation(operatingAnim);
			}
			String from = mHolder.refreshDuration(cur, dur, mActivity);
			mHolder.duration.setVisibility(View.VISIBLE);
			mHolder.duration.setText(Html.fromHtml(from));
		}
	}

	@Override
	public void onRingtonesPause(String mTag, int cur, int dur) {
		View view = mDownloadLv.findViewWithTag(mTag);
		if (view != null) {
			DownloadViewHolder mHolder = new DownloadViewHolder(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_play);
			if (mHolder.disk.getAnimation() != null) {
				mHolder.disk.clearAnimation();
			}
			if (mHolder.disk_pic.getAnimation() != null) {
				mHolder.disk_pic.clearAnimation();
			}
			String from = mHolder.refreshDuration(cur, dur, mActivity);
			mHolder.duration.setVisibility(View.VISIBLE);
			mHolder.duration.setText(Html.fromHtml(from));
		}
	}

	@Override
	public void onRingtonesStop(String mTag, int defDuration) {
		View view = mDownloadLv.findViewWithTag(mTag);
		if (view != null) {
			DownloadViewHolder mHolder = new DownloadViewHolder(view);
			mHolder.play_but.setImageResource(R.drawable.ringtones_play);
			mHolder.disk.clearAnimation();
			mHolder.disk_pic.clearAnimation();
			mHolder.duration.setVisibility(View.GONE);
			//			String mdur = mHolder.recoverDuration(defDuration);
			//			mHolder.duration.setText(mdur);
		}
	}

	private void showReDownloadDialog(final DownloadBean downloadBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadBean redownloadBean = null;
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());
				String mediaType = downloadBean.getMediaType();
				if (mediaType == null)
					return;
				if (mediaType.endsWith(MediaType.APP) || mediaType.endsWith(MediaType.GAME)) {
					//					UpdateApp updateApp = UpdateManage.getInstance(context).getUpdateAppByPackageName(
					//							downloadBean.getPackageName());
					boolean isUpdatePatch = false;
					String downloadUrl = downloadBean.getUrl();
					//					if (updateApp != null && !TextUtils.isEmpty(updateApp.getUpdateAttribute())) {
					//						isUpdatePatch = true;
					//						downloadUrl = updateApp.getUpdateAttribute();
					//					}
					redownloadBean = new DownloadBean(downloadUrl, downloadBean.getName(),
							downloadBean.getTotalBytes(), 0, downloadBean.getIconUrl(), downloadBean.getMediaType(),
							downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
							DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
							downloadBean.getAppId(), downloadBean.getCategoryId(), downloadBean.getStars(),
							isUpdatePatch, downloadBean.getOriginalUrl());
				} else if (mediaType.endsWith(MediaType.IMAGE)) {
					redownloadBean = new DownloadBean(downloadBean.getUrl(), downloadBean.getIconUrl(),
							downloadBean.getName(), (int) downloadBean.getTotalBytes(), 0, MediaType.IMAGE,
							DownloadTask.TASK_DOWNLOADING, downloadBean.getFileSize(), false,
							downloadBean.getResourceId(), downloadBean.getCategoryId());
				} else if (mediaType.endsWith(MediaType.MUSIC)) {
					redownloadBean = new DownloadBean(downloadBean.getUrl(), downloadBean.getIconUrl(),
							downloadBean.getName(), (int) downloadBean.getTotalBytes(), 0, MediaType.MUSIC,
							DownloadTask.TASK_DOWNLOADING, downloadBean.getFileSize(), downloadBean.getResourceId(),
							downloadBean.getCategoryId());
				}

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
	* @Description: 重新下载增量包
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
