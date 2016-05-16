package com.hykj.gamecenter.controller;

import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.hykj.gamecenter.App;

public class ControllerHelper
{

    private static String Tag = "ControllerHelper";
    private static ControllerHelper mInstance;
    private String mGroupsConfigCacheDataVer; //配置信息版本缓存
    private String mSubjectsCacheDataVer; //专题版本缓存
    private String mGroupElemsCacheDataVer; //分组元素版本缓存
    private String mAppInfoCacheDataVer; //应用信息版本缓存
    private String mSplashImageCacheDataVer; //闪屏信息版本缓存
    private String mNoteUserToHandleTask; //进入主界面是否提醒处理被暂停的任务

    private static String GROUP_CONFIG_CACHE_DATA_VERION = "GroupsConfigCacheDataVerion";
    private static String SUBJECTS_CACHE_DATA_VERION = "SubjectsCacheDataVerion";
    private static String GROUP_ELEMS_CACHE_DATA_VERION = "GroupElemsCacheDataVerion";
    private static String APPINFO_CACHE_DATA_VERION = "AppInfoCacheDataVerion";
    private static String SPLASH_IMAGE_CACHE_DATA_VERION = "SplashImageCacheDataVerion";
    private static String NOTE_USER_TO_HANDLE_TASK = "NoteUserToHandleTask";

    public static synchronized ControllerHelper getInstance()
    {
	if( mInstance == null )
	{
	    mInstance = new ControllerHelper( );
	    mInstance.init( );
	}

	return mInstance;
    }

    private ControllerHelper()
    {}

    private void init()
    {
	mGroupsConfigCacheDataVer = App.getSharedPreference( ).getString( GROUP_CONFIG_CACHE_DATA_VERION , "" );
	mSubjectsCacheDataVer = App.getSharedPreference( ).getString( SUBJECTS_CACHE_DATA_VERION , "" );
	mGroupElemsCacheDataVer = App.getSharedPreference( ).getString( GROUP_ELEMS_CACHE_DATA_VERION , "" );
	mAppInfoCacheDataVer = App.getSharedPreference( ).getString( APPINFO_CACHE_DATA_VERION , "" );
	mSplashImageCacheDataVer = App.getSharedPreference( ).getString( SPLASH_IMAGE_CACHE_DATA_VERION , "" );
	mNoteUserToHandleTask = App.getSharedPreference( ).getString( NOTE_USER_TO_HANDLE_TASK , "" );

    }

    public String getGroupsConfigCacheDataVer()
    {
	Log.d( Tag , "getGroupsConfigListCacheDataVer = " + mGroupsConfigCacheDataVer );
	return mGroupsConfigCacheDataVer;
    }

    public void setGroupsConfigCacheDataVer( String groupsConfigCacheDataVer )
    {
	mGroupsConfigCacheDataVer = groupsConfigCacheDataVer;

	Editor editor = App.getSharedPreference( ).edit( );
	editor.putString( GROUP_CONFIG_CACHE_DATA_VERION , mGroupsConfigCacheDataVer );
	editor.commit( );
	Log.d( Tag , "setGroupsConfigCacheDataVer = " + mGroupsConfigCacheDataVer );
    }

    public String getSubjectsCacheDataVer()
    {
	Log.d( Tag , "getSubjectsCacheDataVer = " + mSubjectsCacheDataVer );
	return mSubjectsCacheDataVer;
    }

    public void setSubjectsCacheDataVer( String subjectsCacheDataVer )
    {
	mSubjectsCacheDataVer = subjectsCacheDataVer;

	Editor editor = App.getSharedPreference( ).edit( );
	editor.putString( SUBJECTS_CACHE_DATA_VERION , mSubjectsCacheDataVer );
	editor.commit( );
	Log.d( Tag , "setSubjectsCacheDataVer = " + mSubjectsCacheDataVer );
    }

    public String getGroupElemsCacheDataVer()
    {
	Log.d( Tag , "getGroupElemsCacheDataVer = " + mGroupElemsCacheDataVer );
	return mGroupElemsCacheDataVer;
    }

    public void setGroupElemsCacheDataVer( String groupElemsCacheDataVer )
    {
	mGroupElemsCacheDataVer = groupElemsCacheDataVer;

	Editor editor = App.getSharedPreference( ).edit( );
	editor.putString( GROUP_ELEMS_CACHE_DATA_VERION , mGroupElemsCacheDataVer );
	editor.commit( );
	Log.d( Tag , "setGroupElemsCacheDataVer = " + mGroupElemsCacheDataVer );
    }

    public String getAppInfoCacheDataVer()
    {
	Log.d( Tag , "getAppInfoCacheDataVer = " + mAppInfoCacheDataVer );
	return mAppInfoCacheDataVer;
    }

    public void setAppInfoCacheDataVer( String appInfoCacheDataVer )
    {
	mAppInfoCacheDataVer = appInfoCacheDataVer;

	Editor editor = App.getSharedPreference( ).edit( );
	editor.putString( APPINFO_CACHE_DATA_VERION , mAppInfoCacheDataVer );
	editor.commit( );
	Log.d( Tag , "setAppInfoCacheDataVer = " + mAppInfoCacheDataVer );
    }

    public String getSplashImageCacheDataVer()
    {
	Log.d( Tag , "getSplashImageCacheDataVer = " + mSplashImageCacheDataVer );
	return mSplashImageCacheDataVer;
    }

    public void setSplashImageCacheDataVer( String splashImageCacheDataVer )
    {
	mSplashImageCacheDataVer = splashImageCacheDataVer;

	Editor editor = App.getSharedPreference( ).edit( );
	editor.putString( SPLASH_IMAGE_CACHE_DATA_VERION , mSplashImageCacheDataVer );
	editor.commit( );
	Log.d( Tag , "setSplashImageCacheDataVer = " + mSplashImageCacheDataVer );
    }

    //设置下载任务提醒
    public String getNoteToUserToHandleTask()
    {
	return mNoteUserToHandleTask;
    }

    public void setNoteToUserToHandleTask( String noteToUserToHandleTask )
    {
	mNoteUserToHandleTask = noteToUserToHandleTask;
	Editor editor = App.getSharedPreference( ).edit( );
	editor.putString( NOTE_USER_TO_HANDLE_TASK , mNoteUserToHandleTask );
	editor.commit( );
    }

    public void clearCacheDateVer()
    {
	mGroupsConfigCacheDataVer = "";
	mSubjectsCacheDataVer = "";
	mGroupElemsCacheDataVer = "";
	mAppInfoCacheDataVer = "";
	mSplashImageCacheDataVer = "";
	mNoteUserToHandleTask = "";
	App.getSharedPreference( ).edit( ).putString( GROUP_CONFIG_CACHE_DATA_VERION , mGroupsConfigCacheDataVer );
	App.getSharedPreference( ).edit( ).putString( SUBJECTS_CACHE_DATA_VERION , mSubjectsCacheDataVer );
	App.getSharedPreference( ).edit( ).putString( GROUP_ELEMS_CACHE_DATA_VERION , mGroupElemsCacheDataVer );
	App.getSharedPreference( ).edit( ).putString( APPINFO_CACHE_DATA_VERION , mAppInfoCacheDataVer );
	App.getSharedPreference( ).edit( ).putString( SPLASH_IMAGE_CACHE_DATA_VERION , mSplashImageCacheDataVer );
	App.getSharedPreference( ).edit( ).putString( NOTE_USER_TO_HANDLE_TASK , mNoteUserToHandleTask );
    }

}
