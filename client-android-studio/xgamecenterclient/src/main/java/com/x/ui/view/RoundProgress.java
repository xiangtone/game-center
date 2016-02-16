/**   
* @Title: RoundProgress.java
* @Package com.x.ui.view
* @Description: TODO 

* @date 2014-4-14 下午03:16:57
* @version V1.0   
*/

package com.x.ui.view;

/**
* @ClassName: RoundProgress
* @Description: TODO 

* @date 2014-4-14 下午03:16:57
* 
*/

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.x.R;

/**
 * Example of a custom view for android. 
 
 *
 */
public class RoundProgress extends View {
	private Paint paint;

	/** The back color */
	private int backColor;
	/** The front color */
	private int fillColor;

	/** The progress value (0 - 100) */
	private int progress;
	/** The drawing area */
	private RectF rectF;

	private static final int DEFAULT_PROGRESS = 5;

	public static final int SPECIAL_PROGRESS = 10000;

	/** Bar thickness */
	private float thickness;

	public RoundProgress(Context context) {
		super(context);
		initialize();
	}

	public RoundProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundProgress);
		int defaultBackColor = Color.parseColor("#00000000");
		int defaultFillColor = Color.parseColor("#8eaa1e");
		backColor = array.getColor(R.styleable.RoundProgress_backColor, defaultBackColor);
		fillColor = array.getColor(R.styleable.RoundProgress_progressColor, defaultFillColor);
		thickness = array.getDimension(R.styleable.RoundProgress_thicknessRP, 2f);
		float ht = thickness / 2;
		rectF = new RectF(ht, ht, getWidth() - ht, getHeight() - ht);
		array.recycle();
	}

	public RoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize();

	}

	/**
	 * Private method to initialize default values.
	 */
	private void initialize() {
		paint = new Paint();
		backColor = Color.parseColor("#00000000");
		fillColor = Color.parseColor("#8eaa1e");
		progress = 0;
		thickness = 6;
		float ht = thickness / 2;
		rectF = new RectF(ht, ht, getWidth() - ht, getHeight() - ht);
	}

	/**
	 * Define the color for the placeholder progress (background)
	 * @param backColor
	 */
	public void setBackColor(int backColor) {
		this.backColor = backColor;
		invalidate();
	}

	/**
	 * Define the color for the filled progress (foreground)
	 * @param fillColor
	 */
	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
		invalidate();
	}

	/**
	 * Define the progress thickness.
	 * @param thickness
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
		if (this.thickness > getWidth() / 2)
			this.thickness = getWidth() / 2;
		invalidate();
	}

	/**
	 * Get the progress value.
	 * @return 0 to 100 (%)
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * Get the bar thickness.
	 * @return
	 */
	public float getThickness() {
		return thickness;
	}

	/**
	 * Get current back color.
	 * @return
	 */
	public int getBackColor() {
		return backColor;
	}

	/**
	 * Get current fill color.
	 * @return
	 */
	public int getFillColor() {
		return fillColor;
	}

	/**
	 * Define the value for the progress bar
	 * @param progress
	 */
	public void setProgress(int progress) {
		this.progress = progress;
		if (this.progress == SPECIAL_PROGRESS) {
			this.progress = 0;
		} else if (this.progress > 100) {
			this.progress = 100;
		} else if (this.progress < DEFAULT_PROGRESS) {
			this.progress = DEFAULT_PROGRESS;
		}
		invalidate();
	}

	/**
	 * Update drawing area on resize.
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		float ht = thickness;
		rectF = new RectF(ht, ht, getWidth() - ht, getHeight() - ht);
		this.progress = 0;
	}

	/**
	 * When The view is draw.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		paint.setColor(Color.TRANSPARENT);
		canvas.drawPaint(paint);
		paint.setAntiAlias(true);

		paint.setStyle(Style.STROKE);

		paint.setColor(backColor);
		paint.setStrokeWidth(thickness);
		canvas.drawOval(rectF, paint);

		paint.setStrokeWidth(thickness);
		paint.setColor(fillColor);
		canvas.drawArc(rectF, 270, (progress * 365) / 100, false, paint);
	}

}