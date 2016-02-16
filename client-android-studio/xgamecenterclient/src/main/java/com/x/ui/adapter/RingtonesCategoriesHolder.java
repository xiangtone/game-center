package com.x.ui.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.CategoryInfoBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.ui.view.MyImageView;
import com.x.ui.view.MyImageView.OnMeasureListener;

/**
 * 
 
 * 
 */
public class RingtonesCategoriesHolder {

	private Point mPoint = new Point(0, 0);// 封装ImageView的宽和高
	public MyImageView image;
	public TextView name_num;

	public RingtonesCategoriesHolder(View view) {
		if (view != null) {
			image = (MyImageView) view.findViewById(R.id.ring_category_image);
			name_num = (TextView) view.findViewById(R.id.ring_category_name);

			image.setOnMeasureListener(new OnMeasureListener() {

				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
		}
	}

	public void initData(CategoryInfoBean categoryInfoBean, Context context) {
		NetworkImageUtils.load(context, ImageType.NETWORK,
				categoryInfoBean.getBigicon(),
				R.drawable.banner_default_picture,
				R.drawable.banner_default_picture, image);
		name_num.setText(categoryInfoBean.getName());
	}
}
