package com.x.receiver;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.x.business.alarm.AlarmManage;
import com.x.business.favorite.FavoriteManage;
import com.x.business.statistic.StatisticManager;
import com.x.business.update.AutoCheckUpdateManager;
import com.x.business.update.UpdateManage;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.upgrade.UpgradeManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.FeedbackWarnRequest;
import com.x.publics.http.model.FeedbackWarnResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.Utils;

public class ConnectChangeReceiver extends BroadcastReceiver {
	private static final String TAG = "ConnectChangeReceiver";
	private static final String NET_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	public static final String NETWORK_CONNECTED_ACTION = "zApp.network.connected.auto.notification";
	private static final int REQUEST_INTERVAL = 5 * 1000;
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		if (intent == null || context == null) {
			return;
		}
		//网络切换
		if (NET_ACTION.equals(intent.getAction())) {
			if (!intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
				if (canRequest(context)) {
					//通知UI已经联网，发送广播。
					LogUtil.getLogger().d("network available,send ui update broadcast");
					Intent aIntent = new Intent();
					aIntent.putExtra(MyIntents.TYPE, MyIntents.Types.VALID_WIFI);
					aIntent.setAction(NETWORK_CONNECTED_ACTION);
					BroadcastManager.sendBroadcast(aIntent);

					StatisticManager.getInstance().reActivateDevice(context);
				}

				if (NetworkUtils.NETWORK_TYPE_WIFI.equals(NetworkUtils.getNetworkInfo(context))) {
					if (canRequest(context)) {
						LogUtil.getLogger().d("wifi network available, wifiChangeCheckUpdate");
						FavoriteManage.getInstance(context).autoDownloadFavorteAppControl(true);//wifi 自动下载应用更新
						AutoCheckUpdateManager.getInstance(context).wifiChangeCheckUpdate();
						setLastRequest(context);
						// 新版本检查，wifi自动下载
						boolean canSendReuest = UpdateManage.getInstance(context).canShowUpdateNotification();//根据是否可以展示推送来判断是否发送请求
						if (canSendReuest) {
							UpgradeManager.getInstence(context).checkVersion(UpgradeManager.START_IN_BACKGROUND, false);
						}
					}
				} else {
					if (canRequest(context)) {
						LogUtil.getLogger().d("mobile network available, networkChangeCheckUpdate");
						AutoCheckUpdateManager.getInstance(context).networkChangeCheckUpdate();
						setLastRequest(context);
					}
				}
			}
			//设置开关触发
		} else if (MyIntents.INTENT_FAVORITE_AUTO_WIFI_ACTION.equals(intent.getAction())) {
			FavoriteManage.getInstance(context).autoDownloadFavorteAppControl(false);

		} else if (MyIntents.INTENT_UPDATE_AUTO_WIFI_ACTION.equals(intent.getAction())) {
			UpdateManage.getInstance(context).autoDownloadUpdateAppControl(false);

		} else if (AlarmManage.alarmAction.equals(intent.getAction())) {
			LogUtil.getLogger().d("Auto check update alarm running ");
			SharedPrefsUtil.putValue(context, AlarmManage.alarm_Runing_Time, System.currentTimeMillis());
			AutoCheckUpdateManager.getInstance(context).alarmCheckUpdate();
			// 新版本检查，8小时自动下载
			UpgradeManager.getInstence(context).checkVersion(UpgradeManager.START_IN_BACKGROUND, false);
			//回馈提醒
			feedbackWarn();
		}
	}

	public long getLastRequest(Context context) {
		return SharedPrefsUtil.getValue(context, "lastRequest", 0l);
	}

	public void setLastRequest(Context context) {
		SharedPrefsUtil.putValue(context, "lastRequest", System.currentTimeMillis());
	}

	/** 
	* @Title: canRequest 
	* @Description: 控制在指定时间间隔内只请求一次  避免接收到多次wifi切换广播
	* @param @param context
	* @param @return     
	* @return boolean    
	*/

	private boolean canRequest(Context context) {
		long lastReuestTIme = getLastRequest(context);
		if (lastReuestTIme == 0l)
			return true;
		else if (Math.abs(System.currentTimeMillis() - lastReuestTIme) >= REQUEST_INTERVAL) {
			return true;
		} else {
			return false;
		}
	}

	//--------反馈提醒--------->
	private void feedbackWarn() {
		FeedbackWarnRequest request = new FeedbackWarnRequest();
		request.setImei(Utils.getIMEI(context));
		DataFetcher.getInstance().feedbackWarn(request, warnListent, warnErrorListener);
	}

	/**
	 * < 回馈提醒 >数据响应
	 */
	private Listener<JSONObject> warnListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString());
			// 响应数据，进行操作
			final FeedbackWarnResponse feedbackResponse = (FeedbackWarnResponse) JsonUtil.jsonToBean(response,
					FeedbackWarnResponse.class);

			if (feedbackResponse != null && feedbackResponse.state.code == 200) {
				int feedbackCode = feedbackResponse.getFeedbackCode();
				boolean attention = feedbackResponse.isFeedbackAttention();
				SharedPrefsUtil.putValue(context, "feedbackCode", feedbackCode);
				SharedPrefsUtil.putValue(context, "attention", attention);
				if (attention == true) //提示用户有反馈回复
				{
					Utils.showNotification(context);
				}
			}
		}
	};

	/**
	 * < 回馈提醒 >获取异常响应处理
	 */
	private ErrorListener warnErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			//
		}
	};
}
