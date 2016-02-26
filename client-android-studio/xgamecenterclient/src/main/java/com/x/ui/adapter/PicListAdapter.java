package com.x.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.x.R;
import com.x.publics.model.PictureBean;

/**
 * @ClassName: picListAdapter
 * @Desciption: 应用图片列表（水平）适配器
 
 * @Date: 2014-2-13 下午5:15:44
 */

public class PicListAdapter extends ArrayListBaseAdapter<PictureBean> {

	// constructor
	public PicListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		PictureBean pictureBean = mList.get(position);
		
		if (pictureBean.getLength() < pictureBean.getWidth()) {
			// 320 * 240 horizontal 
			convertView = inflater.inflate(R.layout.app_detail_horizontal_listview_item_horizontal, null);
		} else {
			// 240 * 320 vertical
			convertView = inflater.inflate(R.layout.app_detail_horizontal_listview_item_vertical, null);
		}

		PicViewHolder holder = new PicViewHolder(convertView);
		holder.initData(pictureBean, context);

		return convertView;
	}

}
