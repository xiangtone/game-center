/**   
* @Title: UninstallAppsActivity.java
* @Package com.mas.amineappstore.ui.activity
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-8 下午3:23:42
* @version V1.0   
*/

package com.x.ui.activity.appman;

import java.util.Collections;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.x.business.localapp.sort.PinyinComparator3;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.adapter.UninstallAppsAdapter;

/**
* @ClassName: UninstallAppsActivity
* @Description: TODO(单独的应用卸载功能 )

* @date 2014-10-8 下午3:23:42
* 
*/

public class UninstallAppsActivity extends BaseActivity implements OnClickListener {

	private Context context = this;
	private View loadingPb, loadingLogo;
	private PinyinComparator3 pinyinComparator;
	private UninstallAppsAdapter mUninstallAppsAdapter;
	private ListView uninstallLv;
	private List<InstallAppBean> localList;

	private View emptyView;
	private View loadingView;
	private UninstallReceiver receiver;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_my_apps);
		setContentView(R.layout.activity_uninstallapps);
		initViews();
		initNavigation();
	}

	private void initViews() {
		emptyView = findViewById(R.id.empty_rl);
		loadingView = findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		uninstallLv = (ListView) findViewById(R.id.uninstall_apps_lv);
		mUninstallAppsAdapter = new UninstallAppsAdapter(UninstallAppsActivity.this);
		initAppsAlldata();
	}

	private void initAppsAlldata() {
		showLoadingView();
		Utils.executeAsyncTask(new getAllAppTask());
	}

	private void showLoadingView() {
		uninstallLv.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
	}

	private class getAllAppTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			localList = LocalAppEntityManager.getInstance().getAllLocalApps();
			sort();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			uiHandler.sendEmptyMessage(0);
		}
	}

	private void sort() {
		//实例化汉字转拼音类
		pinyinComparator = new PinyinComparator3();
		// 根据a-z进行排序源数据
		Collections.sort(localList, pinyinComparator);
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (localList != null && !localList.isEmpty()) {
					showListView();
				} else {
					showEmptyView();
				}
				break;
			default:
				break;
			}
		};
	};

	private void showListView() {
		uninstallLv.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		mUninstallAppsAdapter.setList(localList);
		uninstallLv.setAdapter(mUninstallAppsAdapter);
	}

	private void showEmptyView() {
		uninstallLv.setVisibility(View.GONE);
		emptyView.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.GONE);
	}

	/**
	* @ClassName: UninstallReceiver
	* @Description: TODO(监听卸载的广播，卸载完成，删除List里对应的数据)
	
	* @date 2014-10-10 下午1:56:55
	*
	 */
	public class UninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String packageName;
				switch (type) {
				case MyIntents.Types.COMPLETE_UNIINSTALL://完成了卸载所接收到的广播
					packageName = intent.getStringExtra(MyIntents.PACKAGENAME);
					if (!TextUtils.isEmpty(packageName)) {

						List<InstallAppBean> list = mUninstallAppsAdapter.getList();
						if (list != null) {
							for (int i = 0; i < list.size(); i++) {
								InstallAppBean bean = list.get(i);
								if (bean.getPackageName() != null && bean.getPackageName().equals(packageName)) {
									list.remove(bean);
									localList = list;
									Collections.sort(localList, pinyinComparator);
									mUninstallAppsAdapter.notifyDataSetChanged();
								}
							}
						}
					}
					break;
				default:
					break;
				}

			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		registUninstallReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisUninstallReceiver();
	}

	private void registUninstallReceiver() {
		receiver = new UninstallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_UPDATE_UI);
		BroadcastManager.registerReceiver(receiver, filter);
	}

	private void unregisUninstallReceiver() {
		BroadcastManager.unregisterReceiver(receiver);
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
		mTitleTv.setText(R.string.manager_uninstall);
		mNavigationView.setOnClickListener(this);
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(context, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}

}