/**   
 * @Title: ApplockerMainActivity.java
 * @Package com.x.ui.activity.applocker
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-10-9 上午9:49:57
 * @version V1.0   
 */

package com.x.ui.activity.applocker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.TabPageIndicator;

/**
 * @ClassName: ApplockerMainActivity
 * @Description: 应用锁管理控制界面
 
 * @date 2014-10-9 上午9:49:57
 * 
 */

public class AppCommonLockerActivity extends BaseActivity implements View.OnClickListener {

	private Context context = this;
	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private TabPageIndicator indicator;
	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_applocker);
		initTitle();
		initViewPager();
	}

	private void initTitle() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mNavigationView.setOnClickListener(this);

		mTitleTv.setText(R.string.page_applocker_wish_to_lock);
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		// onShow(true);
	}

	/* 初始化页面 */
	private void initViewPager() {
		// 设置页面NEW标题
		String[] titles = { "" };
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles);

		mViewPager = (ViewPager) findViewById(R.id.ama_content_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(1);
		/*
		 * mViewPager.setOnPageChangeListener(new
		 * ViewPager.SimpleOnPageChangeListener() {
		 * 
		 * @Override public void onPageSelected(int position) { } });
		 */
		// indicator = (TabPageIndicator) findViewById(R.id.ama_indicator);
		// indicator.setViewPager(mViewPager);

	}

	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		String[] titles;

		public SectionsPagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.titles = titles;
			notifyDataSetChanged();
		}

		private void setTitles(String[] titles) {
			this.titles = titles;
			indicator.notifyDataSetChanged();
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = LockerFragment.newInstance(null);
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		// onShow(false);
	}

	private void onShow(boolean show) {
		int item = mViewPager.getCurrentItem();
		switch (item) {
		case 0:
			DataEyeManager.getInstance().module(ModuleName.MYAPPS_NEW, show);
			break;
		case 1:
			DataEyeManager.getInstance().module(ModuleName.MYAPPS_ALL, show);
			break;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
	}

}
