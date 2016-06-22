
package com.hykj.gamecenter.broadcast;

/**
 * Wifi状态检查和启动状态检查处理
 *
 * @author Nurmuhammad
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.controller.ProtocolListener.RepAppsUpdateListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUpdateListener;
import com.hykj.gamecenter.controller.ReqAppsUpdateListController;
import com.hykj.gamecenter.controller.ReqUpdateController;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.logic.NotificationCenter;
import com.hykj.gamecenter.logic.entry.SystemAppInfo;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.protocol.Apps;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.LocalAppVer;
import com.hykj.gamecenter.protocol.Updater.RspUpdate;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.NetUtils;
import com.hykj.gamecenter.utils.PackageUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WifiUpdateReceiver extends BroadcastReceiver {
    public static String TAG = "WifiUpdateReceiver";
    private final WifiRepAppsUpdateListListener mReqAppsUpdateListListener = new WifiRepAppsUpdateListListener();
    private Context mContext = null;// App.getAppContext( );
    public static boolean WifiConnected = false;

    public boolean checkConnectivity(Context context) {
        return NetUtils.isNetworkconnected(context);
    }

    private static ArrayList<WifiListener> wifiListeners = new ArrayList<WifiListener>();
    public interface WifiListener{
        void networkChange(int currentNetwork);
    }

    public static void setWifiConnectListen(WifiListener wifiListener){
        if (!wifiListeners.contains(wifiListener)) {
            wifiListeners.add(wifiListener);
        }
    }

    public static void removeWifiListener(WifiListener wifiListener){
        if (wifiListeners.contains(wifiListener)) {
            wifiListeners.remove(wifiListener);
        }
    }

    private void notifyNewworkChange(int currentNetwork){
        for (WifiListener wifiListener :
                wifiListeners) {
            wifiListener.networkChange(currentNetwork);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null)
            return;
        mContext = context;
//        Logger.i(TAG, "Load onReceive", "oddshou");
//        Logger.i(TAG, "(intent.getAction())=============" + (intent.getAction()), "oddshou");
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                State state = networkInfo.getState();
                boolean isConnected = state == State.CONNECTED;// 当然，这边可以更精确的确定状态
                Logger.e(TAG, "isConnected" + isConnected);
                if (isConnected) {
                } else {

                }
            }
        }


        //获取wifi环境才下载游戏的布尔值
        boolean bWifiToDownload = App.getSettingContent().getSettingData().bWifiAutoDownload;

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            Logger.i(TAG, "(intent.getAction())=============" + (intent.getAction()));
            Logger.e(TAG, "android.net.conn.CONNECTIVITY_CHANGE onReceive");
            NetworkInfo info = APNUtil.getActiveNetwork(context);
            if (null != info) {//无网络时 info值为空
                int type = info.getType();//type = 0 为mobile 状态  = 1 为 wifi 状态
                String name = info.getTypeName();
                State state = info.getState();
                if (state == State.CONNECTED) {
                    if (type == 0 || name.equals("mobile")) {//mobile状态  停止下载
                        Logger.i(TAG, "mobile CONNECTED", "oddshou");
                        CSToast.showNormal(context, context.getString(R.string.wifi_mobile_link));
                        notifyNewworkChange(0);
                        if (bWifiToDownload) {
                            if (DownloadService.DOWNLOAD_MANAGER != null) {
                                DownloadService.DOWNLOAD_MANAGER.stopAllDownload();
//                                DownloadService.DOWNLOAD_MANAGER.saveAllTaskToDB();
                            }
                        }
                    } else if (type == 1 || name.equals("WIFI")) {
                        CSToast.showNormal(context, context.getString(R.string.wifi_link));
                        notifyNewworkChange(1);
                        WifiConnected = true;
                        // 网络恢复 继续下载中任务
                        //RestartDownloadingTask( );
                        // 网络恢复连接时显示新手推荐页面
                        if (bWifiToDownload) {
                            HomePageActivity.showNoviceGuidanceViewWhenNetRecover();

                            new Thread(new Runnable() {

                                @Override
                                public void run() { // 网络恢复 继续下载中任务
                                    RestartDownloadingTask();
                                }
                            }).start();
                        }
                    }
                }
            } else {
                //无网络
                CSToast.showNormal(context, context.getString(R.string.wifi_link_none));
                notifyNewworkChange(-1);
            }
        }

        mReqAppsUpdateListListener.setContext(context);
        SharedPreferences datePreference = mContext.getSharedPreferences("date_preferece",
                Context.MODE_PRIVATE);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        boolean shouldCheckUpdate = false;
        try {
            Date dateLast = df.parse(datePreference.getString("last_update_time", "1900-00-00"));
            dateLast.setTime(dateLast.getTime() + 24 * 3600 * 1000);
            //            Log.i(TAG, "dateLast-->" + dateLast.toString());
            Date dateNow = new Date();
            dateNow.setMinutes(0);
            dateNow.setHours(0);
            dateNow.setSeconds(0);
            //            Log.i(TAG, "dateNow-->" + dateNow.toString());
            if (dateNow.after(dateLast)) {
                shouldCheckUpdate = true;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //        Log.i(TAG, "shouldCheckupdate ? " + shouldCheckUpdate);

        if (!shouldCheckUpdate)
            return;

        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            HashMap<String, PackageInfo> mPackageInfoMap = new HashMap<String, PackageInfo>();
            ArrayList<String> deskAppInfo = new ArrayList<String>();
            PackageManager mPackageManager = mContext.getPackageManager();
            List<PackageInfo> packageInfoes = mPackageManager
                    .getInstalledPackages(PackageManager.GET_SIGNATURES);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                State state = networkInfo.getState();
                if (state == State.CONNECTED) {
                    if (mContext != null) {
                        datePreference = mContext.getSharedPreferences("date_preferece",
                                Context.MODE_PRIVATE);
                        Editor editor = datePreference.edit();
                        Date dateNow = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                                Locale.getDefault());
                        String last_update_time = "" + format.format(dateNow);
                        editor.putString("last_update_time", last_update_time);
                        Log.i(TAG, "last_update_time-->" + last_update_time);
                        editor.commit();
                    }
                    mPackageInfoMap.clear();
                    // 获取显示在桌面上的APP
                    List<ResolveInfo> mAllApps = null;
                    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                    mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
                    for (ResolveInfo rInfo : mAllApps) {
                        if (!deskAppInfo.contains(rInfo.activityInfo.packageName)) {
                            deskAppInfo.add(rInfo.activityInfo.packageName);
                        }
                    }
                    for (String appInfo : SystemAppInfo.allSystemApp()) {
                        if (deskAppInfo.contains(appInfo)) {
                            deskAppInfo.remove(appInfo);
                        }
                    }

                    for (String rInfo : deskAppInfo) {
                        for (PackageInfo pInfo : packageInfoes) {
                            if (pInfo.packageName.equals(rInfo)) {
                                mPackageInfoMap.put(pInfo.packageName, pInfo);
                            }
                        }
                    }

                    ArrayList<LocalAppVer> data = new ArrayList<LocalAppVer>();
                    for (PackageInfo pinfo : mPackageInfoMap.values()) {
                        /*LocalAppVer.Builder builder = LocalAppVer.newBuilder();
                        builder.setPackName(pinfo.packageName);
                        builder.setVerName(pinfo.versionName != null ? pinfo.versionName : "1.0");
                        builder.setVerCode(pinfo.versionCode);
                        builder.setSignCode(PackageUtil.getSign(App.getAppContext(),
                                pinfo.packageName));
                        data.add(builder.build());*/
                        LocalAppVer local = new LocalAppVer();
                        local.packName = pinfo.packageName;
                        local.verName = pinfo.versionName != null ? pinfo.versionName : "1.0";
                        local.verCode = pinfo.versionCode;
                        local.signCode = PackageUtil.getSign(App.getAppContext(),
                                pinfo.packageName);
                        data.add(local);
                    }
                    ReqAppsUpdateListController controller = new ReqAppsUpdateListController(data,
                            mReqAppsUpdateListListener);
                    controller.doRequest();
                    sendCheckUpdate();

                }
            }
        }
    }

    class WifiRepAppsUpdateListListener implements RepAppsUpdateListener {

        Context context;

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e(TAG, "mReqAppsUpdateListListener onNetError errCode:" + errCode + ",errorMsg:"
                    + errorMsg);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Logger.e(TAG, "mReqAppsUpdateListListener onReqFailed errCode:" + statusCode
                    + ",errorMsg:" + errorMsg);
        }

        @Override
        public void onRepAppsUpdateSucceed(AppInfo[] infoes) {
            if (infoes != null) {
                final HashMap<String, AppInfo> mUpdateAppInfoMap = new HashMap<String, Apps.AppInfo>();
                for (AppInfo info : infoes) {
                    mUpdateAppInfoMap.put(info.packName, info);
                }
                if (mUpdateAppInfoMap.size() == 0) {
                    return;
                }

                ArrayList<AppInfo> data = new ArrayList<Apps.AppInfo>();
                for (AppInfo info : mUpdateAppInfoMap.values()) {
                    data.add(info);
                }
                NotificationCenter.getInstance().sendUpdateNotificationCustom(data);
                /*
                 * if(mContext != null){s SharedPreferences datePreference
                 * =mContext
                 * .getSharedPreferences("date_preferece",Context.MODE_PRIVATE);
                 * Editor editor = datePreference.edit( ); editor.putLong(
                 * "last_update_time" , GregorianCalendar.getInstance(
                 * ).getTimeInMillis( )); Log.i( TAG , "update_time-->" +
                 * GregorianCalendar.getInstance( ).getTimeInMillis( ) );
                 * editor.commit( ); }
                 */
            }
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

    }

    private void sendCheckUpdate() {
        ReqUpdateController controller = new ReqUpdateController(mReqUpdateStoreListener);
        controller.doRequest();
    }

    private final ReqUpdateListener mReqUpdateStoreListener = new ReqUpdateListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Log.i(TAG, "mReqUpdateStoreListener --onNetError:" + errCode + " " + errorMsg);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Log.i(TAG, "mReqUpdateStoreListener --onReqFailed:" + statusCode + " " + errorMsg);
        }

        @Override
        public void onRequpdateSucceed(RspUpdate updateInfo) {
            if (mContext == null || updateInfo.updateType == 0) {
                return;
            } else {
                Intent intent = new Intent(mContext, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(HomePageActivity.SHOULD_CHECK_UPDATE, true);
                Logger.d("AppManagerActivity", "GOTO_UPDATE =" + true);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder nb = new Notification.Builder(mContext);
                nb.setContentTitle(mContext.getResources().getString(R.string.update_title));
                nb.setContentText(mContext.getResources().getString(R.string.appstore_update_text));
                nb.setSmallIcon(R.mipmap.icon);
                nb.setWhen(System.currentTimeMillis());
                // remoteView.notify( );
                // nb.setContent( remoteView );
                nb.setContentIntent(pendingIntent);
                nb.setOngoing(false);

                Notification notification = nb.getNotification();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE))
                        .notify(
                                PendingIntent.FLAG_UPDATE_CURRENT, nb.getNotification());
            }

        }
    };

    // 继续下载中任务
    public static void RestartDownloadingTask() {

        StatisticManager statisticManager = StatisticManager.getInstance();
        ApkDownloadManager apkDownloadManager = DownloadService.getDownloadManager();

        List<DownloadTask> downloadList = apkDownloadManager.getTaskList();

        for (DownloadTask task : downloadList) {
            Log.i(TAG, "task.getState()===" + task.getState());
            if (task != null) {
                switch (task.getState()) {

                /*
                 * case FAILED_NETWORK: // 网络错误
                 * apkDownloadManager.restartDownload(task);// FAILED_NETWORK //
                 * 从resumeDownload修改为restartDownload break;
                 */
                    case STOPPED:// 暂停状态
                    case FAILED_NETWORK:// 网络错误
                    case FAILED_SERVER:// 服务器繁忙
                    case FAILED_NOFREESPACE:
                        apkDownloadManager.resumeDownload(task);
                        // 暂停下载任务继续下载上报
                        ReportConstants.reportDownloadResume(task.appId, task.packId,
                                task.nFromPos,
                                ReportConstants.STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME, "");
                        MtaUtils.trackDownloadResume();
                        break;
                    case FAILED_BROKEN:// 文件损坏
                    case DELETED:
                        apkDownloadManager.restartDownload(task);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // 继续下载中任务
    public static void stopDownloadingTask() {

        StatisticManager statisticManager = StatisticManager.getInstance();
        ApkDownloadManager apkDownloadManager = DownloadService.getDownloadManager();

        List<DownloadTask> downloadList = apkDownloadManager.getTaskList();

        for (DownloadTask task : downloadList) {
            Log.i(TAG, "task.getState()===" + task.getState());
            if (task != null) {
                switch (task.getState()) {
                    case PREPARING:
                    case WAITING:
                    case STARTED:
                    case LOADING:
                        apkDownloadManager.stopDownload(task);
                        // 用户主动暂停下载任务上报
                        Logger.i("AppInfoActivity", "dinfo.packId=" + task.packId);
                        ReportConstants
                                .reportDownloadStop(
                                        task.appId,
                                        task.packId,
                                        task.nFromPos,
                                        ReportConstants.STAC_DOWNLOAD_APK_OTHERS_STOP,
                                        "");
                        // MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_USER_ACTIVE_STOP);
                        break;
                    case SUCCEEDED:
                        apkDownloadManager.installDownload(task);
                        break;
                    case STOPPED:
                    case FAILED_NETWORK:
                    case FAILED_SERVER:
                    case FAILED_NOFREESPACE:
                        /* apkDownloadManager.resumeDownload(task);
                         // 暂停下载任务继续下载上报
                         statisticManager
                                 .reportDownloadResume(
                                         task.appId,
                                         task.packId,
                                         dinfo.nFromPosStatisticManager.STAC_APP_POSITION_APP_DETAIL,
                                         StatisticManager.STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME,
                                         "");*/
                        break;
                    case FAILED_BROKEN:
                    case DELETED:
                        //                        apkDownloadManager.restartDownload(task);
                        break;
                    case FAILED_NOEXIST:
                        apkDownloadManager.removeDownload(task);
                        // 取消下载任务
                        ReportConstants.reportDownloadStop(task.appId,
                                task.packId, task.nFromPos
                                /*StatisticManager.STAC_APP_POSITION_APP_DETAIL*/,
                                ReportConstants.STAC_DOWNLOAD_APK_CANCEL_TASK, "");
                        //   MtaUtils.trackDownloadCancel(dinfo.appName);
                        break;

                    /*case STOPPED:// 暂停状态
                    case FAILED_NETWORK:// 网络错误
                    case FAILED_SERVER:// 服务器繁忙
                    case FAILED_NOFREESPACE:
                        apkDownloadManager.resumeDownload(task);
                        // 暂停下载任务继续下载上报
                        statisticManager.reportDownloadResume(task.appId, task.packId,
                                task.nFromPos,
                                StatisticManager.STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME, "");
                        MtaUtils.trackDownloadResume();
                        break;
                    case FAILED_BROKEN:// 文件损坏
                    case DELETED:
                        apkDownloadManager.restartDownload(task);
                        break;*/
                    default:
                        break;
                }
            }
        }
    }
}
