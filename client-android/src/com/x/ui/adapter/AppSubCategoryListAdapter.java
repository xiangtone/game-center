/**   
 * @Title: AppCategoryListAdapter.java
 * @Package com.x.ui.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2015-10-19 下午2:31:02
 * @version V1.0   
 */

package com.x.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.CategoryBean.SecondaryCatBean;
import com.x.ui.activity.home.CategoryDetailActivity;

/**
 * @ClassName: AppCategoryListAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2015-10-19 下午2:31:02
 * 
 */

public class AppSubCategoryListAdapter extends
		ArrayListBaseAdapter<SecondaryCatBean> {
	private int ct;

	public AppSubCategoryListAdapter(Activity context, int ct) {
		super(context);
		this.ct = ct;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		final SecondaryCatBean secondaryCatBean = mList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.sub_category_list_item,
					null);
			viewHolder.subCategoryNameTv = (TextView) convertView
					.findViewById(R.id.sub_category_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position != parent.getChildCount()) {
			return convertView;
		}
		viewHolder.subCategoryNameTv.setText(secondaryCatBean.getName());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				CategoryDetailActivity.launch(context,
						secondaryCatBean.getCategoryId(),
						secondaryCatBean.getName(), ct);
			}
		});
		// setSkinTheme(convertView); // set skin theme
		return convertView;
	}

	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView,
				SkinConstan.LIST_VIEW_ITEM_BG);
	}

	class ViewHolder {
		public TextView subCategoryNameTv;
	}
}
