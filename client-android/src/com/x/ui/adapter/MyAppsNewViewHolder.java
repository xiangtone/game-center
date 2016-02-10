/**   
* @Title: MyAppsNewViewHolder.java
* @Package com.x.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-7 下午4:52:00
* @version V1.0   
*/

package com.x.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.MyAppsBean;
import com.x.publics.model.MyAppsBean.ListType;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;

/**
* @ClassName: MyAppsNewViewHolder
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-7-7 下午4:52:00
* 
*/

public class MyAppsNewViewHolder {

	public View catalogRl;
	public TextView catalogTypeTv, catalogNumTv;
	public ImageView appIconIv, arrowIv;
	public TextView appNameTv, appVersionTv, appSizeTv, appNewVersionTv, appPatchSizeTv;
	public TextView appDownloadSizeTv, appDownloadStatusTv;
	public ProgressBar percentPb;
	public View appDownloadStatusView, appSizeView;
	public TextView downloadPauseTv, downloadContinueTv, upgradeTv, installTv;
	public ImageView upgradeLineIv;
	public ImageView favoriteIv;
	public View updateExpandView, preUpdateExpandView, downloadExpandView;
	public boolean hasInited;

	public MyAppsNewViewHolder(View view) {
		if (view != null) {
			catalogRl = view.findViewById(R.id.man_catalog_rl);
			catalogTypeTv = (TextView) view.findViewById(R.id.man_catalog_tv);
			catalogNumTv = (TextView) view.findViewById(R.id.man_catalog_num_tv);

			appIconIv = (ImageView) view.findViewById(R.id.man_app_icon_iv);
			arrowIv = (ImageView) view.findViewById(R.id.man_arrow_iv);
			appNameTv = (TextView) view.findViewById(R.id.man_app_name_tv);
			appNewVersionTv = (TextView) view.findViewById(R.id.man_app_new_version);
			appVersionTv = (TextView) view.findViewById(R.id.man_app_version_tv);
			appSizeView = view.findViewById(R.id.man_size_ll);
			appSizeTv = (TextView) view.findViewById(R.id.man_app_size_tv);
			appPatchSizeTv = (TextView) view.findViewById(R.id.man_patch_size_tv);
			upgradeLineIv = (ImageView) view.findViewById(R.id.man_patch_upgrade_line_iv);
			appDownloadSizeTv = (TextView) view.findViewById(R.id.man_app_download_size_tv);
			appDownloadStatusTv = (TextView) view.findViewById(R.id.man_app_download_status_tv);
			appDownloadStatusView = view.findViewById(R.id.man_app_download_rl);
			percentPb = (ProgressBar) view.findViewById(R.id.man_download_app_pb);

			updateExpandView = view.findViewById(R.id.man_update_app_expand_ll);
			downloadExpandView = view.findViewById(R.id.man_download_app_expand_ll);
			preUpdateExpandView = view.findViewById(R.id.man_update_pre_app_expand_ll);

			downloadPauseTv = (TextView) view.findViewById(R.id.man_download_pause_tv);
			downloadContinueTv = (TextView) view.findViewById(R.id.man_download_continue_tv);
			upgradeTv = (TextView) view.findViewById(R.id.man_upgrade_tv);
			installTv = (TextView) view.findViewById(R.id.man_download_install_tv);

			favoriteIv = (ImageView) view.findViewById(R.id.man_app_favorite_iv);

			hasInited = true;
		}
	}

	public void refreshMerge(Context context) {
		downloadPauseTv.setClickable(false);
	}

	public void refreshInstalling() {
		if (!hasInited)
			return;
		installTv.setClickable(false);
		installTv.setText(R.string.app_status_installing);
	}

	public void refreshInstall() {
		if (!hasInited)
			return;
		installTv.setClickable(true);
		installTv.setText(R.string.app_status_install);
	}

	public void initData(MyAppsBean appsBean, Context context) {
		if (appsBean.getStatus() == AppInfoBean.Status.CANUPGRADE) {
			NetworkImageUtils.load(context, ImageType.APP, appsBean.getPackageName(),
					R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIconIv);
		} else {
			NetworkImageUtils.load(context, ImageType.NETWORK, appsBean.getLogo(),
					R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIconIv);
		}
		appNameTv.setText(appsBean.getAppName());
		appNewVersionTv.setText(appsBean.getVersionName());
		appSizeTv.setText(appsBean.getFileSize() == 0 ? "" : Utils.sizeFormat(Long.valueOf(appsBean.getFileSize())));
		if (appsBean.getFavoriteId() != 0) {
			favoriteIv.setVisibility(View.VISIBLE);
		} else {
			favoriteIv.setVisibility(View.GONE);
		}
		if (appsBean.getIsPatch() && appsBean.getPatchSize() != 0) {
			appPatchSizeTv.setVisibility(View.VISIBLE);
			appPatchSizeTv.setText("-->" + Utils.sizeFormat(appsBean.getPatchSize()));
			upgradeLineIv.setVisibility(View.VISIBLE);
		} else {
			appPatchSizeTv.setVisibility(View.GONE);
			upgradeLineIv.setVisibility(View.GONE);
		}
		if (appsBean.getListType() == ListType.UPDATE) {
			if (appsBean.getSysFlag() == 1) {
				preUpdateExpandView.setVisibility(View.VISIBLE);
				updateExpandView.setVisibility(View.GONE);
			} else {
				preUpdateExpandView.setVisibility(View.GONE);
				updateExpandView.setVisibility(View.VISIBLE);
			}
			downloadExpandView.setVisibility(View.GONE);
			appVersionTv
					.setText(Html.fromHtml(ResourceUtil.getString(context, R.string.app_version)
							+ appsBean.getOldVersionName() + "<font color=#FF3030> -->" + appsBean.getVersionName()
							+ "</font>"));
		} else {
			preUpdateExpandView.setVisibility(View.GONE);
			updateExpandView.setVisibility(View.GONE);
			downloadExpandView.setVisibility(View.VISIBLE);
			appVersionTv.setText(Html.fromHtml(ResourceUtil.getString(context, R.string.app_version)
					+ appsBean.getVersionName()));
		}
		refreshAppStatus(appsBean, context);
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

	public void refreshAppStatus(MyAppsBean appsBean, Context context) {
		if (!hasInited || appsBean == null)
			return;
		int status = appsBean.getStatus();
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
			installTv.setClickable(true);
			installTv.setText(R.string.app_status_install);
			return;
		} else if (status == AppInfoBean.Status.INSTALLING) {
			appVersionTv.setVisibility(View.VISIBLE);
			appSizeView.setVisibility(View.VISIBLE);
			appDownloadStatusView.setVisibility(View.GONE);
			percentPb.setVisibility(View.GONE);
			appNewVersionTv.setVisibility(View.GONE);
			installTv.setVisibility(View.VISIBLE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.GONE);
			downloadContinueTv.setVisibility(View.GONE);
			installTv.setClickable(false);
			installTv.setText(R.string.app_status_installing);
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
			if (appsBean.getSpeed() != null)
				appDownloadStatusTv.setText(appsBean.getSpeed());
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

		appDownloadSizeTv.setText(Utils.sizeFormat(appsBean.getCurrentBytes()) + " / "
				+ Utils.sizeFormat(appsBean.getTotalBytes()));
		if (appsBean.getTotalBytes() == 0) {
			percentPb.setProgress(0);
		} else {
			percentPb.setProgress((int) (appsBean.getCurrentBytes() * 100 / appsBean.getTotalBytes()));
		}

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
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, installTv, SkinConstan.INSTALL_BTN);
			SkinConfigManager.getInstance().setTextViewDrawableTop(context, upgradeTv, SkinConstan.UPGRADE_BTN);
			SkinConfigManager.getInstance().setProgressDrawable(context, percentPb, SkinConstan.DOWNLOAD_PROGRESS_BG);
		}

	}
}
