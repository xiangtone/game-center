package com.x.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.RecommendBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.appdetail.AppDetailActivity;

/**
 * @ClassName: RecommendListAdapter
 * @Desciption: 应用推荐适配器
 
 * @Date: 2014-1-28 下午3:52:55
 */

public class RecommendListAdapter extends ArrayListBaseAdapter<RecommendBean> {
	
	private final int rc;

	public RecommendListAdapter(Activity context, int rc) {
		super(context);
		this.rc = rc;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.recommend_gridview_item, null);
		final RecommendBean recommendBean = mList.get(position);
		RecommendViewHolder holder = new RecommendViewHolder(convertView);
		convertView.setTag(recommendBean.getLogo());
		holder.initData(recommendBean, context);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, ResourceUtil.getString(context,R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}
				
				Intent intent = new Intent(context, AppDetailActivity.class);
				intent.putExtra("appInfoBean", (AppInfoBean) recommendBean);
				intent.putExtra("ct", Constan.Ct.APP_RECOMMEND);
				context.startActivity(intent);

				// 根据Rc值，选择dataeye接口
				switch (rc) {
				case Constan.Rc.GET_APP_RECOMMEND: // 应用推荐
					DataEyeManager.getInstance().source(StatisticConstan.SrcName.RELATED, recommendBean.getFileType(),
							recommendBean.getAppName(), recommendBean.getFileSize(), recommendBean.getVersionName(), null,
							false);
					break;

				case Constan.Rc.ISSUER_APP_RECOMMEND: // 开发商应用推荐
					//TODO.
					break;
				}
				
			}
		});

		return convertView;
	}
}
