/**   
 * @Title: CategoryHotFragment.java
 * @Package com.mas.amineappstore.activity
 * @Description: TODO 
 
 * @date 2014-2-14 上午10:30:24
 * @version V1.0   
 */

package com.x.ui.activity.home;

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
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.CategoryDetailRequest;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.TabResponse;
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
import com.x.publics.utils.SettingsUtils;
import com.x.publics.utils.SilentUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.AppListAdapter;
import com.x.ui.adapter.AppListViewHolder;
import com.x.ui.adapter.gridview.AppGridListAdapter;
import com.x.ui.adapter.gridview.AppGridViewHolder;
import com.x.ui.adapter.gridview.base.GridItemClickListener;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;

/**
 * @ClassName: CategoryHotFragment
 * @Description: TODO
 
 * @date 2014-2-14 上午10:30:24
 * 
 */

public class CategoryHotFragment extends BaseFragment {

	private AppListAdapter appListAdapter;
	private AppGridListAdapter appGridListAdapter;
	private DownloadUiReceiver mDownloadUiReceiver;
	private ArrayList<AppInfoBean> appList = new ArrayList<AppInfoBean>();
	private PullToRefreshListView pulltoRefreshLv;
	private ListView appRecommenedLv;
	private int rowNum = 1;
	private boolean inited = false;
	private boolean isFicheMode = false;
	private boolean lastMode = false;

	private CategoryDetailRequest request;
	private Pager pager;
	private View loadingView;
	private View errorView;
	private int pageNum = 1;
	private int categoryId, ct;
	private View loadingPb, loadingLogo;

	public static Fragment newInstance(Bundle bundle) {
		CategoryHotFragment fragment = new CategoryHotFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		categoryId = getArguments().getInt("categoryId");
		ct = getArguments().getInt("ct");
		lastMode = SettingsUtils.getValue(mActivity, SettingsUtils.IS_FICHE_MODE, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 注册广播
		if (!inited)
			registUiReceiver();

		// 初始化显示模式
		initDisplayMode();

		// 加载数据
		if (appList.isEmpty()) {
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
		rootView = inflater.inflate(R.layout.fragment_home_recommend, container, false);
		errorView = inflater.inflate(R.layout.error, null);

		pulltoRefreshLv = (PullToRefreshListView) rootView.findViewById(R.id.fhr_recommend_app_lv);
		appRecommenedLv = pulltoRefreshLv.getRefreshableView();

		pulltoRefreshLv.setOnRefreshListener(onRefreshListener);
		pulltoRefreshLv.setMode(Mode.DISABLED);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshLv.setEmptyView(loadingView);
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mActivity);
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

	private void getData(int page) {
		request = new CategoryDetailRequest();
		pager = new Pager(page);
		request.setPager(pager);
		request.setCategoryId(categoryId);
		request.setType(getType());
		DataFetcher.getInstance().getCategoryDetailData(request, myResponseListent, myErrorListener, true);
	}

	public String getType() {
		return Constan.Category.CATEGORY_HOT;
	}

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			pulltoRefreshLv.onRefreshComplete();
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
			pulltoRefreshLv.onRefreshComplete();
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

	private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			appList = new ArrayList<AppInfoBean>();
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
		if (appList.isEmpty()) {
			if (isFicheMode) {
				appGridListAdapter.setList(appList);
			} else {
				appListAdapter.setList(appList);
			}
			loadingView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(errorView);
			pulltoRefreshLv.setMode(Mode.DISABLED);
			errorView.findViewById(R.id.e_retry_btn).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
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
				AppListViewHolder viewHolder;
				AppGridViewHolder appGridViewHolder;
				switch (type) {

				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = appRecommenedLv.findViewWithTag(url);
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
						notifyAppDataChange(url, AppInfoBean.Status.NORMAL);
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshDownloadStatus(AppInfoBean.Status.NORMAL, mActivity, null);
						} else {
							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshDownloadStatus(AppInfoBean.Status.NORMAL, mActivity, null);
						}
					}
					break;

				case MyIntents.Types.COMPLETE_INSTALL:
					LogUtil.getLogger().d("handler Intent:COMPLETE_INSTALL");
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANLAUNCH);
						taskListItem = appRecommenedLv.findViewWithTag(url);
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
						notifyAppDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = appRecommenedLv.findViewWithTag(url);
						if (isFicheMode) {
							appGridViewHolder = new AppGridViewHolder(taskListItem);
							appGridViewHolder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);
						} else {
							viewHolder = new AppListViewHolder(taskListItem);
							viewHolder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);
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
			appGridListAdapter = new AppGridListAdapter(mActivity, ct);
			appGridListAdapter.setNumColumns(rowNum);
			appGridListAdapter.setOnGridClickListener(new GridItemClickListener() {
				@Override
				public void onGridItemClicked(View v, int position, long itemId) {

					if (!NetworkUtils.isNetworkAvailable(mActivity)) {
						ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return;
					}
					if (appList.size() > 0) {
						AppInfoBean appInfoBean = appList.get(position);
						Intent intent = new Intent(mActivity, AppDetailActivity.class);
						intent.putExtra("appInfoBean", appInfoBean);
						intent.putExtra("ct", ct);
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
			appListAdapter = new AppListAdapter(mActivity, ct);
			appRecommenedLv.setAdapter(appListAdapter);
			appListAdapter.setList(appList);
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
