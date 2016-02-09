/**   
* @Title: DownloadView java
* @Package com.yimibox.appstore.adapter
* @Description: TODO 

* @date 2013-11-14 上午09:51:51
* @version V1.0   
*/

package com.x.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.favorite.FavoriteManage;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.FavoriteAppBean;
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

public class FavoriteAppViewHolder implements Serializable {

	private static final long serialVersionUID = -1749951969451779513L;
	public static final int KEY_URL = 0;
	public static final int KEY_SPEED = 1;
	public static final int KEY_PROGRESS = 2;
	public static final int KEY_DOWNLOAD_PROMPT = 3;

	public ImageView appIconIv, arrowIv;
	public TextView appNameTv, appVersionTv, appSizeTv, appNewVersionTv, appPatchSizeTv;
	public TextView appDownloadSizeTv, appDownloadSattusTv;
	public ProgressBar percentPb;
	public View appDownloadStatusView, cancelAppTv, detailAppTv, appSizeView;
	public TextView downloadPauseTv, downloadContinueTv, upgradeTv, installTv, launchTv, downloadTv;
	public boolean hasInited;
	public LinearLayout llytIsShow;
	public ImageView upgradeLineIv;

	public FavoriteAppViewHolder(View view) {
		if (view != null) {
			appIconIv = (ImageView) view.findViewById(R.id.favo_app_icon_iv);
			arrowIv = (ImageView) view.findViewById(R.id.favo_arrow_iv);
			appNameTv = (TextView) view.findViewById(R.id.favo_app_name_tv);
			appNewVersionTv = (TextView) view.findViewById(R.id.favo_app_new_version);
			appVersionTv = (TextView) view.findViewById(R.id.favo_app_version_tv);
			appSizeTv = (TextView) view.findViewById(R.id.favo_app_size_tv);
			appPatchSizeTv = (TextView) view.findViewById(R.id.favo_app_patch_size_tv);
			upgradeLineIv = (ImageView) view.findViewById(R.id.favo_patch_upgrade_line_iv);
			appSizeView = view.findViewById(R.id.favo_size_ll);
			appDownloadSizeTv = (TextView) view.findViewById(R.id.favo_app_download_size_tv);
			appDownloadSattusTv = (TextView) view.findViewById(R.id.favo_app_download_status_tv);
			appDownloadStatusView = view.findViewById(R.id.favo_app_download_rl);
			percentPb = (ProgressBar) view.findViewById(R.id.favo_download_app_pb);

			downloadPauseTv = (TextView) view.findViewById(R.id.favo_download_pause_tv);
			downloadContinueTv = (TextView) view.findViewById(R.id.favo_download_continue_tv);
			upgradeTv = (TextView) view.findViewById(R.id.favo_upgrade_tv);
			installTv = (TextView) view.findViewById(R.id.favo_download_install_tv);
			detailAppTv = view.findViewById(R.id.favo_detail_app_tv);
			cancelAppTv = view.findViewById(R.id.favo_cancel_app_tv);
			launchTv = (TextView) view.findViewById(R.id.favo_launch_tv);
			downloadTv = (TextView) view.findViewById(R.id.favo_app_download_tv);
			hasInited = true;
		}
	}

	public void initData(FavoriteAppBean favoriteAppBean, Context context) {
		NetworkImageUtils.load(context, ImageType.NETWORK, favoriteAppBean.getFavoriteIconUrl(),
				R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIconIv);
		appNameTv.setText(favoriteAppBean.getFavoriteAppName());
		boolean isUpdate = favoriteAppBean.getFavoriteStatus() == AppInfoBean.Status.CANUPGRADE ? true : false;
		if (isUpdate)
			appVersionTv.setText(FavoriteManage.getInstance(context).getVersionTips(context,
					favoriteAppBean.getFavoritePackageName()));
		else
			appVersionTv.setText(ResourceUtil.getString(context, R.string.app_version)
					+ favoriteAppBean.getFavoriteVersionName());
		appNewVersionTv.setText("V" + favoriteAppBean.getFavoriteVersionName());
		appSizeTv.setText(favoriteAppBean.getFavoriteSize() == null ? "" : Utils.sizeFormat(Long
				.valueOf(favoriteAppBean.getFavoriteSize())));
		if (favoriteAppBean.isPatch && favoriteAppBean.getPatchSize() != 0) {
			appPatchSizeTv.setText("-->" + Utils.sizeFormat(favoriteAppBean.getPatchSize()));
			appPatchSizeTv.setVisibility(View.VISIBLE);
			upgradeLineIv.setVisibility(View.VISIBLE);
		} else {
			appPatchSizeTv.setVisibility(View.GONE);
			upgradeLineIv.setVisibility(View.GONE);
		}
		refreshAppStatus(favoriteAppBean.getFavoriteStatus(), context);
		initDownloadPercent(context, favoriteAppBean);

	}

	public void refreshMerge(Context context) {
		//		downloadPauseTv.setText(context.getResources().getString(R.string.download_action_merge));
		downloadPauseTv.setClickable(false);
	}

	public void refreshPatchUpdate(FavoriteAppBean favoriteAppBean) {
		if(favoriteAppBean!=null && hasInited)
		{
			appPatchSizeTv.setText(Utils.sizeFormat(favoriteAppBean.getPatchSize()));
			appPatchSizeTv.setVisibility(View.VISIBLE);
			upgradeLineIv.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化下载进度
	 * @param favoriteAppBean
	 */
	private void initDownloadPercent(Context mContext, FavoriteAppBean favoriteAppBean) {
		int status = favoriteAppBean.getFavoriteStatus();
		String speed = mContext.getResources().getString(R.string.download_status_paused);
		if (favoriteAppBean.getResourceId() > 0) {
			switch (status) {
			case AppInfoBean.Status.PAUSED:
				speed = mContext.getResources().getString(R.string.download_status_paused);
				break;
			case AppInfoBean.Status.DOWNLOADING:
				speed = favoriteAppBean.getSpeed();
				break;
			case AppInfoBean.Status.CANLAUNCH:
				speed = mContext.getResources().getString(R.string.app_status_launch);
				break;
			case AppInfoBean.Status.WAITING:
				speed = mContext.getResources().getString(R.string.download_status_waiting);
				break;
			case AppInfoBean.Status.CONNECTING:
				speed = mContext.getResources().getString(R.string.download_status_connecting);
				break;
			default:
				break;
			}
			String prompt = Utils.sizeFormat(favoriteAppBean.getCurrentBytes()) + " / "
					+ Utils.sizeFormat(favoriteAppBean.getTotalBytes());
			int progress = 0;

			if (favoriteAppBean.getTotalBytes() > 0) {
				progress = (int) (favoriteAppBean.getCurrentBytes() * 100 / favoriteAppBean.getTotalBytes());
			}

			refreshDownloadPercent(prompt, speed, progress);
		}
	}

	public void refreshDownloadPercent(String prompt, String speed, int progress) {
		if (hasInited) {
			if (prompt != null)
				appDownloadSizeTv.setText(prompt);
			if (speed != null)
				appDownloadSattusTv.setText(speed);
			percentPb.setProgress(progress);
		}
	}

	public void notifyUpgradeSuccess(String versionName, String newSize) {
		appVersionTv.setText(versionName);
		appSizeTv.setText(Utils.sizeFormat(Long.valueOf(newSize)));
	}

	public void refreshAppStatus(int status, Context context) {
		switch (status) {
		case AppInfoBean.Status.NORMAL:
			appVersionTv.setVisibility(View.VISIBLE);
			appSizeView.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.GONE);

			downloadTv.setVisibility(View.VISIBLE);
			appDownloadStatusView.setVisibility(View.GONE);
			percentPb.setVisibility(View.GONE);
			appNewVersionTv.setVisibility(View.GONE);
			launchTv.setVisibility(View.GONE);
			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.WAITING:
			appVersionTv.setVisibility(View.GONE);
			appSizeView.setVisibility(View.GONE);
			appDownloadStatusView.setVisibility(View.VISIBLE);
			percentPb.setVisibility(View.VISIBLE);
			appNewVersionTv.setVisibility(View.VISIBLE);
			appDownloadSattusTv.setText(context.getResources().getString(R.string.download_status_waiting));

			downloadTv.setVisibility(View.GONE);
			launchTv.setVisibility(View.GONE);
			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.CONNECTING:
			appVersionTv.setVisibility(View.GONE);
			appSizeView.setVisibility(View.GONE);
			appDownloadStatusView.setVisibility(View.VISIBLE);
			percentPb.setVisibility(View.VISIBLE);
			appNewVersionTv.setVisibility(View.VISIBLE);
			appDownloadSattusTv.setText(context.getResources().getString(R.string.download_status_connecting));

			downloadTv.setVisibility(View.GONE);
			launchTv.setVisibility(View.GONE);
			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.GONE);
		case AppInfoBean.Status.DOWNLOADING:
			appVersionTv.setVisibility(View.GONE);
			appSizeView.setVisibility(View.GONE);
			appDownloadStatusView.setVisibility(View.VISIBLE);
			percentPb.setVisibility(View.VISIBLE);
			appNewVersionTv.setVisibility(View.VISIBLE);

			downloadTv.setVisibility(View.GONE);
			launchTv.setVisibility(View.GONE);
			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.VISIBLE);
			downloadContinueTv.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.PAUSED:
			appVersionTv.setVisibility(View.GONE);
			appSizeView.setVisibility(View.GONE);
			appDownloadStatusView.setVisibility(View.VISIBLE);
			percentPb.setVisibility(View.VISIBLE);
			appNewVersionTv.setVisibility(View.VISIBLE);
			appDownloadSattusTv.setText(context.getResources().getString(R.string.download_status_paused));
			appDownloadSizeTv.setVisibility(View.VISIBLE);
			downloadTv.setVisibility(View.GONE);
			launchTv.setVisibility(View.GONE);
			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.GONE);
			downloadContinueTv.setVisibility(View.VISIBLE);
			break;
		case AppInfoBean.Status.CANUPGRADE:
			appVersionTv.setVisibility(View.VISIBLE);
			appSizeView.setVisibility(View.VISIBLE);
			appDownloadStatusView.setVisibility(View.GONE);
			percentPb.setVisibility(View.GONE);
			appNewVersionTv.setVisibility(View.GONE);

			downloadTv.setVisibility(View.GONE);
			launchTv.setVisibility(View.GONE);
			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.VISIBLE);
			downloadPauseTv.setVisibility(View.GONE);
			downloadContinueTv.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.CANINSTALL:
			appVersionTv.setVisibility(View.VISIBLE);
			appSizeView.setVisibility(View.VISIBLE);
			appDownloadStatusView.setVisibility(View.GONE);
			percentPb.setVisibility(View.GONE);
			appNewVersionTv.setVisibility(View.GONE);

			downloadTv.setVisibility(View.GONE);
			launchTv.setVisibility(View.GONE);
			installTv.setVisibility(View.VISIBLE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.GONE);
			downloadContinueTv.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.CANLAUNCH:
			appVersionTv.setVisibility(View.VISIBLE);
			appSizeView.setVisibility(View.VISIBLE);
			appDownloadStatusView.setVisibility(View.GONE);
			percentPb.setVisibility(View.GONE);
			appNewVersionTv.setVisibility(View.GONE);

			downloadTv.setVisibility(View.GONE);
			launchTv.setVisibility(View.VISIBLE);
			installTv.setVisibility(View.GONE);
			upgradeTv.setVisibility(View.GONE);
			downloadPauseTv.setVisibility(View.GONE);
			downloadContinueTv.setVisibility(View.GONE);

			appPatchSizeTv.setVisibility(View.GONE);
			upgradeLineIv.setVisibility(View.GONE);
			break;
		}
	}

}
