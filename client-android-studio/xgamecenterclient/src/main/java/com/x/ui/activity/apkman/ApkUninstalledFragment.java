/**   
* @Title: ApkUninstalledFragment.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-2-14 下午01:43:56
* @version V1.0   
*/

package com.x.ui.activity.apkman;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.x.R;
import com.x.AmineApplication;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.ApkInfoBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.ApkUninstallListAdapter;
import com.x.ui.adapter.ApkUninstallListAdapter.ViewHolder;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView;
import com.x.ui.view.expendlistview.AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener;
import com.x.ui.view.expendlistview.ActionSlideExpandableListView.OnActionClickListener;

/**
* @ClassName: ApkUninstalledFragment
* @Description: TODO 

* @date 2014-2-14 下午01:43:56
* 
*/

public class ApkUninstalledFragment extends BaseFragment implements View.OnClickListener {

	private View loadingPb, loadingLogo;
	private ActionSlideExpandableListView apkUninstallLv;
	private ApkUninstallListAdapter apkListAdapter;
	private TextView deleteAllTv;
	private View emptyView;
	private View loadingView;
	private View deleteView;
	private boolean inited;
	private BroadcastReceiver deleteApkUiReceiver, scanApkUiReceiver;

	public static Fragment newInstance(Bundle bundle) {
		ApkUninstalledFragment fragment = new ApkUninstalledFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frament_app_canupgrade, null);
		rootView.findViewById(R.id.rl_title_bar).setVisibility(View.GONE);
		emptyView = rootView.findViewById(R.id.empty_rl);
		loadingView = rootView.findViewById(R.id.l_loading_rl);
		loadingPb = loadingView.findViewById(R.id.loading_progressbar);
		loadingLogo = loadingView.findViewById(R.id.loading_logo);
		loadingView.setVisibility(View.GONE);
		deleteView = rootView.findViewById(R.id.fac_update_all_rl);
		deleteAllTv = (TextView) rootView.findViewById(R.id.fac_update_all_tv);
		deleteAllTv.setText(ResourceUtil.getString(mActivity, R.string.delete_all));
		deleteAllTv.setOnClickListener(this);
		apkListAdapter = new ApkUninstallListAdapter(mActivity);
		apkUninstallLv = (ActionSlideExpandableListView) rootView.findViewById(R.id.fac_app_upgrade_lv);
		apkUninstallLv.setItemActionListener(actionClickListener, R.id.uai_expand_ll);
		apkListAdapter.setList(getData());
		apkUninstallLv.setAdapter(apkListAdapter, R.id.uai_top_rl, R.id.uai_expand_ll, itemExpandCollapseListener);
		return rootView;
	}

	public ArrayList<ApkInfoBean> getData() {
		ArrayList<ApkInfoBean> apkUninstallFileList = new ArrayList<ApkInfoBean>(AmineApplication.apkUninstallFileList);
		return apkUninstallFileList;
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		if (!inited)
			registerBroadcastReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		inited = false;
		unregisterReceiver();
	}

	public int getDeleteType() {
		return MyIntents.EXTRA_TYPE_UNINSTALL_ALL;
	}

	/**
	* 弹出项点击处理 
	*/
	private OnActionClickListener actionClickListener = new OnActionClickListener() {

		@Override
		public void onClick(View itemView, View clickedView, int position) {
			PackageUtil.installApk(mActivity, getData().get(position).getApkPath());
		}
	};

	/**
	* 点击箭头处理 
	*/
	private OnItemExpandCollapseListener itemExpandCollapseListener = new OnItemExpandCollapseListener() {

		@Override
		public void onExpand(View itemView, View parentView, int position) {
			ViewHolder viewHolder = (ViewHolder) parentView.getTag();
			if (viewHolder != null && viewHolder.arrowIv != null) {
				viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_up);
			}

		}

		@Override
		public void onCollapse(View itemView, View parentView, int position) {
			ViewHolder viewHolder = (ViewHolder) parentView.getTag();
			if (viewHolder != null && viewHolder.arrowIv != null) {
				viewHolder.arrowIv.setBackgroundResource(R.drawable.ic_download_manager_arrow_down);
			}
		}
	};

	private void registerBroadcastReceiver() {
		deleteApkUiReceiver = new DeleteApkUiReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyIntents.INTENT_APKFILE_DELETE_UPDATE_UI);
		BroadcastManager.registerReceiver(deleteApkUiReceiver, filter);

		scanApkUiReceiver = new ScanApkUiReceiver();
		IntentFilter scanfilter = new IntentFilter();
		scanfilter.addAction(MyIntents.INTENT_APK_SCAN_UPDATE_UI);
		BroadcastManager.registerReceiver(scanApkUiReceiver, scanfilter);

		inited = true;
	}

	private void unregisterReceiver() {
		BroadcastManager.unregisterReceiver(deleteApkUiReceiver);
		BroadcastManager.unregisterReceiver(scanApkUiReceiver);
	}

	private class DeleteApkUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int type = intent.getIntExtra(MyIntents.INTENT_DELETE_EXTRA_TYPE, -1);
			if (type != -1) {

				switch (type) {
				case MyIntents.EXTRA_TYPE_INSTALL:
				case MyIntents.EXTRA_TYPE_UNINSTALL:
				case MyIntents.EXTRA_TYPE_UNINSTALL_TO_INSTALL:
					if (apkUninstallLv != null)
						apkUninstallLv.collapse();
					break;
				default:
					break;
				}
			}

		}

	}

	private class ScanApkUiReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int type = intent.getIntExtra(MyIntents.INTENT_EXTRA_APK_SCAN_STATUS, -1);
			if (type != -1) {
				switch (type) {
				case MyIntents.EXTRA_TYPE_APK_SCAN_START:
					enableDeleteBtn(false);
					break;
				case MyIntents.EXTRA_TYPE_APK_SCAN_FINISH:
					enableDeleteBtn(true);
					break;
				case MyIntents.EXTRA_TYPE_APK_SCAN_REFRESH:
					refreshApkList();
					break;
				default:
					break;
				}
			}

		}

	}

	private void enableDeleteBtn(boolean enable) {
		boolean empty = getData().isEmpty();
		if (enable && !empty) {
			deleteView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
		} else {
			deleteView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}
		deleteAllTv.setClickable(enable);
	}

	private void refreshApkList() {
		if (getData().isEmpty()) {
			emptyView.setVisibility(View.VISIBLE);
			apkUninstallLv.setVisibility(View.GONE);
		} else {
			emptyView.setVisibility(View.GONE);
			apkUninstallLv.setVisibility(View.VISIBLE);
			apkListAdapter.setList(getData());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*删除所有*/
		case R.id.fac_update_all_tv:
			DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

					Intent i = new Intent(MyIntents.INTENT_APKFILE_DELETE_UPDATE_UI);
					i.putExtra(MyIntents.INTENT_DELETE_EXTRA_TYPE, getDeleteType());
					BroadcastManager.sendBroadcast(i);
					for (ApkInfoBean apkInfoBean : getData()) {
						Utils.deleteFile(apkInfoBean.getApkPath());
					}
				}
			};
			DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};
			int apkSize = getData().size();
			if (apkSize == 1) {
				Utils.showDialog(mActivity, ResourceUtil.getString(mActivity, R.string.warm_tips),
						ResourceUtil.getString(mActivity, R.string.dialog_delete_prompt_all_single, "" + apkSize),
						ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
						ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
			} else {
				Utils.showDialog(mActivity, ResourceUtil.getString(mActivity, R.string.warm_tips),
						ResourceUtil.getString(mActivity, R.string.dialog_delete_prompt_all, "" + apkSize),
						ResourceUtil.getString(mActivity, R.string.confirm), positiveListener,
						ResourceUtil.getString(mActivity, R.string.cancel), negativeListener);
			}
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
		SkinConfigManager.getInstance().setViewBackground(mActivity, loadingLogo, SkinConstan.LOADING_LOGO);
		SkinConfigManager.getInstance().setIndeterminateDrawable(mActivity, (ProgressBar) loadingPb,
				SkinConstan.LOADING_PROGRASS_BAR);
		SkinConfigManager.getInstance().setViewBackground(mActivity, deleteAllTv, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
	}

}
