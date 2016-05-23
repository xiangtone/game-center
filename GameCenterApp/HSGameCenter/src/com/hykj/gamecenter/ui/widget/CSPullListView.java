
package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.hykj.gamecenter.R;

public class CSPullListView extends ListView implements OnScrollListener {

    private final String TAG = "cs.widget.CSPullListView";
    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private ICSListViewListener mListViewListener;

    // -- header view
    private CSListViewHeader mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // header view's height
    private boolean mHeaderPullEnable = false;//
    private boolean mHeaderPullRefreshing = false; // is refreashing.

    // -- footer view
    private CSListViewHeader mFooterView;
    private boolean mFooterPullEnable = false;
    private boolean mFooterPullRefreshing = false;
    private RelativeLayout mFooterViewContent;
    private int mFooterViewHeight;

    private boolean mIsFooterAdded = false;
    private boolean mIsHeaderAdded = false;
    private boolean mbHeaderAdded = false;

    private boolean isHeaderLoaded = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 100; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 120; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 0.8f; // support iOS like pull

    // feature.

    /**
     * @param context
     */
    public CSPullListView(Context context) {
        this(context, null);
    }

    public CSPullListView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.plCSListStyle);
    }

    public CSPullListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        setLongClickable(false);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void addListHeaderView(View v) {

        this.addHeaderView(v);
        mbHeaderAdded = true;
    }

    public boolean isAddedHeader() {
        return mIsHeaderAdded || mbHeaderAdded;
    }

    public boolean isAddedFooter() {
        return mIsFooterAdded;
    }

    /**
     * enable or disable pull down refresh feature.
     * 
     * @param enable
     */
    public void setHeaderPullEnable(boolean enable) {
        if (mIsHeaderAdded == false && enable) {
            mIsHeaderAdded = true;
            // init header view
            mHeaderView = new CSListViewHeader(getContext(), true);
            mHeaderViewContent = (RelativeLayout) mHeaderView
                    .findViewById(R.id.csl_listview_header_content);
            mHeaderTimeView = (TextView) mHeaderView
                    .findViewById(R.id.csl_listview_header_time);
            addHeaderView(mHeaderView);

            // init header height
            mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // Log.d(Tag,
                            // "addOnGlobalLayoutListener onGlobalLayout mHeaderViewHeight="
                            // + mHeaderViewHeight);
                            mHeaderViewHeight = mHeaderViewContent.getHeight();
                            // getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        }
                    });
        }

        if (mHeaderViewContent == null)
            return;

        mHeaderPullEnable = enable;
        if (!mHeaderPullEnable) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     * 
     * @param enable
     */
    public void setFooterPullEnable(boolean enable) {
        if (mIsFooterAdded == false && enable) {
            mIsFooterAdded = true;
            // init header view
            mFooterView = new CSListViewHeader(getContext(), false);
            mFooterViewContent = (RelativeLayout) mFooterView
                    .findViewById(R.id.csl_listview_header_content);
            // mHeaderTimeView = (TextView)
            // mFooterView.findViewById(R.id.csl_listview_header_time);
            addFooterView(mFooterView);

            // Log.d(Tag, "addFooterView ");

            // init header height
            mFooterView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // Log.d(Tag,
                            // "addOnGlobalLayoutListener onGlobalLayout mHeaderViewHeight="
                            // + mHeaderViewHeight);
                            mFooterViewHeight = mFooterViewContent.getHeight();
                            // getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        }
                    });
        }

        if (mFooterViewContent == null)
            return;

        mFooterPullEnable = enable;
        if (!mFooterPullEnable) { // disable, hide the content
            mFooterViewContent.setVisibility(View.INVISIBLE);
        } else {
            mFooterViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopHeaderRefresh() {
        if (mHeaderPullRefreshing == true) {
            mHeaderPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopFooterRefresh() {
        if (mFooterPullRefreshing == true) {
            mFooterPullRefreshing = false;
            resetFooterHeight();
        }
    }

    // /**
    // * set last refresh time
    // *
    // * @param time
    // */
    // public void setRefreshTime(String time) {
    // mHeaderTimeView.setText(time);
    // }

    private void invokeOnScrolling() {
        // if (mScrollListener instanceof OnXScrollListener) {
        // OnXScrollListener l = (OnXScrollListener) mScrollListener;
        // l.onXScrolling(this);
        // }
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        if (mHeaderView == null)
            return;
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mHeaderPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mHeaderPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void resetFooterHeight() {
        if (mFooterView == null)
            return;
        int height = mFooterView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mFooterPullRefreshing && height <= mFooterViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mFooterPullRefreshing && height > mFooterViewHeight) {
            finalHeight = mFooterViewHeight;
        }
        mScrollBack = SCROLLBACK_FOOTER;
        mScroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void updateHeaderHeight(float delta) {
        if (mHeaderView == null || mHeaderPullEnable == false)
            return;
        mHeaderView.setVisiableHeight((int) (120 * delta / (mHeaderView.getVisiableHeight() + 20))
                + mHeaderView.getVisiableHeight());
        //		mHeaderView.setVisiableHeight((int) delta
        //		        + mHeaderView.getVisiableHeight());
        if (mHeaderPullEnable && !mHeaderPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
                mHeaderView.setState(CSListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(CSListViewHeader.STATE_NORMAL);
            }
            isHeaderLoaded = true;
        }
        setSelection(0); // scroll to top each time
    }

    private void updateFooterHeight(float delta) {
        if (mFooterView == null || mFooterPullEnable == false)
            return;
        /*Logger.i(TAG, "updateHeaderHeight " + delta + "  " + mFooterView.getVisiableHeight(),
                "oddshou");*/
        mFooterView.setVisiableHeight((int) (120 * delta / (mFooterView.getVisiableHeight() + 20))
                + mFooterView.getVisiableHeight());
        /* mFooterView.setVisiableHeight((int) (120 * delta / mFooterView.getVisiableHeight())
                 + mFooterView.getVisiableHeight());*/

        // Log.d(Tag, "updateFooterHeight height=" +
        // mFooterView.getVisiableHeight() + "|mFooterViewHeight=" +
        // mFooterViewHeight + "|" + mFooterPullRefreshing
        // + "|" + delta);

        if (mFooterPullEnable && !mFooterPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mFooterView.getVisiableHeight() >= mFooterViewHeight) {
                mFooterView.setState(CSListViewHeader.STATE_READY);
            } else {
                mFooterView.setState(CSListViewHeader.STATE_NORMAL);
            }
            isHeaderLoaded = false;
        }
        //        setSelection(mTotalItemCount - 1); // scroll to top each time
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        // Log.d(Tag, "onTouchEvent getAction = " + ev.getAction() +
        // "|MotionEvent=" + ev.toString());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && mHeaderPullEnable
                        && (mHeaderView != null)
                        && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY/* / OFFSET_RADIO*/);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && mFooterPullEnable && (mFooterView != null)
                        && (mFooterView.getVisiableHeight() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY /*/ OFFSET_RADIO*/);
                    invokeOnScrolling();
                }
                break;
            default:
                mLastY = -1; // reset
                if (isHeaderLoaded) {
                    if (getFirstVisiblePosition() == 0 && mHeaderPullEnable
                            && mHeaderView != null) {
                        /*Logger.i(TAG, "getFirstVisiblePosition===="
                                + (getFirstVisiblePosition() == 0 && mHeaderPullEnable
                                && mHeaderView != null));*/
                        // invoke refresh
                        if (mHeaderPullEnable
                                && (mHeaderView.getVisiableHeight() > mHeaderViewHeight)
                                && (mHeaderView.getState() != CSListViewHeader.STATE_REFRESHING)) {
                            mHeaderPullRefreshing = true;
                            mHeaderView.setState(CSListViewHeader.STATE_REFRESHING);
                            if (mListViewListener != null) {
                                mListViewListener.onRefresh();
                            }
                        }

                        // Log.d(Tag, "resetHeaderHeight " + ev.getAction());
                        resetHeaderHeight();
                    }
                } else {
                    if (getLastVisiblePosition() == mTotalItemCount - 1
                            && mFooterPullEnable && mFooterView != null) {
                        // invoke refresh
                        /*Logger.i(TAG, "getFirstVisiblePosition===="
                                + (getLastVisiblePosition() == mTotalItemCount - 1
                                        && mFooterPullEnable && mFooterView != null));*/
                        if (mFooterPullEnable
                                && (mFooterView.getVisiableHeight() > mFooterViewHeight)
                                && (mFooterView.getState() != CSListViewHeader.STATE_REFRESHING)) {
                            mFooterPullRefreshing = true;
                            mFooterView.setState(CSListViewHeader.STATE_REFRESHING);
                            if (mListViewListener != null) {
                                mListViewListener.onLoadMore();
                            }
                        }

                        // Log.d(Tag, "resetHeaderHeight " + ev.getAction());
                        resetFooterHeight();

                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                if (mHeaderView != null)
                    mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                if (mFooterView != null)
                    mFooterView.setVisiableHeight(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }

        // Log.d(Tag, "onScroll mTotalItemCount=" + mTotalItemCount);
    }

    public void setCSListViewListener(ICSListViewListener listener) {
        mListViewListener = listener;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface ICSListViewListener {
        void onRefresh();

        void onLoadMore();
    }

}
