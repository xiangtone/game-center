package com.x.ui.adapter;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.CategoryInfoBean;
import com.x.publics.utils.NetworkImageUtils;

/**
 * @ClassName: WallpaperCategoriesViewHolder
 * @Desciption: 壁纸分类 ViewHolder
 
 * @Date: 2014-3-14 下午1:49:44
 */

public class WallpaperCategoriesViewHolder implements Serializable {

	private static final long serialVersionUID = 1L;
	public ImageView logo;
	public TextView categoryName;

	/* the method for initialize components */
	public WallpaperCategoriesViewHolder(View view) {
		if (view != null) {
			categoryName = (TextView) view.findViewById(R.id.wp_tv_category);
			logo = (ImageView) view.findViewById(R.id.wp_img_category);
		}
	}

	/* the method for setter Data to components */
	public void initData(CategoryInfoBean categoryInfoBean, Context context) {
		categoryName.setText(categoryInfoBean.getName());
		NetworkImageUtils.load(context, ImageType.NETWORK, categoryInfoBean.getBigicon(), R.drawable.banner_default_picture,
				R.drawable.banner_default_picture, logo);
	}
}
