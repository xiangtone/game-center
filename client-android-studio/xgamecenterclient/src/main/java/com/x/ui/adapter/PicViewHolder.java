package com.x.ui.adapter;

import java.io.Serializable;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.PictureBean;
import com.x.publics.utils.NetworkImageUtils;

/**
 * @ClassName: picViewHolder
 * @Desciption: 应用图片 的 viewHolder
 
 * @Date: 2014-2-13 下午5:19:10
 */

public class PicViewHolder implements Serializable {

	private static final long serialVersionUID = 1L;
	private ImageView picture;

	/* the method for initialize components */
	public PicViewHolder(View view) {
		if (view != null) {
			picture = (ImageView) view.findViewById(R.id.img_app_picture);
		}
	}

	/* the method for setter Data to components */
	public void initData(PictureBean pictureBean, Context context) {
		
		NetworkImageUtils.load(context, ImageType.NETWORK, pictureBean.getUrl(), R.drawable.banner_default_picture,
				R.drawable.banner_default_picture, picture);
	}
}
