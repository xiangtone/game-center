package com.hykj.gamecenter.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class ThreadTask
{
    private static Looper mLooper = null;

    public static Looper getMLooper()
    {
	if( mLooper == null )
	{
	    HandlerThread mHandlerThread = new HandlerThread( "hsgamecenterThreadtask" );
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
	    tHandler.post( runnable );
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
