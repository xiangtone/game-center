/**   
* @Title: DownloadView java
* @Package com.yimibox.appstore.adapter
* @Description: TODO 

* @date 2013-11-14 上午09:51:51
* @version V1.0   
*/

package com.x.ui.adapter;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;

/**
* @ClassName: DownloadViewHolder
* @Description: 下载管理listview Adapter 的ViewHolder 

* @date 2013-11-14 上午09:51:51
* 
*/

public class DownloadViewHolder implements Serializable {

	private static final long serialVersionUID = -1749951969451779511L;
	public static final int KEY_URL = 0;
	public static final int KEY_SPEED = 1;
	public static final int KEY_PROGRESS = 2;
	public static final int KEY_DOWNLOAD_PROMPT = 3;

	private View rootView;
	public ImageView appIcon, arrowIv;
	public TextView appName;
	public TextView appSizeStatus;
	public TextView appVersionTv;
	public TextView appDownloadStatus;
	public ProgressBar progressBar;
	public TextView downloadPauseTv, downloadContinueTv, downloadInstallTv, launchTv, openTv, settingTv;
	public View expandDividerView, expandDetailView;
	public boolean hasInited;
	public LinearLayout llytIsShow;
	//Music
	public ImageView disk, play_but, disk_pic = null;
	public ProgressBar loading = null;
	public RelativeLayout musicLayout = null;
	public TextView duration = null;

	public DownloadViewHolder(View view) {
		if (view != null) {
			rootView = view.findViewById(R.id.dil_top_rll);
			appIcon = (ImageView) view.findViewById(R.id.dil_app_icon_iv);
			arrowIv = (ImageView) view.findViewById(R.id.dil_arrow_iv);
			appName = (TextView) view.findViewById(R.id.dil_app_name_tv);
			appSizeStatus = (TextView) view.findViewById(R.id.dil_app_download_size_tv);
			appVersionTv = (TextView) view.findViewById(R.id.dil_app_version_tv);
			appDownloadStatus = (TextView) view.findViewById(R.id.dil_app_download_status_tv);
			progressBar = (ProgressBar) view.findViewById(R.id.dil_download_app_pb);
			downloadPauseTv = (TextView) view.findViewById(R.id.dil_download_pause_tv);
			downloadContinueTv = (TextView) view.findViewById(R.id.dil_download_continue_tv);
			downloadInstallTv = (TextView) view.findViewById(R.id.dil_download_install_tv);
			launchTv = (TextView) view.findViewById(R.id.dil_download_launch_tv);
			openTv = (TextView) view.findViewById(R.id.dil_download_open_tv);
			settingTv = (TextView) view.findViewById(R.id.dil_download_setting_tv);

			expandDividerView = view.findViewById(R.id.dil_expand_layout_divider);
			expandDetailView = view.findViewById(R.id.dil_download_detail_tv);
			//Music
			disk_pic = (ImageView) view.findViewById(R.id.mus_disk_pic);
			disk = (ImageView) view.findViewById(R.id.mus_disk);
			loading = (ProgressBar) view.findViewById(R.id.mus_load);
			play_but = (ImageView) view.findViewById(R.id.mus_play);
			duration = (TextView) view.findViewById(R.id.dil_ring_time);
			musicLayout = (RelativeLayout) view.findViewById(R.id.mus_disk_bg);

			hasInited = true;
		}
	}

	public void refreshData(String prompt, String speed, int progress) {
		if (hasInited) {
			if (prompt != null)
				appSizeStatus.setText(prompt);
			if (speed != null)
				appDownloadStatus.setText(speed);
			if (progress != 0)
				progressBar.setProgress(progress);
		}
	}

	public void refreshMerge(Context context) {
		downloadPauseTv.setClickable(false);
		//		appDownloadStatus.setText(context.getResources().getString(R.string.download_action_merge));
	}

	public void initData(DownloadBean downloadBean, Context context) {
		musicLayout.setVisibility(View.INVISIBLE);
		duration.setVisibility(View.GONE);
		appIcon.setVisibility(View.VISIBLE);
		if (MediaType.APP.equals(downloadBean.getMediaType()) || MediaType.GAME.equals(downloadBean.getMediaType())) {
			NetworkImageUtils.load(context, ImageType.NETWORK, downloadBean.getIconUrl(),
					R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIcon);
			appName.setText(downloadBean.getName());
			appVersionTv.setText("V" + downloadBean.getVersionName());
			expandDividerView.setVisibility(View.VISIBLE);
			expandDetailView.setVisibility(View.VISIBLE);
		} else if (MediaType.MUSIC.equals(downloadBean.getMediaType())) {
			NetworkImageUtils.load(context, ImageType.NETWORK, downloadBean.getIconUrl(),
					R.drawable.ringtones_disk_pic, R.drawable.ringtones_disk_pic, disk_pic);
			appName.setText(downloadBean.getName());
			appVersionTv.setVisibility(View.INVISIBLE);
			expandDividerView.setVisibility(View.GONE);
			expandDetailView.setVisibility(View.GONE);
			play_but.setImageResource(R.drawable.ringtones_play);
			disk.clearAnimation();
			disk_pic.clearAnimation();
			loading.setVisibility(View.GONE);
		} else if (MediaType.IMAGE.equals(downloadBean.getMediaType())) {
			if (downloadBean.getStatus() == DownloadTask.TASK_FINISH
					&& StorageUtils.isFileExit(downloadBean.getLocalPath())) {
				NetworkImageUtils.load(context, ImageType.LOCALPIC, downloadBean.getLocalPath(),
						R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIcon);
			} else {
				NetworkImageUtils.load(context, ImageType.NETWORK, downloadBean.getIconUrl(),
						R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIcon);
			}
			appName.setText(downloadBean.getName());
			appVersionTv.setVisibility(View.INVISIBLE);
			expandDividerView.setVisibility(View.GONE);
			expandDetailView.setVisibility(View.GONE);

		} else {
			NetworkImageUtils.load(context, ImageType.NETWORK, downloadBean.getIconUrl(),
					R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIcon);
			appName.setText(downloadBean.getName());
			appVersionTv.setVisibility(View.INVISIBLE);
			expandDividerView.setVisibility(View.GONE);
			expandDetailView.setVisibility(View.GONE);
		}
		refreshDownloadStatus(downloadBean, context);
	}

	public void refreshDownloadStatus(DownloadBean downloadBean, Context context) {
		if (!hasInited)
			return;
		int status = downloadBean.getStatus();
		if (status == DownloadTask.TASK_FINISH) {
			downloadPauseTv.setVisibility(View.INVISIBLE);
			downloadContinueTv.setVisibility(View.INVISIBLE);
			launchTv.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.GONE);
			appVersionTv.setVisibility(View.GONE);
			appDownloadStatus.setVisibility(View.INVISIBLE);
			if (MediaType.APP.equals(downloadBean.getMediaType()) || MediaType.GAME.equals(downloadBean.getMediaType())) {
				downloadInstallTv.setVisibility(View.VISIBLE);
				openTv.setVisibility(View.INVISIBLE);
				settingTv.setVisibility(View.INVISIBLE);
				appSizeStatus.setText(StorageUtils.size(downloadBean.getTotalBytes()) + " | "
						+ downloadBean.getVersionName());
			} else if (MediaType.IMAGE.equals(downloadBean.getMediaType())) {
				downloadInstallTv.setVisibility(View.INVISIBLE);
				openTv.setVisibility(View.VISIBLE);
				settingTv.setVisibility(View.INVISIBLE);
				appSizeStatus.setText(StorageUtils.size(downloadBean.getTotalBytes()));
			} else if (MediaType.MUSIC.equals(downloadBean.getMediaType())) {
				musicLayout.setVisibility(View.VISIBLE);
				appIcon.setVisibility(View.INVISIBLE);
				downloadInstallTv.setVisibility(View.INVISIBLE);
				openTv.setVisibility(View.INVISIBLE);
				settingTv.setVisibility(View.VISIBLE);
				appSizeStatus.setText(StorageUtils.size(downloadBean.getTotalBytes()));
				//						+ " | " + downloadBean.getPalyTime());
			}
			return;
		} else if (status == DownloadTask.TASK_LAUNCH) {
			downloadPauseTv.setVisibility(View.INVISIBLE);
			downloadContinueTv.setVisibility(View.INVISIBLE);
			downloadInstallTv.setVisibility(View.INVISIBLE);
			openTv.setVisibility(View.INVISIBLE);
			settingTv.setVisibility(View.INVISIBLE);
			launchTv.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			appVersionTv.setVisibility(View.GONE);
			appDownloadStatus.setVisibility(View.INVISIBLE);
			appSizeStatus.setText(StorageUtils.size(downloadBean.getTotalBytes()) + " | "
					+ downloadBean.getVersionName());
			return;
		} else if (status == DownloadTask.TASK_DOWNLOADING) {
			downloadPauseTv.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.INVISIBLE);
			appDownloadStatus.setVisibility(View.VISIBLE);
			if (downloadBean.getSpeed() != null)
				appDownloadStatus.setText(downloadBean.getSpeed());
		} else if (status == DownloadTask.TASK_CONNECTING) {
			downloadPauseTv.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.INVISIBLE);
			appDownloadStatus.setVisibility(View.VISIBLE);
			appDownloadStatus.setText(context.getResources().getString(R.string.download_status_connecting));
		} else if (status == DownloadTask.TASK_PAUSE) {
			downloadPauseTv.setVisibility(View.INVISIBLE);
			downloadContinueTv.setVisibility(View.VISIBLE);
			appDownloadStatus.setVisibility(View.VISIBLE);
			appDownloadStatus.setText(context.getResources().getString(R.string.download_status_paused));
		} else if (status == DownloadTask.TASK_WAITING) {
			downloadPauseTv.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.INVISIBLE);
			appDownloadStatus.setVisibility(View.VISIBLE);
			appDownloadStatus.setText(context.getResources().getString(R.string.download_status_waiting));
		}
		downloadInstallTv.setVisibility(View.INVISIBLE);
		launchTv.setVisibility(View.INVISIBLE);
		openTv.setVisibility(View.INVISIBLE);
		settingTv.setVisibility(View.INVISIBLE);
		appSizeStatus.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.VISIBLE);

		if (downloadBean.getMediaType().equals(MediaType.MUSIC)) {
			musicLayout.setVisibility(View.VISIBLE);
			appIcon.setVisibility(View.INVISIBLE);
		}

		appSizeStatus.setText(Utils.sizeFormat(downloadBean.getCurrentBytes()) + " / "
				+ Utils.sizeFormat(downloadBean.getTotalBytes()));
		if (downloadBean.getTotalBytes() == 0) {
			progressBar.setProgress(0);
		} else {
			progressBar.setProgress((int) (downloadBean.getCurrentBytes() * 100 / downloadBean.getTotalBytes()));
		}
	}

	public String refreshDuration(int cur, int dur, Context con) {
		String mdur = Utils.millisTimeToDotFormat(dur);
		String mcur = Utils.millisTimeToDotFormat(cur);
		String bg = con.getResources().getString(R.color.music_dur_bg);
		String from = "<font color=\"" + bg + "\">" + mcur + "</font>" + "/" + mdur;
		return from;
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		if (SkinConstan.skinEnabled) {
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, downloadContinueTv,
					SkinConstan.CONTINUE_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, downloadPauseTv, SkinConstan.PAUSE_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, downloadInstallTv, SkinConstan.INSTALL_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, launchTv, SkinConstan.LAUNCH_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, openTv, SkinConstan.LAUNCH_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, settingTv, SkinConstan.SETTINGS_BTN);
			SkinConfigManager.getInstance().setProgressDrawable(context, progressBar, SkinConstan.DOWNLOAD_PROGRESS_BG);
			SkinConfigManager.getInstance().setViewBackground(context, rootView, SkinConstan.LIST_VIEW_ITEM_BG);
		}
	}
}
