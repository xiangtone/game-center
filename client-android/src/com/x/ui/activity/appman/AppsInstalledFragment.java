/**   
* @Title: AppInstalledFragment.java
* @Package com.mas.amineappstore.activity
* @Description: TODO 

* @date 2014-1-24 上午10:28:04
* @version V1.0   
*/

package com.x.ui.activity.appman;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.R;
import com.x.business.localapp.sort.CharacterParser;
import com.x.business.localapp.sort.PinyinComparator;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.LocalAppListAdapter;
import com.x.ui.adapter.LocalAppListAdapter.ViewHolder;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView.OnActionClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* @ClassName: AppInstalledFragment
* @Description: TODO 

* @date 2014-1-24 上午10:28:04
* 
*/

public class AppsInstalledFragment extends BaseFragment {

	private/*GridView*/ActionSlideExpandableListView mAppGv;
	private LocalAppListAdapter localAppListAdapter;
	private List<InstallAppBean> localAppList = new ArrayList<InstallAppBean>();
	private boolean inited = false;
	private BroadcastReceiver mDownloadUiReceiver;
	private PinyinComparator pinyinComparator;
	private View emptyView;

	public static Fragment newInstance(Bundle bundle) {
		AppsInstalledFragment fragment = new AppsInstalledFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_app_install, null);
		emptyView = rootView.findViewById(R.id.empty_rl);
		mAppGv = (ActionSlideExpandableListView) rootView.findViewById(R.id.fai_app_install_lv);
		localAppListAdapter = new LocalAppListAdapter(mActivity);
		mAppGv.setItemActionListener(actionClickListener, R.id.lail_manager_app_tv, R.id.lail_open_app_tv);
		mAppGv.setRecyclerListener(recyclerListener);
		refreshData();
		return rootView;
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (localAppList.isEmpty())
					emptyView.setVisibility(View.VISIBLE);
				else
					emptyView.setVisibility(View.GONE);
				localAppListAdapter.setList(localAppList);
				mAppGv.setAdapter(localAppListAdapter, R.id.lail_top_rl, R.id.lail_expand_ll, itemExpandCollapseListener);
				break;
			default:
				break;
			}
		};
	};

	private void refreshData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				sort();
			}
		}).start();
	}

	private void sort() {
		//实例化汉字转拼音类
		pinyinComparator = new PinyinComparator();

		localAppList = LocalAppEntityManager.getInstance().getAllLocalApps();
		// 根据a-z进行排序源数据
		Collections.sort(localAppList, pinyinComparator);
		uiHandler.sendEmptyMessage(0);
	}

	/**
	* 点击箭头处理
	*/
	private OnItemExpandCollapseListener itemExpandCollapseListener = new OnItemExpandCollapseListener() {

		@Override
		public void onExpand(View itemView, View parentView, int position) {
			ViewHolder holder = (ViewHolder) parentView.getTag();
			holder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_up);
		}

		@Override
		public void onCollapse(View itemView, View parentView, int position) {
			ViewHolder holder = (ViewHolder) parentView.getTag();
			holder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
		}
	};

	/**
	* 弹出项点击处理
	*/
	private OnActionClickListener actionClickListener = new OnActionClickListener() {

		@Override
		public void onClick(View itemView, View clickedView, int position) {
			InstallAppBean appInfoBean = (InstallAppBean) localAppListAdapter.getList().get(position);
			switch (clickedView.getId()) {
			case R.id.lail_manager_app_tv:
				Utils.showInstalledAppDetails(mActivity, appInfoBean.getPackageName());
				break;
			case R.id.lail_open_app_tv:
				Utils.launchAnotherApp(mActivity, appInfoBean.getPackageName());
				break;
			}
		}
	};

	/**
	* 图片资源回收
	*/
	private RecyclerListener recyclerListener = new RecyclerListener() {

		@Override
		public void onMovedToScrapHeap(View view) {
			ImageView iv = (ImageView) view.findViewById(R.id.lail_app_icon_iv);
			iv.setImageBitmap(null);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		if (!inited)
			registDownloadUiReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregistDownloadUiReceiver();
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

	public class DownloadUiReceiver extends BroadcastReceiver {

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
					if (mAppGv != null)
						mAppGv.collapse();
					sort();
					break;
				default:
					break;
				}
			}
		}
	}

}
