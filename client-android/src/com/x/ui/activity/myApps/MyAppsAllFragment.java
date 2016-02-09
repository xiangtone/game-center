/**   
* @Title: MyAppsAllFragment.java
* @Package com.mas.amineappstore.ui.activity.myApps
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-7 上午11:23:19
* @version V1.0   
*/

package com.x.ui.activity.myApps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.x.R;
import com.x.business.favorite.FavoriteManage;
import com.x.business.localapp.sort.PinYin;
import com.x.business.localapp.sort.PinyinComparator2;
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
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.FavoriteAppBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.MyAppsBean;
import com.x.publics.model.MyAppsBean.ListType;
import com.x.publics.utils.CloneClass;
import com.x.publics.utils.Constan;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SilentUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.MyAppsAllListAdapter;
import com.x.ui.adapter.MyAppsAllViewHolder;
import com.x.ui.adapter.MyAppsNewViewHolder;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView.OnActionClickListener;

/**
* @ClassName: MyAppsAllFragment
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-7-7 上午11:23:19
* 
*/

public class MyAppsAllFragment extends BaseFragment {

	private View loadingPb, loadingLogo;
	private ActionSlideExpandableListView mAppGv;
	private MyAppsAllListAdapter myAppsAllListAdapter;
	private List<MyAppsBean> myAppsBeanList = new ArrayList<MyAppsBean>();
	private List<MyAppsBean> AppsBeanTempList;

	private boolean inited = false;
	private BroadcastReceiver mDownloadUiReceiver;

	private PinyinComparator2 pinyinComparator;

	private List<InstallAppBean> localList;
	private List<InstallAppBean> updateList;
	private List<DownloadBean> downloadList;
	private List<FavoriteAppBean> favoriteList;

	private boolean isVisibleToUser;

	private View emptyView;
	private View loadingView;

	public static Fragment newInstance(Bundle bundle) {
		MyAppsAllFragment fragment = new MyAppsAllFragment();
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
		myAppsAllListAdapter = new MyAppsAllListAdapter(mActivity);
		mAppGv.setItemActionListener(actionClickListener, R.id.maa_update_detail_tv, R.id.maa_uninstall_app_tv,
				R.id.maa_download_detail_tv, R.id.maa_download_delete_tv, R.id.maa_local_manager_tv,
				R.id.maa_local_delete_tv, R.id.maa_zapp_app_detail_tv, R.id.maa_zapp_app_delete_tv,
				R.id.maa_update_pre_detail_tv, R.id.maa_open_pre_app_tv);
		initAppsAlldata();
		return rootView;
	}

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

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		this.isVisibleToUser = isVisibleToUser;
		if (mAppGv != null) {
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.MYAPPS_ALL, isVisibleToUser);
		}
	}

	private void initAppsAlldata() {
		showLoadingView();
		Utils.executeAsyncTask(new getAllAppTask());
	}

	private class getAllAppTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			getMyAllAppData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			uiHandler.sendEmptyMessage(0);
		}
	}

	private void getMyAllAppData() {
		AppsBeanTempList = new ArrayList<MyAppsBean>();
		HashMap<String, InstallAppBean> appMap = LocalAppEntityManager.getInstance().getAllAppsMap();
		HashMap<String, FavoriteAppBean> favoriteMap = FavoriteManage.getInstance(mActivity).getAllFavoriteAppBeanMap();

		localList = LocalAppEntityManager.getInstance().getAllLocalApps();
		updateList = UpdateManage.getInstance(mActivity).findAllUpdateInstallApp();
		favoriteList = FavoriteManage.getInstance(mActivity).getAllFavoriteAppBeanList();
		downloadList = DownloadEntityManager.getInstance().getAllAppDownload();

		if (localList != null) {
			for (InstallAppBean installAppBean : localList) {
				MyAppsBean myAppsBean = new MyAppsBean();
				FavoriteAppBean favoriteAppBean = null;
				try {
					installAppBean.setStatus(AppInfoBean.Status.CANLAUNCH);
					InstallAppBean updateBean = UpdateManage.getInstance(mActivity).getUpdateAppBeanByPackageName(
							installAppBean.getPackageName());
					InstallAppBean localBean = null;
					if (updateBean != null && (localBean = appMap.get(updateBean.getPackageName())) != null) {
						String oldVersionName = localBean.getVersionName();

						installAppBean = (InstallAppBean) CloneClass.clone(updateBean, installAppBean);

						installAppBean.setOldVersionName(oldVersionName);

						installAppBean.setStatus(AppInfoBean.Status.CANUPGRADE);

						installAppBean.setAppName(localBean.getAppName());
						installAppBean.setSysFlag(localBean.getSysFlag());
						for (InstallAppBean updateBean2 : updateList) {
							if (updateBean2.getPackageName().equals(updateBean.getPackageName())) {
								updateList.remove(updateBean2);
								break;
							}
						}
						myAppsBean.setOriginalUrl(installAppBean.getUrl());
					}
					myAppsBean = (MyAppsBean) CloneClass.clone(installAppBean, myAppsBean);

					favoriteAppBean = favoriteMap.get(myAppsBean.getPackageName());

					if (favoriteAppBean != null) { //同步是否收藏
						myAppsBean.setFavoriteId(favoriteAppBean.getFavoriteId());
						for (FavoriteAppBean favoriteAppBean2 : favoriteList) {
							if (favoriteAppBean2.getFavoriteId() == favoriteAppBean.getFavoriteId()) {
								favoriteList.remove(favoriteAppBean2);
								break;
							}
						}
					}

					DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByPkgName(
							myAppsBean.getPackageName(), "" + myAppsBean.getVersionCode());// 如果是更新应用下载历史版本是1.2，本地是1.1，服务器是1.3

					if (downloadBean != null) {
						myAppsBean = (MyAppsBean) CloneClass.clone(downloadBean, myAppsBean);
						myAppsBean.setStatus(Utils.convertStatus(downloadBean.getStatus()));
						myAppsBean.setFileSize(updateBean.getFileSize());

						if (Utils.convertStatus(downloadBean.getStatus()) == AppInfoBean.Status.CANINSTALL) {
							if (myAppsBean.getIsPatch()) {
								if (!UpdateManage.getInstance(mActivity).isNewApkFileExit(myAppsBean.getPackageName(),
										myAppsBean.getVersionName())
										&& UpdateManage.getInstance(mActivity).isNewFileExit(
												myAppsBean.getPackageName(), myAppsBean.getVersionName())) { //当且仅当合成的差量包不存在，全量包存在时，认为是差量升级失败后自动下载的全量升级
									myAppsBean.setIsPatch(false);
								}
							}
						}

						for (DownloadBean bean : downloadList) {
							if (bean.getPackageName().equals(myAppsBean.getPackageName())
									&& bean.getVersionCode() == myAppsBean.getVersionCode()) {
								downloadList.remove(bean);
								break;
							}
						}
						if (TextUtils.isEmpty(myAppsBean.getOriginalUrl())) {//修复1.1升到1.2后originalUrl的初始值为空的bug
							myAppsBean.setOriginalUrl(myAppsBean.getUrl());
						}
					}

					AppsBeanTempList.add(myAppsBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (updateList != null) {//包含预装应用的情况
			for (InstallAppBean updateBean : updateList) {
				MyAppsBean myAppsBean = new MyAppsBean();
				try {

					InstallAppBean localBean = LocalAppEntityManager.getInstance().getLocalAppByPackageName(
							updateBean.getPackageName());
					if (localBean != null) {
						String oldVersionName = localBean.getVersionName();

						updateBean.setOldVersionName(oldVersionName);
						updateBean.setSysFlag(localBean.getSysFlag());
					}
					updateBean.setStatus(AppInfoBean.Status.CANUPGRADE);
					updateBean.setAppName(localBean.getAppName());

					localBean = (InstallAppBean) CloneClass.clone(updateBean, localBean);

					myAppsBean.setOriginalUrl(updateBean.getUrl());

					myAppsBean = (MyAppsBean) CloneClass.clone(localBean, myAppsBean);

					FavoriteAppBean favoriteAppBean = favoriteMap.get(myAppsBean.getPackageName());

					if (favoriteAppBean != null) { //同步是否收藏
						myAppsBean.setFavoriteId(favoriteAppBean.getFavoriteId());

						for (FavoriteAppBean favoriteAppBean2 : favoriteList) {
							if (favoriteAppBean2.getFavoriteId() == favoriteAppBean.getFavoriteId()) {
								favoriteList.remove(favoriteAppBean2);
								break;
							}
						}
					}

					DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByPkgName(
							myAppsBean.getPackageName(), "" + myAppsBean.getVersionCode());

					if (downloadBean != null) { // 应用下载历史版本是1.2，本地是1.1，服务器是1.3
						myAppsBean = (MyAppsBean) CloneClass.clone(downloadBean, myAppsBean);
						myAppsBean.setStatus(Utils.convertStatus(downloadBean.getStatus()));

						for (DownloadBean bean : downloadList) {
							if (bean.getPackageName().equals(myAppsBean.getPackageName())
									&& bean.getVersionCode() == myAppsBean.getVersionCode()) {
								downloadList.remove(bean);
								break;
							}
						}
						if (TextUtils.isEmpty(myAppsBean.getOriginalUrl())) {//修复1.1升到1.2后originalUrl的初始值为空的bug
							myAppsBean.setOriginalUrl(myAppsBean.getUrl());
						}
					}

					AppsBeanTempList.add(myAppsBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (downloadList != null) {
			for (DownloadBean downloadBean : downloadList) {
				MyAppsBean myAppsBean = new MyAppsBean();
				try {
					myAppsBean = (MyAppsBean) CloneClass.clone(downloadBean, myAppsBean);

					FavoriteAppBean favoriteAppBean = favoriteMap.get(myAppsBean.getPackageName());

					if (favoriteAppBean != null) { //同步是否收藏
						myAppsBean.setFavoriteId(favoriteAppBean.getFavoriteId());

						for (FavoriteAppBean favoriteAppBean2 : favoriteList) {
							if (favoriteAppBean2.getFavoriteId() == favoriteAppBean.getFavoriteId()) {
								favoriteList.remove(favoriteAppBean2);
								break;
							}
						}
					}
					myAppsBean.setAppName(downloadBean.getName());
					myAppsBean.setSortLetters(PinYin.getSortLetter(myAppsBean.getAppName()));
					myAppsBean.setLogo(downloadBean.getIconUrl());
					myAppsBean.setApkId(downloadBean.getResourceId());
					myAppsBean.setStatus(Utils.convertStatus(downloadBean.getStatus()));
					if (TextUtils.isEmpty(myAppsBean.getOriginalUrl())) {//修复1.1升到1.2后originalUrl的初始值为空的bug
						myAppsBean.setOriginalUrl(myAppsBean.getUrl());
					}
					AppsBeanTempList.add(myAppsBean);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		if (favoriteList != null) { //剩下的收藏
			for (FavoriteAppBean favoriteAppBean : favoriteList) {

				MyAppsBean myAppsBean = new MyAppsBean();
				try {

					myAppsBean = Utils.getMyAppBean(favoriteAppBean, myAppsBean);
					myAppsBean = (MyAppsBean) CloneClass.clone(favoriteAppBean, myAppsBean);

					myAppsBean.setSortLetters(PinYin.getSortLetter(myAppsBean.getAppName()));
					myAppsBean.setStatus(AppInfoBean.Status.NORMAL);
					AppsBeanTempList.add(myAppsBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		sort();
	}

	private void sort() {
		//实例化汉字转拼音类
		pinyinComparator = new PinyinComparator2();
		// 根据a-z进行排序源数据
		Collections.sort(AppsBeanTempList, pinyinComparator);
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (AppsBeanTempList != null && !AppsBeanTempList.isEmpty()) {
					showListView();
				} else {
					showEmptyView();
				}
				break;
			default:
				break;
			}
		};
	};

	private void showLoadingView() {
		mAppGv.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
	}

	private void showListView() {
		mAppGv.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		myAppsBeanList.clear();
		myAppsBeanList.addAll(AppsBeanTempList);
		myAppsAllListAdapter.setList(myAppsBeanList);
		mAppGv.setAdapter(myAppsAllListAdapter, R.id.maa_top_rl, R.id.maa_expand_ll, itemExpandCollapseListener);
	}

	private void showEmptyView() {
		mAppGv.setVisibility(View.GONE);
		emptyView.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.GONE);
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
				MyAppsAllViewHolder viewHolder;
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
						notifyUnintallComplete(packageName);
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
				case MyIntents.Types.COMPLETE:
					originalUrl = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(originalUrl)) {
						notifyDownloadComplete(originalUrl);
					}
					break;
				case MyIntents.Types.WAIT:
				case MyIntents.Types.PREDOWNLOAD:
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
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
							viewHolder = new MyAppsAllViewHolder(taskListItem);
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
						viewHolder = new MyAppsAllViewHolder(taskListItem);
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

		private MyAppsBean notifySlientInstalling(String url) {
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {
						bean.setStatus(AppInfoBean.Status.INSTALLING);
						myAppsBeanList = list;
						Collections.sort(myAppsBeanList, pinyinComparator);
						myAppsAllListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifySlientInstallFailed(String url) {
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {
						bean.setStatus(AppInfoBean.Status.CANINSTALL);
						myAppsBeanList = list;
						Collections.sort(myAppsBeanList, pinyinComparator);
						myAppsAllListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifyFavoriteDelete(String packageName) {
			if (mAppGv != null)
				mAppGv.collapse();
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getPackageName() != null && bean.getPackageName().equals(packageName)) {

						if (bean.getStatus() == AppInfoBean.Status.NORMAL) {
							list.remove(bean);
						} else {
							bean.setFavoriteId(0);
						}
						myAppsBeanList = list;
						Collections.sort(myAppsBeanList, pinyinComparator);
						myAppsAllListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifyFavoriteAdd(String packageName) {
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
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
						myAppsBeanList = list;
						Collections.sort(myAppsBeanList, pinyinComparator);
						myAppsAllListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifyInstallComplete(String packageName, int versionCode) {
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getPackageName() != null
							&& bean.getPackageName().equals(packageName)
							&& bean.getVersionCode() == versionCode
							&& (bean.getStatus() == AppInfoBean.Status.CANINSTALL || bean.getStatus() == AppInfoBean.Status.INSTALLING)) {
						InstallAppBean localBean = LocalAppEntityManager.getInstance().getLocalAppByPackageName(
								packageName);
						if (localBean != null) {
							bean.setAppName(localBean.getAppName());
							bean.setSortLetters(PinYin.getSortLetter(localBean.getAppName()));
						}

						bean.setStatus(AppInfoBean.Status.CANLAUNCH);

						myAppsBeanList = list;
						Collections.sort(myAppsBeanList, pinyinComparator);
						myAppsAllListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
			}
			return null;
		}

		private MyAppsBean notifyUnintallComplete(String packageName) {
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getPackageName() != null && bean.getPackageName().equals(packageName)) {

						FavoriteAppBean favoriteAppBean = FavoriteManage.getInstance(mActivity)
								.getFavoriteAppBeanByPackageName(packageName);

						if (favoriteAppBean != null && bean.getStatus() != AppInfoBean.Status.CANINSTALL) { //本地应用卸载 同时是收藏 排除更新前卸载
							try {
								bean = Utils.getMyAppBean(favoriteAppBean, bean);
								bean = (MyAppsBean) CloneClass.clone(favoriteAppBean, bean);
								bean.setSortLetters(PinYin.getSortLetter(bean.getAppName()));
								bean.setStatus(AppInfoBean.Status.NORMAL);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (bean.getStatus() == AppInfoBean.Status.CANINSTALL) { //更新前卸载
							// do nothing
							return bean;
						} else if (bean.getStatus() == AppInfoBean.Status.CANLAUNCH
								|| bean.getStatus() == AppInfoBean.Status.CANUPGRADE) { // 本地应用卸载
							list.remove(bean);
						}
						if (mAppGv != null)
							mAppGv.collapse();

						myAppsBeanList = list;
						Collections.sort(myAppsBeanList, pinyinComparator);
						myAppsAllListAdapter.notifyDataSetChanged();
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
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {
						bean.setCurrentBytes(bean.getFileSize());
						bean.setStatus(AppInfoBean.Status.CANINSTALL);
						removeOtherUninstallItem(list, bean);//一个应用有多个升级版本时，在ready to install里面应该只能有一个当前最新的版本
						if (bean.getIsPatch()) {
							if (!UpdateManage.getInstance(mActivity).isNewApkFileExit(bean.getPackageName(),
									bean.getVersionName())
									&& UpdateManage.getInstance(mActivity).isNewFileExit(bean.getPackageName(),
											bean.getVersionName())) { //当且仅当合成的差量包不存在，全量包存在时，认为是差量升级失败后自动下载的全量升级
								bean.setIsPatch(false);
							}
						}

						myAppsBeanList = list;
						Collections.sort(myAppsBeanList, pinyinComparator);
						myAppsAllListAdapter.notifyDataSetChanged();
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

		private MyAppsBean notifyAddDownload(String url, String packageName) {
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {

						if (UpdateManage.getInstance(mActivity).getNewVersion(packageName) != null) {
							myAppsBeanList.remove(bean);
							bean.setCurrentBytes(0);
							bean.setStatus(AppInfoBean.Status.WAITING);
							bean.setListType(ListType.DOWNLOAD);
							myAppsBeanList.add(bean);
							if (mAppGv != null)
								mAppGv.collapse();
						} else {
							myAppsBeanList.add(bean);
						}
						Collections.sort(myAppsBeanList, pinyinComparator);
						myAppsAllListAdapter.notifyDataSetChanged();
						return bean;
					}
				}
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
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					MyAppsBean bean = list.get(i);
					if (bean.getOriginalUrl() != null && bean.getOriginalUrl().equals(url)) {
						InstallAppBean updateBean = UpdateManage.getInstance(mActivity).getUpdateAppBean(packageName,
								versionCode);
						if (updateBean != null) {
							bean.setCurrentBytes(0);
							bean.setIsPatch(updateBean.getIsPatch());
							bean.setStatus(AppInfoBean.Status.CANUPGRADE);
						} else {
							FavoriteAppBean favoriteAppBean = FavoriteManage.getInstance(mActivity)
									.getFavoriteAppBeanByPackageName(packageName);
							if (favoriteAppBean != null) {
								bean.setCurrentBytes(0);
								bean.setStatus(AppInfoBean.Status.NORMAL);
							} else { //删除下载 
								list.remove(bean);
							}

						}

						myAppsBeanList = list;
						Collections.sort(myAppsBeanList, pinyinComparator);
						myAppsAllListAdapter.notifyDataSetChanged();
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
			List<MyAppsBean> list = myAppsAllListAdapter.getList();
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
			MyAppsBean appsBean = (MyAppsBean) myAppsAllListAdapter.getList().get(position);
			if (appsBean == null)
				return;
			switch (clickedView.getId()) {
			case R.id.maa_zapp_app_detail_tv:
			case R.id.maa_download_detail_tv:
			case R.id.maa_update_detail_tv:
			case R.id.maa_update_pre_detail_tv:
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

			case R.id.maa_uninstall_app_tv:
				PackageUtil.unstallApk(mActivity, appsBean.getPackageName());
				break;
			case R.id.maa_download_delete_tv:
				DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
						"" + appsBean.getApkId(), "" + appsBean.getVersionCode());
				if (downloadBean != null)
					delelteDownloadTask(downloadBean.getUrl());
				break;
			case R.id.maa_zapp_app_delete_tv:
				FavoriteManage.getInstance(mActivity).cancelFavoriteApp(appsBean.getApkId());
				break;
			case R.id.maa_local_manager_tv:
				Utils.showInstalledAppDetails(mActivity, appsBean.getPackageName());
				break;
			case R.id.maa_local_delete_tv:
				PackageUtil.unstallApk(mActivity, appsBean.getPackageName());
				break;
			case R.id.maa_open_pre_app_tv:
				Utils.launchAnotherApp(mActivity, appsBean.getPackageName());
				break;
			}
		}
	};

	public void dataeyeSource(MyAppsBean appsBean) {
		if (appsBean != null) {
			String newVersion = UpdateManage.getInstance(MyAppsAllFragment.this.getActivity()).getNewVersion(
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
