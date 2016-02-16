package com.x.ui.activity.myApps;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.x.R;
import com.x.business.favorite.FavoriteManage;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.FavoriteAppBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.CloneClass;
import com.x.publics.utils.Constan;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.Utils;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.adapter.FavoriteAppListAdapter;
import com.x.ui.adapter.FavoriteAppViewHolder;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView.OnActionClickListener;

public class FavoriteAppActivity extends BaseActivity {

	private Activity context = this;
	private BroadcastReceiver mDownloadUiReceiver;
	private FavoriteAppListAdapter favoriteAppListAdapter;
	private ActionSlideExpandableListView mAppGv;
	public List<FavoriteAppBean> favoriteAppBeanList;
	private View emptyView, loadingView;
	private View loadingPb, loadingLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_app_activity);
		loadingView = findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		emptyView = findViewById(R.id.empty_rl);
		mAppGv = (ActionSlideExpandableListView) findViewById(R.id.favorite_app_lv);
		mAppGv.setItemActionListener(actionClickListener, R.id.favo_detail_app_tv, R.id.favo_cancel_app_tv);
		setTabTitle(R.string.page_favorite_apps);
		registerReceiver();
		initFavoriteAppData();

	}

	private OnActionClickListener actionClickListener = new OnActionClickListener() {

		@Override
		public void onClick(View itemView, View clickedView, int position) {
			final FavoriteAppBean favoriteAppBean = (FavoriteAppBean) favoriteAppListAdapter.getList().get(position);
			switch (clickedView.getId()) {
			case R.id.favo_detail_app_tv:
				gotoAppDetail(favoriteAppBean);
				break;
			case R.id.favo_cancel_app_tv:
				cancelFavoriteApp(favoriteAppBean.getFavoriteResourceId());
				break;
			}
		}
	};

	private void cancelFavoriteApp(int resId) {
		FavoriteManage.getInstance(context).cancelFavoriteApp(resId);
		notifyCancleFavorite(resId);
	}

	/**
	 * 跳转至详情页面
	 * @param favoriteAppBean
	 */
	private void gotoAppDetail(FavoriteAppBean favoriteAppBean) {
		Intent intent = new Intent(context, AppDetailActivity.class);
		intent.putExtra("appInfoBean", Utils.getAppInfoBean(favoriteAppBean));
		intent.putExtra("ct", Constan.Ct.APP_FAVORITE_MANAGEMENT); //  应用收藏管理 ct 值
		context.startActivity(intent);

	}

	/**
	 * 注册下载Receiver
	 */
	private void registerReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);
	}

	/**
	 * 初始化应用列表
	 */
	private void initFavoriteAppListView() {
		loadingView.setVisibility(View.GONE);
		mAppGv.setVisibility(View.VISIBLE);
		if (favoriteAppBeanList != null && favoriteAppBeanList.size() > 0) {
			emptyView.setVisibility(View.GONE);
			favoriteAppListAdapter = new FavoriteAppListAdapter(context);

			favoriteAppListAdapter.setList(favoriteAppBeanList);
			mAppGv.setAdapter(favoriteAppListAdapter, R.id.favo_top_rl, R.id.favo_expand_ll,
					new OnItemExpandCollapseListener() {
						@Override
						public void onExpand(View itemView, View parentView, int position) {
							String url = (String) parentView.getTag();
							View taskListItem = mAppGv.findViewWithTag(url);
							FavoriteAppViewHolder viewHolder = new FavoriteAppViewHolder(taskListItem);
							if (viewHolder != null)
								viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_up);
						}

						@Override
						public void onCollapse(View itemView, View parentView, int position) {
							String url = (String) parentView.getTag();
							View taskListItem = mAppGv.findViewWithTag(url);
							FavoriteAppViewHolder viewHolder = new FavoriteAppViewHolder(taskListItem);
							if (viewHolder != null)
								viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
						}
					});
		} else {
			emptyView.setVisibility(View.VISIBLE);
		}

	}

	private void initFavoriteAppData() {
		if (favoriteAppBeanList == null
				|| favoriteAppBeanList.size() != FavoriteManage.getInstance(FavoriteAppActivity.this)
						.getFavoriteAppSize()) {
			loadingView.setVisibility(View.VISIBLE);
			mAppGv.setVisibility(View.GONE);
			Utils.executeAsyncTask(new FavoriteAppDataTask());
		}
	}

	private class FavoriteAppDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			FavoriteManage.getInstance(FavoriteAppActivity.this).initFavoriteAppData();

			favoriteAppBeanList = FavoriteManage.getInstance(FavoriteAppActivity.this).adapterFavoriteAppBean();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			initFavoriteAppListView();
		}
	}

	/**
	 * 下载数据界面更新
	 
	 *
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
				View taskListItem;
				FavoriteAppViewHolder viewHolder;
				DownloadBean downloadBean;
				if (mAppGv == null) {
					return;
				}
				switch (type) {
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					notifyAppDataChange(url, downloadBean);
					if (!TextUtils.isEmpty(url)) {
						//												notifyAppDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = mAppGv.findViewWithTag(url);
						if (taskListItem != null) {
							viewHolder = new FavoriteAppViewHolder(taskListItem);
							viewHolder.refreshAppStatus(AppInfoBean.Status.CANINSTALL, context);
							//							FavoriteManage.getInstance(mContext).updateFavoriteAppStatus(resId, status) ;
						}
					}
					break;
				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						//						notifyAppDataChange(url, AppInfoBean.Status.CANUPGRADE);
						taskListItem = mAppGv.findViewWithTag(url);
						if (taskListItem != null) {
							viewHolder = new FavoriteAppViewHolder(taskListItem);
							viewHolder.refreshAppStatus(AppInfoBean.Status.NORMAL, context);
						}
					}
					break;
				case MyIntents.Types.COMPLETE_UNIINSTALL:
					taskListItem = mAppGv.findViewWithTag(url);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					notifyAppDataChange(url, downloadBean);
					if (taskListItem != null) {
						viewHolder = new FavoriteAppViewHolder(taskListItem);
						viewHolder.refreshAppStatus(AppInfoBean.Status.NORMAL, context);
					}
					break;
				case MyIntents.Types.COMPLETE_INSTALL:
					LogUtil.getLogger().d("handler Intent:COMPLETE_INSTALL");
					url = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					notifyAppDataChange(url, downloadBean);
					taskListItem = mAppGv.findViewWithTag(url);
					if (taskListItem != null) {
						viewHolder = new FavoriteAppViewHolder(taskListItem);
						viewHolder.refreshAppStatus(AppInfoBean.Status.CANLAUNCH, context);
					}
					break;
				case MyIntents.Types.WAIT:
					url = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					notifyAppDataChange(url, downloadBean);
					if (!TextUtils.isEmpty(url)) {
						//						notifyAppDataChange(url, AppInfoBean.Status.WAITING);
						taskListItem = mAppGv.findViewWithTag(url);
						if (taskListItem != null) {
							viewHolder = new FavoriteAppViewHolder(taskListItem);
							viewHolder.refreshAppStatus(AppInfoBean.Status.WAITING, context);
							viewHolder.refreshDownloadPercent(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
									intent.getStringExtra(MyIntents.PROCESS_SPEED),
									Integer.valueOf(intent.getStringExtra(MyIntents.PROCESS_PROGRESS)));
						}
					}
					break;
				case MyIntents.Types.PREDOWNLOAD:
					url = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					notifyAppDataChange(url, downloadBean);
					if (!TextUtils.isEmpty(url)) {
						//						notifyAppDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = mAppGv.findViewWithTag(url);
						if (taskListItem != null) {
							viewHolder = new FavoriteAppViewHolder(taskListItem);
							viewHolder.refreshAppStatus(AppInfoBean.Status.CONNECTING, context);
							viewHolder.refreshDownloadPercent(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
									intent.getStringExtra(MyIntents.PROCESS_SPEED),
									Integer.valueOf(intent.getStringExtra(MyIntents.PROCESS_PROGRESS)));
						}
					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					notifyAppDataChange(url, downloadBean);
					if (!TextUtils.isEmpty(url)) {
						//						notifyAppDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = mAppGv.findViewWithTag(url);
						if (taskListItem != null) {
							viewHolder = new FavoriteAppViewHolder(taskListItem);
							viewHolder.refreshAppStatus(AppInfoBean.Status.PAUSED, context);
							viewHolder.refreshDownloadPercent(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
									intent.getStringExtra(MyIntents.PROCESS_SPEED),
									Integer.valueOf(intent.getStringExtra(MyIntents.PROCESS_PROGRESS)));
						}
					}
					break;
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					notifyAppDataChange(url, downloadBean);
					if (!TextUtils.isEmpty(url)) {
						//						notifyAppDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = mAppGv.findViewWithTag(url);
						if (taskListItem != null) {
							viewHolder = new FavoriteAppViewHolder(taskListItem);
							viewHolder.refreshAppStatus(AppInfoBean.Status.DOWNLOADING, context);
							viewHolder.refreshDownloadPercent(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
									intent.getStringExtra(MyIntents.PROCESS_SPEED),
									Integer.valueOf(intent.getStringExtra(MyIntents.PROCESS_PROGRESS)));
						}
					}
					break;
				case MyIntents.INTENT_TYPE_FAVORITE_REFRESH_LIST:
					initFavoriteAppData();
				case MyIntents.Types.MAKE_UPGRADE:
					notifyPatchUpgrade();
					break;
				case MyIntents.Types.MERGE_PATCH:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						taskListItem = mAppGv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new FavoriteAppViewHolder(taskListItem);
						viewHolder.refreshMerge(context);
					}
					break;
				default:
					break;
				}

			}
		}

	}

	private void notifyPatchUpgrade() {
		if (favoriteAppListAdapter == null)
			return;
		List<FavoriteAppBean> list = favoriteAppListAdapter.getList();
		HashMap<String, InstallAppBean> updateAppMap = UpdateManage.getInstance(this).findAllUpdateInstallAppMap();
		for (FavoriteAppBean favoriteAppBean : list) {
			String packageName = favoriteAppBean.getFavoritePackageName();
			InstallAppBean updateAppBean = updateAppMap.get(packageName);
			if (updateAppBean != null && updateAppBean.getIsPatch()) {
				favoriteAppBean.setPatch(true);
				favoriteAppBean.setPatchUrl(updateAppBean.getUrlPatch());
				favoriteAppBean.setPatchSize(updateAppBean.getPatchSize());

				// 增量更新 刷新显示增量大小
				View taskListItem = mAppGv.findViewWithTag(favoriteAppBean.getFavoriteDownloadUrl());
				FavoriteAppViewHolder viewHolder = new FavoriteAppViewHolder(taskListItem);
				if (viewHolder != null) {
					viewHolder.refreshPatchUpdate(favoriteAppBean);
				}
			}
		}
	}

	private void notifyCancleFavorite(int resId) {
		if (favoriteAppListAdapter == null)
			return;
		List<FavoriteAppBean> list = favoriteAppListAdapter.getList();
		for (int i = 0; i < list.size(); i++) {
			FavoriteAppBean favoriteAppBean = list.get(i);
			if (favoriteAppBean.getFavoriteResourceId() == resId) {
				list.remove(i);
				favoriteAppListAdapter.notifyDataSetChanged();
				mAppGv.collapse(); //add by ZhouHua
				break;
			}
		}
		if (list.isEmpty()) {
			emptyView.setVisibility(View.VISIBLE);
		}
	}

	private void notifyAppDataChange(String url, DownloadBean bean) {
		if (bean == null || favoriteAppListAdapter == null)
			return;
		List<FavoriteAppBean> list = favoriteAppListAdapter.getList();
		for (int i = 0; i < list.size(); i++) {
			FavoriteAppBean favoriteAppBean = list.get(i);
			if (favoriteAppBean.getFavoriteDownloadUrl() != null
					&& favoriteAppBean.getFavoriteDownloadUrl().equals(url)) {
				try {
					favoriteAppBean = (FavoriteAppBean) CloneClass.cloneSuper(bean, favoriteAppBean);
					favoriteAppBean.setFavoriteStatus(Utils.convertStatus(bean.getStatus()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				list.set(i, favoriteAppBean);
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		initFavoriteAppData();
		super.onResume();
		DataEyeManager.getInstance().source(StatisticConstan.SrcName.FAVORITE, 0, null, 0, null, null, false);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {

			String parentActivityN = getIntent().getStringExtra("activity_name");
			if (parentActivityN == null) {
				startActivity(new Intent(this, MainActivity.class));
				LogUtil.getLogger().d("startActivity(new Intent(this, MainActivity.class))");
			}
			finish();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		String parentActivityN = getIntent().getStringExtra("activity_name");
		if (parentActivityN == null) {
			startActivity(new Intent(this, MainActivity.class));
			LogUtil.getLogger().d("startActivity(new Intent(this, MainActivity.class))");
		}
		super.onBackPressed();
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
