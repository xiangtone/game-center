/**   
* @Title: AppCollectionAdapter.java
* @Package com.mas.amineappstore.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-2 上午10:56:57
* @version V1.0   
*/

package com.x.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.AppCollectionBean;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.home.HomeCollectionDetailActivity;

/**
* @ClassName: AppCollectionAdapter
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-2 上午10:56:57
* 
*/

public class AppCollectionListAdapter extends ArrayListBaseAdapter<AppCollectionBean> {

	public AppCollectionListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.home_collection_item, null);
		final AppCollectionBean appCollectionBean = mList.get(position);
		AppCollectionViewHolder viewHolder = new AppCollectionViewHolder(convertView);
		viewHolder.initData(context, appCollectionBean);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}

				Intent intent = new Intent(context, HomeCollectionDetailActivity.class);
				intent.putExtra("appCollectionBean", appCollectionBean);
				context.startActivity(intent);
			}
		});

		setSkinTheme(convertView); // set skin theme

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}
}
