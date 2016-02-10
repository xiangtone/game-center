/**   
* @Title: ScrollViewExtend.java
* @Package com.x.view
* @Description: TODO 

* @date 2014-1-13 下午04:24:38
* @version V1.0   
*/

package com.x.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
* @ClassName: ScrollViewExtend
* @Description: TODO 

* @date 2014-1-13 下午04:24:38
* 
*/

public class ScrollViewExtend extends ScrollView {

	private boolean isCloseDispatchTouchEvent;
	private boolean isEnableDispatchTouchEvent;
	// 滑动距离及坐标
	private float xDistance, yDistance, xLast, yLast;

	private OnBorderListener onBorderListener;

	public ScrollViewExtend(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnBoraderListener(OnBorderListener onBorderListener) {
		this.onBorderListener = onBorderListener;
	}

	public static interface OnBorderListener {

		/**
		 * Called when scroll to bottom
		 */
		public void onBottom();

		/**
		 * Called when scroll to top
		 */
		public void onTop();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				return false;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	/*************************dispatchTouchEvent,onTouchEvent事件自定义***************************/
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (!isEnableDispatchTouchEvent) {
			return super.dispatchTouchEvent(ev);
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (!isCloseDispatchTouchEvent) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if (getScrollY() + getHeight() >= computeVerticalScrollRange()) {
					//System.out.println("------滚动到最下方------"); 
					setEnableDispatchTouchEvent(true);
				} else {
					//System.out.println("------没有滚动到最下方------"); 
					setEnableDispatchTouchEvent(false);
				}
				break;
			case MotionEvent.ACTION_UP:
				//				doOnBorderListener();
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	/** 
	* @return isEnableDispatchTouchEvent 
	*/
	public boolean isEnableDispatchTouchEvent() {
		return isEnableDispatchTouchEvent;
	}

	/** 
	* @param isEnableDispatchTouchEvent 要设置的 isEnableDispatchTouchEvent 
	*/
	public void setEnableDispatchTouchEvent(boolean isEnableDispatchTouchEvent) {
		this.isEnableDispatchTouchEvent = isEnableDispatchTouchEvent;
	}

	/** 
	* @return isCloseDispatchTouchEvent 
	*/
	public boolean isCloseDispatchTouchEvent() {
		return isCloseDispatchTouchEvent;
	}

	/** 
	* @param isCloseDispatchTouchEvent 要设置的 isCloseDispatchTouchEvent 
	*/
	public void setCloseDispatchTouchEvent(boolean isCloseDispatchTouchEvent) {
		this.isCloseDispatchTouchEvent = isCloseDispatchTouchEvent;
	}

	private void doOnBorderListener() {
		if (getChildAt(0) != null && getChildAt(0).getMeasuredHeight() <= getScrollY() + getHeight()) {
			if (onBorderListener != null) {
				onBorderListener.onBottom();
			}
		} else if (getScrollY() == 0) {
			if (onBorderListener != null) {
				onBorderListener.onTop();
			}
		}
	}
}
