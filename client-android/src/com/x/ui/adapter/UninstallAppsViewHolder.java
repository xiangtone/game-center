/**   
* @Title: UninstallAppsViewHolder.java
* @Package com.x.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-10 上午9:40:55
* @version V1.0   
*/

package com.x.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.Utils;

/**
* @ClassName: UninstallAppsViewHolder
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-10 上午9:40:55
* 
*/

public class UninstallAppsViewHolder {

	public TextView catalogTv;
	public ImageView appIconIv, uninstallIv;
	public TextView appNameTv, appVersionTv, appSizeTv, appNewVersionTv, appPatchSizeTv;
	public TextView downloadPauseTv, downloadContinueTv, upgradeTv, installTv, downloadTv, uninstallTv;

	public UninstallAppsViewHolder(View view) {
		if (view != null) {
			catalogTv = (TextView) view.findViewById(R.id.uninstallapp_catalog_tv);
			appIconIv = (ImageView) view.findViewById(R.id.uninstallapp_lefticon_iv);
			uninstallTv = (TextView) view.findViewById(R.id.uninstallapp_tv);
			appNameTv = (TextView) view.findViewById(R.id.uninstallapp_appName_tv);
			appVersionTv = (TextView) view.findViewById(R.id.uninstallapp_version_tv);
			appSizeTv = (TextView) view.findViewById(R.id.uninstallapp_size_tv);
		}
	}

	public void initData(InstallAppBean appsBean, Context context) {

		NetworkImageUtils.load(context, ImageType.APP, appsBean.getPackageName(), R.drawable.ic_screen_default_picture,
				R.drawable.ic_screen_default_picture, appIconIv);
		appNameTv.setText(appsBean.getAppName());
		appVersionTv.setText(appsBean.getVersionName());
		appSizeTv.setText(appsBean.getFileSize() == 0 ? "" : Utils.sizeFormat(Long.valueOf(appsBean.getFileSize())));
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param context    
	* @return void    
	*/
	public void setSkinTheme(Context context) {
		SkinConfigManager.getInstance().setTextViewDrawableTop(context, uninstallTv, SkinConstan.UNINSTALL_BTN);
	}

}
