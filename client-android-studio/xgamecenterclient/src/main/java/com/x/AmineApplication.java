package com.x;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.x.business.alarm.AlarmManage;
import com.x.business.notification.AdNotificationManager;
import com.x.db.DownloadDbAdapter;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppDbAdapter;
import com.x.db.LocalAppEntityManager;
import com.x.db.zerodata.TransferHistoryDbAdapter;
import com.x.db.zerodata.TransferHistoryEnityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.RequestQueueManager;
import com.x.publics.model.ApkInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.CrashHandler;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: AmineApplication
 * @Description: TODO
 
 * @date 2013-12-17 下午02:25:51
 * 
 */

public class AmineApplication extends Application {

  private LocalAppDbAdapter localAppDbAdapter;
  private DownloadDbAdapter downloadDbAdapter;
  private TransferHistoryDbAdapter transferHistoryDbAdapter;

  public static List<DownloadBean> downloadList = new ArrayList<DownloadBean>();
  public static Context context;
  public static boolean scaned = false;
  public static ArrayList<ApkInfoBean> apkUninstallFileList = new ArrayList<ApkInfoBean>();
  public static ArrayList<ApkInfoBean> apkInstallFileList = new ArrayList<ApkInfoBean>();

  @Override
  public void onCreate() {
    super.onCreate();
    context = this;
    initDb();
    RequestQueueManager.init(this);
    BroadcastManager.init(this);
    initDownloadHistory();
    initUIL(this);
    initAlarm(this);
    initErrorHandler();
  }

  private void initDb() {
    localAppDbAdapter = new LocalAppDbAdapter(this);
    localAppDbAdapter.openAndIntiModeManager(LocalAppEntityManager.getInstance());

    downloadDbAdapter = new DownloadDbAdapter(this);
    downloadDbAdapter.openAndIntiModeManager(DownloadEntityManager.getInstance());

    transferHistoryDbAdapter = new TransferHistoryDbAdapter(this);
    transferHistoryDbAdapter.openAndIntiModeManager(TransferHistoryEnityManager.getInstance());

  }

  private void initDownloadHistory() {
    Intent downloadIntent = new Intent(MyIntents.INTENT_DOWNLOADSERVICE);
    downloadIntent.putExtra(MyIntents.TYPE, MyIntents.Types.ADD_HISTORY);
    startService(downloadIntent);
  }

  private void initUIL(Context context) {
    // This configuration tuning is custom. You can tune every option, you may
    // tune some of them,
    // or you can create default configuration by
    // ImageLoaderConfiguration.createDefault(this);
    // method.
    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
        .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
        .memoryCache(new LruMemoryCache(Utils.calculateMemoryCacheSize(context))).threadPoolSize(5)
        /* .memoryCache(new WeakMemoryCache()) */
        .memoryCacheSize(Utils.calculateMemoryCacheSize(context)).discCacheSize(20 * 1024 * 1024)
        .memoryCacheExtraOptions(320, 480)/* .writeDebugLogs() */// Remove for
                                                                 // release app
        .build();
    ImageLoader.getInstance().init(config);
  }

  private void initErrorHandler() {
    // MyCrashHandler crashHandler = MyCrashHandler.getInstance();
    // 设置异常处理实例
    // Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    CrashHandler crashHandler = CrashHandler.getInstance();
    crashHandler.init(getApplicationContext());
  }

  private void initAlarm(Context context) {
    AlarmManage.getInstance(context).runAlarmTask();
    AdNotificationManager.getInstance().runAlarmTask(this);
  }

}
