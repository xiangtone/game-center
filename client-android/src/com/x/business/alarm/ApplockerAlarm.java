/**   
* @Title: ApplockerAlarm.java
* @Package com.mas.amineappstore.business.alarm
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-13 上午11:20:12
* @version V1.0   
*/

package com.x.business.alarm;
import com.x.business.applocker.LockService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
* @ClassName: ApplockerAlarm
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-13 上午11:20:12
* 
*/

public class ApplockerAlarm {
	private static ApplockerAlarm applockerAlarm;
	private Context context;
	public static final String ACTION_START_LOCK_SERVICE = "com.mas.amineappstore.START_LOCK_SERVICE";
	public Long alarm_time = 10 * 1000L;
	public static boolean  isCreatAlarm = false ;
	
	public ApplockerAlarm(Context context) {
		this.context = context;
	}

	public static ApplockerAlarm getInstance(Context context) {
		if (applockerAlarm == null) {
			applockerAlarm = new ApplockerAlarm(context);
		}
		return applockerAlarm;
	}

	public void runAlarmTask() {
		new Thread(new AlarmTask()).start();
	}

	class AlarmTask implements Runnable {
		@Override
		public void run() {
			creatAlarm();
		}
	}

	private void creatAlarm() {
		Intent alarmIntent = new Intent(ApplockerAlarm.this.context, LockService.class);
		alarmIntent.setAction(ACTION_START_LOCK_SERVICE);

		PendingIntent alarmPendingIntent = PendingIntent.getService(this.context, 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager daemonAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		daemonAlarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), alarm_time,
				alarmPendingIntent);
		isCreatAlarm = true ;
	}
	/**
	 * 清除定时器
	 */
	public void cancelAlarm() {
		
		Intent alarmIntent = new Intent(ApplockerAlarm.this.context, LockService.class);
		alarmIntent.setAction(ACTION_START_LOCK_SERVICE);

		PendingIntent alarmPendingIntent = PendingIntent.getService(this.context, 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager daemonAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		daemonAlarmManager.cancel(alarmPendingIntent);
	}
}
