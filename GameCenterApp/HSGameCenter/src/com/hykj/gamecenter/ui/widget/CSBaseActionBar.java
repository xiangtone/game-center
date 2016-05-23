package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hykj.gamecenter.R;

public class CSBaseActionBar extends RelativeLayout
{

    private Context mContext;
    protected ViewGroup mLeftContainer;
    protected ViewGroup mMiddleContainer;
    protected ViewGroup mRightContainer;

    public CSBaseActionBar( Context context )
    {
	this( context , null );
    }

    public CSBaseActionBar( Context context , AttributeSet attrs )
    {
	this( context , attrs , R.attr.plCSActionBarStyle );
    }

    public CSBaseActionBar( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	mContext = context;
	LayoutInflater.from( context ).inflate( R.layout.csl_cs_base_actionbar , this );
    }
    
    @Override
    protected void onFinishInflate()
    {
	super.onFinishInflate( );
	mLeftContainer = (ViewGroup)findViewById( R.id.left_function_layout );
	mMiddleContainer = (ViewGroup)findViewById( R.id.middle_function_layout );
	mRightContainer = (ViewGroup)findViewById( R.id.right_function_layout );
    }
}
