
/**   
 * @Title: PushNotificationClickReceiver.java
 * @Package com.x.receiver
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2015-8-5 下午4:06:04
 * @version V1.0   
 */

package com.x.receiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.x.business.notification.NotificationConstan;
import com.x.business.notification.PushNotificationManager;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Tools;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.activity.home.SplashActivity;
import com.x.ui.activity.myApps.MyAppsActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @ClassName: PushNotificationClickReceiver
 * @Description: 推送通知 的receiver
 
 * @date 2015-8-5 下午4:06:04
 * 
 */

public class PushNotificationClickReceiver extends BroadcastReceiver {
	private int buttonId;
	private String intentStr;
	private Object intentObj;
	private int openType;
	private String clickIntentAction;
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		clickIntentAction = intent.getAction();
		buttonId = intent.getIntExtra(NotificationConstan.INTENT_BUTTONID_TAG,
				0);
		intentObj = intent
				.getStringExtra(NotificationConstan.INTENT_INTENT_TAG);
		openType = intent.getIntExtra(NotificationConstan.INTENT_OPEN_TYPE_TAG,
				-1);

		/** 根据传入的action:跳转详情 */
		if (clickIntentAction.equals(NotificationConstan.ACTION_BUTTON)) {
			switch (buttonId) {
			case NotificationConstan.DETAIL_BUTTON_ID:
				openActivityOrBrowser(openType, intentObj);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 启动 Browser Or Activity
	 * */
	private void openActivityOrBrowser(int openType, Object intentObj) {
		intentStr=intentObj.toString();
		switch (openType) {
		case NotificationConstan.OPEN_URL:
				PushNotificationManager.getInstance(context).cancleNotification(context);
				Tools.openURL2(context, intentStr);
			break;
		case NotificationConstan.OPEN_ACTIVITY:
			try {
				PushNotificationManager.getInstance(context).cancleNotification(context);
				Tools.startActivity(context,Class.forName(intentStr));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}



}

