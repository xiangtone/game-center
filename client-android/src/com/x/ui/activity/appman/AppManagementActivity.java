/**   
* @Title: AppManagementActivity.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-1-14 下午02:56:34
* @version V1.0   
*/

package com.x.ui.activity.appman;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticConstan.ModuleName;
import com.x.business.update.UpdateManage;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.view.TabPageIndicator;

/**
* @ClassName: AppManagementActivity
* @Description: TODO 

* @date 2014-1-14 下午02:56:34
* 
*/

public class AppManagementActivity extends BaseActivity {

	private Context context;
	private List<String> titleList = new ArrayList<String>();
	private String installTips, updateTips;
	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private UiReceiver uiReceiver;
	private boolean inited;
	private int updateNum;
	private TabPageIndicator indicator;
	private Button lucencyBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setTabTitle(R.string.page_app_management);
		setContentView(R.layout.activity_app_management);
		initViewPager();
		initData();
	}

	private void initData() {
		Utils.executeAsyncTask(new GetTabTipsTask(), false);
	}

	private class GetTabTipsTask extends AsyncTask<Boolean, Void, Void> {
		@Override
		protected Void doInBackground(Boolean... params) {
			installTips = ResourceUtil.getString(context, R.string.installed,""+ LocalAppEntityManager.getInstance().getAllLocalApps().size() );
			if(!params[0]){
				updateNum = 0;
			} else {
				updateNum = UpdateManage.getInstance(context).getUpdateAppSize();
			}
			updateTips = ResourceUtil.getString(context, R.string.update,""+updateNum);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			titleList.clear();
			titleList.add(updateTips);
			titleList.add(installTips);
			tabHandler.sendEmptyMessage(1);
		}

	}

	@SuppressLint("NewApi")
	private void initViewPager() {
		lucencyBtn = (Button) findViewById(R.id.act_app_btn);
		installTips = ResourceUtil.getString(context, R.string.installed,"0");
		updateTips = ResourceUtil.getString(context, R.string.update,"0");
		titleList.clear();
		titleList.add(updateTips);
		titleList.add(installTips);

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
				fragment = AppsUpgradeFragment.newInstance(null);
				break;
			case 1:

				fragment = AppsInstalledFragment.newInstance(null);
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
				case MyIntents.Types.COMPLETE_UNIINSTALL:
				case MyIntents.Types.COMPLETE_INSTALL:
				case MyIntents.Types.CHANGE_APP_MANAGEMENT_UPDATE_NUM:
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
				Utils.executeAsyncTask(new GetTabTipsTask(),true);
				break;
			case 1:
				mSectionsPagerAdapter.setTitles(titleList);
				break;
			}
		}
	};
	@Override
	public void onPause() {
		super.onPause();
		lucencyBtn.setVisibility(View.VISIBLE);
		DataEyeManager.getInstance().module(ModuleName.APP_MANAGEMENT, false);
	}
	@Override
	protected void onResume() {
		super.onResume();
		lucencyBtn.setVisibility(View.GONE);
		if (!inited)
			registUiReceiver();
		DataEyeManager.getInstance().module(ModuleName.APP_MANAGEMENT, true);
	    DataEyeManager.getInstance().source(StatisticConstan.SrcName.UPGRADE, 0, null, 0L,  null, null, false) ;	
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregistUiReceiver();
	}

	private void registUiReceiver() {
		uiReceiver = new UiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(uiReceiver, filter);
		inited = true;
	}

	private void unregistUiReceiver() {
		BroadcastManager.unregisterReceiver(uiReceiver);
		inited = false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
