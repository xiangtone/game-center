package com.x.ui.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.x.business.statistic.StatisticConstan;
import com.x.business.update.UpdateManage;
import com.x.publics.download.DownloadTask;
import com.x.publics.http.model.HomeMustHaveResponse.MustHaveCategoryList;
import com.x.publics.model.AppInfoBean;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.model.MustHaveAppInfoBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
* @ClassName: BaseFragment
* @Description: TODO 

* @date 2013-12-16 上午10:38:44
* 
*/

public class BaseFragment extends Fragment {
	public FragmentActivity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this.getActivity();
	}

	public boolean onBackPressed() {
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	/** 
	* @Title: compareList 
	* @Description: 应用状态转换
	* @param @param responseList
	* @param @param localAppMap
	* @param @param downloadAppMap
	* @param @return    
	* @return ArrayList<AppInfoBean>    
	*/

	public ArrayList<AppInfoBean> compareList(ArrayList<AppInfoBean> responseList,
			HashMap<String, InstallAppBean> localAppMap, HashMap<String, DownloadBean> downloadAppMap) {
		HashMap<String, InstallAppBean> updateAppMap = UpdateManage.getInstance(mActivity).findAllUpdateInstallAppMap();

		for (AppInfoBean appInfoBean : responseList) {
			String packageName = appInfoBean.getPackageName();
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

	public ArrayList<MustHaveCategoryList> compareMustHaveList(ArrayList<MustHaveCategoryList> responseList,
			HashMap<String, InstallAppBean> localAppMap, HashMap<String, DownloadBean> downloadAppMap) {
		HashMap<String, InstallAppBean> updateAppMap = UpdateManage.getInstance(mActivity).findAllUpdateInstallAppMap();

		for (MustHaveCategoryList mustHaveCategoryList : responseList) {
			ArrayList<AppInfoBean> applist = mustHaveCategoryList.getApplist();
			if (applist == null || applist.isEmpty()) {
				continue;
			}
			for (AppInfoBean appInfoBean : applist) {
				String packageName = appInfoBean.getPackageName();
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
		}

		return responseList;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public String getPage() {
		return StatisticConstan.Pape.BASEFRAGMENT;
	}
}
