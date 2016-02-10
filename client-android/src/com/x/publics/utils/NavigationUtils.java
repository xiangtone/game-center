/**   
* @Title: NavigationUtils.java
* @Package com.x.publics.utils
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-6-23 上午11:20:04
* @version V1.0   
*/

package com.x.publics.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.x.R;
import com.x.publics.model.AppInfoBean;
import com.x.ui.activity.appdetail.AppDetailActivity;

/**
* @ClassName: NavigationUtils
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-6-23 上午11:20:04
* 
*/

public class NavigationUtils {

	
	/** 
	* @Title: toAppDetailPage 
	* @Description: 跳转详情页
	* @param @param context
	* @param @param appInfoBean
	* @param @param ct    
	* @return void    
	*/ 
	
	public static void toAppDetailPage(Context context, AppInfoBean appInfoBean, int ct) {
		// check network
		if (!NetworkUtils.isNetworkAvailable(context)) {
			ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work), Toast.LENGTH_SHORT);
			return;
		}

		Intent intent = new Intent(context, AppDetailActivity.class);
		intent.putExtra("appInfoBean", appInfoBean);
		intent.putExtra("ct", ct);
		context.startActivity(intent);

	}

}
