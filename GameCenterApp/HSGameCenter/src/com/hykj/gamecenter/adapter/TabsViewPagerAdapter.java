package com.hykj.gamecenter.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

public class TabsViewPagerAdapter extends FragmentPagerAdapter

{

    public TabsViewPagerAdapter( FragmentManager fm )
    {
	super( fm );
	// TODO Auto-generated constructor stub
    }

    public TabsViewPagerAdapter( FragmentManager fm , Activity activity )
    {
	super( fm );
	mContext = activity;
    }

    public static final int FRAGMENT_TYPE = 1;
    public static final int FRAGMENT_CLASS_TYPE = 2;

    //默认构造方法

    private Context mContext;

    private final ArrayList< TabInfo > mTabs = new ArrayList< TabInfo >( );

    static final class TabInfo
    {
	private final Fragment fragment;
	private final int type;
	private final Class< ? > clss;
	private final Bundle args;
	private final String title;

	TabInfo( Fragment _fragment , int _type , Class< ? > _class , Bundle _args , String _title )
	{
	    type = _type;
	    fragment = _fragment;
	    clss = _class;
	    args = _args;
	    title = _title;
	}
    }

    //自定构造方法

    public void addTabFrament( Fragment fragment , int type , String title )
    {
	TabInfo info = new TabInfo( fragment , type , null , null , title );
	mTabs.add( info );
	notifyDataSetChanged( );
    }

    public void addTabFragmentClass( int type , Class< ? > clss , Bundle args , String title )
    {
	TabInfo info = new TabInfo( null , type , clss , args , title );
	mTabs.add( info );
	notifyDataSetChanged( );
    }

    public void clear()
    {
	mTabs.clear( );
    }

    @Override
    public CharSequence getPageTitle( int position )
    {
	return mTabs.get( position ).title;
    }

    @Override
    public int getCount()
    {
	return mTabs.size( );
    }

    @Override
    public Fragment getItem( int position )
    {
	TabInfo info = mTabs.get( position );
	if( info.type == FRAGMENT_TYPE )
	{
	    return info.fragment;
	}
	else if( info.type == FRAGMENT_CLASS_TYPE )
	{
	    return Fragment.instantiate( mContext , info.clss.getName( ) , info.args );
	}
	//空的
	return new Fragment( );
    }

}
