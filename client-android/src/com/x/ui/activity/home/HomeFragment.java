/**   
 * @Title: HomeFragment2.java
 * @Package com.mas.amineappstore.activity
 * @Description: TODO 
 
 * @date 2014-2-11 上午09:44:54
 * @version V1.0   
 */

package com.x.ui.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.x.R;
import com.x.business.settings.PrefsManager;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.publics.utils.ResourceUtil;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.view.TabPageIndicator;

/**
 * @ClassName: HomeFragment2
 * @Description: TODO
 
 * @date 2014-2-11 上午09:44:54
 * 
 */

public class HomeFragment extends BaseFragment {

	public ViewPager mViewPager;
	private TabPageIndicator indicator;
	private HomeFragmentPagerAdapter homeFragmentPagerAdapter;

	public static Fragment newInstance(Bundle bundle) {
		HomeFragment fragment = new HomeFragment();
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
		String[] titles = { ResourceUtil.getString(mActivity, R.string.home_recommend),
				ResourceUtil.getString(mActivity, R.string.home_collection),
				ResourceUtil.getString(mActivity, R.string.home_must_have) };
		homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(this.getChildFragmentManager(), titles);

		mViewPager = (ViewPager) view.findViewById(R.id.fh_content_pager);
		mViewPager.setAdapter(homeFragmentPagerAdapter);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}
		});

		indicator = (TabPageIndicator) view.findViewById(R.id.fh_indicator);
		indicator.setViewPager(mViewPager);
		if (PrefsManager.getInstance().isHomeFirst(mActivity)) {
			mViewPager.setCurrentItem(2);
			DataEyeManager.getInstance().module(ModuleName.HOME_MUST_HAVE, true);
			DataEyeManager.getInstance().source(StatisticConstan.SrcName.HOME_MUST_HAVE, 0, null, 0L, null, null,false);
			PrefsManager.getInstance().setHomeFirst(mActivity, false);
		} else {
			mViewPager.setCurrentItem(0);
			DataEyeManager.getInstance().module(ModuleName.HOME_RECOMMEND, true);
			DataEyeManager.getInstance().source(StatisticConstan.SrcName.HOME_RECOMMEND, 0, null, 0L, null, null,false);
		}
	}

	public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

		String titles[];

		public HomeFragmentPagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.titles = titles;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = HomeRecommendFragment.newInstance(null);
//				DataEyeManager.getInstance().module(ModuleName.HOME_RECOMMEND, true);
//				DataEyeManager.getInstance().source(StatisticConstan.SrcName.HOME_RECOMMEND, 0, null, 0L, null, null,
//						false);
				break;
			case 1:
				fragment = HomeCollectionFragment.newInstance(null);
				break;
			case 2:
				fragment = HomeMustHaveFragment.newInstance(null);
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
