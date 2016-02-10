/**   
* @Title: AdPushReceiver.java
* @Package com.x.receiver
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-8-4 下午1:36:49
* @version V1.0   
*/

package com.x.receiver;

import com.x.business.notification.AdNotificationManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
* @ClassName: AdPushReceiver
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-8-4 下午1:36:49
* 
*/

public class AdPushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (AdNotificationManager.alarmAction.equals(intent.getAction())) {
			AdNotificationManager.getInstance().getAdPush();
		}
	}

}
