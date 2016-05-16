package com.hykj.gamecenter.activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.ui.widget.CSLoadingView;
import com.hykj.gamecenter.ui.widget.CSLoadingView.ICSListViewLoadingRetry;
import com.hykj.gamecenter.ui.widget.ICSLoadingViewListener;
import com.hykj.gamecenter.utils.SystemBarTintManager;

public class CSLoadingActivity extends Activity implements ICSListViewLoadingRetry
{

    private View mRootLayout = null;
    private View mHeaderView = null;
    private FrameLayout mContainerFrameLayout = null;
    private View mContentLayout = null;
    private CSLoadingView mLoadingView = null;
    private ICSLoadingViewListener mListener = null;

    private final int LAS_LOADING = 0x01;
    private final int LAS_NONETWORK = 0x02;
    private final int LAS_NORMAL = 0x03;

    //	public void setContentView(int contentLayoutResID, int headerlayoutResID) {
    //
    //		mRootLayout = new LinearLayout(this);
    //		mRootLayout.setOrientation(LinearLayout.VERTICAL);
    //		LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
    //				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
    //		mRootLayout.setLayoutParams(rllp);
    //
    //		//mHeaderView = new LinearLayout(this);
    //		//mHeaderView.setOrientation(LinearLayout.VERTICAL);
    //		//		LinearLayout.LayoutParams hllp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
    //		//				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    //		//		mHeaderView.setLayoutParams(hllp);
    //
    //		if (headerlayoutResID != 0) {
    //			mHeaderView = LayoutInflater.from(this).inflate(headerlayoutResID, null);
    //			mHeaderView.setVisibility(View.VISIBLE);
    //		} else {
    //			mHeaderView.setVisibility(View.GONE);
    //		}
    //
    //		mRootLayout.addView(mHeaderView);
    //
    //		mContainerFrameLayout = new FrameLayout(this);
    //		FrameLayout.LayoutParams cllp = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
    //				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
    //		mContainerFrameLayout.setLayoutParams(cllp);
    //		mRootLayout.addView(mContainerFrameLayout);
    //
    //		mContentLayout = LayoutInflater.from(this).inflate(contentLayoutResID, null);
    //		mContainerFrameLayout.addView(mContentLayout);
    //
    //		mLoadingView = new CSLoadingView(this);
    //		mLoadingView.setOnRetryListener(this);
    //		mLoadingView.setBackgroundDrawable(mContentLayout.getBackground());
    //		mContainerFrameLayout.addView(mLoadingView);
    //
    //		super.setContentView(mRootLayout);
    //	}

    public void setContentView( int contentLayoutResID , int headerlayoutResID )
    {
	mRootLayout = LayoutInflater.from( this ).inflate( R.layout.csl_cs_loadingactivity , null );

	if( headerlayoutResID != 0 )
	{
	    ViewStub header = (ViewStub)mRootLayout.findViewById( R.id.csl_cs_loading_activity_header_viewstub );
	    header.setLayoutResource( headerlayoutResID );
	    mHeaderView = header.inflate( );
	}

	mContainerFrameLayout = (FrameLayout)mRootLayout.findViewById( R.id.csl_cs_loading_activity_content_container );

	ViewStub content = (ViewStub)mContainerFrameLayout.findViewById( R.id.csl_cs_loading_activity_content_viewstub );
	content.setLayoutResource( contentLayoutResID );
	mContentLayout = content.inflate( );

	mLoadingView = new CSLoadingView( this );
	mLoadingView.setOnRetryListener( this );
	mLoadingView.setBackgroundDrawable( mContentLayout.getBackground( ) );
	mContainerFrameLayout.addView( mLoadingView );

	super.setContentView( mRootLayout );
    SystemBarTintManager.useSystemBar( this , R.color.action_blue_color);
    }

    public View getHeaderView()
    {
	return mHeaderView;
    }

    public void setCSLoadingViewListener( ICSLoadingViewListener listener )
    {
	mListener = listener;
    }

    public void initRequestData()
    {
	if( mListener == null )
	    return;

	setViewStatus( LAS_LOADING );
	mListener.onInitRequestData( );
    }

    public void notifyRequestDataReceived()
    {
	setViewStatus( LAS_NORMAL );
    }

    public void notifyRequestDataError()
    {
	setViewStatus( LAS_NONETWORK );
    }

    public void notifyLoadingState()
    {
	setViewStatus( LAS_LOADING );
    }

    public void setLoadingTipText( String tip )
    {
	mLoadingView.setLoadingTipText( tip );
    }

    public void setLoadingViewBackgroundResource( int resid)
    {
	mLoadingView.setBackgroundResource( resid );
    }
    
    private void setViewStatus( int nStatus )
    {
	switch ( nStatus )
	{
	    case LAS_LOADING :
		mContentLayout.setVisibility( View.GONE );
		mLoadingView.showLoading( );
		break;
	    case LAS_NONETWORK :
		mContentLayout.setVisibility( View.GONE );
		mLoadingView.showNoNetwork( );
		break;
	    case LAS_NORMAL :
		mLoadingView.hide( );
		mContentLayout.setVisibility( View.VISIBLE );
		break;

	    default :
		break;
	}
    }

    @Override
    public void onRetry()
    {
	setViewStatus( LAS_LOADING );
	if( mListener == null )
	    return;

	mListener.onRetryRequestData( );
    }

}
