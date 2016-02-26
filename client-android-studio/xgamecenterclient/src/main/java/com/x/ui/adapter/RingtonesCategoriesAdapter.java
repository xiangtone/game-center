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
import com.x.publics.model.CategoryInfoBean;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.ringtones.RingtonesCategoryActivity;

/**
 * 
 
 * 
 */
public class RingtonesCategoriesAdapter extends ArrayListBaseAdapter<CategoryInfoBean> {

	// constructor
	public RingtonesCategoriesAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.fragment_ringtones_categories_item, null);

		// initialize CommentBean Object to used
		final CategoryInfoBean categoryInfoBean = mList.get(position);
		RingtonesCategoriesHolder holder = new RingtonesCategoriesHolder(convertView);
		holder.initData(categoryInfoBean, context);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// check network
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}
				Intent intent = new Intent(context, RingtonesCategoryActivity.class);
				intent.putExtra("categoryId", categoryInfoBean.getCategoryId());
				intent.putExtra("categoryName", categoryInfoBean.getName());
				context.startActivity(intent);
			}
		});

		setSkinTheme(convertView);// set skin theme

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}
}
