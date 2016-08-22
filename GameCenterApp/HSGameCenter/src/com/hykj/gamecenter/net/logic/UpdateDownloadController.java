
package com.hykj.gamecenter.net.logic;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.logic.NotificationCenter;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.protocol.Reported.ReportedInfo;
import com.hykj.gamecenter.protocol.Updater.RspUpdate;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.utils.FileUtils;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.PackageUtils;
import com.hykj.gamecenter.utils.UpdateUtils;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * 自更新下载，和ApkDownloadManager中的应用下载逻辑不太一样，单独拿出来，以方便后续可能的需求
 */
public class UpdateDownloadController {

    private static UpdateDownloadController mInstance = null;
    private final NotificationCenter mNotificationCenter;
    private Handler mHandler;

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    private UpdateDownloadController() {
        mNotificationCenter = NotificationCenter.getInstance();
    }

    public static UpdateDownloadController getInstance() {
        if (mInstance == null) {
            mInstance = new UpdateDownloadController();
        }

        return mInstance;
    }

    public static final int MSG_UPDATE_PROGRESS = 40008;
    public static final int MSG_SET_PATH = 40009;
    public static final int MSG_DOWNLOAD_FAIL = 40010;

    public void startDonwloadApk(final RspUpdate updateInfo) {
        String filePath = createFile();
        Logger.i("UpdateDownloadController", "url =" + updateInfo.packUrl);
        Logger.i("UpdateDownloadController", "filePath =" + filePath);

        PowerManager pm = (PowerManager) App.getAppContext()
                .getSystemService(Context.POWER_SERVICE);

        RequestParams params = new RequestParams(updateInfo.packUrl);
        Executor executor = new PriorityExecutor(1, true);
        params.setAutoResume(true);
        params.setAutoRename(false);
        params.setSaveFilePath(filePath);
        params.setExecutor(executor);
        DownloadCallBack callBack = new DownloadCallBack(updateInfo);
        x.http().get(params, callBack);

        addDownloadNotification();

//        ReportInfo info = new ReportInfo(ReportLuachInfo.NEW_VERSION_DOWNLOAD, 0,
//                updateInfo.packName,
//                updateInfo.newVerName, updateInfo.newVerCode + "");
//        App.reportInfo(info, null, null);
        ReportedInfo builder = new ReportedInfo();
        builder.statActId = ReportConstants.STATACT_ID_UPDATE_START;
        builder.ext1 = updateInfo.packName;
        builder.ext2 = updateInfo.newVerName;
        builder.ext3 = updateInfo.newVerCode+ "";
        ReportConstants.getInstance().reportReportedInfo(builder);
        MtaUtils.trackUpdateRequest(updateInfo.newVerName, "" + updateInfo.newVerCode);
    }

    // 文件存储相关
    private String createFile() {
        String path = getApkDownloadPath() + "HSGameCenter.apk";
        File file = new File(path);
        // 删除已存在的同名文件
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    private static String getApkDownloadPath() {
        String path = FileUtils.getStorePath(FileUtils.UPDATE_PATH);

        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return path;
    }

    // 通知栏相关
    private void addDownloadNotification() {
        mNotificationCenter.addSelfDownloadStartNotification();
    }

    private void finishDownloadNotification(File file) {
        mNotificationCenter.addSelfDownloadFinishNotification(file);
    }

    private void updateDownloadNotification(int dlsize, int totalsize) {
        mNotificationCenter.addSelfDownloadProgressNotification(dlsize, totalsize);
    }

    private void updateDownloadNotificationError() {
        mNotificationCenter.addSelfDownloadErrorNotification();
    }

    private class DownloadCallBack implements Callback.CommonCallback<File>,
            Callback.ProgressCallback<File>,
            Callback.Cancelable {

        private final RspUpdate updateInfo;

        public DownloadCallBack(RspUpdate updateInfo) {
            this.updateInfo = updateInfo;
        }

        @Override
        public void cancel() {

        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            updateDownloadNotification((int) current, (int) total);

            Logger.i("UpdateDownloadController", "---onLoading");
            Message msg = mHandler.obtainMessage();
            msg.what = MSG_UPDATE_PROGRESS;
            msg.getData().putLong(HomePageActivity.UPDATE_CURRENT, current);
            msg.getData().putLong(HomePageActivity.UPDATE_ALL, total);
            msg.sendToTarget();
        }

        @Override
        public void onSuccess(File file) {
            Logger.i("UpdateDownloadController", "responseInfo=" + file.toString());
            finishDownloadNotification(file);
            Logger.i("UpdateDownloadController", "responseInfo=" + file.toString());
            Logger.i("UpdateDownloadController",
                    "responseInfo's path =" + file.getPath());
            PackageUtils.installNormal(App.getAppContext(), file.getPath());
            Message msg = mHandler.obtainMessage();
            msg.what = MSG_SET_PATH;
            msg.getData().putString(HomePageActivity.APP_PATH, file.getPath());
            msg.sendToTarget();

            Logger.d("UpdateDownloadController", "mHandler  :  " + mHandler);
            if (mHandler != null) {
                //		    mHandler.sendEmptyMessage( MyFragment.MSG_TO_DOWNLOAD_PSF_SUCCESSED );
            }
            //统计,上报启动升级
//                ReportInfo info = new ReportInfo(ReportLuachInfo.NEW_VERSION_DOWNLOAD_SUCCESS, 0,
//                        updateInfo
//                        .packName, updateInfo.newVerName, updateInfo.newVerCode + "");
//                App.reportInfo(info, null, null);
            ReportedInfo builder = new ReportedInfo();
            builder.statActId = ReportConstants.STATACT_ID_UPDATE_SUCCEED;
            builder.ext1 = updateInfo.packName;
            builder.ext2 = updateInfo.newVerName;
            builder.ext3 = updateInfo.newVerCode+ "";
            ReportConstants.getInstance().reportReportedInfo(builder);


            //		UpdateUtils.setUpdatePreference( false , 0 );
            UpdateUtils.saveUpdateState(false);
            MtaUtils.trackUpdateDownloaded(updateInfo.newVerName, "" + updateInfo.newVerCode);
        }

        @Override
        public void onError(Throwable throwable, boolean b) {
            if (throwable instanceof HttpException)
            Logger.i("UpdateDownloadController", "msg = " + throwable.getMessage());

            updateDownloadNotificationError();
            //		UpdateUtils.setUpdatePreference( true , 3 );
            UpdateUtils.saveUpdateState(false);
            Message message = mHandler.obtainMessage();
            message.what = MSG_DOWNLOAD_FAIL;
            message.sendToTarget();
        }

        @Override
        public void onCancelled(CancelledException e) {
            Logger.i("UpdateDownloadController", "---onStopped");
            UpdateUtils.saveUpdateState(false);
        }

        @Override
        public void onFinished() {

        }
    }
}
