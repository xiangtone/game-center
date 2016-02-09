/**   
 * @Title: RecommendFragment.java
 * @Package com.mas.amineappstore.activity
 * @Description: TODO 
 
 * @date 2014-2-12 下午07:48:21
 * @version V1.0   
 */

package com.x.ui.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.x.business.country.CountryManager;
import com.x.business.dynamiclistview.DynamicListViewManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.AppDownloadLogResponse;
import com.x.publics.http.model.BannerRequest;
import com.x.publics.http.model.BannerResponse;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.TabRequest;
import com.x.publics.http.model.TabResponse;
import com.x.publics.http.model.TabRequest.TabRequestData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SettingsUtils;
import com.x.publics.utils.SilentUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.receiver.ConnectChangeReceiver;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.activity.downloadshow.AppsDownloadingShowActivity;
import com.x.ui.adapter.AppListAdapter;
import com.x.ui.adapter.AppListViewHolder;
import com.x.ui.adapter.gridview.AppGridListAdapter;
import com.x.ui.adapter.gridview.AppGridViewHolder;
import com.x.ui.adapter.gridview.base.GridItemClickListener;
import com.x.ui.view.dynamic.DynamicListView;
import com.x.ui.view.dynamic.DynamicListViewAdapter;
import com.x.ui.view.dynamic.DynamicListViewItem;
import com.x.ui.view.dynamic.OnRowAdditionAnimationListener;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;
import com.x.ui.view.viewflow.BannerViewFlow;

/**
 * @ClassName: RecommendFragment
 * @Description: TODO
 
 * @date 2014-2-12 下午07:48:21
 * 
 */

public class HomeRecommendFragment extends BaseFragment {

	private AppListAdapter appListAdapter;
	private AppGridListAdapter appGridListAdapter;
	private DownloadUiReceiver mDownloadUiReceiver;
	private ValidWifiReceiver mValidWifiReceiver;
	private boolean mIsVisibleToUser = false;
	private ArrayList<AppInfoBean> appList = new ArrayList<AppInfoBean>();
	private PullToRefreshListView pulltoRefreshLv;
	private ListView appRecommenedLv;
	private int rowNum = 1;
	private boolean inited = false;
	private boolean isFicheMode = false;
	private boolean lastMode = false;
	private TabRequest request;
	private Pager pager;
	private View loadingView, errorView;
	private int pageNum = 1;
	private BannerViewFlow bannerViewFlow;
	private View banner;
	private int lastCountryId;
	private int currentCountryId;
	private boolean hasBannerRequest = false;
	private View loadingPb, loadingLogo;

	/*新功能：实时下载数据滚动显示*/
	private Timer timer;
	private int itemCount = 1;
	private int index, mItemNum, listSize;
	private DynamicListView dynamicListView;
	private boolean hasData, hasInitedDynamicView = false;
	private DynamicListViewAdapter dynamicListViewAdapter;
	private List<DynamicListViewItem> lists = new ArrayList<DynamicListViewItem>();
	private static final int ADD_ITEM = 101;
	private static final int SHOW_BANNER_VIEW = 102;

	public static Fragment newInstance(Bundle bundle) {
		HomeRecommendFragment fragment = new HomeRecommendFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lastMode = SettingsUtils.getValue(mActivity, SettingsUtils.IS_FICHE_MODE, false);
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

		// 获取当前国家ID
		currentCountryId = CountryManager.getInstance().getCountryId(mActivity);

		// 匹配当前国家ID 和 最后一次国家是否相等
		if (currentCountryId == lastCountryId) {
			// 加载数据
			if (appList.isEmpty()) {
				getData(1);
			}
		} else {
			lastCountryId = currentCountryId;
			appList = new ArrayList<AppInfoBean>();
			errorView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(loadingView);
			if (isFicheMode) {
				appGridListAdapter.setList(appList);
			} else {
				appListAdapter.setList(appList);
			}
			getData(1);
		}

		// 开始滚动任务
		startScrollTask();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopScrollTask();
	}

	/**
	 * 获取banner的ct值
	 * 
	 * @return
	 */
	private int getBannerCt() {

		// Home
		if (getParentFragment().getClass().equals(HomeFragment.class)) {
			return Constan.Ct.HOME_BANNER;
		}
		// App
		if (getParentFragment().getClass().equals(AppsFragment.class)) {
			return Constan.Ct.APP_BANNER;
		}
		// Game
		if (getParentFragment().getClass().equals(GamesFragment.class)) {
			return Constan.Ct.GAME_BANNER;
		}

		return 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = null;
		rootView = inflater.inflate(R.layout.fragment_home_recommend, container, false);

		pulltoRefreshLv = (PullToRefreshListView) rootView.findViewById(R.id.fhr_recommend_app_lv);
		appRecommenedLv = pulltoRefreshLv.getRefreshableView();

		banner = mActivity.getLayoutInflater().inflate(R.layout.fragment_banner_and_scrollview, null);
		bannerViewFlow = new BannerViewFlow(mActivity, banner, R.id.fb_viewflow, R.id.fb_viewflowindic,
				getPargentViewPager(), getBannerCt());
		appRecommenedLv.addHeaderView(banner);

		// 新功能：实时下载数据滚动显示
		initDynamicListView();

		pulltoRefreshLv.setOnRefreshListener(onRefreshListener);
		pulltoRefreshLv.setMode(Mode.DISABLED);
		errorView = inflater.inflate(R.layout.error, null);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		errorView.setVisibility(View.GONE);
		pulltoRefreshLv.setEmptyView(loadingView);
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mActivity);
		soundListener.addSoundEvent(State.REFRESH_TO_RESET, R.raw.refresh);
		pulltoRefreshLv.setOnPullEventListener(soundListener);
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
		request = new TabRequest();
		pager = new Pager(page);
		request.setPager(pager);
		request.setData(new TabRequestData(getCt()));
		DataFetcher.getInstance().getHomeData(request, myResponseListent, myErrorListener, true);

		BannerRequest bannerRequest = new BannerRequest();
		bannerRequest.setCt(getCt());
		DataFetcher.getInstance().getBannerData(bannerRequest, myBannerResponseListent, myBannerErrorListener, true);
		hasBannerRequest = true;
	}

	private void getMoreData(int page) {
		request = new TabRequest();
		pager = new Pager(page);
		request.setPager(pager);
		request.setData(new TabRequestData(getCt()));
		DataFetcher.getInstance().getHomeData(request, myResponseListent, myErrorListener, true);
		hasBannerRequest = false;
	}

	public int getCt() {
		// TODO Auto-generated method stub
		return Constan.Ct.HOME_RECOMMEND;
	}

	public ViewPager getPargentViewPager() {
		return ((HomeFragment) getParentFragment()).mViewPager;
	}

	private Listener<JSONObject> myBannerResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			if (hasBannerRequest) {
				pulltoRefreshLv.onRefreshComplete();
			}
			LogUtil.getLogger().d("response==>" + response.toString());
			BannerResponse bannerResponse = (BannerResponse) JsonUtil.jsonToBean(response, BannerResponse.class);
			if (bannerResponse != null && bannerResponse.state.code == 200 && bannerResponse.themelist != null) {
				bannerViewFlow.creatMyViewFlow(bannerResponse.themelist);
			} else {
				//				bannerViewFlow.creatMyViewFlow(bannerResponse.themelist);
			}
		}
	};

	private ErrorListener myBannerErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
		}
	};

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString());
			if (!hasBannerRequest) {
				pulltoRefreshLv.onRefreshComplete();
			}
			TabResponse tabResponse = (TabResponse) JsonUtil.jsonToBean(response, TabResponse.class);
			if (tabResponse != null && tabResponse.state.code == 200) {
				if (!tabResponse.applist.isEmpty()) {
					Utils.executeAsyncTask(new makeResponseData(), tabResponse);
				} else {
					if (appList.isEmpty()) {
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

	private class makeResponseData extends AsyncTask<TabResponse, Void, TabResponse> {

		@Override
		protected TabResponse doInBackground(TabResponse... params) {
			TabResponse tabResponse = params[0];
			HashMap<String, InstallAppBean> localAppMap = LocalAppEntityManager.getInstance().getAllAppsMap();
			HashMap<String, DownloadBean> downloadAppMap = DownloadEntityManager.getInstance()
					.getAllDownloadResourceIdMap();
			tabResponse.applist = compareList(tabResponse.applist, localAppMap, downloadAppMap);
			return tabResponse;
		}

		@Override
		protected void onPostExecute(TabResponse tabResponse) {
			super.onPostExecute(tabResponse);
			appList.addAll(tabResponse.applist);
			// 判断当前选择的显示模式
			if (isFicheMode) {
				appGridListAdapter.setList(appList);
			} else {
				appListAdapter.setList(appList);
			}

			if (tabResponse.isLast) {
				cancleGridViewScorllable();
			} else {
				pulltoRefreshLv.setMode(Mode.BOTH);
			}
		}

	}

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
			appList = new ArrayList<AppInfoBean>();
			pageNum = 1;
			getData(1);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			getMoreData(++pageNum);
		}

	};

	private void showErrorView() {
		if (pageNum > 1) {
			--pageNum;
		}
		if (appList.isEmpty()) {
			if (isFicheMode) {
				appGridListAdapter.setList(appList);
			} else {
				appListAdapter.setList(appList);
			}
			//pulltoRefreshLv.setVisibility(View.GONE);
			//errorView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(errorView);
			pulltoRefreshLv.setMode(Mode.DISABLED);
			errorView.findViewById(R.id.e_retry_btn).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!NetworkUtils.isNetworkAvailable(mActivity)) {
						ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return;
					}
					errorView.setVisibility(View.GONE);
					pulltoRefreshLv.setEmptyView(loadingView);
					getDynamicData(); // 获取滚动数据
					getData(1);
				}
			});
			stopScrollTask();// 停止滚动任务
		}
	}

	private void cancleGridViewScorllable() {
		if (pulltoRefreshLv != null)
			pulltoRefreshLv.setMode(Mode.PULL_FROM_START);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.getLogger().d("FragmentA==============onDestroyView");
	}

	private void registUiReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);

		mValidWifiReceiver = new ValidWifiReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(ConnectChangeReceiver.NETWORK_CONNECTED_ACTION);
		BroadcastManager.registerReceiver(mValidWifiReceiver, filter2);
		inited = true;
		LogUtil.getLogger().d("FragmentA==============registUiReceiver");
	}

	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String url = null, packageName;
				String percent;
				View taskListItem;
				String localPath;
				int silentInstallResult;

				AppGridViewHolder appGridViewHolder;
				AppListViewHolder viewHolder;
				switch (type) {
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshDownloadStatus(AppInfoBean.Status.CANINSTALL, mActivity, null);
						} else {
							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshDownloadStatus(AppInfoBean.Status.CANINSTALL, mActivity, null);
						}
					}
					break;
				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						int status = getAppstatus(url);
						notifyAppDataChange(url, status);
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshDownloadStatus(status, mActivity, null);
						} else {
							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshDownloadStatus(status, mActivity, null);
						}

					}
					break;
				case MyIntents.Types.COMPLETE_INSTALL:
					LogUtil.getLogger().d("handler Intent:COMPLETE_INSTALL");
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANLAUNCH);
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshDownloadStatus(AppInfoBean.Status.CANLAUNCH, mActivity, null);

						} else {

							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshDownloadStatus(AppInfoBean.Status.CANLAUNCH, mActivity, null);
						}

					}
					mActivity.removeStickyBroadcast(new Intent(MyIntents.INTENT_UPDATE_UI));
					break;

				case MyIntents.Types.COMPLETE_UNIINSTALL:
					packageName = intent.getStringExtra(MyIntents.PACKAGENAME);
					if (!TextUtils.isEmpty(packageName)) {
						notifyUninstall(packageName);
					}
					break;

				case MyIntents.Types.WAIT:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.WAITING);
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshDownloadStatus(AppInfoBean.Status.WAITING, mActivity, percent);
						} else {

							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshDownloadStatus(AppInfoBean.Status.WAITING, mActivity, percent);
						}

					}
					break;
				case MyIntents.Types.PREDOWNLOAD:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CONNECTING);
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshDownloadStatus(AppInfoBean.Status.CONNECTING, mActivity, percent);

						} else {

							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshDownloadStatus(AppInfoBean.Status.CONNECTING, mActivity, percent);
						}

					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshDownloadStatus(AppInfoBean.Status.PAUSED, mActivity, percent);
						} else {
							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshDownloadStatus(AppInfoBean.Status.PAUSED, mActivity, percent);
						}
					}
					break;
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);

						} else {

							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);
						}
					}
					break;
				case MyIntents.Types.MAKE_UPGRADE:
					notifyPatchUpgrade();
					break;
				case MyIntents.Types.MERGE_PATCH:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshMerge();
						} else {
							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshMerge(mActivity);
						}
					}
					break;
				case MyIntents.Types.INSTALLING:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url)) {
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshInstalling();
						} else {
							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshInstalling();
						}
					}
					break;
				case MyIntents.Types.INSTALL_RESULT:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					silentInstallResult = intent.getIntExtra(MyIntents.INSTALL_RESULT_CODE, 0);

					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url) && silentInstallResult != SilentUtil.INSTALL_SUCCEEDED) {

						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshInstall();
						} else {
							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshInstall();
						}
						DownloadEntityManager.getInstance().updateDownloadInstallStatus(url);
						ToastUtil.show(mActivity, R.string.toast_silent_install_failed, Toast.LENGTH_LONG);
						PackageUtil.normalInstall(mActivity, localPath);
					}
					break;
				default:
					break;
				}
			}
		}

		private void notifyPatchUpgrade() {
			List<AppInfoBean> list;
			if (appRecommenedLv.getAdapter() == null) {
				return;
			}
			if (isFicheMode) {
				list = ((AppGridListAdapter) ((HeaderViewListAdapter) appRecommenedLv.getAdapter()).getWrappedAdapter())
						.getList();
			} else {
				list = ((AppListAdapter) ((HeaderViewListAdapter) appRecommenedLv.getAdapter()).getWrappedAdapter())
						.getList();
			}
			if (list == null)
				return;
			HashMap<String, InstallAppBean> updateAppMap = UpdateManage.getInstance(mActivity)
					.findAllUpdateInstallAppMap();
			for (AppInfoBean appInfoBean : list) {
				String packageName = appInfoBean.getPackageName();
				InstallAppBean updateAppBean = updateAppMap.get(packageName);
				if (updateAppBean != null && updateAppBean.getIsPatch()) {
					appInfoBean.setPatch(true);
					appInfoBean.setUrl(updateAppBean.getUrl());
					appInfoBean.setPatchUrl(updateAppBean.getUrlPatch());
					appInfoBean.setPatchFileSize(updateAppBean.getPatchSize());

					if (!isFicheMode) { // 增量更新 刷新显示增量大小
						View taskListItem = appRecommenedLv.findViewWithTag(appInfoBean.getUrl());
						if (taskListItem == null)
							return;
						AppListViewHolder viewHolder = new AppListViewHolder(taskListItem);
						if (viewHolder != null) {
							viewHolder.refreshPatchUpdate(appInfoBean);
						}
					}
				}
			}
		}

		private void notifyAppDataChange(String url, int status) {
			List<AppInfoBean> list;
			if (appRecommenedLv.getAdapter() == null) {
				return;
			}
			if (isFicheMode) {
				list = ((AppGridListAdapter) ((HeaderViewListAdapter) appRecommenedLv.getAdapter()).getWrappedAdapter())
						.getList();
			} else {
				list = ((AppListAdapter) ((HeaderViewListAdapter) appRecommenedLv.getAdapter()).getWrappedAdapter())
						.getList();
			}
			for (AppInfoBean appInfoBean : list) {
				if (appInfoBean.getUrl() != null && appInfoBean.getUrl().equals(url)) {
					appInfoBean.setStatus(status);
					break;
				}
			}
		}

		private int getAppstatus(String url) {
			List<AppInfoBean> list;
			int status = AppInfoBean.Status.NORMAL;
			if (appRecommenedLv.getAdapter() == null) {
				return status;
			}
			if (isFicheMode) {
				list = ((AppGridListAdapter) ((HeaderViewListAdapter) appRecommenedLv.getAdapter()).getWrappedAdapter())
						.getList();
			} else {
				list = ((AppListAdapter) ((HeaderViewListAdapter) appRecommenedLv.getAdapter()).getWrappedAdapter())
						.getList();
			}
			for (AppInfoBean appInfoBean : list) {
				if (appInfoBean.getUrl() != null && appInfoBean.getUrl().equals(url)) {
					HashMap<String, InstallAppBean> map = LocalAppEntityManager.getInstance().getAllAppsMap();
					InstallAppBean installAppBean = map.get(appInfoBean.getPackageName());
					if (installAppBean != null && installAppBean.getVersionCode() < appInfoBean.getVersionCode()) {
						status = AppInfoBean.Status.CANUPGRADE;
					} else if (installAppBean != null) {
						status = AppInfoBean.Status.CANLAUNCH;
					}
					break;
				}
			}
			return status;
		}

		private void notifyUninstall(String packageName) {
			List<AppInfoBean> list;
			if (appRecommenedLv.getAdapter() == null) {
				return;
			}
			if (isFicheMode) {
				list = ((AppGridListAdapter) ((HeaderViewListAdapter) appRecommenedLv.getAdapter()).getWrappedAdapter())
						.getList();
			} else {
				list = ((AppListAdapter) ((HeaderViewListAdapter) appRecommenedLv.getAdapter()).getWrappedAdapter())
						.getList();
			}
			for (AppInfoBean appInfoBean : list) {
				if (appInfoBean.getPackageName() != null && appInfoBean.getPackageName().equals(packageName)) {
					appInfoBean.setStatus(AppInfoBean.Status.NORMAL);
					appInfoBean.setPatch(false);
					appInfoBean.setPatchUrl("");
					appInfoBean.setPatchFileSize(0l);
					break;
				}
			}
			if (isFicheMode) {
				appGridListAdapter.setList(list);
			} else {
				appListAdapter.setList(list);
			}
		}

	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
		BroadcastManager.unregisterReceiver(mValidWifiReceiver);
	}

	/**
	 * 初始化显示模式
	 */
	private void initDisplayMode() {
		// 获取当前选中的显示模式
		isFicheMode = SettingsUtils.getValue(mActivity, SettingsUtils.IS_FICHE_MODE, false);

		if (isFicheMode) {
			// 刷新Item状态
			if (appGridListAdapter != null) {
				appGridListAdapter.notifyDataSetChanged();
			}

			// 判断appGridListAdapter是否已经初始化，并对比上一次显示模式
			if (!appList.isEmpty() && lastMode == isFicheMode) {
				return;
			}

			// 同步当前模式
			lastMode = isFicheMode;
			rowNum = getResources().getInteger(R.integer.gridview_row_count);
			appGridListAdapter = new AppGridListAdapter(mActivity, getCt());
			appGridListAdapter.setNumColumns(rowNum);
			appGridListAdapter.setOnGridClickListener(new GridItemClickListener() {
				@Override
				public void onGridItemClicked(View v, int position, long itemId) {
					// check network
					if (!NetworkUtils.isNetworkAvailable(mActivity)) {
						ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return;
					}

					if (appList.size() > 0) {
						AppInfoBean appInfoBean = appList.get(position);
						Intent intent = new Intent(mActivity, AppDetailActivity.class);
						intent.putExtra("appInfoBean", appInfoBean);
						intent.putExtra("ct", getCt());
						mActivity.startActivity(intent);
					}
				}
			});
			appRecommenedLv.setAdapter(appGridListAdapter);
			appGridListAdapter.setList(appList);

		} else {
			// 刷新Item状态
			if (appListAdapter != null) {
				appListAdapter.notifyDataSetChanged();
			}

			// 判断appListAdapter是否已经初始化，并对比上一次显示模式
			if (!appList.isEmpty() && lastMode == isFicheMode) {
				return;
			}

			// 同步当前模式
			lastMode = isFicheMode;
			appListAdapter = new AppListAdapter(mActivity, getCt());
			appRecommenedLv.setAdapter(appListAdapter);
			appListAdapter.setList(appList);
		}

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
		if (isVisibleToUser && errorView.getVisibility() == View.VISIBLE && !unknow.equals("unknow")) {
			//重新请求数据
			errorView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(loadingView);
			pulltoRefreshLv.setVisibility(View.VISIBLE);
			getData(1);
		}

		if (getCt() == Constan.Ct.HOME_RECOMMEND) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.HOME_RECOMMEND, isVisibleToUser);
			if (isVisibleToUser) {
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.HOME_RECOMMEND, 0, null, 0L, null, null,
						false);
			}
		} else if (getCt() == Constan.Ct.APP_NEW) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.APPS_NEW, isVisibleToUser);
			if (isVisibleToUser) {
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.APPS_NEW, 0, null, 0L, null, null, false);
			}
		} else if (getCt() == Constan.Ct.GAME_NEW) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.GAMES_NEW, isVisibleToUser);
			if (isVisibleToUser) {
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.GAMES_NEW, 0, null, 0L, null, null, false);
			}
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
		if (null != dynamicListView) {
			SkinConfigManager.getInstance()
					.setViewBackground(mActivity, dynamicListView, SkinConstan.LIST_VIEW_ITEM_BG);
		}
	}

	/**
	* @Title: initDynamicListView 
	* @Description: 新功能：实时下载数据滚动显示
	* @param     
	* @return void
	 */
	private void initDynamicListView() {
		if (null != banner && getCt() == Constan.Ct.HOME_RECOMMEND) {
			List<DynamicListViewItem> objs = new ArrayList<DynamicListViewItem>();
			dynamicListView = (DynamicListView) banner.findViewById(R.id.app_dynamiclistview);
			dynamicListViewAdapter = new DynamicListViewAdapter(mActivity, R.layout.app_dynamiclistview_item, objs);
			dynamicListView.setAdapter(dynamicListViewAdapter);
			dynamicListView.setData(objs);
			dynamicListView.setRowAdditionAnimationListener(new OnRowAdditionAnimationListener() {

				@Override
				public void onRowAdditionAnimationStart() {
					// TODO Auto-generated method stub
					dynamicListViewAdapter.notifyDataSetChanged();
				}

				@Override
				public void onRowAdditionAnimationEnd() {
					// TODO Auto-generated method stub
					dynamicListView.showItemCount(itemCount);
				}
			});

			dynamicListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					if (!NetworkUtils.isNetworkAvailable(mActivity)) {
						ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
								ToastUtil.LENGTH_LONG);
						return;
					}
					Intent intent = new Intent(mActivity, AppsDownloadingShowActivity.class);
					intent.putParcelableArrayListExtra("lists", (ArrayList<? extends Parcelable>) lists);
					intent.putExtra("index", index);
					intent.putExtra("listSize", listSize);
					startActivity(intent);
				}
			});

			hasInitedDynamicView = true;
			getDynamicData();

		} else {
			hasInitedDynamicView = false;
		}
	}

	/**
	* @Title: getDynamicData 
	* @Description: 获取滚动数据
	* @param     
	* @return void
	 */
	public void getDynamicData() {
		if (getCt() == Constan.Ct.HOME_RECOMMEND)
			DynamicListViewManager.getInstance().getDynamicListViewResult(mActivity, handler);
	}

	/**
	* @Title: addDynamicListViewItem 
	* @Description: 添加item数据
	* @param     
	* @return void
	 */
	private void addDynamicItem() {
		mItemNum++;
		index = mItemNum % lists.size();
		DynamicListViewItem listItemObject = lists.get(index);
		if (listItemObject != null)
			dynamicListView.addListViewItem(listItemObject);
	}

	/**
	* @Title: startTimerTask 
	* @Description: 开始滚动任务
	* @param     
	* @return void
	 */
	private void startScrollTask() {
		if (hasData && null == timer) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(ADD_ITEM);
				}
			}, 0, 3000);
		}
	}

	/**
	* @Title: stopTimerTask 
	* @Description: 停止滚动任务
	* @param     
	* @return void
	 */
	private void stopScrollTask() {
		if (null != timer) {
			timer.cancel();
			timer = null;
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (hasInitedDynamicView) {

				switch (msg.what) {
				// 获取数据成功
				case DynamicListViewManager.onSuccess:
					AppDownloadLogResponse response = (AppDownloadLogResponse) msg.obj;
					lists = response.dataList;
					listSize = response.listSize;
					handler.sendEmptyMessage(SHOW_BANNER_VIEW);
					hasData = true;
					startScrollTask();
					break;

				// 获取数据失败、空数据
				case DynamicListViewManager.onFailure:
					dynamicListView.setVisibility(View.GONE);
					hasData = false;
					break;

				case ADD_ITEM:
					addDynamicItem();
					break;

				case SHOW_BANNER_VIEW:
					dynamicListView.setVisibility(View.VISIBLE);
					if (isFicheMode) {
						if (appGridListAdapter != null)
							appGridListAdapter.notifyDataSetChanged();
					} else {
						if (appListAdapter != null)
							appListAdapter.notifyDataSetChanged();
					}
					break;
				}
			}
		}
	};

}
