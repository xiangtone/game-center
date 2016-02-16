/**   
* @Title: LocalAppListAdapter.java
* @Package com.x.adapter
* @Description: TODO 

* @date 2014-2-10 上午10:27:32
* @version V1.0   
*/

package com.x.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.NetworkImageUtils;

/**
 * 
* @ClassName: FloatAppListAdapter
* @Description: 悬浮窗，本地APP列表Adapter

* @date 2014-5-27 下午1:45:13
*
 */
public class FloatAppListAdapter extends ArrayListBaseAdapter<InstallAppBean> implements SectionIndexer {

	public FloatAppListAdapter(Activity context) {
		super(context);
	}

	public final static class ViewHolder {
		public ImageView appIconIv;
		public TextView appNameTv, tvLetter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final InstallAppBean installAppBean = mList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.local_applist_layout, null);
			viewHolder.appIconIv = (ImageView) convertView.findViewById(R.id.flail_app_icon_iv);
			viewHolder.appNameTv = (TextView) convertView.findViewById(R.id.flail_app_name_tv);
			viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.flail_catalog_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		NetworkImageUtils.load(context, ImageType.APP, installAppBean.getPackageName(),
				R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, viewHolder.appIconIv);
		viewHolder.appNameTv.setText(installAppBean.getAppName());

		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(installAppBean.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		return convertView;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return mList.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}
}
