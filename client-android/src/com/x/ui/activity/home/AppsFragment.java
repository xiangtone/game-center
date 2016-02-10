/**   
* @Title: AppsFragment2.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-2-13 下午04:53:45
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
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.publics.utils.ResourceUtil;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.view.TabPageIndicator;

/**
* @ClassName: AppsFragment2
* @Description: TODO 

* @date 2014-2-13 下午04:53:45
* 
*/

public class AppsFragment extends BaseFragment {

	public ViewPager mViewPager;
	private TabPageIndicator indicator;
	private AppsFragmentPagerAdapter appsFragmentPagerAdapter;

	public static Fragment newInstance(Bundle bundle) {
		AppsFragment fragment = new AppsFragment();
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
		String[] titles = { ResourceUtil.getString(mActivity, R.string.app_new),
				ResourceUtil.getString(mActivity, R.string.app_hot),
				ResourceUtil.getString(mActivity, R.string.app_categories) };
		appsFragmentPagerAdapter = new AppsFragmentPagerAdapter(this.getChildFragmentManager(), titles);

		mViewPager = (ViewPager) view.findViewById(R.id.fh_content_pager);
		mViewPager.setAdapter(appsFragmentPagerAdapter);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}
		});

		indicator = (TabPageIndicator) view.findViewById(R.id.fh_indicator);
		indicator.setViewPager(mViewPager);
		mViewPager.setCurrentItem(0);
	}

	public class AppsFragmentPagerAdapter extends FragmentPagerAdapter {

		String titles[];

		public AppsFragmentPagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.titles = titles;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = AppsNewFragment.newInstance(null);
				DataEyeManager.getInstance().module(ModuleName.APPS_NEW, true);
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.APPS_NEW, 0, null, 0L, null, null, false);

				break;
			case 1:
				fragment = AppsHotFragment.newInstance(null);
				break;
			case 2:
				fragment = AppsCategoriesFragment.newInstance(null);
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
