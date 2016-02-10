/**   
* @Title: ManagementActivity.java
* @Package com.x.activity
* @Description: TODO 

* @date 2013-12-16 下午02:57:57
* @version V1.0   
*/

package com.x.ui.activity.downloadman;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.x.R;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.http.model.AppsUpgradeResponse;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.view.TabPageIndicator;

/**
* @ClassName: ManagementActivity
* @Description: TODO 

* @date 2013-12-16 下午02:57:57
* 
*/

public class DownloadManagementActivity extends BaseActivity {

	private Context context;
	private List<String> titleList = new ArrayList<String>(2);
	private String downloadingTips, finishedTips;
	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private UiReceiver uiReceiver;
	//	private AutoDownloadErrorPausedReceiver autoDownloadErrorPausedReceiver;
	private boolean inited;
	private AppsUpgradeResponse upgradeResponse;
	private int updateNum;
	private TabPageIndicator indicator;

	private int finishCount, unFinishCount;

	private Button lucencyBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_app_management);
		initUi();
	}
	@Override
	public void onPause() {
		super.onPause();
		lucencyBtn.setVisibility(View.VISIBLE);
	}
	@Override
	protected void onResume() {
		super.onResume();
		lucencyBtn.setVisibility(View.GONE);
		refreshTab();
		if (!inited)
			registBroadReceiver();
		if (getIntent() != null && getIntent().getIntExtra("tabNum", -1) != -1) {
			mViewPager.setCurrentItem(1);
		}
	}

	private void initUi() {
		initViewPager();
		setTabTitle(R.string.page_download_management);
	}

	private void refreshTab() {
		Utils.executeAsyncTask(new getAsyncDownloadTask());
	}

	private class getAsyncDownloadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			initTabTitleData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			refreshTabTitle();
		}

	}

	private void initTabTitleData() {
		finishCount = DownloadEntityManager.getInstance().getAllFinishedDownloadCount();
		unFinishCount = DownloadEntityManager.getInstance().getAllUnfinishedDownloadCount();

		downloadingTips =ResourceUtil.getString(context, R.string.downloading,""+unFinishCount);
		finishedTips =ResourceUtil.getString(context, R.string.download_history,""+finishCount) ;
		titleList.clear();
		titleList.add(downloadingTips);
		titleList.add(finishedTips);
	}

	private void refreshTabTitle() {
		indicator.notifyDataSetChanged();
	}

	@SuppressLint("NewApi")
	private void initViewPager() {
		lucencyBtn = (Button) findViewById(R.id.act_app_btn);
		
		downloadingTips = ResourceUtil.getString(context, R.string.downloading,"0");
		finishedTips = ResourceUtil.getString(context, R.string.download_history,"0");
		titleList.add(downloadingTips);
		titleList.add(finishedTips);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titleList);

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
				fragment = AppsDownloadingFragment.newInstance(getIntent().getExtras());
				break;
			case 1:
				fragment = AppsDownloadHistoryFragment.newInstance(null);
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

	public class UiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				switch (type) {
				case MyIntents.Types.ADD:
				case MyIntents.Types.COMPLETE:
				case MyIntents.Types.COMPLETE_INSTALL:
				case MyIntents.Types.DELETE:
				case MyIntents.Types.DELETE_ALL_DOWNLOADING:
				case MyIntents.Types.DELETE_ALL_HISTORY:
					refreshTab();
					break;

				default:
					break;
				}

			}
		}
	}

	/*public class AutoDownloadErrorPausedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_AUTO_DOWNLOAD_ERRORPAUSED)) {
				DownloadManager.getInstance().autoContinueDownload(context);
			}
		}
	}*/

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregistBroadReceiver();
	}

	private void registBroadReceiver() {
		uiReceiver = new UiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(uiReceiver, filter);
		//		autoDownloadErrorPausedReceiver = new AutoDownloadErrorPausedReceiver();
		//		IntentFilter filter2 = new IntentFilter();
		//		filter2.addAction(MyIntents.INTENT_AUTO_DOWNLOAD_ERRORPAUSED);
		//		BroadcastManager.registerReceiver(autoDownloadErrorPausedReceiver, filter2);
		inited = true;
	}

	private void unregistBroadReceiver() {
		BroadcastManager.unregisterReceiver(uiReceiver);
		//		BroadcastManager.unregisterReceiver(autoDownloadErrorPausedReceiver);
		inited = false;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			onBackPressed();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		String parentActivityN = getIntent().getStringExtra("activity_name");
		if (parentActivityN == null) {
			startActivity(new Intent(this, MainActivity.class));
		}
		super.onBackPressed();
	}

}
