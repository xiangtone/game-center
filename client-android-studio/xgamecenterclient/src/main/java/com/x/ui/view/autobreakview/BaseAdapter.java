package com.x.ui.view.autobreakview;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

/**
 * 
 * @ClassName: BaseAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-8-4 上午11:01:51
 * 
 */
public class BaseAdapter {

	private View myView;
	private ViewGroup myViewGroup;
	private AutoBreakListView mListView;
	private AutoBreakOnItemClickListener listener;
	private AutoBreakOnItemLongClickListener longListener;

	public int getCount() {
		return 0;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		return null;
	}

	/**
	 * 
	 * Add all the View controls to the custom SexangleViewList When you use
	 * this SexangleViewList should be instantiated first and then call Because
	 * here is not intercept and throw such as null pointer exception The name
	 * is called mySexangleView View passed in must be empty Of course the
	 * ViewGroup transfer time must also be empty
	 * 
	 */
	private final void getAllViewAddSexangle() {
		this.mListView.removeAllViews();
		for (int i = 0; i < getCount(); i++) {
			View viewItem = getView(i, this.myView, this.myViewGroup);
			this.mListView.addView(viewItem, i);
		}
	}

	/**
	 * 
	 * The refresh SexangleListView interface Here is set to True representative
	 * will execute reset CustomListView twice This method is called before,
	 * please first instantiation mySexangleListView Otherwise, this method in
	 * redraw CustomListView abnormal happens
	 * 
	 */
	public void notifyDataSetChanged() {
		AutoBreakListView.setAddChildType(true);
		notifyCustomListView(this.mListView);
	}

	/**
	 * Redraw the Custom controls for the first time, you should invoke this
	 * method In order to ensure that each load data do not repeat to get rid of
	 * the custom of the ListView all View objects The following will be set up
	 * to monitor events as controls First load regardless whether
	 * OnItemClickListener and OnItemLongClickListener is NULL, they do not
	 * influence events Settings
	 * 
	 * @param formateList
	 */
	public void notifyCustomListView(AutoBreakListView formateList) {
		this.mListView = formateList;
		mListView.removeAllViews();
		getAllViewAddSexangle();
		setOnItemClickListener(listener);
		setOnItemLongClickListener(longListener);
	}

	/**
	 * Set the click event of each View, external can realize the interface for
	 * your call
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(
			final AutoBreakOnItemClickListener listener) {
		this.listener = listener;
		for (int i = 0; i < mListView.getChildCount(); i++) {
			final int parame = i;
			View view = mListView.getChildAt(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onItemClick(null, v, parame, getCount());
				}
			});
		}
	}

	/**
	 * Set each long press event, the View outside can realize the interface for
	 * your call
	 * 
	 * @param listener
	 */
	public void setOnItemLongClickListener(
			final AutoBreakOnItemLongClickListener listener) {
		this.longListener = listener;
		for (int i = 0; i < mListView.getChildCount(); i++) {
			final int parame = i;
			View view = mListView.getChildAt(i);
			view.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// listener.onItemLongClick(null, v, parame, getCount());
					return true;
				}
			});
		}
	}

}