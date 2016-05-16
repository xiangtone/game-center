/**
 * 
 */
package com.hykj.gamecenter.download;

/**
 * @author springsu
 *
 */
public interface IDownloadTaskStateListener
{
    /**
     * 
     * 2013 2013-11-30 下午4:11:08
     * springsu
     * @param task
     * DownloadTaskStateListener
     * void
     *  需要收到task通知的地方要实现
     */
    void onUpdateTaskProgress(DownloadTask task);

    /**
     * 
     * 2013 2013-11-30 下午4:14:11
     * springsu
     * @param task
     * DownloadTaskStateListener
     * void
     *需要收到task通知的地方要实现
     */

    void onUpdateTaskState(DownloadTask task);

    /**
     * 用于下载管理列表，当task项有变化时，如删除或批量删除时，需要刷新list
     * 2013 2013-11-30 下午4:27:34
     * Administrator
     * DownloadTaskStateListener
     * void
     * 
     *  modify at 20131202
     * add @param obj
     */
    void onUpdateTaskList(Object obj);

}
