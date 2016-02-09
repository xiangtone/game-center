/**   
* @Title: DynamicListViewAdapter.java
* @Package com.mas.amineappstore.ui.view.dynamic
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-3-23 下午2:39:18
* @version V1.0   
*/

package com.x.ui.view.dynamic;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.x.R;
import com.x.publics.model.AppInfoBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.appdetail.AppDetailActivity;

/**
 * 
* @ClassName: DynamicListViewAdapter
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-3-23 下午2:39:18
*
 */
public class DynamicListViewAdapter extends ArrayAdapter<DynamicListViewItem> {
	public static final String IS_FROM_DYNAMIC = "isFromDynamic";
	private int mCounter;
	private Context context;
	private int layoutViewResId;
	private boolean isDetailLayout;
	protected LayoutInflater inflater;
	private List<DynamicListViewItem> data;
	private HashMap<DynamicListViewItem, Integer> mIdMap = new HashMap<DynamicListViewItem, Integer>();

	public DynamicListViewAdapter(Context context, int layoutViewResId, List<DynamicListViewItem> data) {
		super(context, layoutViewResId, data);
		this.data = data;
		this.context = context;
		this.layoutViewResId = layoutViewResId;
		this.inflater = LayoutInflater.from(context);
		updateStableIds();
		if (layoutViewResId == R.layout.dynamiclistview_detail_item) {
			isDetailLayout = true;
		} else {
			isDetailLayout = false;
		}
	}

	public long getItemId(int position) {
		if (null != data && data.size() > position) {
			DynamicListViewItem item = getItem(position);
			if (mIdMap.containsKey(item)) {
				return mIdMap.get(item);
			}
		}
		return -1;
	}

	public void updateStableIds() {
		mIdMap.clear();
		mCounter = 0;
		for (int i = 0; i < data.size(); ++i) {
			mIdMap.put(data.get(i), mCounter++);
		}
	}

	public void addStableIdForDataAtPosition(int position) {
		mIdMap.put(data.get(position), ++mCounter);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		DynamicListViewHolder holder;

		if (null == convertView) {
			convertView = inflater.inflate(layoutViewResId, null);
			holder = new DynamicListViewHolder(convertView, isDetailLayout);
			convertView.setTag(holder);
		} else {
			holder = (DynamicListViewHolder) convertView.getTag();
		}

		final DynamicListViewItem objectItem = data.get(position);
		holder.initData(context, objectItem, isDetailLayout, position);

		if (isDetailLayout) {
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!NetworkUtils.isNetworkAvailable(context)) {
						ToastUtil.show(context, ResourceUtil.getString(context, R.string.network_canot_work),
								ToastUtil.LENGTH_LONG);
						return;
					}
					// TODO Auto-generated method stub
					AppInfoBean appInfoBean = new AppInfoBean();
					appInfoBean.setApkId(objectItem.getApkId());
					appInfoBean.setAppName(objectItem.getAppName());
					Intent intent = new Intent(context, AppDetailActivity.class);
					intent.putExtra("appInfoBean", appInfoBean);
					intent.putExtra(IS_FROM_DYNAMIC, true);
					intent.putExtra("ct", Constan.Ct.DYNAMIC_LISTVIEW_DETAIL);
					context.startActivity(intent);
				}
			});
		}

		return convertView;
	}

}
