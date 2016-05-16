package com.hykj.gamecenter.logic.entry;

public class ReportInfo
{

    int statActId = 0; // 统计操作id1    ( 首次启动=10001, 启动下载=10002)
    int statActId2 = 0; // 统计操作id2
    String ext1 = ""; //packName
    String ext2 = ""; //newVerName
    String ext3 = ""; //newVerCode

    public ReportInfo( int statActId , int statActId2 , String ext1 , String ext2 , String ext3 )
    {
	super( );
	this.statActId = statActId;
	this.statActId2 = statActId2;
	this.ext1 = ext1;
	this.ext2 = ext2;
	this.ext3 = ext3;
    }

    public int getStatActId()
    {
	return statActId;
    }

    public void setStatActId( int statActId )
    {
	this.statActId = statActId;
    }

    public int getStatActId2()
    {
	return statActId2;
    }

    public void setStatActId2( int statActId2 )
    {
	this.statActId2 = statActId2;
    }

    public String getExt1()
    {
	return ext1 + "";
    }

    public void setExt1( String ext1 )
    {
	this.ext1 = ext1;
    }

    public String getExt2()
    {
	return ext2 + "";
    }

    public void setExt2( String ext2 )
    {
	this.ext2 = ext2;
    }

    public String getExt3()
    {
	return ext3 + "";
    }

    public void setExt3( String ext3 )
    {
	this.ext3 = ext3;
    }

    public static class ReportLuachInfo
    {
	/**
	 * 首次启动  10001
	 */
	public static final int FIRST_LAUNCH = 10001;
	/**
	 * 启动下载(普通应用) 10002
	 */
	public static final int DOWNLOAD_APK = 10002;
	/**
	 * 新版启动下载   10003
	 */
	public static final int NEW_VERSION_DOWNLOAD = 10003;
	/**
	 * 新版下载成功 10004
	 */
	public static final int NEW_VERSION_DOWNLOAD_SUCCESS = 10004;
	/**
	 * 新版安装成功 10005
	 */
	public static final int NEW_VERSION_INSTALL_SUCCESS = 10005;

    }

}
