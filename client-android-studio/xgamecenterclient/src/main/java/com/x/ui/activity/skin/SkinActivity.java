/**   
* @Title: SkinActivity.java
* @Package com.x.ui.activity.skin
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-12-9 上午10:48:15
* @version V1.0   
*/

package com.x.ui.activity.skin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.model.SkinListResponse;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.SkinInfoBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.adapter.SkinListAdapter;
import com.x.ui.adapter.SkinViewHolder;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;

/**
* @ClassName: SkinActivity
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-12-9 上午10:48:15
* 
*/

public class SkinActivity extends BaseActivity implements OnClickListener {

	private ListView skinLv;
	private boolean inited = false;
	private Activity mActivity = this;
	private SkinListAdapter skinAdapter;
	private PullToRefreshListView pulltoRefreshLv;
	private DownloadUiReceiver mDownloadUiReceiver;
	private ArrayList<SkinInfoBean> skinList = new ArrayList<SkinInfoBean>();
//	private ArrayList<SkinInfoBean> skinList = new ArrayList<SkinInfoBean>();

	private TextView mTitleTv, mRetryTv;
	private ImageView mGobackIv;
	private View mNavigationView, mTitleView, mTitlePendant, errorView, loadingView, loadingPb, loadingLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skin);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
		// 注册广播
		if (!inited) {
			registUiReceiver();
		}
		getData();
	}

	/** 
	* @Title: initView 
	* @Description: TODO 
	* @return void    
	*/
	private void initView() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mTitleTv.setText(R.string.page_skin);
		mNavigationView.setOnClickListener(this);

		errorView = LayoutInflater.from(mActivity).inflate(R.layout.error, null);
		mRetryTv = (TextView) errorView.findViewById(R.id.e_retry_btn);
		mRetryTv.setOnClickListener(this);
		loadingView = LayoutInflater.from(mActivity).inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);

		pulltoRefreshLv = (PullToRefreshListView) findViewById(R.id.fhr_skin_lv);
		pulltoRefreshLv.setEmptyView(loadingView);
		pulltoRefreshLv.setMode(Mode.DISABLED);

		skinAdapter = new SkinListAdapter(mActivity, mHandler);
		skinLv = pulltoRefreshLv.getRefreshableView();
		skinLv.setAdapter(skinAdapter);
		skinAdapter.setList(skinList);
	}

	/**
	* @Title: getData 
	* @Description: 获取网络数据
	* @return void
	 */
	private void getData() {
		SkinConfigManager.getInstance().getSkinList(mActivity, mHandler);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		case R.id.e_retry_btn:
			// 网络检测
			if (!NetworkUtils.isNetworkAvailable(mActivity)) {
				ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.network_canot_work),
						Toast.LENGTH_SHORT);
				return;
			}
			errorView.setVisibility(View.GONE);
			pulltoRefreshLv.setEmptyView(loadingView);
			getData();
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SkinConfigManager.onSuccess:
				SkinListResponse response = (SkinListResponse) msg.obj;
				//				skinList = (ArrayList<SkinInfoBean>) response.getSkinlist();
				//把服务器和本地已下载的数据进行对比，以正确显示状态
				if (response != null && response.state.code == 200) {
					if (!response.getSkinlist().isEmpty()) {
						Utils.executeAsyncTask(new makeResponseData(), response);
					} else {
						loadingView.setVisibility(View.GONE);
						pulltoRefreshLv.setEmptyView(errorView);
					}
				}
				break;

			case SkinConfigManager.onFailure:
				loadingView.setVisibility(View.GONE);
				pulltoRefreshLv.setEmptyView(errorView);
				break;

			case SkinConfigManager.onRefreshUi:
				setSkinTheme();
				break;
			}
		};
	};

	private class makeResponseData extends AsyncTask<SkinListResponse, Void, SkinListResponse> {

		@Override
		protected SkinListResponse doInBackground(SkinListResponse... params) {
			SkinListResponse response = params[0];
			HashMap<String, DownloadBean> downloadAppMap = DownloadEntityManager.getInstance().getAllDownloadThemeMap();
			//服务器的数据和本地的进行状态的比较
			response.skinlist = (ArrayList<SkinInfoBean>) compareList(response.getSkinlist(), downloadAppMap);
			return response;
		}

		@Override
		protected void onPostExecute(SkinListResponse response) {
			super.onPostExecute(response);
			skinList.clear();
			//只显示默认皮肤
//			skinList.addAll(response.skinlist);
			skinList.add(response.skinlist.get(0));
			skinAdapter.setList(skinList);
			SkinConfigManager.getInstance().setSkinIsRead(mActivity, true);
		}

	}

	/**
	* @Title: compareList 
	* @Description: TODO 
	* @param @param responseList
	* @param @param downloadAppMap
	* @param @return    
	* @return List<SkinInfoBean>
	 */
	private List<SkinInfoBean> compareList(List<SkinInfoBean> responseList, HashMap<String, DownloadBean> downloadAppMap) {
		SkinInfoBean bean = new SkinInfoBean();
		bean.setPackageName(mActivity.getPackageName());
		bean.setSkinName(ResourceUtil.getString(mActivity, R.string.app_name));
		bean.setStatus(AppInfoBean.Status.CANLAUNCH);
		bean.setDescription(mActivity.getResources().getString(R.string.skin_default_description));
		bean.setSkinName(mActivity.getResources().getString(R.string.skin_default_name));

		String currentSkinPkgName = SkinConfigManager.getInstance().getCurrentSkinPackageName(mActivity);
		for (SkinInfoBean skinInfoBean : responseList) {
			String apkId = "" + skinInfoBean.getSkinId();
			DownloadBean downloadBean = null;
			if ((downloadBean = downloadAppMap.get(apkId)) != null
					&& downloadBean.getVersionCode() == skinInfoBean.getVersionCode()) {
				int status = downloadBean.getStatus();
				if (status == DownloadTask.TASK_DOWNLOADING) {
					skinInfoBean.setStatus(AppInfoBean.Status.DOWNLOADING);
					skinInfoBean.setCurrentBytes(downloadBean.getCurrentBytes());
					skinInfoBean.setApkSize(downloadBean.getTotalBytes());
				} else if (status == DownloadTask.TASK_PAUSE) {
					skinInfoBean.setStatus(AppInfoBean.Status.PAUSED);
					skinInfoBean.setCurrentBytes(downloadBean.getCurrentBytes());
					skinInfoBean.setApkSize(downloadBean.getTotalBytes());
				} else if (status == DownloadTask.TASK_FINISH) {

					if (currentSkinPkgName.equals(skinInfoBean.getPackageName())) {
						skinInfoBean.setStatus(AppInfoBean.Status.CANLAUNCH);
						bean.setStatus(AppInfoBean.Status.CANINSTALL);
					} else {
						skinInfoBean.setStatus(AppInfoBean.Status.CANINSTALL);
					}
				} else if (status == DownloadTask.TASK_WAITING) {
					skinInfoBean.setStatus(AppInfoBean.Status.WAITING);
					skinInfoBean.setCurrentBytes(downloadBean.getCurrentBytes());
					skinInfoBean.setApkSize(downloadBean.getTotalBytes());
				}
			}
		}

		responseList.add(0, bean);
		boolean mBoo = false;
		for (SkinInfoBean skinInfoBean : responseList) {
			if (currentSkinPkgName.equals(skinInfoBean.getPackageName()))
				mBoo = true;
		}
		if (mBoo != true)
			responseList.get(0).setStatus(AppInfoBean.Status.CANINSTALL);

		return responseList;
	}

	//********************************注册广播************************************//
	private void registUiReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);
		inited = true;
	}

	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			handleIntent(intent);
		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String url = null;
				String percent;
				View taskListItem;
				SkinViewHolder viewHolder;
				switch (type) {
				case MyIntents.Types.COMPLETE://显示设置
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifySkinDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = skinLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new SkinViewHolder(taskListItem);
						viewHolder.refreshAppStatus(AppInfoBean.Status.CANINSTALL, mActivity, null);
					}
					break;
				case MyIntents.Types.WAIT://
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifySkinDataChange(url, AppInfoBean.Status.WAITING);
						taskListItem = skinLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new SkinViewHolder(taskListItem);
						viewHolder.refreshAppStatus(AppInfoBean.Status.WAITING, mActivity, null);
					}
					break;
				case MyIntents.Types.PREDOWNLOAD://正在下載，显示暂停按钮
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifySkinDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = skinLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new SkinViewHolder(taskListItem);
						viewHolder.refreshAppStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);
					}
					break;

				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifySkinDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = skinLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new SkinViewHolder(taskListItem);
						viewHolder.refreshAppStatus(AppInfoBean.Status.PAUSED, mActivity, percent);
					}
					break;

				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifySkinDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = skinLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new SkinViewHolder(taskListItem);
						viewHolder.refreshAppStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);
					}
					break;

				case MyIntents.Types.ERROR://出现网络错误等
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifySkinDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = skinLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new SkinViewHolder(taskListItem);
						viewHolder.refreshAppStatus(AppInfoBean.Status.PAUSED, mActivity, percent);
					}
					break;
				}
			}
		}

		private void notifySkinDataChange(String url, int status) {
			List<SkinInfoBean> list;
			if (skinLv.getAdapter() == null) {
				return;
			}
			list = ((SkinListAdapter) ((HeaderViewListAdapter) skinLv.getAdapter()).getWrappedAdapter()).getList();
			for (SkinInfoBean skinInfoBean : list) {
				if (skinInfoBean.getApkUrl() != null && skinInfoBean.getApkUrl().equals(url)) {
					skinInfoBean.setStatus(status);
					break;
				}
			}
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
