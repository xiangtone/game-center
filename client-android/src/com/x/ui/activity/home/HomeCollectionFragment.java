/**   
* @Title: HomeCollectionFragment.java
* @Package com.x.ui.activity.home
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-1 下午4:45:44
* @version V1.0   
*/

package com.x.ui.activity.home;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.x.publics.http.model.HomeCollectionRequest;
import com.x.publics.http.model.HomeCollectionResponse;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.HomeCollectionRequest.HomeCollectionRequestData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.AppCollectionBean;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.receiver.ConnectChangeReceiver;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.AppCollectionListAdapter;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;

/**
* @ClassName: HomeCollectionFragment
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-1 下午4:45:44
* 
*/

public class HomeCollectionFragment extends BaseFragment {

	private ListView appCollectionLv;
	private PullToRefreshListView pulltoRefreshLv;
	private AppCollectionListAdapter appCollectionAdapter;
	private ArrayList<AppCollectionBean> collectionList = new ArrayList<AppCollectionBean>();

	private HomeCollectionRequest request;
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
		HomeCollectionFragment fragment = new HomeCollectionFragment();
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

		// 刷新界面
		if (appCollectionAdapter != null) {
			appCollectionAdapter.notifyDataSetChanged();
		}

		// 获取当前国家ID
		currentCountryId = CountryManager.getInstance().getCountryId(mActivity);

		// 匹配当前国家ID 和 最后一次国家是否相等
		if (currentCountryId == lastCountryId) {
			// 加载数据
			if (collectionList.isEmpty()) {
				getData(1);
			}
		} else {
			lastCountryId = currentCountryId;
			collectionList = new ArrayList<AppCollectionBean>();
			pulltoRefreshLv.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(loadingView);
			appCollectionAdapter.setList(collectionList); // 清空adapter，防止显示缓存数据
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
		rootView = inflater.inflate(R.layout.fragment_wallpaper_album, container, false);
		errorView = rootView.findViewById(R.id.e_error_rl);

		pulltoRefreshLv = (PullToRefreshListView) rootView.findViewById(R.id.fhr_wallpaper_album_lv);
		appCollectionLv = pulltoRefreshLv.getRefreshableView();

		pulltoRefreshLv.setOnRefreshListener(onRefreshListener);
		pulltoRefreshLv.setMode(Mode.DISABLED);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshLv.setEmptyView(loadingView);
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mActivity);
		soundListener.addSoundEvent(State.REFRESH_TO_RESET, R.raw.refresh);
		pulltoRefreshLv.setOnPullEventListener(soundListener);
		appCollectionAdapter = new AppCollectionListAdapter(mActivity);
		appCollectionLv.setAdapter(appCollectionAdapter);
		appCollectionAdapter.setList(collectionList);
		appCollectionLv.setRecyclerListener(recyclerListener);
		// 记录最后一次国家ID
		lastCountryId = CountryManager.getInstance().getCountryId(mActivity);
		return rootView;
	}

	private void getData(int page) {
		request = new HomeCollectionRequest();
		pager = new Pager(page);
		request.setPage(pager);
		request.setData(new HomeCollectionRequestData());
		DataFetcher.getInstance().getHomeCollectionData(request, myResponseListent, myErrorListener, true);
	}

	private RecyclerListener recyclerListener = new RecyclerListener() {

		@Override
		public void onMovedToScrapHeap(View view) {
			ImageView image = (ImageView) view.findViewById(R.id.hci_collection_logo_iv);
			Utils.recycleImageView(image);
		}
	};

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString());
			pulltoRefreshLv.onRefreshComplete();
			HomeCollectionResponse homeCollectionResponse = (HomeCollectionResponse) JsonUtil.jsonToBean(response,
					HomeCollectionResponse.class);
			if (homeCollectionResponse != null && homeCollectionResponse.state.code == 200) {
				if (!homeCollectionResponse.collectionList.isEmpty()) {
					collectionList.addAll(homeCollectionResponse.collectionList);
					appCollectionAdapter.setList(collectionList);
					if (homeCollectionResponse.isLast) {
						cancleGridViewScorllable();
					} else {
						pulltoRefreshLv.setMode(Mode.PULL_FROM_END);
					}

				} else {
					if (collectionList.isEmpty()) {
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
			pulltoRefreshLv.onRefreshComplete();
			showErrorView();
			error.printStackTrace();
		}
	};

	private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			collectionList = new ArrayList<AppCollectionBean>();
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
		if (collectionList.isEmpty()) {
			pulltoRefreshLv.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
			errorView.findViewById(R.id.e_retry_btn).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!NetworkUtils.isNetworkAvailable(mActivity)) {
						ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
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

	private void cancleGridViewScorllable() {
		if (pulltoRefreshLv != null)
			pulltoRefreshLv.setMode(Mode.DISABLED);
	}

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
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisibleToUser = isVisibleToUser;
		String unknow = "";
		if (mActivity != null) {
			unknow = NetworkUtils.getNetworkInfo(mActivity);
		}
		if (errorView == null || pulltoRefreshLv == null) {
			return;
		}
		if (isVisibleToUser && errorView.getVisibility() == View.VISIBLE && !unknow.equals("unknow")) {
			// 重新请求数据
			pulltoRefreshLv.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(loadingView);
			getData(1);
		}
		DataEyeManager.getInstance().module(StatisticConstan.ModuleName.HOME_CONLLECTION, isVisibleToUser);
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
