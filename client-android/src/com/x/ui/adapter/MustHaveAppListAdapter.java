/**   
* @Title: MustHaveAppListAdapter.java
* @Package com.mas.amineappstore.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-1 下午5:58:13
* @version V1.0   
*/

package com.x.ui.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.MustHaveAppInfoBean;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.view.stickylistheaders.ExpandableStickyListHeadersListView;
import com.x.ui.view.stickylistheaders.StickyListHeadersAdapter;

/**
* @ClassName: MustHaveAppListAdapter
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-1 下午5:58:13
* 
*/

public class MustHaveAppListAdapter extends ArrayListBaseAdapter<MustHaveAppInfoBean> implements SectionIndexer,
		StickyListHeadersAdapter {

	private int ct;
	private Activity context;
	private String searchKey;
	private ExpandableStickyListHeadersListView appMustHaveLv;
	private HashMap<Integer, HeaderViewHolder> headerViewHolderMap = new HashMap<Integer, MustHaveAppListAdapter.HeaderViewHolder>();

	public MustHaveAppListAdapter(Activity context) {
		super(context);
		this.context = context;
	}

	public MustHaveAppListAdapter(Activity context, int ct) {
		super(context);
		this.ct = ct;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(R.layout.home_must_have_item, null);

		final MustHaveAppInfoBean mustHaveAppInfoBean = mList.get(position);
		MustHaveAppListViewHolder holder = new MustHaveAppListViewHolder(convertView);
		convertView.setTag(mustHaveAppInfoBean.getUrl());//全量包url
		holder.initData(mustHaveAppInfoBean, context);
		holder.setSkinTheme(context);

		holder.appDownloadBtn.setOnClickListener(new MyListener(mustHaveAppInfoBean, holder));
		holder.appPauseView.setOnClickListener(new MyListener(mustHaveAppInfoBean, holder));
		holder.appContinueBtn.setOnClickListener(new MyListener(mustHaveAppInfoBean, holder));
		holder.appInstallBtn.setOnClickListener(new MyListener(mustHaveAppInfoBean, holder));
		holder.appUpgradeBtn.setOnClickListener(new MyListener(mustHaveAppInfoBean, holder));
		holder.appLaunchBtn.setOnClickListener(new MyListener(mustHaveAppInfoBean, holder));

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// check network
				if (!NetworkUtils.isNetworkAvailable(context)) {
					ToastUtil.show(context, context.getResources().getString(R.string.network_canot_work),
							Toast.LENGTH_SHORT);
					return;
				}

				Intent intent = new Intent(context, AppDetailActivity.class);
				intent.putExtra("appInfoBean", mustHaveAppInfoBean);
				intent.putExtra("ct", ct);
				context.startActivity(intent);
			}
		});

		setSkinTheme(convertView); // set skin theme

		return convertView;
	}

	private void setSkinTheme(View convertView) {
		View view = convertView.findViewById(R.id.hmhi_app_layout);
		SkinConfigManager.getInstance().setViewBackground(context, view, SkinConstan.LIST_VIEW_ITEM_BG);
	}

	protected class MyListener implements OnClickListener {

		private MustHaveAppInfoBean mustHaveAppInfoBean;
		private MustHaveAppListViewHolder viewHolder;

		public MyListener(MustHaveAppInfoBean mustHaveAppInfoBean, MustHaveAppListViewHolder viewHolder) {
			this.mustHaveAppInfoBean = mustHaveAppInfoBean;
			this.viewHolder = viewHolder;
		}

		@Override
		public void onClick(View v) {
			DownloadBean downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByResId(
					"" + mustHaveAppInfoBean.getApkId(), "" + mustHaveAppInfoBean.getVersionCode());
			switch (v.getId()) {
			case R.id.hmhi_app_download_btn:
				addDownload(mustHaveAppInfoBean);
				break;
			case R.id.hmhi_app_pause_ll:
				if (downloadBean == null)
					return;
				DownloadManager.getInstance().pauseDownload(context, downloadBean.getUrl());

				break;
			case R.id.hmhi_app_continue_btn:
				if (downloadBean == null)
					return;
				continueDownload(downloadBean.getUrl());
				break;
			case R.id.hmhi_app_install_btn:
				if (downloadBean == null)
					return;
				// to install
				if (downloadBean.isPatch()) {
					if (UpdateManage.getInstance(context).isNewApkFileExit(downloadBean.getPackageName(),
							downloadBean.getVersionName())) {
						PackageUtil.installApk(
								context,
								UpdateManage.getInstance(context).getNewPatchApkPath(downloadBean.getPackageName(),
										downloadBean.getVersionName()));
					} else if (!Utils.isAppExit(downloadBean.getPackageName(), context)) { // 低版本被卸载，下载全量包
						showReDownloadFulldoseDialog(downloadBean, mustHaveAppInfoBean, ResourceUtil.getString(context,
								R.string.dialog_redownload_full_dose, mustHaveAppInfoBean.getAppName()));
					} else {//合并失败，下载增量包
						showReDownloadPatchDialog(downloadBean, mustHaveAppInfoBean, ResourceUtil.getString(context,
								R.string.dialog_redownload_incremental_upgrade, mustHaveAppInfoBean.getAppName()));
					}
				} else if (StorageUtils.isFileExit(downloadBean.getLocalPath())) {
					PackageUtil.installApk(context, downloadBean.getLocalPath());
				} else { //文件不存在，正常重下流程
					showReDownloadDialog(downloadBean, mustHaveAppInfoBean, ResourceUtil.getString(context,
							R.string.dialog_redownload_file_not_found, mustHaveAppInfoBean.getAppName()));
				}
				break;
			case R.id.hmhi_app_upgrade_btn:
				addDownload(mustHaveAppInfoBean);
				break;
			case R.id.hmhi_app_launch_btn:
				// to launch
				Utils.launchAnotherApp(context, mustHaveAppInfoBean.getPackageName());
				break;
			default:
				break;
			}
			viewHolder.refreshAppStatus(mustHaveAppInfoBean.getStatus(), context, null);
		}
	}

	private void addDownload(MustHaveAppInfoBean mustHaveAppInfoBean) {
		SharedPrefsUtil.putValue(context, "ct_" + mustHaveAppInfoBean.getApkId(), ct); // 存储ct值，方便下载统计使用	
		boolean isUpdatePatch = false;
		String downloadUrl = mustHaveAppInfoBean.getUrl();
		if (!TextUtils.isEmpty(mustHaveAppInfoBean.getPatchUrl())) {
			isUpdatePatch = true;
			downloadUrl = mustHaveAppInfoBean.getPatchUrl();
		}
		String mediaType = MediaType.APP;
		if (mustHaveAppInfoBean.getFileType() == 1) {
			mediaType = MediaType.APP;
		} else if (mustHaveAppInfoBean.getFileType() == 2) {
			mediaType = MediaType.GAME;
		}
		DownloadBean downloadBean = new DownloadBean(downloadUrl, mustHaveAppInfoBean.getAppName(),
				mustHaveAppInfoBean.getFileSize(), 0, mustHaveAppInfoBean.getLogo(), mediaType,
				mustHaveAppInfoBean.getApkId(), mustHaveAppInfoBean.getVersionName(),
				mustHaveAppInfoBean.getPackageName(), DownloadTask.TASK_DOWNLOADING, mustHaveAppInfoBean.getFileSize(),
				mustHaveAppInfoBean.getVersionCode(), mustHaveAppInfoBean.getAppId(),
				mustHaveAppInfoBean.getCategoryId(), mustHaveAppInfoBean.getStars(), isUpdatePatch,
				mustHaveAppInfoBean.getUrl());
		DownloadManager.getInstance().addDownload(context, downloadBean);
	}

	private void continueDownload(String url) {
		DownloadManager.getInstance().continueDownload(context, url);

	}

	private void showReDownloadDialog(final DownloadBean downloadBean, final MustHaveAppInfoBean mustHaveAppInfoBean,
			String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getUrl(), mustHaveAppInfoBean.getAppName(),
						downloadBean.getTotalBytes(), 0, mustHaveAppInfoBean.getLogo(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), mustHaveAppInfoBean.getStars(), false,
						downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	/** 
	* @Title: showReDownloadFulldoseDialog 
	* @Description: 重新下载全量包 
	* @param @param downloadBean
	* @param @param tips    
	* @return void    
	*/

	private void showReDownloadFulldoseDialog(final DownloadBean downloadBean,
			final MustHaveAppInfoBean mustHaveAppInfoBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getOriginalUrl(),
						mustHaveAppInfoBean.getAppName(), downloadBean.getTotalBytes(), 0,
						mustHaveAppInfoBean.getLogo(), downloadBean.getMediaType(), downloadBean.getResourceId(),
						downloadBean.getVersionName(), downloadBean.getPackageName(), DownloadTask.TASK_DOWNLOADING,
						downloadBean.getTotalBytes(), downloadBean.getVersionCode(), downloadBean.getAppId(),
						downloadBean.getCategoryId(), mustHaveAppInfoBean.getStars(), false,
						downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	/** 
	* @Title: showReDownloadPatchDialog 
	* @Description: 重下增量包
	* @param @param downloadBean
	* @param @param tips    
	* @return void    
	*/

	private void showReDownloadPatchDialog(final DownloadBean downloadBean,
			final MustHaveAppInfoBean mustHaveAppInfoBean, String tips) {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				DownloadManager.getInstance().deleteDownload(context, downloadBean.getUrl());

				DownloadBean redownloadBean = new DownloadBean(downloadBean.getUrl(), mustHaveAppInfoBean.getAppName(),
						downloadBean.getTotalBytes(), 0, mustHaveAppInfoBean.getLogo(), downloadBean.getMediaType(),
						downloadBean.getResourceId(), downloadBean.getVersionName(), downloadBean.getPackageName(),
						DownloadTask.TASK_DOWNLOADING, downloadBean.getTotalBytes(), downloadBean.getVersionCode(),
						downloadBean.getAppId(), downloadBean.getCategoryId(), mustHaveAppInfoBean.getStars(), true,
						downloadBean.getOriginalUrl());
				DownloadManager.getInstance().addDownload(context, redownloadBean);
			}
		};

		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Utils.showDialog(context, ResourceUtil.getString(context, R.string.warm_tips), tips,
				ResourceUtil.getString(context, R.string.confirm), positiveListener,
				ResourceUtil.getString(context, R.string.cancel), negativeListener);

	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			int catalogId = mList.get(i).getCatalogId();
			if (catalogId == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		return mList.get(position).getCatalogId();
	}
	

	@Override
	public List<MustHaveAppInfoBean> getList() {
		// TODO Auto-generated method stub
		return super.getList();
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		final MustHaveAppInfoBean mustHaveAppInfoBean = mList.get(position);
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = inflater.inflate(R.layout.home_must_have_section_item, parent, false);
			holder.textView = (TextView) convertView.findViewById(R.id.hmhi_catalog_tv);
			holder.dividerbar = (View) convertView.findViewById(R.id.divider_bar);
			holder.dividerLine = (LinearLayout) convertView.findViewById(R.id.hmhi_divider_line);
			holder.expandArrow = (ImageView) convertView.findViewById(R.id.hmhi_show_more_arrow);
//			holder.testIv = (View) convertView.findViewById(R.id.hmhi_app_ll);

			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		holder.setSkinTheme();
		holder.textView.setText(mustHaveAppInfoBean.getCatalogName());
		setHeaderView(appMustHaveLv.isHeaderCollapsed(getHeaderId(position)), holder);
		headerViewHolderMap.put((int) getHeaderId(position), holder);
		return convertView;
	}

	/* (非 Javadoc) 
	* <p>Title: getHeaderId</p> 
	* <p>Description: </p> 
	* @param position
	* @return 
	* @see com.mas.amineappstore.ui.view.stickylistheaders.StickyListHeadersAdapter#getHeaderId(int) 
	*/

	@Override
	public long getHeaderId(int position) {
		if (position >= getCount())
			return 0;
		return mList.get(position).getCatalogId();
	}

	public  class HeaderViewHolder {
		TextView textView;
		View dividerbar;
		LinearLayout dividerLine;
		ImageView expandArrow;
		View testIv;

		void setSkinTheme() {
			//设置皮肤
			SkinConfigManager.getInstance().setViewBackground(context, dividerbar, SkinConstan.DIVIDER_BAR);
		}
	}

	/* (非 Javadoc) 
	* <p>Title: setList</p> 
	* <p>Description: </p> 
	* @param list 
	* @see com.mas.amineappstore.ui.adapter.ArrayListBaseAdapter#setList(java.util.List) 
	*/

	/** 
	* @return appMustHaveLv 
	*/

	public ExpandableStickyListHeadersListView getAppMustHaveLv() {
		return appMustHaveLv;
	}

	/** 
	* @param appMustHaveLv 要设置的 appMustHaveLv 
	*/

	public void setAppMustHaveLv(ExpandableStickyListHeadersListView appMustHaveLv) {
		this.appMustHaveLv = appMustHaveLv;
	}

	/**
	 * 设置刷新时候的展开合并逻辑
	* @Title: setList 
	* @Description: TODO 
	* @param @param list
	* @param @param oldListSize    
	* @return void
	 */
	public void setList(List<MustHaveAppInfoBean> list, int oldListSize) {
		super.setList(list);
		//上拉加载更多时，已经合并的不展开
		//下来刷新时，重新加载数据，所有数据展开
		for (int i = oldListSize < list.size() ? oldListSize : 0; i < list.size(); i++) {
			appMustHaveLv.expand(this.getHeaderId(i));
		}

		//		for (int i = 0; i < list.size(); i++) {
		//			if (headerViewHolderMap.size() != 0)
		//				setHeaderView(appMustHaveLv.isHeaderCollapsed(this.getHeaderId(i)),
		//						headerViewHolderMap.get(getHeaderId(i)));
		//		}

	}

	private void setHeaderView(boolean isHeaderCollapsed, HeaderViewHolder headerViewHolder) {
		if (isHeaderCollapsed) {
			headerViewHolder.dividerLine.setVisibility(View.VISIBLE);
			headerViewHolder.expandArrow.setImageResource(R.drawable.ic_download_manager_arrow_down);
//			headerViewHolder.testIv.setVisibility(View.VISIBLE);
		} else {
			headerViewHolder.dividerLine.setVisibility(View.INVISIBLE);
			headerViewHolder.expandArrow.setImageResource(R.drawable.ic_download_manager_arrow_up);
//			headerViewHolder.testIv.setVisibility(View.GONE);
		}
	}

	/* (非 Javadoc) 
	* <p>Title: notifyDataSetChanged</p> 
	* <p>Description: </p>  
	* @see android.widget.BaseAdapter#notifyDataSetChanged() 
	*/

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

}
