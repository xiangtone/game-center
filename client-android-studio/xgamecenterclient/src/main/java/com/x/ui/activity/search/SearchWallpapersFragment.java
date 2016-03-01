/**   
 * @Title: SearchWallpapersFragment.java
 * @Package com.x.ui.activity.search
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-7-11 下午5:05:55
 * @version V1.3   
 */

package com.x.ui.activity.search;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.x.R;
import com.nostra13.universalimageloader.core.assist.ImageType;
import com.x.business.search.SearchConstan;
import com.x.business.search.SearchManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.publics.http.model.KeywordsResponse;
import com.x.publics.model.KeywordBean;
import com.x.publics.model.WallpaperBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.NetworkImageUtils;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.TextUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.activity.wallpaper.WallpaperDetailActivity;
import com.x.ui.view.CustomSearchView.SearchAutoComplete;
import com.x.ui.view.autobreakview.AutoBreakAdapter;
import com.x.ui.view.autobreakview.AutoBreakListView;
import com.x.ui.view.autobreakview.AutoBreakOnItemClickListener;
import com.x.ui.view.pulltorefresh.PullToRefreshBase;
import com.x.ui.view.pulltorefresh.PullToRefreshScrollView;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.Mode;
import com.x.ui.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.x.ui.view.quiltview.RQuiltView;

/**
 * @ClassName: SearchWallpapersFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-11 下午5:05:56
 * 
 */

public class SearchWallpapersFragment extends BaseFragment {

	private int actionRc;
	private String action;
	private String lastQueryKey;
	private int listPageNum = 1;
	private int listPageSize = 12;
	private int keywordPageNum = 1;
	private boolean hasKeywordData = false;
	private boolean isVisibleToUser = false;

	private RQuiltView quiltView;
	private View loadingPb, loadingLogo;
	private SearchAutoComplete mQueryTextView;
	private PullToRefreshScrollView scrollView;
	private View keywordsView, errorView, loadingView;
	private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
	private List<KeywordBean> keywordList = new ArrayList<KeywordBean>();
	private ArrayList<WallpaperBean> imageList = new ArrayList<WallpaperBean>();

	// 自动换行
	private boolean isLast = false;
	private AutoBreakAdapter autoBreakAdapter;
	private AutoBreakListView autoBreakListView;

	public static Fragment newInstance(Bundle bundle) {
		SearchWallpapersFragment fragment = new SearchWallpapersFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	public int getAlbumId() {
		return SearchConstan.Album.WALLPAPERS_ALBUM_ID;
	}

	public int getRc() {
		return Constan.Rc.POST_WALLPAPER_SEARCH;
	}

	public void setParams() {
		if (SearchManager.getInstance().getInitState(mActivity)) {
			String queryKey = SearchManager.getInstance().getQueryKey(mActivity);
			if (!TextUtils.isEmpty(queryKey) && !queryKey.equals(lastQueryKey)) {
				listPageNum = 1;
				showLoadingView();
				lastQueryKey = queryKey;
				actionRc = getRc();
				action = SearchConstan.IMAGE_ACTION;
				getListData();
			}
		}
	}
	
	public void clearQueryKeyCache() {
		if (!TextUtils.isEmpty(lastQueryKey))
			lastQueryKey = null;
	}

	public void getListData() {
		SearchManager.getInstance().getQueryKeyData(mActivity, handler, actionRc, lastQueryKey, action, listPageSize,
				listPageNum);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search_wallpapers, null);
		// errorView = rootView.findViewById(R.id.e_error_rl);
		errorView = rootView.findViewById(R.id.ll_error_refresh);
		loadingView = rootView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		keywordsView = rootView.findViewById(R.id.ll_keywords_layout);
		quiltView = (RQuiltView) rootView.findViewById(R.id.quiltview);
		scrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.ptr_scrollview);
		scrollView.setOnRefreshListener(onRefreshListener);
		scrollView.setMode(Mode.DISABLED);
		scrollView.setVisibility(View.GONE);

		// 搜索关键字列表--autoBreakListView
		autoBreakAdapter = new AutoBreakAdapter(mActivity);
		autoBreakListView = (AutoBreakListView) rootView.findViewById(R.id.autoBreakView);

		// 获取搜索框
		mQueryTextView = (SearchAutoComplete) getActivity().findViewById(R.id.search_src_text);

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
				mQueryTextView.setText(lastQueryKey);

				// 保存搜索关键字
				showLoadingView();
				SearchManager.getInstance().setInitState(mActivity, true);
				SearchManager.getInstance().setQueryKey(mActivity, lastQueryKey);
				if (actionRc == Constan.Rc.KEYWORDS_WALLPAPER_LIST) {
					DataEyeManager.getInstance().source(
							StatisticConstan.SrcName.SEARCH,
							0,
							null,
							0L,
							null,
							DataEyeManager.getSearchKey(StatisticConstan.SearchType.SEARCH_WALLPAPER_LIST + "-"
									+ lastQueryKey), true);
				}
				getListData();
			}
		});

		SearchManager.getInstance().getKwData(mActivity, handler, getAlbumId(), keywordPageNum);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
	}

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SearchConstan.State.SEARCH_KEYWORDS_SUCCESS:
				hasKeywordData = true;
				KeywordsResponse keywordsResponse = (KeywordsResponse) msg.obj;
				List<KeywordBean> responseList = keywordsResponse.keywordlist;
				keywordList.clear();

				// 将refresh排在第一位
				KeywordBean been = new KeywordBean();
				been.setKeyword(ResourceUtil.getString(mActivity, R.string.search_refresh_text));
				keywordList.add(been);
				for (KeywordBean keywordBean : responseList) {
					keywordList.add(keywordBean);
				}

				autoBreakAdapter.setAnim(); // 设置动画显示
				autoBreakAdapter.setList(keywordList);
				isLast = keywordsResponse.isLast;
				showKeywordsView();
				break;

			case SearchConstan.State.RESPONSE_IMAGE_DATA_SUCCESS:
				String mText = Utils.removeSpace(mQueryTextView.getText().toString());
				if (!TextUtils.isEmpty(mText)) {
					scrollView.onRefreshComplete();
					Bundle bundle = msg.getData();
					isLast = bundle.getBoolean("isLast");
					boolean isRecommend = bundle.getBoolean("isRecommend");
					ArrayList<WallpaperBean> list = bundle.getParcelableArrayList("imageList");
					// 是否推荐数据
					if (isRecommend) {
						imageList = list;
					} else {
						imageList.addAll(list);
					}
					// 是否存在分页
					if (isLast || isRecommend) {
						scrollView.setMode(Mode.DISABLED);
					} else {
						scrollView.setMode(Mode.PULL_FROM_END);
					}

					// 添加图片元素到容器中
					addItemToContainer();
				}
				break;

			case SearchConstan.State.SEARCH_KEYWORDS_FAILURE:
			case SearchConstan.State.RESPONSE_IMAGE_DATA_FAILURE:
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

			case 23:
				quiltView.addPatchImages((ArrayList<ImageView>) msg.obj, mActivity);
				showDataListView();
				break;

			case SearchConstan.State.REFRESH_DATA_SUCCESS:
				refreshData();
				break;
			}
		}
	};

	public void showKeywordsView() {
		if (hasKeywordData) {
			errorView.setVisibility(View.GONE);
			scrollView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			keywordsView.setVisibility(View.VISIBLE);
			SearchManager.getInstance().putCurValue(SearchConstan.WALLPAPER_FRAGMENT_ID, true);
		}
	}

	private void showDataListView() {
		errorView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		keywordsView.setVisibility(View.GONE);
		scrollView.setVisibility(View.VISIBLE);
		SearchManager.getInstance().putCurValue(SearchConstan.WALLPAPER_FRAGMENT_ID, false);
	}

	private void showLoadingView() {
		quiltView.clear(); // 清空界面元素
		imageList.clear(); // 清空数据集合
		errorView.setVisibility(View.GONE);
		scrollView.setVisibility(View.GONE);
		keywordsView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		SearchManager.getInstance().putCurValue(SearchConstan.WALLPAPER_FRAGMENT_ID, false);
	}

	private void showErrorView() {
		scrollView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		keywordsView.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
		SearchManager.getInstance().putCurValue(2, false);
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
				SearchManager.getInstance().getKwData(mActivity, handler, getAlbumId(), keywordPageNum);
			}
		});
	}

	/**
	 * 将图片元素到集合中
	 */
	private void addItemToContainer() {
		int size = imageList.size();
		imageViews.clear(); // 必须先清空，否则报错..
		for (int i = (listPageNum - 1) * listPageSize; i < listPageSize * listPageNum && i < size; i++) {
			imageViews.add(addImage(imageList.get(i), i));
		}
		quiltView.addPatchImages(imageViews, mActivity);
		loadingView.setVisibility(View.GONE);
		keywordsView.setVisibility(View.GONE);
		scrollView.setVisibility(View.VISIBLE);
	}

	/**
	 * 添加图片
	 * 
	 * @param imageUrl
	 * @param columnIndex
	 */
	private ImageView addImage(final WallpaperBean wallpaperBean, final int index) {
		final ImageView imageView = (ImageView) LayoutInflater.from(mActivity).inflate(R.layout.quiltview_item, null);
		// 加载图片资源（中图）
		NetworkImageUtils.load(mActivity, ImageType.NETWORK, wallpaperBean.getBiglogo(),
				R.drawable.banner_default_picture, R.drawable.banner_default_picture, imageView);
		// 按钮触摸事件
		imageView.setOnTouchListener(new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					imageView.setAlpha(255);
					Intent intent = new Intent(mActivity, WallpaperDetailActivity.class);
					intent.putParcelableArrayListExtra("data", imageList);
					intent.putExtra("picIndex", index);
					intent.putExtra("searchKey", lastQueryKey);
					mActivity.startActivity(intent);
					break;

				case MotionEvent.ACTION_MOVE:
					// imageView.setAlpha(255);
					break;

				case MotionEvent.ACTION_DOWN:
					imageView.setAlpha(180);
					break;

				case MotionEvent.ACTION_CANCEL:
					imageView.setAlpha(255);
					break;
				}
				return true;
			}
		});
		return imageView;
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
			DataEyeManager.getInstance().module(StatisticConstan.ModuleName.SEARCH_WALLPAPER, isVisibleToUser);
		}
	}

	/**
	 * 下拉刷新、上拉加载更多数据。。
	 */
	private OnRefreshListener2<ScrollView> onRefreshListener = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
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
