/**
 * 
 */
package com.hykj.gamecenter.download;

import android.os.Handler;
import android.os.Message;

import com.hykj.gamecenter.statistic.MSG_CONSTANTS;

/**
 * @author springsu
 *
 */
public abstract class DownloadTaskStateListener extends Handler implements IDownloadTaskStateListener
{

    @Override
    public void handleMessage( Message msg )
    {
	switch ( msg.what )
	{
	    case MSG_CONSTANTS.MSG_UPDATE_PROGRESS :
	    {
		DownloadTask task = (DownloadTask)msg.obj;
		onUpdateTaskProgress( task );
		break;
	    }
	    case MSG_CONSTANTS.MSG_DOWNLOAD_STATE_CHANGE :
	    {
		DownloadTask task = (DownloadTask)msg.obj;
		onUpdateTaskState( task );
		break;
	    }
	    case MSG_CONSTANTS.MSG_REFRESH_DOWNLOAD :
	    {
		onUpdateTaskList( null );
		break;
	    }
	    //批量更新
	    //
	    case MSG_CONSTANTS.MSG_BATCH_REFRESH_DOWNLOAD :
	    {
		onUpdateTaskList( msg.obj );
		break;
	    }
	}
    }

    public void sendStateChangeMsg( DownloadTask task )
    {
	if( task == null )
	    return;

	Message msg = Message.obtain( );
	msg.what = MSG_CONSTANTS.MSG_DOWNLOAD_STATE_CHANGE;
	msg.obj = task;

	sendMessage( msg );
    }

    public void sendProgressChangeMsg( DownloadTask task )
    {
	if( task == null )
	    return;

	Message msg = Message.obtain( );
	msg.what = MSG_CONSTANTS.MSG_UPDATE_PROGRESS;
	msg.obj = task;

	sendMessage( msg );
    }

    /**
     * 
     * 2013 2013-11-30 下午4:23:14
     * springsu
     * DownloadTaskStateListener
     * void
     * 用于下载列表等UI，当删除下载项时，需要刷新列表项时，dowloadManager会发起这个消息
     */
    public void sendRefreshMsg()
    {
	Message msg = Message.obtain( );
	msg.what = MSG_CONSTANTS.MSG_REFRESH_DOWNLOAD;

	sendMessage( msg );
    }

    /**
     * 
     * 2013 2013-11-30 下午4:23:14
     * 
     * DownloadTaskStateListener
     * void
     * 用于下载列表等UI，当批量删除下载项时，需要刷新列表项时，dowloadManager会发起这个消息
     */
    public void sendBatchRefreshMsg( Object obj )
    {
	Message msg = Message.obtain( );
	msg.what = MSG_CONSTANTS.MSG_BATCH_REFRESH_DOWNLOAD;
	msg.obj = obj;
	sendMessage( msg );
    }
}
