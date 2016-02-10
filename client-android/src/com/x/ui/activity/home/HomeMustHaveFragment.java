/**   
* @Title: HomeMustHaveFragment.java
* @Package com.x.ui.activity.home
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-1 下午5:55:49
* @version V1.0   
*/

package com.x.ui.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
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
import com.x.publics.http.model.HomeMustHaveRequest;
import com.x.publics.http.model.HomeMustHaveResponse;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.HomeMustHaveRequest.HomeMustHaveRequestData;
import com.x.publics.http.model.HomeMustHaveResponse.MustHaveCategoryList;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.MustHaveAppInfoBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SilentUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.receiver.ConnectChangeReceiver;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.MustHaveAppExpandListAdapter;
import com.x.ui.adapter.MustHaveAppListViewHeaderHolder;
import com.x.ui.adapter.MustHaveAppListViewHolder;
import com.x.ui.view.floatsticklist.FloatingGroupExpandableListView;
import com.x.ui.view.floatsticklist.WrapperExpandableListAdapter;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshExpandableStickyListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;
import com.x.ui.view.stickylistheaders.ExpandableStickyListHeadersListView;

/**
* @ClassName: HomeMustHaveFragment
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-1 下午5:55:49
* 
*/

public class HomeMustHaveFragment extends BaseFragment {

	private DownloadUiReceiver mDownloadUiReceiver;
	private ValidWifiReceiver mValidWifiReceiver;
	private boolean mIsVisibleToUser;

	private MustHaveAppExpandListAdapter appListAdapter;
	private ArrayList<MustHaveCategoryList> appList = new ArrayList<MustHaveCategoryList>();

	private PullToRefreshExpandableStickyListView pulltoRefreshLv;
	private FloatingGroupExpandableListView appMustHaveLv;
	private boolean inited = false;

	private HomeMustHaveRequest request;
	private Pager pager;
	private View loadingView;
	private View errorView;
	private int pageNum = 1;
	private int lastCountryId;
	private int currentCountryId;
	private View loadingPb, loadingLogo;
	/**列表开合动画相关*/
	WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();

	public static Fragment newInstance(Bundle bundle) {
		HomeMustHaveFragment fragment = new HomeMustHaveFragment();
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
			registReceiver();
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
			appList = new ArrayList<MustHaveCategoryList>();
			pulltoRefreshLv.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(loadingView);
			appListAdapter.setList(appList); // 清空adapter，防止显示缓存数据
			getData(1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = null;
		rootView = inflater.inflate(R.layout.fragment_home_must_have, container, false);
		errorView = rootView.findViewById(R.id.e_error_rl);

		pulltoRefreshLv = (PullToRefreshExpandableStickyListView) rootView.findViewById(R.id.fhmh_must_have_app_lv);
		appMustHaveLv = pulltoRefreshLv.getRefreshableView();
		appMustHaveLv.setGroupIndicator(null);
		appMustHaveLv.setOnGroupClickListener(onGroupClickListener);
		pulltoRefreshLv.setOnRefreshListener(onRefreshListener);
		pulltoRefreshLv.setMode(Mode.DISABLED);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshLv.setEmptyView(loadingView);
		SoundPullEventListener<FloatingGroupExpandableListView> soundListener = new SoundPullEventListener<FloatingGroupExpandableListView>(
				mActivity);
		soundListener.addSoundEvent(State.REFRESH_TO_RESET, R.raw.refresh);
		pulltoRefreshLv.setOnPullEventListener(soundListener);
		appListAdapter = new MustHaveAppExpandListAdapter(mActivity, getCt());
		appMustHaveLv.setAdapter(appListAdapter);
		appListAdapter.setList(appList);

		// 记录最后一次国家ID
		lastCountryId = CountryManager.getInstance().getCountryId(mActivity);
		return rootView;
	}

	private OnGroupClickListener onGroupClickListener = new OnGroupClickListener() {

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
			MustHaveCategoryList mustHaveCategoryList = (MustHaveCategoryList) appListAdapter.getGroup(groupPosition);
			MustHaveAppListViewHeaderHolder appListViewHeaderHolder = (MustHaveAppListViewHeaderHolder) v.getTag();
			appListViewHeaderHolder.setAppGroupllVisviable(parent.isGroupExpanded(groupPosition), mustHaveCategoryList.getId());
			return false;
		}
	};

	private void getData(int page) {
		request = new HomeMustHaveRequest();
		pager = new Pager(page);
		pager.setPs(6);
		request.setPage(pager);
		request.setData(new HomeMustHaveRequestData());
		DataFetcher.getInstance().getHomeMustHaveData(request, myResponseListent, myErrorListener, true);
	}

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString());
			HomeMustHaveResponse homeMustHaveResponse = (HomeMustHaveResponse) JsonUtil.jsonToBean(response,
					HomeMustHaveResponse.class);
			if (homeMustHaveResponse != null && homeMustHaveResponse.state.code == 200) {
				if (!homeMustHaveResponse.mustHavelist.isEmpty()) {
					Utils.executeAsyncTask(new makeResponseData(), homeMustHaveResponse);
				} else {
					refreshComplete();
					if (appList.isEmpty()) {
						showErrorView();
					} else if (homeMustHaveResponse.isLast) {
						cancleGridViewScorllable();
					}
				}

			} else {
				refreshComplete();
				showErrorView();
			}
		}
	};

	private ErrorListener myErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			refreshComplete();
			showErrorView();
			error.printStackTrace();
		}
	};

	private void refreshComplete() {
		pulltoRefreshLv.onRefreshComplete();
	}

	 

	private class makeResponseData extends AsyncTask<HomeMustHaveResponse, Void, HomeMustHaveResponse> {

		private ArrayList<MustHaveCategoryList> mustHaveTempList;

		@Override
		protected void onPreExecute() {
			mustHaveTempList = new ArrayList<MustHaveCategoryList>();
			super.onPreExecute();
		}

		@Override
		protected HomeMustHaveResponse doInBackground(HomeMustHaveResponse... params) {
			HomeMustHaveResponse homeMustHaveResponse = params[0];
			HashMap<String, InstallAppBean> localAppMap = LocalAppEntityManager.getInstance().getAllAppsMap();
			HashMap<String, DownloadBean> downloadAppMap = DownloadEntityManager.getInstance()
					.getAllDownloadResourceIdMap();

			mustHaveTempList = compareMustHaveList(homeMustHaveResponse.getMustHavelist(), localAppMap, downloadAppMap);
			return homeMustHaveResponse;
		}

		@Override
		protected void onPostExecute(HomeMustHaveResponse homeMustHaveResponse) {
			super.onPostExecute(homeMustHaveResponse);
			refreshComplete();
			appList.addAll(mustHaveTempList);
			appListAdapter.setList(appList);
			if (homeMustHaveResponse.isLast) {
				cancleGridViewScorllable();
			} else {
				pulltoRefreshLv.setMode(Mode.PULL_FROM_END);
			}
		}

	}

	private void showErrorView() {
		if (pageNum > 1) {
			--pageNum;
		}
		if (appList.isEmpty()) {
			appListAdapter.setList(appList);
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

	private OnRefreshListener2<FloatingGroupExpandableListView> onRefreshListener = new OnRefreshListener2<FloatingGroupExpandableListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<FloatingGroupExpandableListView> refreshView) {
			pulltoRefreshLv.setMode(Mode.PULL_FROM_END);
			appList = new ArrayList<MustHaveCategoryList>();
			appListAdapter.cleanCategoryIdSet();
			pageNum = 1;
			getData(1);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<FloatingGroupExpandableListView> refreshView) {
			getData(++pageNum);
		}

	};

	private void registReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);

		mValidWifiReceiver = new ValidWifiReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(ConnectChangeReceiver.NETWORK_CONNECTED_ACTION);
		BroadcastManager.registerReceiver(mValidWifiReceiver, filter2);
		inited = true;
	}

	private void unregisterReceiver() {
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
		BroadcastManager.unregisterReceiver(mValidWifiReceiver);
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
				MustHaveAppListViewHolder viewHolder;
				switch (type) {
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new MustHaveAppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.CANINSTALL, mActivity, null);
					}
					break;
				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						int status = getAppstatus(url);
						notifyAppDataChange(url, status);
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new MustHaveAppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(status, mActivity, null);
					}
					break;
				case MyIntents.Types.COMPLETE_INSTALL:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANLAUNCH);
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new MustHaveAppListViewHolder(taskListItem);
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
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new MustHaveAppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.WAITING, mActivity, percent);
					}
					break;
				case MyIntents.Types.PREDOWNLOAD:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CONNECTING);
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;

						viewHolder = new MustHaveAppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.CONNECTING, mActivity, percent);

					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new MustHaveAppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.PAUSED, mActivity, percent);
					}
					break;
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new MustHaveAppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);
					}
					break;
				case MyIntents.Types.MAKE_UPGRADE:
					notifyPatchUpgrade();
					break;
				case MyIntents.Types.MERGE_PATCH:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new MustHaveAppListViewHolder(taskListItem);
						viewHolder.refreshMerge(mActivity);
					}
					break;
				case MyIntents.Types.INSTALLING:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url)) {
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new MustHaveAppListViewHolder(taskListItem);
						viewHolder.refreshInstalling(mActivity);
					}
					break;
				case MyIntents.Types.INSTALL_RESULT:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					silentInstallResult = intent.getIntExtra(MyIntents.INSTALL_RESULT_CODE, 0);

					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url) && silentInstallResult != SilentUtil.INSTALL_SUCCEEDED) {
						taskListItem = appMustHaveLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new MustHaveAppListViewHolder(taskListItem);
						viewHolder.refreshInstall(mActivity);
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
			List<MustHaveCategoryList> list;
			if (appMustHaveLv.getAdapter() == null) {
				return;
			}
			list = appListAdapter.getList();
			if (list == null)
				return;
			HashMap<String, InstallAppBean> updateAppMap = UpdateManage.getInstance(mActivity)
					.findAllUpdateInstallAppMap();
			for (MustHaveCategoryList mustHaveCategoryList : list) {
				try {
					for (AppInfoBean appInfoBean : mustHaveCategoryList.getApplist()) {
						String packageName = appInfoBean.getPackageName();
						InstallAppBean updateAppBean = updateAppMap.get(packageName);
						if (updateAppBean != null && updateAppBean.getIsPatch()) {
							appInfoBean.setPatch(true);
							appInfoBean.setUrl(updateAppBean.getUrl());
							appInfoBean.setPatchUrl(updateAppBean.getUrlPatch());
							appInfoBean.setPatchFileSize(updateAppBean.getPatchSize());

							// 增量更新 刷新显示增量大小
							View taskListItem = appMustHaveLv.findViewWithTag(appInfoBean.getUrl());
							if (taskListItem == null)
								return;
							MustHaveAppListViewHolder viewHolder = new MustHaveAppListViewHolder(taskListItem);
							if (viewHolder != null) {
								viewHolder.refreshPatchUpdate(appInfoBean);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

		}

		private void notifyAppDataChange(String url, int status) {
			List<MustHaveCategoryList> list;
			if (appMustHaveLv.getAdapter() == null) {
				return;
			}
			list = appListAdapter.getList();
			for (MustHaveCategoryList mustHaveCategoryList : list) {
				try {
					for (AppInfoBean appInfoBean : mustHaveCategoryList.getApplist()) {
						if (appInfoBean.getUrl() != null && appInfoBean.getUrl().equals(url)) {
							appInfoBean.setStatus(status);
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		private int getAppstatus(String url) {
			List<MustHaveCategoryList> list;
			int status = AppInfoBean.Status.NORMAL;
			if (appMustHaveLv.getAdapter() == null) {
				return status;
			}
			list = appListAdapter.getList();
			for (MustHaveCategoryList mustHaveCategoryList : list) {
				try {
					for (AppInfoBean appInfoBean : mustHaveCategoryList.getApplist()) {
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
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			return status;
		}

		private void notifyUninstall(String packageName) {
			ArrayList<MustHaveCategoryList> list;
			if (appMustHaveLv.getAdapter() == null) {
				return;
			}
			list = appListAdapter.getList();
			for (MustHaveCategoryList mustHaveCategoryList : list) {
				try {
					for (AppInfoBean appInfoBean : mustHaveCategoryList.getApplist()) {
						if (appInfoBean.getPackageName() != null && appInfoBean.getPackageName().equals(packageName)) {
							appInfoBean.setStatus(AppInfoBean.Status.NORMAL);
							appInfoBean.setPatch(false);
							appInfoBean.setPatchUrl("");
							appInfoBean.setPatchFileSize(0l);
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			appListAdapter.setList(list);
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
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
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

		if (getCt() == Constan.Ct.HOME_MUST_HAVE) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.HOME_MUST_HAVE, isVisibleToUser);
			if (isVisibleToUser) {
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.HOME_MUST_HAVE, 0, null, 0L, null, null,
						false);
			}
		}
	}

	public int getCt() {
		return Constan.Ct.HOME_MUST_HAVE;
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
