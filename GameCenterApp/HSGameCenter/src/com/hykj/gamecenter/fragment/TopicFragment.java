
package com.hykj.gamecenter.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.activity.HomePageActivity;
import com.hykj.gamecenter.activity.SearchActivity;
import com.hykj.gamecenter.activity.SettingListActivity;
import com.hykj.gamecenter.controller.ProtocolListener;
import com.hykj.gamecenter.protocol.Reported;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.ui.SearchBarCommon;
import com.hykj.gamecenter.ui.widget.PagerSlidingTabStrip;
import com.hykj.gamecenter.utils.DateUtil;
import com.hykj.gamecenter.utils.Interface.IFragmentInfo;
import com.hykj.gamecenter.utils.UpdateUtils;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TopicFragment extends Fragment implements IFragmentInfo,
		OnPageChangeListener {
	private static final String TAG = "TopicAppFragment";
	private View mainView;

	private Context mContext;
	private Activity mActivity;
	TopicAppsFragment mAppFragment = null;
	TopicGamesFragment mGameFragment = null;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	PagerSlidingTabStrip mpsTabStrip = null;
	private static final int DEFAULT_OFFSCREEN_PAGES = 2;
	private final int mCurrentTab = 0;

	public static final int APP_TAB_INDEX = 0;
	public static final int GAME_TAB_INDEX = 1;
	private final ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			/*
			 * case Msg.AAA: break;
             */
				default:
					break;
			}

		}
	};

	private SearchBarCommon mSearchBarCommon;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		if (activity != null)
			((HomePageActivity) activity).setmTopicAppFragment(this);
	}

	/*
	 * 当使用Fragment去嵌套另外一些子Fragment的时候，我们需要去管理子Fragment，
	 * 这时候需要调用ChildFragmentManager去管理这些子Fragment，由此可能产生的Exception主要是：
	 * java.lang.IllegalStateException: No activity
	 *
	 * 首先我们来分析一下Exception出现的原因：
	 * 通过DEBUG发现，当第一次从一个Activity启动Fragment，然后再去启动子Fragment的时候
	 * ，存在指向Activity的变量，但当退出这些Fragment之后回到Activity
	 * ，然后再进入Fragment的时候，这个变量变成null，这就很容易明了为什么抛出的异常是No activity
	 *
	 * 这个Exception是由什么原因造成的呢？如果想知道造成异常的原因，那就必须去看Fragment的相关代码，
	 * 发现Fragment在detached之后都会被reset掉
	 * ，但是它并没有对ChildFragmentManager做reset，所以会造成ChildFragmentManager的状态错误。
	 *
	 * 找到异常出现的原因后就可以很容易的去解决问题了，我们需要在Fragment被detached的时候去重置ChildFragmentManager
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * 如果该Fragment中去实例化了一些子Fragment，在子Fragment中去有返回的启动了另外一个Activity，
	 * 即通过startActivityForResult方式去启动
	 * ，这时候造成的现象会是，子Fragment接收不到OnActivityResult，如果在子Fragment中是以getActivity
	 * .startActivityForResult方式启动
	 * ，那么只有Activity会接收到OnActivityResult，如果是以getParentFragment
	 * .startActivityForResult方式启动
	 * ，那么只有父Fragment能接收（此时Activity也能接收），但无论如何子Fragment接收不到OnActivityResult。
	 * 这是个问题
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getSaveState(savedInstanceState);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mContext = getActivity();
		mActivity = getActivity();
		// TODO Auto-generated method stub
		LogUtils.d("owenli onCreateView");
		mainView = inflater.inflate(R.layout.fragment_topic_tab, null);
		initView();
		return mainView;
	}

	public void initView() {
		mSearchBarCommon = (SearchBarCommon)mainView.findViewById(R.id.searchBarCommon);
		mSearchBarCommon.setSearchBarCommonListen(new SearchBarCommon.SearchBarCommonListen() {
			@Override
			public void clickSearch(View view) {
				//进入search界面进行上报
				// 上报进入个人中心
				Reported.ReportedInfo reportBuilder = new Reported.ReportedInfo();
				reportBuilder.statActId = ReportConstants.STATACT_ID_PAGE_VISIT;
				reportBuilder.statActId2 = 5;//搜索
				ReportConstants.getInstance().reportReportedInfo(reportBuilder);

				Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
				intentSearch.putExtra(ProtocolListener.KEY.MAIN_TYPE, ProtocolListener.MAIN_TYPE./*GAME_CLASS*/ALL);
				intentSearch.putExtra(ProtocolListener.KEY.SUB_TYPE, ProtocolListener.SUB_TYPE.ALL);
				intentSearch.putExtra(ProtocolListener.KEY.ORDERBY, ProtocolListener.ORDER_BY.DOWNLOAD);
				startActivity(intentSearch);
			}

			@Override
			public void clickSettingImage(ImageView imageView) {
				Intent intentSetting = new Intent(getActivity(),
						SettingListActivity.class);
				startActivity(intentSetting);
			}
		});

		mViewPager = (ViewPager) mainView.findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(DEFAULT_OFFSCREEN_PAGES);
		mpsTabStrip = (PagerSlidingTabStrip) mainView
				.findViewById(R.id.tab_strip);
		// fragment切换时的效果 翻页效果
		mViewPager.setPageTransformer(true, new DepthPageTransformer());
		initFragment();
		mViewPager.setAdapter(mTabsAdapter);
		mViewPager.setCurrentItem(mCurrentTab);

		mpsTabStrip.setTabPaddingLeftRight(0);
		mpsTabStrip.setViewPager(mViewPager);
		mpsTabStrip.setOnPageChangeListener(this);
		// 自定义tab的样式
		mpsTabStrip.setUnderlineColorResource(R.color.transparent);
		mpsTabStrip.setIndicatorColorResource(R.color.transparent);
		mpsTabStrip.setDividerColorResource(R.color.transparent);
//		mpsTabStrip.setIndicatorColorResource(R.color.normal_blue);
//		int hight = getResources( ).getDimensionPixelSize( R.dimen.csl_tab_actionbar_divider );
//		mpsTabStrip.setDividerPadding(hight);
		mpsTabStrip.setTextSize(getResources().getDimensionPixelSize(
				R.dimen.text_size_middle));

		// mTabsAdapter = new TabsAdapter( this , mViewPager );
		// mTabsAdapter.addTab( bar.newTab( ).setText( R.string.tab_category ) ,
		// FileCategoryActivity.class , null );
		// mTabsAdapter.addTab( bar.newTab( ).setText( R.string.tab_sd ) ,
		// FileViewActivity.class , null );
		// mTabsAdapter.addTab( bar.newTab( ).setText( R.string.tab_remote ) ,
		// ServerControlActivity.class , null );

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	private void getSaveState(Bundle savedInstanceState) {
		if (null != savedInstanceState) {
		}
	}

	@Override
	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		LogUtils.d("onConfigurationChanged() ");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onResume() {
		LogUtils.d("owenli onConfigurationChanged() ");
		// TODO Auto-generated method stub
		super.onResume();
		mSearchBarCommon.setSettingTipVisible(UpdateUtils.hasUpdate() ? View.VISIBLE
				: View.GONE);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestory");
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

	}

	public Handler getHandler() {
		return mHandler;
	}

	@Override
	public String getFragmentTabLabel() {
		// TODO Auto-generated method stub
		return IFragmentInfo.FragmentTabLabel.TOPIC_LABEL;
	}

	/**
	 * 初始化Fragment
	 *
	 * @param
	 */
	private void initFragment() {

		if (mAppFragment == null && !App.ismAllGame()) {
			mAppFragment = new TopicAppsFragment();
		}
		if (mGameFragment == null) {
			mGameFragment = new TopicGamesFragment();
		}

		if (mAppFragment != null) {
			mFragmentList.add(mAppFragment);
			mpsTabStrip.setVisibility(View.VISIBLE);

		}else {
			mpsTabStrip.setVisibility(View.GONE);
		}
		mFragmentList.add(mGameFragment);

		if (mTabsAdapter == null) {

            /*
			 * 注意这里是getChildFragmentManager()不是getFragmentManager()
             * 因为当前fragment要管理嵌套的fragment,而不是Activity管理fragment
             */
			mTabsAdapter = new TabsAdapter(getChildFragmentManager(),
					mFragmentList);
		}
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */
	public Fragment getCurrentFragment() {
		if (mTabsAdapter != null) {
			return mTabsAdapter.getItem(mViewPager.getCurrentItem());
		}
		return null;
	}

	public Fragment getFragment(int tabIndex) {
		if (mTabsAdapter != null) {
			return mTabsAdapter.getItem(tabIndex);
		}
		return null;
	}

	public class TabsAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.LayoutTabProvider {

		private List<Fragment> fragmentList = new ArrayList<Fragment>();

		public TabsAdapter(FragmentManager fm, List<Fragment> fragmentList) {
			super(fm);
			this.fragmentList = fragmentList;
		}

		@Override
		public int getCount() {

			return fragmentList.size();
		}

		@Override
		public Fragment getItem(int position) {

			return fragmentList.get(position);

		}

		@Override
		public CharSequence getPageTitle(int position) {
			// Locale l = Locale.getDefault( );
			switch (position) {
				case APP_TAB_INDEX:
					return getString(R.string.apps);
				case GAME_TAB_INDEX:
					return getString(R.string.games);
			}
			return null;
		}

		@Override
		public int getPageLayoutResId(int position) {
			switch (position) {
				case 0:
					return R.layout.topic_tab_item_left;
				case 1:
					return R.layout.topic_tab_item_right;
			}
			return 0;
		}

        /*
         * @Override public int getPageLayoutResId(int position) { switch
         * (position) { case 0: return R.layout.classify_manage_tab_item_left;
         * case 1: return R.layout.classify_manage_tab_item_right; } return 0; }
         */
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	// 切换页面的时候
	@Override
	public void onPageSelected(int index) {
		Log.d(TAG, "index : " + index);
		switch (index) {
			case APP_TAB_INDEX:

				break;
			case GAME_TAB_INDEX:

				break;
		}

		// mCurrentPager = index;

	}

	public void setCurrentPage(int page) {
		mViewPager.setCurrentItem(page, true);
	}

	public int getCurrentPage() {
		return mViewPager.getCurrentItem();
	}

	public static abstract class OnTapListener implements OnTouchListener {
		private float mLastTouchX;
		private float mLastTouchY;
		private long mLastTouchTime;
		private final TextView mMakePressedTextView;
		private final int mPressedColor, mGrayColor;
		private final float MAX_MOVEMENT_ALLOWED = 20;
		private final long MAX_TIME_ALLOWED = 500;

		public OnTapListener(Activity activity, TextView makePressedView) {
			mMakePressedTextView = makePressedView;
			mPressedColor = activity.getResources().getColor(R.color.color_red);
			mGrayColor = activity.getResources().getColor(R.color.color_green_normal);
		}

		@Override
		public boolean onTouch(View v, MotionEvent e) {
			switch (e.getAction()) {
				case (MotionEvent.ACTION_DOWN):// 按下
					mLastTouchTime = DateUtil.getTimeNow();
					mLastTouchX = e.getX();
					mLastTouchY = e.getY();
					if (mMakePressedTextView != null) {
						mMakePressedTextView.setTextColor(mPressedColor);
					}
					break;
				case (MotionEvent.ACTION_UP):// 抬起
					float xDiff = Math.abs(e.getX() - mLastTouchX);
					float yDiff = Math.abs(e.getY() - mLastTouchY);
					long timeDiff = (DateUtil.getTimeNow() - mLastTouchTime);
					if (xDiff < MAX_MOVEMENT_ALLOWED
							&& yDiff < MAX_MOVEMENT_ALLOWED
							&& timeDiff < MAX_TIME_ALLOWED) {
						if (mMakePressedTextView != null) {
							v = mMakePressedTextView;
						}
						processClick(v);
						resetValues();
						return true;
					}
					resetValues();
					break;
				case (MotionEvent.ACTION_MOVE):
					xDiff = Math.abs(e.getX() - mLastTouchX);
					yDiff = Math.abs(e.getY() - mLastTouchY);
					if (xDiff >= MAX_MOVEMENT_ALLOWED
							|| yDiff >= MAX_MOVEMENT_ALLOWED) {
						resetValues();
					}
					break;
				default:
					resetValues();
			}
			return false;
		}

		private void resetValues() {
			mLastTouchX = -1 * MAX_MOVEMENT_ALLOWED + 1;
			mLastTouchY = -1 * MAX_MOVEMENT_ALLOWED + 1;
			mLastTouchTime = -1 * MAX_TIME_ALLOWED + 1;
			if (mMakePressedTextView != null) {
				mMakePressedTextView.setTextColor(mGrayColor);
			}
		}

		protected abstract void processClick(View v);
	}

	public class DepthPageTransformer implements PageTransformer {
		@SuppressLint("NewApi")
		@Override
        /*
         * 关键是要理解transformPage(View view, float position)的参数。
         * view理所当然就是滑动中的那个view，position这里是float类型， 不是平时理解的int位置，而是当前滑动状态的一个表示，
         * 比如当滑动到正全屏时，position是0， 而向左滑动，使得右边刚好有一部被进入屏幕时，
         * position是1，如果前一页和下一页基本各在屏幕占一半时， 前一页的position是-0.5，后一页的position是0.5，
         * 所以根据position的值我们就可以自行设置需要的alpha，x/y信息。
         * 
         * 向左滑动时，（开始时左边全屏，右边看不见）左边 0--> -1 ,右边 1--> 0
         */
		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			int halfPageWidth = pageWidth >> 1;
			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);
			} else if (position <= 0) { // [-1,0]
				// Use the default slide transition when
				// moving to the left page
				view.setAlpha(1);
				view.setTranslationX(0);
				view.setScaleX(1);
				view.setScaleY(1);
			} else if (position <= 1) { // (0,1]
				// Fade the page out.
				view.setAlpha(1 - position);
				// Counteract the default slide transition
				// view.setTranslationX( pageWidth * -position );
				view.setTranslationX(-(halfPageWidth * position));// 如：向左滑动时相对与左边(即前一页)的位置(会被左边遮盖的宽度)
				// Scale the page down (between MIN_SCALE and 1)
				// float scaleFactor = MIN_SCALE + ( 1 - MIN_SCALE ) * ( 1 -
				// Math.abs( position ) );
				// view.setScaleX( scaleFactor );
				// view.setScaleY( scaleFactor );
			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}

	}
}
