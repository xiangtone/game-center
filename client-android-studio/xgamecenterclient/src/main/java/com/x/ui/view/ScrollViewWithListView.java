package com.x.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @ClassName: ScrollViewWithListView
 * @Desciption: TODO
 
 * @Date: 2014-3-12 下午2:06:59
 */
public class ScrollViewWithListView extends ListView {

	public ScrollViewWithListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	/**
	 * Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条的高度
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
