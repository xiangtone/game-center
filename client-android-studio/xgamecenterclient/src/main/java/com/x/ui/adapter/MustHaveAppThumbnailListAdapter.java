/**   
* @Title: MustHaveAppThumbnailListAdapter.java
* @Package com.x.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-6-1 下午2:54:09
* @version V1.0   
*/

package com.x.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.MustHaveAppInfoBean;
import com.x.publics.model.RecommendBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.appdetail.AppDetailActivity;

/**
* @ClassName: MustHaveAppThumbnailListAdapter
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-6-1 下午2:54:09
* 
*/

public class MustHaveAppThumbnailListAdapter extends ArrayListBaseAdapter<MustHaveAppInfoBean> {
	private int ct;
	private Activity context;

	/**
	* <p>Title: </p>
	* <p>Description: </p>
	* @param context
	*/

	public MustHaveAppThumbnailListAdapter(Activity context, int ct) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.ct = ct;
	}

	/* (非 Javadoc) 
	* <p>Title: getCount</p> 
	* <p>Description: </p> 
	* @return 
	* @see com.x.ui.adapter.ArrayListBaseAdapter#getCount() 
	*/

	@Override
	public int getCount() {
		int count = mList.size() > 4 ? 4 : mList.size();
		return mList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.recommend_gridview_item, null);
		final MustHaveAppInfoBean mustHaveAppInfoBean = mList.get(position);
		MustHaveAppThumbnailListViewHolder holder = new MustHaveAppThumbnailListViewHolder(convertView);
		convertView.setTag(mustHaveAppInfoBean.getLogo());
		holder.initData(mustHaveAppInfoBean, context);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// check network
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}

				Intent intent = new Intent(context, AppDetailActivity.class);
				intent.putExtra("appInfoBean", mustHaveAppInfoBean);
				intent.putExtra("ct", ct);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

}
