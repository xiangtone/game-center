package com.hykj.gamecenter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/***
 * 自定义ListView子类，继承ListView
 * 
 * @author Administrator
 * 
 */
// Android实现 ScrollView+ListView无滚动条滚动，即ListView的数据会全部显示完，但Listview无滚动条
public class NoScrollListView extends ListView {

	public NoScrollListView(Context context) {
		super(context);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/*
	 * 这个方法是解决 listView 与 ScrollView 滚动冲突的问题
	 * 
	 * @Override public void onMeasure(int widthMeasureSpec, int
	 * heightMeasureSpec) { int expandSpec =
	 * MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
	 * super.onMeasure(widthMeasureSpec, expandSpec); }
	 */

}
