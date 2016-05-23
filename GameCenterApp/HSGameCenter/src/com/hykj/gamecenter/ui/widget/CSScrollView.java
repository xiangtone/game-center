package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.hykj.gamecenter.utils.Logger;

public class CSScrollView extends ScrollView
{

    private static final int MAX_SCROLL_Y = 200;
    private static final float SCROLL_RATIO = 0.5f;// 阻尼系数  

    public CSScrollView( Context context )
    {
	super( context );
	// TODO Auto-generated constructor stub
    }

    public CSScrollView( Context context , AttributeSet attrs )
    {
	super( context , attrs );
	// TODO Auto-generated constructor stub
    }

    public CSScrollView( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	// TODO Auto-generated constructor stub
    }

    @Override
    protected boolean overScrollBy( int deltaX , int deltaY , int scrollX , int scrollY , int scrollRangeX , int scrollRangeY , int maxOverScrollX , int maxOverScrollY , boolean isTouchEvent )
    {
	// TODO Auto-generated method stub
	Logger.d( "CSScrollView" , "overScrollBy" );
	int newDeltaY = deltaY;
	int delta = (int) ( deltaY * SCROLL_RATIO );
	if( delta != 0 )
	    newDeltaY = delta;

	return super.overScrollBy( deltaX , newDeltaY , scrollX , scrollY , scrollRangeX , scrollRangeY , maxOverScrollX , MAX_SCROLL_Y , isTouchEvent );
    }

    @Override
    public void setOverScrollMode( int mode )
    {
	super.setOverScrollMode( View.OVER_SCROLL_ALWAYS );
    }

}
