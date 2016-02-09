/**   
 * @Title: AdNotificationManager.java
 * @Package com.mas.amineappstore.business.notification
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2015-8-4 上午11:23:59
 * @version V1.0   
 */

package com.x.business.notification;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import com.x.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.x.business.alarm.AlarmManage;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.AdPushResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.PushMessageBean;
import com.x.publics.model.PushNotificationBean;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.Tools;
import com.x.publics.utils.Utils;
import com.x.receiver.AdPushReceiver;
import com.x.ui.activity.home.SplashActivity;

/**
 * @ClassName: AdNotificationManager
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2015-8-4 上午11:23:59
 * 
 */

public class AdNotificationManager {

	private static AdNotificationManager adNotificationManager;
	private NotificationManager mNotificationManager;

	private static AlarmManage alarmManage;
	public Context context;
	public Long alarm_time = 12 * 60 * 60 * 1000L;
	public static final String alarmAction = "com.mas.amineappstore.AD_PUSH";

	public AdNotificationManager() {

	}

	public static synchronized AdNotificationManager getInstance() {
		if (adNotificationManager == null) {
			adNotificationManager = new AdNotificationManager();
		}
		return adNotificationManager;
	}

	/**
	 * 运行定时器
	 */
	public void runAlarmTask(Context context) {
		this.context = context;
		new Thread(new AlarmTask()).start();
	}

	class AlarmTask implements Runnable {
		@Override
		public void run() {
			cancelAlarm();
			creatAlarm();
		}
	}

	/**
	 * 构建定时器
	 */
	private void creatAlarm() {
		Intent alarmIntent = new Intent(context, AdPushReceiver.class);
		alarmIntent.setAction(alarmAction);

		PendingIntent alarmPendingIntent = PendingIntent
				.getBroadcast(this.context, 0, alarmIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager daemonAlarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		daemonAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), alarm_time, alarmPendingIntent);
	}

	/**
	 * 清除定时器
	 */
	private void cancelAlarm() {
		Intent alarmIntent = new Intent(context, AdPushReceiver.class);
		alarmIntent.setAction(alarmAction);

		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,
				0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager daemonAlarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		daemonAlarmManager.cancel(alarmPendingIntent);
	}

	public void getAdPush() {
		String imei = Utils.getIMEI(context);
		String versionName = "v" + Utils.getVersionName(context);
		DataFetcher.getInstance().getAdPush(imei, versionName,
				myAdPushResponseListent, myAdPushErrorListener);
	}

	private Listener<JSONObject> myAdPushResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			AdPushResponse adPushResponse = (AdPushResponse) JsonUtil
					.jsonToBean(response, AdPushResponse.class);
			if (adPushResponse != null && adPushResponse.state.code == 200
					&& adPushResponse.messageList != null) {
				processPushResponse(adPushResponse.messageList);
			}
		}

	};

	private ErrorListener myAdPushErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
		}
	};

	private void processPushResponse(ArrayList<PushMessageBean> messageList) {
		if (messageList.isEmpty()) {
			return;
		}
		final PushMessageBean messageBean = messageList.get(0);
		int messageId = messageBean.getId();
		if (AdNotificationHelper.shouldAdPushShow(context, messageId)) {

			final PushNotificationBean notificationBean = new PushNotificationBean();
			notificationBean.setNotificationTitle(messageBean.title);
			notificationBean.setMode(messageBean.mode);
			notificationBean.setOpenType(messageBean.action);
			notificationBean.setNotificationSummary(messageBean.content);
			notificationBean.setNotificationTime(Tools.getCurrentDate(null,
					null));

			if (messageBean.action == 0) {
				notificationBean
						.setIntentObject(SplashActivity.class.getName());
			} else if (messageBean.action == 1
					&& !TextUtils.isEmpty(messageBean.url)) {
				notificationBean.setIntentObject(messageBean.url);
			}

			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.banner_default_picture)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
					.cacheInMemory(true)
					.showImageForEmptyUri(R.drawable.banner_default_picture)
					.showImageOnFail(R.drawable.banner_default_picture)
					.cacheOnDisc(true).considerExifParams(false)
					.displayer(new SimpleBitmapDisplayer()).build();

			if (messageBean.mode == 1 && !TextUtils.isEmpty(messageBean.picUrl)) {

				ImageLoader.getInstance().loadImage(context, true,
						ImageType.NETWORK, messageBean.picUrl, options,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {

							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap arg2) {
								notificationBean.setNotificationPic(arg2);
							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {

							}
						});
			}
			if (!TextUtils.isEmpty(messageBean.icon)) {
				ImageLoader.getInstance().loadImage(context, true,
						ImageType.NETWORK, messageBean.icon, options,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								System.err.println("");
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								System.err.println("");
							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap arg2) {
								notificationBean.setNotificationSmallIcon(arg2);
							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {

							}
						});
			}

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					while (true) {
						SystemClock.sleep(200);
						if (isBitmapAlready(notificationBean, messageBean))
							break;
					}
					PushNotificationManager.getInstance(context)
							.pushNotification(context, notificationBean);
					AdNotificationHelper.saveMessageId(context, messageBean.id);
				}
			}, 0);

		}
	}

	private boolean isBitmapAlready(PushNotificationBean pushNotificationBean,
			PushMessageBean messageBean) {
		if (messageBean.mode == 1) {
			if (!TextUtils.isEmpty(messageBean.icon)
					&& !TextUtils.isEmpty(messageBean.picUrl)) {
				return pushNotificationBean.getNotificationPic() != null
						&& pushNotificationBean.getNotificationSmallIcon() != null;
			} else if (!TextUtils.isEmpty(messageBean.picUrl)) {
				return pushNotificationBean.getNotificationPic() != null;
			}
		} else if (messageBean.mode == 0) {
			if (!TextUtils.isEmpty(messageBean.icon)) {
				return pushNotificationBean.getNotificationSmallIcon() != null;
			}
		}
		return false;
	}
}
