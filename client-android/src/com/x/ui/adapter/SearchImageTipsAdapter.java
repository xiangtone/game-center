/**   
 * @Title: SearchTipsAdapter.java
 * @Package com.x.ui.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-7-30 下午2:54:28
 * @version V1.0   
 */

package com.x.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.WallpaperBean;

/**
 * @ClassName: SearchTipsAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-30 下午2:54:28
 * 
 */

public class SearchImageTipsAdapter extends ArrayListBaseAdapter<WallpaperBean> {

	public SearchImageTipsAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		public TextView tipsName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		WallpaperBean wallpaperBean = mList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.search_tips_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tipsName = (TextView) convertView.findViewById(R.id.tv_tips_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tipsName.setText(wallpaperBean.getImageName());

		setSkinTheme(convertView);// set skin theme

		return convertView;
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @param @param convertView    
	* @return void    
	*/
	private void setSkinTheme(View convertView) {
		SkinConfigManager.getInstance().setViewBackground(context, convertView, SkinConstan.LIST_VIEW_ITEM_BG);
	}

}
