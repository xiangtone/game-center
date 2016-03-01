/**   
 * @Title: SearchAppsFragment.java
 * @Package com.x.ui.activity.search
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-7-11 下午5:05:55
 * @version V1.3   
 */

package com.x.ui.activity.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.x.business.search.SearchConstan;
import com.x.business.search.SearchManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.model.KeywordsResponse;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.KeywordBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SilentUtil;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.AppListAdapter;
import com.x.ui.adapter.AppListViewHolder;
import com.x.ui.view.CustomSearchView.SearchAutoComplete;
import com.x.ui.view.autobreakview.AutoBreakAdapter;
import com.x.ui.view.autobreakview.AutoBreakListView;
import com.x.ui.view.autobreakview.AutoBreakOnItemClickListener;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

/**
 * @ClassName: SearchAppsFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-11 下午5:05:56
 * 
 */

public class SearchAppsFragment extends BaseFragment {

	private int actionRc;
	private String action;
	private String lastQueryKey;
	private int listPageNum = 1;
	private int keywordPageNum = 1;

	private ListView mListView;
	private PullToRefreshListView ptrLv;
	private AppListAdapter appListAdapter;
	private View loadingView, errorView, keywordsView;

	private List<AppInfoBean> appList = new ArrayList<AppInfoBean>();
	private List<KeywordBean> keywordList = new ArrayList<KeywordBean>();

	// 自动换行
	private boolean isLast = false;
	private boolean hasKeywordData = false;
	private boolean isVisibleToUser = false;
	private AutoBreakAdapter autoBreakAdapter;
	private AutoBreakListView autoBreakListView;
	private View loadingPb, loadingLogo;

	// 广播
	private boolean inited = false;
	private DownloadUiReceiver mDownloadUiReceiver;

	public static Fragment newInstance(Bundle bundle) {
		SearchAppsFragment fragment = new SearchAppsFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	public void setParams() {
		if (SearchManager.getInstance().getInitState(mActivity)) {
			String queryKey = SearchManager.getInstance().getQueryKey(mActivity);
			if (!TextUtils.isEmpty(queryKey) && !queryKey.equals(lastQueryKey)) {
				listPageNum = 1;
				showLoadingView();
				lastQueryKey = queryKey;
				actionRc = getRc();
				action = SearchConstan.APP_ACTION;
				getListData();
			}
		}
	}

	public void clearQueryKeyCache() {
		if (!TextUtils.isEmpty(lastQueryKey))
			lastQueryKey = null;
	}

	public void getListData() {
		SearchManager.getInstance().getQueryKeyData(mActivity, handler, actionRc, lastQueryKey, action, 0, listPageNum);
	}

	public int getRc() {
		return Constan.Rc.GET_APPS_SEARCH;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 注册广播
		if (!inited)
			registUiReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_apps, null);
		keywordsView = rootView.findViewById(R.id.ll_keywords_layout);
		ptrLv = (PullToRefreshListView) rootView.findViewById(R.id.ptr_apps_lv);
		mListView = ptrLv.getRefreshableView();
		ptrLv.setOnRefreshListener(onRefreshListener);
		ptrLv.setMode(Mode.DISABLED);
		// errorView = rootView.findViewById(R.id.e_error_rl);
		errorView = rootView.findViewById(R.id.ll_error_refresh);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		ptrLv.setEmptyView(loadingView);

		// 加入ct值
		appListAdapter = new AppListAdapter(mActivity, Constan.Ct.APP_SEARCH);
		mListView.setAdapter(appListAdapter);
		appListAdapter.setList(appList);

		// 搜索关键字列表--autoBreakListView
		autoBreakAdapter = new AutoBreakAdapter(mActivity);
		autoBreakListView = (AutoBreakListView) rootView.findViewById(R.id.autoBreakView);

		// Setter Divider Space
		int dividerSpace = ResourceUtil.getDimensionPixelSize(mActivity, R.dimen.keyword_divider_space);
		autoBreakListView.setDividerWidth(dividerSpace);
		autoBreakListView.setDividerHeight(dividerSpace);

		autoBreakListView.setAdapter(autoBreakAdapter);
		autoBreakListView.setOnItemClickListener(new AutoBreakOnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg1) {
				// check network
				if (!NetworkUtils.isNetworkAvailable(mActivity)) {
					ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}

				/* 搜索关键字 */
				lastQueryKey = Utils.removeSpace(keywordList.get(position).getKeyword());
				action = keywordList.get(position).getAction();
				actionRc = keywordList.get(position).getActionRc();

				if (position == 0) {
					refreshData();
					return;
				}

				if (actionRc == Constan.Rc.SEARCH_APP_DETAIL) {
					Intent intent = new Intent(mActivity, AppDetailActivity.class);
					intent.putExtra(SearchConstan.FROM_SEARCH, true);
					intent.putExtra(SearchConstan.ACTION_URL, action);
					intent.putExtra("searchKey", lastQueryKey);
					DataEyeManager.getInstance().source(
							StatisticConstan.SrcName.SEARCH,
							0,
							null,
							0L,
							null,
							DataEyeManager.getSearchKey(StatisticConstan.SearchType.SEARCH_ZAPP_APP + "-"
									+ lastQueryKey), true);
					startActivity(intent);
				} else {
					// 设置选中的关键字 到 搜索框中
					SearchManager.getInstance().hideSoftInput(mActivity);
					SearchAutoComplete mQueryTextView = (SearchAutoComplete) getActivity().findViewById(
							R.id.search_src_text);
					mQueryTextView.setText(lastQueryKey);

					// 保存搜索关键字
					showLoadingView();
					SearchManager.getInstance().setInitState(mActivity, true);
					SearchManager.getInstance().setQueryKey(mActivity, lastQueryKey);
					if (actionRc == Constan.Rc.KEYWORDS_APPS_LIST) {
						DataEyeManager.getInstance().source(
								StatisticConstan.SrcName.SEARCH,
								0,
								null,
								0L,
								null,
								DataEyeManager.getSearchKey(StatisticConstan.SearchType.SEARCH_ZAPP_LIST + "-"
										+ lastQueryKey), true);
					}
					getListData();
				}
			}
		});

		SearchManager.getInstance().getKwData(mActivity, handler, getAlbumId(), keywordPageNum);

		return rootView;
	}

	/**
	 * 
	 * @Title: getAlbumId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int getAlbumId() {
		return SearchConstan.Album.APPS_ALBUM_ID;
	}

	/**
	 * 
	 * @Title: refreshData
	 * @Description: 刷新数据
	 * @param
	 * @return void
	 * @throws
	 */
	private void refreshData() {

		if (isLast) {
			/*
			 * if (keywordPageNum == 1) { ToastUtil.show(mActivity,
			 * "No more Data!", ToastUtil.LENGTH_SHORT); return; } else {
			 * keywordPageNum = 1; }
			 */
			keywordPageNum = 1;
		} else {
			keywordPageNum++;
		}
		showLoadingView();
		SearchManager.getInstance().getKwData(mActivity, handler, getAlbumId(), keywordPageNum);
	}

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SearchConstan.State.SEARCH_KEYWORDS_SUCCESS:
				hasKeywordData = true;
				KeywordsResponse keywordsResponse = (KeywordsResponse) msg.obj;
				List<KeywordBean> responseList = keywordsResponse.keywordlist;
				keywordList.clear();

				// 将refresh排在第一位
				KeywordBean been = new KeywordBean();
				been.setKeyword(ResourceUtil.getString(mActivity, R.string.search_refresh_text));
				keywordList.add(been);
				for (KeywordBean keywordBean : responseList) {
					keywordList.add(keywordBean);
				}

				autoBreakAdapter.setAnim(); // 设置动画显示
				autoBreakAdapter.setList(keywordList);
				isLast = keywordsResponse.isLast;
				showKeywordsView();
				break;

			case SearchConstan.State.RESPONSE_APP_DATA_SUCCESS:
				ptrLv.onRefreshComplete();
				HashMap<String, InstallAppBean> localAppMap = LocalAppEntityManager.getInstance().getAllAppsMap();
				HashMap<String, DownloadBean> downloadAppMap = DownloadEntityManager.getInstance()
						.getAllDownloadResourceIdMap();

				Bundle bundle = msg.getData();
				isLast = bundle.getBoolean("isLast");
				boolean isRecommend = bundle.getBoolean("isRecommend");
				ArrayList<AppInfoBean> list = bundle.getParcelableArrayList("appList");
				// 是否推荐数据
				if (isRecommend) {
					appList = compareList(list, localAppMap, downloadAppMap);
				} else {
					appList.addAll(list);
					appList = compareList((ArrayList<AppInfoBean>) appList, localAppMap, downloadAppMap);
				}
				// 是否存在分页
				if (isLast || isRecommend) {
					ptrLv.setMode(Mode.DISABLED);
				} else {
					ptrLv.setMode(Mode.PULL_FROM_END);
				}
				appListAdapter.setList(appList);
				break;

			case SearchConstan.State.SEARCH_KEYWORDS_FAILURE:
			case SearchConstan.State.RESPONSE_APP_DATA_FAILURE:
				showErrorView();
				break;

			case SearchConstan.State.SEARCH_KEYWORDS_EMPTY:
				if (keywordList.isEmpty()) {
					showErrorView();
				} else {
					// 刚好是最后一页
					// ToastUtil.show(mActivity, "No more Data!",
					// ToastUtil.LENGTH_SHORT);
					isLast = true;
					showKeywordsView();
				}
				break;

			case SearchConstan.State.REFRESH_DATA_SUCCESS:
				refreshData();
				break;
			}
		}
	};

	public void showKeywordsView() {
		if (hasKeywordData) {
			ptrLv.setVisibility(View.GONE);
			errorView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			keywordsView.setVisibility(View.VISIBLE);
			SearchManager.getInstance().putCurValue(SearchConstan.APP_FRAGMENT_ID, true);
		}
	}

	private void showLoadingView() {
		appList.clear();
		appListAdapter.setList(appList);
		errorView.setVisibility(View.GONE);
		keywordsView.setVisibility(View.GONE);
		ptrLv.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.VISIBLE);
		ptrLv.setEmptyView(loadingView);
		SearchManager.getInstance().putCurValue(SearchConstan.APP_FRAGMENT_ID, false);
	}

	private void showErrorView() {
		ptrLv.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		keywordsView.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
		SearchManager.getInstance().putCurValue(SearchConstan.APP_FRAGMENT_ID, false);
		// errorView.findViewById(R.id.e_retry_btn).setOnClickListener(
		errorView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				// 网络检测
				if (!NetworkUtils.isNetworkAvailable(mActivity)) {
					ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}
				showLoadingView();
				keywordPageNum = 1;
				SearchManager.getInstance().getKwData(mActivity, handler, getAlbumId(), keywordPageNum);
			}
		});
	}

	/**
	 * ======================================注册广播==============================
	 */

	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String url = null;
				String percent, localPath;
				View taskListItem;
				AppListViewHolder viewHolder;
				int silentInstallResult;
				switch (type) {
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.CANINSTALL, mActivity, null);
					}
					break;

				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.NORMAL);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.NORMAL, mActivity, null);
					}
					break;

				case MyIntents.Types.COMPLETE_INSTALL:
					LogUtil.getLogger().d("handler Intent:COMPLETE_INSTALL");
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANLAUNCH);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.CANLAUNCH, mActivity, null);
					}
					mActivity.removeStickyBroadcast(new Intent(MyIntents.INTENT_UPDATE_UI));
					break;

				case MyIntents.Types.WAIT:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.WAITING);
						taskListItem = mListView.findViewWithTag(url);
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
						notifyAppDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);
					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = mListView.findViewWithTag(url);
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
						taskListItem = mListView.findViewWithTag(url);
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
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.appPauseView.setClickable(false);
					}
					break;
				case MyIntents.Types.INSTALLING:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url)) {

						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshInstalling();
					}
					break;
				case MyIntents.Types.INSTALL_RESULT:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					silentInstallResult = intent.getIntExtra(MyIntents.INSTALL_RESULT_CODE, 0);

					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url) && silentInstallResult != SilentUtil.INSTALL_SUCCEEDED) {

						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.appPauseView.setClickable(false);

						viewHolder.refreshInstall();
						DownloadEntityManager.getInstance().updateDownloadInstallStatus(url);
						ToastUtil.show(mActivity, R.string.toast_silent_install_failed, Toast.LENGTH_LONG);
						PackageUtil.normalInstall(mActivity, localPath);
					}
					break;
				}
			}
		}
	}

	private void notifyAppDataChange(String url, int status) {
		List<AppInfoBean> list;
		if (mListView.getAdapter() == null) {
			return;
		}
		list = ((AppListAdapter) ((HeaderViewListAdapter) mListView.getAdapter()).getWrappedAdapter()).getList();
		for (AppInfoBean appInfoBean : list) {
			if (appInfoBean.getUrl() != null && appInfoBean.getUrl().equals(url)) {
				appInfoBean.setStatus(status);
				break;
			}
		}
	}

	private void notifyPatchUpgrade() {
		List<AppInfoBean> list;
		if (mListView.getAdapter() == null) {
			return;
		}
		list = ((AppListAdapter) ((HeaderViewListAdapter) mListView.getAdapter()).getWrappedAdapter()).getList();
		if (list == null)
			return;
		HashMap<String, InstallAppBean> updateAppMap = UpdateManage.getInstance(mActivity).findAllUpdateInstallAppMap();
		for (AppInfoBean appInfoBean : list) {
			String packageName = appInfoBean.getPackageName();
			InstallAppBean updateAppBean = updateAppMap.get(packageName);
			if (updateAppBean != null && updateAppBean.getIsPatch()) {
				appInfoBean.setPatch(true);
				appInfoBean.setUrl(updateAppBean.getUrl());
				appInfoBean.setPatchUrl(updateAppBean.getUrlPatch());
				appInfoBean.setPatchFileSize(updateAppBean.getPatchSize());

				// 增量更新 刷新显示增量大小
				View taskListItem = mListView.findViewWithTag(appInfoBean.getUrl());
				AppListViewHolder viewHolder = new AppListViewHolder(taskListItem);
				if (viewHolder != null) {
					viewHolder.refreshPatchUpdate(appInfoBean);
				}
			}
		}
	}

	private void registUiReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);
		inited = true;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isVisibleToUser) {
			SearchManager.getInstance().startShakeDetector(mActivity, handler);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		this.isVisibleToUser = isVisibleToUser;
		if (mActivity != null && isVisibleToUser) {
			SearchManager.getInstance().startShakeDetector(mActivity, handler);
		}
		if (autoBreakListView != null) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.SEARCH_APP, isVisibleToUser);
		}

	}

	private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// appList = new ArrayList<AppInfoBean>();
			// pageNum = 1;
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			++listPageNum;
			getListData();
		}
	};

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setViewBackground(mActivity, errorView, SkinConstan.SEARCH_REFRESH_BTN_BG);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
