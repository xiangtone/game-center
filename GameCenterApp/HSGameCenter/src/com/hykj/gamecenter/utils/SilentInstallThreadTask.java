package com.hykj.gamecenter.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class SilentInstallThreadTask
{
    private static Looper mLooper = null;

    public static Looper getMLooper()
    {
	if( mLooper == null )
	{
	    HandlerThread mHandlerThread = new HandlerThread( "silentInstallThreadTask" );
	    mHandlerThread.start( );
	    mLooper = mHandlerThread.getLooper( );
	}
	return mLooper;
    }

    public static void stop()
    {
	if( mLooper != null )
	{
	    mLooper.quit( );
	}
    }

    public static void postTask( Runnable runnable )
    {
	if( runnable != null )
	{
	    tHandler.postDelayed( runnable , 10 );
	}
    }

    public static void postTaskAtFront( Runnable runnable )
    {
	if( runnable != null )
	{
	    tHandler.postAtFrontOfQueue( runnable );
	}
    }

    private static Handler tHandler = new Handler( getMLooper( ) );
}
