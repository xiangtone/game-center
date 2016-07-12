
package com.hykj.gamecenter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;

import com.hykj.gamecenter.services.AlarmManagerServiceManager;
import com.hykj.gamecenter.services.MonitorAppsUpdateServiceHelper;
import com.hykj.gamecenter.utils.AsyncHandler;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.ServiceWakeLock;

import java.util.ArrayList;

//服务 的管理类，用于接收相关广播然后 检查并启动 回复反馈服务类
public class MonitorAppsUpdateServiceManagerReceiver extends BroadcastReceiver {
    public static final String TAG = "MonitorAppsUpdateServiceManagerReceiver";

    public MonitorAppsUpdateServiceManagerReceiver(Context context) {
        super();
        initReceiveActionList();
        Logger.e(TAG, " MonitorAppsUpdateServiceManagerReceiver(context)  initReceiveActionList");
    }

    // 系统默认调用，在接受广播时，系统会调用该函数创建对象
    public MonitorAppsUpdateServiceManagerReceiver() {
        super();
        initReceiveActionList();
        Logger.e(TAG, " MonitorAppsUpdateServiceManagerReceiver()  initReceiveActionList");
    }

    private static ArrayList<String> arrayList = new ArrayList<String>();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final PendingResult result = goAsync();
        final WakeLock wl = ServiceWakeLock.createPartialWakeLock(context);
        wl.acquire();
        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                handleIntent(context, intent);
                result.finish();
                wl.release();
            }
        });
    }

    private void handleIntent(Context context, Intent intent) {

        String action = intent.getAction();
        Logger.e(TAG, "Received action: " + action);

        // 改用alarmManager和ACTION_BOOT_COMPLETED管理，alarmManager是系统底层的，进程被杀了仍然会收到广播
        // MonitorAppsUpdateServiceHelper.startMonitorAppsUpdateService(context);
        // Log.e(TAG,
        // "bActionStartMonitorAppsUpdateService(action)="+bActionStartMonitorAppsUpdateService(action));
        // 是否是匹配action
        Logger.e(TAG, "bActionStartMonitorAppsUpdateService: "
                + bActionStartMonitorAppsUpdateService(action));
        if (bActionStartMonitorAppsUpdateService(action)) {
            // 检查和启动 查询应用更新的服务
            MonitorAppsUpdateServiceHelper.startMonitorAppsUpdateService(context);
        }
        else {
            Logger.e(TAG, "Received unexpected intent " + intent.toString());
        }
    }

    // 当前action是否是需要启动MonitorFeedbackService
    private boolean bActionStartMonitorAppsUpdateService(String action) {
        for (String sAction : arrayList) {
            if (action != null && sAction != null && sAction.equals(action)) {
                return true;
            }
        }
        return false;
    }

    // 初始化接收action列表，需同时在xml中配置
    private void initReceiveActionList() {
        arrayList.clear();

        arrayList.add(Intent.ACTION_BOOT_COMPLETED);
        arrayList.add(AlarmManagerServiceManager.ACTION_ALARM_MANAGER_ARRIVAL);
        /*
         * arrayList.add(Intent.ACTION_TIME_TICK); //电量变化
         * arrayList.add(Intent.ACTION_BATTERY_CHANGED); //电话的信号强度已经改变
         * arrayList.add("android.intent.action.SIG_STR"); //设置时间、时区时触发
         * arrayList.add("android.intent.action.TIME_SET");
         * arrayList.add(Intent.ACTION_TIMEZONE_CHANGED);
         * arrayList.add(Intent.ACTION_LOCALE_CHANGED); //DATE_CHANGED
         * 不只监听用户手动更改日期，也监听系统日期的自动变化（0点）
         * arrayList.add(Intent.ACTION_DATE_CHANGED); //亮屏、黑屏、解锁事件的系统广播接收
         * arrayList.add(Intent.ACTION_SCREEN_ON);
         * arrayList.add(Intent.ACTION_SCREEN_OFF);
         * arrayList.add(Intent.ACTION_USER_PRESENT);
         * //来去电事件监听，需要android.permission.READ_PHONE_STATE权限
         * arrayList.add("android.intent.action.PHONE_STATE"); //开关Wifi时触发
         * arrayList.add("android.net.wifi.WIFI_STATE_CHANGED");
         */
    }
}
