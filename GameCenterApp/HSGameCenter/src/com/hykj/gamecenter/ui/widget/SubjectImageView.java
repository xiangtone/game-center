package com.hykj.gamecenter.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SubjectImageView extends ImageView {
	private static final String TAG = "SubjectImageView";

	public SubjectImageView(Context context) {
		super(context);
	}

	public SubjectImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SubjectImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
		int height = /* ( width * 3 ) / 5 */MeasureSpec
				.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
	}
}
