/**   
* @Title: NotificationHelpse.java
* @Package com.x.business.notification
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-8-6 下午3:28:11
* @version V1.0   
*/

package com.x.business.notification;

import com.x.publics.utils.SharedPrefsUtil;

import android.content.Context;
import android.text.TextUtils;

/**
* @ClassName: NotificationHelpse
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-8-6 下午3:28:11
* 
*/

public class AdNotificationHelper {

	private static final String PRE_MESSAGE_ID = "push_message_id";

	public static void saveMessageId(Context context, int messageId) {
		String preMsgId = SharedPrefsUtil.getValue(context, PRE_MESSAGE_ID, "");
		if (TextUtils.isEmpty(preMsgId)) {
			SharedPrefsUtil.putValue(context, PRE_MESSAGE_ID, ""+messageId);
		} else {
			String newMsgId = preMsgId + "," + messageId;
			SharedPrefsUtil.putValue(context, PRE_MESSAGE_ID, newMsgId);
		}
	}

	public static boolean shouldAdPushShow(Context context, int messageId) {
		String preMsgId = SharedPrefsUtil.getValue(context, PRE_MESSAGE_ID, "");
		if (TextUtils.isEmpty(preMsgId)) {
			return true;
		} else {
			if (preMsgId.contains(",")) {
				String[] msgArray = preMsgId.split(",");
				for (String msg : msgArray) {
					if (msg.equals("" + messageId)) {
						return false;
					}
				}
			} else {
				return !preMsgId.equals("" + messageId);
			}
		}
		return true;
	}

}
