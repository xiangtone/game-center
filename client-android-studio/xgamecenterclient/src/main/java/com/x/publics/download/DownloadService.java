package com.x.publics.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.x.business.settings.SettingModel;
import com.x.db.DownloadEntityManager;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: DownloadService
* @Description: 下载后台服务 

* @date 2014-1-10 上午09:38:11
* 
*/

public class DownloadService extends Service {

	private DownloadProcesser mDownloadProcesser;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mDownloadProcesser = new DownloadProcesser(this);
		//		startForeground(100000000, new Notification());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.getAction().equals(MyIntents.INTENT_DOWNLOADSERVICE)) {
			int type = intent.getIntExtra(MyIntents.TYPE, -1);
			DownloadBean downloadBean;
			String url;
			ArrayList<String> urls;
			switch (type) {
			/*启动下载引擎*/
			case MyIntents.Types.START:
				if (!mDownloadProcesser.isRunning()) {
					mDownloadProcesser.startManage();
				}
				break;
			/*添加下载*/
			case MyIntents.Types.ADD:
				downloadBean = (DownloadBean) intent.getExtras().getSerializable(MyIntents.DOWNLOADBEAN);
				if (downloadBean != null && !mDownloadProcesser.hasTask(downloadBean)) {
					mDownloadProcesser.addTask(downloadBean);
				}
				break;
			/*继续下载*/
			case MyIntents.Types.CONTINUE:
				url = intent.getStringExtra(MyIntents.URL);
				if (!TextUtils.isEmpty(url)) {
					mDownloadProcesser.continueTask(url ,true);
				}
				break;
				/*继续自动下载*/
			case MyIntents.Types.AUTO_DOWNLOAD_CONTINUE:
				url = intent.getStringExtra(MyIntents.URL);
				if (!TextUtils.isEmpty(url)) {
					SettingModel settingModel = Utils.getSettingModel(this);
					boolean toInstall = settingModel.isAutoInstall() && settingModel.isSilentInstall();
					mDownloadProcesser.continueTask(url ,toInstall);
				}
				break;
			/*删除下载*/
			case MyIntents.Types.DELETE:
				url = intent.getStringExtra(MyIntents.URL);
				if (!TextUtils.isEmpty(url)) {
					mDownloadProcesser.deleteTask(url);
				}
				break;
			/*删除所有下载历史*/
			case MyIntents.Types.DELETE_ALL_HISTORY:
				mDownloadProcesser.deleteAllFinishedTask();
				break;
			/*删除所有下载未完成*/
			case MyIntents.Types.DELETE_ALL_DOWNLOADING:
				mDownloadProcesser.deleteAllUnFinishedTask();
				break;
			/*删除所有铃声壁纸下载未完成*/
			case MyIntents.Types.DELETE_ALL_DOWNLOADING_MEDIA:
				mDownloadProcesser.deleteAllUnFinishedMediaTask();
				break;
			/*暂停下载*/
			case MyIntents.Types.PAUSE:
				url = intent.getStringExtra(MyIntents.URL);
				if (!TextUtils.isEmpty(url)) {
					mDownloadProcesser.pauseTask(url);
				}
				break;
			/*暂停所有下载*/
			case MyIntents.Types.PAUSE_ALL:
				mDownloadProcesser.pauseAllTask(false);
				break;
			/*暂停指定下载*/
			case MyIntents.Types.PAUSE_SOME:
				urls = intent.getStringArrayListExtra(MyIntents.URL);
				if (urls != null) {
					mDownloadProcesser.pauseTasks(urls);
				}
				break;
			/*停止下载引擎*/
			case MyIntents.Types.STOP:
				mDownloadProcesser.stopManage();
				break;
			/*初始化下载历史*/
			case MyIntents.Types.ADD_HISTORY:
				List<DownloadBean> hist = DownloadEntityManager.getInstance().getAllDownload();
				for (DownloadBean downloadBeanHist : hist) { //数据库升级后 新增字段 originalUrl初始值为空，赋值
					if (TextUtils.isEmpty(downloadBeanHist.getOriginalUrl())) {
						downloadBeanHist.setOriginalUrl(downloadBeanHist.getUrl());
					}
					mDownloadProcesser.addDownloadHistoryTask(downloadBeanHist);
				}
				break;
			default:
				break;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("", "DownloadService onDestroy...");
	}

}
