/**   
* @Title: MyAppsNewFragment.java
* @Package com.x.ui.activity.myApps
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-7 上午11:23:04
* @version V1.0   
*/

package com.x.ui.activity.myApps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.x.business.favorite.FavoriteManage;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.model.AppsUpgradeResponse;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.FavoriteAppBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.MyAppsBean;
import com.x.publics.model.MyAppsBean.ListType;
import com.x.publics.utils.CloneClass;
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
import com.x.publics.utils.Constan.MediaType;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.MyAppsNewListAdapter;
import com.x.ui.adapter.MyAppsNewViewHolder;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView.OnActionClickListener;

/**
* @ClassName: MyAppsNewFragment
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-7-7 上午11:23:04
* 
*/

public class MyAppsNewFragment extends BaseFragment {

	private View loadingPb, loadingLogo;
	private ActionSlideExpandableListView mAppGv;
	private MyAppsNewListAdapter myAppsNewListAdapter;
	private List<MyAppsBean> myAppsBeanList = new ArrayList<MyAppsBean>();
	private List<MyAppsBean> AppsBeanTempList;
	private boolean inited = false;
	private BroadcastReceiver mDownloadUiReceiver;

	private List<InstallAppBean> updateList;
	private List<DownloadBean> downloadingList;
	private List<DownloadBean> unInstallList;

	private View emptyView;
	private View loadingView;

	public static Fragment newInstance(Bundle bundle) {
		MyAppsNewFragment fragment = new MyAppsNewFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_app_new, null);
		emptyView = rootView.findViewById(R.id.empty_rl);
		loadingView = rootView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		mAppGv = (ActionSlideExpandableListView) rootView.findViewById(R.id.fman_apps_lv);
		myAppsNewListAdapter = new MyAppsNewListAdapter(mActivity);
		mAppGv.setItemActionListener(actionClickListener, R.id.man_update_detail_tv, R.id.man_uninstall_app_tv,
				R.id.man_download_detail_tv, R.id.man_download_delete_tv, R.id.man_update_pre_detail_tv,
				R.id.man_open_pre_app_tv);
		//		mAppGv.setRecyclerListener(recyclerListener);
		initAppsNewdata();
		return rootView;
	}

	private void initAppsNewdata() {
		showLoadingView();
		boolean canSendReuest = UpdateManage.getInstance(mActivity).canSendUpdateRequest(mActivity);
		if (canSendReuest) {
			UpdateManage.getInstance(mActivity).getAppsUpdate(myUpgradeResponseListent, myUpgradeErrorListener);
		} else {
			Utils.executeAsyncTask(new getAppUpdateTask());
		}
	}

	private class getAppUpdateTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			getMyAppNewData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			uiHandler.sendEmptyMessage(0);
		}
	}

	private Listener<JSONObject> myUpgradeResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			LogUtil.getLogger().d("getAppsUpdate response==>" + response.toString());
			AppsUpgradeResponse upgradeResponse = (AppsUpgradeResponse) JsonUtil.jsonToBean(response,
					AppsUpgradeResponse.class);
			if (upgradeResponse != null && upgradeResponse.state.code == 200 && upgradeResponse.applist != null) {
				UpdateManage.getInstance(mActivity).setLastUpdateRequestSuccessTime(mActivity);
				UpdateManage.getInstance(mActivity).setLastUpdateRequestSuccess();
				UpdateManage.getInstance(mActivity).deleteAllUpdateApp();
				for (InstallAppBean installAppBean : upgradeResponse.applist) {
					UpdateManage.getInstance(mActivity).addUpdateApp(installAppBean);
				}
				// 获取升级数据，判断是否自动下载更新
				if (Utils.getSettingModel(mActivity).isAutoDownloadUpdateInWifi()
						&& NetworkUtils.getNetworkInfo(mActivity).equals(NetworkUtils.NETWORK_TYPE_WIFI))
					UpdateManage.getInstance(mActivity).autoDownloadUpdateAppControl(true);
			} else {
				UpdateManage.getInstance(mActivity).setLastUpdateRequestFail();
			}
			Utils.executeAsyncTask(new getAppUpdateTask());
		}
	};

	private ErrorListener myUpgradeErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			UpdateManage.getInstance(mActivity).setLastUpdateRequestFail();
			Utils.executeAsyncTask(new getAppUpdateTask());
		}
	};

	private void showLoadingView() {
		mAppGv.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
	}

	private void getMyAppNewData() {
		AppsBeanTempList = new ArrayList<MyAppsBean>();
		downloadingList = DownloadEntityManager.getInstance().getAllUnFinishedAppsDownloadList();
		updateList = UpdateManage.getInstance(mActivity).findAllUpdateInstallApp();

		HashMap<String, InstallAppBean> appMap = LocalAppEntityManager.getInstance().getAllAppsMap();
		HashMap<String, FavoriteAppBean> favoriteMap = FavoriteManage.getInstance(mActivity).getAllFavoriteAppBeanMap();

		if (downloadingList != null) {
			for (DownloadBean downloadBean : downloadingList) {
				MyAppsBean myAppsBean = new MyAppsBean();
				try {
					myAppsBean = (MyAppsBean) CloneClass.clone(downloadBean, myAppsBean);

					FavoriteAppBean favoriteAppBean = favoriteMap.get(myAppsBean.getPackageName());
					if (favoriteAppBean != null) {//同步是否收藏
						myAppsBean.setFavoriteId(favoriteAppBean.getFavoriteId());
					}

					InstallAppBean updateBean = UpdateManage.getInstance(mActivity).getUpdateAppBean(
							downloadBean.getPackageName(), downloadBean.getVersionCode());

					InstallAppBean localBean = null;
					if (updateBean != null && (localBean = appMap.get(updateBean.getPackageName())) != null) { //同步升级数据
						String oldVersionName = localBean.getVersionName();
						myAppsBean.setOldVersionName(oldVersionName);
						updateBean.setSysFlag(localBean.getSysFlag());
						myAppsBean = (MyAppsBean) CloneClass.clone(updateBean, myAppsBean);

						myAppsBean.setAppName(localBean.getAppName()); //显示手机上应用名称
						for (InstallAppBean updateBean2 : updateList) {
							if (updateBean2.getPackageName().equals(updateBean.getPackageName())) {
								updateList.remove(updateBean2);
								break;
							}
						}
					} else {
						myAppsBean.setAppName(downloadBean.getName());
					}
					myAppsBean.setLogo(downloadBean.getIconUrl());
					myAppsBean.setApkId(downloadBean.getResourceId());
					myAppsBean.setStatus(Utils.convertStatus(downloadBean.getStatus()));

					myAppsBean.setCurrentBytes(downloadBean.getCurrentBytes());
					myAppsBean.setTotalBytes(downloadBean.getTotalBytes());
					myAppsBean.setSpeed(downloadBean.getSpeed());
					myAppsBean.setStatus(Utils.convertStatus(downloadBean.getStatus()));
					myAppsBean.setListType(ListType.DOWNLOAD);
					if (TextUtils.isEmpty(myAppsBean.getOriginalUrl())) {//修复1.1升到1.2后originalUrl的初始值为空的bug
						myAppsBean.setOriginalUrl(myAppsBean.getUrl());
					}
					AppsBeanTempList.add(myAppsBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		unInstallList = DownloadEntityManager.getInstance().getAllUnInstallAppsDownloadList();
		if (unInstallList != null) {
			for (DownloadBean downloadBean : unInstallList) {
				MyAppsBean myAppsBean = new MyAppsBean();
				try {
					myAppsBean = (MyAppsBean) CloneClass.clone(downloadBean, myAppsBean);

					FavoriteAppBean favoriteAppBean = favoriteMap.get(myAppsBean.getPackageName());
					if (favoriteAppBean != null) {//同步是否收藏
						myAppsBean.setFavoriteId(favoriteAppBean.getFavoriteId());
					}

					InstallAppBean updateBean = UpdateManage.getInstance(mActivity).getUpdateAppBean(
							downloadBean.getPackageName(), downloadBean.getVersionCode());
					InstallAppBean localBean = null;

					if (updateBean != null && (localBean = appMap.get(updateBean.getPackageName())) != null) {
						String oldVersionName = localBean.getVersionName();
						myAppsBean.setOldVersionName(oldVersionName);
						updateBean.setSysFlag(localBean.getSysFlag());
						myAppsBean = (MyAppsBean) CloneClass.clone(updateBean, myAppsBean);

						myAppsBean.setAppName(localBean.getAppName()); //显示手机上应用名称
						for (InstallAppBean updateBean2 : updateList) {
							if (updateBean2.getPackageName().equals(updateBean.getPackageName())
									&& updateBean2.getVersionCode() == updateBean.getVersionCode()) {
								updateList.remove(updateBean2);
								break;
							}
						}
						if (myAppsBean.getIsPatch()) {
							if (!UpdateManage.getInstance(mActivity).isNewApkFileExit(myAppsBean.getPackageName(),
									myAppsBean.getVersionName())
									&& UpdateManage.getInstance(mActivity).isNewFileExit(myAppsBean.getPackageName(),
											myAppsBean.getVersionName())) { //当且仅当合成的差量包不存在，全量包存在时，认为是差量升级失败后自动下载的全量升级
								myAppsBean.setIsPatch(false);
							}
						}
					} else {
						myAppsBean.setAppName(downloadBean.getName());
					}
					myAppsBean.setLogo(downloadBean.getIconUrl());
					myAppsBean.setApkId(downloadBean.getResourceId());
					myAppsBean.setStatus(Utils.convertStatus(downloadBean.getStatus()));
					myAppsBean.setListType(ListType.UNINSTALL);
					if (TextUtils.isEmpty(myAppsBean.getOriginalUrl())) {//修复1.1升到1.2后originalUrl的初始值为空的bug
						myAppsBean.setOriginalUrl(myAppsBean.getUrl());
					}
					AppsBeanTempList.add(myAppsBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (updateList != null) {
			for (InstallAppBean updateBean : updateList) {
				try {
					updateBean.setStatus(AppInfoBean.Status.CANUPGRADE);
					InstallAppBean localBean = appMap.get(updateBean.getPackageName());
					if (localBean != null) {
						String oldVersionName = localBean.getVersionName();
						updateBean.setOldVersionName(oldVersionName);
						updateBean.setAppName(localBean.getAppName());
						updateBean.setSysFlag(localBean.getSysFlag());
					}

					MyAppsBean myAppsBean = new MyAppsBean();

					myAppsBean = (MyAppsBean) CloneClass.clone(updateBean, myAppsBean);

					FavoriteAppBean favoriteAppBean = favoriteMap.get(myAppsBean.getPackageName());
					if (favoriteAppBean != null) {//同步是否收藏
						myAppsBean.setFavoriteId(favoriteAppBean.getFavoriteId());
					}
					myAppsBean.setOriginalUrl(updateBean.getUrl());
					myAppsBean.setListType(ListType.UPDATE);
					AppsBeanTempList.add(myAppsBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		Collections.sort(AppsBeanTempList);

	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			// 木有数据
			case 0:
				if (AppsBeanTempList != null && !AppsBeanTempList.isEmpty()) {
					showListView();
				}
				break;

			// 隐藏updateAll
			case 1:
				//				hideUpdateAllView();
				break;
			}
		};
	};

	private void showListView() {
		mAppGv.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		myAppsBeanList.clear();
		myAppsBeanList.addAll(AppsBeanTempList);
		myAppsNewListAdapter.setList(myAppsBeanList);
		mAppGv.setAdapter(myAppsNewListAdapter, R.id.man_top_rl, R.id.man_expand_ll, itemExpandCollapseListener);
	}

	/**
	 * 点击箭头处理
	 */
	private OnItemExpandCollapseListener itemExpandCollapseListener = new OnItemExpandCollapseListener() {

		@Override
		public void onExpand(View itemView, View parentView, int position) {
			String url = (String) parentView.getTag();
			View taskListItem = mAppGv.findViewWithTag(url);
			MyAppsNewViewHolder viewHolder = new MyAppsNewViewHolder(taskListItem);
			if (viewHolder != null && viewHolder.arrowIv != null)
				viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_up);
		}

		@Override
		public void onCollapse(View itemView, View parentView, int position) {
			String url = (String) parentView.getTag();
			View taskListItem = mAppGv.findViewWithTag(url);
			MyAppsNewViewHolder viewHolder = new MyAppsNewViewHolder(taskListItem);
			if (viewHolder != null && viewHolder.arrowIv != null)
				viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
		}
	};

	/**
	* 弹出项点击处理
	*/
	private OnActionClickListener actionClickListener = new OnActionClickListener() {

		@Override
		public void onClick(View itemView, View clickedView, int position) {
			MyAppsBean appsBean = (MyAppsBean) myAppsNewListAdapter.getList().get(position);
			switch (clickedView.getId()) {
			case R.id.man_download_detail_tv:
			case R.id.man_update_detail_tv:
			case R.id.man_update_pre_detail_tv:
				if (!NetworkUtils.isNetworkAvailable(mActivity)) {
					ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}
				AppInfoBean appInfoBean = Utils.getAppInfoBean(appsBean);
				Intent intent = new Intent(mActivity, AppDetailActivity.class);
				intent.putExtra("appInfoBean", appInfoBean);
				intent.putExtra("ct", Constan.Ct.APP_UPDATE_MANAGEMENT); // 应用更新
				dataeyeSource(appsBean); // ct值
				mActivity.startActivity(intent);
				break;

			case R.id.man_uninstall_app_tv:
				PackageUtil.unstallApk(mActivity, appsBean.getPackageName());
				break;
			case R.id.man_open_pre_app_tv:
				Utils.launchAnotherApp(mActivity, appsBean.getPackageName());
				break;
			case R.id.man_download_delete_tv:
				DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
						"" + appsBean.getApkId(), "" + appsBean.getVersionCode());
				if (downloadBean != null)
					delelteDownloadTask(downloadBean.getUrl());
				break;
			}
		}
	};

	public void dataeyeSource(MyAppsBean appsBean) {
		if (appsBean != null) {
			String newVersion = UpdateManage.getInstance(MyAppsNewFragment.this.getActivity()).getNewVersion(
					appsBean.getPackageName());
			if (!TextUtils.isEmpty(newVersion)) {
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.UPGRADE, appsBean.getFileType(),
						appsBean.getAppName(), appsBean.getFileSize(), appsBean.getVersionName(), null, false);
			} else if (appsBean.getFavoriteId() > 0) {
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.FAVORITE, appsBean.getFileType(),
						appsBean.getAppName(), appsBean.getFileSize(), appsBean.getVersionName(), null, false);
			}
		}

	}

	private void delelteDownloadTask(final String url) {
		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(mActivity, url);
				mAppGv.collapse(); //add by ZhouHua
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(mActivity, ResourceUtil.getString(mActivity, R.string.warm_tips),
				ResourceUtil.getString(mActivity, R.string.dialog_delete_download_prompt_one, "1"),
				ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
				ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
	}

	/**
	* 图片资源回收
	*/
	private RecyclerListener recyclerListener = new RecyclerListener() {

		@Override
		public void onMovedToScrapHeap(View view) {
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		if (!inited)
			registDownloadUiReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregistDownloadUiReceiver();
	}

	private void registDownloadUiReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);
		inited = true;
	}

	private void unregistDownloadUiReceiver() {
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
	}

	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String originalUrl = null;
				String packageName;
				String localPath;
				int silentInstallResult;
				int versionCode;
				View taskListItem;
				DownloadBean downloadBean;
				MyAppsNewViewHolder viewHolder;
				switch (type) {
				case MyIntents.Types.COMPLETE_INSTALL:
					packageName = intent.getStringExtra(MyIntents.PACKAGENAME);
					versionCode = intent.getIntExtra(MyIntents.VERSION_CODE, 0);
					if (!TextUtils.isEmpty(packageName) && versionCode != 0) {
						notifyInstallComplete(packageName, versionCode);
					}
					break;
				case MyIntents.Types.COMPLETE_UNIINSTALL:
					packageName = intent.getStringExtra(MyIntents.PACKAGENAME);
					if (!TextUtils.isEmpty(packageName)) {
						notifyUnInstallComplete(packageName);
					}
					break;
				case MyIntents.Types.COMPLETE:
					originalUrl = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(originalUrl)) {
						notifyDownloadComplete(originalUrl);
					}
					break;
				case MyIntents.Types.DELETE:
					originalUrl = intent.getStringExtra(MyIntents.URL);
					packageName = intent.getStringExtra(MyIntents.PACKAGENAME);
					versionCode = intent.getIntExtra(MyIntents.VERSION_CODE, 0);
					if (!TextUtils.isEmpty(originalUrl) && !TextUtils.isEmpty(packageName) && versionCode != 0) {
						notifyDownloadDelete(originalUrl, packageName, versionCode);
					}
					break;
				case MyIntents.Types.ADD:
					originalUrl = intent.getStringExtra(MyIntents.URL);
					packageName = intent.getStringExtra(MyIntents.PACKAGENAME);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(originalUrl) && packageName != null && downloadBean != null) {
						notifyAddDownload(originalUrl, packageName, downloadBean);
					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.WAIT:
				case MyIntents.Types.PREDOWNLOAD:
				case MyIntents.Types.PAUSE:
					myAppsNewListAdapter.updateCategoryNumStatusText();
				case MyIntents.Types.PROCESS:
					originalUrl = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(originalUrl) && downloadBean != null) {
						if (downloadBean.getStatus() == DownloadTask.TASK_FINISH) {
							return;
						}
						MyAppsBean appsBean = notifyAppDataChange(originalUrl, downloadBean);
						taskListItem = mAppGv.findViewWithTag(originalUrl);
						if (taskListItem != null) {
							viewHolder = new MyAppsNewViewHolder(taskListItem);
							viewHolder.refreshAppStatus(appsBean, mActivity);
							viewHolder.refreshDownloadPercent(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
									intent.getStringExtra(MyIntents.PROCESS_SPEED),
									Integer.valueOf(intent.getStringExtra(MyIntents.PROCESS_PROGRESS)));
						}
					}
					break;
				case MyIntents.Types.ADD_FAVORITE:
					packageName = intent.getStringExtra(MyIntents.PACKAGENAME);
					if (!TextUtils.isEmpty(packageName)) {
						notifyFavoriteAdd(packageName);
					}
					break;
				case MyIntents.Types.DELETE_FAVORITE:
					packageName = intent.getStringExtra(MyIntents.PACKAGENAME);
					if (!TextUtils.isEmpty(packageName)) {
						notifyFavoriteDelete(packageName);
					}
					break;
				case MyIntents.Types.MERGE_PATCH:
					originalUrl = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(originalUrl)) {
						taskListItem = mAppGv.findViewWithTag(originalUrl);
						if (taskListItem == null)
							return;
						viewHolder = new MyAppsNewViewHolder(taskListItem);
						viewHolder.refreshMerge(mActivity);
					}
					break;
				case MyIntents.Types.INSTALLING:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					if (!TextUtils.isEmpty(localPath)) {
						originalUrl = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(originalUrl)) {
						notifySlientInstalling(originalUrl);
					}
					break;
				case MyIntents.Types.INSTALL_RESULT:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					silentInstallResult = intent.getIntExtra(MyIntents.INSTALL_RESULT_CODE, 0);

					if (!TextUtils.isEmpty(localPath)) {
						originalUrl = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(originalUrl) && silentInstallResult != SilentUtil.INSTALL_SUCCEEDED) {
						notifySlientInstallFailed(originalUrl);
						DownloadEntityManager.getInstance().updateDownloadInstallStatus(originalUrl);
						ToastUtil.show(mActivity, R.string.toast_silent_install_failed, Toast.LENGTH_LONG);
						PackageUtil.normalInstall(mActivity, localPath);
					}
					break;
				default:
					break;
				}

			}
		}

		private MyAppsBean notifyFavoriteAdd(String packageName) {
			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getPackageName() != null && bean.getPackageName().equals(packageName)) {

						FavoriteAppBean favoriteAppBean = FavoriteManage.getInstance(mActivity)
								.getFavoriteAppBeanByPackageName(packageName);
						try {
							bean.setFavoriteId(favoriteAppBean.getFavoriteId());
						} catch (Exception e) {
							e.printStackTrace();
						}
						Collections.sort(myAppsBeanList);
						myAppsNewListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifyFavoriteDelete(String packageName) {
			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getPackageName() != null && bean.getPackageName().equals(packageName)) {
						bean.setFavoriteId(0);
						Collections.sort(myAppsBeanList);
						myAppsNewListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifyInstallComplete(String packageName, int versionCode) {
			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getPackageName() != null
							&& bean.getPackageName().equals(packageName)
							&& bean.getVersionCode() == versionCode
							&& (bean.getStatus() == AppInfoBean.Status.CANINSTALL || bean.getStatus() == AppInfoBean.Status.INSTALLING)) {
						list.remove(bean);
						myAppsBeanList = list;
						Collections.sort(myAppsBeanList);
						myAppsNewListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifyUnInstallComplete(String packageName) {
			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getPackageName() != null && bean.getPackageName().equals(packageName)
							&& bean.getStatus() == AppInfoBean.Status.CANUPGRADE) {
						if (mAppGv != null)
							mAppGv.collapse();
						list.remove(bean);
						myAppsBeanList = list;
						Collections.sort(myAppsBeanList);
						myAppsNewListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifySlientInstalling(String url) {
			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {
						bean.setStatus(AppInfoBean.Status.INSTALLING);
						myAppsBeanList = list;
						Collections.sort(myAppsBeanList);
						myAppsNewListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifySlientInstallFailed(String url) {
			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {
						bean.setStatus(AppInfoBean.Status.CANINSTALL);
						myAppsBeanList = list;
						Collections.sort(myAppsBeanList);
						myAppsNewListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		/**
		 * @Title: notifyDownloadComplete
		 * @Description: 更新数据，防止listview滑动数据不对
		 * @param @param url
		 * @param @return
		 * @return InstallAppBean
		 * @throws
		 */

		private MyAppsBean notifyDownloadComplete(String url) {
			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {

						list.remove(bean);
						removeOtherUninstallItem(list, bean);//一个应用有多个升级版本时，在ready to install里面应该只能有一个当前最新的版本
						bean.setCurrentBytes(bean.getFileSize());
						bean.setStatus(AppInfoBean.Status.CANINSTALL);
						bean.setListType(ListType.UNINSTALL);
						if (bean.getIsPatch()) {
							if (!UpdateManage.getInstance(mActivity).isNewApkFileExit(bean.getPackageName(),
									bean.getVersionName())
									&& UpdateManage.getInstance(mActivity).isNewFileExit(bean.getPackageName(),
											bean.getVersionName())) { //当且仅当合成的差量包不存在，全量包存在时，认为是差量升级失败后自动下载的全量升级
								bean.setIsPatch(false);
							}
						}
						list.add(bean);

						myAppsBeanList = list;
						Collections.sort(myAppsBeanList);
						myAppsNewListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private void removeOtherUninstallItem(List<MyAppsBean> list, MyAppsBean appsBean) {
			String packageName = appsBean.getPackageName();
			DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByPkgName(
					appsBean.getPackageName(), "" + appsBean.getVersionCode());
			if (TextUtils.isEmpty(packageName) || downloadBean == null)
				return;
			for (MyAppsBean myAppsBean : list) {
				if (myAppsBean.getPackageName().equals(downloadBean.getPackageName())
						&& myAppsBean.getVersionCode() != downloadBean.getVersionCode()
						&& myAppsBean.getStatus() == AppInfoBean.Status.CANINSTALL) {
					list.remove(myAppsBean);
					break;
				}
			}

		}

		private MyAppsBean notifyAddDownload(String url, String packageName, DownloadBean downloadBean) {
			if (downloadBean.getMediaType() != null
					&& (downloadBean.getMediaType().equals(MediaType.IMAGE) || downloadBean.getMediaType().equals(
							MediaType.MUSIC)))
				return null;

			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean appsBean = list.get(i);
					if (appsBean.getOriginalUrl() != null && appsBean.getOriginalUrl().equals(url)) {

						InstallAppBean updateBean = UpdateManage.getInstance(mActivity).getUpdateAppBean(packageName,
								downloadBean.getVersionCode());

						FavoriteAppBean favoriteAppBean = FavoriteManage.getInstance(mActivity)
								.getFavoriteAppBeanByPackageName(packageName);

						if (updateBean != null && appsBean.getStatus() == AppInfoBean.Status.CANUPGRADE) { //点击Update的更新 
							list.remove(appsBean);
							appsBean.setCurrentBytes(0);
							appsBean.setStatus(AppInfoBean.Status.WAITING);
							appsBean.setListType(ListType.DOWNLOAD);
							list.add(appsBean);
							if (mAppGv != null)
								mAppGv.collapse();
						} else if (favoriteAppBean != null && appsBean.getStatus() == AppInfoBean.Status.PAUSED) { //wifi切换自动下载收藏
							appsBean.setStatus(AppInfoBean.Status.WAITING);
							appsBean.setListType(ListType.DOWNLOAD);
						}

						myAppsBeanList = list;
						Collections.sort(myAppsBeanList);
						myAppsNewListAdapter.notifyDataSetChanged();
						return appsBean;
					}
				}
				//不在list中的下载，从All中的收藏点击下载
				MyAppsBean appsBean = new MyAppsBean();
				try {

					FavoriteAppBean favoriteAppBean = FavoriteManage.getInstance(mActivity)
							.getFavoriteAppBeanByPackageName(packageName);
					if (favoriteAppBean != null) {
						appsBean = (MyAppsBean) CloneClass.clone(favoriteAppBean, appsBean);
					}

					appsBean = (MyAppsBean) CloneClass.clone(downloadBean, appsBean);
					appsBean.setListType(ListType.DOWNLOAD);
					appsBean.setLogo(downloadBean.getIconUrl());
					appsBean.setApkId(downloadBean.getResourceId());
					appsBean.setStatus(Utils.convertStatus(downloadBean.getStatus()));
					appsBean.setAppName(downloadBean.getName());

					list.add(appsBean);
					myAppsBeanList = list;
					Collections.sort(myAppsBeanList);
					myAppsNewListAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return appsBean;
			}
			return null;
		}

		/**
		 * @Title: notifyDownloadDelete
		 * @Description: 更新数据，防止listview滑动数据不对
		 * @param @param url
		 * @param @return
		 * @return InstallAppBean
		 * @throws
		 */

		private MyAppsBean notifyDownloadDelete(String url, String packageName, int versionCode) {
			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {

						InstallAppBean updateBean = UpdateManage.getInstance(mActivity).getUpdateAppBean(packageName,
								versionCode);

						if (updateBean != null) {
							list.remove(bean);
							bean.setCurrentBytes(0);
							bean.setIsPatch(updateBean.getIsPatch());
							bean.setStatus(AppInfoBean.Status.CANUPGRADE);
							bean.setListType(ListType.UPDATE);
							list.add(bean);
						} else {
							list.remove(bean);
						}

						myAppsBeanList = list;
						Collections.sort(myAppsBeanList);
						myAppsNewListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		/**
		 * @Title: notifyAppDataChange
		 * @Description: 更新数据，防止listview滑动数据不对
		 * @param @param url
		 * @param @param installAppBean
		 * @return void
		 * @throws
		 */

		private MyAppsBean notifyAppDataChange(String url, DownloadBean downloadBean) {
			if (downloadBean == null || url == null)
				return null;
			List<MyAppsBean> list = myAppsNewListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {
						bean.setStatus(Utils.convertStatus(downloadBean.getStatus()));
						bean.setCurrentBytes(downloadBean.getCurrentBytes());
						bean.setTotalBytes(downloadBean.getTotalBytes());
						bean.setSpeed(downloadBean.getSpeed());
						return bean;
					}
				}
			}
			return null;
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (mAppGv != null) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.MYAPPS_NEW, isVisibleToUser);
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
