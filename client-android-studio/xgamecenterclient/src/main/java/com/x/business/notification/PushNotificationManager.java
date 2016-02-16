package com.x.business.notification;

import com.x.R;
import com.x.publics.model.PushNotificationBean;
import com.x.publics.utils.Tools;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

/**
 * @ClassName: PushNotificationManager
 * @Description: 推送通知
 
 * @date 2015-8-5 下午3:08:54
 * 
 */

public class PushNotificationManager {
	private Context context;
	private NotificationManager notifyManager;
	private static PushNotificationManager pushNotifyManagerInstance;
	private Notification mNotification;
	private NotificationCompat.Builder notifyBuilder;
	/** 通知 intent */
	private Object intentObject;
	/** 打开类型 */
	private int openType = -1;
	/** 推送类型 */
	private int mode = -1;

	public PushNotificationManager(Context context) {
		this.context = context;
	}

	/**
	 * 获取 push manager 单例
	 */
	public static PushNotificationManager getInstance(Context context) {
		if (pushNotifyManagerInstance == null) {
			pushNotifyManagerInstance = new PushNotificationManager(context);
		}
		return pushNotifyManagerInstance;
	}

	/***
	 * 
	 * @Description: 开启推送通知
	 * @param @param context
	 * @param @param pushNotificationBean
	 * @return void
	 */
	@SuppressLint("NewApi")
	public void pushNotification(Context context,
			PushNotificationBean pushNotificationBean) {
		this.context = context;
		notifyManager = (NotificationManager) context
				.getSystemService(Service.NOTIFICATION_SERVICE);

		/** intentStr赋值 */
		intentObject = pushNotificationBean.getIntentObject();
		/** openType 赋值 */
		openType = pushNotificationBean.getOpenType();
		/** mode 赋值 */
		mode = pushNotificationBean.getMode();
		/** 自定义推送布局 */
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.notification_pic_view);
		/** 信息图片 */
		remoteViews.setImageViewBitmap(R.id.notify_img,
				pushNotificationBean.getNotificationPic());
		/** 右下角 icon */
		if(pushNotificationBean.getNotificationSmallIcon()==null){
			remoteViews.setImageViewBitmap(R.id.notify_small_icon,
					Tools.getApplicationIcon(context, context.getPackageName()));
		}else{
			remoteViews.setImageViewBitmap(R.id.notify_small_icon,
					pushNotificationBean.getNotificationSmallIcon());
		}
		/** 标题 */
		remoteViews.setTextViewText(R.id.notify_title,
				pushNotificationBean.getNotificationTitle());
		/** 推送时间 */
		remoteViews.setTextViewText(R.id.notify_time,
				pushNotificationBean.getNotificationTime());
		/** 描述 内容 */
		remoteViews.setTextViewText(R.id.notify_summary,
				pushNotificationBean.getNotificationSummary());
		/** titile +summary layout listener */
		remoteViews.setOnClickPendingIntent(
				R.id.notify_content_rl,
				getPendingIntent(NotificationConstan.DETAIL_REQ_CODE,
						NotificationConstan.DETAIL_BUTTON_ID));

		notifyBuilder = new NotificationCompat.Builder(context);
		mNotification = buildNotification(pushNotificationBean);

		/** 区分sdk版本 */
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			mNotification.bigContentView = remoteViews;
		} else {
			remoteViews.setViewVisibility(R.id.notify_img, View.INVISIBLE);
			mNotification.contentView = remoteViews;
		}

		/** 控制:推送类型: 1:图片 0：文字 */
		if (mode == NotificationConstan.PUSH_MODE_PICTURE) {
			mNotification.bigContentView = remoteViews;
		} else if (mode == NotificationConstan.PUSH_MODE_TEXT) {
			mNotification = buildNotification(pushNotificationBean); //获取系统默认样式
		}
		
		/** 通知属性 */
		mNotification.defaults |= Notification.DEFAULT_LIGHTS;
		mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		
		/** get pendingIntent 请求码，控件ID */
		mNotification.contentIntent = getPendingIntent(
				NotificationConstan.DETAIL_REQ_CODE,
				NotificationConstan.DETAIL_BUTTON_ID);

		/** 启动通知 :标志：详情ID */
		notifyManager.notify(NotificationConstan.NOTI_DETAIL_ID, mNotification);
	}

	/***
	 * 
	 * @Description: 获取推送信息的pendingIntent
	 * @param @param requestCode
	 * @param @param notificationId
	 * @param @return
	 * @return PendingIntent
	 */
	private PendingIntent getPendingIntent(int requestCode, int buttonId) {
		/** 传入点击意图 */
		Intent clickIntent = new Intent(NotificationConstan.ACTION_BUTTON);
		/** 传入：通知栏 控件ID */
		clickIntent.putExtra(NotificationConstan.INTENT_BUTTONID_TAG, buttonId);
		/** 传入intent (activity 或者 url) */
		clickIntent.putExtra(NotificationConstan.INTENT_INTENT_TAG,
				intentObject.toString());
		clickIntent
				.putExtra(NotificationConstan.INTENT_OPEN_TYPE_TAG, openType);
		/** 传入：请求类型Code */
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				requestCode, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}



	/***
	 * 
	 * @Description: buider 组装 notification对象(推送纯文字情况下的通知)
	 * @param @param pushNotificationBean
	 * @param @return
	 * @return Notification
	 */
	private Notification buildNotification(
			PushNotificationBean pushNotificationBean) {
		mNotification = notifyBuilder
				.setContentTitle(pushNotificationBean.getNotificationTitle())
				.setContentText(pushNotificationBean.getNotificationSummary())
				.setSmallIcon(R.drawable.tiker_bar_icon)
				.setLargeIcon(pushNotificationBean.getNotificationSmallIcon())
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setWhen(System.currentTimeMillis()).setTicker(context.getResources().getString(R.string.push_notification_tricker))
				.setAutoCancel(true)
				.build();

		return mNotification;
	}
	
	/***
	 * 
	 * @Description: 根据 ：详情标志位，取消通知栏
	 * @param @param context
	 * @return void
	 */
	public void cancleNotification(Context context) {
		if (notifyManager == null) {
			notifyManager = (NotificationManager) context
					.getSystemService(Service.NOTIFICATION_SERVICE);
		}
		/** 根据 notify 信息标志位 取消信息 */
		notifyManager.cancel(NotificationConstan.NOTI_DETAIL_ID);
	}
}
