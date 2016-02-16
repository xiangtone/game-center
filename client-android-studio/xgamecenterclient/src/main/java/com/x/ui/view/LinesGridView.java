package com.x.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.x.R;

/**
 * 
* @ClassName: LinesGridView
* @Description: 自定义带细边框的GridView

* @date 2014-4-25 下午6:05:28
*
 */
public class LinesGridView extends GridView {
	public LinesGridView(Context context) {
		super(context);
	}

	public LinesGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LinesGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		View view = getChildAt(0);
		if (view != null) {
			int column = getWidth() / view.getWidth();
			int childCount = getChildCount();
			Paint localPaint;
			localPaint = new Paint();
			localPaint.setStyle(Paint.Style.STROKE);
			localPaint.setColor(getContext().getResources().getColor(R.color.gray));
			for (int i = 0; i < childCount; i++) {
				View cellView = getChildAt(i);
				if ((i + 1) % column == 0) {
					canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(),
							cellView.getBottom(), localPaint);
				} else if ((i + 1) > (childCount - (childCount % column))) {
					canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(),
							localPaint);
				} else {
					canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(),
							localPaint);
					canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(),
							cellView.getBottom(), localPaint);
				}
			}
		}
	}
}
