package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utilscs.CSLibSettings;

public class CSDownloadManageActionBar extends CSBaseActionBar implements OnClickListener
{

    private CharSequence mTitle;
    private TextView mTitleView = null;

    //    private CharSequence mHomeUpTitle;
    //    private TextView mHomeUpTitleView = null;
    //    private ImageView mHomeUpImageView = null;

    private PagerSlidingTabStrip mCSPagerSlidingTabStrip;

    private ImageView mLogoImageView = null;

    private TextView mLogoTextView;

    private CharSequence mLogoTitle;
    private int mLogoIcon;

    private TextView mTipView = null;

    private ImageView mDownloadButton = null;
    private ImageView mSettingButton = null;
    private ImageView mSettingTipView = null;
    private ImageView mSearchButton = null;

    private Button mLeftEditButton = null;
    private ImageView mReturnButton = null;
    private ImageView mRightEditButton = null;
    private ImageView mRightEditIcon = null;

    private boolean mShowTitle = false;
    //    private boolean mShowHomeUpTitle = false;
    private boolean mShowLogo = false;
    private boolean mShowSetting = true;
    private boolean mShowManage = true;
    private boolean mShowSearch = false;
    private boolean mShowLogoTitle = true;

    // add 
    private boolean mShowLeftEdit = false;
    private boolean mShowRightEdit = false;
    private boolean mShowReturn = false;
    private boolean mShowRightEditButton = false;
    private boolean mShowRightEditIcon = false;

    private int mDownloadBtnLeft = 0;

    private Context mContext;

    private OnActionBarClickListener mOnActionBarListener = null;

    public CSDownloadManageActionBar( Context context )
    {
	this( context , null );
    }

    public CSDownloadManageActionBar( Context context , AttributeSet attrs )
    {
	this( context , attrs , R.attr.plCSActionBarStyle );
    }

    public CSDownloadManageActionBar( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	mContext = context;

	TypedArray a = context.obtainStyledAttributes( attrs , R.styleable.CSCommonActionBar , defStyle , 0 );

	mTitle = a.getString( R.styleable.CSCommonActionBar_comm_title );

	mShowTitle = a.getBoolean( R.styleable.CSCommonActionBar_comm_showTitle , false );
	//	mShowHomeUpTitle = a.getBoolean( R.styleable.GameActionBar_gc_showHomeUpTitle , false );
	mShowLogo = a.getBoolean( R.styleable.CSCommonActionBar_comm_showLogo , CSLibSettings.ACTION_BAR_SHOW_LOGO_ICON );
	mShowLogoTitle = a.getBoolean( R.styleable.CSCommonActionBar_comm_showLogoTitle , CSLibSettings.ACTION_BAR_SHOW_LOGO_TITLE );
	mLogoTitle = a.getString( R.styleable.CSCommonActionBar_comm_logoTitle );
	mLogoIcon = a.getResourceId( R.styleable.CSCommonActionBar_comm_logoIcon , R.drawable.csl_comm_actionbar_logo );

	mShowSetting = a.getBoolean( R.styleable.CSCommonActionBar_comm_showSetting , false );
	mShowManage = a.getBoolean( R.styleable.CSCommonActionBar_comm_showManage , false );
	mShowSearch = a.getBoolean( R.styleable.CSCommonActionBar_comm_showSearch , false );

	mShowReturn = a.getBoolean( R.styleable.CSCommonActionBar_comm_showReturn , false );
	mShowLeftEdit = a.getBoolean( R.styleable.CSCommonActionBar_comm_showLeftEdit , false );
	mShowRightEdit = a.getBoolean( R.styleable.CSCommonActionBar_comm_showRightEdit , false );

	mShowRightEditButton = a.getBoolean( R.styleable.CSCommonActionBar_comm_showRightEditButton , false );
	mShowRightEditIcon = a.getBoolean( R.styleable.CSCommonActionBar_comm_showRightEditIcon , false );
	//	LayoutInflater.from( context ).inflate( R.layout.csl_cs_comm_actionbar , this );
	a.recycle( );
    }

    private void addSearchIcon( ViewGroup container )
    {
	View iconView = LayoutInflater.from( mContext ).inflate( R.layout.csl_cs_comm_actionbar_search , container );
	//container.addView(iconView);

	mSearchButton = (ImageView)iconView.findViewById( R.id.search_icon );
	mSearchButton.setTag( OnActionBarClickListener.SEARCH_BNT );
	mSearchButton.setOnClickListener( this );
    }

    private void addManangerIcon( ViewGroup container )
    {
	View iconView = LayoutInflater.from( mContext ).inflate( R.layout.csl_cs_comm_actionbar_manage , container );
	//container.addView(iconView);

	mDownloadButton = (ImageView)iconView.findViewById( R.id.manage_icon );
	mTipView = (TextView)findViewById( R.id.tip_accoute_view );

	mDownloadButton.setTag( OnActionBarClickListener.MANAGE_BNT );
	mDownloadButton.setOnClickListener( this );

    }

    public int getDownLoadBtnLeft()
    {
	return mDownloadBtnLeft;
    }

    private void addPagerSlidingTabStrip( ViewGroup container )
    {
	View tabView = LayoutInflater.from( mContext ).inflate( R.layout.csl_cs_pager_sliding_tab_strip , container );
	//container.addView(iconView);
	mCSPagerSlidingTabStrip = (PagerSlidingTabStrip)tabView.findViewById( R.id.tab_strip );
	initPagerSlidingTabStrip( );

    }

    private void addReturnIcon( ViewGroup container )
    {
	View iconView = LayoutInflater.from( mContext ).inflate( R.layout.csl_cs_comm_actionbar_left_return , container );
	//container.addView(iconView);

	mReturnButton = (ImageView)iconView.findViewById( R.id.return_icon );
	mReturnButton.setTag( OnActionBarClickListener.RETURN_BNT );
	mReturnButton.setOnClickListener( this );

	iconView.findViewById( R.id.return_icon ).setTag( OnActionBarClickListener.RETURN_BNT );
	iconView.findViewById( R.id.return_icon ).setOnClickListener( this );

    }

    private void addLeftEditIcon( ViewGroup container )
    {
	View iconView = LayoutInflater.from( mContext ).inflate( R.layout.csl_cs_comm_actionbar_left_edit , container );
	//container.addView(iconView);
	//TODO
	mLeftEditButton = (Button)iconView.findViewById( R.id.left_icon );
	mLeftEditButton.setTag( OnActionBarClickListener.LEFT_BNT );
	mLeftEditButton.setVisibility( View.GONE );
	mLeftEditButton.setOnClickListener( this );
    }

    private void addRightEditIcon( ViewGroup container )
    {
	View iconView = LayoutInflater.from( mContext ).inflate( R.layout.csl_cs_comm_actionbar_right_edit , container );
	//container.addView(iconView);

	mRightEditIcon = (ImageView)iconView.findViewById( R.id.right_icon );
	mRightEditIcon.setTag( OnActionBarClickListener.RIGHT_EDIT_ICON );
	mRightEditIcon.setOnClickListener( this );

	mRightEditButton = (ImageView)iconView.findViewById( R.id.right_button );
	mRightEditButton.setTag( OnActionBarClickListener.RIGHT_EDIT_BNT );
	mRightEditButton.setOnClickListener( this );

	if( mShowRightEditButton )
	{
	    mRightEditButton.setVisibility( View.VISIBLE );
	}

	if( mShowRightEditIcon )
	{
	    mRightEditIcon.setVisibility( View.VISIBLE );
	}
    }

    private void addSettingIcon( ViewGroup container )
    {
	View iconView = LayoutInflater.from( mContext ).inflate( R.layout.csl_cs_comm_actionbar_settting , container );
	//	container.addView( iconView );

	mSettingTipView = (ImageView)iconView.findViewById( R.id.setting_tip_view );
	mSettingButton = (ImageView)iconView.findViewById( R.id.setting_icon );
	mSettingButton.setTag( OnActionBarClickListener.SETTING_BNT );
	mSettingButton.setOnClickListener( this );
    }

    //    private void addDivider( ViewGroup container )
    //    {
    //	LayoutInflater.from( mContext ).inflate( R.layout.icon_vertical_divider , container );
    //    }

    @Override
    protected void onFinishInflate()
    {
	super.onFinishInflate( );

	//	mTitleView = (TextView)findViewById( R.id.actionbar_title );
	//
	//	//	mHomeUpTitleView = (TextView)findViewById( R.id.actionbar_homeup_title );
	//	//	mHomeUpImageView = (ImageView)findViewById( R.id.actionbar_homeup_image );
	//	//	mHomeUpImageView.setTag( OnActionBarClickListener.HOMEUP_BNT );
	//	mLogoImageView = (ImageView)findViewById( R.id.logo );
	//
	//	mLogoTextView = (TextView)findViewById( R.id.logo_text );
	//
	//	if( mShowTitle )
	//	{
	//	    mTitleView.setText( mTitle );
	//	    mTitleView.setVisibility( VISIBLE );
	//	}
	//	else
	//	{
	//	    mTitleView.setVisibility( INVISIBLE );
	//	}
	//
	//	//	if( mShowHomeUpTitle )
	//	//	{
	//	//	    mHomeUpTitleView.setText( mHomeUpTitle );
	//	//	    mHomeUpImageView.setVisibility( VISIBLE );
	//	//	    mHomeUpImageView.setOnClickListener( this );
	//	//	    mHomeUpImageView.setTag( OnActionBarClickListener.HOMEUP_BNT );
	//	//	    mHomeUpTitleView.setVisibility( VISIBLE );
	//	//	    mHomeUpTitleView.setOnClickListener( this );
	//	//	    mHomeUpTitleView.setTag( OnActionBarClickListener.HOMEUP_BNT );
	//	//	}
	//	//	else
	//	//	{
	//	//	    mHomeUpImageView.setVisibility( GONE );
	//	//	    mHomeUpTitleView.setVisibility( GONE );
	//	//	}
	//
	//	mLogoImageView.setVisibility( mShowLogo ? VISIBLE : GONE );
	//	mLogoImageView.setImageResource( mLogoIcon );
	//	mLogoTextView.setVisibility( mShowLogoTitle ? VISIBLE : GONE );
	//	mLogoTextView.setText( mLogoTitle );
	//
	//	ViewGroup iconContainer = (ViewGroup)findViewById( R.id.right_function_icons );
	//
	//	if( mShowSearch )
	//	{
	//	    addSearchIcon( iconContainer );
	//	}
	//	if( mShowManage )
	//	{
	//	    //	    if( mShowSearch )
	//	    //	    {
	//	    //		addDivider( iconContainer );
	//	    //	    }
	//	    addManangerIcon( iconContainer );
	//	}
	//	if( mShowSetting )
	//	{
	//	    //	    if( mShowSearch || mShowManage )
	//	    //	    {
	//	    //		addDivider( iconContainer );
	//	    //	    }
	//	    addSettingIcon( iconContainer );
	//	}
	//	ViewGroup leftIconContainer = (ViewGroup)findViewById( R.id.left_function_icons );
	//	if( mShowLeftEdit )
	//	{
	//	    addLeftEditIcon( leftIconContainer );
	//	}
	if( mShowRightEdit )
	{
	    addRightEditIcon( mRightContainer );
	}
	if( mShowReturn )
	{
	    addReturnIcon( mLeftContainer );
	}
	addPagerSlidingTabStrip( mMiddleContainer );
	//	final int [] location = new int [2];
	//	mDownloadButton.getLocationInWindow( location );
	//	mDownloadBtnLeft = location[0];
	//	LogUtils.e( "mDownloadBtnLeft=" + mDownloadBtnLeft );
    }

    @Override
    public void onClick( View view )
    {
	if( mOnActionBarListener == null )
	    return;

	//	if( UITools.isFastDoubleClick( ) )
	//	    return;

	mOnActionBarListener.onActionBarClicked( (Integer)view.getTag( ) );
    }

    public void SetOnActionBarClickListener( OnActionBarClickListener listener )
    {
	mOnActionBarListener = listener;
    }

    public interface OnActionBarClickListener
    {
	//	public static final int HOMEUP_BNT = 0x01;
    int MANAGE_BNT = 0x02;
	int SETTING_BNT = 0x03;
	int SEARCH_BNT = 0x04;
	int RETURN_BNT = 0x05;
	int LEFT_BNT = 0x06;
	int RIGHT_EDIT_BNT = 0x07;
	int RIGHT_EDIT_ICON = 0x08;

	void onActionBarClicked( int position );
    }

    public void setTitle( CharSequence title )
    {
	mTitle = title;
	if( mTitleView != null )
	    mTitleView.setText( title );
    }

    public void setLogoTitle( CharSequence title )
    {
	mLogoTitle = title;
	if( mLogoTextView != null )
	    mLogoTextView.setText( title );
    }

    public void setManageTipVisible( boolean visibility , int size )
    {
	if( mTipView != null )
	{
	    mTipView.setVisibility( visibility ? View.VISIBLE : View.GONE );
	    mTipView.setText( size + "" );
	}

    }

    public TextView getmTitleView()
    {
	return mTitleView;
    }

    public void setSettingTipVisible( int visibility )
    {
	if( mSettingTipView != null )
	{
	    mSettingTipView.setVisibility( visibility );
	}

    }

    public Button getmLeftEditButton()
    {
	int padding = dpTopx( mContext , 14 );
	mLeftEditButton.setPadding( padding , 0 , padding , 0 );
	return mLeftEditButton;
    }

    public void setmLogoTextView( TextView mLogoTextView )
    {
	this.mLogoTextView = mLogoTextView;
    }

    public TextView getmLogoTextView()
    {
	return mLogoTextView;
    }

    public ImageView getmReturnButton()
    {
	return mReturnButton;
    }

    public ImageView getmRightEditIcon()
    {
	//	int padding = dpTopx( mContext , 14 );
	//	mRightEditButton.setPadding( padding , 0 , padding , 0 );
	return mRightEditIcon;
    }

    public ImageView getmRightEditButton()
    {
	//	int padding = dpTopx( mContext , 14 );
	//	mRightEditButton.setPadding( padding , 0 , padding , 0 );
	return mRightEditButton;
    }

    //dp to px 
    public int dpTopx( Context context , int dpValue )
    {
	DisplayMetrics dm = context.getApplicationContext( ).getResources( ).getDisplayMetrics( );
	return (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , dpValue , dm );
    }

    private void initPagerSlidingTabStrip()
    {
	mCSPagerSlidingTabStrip.setUnderlineColorResource( R.color.csl_tab_underline_color );
	mCSPagerSlidingTabStrip.setIndicatorColorResource( R.color.csl_tab_indicator_color );
	mCSPagerSlidingTabStrip.setDividerColorResource( R.color.csl_tab_divider_color );

	int hight = getResources( ).getDimensionPixelSize( R.dimen.csl_tab_actionbar_divider );
	mCSPagerSlidingTabStrip.setDividerPadding( hight );
	int width = getResources( ).getDimensionPixelSize( R.dimen.csl_tab_actionbar_underline_width );
    }

    public PagerSlidingTabStrip getPagerSlidingTabStrip()
    {
	return mCSPagerSlidingTabStrip;
    }
}
