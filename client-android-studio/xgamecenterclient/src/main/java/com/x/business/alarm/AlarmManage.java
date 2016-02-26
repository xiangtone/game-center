package com.x.business.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.x.receiver.BootReceiver;
import com.x.receiver.ConnectChangeReceiver;

public class AlarmManage {

	private static AlarmManage alarmManage;
	public Context context;
	public static final String alarm_Runing_Time = "alarmRuningTime";
	public Long alarm_time = 8 * 60 * 60 * 1000L;/*2 * 60 * 1000L ;*/
	public static final String alarmAction = "com.x.ALARM";

	public Long first_alarm_time =  60 * 60 * 1000L;
	public Long second_alarm_time = 4 * 60 * 60 * 1000L;
	public static final String firstAlarmAction = "com.x.alarm.FIRST";
	public static final String secondAlarmAction = "com.x.alarm.SECOND";

	public AlarmManage(Context context) {
		this.context = context;
	}

	/**
	 * 构建实例
	 
	 * @param context
	 * @return
	 */
	public static AlarmManage getInstance(Context context) {
		if (alarmManage == null) {
			alarmManage = new AlarmManage(context);
		}
		return alarmManage;
	}

	/**
	 * 运行定时器
	 */
	public void runAlarmTask() {
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
		Intent alarmIntent = new Intent(AlarmManage.this.context, ConnectChangeReceiver.class);
		alarmIntent.setAction(alarmAction);

		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this.context, 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager daemonAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		daemonAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarm_time, alarm_time,
				alarmPendingIntent);
	}

	/**
	 * 清除定时器
	 */
	private void cancelAlarm() {
		Intent alarmIntent = new Intent(AlarmManage.this.context, ConnectChangeReceiver.class);
		alarmIntent.setAction(alarmAction);

		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this.context, 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager daemonAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		daemonAlarmManager.cancel(alarmPendingIntent);
	}

	public void createFirstAlarm() {
		Intent alarmIntent = new Intent(AlarmManage.this.context, BootReceiver.class);
		alarmIntent.setAction(firstAlarmAction);

		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this.context, 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager daemonAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		daemonAlarmManager.cancel(alarmPendingIntent);
		daemonAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + first_alarm_time,
				alarmPendingIntent);
		
	}

	public void createSecondAlarm() {
		Intent alarmIntent = new Intent(AlarmManage.this.context, BootReceiver.class);
		alarmIntent.setAction(secondAlarmAction);

		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this.context, 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager daemonAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		daemonAlarmManager.cancel(alarmPendingIntent);
		daemonAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + second_alarm_time,
				alarmPendingIntent);
	}
	

}
