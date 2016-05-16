package com.hykj.gamecenter.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import android.text.TextUtils;
import com.hykj.gamecenter.utilscs.LogUtils;

import com.hykj.gamecenter.download.DownloadTask;
import com.hykj.gamecenter.logic.ApkInstalledManager;

/**
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流
 * 
 * @author froyohuang
 * 
 */
public class RuntimeStream extends Thread
{
    InputStream is;
    String type;
    OutputStream os;
    DownloadTask dinfo;

    RuntimeStream( InputStream is , String type , DownloadTask dinfo )
    {
	this( is , type , null , dinfo );
    }

    RuntimeStream( InputStream is , String type , OutputStream redirect , DownloadTask dinfo )
    {
	this.is = is;
	this.type = type;
	this.dinfo = dinfo;
	os = redirect;
    }

    @Override
    public void run()
    {
	BufferedReader br = null;
	PrintWriter pw = null;
	StringBuilder msgBuilder = new StringBuilder( );
	String msg;
	try
	{
	    if( os != null )
		pw = new PrintWriter( os );

	    br = new BufferedReader( new InputStreamReader( is ) );
	    String line = null;
	    while ( ( line = br.readLine( ) ) != null )
	    {
		msgBuilder.append( line );
		if( pw != null )
		    pw.println( line );
	    }
	    msg = msgBuilder.toString( );
	    if( TextUtils.isEmpty( msg ) )
	    {
		msg = "other";
	    }
	    LogUtils.e( "RuntimeStream ,type >" + msg );
	    if( type.equals( "ERROR" ) )
	    {
		LogUtils.e( "静默安装，putInstallErrorMsg" );
		ApkInstalledManager.getInstance( ).putInstallErrorMsg( dinfo , msg );
		LogUtils.e( "putInstallErrorMsg  = " + msg + "dinfo = " + dinfo.toString( ) );
	    }
	    else
	    {
		LogUtils.e( "静默安装，putInstallSuccessMsg" );
		ApkInstalledManager.getInstance( ).putInstallSuccessMsg( dinfo , msg );
		LogUtils.e( "putInstallSuccessMsg  = " + msg + "dinfo = " + dinfo.toString( ) );
	    }

	    if( pw != null )
	    {
		pw.flush( );
		pw.close( );
	    }

	    br.close( );
	}
	catch ( IOException ioe )
	{
	    ioe.printStackTrace( );
	}
    }
}
