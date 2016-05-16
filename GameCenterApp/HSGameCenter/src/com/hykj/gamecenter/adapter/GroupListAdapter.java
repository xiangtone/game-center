package com.hykj.gamecenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hykj.gamecenter.R;
import com.hykj.gamecenter.utils.Tools;

public abstract class GroupListAdapter extends BaseAdapter {

	private static String TAG = "GroupListAdapter1";
	protected final Context mContext;
	// private View titleView;
	// protected Handler mHandler;
	protected int mColumnCount = 2;
	protected final int mAppType;
	Resources mRes = null;

	private static final LinearLayout.LayoutParams llp_padding = new LayoutParams(
			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	private static final LinearLayout.LayoutParams llp = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

	public GroupListAdapter(Context context, int appType, int columnCount) {
		super();

		mContext = context;
		mColumnCount = columnCount;
		mAppType = appType;
		llp.weight = 1;

		mRes = context.getResources();

		llp.weight = 1;
		int margins = (int) mRes.getDimension(R.dimen.csl_cs_padding_size);
		int marginBottom = (int) mRes
				.getDimension(R.dimen.list_item_padding_bottom);
		llp.setMargins(0, margins, 0, marginBottom);// 只填充了左margin

	}

	@Override
	public int getCount() {
		int nCount = (int) Math.ceil(getAllCount() * 1.0 / mColumnCount);
		// Log.d("GridAdapter", "getCount =" + nCount);
		return nCount;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setColumnCount(int count) {
		mColumnCount = count;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout lineContainer = (LinearLayout) convertView;
		// LinearLayout topContent = null;

		int nItemWidth = 0;

		// 首先创建带有上下分割线的的整行的Layout

		if (lineContainer == null) {

			lineContainer = new LinearLayout(mContext);
			lineContainer.setOrientation(LinearLayout.HORIZONTAL);
			lineContainer.setGravity(Gravity.CENTER_VERTICAL);

		} else {
			lineContainer.removeAllViews();
		}

		// 获取或者初始化重用的itemview和divider的view
		ArrayList<View> viewHolder = (ArrayList<View>) lineContainer
				.getTag(R.id.tag_viewHolder);
		if (viewHolder == null) {
			viewHolder = new ArrayList<View>();
			lineContainer.setTag(R.id.tag_viewHolder, viewHolder);
		}

		// 对每一行，生成单独的实际itemview加入上面创建的layout中
		int index = position * mColumnCount;
		int end = index + mColumnCount;
		for (; index < end && index < getAllCount(); index++) {
			int posInLine = mColumnCount - (end - index);
			View itemView = null;

			if (viewHolder.size() > posInLine) {
				itemView = viewHolder.get(posInLine);
				itemView = getPeaceView(index, itemView, lineContainer);
			} else {
				itemView = getPeaceView(index, itemView, lineContainer);
				viewHolder.add(itemView);
			}

			if (itemView != null) {
				/*
				 * int w = View.MeasureSpec.makeMeasureSpec(0,
				 * View.MeasureSpec.UNSPECIFIED);
				 * 
				 * int h = View.MeasureSpec.makeMeasureSpec(0,
				 * View.MeasureSpec.UNSPECIFIED);
				 * 
				 * itemView.measure(w, w); // int height =
				 * itemView.getMeasuredHeight(); int width =
				 * itemView.getMeasuredWidth();
				 * 
				 * 不需要测量，固定宽度
				 */

				int screenWidth = Tools.getDisplayWidth(mContext);

				// getDimension返回的是绝对尺寸，而不是相对尺寸（dp\sp等)
				int margins = (int) mRes
						.getDimension(R.dimen.csl_cs_padding_size);
				int ParentWidth = screenWidth - 2 * margins;
				// listItem间距
				int iPaddingWidth = 0;
				if (mColumnCount > 1) {
					iPaddingWidth = (int) mRes
							.getDimension(R.dimen.list_view_item_spacing);
				} else {
					iPaddingWidth = 0;
				}

				int MaginW = 0;
				// item宽度
				int iItemWidth = (int) ((ParentWidth - MaginW * mColumnCount - iPaddingWidth
						* (mColumnCount - 1))
						* 1.0f / mColumnCount + 0.5f);
				// 固定宽布局
				LinearLayout.LayoutParams llp_appFixedWidth = new LayoutParams(
						iItemWidth, LayoutParams.WRAP_CONTENT);

				int marginBottom = (int) mRes
						.getDimension(R.dimen.list_item_padding_bottom);
				llp_appFixedWidth.setMargins(0, margins, 0, marginBottom);// 只填充了左margin

				lineContainer.addView(itemView, llp_appFixedWidth);

				// 加入填充布局
				if (index >= 0 && index < end - 1) {
					TextView tv = new TextView(mContext);
					tv.setWidth(iPaddingWidth);
					// tv.setBackgroundResource(R.color.cs_btn_red_normal);
					lineContainer.addView(tv, llp_padding);
				}
			}

		}
		// 当一行未填满，填充空白view
		if (index < end) {
			for (int blankNum = end - index - 1; blankNum >= 0; blankNum--) {
				View bk = genBlankView();
				lineContainer.addView(bk, llp);
			}
		}
		return lineContainer;
	}

	// public void setHandler( Handler handler )
	// {
	// mHandler = handler;
	// }

	/**
	 * 获取列表数据项的总长度
	 * 
	 * @return
	 */
	public abstract int getAllCount();

	/**
	 * 获取每个数据项的视图
	 * 
	 * @param positionInTotal
	 * @param convertView
	 * @param parentView
	 * @return
	 */
	public abstract View getPeaceView(int positionInTotal, View convertView,
			ViewGroup parentView);

	protected View getHorizontalDivider() {
		ImageView imageView = new ImageView(mContext);
		// imageView.setBackgroundResource(R.drawable.divider_horizontal);
		// imageView.setBackgroundResource( R.color.background );
		LinearLayout.LayoutParams llp = getHorizontalDiveiderLayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(llp);
		return imageView;
	}

	private LinearLayout.LayoutParams getHorizontalDiveiderLayoutParams(
			int nWidth) {
		LinearLayout.LayoutParams llp;
		int hight = mContext.getResources().getDimensionPixelSize(
				R.dimen.csl_cs_padding_half_size);
		llp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, hight);

		return llp;
	}

	//
	// protected View getVerticalDivider()
	// {
	// ImageView imageView = new ImageView( mContext );
	// LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams( 6 ,
	// android.view.ViewGroup.LayoutParams.MATCH_PARENT );
	// // imageView.setBackgroundResource(R.drawable.divider_vertical);
	// imageView.setBackgroundResource( R.color.background );
	// imageView.setLayoutParams( llp );
	// return imageView;
	// }

	protected View genBlankView() {
		View blankView = new LinearLayout(mContext);
		return blankView;
	}

}
