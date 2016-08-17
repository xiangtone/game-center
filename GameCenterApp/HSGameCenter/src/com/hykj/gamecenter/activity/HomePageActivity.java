
package com.hykj.gamecenter.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.GlobalConfigControllerManager;
import com.hykj.gamecenter.GlobalConfigControllerManager.LoadingStateListener;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.adapter.IViewVisiableChangedListener;
import com.hykj.gamecenter.broadcast.WifiUpdateReceiver;
import com.hykj.gamecenter.controller.ControllerHelper;
import com.hykj.gamecenter.controller.ProtocolListener.ACTION_PATH;
import com.hykj.gamecenter.controller.ProtocolListener.KEY;
import com.hykj.gamecenter.controller.ProtocolListener.PAGE_SIZE;
import com.hykj.gamecenter.controller.ProtocolListener.ReqReportedListener;
import com.hykj.gamecenter.controller.ProtocolListener.ReqUpdateListener;
import com.hykj.gamecenter.controller.ReqUpdateController;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTaskManager;
import com.hykj.gamecenter.fragment.AppManagerFragment;
import com.hykj.gamecenter.fragment.BackHandledFragment;
import com.hykj.gamecenter.fragment.ClassifyFragment;
import com.hykj.gamecenter.fragment.GameFragment;
import com.hykj.gamecenter.fragment.TopicFragment;
import com.hykj.gamecenter.fragment.WifiFragment;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.ApkInstalledManager.InstallFinishListener;
import com.hykj.gamecenter.logic.BroadcastManager;
import com.hykj.gamecenter.logic.NotificationCenter;
import com.hykj.gamecenter.logic.SearchRecommendControllerManager;
import com.hykj.gamecenter.logic.entry.ISaveInfo;
import com.hykj.gamecenter.logic.entry.Msg;
import com.hykj.gamecenter.net.JsonCallback;
import com.hykj.gamecenter.net.WifiHttpUtils;
import com.hykj.gamecenter.net.logic.UpdateDownloadController;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.protocol.Updater.RspUpdate;
import com.hykj.gamecenter.services.MonitorAppsUpdateService;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.BadgeView;
import com.hykj.gamecenter.ui.DownloadStateViewCustomization;
import com.hykj.gamecenter.ui.NoviceGuidanceAppView;
import com.hykj.gamecenter.ui.widget.CSAlertDialog;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.UpdateDialog;
import com.hykj.gamecenter.utils.Interface.IDownloadTaskCountChangeListener;
import com.hykj.gamecenter.utils.Interface.IDownloadTaskObserver;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.NetUtils;
import com.hykj.gamecenter.utils.SystemBarTintManager;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utils.UpdateUtils;
import com.hykj.gamecenter.utilscs.LogUtils;

import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class HomePageActivity extends Activity implements IDownloadTaskCountChangeListener,
        InstallFinishListener, BackHandledFragment.BackHandledInterface, IDownloadTaskObserver,
        WifiFragment.IWifiConnected {

    public static final int REQ_PAGE_SIZE = PAGE_SIZE.GENERAL;
    private static final String TAG = "HomePageActivity";

    public static final String APP_INFO = "appinfo";
    public static final String SHOULD_CHECK_UPDATE = "shouldCheckUpdate";
    private static final int DEFAULT_OFFSCREEN_PAGES = 4;
    public static final int FRAGMENT_LOAD_DELAY_TIME = 800;
    public static final int FORCE_UPDATE_TYPE = 3;
    public static final String UPDATE_ALL = "total_size";
    public static final String UPDATE_CURRENT = "current_size";
    public static final String APP_PATH = "apk_path";
    public static final String FROM_NOTIFICATION = "FROM_NOTIFICATION";
    public static final String KEY_GOTO_RECOMM = "key_goto_recomm";
    public static final String KEY_SELECT_ITEM = "key_select_item";
    public static final String KEY_GOTO_UPDATE = "key_goto_update";
    public static final String KEY_UPDATE_ALL = "key_update_all";
    private BadgeView mBadgeView;
    private ImageView mImgWifi;


    // public static boolean mbAppInitRuning = false; // 应用程序刚启动时设置该变量

    public static class PAGE_INDEX {
        public static final int INDEX_HOME = 0;// 代表首页
        public static final int INDEX_RANK = 1;// 代表排行
        public static final int INDEX_CLASSIFY = 2;// 分类
        public static final int INDEX_UPDATE = 3;// 升级
        public static final int IMPROPER_DETAIL = 6;// 权限详情
        public static final int INDEX_WIFI = 7;
    }

    NoviceGuidanceAppView mNoviceGuidanceView = null; // 新手推荐页
    // Activity是MVC的C,所以新手推荐页设计成一个View，而不设计成Activity
    LinearLayout mlinelayoutHomepager = null; // 主页

    private ApkInstalledManager mApkInstalledManager = null;
    private ApkDownloadManager mApkDownloadManager = null;
    private BroadcastManager mBroadcastManager = null;
    // private GlobalConfigControllerManager mGlobalConfigControllerManager;

    ServiceConnection mSc = null;// 通过binder实现与服务通信(binder机制是贯穿整个android系统的进程间访问机制，经常被用来访问service)
    // 查询是否有应用更新的服务 的管理类，用于接收相关广播然后 检查并启动 查询应用更新类
//	MonitorAppsUpdateServiceManagerReceiver monitorAppsUpdateServiceManagerReceiver = new MonitorAppsUpdateServiceManagerReceiver(
//			this);

    // private RecommedFragment mRecommedFragment = null;
    private TopicFragment mTopicAppFragment = null;
    private ClassifyFragment mClassifyFragment = null;
    private GameFragment mGameFragment = null;
    private AppManagerFragment mAppManagerFragment = null;
    private WifiFragment mWifiFragment = null;
    private UpdateDialog mUpdateDialog;
    private RspUpdate mRspUpdate;
    private boolean mbFirstLaunch = true;
    private boolean isAction = false;
    private boolean isUpdateDialogShowing = false;
    private boolean isNoteDialogShowing = false;
    private final String UPDATE_DIALOG_SHOWING = "shouldUpdateDialogShowing";
    private final String NOTE_DIALOG_SHOWING = "shouldNoteDialogShowing";
    private int currentTab = 0;
    private boolean hasDestroied = false;
    private boolean isChechedSelect = false;

    /*
     * public static void setAppInitRunning() { mbAppInitRuning = true; } public
     * static void resetAppInitRunning() { mbAppInitRuning = false; } public
     * static boolean getAppInitRunning() { return mbAppInitRuning; }
     */

    /*
     * public void setmRecommedFragment(RecommedFragment mRecommedFragment) {
     * this.mRecommedFragment = mRecommedFragment; }
     */

    public void setmTopicAppFragment(TopicFragment mTopicAppFragment) {
        this.mTopicAppFragment = mTopicAppFragment;
    }

    public void setmClassifyFragment(ClassifyFragment mClassifyFragment) {
        this.mClassifyFragment = mClassifyFragment;
    }

    public void setmGameFragment(GameFragment mGameFragment) {
        this.mGameFragment = mGameFragment;
    }

    /*
     * public RecommedFragment getRecommedFragment() { return mRecommedFragment;
     * }
     */

    public TopicFragment getTopicAppFragment() {
        return mTopicAppFragment;
    }

    public ClassifyFragment getClassifyFragment() {
        return mClassifyFragment;
    }

    public GameFragment getGameFragment() {
        return mGameFragment;
    }

    private CSAlertDialog noteDialog;
    private Context mContext;

    private boolean hasUpdated = false;

    IViewVisiableChangedListener mNoviceGuidanceViewVisiableChangedListener = null; // 新手推荐页显示与隐藏监听

    private static boolean mbNetworkConnectedShowGuidanceView = false; // 网络恢复时需要显示新手推荐页面

    // 显示新手推荐
    private void showNoviceGuidanceView() {

        if (mbNetworkConnectedShowGuidanceView) {
            // 刷新数据
            mNoviceGuidanceView.getHandler().sendEmptyMessage(Msg.REFRESH_LIST);
        }

        if (!NetUtils.isNetworkconnected(mContext)) {
            mbNetworkConnectedShowGuidanceView = true;
            Log.d(TAG, "No net showNoviceGuidanceView ");
            return;
        }
        Log.d(TAG, "showNoviceGuidanceView");
        if (null == mNoviceGuidanceViewVisiableChangedListener) {
            mNoviceGuidanceViewVisiableChangedListener = new IViewVisiableChangedListener() {

                @Override
                public void onVisibilityChanged(View changedView, int visibility) {
                    // TODO Auto-generated method stub
                    if (visibility == View.GONE) {

                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sleep(1000); // 睡眠1000ms是等待下载的状态发生改变
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                HomePageActivity.this.mUIHandler
                                        .sendEmptyMessage(MSG_REFRESH_DOWNLOAD_COUNT_ICON);// 刷新下载图标个数
                            }

                        }.start();

                    }
                }
            };
            mNoviceGuidanceView
                    .setOnVisiableChangedListener(mNoviceGuidanceViewVisiableChangedListener);
        }

        mNoviceGuidanceView.setVisibility(View.VISIBLE);
        mbNetworkConnectedShowGuidanceView = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        boolean switchToUpdate = getIntent().getBooleanExtra(KEY_GOTO_UPDATE, false);
        boolean updateAll = getIntent().getBooleanExtra(KEY_UPDATE_ALL, false);
        int itemSelect = getIntent().getIntExtra(KEY_SELECT_ITEM, 0);
        if (itemSelect != 0){
            showTagFragment(String.valueOf(itemSelect), getViewbyTag(itemSelect));
        }
        if (itemSelect == PAGE_INDEX.INDEX_UPDATE && switchToUpdate | updateAll) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(KEY_GOTO_UPDATE, switchToUpdate);
            bundle.putBoolean(KEY_UPDATE_ALL, updateAll);
            mAppManagerFragment.setArguments(bundle);
        }

        boolean gotoRecommm = intent.getBooleanExtra(KEY_GOTO_RECOMM, false);
        Logger.d(TAG, "onNewIntent gotoRecommm " + gotoRecommm);
        if (gotoRecommm) {
            showNoviceGuidanceView();
        }

    }

    // 显示主页
    private void showHomePage() {
        mNoviceGuidanceView.setVisibility(View.GONE);
        // mlinelayoutHomepager.setVisibility(View.VISIBLE);
        // //用的是framelayout不需要这句
        HomePageActivity.this.mUIHandler.sendEmptyMessage(MSG_REFRESH_DOWNLOAD_COUNT_ICON);// 刷新下载图标个数
        Log.i(TAG, "oddshou showHomePage.............");
    }

    private void initView(Bundle savedInstanceState) {
        mContext = App.getAppContext();

        mNoviceGuidanceView = (NoviceGuidanceAppView) findViewById(R.id.noviceguidance_pager);

        View viewWifiMask = findViewById(R.id.viewWifiMask);
        viewWifiMask.setOnClickListener(mViewOnclickListener);
        boolean showWifiMask = App.getSharedPreference().getBoolean(StatisticManager.KEY_WIFIMASK_SHOW, false);
        viewWifiMask.setVisibility(showWifiMask ? View.GONE : View.VISIBLE);

        Log.d(TAG, "mNoviceGuidanceView =" + mNoviceGuidanceView);
        mlinelayoutHomepager = (LinearLayout) findViewById(R.id.home_pager);

        View mTextRecommed = findViewById(R.id.textRecommed);
        mTextRecommed.setOnClickListener(mViewOnclickListener);

        View mTextMine = findViewById(R.id.textMine);
        mTextMine.setOnClickListener(mViewOnclickListener);
        findViewById(R.id.textRank).setOnClickListener(mViewOnclickListener);
        findViewById(R.id.textClassily).setOnClickListener(mViewOnclickListener);
        mImgWifi = (ImageView)findViewById(R.id.imgWifi);
        findViewById(R.id.imgWifi).setOnClickListener(mViewOnclickListener);


        mBadgeView = new BadgeView(this, mTextMine);
//		mBadgeView.setText("12"); //显示类容
        mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);//显示的位置.中间，还有其他位置属性
        mBadgeView.setTextColor(Color.WHITE);  //文本颜色
        mBadgeView.setBadgeBackgroundColor(Color.RED); //背景颜色
        mBadgeView.setTextSize(12); //文本大小
//        int offset = getResources().getDimensionPixelOffset(R.dimen.tips_offset);
        int offset = Tools.getDisplayWidth(this) / 20;
//        mBadgeView.setBadgeMargin(offset - 64, 0); //水平和竖直方向的间距
        mBadgeView.setBadgeMargin(offset, 0);
        mBadgeView.toggle();

        handleAction();

        //wifi状态相关处理
        //判断当前网络是否连接并测试连接状态
        boolean checkIndentifySsid = NetUtils.CheckIndentifySsid(this, WifiHttpUtils.SSID_HEAD);
//        updateState(checkIndentifySsid ? ConnectState.CONNECTED : ConnectState.UNCONNECTED);
        //ping 公网进一步验证
        if (checkIndentifySsid) {
            WifiHttpUtils wifiHttpUtils = new WifiHttpUtils(new JSONObject());
            wifiHttpUtils.getmHdata().setVer(2);
            doPost(WifiHttpUtils.URL_WIFI_FRESH, wifiHttpUtils);
        }

        WifiUpdateReceiver.setWifiConnectListen(mWifiListener);
    }

    private String mLastFragmentTag;

//	RadioGroup.OnCheckedChangeListener mRadioCheckChangeListener = new RadioGroup.OnCheckedChangeListener() {
//		@Override
//		public void onCheckedChanged(RadioGroup group, int checkedId) {
//			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//			switch (checkedId) {
//				case R.id.radioRecommed:
//					showTagFragment(String.valueOf(PAGE_INDEX.INDEX_HOME));
//					break;
//				case R.id.radioRank:
//					showTagFragment(String.valueOf(PAGE_INDEX.INDEX_RANK));
//					break;
//				case R.id.radioClassily:
//					showTagFragment(String.valueOf(PAGE_INDEX.INDEX_CLASSIFY));
//					break;
//				case R.id.radioMine:
//					showTagFragment(String.valueOf(PAGE_INDEX.INDEX_UPDATE));
//					break;
//			}
//		}
//

//	};

    private void showTagFragment(String nowTag, View v) {

        if (nowTag != mLastFragmentTag) {
            Fragment lastFragment = getFragmentManager().findFragmentByTag(mLastFragmentTag);
            Fragment nowFragment = getFragmentManager().findFragmentByTag(nowTag);
            if (nowFragment != null) {
                getFragmentManager().beginTransaction()
                        .show(nowFragment)
                        .hide(lastFragment)
                        .commit();
            } else {
                Fragment fragment = null;
                fragment = getFragment(Integer.valueOf(nowTag), fragment);
                getFragmentManager().beginTransaction()
                        .add(R.id.homeContainer, fragment, nowTag)
                        .addToBackStack(null)
                        .hide(lastFragment)
                        .commit();
            }
            mLastFragmentTag = nowTag;
            //设置选中状态
            if (v.getId() != mImgWifi.getId()) {
                v.setSelected(true);
            }
            if (mLastSelectedView.getId() != mImgWifi.getId()){
                mLastSelectedView.setSelected(false);
            }
            mLastSelectedView = v;

            // 上报访问当前页面
            ReportedInfo builder = new ReportedInfo();
            builder.statActId = ReportConstants.STATACT_ID_PAGE_VISIT;
            builder.statActId2 = Integer.valueOf(nowTag) + 1;// viewpage 页面切换监听
            ReportConstants.getInstance().reportReportedInfo(builder);
        }
    }

    private View getViewbyTag(int fragmentTag) {
        View v = null;
        switch (fragmentTag) {
            case PAGE_INDEX.INDEX_HOME:
                v = findViewById(R.id.textRecommed);
                break;
            case PAGE_INDEX.INDEX_RANK:
                v = findViewById(R.id.textRank);
                break;
            case PAGE_INDEX.INDEX_CLASSIFY:
                v = findViewById(R.id.textClassily);
                break;
            case PAGE_INDEX.INDEX_UPDATE:
                v = findViewById(R.id.textMine);
                break;
            case PAGE_INDEX.INDEX_WIFI:
                v = findViewById(R.id.imgWifi);
                break;
        }

        return v;
    }

    public static String KEY_WIFI_CONNECTED = "key_wifi_connected";
    private Fragment getFragment(int nowTag, Fragment fragment) {
        switch (nowTag) {
            case PAGE_INDEX.INDEX_HOME:
                fragment = mGameFragment = new GameFragment();
                if (mUIHandler != null)
                    mGameFragment.addHandler(mUIHandler);
                break;
            case PAGE_INDEX.INDEX_RANK:
                fragment = mTopicAppFragment = new TopicFragment();
                break;
            case PAGE_INDEX.INDEX_CLASSIFY:
                fragment = mClassifyFragment = new ClassifyFragment();
                break;
            case PAGE_INDEX.INDEX_UPDATE:
                fragment = mAppManagerFragment = new AppManagerFragment();
                break;
            case PAGE_INDEX.INDEX_WIFI:
                fragment = mWifiFragment = new WifiFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean(KEY_WIFI_CONNECTED, findViewById(R.id.imgWifi).isSelected());
                mWifiFragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    private View mLastSelectedView;
    private OnClickListener mViewOnclickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textRecommed:
                    showTagFragment(String.valueOf(PAGE_INDEX.INDEX_HOME), v);
                    break;
                case R.id.textRank:
                    showTagFragment(String.valueOf(PAGE_INDEX.INDEX_RANK), v);
                    break;
                case R.id.textClassily:
                    showTagFragment(String.valueOf(PAGE_INDEX.INDEX_CLASSIFY), v);
                    break;
                case R.id.textMine:
                    showTagFragment(String.valueOf(PAGE_INDEX.INDEX_UPDATE), v);
                    break;
                case R.id.imgWifi:
                    showTagFragment(String.valueOf(PAGE_INDEX.INDEX_WIFI), v);
                    break;
                case R.id.viewWifiMask:
                    App.getSharedPreference().edit().
                            putBoolean(StatisticManager.KEY_WIFIMASK_SHOW, true).apply();
                    v.setVisibility(View.GONE);
                    break;
            }

        }
    };

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate" + "this=" + this);

        synchronized (mHomePageActivityPtrLock) {
            mHomePageActivityPtr = this;
        }


        Logger.i(TAG, "onCreate getDisplayDensity = " + Tools.getDisplayDensity(this));

        // ################  tomqian 由用户控制横竖屏
        if (App.getDevicesType() == App.PHONE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // ################  tomqian
        setContentView(R.layout.activity_home_tab);
        SystemBarTintManager.useSystemBar(this, R.color.action_blue_color);
        initData();// loadApps() 这里会查询所有需要更新的应用
        initView(savedInstanceState);

        //非华硕渠道号版本不用弹提示框
//		if (App.getmChNoSelf().equals(StatisticManager.ChnNo.ASUS)) {
//			showFirstDialog();
//		} else {
        createAfterInit();
//		}


        // 如果是从通知栏启动的，先启动AppManageActivity
        // if (bFromNofitication) {
        // Intent intent = new Intent(HomePageActivity.this,
        // AppManageActivity.class);
        //
        // // GOTO_UPDATE 这个标志位为会true 启动AppManageActivity时会查询可更新应用
        // intent.putExtra(AppManageActivity.GOTO_UPDATE, true);
        // HomePageActivity.this.startActivity(intent);
        // }


    }

    private void showFirstDialog() {
        boolean showFirstDialog = App.getSharedPreference().getBoolean(StatisticManager.KEY_SHOW_TIPS_DIALOG, true);
        if (!showFirstDialog) {
            createAfterInit();
            return;
        }

        final CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(true);
        checkBox.setText(R.string.no_hint_later);
        //提示dialog
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.tips)
                .setMessage(getString(R.string.first_start_tips))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = App.getSharedPreference().edit();
                        editor.putBoolean(StatisticManager.KEY_SHOW_TIPS_DIALOG, !checkBox.isChecked());
                        editor.apply();
                        createAfterInit();
                        resume();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = App.getSharedPreference().edit();
                        editor.putBoolean(StatisticManager.KEY_SHOW_TIPS_DIALOG, !checkBox.isChecked());
                        editor.apply();
                        finish();
                    }
                })
                .setView(checkBox)
                .setCancelable(false)
//                .setMultiChoiceItems(new String [] {"下次不再提示"}, new boolean[]{true}, null)
                .create();
        dialog.show();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        Logger.i(TAG, "onConfigurationChanged() ");

        if (mNoviceGuidanceView != null && mNoviceGuidanceView.isShown()) {
            mNoviceGuidanceView.onSwitchScreenOrientation(newConfig);
        }
        super.onConfigurationChanged(newConfig);
    }

    private int chnNo = 0;

    public int getChnNo() {
        return chnNo;
    }

    private void createAfterInit() {

		/*
         * 由App移入该处，修改增加专题不能按OnBackPressed退出后 (应用其实并未完全退出，再进入时App
         * OnCreate不会被调用，除非杀死后台程序) ，这样运营增加个专题不能实时显示
         */
        GlobalConfigControllerManager.getInstance().setLoadingState(
                GlobalConfigControllerManager.LOADING_STATE);
        GlobalConfigControllerManager.getInstance().reqGlobalConfig();

        mbFirstLaunch = firstLaunch();
        Log.d(TAG, "mbFirstLaunch = " + mbFirstLaunch);
        // 上报 启动
        ReportedInfo build = new ReportedInfo();
        build.statActId = ReportConstants.STATACT_ID_LAUNCH;
        if (mbFirstLaunch) {

            // 上报首次启动
            build.statActId2 = 1;
            ReportConstants.getInstance().reportReportedInfoNow(build, mReqReportedListener);
        } else {
            //上报普通启动
            build.statActId2 = 2;
            ReportConstants.getInstance().reportReportedInfoNow(build, null);
        }
        ApkInstalledManager.getInstance().loadApps();// 这里会查询所有需要更新的应用

        // 上报访问当前页面
        ReportedInfo builder = new ReportedInfo();
        builder.statActId = ReportConstants.STATACT_ID_PAGE_VISIT;
        builder.statActId2 = PAGE_INDEX.INDEX_HOME + 1;// viewpage 页面切换监听
        ReportConstants.getInstance().reportReportedInfo(builder);

        // 是否是其他页面按返回键跳转到该页面
        boolean backPressed = getIntent().getBooleanExtra(KEY.BACK_PRESSED, false);
        // 如果是首次启动
//		Log.d(TAG, "bFirstLaunch = " + mbFirstLaunch + " mbBackPressed = " + backPressed);
        boolean gotoRecommm = getIntent().getBooleanExtra(KEY_GOTO_RECOMM, false);
        boolean wifiHasShow = App.getSharedPreference().getBoolean(StatisticManager.KEY_WIFIMASK_SHOW, false);
        if (gotoRecommm || (UpdateUtils.shouldCheckRecommTiem() && !backPressed && wifiHasShow)) {

            showNoviceGuidanceView();
            // 首次启动发送可更新应用通知，不管有没有可更新应用都发送， ##############oddshou 暂时去除
            // NotificationCenter.getInstance().sendFirstLaunchUpdateNotification();
        } else {
            showHomePage();
        }

        //判断是update fragment 执行 updateAll、switchToUpdate
        int itemSelect = getIntent().getIntExtra(KEY_SELECT_ITEM, 0);
        boolean switchToUpdate = getIntent().getBooleanExtra(KEY_GOTO_UPDATE, false);
        boolean updateAll = getIntent().getBooleanExtra(KEY_UPDATE_ALL, false);
        Fragment fragment = null;
        fragment = getFragment(itemSelect, fragment);
        mLastFragmentTag = String.valueOf(itemSelect);
        getFragmentManager().beginTransaction()
                .add(R.id.homeContainer, fragment, mLastFragmentTag)
                .addToBackStack(null)
                .commit();
        if (itemSelect == PAGE_INDEX.INDEX_UPDATE && switchToUpdate | updateAll) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(KEY_GOTO_UPDATE, switchToUpdate);
            bundle.putBoolean(KEY_UPDATE_ALL, updateAll);
            mAppManagerFragment.setArguments(bundle);
        }
        mLastSelectedView = getViewbyTag(itemSelect);
        mLastSelectedView.setSelected(true);

        cheakLoadingState();
    }

    // loadApps() 这里会查询所有需要更新的应用
    private void initData() {
        chnNo = getIntent().getIntExtra("account_to_game_flag", 0);

        if (chnNo == 0) {
            chnNo = getIntent().getIntExtra("chnNo", 0);
        }
        LogUtils.e("chnNo = " + chnNo);
        App.initChnNo(chnNo);
        BroadcastManager.getInstance().registerReceiveres();
        DownloadService.getDownloadManager();
        mApkInstalledManager = ApkInstalledManager.getInstance();
        mApkDownloadManager = DownloadService.getDownloadManager();
        mApkInstalledManager.addInstallListener(this);
        // mGlobalConfigControllerManager =
        // GlobalConfigControllerManager.getInstance( );
        mBroadcastManager = BroadcastManager.getInstance();
        mBroadcastManager.registerKeyEventReceive();

        // 这里需要用到ServiceConnection (Context.bindService和context.unBindService()
        // )
        // 在bindService之后就可以ServiceConnection在onServiceConnected中获得服务对象了
        mSc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "service connected");
                MonitorAppsUpdateService ss = ((MonitorAppsUpdateService.MonitorAppsUpdateBinder) service)
                        .getService();
                // ss.sayHelloWorld();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "service disconnected");
            }
        };

//		startMtaService();

    }

    ReqReportedListener mReqReportedListener = new ReqReportedListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReqReportedSucceed() {
            // TODO Auto-generated method stub
            Log.d(TAG, "onReportLanuchInfoSucceed");
            App.getSharedPreference().edit().putBoolean(App.SHARED_FIRST_LAUNCH_NAME, false)
                    .commit();
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            // TODO Auto-generated method stub

        }
    };

    private boolean firstLaunch() {
        SharedPreferences sp = App.getSharedPreference();
        return sp.getBoolean(App.SHARED_FIRST_LAUNCH_NAME, true);
    }

    public void handleAction() {
        String action = getIntent().getAction();
        if (action != null && action.equals("com.hykj.gamecenter.activity.HomePageActivity")) {
            Uri uri = getIntent().getData();
            if (uri != null && uri.getPath().equals(ACTION_PATH.HOME_PAGE)) {
                currentTab = Integer.parseInt(uri.getQueryParameter("tab"));
                LogUtils.e("currentTab =" + currentTab);
                chnNo = getIntent().getIntExtra("chnNo", 0);

                // App.init();
                App.initChnNo(chnNo);

            }
        }
    }

    @Override
    protected void onResume() {
        LogUtils.i("onResume");
        Log.d(TAG, "onResume");
        super.onResume();
        isAction = true;
        resume();
    }

    private void resume() {
        refreshActionBarTip();

        if ((mRspUpdate != null && mRspUpdate.updateType == FORCE_UPDATE_TYPE || isUpdateDialogShowing)
                && GlobalConfigControllerManager.getInstance().isNotificationShow()) {// 显示提示
            mUIHandler.sendEmptyMessage(MSG_SHOW_UPDATE_DIALOG);
        }
        if (isNoteDialogShowing) {
            // showNoteDialog();
        }
        mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);
        Intent intent = getIntent();


        Log.d(TAG, "onResume hasUpdated = " + hasUpdated
                        + " intent.getBooleanExtra(SHOULD_CHECK_UPDATE, false) ="
                        + intent.getBooleanExtra(SHOULD_CHECK_UPDATE, false)
                /* + " getAppInitRunning() = " + getAppInitRunning() */);
        if (intent.getBooleanExtra(SHOULD_CHECK_UPDATE, false) && !hasUpdated
        /* || getAppInitRunning() && !hasUpdated */) {
            Log.d(TAG, "onResume check self update");
            mUIHandler.sendEmptyMessage(MSG_GOTO_UPDATE);
            hasUpdated = true;
        }
    }

    @Override
    protected void onPause() {
        Logger.i(TAG, "onPause");
        super.onPause();
        isAction = false;
        mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);
        // 不显示通知栏通知(一键升级)
        GlobalConfigControllerManager.getInstance().setNotificationVisible(false);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        Logger.i(TAG, "onStart");
        super.onStart();
        /*
         * Intent service=new
         * Intent(this.getApplicationContext(),MonitorFeedbackService.class);
         * this.bindService(service, mSc, Context.BIND_AUTO_CREATE);
         */
        // 注册下载任务个数变化监听
        DownloadStateViewCustomization.addDownloadTaskCountChangeListener(this);
        // 检查和启动 查询是否有应用可升级的服务
        // MonitorAppsUpdateServiceHelper.startMonitorAppsUpdateService(this);

        // 注册下载任务个数变化监听
        DownloadTaskManager.addDownloadTaskCountChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.i(TAG, "onStop");
        // 反注册下载任务个数变化监听
        DownloadStateViewCustomization.removeDownloadTaskCountChangeListener(this);
        // Debug.stopMethodTracing( );
        // 反注册下载任务个数变化监听
        DownloadTaskManager.removeDownloadTaskCountChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        Logger.i(TAG, "onDestroy");
        Log.d(TAG, "onDestroy");
        ReportConstants.getInstance().reportAllLast();        //将剩余操作全部上报


        synchronized (mHomePageActivityPtrLock) {
            mHomePageActivityPtr = null;
        }
        // 退出应用时需要发送下载数和更新数给桌面
        mBroadcastManager.sendBroadCastToDesk(false);
        mBroadcastManager.unregisterReceiveres();
        mBroadcastManager.unregisterKeyEventReceive();

        // 显示通知栏通知(一键升级)
        GlobalConfigControllerManager.getInstance().setNotificationVisible(true);
        // 停止更新下载通知定时器
        NotificationCenter.getInstance().stopUpdateDownloadNotification();
        // mGlobalConfigControllerManager.removeLoadingStateListener( );

        WifiUpdateReceiver.removeWifiListener(mWifiListener);
        super.onDestroy();
    }

    /*
     * // 应用商店本身更新 强制升级提醒
     */
    private void sendCheckUpdate() {
        ReqUpdateController controller = new ReqUpdateController(mReqUpdateListener);
        controller.doRequest();
    }

    private final static int UPDATE_NO_PACKAGE_UPDATE = 0;
    private final ReqUpdateListener mReqUpdateListener = new ReqUpdateListener() {

        @Override
        public void onNetError(int errCode, String errorMsg) {
            LogUtils.d("errCode " + errCode + " errorMsg: " + errorMsg);
            // showNoteDialog();
        }

        @Override
        public void onReqFailed(int statusCode, String errorMsg) {
            LogUtils.d("statusCode " + statusCode + " errorMsg: " + errorMsg);
            // showNoteDialog();
        }

        @Override
        public void onRequpdateSucceed(RspUpdate updateInfo) {

            Log.d(TAG, "onRequpdateSucceeds");
            // resetAppInitRunning();
            // updateInfo.updateType = 3;
            // updateInfo.updatePrompt = "hhahaahahahaa";
            mRspUpdate = updateInfo;
            LogUtils.d("updateInfo.getUpdateType: " + updateInfo.updateType);
            if (updateInfo.updateType == UPDATE_NO_PACKAGE_UPDATE) {
                // 无更新
                UpdateUtils.setUpdatePreference(false, updateInfo.updateType);
                // mSettingUiHandler.sendEmptyMessage( MSG_REFRESH_LIST );
                // showNoteDialog();
                Log.d(TAG, "onRequpdateSucceeds no update package");
            } else {
                UpdateUtils.setUpdatePreference(true, updateInfo.updateType);

                // if( updateInfo.getUpdateType( ) == FORCE_UPDATE_TYPE )
                // {
                // Message msg = mUIHandler.obtainMessage( );
                // msg.what = MSG_SHOW_UPDATE_DIALOG;
                // msg.obj = updateInfo;
                Log.d(TAG, "onRequpdateSucceeds 2");
                mUIHandler.sendEmptyMessage(MSG_SHOW_UPDATE_DIALOG);
                if (mGameFragment != null) {
                    mGameFragment.refreshTipVisible();
                }
                // }
                // else
                // {
                // showNoteDialog( );
                // }
                if (UpdateUtils.shouldResetUpdateState()) {
                    // 每隔10min后把更新状态置为false，解决多次下载更新导致的更新包解析错误
                    UpdateUtils.saveUpdateState(false);
                }

                // mUIHandler.sendEmptyMessage(MSG_SHOW_ACTIONBAR_TIP);
            }
        }

    };

    private void cheakLoadingState() {
        int isGlobal = GlobalConfigControllerManager.getInstance().getLoadingState();
        Log.d(TAG, "cheakLoadingState isGlobal = " + isGlobal);
        switch (isGlobal) {
            case GlobalConfigControllerManager.NORMAL_STATE:
                notifyStatusChange(Msg.APPEND_DATA);
                if (SearchRecommendControllerManager.getInstance().getHotSearchLoadingState() == SearchRecommendControllerManager
                        .getInstance().NONETWORK_STATE) {
                    SearchRecommendControllerManager.getInstance().reqConfig();
                }
                break;

            case GlobalConfigControllerManager.LOADING_STATE:
                // mGlobalConfigControllerManager.setLoadingStateListener(
                // mLoadingStateListener );
                notifyStatusChange(Msg.LOADING);
                break;

            case GlobalConfigControllerManager.NONETWORK_STATE:
                // mGlobalConfigControllerManager.setLoadingStateListener(
                // mLoadingStateListener );
                notifyStatusChange(Msg.NET_ERROR);
                break;

            default:
                break;
        }

    }

    private void notifyStatusChange(int status) {
        switch (status) {
            case Msg.APPEND_DATA:
                // 通知NoviceGuidanceView请求数据
                if (GlobalConfigControllerManager.getInstance().getLoadingState() == GlobalConfigControllerManager.NORMAL_STATE) {
                    mNoviceGuidanceView.getHandler().sendEmptyMessage(Msg.REQUEST_DATA);
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mUpdateRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONSTANTS.MSG_CHECK_TOAST:
                    CSToast.show(mContext, (String) msg.obj);
                    break;
                //################## oddshou 添加，修复 下载个数 tips 显示 不正确的问题
                case MSG_CONSTANTS.MSG_CHECK_UPDATE:
                    refreshActionBarTip();
                    break;

            }
        }
    };

    private final LoadingStateListener mLoadingStateListener = new LoadingStateListener() {

        @Override
        public void onChange() {
            int flag = GlobalConfigControllerManager.getInstance().getLoadingState();
            if (flag == GlobalConfigControllerManager.NORMAL_STATE) {
                notifyStatusChange(Msg.APPEND_DATA);
                SearchRecommendControllerManager.getInstance().reqConfig();
            } else if (flag == GlobalConfigControllerManager.LOADING_STATE) {
                notifyStatusChange(Msg.LOADING);
            } else if (flag == GlobalConfigControllerManager.NONETWORK_STATE) {
                notifyStatusChange(Msg.NET_ERROR);
            }

        }

    };

    public void initNoteDialog() {

        if (mApkDownloadManager.getTaskCount() > 0) {
            if (noteDialog == null) {
                noteDialog = new CSAlertDialog(this, getString(R.string.game_center_in_tip,
                        mApkDownloadManager.getTaskCount()), true, false);

            }
            noteDialog.setmLeftBtnTitle(getString(R.string.wait_to_handle));
            noteDialog.setmRightBtnTitle(getString(R.string.imediate_to_handle));
            noteDialog.addRightBtnListener(mNoteDialogLeftClickListener);
            noteDialog.addCheckBoxListener(mCheckBoxListener);
            if (this.isStopping || this.isFinishing()) {
            } else {
                noteDialog.show();
            }
        }

    }

    private final View.OnClickListener mCheckBoxListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox) noteDialog.findViewById(R.id.check);
            if (!checkBox.isChecked()) {
                checkBox.setChecked(false);
                ControllerHelper.getInstance().setNoteToUserToHandleTask("0");
            } else {
                checkBox.setChecked(true);
                ControllerHelper.getInstance().setNoteToUserToHandleTask("1");
            }
        }
    };

    @SuppressWarnings("unused")
    private final View.OnClickListener mNoteDialogLeftClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intentManage = new Intent(HomePageActivity.this, AppManageActivity.class);
            startActivity(intentManage);
            if (noteDialog != null && noteDialog.isShowing()) {
                noteDialog.dismiss();
            }

        }
    };

    private void refreshActionBarTip() {
        // 迁移 可自升级提示到 personalActivity中#############oddhsou
        // mActionBar.setSettingTipVisible(UpdateUtils.hasUpdate() ?
        // View.VISIBLE : View.GONE);
        Logger.i(
                TAG,
                "refreshActionBarTip has updateOrDownload "
                        + mApkInstalledManager.hasUpdateOrDownload() + " count "
                        + getDownloadAndUpCount(), "oddshou");
        mBadgeView.setText(String.valueOf(getDownloadAndUpCount()));
        if (mApkInstalledManager.hasUpdateOrDownload()) {
            mBadgeView.show(true);
        } else {
            mBadgeView.hide();
        }

    }

    private int getDownloadAndUpCount() {
        /*Logger.i(TAG,"getTaskCount === "+mApkDownloadManager.getDownloadingTaskCount());
        Logger.i(TAG,"getTaskCount === "+mApkDownloadManager.getTaskCount());
        Logger.i(TAG,"getUpdateInfoNotInTaskCount ==="+mApkInstalledManager.getUpdateInfoNotInTaskCount());*/
        return mApkDownloadManager.getTaskCount()
                + mApkInstalledManager.getUpdateInfoNotInTaskCount();
    }

//	public int getCurrentItem() {
//		return mViewPager == null ? 0 : mViewPager.getCurrentItem();
//	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Logger.i(TAG, "onSaveInstanceState");
        int flag = mBroadcastManager.getRegisterCount();
        outState.putInt(ISaveInfo.BROAD_CAST_FLAG, flag);
        outState.putBoolean(ISaveInfo.SHOULD_UPDATE, hasUpdated);
        if (mUpdateDialog != null) {
            outState.putBoolean(UPDATE_DIALOG_SHOWING, mUpdateDialog.isShowing());
        }
        if (noteDialog != null) {
            outState.putBoolean(NOTE_DIALOG_SHOWING, noteDialog.isShowing());
        }
        if (mRspUpdate != null) {
            // outState.putSerializable(ISaveInfo.RSP_UPDATE, mRspUpdate);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Logger.i(TAG, "onRestoreInstanceState");
        mApkDownloadManager.setFirstConnect(true);
        int flag = savedInstanceState.getInt(ISaveInfo.BROAD_CAST_FLAG);
        mBroadcastManager.setRegisterCount(flag - 1);
        mBroadcastManager.registerReceiveres();
    }

    private long lastClickKeyBackTime;
    private boolean isStopping = false;

    @Override
    public void onBackPressed() {
        Logger.i(TAG, "oddhsou onBackPressed....");
        Logger.i(TAG, "getTaskCount === " + mApkDownloadManager.getDownloadingTaskCount());
        if (View.VISIBLE == mNoviceGuidanceView.getVisibility()) {
            mNoviceGuidanceView.setVisibility(View.GONE);
        } else if (mBackHandedFragment != null && mBackHandedFragment.onBackPressed()) {
            return;
        } else if (mApkDownloadManager.getDownloadingTaskCount() > 0) {
            showDownLoadTaskIsRun();
        } else {
            long now = System.currentTimeMillis();
            if (now - lastClickKeyBackTime > 2000) {
                CSToast.show(this, getString(R.string.click_exit_label));
                lastClickKeyBackTime = now;
            } else {
                if (!isStopping) {
                    isStopping = true;
                    CSToast.cancelToast();

                    finishAndStopDownlaodService();
                    //                    finish();
                    // 是否是应用主界面没有启动时从通知进入的该界面
                    NotificationCenter.setIsHomeNotRunFromNotification(true);
                }
                // android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    private BackHandledFragment mBackHandedFragment;

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    public void wifiConnected() {
        //wifi状态
        Message msg = mUIHandler.obtainMessage(MSG_REFRESH_WIFI);
        msg.arg1 = 1;
        mUIHandler.sendMessage(msg);
    }

    public final static int MSG_NOTE_TO_HANDLE_DOWNLOAD_TASK = 40001;
    public final static int MSG_CHEAK_LOADING_STATE = 40002;
    public static final int MSG_START_AN = 40004;
    public static final int MSG_SHOW_UPDATE_DIALOG = 40005;
    public static final int MSG_SHOW_ACTIONBAR_TIP = 40006;
    public static final int MSG_GOTO_UPDATE = 40007;

    public static final int MSG_REFRESH_DOWNLOAD_COUNT_ICON = 40011;
    public static final int MSG_SHOW_NOVICE_GUIDANCE_VIEW = 40012;// 网络恢复连接时显示新手推荐页面

    // 操作同步锁，保证操作mHomePageActivityPtrLock的方法同时只有一个执行
    public static final Object mHomePageActivityPtrLock = new Object();
    public static HomePageActivity mHomePageActivityPtr = null;

    // 网络恢复连接时显示新手推荐页面
    public static void showNoviceGuidanceViewWhenNetRecover() {

        synchronized (mHomePageActivityPtrLock) {
            if (mHomePageActivityPtr != null && mbNetworkConnectedShowGuidanceView) {
                mHomePageActivityPtr.mUIHandler.sendEmptyMessage(MSG_SHOW_NOVICE_GUIDANCE_VIEW);
            }
        }
    }

    public static boolean IsHomePageFinish() {
        synchronized (mHomePageActivityPtrLock) {
            return mHomePageActivityPtr == null;
        }
    }

    private static final int MSG_REFRESH_WIFI = 0X01;

    @SuppressLint("HandlerLeak")
    private final Handler mUIHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_SHOW_NOVICE_GUIDANCE_VIEW:
                    showNoviceGuidanceView();
                    break;
                case MSG_REFRESH_DOWNLOAD_COUNT_ICON:
                    refreshActionBarTip();
                    break;
                case MSG_GOTO_UPDATE:
                    // if(!hasUpdated){
                    sendCheckUpdate();
                    /*
                     * } hasUpdated = true;
                     */
                    break;
                case MSG_NOTE_TO_HANDLE_DOWNLOAD_TASK:
                    // 判断应用商店本身是否需要更新，主要是看上次与本次的更新时间间隔
                    if (UpdateUtils.shouldCheckUpdate()) {
                        sendCheckUpdate();
                    } else {
                        // showNoteDialog();
                    }

                    break;
                case MSG_CHEAK_LOADING_STATE:
                    Log.d(TAG, "MSG_CHEAK_LOADING_STATE");
                    cheakLoadingState();
                    break;
                case MSG_SHOW_UPDATE_DIALOG:
                    Log.d(TAG, "MSG_SHOW_UPDATE_DIALOG isAction =" + isAction);
                    if (!isAction)
                        break;
                    if (mUpdateDialog == null) {
                        mUpdateDialog = new UpdateDialog(HomePageActivity.this, mRspUpdate);
                    } else {
                        mUpdateDialog.setmUpdateInfo(mRspUpdate);
                    }
                    if (mUpdateDialog.isShowing()) {
                        break;
                    }
                    mUpdateDialog.setListeners(mUpdateListener, mUpdateCancelListener);
                    mUpdateDialog.show();
                    LogUtils.i("isUpdateDialogShowing--" + mUpdateDialog.isShowing());
                    Log.d(TAG, "isUpdateDialogShowing--" + mUpdateDialog.isShowing());
                    break;

                case MSG_SHOW_ACTIONBAR_TIP: // 有更新的时候显示设置上面的图标,已经迁移
                    // mActionBar.setSettingTipVisible(UpdateUtils.hasUpdate() ?
                    // View.VISIBLE
                    // : View.GONE);
                    break;
                case MSG_REFRESH_WIFI:
                    int state = msg.arg1;
                    findViewById(R.id.imgWifi).setSelected(state == 1);
                    break;
                case UpdateDownloadController.MSG_UPDATE_PROGRESS:
                    Bundle bundle = msg.getData();
                    long all_size = bundle.getLong(UPDATE_ALL);
                    long current_size = bundle.getLong(UPDATE_CURRENT);
                    int progress = (int) ((current_size * 100 / all_size));
                    if (mUpdateDialog != null) {
                        mUpdateDialog.setProgress(progress);
                    }
                    break;
                case UpdateDownloadController.MSG_SET_PATH:
                    Bundle bundleP = msg.getData();
                    if (mUpdateDialog != null) {
                        mUpdateDialog.setAppPath(bundleP.getString(APP_PATH));
                    }
                case UpdateDownloadController.MSG_DOWNLOAD_FAIL:
                    if (mUpdateDialog.isShowing()) {
                        mUpdateDialog.setDownloadFailStatus();
                    }
                default:
                    break;
            }
        }

    };

    private final OnClickListener mUpdateListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            if (mUpdateDialog != null) {
                // mUpdateDialog.dismiss( );

                if (mRspUpdate != null && mRspUpdate.updateType == FORCE_UPDATE_TYPE) {
                    mUpdateDialog.startDownload();
                } else {
                    mUpdateDialog.dismiss();
                }
            }
            if (UpdateUtils.getUpdateState()) {
                return;
            }
            UpdateUtils.saveUpdateState(true);
            UpdateDownloadController controller = UpdateDownloadController.getInstance();
            controller.setmHandler(mUIHandler);
            controller.startDonwloadApk((RspUpdate) view.getTag());
            UpdateUtils.setUpdatePreference(false, 3);

        }
    };

    private final OnClickListener mUpdateCancelListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            // LogUtils.d( "mUpdateCancelListener onClick mUpdateDialog: " +
            // mUpdateDialog + "mUpdateDialog.isShowing : " +
            // mUpdateDialog.isShowing( ) );

            // if( mUpdateDialog != null && mUpdateDialog.isShowing( ) )
            if (mUpdateDialog != null) {
                mUpdateDialog.dismiss();
                // LogUtils.d( "mUpdateCancelListener mUpdateDialog.dismiss " );
            }
        }
    };

    public void finishAndStopDownlaodService() {

        if (mToastDialog != null && mToastDialog.isShowing()) {
            mToastDialog.dismiss();
        }
        mApkDownloadManager.stopAllDownload();
//		mApkDownloadManager.saveAllTaskToDB();

        finish();
    }

    private CSAlertDialog mToastDialog;

    public void showDownLoadTaskIsRun() {
        if (mToastDialog == null) {
            mToastDialog = new CSAlertDialog(this, getString(R.string.game_center_out_tip,
                    mApkDownloadManager.getDownloadingTaskCount()), true, true);
        }
        mToastDialog.setTips(getString(R.string.game_center_out_tip,
                mApkDownloadManager.getDownloadingTaskCount()));
        mToastDialog.setmCheckTitle(getString(R.string.check_title_continue));
        mToastDialog.addCheckBoxListener(mToastCheckListener);
        mToastDialog.setmLeftBtnTitle(getString(R.string.app_setting_about_button_text));
        mToastDialog.setmRightBtnTitle(getString(R.string.csl_cancel));
        mToastDialog.addLeftBtnListener(mDialogLeftClickListener);
        mToastDialog.addRightBtnListener(mDialogRightClickListener);
        mToastDialog.show();

    }

    private final View.OnClickListener mToastCheckListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox) mToastDialog.findViewById(R.id.check);
            if (!checkBox.isChecked()) {
                checkBox.setChecked(false);
                isChechedSelect = true;
            } else {
                checkBox.setChecked(true);
                isChechedSelect = false;
            }
        }
    };

    private final View.OnClickListener mDialogLeftClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // 退出处理
            if (isChechedSelect) {
                finishAndStopDownlaodService();
            }
            if (mToastDialog != null && mToastDialog.isShowing()) {
                mToastDialog.dismiss();
            }
            finish();
        }
    };

    private final View.OnClickListener mDialogRightClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            isStopping = false;
            if (mToastDialog != null && mToastDialog.isShowing()) {
                mToastDialog.dismiss();
            }
        }
    };

    private void showNoteDialog() {
        if ("0".equals(ControllerHelper.getInstance().getNoteToUserToHandleTask())
                || "".equals(ControllerHelper.getInstance().getNoteToUserToHandleTask())) {
            initNoteDialog();
        }
    }


    public boolean isHasDestroied() {
        return hasDestroied;
    }

    public void setHasDestroied(boolean hasDestroied) {
        this.hasDestroied = hasDestroied;
    }

    @Override
    public void onDownloadTaskCountChange() {
        //这个方法两个接口公用，IDownloadTaskObserver，IDownloadTaskCountChangeListener
        HomePageActivity.this.mUIHandler.sendEmptyMessage(MSG_REFRESH_DOWNLOAD_COUNT_ICON);// 刷新下载图标个数

    }

    @Override
    public void apkInstallFinish(DownloadTask info) {
        // TODO Auto-generated method stub
        mUIHandler.sendEmptyMessage(MSG_REFRESH_DOWNLOAD_COUNT_ICON);// 刷新下载图标个数
    }

    /**
     * xutils web 请求
     *
     * @param url
     */
    private void doPost(final String url, final WifiHttpUtils wifiHttpUtils) {
        RequestParams params = new RequestParams(url);
        params.addParameter("_data", wifiHttpUtils.getParmarData());
        params.addParameter("_sign", wifiHttpUtils.getParmarSign());
        x.http().post(params, new JsonCallback(url, this) {

            @Override
            protected void handleSucced(JSONObject ddata, String url) {
                switch (url) {
                    case WifiHttpUtils.URL_WIFI_FRESH:      //刷新会话成功
                        Message msg = mUIHandler.obtainMessage(MSG_REFRESH_WIFI);
                        msg.arg1 = 1;
                        mUIHandler.sendMessage(msg);
                        Log.e(TAG, "refrsh success");
                        break;
                }
            }

            @Override
            protected void onException(String code, String codemsg, String url) {
                if (!code.equals("99")) {
                    //失败
                    Message msg = mUIHandler.obtainMessage(MSG_REFRESH_WIFI);
                    msg.arg1 = 0;
                    mUIHandler.sendMessage(msg);
                }
            }
        });

    }

    /**
     * 监听断开wifi或者网络切换，如果从wifi页开网成功需要回调
     */
    private WifiUpdateReceiver.WifiListener mWifiListener = new WifiUpdateReceiver.WifiListener() {
        @Override
        public void networkChange(int currentNetwork, NetworkInfo networkInfo) {
            if (currentNetwork == 1) {
               //如果连上wifi可以刷新会话测试是否连接成功
                boolean checkIndentifySsid = NetUtils.CheckIndentifySsid(HomePageActivity.this, WifiHttpUtils.SSID_HEAD);
                //ping 公网进一步验证
                if (checkIndentifySsid) {
                    WifiHttpUtils wifiHttpUtils = new WifiHttpUtils(new JSONObject());
                    wifiHttpUtils.getmHdata().setVer(2);
                    doPost(WifiHttpUtils.URL_WIFI_FRESH, wifiHttpUtils);
                }
            } else {
                //wifi切换到其他状态
                Message msg = mUIHandler.obtainMessage(MSG_REFRESH_WIFI);
                msg.arg1 = 0;
                mUIHandler.sendMessage(msg);
            }
        }
    };
}
