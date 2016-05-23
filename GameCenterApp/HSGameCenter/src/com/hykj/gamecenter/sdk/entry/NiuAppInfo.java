package com.hykj.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

public class NiuAppInfo implements Parcelable
{

    private int appId;
    private String appKey;
    private String appName;
    private String appPackageName;
    private int appType;
    private int devMode;

    public int getDevMode()
    {
	return devMode;
    }

    public void setDevMode( int devMode )
    {
	this.devMode = devMode;
    }

    public NiuAppInfo( int appId , String appKey , String appName , String appPackageName , int appType )
    {
	this.appId = appId;
	this.appKey = appKey;
	this.appName = appName;
	this.appPackageName = appPackageName;
	this.appType = appType;
	devMode = NiuSDKConstant.SDK_PUBLIC_MODE; //默认为正式环境
    }

    public NiuAppInfo()
    {

    }

    public int getAppId()
    {
	return appId;
    }

    public void setAppId( int appId )
    {
	this.appId = appId;
    }

    public String getAppKey()
    {
	return appKey;
    }

    public void setAppKey( String appKey )
    {
	this.appKey = appKey;
    }

    public String getAppName()
    {
	return appName;
    }

    public void setAppName( String appName )
    {
	this.appName = appName;
    }

    public String getAppPackageName()
    {
	return appPackageName;
    }

    public void setAppPackageName( String appPackageName )
    {
	this.appPackageName = appPackageName;
    }

    public int getAppType()
    {
	return appType;
    }

    public void setAppType( int appType )
    {
	this.appType = appType;
    }

    @Override
    public int describeContents()
    {
	return 0;
    }

    @Override
    public void writeToParcel( Parcel dest , int flags )
    {
	dest.writeInt( appId );
	dest.writeString( appKey );
	dest.writeString( appName );
	dest.writeString( appPackageName );
	dest.writeInt( appType );
	dest.writeInt( devMode );
    }

    public static final Parcelable.Creator< NiuAppInfo > CREATOR = new Creator< NiuAppInfo >( )
    {

	@Override
	public NiuAppInfo createFromParcel( Parcel source )
	{
	    NiuAppInfo entry = new NiuAppInfo( source.readInt( ) , source.readString( ) , source.readString( ) , source.readString( ) , source.readInt( ) );
	    entry.setDevMode( source.readInt( ) );
	    return entry;
	}

	@Override
	public NiuAppInfo [] newArray( int size )
	{
	    return new NiuAppInfo [size];
	}

    };

    @Override
    public String toString()
    {
	return "NiuAppInfo [appId=" + appId + ", appKey=" + appKey + ", appName=" + appName + ", appPackageName=" + appPackageName + ", appType=" + appType + ", devMode=" + devMode + "]";
    }
}
