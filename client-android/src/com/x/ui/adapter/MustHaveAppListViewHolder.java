/**   
* @Title: AppListViewHolder.java
* @Package com.mas.amineappstore.adapter
* @Description: TODO 

* @date 2013-12-23 上午10:30:31
* @version V1.0   
*/

package com.x.ui.adapter;

import java.io.Serializable;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
* @ClassName: AppListViewHolder
* @Description: AppListAdapter 的ViewHolder 

* @date 2013-12-23 上午10:30:31
* 
*/

public class MustHaveAppListViewHolder implements Serializable {

	private static final long serialVersionUID = -5203245468516308848L;

	private boolean hasInited;
	public ImageView appIconIv;
	public TextView appNameTv, appSizeTv, appCommentTv;
	public RatingBar ratingBar;
	public LinearLayout catalogLl;
	public TextView catalogTv;
	public TextView appDownloadBtn, appPauseBtn, appContinueBtn, appUpgradeBtn, appLaunchBtn, appInstallBtn;
	public TextView appPatchSizeTv;
	public ImageView upgradeLineIv;
	public RoundProgress downloadPercentPv;
	public View appPauseView, dividerBar;
	public AnimationDrawable downloadAnimation;

	public MustHaveAppListViewHolder(View view) {
		if (view != null) {
//			catalogLl = (LinearLayout) view.findViewById(R.id.hmhi_catalog_ll);
//			dividerBar = view.findViewById(R.id.divider_bar);
//			catalogTv = (TextView) view.findViewById(R.id.hmhi_catalog_tv);
			appIconIv = (ImageView) view.findViewById(R.id.hmhi_app_icon);
			appNameTv = (TextView) view.findViewById(R.id.hmhi_app_name);
			appSizeTv = (TextView) view.findViewById(R.id.hmhi_app_size);
			ratingBar = (RatingBar) view.findViewById(R.id.hmhi_app_ratingBar);
			appCommentTv = (TextView) view.findViewById(R.id.hmhi_app_comment);
			appPatchSizeTv = (TextView) view.findViewById(R.id.hmhi_app_patch_size);
			upgradeLineIv = (ImageView) view.findViewById(R.id.hmhi_patch_upgrade_line_iv);
			//			appPercentTv = (TextView) view.findViewById(R.id.gil_app_icon_percent_tv);

			appDownloadBtn = (TextView) view.findViewById(R.id.hmhi_app_download_btn);

			appPauseView = view.findViewById(R.id.hmhi_app_pause_ll);
			appPauseBtn = (TextView) view.findViewById(R.id.hmhi_app_pause_btn);
			downloadPercentPv = (RoundProgress) view.findViewById(R.id.hmhi_app_pause_rp);
			downloadPercentPv.setBackgroundResource(R.anim.download);
			downloadAnimation = (AnimationDrawable) downloadPercentPv.getBackground();

			appContinueBtn = (TextView) view.findViewById(R.id.hmhi_app_continue_btn);
			appUpgradeBtn = (TextView) view.findViewById(R.id.hmhi_app_upgrade_btn);
			appLaunchBtn = (TextView) view.findViewById(R.id.hmhi_app_launch_btn);
			appInstallBtn = (TextView) view.findViewById(R.id.hmhi_app_install_btn);
			hasInited = true;
		}
	}

	public void initData(AppInfoBean appInfoBean, Context context) {
		NetworkImageUtils.load(context, ImageType.NETWORK, appInfoBean.getLogo(), R.drawable.ic_screen_default_picture,
				R.drawable.ic_screen_default_picture, appIconIv);
		appNameTv.setText(appInfoBean.getAppName());
		appSizeTv.setText(Utils.sizeFormat(appInfoBean.getFileSize()));
		if (appInfoBean.isPatch() && appInfoBean.getPatchFileSize() != 0) {
			appPatchSizeTv.setText(Utils.sizeFormat(appInfoBean.getPatchFileSize()));
			appPatchSizeTv.setVisibility(View.VISIBLE);
			upgradeLineIv.setVisibility(View.VISIBLE);
		} else {
			appPatchSizeTv.setVisibility(View.GONE);
			upgradeLineIv.setVisibility(View.GONE);
		}
		ratingBar.setRating(appInfoBean.getStars());
		appCommentTv.setText(appInfoBean.getBrief());
		long totalBytes = appInfoBean.getTotalBytes();
		long currentBytes = appInfoBean.getCurrentBytes();
		String percent = null;
		if (totalBytes != 0)
			percent = (currentBytes * 100 / totalBytes) + "";
		refreshAppStatus(appInfoBean.getStatus(), context, percent);
	}

	public void refreshPatchUpdate(AppInfoBean appInfoBean) {
		if (!hasInited || appInfoBean == null)
			return;
		appPatchSizeTv.setText(Utils.sizeFormat(appInfoBean.getPatchFileSize()));
		appPatchSizeTv.setVisibility(View.VISIBLE);
		upgradeLineIv.setVisibility(View.VISIBLE);
	}

	public void refreshMerge(Context context) {
		if (!hasInited)
			return;
		appPauseBtn.setClickable(false);
	}

	public void refreshInstalling(Context context) {
		if (!hasInited)
			return;
		appInstallBtn.setClickable(false);
		appInstallBtn.setText(R.string.app_status_installing);
	}

	public void refreshInstall(Context context) {
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
			appPauseView.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.WAITING:
			appDownloadBtn.setVisibility(View.GONE);
			appPauseView.setVisibility(View.VISIBLE);
			appPauseBtn.setText(context.getResources().getString(R.string.app_status_waiting));
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			downloadPercentPv.setProgress(RoundProgress.SPECIAL_PROGRESS);
			startDownloadAnimation();
			break;
		case AppInfoBean.Status.CONNECTING:
			appDownloadBtn.setVisibility(View.GONE);
			appPauseView.setVisibility(View.VISIBLE);
			appPauseBtn.setText(context.getResources().getString(R.string.app_status_connecting));
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			downloadPercentPv.setProgress(RoundProgress.SPECIAL_PROGRESS);
			startDownloadAnimation();
			break;
		case AppInfoBean.Status.DOWNLOADING:
			appDownloadBtn.setVisibility(View.GONE);
			appPauseView.setVisibility(View.VISIBLE);
			downloadPercentPv.setVisibility(View.VISIBLE);
			if (percent != null) {
				downloadPercentPv.setProgress(Integer.valueOf(percent));
			}
			startDownloadAnimation();
			appPauseBtn.setText(context.getResources().getString(R.string.app_status_pause));
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.PAUSED:
			appDownloadBtn.setVisibility(View.GONE);
			appPauseView.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.VISIBLE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.GONE);
			stopDownloadAnimation();
			break;
		case AppInfoBean.Status.CANUPGRADE:
			appDownloadBtn.setVisibility(View.GONE);
			appPauseView.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.VISIBLE);
			appInstallBtn.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.CANINSTALL:
			appDownloadBtn.setVisibility(View.GONE);
			appPauseView.setVisibility(View.GONE);
			appContinueBtn.setVisibility(View.GONE);
			appLaunchBtn.setVisibility(View.GONE);
			appUpgradeBtn.setVisibility(View.GONE);
			appInstallBtn.setVisibility(View.VISIBLE);
			stopDownloadAnimation();
			break;
		case AppInfoBean.Status.INSTALLING:
			appDownloadBtn.setVisibility(View.GONE);
			appPauseView.setVisibility(View.GONE);
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
			appPauseView.setVisibility(View.GONE);
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
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, appLaunchBtn, SkinConstan.LAUNCH_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, appInstallBtn, SkinConstan.INSTALL_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, appUpgradeBtn, SkinConstan.UPGRADE_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, appDownloadBtn, SkinConstan.DOWNLOAD_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, appContinueBtn, SkinConstan.CONTINUE_BTN);
//			SkinConfigManager.getInstance().setViewBackground(context, dividerBar, SkinConstan.DIVIDER_BAR);

			// RoundProgress Color
			SkinConfigManager.getInstance().setRoundProgressColor(context, downloadPercentPv,
					SkinConstan.ROUND_PROGRESS_COLOR);
		}
	}

}
