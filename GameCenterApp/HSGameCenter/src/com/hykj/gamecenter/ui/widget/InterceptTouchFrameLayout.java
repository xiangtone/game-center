package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/*
 * 拦截点击事件，禁止上层的fragment点击事件往下层传递
 * owenli
 * */

public class InterceptTouchFrameLayout extends FrameLayout {
	public InterceptTouchFrameLayout(Context context) {
		super(context);
	}

	public InterceptTouchFrameLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public InterceptTouchFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptHoverEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

}
