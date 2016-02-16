package com.x.ui.activity.ringtones;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.DataFetcher;
import com.x.publics.http.model.Pager;
import com.x.publics.http.model.RingtonesRequest;
import com.x.publics.http.model.RingtonesResponse;
import com.x.publics.http.model.RingtonesRequest.MusicRequestData;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.RingtonesBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.JsonUtil;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Constan.MediaType;
import com.x.receiver.ConnectChangeReceiver;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.RingtonesAdapter;
import com.x.ui.adapter.RingtonesHolder;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.State;
import com.x.ui.view.pulltorefresh.extra.SoundPullEventListener;

/**
 * 
* @ClassName: RingtonesCategoryHotFragment
* @Description: 铃声分类 HOT 子页面

* @date 2014-4-8 上午10:45:50
*
 */
public class RingtonesCategoryHotFragment extends BaseFragment {

	private PullToRefreshListView pulltoRefreshLv;
	private ListView listView = null;
	private ArrayList<RingtonesBean> listPath = new ArrayList<RingtonesBean>();
	private RingtonesAdapter adapter = null;
	private DownloadUiReceiver mDownloadUiReceiver;
	private RingtonesRequest request;
	private Pager pager;
	private View loadingView;
	private View errorView;
	private int pageNum = 1;
	private int categoryId;
	private boolean inited = false;
	private ValidWifiReceiver mValidWifiReceiver;
	private boolean mIsVisibleToUser = false;
	private View loadingPb, loadingLogo;

	public static Fragment newInstance(Bundle bundle) {
		RingtonesCategoryHotFragment fragment = new RingtonesCategoryHotFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = null;
		rootView = inflater.inflate(R.layout.fragment_ringtones, container, false);
		errorView = rootView.findViewById(R.id.e_error_rl);
		pulltoRefreshLv = (PullToRefreshListView) rootView.findViewById(R.id.ringtones_list_lv);
		listView = pulltoRefreshLv.getRefreshableView();

		pulltoRefreshLv.setMode(Mode.DISABLED);
		pulltoRefreshLv.setOnRefreshListener(onRefreshListener);
		loadingView = inflater.inflate(R.layout.loading, null);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		pulltoRefreshLv.setEmptyView(loadingView);
		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mActivity);
		soundListener.addSoundEvent(State.REFRESH_TO_RESET, R.raw.refresh);
		pulltoRefreshLv.setOnPullEventListener(soundListener);
		adapter = new RingtonesAdapter(mActivity,2,getCt(),getCategory());
		listView.setAdapter(adapter);
		adapter.setList(listPath);
		adapter.setListView(listView);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		categoryId = mActivity.getIntent().getIntExtra("categoryId", 0);
		// 注册广播
		if (!inited) {
			registUiReceiver();
		}
		// 加载数据
		if (listPath.isEmpty()) {
			getData(1);
		}
	}

	private void getData(int page) {
		request = new RingtonesRequest();
		pager = new Pager(page);
		//		pager.setPs(pageSize);
		request.setPager(pager);
		request.setColumn(getCategory());
		request.setCategoryId(categoryId);
		request.setData(new MusicRequestData(getCt()));
		DataFetcher.getInstance().getMusicCategoryData(request, myResponseListent, myErrorListener, true);
	}

	public int getCt() {
		return Constan.Ct.RINGTONES_CATEGORY;
	}

	public String getCategory() {
		return Constan.Category.CATEGORY_HOT;
	}

	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {
			pulltoRefreshLv.onRefreshComplete();
			LogUtil.getLogger().d("response==>" + response.toString());
			RingtonesResponse musicResponse = (RingtonesResponse) JsonUtil
					.jsonToBean(response, RingtonesResponse.class);
			if (musicResponse != null && musicResponse.state.code == 200) {
				if (!musicResponse.musiclist.isEmpty()) {
					listPath.addAll(musicResponse.musiclist);
					ArrayList<DownloadBean> download = DownloadEntityManager.getInstance().getAllDownload();
					listPath = compareList(listPath, download);
					adapter.setList(listPath);
					if (musicResponse.isLast) {
						cancleGridViewScorllable();
					} else {
						pulltoRefreshLv.setMode(Mode.BOTH);
					}
				} else {
					if (listPath.isEmpty()) {
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

	private OnRefreshListener2<ListView> onRefreshListener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			listPath = new ArrayList<RingtonesBean>();
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
		if (listPath.isEmpty()) {
			adapter.setList(listPath);
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
			pulltoRefreshLv.setMode(Mode.PULL_FROM_START);
	}

	public ArrayList<RingtonesBean> compareList(ArrayList<RingtonesBean> responseList, ArrayList<DownloadBean> download) {
		for (RingtonesBean ringtonesBean : responseList) {
			for (int i = 0; i < download.size(); i++) {
				DownloadBean downloadBean = download.get(i);
				String type = downloadBean.getMediaType();
				if (type.equals(MediaType.MUSIC) && downloadBean.getName().equals(ringtonesBean.getMusicName())) {
					int status = downloadBean.getStatus();
					switch (status) {
					case DownloadTask.TASK_DOWNLOADING:
						ringtonesBean.setStatus(AppInfoBean.Status.DOWNLOADING);
						ringtonesBean.setCurrentBytes((int) downloadBean.getCurrentBytes());
						ringtonesBean.setTotalBytes((int) downloadBean.getTotalBytes());
						break;
					case DownloadTask.TASK_PAUSE:
						ringtonesBean.setStatus(AppInfoBean.Status.PAUSED);
						ringtonesBean.setCurrentBytes((int) downloadBean.getCurrentBytes());
						ringtonesBean.setTotalBytes((int) downloadBean.getTotalBytes());
						break;
					case DownloadTask.TASK_FINISH:
						ringtonesBean.setStatus(AppInfoBean.Status.CANINSTALL);
						ringtonesBean.setFilepath(downloadBean.getLocalPath());
						break;
					case DownloadTask.TASK_LAUNCH:
						ringtonesBean.setStatus(AppInfoBean.Status.CANLAUNCH);
						break;
					case DownloadTask.TASK_WAITING:
						ringtonesBean.setStatus(AppInfoBean.Status.WAITING);
						ringtonesBean.setCurrentBytes((int) downloadBean.getCurrentBytes());
						ringtonesBean.setTotalBytes((int) downloadBean.getTotalBytes());
						break;
					case DownloadTask.TASK_CONNECTING:
						ringtonesBean.setStatus(AppInfoBean.Status.CONNECTING);
						ringtonesBean.setCurrentBytes((int) downloadBean.getCurrentBytes());
						ringtonesBean.setTotalBytes((int) downloadBean.getTotalBytes());
						break;
					}
				}
			}
		}
		return responseList;
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
				String url = intent.getStringExtra(MyIntents.URL);
				String percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
				int status = -1;
				switch (type) {
				case MyIntents.Types.COMPLETE:
					if (!TextUtils.isEmpty(url)) {
						status = AppInfoBean.Status.CANINSTALL;
						View view = listView.findViewWithTag(url);
						if (view != null) {
							RingtonesHolder mHolder = new RingtonesHolder(view);
							mHolder.refreshRingStatus(status, mActivity, percent);
						}
					}
					break;
				case MyIntents.Types.DELETE:
					if (!TextUtils.isEmpty(url)) {
						status = AppInfoBean.Status.NORMAL;
						View view = listView.findViewWithTag(url);
						if (view != null) {
							RingtonesHolder mHolder = new RingtonesHolder(view);
							mHolder.refreshRingStatus(status, mActivity, percent);
						}
					}
					break;
				case MyIntents.Types.WAIT:
					if (!TextUtils.isEmpty(url)) {
						status = AppInfoBean.Status.WAITING;
						View view = listView.findViewWithTag(url);
						if (view != null) {
							RingtonesHolder mHolder = new RingtonesHolder(view);
							mHolder.refreshRingStatus(status, mActivity, percent);
						}
					}
					break;
				case MyIntents.Types.PREDOWNLOAD:
					if (!TextUtils.isEmpty(url)) {
						status = AppInfoBean.Status.CONNECTING;
						View view = listView.findViewWithTag(url);
						if (view != null) {
							RingtonesHolder mHolder = new RingtonesHolder(view);
							mHolder.refreshRingStatus(status, mActivity, percent);
						}
					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					if (!TextUtils.isEmpty(url)) {
						status = AppInfoBean.Status.PAUSED;
						View view = listView.findViewWithTag(url);
						if (view != null) {
							RingtonesHolder mHolder = new RingtonesHolder(view);
							mHolder.refreshRingStatus(status, mActivity, percent);
						}
					}
					break;
				case MyIntents.Types.PROCESS:
					if (!TextUtils.isEmpty(url)) {
						status = AppInfoBean.Status.DOWNLOADING;
						View view = listView.findViewWithTag(url);
						if (view != null) {
							RingtonesHolder mHolder = new RingtonesHolder(view);
							mHolder.refreshRingStatus(status, mActivity, percent);
						}
					}
					break;
				default:
					break;
				}
				//update
				notifyDataChange(url, status, percent);
			}
		}

		private void notifyDataChange(String url, int status, String process) {
			List<RingtonesBean> list;
			if (listView.getAdapter() == null) {
				return;
			}
			list = ((RingtonesAdapter) ((HeaderViewListAdapter) listView.getAdapter()).getWrappedAdapter()).getList();
			for (RingtonesBean ringtonesBean : list) {
				if (ringtonesBean.getUrl() != null && ringtonesBean.getUrl().equals(url)) {
					ringtonesBean.setStatus(status);
					ringtonesBean.setProcess(process);
					if (status == AppInfoBean.Status.CANINSTALL) {
						ArrayList<DownloadBean> download = DownloadEntityManager.getInstance().getAllDownload();
						for (int i = 0; i < download.size(); i++) {
							if (url.equals(download.get(i).getUrl())) {
								ringtonesBean.setFilepath(download.get(i).getLocalPath());
							}
						}
					}
					break;
				}
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		//pause
		MediaPlayerUtil.getInstance(mActivity).release();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public ViewPager getPargentViewPager() {
		return ((RingtonesFragment) getParentFragment()).mViewPager;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LogUtil.getLogger().d("FragmentA==============onDestroyView");
		//release
		MediaPlayerUtil.getInstance(mActivity).release();
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
