/**   
 * @Title: AppsCategoriesFragment.java
 * @Package com.mas.amineappstore.activity
 * @Description: TODO 
 
 * @date 2014-2-13 下午04:47:14
 * @version V1.0   
 */

package com.x.ui.activity.home;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.x.business.category.AppGamesCategoryManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.model.AppGamesCategoryResponse;
import com.x.publics.http.model.CategoryRequest;
import com.x.publics.http.model.Pager;
import com.x.publics.model.CategoryBean;
import com.x.publics.model.CategoryBean.SecondaryCatBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SettingsUtils;
import com.x.publics.utils.ToastUtil;
import com.x.receiver.ConnectChangeReceiver;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.AppSubCategoryListAdapter;
import com.x.ui.adapter.CategoryListAdapter;
import com.x.ui.adapter.gridview.CategoryGridListAdapter;
import com.x.ui.adapter.gridview.base.GridItemClickListener;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;

/**
 * @ClassName: AppsCategoriesFragment
 * @Description: TODO
 
 * @date 2014-2-13 下午04:47:14
 * 
 */

public class AppsCategoriesFragment extends BaseFragment {
	private Context context;
	private CategoryListAdapter categoryListAdapter;
	private CategoryGridListAdapter categoryGridListAdapter;
	private AppSubCategoryListAdapter appSubCategoryListAdapter;
	// private List<CategoryInfoBean> categoryList = new
	// ArrayList<CategoryInfoBean>();
	private List<CategoryBean> categoryList = new ArrayList<CategoryBean>();
	private List<SecondaryCatBean> secondaryCatList = new ArrayList<CategoryBean.SecondaryCatBean>();
	private PullToRefreshListView pulltoRefreshLv;
	private ListView categoryLv;
	private int rowNum = 1;
	private boolean isFicheMode = false;
	private boolean lastMode = false;

	private CategoryRequest request;
	private Pager pager;
	private View loadingView;
	private View errorView;
	private int pageNum = 1;
	private ValidWifiReceiver mValidWifiReceiver;
	private boolean mIsVisibleToUser = false;
	private boolean inited = false;
	private View loadingPb, loadingLogo;

	private AppGamesCategoryManager appGamesCategoryManager;

	public static Fragment newInstance(Bundle bundle) {
		AppsCategoriesFragment fragment = new AppsCategoriesFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getActivity().getApplicationContext();
		lastMode = SettingsUtils.getValue(mActivity,
				SettingsUtils.IS_FICHE_MODE, false);
		appGamesCategoryManager = AppGamesCategoryManager.getInstance(context);
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 注册广播
		if (!inited) {
			registUiReceiver();
		}
		// 初始化显示模式
		initDisplayMode();

		// 加载数据
		if (categoryList.isEmpty()) {
			getData(1);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		rootView = inflater.inflate(R.layout.fragment_apps_categories,
				container, false);
		errorView = rootView.findViewById(R.id.e_error_rl);

		pulltoRefreshLv = (PullToRefreshListView) rootView
				.findViewById(R.id.fac_app_category_lv);
		categoryLv = pulltoRefreshLv.getRefreshableView();

		pulltoRefreshLv.setOnRefreshListener(onRefreshListener);
		pulltoRefreshLv.setMode(Mode.PULL_FROM_START);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshLv.setEmptyView(loadingView);
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
				mActivity);
		soundListener.addSoundEvent(State.REFRESH_TO_RESET, R.raw.refresh);
		pulltoRefreshLv.setOnPullEventListener(soundListener);
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

	// private void getData(int page) {
	// request = new CategoryRequest();
	// pager = new Pager(page);
	// request.setPager(pager);
	// request.setCt(getCt());
	// DataFetcher.getInstance().getCategoryData(request, myResponseListent,
	// myErrorListener, true);
	// }

	/***
	 
	 * @Title: getData
	 * @Description: TODO
	 * @param @param page
	 * @return void
	 */
	private void getData(int page) {
		pager = new Pager(page);
		pager.setPs(5);
		int cat = getCt();
		appGamesCategoryManager.getSubCategoriesList(context, mHandler, pager,
				cat);
	}

	public int getCt() {
		return Constan.Ct.APP_CATEGORY;
	}

	public int getCt2() {
		return Constan.Ct.APP_CATEGORY_DETAIL;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case AppGamesCategoryManager.onSuccess:
				pulltoRefreshLv.onRefreshComplete();
				AppGamesCategoryResponse response = (AppGamesCategoryResponse) msg.obj;
				if (response != null && !response.categorylist.isEmpty()) {
					categoryList.addAll(response.categorylist);
					categoryListAdapter.setList(categoryList);
				} else {
					showErrorView();
				}
				break;
			case AppGamesCategoryManager.onFailure:
				pulltoRefreshLv.onRefreshComplete();
				showErrorView();
				break;
			default:
				break;
			}
		}

	};

	// private Listener<JSONObject> myResponseListent = new
	// Listener<JSONObject>() {
	//
	// @Override
	// public void onResponse(JSONObject response) {
	// pulltoRefreshLv.onRefreshComplete();
	// LogUtil.getLogger().d("response==>" + response.toString());
	// CategoryResponse categoryResponse = (CategoryResponse) JsonUtil
	// .jsonToBean(response, CategoryResponse.class);
	// if (categoryResponse != null && categoryResponse.state.code == 200) {
	// if (!categoryResponse.categorylist.isEmpty()) {
	// categoryList.addAll(categoryResponse.categorylist); // 判断当前选择的显示模式
	// // //
	// if (isFicheMode) {
	// categoryGridListAdapter.setList(categoryList);
	// } else {
	// categoryListAdapter.setList(categoryList);
	//
	// }
	//
	// } else {
	// showErrorView();
	// }
	//
	// } else {
	// showErrorView();
	// }
	// }
	// };

	/*
	 * private ErrorListener myErrorListener = new ErrorListener() {
	 * 
	 * @Override public void onErrorResponse(VolleyError error) {
	 * pulltoRefreshLv.onRefreshComplete(); showErrorView();
	 * error.printStackTrace(); } };
	 */

	private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			categoryList = new ArrayList<CategoryBean>();
			pageNum = 1;
			getData(1);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			getData(++pageNum);
		}

	};

	private void showErrorView() {
		if (pageNum > 1) {
			--pageNum;
		}
		if (categoryList.isEmpty()) {
			if (isFicheMode) {
				// categoryGridListAdapter.setList(categoryList);
				categoryListAdapter.setList(categoryList);
			} else {
				categoryListAdapter.setList(categoryList);
			}
			pulltoRefreshLv.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
			errorView.findViewById(R.id.e_retry_btn).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (!NetworkUtils.isNetworkAvailable(mActivity)) {
								ToastUtil.show(mActivity, ResourceUtil
										.getString(mActivity,
												R.string.network_canot_work),
										Toast.LENGTH_SHORT);
								return;
							}
							pulltoRefreshLv.setVisibility(View.VISIBLE);
							errorView.setVisibility(View.GONE);
							pulltoRefreshLv.setEmptyView(loadingView);
							getData(1);
						}
					});
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(mValidWifiReceiver);
	}

	/**
	 * 初始化显示模式
	 */
	private void initDisplayMode() {
		// 获取当前选中的显示模式
		isFicheMode = SettingsUtils.getValue(mActivity,
				SettingsUtils.IS_FICHE_MODE, false);

		// if (isFicheMode) {
		// // 刷新Item状态
		// if (categoryGridListAdapter != null) {
		// categoryGridListAdapter.notifyDataSetChanged();
		// }
		//
		// // 判断categoryGridListAdapter是否已经初始化，并对比上一次显示模式
		// if (!categoryList.isEmpty() && lastMode == isFicheMode) {
		// return;
		// }
		//
		// // 同步当前模式
		// lastMode = isFicheMode;
		// rowNum = getResources().getInteger(R.integer.gridview_row_count);
		// categoryGridListAdapter = new CategoryGridListAdapter(mActivity);
		// categoryGridListAdapter.setNumColumns(rowNum);
		// categoryGridListAdapter
		// .setOnGridClickListener(new GridItemClickListener() {
		// @Override
		// public void onGridItemClicked(View v, int position,
		// long itemId) {
		//
		// if (!NetworkUtils.isNetworkAvailable(mActivity)) {
		// ToastUtil.show(
		// mActivity,
		// mActivity.getResources().getString(
		// R.string.network_canot_work),
		// Toast.LENGTH_SHORT);
		// return;
		// }
		// if (categoryList.size() > 0) {
		// CategoryBean categoryInfoBean = categoryList
		// .get(position);
		// CategoryDetailActivity.launch(mActivity,
		// categoryInfoBean.getCategoryId(),
		// categoryInfoBean.getName(), getCt2());
		// }
		// }
		// });
		// categoryLv.setAdapter(categoryGridListAdapter);
		// categoryGridListAdapter.setList(categoryList);
		//
		// } else
		// {
		// 刷新Item状态
		if (categoryListAdapter != null) {
			categoryListAdapter.notifyDataSetChanged();
		}

		// 判断categoryListAdapter是否已经初始化，并对比上一次显示模式
		if (!categoryList.isEmpty() && lastMode == isFicheMode) {
			return;
		}

		// 同步当前模式
		lastMode = isFicheMode;
		categoryListAdapter = new CategoryListAdapter(mActivity, getCt2());
		categoryLv.setAdapter(categoryListAdapter);
		categoryListAdapter.setList(categoryList);
		// }

	}

	private void registUiReceiver() {

		mValidWifiReceiver = new ValidWifiReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(ConnectChangeReceiver.NETWORK_CONNECTED_ACTION);
		BroadcastManager.registerReceiver(mValidWifiReceiver, filter2);
		inited = true;
	}

	private class ValidWifiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {
			if (intent != null && mIsVisibleToUser == true
					&& errorView.getVisibility() == View.VISIBLE) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				switch (type) {
				case MyIntents.Types.VALID_WIFI:// 由没有网络到有wifi的通知
					// 重新请求数据
					if (getUserVisibleHint()) {
						pulltoRefreshLv.setVisibility(View.VISIBLE);
						errorView.setVisibility(View.GONE);
						pulltoRefreshLv.setEmptyView(loadingView);
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
		if (errorView == null || pulltoRefreshLv == null)
			return;
		if (isVisibleToUser && errorView.getVisibility() == View.VISIBLE
				&& !unknow.equals("unknow")) {
			// 重新请求数据
			errorView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(loadingView);
			pulltoRefreshLv.setVisibility(View.VISIBLE);
			getData(1);
		}
		if (getCt() == Constan.Ct.APP_CATEGORY) {
			DataEyeManager.getInstance().module(
					StatisticConstan.ModuleName.APPS_CATEGORIES,
					isVisibleToUser);
			if (isVisibleToUser) {
				DataEyeManager.getInstance().source(
						StatisticConstan.SrcName.APPS_CATEGORIES, 0, null, 0L,
						null, null, false);
			}
		} else if (getCt() == Constan.Ct.GAME_CATEGORY) {
			DataEyeManager.getInstance().module(
					StatisticConstan.ModuleName.GAMES_CATEGORIES,
					isVisibleToUser);
			if (isVisibleToUser) {
				DataEyeManager.getInstance().source(
						StatisticConstan.SrcName.GAMES_CATEGORIES, 0, null, 0L,
						null, null, false);
			}
		}
	}

	/**
	 * @Title: setSkinTheme
	 * @Description: TODO
	 * @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(mActivity,
				loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity,
				(ProgressBar) loadingPb, SkinConstan.LOADING_PROGRASS_BAR);
	}
}
