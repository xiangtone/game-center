package com.x.ui.activity.wallpaper;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.x.business.country.CountryManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.WallpaperRequest;
import com.x.publics.http.model.WallpaperResponse;
import com.x.publics.http.model.WallpaperRequest.WallpaperRequestData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.CategoryInfoBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.receiver.ConnectChangeReceiver;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.WallpaperCategoriesListAdapter;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshGridView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;

/**
 * @ClassName: WallpaperAlbumFragment
 * @Desciption: TODO
 
 * @Date: 2014-3-13 下午5:00:54
 */

public class WallpaperCategoriesFragment extends BaseFragment {

	private GridView mGridView;
	private PullToRefreshGridView pulltoRefreshGv;
	private WallpaperCategoriesListAdapter wallpaperCategoriesListAdapter;
	private ArrayList<CategoryInfoBean> categoryList = new ArrayList<CategoryInfoBean>();

	private WallpaperRequest request;
	private Pager pager;
	private View loadingView;
	private View errorView;
	private int pageNum = 1;
	private int lastCountryId;
	private int currentCountryId;

	// 广播相关
	private boolean inited = false;
	private boolean mIsVisibleToUser = false;
	private ValidWifiReceiver mValidWifiReceiver;
	private View loadingPb, loadingLogo;

	public static Fragment newInstance(Bundle bundle) {
		WallpaperCategoriesFragment fragment = new WallpaperCategoriesFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 注册广播
		if (!inited) {
			registReceiver();
		}

		// 获取当前国家ID
		currentCountryId = CountryManager.getInstance().getCountryId(mActivity);

		// 匹配当前国家ID 和 最后一次国家是否相等
		if (currentCountryId == lastCountryId) {
			// 加载数据
			if (categoryList.isEmpty()) {
				getData(1);
			}
		} else {
			lastCountryId = currentCountryId;
			categoryList = new ArrayList<CategoryInfoBean>();
			pulltoRefreshGv.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			pulltoRefreshGv.setEmptyView(loadingView);
			wallpaperCategoriesListAdapter.setList(categoryList); // 清空adapter，防止显示缓存数据
			getData(1);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = null;
		rootView = inflater.inflate(R.layout.fragment_wallpaper_categories, container, false);
		errorView = rootView.findViewById(R.id.e_error_rl);

		pulltoRefreshGv = (PullToRefreshGridView) rootView.findViewById(R.id.fhr_wallpaper_categories_gv);
		mGridView = pulltoRefreshGv.getRefreshableView();

		// 设置列数
		mGridView.setNumColumns(2);
		//mGridView.setVerticalSpacing((int) getResources().getDimension(R.dimen.grid_vertical_space));
		//mGridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.grid_horizontal_space));

		pulltoRefreshGv.setMode(Mode.DISABLED);
		pulltoRefreshGv.setOnRefreshListener(onRefreshListener);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshGv.setEmptyView(loadingView);
		SoundPullEventListener<GridView> soundListener = new SoundPullEventListener<GridView>(mActivity);
		soundListener.addSoundEvent(State.REFRESH_TO_RESET, R.raw.refresh);
		pulltoRefreshGv.setOnPullEventListener(soundListener);
		wallpaperCategoriesListAdapter = new WallpaperCategoriesListAdapter(mActivity);
		mGridView.setAdapter(wallpaperCategoriesListAdapter);
		wallpaperCategoriesListAdapter.setList(categoryList);

		// 记录最后一次国家ID
		lastCountryId = CountryManager.getInstance().getCountryId(mActivity);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void getData(int page) {
		request = new WallpaperRequest();
		pager = new Pager(page);
		request.setPager(pager);
		request.setData(new WallpaperRequestData(getCt()));
		DataFetcher.getInstance().getWallpaperData(request, myResponseListent, myErrorListener, false);
	}

	public int getCt() {
		return Constan.Ct.WALLPAPER_CATEGORY;
	}

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			pulltoRefreshGv.onRefreshComplete();
			LogUtil.getLogger().d("response==>" + response.toString());
			WallpaperResponse wallpaperResponse = (WallpaperResponse) JsonUtil.jsonToBean(response,
					WallpaperResponse.class);
			if (wallpaperResponse != null && wallpaperResponse.state.code == 200) {
				if (!wallpaperResponse.categorylist.isEmpty()) {
					categoryList.addAll(wallpaperResponse.categorylist);
					wallpaperCategoriesListAdapter.setList(categoryList);
					if (wallpaperResponse.isLast) {
						cancleGridViewScorllable();
					} else {
						pulltoRefreshGv.setMode(Mode.BOTH);
					}
				} else {
					if (categoryList.isEmpty()) {
						showErrorView();
					} else {
						cancleGridViewScorllable();
					}
				}

			} else {
				showErrorView();
			}
		}
	};

	private ErrorListener myErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			pulltoRefreshGv.onRefreshComplete();
			showErrorView();
			error.printStackTrace();
		}
	};

	private OnRefreshListener2<GridView> onRefreshListener = new OnRefreshListener2<GridView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
			categoryList.clear();
			pageNum = 1;
			getData(1);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
			getData(++pageNum);
		}

	};

	private void showErrorView() {
		if (pageNum > 1) {
			--pageNum;
		}
		if (categoryList.isEmpty()) {
			pulltoRefreshGv.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
			errorView.findViewById(R.id.e_retry_btn).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!NetworkUtils.isNetworkAvailable(mActivity)) {
						ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return;
					}
					pulltoRefreshGv.setVisibility(View.VISIBLE);
					errorView.setVisibility(View.GONE);
					pulltoRefreshGv.setEmptyView(loadingView);
					getData(1);
				}
			});
		}
	}

	private void cancleGridViewScorllable() {
		if (pulltoRefreshGv != null)
			pulltoRefreshGv.setMode(Mode.PULL_FROM_START);
	}

	public ViewPager getPargentViewPager() {
		return ((WallpaperFragment) getParentFragment()).mViewPager;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.getLogger().d("FragmentA==============onDestroyView");
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	/**=============================================验证Wifi是否打开================================================*/

	/**
	* @Title: registReceiver 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void registReceiver() {
		mValidWifiReceiver = new ValidWifiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectChangeReceiver.NETWORK_CONNECTED_ACTION);
		BroadcastManager.registerReceiver(mValidWifiReceiver, filter);
		inited = true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(mValidWifiReceiver);
	}

	private class ValidWifiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			handleIntent(intent);
		}

		private void handleIntent(Intent intent) {
			// TODO Auto-generated method stub
			if (intent != null && mIsVisibleToUser == true && errorView.getVisibility() == View.VISIBLE) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				switch (type) {

				// 由没有网络到有wifi的通知
				case MyIntents.Types.VALID_WIFI:
					// 重新请求数据
					if (getUserVisibleHint()) {
						pulltoRefreshGv.setVisibility(View.VISIBLE);
						errorView.setVisibility(View.GONE);
						pulltoRefreshGv.setEmptyView(loadingView);
						getData(1);
					}
					break;
				}
			}
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisibleToUser = isVisibleToUser;
		String unknow = "";
		if (mActivity != null) {
			unknow = NetworkUtils.getNetworkInfo(mActivity);
		}
		if (errorView == null || pulltoRefreshGv == null && unknow.equals("unknow")) {
			return;
		}
		if (isVisibleToUser && errorView.getVisibility() == View.VISIBLE && !unknow.equals("unknow")) {
			// 重新请求数据
			pulltoRefreshGv.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			pulltoRefreshGv.setEmptyView(loadingView);
			getData(1);
		}
		DataEyeManager.getInstance().module(StatisticConstan.ModuleName.WALLPAPER_CATEGORIES, isVisibleToUser);
		if (isVisibleToUser) {
			DataEyeManager.getInstance().source(StatisticConstan.SrcName.WALLPAPER_CATEGORIES, 0, null, 0L, null, null,
					false);
		}
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
