/**   
* @Title: AutoCheckUpdateManager.java
* @Package com.x.manage
* @Description: TODO 

* @date 2014-2-28 上午11:26:01
* @version V1.0   
*/

package com.x.business.update;

import java.util.List;

import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

import com.x.R;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.model.AppsUpgradeResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.myApps.MyAppsActivity;

/**
* @ClassName: AutoCheckUpdateManager
* @Description: TODO 

* @date 2014-2-28 上午11:26:01
* 
*/

public class AutoCheckUpdateManager {
	private static final String TAG = "AutoCheckUpdateManager";
	private static AutoCheckUpdateManager autoCheckUpdateManager;
	private Context context;

	public static AutoCheckUpdateManager getInstance(Context context) {
		if (autoCheckUpdateManager == null)
			return new AutoCheckUpdateManager(context);
		else
			return autoCheckUpdateManager;
	}

	private AutoCheckUpdateManager(Context context) {
		this.context = context;
	}

	/** 
	* @Title: networkChangeCheckUpdate 
	* @Description: 移动网络打开检查更新 (非强制检查更新)
	* @param     
	* @return void    
	*/

	public void networkChangeCheckUpdate() {
		boolean canSendReuest = UpdateManage.getInstance(context).canSendUpdateRequest(context);
		if (canSendReuest) {
			LogUtil.getLogger().d("networkChangeCheckUpdate, 强制更新");
			UpdateManage.getInstance(context).getAppsUpdate(myUpgradeResponseListent, myUpgradeErrorListener);
		}
	}

	/** 
	* @Title: wifiChangeCheckUpdate 
	* @Description: wifi打开检查更新 (非强制检查更新)
	* @param     
	* @return void    
	*/

	public void wifiChangeCheckUpdate() {
		boolean canSendReuest = UpdateManage.getInstance(context).canSendUpdateRequest(context);
		if (canSendReuest) {
			LogUtil.getLogger().d("wifiChangeCheckUpdate, 强制更新");
			UpdateManage.getInstance(context).getAppsUpdate(myUpgradeResponseListent, myUpgradeErrorListener);
		} else {
			LogUtil.getLogger().d("wifiChangeCheckUpdate, 非强制更新，获取缓存");
			List<InstallAppBean> updatelist = UpdateManage.getInstance(context).findAllUpdateInstallApp();
			if (updatelist != null && !updatelist.isEmpty()) {
				UpdateManage.getInstance(context).showUpdateNotification(updatelist);
			}
			//获取升级数据，判断是否自动下载更新
			if (Utils.getSettingModel(context).isAutoDownloadUpdateInWifi()
					&& NetworkUtils.getNetworkInfo(context).equals(NetworkUtils.NETWORK_TYPE_WIFI))
				UpdateManage.getInstance(context).autoDownloadUpdateAppControl(true);
			sendUpdateWifiChangeBroadcast();
		}
	}

	/** 
	* @Title: autoCheckUpdate 
	* @Description: 定时检查更新 (强制检查更新)
	* @param     
	* @return void    
	*/

	public void alarmCheckUpdate() {
		LogUtil.getLogger().d("autoCheckUpdate, 强制更新");
		UpdateManage.getInstance(context).getAppsUpdate(myUpgradeResponseListent, myUpgradeErrorListener);
	}

	private Listener<JSONObject> myUpgradeResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d(" getAppsUpdate response==>" + response.toString());
			AppsUpgradeResponse upgradeResponse = (AppsUpgradeResponse) JsonUtil.jsonToBean(response,
					AppsUpgradeResponse.class);
			if (upgradeResponse != null && upgradeResponse.state.code == 200 && upgradeResponse.applist != null) {
				UpdateManage.getInstance(context).setLastUpdateRequestSuccessTime(context);
				UpdateManage.getInstance(context).setLastUpdateRequestSuccess();
				UpdateManage.getInstance(context).deleteAllUpdateApp();
				for (InstallAppBean installAppBean : upgradeResponse.applist) {
					UpdateManage.getInstance(context).addUpdateApp(installAppBean);
				}
				//获取升级数据，判断是否自动下载更新
				if (Utils.getSettingModel(context).isAutoDownloadUpdateInWifi()
						&& NetworkUtils.getNetworkInfo(context).equals(NetworkUtils.NETWORK_TYPE_WIFI))
					UpdateManage.getInstance(context).autoDownloadUpdateAppControl(true);
				if (!upgradeResponse.applist.isEmpty())
					UpdateManage.getInstance(context).showUpdateNotification(upgradeResponse.applist);

				sendUpdateWifiChangeBroadcast();
			} else {
				UpdateManage.getInstance(context).setLastUpdateRequestFail();
			}
		}
	};

	public void showNotification(int id, List<InstallAppBean> applist) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Service.NOTIFICATION_SERVICE);
		int appNum = applist.size();
		String title = ResourceUtil.getString(context, R.string.update_notification_title, "" + appNum);
		String content = "";
		for (InstallAppBean bean : applist) {
			content += bean.getAppName() + ",";
		}
		content = content.subSequence(0, content.length() - 1).toString();
		NotificationCompat.Builder builder = new Builder(context);
		builder.setTicker(title);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.tiker_bar_icon);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.mas_ic_launcher));
		builder.setAutoCancel(true);
		builder.setPriority(NotificationCompat.PRIORITY_HIGH);

		Intent intent = new Intent(context, MyAppsActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		builder.setContentIntent(resultPendingIntent);
		notificationManager.notify(id, builder.build());
	}

	private ErrorListener myUpgradeErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			UpdateManage.getInstance(context).setLastUpdateRequestFail();
		}
	};

	/** 
	* @Title: sendUpdateWifiChangeBroadcast 
	* @Description: 发送广播通知首页更新可更新按钮数字提示 
	* @param     
	* @return void    
	*/

	private void sendUpdateWifiChangeBroadcast() {
		Intent intent = new Intent(MyIntents.INTENT_UPDATE_CHANGE_WIFI_ACTION);
		intent.putExtra(MyIntents.TYPE, MyIntents.Types.CHANGE_HOMEPAGE_UPDATE_NUM);
		BroadcastManager.sendBroadcast(intent);
	}

}
