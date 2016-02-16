/**   
 * @Title: SubCategoryGridView.java
 * @Package com.x.ui.view.categorygridview
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2015-10-19 下午6:06:14
 * @version V1.0   
 */

package com.x.ui.view.categorygridview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @ClassName: SubCategoryGridView
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2015-10-19 下午6:06:14
 * 
 */

public class SubCategoryGridView extends GridView {
	int expandSpec;
	public SubCategoryGridView(Context context) {
		super(context);
	}

	public SubCategoryGridView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public SubCategoryGridView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SubCategoryGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(getLayoutParams().height == LayoutParams.WRAP_CONTENT){
			expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
					MeasureSpec.AT_MOST);
		} else {
			expandSpec = heightMeasureSpec;
        }
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
	
}
