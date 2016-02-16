/**   
* @Title: DownloadView java
* @Package com.yimibox.appstore.adapter
* @Description: TODO 

* @date 2013-11-14 上午09:51:51
* @version V1.0   
*/

package com.x.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;

import java.io.Serializable;

/**
* @ClassName: LocalAppListViewHolder
* @Description: 本地应用适配器viewHolder 

* @date 2014-1-16 下午04:49:01
* 
*/

public class UpdateAppListViewHolder implements Serializable {

	private static final long serialVersionUID = -1749951969451779511L;
	public static final int KEY_URL = 0;
	public static final int KEY_SPEED = 1;
	public static final int KEY_PROGRESS = 2;
	public static final int KEY_DOWNLOAD_PROMPT = 3;

	public ImageView appIconIv, arrowIv;
	public TextView appNameTv, appVersionTv, appSizeTv, appNewVersionTv, appPatchSizeTv;
	public TextView appDownloadSizeTv, appDownloadStatusTv;
	public ProgressBar percentPb;
	public View appDownloadStatusView, appSizeView;
	public TextView downloadPauseTv, downloadContinueTv, upgradeTv, installTv;
	public ImageView upgradeLineIv;
	public boolean hasInited;
	public LinearLayout llytIsShow;

	public UpdateAppListViewHolder(View view) {
		if (view != null) {
			appIconIv = (ImageView) view.findViewById(R.id.uail_app_icon_iv);
			arrowIv = (ImageView) view.findViewById(R.id.uail_arrow_iv);
			appNameTv = (TextView) view.findViewById(R.id.uail_app_name_tv);
			appNewVersionTv = (TextView) view.findViewById(R.id.uail_app_new_version);
			appVersionTv = (TextView) view.findViewById(R.id.uail_app_version_tv);
			appSizeView = view.findViewById(R.id.uail_size_ll);
			appSizeTv = (TextView) view.findViewById(R.id.uail_app_size_tv);
			appPatchSizeTv = (TextView) view.findViewById(R.id.uail_patch_size_tv);
			upgradeLineIv = (ImageView) view.findViewById(R.id.uail_patch_upgrade_line_iv);
			appDownloadSizeTv = (TextView) view.findViewById(R.id.uail_app_download_size_tv);
			appDownloadStatusTv = (TextView) view.findViewById(R.id.uail_app_download_status_tv);
			appDownloadStatusView = view.findViewById(R.id.uail_app_download_rl);
			percentPb = (ProgressBar) view.findViewById(R.id.uail_download_app_pb);

			downloadPauseTv = (TextView) view.findViewById(R.id.uail_download_pause_tv);
			downloadContinueTv = (TextView) view.findViewById(R.id.uail_download_continue_tv);
			upgradeTv = (TextView) view.findViewById(R.id.uail_upgrade_tv);
			installTv = (TextView) view.findViewById(R.id.uail_download_install_tv);
			hasInited = true;
		}
	}

	public void initData(InstallAppBean infoBean, Context context) {
		NetworkImageUtils.load(context, ImageType.APP, infoBean.getPackageName(),
				R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIconIv);
		appNameTv.setText(infoBean.getAppName());
		appVersionTv.setText(Html.fromHtml(ResourceUtil.getString(context, R.string.app_version) + infoBean.getOldVersionName() + "<font color=#FF3030> -->"
				+ infoBean.getVersionName() + "</font>"));
		appNewVersionTv.setText(infoBean.getVersionName());
		appSizeTv.setText(infoBean.getFileSize() == 0 ? "" : Utils.sizeFormat(Long.valueOf(infoBean.getFileSize())));
		if (infoBean.getIsPatch() && infoBean.getPatchSize() != 0) {
			appPatchSizeTv.setVisibility(View.VISIBLE);
			appPatchSizeTv.setText("-->" + Utils.sizeFormat(infoBean.getPatchSize()));
			upgradeLineIv.setVisibility(View.VISIBLE);
		} else {
			appPatchSizeTv.setVisibility(View.GONE);
			upgradeLineIv.setVisibility(View.GONE);
		}
		refreshAppStatus(infoBean, context);
	}

	public void refreshMerge(Context context) {
//		downloadPauseTv.setText(context.getResources().getString(R.string.download_action_merge));
		downloadPauseTv.setClickable(false);
	}

	public void refreshDownloadPercent(String prompt, String speed, int progress) {
		if (hasInited) {
			if (prompt != null)
				appDownloadSizeTv.setText(prompt);
			if (speed != null)
				appDownloadStatusTv.setText(speed);
			if (progress != 0)
				percentPb.setProgress(progress);
		}
	}

	public void refreshAppStatus(InstallAppBean installAppBean, Context context) {
		if (!hasInited || installAppBean == null)
			return;
		int status = installAppBean.getStatus();
		if (status == AppInfoBean.Status.CANUPGRADE) {
			appVersionTv.setVisibility(View.VISIBLE);
			appSizeView.setVisibility(View.VISIBLE);
			appDownloadStatusView.setVisibility(View.GONE);
			percentPb.setVisibility(View.GONE);
			appNewVersionTv.setVisibility(View.GONE);

			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.VISIBLE);
			downloadPauseTv.setVisibility(View.GONE);
			downloadContinueTv.setVisibility(View.GONE);
			return;
		} else if (status == AppInfoBean.Status.CANINSTALL) {
			appVersionTv.setVisibility(View.VISIBLE);
			appSizeView.setVisibility(View.VISIBLE);
			appDownloadStatusView.setVisibility(View.GONE);
			percentPb.setVisibility(View.GONE);
			appNewVersionTv.setVisibility(View.GONE);
			installTv.setVisibility(View.VISIBLE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.GONE);
			downloadContinueTv.setVisibility(View.GONE);
			return;
		} else if (status == AppInfoBean.Status.WAITING) {
			appVersionTv.setVisibility(View.GONE);
			appSizeView.setVisibility(View.GONE);
			appDownloadStatusView.setVisibility(View.VISIBLE);
			percentPb.setVisibility(View.VISIBLE);
			appNewVersionTv.setVisibility(View.VISIBLE);
			appDownloadStatusTv.setText(context.getResources().getString(R.string.download_status_waiting));

			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.GONE);
		} else if (status == AppInfoBean.Status.CONNECTING) {
			appVersionTv.setVisibility(View.GONE);
			appSizeView.setVisibility(View.GONE);
			appDownloadStatusView.setVisibility(View.VISIBLE);
			percentPb.setVisibility(View.VISIBLE);
			appNewVersionTv.setVisibility(View.VISIBLE);
			appDownloadStatusTv.setText(context.getResources().getString(R.string.download_status_connecting));

			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.GONE);
		} else if (status == AppInfoBean.Status.DOWNLOADING) {
			appVersionTv.setVisibility(View.GONE);
			appSizeView.setVisibility(View.GONE);
			appDownloadStatusView.setVisibility(View.VISIBLE);
			percentPb.setVisibility(View.VISIBLE);
			appNewVersionTv.setVisibility(View.VISIBLE);
			if (installAppBean.getSpeed() != null)
				appDownloadStatusTv.setText(installAppBean.getSpeed());
			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.GONE);
		} else if (status == AppInfoBean.Status.PAUSED) {
			appVersionTv.setVisibility(View.GONE);
			appSizeView.setVisibility(View.GONE);
			appDownloadStatusView.setVisibility(View.VISIBLE);
			percentPb.setVisibility(View.VISIBLE);
			appNewVersionTv.setVisibility(View.VISIBLE);
			appDownloadStatusTv.setText(context.getResources().getString(R.string.download_status_paused));

			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.GONE);
			downloadContinueTv.setVisibility(View.VISIBLE);
		}

		appDownloadSizeTv.setText(Utils.sizeFormat(installAppBean.getCurrentBytes()) + " / "
				+ Utils.sizeFormat(installAppBean.getTotalBytes()));
		if (installAppBean.getTotalBytes() == 0) {
			percentPb.setProgress(0);
		} else {
			percentPb.setProgress((int) (installAppBean.getCurrentBytes() * 100 / installAppBean.getTotalBytes()));
		}

	}

}
