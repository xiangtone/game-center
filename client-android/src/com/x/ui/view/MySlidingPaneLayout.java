/**   
* @Title: MySlidingPaneLayout.java
* @Package com.mas.amineappstore.ui.view
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-27 下午7:08:50
* @version V1.0   
*/

package com.x.ui.view;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

/**
* @ClassName: MySlidingPaneLayout
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-27 下午7:08:50
* 
*/

public class MySlidingPaneLayout extends SlidingPaneLayout {

	public MySlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MySlidingPaneLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySlidingPaneLayout(Context context) {
		super(context);
	}
	private DisplayMetrics displayMetrics ;
	private boolean mTouchedDown = false;
	private boolean mForwardTouchesToChildren = false;
	private static final int IGNORE_DISTANCE = 48;
	public void setDisplayMetrics(DisplayMetrics displayMetrics){
		this.displayMetrics = displayMetrics;
	}
	
	 /**
     *
     * @return true ignore the touch event , don't deliver the touch event to SlidingPaneLayout
     */
	private boolean ignoreTouchEvent(MotionEvent e){
		int height = 0;
		float x = e.getX();
		float y = e.getY();
		if(displayMetrics != null){
			height = displayMetrics.heightPixels;
		}
		return (x > IGNORE_DISTANCE || (y < IGNORE_DISTANCE || (height - y) <IGNORE_DISTANCE ));
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		/*
		 * If not slideable or if the "sliding" pane is open, let super method
		 * handle it.
		 */
		if (!isSlideable() || isOpen())
			return super.onInterceptTouchEvent(e);

		switch (e.getActionMasked()) {
		case MotionEvent.ACTION_DOWN: {
			mTouchedDown = true;

			/*
			 * "50" should be defined as a constant. Also this should be
			 * re-calculated in case the "sliding" pane is put at right side.
			 */
			if (ignoreTouchEvent(e)) {
				mForwardTouchesToChildren = true;
				return false;
			} else
				mForwardTouchesToChildren = false;

			break;
		}

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			mTouchedDown = false;
			mForwardTouchesToChildren = false;

			break;
		}
		}

		if (mTouchedDown && mForwardTouchesToChildren)
			return false;

		if (mTouchedDown)
			return true;

		return super.onInterceptTouchEvent(e);
	}// onInterceptTouchEvent()

}
