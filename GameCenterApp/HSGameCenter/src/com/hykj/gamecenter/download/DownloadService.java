
package com.hykj.gamecenter.download;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.List;

//#########oddshou 只在 onDestroy 停止 ApkDownloadManager
public class DownloadService extends Service {
    public static ApkDownloadManager DOWNLOAD_MANAGER;

    public static ApkDownloadManager getDownloadManager() {
        if (!DownloadService.isServiceRunning()) {
            Context appContext = App.getAppContext();

//            Intent downloadSvr = new Intent("com.sjhd.gamecenter.download.service.action");
            //android 5.0 以上service 需要显示启动
            Intent downloadSvr = new Intent(appContext, DownloadService.class);
            appContext.startService(downloadSvr);
        }
        if (DownloadService.DOWNLOAD_MANAGER == null) {
            DownloadService.DOWNLOAD_MANAGER = new ApkDownloadManager();
        }

        return DOWNLOAD_MANAGER;
    }

    public DownloadService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    // 调用stopService才会出发onDestroy
    @Override
    public void onDestroy() {
        LogUtils.e("DownloadService is destroy!");

        if (DOWNLOAD_MANAGER != null) {
            DOWNLOAD_MANAGER.stopAllDownload();
            // TODO: 2015/12/23 注意这个调用
//            DOWNLOAD_MANAGER.saveAllTaskToDB();
        }

        super.onDestroy();
    }

    public static boolean isServiceRunning() {
        boolean isRunning = false;

        Context context = App.getAppContext();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (serviceList.size() < 1) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(
                    DownloadService.class.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
