/**   
 * @Title: NotificationBtnClickReceiver.java
 * @Package com.x.receiver
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-8-7 上午10:43:12
 * @version V1.0   
 */

package com.x.receiver;

import java.lang.reflect.Method;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.x.R;
import com.x.business.notice.NoticeActivity;
import com.x.business.update.UpdateManage;
import com.x.publics.download.upgrade.FxActivity;
import com.x.publics.utils.Constan;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.myApps.MyAppsActivity;

/**
 * @ClassName: NotificationBtnClickReceiver
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-8-7 上午10:43:12
 * 
 */

public class NotificationBtnClickReceiver extends BroadcastReceiver {

	/** 按钮标识 */
	public final static String INTENT_BUTTONID_TAG = "ButtonId";
	/** 通知栏标识 */
	public final static String INTENT_NOTICEID_TAG = "NoticeId";
	/** 应用名称标识 */
	public final static String INTENT_APPNAME_TAG = "AppName";
	/** 应用包名标识 */
	public final static String INTENT_PACKAGENAME_TAG = "PackageName";
	/** Update All 按钮点击 ID */
	public final static int BUTTON_UPDATE_ALL_ID = 1;
	/** 点击整个通知栏 */
	public final static int BUTTON_ONTIFI_BAR_ID = 2;

	public static final int NOTI_DETAIL_BUTTON_ID = 1004; // 详情按钮ID
	public static final int NOTI_INSTALL_BUTTON_ID = 1005;// 安装按钮ID
	public static final int NOTI_OPEN_BUTTON_ID = 1006;// 打开按钮ID
	public static final int NOTI_SHARE_BUTTON_ID = 1007;// 分享按钮ID

	/** 通知栏按钮点击事件对应的ACTION */
	public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(ACTION_BUTTON)) {
			// 通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
			int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
			int noticeId = intent.getIntExtra(INTENT_NOTICEID_TAG, 0);
			String appName = intent.getStringExtra(INTENT_APPNAME_TAG);
			String packageName = intent.getStringExtra(INTENT_PACKAGENAME_TAG);

			// NotificationManager
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Service.NOTIFICATION_SERVICE);

			switch (buttonId) {

			// 'Update All' 按钮
			case BUTTON_UPDATE_ALL_ID:
				notificationManager.cancel(Constan.Notification.UPDATE_ID);
				UpdateManage.getInstance(context).downloadAllUpdate(
						context,
						UpdateManage.getInstance(context)
								.findAllUpdateInstallApp());
				break;

			case BUTTON_ONTIFI_BAR_ID:
				Intent actionIntent = new Intent(context, MyAppsActivity.class);
				actionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(actionIntent);
				break;

			case NOTI_DETAIL_BUTTON_ID:
			case NOTI_INSTALL_BUTTON_ID:
				Intent fxIntent = new Intent(context, FxActivity.class);
				fxIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				fxIntent.putExtra("ACTION_ID", buttonId);
				fxIntent.putExtra("ACTION_PENDING", true);
				context.startActivity(fxIntent);
				break;

			case NOTI_OPEN_BUTTON_ID:
				notificationManager.cancel(noticeId);
				if (Utils.isAppExit(packageName, context)) {
					Utils.launchAnotherApp(context, packageName);
				} else {
					ToastUtil.show(context, ResourceUtil.getString(context,
							R.string.app_no_exists_tips),
							ToastUtil.LENGTH_SHORT);
				}
				break;

			case NOTI_SHARE_BUTTON_ID:
				notificationManager.cancel(noticeId);
				Intent noticeIntent = new Intent(context, NoticeActivity.class);
				noticeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				noticeIntent.putExtra("appName", appName);
				noticeIntent.putExtra("packageName", packageName);
				context.startActivity(noticeIntent);
				break;
			}

			// hide notification
			Utils.collapseStatusBar(context);
		}
	}

}
