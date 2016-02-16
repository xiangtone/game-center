/**   
* @Title: MyAppsActivity.java
* @Package com.x.ui.activity
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-7 上午10:24:09
* @version V1.0   
*/

package com.x.ui.activity.myApps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.view.TabPageIndicator;

/**
* @ClassName: MyAppsActivity
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-7-7 上午10:24:09
* 
*/

public class MyAppsActivity extends BaseActivity implements OnClickListener {

	private static final int ADD_NEW_TAB = 1;
	private static final int DELETE_NEW_TAB = 2;
	private Context context = this;
	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private TabPageIndicator indicator;

	private BroadcastReceiver mDownloadUiReceiver;
	private boolean inited = false;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_my_apps);
		setContentView(R.layout.activity_my_apps);
		initViewPager();
		initNavigation();
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		if (!inited)
			registDownloadUiReceiver();

		onShow(true);
	}

	private void initViewPager() {
		if (isNewTabCanVisviable()) {
			String[] titles = { ResourceUtil.getString(context, R.string.my_apps_new),
					ResourceUtil.getString(context, R.string.my_apps_all) };
			mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles);
		} else {
			String[] titles = { ResourceUtil.getString(context, R.string.my_apps_all) };
			mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles);
		}

		mViewPager = (ViewPager) findViewById(R.id.ama_content_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}
		});

		indicator = (TabPageIndicator) findViewById(R.id.ama_indicator);
		indicator.setViewPager(mViewPager);
	}

	private void registDownloadUiReceiver() {
		mDownloadUiReceiver = new DownloadUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(mDownloadUiReceiver, filter);
		inited = true;
	}

	private void unregistDownloadUiReceiver() {
		BroadcastManager.unregisterReceiver(mDownloadUiReceiver);
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
			if (getCount() > 1) {
				switch (position) {
				case 0:
					fragment = MyAppsNewFragment.newInstance(null);
					//					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.MYAPPS_NEW, true) ;
					break;
				case 1:

					fragment = MyAppsAllFragment.newInstance(null);
					break;
				}
			} else {
				switch (position) {
				case 0:
					fragment = MyAppsAllFragment.newInstance(null);
					//					DataEyeManager.getInstance().module(StatisticConstan.ModuleName.MYAPPS_ALL, true) ;
					break;
				}
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

	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				switch (type) {
				case MyIntents.Types.COMPLETE_INSTALL:
				case MyIntents.Types.DELETE:
				case MyIntents.Types.ADD:
					tabHandler.sendEmptyMessage(0);
					break;
				default:
					break;
				}

			}
		}

	}

	private Handler tabHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Utils.executeAsyncTask(new NotifyTabTipsTask());
				break;
			case 1:
				initViewPager();
				break;
			}
		}
	};

	private class NotifyTabTipsTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			boolean isNewTabCanVisviable = isNewTabCanVisviable();
			int currentTabCount = mSectionsPagerAdapter.getCount();
			if (currentTabCount <= 1 && isNewTabCanVisviable) { //添加NEW tab
				return ADD_NEW_TAB;
			} else if (currentTabCount > 1 && !isNewTabCanVisviable) {//删除NEW tab
				return DELETE_NEW_TAB;
			}
			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == ADD_NEW_TAB) {
				String[] titles = { ResourceUtil.getString(context, R.string.my_apps_new),
						ResourceUtil.getString(context, R.string.my_apps_all) };
				mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles);
				mViewPager.setAdapter(mSectionsPagerAdapter);
				mSectionsPagerAdapter.setTitles(titles);
			} else if (result == DELETE_NEW_TAB) {
				String[] titles = { ResourceUtil.getString(context, R.string.my_apps_all) };
				mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles);
				mViewPager.setAdapter(mSectionsPagerAdapter);
				mSectionsPagerAdapter.setTitles(titles);
			}
		}
	}

	private boolean isNewTabCanVisviable() {
		int updateCount = UpdateManage.getInstance(context).getUpdateAppSize();
		int downloadCount = DownloadEntityManager.getInstance().getAllUnCompleteAppsDownloadCount();
		return (updateCount + downloadCount) > 0;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregistDownloadUiReceiver();
	}

	@Override
	public void onPause() {
		super.onPause();
		onShow(false);
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
		if (show) {
			DataEyeManager.getInstance().source(StatisticConstan.SrcName.MY_APPS, 0, null, 0L, null, null, false);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;

		default:
			break;
		}

	}

	/**
	* @Title: initNavigation 
	* @Description: 初始化导航栏 
	* @param     
	* @return void
	 */
	private void initNavigation() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.page_my_apps);
		mNavigationView.setOnClickListener(this);
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
