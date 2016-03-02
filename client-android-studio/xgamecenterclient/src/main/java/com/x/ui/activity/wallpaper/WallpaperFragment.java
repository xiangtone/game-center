package com.x.ui.activity.wallpaper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.publics.utils.ResourceUtil;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.view.TabPageIndicator;

/**
 * @ClassName: WallpaperFragment
 * @Desciption: TODO
 
 * @Date: 2014-3-13 下午2:20:16
 */

public class WallpaperFragment extends BaseFragment {

	public ViewPager mViewPager;
	private TabPageIndicator indicator;
	private WallpaperFragmentPagerAdapter wallpaperFragmentPagerAdapter;

	public static Fragment newInstance(Bundle bundle) {
		WallpaperFragment fragment = new WallpaperFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View converView = inflater.inflate(R.layout.fragment_home, null);
		initViewPager(converView);
		return converView;
	}

	private void initViewPager(View view) {
		String[] titles = { ResourceUtil.getString(mActivity, R.string.wallpaper_new),
				ResourceUtil.getString(mActivity, R.string.wallpaper_hot),
				ResourceUtil.getString(mActivity, R.string.wallpaper_categories) };
		wallpaperFragmentPagerAdapter = new WallpaperFragmentPagerAdapter(this.getChildFragmentManager(), titles);

		mViewPager = (ViewPager) view.findViewById(R.id.fh_content_pager);
		mViewPager.setAdapter(wallpaperFragmentPagerAdapter);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}
		});

		indicator = (TabPageIndicator) view.findViewById(R.id.fh_indicator);
		indicator.setViewPager(mViewPager);
		mViewPager.setCurrentItem(0); // 当前位置
	}

	public class WallpaperFragmentPagerAdapter extends FragmentPagerAdapter {

		String titles[];

		public WallpaperFragmentPagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.titles = titles;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = WallpaperCategoryNewFragment.newInstance(null);
				DataEyeManager.getInstance().module(ModuleName.WALLPAPER_NEW, true);
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.WALLPAPER_NEW, 0, null, 0L, null, null,
						false);
				break;
			case 1:
				fragment = WallpaperNewFragment.newInstance(null);
				break;
			case 2:
				fragment = WallpaperCategoriesFragment.newInstance(null);
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
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		indicator.notifyDataSetChanged();
	}

}
