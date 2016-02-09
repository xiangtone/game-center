package com.x.ui.adapter.gridview;

import java.io.Serializable;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.AppInfoBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.Utils;
import com.x.ui.view.RoundProgress;

/**
 * @ClassName: AppGridViewHolder
 * @Desciption: TODO
 
 * @Date: 2014-3-6 下午5:49:36
 */

public class AppGridViewHolder implements Serializable {

	private static final long serialVersionUID = 3499149198799009718L;
	private boolean hasInited;
	public ImageView appIconIv;
	public TextView appNameTv, appSizeTv, appCommentTv;
	public RatingBar ratingBar;
	public TextView appDownloadBtn, appContinueBtn, appUpgradeBtn, appLaunchBtn, appInstallBtn;
	public RoundProgress downloadPercentPv;
	public AnimationDrawable downloadAnimation;

	public AppGridViewHolder(View view) {
		if (view != null) {
			appIconIv = (ImageView) view.findViewById(R.id.gl_app_icon);
			appNameTv = (TextView) view.findViewById(R.id.gl_app_name);
			appSizeTv = (TextView) view.findViewById(R.id.gl_app_size);
			ratingBar = (RatingBar) view.findViewById(R.id.gl_app_ratingBar);
			appCommentTv = (TextView) view.findViewById(R.id.gl_app_comment);
			appDownloadBtn = (TextView) view.findViewById(R.id.gl_app_download_btn);

			downloadPercentPv = (RoundProgress) view.findViewById(R.id.gl_app_pause_rp);
			downloadPercentPv.setBackgroundResource(R.anim.download);
			downloadAnimation = (AnimationDrawable) downloadPercentPv.getBackground();

			appContinueBtn = (TextView) view.findViewById(R.id.gl_app_continue_btn);
			appUpgradeBtn = (TextView) view.findViewById(R.id.gl_app_upgrade_btn);
			appLaunchBtn = (TextView) view.findViewById(R.id.gl_app_launch_btn);
			appInstallBtn = (TextView) view.findViewById(R.id.gl_app_install_btn);
			hasInited = true;
		}
	}

	public void initData(AppInfoBean appInfoBean, Context context) {

		long totalBytes = appInfoBean.getTotalBytes();
		long currentBytes = appInfoBean.getCurrentBytes();
		String percent = null;
		if (totalBytes != 0)
			percent = (currentBytes * 100 / totalBytes) + "";
		refreshAppStatus(appInfoBean.getStatus(), context, percent);
		NetworkImageUtils.load(context, ImageType.NETWORK, appInfoBean.getLogo(), R.drawable.ic_screen_default_picture,
				R.drawable.ic_screen_default_picture, appIconIv);
		appNameTv.setText(appInfoBean.getAppName());
		appSizeTv.setText(Utils.sizeFormat(appInfoBean.getFileSize()));
		ratingBar.setRating(appInfoBean.getStars());
		appCommentTv.setText(appInfoBean.getBrief());
	}

	public void refreshMerge() {
		if (!hasInited)
			return;
		downloadPercentPv.setClickable(false);
	}

	public void refreshInstalling() {
		if (!hasInited)
			return;
		appInstallBtn.setClickable(false);
		appInstallBtn.setText(R.string.app_status_installing);
	}

	public void refreshInstall() {
		if (!hasInited)
			return;
		appInstallBtn.setClickable(true);
		appInstallBtn.setText(R.string.app_status_install);
	}

	public void refreshDownloadStatus(int status, Context context, String percent) {
		if (!hasInited)
			return;
		refreshAppStatus(status, context, percent);
	}

	public void refreshAppStatus(int status, Context context, String percent) {
		if (!hasInited)
			return;
		switch (status) {
		case AppInfoBean.Status.NORMAL:
			appDownloadBtn.setVisibility(View.VISIBLE);
			downloadPercentPv.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.WAITING:
			appDownloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setVisibility(View.VISIBLE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			downloadPercentPv.setProgress(RoundProgress.SPECIAL_PROGRESS);
			startDownloadAnimation();
			break;
		case AppInfoBean.Status.CONNECTING:
			appDownloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setVisibility(View.VISIBLE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			downloadPercentPv.setProgress(RoundProgress.SPECIAL_PROGRESS);
			startDownloadAnimation();
			break;
		case AppInfoBean.Status.DOWNLOADING:
			appDownloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setVisibility(View.VISIBLE);
			if (percent != null) {
				downloadPercentPv.setProgress(Integer.valueOf(percent));
			}
			startDownloadAnimation();
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.PAUSED:
			appDownloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.VISIBLE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			stopDownloadAnimation();
			break;
		case AppInfoBean.Status.CANUPGRADE:
			appDownloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.VISIBLE);
			appInstallBtn.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.CANINSTALL:
			appDownloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.VISIBLE);
			stopDownloadAnimation();
			break;
		case AppInfoBean.Status.INSTALLING:
			appDownloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.VISIBLE);
			appInstallBtn.setClickable(false);
			appInstallBtn.setText(R.string.app_status_installing);
			stopDownloadAnimation();
			break;

		case AppInfoBean.Status.CANLAUNCH:
			appDownloadBtn.setVisibility(View.GONE);
			downloadPercentPv.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.VISIBLE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private void startDownloadAnimation() {
		if (downloadAnimation == null) {
			downloadAnimation = (AnimationDrawable) downloadPercentPv.getBackground();
		}
		if (downloadAnimation.isRunning()) {
			return;
		}
		downloadAnimation.start();
	}

	private void stopDownloadAnimation() {
		if (downloadAnimation != null && downloadAnimation.isRunning()) {
			downloadAnimation.stop();
			Utils.setBackgroundDrawable(downloadPercentPv, null);
			downloadPercentPv.setBackgroundResource(R.anim.download);
			downloadAnimation = null;
		}
	}

	/** 
	* @param context 
	 * @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		if (SkinConstan.skinEnabled) {
			SkinConfigManager.getInstance().setViewBackground(context, appLaunchBtn, SkinConstan.LAUNCH_BTN);
			SkinConfigManager.getInstance().setViewBackground(context, appInstallBtn, SkinConstan.INSTALL_BTN);
			SkinConfigManager.getInstance().setViewBackground(context, appUpgradeBtn, SkinConstan.UPGRADE_BTN);
			SkinConfigManager.getInstance().setViewBackground(context, appDownloadBtn, SkinConstan.DOWNLOAD_BTN);
			SkinConfigManager.getInstance().setViewBackground(context, appContinueBtn, SkinConstan.CONTINUE_BTN);

			// RoundProgress Color
			SkinConfigManager.getInstance().setRoundProgressColor(context, downloadPercentPv,
					SkinConstan.ROUND_PROGRESS_COLOR);
		}
	}
}
