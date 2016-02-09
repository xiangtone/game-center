package com.x.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.StaticLayout;

import com.x.business.alarm.AlarmManage;
import com.x.business.alarm.ApplockerAlarm;
import com.x.business.applocker.LockManager;
import com.x.business.applocker.LockService;
import com.x.business.notification.AdNotificationManager;
import com.x.business.settings.SettingModel;
import com.x.business.statistic.DataEyeHelper;
import com.x.business.statistic.StatisticManager;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.floating.FloatService;

/**
 * 
* @ClassName: BootReceiver
* @Description: 开机自启广播

* @date 2014-6-3 下午4:59:39
*
 */
public class BootReceiver extends BroadcastReceiver {

	public static final String FIRST_ALARM_ACTION = "com.mas.amineappstore.alarm.FIRST";
	public static final String SECOND_ALARM_ACTION = "com.mas.amineappstore.alarm.SECOND";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
		 
			//推送定时
			AdNotificationManager.getInstance().runAlarmTask(context);
			
			//applock组件
			LockManager.getInstance(context).startApplockerComponent();

			if (!StatisticManager.getInstance().iszAppInSystem(context))
				return;
			if (!StatisticManager.getInstance().isActivateServerSuccess(context)) { // 未激活成功
				if (StatisticManager.getInstance().isAlreadyActivateDevice(context)) { //手机已激活,上传失败
					StatisticManager.getInstance().activateDevice(context);
				} else { //手机未激活 

					if (!StatisticManager.getInstance().isFirstAlarmSuccess(context)) {
						AlarmManage.getInstance(context).createFirstAlarm();
						LogUtil.getLogger().d("BootReceiver", "createFirstAlarm()");
					} else {
						AlarmManage.getInstance(context).createSecondAlarm();
						LogUtil.getLogger().d("BootReceiver", "createSecondAlarm()");
					}
				}
			}

		} else if (intent.getAction().equals(FIRST_ALARM_ACTION)) {
			StatisticManager.getInstance().setFirstAlarmSuccess(context);
			AlarmManage.getInstance(context).createSecondAlarm();
			LogUtil.getLogger().d("BootReceiver", "FIRST_ALARM_ACTION");
		} else if (intent.getAction().equals(SECOND_ALARM_ACTION)) {
			StatisticManager.getInstance().setDeviceActivateTime(context);
			StatisticManager.getInstance().activateDevice(context);
			LogUtil.getLogger().d("BootReceiver", "SECOND_ALARM_ACTION");
		}
	}
}
