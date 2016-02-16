package com.x.ui.adapter.gridview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.CategoryBean;
import com.x.publics.model.CategoryInfoBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.ui.adapter.gridview.base.ListAsGridBaseAdapter;

/**
 * @ClassName: CategoryGridListAdapter
 * @Desciption: categoryGridListAdapter 适配器
 
 * @Date: 2014-3-1 下午3:14:04
 */

public class CategoryGridListAdapter extends ListAsGridBaseAdapter {

	private Context context;
	private List<CategoryBean> data = new ArrayList<CategoryBean>();
	private LayoutInflater mInflater;

	public CategoryGridListAdapter(Context context) {
		super(context, R.layout.category_gridview_item);
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	class ViewHolder {
		public ImageView logoIv;
		public TextView categoryNameTv, appRecommendTv;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return getList().size();
	}

	@Override
	protected View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		final CategoryBean categoryInfoBean = data.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.category_gridview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.logoIv = (ImageView) convertView.findViewById(R.id.gl_category_icon_iv);
			viewHolder.categoryNameTv = (TextView) convertView.findViewById(R.id.gl_category_name_tv);
			viewHolder.appRecommendTv = (TextView) convertView.findViewById(R.id.gl_category_app_recommend_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		NetworkImageUtils.load(context, ImageType.NETWORK, categoryInfoBean.getIcon(),
				R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, viewHolder.logoIv);
		viewHolder.categoryNameTv.setText(categoryInfoBean.getName());
		viewHolder.appRecommendTv.setText(categoryInfoBean.getRecommend());

		setSkinTheme(convertView);// set skin theme

		return convertView;
	}

	public void setList(List<CategoryBean> list) {
		this.data = list;
		notifyDataSetChanged();
	}

	public List<CategoryBean> getList() {
		return data;
	}

	private void setSkinTheme(View convertView) {
		View view = convertView.findViewById(R.id.item_gridview_ll);
		SkinConfigManager.getInstance().setViewBackground(context, view, SkinConstan.GRID_VIEW_ITEM_BG);
	}
}
