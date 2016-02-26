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
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.ThemeResponse;
import com.x.publics.http.model.WallpaperRequest;
import com.x.publics.http.model.WallpaperRequest.WallpaperRequestData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.ThemeBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.receiver.ConnectChangeReceiver;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.WallpaperAlbumListAdapter;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;

/**
 * @ClassName: WallpaperAlbumFragment
 * @Desciption: TODO
 
 * @Date: 2014-3-13 下午5:00:54
 */

public class WallpaperAlbumFragment extends BaseFragment {

	private ListView wallpaperAlbumLv;
	private PullToRefreshListView pulltoRefreshLv;
	private WallpaperAlbumListAdapter wallpaperAlbumListAdapter;
	private ArrayList<ThemeBean> themeList = new ArrayList<ThemeBean>();

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
		WallpaperAlbumFragment fragment = new WallpaperAlbumFragment();
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
		if (wallpaperAlbumListAdapter != null) {
			wallpaperAlbumListAdapter.notifyDataSetChanged();
		}

		// 获取当前国家ID
		currentCountryId = CountryManager.getInstance().getCountryId(mActivity);

		// 匹配当前国家ID 和 最后一次国家是否相等
		if (currentCountryId == lastCountryId) {
			// 加载数据
			if (themeList.isEmpty()) {
				getData(1);
			}
		} else {
			lastCountryId = currentCountryId;
			themeList = new ArrayList<ThemeBean>();
			pulltoRefreshLv.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(loadingView);
			wallpaperAlbumListAdapter.setList(themeList); // 清空adapter，防止显示缓存数据
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
		rootView = inflater.inflate(R.layout.fragment_wallpaper_album, container, false);  //fragment根布局
		errorView = rootView.findViewById(R.id.e_error_rl);

		pulltoRefreshLv = (PullToRefreshListView) rootView.findViewById(R.id.fhr_wallpaper_album_lv);  //下拉刷新控件
		wallpaperAlbumLv = pulltoRefreshLv.getRefreshableView();

		pulltoRefreshLv.setOnRefreshListener(onRefreshListener);
		pulltoRefreshLv.setMode(Mode.DISABLED);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshLv.setEmptyView(loadingView);
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mActivity);
		soundListener.addSoundEvent(State.REFRESH_TO_RESET, R.raw.refresh);
		pulltoRefreshLv.setOnPullEventListener(soundListener);  //下拉监听
		wallpaperAlbumListAdapter = new WallpaperAlbumListAdapter(mActivity); 
		wallpaperAlbumLv.setAdapter(wallpaperAlbumListAdapter); //装载数据
		wallpaperAlbumListAdapter.setList(themeList);

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
		return Constan.Ct.WALLPAPER_ALBUM;
	}

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			pulltoRefreshLv.onRefreshComplete();
			LogUtil.getLogger().d("response==>" + response.toString());
			ThemeResponse themeResponse = (ThemeResponse) JsonUtil.jsonToBean(response, ThemeResponse.class);
			if (themeResponse != null && themeResponse.state.code == 200) {
				if (!themeResponse.themelist.isEmpty()) {
					themeList.addAll(themeResponse.themelist);
					wallpaperAlbumListAdapter.setList(themeList);
					if (themeResponse.isLast) {
						cancleGridViewScorllable();
					} else {
						pulltoRefreshLv.setMode(Mode.BOTH);
					}
				} else {
					if (themeList.isEmpty()) {
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
			themeList = new ArrayList<ThemeBean>();
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
		if (themeList.isEmpty()) {
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
			pulltoRefreshLv.setMode(Mode.PULL_FROM_START);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
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
		DataEyeManager.getInstance().module(StatisticConstan.ModuleName.WALLPAPER_ALBUM, isVisibleToUser);
		if (isVisibleToUser) {
			//			DataEyeManager.getInstance().source(StatisticConstan.SrcName.WALLPAPER_LIVE_WALLPAPER, 0, null, 0L, null, null,false);
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
