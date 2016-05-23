
package com.hykj.gamecenter.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTaskStateListener;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.ApkInstalledManager.InstallFinishListener;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.protocol.Apps.GroupElemInfo;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog.KnowBtnOnClickListener;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.ui.widget.DownloadListButton;
import com.hykj.gamecenter.ui.widget.InterceptTouchFrameLayout;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.WifiDownLoadOnClickListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utilscs.LogUtils;

//显示下载，暂停，继续，安装，打开状态
public class DownloadStateView extends InterceptTouchFrameLayout implements
        InstallFinishListener {

    private static final String TAG = DownloadStateView.class.getName();

    private final Context mContext;
    private Resources mRes = null;

    private AppInfo mAppInfo = null;
    private int mAppPosType = 0;// STATIS_TYPE类型

    private ApkInstalledManager mApkInstalledManager;
    private ApkDownloadManager mApkDownloadManager;
    private StatisticManager mStatisticManager;

    private DownloadListButton mAppDownloadBtn;
    private DownloadListButton mAppOpenOrUpdateBtn;
    private TextView mAppInstalled;
    private FrameLayout mFrameLayout;
    // private ProgressBar mAppActiveProgress;

    public static final String APP_INFO_TAG = "appinfo_tag";
    public static final String APP_TYPE = "app_type";

    // details
    private GroupElemInfo mGroupElemInfo = null;

    public DownloadStateView(Context context) {
        super(context);
        this.mContext = context;
    }

    public DownloadStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public DownloadStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    /*
     * (non-Javadoc)
    <<<<<<< HEAD
     * 
    =======
    >>>>>>> 25e75dfba511f13b00ca73ba21d7437a249efba2
     * @see android.view.ViewGroup#onAttachedToWindow()
     */
    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();

        // Logger.d(TAG, "onAttachedToWindow");

        // updateDownLoadBtn(null);
        mApkDownloadManager.registerListener(downloadTaskStateListener);
        // 下载消息通知注册
        mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);

    }

    /*
     * (non-Javadoc)
    <<<<<<< HEAD
     * 
    =======
    >>>>>>> 25e75dfba511f13b00ca73ba21d7437a249efba2
     * @see android.view.ViewGroup#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();

        // Logger.d(TAG, "onDetachedFromWindow");

        mApkDownloadManager.unregisterListener(downloadTaskStateListener);//
        // 界面关闭反注册消息通知
        mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initView();
    }

    // final 对于对象引用，不能改变的是他的引用，而对象本身是可以修改的
    public void setAppPosType(final int appPosType) {// appPosType
                                                     // :STATIS_TYPE类型
        mAppPosType = appPosType;
    }

    // final 对于对象引用，不能改变的是他的引用，而对象本身是可以修改的
    public void setGroupElemInfo(final GroupElemInfo groupElemInfo) {
        mGroupElemInfo = groupElemInfo;
        mAppInfo = Tools.createAppInfo(mGroupElemInfo);
        updateDownLoadBtn(null);
    }

    public void initView() {
        Logger.i(TAG, "initView()");
        mRes = mContext.getResources();

        /*
         * // 应用信息 mGroupElemInfo = (GroupElemInfo)
         * getIntent().getSerializableExtra( KEY.GROUP_INFO); ？？？等会添加
         */

        mFrameLayout = (FrameLayout) findViewById(R.id.root_first_child_layout);
        /* mAppPosType = STATIS_TYPE.DOWNLOAD; */
        // 安装
        mApkInstalledManager = ApkInstalledManager.getInstance();
        mApkDownloadManager = DownloadService.getDownloadManager();

        // TODO
        mApkInstalledManager.addInstallListener(this);
        mStatisticManager = StatisticManager.getInstance();

        if (mGroupElemInfo == null) {
            // TODO
            // 用于支持云指令
            // handleAction(); 当前不支持，后期支持可以写一个Receiver，这里可以
        } else {
            mAppInfo = Tools.createAppInfo(mGroupElemInfo);
        }

        mApkDownloadManager.setmPagePostion(mAppPosType);

        initHeaderAppInfo();

        updateDownLoadBtn(null);
        /*
         * mApkDownloadManager.registerListener(downloadTaskStateListener); //
         * 下载消息通知注册
         * mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);
         */

        // initRequestData(); 目前不需要请求详细信息，只需要去下载队列中查询下载或像下载队列中添加即可，不需要向后台请求数据
    }

    public void onSwitchScreenOrientation(
            android.content.res.Configuration newConfig) {
        Logger.i(TAG, "onSwitchScreenOrientation() ");

    }

    private void UnregisterDownloadListener() {

        mApkDownloadManager.unregisterListener(downloadTaskStateListener);
        // 界面关闭反注册消息通知
        mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);

    }

    private void initHeaderAppInfo() {

        mAppDownloadBtn = (DownloadListButton) findViewById(R.id.app_download_btn);
        mAppOpenOrUpdateBtn = (DownloadListButton) findViewById(R.id.app_openorupdate);
        mAppInstalled = (TextView) findViewById(R.id.app_installed);

        // mAppProgress = (CSProgressButton)findViewById(
        // R.id.app_progress_button );
        // mAppActiveProgress = (ProgressBar)
        // findViewById(R.id.action_detail_progress_bar);

        mAppDownloadBtn.setKownOnClickListener(mKnowBtnOnClickListener);
        mAppDownloadBtn
                .setWifiLoadOnClickListener(mWifiDownLoadOnClickListener);

        mAppOpenOrUpdateBtn.setKownOnClickListener(mKnowBtnOnClickListener);
        mAppOpenOrUpdateBtn
                .setWifiLoadOnClickListener(mOpenOrUpdateOnClickListener);

        mAppDownloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

    }

    private final DownloadTaskStateListener downloadTaskStateListener = new DownloadTaskStateListener() {
        @Override
        public void onUpdateTaskState(DownloadTask task) {

            if (null == mAppInfo) {
                return;
            }
            LogUtils.e("请求更新状态的应用:" + task.appName + "|应用编号:" + task.appId);
            if (task.appId == mAppInfo.appId) {
                LogUtils.e("实际更新状态的应用:" + task.appName + "|应用编号:" + task.appId);
                mAppDownloadBtn.setTag(task);
                updateDownLoadBtn(task);
            }
        }

        @Override
        public void onUpdateTaskProgress(DownloadTask task) {

            if (null == mAppInfo) {
                return;
            }
            LogUtils.e("请求更新进度的应用:" + task.appName + "|应用编号:" + task.appId);
            if (task.appId == mAppInfo.appId) {
                LogUtils.e("实际更新进度的应用:" + task.appName + "|应用编号:" + task.appId);
                initProgress(task);
            }

        }

        @Override
        public void onUpdateTaskList(Object obj) {
            updateDownLoadBtn(null);
        }
    };

    public void initProgress(DownloadTask task) {
        if (task != null) {
            long totalSize = task.fileLength;
            long dealtSize = task.progress;
            // LogUtils.e( "task.fileLength=" + task.fileLength );
            // LogUtils.e( "task.progress=" + task.progress );
            // mAdapter.updateProgress( packageName , dealtSize , totalSize );
            int percent = (int) (((double) dealtSize / (double) totalSize) * 100);
            if (percent <= 99
                    && task.packageName.equals(mAppInfo.packName)) {
                // mAppProgress.setProgress( 100 - percent );
                // mAppActiveProgress.setProgress(percent);
            }
        }

    }

    public void downloadBtnToOpenOrUpdate(String packName) {
        LogUtils.e("应用:" + packName + ",已安装且未在下载队列中。");
        mAppDownloadBtn.setVisibility(View.GONE);
        mAppInstalled.setVisibility(View.GONE);
        mAppInstalled.setText(mRes.getString(R.string.app_installed));

        // mAppActiveProgress.setVisibility(View.GONE);

        mAppOpenOrUpdateBtn.setVisibility(View.VISIBLE);
        if (mApkInstalledManager.isApkNeedToUpdate(packName)) {
            mAppOpenOrUpdateBtn
                    .setText(mContext.getString(R.string.app_update));
            mAppOpenOrUpdateBtn
                    .setBackgroundResource(R.drawable.csls_button_blue_edit);
            mAppOpenOrUpdateBtn.setTextColor(getResources().getColor(
                    R.color.csls_button_normal_text_edit));
            mAppOpenOrUpdateBtn.setTag(mAppInfo);
        } else {
            mAppOpenOrUpdateBtn.setText(mContext.getString(R.string.app_open));
            mAppOpenOrUpdateBtn
                    .setBackgroundResource(R.drawable.csls_button_white_edit);
            mAppOpenOrUpdateBtn.setTextColor(getResources().getColor(
                    R.color.csls_button_open_text_edit));
            LogUtils.e("activity class name = "
                    + mApkInstalledManager.getActivityClassName(packName));
            LogUtils.e("packname = " + packName);
            mAppOpenOrUpdateBtn.setTag(new String[] {
                    mApkInstalledManager.getActivityClassName(packName),
                    packName
            });
        }
    }

    private void updateDownLoadBtn(DownloadTask dinfo) {

        // int padding = dpTopx( 18 );
        // mAppDownloadBtn.setPadding( padding , 0 , padding , 0 );
        LogUtils.e("updateDownLoadBtn mAppInfo = " + mAppInfo);

        if (mAppInfo == null) {
            return;
        }
        dinfo = mApkDownloadManager.getDownloadTaskByAppId(mAppInfo.appId);
        final String packageName = mAppInfo.packName;
        if (mApkInstalledManager.isApkLocalInstalled(packageName)
                && dinfo == null) {
            downloadBtnToOpenOrUpdate(packageName);
            return;
        }
        if (dinfo != null) {
            initProgress(dinfo);
            mAppDownloadBtn.setTag(dinfo);

            // mAppActiveProgress.setVisibility(View.GONE);
            mAppInstalled.setVisibility(View.GONE);
            mAppDownloadBtn.setVisibility(View.VISIBLE);
            mAppOpenOrUpdateBtn.setVisibility(View.GONE);

            switch (dinfo.getState()) {
                case PREPARING:
                    // mAppActiveProgress.setProgress(0);
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_yellow_edit);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_pause));
                    mAppDownloadBtn.setEnabled(true);
                    // mAppActiveProgress.setVisibility(View.VISIBLE);
                    break;
                case WAITING:
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_yellow_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_pause));
                    // mAppActiveProgress.setVisibility(View.VISIBLE);
                    break;
                case STARTED:
                case LOADING:
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_yellow_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_pause));
                    // mAppActiveProgress.setVisibility(View.VISIBLE);
                    break;
                case STOPPED:
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_blue_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_resume));
                    // mAppActiveProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCEEDED:
                    // TODO
                    // mAppActiveProgress.setProgress(100);
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_blue_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_install));
                    // if( mApkInstalledManager.isApkLocalInstalled(
                    // dinfo.packageName ) )
                    // {
                    // //如果安装立即刷新列表
                    // mAppDownloadBtn.setVisibility( View.GONE );
                    // mAppInstalled.setVisibility( View.VISIBLE );
                    // mAppInstalled.setText( getString( R.string.app_installed ) );
                    // LogUtils.e( "详情界面,应用:" + dinfo.appName + "|" +
                    // dinfo.packageName + ",已安装" );
                    // downloadBtnToOpenOrUpdate( dinfo.packageName );
                    // }
                    // mAppProgress.setVisibility( View.GONE );
                    break;
                case DELETED:
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_green_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn
                            .setText(mRes.getString(R.string.app_redownload));
                    // mAppActiveProgress.setVisibility(View.VISIBLE);
                    mAppInstalled.setVisibility(View.GONE);
                    mAppDownloadBtn.setVisibility(View.VISIBLE);
                    break;
                case INSTALLING:
                    // TODO
                    // mAppActiveProgress.setProgress(100);
                    mAppInstalled.setVisibility(View.VISIBLE);
                    mAppDownloadBtn.setVisibility(View.GONE);
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_blue_edit);
                    mAppDownloadBtn.setEnabled(false);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_install));
                    // mAppProgress.setVisibility( View.GONE );
                    mAppInstalled.setText(mRes.getString(R.string.app_installing));
                    break;

                case FAILED_NETWORK:
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_green_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_retry));
                    // mAppActiveProgress.setVisibility(View.VISIBLE);
                    break;
                case FAILED_BROKEN:
                    // mAppProgress.setVisibility( View.GONE );
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_green_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn
                            .setText(mRes.getString(R.string.app_redownload));
                    break;
                case FAILED_NOEXIST:
                    // mAppProgress.setVisibility( View.GONE );
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_red_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_delete));
                    break;
                case FAILED_SERVER:
                    // mAppProgress.setVisibility( View.GONE );
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_green_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_retry));

                    break;
                case FAILED_NOFREESPACE:
                    // mAppProgress.setVisibility( View.GONE );
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.csls_button_green_edit);
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_retry));
                    break;
            }
        } else {
            mAppDownloadBtn.setVisibility(View.VISIBLE);
            mAppDownloadBtn.setText(mRes.getString(R.string.app_download));
            mAppDownloadBtn
                    .setBackgroundResource(R.drawable.csls_button_green_edit);
            mAppDownloadBtn.setEnabled(true);
            mAppInstalled.setVisibility(View.GONE);
            mAppDownloadBtn.setTag(dinfo);
            // mAppActiveProgress.setVisibility(View.GONE);
            mAppOpenOrUpdateBtn.setVisibility(View.GONE);
        }

    }

    // 加载
    // private final ICSLoadingViewListener mCSLoadingViewListener = new
    // ICSLoadingViewListener( )
    // {
    //
    // @Override
    // public void onRetryRequestData()
    // {
    // mUiHandler.sendEmptyMessage( MSG_REQUEST_DETAIL_DATA );
    // }
    //
    // @Override
    // public void onInitRequestData()
    // {
    // mUiHandler.sendEmptyMessage( MSG_REQUEST_DETAIL_DATA );
    // }
    // };

    /*
     * private final ICSListViewLoadingRetry mICSListViewLoadingRetry = new
     * ICSListViewLoadingRetry() {
     * 
     * @Override public void onRetry() { // initRequestData(); //
     * 目前不需要请求详细信息，只需要去下载队列中查询下载或像下载队列中添加即可，不需要向后台请求数据 }
     * 
     * };
     */

    private static final int MSG_REQUEST_DETAIL_DATA = 1000;
    private static final int MSG_GET_DETAIL = 1001;
    private static final int MSG_NET_ERROR = 1002;
    private final static int MSG_REFRESH_APP_DOWNLOAD_BTN = 2001;

    private final Handler mUpdateRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONSTANTS.MSG_CHECK_UPDATE: {
                    // mAppDownloadBtn.setVisibility( View.GONE );
                    // mAppInstalled.setVisibility( View.VISIBLE );
                    // mAppInstalled.setText( getString( R.string.app_installed ) );
                    break;
                }
                case MSG_CONSTANTS.MSG_CHECK_TOAST: {
                    CSToast.show(mContext, (String) msg.obj);
                    break;
                }
            }
        }
    };

    private final Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            /*
             * 目前不需要请求详细信息，只需要去下载队列中查询下载或像下载队列中添加即可，不需要向后台请求数据 case
             * MSG_REQUEST_DETAIL_DATA: reqAppDetailData(); break; case
             * MSG_GET_DETAIL: { mAppDownload.setText(mAppInfo.getDownTimes() +
             * ""); // mAppSize.setText( mContext.getString( //
             * R.string.app_info_size_flag , mAppInfo.getPackSize( ) ) );
             * mAppSize.setText(StringUtils.byteToString(mAppInfo
             * .getPackSize())) ; // 设置请求的到值 break; } case MSG_NET_ERROR: break;
             */
                case MSG_REFRESH_APP_DOWNLOAD_BTN:
                    LogUtils.e("安装成功刷新界面开始");

                    updateDownLoadBtn((DownloadTask) msg.obj);
                    break;

            }
        }
    };

    // 非wifi状态下才会调用此函数
    private final KnowBtnOnClickListener mKnowBtnOnClickListener = new KnowBtnOnClickListener() {

        @Override
        public void onKnowClickListener(View v) {

            Log.d(TAG, "mKnowBtnOnClickListener onKnowClickListener ");
            DownloadTask dinfo = (DownloadTask) v.getTag();
            if (dinfo == null) {
//                addTaskToDownLoadList(v);
              //###############oddshou 非 wifi状态下 继续下载
                mApkDownloadManager.startDownload(mAppInfo,
                        ReportConstants.STAC_APP_POSITION_APP_DETAIL, mAppPosType);
            } else {
                return;
            }
        }
    };

    // 当前是没有WiFi的情况下调用该函数
//    private void addTaskToDownLoadList(final View v) {
//        mApkDownloadManager.startDownload(mAppInfo,
//                StatisticManager.STAC_APP_POSITION_APP_DETAIL, mAppPosType);
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sleep(500); // 睡眠500ms是等待下载的状态发生改变
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                DownloadTask task = (DownloadTask) v.getTag();
//                mApkDownloadManager.stopDownload(task);
//                // 非wifi情况下，系统将任务加入下载队列之后，自动暂停下载任务
//                mStatisticManager.reportDownloadStop(task.appId, task.packId,
//                        task.nFromPos,
//                        StatisticManager.STAC_DOWNLOAD_APK_OTHERS_STOP,
//                        mRes.getString(R.string.not_wifi_stop));
//                MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_OTHERS);
//            };
//        }.start();
//    }

    private final WifiDownLoadOnClickListener mOpenOrUpdateOnClickListener = new WifiDownLoadOnClickListener() {

        @Override
        public void onWifiClickListener(View v) {

            Log.d(TAG, "mOpenOrUpdateOnClickListener onWifiClickListener ");
            Object obj = v.getTag();
            if (obj instanceof String[]) {
                String[] args = (String[]) obj;
                Intent intent = new Intent("android.intent.action.MAIN");
                LogUtils.e(args[0] + "|" + args[1]);
                intent.setClassName(args[1], args[0]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else if (obj instanceof AppInfo) {
                mApkDownloadManager.startDownload(mAppInfo,
                        ReportConstants.STAC_APP_POSITION_APP_DETAIL,
                        mAppPosType);
            }
        }
    };

    private final WifiDownLoadOnClickListener mWifiDownLoadOnClickListener = new WifiDownLoadOnClickListener() {

        @Override
        public void onWifiClickListener(View v) {

            Log.d(TAG, "mWifiDownLoadOnClickListener onWifiClickListener ");
            // if( UITools.isFastDoubleClick( ) )
            // {
            // return;
            // }
            v.setEnabled(false);
            DownloadTask dinfo = (DownloadTask) v.getTag();
            if (dinfo != null) {
                switch (dinfo.getState()) {
                    case PREPARING:
                    case WAITING:
                    case STARTED:
                    case LOADING:
                        mApkDownloadManager.stopDownload(dinfo);
                        // 用户主动暂停下载任务上报
                        Logger.i("AppInfoActivity", "dinfo.packId=" + dinfo.packId);
                        ReportConstants
                                .reportDownloadStop(
                                        dinfo.appId,
                                        dinfo.packId,
                                        dinfo.nFromPos,
                                        ReportConstants.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP,
                                        "");
                        MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_USER_ACTIVE_STOP);
                        break;
                    case SUCCEEDED:
                        mApkDownloadManager.installDownload(dinfo);
                        break;
                    case STOPPED:
                    case FAILED_NETWORK:
                    case FAILED_SERVER:
                    case FAILED_NOFREESPACE:
                        mApkDownloadManager.resumeDownload(dinfo);
                        // 暂停下载任务继续下载上报
                        ReportConstants
                                .reportDownloadResume(
                                        dinfo.appId,
                                        dinfo.packId,
                                        dinfo.nFromPos,
                                        ReportConstants.STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME,
                                        "");
                        MtaUtils.trackDownloadResume();
                        break;
                    case FAILED_BROKEN:
                    case DELETED:
                        mApkDownloadManager.restartDownload(dinfo);
                        break;
                    case FAILED_NOEXIST:
                        mApkDownloadManager.removeDownload(dinfo);
                        // 取消下载任务
                        ReportConstants.reportDownloadStop(dinfo.appId,
                                dinfo.packId, dinfo.nFromPos,
                                ReportConstants.STAC_DOWNLOAD_APK_CANCEL_TASK, "");
                        MtaUtils.trackDownloadCancel(dinfo.appName);
                        break;
                }
            } else {
                // 详情下载位置上报
                mApkDownloadManager.startDownload(mAppInfo,
                        ReportConstants.STAC_APP_POSITION_APP_DETAIL,
                        mAppPosType);
                // mAppActiveProgress.setProgress(0);
            }
        }
    };

    @Override
    public void apkInstallFinish(DownloadTask task) {

        if (task == null || mAppInfo == null) {
            return;
        }
        if (task.appId == mAppInfo.appId) {
            Message msg = Message.obtain();
            msg.what = MSG_REFRESH_APP_DOWNLOAD_BTN;
            msg.obj = task;
            mUiHandler.sendMessage(msg);
        }
    }
}
