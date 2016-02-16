package com.x.ui.activity.ringtones;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
 * 
* @ClassName: RingtonesFragment
* @Description: 铃声主界面

* @date 2014-4-8 上午10:46:32
*
 */
public class RingtonesFragment extends BaseFragment {

	public ViewPager mViewPager;
	private TabPageIndicator indicator;
	private RingtonesFragmentPagerAdapter tingtonesFragmentPagerAdapter;

	public static Fragment newInstance(Bundle bundle) {
		RingtonesFragment fragment = new RingtonesFragment();
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
		String[] titles = { ResourceUtil.getString(mActivity, R.string.ringtone_new),
				ResourceUtil.getString(mActivity, R.string.ringtone_hot),

				ResourceUtil.getString(mActivity, R.string.ringtone_categories) };
		tingtonesFragmentPagerAdapter = new RingtonesFragmentPagerAdapter(this.getChildFragmentManager(), titles);

		mViewPager = (ViewPager) view.findViewById(R.id.fh_content_pager);
		mViewPager.setAdapter(tingtonesFragmentPagerAdapter);
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				
			}
		});

		indicator = (TabPageIndicator) view.findViewById(R.id.fh_indicator);
		indicator.setViewPager(mViewPager);
		mViewPager.setCurrentItem(0);
	}

	public class RingtonesFragmentPagerAdapter extends FragmentPagerAdapter {

		String titles[];

		public RingtonesFragmentPagerAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			this.titles = titles;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = RingtonesNewFragment.newInstance(null);
				DataEyeManager.getInstance().module(ModuleName.RINGTONE_NEW, true);
				DataEyeManager.getInstance().source(StatisticConstan.SrcName.RINGTONE_NEW, 0, null, 0L, null, null,
						false);
				break;
			case 1:
				fragment = RingtonesHotFragment.newInstance(null);

				break;
			case 2:
				fragment = RingtonesCategoriesFragment.newInstance(null);
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
