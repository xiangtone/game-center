/**   
* @Title: AppsUpgradeFragment.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-1-24 上午10:28:28
* @version V1.0   
*/

package com.x.ui.activity.appman;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.http.model.AppsUpgradeResponse;
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
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.UpdateAppListAdapter;
import com.x.ui.adapter.UpdateAppListViewHolder;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView.OnActionClickListener;

/**
* @ClassName: AppsUpgradeFragment
* @Description: TODO 

* @date 2014-1-24 上午10:28:28
* 
*/

public class AppsUpgradeFragment extends BaseFragment implements OnClickListener {

	private View loadingPb, loadingLogo;
	private ActionSlideExpandableListView mAppGv;
	private UpdateAppListAdapter updateAppListAdapter;
	private BroadcastReceiver mDownloadUiReceiver, mWifiChangeUpdateReceiver;
	private boolean inited = false;
	private List<InstallAppBean> updateList;
	private TextView updateAllTv;
	private View emptyView;
	private View loadingView;
	private View updateView;
	private View errorView;
	private View retryView;

	public static Fragment newInstance(Bundle bundle) {
		AppsUpgradeFragment fragment = new AppsUpgradeFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frament_app_canupgrade, null);
		emptyView = rootView.findViewById(R.id.empty_rl);
		loadingView = rootView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		errorView = rootView.findViewById(R.id.e_error_rl);
		retryView = rootView.findViewById(R.id.e_retry_btn);
		updateView = rootView.findViewById(R.id.fac_update_all_rl);
		updateAllTv = (TextView) rootView.findViewById(R.id.fac_update_all_tv);
		mAppGv = (ActionSlideExpandableListView) rootView.findViewById(R.id.fac_app_upgrade_lv);
		updateAppListAdapter = new UpdateAppListAdapter(mActivity);
		mAppGv.setItemActionListener(actionClickListener, R.id.uail_detail_app_tv, R.id.uail_open_app_tv);
		updateAllTv.setOnClickListener(this);
		retryView.setOnClickListener(this);
		mAppGv.setRecyclerListener(recyclerListener);
		initUpdata();
		return rootView;
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			// 木有数据
			case 0:
				if (updateList != null && !updateList.isEmpty()) {
					showListView();
				} else {
					showEmptyView();
				}
				break;

			// 隐藏updateAll
			case 1:
				hideUpdateAllView();
				break;
			}
		};
	};

	private void hideUpdateAllView() {
		updateView.setVisibility(View.GONE);
	}

	private void showListView() {
		mAppGv.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		updateView.setVisibility(View.VISIBLE);
		updateAllTv.setVisibility(View.VISIBLE);
		errorView.setVisibility(View.GONE);
		updateAppListAdapter.setList(updateList);
		mAppGv.setAdapter(updateAppListAdapter, R.id.uail_top_rl, R.id.uail_expand_ll, itemExpandCollapseListener);
	}

	private void showEmptyView() {
		mAppGv.setVisibility(View.GONE);
		emptyView.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.GONE);
		updateView.setVisibility(View.GONE);
		updateAllTv.setVisibility(View.GONE);
		errorView.setVisibility(View.GONE);
	}

	private void showLoadingView() {
		mAppGv.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		updateView.setVisibility(View.GONE);
		errorView.setVisibility(View.GONE);
	}

	private void showErrorView() {
		mAppGv.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		updateView.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
	}

	/**
	 * @Title: initUpdata
	 * @Description: 获取更新数据
	 * @param
	 * @return void
	 * @throws
	 */

	private void initUpdata() {
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
			updateList = UpdateManage.getInstance(mActivity).findAllUpdateInstallApp();
			HashMap<String, InstallAppBean> appMap = LocalAppEntityManager.getInstance().getAllAppsMap();
			if (updateList != null) {
				for (InstallAppBean updateBean : updateList) {
					try {
						updateBean.setStatus(AppInfoBean.Status.CANUPGRADE);
						int apkId = updateBean.getApkId();
						DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
								"" + apkId, "" + updateBean.getVersionCode());
						if (apkId != 0 && downloadBean != null) {
							updateBean.setStatus(Utils.convertStatus(downloadBean.getStatus()));
							updateBean.setCurrentBytes(downloadBean.getCurrentBytes());
							updateBean.setTotalBytes(downloadBean.getTotalBytes());
							updateBean.setSpeed(downloadBean.getSpeed());
						}
						String oldVersionName = appMap.get(updateBean.getPackageName()).getVersionName();
						updateBean.setOldVersionName(oldVersionName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			uiHandler.sendEmptyMessage(0);
			// 调用方法
			refreshView();
			sendChangeUpdateNumBroadcast();
		}
	}

	private void sendChangeUpdateNumBroadcast() {
		Intent intent = new Intent(MyIntents.INTENT_UPDATE_UI);
		intent.putExtra(MyIntents.TYPE, MyIntents.Types.CHANGE_APP_MANAGEMENT_UPDATE_NUM);
		BroadcastManager.sendBroadcast(intent);
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
			} else {
				UpdateManage.getInstance(mActivity).setLastUpdateRequestFail();
				//				showErrorView();
			}
			Utils.executeAsyncTask(new getAppUpdateTask());
		}
	};

	private ErrorListener myUpgradeErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
			UpdateManage.getInstance(mActivity).setLastUpdateRequestFail();
			//			showErrorView();
			Utils.executeAsyncTask(new getAppUpdateTask());
		}
	};

	/**
	 * 点击箭头处理
	 */
	private OnItemExpandCollapseListener itemExpandCollapseListener = new OnItemExpandCollapseListener() {

		@Override
		public void onExpand(View itemView, View parentView, int position) {
			String url = (String) parentView.getTag();
			View taskListItem = mAppGv.findViewWithTag(url);
			UpdateAppListViewHolder viewHolder = new UpdateAppListViewHolder(taskListItem);
			if (viewHolder != null && viewHolder.arrowIv != null)
				viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_up);
		}

		@Override
		public void onCollapse(View itemView, View parentView, int position) {
			String url = (String) parentView.getTag();
			View taskListItem = mAppGv.findViewWithTag(url);
			UpdateAppListViewHolder viewHolder = new UpdateAppListViewHolder(taskListItem);
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
			InstallAppBean installBean = (InstallAppBean) updateAppListAdapter.getList().get(position);
			switch (clickedView.getId()) {
			case R.id.uail_detail_app_tv:
				if (!NetworkUtils.isNetworkAvailable(mActivity)) {
					ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}
				AppInfoBean appInfoBean = Utils.getAppInfoBean(installBean);
				Intent intent = new Intent(mActivity, AppDetailActivity.class);
				intent.putExtra("appInfoBean", appInfoBean);
				intent.putExtra("ct", Constan.Ct.APP_UPDATE_MANAGEMENT); // 应用更新
																			// ct值
				mActivity.startActivity(intent);
				break;

			case R.id.uail_open_app_tv:
				Utils.launchAnotherApp(mActivity, installBean.getPackageName());
				break;
			}
		}
	};

	/**
	 * 图片资源回收
	 */
	private RecyclerListener recyclerListener = new RecyclerListener() {

		@Override
		public void onMovedToScrapHeap(View view) {
			ImageView iv = (ImageView) view.findViewById(R.id.uail_app_icon_iv);
			iv.setImageBitmap(null);
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

		mWifiChangeUpdateReceiver = new WifiChangeUpdateReceiver();
		IntentFilter wifiChangeUpdateReceiverfilter = new IntentFilter();
		wifiChangeUpdateReceiverfilter.addAction(MyIntents.INTENT_UPDATE_CHANGE_WIFI_ACTION);
		BroadcastManager.registerReceiver(mWifiChangeUpdateReceiver, wifiChangeUpdateReceiverfilter);

		inited = true;
	}

	private void unregistDownloadUiReceiver() {
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
		BroadcastManager.unregisterReceiver(mWifiChangeUpdateReceiver);
	}

	public class WifiChangeUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_CHANGE_WIFI_ACTION)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				switch (type) {
				case MyIntents.Types.CHANGE_HOMEPAGE_UPDATE_NUM:
					initUpdata();
					break;
				default:
					break;
				}

			}
		}
	}

	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String url;
				String packageName;
				View taskListItem;
				DownloadBean downloadBean;
				UpdateAppListViewHolder viewHolder;
				switch (type) {
				case MyIntents.Types.COMPLETE_UNIINSTALL:
					//				case MyIntents.Types.COMPLETE_INSTALL:
					url = intent.getStringExtra(MyIntents.URL);
					packageName = intent.getStringExtra(MyIntents.PACKAGENAME);
					if (!TextUtils.isEmpty(url) || !TextUtils.isEmpty(packageName)) {
						if (mAppGv != null)
							mAppGv.collapse();
						initUpdata();
					}
					break;
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						taskListItem = mAppGv.findViewWithTag(url);
						InstallAppBean installAppBean = notifyDownloadComplete(url);
						if (taskListItem != null && installAppBean != null) {
							viewHolder = new UpdateAppListViewHolder(taskListItem);
							viewHolder.refreshAppStatus(installAppBean, mActivity);
						}
						// 调用方法
						refreshView();
					}

					break;
				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						InstallAppBean installAppBean = notifyDownloadDelete(url);
						taskListItem = mAppGv.findViewWithTag(url);
						if (taskListItem != null && installAppBean != null) {
							viewHolder = new UpdateAppListViewHolder(taskListItem);
							viewHolder.refreshAppStatus(installAppBean, mActivity);
						}
					}
					break;
				case MyIntents.Types.WAIT:
				case MyIntents.Types.PREDOWNLOAD:
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(url) && downloadBean != null) {
						InstallAppBean installAppBean = notifyAppDataChange(url, downloadBean);
						taskListItem = mAppGv.findViewWithTag(url);
						if (taskListItem != null) {
							viewHolder = new UpdateAppListViewHolder(taskListItem);
							viewHolder.refreshAppStatus(installAppBean, mActivity);
							viewHolder.refreshDownloadPercent(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
									intent.getStringExtra(MyIntents.PROCESS_SPEED),
									Integer.valueOf(intent.getStringExtra(MyIntents.PROCESS_PROGRESS)));
						}
					}
					break;
				default:
					break;
				}

			}
		}

		/**
		 * @Title: notifyDownloadComplete
		 * @Description: 更新数据，防止listview滑动数据不对
		 * @param @param url
		 * @param @return
		 * @return InstallAppBean
		 * @throws
		 */

		private InstallAppBean notifyDownloadComplete(String url) {
			List<InstallAppBean> list = updateAppListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					InstallAppBean bean = list.get(i);
					if (bean.getUrl() != null && bean.getUrl().equals(url)) {
						bean.setStatus(AppInfoBean.Status.CANINSTALL);
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

		private InstallAppBean notifyDownloadDelete(String url) {
			List<InstallAppBean> list = updateAppListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					InstallAppBean bean = list.get(i);
					if (bean.getUrl() != null && bean.getUrl().equals(url)) {
						bean.setCurrentBytes(0);
						bean.setStatus(AppInfoBean.Status.CANUPGRADE);
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

		private InstallAppBean notifyAppDataChange(String url, DownloadBean downloadBean) {
			if (downloadBean == null || url == null)
				return null;
			List<InstallAppBean> list = updateAppListAdapter.getList();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					InstallAppBean bean = list.get(i);
					if (bean.getUrl() != null && bean.getUrl().equals(url)) {
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
	public void onClick(View v) {
		switch (v.getId()) {
		/*更新所有*/
		case R.id.fac_update_all_tv:
			if (updateList == null)
				return;
			if (!DownloadManager.getInstance().canDownload(mActivity))
				return;
			if (!NetworkUtils.getNetworkInfo(mActivity).equals(NetworkUtils.NETWORK_TYPE_WIFI)
					&& Utils.getSettingModel(mActivity).isGprsDownloadPromt()) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						for (InstallAppBean appBean : updateList) {
							if (appBean.getStatus() != AppInfoBean.Status.CANINSTALL) {

								DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
										"" + appBean.getApkId(), "" + appBean.getVersionCode());
								if (downloadBean != null) {
									DataEyeManager.getInstance().source(StatisticConstan.SrcName.UPGRADE, 0, null, 0,
											null, null, false);
								}
								UpdateManage.getInstance(mActivity).addDownload(appBean, mActivity, true, false);
							}

						}
					}
				};

				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				};
				Utils.showDialog(mActivity, ResourceUtil.getString(mActivity, R.string.warm_tips),
						ResourceUtil.getString(mActivity, R.string.dialog_download_prompt),
						ResourceUtil.getString(mActivity, R.string.dialog_download_prompt_download), positiveListener,
						ResourceUtil.getString(mActivity, R.string.dialog_download_prompt_cancle), negativeListener);
			} else {
				for (InstallAppBean appBean : updateList) {

					if (appBean.getStatus() != AppInfoBean.Status.CANINSTALL) {
						DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
								"" + appBean.getApkId(), "" + appBean.getVersionCode());
						if (downloadBean != null) {
							DataEyeManager.getInstance().source(StatisticConstan.SrcName.UPGRADE, 0, null, 0, null,
									null, false);
						}
						UpdateManage.getInstance(mActivity).addDownload(appBean, mActivity, true, false);
					}

				}
			}
			break;
		case R.id.e_retry_btn:
			initUpdata();
			break;
		}
	}

	/**
	 * @desc: 刷新updateAll控件
	 * @params:
	 * @return: void
	 */
	public void refreshView() {
		if (updateList == null) {
			uiHandler.sendEmptyMessage(0);
			return;
		}

		int count = 0;
		for (InstallAppBean appBean : updateList) {
			if (appBean.getStatus() == AppInfoBean.Status.CANINSTALL) {
				count++;
			}
		}
		if (count == updateList.size()) {
			uiHandler.sendEmptyMessage(1);
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
