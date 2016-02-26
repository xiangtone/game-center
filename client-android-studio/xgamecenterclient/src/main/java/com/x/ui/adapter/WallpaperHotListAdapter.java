package com.x.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.x.R;
import com.x.publics.model.WallpaperBean;

/**
 * @ClassName: WallpaperHotListAdapter
 * @Desciption: 热门壁纸适配器 
 
 * @Date: 2014-3-14 下午4:00:03
 */

public class WallpaperHotListAdapter extends ArrayListBaseAdapter<WallpaperBean> {

	// constructor
	public WallpaperHotListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.wallpaper_hot_item, null);

		// initialize CommentBean Object to used
		WallpaperBean wallpaperBean = mList.get(position);
		WallpaperHotViewHolder holder = new WallpaperHotViewHolder(convertView);
		holder.initData(wallpaperBean, context);

		return convertView;
	}

}
