package com.x.ui.adapter.gridview.base;

import android.view.View;

/**
 * @ClassName: GridItemClickListener
 * @Desciption: gridView Item 监听事件
 
 * @Date: 2014-3-1 下午3:10:52
 */
public interface GridItemClickListener {

	void onGridItemClicked(View v, int position, long itemId);

}