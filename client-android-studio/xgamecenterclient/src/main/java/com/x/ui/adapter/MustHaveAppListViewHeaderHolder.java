/**   
* @Title: AppListViewHolder.java
* @Package com.x.adapter
* @Description: TODO 

* @date 2013-12-23 上午10:30:31
* @version V1.0   
*/

package com.x.ui.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.http.model.HomeMustHaveResponse.MustHaveCategoryList;
import com.x.publics.model.AppInfoBean;
import com.x.publics.utils.NavigationUtils;
import com.x.publics.utils.NetworkImageUtils;
import com.x.ui.view.floatsticklist.WrapperExpandableListAdapter;

/**
* @ClassName: AppListViewHolder
* @Description: AppListAdapter 的ViewHolder 

* @date 2013-12-23 上午10:30:31
* 
*/

public class MustHaveAppListViewHeaderHolder {
	private boolean hasInited;
	private HashSet<Integer> expandCategoryIdSet;

	int[] appRlItemId = { R.id.imhha_app_item_rl01, R.id.imhha_app_item_rl02, R.id.imhha_app_item_rl03,
			R.id.imhha_app_item_rl04 };
	int[] appIconItemId = { R.id.img_app_icon01, R.id.img_app_icon02, R.id.img_app_icon03, R.id.img_app_icon04 };
	int[] appNameItemId = { R.id.tv_app_name01, R.id.tv_app_name02, R.id.tv_app_name03, R.id.tv_app_name04 };

	RelativeLayout[] appRlItem = new RelativeLayout[4];
	ImageView[] appIconItem = new ImageView[4];
	TextView[] appNameItem = new TextView[4];

	TextView textView;
	View dividerbar;
	LinearLayout dividerLine;
	ImageView expandArrow;
	LinearLayout appGroupll;
	ViewGroup currentItemView;
	LayoutInflater inflater;

	public MustHaveAppListViewHeaderHolder(View view, LayoutInflater inflater, HashSet<Integer> expandCategoryIdSet) {
		if (view != null) {
			this.expandCategoryIdSet = expandCategoryIdSet;
			this.inflater = inflater;
			currentItemView = (ViewGroup) view;
			textView = (TextView) view.findViewById(R.id.hmhi_catalog_tv);
			dividerbar = (View) view.findViewById(R.id.divider_bar);
			dividerLine = (LinearLayout) view.findViewById(R.id.hmhi_divider_line);
			expandArrow = (ImageView) view.findViewById(R.id.hmhi_show_more_arrow);

			appGroupll = (LinearLayout) view.findViewById(R.id.hmhi_app_ll);
			appGroupll.setTag(R.id.fgelv_tag_header_item);
			for (int i = 0; i < 4; i++) {
				appRlItem[i] = (RelativeLayout) view.findViewById(appRlItemId[i]);
				appIconItem[i] = (ImageView) view.findViewById(appIconItemId[i]);
				appNameItem[i] = (TextView) view.findViewById(appNameItemId[i]);
			}
			hasInited = true;
		}
	}

	public void setSkinTheme(Context context) {
		//设置皮肤
		SkinConfigManager.getInstance().setViewBackground(context, dividerbar, SkinConstan.DIVIDER_BAR);
	}

	public void initData(final Context context, MustHaveCategoryList mustHaveCategoryList, final int ct, View v,
			int groupPosition) {
		textView.setText(mustHaveCategoryList.getName());
		ArrayList<AppInfoBean> applist = mustHaveCategoryList.getApplist();
		if (applist != null) {
			int size = applist.size();
			if (expandCategoryIdSet.contains(mustHaveCategoryList.getId())) {
				appGroupll.setVisibility(View.GONE);
			} else {
				appGroupll.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < 4; i++) {
				if (size > i) {
					final AppInfoBean appInfoBean = applist.get(i);
					appRlItem[i].setVisibility(View.VISIBLE);
					appNameItem[i].setText(appInfoBean.getAppName());
					NetworkImageUtils.load(context, ImageType.NETWORK, appInfoBean.getLogo(),
							R.drawable.ic_screen_default_picture, R.drawable.ic_screen_default_picture, appIconItem[i]);
					appRlItem[i].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							NavigationUtils.toAppDetailPage(context, appInfoBean, ct);
						}
					});
				} else {
					appRlItem[i].setVisibility(View.INVISIBLE);
				}
			}
		} else {
			for (int i = 0; i < 4; i++) {
				appRlItem[i].setVisibility(View.INVISIBLE);
			}
		}

	}

	public void setAppGroupllVisviable(boolean expand, int categoryId) {
		appGroupll.setVisibility(expand ? View.VISIBLE : View.GONE);
		expandArrow.setImageResource(expand ? R.drawable.ic_download_manager_arrow_down
				: R.drawable.ic_download_manager_arrow_up);
		if (expand) {
			expandCategoryIdSet.remove(categoryId);
		} else {
			expandCategoryIdSet.add(categoryId);
		}
	}

	public boolean isGroupExpand(int categoryId) {
		return !expandCategoryIdSet.contains(categoryId);
	}

}
