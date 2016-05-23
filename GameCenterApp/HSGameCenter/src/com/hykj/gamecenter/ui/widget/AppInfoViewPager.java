package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AppInfoViewPager extends ViewPager
{

    private boolean isCanScroll = true;

    public boolean isCanScroll()
    {
	return isCanScroll;
    }

    public void setCanScroll( boolean isCanScroll )
    {
	this.isCanScroll = isCanScroll;
    }

    public AppInfoViewPager( Context context )
    {
	super( context );
    }

    public AppInfoViewPager( Context context , AttributeSet attrs )
    {
	super( context , attrs );
    }

    @Override
    public boolean onTouchEvent( MotionEvent ev )
    {
	if( isCanScroll == false )
	{
	    return false;
	}
	else
	{
	    return super.onTouchEvent( ev );
	}
    }

    @Override
    public boolean onInterceptTouchEvent( MotionEvent ev )
    {
	if( isCanScroll == false )
	{
	    return false;
	}
	else
	{
	    return super.onInterceptTouchEvent( ev );
	}

    }
    
    @Override
    protected void onMeasure(int arg0, int arg1) {
        // TODO Auto-generated method stub
        super.onMeasure(arg0, arg1);
        
    }

}
