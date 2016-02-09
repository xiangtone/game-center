/**   
 * @Title: SearchActivity.java
 * @Package com.mas.amineappstore.ui.activity.search
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-7-11 下午4:33:19
 * @version V1.3   
 */

package com.x.ui.activity.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.x.R;
import com.x.business.search.SearchConstan;
import com.x.business.search.SearchManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.RingtonesBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SilentUtil;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.adapter.AppListViewHolder;
import com.x.ui.adapter.SearchAppTipsAdapter;
import com.x.ui.adapter.SearchImageTipsAdapter;
import com.x.ui.adapter.SearchMusicTipsAdapter;
import com.x.ui.view.CustomSearchView;
import com.x.ui.view.TabPageIndicator;
import com.x.ui.view.CustomSearchView.OnQueryTextListener;
import com.x.ui.view.CustomSearchView.SearchAutoComplete;

/**
 * @ClassName: SearchActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-11 下午4:33:19
 * 
 */

public class SearchActivity extends BaseActivity {

	private int tabIndex;
	private View mSearchIcon;
	private ViewPager mViewPager;
	private Context context = this;
	private TabPageIndicator indicator;
	private CustomSearchView searchView;
	private SearchAutoComplete mQueryTextView;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private String appsTips, ringtonesTips, wallpapersTips;
	private List<String> titleList = new ArrayList<String>();
	private Fragment searchAppsFragment, searchRingtonesFragment, searchWallpapersFragment;
	private String lastQueryKey;
	private String currentInputText;
	private int currentPosition = 0;
	private boolean isChoose = false;

	private int pageNum = 1;
	private ListView mListView;
	private SearchAppTipsAdapter appTipsAdapter;
	private SearchImageTipsAdapter imageTipsAdapter;
	private SearchMusicTipsAdapter musicTipsAdapter;
	private View fragmentView, mTitleView, mNavigationView, mTitlePendant;
	private ArrayList<AppInfoBean> appList = new ArrayList<AppInfoBean>();
	private ArrayList<RingtonesBean> musicList = new ArrayList<RingtonesBean>();
	private ArrayList<WallpaperBean> imageList = new ArrayList<WallpaperBean>();

	// 注册广播
	private boolean inited = false;
	private DownloadUiReceiver mDownloadUiReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		SearchManager.getInstance().initParams(this);
		initTipsView();
		initSearchBox();
		initViewPager();
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		onShow(true);
	}

	@Override
	public void onPause() {
		super.onPause();
		onShow(false);
		SearchManager.getInstance().hideSoftInput(this);
		SearchManager.getInstance().stopShakeDetector();
	}

	public void onShow(boolean show) {
		int item = mViewPager.getCurrentItem();
		switch (item) {

		case 0:
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.SEARCH_APP, show);
			break;

		case 1:
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.SEARCH_RINGTONE, show);

			break;

		case 2:
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.SEARCH_WALLPAPER, show);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// when activity destroy remove the 'query key'
		SearchManager.getInstance().removeQueryKey(context);
		unRegistUiReceiver();
	}

	private void initViewPager() {
		appsTips = ResourceUtil.getString(context, R.string.option_search_apps);
		ringtonesTips = ResourceUtil.getString(context, R.string.option_search_ringtones);
		wallpapersTips = ResourceUtil.getString(context, R.string.option_search_wallpapers);
		titleList.clear();
		titleList.add(appsTips);
		titleList.add(ringtonesTips);
		titleList.add(wallpapersTips);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titleList);

		mViewPager = (ViewPager) findViewById(R.id.aam_content_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(3);

		indicator = (TabPageIndicator) findViewById(R.id.aam_indicator);
		indicator.setViewPager(mViewPager);
		indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// switch page to use the method
				currentPosition = position;
				setQueryHint(position);
				switchFragmentPage();
				SearchManager.getInstance().setPosition(context, position);
			}
		});
		indicator.setCurrentItem(tabIndex); // 当前位置
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		List<String> titles;

		public SectionsPagerAdapter(FragmentManager fm, List<String> titles) {
			super(fm);
			this.titles = titles;
		}

		private void setTitles(List<String> titles) {
			this.titles = titles;
			indicator.notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = SearchAppsFragment.newInstance(null);
				searchAppsFragment = (SearchAppsFragment) fragment;
				break;
			case 1:
				fragment = SearchRingtonesFragment.newInstance(null);
				searchRingtonesFragment = (SearchRingtonesFragment) fragment;
				break;
			case 2:
				fragment = SearchWallpapersFragment.newInstance(null);
				searchWallpapersFragment = (SearchWallpapersFragment) fragment;
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return PagerAdapter.POSITION_NONE;
		}
	}

	/**
	 * 
	 * @Title: initSearchBox
	 * @Description: 初始化搜索框
	 * @param
	 * @return void
	 */
	@SuppressLint("NewApi")
	private void initSearchBox() {
		/*		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				getSupportActionBar().setHomeButtonEnabled(true);
				getSupportActionBar().setDisplayOptions(
						ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP);
				getSupportActionBar().setCustomView(R.layout.actionbar_searchbar);
				searchView = (CustomSearchView) getSupportActionBar().getCustomView().findViewById(R.id.ab_searchView);*/

		// Back to main page
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.btn_back_ll);
		mNavigationView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// Search view
		searchView = (CustomSearchView) findViewById(R.id.ab_searchView);
		mQueryTextView = (SearchAutoComplete) findViewById(R.id.search_src_text);
		// mQueryTextView.setText(searchKey);
		setQueryHint(tabIndex);
		if (searchView == null)
			return;
		searchView.setSubmitButtonEnabled(true);
		if (searchView != null) {
			searchView.setOnQueryTextListener(new OnQueryTextListener() {

				@Override
				public boolean onQueryTextSubmit(String queryKey) {
					// TODO Auto-generated method stub
					if (!NetworkUtils.isNetworkAvailable(context)) {
						ToastUtil.show(context, ResourceUtil.getString(context, R.string.network_canot_work),
								Toast.LENGTH_SHORT);
						return true;
					}
					queryKey = Utils.removeSpace(queryKey);
					if (!TextUtils.isEmpty(queryKey) && !queryKey.equals(lastQueryKey)) {
						SearchManager.getInstance().hideSoftInput(SearchActivity.this);
						lastQueryKey = queryKey;
						SearchManager.getInstance().setInitState(context, true);
						SearchManager.getInstance().setQueryKey(context, lastQueryKey);
						showFragmentView();
						switchFragmentPage();
					}
					return false;
				}

				@Override
				public boolean onQueryTextChange(String inputText) {
					// TODO Auto-generated method stub
					if (!NetworkUtils.isNetworkAvailable(context)) {
						return true;
					}
					currentInputText = inputText;
					if (TextUtils.isEmpty(inputText)) {
						showKeywordView();
						showFragmentView();
					} else {
						// 将光标焦点移到文字末尾
						//mQueryTextView.setSelection(inputText.length());
						int rc = 0;
						switch (currentPosition) {
						case 0:
							rc = Constan.Rc.SEARCH_APPS_TIPS;
							break;
						case 1:
							rc = Constan.Rc.SEARCH_MUSIC_TIPS;
							break;
						case 2:
							rc = Constan.Rc.SEARCH_IMAGE_TIPS;
							break;
						}
						if (!isChoose && mQueryTextView.isFocused()) // 必须是获取焦点
							SearchManager.getInstance().getSearchTips(context, handler, rc, inputText, pageNum);
					}
					return false;
				}
			});
		}
		ImageView searchIcon = (ImageView) searchView.findViewById(R.id.search_mag_icon);
		searchIcon.setImageResource(R.drawable.transparent);
		searchIcon.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

		// Search Icon
		mSearchIcon = searchView.findViewById(R.id.search_go_btn);

		getSearchFocus(); // 获取焦点并自动弹出键盘
		//clearSearchFocus(); // 清除焦点
	}

	private void initTipsView() {
		tabIndex = getIntent().getIntExtra(SearchConstan.TAB_INDEX, 0);
		fragmentView = findViewById(R.id.ll_fragment_layout);
		mListView = (ListView) findViewById(R.id.search_tips_listview);
		// 赋值=去除ListView点击时，默认的黄（蓝）色背景色
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mListView.setVisibility(View.GONE);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				// check network
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, ResourceUtil.getString(context, R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}

				if (position == 0 && currentPosition == 0) {
					if (appList == null || appList.size() == 0) {
						return;
					}
					AppInfoBean appInfoBean = appList.get(position);
					Intent intent = new Intent(context, AppDetailActivity.class);
					intent.putExtra("appInfoBean", appInfoBean);
					intent.putExtra("ct", Constan.Ct.APP_SEARCH);
					startActivity(intent);
					// DataEyeManager.getInstance().source(StatisticConstan.SrcName.SEARCH, 0, null, 0L,  null, appInfoBean.getAppName(), true) ;
				} else {
					SearchManager.getInstance().hideSoftInput(SearchActivity.this);
					switch (currentPosition) {
					case 0:
						if (appList == null || appList.size() == 0) {
							return;
						}
						lastQueryKey = appList.get(position).getAppName();
						break;

					case 1:
						if (musicList == null || musicList.size() == 0) {
							return;
						}
						lastQueryKey = musicList.get(position).getMusicName();
						break;

					case 2:
						if (imageList == null || imageList.size() == 0) {
							return;
						}
						lastQueryKey = imageList.get(position).getImageName();
						break;
					}
					isChoose = true;
					mQueryTextView.setText(lastQueryKey);// 设置EditText控件的内容
					mQueryTextView.setSelection(lastQueryKey.length());// 将光标移至文字末尾
					SearchManager.getInstance().setInitState(context, true);
					SearchManager.getInstance().setQueryKey(context, lastQueryKey);
					showFragmentView();
					switchFragmentPage();
				}
			}
		});

		appTipsAdapter = new SearchAppTipsAdapter(this);
		imageTipsAdapter = new SearchImageTipsAdapter(this);
		musicTipsAdapter = new SearchMusicTipsAdapter(this);

	}

	private void showTipsView() {
		fragmentView.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		SearchManager.getInstance().setShowTipsView(context, true);
	}

	private void hideTipsView() {
		mListView.setVisibility(View.GONE);
		fragmentView.setVisibility(View.VISIBLE);
		SearchManager.getInstance().setShowTipsView(context, false);
	}

	private void showFragmentView() {
		isChoose = false;
		mListView.setVisibility(View.GONE);
		fragmentView.setVisibility(View.VISIBLE);
		SearchManager.getInstance().setShowTipsView(context, false);
		unRegistUiReceiver();
	}

	private void showKeywordView() {
		hideTipsView();
		// 清除保存的参数
		SearchManager.getInstance().setInitState(context, false);
		SearchManager.getInstance().setQueryKey(context, null);
		clearAllCache();

		if (searchAppsFragment != null) {
			((SearchAppsFragment) searchAppsFragment).showKeywordsView();
			SearchManager.getInstance().putCurValue(SearchConstan.APP_FRAGMENT_ID, true);
		}

		if (searchRingtonesFragment != null) {
			((SearchRingtonesFragment) searchRingtonesFragment).showKeywordsView();
			SearchManager.getInstance().putCurValue(SearchConstan.RINGTONE_FRAGMENT_ID, true);
		}

		if (searchWallpapersFragment != null) {
			((SearchWallpapersFragment) searchWallpapersFragment).showKeywordsView();
			SearchManager.getInstance().putCurValue(SearchConstan.WALLPAPER_FRAGMENT_ID, true);
		}
	}

	private void getSearchFocus() {
		if (searchView != null) {
			searchView.setFocusable(true);
			searchView.setFocusableInTouchMode(true);
			searchView.requestFocus();
			SearchManager.getInstance().showSoftInput(this);
		}
	}

	private void clearSearchFocus() {
		if (searchView != null) {
			searchView.clearFocus();
		}
	}

	private void setQueryHint(int index) {
		switch (index) {
		case 0:
			if (searchView != null)
				searchView.setQueryHint(ResourceUtil.getString(context, R.string.app_query_hint));
			break;
		case 1:
			if (searchView != null)
				searchView.setQueryHint(ResourceUtil.getString(context, R.string.ringtone_query_hint));
			break;
		case 2:
			if (searchView != null)
				searchView.setQueryHint(ResourceUtil.getString(context, R.string.wallpaper_query_hint));
			break;
		}
	}

	/**
	 * 
	 * @Title: switchFragmentPage
	 * @Description: TODO
	 * @param @param position
	 * @return void
	 */
	private void switchFragmentPage() {
		switch (currentPosition) {
		case 0:
			if (searchAppsFragment != null)
				((SearchAppsFragment) searchAppsFragment).setParams();
			break;

		case 1:
			if (searchRingtonesFragment != null)
				((SearchRingtonesFragment) searchRingtonesFragment).setParams();
			break;

		case 2:
			if (searchWallpapersFragment != null)
				((SearchWallpapersFragment) searchWallpapersFragment).setParams();
			break;
		}
	}

	/**
	* @Title: clearAllCache 
	* @Description: 清除所有搜索关键字的缓存数据
	* @param     
	* @return void
	 */
	private void clearAllCache() {
		if (!TextUtils.isEmpty(lastQueryKey))
			lastQueryKey = null;

		if (searchAppsFragment != null)
			((SearchAppsFragment) searchAppsFragment).clearQueryKeyCache();

		if (searchRingtonesFragment != null)
			((SearchRingtonesFragment) searchRingtonesFragment).clearQueryKeyCache();

		if (searchWallpapersFragment != null)
			((SearchWallpapersFragment) searchWallpapersFragment).clearQueryKeyCache();
	}

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SearchConstan.State.RESPONSE_APPS_TIPS_SUCCESS:
				currentInputText = Utils.removeSpace(currentInputText);
				if (!TextUtils.isEmpty(currentInputText)) {
					registUiReceiver(); // 注册广播
					HashMap<String, InstallAppBean> localAppMap = LocalAppEntityManager.getInstance().getAllAppsMap();
					HashMap<String, DownloadBean> downloadAppMap = DownloadEntityManager.getInstance()
							.getAllDownloadResourceIdMap();
					mListView.setAdapter(appTipsAdapter);
					appList = compareList((ArrayList<AppInfoBean>) msg.obj, localAppMap, downloadAppMap);
					appTipsAdapter.setList(appList);
					showTipsView();
					DataEyeManager.getInstance().source(
							StatisticConstan.SrcName.SEARCH,
							0,
							null,
							0L,
							null,
							DataEyeManager.getSearchKey(StatisticConstan.SearchType.SEARCH_ZAPP_APP + "-"
									+ currentInputText), true);
				}
				break;

			case SearchConstan.State.RESPONSE_RINGTONES_TIPS_SUCCESS:
				currentInputText = Utils.removeSpace(currentInputText);
				if (!TextUtils.isEmpty(currentInputText)) {
					musicList.clear();
					mListView.setAdapter(musicTipsAdapter);
					musicList.addAll((ArrayList<RingtonesBean>) msg.obj);
					musicTipsAdapter.setList(musicList);
					showTipsView();
				}
				break;

			case SearchConstan.State.RESPONSE_WALLPAPERS_TIPS_SUCCESS:
				currentInputText = Utils.removeSpace(currentInputText);
				if (!TextUtils.isEmpty(currentInputText)) {
					imageList.clear();
					mListView.setAdapter(imageTipsAdapter);
					imageList.addAll((ArrayList<WallpaperBean>) msg.obj);
					imageTipsAdapter.setList(imageList);
					showTipsView();
				}
				break;
			}
		}
	};

	/***
	 * =====================================注册广播================================
	 * ==
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
				String percent, localPath;
				View taskListItem;
				AppListViewHolder viewHolder;
				int silentInstallResult;
				switch (type) {
				case MyIntents.Types.COMPLETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANINSTALL);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.CANINSTALL, context, null);
					}
					break;

				case MyIntents.Types.DELETE:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.NORMAL);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.NORMAL, context, null);
					}
					break;

				case MyIntents.Types.COMPLETE_INSTALL:
					LogUtil.getLogger().d("handler Intent:COMPLETE_INSTALL");
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.CANLAUNCH);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.CANLAUNCH, context, null);
					}
					context.removeStickyBroadcast(new Intent(MyIntents.INTENT_UPDATE_UI));
					break;

				case MyIntents.Types.WAIT:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.WAITING);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.WAITING, context, percent);
					}
					break;

				case MyIntents.Types.PREDOWNLOAD:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, context, percent);
					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.PAUSED);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.PAUSED, context, percent);
					}
					break;
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					percent = intent.getStringExtra(MyIntents.PROCESS_PROGRESS);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, AppInfoBean.Status.DOWNLOADING);
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(AppInfoBean.Status.DOWNLOADING, context, percent);
					}

					break;
				case MyIntents.Types.MAKE_UPGRADE:
					notifyPatchUpgrade();
					break;
				case MyIntents.Types.MERGE_PATCH:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.appPauseView.setClickable(false);
					}
					break;
				case MyIntents.Types.INSTALLING:
					localPath = intent.getStringExtra(MyIntents.LOCAL_PATH);
					if (!TextUtils.isEmpty(localPath)) {
						url = DownloadEntityManager.getInstance().getOriginalUrlByLocalPath(localPath);
					}
					if (!TextUtils.isEmpty(url)) {

						taskListItem = mListView.findViewWithTag(url);
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

						taskListItem = mListView.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new AppListViewHolder(taskListItem);
						viewHolder.appPauseView.setClickable(false);

						viewHolder.refreshInstall();
						DownloadEntityManager.getInstance().updateDownloadInstallStatus(url);
						ToastUtil.show(context, R.string.toast_silent_install_failed, Toast.LENGTH_LONG);
						PackageUtil.normalInstall(context, localPath);
					}
					break;
				}
			}
		}
	}

	private void notifyAppDataChange(String url, int status) {
		if (mListView.getAdapter() == null) {
			return;
		}
		AppInfoBean appInfoBean = (AppInfoBean) mListView.getAdapter().getItem(0);
		if (appInfoBean.getUrl() != null && appInfoBean.getUrl().equals(url)) {
			appInfoBean.setStatus(status);
		}
	}

	private void notifyPatchUpgrade() {
		if (mListView.getAdapter() == null) {
			return;
		}
		HashMap<String, InstallAppBean> updateAppMap = UpdateManage.getInstance(context).findAllUpdateInstallAppMap();
		AppInfoBean appInfoBean = (AppInfoBean) mListView.getAdapter().getItem(0);
		if (appInfoBean != null && appInfoBean.getPackageName() != null) {
			String packageName = appInfoBean.getPackageName();
			InstallAppBean updateAppBean = updateAppMap.get(packageName);
			if (updateAppBean != null && updateAppBean.getIsPatch()) {
				appInfoBean.setPatch(true);
				appInfoBean.setUrl(updateAppBean.getUrl());
				appInfoBean.setPatchUrl(updateAppBean.getUrlPatch());
				appInfoBean.setPatchFileSize(updateAppBean.getPatchSize());

				// 增量更新 刷新显示增量大小
				View taskListItem = mListView.findViewWithTag(appInfoBean.getUrl());
				AppListViewHolder viewHolder = new AppListViewHolder(taskListItem);
				if (viewHolder != null) {
					viewHolder.refreshPatchUpdate(appInfoBean);
				}
			}
		}
	}

	private void registUiReceiver() {
		if (!inited) {
			mDownloadUiReceiver = new DownloadUiReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(MyIntents.INTENT_UPDATE_UI);
			BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);
			inited = true;
		}
	}

	private void unRegistUiReceiver() {
		if (mDownloadUiReceiver != null) {
			BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
			inited = false;
		}
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, mSearchIcon, SkinConstan.SEARCH_ICON_BG);
	}
}
