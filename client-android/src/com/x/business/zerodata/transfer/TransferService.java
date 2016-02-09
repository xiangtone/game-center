package com.x.business.zerodata.transfer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.x.publics.utils.TextUtils;

import java.util.ArrayList;

public class TransferService extends Service {

	private TransferProcesser mDownloadProcesser;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mDownloadProcesser = new TransferProcesser(this);
		//		startForeground(100000000, new Notification());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.getAction().equals(TransferIntent.INTENT_DOWNLOADSERVICE)) {
			int type = intent.getIntExtra(TransferIntent.TYPE, -1);
			TransferBean transferBean;
			String url;
			ArrayList<String> urls;
			switch (type) {
			/*启动下载引擎*/
			case TransferIntent.Types.START:
				if (!mDownloadProcesser.isRunning()) {
					mDownloadProcesser.startManage();
				}
				break;
			/*添加下载*/
			case TransferIntent.Types.ADD:
				transferBean = (TransferBean) intent.getExtras().getParcelable(TransferIntent.DOWNLOADBEAN);
				if (transferBean != null && !mDownloadProcesser.hasTask(transferBean)) {
					mDownloadProcesser.addTask(transferBean);
				}
				break;
			case TransferIntent.Types.ADD_SOME:
				ArrayList<TransferBean> transferBeanList = intent.getExtras().getParcelableArrayList(
						TransferIntent.DOWNLOADBEAN_LIST);
				if (transferBeanList != null) {
					mDownloadProcesser.addTasks(transferBeanList);
				}
				break;
			/*继续下载*/
			case TransferIntent.Types.CONTINUE:
				url = intent.getStringExtra(TransferIntent.URL);
				if (!TextUtils.isEmpty(url)) {
					mDownloadProcesser.continueTask(url);
				}
				break;
			/*删除下载*/
			case TransferIntent.Types.DELETE:
				url = intent.getStringExtra(TransferIntent.URL);
				if (!TextUtils.isEmpty(url)) {
					mDownloadProcesser.deleteTask(url);
				}
				break;
			/*删除所有下载历史*/
			case TransferIntent.Types.DELETE_ALL_HISTORY:
				mDownloadProcesser.deleteAllFinishedTask();
				break;
			/*删除所有下载未完成*/
			case TransferIntent.Types.DELETE_ALL_DOWNLOADING:
				mDownloadProcesser.deleteAllUnFinishedTask();
				break;
			/*暂停下载*/
			case TransferIntent.Types.PAUSE:
				url = intent.getStringExtra(TransferIntent.URL);
				if (!TextUtils.isEmpty(url)) {
					mDownloadProcesser.pauseTask(url);
				}
				break;
			/*暂停所有下载*/
			case TransferIntent.Types.PAUSE_ALL:
				mDownloadProcesser.pauseAllTask(false);
				break;
			/*暂停指定下载*/
			case TransferIntent.Types.PAUSE_SOME:
				urls = intent.getStringArrayListExtra(TransferIntent.URL);
				if (urls != null) {
					mDownloadProcesser.pauseTasks(urls);
				}
				break;
			/*停止下载引擎*/
			case TransferIntent.Types.STOP:
				mDownloadProcesser.stopManage();
				break;
			/*初始化下载历史*/
			case TransferIntent.Types.ADD_HISTORY:
				//				List<DownloadBean> hist = DownloadEntityManager.getInstance().getAllDownload();
				//				for (DownloadBean downloadBeanHist : hist) {
				//					mDownloadProcesser.addDownloadHistoryTask(downloadBeanHist);
				//				}
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
