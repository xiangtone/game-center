package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hykj.gamecenter.ui.HorizonScrollLayout;
import com.hykj.gamecenter.utils.UITools;

public class FirstAdvImageView extends ImageView {

	Resources mRes = null;

	public FirstAdvImageView(Context context) {
		super(context);
		mRes = context.getResources();
	}

	public FirstAdvImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRes = context.getResources();
	}

	public FirstAdvImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mRes = context.getResources();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		// TODO
		// 竖屏
		// int widthScale = width * RATIO_X / RATIO_Y;
		int height = (width) / HorizonScrollLayout.RATIO_Y; // 宽高3:1
		// 横屏
		if (!UITools.isPortrait()) {
			height = (width * HorizonScrollLayout.RATIO_X / HorizonScrollLayout.RATIO_Y)
					/ HorizonScrollLayout.RATIO_X;
			width = width * HorizonScrollLayout.RATIO_X
					/ HorizonScrollLayout.RATIO_Y;
		}

		/*
		 * // 取整 height = mRes
		 * .getDimensionPixelOffset(R.dimen.recommend_headview_adv_height);
		 */
		height = (int) ((width) / HorizonScrollLayout.RATIO_Y_X + 0.5f); // 宽高2.5:1
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
	}
}
