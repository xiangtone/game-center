package com.hykj.gamecenter.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.hykj.gamecenter.receiver.MonitorAppsUpdateServiceManagerReceiver;

public class AlarmManagerServiceManager {
	public static final String ACTION_ALARM_MANAGER_ARRIVAL = "com.android.services.AlarmManagerService.alarmArrival";

	// 启用alarmManager管理，alarmManager是系统底层的，进程被杀了仍然会收到广播,设置一次性闹钟
	public static void startAlarmRepeatingOnce(Context context,
			long intervalMillis) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// MonitorAppsUpdateServiceManagerReceiver：接收广播的receiver
		Intent intent = new Intent(context,
				MonitorAppsUpdateServiceManagerReceiver.class);
		intent.setAction(ACTION_ALARM_MANAGER_ARRIVAL);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		// 用Repeating的方式。
		// 每隔intervalMillis秒发一条广播消息过去。RTC_WAKEUP的方式，保证即使手机休眠了，也依然会发广播消息。
		am.set(AlarmManager.RTC_WAKEUP, now + intervalMillis, pi);
	}

	// 启用alarmManager管理，alarmManager是系统底层的，进程被杀了仍然会收到广播，设置重复闹钟
	public static void startAlarmRepeating(Context context, long intervalMillis) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		// MonitorAppsUpdateServiceManagerReceiver：接收广播的receiver
		Intent intent = new Intent(context,
				MonitorAppsUpdateServiceManagerReceiver.class);
		intent.setAction(ACTION_ALARM_MANAGER_ARRIVAL);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		// 用Repeating的方式。
		// 每隔intervalMillis秒发一条广播消息过去。RTC_WAKEUP的方式，保证即使手机休眠了，也依然会发广播消息。
		am.setRepeating(AlarmManager.RTC_WAKEUP, now, now + intervalMillis, pi);
	}

}