package com.x.ui.adapter;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.ThemeBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.NetworkImageUtils;

/**
 * @ClassName: WallpaperHotViewHolder
 * @Desciption: 热门壁纸 ViewHolder
 
 * @Date: 2014-3-14 下午1:49:44
 */

public class WallpaperHotViewHolder implements Serializable {

	private static final long serialVersionUID = 1L;
	private ImageView logo;

	/* the method for initialize components */
	public WallpaperHotViewHolder(View view) {
		if (view != null) {
			logo = (ImageView) view.findViewById(R.id.wp_img_logo);
		}
	}

	/* the method for setter Data to components */
	public void initData(WallpaperBean wallpaperBean, Context context) {
		NetworkImageUtils.load(context, ImageType.NETWORK, wallpaperBean.getLogo(), R.drawable.banner_default_picture,
				R.drawable.banner_default_picture, logo);
	}
}
