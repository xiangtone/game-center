/**   
* @Title: UninstallAppsAdapter.java
* @Package com.mas.amineappstore.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-8 下午3:45:15
* @version V1.0   
*/

package com.x.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.x.R;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.PackageUtil;

/**
* @ClassName: UninstallAppsAdapter
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-10-8 下午3:45:15
* 
*/

public class UninstallAppsAdapter extends ArrayListBaseAdapter<InstallAppBean> implements SectionIndexer {

	public UninstallAppsAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.uninstall_item, null);
			}
			final InstallAppBean appsBean = mList.get(position);
			final UninstallAppsViewHolder holder = new UninstallAppsViewHolder(convertView);
			holder.initData(appsBean, context);
			holder.uninstallTv.setOnClickListener(new MyBtnListener(appsBean));
			holder.setSkinTheme(context);

			convertView.setOnClickListener(null);

			//根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);

			//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				holder.catalogTv.setVisibility(View.VISIBLE);
				holder.catalogTv.setText(appsBean.getSortLetters());
			} else {
				holder.catalogTv.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	/**
	* @ClassName: LvItemLister
	* @Description: TODO(在此删除某个应用)
	
	* @date 2014-10-10 下午2:08:43
	*
	 */
	private class MyBtnListener implements OnClickListener {
		InstallAppBean appsBean;

		public MyBtnListener(InstallAppBean appsBean) {
			this.appsBean = appsBean;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.uninstallapp_tv:
				PackageUtil.unstallApk(context, appsBean.getPackageName());
				break;
			default:
				break;
			}

		}
	}

	@Override
	public Object[] getSections() {
		return null;
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
}
