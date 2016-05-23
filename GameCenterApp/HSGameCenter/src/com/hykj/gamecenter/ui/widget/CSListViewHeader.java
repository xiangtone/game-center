package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class CSListViewHeader extends LinearLayout
{
    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private int mState = STATE_NORMAL;
    private boolean bIsHeaderPosition = true;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private final int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    public CSListViewHeader( Context context , boolean bHeaderPos )
    {
	super( context );
	bIsHeaderPosition = bHeaderPos;
	initView( context );
    }

    /**
     * @param context
     * @param attrs
     */
    public CSListViewHeader( Context context , AttributeSet attrs , boolean bHeaderPos )
    {
	super( context , attrs );
	bIsHeaderPosition = bHeaderPos;
	initView( context );
    }

    private void initView( Context context )
    {
	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( android.view.ViewGroup.LayoutParams.MATCH_PARENT , 0 );
	mContainer = (LinearLayout)LayoutInflater.from( context ).inflate( R.layout.csl_cs_listview_header , null );
	addView( mContainer , lp );

	mContainer.setGravity( bIsHeaderPosition ? Gravity.BOTTOM : Gravity.TOP );

	mArrowImageView = (ImageView)findViewById( R.id.csl_listview_header_arrow );
	if( bIsHeaderPosition == false )
	{
	    mArrowImageView.setImageResource( R.drawable.csl_pull_refresh_arrow_up );
	    //			TextView tipTextView = (TextView) mContainer.findViewById(R.id.csl_listview_header_hint_textview);
	    //			tipTextView.setText(R.string.csl_listview_header_hint_normal_down);
	}
	mHintTextView = (TextView)findViewById( R.id.csl_listview_header_hint_textview );
	mProgressBar = (ProgressBar)findViewById( R.id.csl_listview_header_progressbar );

	mRotateUpAnim = new RotateAnimation( 0.0f , -180.0f , Animation.RELATIVE_TO_SELF , 0.5f , Animation.RELATIVE_TO_SELF , 0.5f );
	mRotateUpAnim.setDuration( ROTATE_ANIM_DURATION );
	mRotateUpAnim.setFillAfter( true );
	mRotateDownAnim = new RotateAnimation( -180.0f , 0.0f , Animation.RELATIVE_TO_SELF , 0.5f , Animation.RELATIVE_TO_SELF , 0.5f );
	mRotateDownAnim.setDuration( ROTATE_ANIM_DURATION );
	mRotateDownAnim.setFillAfter( true );
    }

    public int getState()
    {
	return mState;
    }

    public void setState( int state )
    {
	if( state == mState )
	    return;

	if( state == STATE_REFRESHING )
	{ 
	    mArrowImageView.clearAnimation( );
	    mArrowImageView.setVisibility( View.INVISIBLE );
	    mProgressBar.setVisibility( View.VISIBLE );
	}
	else
	{ 
	    mArrowImageView.setVisibility( View.VISIBLE );
	    mProgressBar.setVisibility( View.INVISIBLE );
	}

	switch ( state )
	{
	    case STATE_NORMAL :
		if( mState == STATE_READY )
		{
		    mArrowImageView.startAnimation( mRotateDownAnim );
		}
		if( mState == STATE_REFRESHING )
		{
		    mArrowImageView.clearAnimation( );
		}
		mHintTextView.setText( bIsHeaderPosition ? R.string.csl_listview_header_hint_normal_up : R.string.csl_listview_header_hint_normal_down );
		break;
	    case STATE_READY :
		if( mState != STATE_READY )
		{
		    mArrowImageView.clearAnimation( );
		    mArrowImageView.startAnimation( mRotateUpAnim );
		    mHintTextView.setText( R.string.csl_listview_header_hint_ready );
		}
		break;
	    case STATE_REFRESHING :
		mHintTextView.setText( R.string.csl_listview_header_hint_loading );
		break;
	    default :
	}

	mState = state;
    }

    public void setVisiableHeight( int height )
    {
	if( height < 0 )
	    height = 0;
	LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContainer.getLayoutParams( );
	lp.height = height;
	mContainer.setLayoutParams( lp );
    }

    public int getVisiableHeight()
    {
	return mContainer.getHeight( );
    }

}
