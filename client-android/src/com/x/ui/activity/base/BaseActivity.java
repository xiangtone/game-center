/**   
* @Title: BaseActivity.java
* @Package com.mas.amineappstore.activity.base
* @Description: TODO 

* @date 2013-12-16 下午01:07:11
* @version V1.0   
*/

package com.x.ui.activity.base;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.x.business.statistic.DataEyeManager;
import com.x.business.update.UpdateManage;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;

/**
* @ClassName: BaseActivity
* @Description: TODO 

* @date 2013-12-16 下午01:07:11
* 
*/

public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// enable ActionBar app icon to behave as action to toggle nav drawer
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		DataEyeManager.getInstance().onResume(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void setTabTitle(CharSequence title) {
		setTitle(title);
	}

	public void setTabTitle(int titleId) {
		setTitle(titleId);
	}

	/** 
	* @Title: compareList 
	* @Description: 比较状态 
	* @param @param responseList
	* @param @param localAppMap
	* @param @param downloadAppMap
	* @param @return     
	* @return ArrayList<AppInfoBean>    
	*/

	public ArrayList<AppInfoBean> compareList(ArrayList<AppInfoBean> responseList,
			HashMap<String, InstallAppBean> localAppMap, HashMap<String, DownloadBean> downloadAppMap) {
		HashMap<String, InstallAppBean> updateAppMap = UpdateManage.getInstance(this).findAllUpdateInstallAppMap();

		for (AppInfoBean appInfoBean : responseList) {
			String packageName = appInfoBean.getPackageName();
			if (packageName == null) {
				continue;
			}
			String apkId = "" + appInfoBean.getApkId();
			InstallAppBean installAppBean = null;
			InstallAppBean updateAppBean = null;
			if ((updateAppBean = updateAppMap.get(packageName)) != null
					&& updateAppBean.getVersionCode() == appInfoBean.getVersionCode()) {
				appInfoBean.setStatus(AppInfoBean.Status.CANUPGRADE);
				if (updateAppBean.getIsPatch()) {
					appInfoBean.setPatch(true);
					appInfoBean.setUrl(updateAppBean.getUrl());
					appInfoBean.setPatchUrl(updateAppBean.getUrlPatch());
					appInfoBean.setPatchFileSize(updateAppBean.getPatchSize());
				}
			} else if ((installAppBean = localAppMap.get(packageName)) != null) {
				if (appInfoBean.getVersionCode() > Integer.valueOf(installAppBean.getVersionCode())) {
					appInfoBean.setStatus(AppInfoBean.Status.CANUPGRADE);
				} else {
					appInfoBean.setStatus(AppInfoBean.Status.CANLAUNCH);
				}

			}
			DownloadBean downloadBean = null;
			if ((downloadBean = downloadAppMap.get(apkId)) != null
					&& downloadBean.getVersionCode() == appInfoBean.getVersionCode()) {
				int status = downloadBean.getStatus();
				if (status == DownloadTask.TASK_DOWNLOADING) {
					appInfoBean.setStatus(AppInfoBean.Status.DOWNLOADING);
					appInfoBean.setCurrentBytes(downloadBean.getCurrentBytes());
					appInfoBean.setTotalBytes(downloadBean.getTotalBytes());
				} else if (status == DownloadTask.TASK_PAUSE) {
					appInfoBean.setStatus(AppInfoBean.Status.PAUSED);
					appInfoBean.setCurrentBytes(downloadBean.getCurrentBytes());
					appInfoBean.setTotalBytes(downloadBean.getTotalBytes());
				} else if (status == DownloadTask.TASK_FINISH) {
					appInfoBean.setStatus(AppInfoBean.Status.CANINSTALL);
				} else if (status == DownloadTask.TASK_INSTALLING) {
					appInfoBean.setStatus(AppInfoBean.Status.INSTALLING);
				} else if (status == DownloadTask.TASK_LAUNCH) {
					appInfoBean.setStatus(AppInfoBean.Status.CANLAUNCH);
				} else if (status == DownloadTask.TASK_WAITING) {
					appInfoBean.setStatus(AppInfoBean.Status.WAITING);
					appInfoBean.setCurrentBytes(downloadBean.getCurrentBytes());
					appInfoBean.setTotalBytes(downloadBean.getTotalBytes());
				} else if (status == DownloadTask.TASK_CONNECTING) {
					appInfoBean.setStatus(AppInfoBean.Status.CONNECTING);
					appInfoBean.setCurrentBytes(downloadBean.getCurrentBytes());
					appInfoBean.setTotalBytes(downloadBean.getTotalBytes());
				}
			}
		}
		return responseList;
	}

	@Override
	public void onPause() {
		super.onPause();
		DataEyeManager.getInstance().onPause(this);
	}
}
