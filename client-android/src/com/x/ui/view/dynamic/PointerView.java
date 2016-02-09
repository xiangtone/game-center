/**   
* @Title: PointerView.java
* @Package com.mas.amineappstore.ui.view.dynamic
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-3-23 下午2:41:06
* @version V1.0   
*/

package com.x.ui.view.dynamic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.x.R;

/**
 * 
* @ClassName: PointerView
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-3-23 下午2:41:06
*
 */

public class PointerView extends View {

	private static final int DEFAULT_POINTER_COLOR = Color.argb(155, 167, 190, 206);

	private Paint paint, paint1;
	private Context context;
	private int pointColor = DEFAULT_POINTER_COLOR;

	public PointerView(Context context) {
		super(context);
	}

	public PointerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context, attrs, 0);
	}

	public PointerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	/** 
	* @Title: init 
	* @Description: TODO 
	* @param @param context2    
	* @return void    
	*/
	private void init(Context context, AttributeSet attrs, int defStyle) {

		TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.PointerView, defStyle, 0);

		String tempColor = attrArray.getString(R.styleable.PointerView_mCircle_color);
		if (tempColor != null) {
			try {
				pointColor = Color.parseColor(tempColor);
			} catch (IllegalArgumentException e) {
				pointColor = DEFAULT_POINTER_COLOR;
			}
		}

		attrArray.recycle();

		paint = new Paint();
		paint1 = new Paint();
		paint.setAntiAlias(true); //消除锯齿  
		paint1.setAntiAlias(true); //消除锯齿  

		paint.setStyle(Paint.Style.FILL); //绘制实心圆   
		paint1.setStyle(Paint.Style.STROKE); //绘制空心圆   
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int center = getWidth() / 2;
		int innerCircle = dip2px(context, 3); //设置内圆半径  
		int ringWidth = dip2px(context, 2); //设置圆环宽度  

		//绘制内圆  
		//this.paint.setARGB(155, 167, 190, 206);
		this.paint.setColor(pointColor);
		this.paint.setStrokeWidth(2);
		canvas.drawCircle(center, center, innerCircle, this.paint);

		//绘制中圆  
		//this.paint.setARGB(255, 212, 225, 233);
		this.paint.setColor(pointColor);
		this.paint.setStrokeWidth(ringWidth);
		canvas.drawCircle(center, center, innerCircle + 1 + ringWidth / 2, this.paint);

		//绘制外圆  
		this.paint1.setARGB(255, 255, 255, 255);
		//paint1.setColor(pointColor);
		this.paint1.setStrokeWidth(5);
		this.paint1.setAlpha(230);
		canvas.drawCircle(center, center, innerCircle + ringWidth, this.paint1);

		super.onDraw(canvas);
	}

	/**
	* @Title: dip2px 
	* @Description: 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	* @param @param context
	* @param @param dpValue
	* @param @return    
	* @return int
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public void setPointerColor(int color) {
		pointColor = color;
		this.paint.setColor(pointColor);
		invalidate();
	}
}
