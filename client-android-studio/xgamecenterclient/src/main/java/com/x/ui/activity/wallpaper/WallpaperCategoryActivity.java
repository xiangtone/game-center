package com.x.ui.activity.wallpaper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.publics.utils.ResourceUtil;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.TabPageIndicator;

/**
 * @ClassName: WallpaperCategoryActivity
 * @Desciption: 壁纸分类
 
 * @Date: 2014-3-26 下午4:55:59
 */

public class WallpaperCategoryActivity extends BaseActivity implements View.OnClickListener {

	private ViewPager mViewPager;
	private Context context = this;
	private TabPageIndicator indicator;
	private SectionsPagerAdapter mSectionsPagerAdapter;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper_categories);
		initTitle();
		initViewPager();
	}

	private void initTitle() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mNavigationView.setOnClickListener(this);

		mTitleTv.setText(getIntent().getStringExtra("categoryName"));
	}

	private void initViewPager() {
		String[] titles = { ResourceUtil.getString(this, R.string.app_hot),
				ResourceUtil.getString(this, R.string.app_new) };
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles);

		mViewPager = (ViewPager) findViewById(R.id.aam_content_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}
		});

		indicator = (TabPageIndicator) findViewById(R.id.aam_indicator);
		indicator.setViewPager(mViewPager);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		String[] titles;

		public SectionsPagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.titles = titles;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = WallpaperCategoryHotFragment.newInstance(null);
				break;
			case 1:

				fragment = WallpaperCategoryNewFragment.newInstance(null);
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
			// TODO Auto-generated method stub
			return PagerAdapter.POSITION_NONE;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSkinTheme();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
