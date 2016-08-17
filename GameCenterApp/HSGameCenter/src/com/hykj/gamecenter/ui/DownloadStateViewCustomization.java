
package com.hykj.gamecenter.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import com.hykj.gamecenter.utils.Interface.IDownloadTaskCountChangeListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.Tools;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.util.ArrayList;

//所有状态不显示，只显示下载按钮，下载过程中显示进度，下载开始后不能暂停(所有状态图标都显示的是一个图片，但功能不变，相当于下一步按钮)
public class DownloadStateViewCustomization extends InterceptTouchFrameLayout implements
        InstallFinishListener
{

    private static final String TAG = "DownloadStateViewCustomization";

    private final Context mContext;
    private Resources mRes = null;

    private AppInfo mAppInfo = null;
    private int mAppPosType = 0;// STATIS_TYPE类型

    /*
     * 下载任务个数变化监听(用户通知相关界面进行操作，比如说更新界面下载个数)
     * 不放在ApkDownloadManager管理中是为了ApkDownloadManager中只做下载的事情，
     * 界面相关处理交给DownloadStateViewCustomization去分发(降低耦合，并且
     * 个数变化也不是ApkDownloadManager必须的，当然也可以在ApkDownloadManager用一个 线程处理消息分发)
     */
    private static final ArrayList<IDownloadTaskCountChangeListener> mDownloadTaskCountChangeListenerList = new ArrayList<IDownloadTaskCountChangeListener>();

    // modify at 20131128
    private void sendDownloadTaskCountChangeNotify()
    {
        for (IDownloadTaskCountChangeListener listener : mDownloadTaskCountChangeListenerList)
        {
            listener.onDownloadTaskCountChange();
        }
    }

    public static void addDownloadTaskCountChangeListener(IDownloadTaskCountChangeListener listener)
    {
        if (!mDownloadTaskCountChangeListenerList.contains(listener))
        {
            mDownloadTaskCountChangeListenerList.add(listener);
        }
    }

    public static void removeDownloadTaskCountChangeListener(
            IDownloadTaskCountChangeListener listener)
    {
        if (!mDownloadTaskCountChangeListenerList.contains(listener))
        {
            return;
        }
        mDownloadTaskCountChangeListenerList.remove(listener);
    }

    private ApkInstalledManager mApkInstalledManager;
    private ApkDownloadManager mApkDownloadManager;
    private StatisticManager mStatisticManager;

    private DownloadListButton mAppDownloadBtn;
    private DownloadListButton mAppOpenOrUpdateBtn;
    private TextView mAppInstalled;

    private ProgressBar mAppActiveProgressLoading;
    private ProgressBar mAppActiveProgressResume;
    private FrameLayout frameLayoutRoundProgressBarlayout;
    public static final String APP_INFO_TAG = "appinfo_tag";
    public static final String APP_TYPE = "app_type";

    // details
    private GroupElemInfo mGroupElemInfo = null;
    //用于统计，位置 id
    private int mAppPosition = 0;

    public DownloadStateViewCustomization(Context context)
    {
        super(context);
        this.mContext = context;
    }

    public DownloadStateViewCustomization(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
    }

    public DownloadStateViewCustomization(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.ViewGroup#onAttachedToWindow()
     */
    @Override
    protected void onAttachedToWindow()
    {
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
     * 
     * @see android.view.ViewGroup#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow()
    {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();

        // Logger.d(TAG, "onDetachedFromWindow");

        mApkDownloadManager.unregisterListener(downloadTaskStateListener);
        // 界面关闭反注册消息通知
        mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);

    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        initView();
    }

    // final 对于对象引用，不能改变的是他的引用，而对象本身是可以修改的
    public void setAppPosType(final int appPosType)
    {// appPosType
     // :STATIS_TYPE类型
        mAppPosType = appPosType;
    }

    public void setAppPosition(int appPosition) {
        mAppPosition = appPosition;
    }

    // final 对于对象引用，不能改变的是他的引用，而对象本身是可以修改的
    public void setGroupElemInfo(final GroupElemInfo groupElemInfo)
    {
        mGroupElemInfo = groupElemInfo;
        mAppInfo = Tools.createAppInfo(mGroupElemInfo);
        //        DownloadTask dinfo = mApkDownloadManager.getDownloadTaskByAppId(mAppInfo.appId);
        //        updateDownLoadBtn(dinfo);
        updateDownLoadBtn(null);
    }

    public void initView()
    {
        /* Logger.i(TAG, "initView()"); */
        mRes = mContext.getResources();

        // 安装
        mApkInstalledManager = ApkInstalledManager.getInstance();
        mApkDownloadManager = DownloadService.getDownloadManager();

        // TODO
        mApkInstalledManager.addInstallListener(this);
        mStatisticManager = StatisticManager.getInstance();

        if (mGroupElemInfo == null)
        {
            // TODO
            // 用于支持云指令
            // handleAction(); 当前不支持，后期支持可以写一个Receiver，这里可以
        }
        else
        {
            mAppInfo = Tools.createAppInfo(mGroupElemInfo);
        }

        mApkDownloadManager.setmPagePostion(mAppPosType);

        initHeaderAppInfo();

        updateDownLoadBtn(null);
        /*
         * mApkDownloadManager.registerListener(downloadTaskStateListener);
         * 下载消息通知注册
         * mApkInstalledManager.addUiRefreshHandler(mUpdateRefreshHandler);
         */

        // initRequestData(); 目前不需要请求详细信息，只需要去下载队列中查询下载或像下载队列中添加即可，不需要向后台请求数据
    }

    public void onSwitchScreenOrientation(android.content.res.Configuration newConfig)
    {
        Logger.i(TAG, "onSwitchScreenOrientation() ");

    }

    private void UnregisterDownloadListener()
    {

        mApkDownloadManager.unregisterListener(downloadTaskStateListener);
        // 界面关闭反注册消息通知
        mApkInstalledManager.removeUiRefreshHandler(mUpdateRefreshHandler);

    }

    private void initHeaderAppInfo()
    {
        mAppActiveProgressLoading = (ProgressBar)findViewById(R.id.action_detail_progress_bar_loading);
        mAppActiveProgressResume = (ProgressBar)findViewById(R.id.action_detail_progress_bar_resume);
        mAppDownloadBtn = (DownloadListButton) findViewById(R.id.app_download_btn);//下载按钮
        mAppDownloadBtn.setBackgroundResource(R.drawable.btn_first_framework_selector);
        mAppOpenOrUpdateBtn = (DownloadListButton) findViewById(R.id.app_openorupdate);//打开按钮
        mAppInstalled = (TextView) findViewById(R.id.app_installed);//已安装

        frameLayoutRoundProgressBarlayout = (FrameLayout) findViewById(R.id.RoundProgressBarlayout);

        mAppDownloadBtn.setKownOnClickListener(mKnowBtnOnClickListener);
        mAppDownloadBtn.setWifiLoadOnClickListener(mWifiDownLoadOnClickListener);

        mAppOpenOrUpdateBtn.setKownOnClickListener(mKnowBtnOnClickListener);
        mAppOpenOrUpdateBtn.setWifiLoadOnClickListener(mOpenOrUpdateOnClickListener);

//        mAppDownloadBtn.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                return;
//            }
//        });

    }

    private final DownloadTaskStateListener downloadTaskStateListener = new DownloadTaskStateListener()
    {
        @Override
        public void onUpdateTaskState(DownloadTask task)
        {

            if (null == mAppInfo)
            {
                return;
            }
            //            Logger.i(TAG, "请求更新状态的应用:" + task.appName + "|应用编号:" + task.appId);
            if (task.appId == mAppInfo.appId)
            {
                Logger.i(TAG, "实际更新状态的应用:" + task.appName + "|应用编号:" +
                        task.appId);
                mAppDownloadBtn.setTag(task);
                updateDownLoadBtn(task);
            }
        }

        @Override
        public void onUpdateTaskProgress(DownloadTask task)
        {

            if (null == mAppInfo)
            {
                return;
            }
            //            Logger.e(TAG, "请求更新进度的应用:" + task.appName + "|应用编号:" + task.appId);
            if (task.appId == mAppInfo.appId)
            {
                //                Logger.e(TAG, "实际更新进度的应用:" + task.appName + "|应用编号:" +
                //                        task.appId);
                initProgress(task);
            }

        }

        @Override
        public void onUpdateTaskList(Object obj)
        {
            updateDownLoadBtn(null);
        }
    };

    public void initProgress(DownloadTask task)
    {
        if (task != null)
        {
            long totalSize = task.fileLength;
            long dealtSize = task.progress;
            // LogUtils.e( "task.fileLength=" + task.fileLength );
            // LogUtils.e( "task.progress=" + task.progress );
            int percent = (int) (((double) dealtSize / (double) totalSize) * 100);
            if (percent <= 99 && task.packageName.equals(mAppInfo.packName))
            {
                mAppActiveProgressLoading.setProgress(percent);
                mAppActiveProgressResume.setProgress(percent);
            }
        }

    }

    public void downloadBtnToOpenOrUpdate(String packName)
    {
        // LogUtils.e("应用:" + packName + ",已安装且未在下载队列中。");
        mAppDownloadBtn.setVisibility(View.GONE);
        mAppInstalled.setVisibility(View.GONE);
        mAppInstalled.setText(mRes.getString(R.string.app_installed));

        frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);
        mAppActiveProgressLoading.setVisibility(View.GONE);
        mAppActiveProgressResume.setVisibility(View.GONE);

        mAppOpenOrUpdateBtn.setVisibility(View.VISIBLE);
        if (mApkInstalledManager.isApkNeedToUpdate(packName))
        {
            mAppOpenOrUpdateBtn.setText(mContext.getString(R.string.app_update));
            mAppOpenOrUpdateBtn.setBackgroundResource(R.drawable.btn_first_framework_selector);
            mAppOpenOrUpdateBtn.setTextColor(mRes.getColorStateList(R.color.color_first_white_selector));
            mAppOpenOrUpdateBtn.setTag(mAppInfo);

        }
        else
        {
            mAppOpenOrUpdateBtn.setText(mContext.getString(R.string.app_open));
            mAppOpenOrUpdateBtn.setBackgroundResource(R.drawable.btn_gray_selector);
            mAppOpenOrUpdateBtn.setTextColor(mRes.getColorStateList(R.color.btn_gray_color));

            // LogUtils.e("activity class name = "
            // + mApkInstalledManager.getActivityClassName(packName));
            // LogUtils.e("packname = " + packName);
            mAppOpenOrUpdateBtn.setTag(new String[] {
                    mApkInstalledManager.getActivityClassName(packName), packName
            });
        }
    }

    private void updateDownLoadBtn(DownloadTask dinfo)
    {

        // int padding = dpTopx( 18 );
        // mAppDownloadBtn.setPadding( padding , 0 , padding , 0 );
        //        Logger.i(TAG, "updateDownLoadBtn mAppInfo = " + mAppInfo);
        //        Logger.i(TAG, "DownloadTask  dinfo:" + dinfo);
        if (mAppInfo == null)
        {
            return;
        }
        dinfo = mApkDownloadManager.getDownloadTaskByAppId(mAppInfo.appId);
        final String packageName = mAppInfo.packName;
        if (mApkInstalledManager.isApkLocalInstalled(packageName) && dinfo == null)
        {
            downloadBtnToOpenOrUpdate(packageName);
            return;
        }
        if (dinfo != null)
        {
            initProgress(dinfo);
            mAppDownloadBtn.setTag(dinfo);
            frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);
            mAppActiveProgressLoading.setVisibility(View.GONE);
            mAppActiveProgressResume.setVisibility(View.GONE);
            mAppInstalled.setVisibility(View.GONE);
            mAppDownloadBtn.setVisibility(View.VISIBLE);
            mAppOpenOrUpdateBtn.setVisibility(View.GONE);
            // Log.e(TAG, "updateDownLoadBtn State =  " + dinfo.getState());
            switch (dinfo.getState())
            {
                case PREPARING:// 当前不让用户暂停
                    mAppActiveProgressLoading.setProgress(0);
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.selector_framework_first_gray);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
                    //                    mAppDownloadBtn.setText("");
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_pause));
                    mAppDownloadBtn.setEnabled(true);
                    //                    frameLayoutRoundProgressBarlayout.setVisibility(View.VISIBLE);
                    mAppActiveProgressLoading.setVisibility(View.VISIBLE);
                    mAppActiveProgressResume.setVisibility(View.GONE);
                    break;
                case WAITING:// 当前不让用户暂停
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.selector_framework_first_gray);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
                    mAppDownloadBtn.setEnabled(true);
                    //                    mAppDownloadBtn.setText("");
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_pause));
                    //                    frameLayoutRoundProgressBarlayout.setVisibility(View.VISIBLE);
                    mAppActiveProgressLoading.setVisibility(View.VISIBLE);
                    mAppActiveProgressResume.setVisibility(View.GONE);
                    break;
                case STARTED:// 当前不让用户暂停
                case LOADING:// 当前不让用户暂停
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.selector_framework_first_gray);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
                    mAppDownloadBtn.setEnabled(true);
                    //                    mAppDownloadBtn.setText("");
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_pause));
                    //                    frameLayoutRoundProgressBarlayout.setVisibility(View.VISIBLE);
                    mAppActiveProgressLoading.setVisibility(View.VISIBLE);
                    mAppActiveProgressResume.setVisibility(View.GONE);
                    break;
                case STOPPED:
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.btn_framework_colorresume_selector);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_resume_normal));
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_resume));
                    //                    frameLayoutRoundProgressBarlayout.setVisibility(View.VISIBLE);
                    mAppActiveProgressLoading.setVisibility(View.GONE);
                    mAppActiveProgressResume.setVisibility(View.VISIBLE);
                    break;
                case SUCCEEDED:
                    // TODO
                    /*
                     * mAppActiveProgress.setProgress(100); mAppDownloadBtn
                     * .setBackgroundResource(R.drawable.csls_button_blue_edit);
                     * mAppDownloadBtn.setEnabled(true);
                     * mAppDownloadBtn.setText(mRes
                     * .getString(R.string.app_install));
                     */
                    mAppActiveProgressLoading.setProgress(100);
                    mAppActiveProgressResume.setVisibility(View.GONE);
                    frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);

                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.btn_framework_colorresume_selector);
                    mAppDownloadBtn.setTextColor(mRes
                            .getColor(R.color.color_resume_normal));
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_install));
                    mAppActiveProgressLoading.setVisibility(View.GONE);
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
                    /*
                     * mAppDownloadBtn
                     * .setBackgroundResource(R.drawable.csls_button_green_edit);
                     * mAppDownloadBtn.setEnabled(true); mAppDownloadBtn
                     * .setText(mRes.getString(R.string.app_redownload));
                     */
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.selector_framework_first_gray);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_redownload));
                    frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);
                    mAppActiveProgressLoading.setVisibility(View.GONE);
                    mAppActiveProgressResume.setVisibility(View.GONE);
                    mAppInstalled.setVisibility(View.GONE);
                    mAppDownloadBtn.setVisibility(View.VISIBLE);
                    break;
                case INSTALLING:
                    // TODO
                    frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);
                    mAppActiveProgressLoading.setProgress(100);
                    mAppActiveProgressResume.setVisibility(View.GONE);
                    // mAppInstalled.setVisibility(View.VISIBLE);
                    mAppInstalled.setVisibility(View.VISIBLE);
                    mAppDownloadBtn.setVisibility(View.GONE);
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.selector_framework_first_gray);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
                    mAppDownloadBtn.setEnabled(false);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_install));
                    mAppInstalled.setText(mRes.getString(R.string.app_installing));
                    // mAppInstalled.setText(mRes.getString(R.string.app_installing));
                    break;

                case FAILED_NETWORK: // 网络错误
                    /*
                     * mAppDownloadBtn
                     * .setBackgroundResource(R.drawable.csls_button_green_edit);
                     * mAppDownloadBtn.setEnabled(true);
                     * mAppDownloadBtn.setText(mRes.getString(R.string.app_retry));
                     */
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.selector_framework_first_gray);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_retry));
                    mAppActiveProgressLoading.setVisibility(View.VISIBLE);
                    mAppActiveProgressResume.setVisibility(View.GONE);

                    //                    frameLayoutRoundProgressBarlayout.setVisibility(View.VISIBLE);
                    // frameLayoutRoundProgressBarlayout.setVisibility(View.VISIBLE);
                    // mAppActiveProgress.setVisibility(View.VISIBLE);
                    break;
                case FAILED_BROKEN:// 文件损坏
                    /*
                     * mAppDownloadBtn
                     * .setBackgroundResource(R.drawable.csls_button_green_edit);
                     * mAppDownloadBtn.setEnabled(true); mAppDownloadBtn
                     * .setText(mRes.getString(R.string.app_redownload));
                     */
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.selector_framework_first_gray);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_redownload));
                    frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);

                    break;
                case FAILED_NOEXIST:// 服务器端不存在该文件
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.btn_gray_selector);
                    mAppDownloadBtn.setTextColor(mRes.getColorStateList(R.color.btn_gray_color));
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_delete));
                    frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);

                    break;
                case FAILED_SERVER:// 服务器繁忙

                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.selector_framework_first_gray);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_retry));

                    frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);
                    break;
                case FAILED_NOFREESPACE:
                    mAppDownloadBtn
                            .setBackgroundResource(R.drawable.selector_framework_first_gray);
                    mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
                    mAppDownloadBtn.setEnabled(true);
                    mAppDownloadBtn.setText(mRes.getString(R.string.app_retry));

                    frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);
                    break;
            }
        }
        else
        {
            mAppDownloadBtn.setVisibility(View.VISIBLE);
            mAppDownloadBtn
                    .setBackgroundResource(R.drawable.selector_framework_first_gray);
            mAppDownloadBtn.setText(mRes.getString(R.string.app_download));

            mAppDownloadBtn.setTextColor(mRes.getColor(R.color.color_first_normal));
            mAppDownloadBtn.setEnabled(true);
            mAppDownloadBtn.setVisibility(View.VISIBLE);
            mAppInstalled.setVisibility(View.GONE);
            mAppDownloadBtn.setTag(dinfo);
            frameLayoutRoundProgressBarlayout.setVisibility(View.GONE);
            mAppActiveProgressLoading.setVisibility(View.GONE);
            mAppActiveProgressResume.setVisibility(View.GONE);
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

    private final Handler mUpdateRefreshHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_CONSTANTS.MSG_CHECK_UPDATE:
                {
                    // mAppDownloadBtn.setVisibility( View.GONE );
                    // mAppInstalled.setVisibility( View.VISIBLE );
                    // mAppInstalled.setText( getString( R.string.app_installed ) );
                    break;
                }
                case MSG_CONSTANTS.MSG_CHECK_TOAST:
                {
                    CSToast.show(mContext, (String) msg.obj);
                    break;
                }
            }
        }
    };

    private final Handler mUiHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            /*
             * 目前不需要请求详细信息，只需要去下载队列中查询下载或像下载队列中添加即可，不需要向后台请求数据 case
             * MSG_REQUEST_DETAIL_DATA: reqAppDetailData(); break; case
             * MSG_GET_DETAIL: {
             * 
             * 
             * mAppDownload.setText(mAppInfo.getDownTimes() + ""); //
             * mAppSize.setText( mContext.getString( //
             * R.string.app_info_size_flag , mAppInfo.getPackSize( ) ) );
             * mAppSize.setText(StringUtils.byteToString(mAppInfo
             * .getPackSize())) ; // 设置请求的到值
             * 
             * break; } case MSG_NET_ERROR: break;
             */
                case MSG_REFRESH_APP_DOWNLOAD_BTN:
                    // LogUtils.e("安装成功刷新界面开始");

                    updateDownLoadBtn((DownloadTask) msg.obj);
                    break;

            }
        }
    };

    // 非wifi状态下才会调用此函数
    private final KnowBtnOnClickListener mKnowBtnOnClickListener = new KnowBtnOnClickListener()
    {

        @Override
        public void onKnowClickListener(View v)
        {
            Logger.i("mKnowBtnOnClickListener", "mKnowBtnOnClickListener onKnowClickListener ");
            sendDownloadTaskCountChangeNotify();

            // Log.d(TAG, "mKnowBtnOnClickListener onKnowClickListener ");
            DownloadTask dinfo = (DownloadTask) v.getTag();
            if (dinfo == null)
            {
                //                addTaskToDownLoadList(v);
                //###############oddshou 非 wifi状态下 继续下载
                mApkDownloadManager.startDownload(mAppInfo,
                        mAppPosition != 0 ? mAppPosition
                                : ReportConstants.STAC_APP_POSITION_APP_DETAIL,
                        mAppPosType);
            }
            else
            {
                Logger.i("mKnowBtnOnClickListener", "mKnowBtnOnClickListener onKnowClickListener ");
            }
        }
    };

    // 当前是没有WiFi的情况下调用该函数
    //    private void addTaskToDownLoadList(final View v)
    //    {
    //        mApkDownloadManager.startDownload(mAppInfo, StatisticManager.STAC_APP_POSITION_APP_DETAIL,
    //                mAppPosType);
    //        new Thread()
    //        {
    //            @Override
    //            public void run()
    //            {
    //                try
    //                {
    //                    sleep(500); // 睡眠500ms是等待下载的状态发生改变
    //                } catch (InterruptedException e)
    //                {
    //                    e.printStackTrace();
    //                }
    //                DownloadTask task = (DownloadTask) v.getTag();
    //                mApkDownloadManager.stopDownload(task);
    //                // 非wifi情况下，系统将任务加入下载队列之后，自动暂停下载任务
    //                mStatisticManager.reportDownloadStop(task.appId, task.packId, task.nFromPos,
    //                        StatisticManager.STAC_DOWNLOAD_APK_OTHERS_STOP,
    //                        mRes.getString(R.string.not_wifi_stop));
    //                MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_OTHERS);
    //            };
    //        }.start();
    //    }

    private final WifiDownLoadOnClickListener mOpenOrUpdateOnClickListener = new WifiDownLoadOnClickListener()
    {

        @Override
        public void onWifiClickListener(View v)
        {
            sendDownloadTaskCountChangeNotify();

            // Log.d(TAG, "mOpenOrUpdateOnClickListener onWifiClickListener ");
            Object obj = v.getTag();
            if (obj instanceof String[])
            {
                String[] args = (String[]) obj;
                Intent intent = new Intent("android.intent.action.MAIN");
                LogUtils.e(args[0] + "|" + args[1]);
                intent.setClassName(args[1], args[0]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
            else if (obj instanceof AppInfo)
            {
                Logger.i(TAG, "mAppPosition===" + mAppPosition);
                mApkDownloadManager.startUpgradeDownload(mAppInfo,
                        mAppPosition != 0 ? mAppPosition
                                : ReportConstants.STAC_APP_POSITION_APP_DETAIL);
                //                mApkDownloadManager.startDownload(mAppInfo,
                //                        StatisticManager.STAC_APP_POSITION_APP_DETAIL, mAppPosType, 1);
            }
        }
    };

    private final WifiDownLoadOnClickListener mWifiDownLoadOnClickListener = new WifiDownLoadOnClickListener()
    {

        @Override
        public void onWifiClickListener(View v)
        {

            // Log.d(TAG, "mWifiDownLoadOnClickListener onWifiClickListener ");
            // if( UITools.isFastDoubleClick( ) )
            // {
            // return;
            // }
            // 通知下载个数变化
            sendDownloadTaskCountChangeNotify();
            v.setEnabled(false);

            DownloadTask dinfo = (DownloadTask) v.getTag();
            if (dinfo != null)
            {

                Logger.e(TAG, "dinfo. State = " + dinfo.getState());
                Logger.i(TAG, "onWifiClickListener  stopDownload  dinfo.packId="
                        + dinfo.packId
                        + " mAppPosition=" + mAppPosition, "oddshou");
                if (mAppPosition != 0) {
                    dinfo.setnFromPos(mAppPosition);
                }

                switch (dinfo.getState())
                {
                    case PREPARING:
                    case WAITING:
                    case STARTED:
                    case LOADING:
                        mApkDownloadManager.stopDownload(dinfo);
                        // 用户主动暂停下载任务上报
                        ReportConstants
                                .reportDownloadStop(
                                        dinfo.appId,
                                        dinfo.packId, dinfo.nFromPos,
                                        /*dinfo.nFromPos StatisticManager.STAC_APP_POSITION_APP_DETAIL*/
                                        ReportConstants.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP,
                                        "");

                        // 为了下载量，一旦下载禁止暂停
                        /*
                         * mApkDownloadManager.stopDownload(dinfo); // 用户主动暂停下载任务上报
                         * Logger.i("AppInfoActivity", "dinfo.packId=" +
                         * dinfo.packId); mStatisticManager .reportDownloadStop(
                         * dinfo.appId, dinfo.packId, dinfo.nFromPos,
                         * StatisticManager.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP, "");
                         * MtaUtils.trackDownloadStop(StatisticManager.
                         * STOP_REASON_USER_ACTIVE_STOP);
                         */
                        break;
                    case SUCCEEDED:
                        mApkDownloadManager.installDownload(dinfo);
                        break;
                    /*
                     * case FAILED_NETWORK: // 网络错误
                     * mApkDownloadManager.restartDownload(dinfo);// 重试不行，所以改为重新下载
                     * break;
                     */
                    case STOPPED:// 暂停状态
                    case FAILED_NETWORK: // 网络错误
                    case FAILED_SERVER:// 服务器繁忙
                    case FAILED_NOFREESPACE:
                        mApkDownloadManager.resumeDownload(dinfo);
                        Logger.i(TAG, "onWifiClickListener resumeDownload  dinfo.packId="
                                + dinfo.packId
                                + " mAppPosition=" + mAppPosition, "oddshou");

                        // 暂停下载任务继续下载上报
                        ReportConstants.reportDownloadResume(dinfo.appId, dinfo.packId,
                                dinfo.nFromPos,
                                ReportConstants.STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME, "");
                        MtaUtils.trackDownloadResume();
                        break;
                    case FAILED_BROKEN:// 文件损坏
                    case DELETED:
                        mApkDownloadManager.restartDownload(dinfo);
                        break;
                    case FAILED_NOEXIST:// 服务器端不存在该文件
                        mApkDownloadManager.removeDownload(dinfo);
                        // 取消下载任务
                        ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId,
                                dinfo.nFromPos, ReportConstants.STAC_DOWNLOAD_APK_CANCEL_TASK, "");
                        MtaUtils.trackDownloadCancel(dinfo.appName);
                        break;
                }
            }
            else
            {
                // 详情下载位置上报
                mApkDownloadManager.startDownload(mAppInfo,
                        mAppPosition != 0 ? mAppPosition
                                : ReportConstants.STAC_APP_POSITION_APP_DETAIL,
                        mAppPosType, 1, mGroupElemInfo != null ? mGroupElemInfo.groupId : 0);
                Logger.i(TAG, "onWifiClickListener  mAppPosition=" + mAppPosition, "oddshou");
                mAppActiveProgressLoading.setProgress(0);
                mAppActiveProgressResume.setProgress(0);
            }
        }
    };

    @Override
    public void apkInstallFinish(DownloadTask task)
    {

        if (task == null || mAppInfo == null)
        {
            return;
        }
        if (task.appId == mAppInfo.appId)
        {
            Message msg = Message.obtain();
            msg.what = MSG_REFRESH_APP_DOWNLOAD_BTN;
            msg.obj = task;
            mUiHandler.sendMessage(msg);
        }
    }
}
