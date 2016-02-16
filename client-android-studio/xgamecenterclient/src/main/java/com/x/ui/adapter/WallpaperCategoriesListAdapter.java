package com.x.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.x.R;
import com.x.publics.model.CategoryInfoBean;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.wallpaper.WallpaperCategoryActivity;

/**
 * @ClassName: WallpaperCategoriesListAdapter
 * @Desciption: 壁纸分类适配器 
 
 * @Date: 2014-3-14 下午4:00:03
 */

public class WallpaperCategoriesListAdapter extends ArrayListBaseAdapter<CategoryInfoBean> {

	// constructor
	public WallpaperCategoriesListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.wallpaper_categories_item, null);

		// initialize CommentBean Object to used
		final CategoryInfoBean categoryInfoBean = mList.get(position);
		final WallpaperCategoriesViewHolder holder = new WallpaperCategoriesViewHolder(convertView);
		holder.initData(categoryInfoBean, context);
		
		// 按钮触摸事件
		holder.logo.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					holder.logo.setAlpha(255);
					// check network
					if (!NetworkUtils.isNetworkAvailable(context)) {
						ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return true;
					}
					Intent intent = new Intent(context,WallpaperCategoryActivity.class);
					intent.putExtra("categoryId", categoryInfoBean.getCategoryId());
					intent.putExtra("categoryName", categoryInfoBean.getName());
					context.startActivity(intent);
					break;
					
				case MotionEvent.ACTION_MOVE:
					// imageView.setAlpha(255);
					break;
					
				case MotionEvent.ACTION_DOWN:
					holder.logo.setAlpha(180);
					break;

				case MotionEvent.ACTION_CANCEL:
					holder.logo.setAlpha(255);
					break;

				default:
					break;
				}

				return true;
			}
		});
		
		return convertView;
	}

}
