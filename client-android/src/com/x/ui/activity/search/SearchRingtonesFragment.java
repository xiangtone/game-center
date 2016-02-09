/**   
 * @Title: SearchRingtonesFragment.java
 * @Package com.mas.amineappstore.ui.activity.search
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-7-11 下午5:05:55
 * @version V1.3   
 */

package com.x.ui.activity.search;

import java.util.ArrayList;
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
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.model.KeywordsResponse;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.KeywordBean;
import com.x.publics.model.RingtonesBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.RingtonesAdapter;
import com.x.ui.adapter.RingtonesHolder;
import com.x.ui.view.CustomSearchView.SearchAutoComplete;
import com.x.ui.view.autobreakview.AutoBreakAdapter;
import com.x.ui.view.autobreakview.AutoBreakListView;
import com.x.ui.view.autobreakview.AutoBreakOnItemClickListener;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshListView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;

/**
 * @ClassName: SearchRingtonesFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-11 下午5:05:56
 * 
 */

public class SearchRingtonesFragment extends BaseFragment {

	private int actionRc;
	private String action;
	private String lastQueryKey;
	private int listPageNum = 1;
	private int keywordPageNum = 1;

	private ListView mListView;
	private View loadingPb, loadingLogo;
	private PullToRefreshListView ptrLv;
	private RingtonesAdapter ringtonesAdapter;
	private View loadingView, errorView, keywordsView;

	private List<KeywordBean> keywordList = new ArrayList<KeywordBean>();
	private ArrayList<RingtonesBean> musicList = new ArrayList<RingtonesBean>();

	// 自动换行
	private boolean isLast = false;
	private boolean hasKeywordData = false;
	private boolean isVisibleToUser = false;
	private AutoBreakAdapter autoBreakAdapter;
	private AutoBreakListView autoBreakListView;

	// 广播
	private boolean inited = false;
	private DownloadUiReceiver mDownloadUiReceiver;

	public static Fragment newInstance(Bundle bundle) {
		SearchRingtonesFragment fragment = new SearchRingtonesFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	public int getAlbumId() {
		return SearchConstan.Album.RINGTONES_ALBUM_ID;
	}

	public int getRc() {
		return Constan.Rc.POST_MUSIC_SEARCH;
	}

	public void setParams() {
		if (SearchManager.getInstance().getInitState(mActivity)) {
			String queryKey = SearchManager.getInstance().getQueryKey(mActivity);
			if (!TextUtils.isEmpty(queryKey) && !queryKey.equals(lastQueryKey)) {
				listPageNum = 1;
				showLoadingView();
				lastQueryKey = queryKey;
				actionRc = getRc();
				action = SearchConstan.MUSIC_ACTION;
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

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// 注册广播
		if (!inited) {
			registUiReceiver();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// pause
		MediaPlayerUtil.getInstance(mActivity).pause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
		// release
		MediaPlayerUtil.getInstance(mActivity).release();
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

		ringtonesAdapter = new RingtonesAdapter(mActivity,3,0,"");
		mListView.setAdapter(ringtonesAdapter);
		ringtonesAdapter.setList(musicList);
		ringtonesAdapter.setListView(mListView);

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

				// 设置选中的关键字 到 搜索框中
				SearchManager.getInstance().hideSoftInput(mActivity);
				SearchAutoComplete mQueryTextView = (SearchAutoComplete) getActivity().findViewById(
						R.id.search_src_text);
				mQueryTextView.setText(lastQueryKey);

				// 保存搜索关键字
				showLoadingView();
				SearchManager.getInstance().setInitState(mActivity, true);
				SearchManager.getInstance().setQueryKey(mActivity, lastQueryKey);
				if (actionRc == Constan.Rc.KEYWORDS_MUSIC_LIST) {
					DataEyeManager.getInstance().source(
							StatisticConstan.SrcName.SEARCH,
							0,
							null,
							0L,
							null,
							DataEyeManager.getSearchKey(StatisticConstan.SearchType.SEARCH_RINGTONE_LIST + "-"
									+ lastQueryKey), true);
				}
				getListData();
			}
		});

		SearchManager.getInstance().getKwData(mActivity, handler, getAlbumId(), keywordPageNum);

		return rootView;
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
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SearchConstan.State.SEARCH_KEYWORDS_SUCCESS:
				hasKeywordData = true;
				KeywordsResponse keywordsResponse = (KeywordsResponse) msg.obj;
				List<KeywordBean> responseList = keywordsResponse.keywordlist;
				keywordList.clear();

				// 将refresh排在第一位
				KeywordBean been = new KeywordBean();
				been.setKeyword("Refresh");
				keywordList.add(been);
				for (KeywordBean keywordBean : responseList) {
					keywordList.add(keywordBean);
				}

				autoBreakAdapter.setAnim(); // 设置动画显示
				autoBreakAdapter.setList(keywordList);
				isLast = keywordsResponse.isLast;
				showKeywordsView();
				break;

			case SearchConstan.State.RESPONSE_MUSIC_DATA_SUCCESS:
				ptrLv.onRefreshComplete();
				ArrayList<DownloadBean> download = DownloadEntityManager.getInstance().getAllDownload();

				Bundle bundle = msg.getData();
				isLast = bundle.getBoolean("isLast");
				boolean isRecommend = bundle.getBoolean("isRecommend");
				ArrayList<RingtonesBean> list = bundle.getParcelableArrayList("musicList");
				// 是否推荐数据
				if (isRecommend) {
					musicList = compareList(list, download);
				} else {
					musicList.addAll(list);
					musicList = compareList((ArrayList<RingtonesBean>) musicList, download);
				}
				// 是否存在分页
				if (isLast || isRecommend) {
					ptrLv.setMode(Mode.DISABLED);
				} else {
					ptrLv.setMode(Mode.PULL_FROM_END);
				}

				ringtonesAdapter.setList(musicList);
				break;

			case SearchConstan.State.SEARCH_KEYWORDS_FAILURE:
			case SearchConstan.State.RESPONSE_MUSIC_DATA_FAILURE:
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
		};
	};

	public void showKeywordsView() {
		if (hasKeywordData) {
			ptrLv.setVisibility(View.GONE);
			errorView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			keywordsView.setVisibility(View.VISIBLE);
			SearchManager.getInstance().putCurValue(SearchConstan.RINGTONE_FRAGMENT_ID, true);
			MediaPlayerUtil.getInstance(mActivity).pause(); // 暂停播放音乐
		}
	}

	private void showLoadingView() {
		musicList.clear();
		ringtonesAdapter.setList(musicList);
		errorView.setVisibility(View.GONE);
		keywordsView.setVisibility(View.GONE);
		ptrLv.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.VISIBLE);
		ptrLv.setEmptyView(loadingView);
		SearchManager.getInstance().putCurValue(SearchConstan.RINGTONE_FRAGMENT_ID, false);
		MediaPlayerUtil.getInstance(mActivity).pause(); // 暂停播放音乐
	}

	private void showErrorView() {
		ptrLv.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		keywordsView.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
		SearchManager.getInstance().putCurValue(SearchConstan.RINGTONE_FRAGMENT_ID, false);
		MediaPlayerUtil.getInstance(mActivity).pause(); // 暂停播放音乐
		// errorView.findViewById(R.id.e_retry_btn).setOnClickListener(
		errorView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
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
	 * 
	 * @Title: compareList
	 * @Description: 比较状态
	 * @param @param responseList
	 * @param @param download
	 * @param @return
	 * @return ArrayList<RingtonesBean>
	 */
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
				String url;
				String percent;
				View taskListItem;

				RingtonesHolder holder;
				switch (type) {
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						holder = new RingtonesHolder(taskListItem);
						holder.refreshDownloadStatus(AppInfoBean.Status.CANINSTALL, mActivity, null);
					}
					break;
				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyDataChange(url, AppInfoBean.Status.NORMAL);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						holder = new RingtonesHolder(taskListItem);
						holder.refreshDownloadStatus(AppInfoBean.Status.NORMAL, mActivity, null);
					}
					break;
				case MyIntents.Types.WAIT:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyDataChange(url, AppInfoBean.Status.WAITING);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						holder = new RingtonesHolder(taskListItem);
						holder.refreshDownloadStatus(AppInfoBean.Status.WAITING, mActivity, percent);
					}
					break;
				case MyIntents.Types.PREDOWNLOAD:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyDataChange(url, AppInfoBean.Status.CONNECTING);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						holder = new RingtonesHolder(taskListItem);
						holder.refreshDownloadStatus(AppInfoBean.Status.CONNECTING, mActivity, percent);
					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						holder = new RingtonesHolder(taskListItem);
						holder.refreshDownloadStatus(AppInfoBean.Status.PAUSED, mActivity, percent);
					}
					break;
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						holder = new RingtonesHolder(taskListItem);
						holder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, mActivity, percent);
					}
					break;
				default:
					break;
				}
			}
		}

		private void notifyDataChange(String url, int status) {
			List<RingtonesBean> list;
			if (mListView.getAdapter() == null) {
				return;
			}
			list = ((RingtonesAdapter) ((HeaderViewListAdapter) mListView.getAdapter()).getWrappedAdapter()).getList();
			for (RingtonesBean ringtonesBean : list) {
				if (ringtonesBean.getUrl() != null && ringtonesBean.getUrl().equals(url)) {
					ringtonesBean.setStatus(status);
					if (status == MyIntents.Types.COMPLETE) {
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
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.SEARCH_RINGTONE, isVisibleToUser);
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
