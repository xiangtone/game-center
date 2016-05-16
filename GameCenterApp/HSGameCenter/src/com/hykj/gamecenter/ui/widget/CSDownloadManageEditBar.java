package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class CSDownloadManageEditBar extends RelativeLayout implements OnClickListener
{
    private OnHeaderBarClickListener mOnHeaderBarClickListener;
    private Context mContext;
    private ImageView mLeftHandleBtn;
    private ImageView mRightHandleBtn;
    private TextView mHeaderTitleTv;

    private String mRightHandleBtnTitle;
    private String mLeftHandleBtnTitle;

    private String mHeaderTitle;

    public CSDownloadManageEditBar( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	TypedArray a = context.obtainStyledAttributes( attrs , R.styleable.CSHeaderBar , defStyle , 0 );
	mContext = context;
	mLeftHandleBtnTitle = a.getString( R.styleable.CSHeaderBar_left_title );
	mRightHandleBtnTitle = a.getString( R.styleable.CSHeaderBar_right_title );
	mHeaderTitle = a.getString( R.styleable.CSHeaderBar_header_title );

	LayoutInflater.from( context ).inflate( R.layout.csl_cs_download_manager_edit_bar , this );
	a.recycle( );
    }

    public CSDownloadManageEditBar( Context context , AttributeSet attrs )
    {
	this( context , attrs , R.attr.plHeaderBarStyle );
	// TODO Auto-generated constructor stub
    }

    public CSDownloadManageEditBar( Context context )
    {
	this( context , null );
	// TODO Auto-generated constructor stub
    }

    @Override
    protected void onFinishInflate()
    {
	super.onFinishInflate( );

	mHeaderTitleTv = (TextView)findViewById( R.id.header_title );
	mHeaderTitleTv.setText( mHeaderTitle == null ? mContext.getString( R.string.csl_edit_mode ) : mHeaderTitle );

	mLeftHandleBtn = (ImageView)findViewById( R.id.left_handle );
	mRightHandleBtn = (ImageView)findViewById( R.id.right_handle );
//	mRightHandleBtn.setText( mRightHandleBtnTitle == null ? mContext.getString( R.string.csl_submit ) : mRightHandleBtnTitle );
//	mLeftHandleBtn.setText( mLeftHandleBtnTitle == null ? mContext.getString( R.string.csl_cancel ) : mLeftHandleBtnTitle );

	mLeftHandleBtn.setOnClickListener( this );
	mRightHandleBtn.setOnClickListener( this );
	mLeftHandleBtn.setTag( OnHeaderBarClickListener.LEFT_HANDLE_BNT );
	mRightHandleBtn.setTag( OnHeaderBarClickListener.RIGHT_HANDLE_BNT );
    }

    @Override
    public void onClick( View view )
    {
	if( mOnHeaderBarClickListener == null )
	    return;

	mOnHeaderBarClickListener.onHeaderBarClicked( (Integer)view.getTag( ) );
    }

    public void setOnHeaderBarClickListener( OnHeaderBarClickListener listener )
    {
	mOnHeaderBarClickListener = listener;
    }

    public interface OnHeaderBarClickListener
    {
	int LEFT_HANDLE_BNT = 0x01;
	int RIGHT_HANDLE_BNT = 0x02;

	void onHeaderBarClicked( int position );
    }

    public void setmHeaderTitle( String mHeaderTitle )
    {
	if( mHeaderTitleTv != null )
	    mHeaderTitleTv.setText( mHeaderTitle );
    }

    public void setmRightHandleBtnTitle( String mRightHandleBtnTitle )
    {
	this.mRightHandleBtnTitle = mRightHandleBtnTitle;
//	this.mRightHandleBtn.setText( mRightHandleBtnTitle );
    }

    public void setmLeftHandleBtnTitle( String mLeftHandleBtnTitle )
    {
	this.mLeftHandleBtnTitle = mLeftHandleBtnTitle;
//	this.mLeftHandleBtn.setText( mLeftHandleBtnTitle );
    }

    public void setmRightHandleBtnBackground( int resoureId )
    {
//	this.mRightHandleBtn.setBackgroundResource( resoureId );
    }

    public void setmLeftHandleBtnBackground( int resoureId )
    {
//	this.mLeftHandleBtn.setBackgroundResource( resoureId );
    }

    public ImageView getmRightHandleBtn()
    {
	return mRightHandleBtn;
    }

    public ImageView getmLeftHandleBtn()
    {
	return mLeftHandleBtn;
    }

}
