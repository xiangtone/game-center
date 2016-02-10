/**   
 * @Title: CategoryListAdapter.java
 * @Package com.x.adapter
 * @Description: TODO 
 
 * @date 2014-2-13 下午08:56:14
 * @version V1.0   
 */

package com.x.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.http.model.AppGamesCategoryResponse;
import com.x.publics.model.CategoryBean;
import com.x.publics.model.CategoryBean.SecondaryCatBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.ui.activity.home.CategoryDetailActivity;
import com.x.ui.view.categorygridview.SubCategoryGridView;

/**
 * @ClassName: CategoryListAdapter
 * @Description: TODO
 
 * @date 2014-2-13 下午08:56:14
 * 
 */

public class CategoryListAdapter extends ArrayListBaseAdapter<CategoryBean> {

	private int ct;
	private Context context;
	private AppSubCategoryListAdapter appSubCategoryListAdapter;
	private AppGamesCategoryResponse response;
	private List<SecondaryCatBean> secondaryCatList = new ArrayList<SecondaryCatBean>();

	public AppGamesCategoryResponse getResponse() {
		return response;
	}

	public void setResponse(AppGamesCategoryResponse response) {
		this.response = response;
	}

	public CategoryListAdapter(Activity context) {
		super(context);
	}

	public CategoryListAdapter(Activity context, int ct) {
		super(context);
		this.ct = ct;
		this.context = context;

	}
	 int mgridWidth;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final CategoryBean categoryBean = mList.get(position);
		secondaryCatList = categoryBean.getSecondaryCatList();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.category_item, null);
			viewHolder = new ViewHolder();
			viewHolder.haliAppLayout=(RelativeLayout) convertView
					.findViewById(R.id.hali_app_layout);
			viewHolder.logoIv = (ImageView) convertView
					.findViewById(R.id.ci_category_icon_iv);
			viewHolder.categoryNameTv = (TextView) convertView
					.findViewById(R.id.ci_category_name_tv);
			viewHolder.appRecommendTv = (TextView) convertView
					.findViewById(R.id.ci_category_app_recommend_tv);
			viewHolder.subCategoryGv = (SubCategoryGridView) convertView
					.findViewById(R.id.ci_category_sub_category);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		NetworkImageUtils.load(context, ImageType.NETWORK,
				categoryBean.getIcon(), R.drawable.ic_screen_default_picture,
				R.drawable.ic_screen_default_picture, viewHolder.logoIv);
		viewHolder.categoryNameTv.setText(categoryBean.getName());
		viewHolder.appRecommendTv.setText(categoryBean.getRecommend());
		appSubCategoryListAdapter = new AppSubCategoryListAdapter(
				(Activity) context, ct);

		if (secondaryCatList == null) {
			 viewHolder.subCategoryGv.setVisibility(View.VISIBLE);
			 viewHolder.subCategoryGv.setPadding(8, -8, 8, 0);
			 viewHolder.haliAppLayout.setGravity(Gravity.CENTER_VERTICAL);
	
		}else{
			viewHolder.subCategoryGv.setPadding(8, 16, 8, 8);
		}
		appSubCategoryListAdapter.setList(secondaryCatList);
		viewHolder.subCategoryGv.setAdapter(appSubCategoryListAdapter);

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CategoryDetailActivity.launch(context,
						categoryBean.getCategoryId(), categoryBean.getName(),
						ct);
			}
		});

		setSkinTheme(convertView); // set skin theme

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView,
				SkinConstan.LIST_VIEW_ITEM_BG);
	}

	class ViewHolder {
		public ImageView logoIv;
		public TextView categoryNameTv, appRecommendTv;
		public SubCategoryGridView subCategoryGv;
		public RelativeLayout haliAppLayout;
	}

}
