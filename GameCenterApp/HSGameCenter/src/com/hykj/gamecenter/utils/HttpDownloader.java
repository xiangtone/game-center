package com.hykj.gamecenter.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader
{
    public interface ProgressChangedCallBack
    {
	void loadFileLength( Object obj , long length );

	void onProgressChanged( int progress , Object obj );

	void onCompleted( Object obj , boolean success , File file );
    }

    public static File DownLoadFile( String httpUrl , String savePath , String saveFileName , ProgressChangedCallBack callBack , Object obj )
    {
	final String fileName = saveFileName;
	File Dir = new File( savePath );

	if( Dir.exists( ) == true && Dir.isFile( ) )
	{
	    Dir.delete( );
	}

	if( Dir.exists( ) == false )
	{
	    Dir.mkdir( );
	}

	final File file = new File( savePath + fileName );

	boolean success = true;
	try
	{
	    URL url = new URL( httpUrl );
	    try
	    {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection( );

		conn.setConnectTimeout( 10000 );
		conn.setReadTimeout( 10000 );

		int length = conn.getContentLength( );
		int currentLength = 0;
		float currentProgress = 0;
		final float interval = 1;

		//		length = -1;

		callBack.loadFileLength( obj , length );
		if( file.exists( ) == true )
		{
		    if( length != -1 && file.length( ) != length )
		    {
			file.delete( );
		    }
		    else
		    {
			conn.disconnect( );
			return file;
		    }
		}
		InputStream is = conn.getInputStream( );
		FileOutputStream fos = new FileOutputStream( file );
		byte [] buf = new byte [256];
		conn.connect( );
		double count = 0;
		if( conn.getResponseCode( ) >= 400 )
		{
		    if( callBack != null )
		    {
			callBack.onCompleted( obj , false , null );
		    }
		    return null;
		}
		else
		{
		    while ( count <= 100 )
		    {
			if( is != null )
			{
			    int numRead = is.read( buf );
			    if( numRead <= 0 )
			    {
				break;
			    }
			    else
			    {
				fos.write( buf , 0 , numRead );
				currentLength += numRead;

				float tempProgress = ( (float)currentLength / (float)length ) * 100.0f;

				if( tempProgress - currentProgress >= interval )
				{
				    if( callBack != null )
				    {
					callBack.onProgressChanged( (int)tempProgress , obj );
				    }
				    currentProgress = tempProgress;
				}
			    }
			}
			else
			{
			    break;
			}
		    }
		}
		conn.disconnect( );
		fos.close( );
		is.close( );
	    }
	    catch ( IOException e )
	    {
		success = false;
		e.printStackTrace( );
	    }
	}
	catch ( MalformedURLException e )
	{
	    success = false;
	    e.printStackTrace( );
	}
	finally
	{
	    if( callBack != null )
	    {
		callBack.onCompleted( obj , success , file );
	    }
	}
	return file;
    }
}
