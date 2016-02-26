/**   
* @Title: DynamicListView.java
* @Package com.x.ui.view.dynamic
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-3-23 下午2:38:43
* @version V1.0   
*/

package com.x.ui.view.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 
* @ClassName: DynamicListView
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-3-23 下午2:38:43
*
 */
public class DynamicListView extends ListView {

	private static final int NEW_ROW_DURATION = 1000;

	private DynamicListViewAdapter adapter;
	private OnRowAdditionAnimationListener mRowAdditionAnimationListener;
	private List<DynamicListViewItem> mData = new ArrayList<DynamicListViewItem>();

	public DynamicListView(Context context) {
		super(context);
		init(context);
	}

	public DynamicListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DynamicListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void init(Context context) {
		setDivider(null);
	}

	/** Setter for the underlying data set controlling the adapter. */
	public void setData(List<DynamicListViewItem> list) {
		mData = list;
	}

	public void setRowAdditionAnimationListener(OnRowAdditionAnimationListener rowAdditionAnimationListener) {
		mRowAdditionAnimationListener = rowAdditionAnimationListener;
	}

	/** 
	* @Title: addListViewItem 
	* @Description: TODO 
	* @param @param DynamicListViewItem    
	* @return void    
	*/
	public void addListViewItem(DynamicListViewItem newObj) {

		adapter = (DynamicListViewAdapter) getAdapter();

		/**
		 * Stores the starting bounds and the corresponding bitmap drawables of every
		 * cell present in the ListView before the data set change takes place.
		 */
		final HashMap<Long, Rect> listViewItemBounds = new HashMap<Long, Rect>();

		int firstVisiblePosition = getFirstVisiblePosition();
		for (int i = 0; i < getChildCount(); ++i) {
			View child = getChildAt(i);
			int position = firstVisiblePosition + i;
			long itemID = adapter.getItemId(position);
			Rect startRect = new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
			listViewItemBounds.put(itemID, startRect);
		}

		mData.add(0, newObj);
		adapter.addStableIdForDataAtPosition(0);
		adapter.notifyDataSetChanged();

		final ViewTreeObserver observer = getViewTreeObserver();
		observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				getViewTreeObserver().removeOnPreDrawListener(this);

				ArrayList<Animator> animations = new ArrayList<Animator>();

				int firstVisiblePosition = getFirstVisiblePosition();

				/** Loops through all the current visible cells in the ListView and animates
				 * all of them into their post layout positions from their original positions.*/
				for (int i = 0; i < getChildCount(); i++) {
					View child = getChildAt(i);
					int position = firstVisiblePosition + i;
					long itemId = adapter.getItemId(position);
					Rect startRect = listViewItemBounds.get(itemId);
					int top = child.getTop();
					if (startRect != null) {
						/** If the cell was visible before the data set change and
						 * after the data set change, then animate the cell between
						 * the two positions.*/
						int startTop = startRect.top;
						int delta = startTop - top;
						ObjectAnimator animation = ObjectAnimator.ofFloat(child, "translationY", delta, 0);
						animations.add(animation);
					} else {
						/** If the cell was not visible (or present) before the data set
						 * change but is visible after the data set change, then use its
						 * height to determine the delta by which it should be animated.*/
						int childHeight = child.getHeight() + getDividerHeight();
						int startTop = top + (i > 0 ? childHeight : -childHeight);
						int delta = startTop - top;
						ObjectAnimator animation = ObjectAnimator.ofFloat(child, "translationY", delta, 0);
						animations.add(animation);
					}
					listViewItemBounds.remove(itemId);
				}

				/** Animates all the cells from their old position to their new position
				 *  at the same time.*/
				setEnabled(false);
				mRowAdditionAnimationListener.onRowAdditionAnimationStart();
				AnimatorSet set = new AnimatorSet();
				set.setDuration(NEW_ROW_DURATION);
				set.playTogether(animations);
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mRowAdditionAnimationListener.onRowAdditionAnimationEnd();
						setEnabled(true);
						invalidate();
					}
				});
				set.start();

				listViewItemBounds.clear();

				return true;
			}
		});
	}

	/**
	* @Title: showItemCount 
	* @Description: TODO 
	* @param @param count    
	* @return void
	 */
	public void showItemCount(int count) {
		int size = mData.size();
		if (adapter == null)
			adapter = (DynamicListViewAdapter) getAdapter();
		if (size > count) {
			adapter.remove(mData.get(count));
			adapter.notifyDataSetChanged();
		}
	}
}
