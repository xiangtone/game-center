package com.hykj.gamecenter.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

import com.hykj.gamecenter.utils.Logger;

/**
 * 实现水平滚动条滾到两边边界线后，再拖拉到一半时，放开回弹效果 主要思路是：继承HorizontalScrollView，然后移动它里面的第一个child
 * view 不断改变它的左右边界值mInnerView.getLeft()，mInnerView.getRight()，
 * 
 * @author froyohuang
 * 
 */
public class ReboundHorizotalScrollView extends HorizontalScrollView
{

    private static final int MAX_SCROLL_Y = 200;
    private static final float SCROLL_RATIO = 0.5f;// 阻尼系数  
    /**
     * HorizontalScrollView內嵌的子view
     */
    private View mInnerView;

    /**
     * 上次移动的横坐标
     */
    private float mLastXPos;

    /**
     * 保存mInnerView正常的位置
     */
    private final Rect mNormalRect = new Rect( );

    /**
     * 处理降低上下滑动敏感度
     */
    private float mLastMotionX = -1;
    private float mLastMotionY = -1;
    private final float mAngleSlop = 1.732f;

    public ReboundHorizotalScrollView( Context context )
    {
    	super( context );
		init( );
    }

    public ReboundHorizotalScrollView( Context context , AttributeSet attrs )
    {
	super( context , attrs );
	init( );
    }

    public ReboundHorizotalScrollView( Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	init( );
    }

    private void init()
    {
    	setSmoothScrollingEnabled( false );//来设置箭头滚动是否可以引发视图滚动
    }

    /*
     * 比如你 自定义一个view叫myView ,路径是，
     * com.test.view.MyView,此view是继承LinearLayout，
     * 定义的布局文件是my_view.xml里面内容是：
     * <com.test.view.MyView>
     *         <xxxx />
     * </com.test.view.MyView>
     * 当你在使用的时候，可以这样使用
     * MyView mv = (MyView)View.inflate (context,R.layout.my_view,null);
     * 当加载完成xml后，就会执行那个方法。*/
    
    @Override
    protected void onFinishInflate()
    {
	super.onFinishInflate( );
	if( getChildCount( ) > 0 )
	{
	    mInnerView = getChildAt( 0 );// 滚动画廊 <LinearLayout  android:id="@+id/gallery_container"... /> 这个控件
	}
    }

    @Override
    public void fling( int velocityX )
    {
	/**
	 * 滑速太快，设置降速一半
	 */
	super.fling( velocityX / 2 );
    }

    @Override
    public boolean onTouchEvent( MotionEvent ev )
    {
	if( mInnerView == null )
	{
	    return super.onTouchEvent( ev );
	}
	else
	{
	    boolean ret = commOnTouchEvent( ev );
	    if( ret )
	    {
		return true;
	    }
	}
	return super.onTouchEvent( ev );
    }

    public boolean commOnTouchEvent( MotionEvent ev )
    {

	int action = ev.getAction( );
	switch ( action )
	{
	    case MotionEvent.ACTION_DOWN :
		// 重新清零，此处ev.getX()返回的值不准确
		Logger.i( "Rebound" , "mLastXPos=" + mLastXPos );
		mLastXPos = 0;// ev.getX();
		/*
		 * getX()是表示Widget相对于自身左上角的x坐标
			而getRawX()是表示相对于屏幕左上角的x坐标值
			(注意:这个屏幕左上角是手机屏幕左上角,
			不管activity是否有titleBar或是否全屏幕),
			getY(),getRawY()一样的道理
		 * */
		mLastMotionX = ev.getX( );
		mLastMotionY = ev.getY( );
		Logger.i( "Rebound" , "ACTION_DOWN mLastXPos=" + mLastXPos );
		break;
	    case MotionEvent.ACTION_UP :
		reset( );
		break;
	    case MotionEvent.ACTION_MOVE :
		float nowX = ev.getX( );
		if( mLastXPos == 0 )
		{
		    mLastXPos = nowX + 5;
		}
		final float preX = mLastXPos;

		int deltaX = (int) ( preX - nowX ) / 2;
		// 滚动
		scrollBy( deltaX / 2 , 0 );
		mLastXPos = nowX;
		// 当滚动到最上或最下时就不会再滚动，这是移动布局
		if( mInnerView != null && isNeedMove( ) )
		{
		    if( mNormalRect.isEmpty( ) )
		    {
			// 保存正常的布局位置
			mNormalRect.set( mInnerView.getLeft( ) , mInnerView.getTop( ) , mInnerView.getRight( ) , mInnerView.getBottom( ) );
		    }
		    // 移动布局
		    mInnerView.layout( mInnerView.getLeft( ) - deltaX , mInnerView.getTop( ) , mInnerView.getRight( ) - deltaX , mInnerView.getBottom( ) );
		}

		/**
		 * 这里的处理主要是降低上下滑动的敏感度
		 */
		final float xDiff = Math.abs( mLastMotionX - ev.getX( ) );
		final float yDiff = Math.abs( mLastMotionY - ev.getY( ) );
		float tan = yDiff / xDiff;
		if( tan > mAngleSlop )
		{
		    return true;
		}

		Logger.i( "Rebound" , "ACTION_MOVE mLastXPos=" + mLastXPos );
		break;
	    default :
		break;
	}

	return false;// 默认返回false，就是不对事件进行特殊处理，让父节点处理
    }

    // 开启动画移动
    public void animation()
    {
	// 开启移动动画,先执行移动动画，然后设置到正常布局位置
	TranslateAnimation ta = new TranslateAnimation( mInnerView.getLeft( ) , mNormalRect.left , 0 , 0 );
	ta.setDuration( 100 );
	mInnerView.startAnimation( ta );
	// 设置回到正常的布局位置
	if( mInnerView != null )
	{
	    Logger.i( "Rebound" , "animation mInnerView mLastXPos=" + mLastXPos );
	    mInnerView.layout( mNormalRect.left , mNormalRect.top , mNormalRect.right , mNormalRect.bottom );
	}

	Logger.i( "Rebound" , "animation mLastXPos=" + mLastXPos );
	mNormalRect.setEmpty( );
    }

    // 是否需要开启动画
    public boolean isNeedAnimation()
    {
	return !mNormalRect.isEmpty( );
    }

    // 是否需要移动布局（是否需要显示反弹效果）
    public boolean isNeedMove()
    {
	/**
	 * 加20是因为还要加上HorizontalScrollView本身的左右Padding android:paddingLeft="10px"
	 * android:paddingRight="10px"
	 */
	int offset = 0;
	if( mInnerView != null )
	{
		//getMeasuredWidth()实际的长度(有滚动条时，包括所有未显示的长度)，有可能超出屏幕
	    offset = mInnerView.getMeasuredWidth( ) - getWidth( ) + ( getPaddingLeft( ) + getPaddingRight( ) );
	}
    
	//移动到最左边或最右边
	int scrollX = getScrollX( );
		return scrollX == 0 || scrollX == offset;
	}

    public void reset()
    {
	if( isNeedAnimation( ) )
	{
	    animation( );
	    Logger.i( "Rebound" , "reset isNeedAnimation mLastXPos=" + mLastXPos );
	}
	// 重新清零
	mLastXPos = 0;
	Logger.i( "Rebound" , "reset mLastXPos=" + mLastXPos );

    }

    //    @Override
    //    protected boolean overScrollBy( int deltaX , int deltaY , int scrollX , int scrollY , int scrollRangeX , int scrollRangeY , int maxOverScrollX , int maxOverScrollY , boolean isTouchEvent )
    //    {
    //	// TODO Auto-generated method stub
    //	Logger.d( "CSScrollView" , "overScrollBy" );
    //	int newDeltaY = deltaY;
    //	int delta = (int) ( deltaY * SCROLL_RATIO );
    //	if( delta != 0 )
    //	    newDeltaY = delta;
    //
    //	return super.overScrollBy( deltaX , newDeltaY , scrollX , scrollY , scrollRangeX , scrollRangeY , maxOverScrollX , MAX_SCROLL_Y , isTouchEvent );
    //    }
    //
    //    @Override
    //    public void setOverScrollMode( int mode )
    //    {
    //	super.setOverScrollMode( View.OVER_SCROLL_ALWAYS );
    //    }

}
