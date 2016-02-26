/**   
* @Title: ApkReceiver.java
* @Package com.x.receiver
* @Description: TODO 

* @date 2013-12-17 上午10:46:55
* @version V1.0   
*/

package com.x.receiver;

import java.io.File;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.x.business.applocker.LockManager;
import com.x.business.notice.NoticeManager;
import com.x.business.update.UpdateManage;
import com.x.business.zerodata.transfer.TransferIntent;
import com.x.db.DownloadEntityManager;
import com.x.db.LocalAppEntityManager;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.DownloadNotificationManager;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.DownloadBean;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.Utils;

/**
* @ClassName: ApkReceiver
* @Description: 应用安装下载广播拦截类 

* @date 2013-12-17 上午10:46:55
* 
*/

public class ApkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String packageName = intent.getDataString().substring(8);
			LocalAppEntityManager localAppEntityManager = LocalAppEntityManager.getInstance();
			if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
				localAppEntityManager.deleteApp(packageName);
				//				changeDownloadHistoryStatus(packageName);
				UpdateManage.getInstance(context).deleteUpdateAppByPackageName(packageName);
				Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
				nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.COMPLETE_UNIINSTALL);
				nofityIntent.putExtra(MyIntents.PACKAGENAME, packageName);
				BroadcastManager.sendBroadcast(nofityIntent);
				LogUtil.getLogger().e("ApkReceiver", "ACTION_PACKAGE_REMOVED");
				LockManager.getInstance(context).deleteLockApp(packageName) ;//删除应用锁应用
			} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
				LogUtil.getLogger().e("ApkReceiver", "ACTION_PACKAGE_ADDED");
				PackageManager pm = context.getPackageManager();
				ApplicationInfo pinfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
				PackageInfo info = pm.getPackageInfo(packageName, 0);
				int versionCode = 0;
				long appSize = 0;
				boolean sysUpdate = false;
				int resId = 0;
				DownloadBean downloadBean = null;
				String appName = null, localtion = null, versionName = null;
				if (info != null) {
					try {
						sysUpdate = (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
						//					Drawable icon = pm.getApplicationIcon(pinfo);
						appName = (String) pm.getApplicationLabel(pinfo).toString();
						localtion = pinfo.publicSourceDir;
						if (sysUpdate) {
							sysUpdate = !localtion.startsWith("/data/");
						}
						appSize = Long.valueOf((int) new File(localtion).length());
						versionName = info.versionName;
						versionCode = info.versionCode;
						InstallAppBean bean = new InstallAppBean(0, appName, packageName, versionName, versionCode, ""
								+ appSize, localtion, 1, sysUpdate ? 1 : 0, null);
						LogUtil.getLogger().d(" (InstallAppBean) bean = " + bean.toString());
						localAppEntityManager.addApp(bean);
					} catch (Exception e) {
						e.printStackTrace();
					}
					downloadBean = DownloadEntityManager.getInstance().getDownloadBeanByPkgName(packageName,
							"" + versionCode);
					if (downloadBean != null && (resId = downloadBean.getResourceId()) != 0) {
						// update download history
						//						downloadBean.setStatus(DownloadTask.TASK_LAUNCH);
						//						DownloadEntityManager.getInstance().update(downloadBean);
						DownloadEntityManager.getInstance().deleteByUrl(downloadBean.getUrl());

						// notify list changed
						String url = downloadBean.getOriginalUrl();
						if (!TextUtils.isEmpty(url)) {
							Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
							nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.COMPLETE_INSTALL);
							nofityIntent.putExtra(MyIntents.URL, url);
							nofityIntent.putExtra(MyIntents.VERSION_CODE, downloadBean.getVersionCode());
							nofityIntent.putExtra(MyIntents.FILE_SIZE, downloadBean.getTotalBytes());
							nofityIntent.putExtra(MyIntents.PACKAGENAME, packageName);
							BroadcastManager.sendBroadcast(nofityIntent);
							UpdateManage.getInstance(context).refreshUpdateByResId(resId);

							// 安装完成，通知栏显示
							NoticeManager.getInstance().showNoticeBar(context, downloadBean.getAppId(),
									downloadBean.getName(), downloadBean.getPackageName());

							LogUtil.getLogger().e("ApkReceiver", "send COMPLETE_INSTALL");
						}
					} else {
						// app installed isnot from uplay
						Intent notifyIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
						notifyIntent.putExtra(MyIntents.TYPE, MyIntents.Types.COMPLETE_INSTALL);
						notifyIntent.putExtra(MyIntents.VERSION_CODE, versionCode);
						BroadcastManager.sendBroadcast(notifyIntent);

						//zero data share
						Intent notifyIntent2 = new Intent(TransferIntent.INTENT_UPDATE_UI);
						notifyIntent2.putExtra(TransferIntent.TYPE, TransferIntent.Types.COMPLETE_INSTALL);
						notifyIntent2.putExtra(TransferIntent.PACKAGENAME, packageName);
						BroadcastManager.sendBroadcast(notifyIntent2);
					}
				}
				//apk管理暂时屏蔽
				/*try {
					//notify apk manager list change
					ArrayList<ApkInfoBean> apkList = new ArrayList<ApkInfoBean>(AmineApplication.apkInstallFileList);
					apkList.addAll(AmineApplication.apkUninstallFileList);
					String apkPath = null;
					boolean installed = false;
					for (ApkInfoBean apkInfoBean : apkList) {
						if (apkInfoBean.getPackageName().equals(packageName)
								&& apkInfoBean.getVersionCode() == info.versionCode) {
							apkPath = apkInfoBean.getApkPath();
							installed = apkInfoBean.isInstalled();
							break;
						}
					}
					if (apkPath != null && !installed) {
						Intent i = new Intent(MyIntents.INTENT_APKFILE_DELETE_UPDATE_UI);
						int value = MyIntents.EXTRA_TYPE_UNINSTALL_TO_INSTALL;
						i.putExtra(MyIntents.INTENT_DELETE_EXTRA_TYPE, value);
						ArrayList<String> paths = new ArrayList<String>();
						paths.add(apkPath);
						i.putStringArrayListExtra(MyIntents.INTENT_DELETE_EXTRA_PATH, paths);
						BroadcastManager.sendBroadcast(i);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}*/

				if (resId != 0) {
					// cancle the nitification
					DownloadNotificationManager.getInstance(context).showCompleteNotification();
					// delete local file 
					if (Utils.getSettingModel(context).isDeleteApkFile())
						Utils.deleteFile(downloadBean.getLocalPath());
				}
				
				LockManager.getInstance(context).addLockApp(packageName) ;//添加应用锁应用
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: changeDownloadHistoryStatus 
	* @Description: 卸载app后，历史记录可运行改为可安装 
	* @param @param packageName     
	* @return void    
	*/

	private void changeDownloadHistoryStatus(String packageName) {
		ArrayList<DownloadBean> downloadBeanList = DownloadEntityManager.getInstance()
				.getAllLaunchDownloadByPackageName(packageName);
		for (DownloadBean downloadBean : downloadBeanList) {
			downloadBean.setStatus(DownloadTask.TASK_FINISH);
			DownloadEntityManager.getInstance().update(downloadBean);
		}
	}

}
