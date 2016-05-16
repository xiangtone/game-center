package com.hykj.gamecenter.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utilscs.CSLibSettings;

import java.util.Locale;

public class CSPagerSlidingTabStrip extends HorizontalScrollView
{

    public interface IconTabProvider
    {
	int getPageIconResId(int position);
    }

    private static final String TAG = "PagerSlidingTabStrip";

    /** @formatter:off */
    private static final int [] ATTRS = new int [] { android.R.attr.textSize , android.R.attr.textColor };
    /** @formatter:on*/

    private final LinearLayout.LayoutParams defaultTabLayoutParams;
    private final LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener( );
    public OnPageChangeListener delegatePageListener;

    private OnPageChangedRefreshMainUIListener mOnPageChangeRefreshMainUIListener = null;
    private OnDoubleClickTabListener mOnDoubleClickTabListener = null;
    private final LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int weightSum;

    private int currentPosition = 0;
    private int cuttentDispayPosition = 0;
    private float currentPositionOffset = 0f;
    private final Paint rectPaint;
    private final Paint dividerPaint;

    private boolean checkedTabWidths = false;

    private int indicatorColor = 0xFFe83619;
    private int underlineColor = 0xFFD5D5D6;
    private int dividerColor = 0x1A000000;

    private boolean shouldExpand = false;
    private boolean textAllCaps = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 3;
    private int underlineHeight = 1;
    private int underlinePadding = 0;
    private int dividerPadding = 12;
    private int tabPadding = 12;
    private int dividerWidth = 1;

    private int tabTextSize = 20;

    private int tabTextColor = 0xFF000000;
    private int tabSelectTextColor = 0xFF02AA7C;

    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;
    private int tabBackgroundResId = R.drawable.csls_pagerslidingtabstrip_tab_bg;
    private Locale locale;

    private Runnable mTabSelector;

    //    private Path mPath;
    //    private TriangleView mTriangleView;

    // add at 20140522
    private CSTabIndicateFrameLayout tabsFrameContainer;
    //    private final ImageView mImageView;
    //    private final static int TRANSLATE_ANIMATION = 0;
    //    private final static int ANIMATION_DRAW = 1;
    //    private int mTabChangeMethod = TRANSLATE_ANIMATION;

    private Context mContext;
    private Bitmap mBitmap;

    public CSPagerSlidingTabStrip( Context context )
    {
	this( context , null );
	mContext = context;
    }

    public CSPagerSlidingTabStrip( Context context , AttributeSet attrs )
    {
	this( context , attrs , 0 );
	mContext = context;
    }

    public CSPagerSlidingTabStrip( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	mContext = context;

	setFillViewport( true );
	setWillNotDraw( false );

	tabsFrameContainer = new CSTabIndicateFrameLayout( context );

	tabsContainer = new LinearLayout( context );
	tabsContainer.setOrientation( LinearLayout.HORIZONTAL );
	tabsContainer.setLayoutParams( new LayoutParams( LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT ) );

	tabsFrameContainer.setLayoutParams( new LayoutParams( LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT ) );
	tabsFrameContainer.addView( tabsContainer );
	addView( tabsFrameContainer );

	DisplayMetrics dm = getResources( ).getDisplayMetrics( );

	scrollOffset = (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , scrollOffset , dm );
	indicatorHeight = (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , indicatorHeight , dm );
	underlineHeight = (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , underlineHeight , dm );
	dividerPadding = (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , dividerPadding , dm );
	tabPadding = (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , tabPadding , dm );
	dividerWidth = (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , dividerWidth , dm );
	tabTextSize = (int)TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_SP , tabTextSize , dm );

	TypedArray a = context.obtainStyledAttributes( attrs , ATTRS );
	tabTextColor = a.getColor( 1 , tabTextColor );

	a.recycle( );

	a = context.obtainStyledAttributes( attrs , R.styleable.CSPagerSlidingTabStrip );

	indicatorColor = a.getColor( R.styleable.CSPagerSlidingTabStrip_indicatorColor , indicatorColor );
	underlineColor = a.getColor( R.styleable.CSPagerSlidingTabStrip_underlineColor , underlineColor );
	dividerColor = a.getColor( R.styleable.CSPagerSlidingTabStrip_dividerColor , dividerColor );
	indicatorHeight = a.getDimensionPixelSize( R.styleable.CSPagerSlidingTabStrip_indicatorHeight , indicatorHeight );
	underlineHeight = a.getDimensionPixelSize( R.styleable.CSPagerSlidingTabStrip_underlineHeight , underlineHeight );
	dividerPadding = a.getDimensionPixelSize( R.styleable.CSPagerSlidingTabStrip_dividerPadding , dividerPadding );
	tabPadding = a.getDimensionPixelSize( R.styleable.CSPagerSlidingTabStrip_tabPaddingLeftRight , tabPadding );
	tabBackgroundResId = a.getResourceId( R.styleable.CSPagerSlidingTabStrip_tabBackground , tabBackgroundResId );
	shouldExpand = a.getBoolean( R.styleable.CSPagerSlidingTabStrip_shouldExpand , shouldExpand );
	scrollOffset = a.getDimensionPixelSize( R.styleable.CSPagerSlidingTabStrip_scrollOffset , scrollOffset );
	textAllCaps = a.getBoolean( R.styleable.CSPagerSlidingTabStrip_textAllCaps , textAllCaps );

	a.recycle( );

	rectPaint = new Paint( );
	rectPaint.setAntiAlias( true );
	rectPaint.setStyle( Style.FILL );

	dividerPaint = new Paint( );
	dividerPaint.setAntiAlias( true );
	dividerPaint.setStrokeWidth( dividerWidth );

	defaultTabLayoutParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT , 1.0f );
	expandedTabLayoutParams = new LinearLayout.LayoutParams( 0 , LayoutParams.MATCH_PARENT , 1.0f );

	if( locale == null )
	{
	    locale = getResources( ).getConfiguration( ).locale;
	}
    }

    public void setViewPager( ViewPager pager )
    {
	this.pager = pager;

	if( pager.getAdapter( ) == null )
	{
	    throw new IllegalStateException( "ViewPager does not have adapter instance." );
	}

	pager.setOnPageChangeListener( pageListener );

	notifyDataSetChanged( );
    }

    public void setOnPageChangeListener( OnPageChangeListener listener )
    {
	this.delegatePageListener = listener;
    }

    public void setmOnPageChangeRefreshMainUIListener( OnPageChangedRefreshMainUIListener mOnPageChangeRefreshMainUIListener )
    {
	this.mOnPageChangeRefreshMainUIListener = mOnPageChangeRefreshMainUIListener;
    }

    public void setmOnDoubleClickTabListener( OnDoubleClickTabListener mOnDoubleClickTabListener )
    {
	this.mOnDoubleClickTabListener = mOnDoubleClickTabListener;
    }

    public void notifyDataSetChanged()
    {

	tabsContainer.removeAllViews( );
	tabCount = pager.getAdapter( ).getCount( );
	weightSum = tabCount;
	tabsContainer.setWeightSum( weightSum );
	tabsFrameContainer.setTabCount( tabCount );

	for( int i = 0 ; i < tabCount ; i++ )
	{

	    if( pager.getAdapter( ) instanceof IconTabProvider )
	    {
		addIconTab( i , ( (IconTabProvider)pager.getAdapter( ) ).getPageIconResId( i ) );
	    }
	    else
	    {
		if( pager != null && pager.getAdapter( ) != null && pager.getAdapter( ).getPageTitle( i ) != null )
		    addTextTab( i , pager.getAdapter( ).getPageTitle( i ).toString( ) );

	    }

	}
	updateTabStyles( );

	checkedTabWidths = false;

	getViewTreeObserver( ).addOnGlobalLayoutListener( new OnGlobalLayoutListener( )
	{

	    @SuppressWarnings( "deprecation" )
	    @SuppressLint( "NewApi" )
	    @Override
	    public void onGlobalLayout()
	    {

//		if( Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN )
//		{
		    getViewTreeObserver( ).removeGlobalOnLayoutListener( this );
//		}
//		else
//		{
//		    getViewTreeObserver( ).removeOnGlobalLayoutListener( this );
//		}

		currentPosition = pager.getCurrentItem( );
		//		pager.getChildAt( currentPosition ).measure( getMeasuredWidth( ) , getMeasuredHeight( ) );
		scrollToChild( currentPosition , 0 );
	    }
	} );
    }

    private void addTextTab( final int position , String title )
    {

	TextView tab = new TextView( getContext( ) );
	tab.setText( title );
	tab.setFocusable( true );
	tab.setGravity( Gravity.CENTER );
	tab.setLayoutParams( defaultTabLayoutParams );
	tab.setSingleLine( );
	//	tab.setBackgroundResource( R.drawable.csl_ab_btn_green_pressed_holo_light );
	//	tab.setBackgroundColor( R.color.csl_red );

	tab.setOnClickListener( new OnClickListener( )
	{
	    @Override
	    public void onClick( View v )
	    {
		if( pager.getVisibility( ) == View.VISIBLE )
		{
		    if( isFastDoubleClick( ) && position == 1 && mOnDoubleClickTabListener != null )
		    {
			mOnDoubleClickTabListener.onDoubleClickTabToHandle( position );
		    }
		    else
		    {
			pager.setCurrentItem( position );
		    }
		}

	    }
	} );

	tabsContainer.addView( tab );
    }

    public void enabledOrNotTabs( boolean b )
    {

	for( int i = 0 ; i < tabCount ; i++ )
	{
	    View v = tabsContainer.getChildAt( i );
	    v.setEnabled( b );
	}
    }

    private void addIconTab( final int position , int resId )
    {

	ImageButton tab = new ImageButton( getContext( ) );
	tab.setFocusable( true );
	tab.setImageResource( resId );

	tab.setOnClickListener( new OnClickListener( )
	{
	    @Override
	    public void onClick( View v )
	    {
		pager.setCurrentItem( position );
	    }
	} );

	tabsContainer.addView( tab );

    }

    private void updateTabStyles()
    {

	//	LogUtils.e( "tabCount=" + tabCount );

	for( int i = 0 ; i < tabCount ; i++ )
	{

	    View v = tabsContainer.getChildAt( i );

	    //	    LogUtils.e( "i=" + i + "|v=" + ( v == null ? "" : v ) );
	    if( v == null )
		continue;

	    v.setLayoutParams( defaultTabLayoutParams );
	    v.setBackgroundResource( tabBackgroundResId );
	    if( shouldExpand )
	    {
		v.setPadding( 0 , 0 , 0 , 0 );
	    }
	    else
	    {
		v.setPadding( tabPadding , 0 , tabPadding , 0 );
	    }

	    if( v instanceof TextView )
	    {

		TextView tab = (TextView)v;
		tab.setTextSize( TypedValue.COMPLEX_UNIT_PX , tabTextSize );
		tab.setTypeface( tabTypeface , tabTypefaceStyle );
		tab.setTextColor( tabTextColor );
		if( i == cuttentDispayPosition )
		{
		    tab.setTextColor( tabSelectTextColor );
		}
	    }

	}

    }

    @Override
    protected void onMeasure( int widthMeasureSpec , int heightMeasureSpec )
    {
	super.onMeasure( widthMeasureSpec , heightMeasureSpec );

	if( !shouldExpand || MeasureSpec.getMode( widthMeasureSpec ) == MeasureSpec.UNSPECIFIED )
	{
	    return;
	}
	int myWidth = getMeasuredWidth( );
	int childWidth = 0;
	for( int i = 0 ; i < tabCount ; i++ )
	{
	    childWidth += tabsContainer.getChildAt( i ).getMeasuredWidth( );
	}

	if( !checkedTabWidths && childWidth > 0 && myWidth > 0 )
	{

	    if( childWidth <= myWidth )
	    {
		for( int i = 0 ; i < tabCount ; i++ )
		{
		    tabsContainer.getChildAt( i ).setLayoutParams( expandedTabLayoutParams );
		}
	    }

	    checkedTabWidths = true;
	}
    }

    private void scrollToChild( int position , int offset )
    {

	//	long start = System.currentTimeMillis( );
	//	Logger.i( TAG , "start time:" + start );

	if( tabCount == 0 || tabsContainer == null || tabsContainer.getChildAt( position ) == null )
	{
	    return;
	}
	int newScrollX = tabsContainer.getChildAt( position ).getLeft( ) + offset;

	if( position > 0 || offset > 0 )
	{
	    newScrollX -= scrollOffset;
	}

	if( newScrollX != lastScrollX )
	{
	    lastScrollX = newScrollX;
	    //	    scrollTo( newScrollX , 0 );
	    final int newScrollX2 = newScrollX;
	    //	    mTabSelector = new Runnable( )
	    //	    {
	    //		@Override
	    //		public void run()
	    //		{
	    scrollTo( newScrollX2 , 0 );
	    //		    mTabSelector = null;
	    //		}
	    //	    };
	    //	    post( mTabSelector );
	    //	    long end = System.currentTimeMillis( );
	    //	    Logger.i( TAG , "end time:" + end );
	    //	    Logger.i( TAG , "result time:" + ( end - start ) );
	}

    }

    @Override
    protected void onDraw( final Canvas canvas )
    {
	super.onDraw( canvas );

	if( isInEditMode( ) || tabCount == 0 )
	{
	    return;
	}

	final int height = getHeight( );

	// draw indicator line

	rectPaint.setColor( indicatorColor );

	// default: line below current tab
	View currentTab = tabsContainer.getChildAt( currentPosition );
	float lineLeft = currentTab.getLeft( );

	//LogUtils.e( "lineLeft =" + lineLeft );

	float lineRight = currentTab.getRight( );

	// if there is an offset, start interpolating left and right coordinates between current and next tab
	if( currentPositionOffset > 0f && currentPosition < tabCount - 1 )
	{

	    View nextTab = tabsContainer.getChildAt( currentPosition + 1 );
	    final float nextTabLeft = nextTab.getLeft( );
	    final float nextTabRight = nextTab.getRight( );

	    lineLeft = ( currentPositionOffset * nextTabLeft + ( 1f - currentPositionOffset ) * lineLeft );
	    lineRight = ( currentPositionOffset * nextTabRight + ( 1f - currentPositionOffset ) * lineRight );
	}

	float left = lineLeft + underlinePadding;
	if( CSLibSettings.ACTION_BAR_INDICATE_TYPE == 1 )
	{
	    left = lineLeft;
	}
	final float top = height - indicatorHeight;
	final float right = lineRight - underlinePadding;
	final float buttom = height;

	tabsFrameContainer.setPos( left , top , right , buttom , indicatorColor );

	rectPaint.setColor( underlineColor );
	if( CSLibSettings.ACTION_BAR_DRAW_UNDERLINE )
	{
	    canvas.drawRect( 0 , height - underlineHeight , tabsContainer.getWidth( ) , height , rectPaint );
	}
	// draw divider

	dividerPaint.setColor( dividerColor );
	//
	for( int i = 0 ; i < tabCount - 1 ; i++ )
	{
	    View tab = tabsContainer.getChildAt( i );
	    if( tab == null )
		return;
	    canvas.drawLine( tab.getRight( ) , dividerPadding , tab.getRight( ) , height - dividerPadding , dividerPaint );
	}
    }

    private class PageListener implements OnPageChangeListener
    {

	@Override
	public void onPageScrolled( int position , float positionOffset , int positionOffsetPixels )
	{

	    currentPosition = position;
	    currentPositionOffset = positionOffset;

	    scrollToChild( position , (int) ( positionOffset * tabsContainer.getChildAt( position ).getWidth( ) ) );

	    invalidate( );

	    if( delegatePageListener != null )
	    {
		delegatePageListener.onPageScrolled( position , positionOffset , positionOffsetPixels );
	    }

	}

	@Override
	public void onPageScrollStateChanged( int state )
	{
	    if( state == ViewPager.SCROLL_STATE_IDLE )
	    {
		scrollToChild( pager.getCurrentItem( ) , 0 );
	    }

	    if( delegatePageListener != null )
	    {
		delegatePageListener.onPageScrollStateChanged( state );
	    }

	}

	@Override
	public void onPageSelected( int position )
	{
	    if( delegatePageListener != null )
	    {
		delegatePageListener.onPageSelected( position );
	    }
	    if( mOnPageChangeRefreshMainUIListener != null )
	    {
		mOnPageChangeRefreshMainUIListener.onPageChangedRefreshMainUI( pager.getCurrentItem( ) );
	    }

	    cuttentDispayPosition = position;
	    updateTabStyles( );

	}
    }

    public void setIndicatorColor( int indicatorColor )
    {
	this.indicatorColor = indicatorColor;
	invalidate( );
    }

    public void setIndicatorColorResource( int resId )
    {
	this.indicatorColor = getResources( ).getColor( resId );
	invalidate( );
    }

    public int getIndicatorColor()
    {
	return this.indicatorColor;
    }

    public void setIndicatorHeight( int indicatorLineHeightPx )
    {
	this.indicatorHeight = indicatorLineHeightPx;
	invalidate( );
    }

    public int getIndicatorHeight()
    {
	return indicatorHeight;
    }

    public void setUnderlineColor( int underlineColor )
    {
	this.underlineColor = underlineColor;
	invalidate( );
    }

    public void setUnderlineColorResource( int resId )
    {
	this.underlineColor = getResources( ).getColor( resId );
	invalidate( );
    }

    public void setTabSelectTextColorResource( int resId )
    {
	this.tabSelectTextColor = getResources( ).getColor( resId );
	updateTabStyles( );
    }

    public int getUnderlineColor()
    {
	return underlineColor;
    }

    public void setDividerColor( int dividerColor )
    {
	this.dividerColor = dividerColor;
	invalidate( );
    }

    public void setDividerColorResource( int resId )
    {
	this.dividerColor = getResources( ).getColor( resId );
	invalidate( );
    }

    public int getDividerColor()
    {
	return dividerColor;
    }

    public void setUnderlineHeight( int underlineHeightPx )
    {
	this.underlineHeight = underlineHeightPx;
	invalidate( );
    }

    public int getUnderlineHeight()
    {
	return underlineHeight;
    }

    public void setDividerPadding( int dividerPaddingPx )
    {
	this.dividerPadding = dividerPaddingPx;
	invalidate( );
    }

    public int getDividerPadding()
    {
	return dividerPadding;
    }

    public void setUnderlinePadding( int underlinePadding )
    {
	this.underlinePadding = underlinePadding;
	invalidate( );
    }

    public void setScrollOffset( int scrollOffsetPx )
    {
	this.scrollOffset = scrollOffsetPx;
	invalidate( );
    }

    public int getScrollOffset()
    {
	return scrollOffset;
    }

    public void setShouldExpand( boolean shouldExpand )
    {
	this.shouldExpand = shouldExpand;
	requestLayout( );
    }

    public boolean getShouldExpand()
    {
	return shouldExpand;
    }

    public boolean isTextAllCaps()
    {
	return textAllCaps;
    }

    public void setAllCaps( boolean textAllCaps )
    {
	this.textAllCaps = textAllCaps;
    }

    public void setTextSize( int textSizePx )
    {
	this.tabTextSize = textSizePx;
	updateTabStyles( );
    }

    public int getTextSize()
    {
	return tabTextSize;
    }

    public void setTextColor( int textColor )
    {
	this.tabTextColor = textColor;
	updateTabStyles( );
    }

    public void setTextColorResource( int resId )
    {
	this.tabTextColor = getResources( ).getColor( resId );
	updateTabStyles( );
    }

    public int getTextColor()
    {
	return tabTextColor;
    }

    public void setTypeface( Typeface typeface , int style )
    {
	this.tabTypeface = typeface;
	this.tabTypefaceStyle = style;
	updateTabStyles( );
    }

    public void setTabBackground( int resId )
    {
	this.tabBackgroundResId = resId;
    }

    public int getTabBackground()
    {
	return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight( int paddingPx )
    {
	this.tabPadding = paddingPx;
	updateTabStyles( );
    }

    public int getTabPaddingLeftRight()
    {
	return tabPadding;
    }

    @Override
    public void onRestoreInstanceState( Parcelable state )
    {
	SavedState savedState = (SavedState)state;
	super.onRestoreInstanceState( savedState.getSuperState( ) );
	currentPosition = savedState.currentPosition;
	requestLayout( );
    }

    @Override
    public Parcelable onSaveInstanceState()
    {
	Parcelable superState = super.onSaveInstanceState( );
	SavedState savedState = new SavedState( superState );
	savedState.currentPosition = currentPosition;
	return savedState;
    }

    static class SavedState extends BaseSavedState
    {
	int currentPosition;

	public SavedState( Parcelable superState )
	{
	    super( superState );
	}

	private SavedState( Parcel in )
	{
	    super( in );
	    currentPosition = in.readInt( );
	}

	@Override
	public void writeToParcel( Parcel dest , int flags )
	{
	    super.writeToParcel( dest , flags );
	    dest.writeInt( currentPosition );
	}

	public static final Parcelable.Creator< SavedState > CREATOR = new Parcelable.Creator< SavedState >( )
	{
	    @Override
	    public SavedState createFromParcel( Parcel in )
	    {
		return new SavedState( in );
	    }

	    @Override
	    public SavedState [] newArray( int size )
	    {
		return new SavedState [size];
	    }
	};
    }

    public interface OnPageChangedRefreshMainUIListener
    {
	void onPageChangedRefreshMainUI( int position );
    }

    public interface OnDoubleClickTabListener
    {
	void onDoubleClickTabToHandle( int position );
    }

    private static long lastClickTime = 0;

    public static synchronized boolean isFastDoubleClick()
    {
	long time = System.currentTimeMillis( );
	long timeD = time - lastClickTime;
	if( 0 < timeD && timeD < 500 )
	{
	    return true;
	}
	lastClickTime = time;
	return false;
    }

}
