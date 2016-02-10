/**   
* @Title: ApkInstalledFragment.java
* @Package com.x.activity
* @Description: TODO 

* @date 2014-2-14 下午01:44:10
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
import android.widget.ListView;

import com.x.R;
import com.x.AmineApplication;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.publics.download.BroadcastManager;
import com.x.publics.model.ApkInfoBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseFragment;
import com.x.ui.adapter.ApkInstallListAdapter;

/**
* @ClassName: ApkInstalledFragment
* @Description: TODO 

* @date 2014-2-14 下午01:44:10
* 
*/

public class ApkInstalledFragment extends BaseFragment implements View.OnClickListener {

	private ListView apkInstallLv;
	private ApkInstallListAdapter apkListAdapter;
	private View deleteAllView;
	private View emptyView;
	private BroadcastReceiver scanApkUiReceiver;
	private boolean inited;

	public static Fragment newInstance(Bundle bundle) {
		ApkInstalledFragment fragment = new ApkInstalledFragment();
		if (bundle != null)
			fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_apk_install, null);
		deleteAllView = rootView.findViewById(R.id.fai_delete_all_tv);
		emptyView = rootView.findViewById(R.id.empty_rl);
		deleteAllView.setOnClickListener(this);
		apkListAdapter = new ApkInstallListAdapter(mActivity);
		apkInstallLv = (ListView) rootView.findViewById(R.id.fai_apk_install_lv);
		apkInstallLv.setAdapter(apkListAdapter);
		apkListAdapter.setList(getData());
		return rootView;
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

	public ArrayList<ApkInfoBean> getData() {
		ArrayList<ApkInfoBean> apkInstallFileList = new ArrayList<ApkInfoBean>(AmineApplication.apkInstallFileList);
		return apkInstallFileList;
	}

	public void notifyDataChange() {
		apkListAdapter.setList(getData());
	}

	public int getDeleteType() {
		return MyIntents.EXTRA_TYPE_INSTALL_ALL;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fai_delete_all_tv:

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

	private void registerBroadcastReceiver() {

		scanApkUiReceiver = new ScanApkUiReceiver();
		IntentFilter scanfilter = new IntentFilter();
		scanfilter.addAction(MyIntents.INTENT_APK_SCAN_UPDATE_UI);
		BroadcastManager.registerReceiver(scanApkUiReceiver, scanfilter);
		inited = true;
	}

	private void unregisterReceiver() {
		BroadcastManager.unregisterReceiver(scanApkUiReceiver);
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
			deleteAllView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
		} else {
			emptyView.setVisibility(View.VISIBLE);
			deleteAllView.setVisibility(View.GONE);
		}
		deleteAllView.setClickable(enable);
	}

	private void refreshApkList() {
		if (getData().isEmpty()) {
			emptyView.setVisibility(View.VISIBLE);
			apkInstallLv.setVisibility(View.GONE);
		} else {
			emptyView.setVisibility(View.GONE);
			apkInstallLv.setVisibility(View.VISIBLE);
			apkListAdapter.setList(getData());
		}
	}

	/**
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void
	 */
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setViewBackground(mActivity, deleteAllView, SkinConstan.BTN_AND_PROGRESS_THEME_BG);
	}

}
