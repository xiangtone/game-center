/**   
* @Title: HomeCollectionDetailActivity.java
* @Package com.mas.amineappstore.ui.activity.home
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-2 下午2:45:56
* @version V1.0   
*/

package com.x.ui.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.HomeCollectionDetailRequest;
import com.x.publics.http.model.HomeCollectionDetailResponse;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.HomeCollectionDetailRequest.HomeCollectionDetailRequestData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.AppCollectionBean;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SilentUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.adapter.AppListAdapter;
import com.x.ui.adapter.AppListViewHolder;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

/**
* @ClassName: HomeCollectionDetailActivity
* @Description: 应用专辑详情页

* @date 2014-9-2 下午2:45:56
* 
*/

public class HomeCollectionDetailActivity extends BaseActivity implements View.OnClickListener {
	private Activity mActivity;
	private AppListAdapter appListAdapter;
	private DownloadUiReceiver mDownloadUiReceiver;
	private ArrayList<AppInfoBean> appList = new ArrayList<AppInfoBean>();
	private PullToRefreshListView pulltoRefreshLv;
	private ListView appCollectionDetailLv;
	private boolean inited = false;
	private View loadingView;
	private View errorView;
	private View logoView;
	private ImageView collectionLogoIv;
	private TextView collectionNameTv, collectionBriefTv, collectionDateTv;
	private int pageNum = 1;
	private Pager pager;
	private HomeCollectionDetailRequest request;
	private AppCollectionBean appCollectionBean;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;
	private View loadingPb, loadingLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		setContentView(R.layout.activity_collection_detail);
		initUi();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		if (!inited) {
			registUiReceiver();
		}
		if (appCollectionBean != null) {
			DataEyeManager.getInstance().module(
					StatisticConstan.ModuleName.COLLECTION + "-" + appCollectionBean.getName(), true);
			DataEyeManager.getInstance().source(appCollectionBean.getCollectionId(), 0, null, 0L, null, null, false);
		}
	}

	private void initUi() {
		errorView = findViewById(R.id.e_error_rl);

		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mNavigationView.setOnClickListener(this);

		logoView = this.getLayoutInflater().inflate(R.layout.home_collection_item, null);
		collectionLogoIv = (ImageView) logoView.findViewById(R.id.hci_collection_logo_iv);
		collectionNameTv = (TextView) logoView.findViewById(R.id.hci_collection_name_tv);
		collectionBriefTv = (TextView) logoView.findViewById(R.id.hci_collection_brief_tv);
		collectionDateTv = (TextView) logoView.findViewById(R.id.hci_collection_date_tv);
		collectionBriefTv.setMaxLines(10);

		pulltoRefreshLv = (PullToRefreshListView) findViewById(R.id.acd_collection_app_lv);
		appCollectionDetailLv = pulltoRefreshLv.getRefreshableView();
		appCollectionDetailLv.addHeaderView(logoView);
		pulltoRefreshLv.setOnRefreshListener(onRefreshListener);
		pulltoRefreshLv.setMode(Mode.DISABLED);
		loadingView = this.getLayoutInflater().inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshLv.setEmptyView(loadingView);

		appListAdapter = new AppListAdapter(mActivity, getCt());
		appCollectionDetailLv.setAdapter(appListAdapter);
		appListAdapter.setList(appList);

		Intent intent = getIntent();
		if (intent != null && intent.getSerializableExtra("appCollectionBean") != null) {
			appCollectionBean = (AppCollectionBean) intent.getSerializableExtra("appCollectionBean");
			mTitleTv.setText(appCollectionBean.getName());
		}
		getData(1);
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

	public int getCt() {
		return Constan.Ct.HOME_COLLECTION;
	}

	private void getData(int page) {
		if (appCollectionBean != null) {
			createLogoData();
			request = new HomeCollectionDetailRequest();
			pager = new Pager(page);
			request.setPage(pager);
			request.setData(new HomeCollectionDetailRequestData(appCollectionBean.getCollectionId()));
			DataFetcher.getInstance().getHomeCollectionDetailData(request, myResponseListent, myErrorListener, true);
		} else {
			showErrorView();
		}
	}

	private void createLogoData() {
		NetworkImageUtils.load(mActivity, ImageType.NETWORK, appCollectionBean.getBigicon(),
				R.drawable.banner_default_picture, R.drawable.banner_default_picture, collectionLogoIv);
		collectionNameTv.setText(appCollectionBean.getName());
		collectionBriefTv.setText(appCollectionBean.getDescription());
		collectionDateTv.setText(appCollectionBean.getPublishTime());
	}

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("response==>" + response.toString());
			pulltoRefreshLv.onRefreshComplete();
			HomeCollectionDetailResponse homeCollectionDetailResponse = (HomeCollectionDetailResponse) JsonUtil
					.jsonToBean(response, HomeCollectionDetailResponse.class);
			if (homeCollectionDetailResponse != null && homeCollectionDetailResponse.state.code == 200) {
				if (!homeCollectionDetailResponse.applist.isEmpty()) {
					Utils.executeAsyncTask(new makeResponseData(), homeCollectionDetailResponse);

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

	private class makeResponseData extends AsyncTask<HomeCollectionDetailResponse, Void, HomeCollectionDetailResponse> {

		@Override
		protected HomeCollectionDetailResponse doInBackground(HomeCollectionDetailResponse... params) {
			HomeCollectionDetailResponse homeCollectionDetailResponse = params[0];
			HashMap<String, InstallAppBean> localAppMap = LocalAppEntityManager.getInstance().getAllAppsMap();
			HashMap<String, DownloadBean> downloadAppMap = DownloadEntityManager.getInstance()
					.getAllDownloadResourceIdMap();
			homeCollectionDetailResponse.applist = compareList(homeCollectionDetailResponse.applist, localAppMap,
					downloadAppMap);
			return homeCollectionDetailResponse;
		}

		@Override
		protected void onPostExecute(HomeCollectionDetailResponse homeCollectionDetailResponse) {
			super.onPostExecute(homeCollectionDetailResponse);
			appList.addAll(homeCollectionDetailResponse.applist);
			appListAdapter.setList(appList);

			if (homeCollectionDetailResponse.isLast) {
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
				switch (type) {
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = appCollectionDetailLv.findViewWithTag(url);
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
						taskListItem = appCollectionDetailLv.findViewWithTag(url);
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
						taskListItem = appCollectionDetailLv.findViewWithTag(url);
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
						taskListItem = appCollectionDetailLv.findViewWithTag(url);
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
						taskListItem = appCollectionDetailLv.findViewWithTag(url);
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
						taskListItem = appCollectionDetailLv.findViewWithTag(url);
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
						taskListItem = appCollectionDetailLv.findViewWithTag(url);
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
						taskListItem = appCollectionDetailLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshMerge(mActivity);
					}
					break;
				case MyIntents.Types.INSTALLING:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url)) {
						taskListItem = appCollectionDetailLv.findViewWithTag(url);
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

						taskListItem = appCollectionDetailLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshInstall();
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
			if (appCollectionDetailLv.getAdapter() == null) {
				return;
			}
			list = ((AppListAdapter) ((HeaderViewListAdapter) appCollectionDetailLv.getAdapter()).getWrappedAdapter())
					.getList();
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
					View taskListItem = appCollectionDetailLv.findViewWithTag(appInfoBean.getUrl());
					if (taskListItem == null)
						return;
					AppListViewHolder viewHolder = new AppListViewHolder(taskListItem);
					if (viewHolder != null) {
						viewHolder.refreshPatchUpdate(appInfoBean);
					}
				}
			}
		}

		private void notifyAppDataChange(String url, int status) {
			List<AppInfoBean> list;
			if (appCollectionDetailLv.getAdapter() == null) {
				return;
			}
			list = ((AppListAdapter) ((HeaderViewListAdapter) appCollectionDetailLv.getAdapter()).getWrappedAdapter())
					.getList();
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
			if (appCollectionDetailLv.getAdapter() == null) {
				return status;
			}
			list = ((AppListAdapter) ((HeaderViewListAdapter) appCollectionDetailLv.getAdapter()).getWrappedAdapter())
					.getList();
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
			if (appCollectionDetailLv.getAdapter() == null) {
				return;
			}
			list = ((AppListAdapter) ((HeaderViewListAdapter) appCollectionDetailLv.getAdapter()).getWrappedAdapter())
					.getList();
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
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver();
	}

	private void registUiReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);
		inited = true;
	}

	private void unregisterReceiver() {
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (appCollectionBean != null) {
			DataEyeManager.getInstance().module(
					StatisticConstan.ModuleName.COLLECTION + "-" + appCollectionBean.getName(), false);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(mActivity, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}

}
