package com.x.ui.activity.wallpaper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.DataFetcher;
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
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.receiver.ConnectChangeReceiver;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.AppListAdapter;
import com.x.ui.adapter.AppListViewHolder;
import com.x.ui.adapter.gridview.AppGridViewHolder;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshGridView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;

/**
 * 
* @ClassName: WallpaperLiveFragment
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-4 上午10:04:07
*
 */

public class WallpaperLiveFragment extends BaseFragment {

	private GridView mGridView;
	private PullToRefreshGridView pulltoRefreshGv;

	private AppListAdapter appListAdapter;
	private DownloadUiReceiver mDownloadUiReceiver;
	private ValidWifiReceiver mValidWifiReceiver;
	private boolean mIsVisibleToUser;
	private ArrayList<AppInfoBean> appList = new ArrayList<AppInfoBean>();
	private boolean inited = false;

	private TabRequest request;
	private Pager pager;
	private View loadingView;
	private View errorView;
	private int pageNum = 1;
	private int lastCountryId;
	private int currentCountryId;
	private View loadingPb, loadingLogo;

	public static Fragment newInstance(Bundle bundle) {
		WallpaperLiveFragment fragment = new WallpaperLiveFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 注册广播
		if (!inited) {
			registUiReceiver();
		}

		// 刷新界面
		if (appListAdapter != null) {
			appListAdapter.notifyDataSetChanged();
		}

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
			pulltoRefreshGv.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			pulltoRefreshGv.setEmptyView(loadingView);
			appListAdapter.setList(appList);
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
		mGridView.setNumColumns(ResourceUtil.getInteger(mActivity, R.integer.LIVE_WALLPAPER_COLUMN));

		pulltoRefreshGv.setOnRefreshListener(onRefreshListener);
		pulltoRefreshGv.setMode(Mode.DISABLED);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshGv.setEmptyView(loadingView);
		SoundPullEventListener<GridView> soundListener = new SoundPullEventListener<GridView>(mActivity);
		soundListener.addSoundEvent(State.REFRESH_TO_RESET, R.raw.refresh);
		pulltoRefreshGv.setOnPullEventListener(soundListener);

		appListAdapter = new AppListAdapter(mActivity, getCt());
		mGridView.setAdapter(appListAdapter);
		appListAdapter.setList(appList);

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
	}

	public int getCt() {
		return Constan.Ct.WALLPAPER_LIVE;
	}

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			pulltoRefreshGv.onRefreshComplete();
			LogUtil.getLogger().d("response==>" + response.toString());
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

	private ErrorListener myErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			pulltoRefreshGv.onRefreshComplete();
			showErrorView();
			error.printStackTrace();
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
			appListAdapter.setList(appList);

			if (tabResponse.isLast) {
				cancleGridViewScorllable();
			} else {
				pulltoRefreshGv.setMode(Mode.BOTH);
			}
		}

	}

	private OnRefreshListener2<GridView> onRefreshListener = new OnRefreshListener2<GridView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
			appList = new ArrayList<AppInfoBean>();
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
		if (appList.isEmpty()) {
			appListAdapter.setList(appList);
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
				String url, packageName;
				String percent;
				View taskListItem;
				AppListViewHolder viewHolder;
				AppGridViewHolder appGridViewHolder;
				switch (type) {
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = mGridView.findViewWithTag(url);
						if (taskListItem == null)
							return;

						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.CANINSTALL, mActivity, null);
					}
					break;
				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						int status = getAppstatus(url);
						notifyAppDataChange(url, status);
						taskListItem = mGridView.findViewWithTag(url);
						if (taskListItem == null)
							return;

						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(status, mActivity, null);
					}
					break;

				case MyIntents.Types.COMPLETE_INSTALL:
					LogUtil.getLogger().d("handler Intent:COMPLETE_INSTALL");
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANLAUNCH);
						taskListItem = mGridView.findViewWithTag(url);
						if (taskListItem == null)
							return;

						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.CANLAUNCH, mActivity, null);
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
						taskListItem = mGridView.findViewWithTag(url);
						if (taskListItem == null)
							return;

						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.WAITING, mActivity, percent);
					}
					break;
				case MyIntents.Types.PREDOWNLOAD:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CONNECTING);
						taskListItem = mGridView.findViewWithTag(url);
						if (taskListItem == null)
							return;

						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.CONNECTING, mActivity, percent);
					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = mGridView.findViewWithTag(url);
						if (taskListItem == null)
							return;

						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.PAUSED, mActivity, percent);
					}
					break;
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = mGridView.findViewWithTag(url);
						if (taskListItem == null)
							return;

						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);
					}

					break;
				case MyIntents.Types.MAKE_UPGRADE:
					notifyPatchUpgrade();
					break;
				case MyIntents.Types.MERGE_PATCH:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						taskListItem = mGridView.findViewWithTag(url);
						if (taskListItem == null)
							return;

						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshMerge(mActivity);
					}
					break;
				}
			}
		}

		private void notifyPatchUpgrade() {
			List<AppInfoBean> list;
			if (mGridView.getAdapter() == null) {
				return;
			}
			list = ((AppListAdapter) (mGridView.getAdapter())).getList();
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

					// 增量更新 刷新显示增量大小
					View taskListItem = mGridView.findViewWithTag(appInfoBean.getUrl());
					AppListViewHolder viewHolder = new AppListViewHolder(taskListItem);
					if (viewHolder != null) {
						viewHolder.refreshPatchUpdate(appInfoBean);
					}
				}
			}
		}

		private int getAppstatus(String url) {
			List<AppInfoBean> list;
			int status = AppInfoBean.Status.NORMAL;
			if (mGridView.getAdapter() == null) {
				return status;
			}
			list = ((AppListAdapter) (mGridView.getAdapter())).getList();
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

		private void notifyAppDataChange(String url, int status) {
			List<AppInfoBean> list;
			if (mGridView.getAdapter() == null) {
				return;
			}
			list = ((AppListAdapter) (mGridView.getAdapter())).getList();
			for (AppInfoBean appInfoBean : list) {
				if (appInfoBean.getUrl() != null && appInfoBean.getUrl().equals(url)) {
					appInfoBean.setStatus(status);
					break;
				}
			}
		}

		private void notifyUninstall(String packageName) {
			List<AppInfoBean> list;
			if (mGridView.getAdapter() == null) {
				return;
			}
			list = ((AppListAdapter) (mGridView.getAdapter())).getList();
			for (AppInfoBean appInfoBean : list) {
				if (appInfoBean.getPackageName() != null && appInfoBean.getPackageName().equals(packageName)) {
					appInfoBean.setStatus(AppInfoBean.Status.NORMAL);
					appInfoBean.setPatch(false);
					appInfoBean.setPatchUrl("");
					appInfoBean.setPatchFileSize(0l);
					break;
				}
			}
			appListAdapter.setList(list);
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

	private class ValidWifiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && mIsVisibleToUser == true && errorView.getVisibility() == View.VISIBLE) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				switch (type) {
				case MyIntents.Types.VALID_WIFI:// 由无网络到有wifi的通知
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
		DataEyeManager.getInstance().module(StatisticConstan.ModuleName.WALLPAPER_LIVE_WALLPAPER, isVisibleToUser);
		if (isVisibleToUser) {
			DataEyeManager.getInstance().source(StatisticConstan.SrcName.WALLPAPER_LIVE_WALLPAPER, 0, null, 0L, null,
					null, false);
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
