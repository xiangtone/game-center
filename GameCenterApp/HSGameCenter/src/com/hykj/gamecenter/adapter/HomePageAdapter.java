package com.hykj.gamecenter.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class HomePageAdapter extends FragmentPagerAdapter
{

    private static final String TAG = "HomPageAdapter";
    private ArrayList< TabInfo > mTab = new ArrayList< HomePageAdapter.TabInfo >( );
    private Activity mContext;
    private ViewPager mViewPager;

    public HomePageAdapter( Activity activity , ViewPager pager )
    {
	super( activity.getFragmentManager( ) );
	mContext = activity;
	mViewPager = pager;
	mViewPager.setAdapter( this );
    }

    static final class TabInfo
    {
	private Class< ? > clss;
	private Bundle args;
	private String title;
	private Fragment fragment;

	TabInfo( Class< ? > _class , Bundle _args , String _title )
	{
	    clss = _class;
	    args = _args;
	    title = _title;
	}
    }

    public void addTab( Class< ? > clss , Bundle args , String title )
    {
	TabInfo info = new TabInfo( clss , args , title );
	mTab.add( info );
	notifyDataSetChanged( );
    }

    @Override
    public Fragment getItem( int position )
    {
	TabInfo info = mTab.get( position );
	if( info.fragment == null )
	{
	    info.fragment = Fragment.instantiate( mContext , info.clss.getName( ) , info.args );
	}
	return info.fragment;
    }

    @Override
    public int getCount()
    {
	return mTab.size( );
    }

    @Override
    public CharSequence getPageTitle( int position )
    {
	return mTab.get( position ).title;
    }

}
