
package com.hykj.gamecenter.logic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.controller.ProtocolListener.RepAppsUpdateListener;
import com.hykj.gamecenter.controller.ReqAppsUpdateListController;
import com.hykj.gamecenter.data.SettingContent;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTaskManager;
import com.hykj.gamecenter.logic.entry.SystemAppInfo;
import com.hykj.gamecenter.protocol.Apps;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.LocalAppVer;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PackageUtil;
import com.hykj.gamecenter.utils.PackageUtils;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * 处理apk的安装、卸载、更新信息
 * 
 * @author froyohuang
 */

public class ApkInstalledManager {

    private static final String TAG = "ApkInstalledManager";

    private static ApkInstalledManager mInstance;

    private static ApkDownloadManager mApkDownloadManager;
    private static PackageManager mPackageManager;

    private final static ArrayList<InstallFinishListener> listenerList = new ArrayList<InstallFinishListener>();

    // packageName 对应packageInfo的缓存 ,HashMap是非同步的(非线程安全)
    private final static HashMap<String, PackageInfo> mPackageInfoMap = new HashMap<String, PackageInfo>();
    // name 对应应用第一个activity的class name
    private final static HashMap<String, String> mActivityInfoMap = new HashMap<String, String>();
    // packageName 对应可更新appinfo缓存
    private final static HashMap<String, AppInfo> mUpdateAppInfoMap = new HashMap<String, Apps.AppInfo>();

    // 操作同步锁，保证操作mPackageInfoMapLock的方法同时只有一个执行
    public static final Object mPackageInfoMapLock = new Object();
    public static final Object mActivityInfoMapLock = new Object();
    public static final Object mUpdateAppInfoMapLock = new Object();

    public static final Object mUiRefreshHandlerListLock = new Object();
    /** 用于通知UI当前下载进度的队列 */
    private final Vector<Handler> mUiRefreshHandlerList = new Vector<Handler>(); // 需要刷新应用更新个数的界面集合

    private ApkInstalledManager() {
        LogUtils.e("ApkInstalledManager is onCreate");
        mApkDownloadManager = DownloadService.getDownloadManager();
        mPackageManager = App.getAppContext().getPackageManager();

    }

    // modify at 20131128
    private void sendUiInstallFinish(DownloadTask info) {
        for (InstallFinishListener listener : listenerList) {
            listener.apkInstallFinish(info);
        }
    }

    public synchronized static ApkInstalledManager getInstance() {
        if (mInstance == null) {
            mInstance = new ApkInstalledManager();
        }
        return mInstance;
    }

    /*
     * 载入非系统应用,这里会查询所有需要更新的应用 , 因为后台需要查询应用更新， 有可能会同时操作 loadLocalApps
     * loadApps中的数据结构 ，所以方法需要synchronized
     */
    public void loadApps() {
        // 耗时操作，稍后需放入线程中
        loadLocalApps();
        sendUpdateReq(false, null);
        filterIntalledDownLoadTask();
//        sendUiRefreshHandler();       //移到请求完成后执行
        // 判断当前应用是否在前台运行
        /*
         * if (Utils.IsActivityRunning(App.getAppContext(), "com.niuwan.gamecenter"))
         * { // FeedBackActivity正在运行，发送有新消息到来的消息 }
         */

    }

    // add new start
    public void addInstallListener(InstallFinishListener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    public void removeInstallListener(InstallFinishListener listener) {
        if (!listenerList.contains(listener)) {
            return;
        }
        listenerList.remove(listener);
    }

    // add new end
    // 获取显示在桌面上的APP
    private ArrayList<String> getDeskApp() {

        ArrayList<String> deskAppInfo = new ArrayList<String>();
        List<ResolveInfo> mAllApps = null;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        synchronized (mActivityInfoMapLock) {
            mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
            for (ResolveInfo rInfo : mAllApps) {
                if (!deskAppInfo.contains(rInfo.activityInfo.packageName)) {
                    deskAppInfo.add(rInfo.activityInfo.packageName);
                    mActivityInfoMap.put(rInfo.activityInfo.packageName, rInfo.activityInfo.name);
                    // TODO 用于打开桌面上的程序
                    // String name = rInfo.activityInfo.name;
                    // LogUtils.e( "应用第一个activity的name=" + name );
                }
            }
            for (String appInfo : SystemAppInfo.allSystemApp()) {
                if (deskAppInfo.contains(appInfo)) {
                    deskAppInfo.remove(appInfo);
                    mActivityInfoMap.remove(appInfo);
                }
            }
        }
        // LogUtils.e( "本地应用拼接成的字符串:" + deskAppInfo.toString( ) );
        return deskAppInfo;
    }

    // 因为后台需要查询应用更新， 有可能会同时操作 loadLocalApps 中的数据结构
    // ，所以方法需要synchronized,保证互斥访问mPackageInfoMap，mActivityInfoMap
    private void loadLocalApps() {
        // 加载前先清理

        synchronized (mPackageInfoMapLock) {
            mPackageInfoMap.clear();
            synchronized (mActivityInfoMapLock) {
                mActivityInfoMap.clear();
            }
            if (null == mPackageManager) {
                mPackageManager = App.getAppContext().getPackageManager();
            }
            List<PackageInfo> packageInfoes = mPackageManager
                    .getInstalledPackages(PackageManager.GET_SIGNATURES);
            for (String rInfo : getDeskApp()) {
                for (PackageInfo pInfo : packageInfoes) {
                    if (pInfo.packageName.equals(rInfo)) {
                        // LogUtils.e( "本地应用:" + pInfo.packageName );
                        mPackageInfoMap.put(pInfo.packageName, pInfo);
                    }
                }
            }
        }
    }

    // 查询应用更新，isInstalledReport = true：查询当前应用，false:查询所有应用
    public void sendUpdateReq(boolean isInstalledReport, PackageInfo info) {
        ReqAppsUpdateListController controller = null;
        if (isInstalledReport) // 当安装的时候上报安装的应用
        {
            Logger.e(TAG, "当安装的时候上报安装的应用");
            ArrayList<LocalAppVer> data = new ArrayList<Apps.LocalAppVer>();
            if (info != null) {
                /*LocalAppVer.Builder builder = LocalAppVer.newBuilder( );
                builder.setPackName( info.packageName );
                builder.setVerName( info.versionName != null ? info.versionName : "1.0" );
                builder.setVerCode( info.versionCode );
                builder.setSignCode( PackageUtil.getSign( App.getAppContext( ) , info.packageName ) );*/
                LocalAppVer local = new LocalAppVer();
                local.packName = info.packageName;
                local.verName = info.versionName != null ? info.versionName : "1.0";
                local.verCode = info.versionCode;
                local.signCode = PackageUtil.getSign(App.getAppContext(), info.packageName);
                data.add(local);

                /*
                 * Log.d(TAG, "2 Sign = " +
                 * PackageUtil.getSign(App.getAppContext(), info.packageName) +
                 * " packageName = " + info.packageName);
                 */
                //                data.add(builder.build());
            }
            controller = new ReqAppsUpdateListController(data, mReqAppsUpdateListListener);
        }
        else
        // 上报所有的应用
        {
            Logger.e("MonitorAppsUpdateServiceManagerReceiver", "上报所有的应用");
            controller = new ReqAppsUpdateListController(getLocalAppsUpdateInfo(false),
                    mReqAppsUpdateListListener);
        }
        if (controller != null) {
            controller.doRequest();
        }
    }

    // 批量获取更新数据
    // TODO
    private final RepAppsUpdateListener mReqAppsUpdateListListener = new RepAppsUpdateListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            Logger.e("MonitorAppsUpdateServiceManagerReceiver",
                    "mReqAppsUpdateListListener onNetError errCode:" + errCode
                            + ",errorMsg:" + errorMsg);
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            Logger.e("MonitorAppsUpdateServiceManagerReceiver",
                    "mReqAppsUpdateListListener onReqFailed errCode:" + statusCode
                            + ",errorMsg:" + errorMsg);
        }

        @Override
        public void onRepAppsUpdateSucceed(AppInfo[] infoes) {
            Logger.e("MonitorAppsUpdateServiceManagerReceiver", "更新 infoes 的大小:" + infoes.length,
                    "oddshou");
            if (infoes.length != 0) {
                for (AppInfo info : infoes) {
                    synchronized (mUpdateAppInfoMapLock) {
                        mUpdateAppInfoMap.put(info.packName, info);
                    }
                }
                Logger.e("MonitorAppsUpdateServiceManagerReceiver",
                        "onRepAppsUpdateSucceed,发送更新信息到注册界面");
//                sendUiRefreshHandler(); // 界面刷新放到安装完成之后, 如果 infoes.length == 0 将无法进入

                Logger.e("MonitorAppsUpdateServiceManagerReceiver",
                        "onRepAppsUpdateSucceed notificationShow = "
                                + GlobalConfigControllerManager.getInstance().isNotificationShow());
                if (GlobalConfigControllerManager.getInstance().isNotificationShow()) {
                    // 非回退键进入的HomePageActivity界面发送通知
                    sendUpdateNotification();// notify通知栏
                }
                sendUiRefreshHandler();
            }
        }

    };

    public void addUiRefreshHandler(Handler handler) {
        synchronized (mUiRefreshHandlerListLock) {
            mUiRefreshHandlerList.add(handler);
        }
    }

    public void removeUiRefreshHandler(Handler handler) {
        synchronized (mUiRefreshHandlerListLock) {
            if (mUiRefreshHandlerList.contains(handler)) {
                mUiRefreshHandlerList.remove(handler);
            }
        }
    }

    private void sendUiRefreshHandler() {
        synchronized (mUiRefreshHandlerListLock) {
            for (Handler handler : mUiRefreshHandlerList) {
                handler.sendEmptyMessage(MSG_CONSTANTS.MSG_CHECK_UPDATE);
            }
        }
    }

    // 处理应用安装
    public void onApkInstalled(Context context, Intent intent) {
        String packageName = intent.getDataString();
        // 广播里取的packageName是package:com.xxx.xxx，需要把前面的package:去掉，剩下的才是包名
        if (packageName != null && packageName.length() > 0) {
            packageName = packageName.substring(packageName.lastIndexOf(':') + 1);
        }
        loadLocalApps();
        synchronized (mUpdateAppInfoMapLock) {
            mUpdateAppInfoMap.remove(packageName);
        }

        // ################  tomqian 应用安装结束后，发出系统广播，此处使用apkInstallFinish接口回调
        DownloadTask di = mApkDownloadManager.getDownloadTaskByPackageName(packageName);
        sendUiInstallFinish(di);
        // ################  tomqian

        // 如果设置打开了安装成功后删除安装文件，这里需要删除一下
        if (SettingContent.getInstance().getSettingData().bDeletePackage) {
            mApkDownloadManager.removeDownload(packageName);
            Logger.e(TAG, "removeDownload:" + packageName);
        }
        else {
            mApkDownloadManager.removeDownloadTask(packageName);
            Logger.e(TAG, "removeDownloadTask:" + packageName);
        }

        // 安装成功是发送消息给桌面下载数发生改变
        BroadcastManager.getInstance().sendBroadCastToDesk(false);
        // 修改AppManagerList安装完成的时候界面刷新的问题
        sendUpdateReq(true, getPackageinfoByName(packageName));
        sendUiRefreshHandler(); // 界面刷新放到安装完成之后
    }

    private PackageInfo getPackageinfoByName(String packageName) {

        synchronized (mPackageInfoMapLock) {
            Iterator<Entry<String, PackageInfo>> iter = mPackageInfoMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, PackageInfo> entry = iter.next();
                PackageInfo val = entry.getValue();
                if (packageName.equals(val.packageName)) {
                    return val;
                }
            }
            return null;
        }
    }

    public void onApkUnInstalled(Context context, Intent intent) {
        String packageName = intent.getDataString();

        LogUtils.e("onApkUnInstalled packageName= " + packageName);

        // 广播里取的packageName是package:com.xxx.xxx，需要把前面的package:去掉，剩下的才是包名
        if (packageName != null && packageName.length() > 0) {
            packageName = packageName.substring(packageName.lastIndexOf(':') + 1);
        }
        synchronized (mPackageInfoMapLock) {
            mPackageInfoMap.remove(packageName);
            synchronized (mActivityInfoMapLock) { // 其他嵌套地方注意防止死锁
                mActivityInfoMap.remove(packageName);
            }
            synchronized (mUpdateAppInfoMapLock) {
                mUpdateAppInfoMap.remove(packageName);
            }
        }
        LogUtils.e("UnInstalled,发送更新信息到注册界面");
        sendUiRefreshHandler();
        // 发送更新请求
        // sendUpdateReq( );
    }

    public boolean isApkLocalInstalled(String packageName) {
        synchronized (mPackageInfoMapLock) {
            boolean isInstalled = (mPackageInfoMap.get(packageName) != null);
//            Logger.i(TAG, "isApkLocalInstalled " + "name " + packageName + "  " + isInstalled, "oddshou");
            return isInstalled;
        }
    }

    public String getActivityClassName(String packageName) {
        synchronized (mActivityInfoMapLock) {
            return mActivityInfoMap.get(packageName);
        }
    }

    // 是否需要更新
    public boolean isApkNeedToUpdate(String packageName) {
        synchronized (mUpdateAppInfoMapLock) {
            return mUpdateAppInfoMap.get(packageName) != null;
        }
    }

    public synchronized ArrayList<LocalAppVer> getLocalAppsUpdateInfo(boolean fromService) {
        if (fromService) {
            loadLocalApps();
        }
        ArrayList<LocalAppVer> data = new ArrayList<LocalAppVer>();

        synchronized (mPackageInfoMapLock) {
            for (PackageInfo pinfo : mPackageInfoMap.values()) {
                /*LocalAppVer.Builder builder = LocalAppVer.newBuilder();
                builder.setPackName(pinfo.packageName);
                builder.setVerName(pinfo.versionName != null ? pinfo.versionName : "1.0");
                builder.setVerCode(pinfo.versionCode);

                builder.setSignCode(PackageUtil.getSign(App.getAppContext(), pinfo.packageName));
                data.add(builder.build());*/

                LocalAppVer local = new LocalAppVer();
                local.packName = pinfo.packageName;
                local.verName = pinfo.versionName != null ? pinfo.versionName : "1.0";
                local.verCode = pinfo.versionCode;
                local.signCode = PackageUtil.getSign(App.getAppContext(), pinfo.packageName);
                data.add(local);
            }
            Logger.i(TAG, "本地应用的数据量：" + data.size());
            return data;
        }
    }

    public ArrayList<AppInfo> getAppUpdateInfo() {
        ArrayList<AppInfo> data = new ArrayList<Apps.AppInfo>();
        synchronized (mUpdateAppInfoMapLock) {
            for (AppInfo info : mUpdateAppInfoMap.values()) {
                data.add(info);
            }
        }
        return data;
    }

    // 得到正在任务中的数量
    // 此方法中出现异常 java.util.ConcurrentModificationException
    public int getUpdateInfoNotInTaskCount() {
        int i = 0;

        synchronized (mUpdateAppInfoMapLock) {
            for (AppInfo info : mUpdateAppInfoMap.values()) {
                if (!mApkDownloadManager.isAppInTaskList(info.appId)) {
                    i++;
                }
            }
        }
        return i;
    }

    public boolean canUpdate(AppInfo ainfo) {
        boolean isNullAInfo = (ainfo != null);
        synchronized (mUpdateAppInfoMapLock) {

            boolean isUpdateAppInfoMap = (mUpdateAppInfoMap != null);
            boolean isUpdateContainsKey = (mUpdateAppInfoMap.containsKey(ainfo.packName));
            return isNullAInfo && isUpdateAppInfoMap && isUpdateContainsKey;
        }
    }

    public String getInstallVersionNameByPackageName(String packageName) {
        synchronized (mPackageInfoMapLock) {
            if (mPackageInfoMap.get(packageName) == null) {
                return "1.0";
            }
            else {
                return mPackageInfoMap.get(packageName).versionName;
            }
        }
    }

    public boolean hasUpdateOrDownload() {
        synchronized (mUpdateAppInfoMapLock) {
            return mApkDownloadManager.getTaskCount() + mUpdateAppInfoMap.size() > 0;
        }
    }

    public int getUpdateAppInfoCount() {
        synchronized (mUpdateAppInfoMapLock) {
            return mUpdateAppInfoMap.size();
        }
    }

    public boolean hasUpdate() {
        synchronized (mUpdateAppInfoMapLock) {
            return mUpdateAppInfoMap.size() > 0;
        }
    }

    private void sendUpdateNotification() {

        synchronized (mUpdateAppInfoMapLock) {
            if (mUpdateAppInfoMap.size() == 0) {
                return;
            }
        }
        /*
         * NotificationCenter.getInstance().sendUpdateNotification(
         * getAppUpdateInfo());
         */
        NotificationCenter.getInstance().sendUpdateNotificationCustom(getAppUpdateInfo());

    }

    // 管理静默安装相关
    private final HashMap<DownloadTask, SilentInstallResult> silentInstallResultMap = new HashMap<DownloadTask, ApkInstalledManager.SilentInstallResult>();
    //putInstallSuccessMsg 与 putInstallErrorMsg 是一样的可以删除一个 ########oddshou
    public void putInstallSuccessMsg(DownloadTask dInfo, String msg) {
        SilentInstallResult ret;
        if (silentInstallResultMap.containsKey(dInfo)) {
            ret = silentInstallResultMap.get(dInfo);
        }
        else {
            ret = new SilentInstallResult();
        }
        ret.successMsg = msg;
        silentInstallResultMap.put(dInfo, ret);
        LogUtils.i("putInstallSuccessMsg");
        checkInstallResut(ret, dInfo);
    }

    public void putInstallErrorMsg(DownloadTask dInfo, String msg) {
        SilentInstallResult ret;
        if (silentInstallResultMap.containsKey(dInfo)) {
            ret = silentInstallResultMap.get(dInfo);
        }
        else {
            ret = new SilentInstallResult();
        }
        ret.errorMsg = msg;
        silentInstallResultMap.put(dInfo, ret);
        LogUtils.i("putInstallErrorMsg");
        checkInstallResut(ret, dInfo);
    }

    public void putInstallAsus(DownloadTask dInfo, int result) {
        if (result == 1) {
            sendUiInstallFinish(dInfo);
            sendUiToastHandler(App.getAppContext().getString(R.string.install_succeed,
                    dInfo.appName));
        }else {
            PackageUtils.installNormal(App.getAppContext(), dInfo.fileSavePath);
        }
    }

    private void checkInstallResut(SilentInstallResult result, DownloadTask dInfo) {
        LogUtils.e("result.successMsg = " + result.successMsg);
        LogUtils.e("result.errorMsg = " + result.errorMsg);
        // successMsg和errorMsg都填入了，则说明安装结束了(可能是成功或者失败)
        if (!TextUtils.isEmpty(result.successMsg) && !TextUtils.isEmpty(result.errorMsg)) {
            if ((result.successMsg.contains("Success") || result.successMsg.contains("success"))) {
                result.result = PackageUtils.INSTALL_SILENT_SUCCEEDED;
                silentInstallResultMap.remove(dInfo);
                sendUiInstallFinish(dInfo);
                sendUiToastHandler(App.getAppContext().getString(R.string.install_succeed,
                        dInfo.appName));
                LogUtils.e(dInfo.appName + ": 安装成功");
                return;
            }

            if (result.errorMsg.contains("INSTALL_FAILED_ALREADY_EXISTS")) {
                result.result = PackageUtils.INSTALL_FAILED_ALREADY_EXISTS;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_INVALID_APK")) {
                result.result = PackageUtils.INSTALL_FAILED_INVALID_APK;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_INVALID_URI")) {
                result.result = PackageUtils.INSTALL_FAILED_INVALID_URI;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
                result.result = PackageUtils.INSTALL_FAILED_INSUFFICIENT_STORAGE;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_DUPLICATE_PACKAGE")) {
                result.result = PackageUtils.INSTALL_FAILED_DUPLICATE_PACKAGE;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_NO_SHARED_USER")) {
                result.result = PackageUtils.INSTALL_FAILED_NO_SHARED_USER;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")) {
                result.result = PackageUtils.INSTALL_FAILED_UPDATE_INCOMPATIBLE;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE")) {
                result.result = PackageUtils.INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY")) {
                result.result = PackageUtils.INSTALL_FAILED_MISSING_SHARED_LIBRARY;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE")) {
                result.result = PackageUtils.INSTALL_FAILED_REPLACE_COULDNT_DELETE;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_DEXOPT")) {
                result.result = PackageUtils.INSTALL_FAILED_DEXOPT;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_OLDER_SDK")) {
                result.result = PackageUtils.INSTALL_FAILED_OLDER_SDK;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_CONFLICTING_PROVIDER")) {
                result.result = PackageUtils.INSTALL_FAILED_CONFLICTING_PROVIDER;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_NEWER_SDK")) {
                result.result = PackageUtils.INSTALL_FAILED_NEWER_SDK;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_TEST_ONLY")) {
                result.result = PackageUtils.INSTALL_FAILED_TEST_ONLY;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE")) {
                result.result = PackageUtils.INSTALL_FAILED_CPU_ABI_INCOMPATIBLE;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_MISSING_FEATURE")) {
                result.result = PackageUtils.INSTALL_FAILED_MISSING_FEATURE;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_CONTAINER_ERROR")) {
                result.result = PackageUtils.INSTALL_FAILED_CONTAINER_ERROR;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION")) {
                result.result = PackageUtils.INSTALL_FAILED_INVALID_INSTALL_LOCATION;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE")) {
                result.result = PackageUtils.INSTALL_FAILED_MEDIA_UNAVAILABLE;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT")) {
                result.result = PackageUtils.INSTALL_FAILED_VERIFICATION_TIMEOUT;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_VERIFICATION_FAILURE")) {
                result.result = PackageUtils.INSTALL_FAILED_VERIFICATION_FAILURE;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_PACKAGE_CHANGED")) {
                result.result = PackageUtils.INSTALL_FAILED_PACKAGE_CHANGED;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_UID_CHANGED")) {
                result.result = PackageUtils.INSTALL_FAILED_UID_CHANGED;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_NOT_APK")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_NOT_APK;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_MANIFEST")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_BAD_MANIFEST;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_NO_CERTIFICATES;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_MALFORMED")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            }
            if (result.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_EMPTY")) {
                result.result = PackageUtils.INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
            }
            if (result.errorMsg.contains("INSTALL_FAILED_INTERNAL_ERROR")) {
                result.result = PackageUtils.INSTALL_FAILED_INTERNAL_ERROR;
            }

            result.result = PackageUtils.INSTALL_FAILED_OTHER;
            LogUtils.i("静默安装调用，dInfo.appName=" + dInfo.appName);
            silentInstallResultMap.remove(dInfo);
            PackageUtils.installNormal(App.getAppContext(), dInfo.fileSavePath);
        }
    }

    private void sendUiToastHandler(String msgStr) {
        synchronized (mUiRefreshHandlerListLock) {
            for (Handler handler : mUiRefreshHandlerList) {
                Message msg = handler.obtainMessage(MSG_CONSTANTS.MSG_CHECK_TOAST);
                msg.obj = msgStr;
                handler.sendMessage(msg);
            }
        }
    }

    public synchronized void releaseApkInstallManager() {
        mInstance = null;
    }

    public class SilentInstallResult {

        /** result of command **/
        public int result;
        /** success message of command result **/
        public String successMsg;
        /** error message of command result **/
        public String errorMsg;

        public SilentInstallResult() {
            result = PackageUtils.INSTALL_SILENT_PENDING;
        }

        public SilentInstallResult(int result) {
            this.result = result;
        }

        public SilentInstallResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }

    public interface InstallFinishListener {
        /**
         * @author greatzhang 安装完成!
         */
        void apkInstallFinish(DownloadTask info);
    }

    /**
     * 当下载完成后有任务在安装时，直接强制关闭应用商店，下载任务列表不同步问题
     */
    // 保证互斥访问mPackageInfoMap
    public synchronized void filterIntalledDownLoadTask() {
        LogUtils.e("检测任务开始");
        synchronized (mPackageInfoMapLock) {
            for (PackageInfo packInfo : mPackageInfoMap.values()) {
                    if (!isApkNeedToUpdate(packInfo.packageName)) {
                    DownloadTaskManager.getInstance().removeTaskByPackageName(packInfo.packageName);
                }
            }
        }
    }
}
