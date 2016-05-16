package com.hykj.gamecenter.logic.entry;

public class DownloadInfo
{

    // 临时文件下载路径
    public String tempPath;

    public String url;
    public String packageName;
    public String appName;
    public int appId;
    public int rating;
    public int totalSize;
    public int dlSize;
    public String boxPicUrl;
    public int packId;
    // 当前的下载状态̬
    public int state = 0;

    //    public static final int STATE_WAITTING = 0; // 等待中，刚点击下载或者更新，还未开始下载的状态  //下载
    //    public static final int STATE_DOWNLOADING = 1; // 下载中 //下载中
    //    public static final int STATE_PAUSE = 2; // 暂停 //继续
    //    public static final int STATE_COMPLE = 3; // 下载完成  //安装
    //    public static final int STATE_DOWNLOAD_FAILED = 4; // 下载失败 //重试
    //    public static final int STATE_DELETED = 5; // 下载文件已删除   //TODO 测试是否有这个状态的存在
    //    public static final int STATE_INSTALLING = 6; // 正在安装  //安装中
    //    public static final int STATE_INSTALLED = 7;//已安装
    //    public static final int STATE_APK_BROKE = 8; //安装包损坏

    public DownloadInfo()
    {}

    @Override
    public String toString()
    {
	return "DownloadInfo [tempPath=" + tempPath + ", url=" + url + ", packageName=" + packageName + ", appName=" + appName + ", appId=" + appId + ", totalSize=" + totalSize + ", dlSize=" + dlSize
		+ ", state=" + state + "]";
    }

    @Override
    public boolean equals( Object other )
    {
	if( other instanceof DownloadInfo )
	{
	    DownloadInfo otherinfo = (DownloadInfo)other;
	    if( otherinfo.appId == appId )
	    {
		return true;
	    }
	}
	return false;
    }
}
