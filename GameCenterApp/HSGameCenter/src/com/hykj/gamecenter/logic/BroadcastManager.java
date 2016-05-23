
package com.hykj.gamecenter.logic;

import android.content.Intent;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.broadcast.KeyEventBroadcast;
import com.hykj.gamecenter.controller.ProtocolListener.RepAppsUpdateListener;
import com.hykj.gamecenter.controller.ReqAppsUpdateListController;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.utils.Logger;

public class BroadcastManager {

    private static final String TAG = "BroadcastManager";
    private static BroadcastManager mInstance;

    private final ApkBroadcastReceiver mApkBroadcastReceiver;
    private final NetworkMonitorReceiver mNetworkMonitorReceiver;
    private KeyEventBroadcast mKeyEventBroadcast = null;

    private int registerCount = 0;

    private static int downloadAndUpdateCount = 0;

    public static final String ACTION = "com.cs.launcher.RECEIVE_UNREAD";
    public static String packageAndClassName = "com.hykj.gamecenter" + "/"
            + "com.hykj.gamecenter.activity.HomePageActivity";

    private BroadcastManager() {
        mApkBroadcastReceiver = new ApkBroadcastReceiver();
        mNetworkMonitorReceiver = new NetworkMonitorReceiver();
        mKeyEventBroadcast = new com.hykj.gamecenter.broadcast.KeyEventBroadcast();
    }

    public static BroadcastManager getInstance() {
        if (mInstance == null) {
            mInstance = new BroadcastManager();
        }
        return mInstance;
    }

    /**
     * 注册监听系统按键广播
     */
    public void registerKeyEventReceive() {
        mKeyEventBroadcast.registerReceive();
    }

    /**
     * 注销监听系统按键广播
     */
    public void unregisterKeyEventReceive() {
        mKeyEventBroadcast.unRegisterReceive();
    }

    // 因为有计数器，注册和取消必须成对出现
    // 现在的有2�? splashactivity的oncreate + homepageactiviy的ondestroy
    // appmanageractivity的oncreate+ondestroy
    public void registerReceiveres() {

        Logger.d("resumeDownload", "registerReceiveres");
        if (registerCount == 0) {
            mApkBroadcastReceiver.registerReceiver();
            mNetworkMonitorReceiver.registerReceiver();
        }
        registerCount++;
    }

    public void unregisterReceiveres() {
        registerCount--;
        if (registerCount == 0) {
            mApkBroadcastReceiver.unregisterReceiver();
            mNetworkMonitorReceiver.unregisterReceiver();
        }
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }

    /**
     * 发�?下载数和更新数之和给桌面
     */
    public void sendBroadCastToDesk(boolean startFromService) {
        if (startFromService) {
            ReqAppsUpdateListController controller = new ReqAppsUpdateListController(
                    ApkInstalledManager.getInstance().getLocalAppsUpdateInfo(
                            true), mReqAppsUpdateListListener);
            controller.doRequest();
        } else {
            int updateCount = ApkInstalledManager.getInstance()
                    .getUpdateInfoNotInTaskCount();
            //不添加 下载应用, 只显示可更新应用
            /*int downloadCount = DownloadService.getDownloadManager()
            		.getTaskCount();*/
            int unread = /*downloadCount + */updateCount;

            Logger.i(TAG, "sendBroadCastToDesk + size " + unread + " downloadAndUpdateCount "
                    + downloadAndUpdateCount);

            //			if (downloadAndUpdateCount != unread) {
            downloadAndUpdateCount = unread;
            Logger.i(TAG, "sendBroadCastToDesk-----------");
            Intent intent2 = new Intent();
            intent2.putExtra("packageAndClassName", packageAndClassName);
            intent2.putExtra("unread", unread);
            intent2.setAction(ACTION);
            App.getAppContext().sendBroadcast(intent2);
            /*
             * App.getAppContext() .sendBroadcastAsUser(intent2,
             * UserHandle.ALL);
             */

            //			}

        }

    }

    // TODO
    private final RepAppsUpdateListener mReqAppsUpdateListListener = new RepAppsUpdateListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.d("CMD", "mReqAppsUpdateListListener onNetError errCode:"
                    + errCode + ",errorMsg:" + errorMsg);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Logger.d("CMD", "mReqAppsUpdateListListener onReqFailed errCode:"
                    + statusCode + ",errorMsg:" + errorMsg);
        }

        @Override
        public void onRepAppsUpdateSucceed(AppInfo[] infoes) {
            Intent intent2 = new Intent();
            intent2.putExtra("packageAndClassName", packageAndClassName);
            intent2.putExtra("unread", infoes.length);
            intent2.setAction(ACTION);
            App.getAppContext().sendBroadcast(intent2);
            /*
             * App.getAppContext() .sendBroadcastAsUser(intent2,
             * UserHandle.ALL);
             */
        }

    };
}
