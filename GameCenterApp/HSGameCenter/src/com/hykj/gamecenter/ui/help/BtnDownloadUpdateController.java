
package com.hykj.gamecenter.ui.help;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.download.DownloadTask.TaskState;
import com.hykj.gamecenter.download.DownloadTaskStateListener;
import com.hykj.gamecenter.logic.ApkInstalledManager;
import com.hykj.gamecenter.logic.ApkInstalledManager.InstallFinishListener;
import com.hykj.gamecenter.net.APNUtil;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog;
import com.hykj.gamecenter.ui.InWifiDownLoadDialog.KnowBtnOnClickListener;
import com.hykj.gamecenter.ui.NoWifiContinueDownloadDialog;
import com.hykj.gamecenter.ui.widget.BtnDownloadUpdate;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.NoWifiContinueDownloadListener;
import com.hykj.gamecenter.ui.widget.OnWifiClickListener.WifiDownLoadOnClickListener;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utilscs.LogUtils;

/**
 * 接管 {@link BtnDownloadUpdate} 的所有处理,将所有需要的显示的结果返回给 它
 * 
 * @author oddshou
 */
public class BtnDownloadUpdateController implements InstallFinishListener {

    private static final String TAG = "BtnDownloadUpdateController";
    private Context mContext;
    /**
     * 当前controller 绑定的View
     */
    private View mAttachView;
    private ApkDownloadManager mApkDownloadManager = null;
    private ApkInstalledManager mApkInstalledManager;
    private AppInfo mAppInfo;
    private int mAppPosType = 0;
    //关于统计位置
    private int mAppPosition;

    public BtnDownloadUpdateController(Context context, View btn) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mAttachView = btn;
        mApkDownloadManager = DownloadService.getDownloadManager();
    }

    public void bindData(AppInfo appInfo, int appPositionType) {
        // TODO Auto-generated method stub
        mAppInfo = appInfo;
        mAppPosition = ReportConstants.reportPos(appPositionType);
        mApkDownloadManager.registerListener(downloadTaskStateListener); // 下载消息通知注册
        mApkInstalledManager = ApkInstalledManager.getInstance();
        mApkInstalledManager.addInstallListener(this);
        updateDownLoadBtn(null);
    }
    
    private void updateDownLoadBtn(DownloadTask dinfo) {
        LogUtils.e("updateDownLoadBtn");

        if (mAppInfo == null) {
            return;
        }
        dinfo = mApkDownloadManager.getDownloadTaskByAppId(mAppInfo.appId);
        final String packName = mAppInfo.packName;
        BtnState btnState = new BtnState();
        if (mApkInstalledManager.isApkLocalInstalled(packName)
                && dinfo == null) {
            LogUtils.e("应用:" + packName + ",已安装且未在下载队列中。");
            if (mApkInstalledManager.isApkNeedToUpdate(packName)) {
                //需要更新
                mAttachView.setTag(mAppInfo);
                btnState.updateState = UpdateState.NEEDUPDATE;
            } else {
                //打开
                btnState.updateState = UpdateState.OPEN;
                mAttachView.setTag(new String[] {mApkInstalledManager.getActivityClassName(packName), packName});
            }
            ((BtnDownloadUpdate)mAttachView).updateDownLoadBtn(btnState , null);
            return;
        }
        mAttachView.setTag(dinfo);
        if (dinfo != null) {
            //下载过程中的状态更新
            //按钮的状态交给自己处理
            btnState.downloadState = dinfo.getState();
            btnState.updateState = UpdateState.NULL;
        } else {
            //普通状态显示下载
            btnState.updateState = UpdateState.NORMAL;
        }
        ((BtnDownloadUpdate)mAttachView).updateDownLoadBtn(btnState , dinfo);
    }

    private final DownloadTaskStateListener downloadTaskStateListener = new DownloadTaskStateListener() {
        @Override
        public void onUpdateTaskState(DownloadTask task) {

            LogUtils.e("请求更新状态的应用:" + task.appName + "|应用编号:" + task.appId);
            if (task.appId == mAppInfo.appId) {
                LogUtils.e("实际更新状态的应用:" + task.appName + "|应用编号:" + task.appId);
                mAttachView.setTag(task);
                //########oddshou
                updateDownLoadBtn(task);
            }
        }

        @Override
        public void onUpdateTaskProgress(DownloadTask task) {
            LogUtils.e("请求更新进度的应用:" + task.appName + "|应用编号:" + task.appId);
            if (task.appId == mAppInfo.appId) {
                LogUtils.e("实际更新进度的应用:" + task.appName + "|应用编号:" + task.appId);
                //##########oddshou
//                initProgress(task);
            }

        }

        @Override
        public void onUpdateTaskList(Object obj) {
            //############oddhsou
            updateDownLoadBtn(null);
        }
    };

    private final KnowBtnOnClickListener kListener = new KnowBtnOnClickListener() {

        @Override
        public void onKnowClickListener(View v) {
            DownloadTask dinfo = (DownloadTask) v.getTag();
            if (dinfo == null) {
                // addTaskToDownLoadList(v);
                // ###############oddshou 非 wifi状态下 继续下载
                mApkDownloadManager.startDownload(mAppInfo,
                        mAppPosition, mAppPosType);
            } else {
                return;
            }
        }
    };

    private final WifiDownLoadOnClickListener mWifiDownLoadOnClickListener = new WifiDownLoadOnClickListener() {

        @Override
        public void onWifiClickListener(View v) {
            // if( UITools.isFastDoubleClick( ) )
            // {
            // return;
            // }
            Logger.i(TAG, "mWifiDownLoadOnClickListener onWifiClickListener", "oddshou");
            Object obj = v.getTag();
            if (obj instanceof String[]) {
                String[] args = (String[]) obj;
                Intent intent = new Intent("android.intent.action.MAIN");
                LogUtils.e(args[0] + "|" + args[1]);
                intent.setClassName(args[1], args[0]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                return;
            } else if (obj instanceof AppInfo) {
                mApkDownloadManager.startDownload(mAppInfo,
                        mAppPosition,
                        mAppPosType);
                return;
            }
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
                                        /* dinfo.nFromPos */mAppPosition,
                                        ReportConstants.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP,
                                        "");
                        // MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_USER_ACTIVE_STOP);
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
                                        /* dinfo.nFromPos */mAppPosition,
                                        ReportConstants.STAC_DOWNLOAD_APK_STOP_BREAKPOINT_RESUME,
                                        "");
                        // MtaUtils.trackDownloadResume();
                        break;
                    case FAILED_BROKEN:
                    case DELETED:
                        mApkDownloadManager.restartDownload(dinfo);
                        break;
                    case FAILED_NOEXIST:
                        mApkDownloadManager.removeDownload(dinfo);
                        // 取消下载任务
                        ReportConstants.reportDownloadStop(dinfo.appId,
                                dinfo.packId, /* dinfo.nFromPos */
                                mAppPosition,
                                ReportConstants.STAC_DOWNLOAD_APK_CANCEL_TASK, "");
                        // MtaUtils.trackDownloadCancel(dinfo.appName);
                        break;
                }
            } else {
                // 详情下载位置上报
                Logger.i(TAG, "mAppPosType mAppPosType mAppPosType = " + mAppPosType);
                mApkDownloadManager.startDownload(mAppInfo,
                        mAppPosition,
                        mAppPosType);
                //##########oddshou
//                mAppActiveProgress.setProgress(0);
            }
        }
    };

    public void onBtnClick(View v) {

            boolean bWifiToDownload = App.getSettingContent().getSettingData().bWifiToDownload;
            Logger.i(TAG, "DownloadListButton = " + bWifiToDownload, "oddshou");

            DownloadTask task = null;
            if (mAttachView.getTag() instanceof DownloadTask) {
                task = (DownloadTask) mAttachView.getTag();
            }
            if (bWifiToDownload) {
                if (APNUtil.isMobileDataEnable(mContext)) {
                    if (null != task) {
                        Logger.e(TAG, "DownloadListButton task.getState =  " + task.getState(),
                                "oddshou");
                        if (task.getState().equals(TaskState.SUCCEEDED)) {
                            mWifiDownLoadOnClickListener.onWifiClickListener(mAttachView);
                        }
                        else if (task.getState().equals(TaskState.STOPPED)
                                || task.getState().equals(TaskState.FAILED_SERVER)) {
                            showContinueDialog(task);
                        }
                        else if (task.getState().equals(TaskState.FAILED_NETWORK)) {
                            showContinueDialog(task);
                        }
                        else if (task.getState().equals(TaskState.LOADING)
                                || task.getState().equals(TaskState.STARTED)) {
                            mWifiDownLoadOnClickListener.onWifiClickListener(mAttachView);
                        }
                        return;
                    }
                    showDialog();
                }
                else {
                    mWifiDownLoadOnClickListener.onWifiClickListener(mAttachView);
                }
            }
            else {
                mWifiDownLoadOnClickListener.onWifiClickListener(mAttachView);
            }
    }

    private void showContinueDialog(final DownloadTask dinfo) {
        NoWifiContinueDownloadDialog dialog = new NoWifiContinueDownloadDialog(mContext,
                R.style.MyDialog);
        dialog.setView(mAttachView);
        dialog.setNoWifiContinueDownloadListener(new NoWifiContinueDownloadListener() {

            @Override
            public void NoWifiContinueListener(View v) {
                mApkDownloadManager.resumeDownload(dinfo);
            }
        });
        dialog.show();
    }

    private void showDialog() {
        InWifiDownLoadDialog dialog = new InWifiDownLoadDialog(mContext, R.style.MyDialog);
        dialog.setView(mAttachView);
        dialog.setKnowBtnOnClickListener(kListener);
        dialog.show();
    }

    /**
     * view 的 onDetachedFromWindow 方法被调用，适当做一些销毁
     */
    public void remove() {
        // TODO Auto-generated method stub
        mApkDownloadManager.unregisterListener(downloadTaskStateListener);// 界面关闭反注册消息通知
    }
    
    public enum UpdateState{
        NORMAL,NEEDUPDATE,OPEN,NULL
    }
    
    public class BtnState{
        public BtnState() {
            // TODO Auto-generated constructor stub
        }
        public BtnState(DownloadTask.TaskState downloadState, UpdateState updateState) {
            // TODO Auto-generated constructor stub
            this.downloadState = downloadState;
            this.updateState = updateState;
        }
        /**
         * 下载中得状态
         */
        public DownloadTask.TaskState downloadState;
        /**
         * 普通，需要更新，打开
         */
        public UpdateState updateState;
    }
    
    public static final int MSG_UPDATE_BTN = 0X01;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_BTN:
                    updateDownLoadBtn((DownloadTask)msg.obj);
                    break;

                default:
                    break;
            }
            
            
        }
    };

    @Override
    public void apkInstallFinish(DownloadTask info) {
        // TODO Auto-generated method stub
//        updateDownLoadBtn(info);
        Message msg = mHandler.obtainMessage(MSG_UPDATE_BTN, info);
        mHandler.sendMessage(msg);
    }

}
