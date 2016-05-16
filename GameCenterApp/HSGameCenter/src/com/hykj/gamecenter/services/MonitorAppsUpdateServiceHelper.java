
package com.hykj.gamecenter.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.hykj.gamecenter.utils.Logger;

//MonitorAppsUpdateServiceManagerReceiver 是 查询回复反馈的服务 的帮助类
public class MonitorAppsUpdateServiceHelper {
    private static final String TAG = "MonitorFeedbackServiceHelper";

    /*
     * Intent.ACTION_TIME_TICK的使用 我们知道广播的注册有静态注册和动态注册， 但此系统广播只能通过动态注册的方式使用。
     * 即你不能通过在manifest.xml里注册的方式接收到这个广播， 只能在代码里通过registerReceiver()方法注册。
     */
    // 注册ACTION_TIME_TICK的监听,为了保持服务的启动
    public static Intent registerTimeTickReceiver(Context context,
            BroadcastReceiver connectionReceiver) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        return context.registerReceiver(connectionReceiver, filter);
    }

    // 注销ACTION_TIME_TICK的监听
    public static void unRegisterTimeTickReceiver(Context context,
            BroadcastReceiver connectionReceiver) {
        if (connectionReceiver != null) {
            context.unregisterReceiver(connectionReceiver);
        }
    }

    // 检查和启动MonitorFeedbackService服务
    public static void startMonitorAppsUpdateService(Context context) {
        Intent i = new Intent(context, MonitorAppsUpdateService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);
        Logger.e("MonitorAppsUpdateServiceManagerReceiver", "start MonitorAppsUpdateService");


//        boolean isServiceRunning = false;
//
//        ActivityManager manager = (ActivityManager) context
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        for (RunningServiceInfo service : manager
//                .getRunningServices(Integer.MAX_VALUE)) {
//
//            // Log.d(TAG,"service.ClassName: "+service.service.getClassName());
//
//            // Service的类名
//            if (/*
//                 * service.service.getClassName().contains(
//                 * "MonitorAppsUpdateService")
//                 */
//            "com.android.services.MonitorAppsUpdateService"
//                    .equals(service.service.getClassName())) {
//                isServiceRunning = true;
//                break;
//            }
//        }
//        Logger.e("MonitorAppsUpdateServiceManagerReceiver", "isServiceRunning=" + isServiceRunning);
//
//        if (isServiceRunning) {
//            // 关闭再启动,这样才能执行查询
////            stopMonitorAppsUpdateService(context);
//            isServiceRunning = false;
//        }
//        if (!isServiceRunning) {
//            Intent i = new Intent(context, MonitorAppsUpdateService.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startService(i);
//            Logger.e("MonitorAppsUpdateServiceManagerReceiver", "start MonitorAppsUpdateService");
//        }
    }

    // 停止MonitorAppsUpdateService服务
    public static void stopMonitorAppsUpdateService(Context context) {
        context.stopService(new Intent(context, MonitorAppsUpdateService.class));
    }
}
