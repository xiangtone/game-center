package com.x.ui.activity.ringtones;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.x.publics.http.model.RingtonesRequest;
import com.x.publics.http.model.RingtonesResponse;
import com.x.publics.http.model.RingtonesRequest.MusicRequestData;
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
import com.x.ui.adapter.RingtonesCategoriesAdapter;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshGridView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;

/**
 * 
* @ClassName: RingtonesCategoriesFragment
* @Description: 铃声分类

* @date 2014-4-8 上午10:43:55
*
 */
public class RingtonesCategoriesFragment extends BaseFragment {

	private GridView mGridView;
	private PullToRefreshGridView pulltoRefreshGv;
	private ArrayList<CategoryInfoBean> list = new ArrayList<CategoryInfoBean>();
	private RingtonesCategoriesAdapter categoriesAdapter = null;

	private RingtonesRequest request;
	private Pager pager;
	private View loadingView;
	private View errorView;
	private int pageNum = 1;
	private int lastCountryId;
	private int currentCountryId;

	private ValidWifiReceiver mValidWifiReceiver;
	private boolean mIsVisibleToUser = false;
	private boolean inited = false;
	private View loadingPb, loadingLogo;

	public static Fragment newInstance(Bundle bundle) {
		RingtonesCategoriesFragment fragment = new RingtonesCategoriesFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_ringtones_categories, container, false);
		errorView = rootView.findViewById(R.id.e_error_rl);
		pulltoRefreshGv = (PullToRefreshGridView) rootView.findViewById(R.id.fhr_music_categories_gv);
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
		categoriesAdapter = new RingtonesCategoriesAdapter(mActivity);
		mGridView.setAdapter(categoriesAdapter);
		categoriesAdapter.setList(list);

		// 记录最后一次国家ID
		lastCountryId = CountryManager.getInstance().getCountryId(mActivity);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();

		if (!inited) {
			registUiReceiver();
		}

		// 刷新界面
		if (categoriesAdapter != null) {
			categoriesAdapter.notifyDataSetChanged();
		}

		// 获取当前国家ID
		currentCountryId = CountryManager.getInstance().getCountryId(mActivity);

		// 匹配当前国家ID 和 最后一次国家是否相等
		if (currentCountryId == lastCountryId) {
			// 加载数据
			if (list.isEmpty()) {
				getData(1);
			}
		} else {
			lastCountryId = currentCountryId;
			list = new ArrayList<CategoryInfoBean>();
			pulltoRefreshGv.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			pulltoRefreshGv.setEmptyView(loadingView);
			categoriesAdapter.setList(list); // 清空adapter，防止显示缓存数据
			getData(1);
		}
	}

	private void getData(int page) {
		request = new RingtonesRequest();
		pager = new Pager(page);
		request.setPager(pager);
		request.setData(new MusicRequestData(getCt()));
		DataFetcher.getInstance().getMusicData(request, myResponseListent, myErrorListener, true);
	}

	public int getCt() {
		return Constan.Ct.RINGTONES_CATEGORY;
	}

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			pulltoRefreshGv.onRefreshComplete();
			LogUtil.getLogger().d("response==>" + response.toString());
			RingtonesResponse musicResponse = (RingtonesResponse) JsonUtil
					.jsonToBean(response, RingtonesResponse.class);
			if (musicResponse != null && musicResponse.state.code == 200) {
				if (!musicResponse.categorylist.isEmpty()) {
					list.addAll(musicResponse.categorylist);
					categoriesAdapter.setList(list);
					if (musicResponse.isLast) {
						cancleGridViewScorllable();
					} else {
						pulltoRefreshGv.setMode(Mode.BOTH);
					}
				} else {
					if (list.isEmpty()) {
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
			list.clear();
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
		if (list.isEmpty()) {
			categoriesAdapter.setList(list);
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public ViewPager getPargentViewPager() {
		return ((RingtonesFragment) getParentFragment()).mViewPager;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.getLogger().d("FragmentA==============onDestroyView");
		BroadcastManager.unregisterReceiver(mValidWifiReceiver);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class ValidWifiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {
			if (intent != null && mIsVisibleToUser == true && errorView.getVisibility() == View.VISIBLE) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				switch (type) {
				case MyIntents.Types.VALID_WIFI:// 由没有网络到有wifi的通知
					//重新请求数据
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
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisibleToUser = isVisibleToUser;
		String unknow = "";
		if (mActivity != null) {
			unknow = NetworkUtils.getNetworkInfo(mActivity);
		}
		if (errorView == null || pulltoRefreshGv == null)
			return;
		if (isVisibleToUser && errorView.getVisibility() == View.VISIBLE && !unknow.equals("unknow")) {
			//重新请求数据
			errorView.setVisibility(View.GONE);
			pulltoRefreshGv.setEmptyView(loadingView);
			pulltoRefreshGv.setVisibility(View.VISIBLE);
			getData(1);
		}
		DataEyeManager.getInstance().module(StatisticConstan.ModuleName.RINGTONE_CATEGORIES, isVisibleToUser);
		if (isVisibleToUser) {
			DataEyeManager.getInstance().source(StatisticConstan.SrcName.RINGTONE_CATEGORIES, 0, null, 0L, null, null,
					false);
		}
	}

	private void registUiReceiver() {
		mValidWifiReceiver = new ValidWifiReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(ConnectChangeReceiver.NETWORK_CONNECTED_ACTION);
		BroadcastManager.registerReceiver(mValidWifiReceiver, filter2);
		inited = true;
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
