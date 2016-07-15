
package com.hykj.gamecenter;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import com.hykj.gamecenter.controller.ControllerHelper;
import com.hykj.gamecenter.data.SettingContent;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.logic.ApkBroadcastReceiver;
import com.hykj.gamecenter.logic.BroadcastManager;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PackageUtil;
import com.hykj.gamecenter.utilscs.FileUtils;
import com.hykj.gamecenter.utilscs.LogUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.android.tpush.XGPushManager;

import org.xutils.x;


public class App extends Application {

    private static final String TAG = "App";

    private static App mAppContext = null;
    private static int mVerCode;
    private static String mVerName;
    private static String mPackageName;
    private static String mChNoSelf;

    public static String getmChNoSelf() {
        return mChNoSelf;
    }

    public static int getmClientId() {
        return mClientId;
    }

    private static int mClientId;

    public static boolean ismAllGame() {
        return mAllGame;
    }

    private static boolean mAllGame = false;    //按clientId区分是否全是游戏
    public static SharedPreferences mSharePrefences;
    private static SettingContent mSettingContent;

    public final static String SHARED_PERFERENCE_NAME = "appstore.conf";
    private final static String PERFERENCE_SHORTCUT = "shortcut";
    private static final String DB_NAME = "cssf.db";
    public final static String SHARED_FIRST_LAUNCH_NAME = "launched";

    public static int PAD = 0;
    public static int PHONE = 1;

    private static boolean bDebugMode = false;
    private static String debugLogFilePath = null;

    private static int chnNo = 0;

    private static ApkBroadcastReceiver mApkBroadcastReceiver;

    // private static int loadingstate; //GlobalConfigControllerManager
    // 保存loadingstate状态
    //
    // public static int getLoadingstate()
    // {
    // return loadingstate;
    // }
    //
    // public static void setLoadingstate( int loadingstate )
    // {
    // App.loadingstate = loadingstate;
    // }
    //
    public static App getAppContext() {
        return mAppContext;
    }

    public static SharedPreferences getSharedPreference() {
        return mSharePrefences;
    }

    public static SettingContent getSettingContent() {
        return mSettingContent;
    }

    public static boolean isDebugMode() {
        return bDebugMode;
    }

    public static void openDebugMode() {
        bDebugMode = true;
    }

    public static String getDebugLogFilePath() {
        return debugLogFilePath;
    }

    public static void setDebugLogFilePath(String debugLogFilePath) {
        App.debugLogFilePath = debugLogFilePath;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //xutils init
        x.Ext.init(this);
        x.Ext.setDebug(Logger.isDebug);

        LogUtils.i("------App onCreate");
        Log.d(TAG, "------App onCreate");
        mAppContext = this;
        mSharePrefences = getSharedPreferences(SHARED_PERFERENCE_NAME, 0);
        try {
            mClientId = getResources().getInteger(R.integer.client_id);
            if (mClientId == 12) {
                mAllGame = true;
            }
            PackageInfo packageInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            mVerName = packageInfo.versionName;
            mVerCode = packageInfo.versionCode;
            mPackageName = packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mChNoSelf = PackageUtil.getPkgChnNo(this, this.getPackageName());

        /*bugglyInit(mChNoSelf);*/
        XGPushManager.registerPush(this);

        bDebugMode = mAppContext.getResources().getBoolean(R.bool.config_debug_mode);
        DeviceInfo.setDeviceInfo(mAppContext);

        // 保证在mSharePrefences初始化之后调用，SettingContent会用到sharePrefences
        mSettingContent = SettingContent.getInstance();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheSize(50 * 1024 * 1024)
                .threadPoolSize(3)
                        // .writeDebugLogs() // Remove
                        // for
                        // release
                        // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        // 缓存参数初始化
        ControllerHelper.getInstance();

        Log.d(TAG, "App setLoadingState LOADING_STATE reqGlobalConfig");
        // 初始化参数
        Settings.loadSettings(mAppContext);
        // 移入HomePageAcitvity
        /*
		 * GlobalConfigControllerManager.getInstance().setLoadingState(
         * GlobalConfigControllerManager.LOADING_STATE);
         * GlobalConfigControllerManager.getInstance().reqGlobalConfig();
         */

        // 保证这是最后一句代码
        createShortcut();

        // StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder(
        // ).detectDiskReads( ).detectDiskWrites( ).detectNetwork( ) // or
        // .detectAll() for all detectable problems
        // .penaltyLog( ).build( ) );
        // StrictMode.setVmPolicy( new StrictMode.VmPolicy.Builder(
        // ).detectLeakedSqlLiteObjects( ).detectLeakedClosableObjects(
        // ).penaltyLog( ).penaltyDeath( ).build( ) );

        //oddshou############ 所有 init 只调用一次 上报本地应用，检测更新
        init();
        //oddshou############

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				initMTAConfig(true);
//				long latestTime = getLatestLoginTime();
//				if (latestTime != -1
//						&& (System.currentTimeMillis() - latestTime) > 24 * 60 * 60 * 1000 * 30) {
//					MtaUtils.trackReturn();
//				}
//				setLatestLoginTime();
//				int versionCode = (int) getForeVersionCode();
//				if (versionCode != -1 && versionCode < mVerCode) {
//					MtaUtils.trackUpdateInstalled(mVerName, "" + mVerCode);
//				}
//				setForeVersionCode();
//			}
//		}).start();

//		Tools.getDisplayDensity(this);
    }

    //bugly 注释
/*    private void bugglyInit(String chNo) {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        if (Logger.isDebug) {
            strategy.setAppVersion("88.88.88");        //测试统一版本号
        }
        strategy.setAppChannel(chNo);
        CrashReport.initCrashReport(this, StatisticManager.getBuglyAppid(), false, strategy);
    }*/

    @Override
    public void onTerminate() {
        mSharePrefences = null;
        if (mApkBroadcastReceiver != null)
            mApkBroadcastReceiver.unregisterReceiver();
        LogUtils.e("游戏中心 App被onTerminate!");


        super.onTerminate();
    }


    /**
     * @author oddshou 这个 init被 频繁调用，是有问题的。
     * <p/>
     * 先在修改为 app onCreate 中调用
     */
    public static void init() {
        BroadcastManager.getInstance().registerReceiveres();

        // 管理已安装APk，本地更新apk，图片管理的manager
        // 每次进入闪屏界面，加载本地应用数据
//		ApkInstalledManager.getInstance().loadApps();// 这里会查询所有需要更新的应用
        // 初始化
        DownloadService.getDownloadManager();
        //启动唤醒service
//        Intent daemonService = new Intent(mAppContext, DaemonService.class);
//        mAppContext.startService(daemonService);
    }

    public static void initChnNo(int chNo) {
        chnNo = chNo;
    }

    public static int getChnNo() {
        return chnNo;
    }

    public static String PackageName() {
        return mPackageName;
    }

    public static int VersionCode() {
        return mVerCode;
    }

    public static String VersionName() {
        return mVerName;
    }

    // 创建快捷方式
    private void createShortcut() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean bhasShortcut = mSharePrefences.getBoolean(PERFERENCE_SHORTCUT, false);
                if (bhasShortcut)
                    return;

                String appName = mAppContext.getResources().getString(R.string.app_name);
                if (FileUtils.hasShortcutByAppName(mAppContext, appName) > 0) {
                    return;
                }
                FileUtils.addShortCut(R.mipmap.icon, appName, getPackageName(), mAppContext);
                SharedPreferences.Editor editor = mSharePrefences.edit();
                editor.putBoolean(PERFERENCE_SHORTCUT, true);
                editor.commit();
            }
        }).start();
    }

    public void exit() {
        System.exit(0);
    }

    private void setLatestLoginTime() {
        SharedPreferences loginPreference = this
                .getSharedPreferences("lastest_login_preferece", Context.MODE_PRIVATE);
        Editor editor = loginPreference.edit();
        editor.putLong("lastest_login_time", System.currentTimeMillis());
        editor.commit();
    }

    private long getLatestLoginTime() {
        SharedPreferences loginPreference = this
                .getSharedPreferences("lastest_login_preferece", Context.MODE_PRIVATE);
        return loginPreference.getLong("lastest_login_time", -1);
    }

    private long getForeVersionCode() {
        SharedPreferences loginPreference = this
                .getSharedPreferences("lastest_login_preferece", Context.MODE_PRIVATE);
        return loginPreference.getLong("fore_version_code", -1);
    }

    private void setForeVersionCode() {
        SharedPreferences loginPreference = this
                .getSharedPreferences("lastest_login_preferece", Context.MODE_PRIVATE);
        Editor editor = loginPreference.edit();
        editor.putLong("fore_version_code", mVerCode);
        editor.commit();
    }

    public static int getDevicesType() {
        return mAppContext.getResources().getInteger(R.integer.devices_type);
    }

    // 验证码定时器
    public interface TimeRun {
        void onTimer(int count);
    }

    private TimeRun mTimeListen;
    private int mCurrentTimer = 0;

    public int getmCurrentTimer() {
        return mCurrentTimer;
    }

    public void setmTimeListen(TimeRun mTimeListen) {
        this.mTimeListen = mTimeListen;
    }

    public void startTimeRun(int time) {
        mCurrentTimer = time;
        mTimeHandler.removeMessages(MSG_TIME_RUN);
        mTimeHandler.sendEmptyMessage(MSG_TIME_RUN);
    }

    public static final int MSG_TIME_RUN = 0X01;
    private Handler mTimeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_TIME_RUN:
                    if (mTimeListen != null) {
                        mTimeListen.onTimer(mCurrentTimer);
                    }
                    if (mCurrentTimer > 0) {
                        mTimeHandler.sendEmptyMessageDelayed(MSG_TIME_RUN, 1000);
                        mCurrentTimer--;
                    }
                    break;

                default:
                    break;
            }

        }

    };


}
