/**   
* @Title: LocalAppListAdapter.java
* @Package com.mas.amineappstore.adapter
* @Description: TODO 

* @date 2014-2-10 上午10:27:32
* @version V1.0   
*/

package com.x.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;

/**
* @ClassName: LocalAppListAdapter
* @Description: TODO 

* @date 2014-2-10 上午10:27:32
* 
*/

public class LocalAppListAdapter extends ArrayListBaseAdapter<InstallAppBean> implements SectionIndexer {

	public LocalAppListAdapter(Activity context) {
		super(context);
	}

	public final static class ViewHolder {
		public ImageView appIconIv, arrowIv;
		public TextView appNameTv, appVersionTv, appSizeTv, uninstallTv, tvLetter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final InstallAppBean installAppBean = mList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.local_app_item_layout, null);
			viewHolder.appIconIv = (ImageView) convertView.findViewById(R.id.lail_app_icon_iv);
			viewHolder.arrowIv = (ImageView) convertView.findViewById(R.id.lail_arrow_iv);
			viewHolder.appNameTv = (TextView) convertView.findViewById(R.id.lail_app_name_tv);
			viewHolder.appVersionTv = (TextView) convertView.findViewById(R.id.lail_app_version_tv);
			viewHolder.appSizeTv = (TextView) convertView.findViewById(R.id.lail_app_size_tv);
			viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.lail_catalog_tv);
			viewHolder.uninstallTv = (TextView) convertView.findViewById(R.id.lail_uninstall_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
		viewHolder.uninstallTv.setOnClickListener(new MyBtnListener(viewHolder, installAppBean));
		NetworkImageUtils.load(context, ImageType.APP, installAppBean.getPackageName(),
				R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, viewHolder.appIconIv);
		viewHolder.appNameTv.setText(installAppBean.getAppName());
		viewHolder.appVersionTv.setText(ResourceUtil.getString(context, R.string.app_version) + installAppBean.getVersionName());
		viewHolder.appSizeTv.setText(installAppBean.getFileSize() == 0 ? "" : ResourceUtil.getString(context, R.string.app_size)
				+ Utils.sizeFormat(Long.valueOf(installAppBean.getFileSize())));

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

	private class MyBtnListener implements OnClickListener {
		ViewHolder viewHolder;
		InstallAppBean infoBean;

		public MyBtnListener(ViewHolder viewHolder, InstallAppBean infoBean) {
			this.viewHolder = viewHolder;
			this.infoBean = infoBean;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.lail_uninstall_tv:
				//				Utils.launchAnotherApp(mContext, infoBean.getPackageName());
				PackageUtil.unstallApk(context, infoBean.getPackageName());
				break;
			}

		}

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
