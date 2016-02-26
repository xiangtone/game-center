/**   
* @Title: BannerViewFlow.java
* @Package com.x.view.viewflow
* @Description: TODO 

* @date 2014-2-13 上午11:22:46
* @version V1.0   
*/

package com.x.ui.view.viewflow;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.BannerInfoBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.ui.activity.appdetail.AppDetailActivity;

/**
* @ClassName: BannerViewFlow
* @Description: TODO 

* @date 2014-2-13 上午11:22:46
* 
*/

public class BannerViewFlow {
	private Context context;
	private ViewFlow viewFlow;
	private ImageAdapter imageAdapter;
	private int viewflowId, viewflowindicId,ct;
	private ViewPager parentViewPager;
	private View rootView;

	public BannerViewFlow(Context context, View rootView, int viewflowId, int viewflowindicId, ViewPager parentViewPager, int ct) {
		this.context = context;
		this.rootView = rootView;
		this.viewflowId = viewflowId;
		this.viewflowindicId = viewflowindicId;
		this.parentViewPager = parentViewPager;
        this.ct = ct;
	}

	public ViewFlow creatMyViewFlow(List<BannerInfoBean> appList) {
		viewFlow = null;
		viewFlow = (ViewFlow) rootView.findViewById(viewflowId);
		imageAdapter = new ImageAdapter(context, appList);
		viewFlow.setParentViewpager(parentViewPager);
		viewFlow.setAdapter(imageAdapter);
		int size = appList.size();
		//		if (size > 3) {
		viewFlow.setmSideBuffer(size); // 实际图片张数
		//		} else {
		//			viewFlow.setmSideBuffer(3); // 实际图片张数
		//		}
		CircleFlowIndicator indic = (CircleFlowIndicator) rootView.findViewById(viewflowindicId);
		viewFlow.setFlowIndicator(indic);
        viewFlow.setTimeSpan(8 * 1000); // 设置时间间隔
		viewFlow.setSelection(size * 1000); //设置初始位置，图片总数的倍数
		viewFlow.startAutoFlowTimer(); //启动自动播放 
		return viewFlow;
	}

	class ImageAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private List<BannerInfoBean> mShow = new ArrayList<BannerInfoBean>();
		private Context context;

		public ImageAdapter(Context context, List<BannerInfoBean> appList) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.context = context;
			this.mShow = appList;
		}

		public int getCount() {
			return Integer.MAX_VALUE; //返回很大的值使得getView中的position不断增大来实现循环。
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if(mShow.isEmpty())
				return (ImageView) mInflater.inflate(R.layout.banner_item, null);
			if (convertView == null)
				convertView = (ImageView) mInflater.inflate(R.layout.banner_item, null);
			final BannerInfoBean bannerInfoBean = mShow.get(position % mShow.size());
			convertView.setTag(bannerInfoBean);
			NetworkImageUtils.load(context, ImageType.NETWORK, bannerInfoBean.getIcon(),
					R.drawable.banner_default_picture, R.drawable.banner_default_picture, (ImageView) convertView);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!NetworkUtils.isNetworkAvailable(context)) {
						ToastUtil.show(context, ResourceUtil.getString(context, R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return;
					}
					AppInfoBean appInfoBean = new AppInfoBean();
					appInfoBean.setApkId(bannerInfoBean.getApkId());
					appInfoBean.setAppName(bannerInfoBean.getName());
					Intent intent = new Intent(context, AppDetailActivity.class);
					intent.putExtra("appInfoBean", appInfoBean);
					intent.putExtra("isFromBanner", true);
					intent.putExtra("ct", ct);
					context.startActivity(intent);
					//dataeye
					int srcName = 0 ;
					switch (ct) {
					case Constan.Ct.HOME_BANNER:
						srcName =StatisticConstan.SrcName.HOME_BANNER ;
						break ;
					case Constan.Ct.APP_BANNER:
						srcName =StatisticConstan.SrcName.APPS_BANNER ;
						break ;
					case Constan.Ct.GAME_BANNER:
						srcName =StatisticConstan.SrcName.GAME_BANNER ;
						break ;
					default:
						break ;
					}
					DataEyeManager.getInstance().source(srcName, bannerInfoBean.getFileType(), null, 0L, null, null, false) ;
				}
			});
			return convertView;
		}

	}
}
