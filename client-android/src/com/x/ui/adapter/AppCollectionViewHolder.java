/**   
* @Title: AppCollectionViewHolder.java
* @Package com.x.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-2 下午2:04:49
* @version V1.0   
*/

package com.x.ui.adapter;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.AppCollectionBean;
import com.x.publics.model.ThemeBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.SharedPrefsUtil;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
* @ClassName: AppCollectionViewHolder
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-2 下午2:04:49
* 
*/

public class AppCollectionViewHolder {

	public ImageView collectionLogoIv;
	public TextView collectionNameTv, collectionBriefTv, collectionDateTv;
	private boolean hasInited;

	public AppCollectionViewHolder(View view) {
		if (view != null) {
			collectionLogoIv = (ImageView) view.findViewById(R.id.hci_collection_logo_iv);
			collectionNameTv = (TextView) view.findViewById(R.id.hci_collection_name_tv);
			collectionBriefTv = (TextView) view.findViewById(R.id.hci_collection_brief_tv);
			collectionDateTv = (TextView) view.findViewById(R.id.hci_collection_date_tv);
			hasInited = true;
		}
	}

	public void initData(Context context, AppCollectionBean appCollectionBean) {
		if (hasInited && appCollectionBean != null) {
			NetworkImageUtils.load(context, ImageType.NETWORK, appCollectionBean.getBigicon(), R.drawable.banner_default_picture,
					R.drawable.banner_default_picture, collectionLogoIv);
			collectionNameTv.setText(appCollectionBean.getName());
			collectionBriefTv.setText(appCollectionBean.getDescription());
			collectionDateTv.setText(appCollectionBean.getPublishTime());
		}
	}
}
