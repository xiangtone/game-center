package com.x.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.RecommendBean;
import com.x.publics.utils.NetworkImageUtils;

import java.io.Serializable;

/**
 * @ClassName: RecommendViewHolder
 * @Desciption: 应用推荐
 
 * @Date: 2014-1-28 下午3:43:58
 */

public class RecommendViewHolder implements Serializable {

	private static final long serialVersionUID = 7508016668100195004L;
	public TextView appName;
	public ImageView appIcon;

	public RecommendViewHolder(View view) {
		if (view != null) {
			appIcon = (ImageView) view.findViewById(R.id.img_app_icon);
			appName = (TextView) view.findViewById(R.id.tv_app_name);
		}
	}

	public void initData(RecommendBean recommendBean, Context context) {
		NetworkImageUtils.load(context, ImageType.NETWORK, recommendBean.getLogo(),
				R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIcon);
		appName.setText(recommendBean.getAppName());
	}

}
