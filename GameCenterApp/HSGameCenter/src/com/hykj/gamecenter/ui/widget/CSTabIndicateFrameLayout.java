package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utilscs.CSLibSettings;

public class CSTabIndicateFrameLayout extends FrameLayout
{

    public CSTabIndicateFrameLayout( Context context )
    {
	this( context , null );
	// TODO Auto-generated constructor stub
    }

    public CSTabIndicateFrameLayout( Context context , AttributeSet attrs )
    {
	super( context , attrs );
	// TODO Auto-generated constructor stub
    }

    Bitmap bitmap;
    int tabCount = 1;
    private float oldLeft = 0;

    public void setTabCount( int tabCount )
    {
	this.tabCount = tabCount;
	//	int oneColumnWidth = getWidth( ) / tabCount;
	//	float desX = oneColumnWidth * pos + ( oneColumnWidth - bitmap.getWidth( ) ) * 0.5f;
	//	left = desX;
    }

    @Override
    protected void onAttachedToWindow()
    {
	// TODO Auto-generated method stub
	super.onAttachedToWindow( );
	bitmap = BitmapFactory.decodeResource( getContext( ).getResources( ) , R.drawable.csl_tab_selected_indicater );

    }

    private float left = 0;
    private float top = 0;
    private float right = 0;
    private float bottom = 0;
    private static int color;
    private Paint mPaint;

    public void setPos( float left , float top , float right , float bottom , int color )
    {
	this.left = left;
	this.top = top;
	this.right = right;
	this.bottom = bottom;
	CSTabIndicateFrameLayout.color = color;
    }

    float curX;

    @Override
    protected void dispatchDraw( Canvas canvas )
    {
	// TODO Auto-generated method stub
	super.dispatchDraw( canvas );
	if( CSLibSettings.ACTION_BAR_INDICATE_TYPE == 1 && CSLibSettings.ACTION_BAR_SHOW_INDICATE )
	{
	    int oneColumnWidth = getWidth( ) / tabCount;
	    //	    LogUtils.e( "oneColumnWidth = " + oneColumnWidth );
	    //	    LogUtils.e( "getLeft( ) =" + getLeft( ) );
	    float desX = ( oneColumnWidth - bitmap.getWidth( ) ) * 0.5f + getLeft( );
	    //	    LogUtils.e( "desX = " + desX );
	    //	    LogUtils.e( "dfdfdfd left=" + left );
	    oldLeft = left;
	    left = left + desX;
	    //	    LogUtils.e( "left=" + left );
	    canvas.drawBitmap( bitmap , left , getBottom( ) - bitmap.getHeight( ) , null );
	    postInvalidate( );
	    left = oldLeft;
	}
	else if( CSLibSettings.ACTION_BAR_INDICATE_TYPE == 0 && CSLibSettings.ACTION_BAR_SHOW_INDICATE )
	{
	    if( mPaint == null )
	    {
		mPaint = new Paint( );
	    }
	    mPaint.setColor( color );
	    canvas.drawRect( left , top , right , bottom , mPaint );
	    postInvalidate( );
	}
    }

    @Override
    protected void onDetachedFromWindow()
    {
	// TODO Auto-generated method stub
	super.onDetachedFromWindow( );
	bitmap.recycle( );
    }
}
