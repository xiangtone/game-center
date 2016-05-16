
package com.hykj.gamecenter.ui;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Scroller;

public class ImageSlideGallery extends AdapterView<BaseAdapter>
{

    /**
     * 调试用的tag
     */
    public static final String TAG = "SlideGallery2";

    /**
     * 滑动的时间
     */
    public static final int SCROLL_DURATION = 252;

    /**
     * 移动多少距离就该更换图片了
     */
    public static final int SWITCH_OFFSET = 20;

    /**
     * scroller有变化的命令
     */
    public static final int CMD_SCROLLER = 1;

    /**
     * 适配器
     */
    private BaseAdapter mAdapter;

    /**
     * View的回收站队列,不需要的View将会被添加到队列以供下次需要新View的时候复用
     */
    private final Queue<View> mViewsRecycleBin = new LinkedList<View>();

    /**
     * 最左边的View在整个数据中的位置
     */
    private int mLeftViewPosition;

    /**
     * 最右边的View在整个数据中的位置
     */
    private int mRightViewPosition;

    /**
     * 所有显示的View的左边缘
     */
    private int mViewsLeft;

    /**
     * 用于计算的当前最左边缘
     */
    private int mCurrentLeft;

    /**
     * 用于计算的下一个最左边缘
     */
    private int mNextLeft;

    /**
     * 当前拉动的位移
     */
    private int mCurrentOffset;

    /**
     * 最大的子控件高度
     */
    private int mMaxChildHeight = 0;

    /**
     * 手势监控
     */
    private GestureDetector mGestureDetector;

    /**
     * 滑动计算器
     */
    private Scroller mScroller;

    /**
     * 上一次选中的子控件
     */
    private View mPrevSelectedChild;

    /**
     * 当前选中的
     */
    private int mCurrentSelection = 0;

    /**
     * 当前选中的View
     */
    private View mCurrentSelectedView = null;

    /**
     * 最左边是否有阻力
     */
    private boolean mLeftMostResistance = true;

    /**
     * 最右边是否有阻力
     */
    private boolean mRightMostResistance = true;

    private OnImageChangeListener mOnImageChangeListener = null;

    public OnImageChangeListener getOnImageChangeListener()
    {
        return mOnImageChangeListener;
    }

    public void setOnImageChangeListener(OnImageChangeListener onImageChangeListener)
    {
        this.mOnImageChangeListener = onImageChangeListener;
    }

    /**
     * 设置边缘是否有阻力,且是否滑边缘还返回到原位置,这个设定比较特殊<br/>
     * 需要的话,可自行修改
     * 
     * @param leftMost 最左边是否有阻力
     * @param rightMost 最右边是否有阻力
     */
    public void setResistance(boolean leftMost, boolean rightMost)
    {
        mLeftMostResistance = leftMost;
        mRightMostResistance = rightMost;
    }

    public interface OnImageChangeListener
    {
        void onPageSelected(int position);
    }

    /*
     * 构造方法三兄弟
     */
    private final Context mContext;

    public ImageSlideGallery(Context context)
    {
        this(context, null);
    }

    public ImageSlideGallery(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ImageSlideGallery(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init()
    {
        mGestureDetector = new GestureDetector(mContext, mGestureListener);
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
        mHandler.removeMessages(CMD_SCROLLER);
        mCurrentLeft = 0;
        mNextLeft = 0;
        mLeftViewPosition = -1;
        mRightViewPosition = 0;
        mViewsLeft = 0;
        mCurrentOffset = 0;
        mMaxChildHeight = 0;
        mPrevSelectedChild = null;
        mCurrentSelectedView = null;
    }

    /**
     * 用于处理滑动的handler
     */
    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            int what = msg.what;
            switch (what)
            {
                case CMD_SCROLLER:
                    if (mScroller.computeScrollOffset())
                    {
                        int scrollx = mScroller.getCurrX();
                        mNextLeft = scrollx;
                        requestLayout();
                    }
                    if (!mScroller.isFinished())
                    {
                        sendEmptyMessage(CMD_SCROLLER);
                    }
                    else
                    {
                    }
                    break;
            }
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        resetLayout(w, h);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mAdapter == null)
        {
            return;
        }

        int deltaLeft = mCurrentLeft - mNextLeft;

        /* 回收看不到的Views */
        recycleViewsOutOfBounds(deltaLeft);

        /* 添加新的View到布局 */
        addViews(deltaLeft);

        /* 摆放子控件 */
        layoutChildren(deltaLeft);

        mCurrentLeft = mNextLeft;

        /* ==== 计算父控件应该有的尺寸 ==== */
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST)
        {
            heightSize = getPaddingTop() + getPaddingBottom() + mMaxChildHeight
                    + getHorizontalScrollbarHeight();
        }

        setMeasuredDimension(getMeasuredWidth(), heightSize);
    }
    
    @Override
    public BaseAdapter getAdapter()
    {
        return mAdapter;
    }

    @Override
    public void setAdapter(BaseAdapter adapter)
    {
        if (adapter != mAdapter)
        {
            removeAllViewsInLayout();
            init();
        }
        mAdapter = adapter;
        mViewsRecycleBin.clear();
        // TODO 注册数据改变监听器
    }

    @Override
    public View getSelectedView()
    {
        return mCurrentSelectedView;
    }
    
    @Override
    public void setSelection(int position)
    {
        init();
        removeAllViewsInLayout();
        mViewsRecycleBin.clear();
        mRightViewPosition = position;
        mLeftViewPosition = position - 1;
        mCurrentSelection = position;
        requestLayout();
    }

    /**
     * 调整现在应该在的位置
     */
    private void resetLayout(int w, int h)
    {
        if (mCurrentSelectedView != null)
        {
            // 先摆中间的View
            int totalWidth = getWidth();
            mCurrentSelectedView.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
            mCurrentSelectedView.layout(0, 0, w, h);
            int currentIndex = indexOfChild(mCurrentSelectedView);
            mViewsLeft = 0;
            int childCount = getChildCount();

            // 添加左边的View
            int leftCount = 0;
            for (int i = currentIndex - 1; i >= 0; i--)
            {
                leftCount++;
                View child = getChildAt(i);
                if (child != null)
                {
                    int childLeft = leftCount * -totalWidth;
                    child.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
                    child.layout(childLeft, 0, childLeft + totalWidth, child.getMeasuredHeight());
                    mViewsLeft -= totalWidth;
                }
            }

            // 添加右边的View
            int rightCount = 0;
            for (int i = currentIndex + 1; i < childCount; i++)
            {
                rightCount++;
                View child = getChildAt(i);
                if (child != null)
                {
                    int childLeft = rightCount * totalWidth;
                    child.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
                    child.layout(childLeft, 0, childLeft + totalWidth, child.getMeasuredHeight());
                }
            }
        }
    }



    /**
     * 回收超出可视边界的Views
     * 
     * @param deltaLeft 左边界变化的值
     */
    private void recycleViewsOutOfBounds(int deltaLeft)
    {
        // 当最左边的View超出了可视范围,就将其放入回收站
        View child = getChildAt(0);
        while (child != null && child.getRight() + deltaLeft <= 0 - getWidth())
        {
            // 删掉了最左边的View后,整个View的最左边也变化了
            mViewsLeft += child.getMeasuredWidth();
            mViewsRecycleBin.offer(child);
            removeViewInLayout(child);
            if (mPrevSelectedChild == child)
            {
                mPrevSelectedChild.setSelected(false);
                mPrevSelectedChild = null;
            }
            mLeftViewPosition++;

            child = getChildAt(0);
        }

        // 当最右边的View超出了可视范围,就将其放入回收站
        child = getChildAt(getChildCount() - 1);
        while (child != null && child.getLeft() + deltaLeft >= getWidth() * 2)
        {
            mViewsRecycleBin.offer(child);
            removeViewInLayout(child);
            if (mPrevSelectedChild == child)
            {
                mPrevSelectedChild.setSelected(false);
                mPrevSelectedChild = null;
            }
            mRightViewPosition--;

            child = getChildAt(getChildCount() - 1);
        }
    }

    /**
     * 填充Views到界面,涉及到通过Adapter的getView方法获得view并且添加到界面
     * 
     * @deltaLeft 左边界变化的值
     */
    private void addViews(int deltaLeft)
    {
        int rightEdge = 0;
        View rightMostchild = getChildAt(getChildCount() - 1);
        if (rightMostchild != null)
        {
            rightEdge = rightMostchild.getRight();
        }

        while (rightEdge + deltaLeft < getWidth() * 2 && mRightViewPosition < mAdapter.getCount())
        {

            View newChild = mAdapter.getView(mRightViewPosition, mViewsRecycleBin.poll(), this);
            if (newChild == null)
            {
                continue;
            }
            addSingleChild(newChild, -1);
            rightEdge += newChild.getMeasuredWidth();

            mRightViewPosition++;
        }

        View leftMostChild = getChildAt(0);
        int leftEdge = 0;
        if (leftMostChild != null)
        {
            leftEdge = leftMostChild.getLeft();
        }

        while (leftEdge + deltaLeft > 0 - getWidth() && mLeftViewPosition >= 0)
        {

            View newChild = mAdapter.getView(mLeftViewPosition, mViewsRecycleBin.poll(), this);
            if (newChild != null)
            {
                addSingleChild(newChild, 0);

                leftEdge -= newChild.getMeasuredWidth();
                mLeftViewPosition--;

                // 添加了最左边的View后,整个View的最左边也变化了
                mViewsLeft -= newChild.getMeasuredWidth();

            }
        }
    }

    /**
     * 添加单个View到AdapterView中,并且计算其尺寸
     * 
     * @param child 要添加的View
     * @param viewPos 添加的位置(传入0表示加到最前面,传入负数表示添加到末尾)
     */
    private void addSingleChild(final View child, int viewPos)
    {
        LayoutParams params = child.getLayoutParams();
        if (params == null)
        {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }

        addViewInLayout(child, viewPos, params, true);
        child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
        int height = child.getMeasuredHeight();
        if (height > mMaxChildHeight)
        {
            mMaxChildHeight = height;
        }
        if (mCurrentSelectedView == null)
        {
            // 初始化第一个选中的View
            mCurrentSelectedView = child;
            // if (mListener != null) {
            // mListener.onItemSelected(this, mCurrentSelectedView,
            // mCurrentSelection);
            // }
        }
    }

    /**
     * 将子控件,从左往右挨个摆到界面
     * 
     * @param deltaLeft 左边界变化的值
     */
    private void layoutChildren(int deltaLeft)
    {
        if (getChildCount() > 0)
        {
            mViewsLeft += deltaLeft;
            int left = mViewsLeft;
            for (int i = 0; i < getChildCount(); i++)
            {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
                left += childWidth;
            }
        }
    }


    /**
     * 将所有的Views都删除并且移到回收站
     * 
     * @deprecated 暂时不用这个方法
     */
    @Deprecated
    private void recycleAllViews()
    {
        View child = getChildAt(0);
        while (child != null)
        {
            mViewsRecycleBin.offer(child);
            removeViewInLayout(child);
            child = getChildAt(0);
        }
    }
    
    /**
     * 有关手势动作的监听器
     */
    private final SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener()
    {

        @Override
        public boolean onDown(MotionEvent e)
        {
            mHandler.removeMessages(CMD_SCROLLER);

            for (int i = 0; i < getChildCount(); i++)
            {
                View child = getChildAt(i);
                if (isEventWithinView(e, child))
                {
                    if (mPrevSelectedChild != null)
                    {
                        mPrevSelectedChild.setSelected(false);
                    }
                    mPrevSelectedChild = child;
                    mPrevSelectedChild.setSelected(true);
                    break;
                }
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            if (mPrevSelectedChild != null)
            {
                mPrevSelectedChild.setSelected(false);
            }

            if (mAdapter != null)
            {
                int distance = (int) distanceX;

                if ((distance < 0 && mLeftMostResistance && mCurrentSelection == 0)
                        || (distance > 0 && mRightMostResistance && mCurrentSelection == mAdapter
                                .getCount() - 1))
                {
                    // 滑到边缘了,增加阻力
                    distance *= 0.2;
                }
                else
                {
                    distance *= 0.5;
                }

                mCurrentOffset += distance;
                scrollGallery(-distance);
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            for (int i = 0; i < getChildCount(); i++)
            {
                View child = getChildAt(i);
                if (isEventWithinView(e, child))
                {

                    if (getOnItemClickListener() != null)
                    {
                        int position = mLeftViewPosition + 1 + i;
                        int count = mAdapter.getCount();
                        // 这里由于可能会出现一屏幕已经有全部元素的情况,所以取余
                        if (position >= count)
                        {
                            position %= count;
                        }
                        getOnItemClickListener().onItemClick(ImageSlideGallery.this, child,
                                position, mAdapter.getItemId(position));
                    }
                    break;
                }
            }
            return true;
        }

        /**
         * 该事件是否在该child的范围
         * 
         * @param e
         * @param child
         * @return
         */
        private boolean isEventWithinView(MotionEvent e, View child)
        {
            Rect viewRect = new Rect();
            int[] childPosition = new int[2];
            child.getLocationOnScreen(childPosition);
            int left = childPosition[0];
            int right = left + child.getWidth();
            int top = childPosition[1];
            int bottom = top + child.getHeight();
            viewRect.set(left, top, right, bottom);
            return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
        }
    };

    /*
     * ============== 触摸事件相关 ==============
     */
    /**
     * 是否在横向拖动模式
     */
    private boolean mIsBeingDragged = false;

    /**
     * 重置是否是横向拖动的标志位
     */
    protected void resetDragStatus()
    {
        mIsBeingDragged = false;
    }

    /**
     * 上一次的x
     */
    private float mLastMotionX = -1;

    /**
     * 触摸区域的值
     */
    private int mTouchSlop;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {

        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX(0);
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                if (xDiff > mTouchSlop)
                {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                mGestureDetector.onTouchEvent(ev);
                mLastMotionX = x;
                mIsBeingDragged = false;
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_POINTER_UP:
                if (mPrevSelectedChild != null)
                {
                    mPrevSelectedChild.setSelected(false);
                    mPrevSelectedChild = null;
                }
                adjust();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 调整当前应该在的位置
     */
    private void adjust()
    {
        if (mCurrentOffset > SWITCH_OFFSET)
        {
            if (mCurrentSelection < mAdapter.getCount() - 1)
            {
                switchToNext();
                if (mOnImageChangeListener != null)
                    mOnImageChangeListener.onPageSelected(mCurrentSelection);
            }
            else
            {
                // 划到最后一个
                if (mRightMostResistance == true)
                {
                    switchToCurrent();
                }
                // if (mListener != null) {
                // mListener.onOverScrolled(false);
                // }
            }
        }
        else if (mCurrentOffset < -SWITCH_OFFSET)
        {
            if (mCurrentSelection > 0)
            {
                switchToPrev();
                if (mOnImageChangeListener != null)
                    mOnImageChangeListener.onPageSelected(mCurrentSelection);
            }
            else
            {
                // 划到第一个
                if (mLeftMostResistance == true)
                {
                    switchToCurrent();
                }
                // if (mListener != null) {
                // mListener.onOverScrolled(true);
                // }
            }
        }
        else
        {
            switchToCurrent();
        }
        mCurrentOffset = 0;
    }

    /**
     * 移动到下一个View
     */
    private void switchToNext()
    {
        if (mCurrentSelectedView != null)
        {
            int nextIndex = indexOfChild(mCurrentSelectedView) + 1;
            if (nextIndex >= 0 && nextIndex < getChildCount())
            {
                View nextView = getChildAt(nextIndex);
                if (nextView != null)
                {
                    int currentViewsLeft = getChildAt(0).getLeft();
                    // 这里加上真实左边缘的补偿
                    int offset = nextView.getLeft() + (mViewsLeft - currentViewsLeft);
                    if (mPrevSelectedChild != null)
                    {
                        mPrevSelectedChild.setSelected(false);
                    }
                    startScroll(offset);
                    mCurrentSelectedView = nextView;
                    mCurrentSelection++;
                    // if (mListener != null) {
                    // mListener.onItemSelected(this, mCurrentSelectedView,
                    // mCurrentSelection);
                    // }
                }
            }
        }
    }

    /**
     * 移动到上一个View
     */
    private void switchToPrev()
    {
        if (mCurrentSelectedView != null)
        {
            int prevIndex = indexOfChild(mCurrentSelectedView) - 1;
            if (prevIndex >= 0 && prevIndex < getChildCount())
            {
                View prevView = getChildAt(prevIndex);
                if (prevView != null)
                {
                    int currentViewsLeft = getChildAt(0).getLeft();
                    // 这里加上真实左边缘的补偿
                    int offset = prevView.getLeft() + (mViewsLeft - currentViewsLeft);
                    if (mPrevSelectedChild != null)
                    {
                        mPrevSelectedChild.setSelected(false);
                    }

                    startScroll(offset);
                    mCurrentSelectedView = prevView;
                    mCurrentSelection--;
                    // if (mListener != null) {
                    // mListener.onItemSelected(this, mCurrentSelectedView,
                    // mCurrentSelection);
                    // }
                }
            }
        }
    }
    
    /**
     * 切换到当前View
     */
    private void switchToCurrent()
    {
        if (mCurrentSelectedView != null)
        {
            int currentViewsLeft = getChildAt(0).getLeft();
            // 这里加上真实左边缘的补偿
            int offset = mCurrentSelectedView.getLeft() + (mViewsLeft - currentViewsLeft);
            if (mPrevSelectedChild != null)
            {
                mPrevSelectedChild.setSelected(false);
            }
            startScroll(offset);
        }
    }

    /**
     * 开始滑动
     * 
     * @param offset
     */
    private void startScroll(int offset)
    {
        mHandler.removeMessages(CMD_SCROLLER);
        mScroller.forceFinished(true);
        mNextLeft = 0;
        mCurrentLeft = 0;
        mScroller.startScroll(mNextLeft, 0, offset, 0, SCROLL_DURATION);
        mHandler.sendEmptyMessage(CMD_SCROLLER);
    }

    /**
     * 滑动gallery
     */
    private void scrollGallery(int offset)
    {
        mViewsLeft += offset;
        requestLayout();
    }

    /*
     * 下面覆盖的两个方法是为了显示横向的渐隐效果
     */
    @Override
    protected float getLeftFadingEdgeStrength()
    {
        return 1.0f;
    }

    @Override
    protected float getRightFadingEdgeStrength()
    {
        return 1.0f;
    }

    /**
     * 监听器
     */
    // private GalleryListener mListener;

    /**
     * 为这个画廊设置监听器
     * 
     * @param listener
     */
    // public void setGalleryListener(GalleryListener listener) {
    // mListener = listener;
    // }
    //用于图片加载完成后刷新
    public void notifyData() {
        init();
        removeAllViewsInLayout();
        mViewsRecycleBin.clear();
        int position = mCurrentSelection;
        mRightViewPosition = position;
        mLeftViewPosition = position - 1;
        mCurrentSelection = position;
        requestLayout();
    }

}
