package com.hykj.gamecenter.ui.anim;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ParabolaAnimation extends Animation {

	private final int mFromXType = ABSOLUTE;
	private final int mToXType = ABSOLUTE;

	private final int mFromYType = ABSOLUTE;
	private final int mToYType = ABSOLUTE;

	private float mFromXValue = 0.0f;
	private float mToXValue = 0.0f;

	private float mFromYValue = 0.0f;
	private float mToYValue = 0.0f;

	private float mFromXDelta;
	private float mToXDelta;
	private float mFromYDelta;
	private float mToYDelta;
	private final float p;

	public ParabolaAnimation(float mFromXValue, float mToXValue, float mFromYValue, float mToYValue) {
		super();
		this.mFromXValue = mFromXValue;
		this.mToXValue = mToXValue;
		this.mFromYValue = mFromYValue;
		this.mToYValue = mToYValue;
		p = this.mToXValue * this.mToXValue / (2 * this.mToYValue);
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
		mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
		mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
		mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float dx = mFromXDelta;
		float dy = mFromYDelta;
		if (mFromYDelta != mToYDelta) {
			dy = mFromYDelta + ((mToYDelta - mFromYDelta) * interpolatedTime);
			int nSymbol = (mToXDelta - mFromXDelta) < 0 ? -1 : 1;
			dx = (float) Math.sqrt(2 * p * dy) * nSymbol;
		}
		t.getMatrix().setTranslate(dx, dy);
	}

}
