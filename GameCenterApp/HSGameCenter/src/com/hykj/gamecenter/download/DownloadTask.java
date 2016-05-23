
package com.hykj.gamecenter.download;

public class DownloadTask
{

    private static final String TAG = DownloadTask.class.getName();

    public DownloadTask()
    {
        bRealAppDownloadURL = false;
    }

    //    public HttpHandler<File> handler;
//    public Callback.Cancelable cancelable;  //xutils3.0 修改
    public ApkDownloadManager.DownloadCallBack callBack;


    public ApkDownloadManager.TaskCallback taskCallback;
    public String appDownloadURL;
    public String fileSavePath;
    public long progress; // 已下载大小
    public long fileLength; // 文件总大小 progress/fileLength 为进度
    public long avgSpeed;
    public long remainSeconds;
    public String packageName;
    public String appName;
    public int appId;
    public String appIconURL;
    public int packId;
    public String packMD5;
    // 当前的下载状态̬
    private TaskState state;

    // 从什么入口启动的下载，用于统计
    public int nFromPos = -1;
    //统计 groupId
    public int groupId = 0;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean bRealAppDownloadURL = false; // 是否是真实下载地址(有可能是需要重定向的下载地址)
    // for debug
    public String stateMsg;

    public TaskState getState()
    {
        return state;
    }

    public enum TaskState
    {
        PREPARING(0), WAITING(1), STARTED(2), LOADING(3), STOPPED(4), SUCCEEDED(5), INSTALLING(6), DELETED(
                7), FAILED_BROKEN(-1), FAILED_NOEXIST(-2), FAILED_NETWORK(-3), FAILED_SERVER(-4), FAILED_NOFREESPACE(
                -5);
        // LOADING下载中 STOPPED 停止下载 SUCCEEDED 安装成功
        private int value = 0;

        TaskState(int value)
        {
            this.value = value;
        }

        public static TaskState valueOf(int value)
        {
            switch (value)
            {
                case 0:
                    return PREPARING;
                case 1:
                    return WAITING;
                case 2:
                    return STARTED;
                case 3:
                    return LOADING;
                case 4:
                    return STOPPED;
                case 5:
                    return SUCCEEDED;
                case 6:
                    return INSTALLING;
                case 7:
                    return DELETED;

                case -1:
                    return FAILED_BROKEN;
                case -2:
                    return FAILED_NOEXIST;
                case -3:
                    return FAILED_NETWORK;
                case -4:
                    return FAILED_SERVER;
                case -5:
                    return FAILED_NOFREESPACE;
                default:
                    return null;
            }
        }

        public int value()
        {
            return this.value;
        }

        @Override
        public String toString()
        {
            switch (value)
            {
                case 0:
                    return "PREPARING";
                case 1:
                    return "WAITING";
                case 2:
                    return "STARTED";
                case 3:
                    return "LOADING";
                case 4:
                    return "STOPPED";
                case 5:
                    return "SUCCEEDED";
                case 6:
                    return "INSTALLING";
                case 7:
                    return "DELETEED";

                case -1:
                    return "FAILED_BROKEN";
                case -2:
                    return "FAILED_NOEXIST";
                case -3:
                    return "FAILED_NETWORK";
                case -4:
                    return "FAILED_SERVER";
                case -5:
                    return "FAILED_NOFREESPACE";
                default:
                    return "state_none";
            }
        }
    }

    @Override
    public String toString()
    {
        return "DownloadInfo [packMD5=" + packMD5 + ",fileSavePath=" + fileSavePath + ", url="
                + appDownloadURL + ", packageName=" + packageName + ", appName=" + appName
                + ", appId=" + appId + ", totalSize=" + fileLength + ", dlSize="
                + progress +", nfrom=" + nFromPos + ", state=" + state.toString() + "]";
    }

    public void reset()
    {
        progress = 0;
        // fileLength = 0;
        state = TaskState.PREPARING;
//        handler = null;
        callBack = null;
        fileSavePath = null;
        bRealAppDownloadURL = false;
    }

    public void resetForDeleted()
    {
        progress = 0;
        state = TaskState.DELETED;
//        handler = null;
        callBack = null;
        fileSavePath = null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof DownloadTask) {
            DownloadTask otherinfo = (DownloadTask) other;
            if (otherinfo.appId == appId) {
                return true;
            }
        }
        return false;
    }
//
//    public HttpHandler<File> getHandler() {
//        return handler;
//    }
//
//    public void setHandler(HttpHandler<File> handler) {
//        this.handler = handler;
//    }

    public ApkDownloadManager.DownloadCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(ApkDownloadManager.DownloadCallBack callBack) {
        this.callBack = callBack;
    }

    public ApkDownloadManager.TaskCallback getTaskCallback() {
        return taskCallback;
    }

    public void setTaskCallback(ApkDownloadManager.TaskCallback taskCallback) {
        this.taskCallback = taskCallback;
    }


    public String getAppDownloadURL() {
        return appDownloadURL;
    }

    public void setAppDownloadURL(String appDownloadURL) {
        this.appDownloadURL = appDownloadURL;
    }

    public long getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(long avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public long getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(long remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppIconURL() {
        return appIconURL;
    }

    public void setAppIconURL(String appIconURL) {
        this.appIconURL = appIconURL;
    }

    public int getPackId() {
        return packId;
    }

    public void setPackId(int packId) {
        this.packId = packId;
    }

    public String getPackMD5() {
        return packMD5;
    }

    public void setPackMD5(String packMD5) {
        this.packMD5 = packMD5;
    }

    public int getnFromPos() {
        return nFromPos;
    }

    public void setnFromPos(int nFromPos) {
        this.nFromPos = nFromPos;
    }

    public boolean isbRealAppDownloadURL() {
        return bRealAppDownloadURL;
    }

    public void setbRealAppDownloadURL(boolean bRealAppDownloadURL) {
        this.bRealAppDownloadURL = bRealAppDownloadURL;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }

//    public HttpHandler<File> getHttpHandler()
//    {
//        return handler;
//    }
//
//    public void setHttpHandler(HttpHandler<File> handler)
//    {
//        this.handler = handler;
//    }

    public void setState(TaskState state)
    {
        this.state = state;
    }

    public String getDownloadUrl()
    {
        return appDownloadURL;
    }

    public void setDownloadUrl(String downloadUrl)
    {
        this.appDownloadURL = downloadUrl;
    }

    public void setIsRealAppDownloadURL(boolean bRealDownloadUrl)
    {
        bRealAppDownloadURL = bRealDownloadUrl;
    }

    public void resetRealAppDownloadURL()
    {
        bRealAppDownloadURL = false;
    }

    public boolean getIsRealAppDownloadURL()
    {
        return bRealAppDownloadURL;
    }

    public String getFileSavePath()
    {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath)
    {
        this.fileSavePath = fileSavePath;
    }

    public long getProgress()
    {
        return progress;
    }

    public void setProgress(long progress)
    {
        this.progress = progress;
    }

    public long getFileLength()
    {
        return fileLength;
    }

    public void setFileLength(long fileLength)
    {
        this.fileLength = fileLength;
    }

    // 正在下载或者等待状态的任务的总进度
    public int getDownloadingProgress()
    {

        double downloadedSize = 0, fileSize = 0;
        if (getState() == TaskState.LOADING || getState() == TaskState.PREPARING
                || getState() == TaskState.WAITING || getState() == TaskState.STARTED)
        {
            downloadedSize = progress;
            fileSize += fileLength;
            /*
             * Log.d(TAG, "info.progress = " + progress + " info.fileLength = "
             * + fileLength);
             */
        }

        if (fileSize != 0)
        {
            int percent = (int) ((downloadedSize / fileSize) * 100);
            if (percent >= 100)
            {
                percent = 100;
            }
            return percent;
        }
        return 0;
    }
    //
    //    @Override
    //    protected Object clone() {
    //        // TODO Auto-generated method stub
    //        DownloadTask clone = null;
    //        try {
    //            clone = (DownloadTask) super.clone();
    //        } catch (CloneNotSupportedException e) {
    //            throw new RuntimeException(e); // won't happen 
    //        }
    //        return clone;
    //    }
}
