/**   
* @Title: DynamicListViewHolder.java
* @Package com.mas.amineappstore.ui.view.dynamic
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-3-23 下午2:39:57
* @version V1.0   
*/

package com.x.ui.view.dynamic;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.dynamiclistview.DynamicListViewManager;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;

/**
 * 
* @ClassName: DynamicListViewHolder
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-3-23 下午2:39:57
*
 */

public class DynamicListViewHolder {

	private View lineView;
	public PointerView pointView;
	public TextView appName, countryName;
	public ImageView appLogo, countryLogo;
	private boolean inited = false;

	public DynamicListViewHolder(View view, boolean isDetailLayout) {
		if (null != view) {
			if (isDetailLayout) {
				pointView = (PointerView) view.findViewById(R.id.pointView);
				lineView = view.findViewById(R.id.dynamic_line);
			}
			appName = (TextView) view.findViewById(R.id.tv_app_name);
			countryName = (TextView) view.findViewById(R.id.tv_country_name);
			appLogo = (ImageView) view.findViewById(R.id.iv_app_logo);
			countryLogo = (ImageView) view.findViewById(R.id.iv_country_logo);
			inited = true;
		}
	}

	public void initData(Context context, DynamicListViewItem objectItem, boolean isDetailLayout, int position) {
		if (inited) {
			appName.setText(objectItem.getAppName());
			if (isDetailLayout) {
				if (position == 0) {
					Utils.setBackgroundDrawable(lineView,
							ResourceUtil.getDrawable(context, R.drawable.downingshow_line));
					pointView.setPointerColor(ResourceUtil.getInteger(context, R.color.white));
				} else {
					Utils.setBackgroundDrawable(lineView,
							ResourceUtil.getDrawable(context, R.drawable.downingshow_line_repeat));
					pointView.setPointerColor(ResourceUtil.getInteger(context, DynamicListViewManager.getInstance()
							.getColorId(context)));
				}
				countryName.setText(ResourceUtil.getString(context, R.string.country_title2,
						objectItem.getCountryName()));
			} else {
				countryName.setText(ResourceUtil.getString(context, R.string.country_title1,
						objectItem.getCountryName()));
			}

			NetworkImageUtils.load(context, ImageType.NETWORK, objectItem.getAppIcon(),
					R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appLogo);
			NetworkImageUtils.load(context, ImageType.NETWORK, objectItem.getCountryIcon(),
					R.drawable.ic_default_countrylogo, R.drawable.ic_default_countrylogo, countryLogo);
		}
	}

}
