package com.hykj.gamecenter.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.UITools;

/**
 * 类似桌面的workspace控件
 * 
 * @author greatzhang
 */
public class HorizonScrollLayout extends ViewGroup {
	public static float RATIO_Y_X = 2.5f;
	public static int RATIO_Y = 3;
	public static int RATIO_X = 2;
	public static int RATIO_PAD = 5;
	private static final String TAG = "HorizonScrollLayout";
	private Scroller mScroller;
	// 计算速度
	private VelocityTracker mVelocityTracker;

	private int mCurScreen = 0;
	private int mDefaultScreen = 0;
	private int mChildScreenWidth = 0;
	private int mScreenWidth = 0;

	// 休息状态
	private static final int TOUCH_STATE_REST = 0;
	// 滑动状态
	private static final int TOUCH_STATE_SCROLLING = 1;
	// 锁定状态
	private static final int TOUCH_STATE_LOCK = 2;

	// 快速的速度
	private static final int SNAP_VELOCITY = 600;

	private Resources mRes = null;
	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop = 24; //
	private float mAngleSlop = 0.577f; // tan(a) = x/y , 滑动的角度30
	private float mLastMotionX;
	private float mLastMotionY;

	private float mMinScrollX = 0;
	private float mMaxScrollX = 0;

	private float mPreScrollX = 0;

	// 是否可以划出边界
	private boolean mEnableOverScroll = true;
	// private float mDurationCorr = (float) (Math.PI/1000.0f); // 时间的调和数
	// 屏幕状态
	private int mScrollState = OnTouchScrollListener.SCROLL_STATE_IDLE;

	// 是否禁止滚动标记
	private boolean mEnableScroll = true;

	// 是否锁定
	private boolean mLockAllWhenTouch = false;

	// private int mWidth = 0;

	// 状态回调
	private OnTouchScrollListener mTouchScrollListener = null;

	private static final int INVALID_SCREEN = -999;
	private int mNextScreen = INVALID_SCREEN;

	// private LinkedList< View > mViewList = new LinkedList< View >( );
	// private LinkedList< Integer > mIntegerList = new LinkedList< Integer >(
	// );
	// private int mPos = 0;
	// private View mCacheView = null;

	int mScrollX;
	int mScrollY;
	// 圆周
	boolean mIsCircle = false;
	boolean mChangeCoordinate = true;

	public final static int NORMAL = 10000;
	public final static int LEFT = 10001;
	public final static int RIGHT = 10002;

	private final int mScrollOrientation = NORMAL;

	/**
	 * 屏幕滚动回调
	 * 
	 * @author albertzhong
	 * 
	 */
	public interface OnTouchScrollListener {
		// scroll state
		// 闲置状态
		int SCROLL_STATE_IDLE = 0;
		int SCROLL_STATE_TOUCH_SCROLL = 1;
		int SCROLL_STATE_FLING = 2;

		/**
		 * 当前显示的屏幕改变
		 * 
		 * @param displayScreem
		 *            :显示的屏幕
		 */
		void onScreenChange(int displayScreem, Object obj);

		/**
		 * 屏幕当前滚动的位置
		 * 
		 * @param leftX
		 *            :当前屏左边的位置
		 * @param screemWidth
		 *            :屏幕的总宽度
		 */
		void onScroll(View view, float leftX, float screemWidth);

		/**
		 * 滚动状态
		 * 
		 * @param scrollState
		 * @param currentScreem
		 */
		void onScrollStateChanged(int scrollState, int currentScreem);
	}

	public HorizonScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mRes = context.getResources();
	}

	public HorizonScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mRes = context.getResources();
		mScroller = new Scroller(context);

		mCurScreen = mDefaultScreen;
		// LogUtils.e( "HorizonScrollLayout,mCurScreen = " + mCurScreen );
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		// LogUtils.e( "HorizonScrollLayout,mTouchSlop = " + mTouchSlop );

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.HorizonScrollLayout);

		// 获取自定义属性和默认值
		RATIO_X = mTypedArray.getInteger(
				R.styleable.HorizonScrollLayout_RATIO_X, 3);
		RATIO_Y = mTypedArray.getInteger(
				R.styleable.HorizonScrollLayout_RATIO_Y, 4);

		mTypedArray.recycle();

	}

	/**
	 * 设置是否可以滑动出边界
	 * 
	 * @param enable
	 */
	public void setEnableOverScroll(boolean enable) {
		mEnableOverScroll = enable;
	}

	/**
	 * 设置水平滑动的响应角度的水平值:tan(30);
	 * 
	 * @param slop
	 */
	public void setScrollSlop(float slop) {
		mAngleSlop = slop;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		// LogUtils.e( "onLayout" );
		int childLeft = 0;
		final int childCount = getChildCount();
		// for( int i = 0 ; i < childCount ; i++ )
		// {
		// final View childView = getChildAt( i );
		// if( childView.getVisibility( ) != View.GONE )
		// {
		// int childWidth = childView.getMeasuredWidth( );
		//
		// if( !UITools.isPortrait( ) )
		// {
		// childWidth = childWidth * RATIO_X / RATIO_Y;
		// }
		// // 如果屏幕大小末发生变化, 只对当前屏幕重新布局, 否则全部布局
		// if( changed == false )
		// {
		// if( i == mCurScreen )
		// {
		// childView.layout( childLeft , 0 , childLeft + childWidth ,
		// childView.getMeasuredHeight( ) );
		// //刷新
		// childView.postInvalidate( );
		// break;
		// }
		// }
		// else
		// {
		// childView.layout( childLeft , 0 , childLeft + childWidth ,
		// childView.getMeasuredHeight( ) );
		// childView.postInvalidate( );
		// }
		// childLeft += childWidth;
		// }
		// }

		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			int childWidth = childView.getMeasuredWidth();
			if (!UITools.isPortrait()) {
				childWidth = (int) ((childWidth * RATIO_X * 1.0f / RATIO_Y) + 0.5f);
			}
			// 如果屏幕大小末发生变化, 只对当前屏幕重新布局, 否则全部布局
			// if( changed == false )
			// {
			// if( i == mCurScreen )
			// {
			// childView.layout( childLeft , 0 , childLeft + childWidth ,
			// childView.getMeasuredHeight( ) );
			// //刷新
			// childView.postInvalidate( );
			// break;
			// }
			// }
			// else
			// {
			childView.layout(childLeft, 0, childLeft + childWidth,
					childView.getMeasuredHeight());
			childView.postInvalidate();
			// }
			childLeft += childWidth;
		}

		if (mCurScreen == getChildCount() - 1 && !UITools.isPortrait()) {
			final View childViewExtra = getChildAt(0);
			int childWidth = childViewExtra.getMeasuredWidth();// view中内容的宽度
			if (!UITools.isPortrait()) {
				childWidth = (int) ((childWidth * RATIO_X * 1.0f / RATIO_Y) + 0.5f);
			}
			/*
			 * public void layout(int l, int t, int r, int b);
			 * 
			 * 该方法是View的放置方法，在View类实现。 调用该方法需要传入放置View的矩形空间左上角left、top值和
			 * 右下角right、bottom值。这四个值是相对于父控件而言的。 例如传入的是（10, 10, 100, 100），
			 * 则该View在距离父控件的左上角位置(10, 10)处显示， 显示的大小是宽高是90(参数r,b是相对左上角的)，
			 * 这有点像绝对布局。
			 */
			// 设置该view在父控件(viewGroup)的放置位置
			childViewExtra.layout(childLeft, 0, childLeft + childWidth,
					childViewExtra.getMeasuredHeight());
			childViewExtra.postInvalidate();
		}

		if (mCurScreen == getChildCount() - 2 && mScrollOrientation == LEFT
				&& !UITools.isPortrait()) {
			final View childViewExtra = getChildAt(0);
			int childWidth = childViewExtra.getMeasuredWidth();
			if (!UITools.isPortrait()) {
				childWidth = (int) ((childWidth * RATIO_X * 1.0f / RATIO_Y) + 0.5f);
			}
			// 设置该view在父控件(viewGroup)的放置位置
			childViewExtra.layout(childLeft, 0, childLeft + childWidth,
					childViewExtra.getMeasuredHeight());
			childViewExtra.postInvalidate();
		}

		// 重新计算时间参数
		// if( changed){
		// mDurationCorr = (float) (Math.PI / ((getChildCount()-1) *
		// getWidth()));
		// }

		// 限制滑动范围
		mChildScreenWidth = getWidth(); // 子屏幕宽度
		if (!UITools.isPortrait()) {
			mChildScreenWidth = (int) ((mChildScreenWidth * RATIO_X * 1.0f / RATIO_Y) + 0.5f); // 子屏幕宽度
		}
		// LogUtils.e( "限制滑动范围 =" + mChildScreenWidth );
		mScreenWidth = mChildScreenWidth * childCount; // 屏幕总宽度
		if (mEnableOverScroll) {
			mMinScrollX = -(mChildScreenWidth >> 2); // 向左移动范围
			mMaxScrollX = mScreenWidth - mChildScreenWidth - mMinScrollX; // 向右移动范围
		} else {
			mMinScrollX = 0;
			mMaxScrollX = mScreenWidth - mChildScreenWidth;
		}
		// LogUtils.e( "mMinScrollX = " + mMinScrollX );
		// LogUtils.e( "mMaxScrollX = " + mMaxScrollX );

		// 区域改变后,重新移动
		if (changed == true) {
			// 解决横竖屏切换位置不正确问题
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			if (UITools.isPortrait()) {
				scrollTo(mCurScreen * getWidth(), 0);
			} else {
				scrollTo((int) (mCurScreen * getWidth() * RATIO_X * 1.0f
						/ RATIO_Y + 0.5f), 0);
			}

		}
	}

	// 重新定义位置
	// public void changeIntegerList( boolean b )
	// {
	//
	// if( mIntegerList.size( ) > 0 )
	// {
	// if( b )
	// {
	// Integer first = mIntegerList.getFirst( );
	// mIntegerList.removeFirst( );
	// mIntegerList.add( first );
	// }
	// else
	// {
	// Integer last = mIntegerList.getLast( );
	// mIntegerList.removeLast( );
	// mIntegerList.addFirst( last );
	// }
	// }
	//
	// for( int i = 0 ; i < mIntegerList.size( ) ; i++ )
	// {
	// LogUtils.e( "changeIntegerList,mIntegerList.get( i ) = " +
	// mIntegerList.get( i ) );
	// }
	// }

	/**
	 * 设置到第几个
	 * 
	 * @param child
	 */
	public void layoutChild(int child) {
		// LogUtils.e( "layoutChild" );
		final View childView = getChildAt(child);
		if (childView != null) {
			childView.requestLayout();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// LogUtils.e( "onMeasure" );
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		// 横屏
		// int widthScale = (int)(width*WIDTH_RATIO);
		int height = (width) / RATIO_Y; // 宽高3:1
		if (!UITools.isPortrait()) {
			height = (int) ((width * RATIO_X / RATIO_Y) / RATIO_X + 0.5f);// 每一格保证为正方形
		}

		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			// throw new
			// IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			// throw new
			// IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
		}

		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);

		}
		// 取整
		/*
		 * height = mRes
		 * .getDimensionPixelOffset(R.dimen.recommend_headview_adv_height);
		 */
		if (App.getDevicesType() == App.PHONE){
			RATIO_Y_X = 2.5f;
		}else if (UITools.isPortrait()) {
			RATIO_Y_X = 3;
		}else {
			RATIO_Y_X = 4;
		}
		height = (int) ((width) / RATIO_Y_X + 0.5f); // 宽高2.5:1
		setMeasuredDimension(width, height);
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page.
	 */
	protected void snapToDestination() {
		final int screenWidth = !UITools.isPortrait() ? (int) (getWidth()
				* RATIO_X / RATIO_Y + 0.5f) : getWidth();

		final int destScreen = (mScrollX + screenWidth / 2) / screenWidth;
		// LogUtils.e( "snapToDestination,destScreen = " + destScreen );
		snapToScreen(destScreen, true);
	}

	/* 滚动WIDTH_RATIO宽度 */
	protected void snapToScreen(int whichScreen, boolean isCallback) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		mNextScreen = whichScreen;
		// LogUtils.e( "snapToScreen,mNextScreen = " + mNextScreen );
		int otherWidth = getWidth();
		if (!UITools.isPortrait()) {
			otherWidth = (int) (getWidth() * RATIO_X / RATIO_Y + 0.5f);
		}

		if (mScrollX != (whichScreen * otherWidth)) {
			// 回调处理
			if (isCallback == true && mTouchScrollListener != null
					&& mCurScreen != whichScreen) {
				// mTouchScrollListener.onScreenChange( whichScreen );
				mTouchScrollListener.onScreenChange(
						whichScreen,
						getChildAt(whichScreen) == null ? null : getChildAt(
								whichScreen).getTag());
			}

			final int delta = whichScreen * otherWidth - mScrollX;
			// int duration = (int) (1000 * Math.atan(Math.abs(mDurationCorr *
			// delta))); // 0ms-1500ms
			int duration = (int) (1000 * Math.atan(Math.abs(Math.PI * delta
					/ 1000.0f))); // 0ms-1500ms
			// TLog.v(TAG, "new:" + duration + ", old:" + Math.abs(delta) * 2 +
			// ", duration1:" + duration1);
			mScroller.startScroll(mScrollX, 0, delta, 0, duration/*
																 * Math.abs(delta
																 * ) * 2
																 */);
			mCurScreen = whichScreen;
			layoutChild(mCurScreen);
			invalidate(); // Redraw the layout
		}
	}

	protected void snapToLastScreen() {
		int otherWidth = getWidth();
		if (!UITools.isPortrait()) {
			otherWidth = (int) (getWidth() * RATIO_X / RATIO_Y + 0.5f);
		}
		// get the valid layout page
		int childCount = getChildCount();
		mNextScreen = childCount - 1;
		if (mScrollX != (childCount - 1) * otherWidth) {
			if (mTouchScrollListener != null) {
				mTouchScrollListener.onScreenChange(
						mNextScreen,
						getChildAt(mNextScreen) == null ? null : getChildAt(
								mNextScreen).getTag());
			}
			int delta = 0;
			if (mScrollX < 0 && mScrollX > 0 - otherWidth / 2) {
				mScrollX = childCount * otherWidth + mScrollX;
				mChangeCoordinate = false;
				scrollTo(mScrollX, mScrollY);
				mChangeCoordinate = true;
			}
			delta = 0 - (mScrollX - otherWidth * (childCount - 1));
			// int duration = (int) (1000 * Math.atan(Math.abs(mDurationCorr *
			// delta))); // 0ms-1500ms
			int duration = (int) (1000 * Math.atan(Math.abs(Math.PI * delta
					/ 1000.0f))); // 0ms-1500ms
			// TLog.v(TAG, "new:" + duration + ", old:" + Math.abs(delta) * 2 +
			// ", duration1:" + duration1);
			mScroller.startScroll(mScrollX, 0, delta, 0, duration/*
																 * Math.abs(delta
																 * ) * 2
																 */);

			mCurScreen = mNextScreen;
			layoutChild(mCurScreen);
			// LogUtils.e( "snapToLastScreen,mCurScreen =" + mCurScreen );
			invalidate(); // Redraw the layout
		}
	}

	protected void snapToFirstScreen() {

		int otherWidth = getWidth();
		if (!UITools.isPortrait()) {
			otherWidth = (int) (getWidth() * RATIO_X / RATIO_Y + 0.5f);
		}
		// get the valid layout page
		mNextScreen = 0;
		if (mScrollX != 0) {
			if (mTouchScrollListener != null) {
				mTouchScrollListener.onScreenChange(
						mNextScreen,
						getChildAt(mNextScreen) == null ? null : getChildAt(
								mNextScreen).getTag());
			}
			if (mScrollX > (getChildCount() - 1) * otherWidth - 1
					&& mScrollX < getChildCount() * otherWidth - otherWidth / 2) {
				mScrollX = mScrollX - getChildCount() * otherWidth;
				mChangeCoordinate = false;
				scrollTo(mScrollX, mScrollY);
				mChangeCoordinate = true;
			}
			final int delta = 0 - mScrollX;
			// int duration = (int) (1000 * Math.atan(Math.abs(mDurationCorr *
			// delta))); // 0ms-1500ms
			int duration = (int) (1000 * Math.atan(Math.abs(Math.PI * delta
					/ 1000.0f))); // 0ms-1500ms
			// TLog.v(TAG, "new:" + duration + ", old:" + Math.abs(delta) * 2 +
			// ", duration1:" + duration1);
			mScroller.startScroll(mScrollX, 0, delta, 0, duration/*
																 * Math.abs(delta
																 * ) * 2
																 */);

			mCurScreen = mNextScreen;
			layoutChild(mCurScreen);
			invalidate(); // Redraw the layout
		}
	}

	/**
	 * 显示下一屏幕
	 */
	public void displayNextScreen() {
		if (mCurScreen < getChildCount() - 1) {
			setDisplayedChild(mCurScreen + 1, true);
			// changeIntegerList( true );
		} else {
			snapToFirstScreen();
		}
	}

	/**
	 * 显示上一屏幕
	 */
	public void displayPreScreen() {
		if (mCurScreen > 0) {
			setDisplayedChild(mCurScreen - 1, true);
			// changeIntegerList( false );
		} else {
			snapToLastScreen();
		}
	}

	public int getCurScreen() {
		return mCurScreen;
	}

	/*
	 * computeScroll 调用过程位于View绘制流程draw-->dispatchDraw --> drawChild
	 * -->computeScroll computeScroll()去实现缓慢移动过程
	 */
	@Override
	public void computeScroll() {

		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();

			// 状态回调
			if (mTouchScrollListener != null) {
				// 开始
				if (mScroller.isFinished() == false) {
					if (mScrollState != OnTouchScrollListener.SCROLL_STATE_FLING) {
						mTouchScrollListener.onScrollStateChanged(
								OnTouchScrollListener.SCROLL_STATE_FLING,
								mCurScreen);
						mScrollState = OnTouchScrollListener.SCROLL_STATE_FLING;
					}
				} else {
					// 结束
					if (mScrollState != OnTouchScrollListener.SCROLL_STATE_IDLE) {
						mTouchScrollListener.onScrollStateChanged(
								OnTouchScrollListener.SCROLL_STATE_IDLE,
								mCurScreen);
						mScrollState = OnTouchScrollListener.SCROLL_STATE_IDLE;
					}
				}
			}
		} else {
			if (mTouchScrollListener != null) {
				// 因为自由滚动结束后,还会调用一次computeScroll, 所以需要加入mTouchState的判断
				if (mScrollState != OnTouchScrollListener.SCROLL_STATE_TOUCH_SCROLL
						&& mTouchState == TOUCH_STATE_SCROLLING) {
					mTouchScrollListener.onScrollStateChanged(
							OnTouchScrollListener.SCROLL_STATE_TOUCH_SCROLL,
							mCurScreen);
					mScrollState = OnTouchScrollListener.SCROLL_STATE_TOUCH_SCROLL;
				}
			}
		}

		// 滚动回调, 类似 listView的onScroll
		if (mPreScrollX != getScrollX()) {
			mPreScrollX = getScrollX();
			if (mTouchScrollListener != null) {
				mTouchScrollListener.onScroll(this, getScrollX(), mScreenWidth);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mEnableScroll) {
			return true;
		}

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getRawX();
		final float y = event.getRawY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			// LogUtils.e( "onTouchEvent,MotionEvent.ACTION_DOWN,mLastMotionX ="
			// + mLastMotionX );
			break;

		case MotionEvent.ACTION_MOVE:
			// 锁定,当手势向下滑动后,就不进行左右滑动
			if (mTouchState == TOUCH_STATE_LOCK) {
				break;
			}
			if (getParent() != null) {
				// 如果父类有滑动事件，抛给父类
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			int deltaX = (int) (mLastMotionX - x);
			mLastMotionX = x;
			// LogUtils.e( "onTouchEvent,MotionEvent.ACTION_MOVE,mLastMotionX ="
			// + mLastMotionX );
			int beScrollTo = getScrollX() + deltaX;
			if (mIsCircle
					|| (beScrollTo > mMinScrollX && beScrollTo < mMaxScrollX)) {
				scrollBy(deltaX, 0);
			}
			/*
			 * View类的源代码如下所示，mScrollX记录的是当前View针对 屏幕坐标在水平方向上的偏移量，而mScrollY则是记录的时
			 * 当前View针对屏幕在竖值方向上的偏移量。 从以下代码我们可以得知，scrollTo就是把View移动到
			 * 屏幕的X和Y位置，也就是绝对位置。 而scrollBy其实就是调用的scrollTo，
			 * 但是参数是当前mScrollX和mScrollY加上X和Y的位置，
			 * 所以ScrollBy调用的是相对于mScrollX和mScrollY的位置。 我们在上面的代码中可以看到当我们手指不放移动屏幕时，
			 * 就会调用scrollBy来移动一段相对的距离。 而当我们手指松开后，
			 * 会调用mScroller.startScroll(mUnboundedScrollX, 0, delta, 0,
			 * duration);来产生一段动画来移动到相应的页面， 在这个过程中系统回不断调用computeScroll()，
			 * 我们再使用scrollTo来把View移动到当前Scroller所在的绝对位置。
			 */
			break;
		case MotionEvent.ACTION_UP:
			// if (mTouchState == TOUCH_STATE_SCROLLING) {
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();

			if (velocityX > SNAP_VELOCITY && mCurScreen >= 0) {
				// Fling enough to move left
				int left = mCurScreen - 1;
				if (mIsCircle && left < 0) {
					left += getChildCount();
					snapToLastScreen();
					// init( );
				} else {
					snapToScreen(left, true);
					// changeIntegerList( false );
				}
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen <= getChildCount() - 1) {
				// Fling enough to move right
				int right = (mCurScreen + 1);
				if (mIsCircle && right > getChildCount() - 1) {
					right -= getChildCount();
					snapToFirstScreen();
					// init( );
				} else {
					snapToScreen(right, true);
					// changeIntegerList( true );
				}
			} else {
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			// }
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mLockAllWhenTouch) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (!mEnableScroll) {
			return false;
		}

		final int action = ev.getAction();

		// 移动过程中,锁定
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState == TOUCH_STATE_SCROLLING)) {
			return true;
		}

		final float x = ev.getRawX();
		final float y = ev.getRawY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			if (mTouchState == TOUCH_STATE_LOCK) {
				break;
			}
			final float xDiff = Math.abs(mLastMotionX - x);
			final float yDiff = Math.abs(mLastMotionY - y);

			if (xDiff > mTouchSlop) {
				float tan = yDiff / xDiff;
				if (tan < mAngleSlop) {
					mTouchState = TOUCH_STATE_SCROLLING;
				} else {
					mTouchState = TOUCH_STATE_LOCK;
				}
			}
			break;

		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return mTouchState == TOUCH_STATE_SCROLLING;
	}

	public void setDisplayedChild(int i) {
		// 有动画
		snapToScreen(i, false);
		// 无动画
		// setToScreen(i);
	}

	public void setDisplayedChild(int i, boolean callback) {
		// 有动画
		/* 滚动WIDTH_RATIO宽度 */
		snapToScreen(i, callback);
		// 无动画
		// setToScreen(i);
	}

	// 无过度动画
	public void setDisplayedChildNoAmin(int whichScreen) {
		if (mCurScreen == whichScreen) {
			return;
		}

		int otherWidth = getWidth();
		if (!UITools.isPortrait()) {
			otherWidth = (int) (getWidth() * RATIO_X / RATIO_Y + 0.5f);
		}

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		mCurScreen = whichScreen;
		scrollTo(whichScreen * otherWidth, 0);
		if (mTouchScrollListener != null) {
			mTouchScrollListener.onScreenChange(
					whichScreen,
					getChildAt(whichScreen) == null ? null : getChildAt(
							whichScreen).getTag());
		}
		layoutChild(mCurScreen);
		invalidate(); // Redraw the layout
	}

	public int getDisplayedChild() {
		return mCurScreen;
	}

	public void setOnTouchScrollListener(OnTouchScrollListener listener) {
		mTouchScrollListener = listener;
	}

	public OnTouchScrollListener getOnTouchScrollListener() {
		return mTouchScrollListener;
	}

	public void setTouchScrollEnable(boolean enable) {
		mEnableScroll = enable;
	}

	public boolean getEnableScroll() {
		return mEnableScroll;
	}

	public void setLockAllWhenTouch(boolean lock) {
		mLockAllWhenTouch = lock;
	}

	public void setDefaultScreem(int screem) {
		mDefaultScreen = screem;
		mCurScreen = mDefaultScreen;
	}

	public int getDefaultScreem() {
		return mDefaultScreen;
	}

	public void destroy() {
		mScroller = null;
		mVelocityTracker = null;
		mTouchScrollListener = null;
	}

	@Override
	public void scrollTo(int x, int y) {
		if (mIsCircle && mChangeCoordinate) {
			final int width = !UITools.isPortrait() ? (int) (getWidth()
					* RATIO_X / RATIO_Y + 0.5f) : getWidth();

			final int count = getChildCount();
			if (x <= -width / 2) {
				x = width * count + x;
			} else if (x >= width * count - width / 2) {
				x = x - width * count;
			}
		}
		mScrollX = x;
		mScrollY = y;
		super.scrollTo(x, y);
	}

	public void setCircle(boolean circle) {
		// circle 圆
		mIsCircle = circle;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// LogUtils.e( "dispatchDraw" );
		// LogUtils.e( "mCurScreen = " +
		// mCurScreen+"| mCurScreen >= getChildCount( ) ="+ (mCurScreen >=
		// getChildCount( ))+"|mIsCircle = " +mIsCircle);
		if (!mIsCircle || mCurScreen >= getChildCount() || mCurScreen < 0) {
			// LogUtils.e( "super.dispatchDraw()" );
			super.dispatchDraw(canvas);
			return;
		}
		// 循环滑动的绘制
		boolean restore = false;
		int restoreCount = 0;
		// ViewGroup.dispatchDraw() supports many features we don't need:
		// clip to padding, layout animation, animation listener, disappearing
		// children, etc. The following implementation attempts to fast-track
		// the drawing dispatch by drawing only what we know needs to be drawn.
		// cycleSlide modified:
		boolean fastDraw = mTouchState != TOUCH_STATE_SCROLLING
				&& mNextScreen == INVALID_SCREEN;
		// If we are not scrolling or flinging, draw only the current screen
		if (fastDraw) {
			// LogUtils.e( "00000 mCurScreen = " + mCurScreen );
			// LogUtils.e( "00000 mCurScreen+1 = " + ( mCurScreen + 1 > (
			// getChildCount( ) - 1 ) ? 0 : ( mCurScreen + 1 ) ) );
			// LogUtils.e( "00000 mCurScreen-1 = " + ( ( mCurScreen - 1 ) < 0 ?
			// ( getChildCount( ) - 1 ) : ( mCurScreen - 1 ) ) );
			drawChild(canvas, getChildAt(mCurScreen), getDrawingTime());
			drawChild(canvas,
					getChildAt(mCurScreen + 1 > (getChildCount() - 1) ? 0
							: (mCurScreen + 1)), getDrawingTime());
			drawChild(canvas,
					getChildAt((mCurScreen - 1) < 0 ? (getChildCount() - 1)
							: (mCurScreen - 1)), getDrawingTime());
		} else {
			/*
			 * final long drawingTime = getDrawingTime(); final float scrollPos
			 * = (float) mScrollX / getWidth(); final int leftScreen = (int)
			 * scrollPos; final int rightScreen = leftScreen + 1; if (leftScreen
			 * >= 0) { drawChild(canvas, getChildAt(leftScreen), drawingTime); }
			 * if (scrollPos != leftScreen && rightScreen < getChildCount()) {
			 * drawChild(canvas, getChildAt(rightScreen), drawingTime); }
			 */
			/*
			 * cycleSlide modified: 1、 slide is complete: draw CurScreen 2、
			 * slide is running： draw leftScreen and rightScreen 3、 slide is
			 * cycle running: do translate
			 */
			long drawingTime = getDrawingTime();
			int width = getWidth();
			if (!UITools.isPortrait()) {
				width = (int) (getWidth() * RATIO_X / RATIO_Y + 0.5f);
			}
			float scrollPos = (float) getScrollX() / width;

			boolean endlessScrolling = true;

			int leftScreen;
			int rightScreen;
			boolean isScrollToRight = false;
			int childCount = getChildCount();
			// LogUtils.e( "scrollPos =" + scrollPos );
			// LogUtils.e( "endlessScrolling =" + endlessScrolling );
			// LogUtils.e( "isScrollToRight =" + isScrollToRight );
			if (scrollPos < 0 && endlessScrolling) {
				leftScreen = childCount - 1;
				rightScreen = 0;
			} else {
				leftScreen = Math.min((int) scrollPos, childCount - 1);
				rightScreen = leftScreen + 1;
				if (endlessScrolling) {
					rightScreen = rightScreen % childCount;
					isScrollToRight = true;
				}
			}
			if (isScreenNoValid(leftScreen)) {
				if (rightScreen == 0 && !isScrollToRight) { // 向左滑动，如果rightScreen是0
					int offset = childCount * width;
					canvas.translate(-offset, 0);
					// LogUtils.e( "11111 leftScreen = " + leftScreen );
					// LogUtils.e( "11111 leftScreen+1 = " + ( leftScreen + 1 >
					// ( getChildCount( ) - 1 ) ? 0 : ( leftScreen + 1 ) ) );
					// LogUtils.e( "11111 leftScreen-1 = " + ( ( leftScreen - 1
					// ) < 0 ? ( getChildCount( ) - 1 ) : ( leftScreen - 1 ) )
					// );
					drawChild(canvas, getChildAt(leftScreen), drawingTime);
					drawChild(
							canvas,
							getChildAt(leftScreen + 1 > (getChildCount() - 1) ? 0
									: (leftScreen + 1)), drawingTime);
					drawChild(
							canvas,
							getChildAt((leftScreen - 1) < 0 ? (getChildCount() - 1)
									: (leftScreen - 1)), drawingTime);
					canvas.translate(+offset, 0);
				} else {
					int offset = (childCount + 1) * width;
					if (scrollPos == leftScreen && scrollPos == getChildCount()
							&& leftScreen == getChildCount()
							&& endlessScrolling && rightScreen == 0
							&& isScrollToRight) {
						// LogUtils.e( "22222 rightScreen = " + rightScreen );
						// LogUtils.e( "22222 rightScreen+1 = " + ( rightScreen
						// + 1 > ( getChildCount( ) - 1 ) ? 0 : ( rightScreen +
						// 1 ) ) );
						// LogUtils.e( "22222 rightScreen-1 = " + ( (
						// rightScreen - 1 ) < 0 ? ( getChildCount( ) - 1 ) : (
						// rightScreen - 1 ) ) );
						canvas.translate(+offset, 0);
						drawChild(canvas, getChildAt(rightScreen), drawingTime);
						drawChild(
								canvas,
								getChildAt(rightScreen + 1 > (getChildCount() - 1) ? 0
										: (rightScreen + 1)), drawingTime);
						drawChild(
								canvas,
								getChildAt((rightScreen - 1) < 0 ? (getChildCount() - 1)
										: (rightScreen - 1)), drawingTime);
						canvas.translate(-offset, 0);
					} else {
						// LogUtils.e( "33333 leftScreen = " + leftScreen );
						// LogUtils.e( "33333 leftScreen+1 = " + ( leftScreen +
						// 1 > ( getChildCount( ) - 1 ) ? 0 : ( leftScreen + 1 )
						// ) );
						// LogUtils.e( "33333 leftScreen-1 = " + ( ( leftScreen
						// - 1 ) < 0 ? ( getChildCount( ) - 1 ) : ( leftScreen -
						// 1 ) ) );
						drawChild(canvas, getChildAt(leftScreen), drawingTime);
						drawChild(
								canvas,
								getChildAt(leftScreen + 1 > (getChildCount() - 1) ? 0
										: (leftScreen + 1)), drawingTime);
						drawChild(
								canvas,
								getChildAt((leftScreen - 1) < 0 ? (getChildCount() - 1)
										: (leftScreen - 1)), drawingTime);
					}

				}
			}
			if (scrollPos != leftScreen && isScreenNoValid(rightScreen)) {
				if (endlessScrolling && rightScreen == 0 && isScrollToRight) {
					int offset = childCount * width;
					// LogUtils.e( "44444 rightScreen = " + rightScreen );
					// LogUtils.e( "44444 rightScreen+1 = " + ( rightScreen + 1
					// > ( getChildCount( ) - 1 ) ? 0 : ( rightScreen + 1 ) ) );
					// LogUtils.e( "44444 rightScreen-1 = " + ( ( rightScreen -
					// 1 ) < 0 ? ( getChildCount( ) - 1 ) : ( rightScreen - 1 )
					// ) );
					canvas.translate(+offset, 0);
					drawChild(canvas, getChildAt(rightScreen), drawingTime);
					drawChild(
							canvas,
							getChildAt(rightScreen + 1 > (getChildCount() - 1) ? 0
									: (rightScreen + 1)), drawingTime);
					drawChild(
							canvas,
							getChildAt((rightScreen - 1) < 0 ? (getChildCount() - 1)
									: (rightScreen - 1)), drawingTime);
					canvas.translate(-offset, 0);
				} else {
					// LogUtils.e( "55555 rightScreen = " + rightScreen );
					// LogUtils.e( "55555 rightScreen+1 = " + ( rightScreen + 1
					// > ( getChildCount( ) - 1 ) ? 0 : ( rightScreen + 1 ) ) );
					// LogUtils.e( "55555 rightScreen-1 = " + ( ( rightScreen -
					// 1 ) < 0 ? ( getChildCount( ) - 1 ) : ( rightScreen - 1 )
					// ) );
					drawChild(canvas, getChildAt(rightScreen), drawingTime);
					drawChild(
							canvas,
							getChildAt(rightScreen + 1 > (getChildCount() - 1) ? 0
									: (rightScreen + 1)), drawingTime);
					drawChild(
							canvas,
							getChildAt((rightScreen - 1) < 0 ? (getChildCount() - 1)
									: (rightScreen - 1)), drawingTime);
				}
			}
		}
		if (restore) {
			canvas.restoreToCount(restoreCount);
		}
		// for( int i = 0 ; i < getChildCount( ) ; i++ )
		// {
		// drawChild( canvas , getChildAt( i ) , getDrawingTime( ) );
		// }
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		// LogUtils.e( "drawChild" );
		// TODO Auto-generated method stub
		return super.drawChild(canvas, child, drawingTime);
	}

	private boolean isScreenNoValid(int screen) {
		return screen >= 0 && screen < getChildCount();
	}

	// @Override
	// public void addView( View child )
	// {
	// LogUtils.e( "addView" );
	// super.addView( child );
	// mViewList.add( child );
	// mIntegerList.add( mPos );
	// LogUtils.e( "mPos=" + mPos );
	// mPos++;
	// }
	//
	// public void init()
	// {
	// mIntegerList.clear( );
	// for( int i = 0 ; i < getChildCount( ) ; i++ )
	// {
	// mIntegerList.add( i );
	// }
	// }

}
