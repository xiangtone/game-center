/**   
* @Title: NoticeManager.java
* @Package com.x.business.notice
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-10 下午2:31:53
* @version V1.0   
*/

package com.x.business.notice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.x.R;
import com.x.publics.utils.Utils;
import com.x.receiver.NotificationBtnClickReceiver;

/**
* @ClassName: NoticeManager
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-10 下午2:31:53
* 
*/

@SuppressLint("NewApi")
public class NoticeManager {

	private static NoticeManager noticeManager;
	private NotificationManager mNotificationManager;

	private int openReqCode = 10101001;// 打开requestCode
	private int shareReqCode = 20202001;// 分享requestCode

	/**
	 * @Title: NoticeManager
	 * @Description: 私有化的构造方法，保证外部的类不能通过构造器来实例化。
	 * @param @return
	 * @throws
	 */
	private NoticeManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @Title: getInstance
	 * @Description: 获取单例对象实例，同步方法，实现线程互斥访问，保证线程安全。
	 * @param @return 构建单例模式
	 * @return NoticeManager
	 * @throws
	 */
	public static synchronized NoticeManager getInstance() {
		if (noticeManager == null) {
			noticeManager = new NoticeManager();
		}
		return noticeManager;
	}

	/**
	* @Title: showNoticeBar 
	* @Description: TODO 
	* @param @param context    
	* @return void
	 */
	public void showNoticeBar(Context context, int appId, String title, String packageName) {

		// When the same Notification more than twice,'reqCode' must be different. 
		openReqCode++;
		shareReqCode++;

		// initialize NotificationManager
		mNotificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

		// Remote view
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notice_install_complete);
		remoteViews.setTextViewText(R.id.noti_title, title);
		remoteViews.setTextViewText(R.id.noti_time, Utils.getTimeShort());
		remoteViews.setImageViewBitmap(R.id.noti_icon, Utils.getApplicationIcon(context, packageName));
		if (android.os.Build.VERSION.SDK_INT <= 16)
			remoteViews.setViewVisibility(R.id.noti_drag_tips, View.GONE);

		// Button events
		remoteViews.setOnClickPendingIntent(
				R.id.noti_open_btn,
				getActionPendingIntent(context, appId, title, packageName,
						NotificationBtnClickReceiver.NOTI_OPEN_BUTTON_ID, openReqCode));
		remoteViews.setOnClickPendingIntent(
				R.id.noti_share_btn,
				getActionPendingIntent(context, appId, title, packageName,
						NotificationBtnClickReceiver.NOTI_SHARE_BUTTON_ID, shareReqCode));

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		Notification mNotification = mBuilder.setContent(remoteViews).setSmallIcon(R.drawable.ic_noti_open)
				.setPriority(NotificationCompat.PRIORITY_MAX).setAutoCancel(true).build();

		mNotification.contentIntent = getActionPendingIntent(context, appId, title, packageName,
				NotificationBtnClickReceiver.NOTI_OPEN_BUTTON_ID, openReqCode);

		if (android.os.Build.VERSION.SDK_INT >= 16) {
			mNotification.bigContentView = remoteViews; // 可显示多行，只支持4.1+
		} else {
			mNotification.contentView = remoteViews; // 显示单行，高度固定
		}

		mNotificationManager.notify(appId, mNotification);
	}

	/**
	* @Title: getActionPendingIntent 
	* @Description: TODO 
	* @param @param actionId
	* @param @param requestCode
	* @param @return    
	* @return PendingIntent
	 */
	private PendingIntent getActionPendingIntent(Context context, int appId, String appName, String packageName,
			int actionId, int requestCode) {
		// 点击的事件处理
		Intent buttonIntent = new Intent(NotificationBtnClickReceiver.ACTION_BUTTON);
		buttonIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		buttonIntent.putExtra(NotificationBtnClickReceiver.INTENT_BUTTONID_TAG, actionId);
		buttonIntent.putExtra(NotificationBtnClickReceiver.INTENT_NOTICEID_TAG, appId);
		buttonIntent.putExtra(NotificationBtnClickReceiver.INTENT_APPNAME_TAG, appName);
		buttonIntent.putExtra(NotificationBtnClickReceiver.INTENT_PACKAGENAME_TAG, packageName);
		// 这里加了广播，所及INTENT的必须用getBroadcast方法
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, buttonIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}
}
