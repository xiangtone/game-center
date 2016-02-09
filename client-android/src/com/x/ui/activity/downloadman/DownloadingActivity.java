/**   
* @Title: DownloadingActivity.java
* @Package com.mas.amineappstore.ui.activity
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-4 下午3:23:34
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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
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
import com.x.publics.utils.Constan.MediaType;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.activity.base.BaseActivity;
import com.x.ui.activity.home.MainActivity;
import com.x.ui.adapter.DownloadListAdapter;
import com.x.ui.adapter.DownloadViewHolder;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView.OnActionClickListener;

/**
* @ClassName: DownloadingActivity
* @Description: TODO(铃声/壁纸下载任务页)

* @date 2014-7-4 下午3:23:34
* 
*/

public class DownloadingActivity extends BaseActivity implements View.OnClickListener {

	private DownloadingActivity mActivity;
	private ActionSlideExpandableListView mDownloadLv;
	private DownloadListAdapter mDownloadListAdapter;
	private TextView deleteAllTv;
	public ArrayList<DownloadBean> hist;
	private boolean inited;
	private DownloadUiReceiver mDownloadUiReceiver;
	private View loadingView, emptyView, deleteView, empty_rl, toHomeBtn;

	private ImageView mGobackIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		setTabTitle(R.string.page_content_downloading);
		setContentView(R.layout.frament_app_canupgrade);
		initUi();
		initdata();
		initNavigation();
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
		mTitleTv.setText(R.string.page_content_downloading);
		mNavigationView.setOnClickListener(this);
	}

	private void initUi() {
		deleteAllTv = (TextView) findViewById(R.id.fac_update_all_tv);
		deleteAllTv.setText(R.string.delete_all);
		loadingView = findViewById(R.id.fac_app_upgrade_loadingll);
		loadingView.setVisibility(View.VISIBLE);
		emptyView = findViewById(R.id.fac_app_upgrade_emptyll);
		empty_rl = findViewById(R.id.empty_rl);
		deleteView = findViewById(R.id.fac_update_all_rl);
		toHomeBtn = findViewById(R.id.e_home_btn);
		toHomeBtn.setOnClickListener(this);
		mDownloadListAdapter = new DownloadListAdapter(mActivity);
		mDownloadLv = (ActionSlideExpandableListView) findViewById(R.id.fac_app_upgrade_lv);
		mDownloadLv
				.setItemActionListener(actionClickListener, R.id.dil_download_detail_tv, R.id.dil_download_delete_tv);
		mDownloadListAdapter.setListView(mDownloadLv);
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
		hist = DownloadEntityManager.getInstance().getAllUnfinishedMediaDownloadList();
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
		return MyIntents.Types.DELETE_ALL_DOWNLOADING_MEDIA;
	}

	public class DownloadUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			handleIntent(intent);

		}

		private void handleIntent(Intent intent) {

			if (intent != null && intent.getAction().equals(MyIntents.INTENT_UPDATE_UI)) {
				int type = intent.getIntExtra(MyIntents.TYPE, -1);
				String originalUrl;
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
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					if (downloadBean != null
							&& (downloadBean.getMediaType().equals(MediaType.IMAGE) || downloadBean.getMediaType()
									.equals(MediaType.MUSIC))) {
						notifyAppDataAdd(downloadBean);
						showDeleteAll();
					}
					break;
				case MyIntents.Types.COMPLETE:
				case MyIntents.Types.DELETE:
					originalUrl = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(originalUrl)
							&& (downloadBean.getMediaType().equals(MediaType.IMAGE) || downloadBean.getMediaType()
									.equals(MediaType.MUSIC))) {
						notifyAppDataDelete(originalUrl);
						showDeleteAll();
					}
					break;
				case MyIntents.Types.ERROR:
				case MyIntents.Types.PAUSE:
				case MyIntents.Types.WAIT:
				case MyIntents.Types.PREDOWNLOAD:
				case MyIntents.Types.PROCESS:
					originalUrl = intent.getStringExtra(MyIntents.URL);
					downloadBean = (DownloadBean) intent.getSerializableExtra(MyIntents.DOWNLOADBEAN);
					if (!TextUtils.isEmpty(originalUrl)
							&& (downloadBean.getMediaType().equals(MediaType.IMAGE) || downloadBean.getMediaType()
									.equals(MediaType.MUSIC))) {
						notifyAppDataChange(originalUrl, downloadBean);
						taskListItem = mDownloadLv.findViewWithTag(originalUrl);
						viewHolder = new DownloadViewHolder(taskListItem);
						viewHolder.refreshDownloadStatus(downloadBean, mActivity);
						viewHolder.refreshData(intent.getStringExtra(MyIntents.PROCESS_PROMOT),
								intent.getStringExtra(MyIntents.PROCESS_SPEED),
								Integer.valueOf(intent.getStringExtra(MyIntents.PROCESS_PROGRESS)));
					}
					break;
				}
			}
		}

		private void notifyAppDataAdd(DownloadBean downloadBean) {
			if (hist != null) {
				hist.add(downloadBean);
				mDownloadListAdapter.notifyDataSetChanged();
			}
		}

		private void notifyAppDataDelete(String url) {
			if (hist != null) {
				for (int i = 0; i < hist.size(); i++) {
					DownloadBean downloadBean = hist.get(i);
					if (downloadBean.getOriginalUrl() != null && downloadBean.getOriginalUrl().equals(url)) {
						hist.remove(downloadBean);
						mDownloadListAdapter.notifyDataSetChanged();
						break;
					}
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
		if (hist != null && !hist.isEmpty()) {
			deleteView.setVisibility(View.VISIBLE);
			deleteAllTv.setVisibility(View.VISIBLE);
			mDownloadLv.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);
			empty_rl.setVisibility(View.GONE);
		} else {
			deleteView.setVisibility(View.GONE);
			deleteAllTv.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
			toHomeBtn.setVisibility(View.VISIBLE);
			mDownloadLv.setVisibility(View.GONE);
			empty_rl.setVisibility(View.VISIBLE);
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
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		case R.id.e_home_btn:
			SharedPrefsUtil.putValue(mActivity, "isGoHome", true);
			Intent intent = new Intent(mActivity, MainActivity.class);
			intent.putExtra("tabNum", "0");
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
		SkinConfigManager.getInstance().setTitleSkin(mActivity, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(mActivity, toHomeBtn, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
		SkinConfigManager.getInstance()
				.setViewBackground(mActivity, deleteAllTv, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
	}

}
