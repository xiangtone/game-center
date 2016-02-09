/**   
 * @Title: LockerFragment.java
 * @Package com.mas.amineappstore.ui.activity.applocker
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-10-10 下午2:39:19
 * @version V1.0   
 */

package com.x.ui.activity.applocker;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.x.business.applocker.LockManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.model.AppLockerBean;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.applocker.AppLockerAppListAdpter;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;

/**
 * @ClassName: LockerFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-10-10 下午2:39:19
 * 
 */

public class LockerFragment extends BaseFragment {

	private View loadingPb, loadingLogo;
	private ActionSlideExpandableListView mAppGv; // APP应用 listView页面
	private AppLockerAppListAdpter appLockerAppListAdpter;
	private List<AppLockerBean> appLockerList = new ArrayList<AppLockerBean>();

	private View emptyView;
	private View loadingView;

	private TextView frequentAppTv;

	public static Fragment newInstance(Bundle bundle) {
		LockerFragment fragment = new LockerFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_my_app_new, null);
		emptyView = rootView.findViewById(R.id.empty_rl);
		loadingView = rootView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		mAppGv = (ActionSlideExpandableListView) rootView.findViewById(R.id.fman_apps_lv);
		appLockerAppListAdpter = new AppLockerAppListAdpter(mActivity);

		initAppsAlldata();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	private void initAppsAlldata() {
		showLoadingView();
		Utils.executeAsyncTask(new getAppLockerTask());
	}

	private class getAppLockerTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			appLockerList = LockManager.getInstance(mActivity).getAppLockerList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			uiHandler.sendEmptyMessage(0);
		}
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (appLockerList != null && appLockerList.size() > 0) {
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

	private void showLoadingView() {
		mAppGv.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);

	}

	// 显示app 应用列表
	private void showListView() {
		mAppGv.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		appLockerAppListAdpter.setList(appLockerList);// 加入 数据

		// mAppGv.setAdapter(appLockerAppListAdpter, R.id.maa_top_rl,
		// R.id.maa_expand_ll, itemExpandCollapseListener);
		mAppGv.setAdapter(appLockerAppListAdpter, R.id.maa_top_rl);
	}

	private void showEmptyView() {
		mAppGv.setVisibility(View.GONE);
		emptyView.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.GONE);
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}
}
