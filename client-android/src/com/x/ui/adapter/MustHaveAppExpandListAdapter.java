/**   
* @Title: MustHaveAppExpandListAdapter.java
* @Package com.x.ui.adapter
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-6-18 下午4:48:16
* @version V1.0   
*/

package com.x.ui.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.publics.download.DownloadManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.model.HomeMustHaveResponse.MustHaveCategoryList;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.MustHaveAppInfoBean;
import com.x.publics.utils.NavigationUtils;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;
import com.x.ui.activity.appdetail.AppDetailActivity;
import com.x.ui.view.floatsticklist.WrapperExpandableListAdapter;

/**
* @ClassName: MustHaveAppExpandListAdapter
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-6-18 下午4:48:16
* 
*/

public class MustHaveAppExpandListAdapter extends BaseExpandableListAdapter {

	private int ct;
	private Activity context;
	private String searchKey;

	protected LayoutInflater inflater;
	private HashSet<Integer> expandCategoryIdSet = new HashSet<Integer>();
	public ArrayList<MustHaveCategoryList> mustHavelist = new ArrayList<MustHaveCategoryList>();

	public MustHaveAppExpandListAdapter(Activity context) {
		this.context = context;
		this.inflater = context.getLayoutInflater();
	}

	public MustHaveAppExpandListAdapter(Activity context, int ct) {
		this.ct = ct;
		this.context = context;
		this.inflater = context.getLayoutInflater();
	}

	public void setList(ArrayList<MustHaveCategoryList> mustHavelist) {
		this.mustHavelist = mustHavelist;
		notifyDataSetChanged();
	}

	public ArrayList<MustHaveCategoryList> getList() {
		return mustHavelist;
	}

	@Override
	public int getGroupCount() {
		return mustHavelist.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		try {
			return mustHavelist.get(groupPosition).getApplist().size();
			
		} catch (Exception e) {
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mustHavelist.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mustHavelist.get(groupPosition).getApplist().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		MustHaveAppListViewHeaderHolder holder;
		final MustHaveCategoryList mustHaveCategoryList = mustHavelist.get(groupPosition);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.home_must_have_section_item, parent, false);
			holder = new MustHaveAppListViewHeaderHolder(convertView,inflater,expandCategoryIdSet);
			convertView.setTag(holder);
		} else {
			holder = (MustHaveAppListViewHeaderHolder) convertView.getTag();
		}
		holder.initData(context,mustHaveCategoryList,ct,convertView ,groupPosition);
		holder.setSkinTheme(context);

		return convertView;
	}

	public class HeaderViewHolder {
		TextView textView;
		View dividerbar;
		LinearLayout dividerLine;
		ImageView expandArrow;
		LinearLayout appGroupll;

		void setSkinTheme() {
			//设置皮肤
			SkinConfigManager.getInstance().setViewBackground(context, dividerbar, SkinConstan.DIVIDER_BAR);
		}

		void initData(MustHaveCategoryList mustHaveCategoryList) {
			textView.setText(mustHaveCategoryList.getName());
		}

	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		if (convertView == null)
			convertView = inflater.inflate(R.layout.home_must_have_item, null);

		final AppInfoBean mustHaveAppInfoBean = mustHavelist.get(groupPosition).applist.get(childPosition);
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
				NavigationUtils.toAppDetailPage(context, mustHaveAppInfoBean, ct);
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

		private AppInfoBean mustHaveAppInfoBean;
		private MustHaveAppListViewHolder viewHolder;

		public MyListener(AppInfoBean mustHaveAppInfoBean, MustHaveAppListViewHolder viewHolder) {
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

	private void addDownload(AppInfoBean mustHaveAppInfoBean) {
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

	private void showReDownloadDialog(final DownloadBean downloadBean, final AppInfoBean mustHaveAppInfoBean,
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

	private void showReDownloadFulldoseDialog(final DownloadBean downloadBean, final AppInfoBean mustHaveAppInfoBean,
			String tips) {

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

	private void showReDownloadPatchDialog(final DownloadBean downloadBean, final AppInfoBean mustHaveAppInfoBean,
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
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void cleanCategoryIdSet(){
		expandCategoryIdSet.clear();
	}

}
