/**   
 * @Title: AppLockerViewHolder.java
 * @Package com.x.ui.activity.applocker
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-10-11 上午10:54:13
 * @version V1.0   
 */

package com.x.ui.adapter.applocker;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.AppLockerBean;
import com.x.publics.utils.NetworkImageUtils;

/**
 * @ClassName: AppLockerViewHolder
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-10-11 上午10:54:13
 * 
 */

public class AppLockerViewHolder {
	public TextView catalogTypeTv, frequentAppTv;
	public ImageView appIconIv, arrowIv;
	public TextView appNameTv, appVersionTv, appSizeTv, appNewVersionTv, appPatchSizeTv, maa_app_size_tv;
	public TextView appDownloadSizeTv, appDownloadStatusTv;
	public ProgressBar percentPb;
	public View appDownloadStatusView, appSizeView;
	public TextView downloadPauseTv, downloadContinueTv, upgradeTv, installTv, downloadTv, launchTv;
	public ImageView upgradeLineIv;
	public CheckBox appLocker_isLock;
	public View updateExpandView, downloadExpandView, localAppExpandView, zappAppExpandView, preUpdateExpandView,
			rootView;

	public ImageView favoriteIv, categoryIcon;
	public LinearLayout llCategoryLayout;
	public boolean hasInited;

	public AppLockerViewHolder(View view) {
		if (view != null) {
			rootView = view.findViewById(R.id.maa_top_rl);
			// 定义常用软件栏显示
			llCategoryLayout = (LinearLayout) view.findViewById(R.id.ll_category_layout);
			categoryIcon = (ImageView) view.findViewById(R.id.category_icon);
			frequentAppTv = (TextView) view.findViewById(R.id.frequent_app_tv);
			maa_app_size_tv = (TextView) view.findViewById(R.id.maa_app_size_tv);
			appIconIv = (ImageView) view.findViewById(R.id.maa_app_icon_iv);
			arrowIv = (ImageView) view.findViewById(R.id.maa_arrow_iv);
			appNameTv = (TextView) view.findViewById(R.id.maa_app_name_tv);
			// checkBox
			appLocker_isLock = (CheckBox) view.findViewById(R.id.applokcer_islock);

			hasInited = true;
		}
	}

	public void refreshMerge(Context context) {
		downloadPauseTv.setClickable(false);
	}

	public void initData(AppLockerBean appLockerBean, Context context) {

		if (appLockerBean.isGroupApp) {
			ComponentName componentName = new ComponentName(appLockerBean.getPackageName(),
					appLockerBean.getActivityName());
			try {
				Drawable drawable = context.getPackageManager().getActivityIcon(componentName);
				appIconIv.setImageDrawable(drawable);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			NetworkImageUtils.load(context, ImageType.APP, appLockerBean.getPackageName(),
					R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIconIv);
		}
		// 设置App名字
		appNameTv.setText(appLockerBean.getAppName());
		// 设置 APP 描述
		maa_app_size_tv.setText(appLockerBean.getLockerDesc());
		// *获取是否上锁
		appLockerBean.getLockerSortTypeName();
		appLocker_isLock.setChecked(appLockerBean.isLocked());

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

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setViewBackground(context, rootView, SkinConstan.LIST_VIEW_ITEM_BG);
		SkinConfigManager.getInstance().setViewBackground(context, categoryIcon, SkinConstan.DIVIDER_BAR);
	}

}
