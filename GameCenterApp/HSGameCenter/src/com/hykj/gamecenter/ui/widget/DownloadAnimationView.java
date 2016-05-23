package com.hykj.gamecenter.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class DownloadAnimationView extends ImageView implements AnimatorUpdateListener
{
    private Context mContext;
    private float mFirstX;
    private float mEndX;
    private static ObjectAnimator animDown;

    public DownloadAnimationView( Context context )
    {
	super( context );
	mContext = context;
    }

    public DownloadAnimationView( Context context , AttributeSet attrs )
    {
	super( context , attrs );
	mContext = context;
    }

    public DownloadAnimationView( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	mContext = context;
    }

    public void init( float firstX , float endX )
    {
	mFirstX = firstX;
	mEndX = endX;
    }

    public void createAnimation()
    {
	if( animDown == null )
	{
	    animDown = ObjectAnimator.ofFloat( this , "x" , mFirstX , mEndX ).setDuration( 2000 );
	}
	else
	{
	    animDown.setPropertyName( "x" );
	    animDown.setFloatValues( mFirstX , mEndX );
	    animDown.setTarget( this );
	    animDown.setDuration( 2000 );
	}
	animDown.setInterpolator( new LinearInterpolator( ) );
	animDown.setRepeatCount( Integer.MAX_VALUE );
	animDown.setRepeatMode( Animation.RESTART );
	animDown.addUpdateListener( this );

    }

    public void startAnimation()
    {
	createAnimation( );
	animDown.start( );
    }

    public void endAnimation()
    {

	if( animDown != null && animDown.isRunning( ) && animDown.isStarted( ) )
	{
	    animDown.end( );
	    animDown = null;
	}

    }

    @Override
    public void onAnimationUpdate( ValueAnimator animation )
    {
	invalidate( );
    }

}
