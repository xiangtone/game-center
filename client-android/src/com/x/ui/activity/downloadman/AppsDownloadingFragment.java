/**   

* @Title: AppsDownloadingFragment.java
* @Package com.mas.amineappstore.activity
* @Description: TODO 

* @date 2014-2-18 下午05:43:38
* @version V1.0   
*/

package com.x.ui.activity.downloadman;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.Constan;
import com.x.publics.utils.MediaPlayerUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.adapter.DownloadListAdapter;
import com.x.ui.adapter.DownloadViewHolder;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView.OnActionClickListener;

/**
* @ClassName: AppsDownloadingFragment
* @Description: TODO 

* @date 2014-2-18 下午05:43:38
* 
*/

public class AppsDownloadingFragment extends BaseFragment implements View.OnClickListener {

	private View loadingPb, loadingLogo;
	private ActionSlideExpandableListView mDownloadLv;
	private DownloadListAdapter mDownloadListAdapter;
	private TextView deleteAllTv;
	public ArrayList<DownloadBean> hist;
	private boolean inited;
	private DownloadUiReceiver mDownloadUiReceiver;
	private View emptyView, loadingView, deleteView, toHomeBtn;

	public static Fragment newInstance(Bundle bundle) {
		AppsDownloadingFragment fragment = new AppsDownloadingFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frament_app_canupgrade, null);
		deleteAllTv = (TextView) rootView.findViewById(R.id.fac_update_all_tv);
		deleteAllTv.setText(R.string.delete_all);
		loadingView = rootView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		emptyView = rootView.findViewById(R.id.empty_rl);
		deleteView = rootView.findViewById(R.id.fac_update_all_rl);
		toHomeBtn = rootView.findViewById(R.id.e_home_btn);
		toHomeBtn.setOnClickListener(this);
		mDownloadListAdapter = new DownloadListAdapter(mActivity);
		mDownloadLv = (ActionSlideExpandableListView) rootView.findViewById(R.id.fac_app_upgrade_lv);
		mDownloadLv
				.setItemActionListener(actionClickListener, R.id.dil_download_detail_tv, R.id.dil_download_delete_tv);
		mDownloadListAdapter.setListView(mDownloadLv);
		initdata();
		return rootView;
	}

	private void initdata() {
		Utils.executeAsyncTask(new getAsyncDownloadTask());
	}

	private class getAsyncDownloadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			getData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (hist != null && !hist.isEmpty()) {
				mDownloadListAdapter.setList(hist);
				mDownloadLv.setAdapter(mDownloadListAdapter, R.id.dil_top_rll, R.id.dil_expand_ll,
						itemExpandCollapseListener);
			}
			showDeleteAll();
		}
	}

	public ArrayList<DownloadBean> getData() {
		hist = DownloadEntityManager.getInstance().getAllUnfinishedDownload();
		return hist;
	}

	private OnActionClickListener actionClickListener = new OnActionClickListener() {

		@Override
		public void onClick(View itemView, View clickedView, int position) {
			final DownloadBean downloadBean = (DownloadBean) mDownloadListAdapter.getList().get(position);
			switch (clickedView.getId()) {
			case R.id.dil_download_detail_tv:
				// check network
				if (!NetworkUtils.isNetworkAvailable(mActivity)) {
					ToastUtil.show(mActivity, ResourceUtil.getString(mActivity, R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}
				AppInfoBean appInfoBean = Utils.getAppInfoBean(downloadBean);
				Intent intent = new Intent(mActivity, AppDetailActivity.class);
				intent.putExtra("appInfoBean", appInfoBean);
				intent.putExtra("ct", Constan.Ct.APP_DOWNLOAD_MANAGEMENT); // 下载管理 ct 值
				mActivity.startActivity(intent);
				break;

			case R.id.dil_download_delete_tv:

				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						DownloadManager.getInstance().deleteDownload(mActivity, downloadBean.getUrl());
						mDownloadLv.collapse(); //add by ZhouHua
						String tag = MediaPlayerUtil.getInstance(mActivity).getTag();
						if (tag != null) {
							if (tag.equals(downloadBean.getUrl())) {
								MediaPlayerUtil.getInstance(mActivity).release();
							}
						}
					}
				};

				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				Utils.showDialog(mActivity, ResourceUtil.getString(mActivity, R.string.warm_tips),
						ResourceUtil.getString(mActivity, R.string.dialog_delete_download_prompt_one, "1"),
						ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
						ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);

				break;
			}
		}
	};

	private OnItemExpandCollapseListener itemExpandCollapseListener = new OnItemExpandCollapseListener() {

		@Override
		public void onExpand(View itemView, View parentView, int position) {
			String url = (String) parentView.getTag();
			View taskListItem = mDownloadLv.findViewWithTag(url);
			DownloadViewHolder viewHolder = new DownloadViewHolder(taskListItem);
			if (viewHolder != null && viewHolder.arrowIv != null)
				viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_up);
		}

		@Override
		public void onCollapse(View itemView, View parentView, int position) {
			String url = (String) parentView.getTag();
			View taskListItem = mDownloadLv.findViewWithTag(url);
			DownloadViewHolder viewHolder = new DownloadViewHolder(taskListItem);
			if (viewHolder != null && viewHolder.arrowIv != null)
				viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();

		if (!inited)
			registDownloadUiReceiver();
		deleteAllTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						DownloadManager.getInstance().deleteAllDownload(mActivity, getDeleteType());
						if (MediaPlayerUtil.getInstance(mActivity).getIsPlaying()) {
							MediaPlayerUtil.getInstance(mActivity).release();
						}
					}
				};

				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};
				if (hist.size() == 1) {
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.dialog_delete_download_prompt_all_single, ""
									+ hist.size()), ResourceUtil.getString(mActivity, R.string.confirm),
							positiveListener, ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				} else {
					Utils.showDialog(
							mActivity,
							ResourceUtil.getString(mActivity, R.string.warm_tips),
							ResourceUtil.getString(mActivity, R.string.dialog_delete_download_prompt_all,
									"" + hist.size()), ResourceUtil.getString(mActivity, R.string.confirm),
							positiveListener, ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
				}

			}
		});
	}

	public int getDeleteType() {
		return MyIntents.Types.DELETE_ALL_DOWNLOADING;
	}

	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String url;
				View taskListItem;
				DownloadBean downloadBean;
				DownloadViewHolder viewHolder;
				switch (type) {
				case MyIntents.Types.DELETE_ALL_DOWNLOADING:
				case MyIntents.Types.DELETE_ALL_HISTORY:
					mDownloadListAdapter.setList(getData());
					mDownloadLv.setAdapter(mDownloadListAdapter, R.id.dil_top_rll, R.id.dil_expand_ll,
							itemExpandCollapseListener);
					showDeleteAll();
					break;
				case MyIntents.Types.ADD:
				case MyIntents.Types.COMPLETE:
				case MyIntents.Types.DELETE:
				case MyIntents.Types.COMPLETE_INSTALL:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						mDownloadListAdapter.setList(getData());
						mDownloadLv.setAdapter(mDownloadListAdapter, R.id.dil_top_rll, R.id.dil_expand_ll,
								itemExpandCollapseListener);
						showDeleteAll();
					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
				case MyIntents.Types.WAIT:
				case MyIntents.Types.PREDOWNLOAD:
				case MyIntents.Types.PROCESS:
					url = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(url)) {
						notifyAppDataChange(url, downloadBean);
						taskListItem = mDownloadLv.findViewWithTag(url);
						viewHolder = new DownloadViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(downloadBean, mActivity);
						viewHolder.refreshData(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
								intent.getStringExtra(MyIntents.PROCESS_SPEED),
								Integer.valueOf(intent.getStringExtra(MyIntents.PROCESS_PROGRESS)));
					}
					break;
				case MyIntents.Types.MERGE_PATCH:
					url = intent.getStringExtra(MyIntents.URL);
					if (!TextUtils.isEmpty(url)) {
						taskListItem = mDownloadLv.findViewWithTag(url);
						if (taskListItem == null)
							return;
						viewHolder = new DownloadViewHolder(taskListItem);
						viewHolder.refreshMerge(mActivity);
					}
					break;
				}
			}
		}

		/** 
		* @Title: notifyAppDataChange 
		* @Description: 修改adapter数据，防止滑动list数据不对 
		* @param @param url
		* @param @param bean     
		* @return void    
		* @throws 
		*/

		private void notifyAppDataChange(String url, DownloadBean bean) {
			List<DownloadBean> list = mDownloadListAdapter.getList();
			if (bean == null || list == null)
				return;
			for (int i = 0; i < list.size(); i++) {
				DownloadBean downloadBean = list.get(i);
				if (downloadBean.getUrl() != null && downloadBean.getUrl().equals(url)) {
					list.set(i, bean);
					break;
				}
			}
		}
	}

	private void showDeleteAll() {
		if (hist.size() != 0) {
			deleteView.setVisibility(View.VISIBLE);
			deleteAllTv.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			mDownloadLv.setVisibility(View.VISIBLE);
		} else {
			deleteView.setVisibility(View.GONE);
			deleteAllTv.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			toHomeBtn.setVisibility(View.VISIBLE);
			mDownloadLv.setVisibility(View.GONE);
		}
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregistDownloadUiReceiver();
		//release
		MediaPlayerUtil.getInstance(mActivity).release();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.e_home_btn:
			SharedPrefsUtil.putValue(mActivity, "isGoHome", true);
			Intent intent = new Intent(mActivity, MainActivity.class);
			intent.putExtra("tabNum", 0);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(mActivity, toHomeBtn, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
	}

}
