/**   
* @Title: SkinViewHolder.java
* @Package com.mas.amineappstore.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-11-28 下午1:55:42
* @version V1.0   
*/

package com.x.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.SkinInfoBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.Utils;

/**
* @ClassName: SkinViewHolder
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-11-28 下午1:55:42
* 
*/

public class SkinViewHolder {

	public ImageView skinLogoIv;
	public TextView skinNameTv, skinBriefTv, skinApkSizeTv;
	public FrameLayout skinDownloadFra, skinPauseFra, skinContinueFra, settingFra, skinUsed;//按界面显示的状态命名
	public ProgressBar skinPausePb, skinContinuePb;
	public LinearLayout skinItemLl;

	public SkinViewHolder(View view) {
		if (view != null) {
			skinItemLl = (LinearLayout) view.findViewById(R.id.skin_item_ll);
			skinLogoIv = (ImageView) view.findViewById(R.id.skin_logo_iv);
			skinNameTv = (TextView) view.findViewById(R.id.skin_name_tv);
			skinBriefTv = (TextView) view.findViewById(R.id.skin_brief_tv);
			skinApkSizeTv = (TextView) view.findViewById(R.id.skin_apkSizeTv);

			skinDownloadFra = (FrameLayout) view.findViewById(R.id.skin_download_fra);
			skinPauseFra = (FrameLayout) view.findViewById(R.id.skin_pause_fra);
			skinContinueFra = (FrameLayout) view.findViewById(R.id.skin_continue_fra);
			settingFra = (FrameLayout) view.findViewById(R.id.skin_settings_fra);
			skinUsed = (FrameLayout) view.findViewById(R.id.skin_skinpeelered_fra);
			skinPausePb = (ProgressBar) view.findViewById(R.id.skin_pause_percent);
			skinContinuePb = (ProgressBar) view.findViewById(R.id.skin_continue_percent);

		}
	}

	public void initData(Context context, SkinInfoBean skinBean, int position) {
		if (skinBean != null) {
			long totalBytes, currentBytes;
			String percent = null;
			if (position == 0) {//默認膚色
				NetworkImageUtils.load(context, ImageType.APK, skinBean.getLogo(), R.drawable.skin_default,
						R.drawable.skin_default, skinLogoIv);
				skinApkSizeTv.setText("");
				skinNameTv.setText(skinBean.getSkinName());
				skinBriefTv.setText(skinBean.getDescription());
			} else {
				NetworkImageUtils.load(context, ImageType.NETWORK, skinBean.getLogo(),
						R.drawable.banner_default_picture, R.drawable.banner_default_picture, skinLogoIv);
				skinApkSizeTv.setText(Utils.sizeFormat2(skinBean.getApkSize()));
				skinNameTv.setText(skinBean.getSkinName());
				skinBriefTv.setText(skinBean.getDescription());
				totalBytes = skinBean.getApkSize();
				currentBytes = skinBean.getCurrentBytes();
				if (totalBytes != 0)
					percent = (currentBytes * 100 / totalBytes) + "";
			}
			refreshAppStatus(skinBean.getStatus(), context, percent);
		}
	}

	public void refreshAppStatus(int status, Context context, String percent) {
		switch (status) {
		case AppInfoBean.Status.NORMAL://正常状态下显示下载
			skinDownloadFra.setVisibility(View.VISIBLE);
			skinPauseFra.setVisibility(View.GONE);
			skinContinueFra.setVisibility(View.GONE);
			settingFra.setVisibility(View.GONE);
			skinUsed.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.WAITING://waiting
			skinDownloadFra.setVisibility(View.GONE);
			skinContinueFra.setVisibility(View.GONE);
			skinPauseFra.setVisibility(View.VISIBLE);
			skinPausePb.setVisibility(View.GONE);
			settingFra.setVisibility(View.GONE);
			skinUsed.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.PAUSED://显示继续
			skinDownloadFra.setVisibility(View.GONE);
			skinPauseFra.setVisibility(View.GONE);
			skinContinueFra.setVisibility(View.VISIBLE);
			if (percent != null) {
				skinContinuePb.setProgress(Integer.valueOf(percent));
			}
			settingFra.setVisibility(View.GONE);
			skinUsed.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.DOWNLOADING://显示百分比
			skinDownloadFra.setVisibility(View.GONE);
			skinContinueFra.setVisibility(View.GONE);
			skinPauseFra.setVisibility(View.VISIBLE);
			skinPausePb.setVisibility(View.VISIBLE);
			if (percent != null) {
				skinPausePb.setProgress(Integer.valueOf(percent));
			}
			settingFra.setVisibility(View.GONE);
			skinUsed.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.CANINSTALL://下载完毕显示设置
			skinPauseFra.setVisibility(View.GONE);
			skinDownloadFra.setVisibility(View.GONE);
			skinContinueFra.setVisibility(View.GONE);
			settingFra.setVisibility(View.VISIBLE);
			skinUsed.setVisibility(View.GONE);
			break;
		case AppInfoBean.Status.CANLAUNCH://正在使用的皮肤
			skinPauseFra.setVisibility(View.GONE);
			skinDownloadFra.setVisibility(View.GONE);
			skinContinueFra.setVisibility(View.GONE);
			settingFra.setVisibility(View.GONE);
			skinUsed.setVisibility(View.VISIBLE);
			break;
		case AppInfoBean.Status.ERROR://显示“继续”图标
			skinDownloadFra.setVisibility(View.GONE);
			skinPauseFra.setVisibility(View.GONE);
			skinContinueFra.setVisibility(View.VISIBLE);
			if (percent != null) {
				skinContinuePb.setProgress(Integer.valueOf(percent));
			}
			settingFra.setVisibility(View.GONE);
			skinUsed.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

}
