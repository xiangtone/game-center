/**
 * RefreshUnreadNumService.java
 *
 * classes : com.niuwan.gamecenter.broadcast.RefreshUnreadNumService
 *
 * Create by chenming at 2014-3-18 下午7:32:14
 *
 */
package com.hykj.gamecenter.broadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hykj.gamecenter.logic.BroadcastManager;

/**
 * @author chenming 
 *
 * com.niuwan.gamecenter.broadcast.RefreshUnreadNumService.java
 *
 * Create at 2014-3-18 下午7:32:14 by chenming
 *
 * 类描述:
 */
public class RefreshUnreadNumService extends Service
{
    private static final String ACTION = "com.cs.launcher.SEND_UNREAD_REQUEST";

    /* (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind( Intent intent )
    {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate()
    {
	// TODO Auto-generated method stub
	super.onCreate( );
    }

    /* (non-Javadoc)
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand( Intent intent , int flags , int startId )
    {
	// TODO Auto-generated method stub
	BroadcastManager.getInstance( ).sendBroadCastToDesk( true );
	return super.onStartCommand( intent , flags , startId );
    }

}
