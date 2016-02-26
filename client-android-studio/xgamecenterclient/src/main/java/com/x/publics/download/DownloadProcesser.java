package com.x.publics.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.http.conn.ConnectTimeoutException;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.x.R;
import com.x.business.statistic.DataEyeManager;
import com.x.business.statistic.StatisticConstan;
import com.x.business.statistic.StatisticManager;
import com.x.business.update.UpdateManage;
import com.x.db.DownloadEntityManager;
import com.x.db.resource.NativeResourceDBHelper;
import com.x.publics.download.error.NoMemoryException;
import com.x.publics.download.error.ServerException;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.MyIntents;
import com.x.publics.utils.NetworkUtils;
import com.x.publics.utils.PackageUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.publics.utils.Constan.MediaType;

/**
* @ClassName: DownloadManager
* @Description: 下载管理线程 

* @date 2014-1-10 上午09:35:31
* 
*/

public class DownloadProcesser extends Thread {

	public static final int MAX_TASK_COUNT = 99;
	private static final int MAX_DOWNLOAD_THREAD_COUNT = 3;

	private Context context;

	private TaskQueue mTaskQueue;
	private List<DownloadTask> mDownloadingTasks;
	private List<DownloadTask> mDownloadFinishTasks;
	private List<DownloadTask> mPausingTasks;

	private Boolean isRunning = false;

	public DownloadProcesser(Context context) {

		this.context = context;
		mTaskQueue = new TaskQueue();
		mDownloadingTasks = new ArrayList<DownloadTask>();
		mDownloadFinishTasks = new ArrayList<DownloadTask>();
		mPausingTasks = new ArrayList<DownloadTask>();
	}

	public synchronized void restartManager() {
		if (this.getState() == Thread.State.NEW) {
			startManage();
		} else {
			if (isRunning == false) {
				isRunning = true;
				this.run();
			}
		}

	}

	public synchronized void startManage() {

		isRunning = true;
		this.start();
	}

	public synchronized void stopManage() {
		pauseAllTask(true);
		//		isRunning = false;
	}

	public boolean isRunning() {

		return isRunning;
	}

	@Override
	public void run() {

		super.run();
		while (isRunning) {
			DownloadTask task = mTaskQueue.poll();
			mDownloadingTasks.add(task);
			Utils.executeAsyncTask(task);
		}
		LogUtil.getLogger().w("thread finish !!!!!!!!!!");
	}

	/** 
	* @Title: addDownloadHistoryTask 
	* @Description: 初始化下载记录到下载队列 
	* @param @param downloadBean     
	* @return void    
	* @throws 
	*/

	public void addDownloadHistoryTask(DownloadBean downloadBean) {
		try {
			if (downloadBean.getStatus() == DownloadTask.TASK_FINISH
					|| downloadBean.getStatus() == DownloadTask.TASK_LAUNCH) {
				mDownloadFinishTasks.add(newDownloadTask(downloadBean));
			} else if (downloadBean.getStatus() == DownloadTask.TASK_PAUSE) {
				mPausingTasks.add(newDownloadTask(downloadBean));
			} else {
				if (!DownloadManager.getInstance().canDownload(context)) {
					mPausingTasks.add(newDownloadTask(downloadBean));
				} else if (!hasTask(downloadBean)) {
					addTask(newDownloadTask(downloadBean));
				}
			}
			//			if (downloadBean.getStatus() != DownloadTask.TASK_FINISH
			//					&& downloadBean.getStatus() != DownloadTask.TASK_LAUNCH)
			//				mPausingTasks.add(newDownloadTask(downloadBean));
			//			else 
			//				mDownloadFinishTasks.add(newDownloadTask(downloadBean));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/** 
	* @Title: addTask 
	* @Description: 添加下载任务 
	* @param @param downloadBean     
	* @return void    
	* @throws 
	*/

	public synchronized void addTask(DownloadBean downloadBean) {
		if (getTotalTaskCount() >= MAX_TASK_COUNT) {
			ToastUtil.show(context, context.getResources().getString(R.string.download_too_many_downloads),
					Toast.LENGTH_LONG);
			//			broadcastAddTaskError(downloadBean, mContext.getResources().getString(R.string.download_queue_is_full));
			return;
		}
		DownloadEntityManager.getInstance().save(downloadBean);
		DataEyeManager.getInstance().download(downloadBean);
		try {
			StorageUtils.mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			addTask(newDownloadTask(downloadBean));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			broadcastAddTaskError(downloadBean, context.getResources().getString(R.string.download_status_illega_error));
		}

	}

	/** 
	* @Title: broadcastAddTaskError 
	* @Description: 发送添加下载任务失败广播
	* @param @param downloadBean
	* @param @param msg     
	* @return void    
	* @throws 
	*/

	private void broadcastAddTaskError(DownloadBean downloadBean, String msg) {
		Intent updateIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		updateIntent.putExtra(MyIntents.TYPE, MyIntents.Types.PAUSE);
		updateIntent.putExtra(MyIntents.PROCESS_SPEED, context.getResources()
				.getString(R.string.download_status_paused));
		updateIntent.putExtra(MyIntents.PROCESS_PROMOT, msg);
		updateIntent.putExtra(MyIntents.PROCESS_PROGRESS, "0");
		updateIntent.putExtra(MyIntents.URL, downloadBean.getUrl());
		BroadcastManager.sendBroadcast(updateIntent);
	}

	/** 
	* @Title: addTask 
	* @Description: 添加下载任务到下载队列 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	private void addTask(DownloadTask task) {
		broadcastAddTask(task);
		mTaskQueue.offer(task);
		if (!this.isAlive()) {
			//			restartManager();
			startManage();
		}
		try {
			Thread.sleep(50); // sleep
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (mDownloadingTasks.size() >= MAX_DOWNLOAD_THREAD_COUNT) {
			DownloadTask dk = mDownloadingTasks.get(MAX_DOWNLOAD_THREAD_COUNT - 1);
			if (dk.downloadBean.getResourceId() != task.downloadBean.getResourceId())
				task.getListener().waitDownload(task);
		} else {
			Log.d("addTask", "mDownloadingTasks.size():" + mDownloadingTasks.size());
		}
	}

	/** 
	* @Title: broadcastAddTask 
	* @Description: 发送添加下载任务的广播 
	* @param @param url     
	* @return void    
	* @throws 
	*/

	private void broadcastAddTask(DownloadTask task) {

		broadcastAddTask(task, false);
	}

	private void broadcastAddTask(DownloadTask task, boolean isInterrupt) {

		Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.ADD);
		nofityIntent.putExtra(MyIntents.DOWNLOADBEAN, task.downloadBean);
		nofityIntent.putExtra(MyIntents.URL, task.downloadBean.getOriginalUrl());
		nofityIntent.putExtra(MyIntents.PACKAGENAME, task.downloadBean.getPackageName());
		nofityIntent.putExtra(MyIntents.IS_PAUSED, isInterrupt);
		BroadcastManager.sendBroadcast(nofityIntent);
	}

	public void reBroadcastAddAllTask() {

		DownloadTask task;
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			broadcastAddTask(task, task.isInterrupt());
		}
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			broadcastAddTask(task);
		}
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			broadcastAddTask(task);
		}
	}

	/** 
	* @Title: hasTask 
	* @Description: 判断是否存在下载任务 
	* @param @param bean
	* @param @return     
	* @return boolean    
	* @throws 
	*/

	public boolean hasTask(DownloadBean bean) {

		DownloadTask task;
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task.getUrl().equals(bean.getUrl())) {
				return true;
			}
		}
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task.getUrl().equals(bean.getUrl())) {
				return true;
			}
		}
		return false;
	}

	/** 
	* @Title: getTask 
	* @Description: 获取下载任务 
	* @param @param position
	* @param @return     
	* @return DownloadTask    
	* @throws 
	*/

	public DownloadTask getTask(int position) {

		if (position >= mDownloadingTasks.size()) {
			return mTaskQueue.get(position - mDownloadingTasks.size());
		} else {
			return mDownloadingTasks.get(position);
		}
	}

	/** 
	* @Title: getQueueTaskCount 
	* @Description: 获取等待队列大小 
	* @param @return     
	* @return int    
	* @throws 
	*/

	public int getQueueTaskCount() {

		return mTaskQueue.size();
	}

	/** 
	* @Title: getDownloadingTaskCount 
	* @Description: 获取下载中队列大小 
	* @param @return     
	* @return int    
	* @throws 
	*/

	public int getDownloadingTaskCount() {

		return mDownloadingTasks.size();
	}

	/** 
	* @Title: getPausingTaskCount 
	* @Description: 获取暂停队列大小 
	* @param @return     
	* @return int    
	* @throws 
	*/

	public int getPausingTaskCount() {

		return mPausingTasks.size();
	}

	/** 
	* @Title: getTotalTaskCount 
	* @Description: 获取所有队列大小 
	* @param @return     
	* @return int    
	* @throws 
	*/

	public int getTotalTaskCount() {

		return DownloadEntityManager.getInstance().getAllUnfinishedDownloadCount();
	}

	public void checkUncompleteTasks() {

		List<DownloadBean> beanList = DownloadEntityManager.getInstance().getAllDownload();
		if (beanList.size() >= 0) {
			for (int i = 0; i < beanList.size(); i++) {
				addTask(beanList.get(i));
			}
		}
	}

	/** 
	* @Title: pauseTask 
	* @Description: 暂停任务 
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public synchronized void pauseTask(String url) {

		DownloadTask task;
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				pauseTask(task, true, true);
				break;
			}
		}

		// pause the waiting task
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && task.getUrl().equals(url)) {
				mTaskQueue.remove(task);
				pauseTask(task, true, true);
				break;
			}
		}
	}

	/** 
	* @Title: pauseTasks 
	* @Description: 暂停指定任务列表 
	* @param @param urls     
	* @return void    
	* @throws 
	*/

	public synchronized void pauseTasks(List<String> urls) {
		if (urls == null)
			return;
		DownloadTask task;
		// pause the waiting task
		List<DownloadTask> pauseList = new ArrayList<DownloadTask>();
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && urls.contains(task.getUrl())) {
				pauseList.add(task);
				pauseTask(task, false, false);
			}
		}
		for (DownloadTask downloadTask : pauseList) {
			mTaskQueue.remove(downloadTask);
		}
		pauseList = new ArrayList<DownloadTask>();
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && urls.contains(task.getUrl())) {

				pauseList.add(task);
				pauseTask(task, false, false);
			}
		}
		for (DownloadTask downloadTask : pauseList) {
			mDownloadingTasks.remove(downloadTask);
		}

	}

	/** 
	* @Title: pauseTask 
	* @Description: 暂停任务 
	* @param @param downloadBean     
	* @return void    
	* @throws 
	*/

	public synchronized void pauseTask(DownloadBean downloadBean) {

		DownloadTask task;
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && task.getUrl().equals(downloadBean.getUrl())) {
				pauseTask(task, true, true);
				break;
			}
		}

		// pause the waiting task
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && task.getUrl().equals(downloadBean.getUrl())) {
				mTaskQueue.remove(task);
				pauseTask(task, true, true);
				break;
			}
		}
	}

	/** 
	* @Title: pauseAllTask 
	* @Description: 暂停所有任务 
	* @param @param stop     是否退出应用
	* @return void    
	* @throws 
	*/

	public synchronized void pauseAllTask(boolean stop) {
		DownloadTask task;
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			LogUtil.getLogger().e("pause waitingTask " + task.downloadBean.getName());
			if (task != null) {
				pauseTask(task, true, false);
			}

		}
		mTaskQueue.removeAll();
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null) {
				pauseTask(task, true, false);
			}
		}
		mDownloadingTasks.clear();
		if (stop) {
			Intent nofityIntent = new Intent(MyIntents.INTENT_FINISH_ACTIVITY);
			BroadcastManager.sendBroadcast(nofityIntent);
		}
	}

	/** 
	* @Title: deleteTask 
	* @Description: 删除任务
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public synchronized void deleteTask(String url) {

		DownloadTask task = null;

		for (int i = 0; i < mDownloadFinishTasks.size(); i++) {
			task = mDownloadFinishTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				deleteTask(task);
//				return;
			}
		}

		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				task.onCancelled();
				deleteTask(task);
//				return;
			}
		}
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && task.getUrl().equals(url)) {
				mTaskQueue.remove(task);
				deleteTask(task);
//				return;
			}
		}
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				mPausingTasks.remove(task);
				deleteTask(task);
//				return;
			}
		}
	}

	/** 
	* @Title: deleteAllFinishedTask 
	* @Description: 删除所有已完成任务  （可安装，可运行）
	* @param      
	* @return void    
	* @throws 
	*/

	public synchronized void deleteAllFinishedTask() {
		DownloadTask task = null;

		for (int i = 0; i < mDownloadFinishTasks.size(); i++) {
			task = mDownloadFinishTasks.get(i);
			delete(task);
		}
		mDownloadFinishTasks.clear();

		// notify list changed
		Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.DELETE_ALL_HISTORY);
		BroadcastManager.sendBroadcast(nofityIntent);
	}

	/** 
	* @Title: deleteAllUnFinishedTask 
	* @Description: 删除所有未完成任务，（暂停，下载中，等待 ）
	* @param      
	* @return void    
	* @throws 
	*/

	public synchronized void deleteAllUnFinishedTask() {

		DownloadTask task = null;
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			delete(task);
		}
		mTaskQueue.removeAll();

		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			delete(task);
		}
		mPausingTasks.clear();

		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			task.onCancelled();
			delete(task);
		}
		mDownloadingTasks.clear();

		// notify list changed
		Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.DELETE_ALL_DOWNLOADING);
		BroadcastManager.sendBroadcast(nofityIntent);
	}

	public synchronized void deleteAllUnFinishedMediaTask() {

		//清除等待中
		DownloadTask task = null;
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (MediaType.IMAGE.equals(task.downloadBean.getMediaType())
					|| MediaType.MUSIC.equals(task.downloadBean.getMediaType())) {
				delete(task);
			}
		}
		mTaskQueue.removeAllMediaTask();

		//清除暂停
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (MediaType.IMAGE.equals(task.downloadBean.getMediaType())
					|| MediaType.MUSIC.equals(task.downloadBean.getMediaType())) {
				delete(task);
			}
		}
		ArrayList<DownloadTask> pauseList = new ArrayList<DownloadTask>();
		for (DownloadTask pauseTask : mPausingTasks) {
			if (MediaType.IMAGE.equals(pauseTask.downloadBean.getMediaType())
					|| MediaType.MUSIC.equals(pauseTask.downloadBean.getMediaType()))
				pauseList.add(pauseTask);
		}
		try {
			mPausingTasks.removeAll(pauseList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (MediaType.IMAGE.equals(task.downloadBean.getMediaType())
					|| MediaType.MUSIC.equals(task.downloadBean.getMediaType())) {
				task.onCancelled();
				delete(task);
			}
		}

		//清除下载中
		ArrayList<DownloadTask> downloadingList = new ArrayList<DownloadTask>();
		for (DownloadTask downloadingTask : mDownloadingTasks) {
			if (MediaType.IMAGE.equals(downloadingTask.downloadBean.getMediaType())
					|| MediaType.MUSIC.equals(downloadingTask.downloadBean.getMediaType()))
				downloadingList.add(downloadingTask);
		}
		try {
			mDownloadingTasks.removeAll(downloadingList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// notify list changed
		Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.DELETE_ALL_DOWNLOADING_MEDIA);
		BroadcastManager.sendBroadcast(nofityIntent);
	}

	/** 
	* @Title: deleteTask 
	* @Description: 删除下载任务
	* @param @param downloadBean     
	* @return void    
	* @throws 
	*/

	public synchronized void deleteTask(DownloadBean downloadBean) {

		DownloadTask task = null;

		for (int i = 0; i < mDownloadFinishTasks.size(); i++) {
			task = mDownloadFinishTasks.get(i);
			if (task != null && task.getUrl().equals(downloadBean.getUrl())) {
				deleteTask(task);
				return;
			}
		}

		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && task.getUrl().equals(downloadBean.getUrl())) {
				task.onCancelled();
				deleteTask(task);
				return;
			}
		}
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && task.getUrl().equals(downloadBean.getUrl())) {
				deleteTask(task);
				return;
			}
		}
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(downloadBean.getUrl())) {
				deleteTask(task);
				return;
			}
		}
	}

	/** 
	* @Title: continueTask 
	* @Description: 继续下载任务
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public synchronized void continueTask(String url, boolean autoInstall) {

		DownloadTask task;
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				continueTask(task, autoInstall);
				break;
			}

		}
	}

	/** 
	* @Title: continueTask 
	* @Description: 继续下载任务
	* @param @param downloadBean     
	* @return void    
	* @throws 
	*/

	public synchronized void continueTask(DownloadBean downloadBean) {

		DownloadTask task;
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(downloadBean.getUrl())) {
				continueTask(task, true);
				break;
			}

		}
	}

	/** 
	* @Title: pauseTask 
	* @Description: 暂停任务
	* @param @param task
	* @param @param autoInstall 是否自动安装
	* @param @param removed     是否移出下载中队列
	* @return void    
	* @throws 
	*/

	public synchronized void pauseTask(DownloadTask task, boolean autoInstall, boolean removed) {

		if (task != null) {
			task.onCancelled();

			// move to pausing list
			DownloadBean downloadBean = task.downloadBean;
			downloadBean.setAutoInstall(autoInstall);//重置应用收藏wifi自动下载暂停后再下载是否自动安装
			try {
				if (removed)
					mDownloadingTasks.remove(task);
				task = newDownloadTask(downloadBean);
				mPausingTasks.add(task);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
	}

	public synchronized void pauseErrorTask(DownloadTask task) {

		if (task != null) {
			// move to pausing list
			DownloadBean downloadBean = task.downloadBean;
			try {
				mDownloadingTasks.remove(task);
				task = newDownloadTask(downloadBean);
				mPausingTasks.add(task);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
	}

	/** 
	* @Title: continueTask 
	* @Description: 继续任务
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public synchronized void continueTask(DownloadTask task, boolean autoInstall) {

		if (task != null) {
			//			broadcastContinueTask(task);
			try {
				Thread.sleep(50); // sleep
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mPausingTasks.remove(task);
			task.downloadBean.setAutoInstall(autoInstall);
			mTaskQueue.offer(task);
			if (mDownloadingTasks.size() >= MAX_DOWNLOAD_THREAD_COUNT) {
				task.getListener().waitDownload(task);
			}
			LogUtil.getLogger().d("continue download task :" + task.downloadBean.getName());
			if (!this.isAlive()) {
				//				restartManager();
				startManage();
			}
		}
	}

	/** 
	* @Title: completeTask 
	* @Description: 下载完成
	* @param @param task     
	* @return void    
	*/

	public synchronized void completeTask(DownloadTask task) {
		// 下载完成后，统计app
		StatisticManager.getInstance().statisticDownloads(task.downloadBean, context);
		task.downloadBean.setStatus(DownloadTask.TASK_FINISH);
		// 下载完成通知系统更新
		NativeResourceDBHelper.getInstance(context).notifyFileSystemChanged(task.downloadBean.getLocalPath());

		task.downloadBean.setFinishedTime(Utils.formatData(System.currentTimeMillis()));
		DownloadEntityManager.getInstance().update(task.downloadBean);
		if (mDownloadingTasks.contains(task)) {
			mDownloadingTasks.remove(task);
			mDownloadFinishTasks.add(task);
		}
		//删除同一应用不同版本的下载记录
		DownloadManager.getInstance().deleteOldVersionDownloadHistory(task.downloadBean);

		if (task.downloadBean.isPatch()) { // 合并差异包
			UpdateManage.getInstance(context).patchUpdate(task.downloadBean);
		} else {

			// notify list changed
			Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
			nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.COMPLETE);
			nofityIntent.putExtra(MyIntents.URL, task.downloadBean.getOriginalUrl());
			nofityIntent.putExtra(MyIntents.DOWNLOADBEAN, task.downloadBean);
			BroadcastManager.sendBroadcast(nofityIntent);

			if (MediaType.APP.equals(task.downloadBean.getMediaType())
					|| MediaType.GAME.equals(task.downloadBean.getMediaType())) {
				DownloadNotificationManager.getInstance(context).showCompleteNotification();
				DownloadNotificationManager.getInstance(context).showDownloadingNotification();
			}
			// to install 
			if ((MediaType.APP.equals(task.downloadBean.getMediaType()) || MediaType.GAME.equals(task.downloadBean
					.getMediaType()))
					&& Utils.getSettingModel(context).isAutoInstall()
					&& task.downloadBean.isAutoInstall())
				PackageUtil.installApk(context, task.downloadBean.getLocalPath());
		}

		DataEyeManager.getInstance().downloadSuccess(task.downloadBean);
	}

	/** 
	* @Title: deleteTask 
	* @Description: 删除任务
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public synchronized void deleteTask(DownloadTask task) {
		if (mDownloadingTasks.contains(task)) {
			mDownloadingTasks.remove(task);
		}
		if (mDownloadFinishTasks.contains(task)) {
			mDownloadFinishTasks.remove(task);
			// 删除已下载文件，通知系统更新
			NativeResourceDBHelper.getInstance(context).notifyFileSystemChanged(task.downloadBean.getLocalPath());
		}
		if (mPausingTasks.contains(task)) {
			mPausingTasks.remove(task);
		}
		if (mTaskQueue.contains(task)) {
			mTaskQueue.remove(task);
		}

		delete(task);

	}

	private synchronized void delete(DownloadTask task) {

		String parentPath = StorageUtils.FILE_DOWNLOAD_APK_PATH;
		if (MediaType.APP.equals(task.downloadBean.getMediaType())
				|| MediaType.GAME.equals(task.downloadBean.getMediaType())) {
			parentPath = StorageUtils.FILE_DOWNLOAD_APK_PATH;
		} else if (MediaType.IMAGE.equals(task.downloadBean.getMediaType())) {
			parentPath = StorageUtils.FILE_DOWNLOAD_WALLPAPER_PATH;
		} else if (MediaType.MUSIC.equals(task.downloadBean.getMediaType())) {
			parentPath = StorageUtils.FILE_DOWNLOAD_MUSIC_PATH;
		}
		File tempfile = new File(parentPath + NetworkUtils.getFileNameFromDownloadBean(task.downloadBean)
				+ DownloadTask.TEMP_SUFFIX);

		tempfile.delete();

		File file = new File(task.downloadBean.getLocalPath());
		if (file.delete()) {
			// 删除已下载文件，通知系统更新
			NativeResourceDBHelper.getInstance(context).notifyFileSystemChanged(task.downloadBean.getLocalPath());
		}

		if (MediaType.APP.equals(task.downloadBean.getMediaType())
				|| MediaType.GAME.equals(task.downloadBean.getMediaType())) {
			DownloadNotificationManager.getInstance(context).showCompleteNotification();
			DownloadNotificationManager.getInstance(context).showDownloadingNotification();
		}

		// notify list changed
		Intent nofityIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
		nofityIntent.putExtra(MyIntents.TYPE, MyIntents.Types.DELETE);
		nofityIntent.putExtra(MyIntents.PACKAGENAME, task.downloadBean.getPackageName());
		nofityIntent.putExtra(MyIntents.URL, task.downloadBean.getOriginalUrl());
		nofityIntent.putExtra(MyIntents.DOWNLOADBEAN, task.downloadBean);
		nofityIntent.putExtra(MyIntents.VERSION_CODE, task.downloadBean.getVersionCode());
		BroadcastManager.sendBroadcast(nofityIntent);

		if (task.downloadBean.getStatus() != DownloadTask.TASK_FINISH
				&& task.downloadBean.getStatus() != DownloadTask.TASK_LAUNCH) {
			DataEyeManager.getInstance().downloadFail(task.downloadBean, StatisticConstan.FailReason.MANUAL_CANCLE);
		}
	}

	/** 
	* @Title: newDownloadTask 
	* @Description: 生成新下载task
	* @param @param downloadBean
	* @param @return
	* @param @throws MalformedURLException     
	* @return DownloadTask    
	* @throws 
	*/

	private DownloadTask newDownloadTask(DownloadBean downloadBean) throws MalformedURLException {

		DownloadTaskListener taskListener = new DownloadTaskListener() {

			/**
			 *等待回调
			 */
			@Override
			public void waitDownload(DownloadTask task) {
				task.downloadBean.setStatus(DownloadTask.TASK_WAITING);
				DownloadEntityManager.getInstance().update(task.downloadBean);

				Intent updateIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
				updateIntent.putExtra(MyIntents.TYPE, MyIntents.Types.WAIT);
				updateIntent.putExtra(MyIntents.PROCESS_SPEED,
						context.getResources().getString(R.string.download_status_waiting));
				updateIntent.putExtra(MyIntents.PROCESS_PROMOT, Utils.sizeFormat(task.getDownloadSize()) + " / "
						+ Utils.sizeFormat(task.getTotalSize()));
				updateIntent.putExtra(MyIntents.PROCESS_PROGRESS, task.getDownloadPercent() + "");
				updateIntent.putExtra(MyIntents.URL, task.downloadBean.getOriginalUrl());
				updateIntent.putExtra(MyIntents.DOWNLOADBEAN, task.downloadBean);
				BroadcastManager.sendBroadcast(updateIntent);

				if (MediaType.APP.equals(task.downloadBean.getMediaType())
						|| MediaType.GAME.equals(task.downloadBean.getMediaType())) {
					DownloadNotificationManager.getInstance(context).showDownloadingNotification();
				}

			}

			/**
			 *更新回调
			 */
			@Override
			public void updateProcess(DownloadTask task) {
				task.downloadBean.setStatus(DownloadTask.TASK_DOWNLOADING);
				task.downloadBean.setSpeed(Utils.sizeFormat2(task.getDownloadSpeed() * 1000) + "/s ");
				DownloadEntityManager.getInstance().update(task.downloadBean);

				Intent updateIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
				updateIntent.putExtra(MyIntents.TYPE, MyIntents.Types.PROCESS);
				updateIntent.putExtra(MyIntents.PROCESS_SPEED, Utils.sizeFormat2(task.getDownloadSpeed() * 1000)
						+ "/s ");
				updateIntent.putExtra(MyIntents.PROCESS_PROMOT, Utils.sizeFormat(task.getDownloadSize()) + " / "
						+ Utils.sizeFormat(task.getTotalSize()));
				updateIntent.putExtra(MyIntents.PROCESS_PROGRESS, task.getDownloadPercent() + "");
				updateIntent.putExtra(MyIntents.URL, task.downloadBean.getOriginalUrl());
				updateIntent.putExtra(MyIntents.DOWNLOADBEAN, task.downloadBean);
				BroadcastManager.sendBroadcast(updateIntent);
			}

			/**
			 *暂停回调
			 */
			@Override
			public void pauseDownload(DownloadTask task) {
				task.downloadBean.setStatus(DownloadTask.TASK_PAUSE);
				DownloadEntityManager.getInstance().update(task.downloadBean);

				Intent updateIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
				updateIntent.putExtra(MyIntents.TYPE, MyIntents.Types.PAUSE);
				updateIntent.putExtra(MyIntents.PROCESS_SPEED,
						context.getResources().getString(R.string.download_status_paused));
				updateIntent.putExtra(MyIntents.PROCESS_PROMOT, Utils.sizeFormat(task.getDownloadSize()) + " / "
						+ Utils.sizeFormat(task.getTotalSize()));
				updateIntent.putExtra(MyIntents.PROCESS_PROGRESS, task.getDownloadPercent() + "");
				updateIntent.putExtra(MyIntents.URL, task.downloadBean.getOriginalUrl());
				updateIntent.putExtra(MyIntents.DOWNLOADBEAN, task.downloadBean);
				updateIntent.putExtra(MyIntents.PACKAGENAME, task.downloadBean.getPackageName());
				BroadcastManager.sendBroadcast(updateIntent);

				if (MediaType.APP.equals(task.downloadBean.getMediaType())
						|| MediaType.GAME.equals(task.downloadBean.getMediaType())) {
					DownloadNotificationManager.getInstance(context).showDownloadingNotification();
				}

				DataEyeManager.getInstance().downloadInterrupt(task.downloadBean,
						StatisticConstan.PauseReason.MANUAL_PAUSE);
			}

			/**
			 *连接中回调
			 */
			@Override
			public void preDownload(DownloadTask task) {
				task.downloadBean.setStatus(DownloadTask.TASK_CONNECTING);
				DownloadEntityManager.getInstance().update(task.downloadBean);

				Intent updateIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
				updateIntent.putExtra(MyIntents.TYPE, MyIntents.Types.PREDOWNLOAD);
				updateIntent.putExtra(MyIntents.PROCESS_SPEED,
						context.getResources().getString(R.string.download_status_connecting));
				updateIntent.putExtra(MyIntents.PROCESS_PROMOT, Utils.sizeFormat(task.getDownloadSize()) + " / "
						+ Utils.sizeFormat(task.getTotalSize()));
				updateIntent.putExtra(MyIntents.PROCESS_PROGRESS, task.getDownloadPercent() + "");
				updateIntent.putExtra(MyIntents.URL, task.downloadBean.getOriginalUrl());
				updateIntent.putExtra(MyIntents.DOWNLOADBEAN, task.downloadBean);
				updateIntent.putExtra(MyIntents.PACKAGENAME, task.downloadBean.getPackageName());
				BroadcastManager.sendBroadcast(updateIntent);

				if (MediaType.APP.equals(task.downloadBean.getMediaType())
						|| MediaType.GAME.equals(task.downloadBean.getMediaType())) {
					DownloadNotificationManager.getInstance(context).showDownloadingNotification();
				}

				if (task.downloadBean.getCurrentBytes() > 0) {
					DataEyeManager.getInstance().downloadReopen(task.downloadBean,
							StatisticConstan.ContinueReason.NETWORK_OK);
				} else {
					DataEyeManager.getInstance().downloadStart(task.downloadBean);
				}

			}

			/**
			 *下载完成回调
			 */
			@Override
			public void finishDownload(DownloadTask task) {
				completeTask(task);
			}

			/**
			 *下载失败回调
			 */
			@Override
			public void errorDownload(DownloadTask task, Throwable error) {
				pauseErrorTask(task);

				task.downloadBean.setStatus(DownloadTask.TASK_PAUSE);
				DownloadEntityManager.getInstance().update(task.downloadBean);

				String dataEyePauseReason = null;
				String prompt = null;
				if (error != null) {
					if (error instanceof NetworkErrorException || error instanceof ConnectTimeoutException) {
						prompt = context.getResources().getString(R.string.download_status_network_error);
						dataEyePauseReason = StatisticConstan.PauseReason.NETWORK_ERROR;
					} else if (error instanceof NoMemoryException) {
						prompt = context.getResources().getString(R.string.download_status_memory_error);
						dataEyePauseReason = StatisticConstan.PauseReason.MEMORY_ERROR;
					} else if (error instanceof SocketException) {
						prompt = context.getResources().getString(R.string.download_status_network_error);
						dataEyePauseReason = StatisticConstan.PauseReason.NETWORK_ERROR;
					} else if (error instanceof IOException) {
						prompt = context.getResources().getString(R.string.download_status_io_error);
						dataEyePauseReason = StatisticConstan.PauseReason.IO_ERROR;
					} else if (error instanceof IllegalArgumentException) {
						prompt = context.getResources().getString(R.string.download_status_illega_error);
						dataEyePauseReason = StatisticConstan.PauseReason.URL_ERROR;
					} else if (error instanceof ServerException) {
						prompt = context.getResources().getString(R.string.download_status_server_error);
						dataEyePauseReason = StatisticConstan.PauseReason.SERVER_ERROR;
					} else {
						prompt = context.getResources().getString(R.string.download_status_error);
						dataEyePauseReason = StatisticConstan.PauseReason.NETWORK_ERROR;
					}
					ToastUtil.show(
							context,
							ResourceUtil.getString(context, R.string.download_failed_tips, prompt,
									task.downloadBean.getName()), Toast.LENGTH_LONG);
				}
				prompt = context.getResources().getString(R.string.download_status_paused);
				Intent errorIntent = new Intent(MyIntents.INTENT_UPDATE_UI);
				errorIntent.putExtra(MyIntents.TYPE, MyIntents.Types.ERROR);
				errorIntent.putExtra(MyIntents.URL, task.downloadBean.getOriginalUrl());
				errorIntent.putExtra(MyIntents.PROCESS_SPEED, prompt);
				errorIntent.putExtra(MyIntents.PROCESS_PROMOT,
						Utils.sizeFormat(task.getDownloadSize()) + " / " + Utils.sizeFormat(task.getTotalSize()));
				errorIntent.putExtra(MyIntents.PROCESS_PROGRESS, task.getDownloadPercent() + "");
				errorIntent.putExtra(MyIntents.URL, task.downloadBean.getOriginalUrl());
				errorIntent.putExtra(MyIntents.DOWNLOADBEAN, task.downloadBean);
				BroadcastManager.sendBroadcast(errorIntent);

				if (MediaType.APP.equals(task.downloadBean.getMediaType())
						|| MediaType.GAME.equals(task.downloadBean.getMediaType())) {
					DownloadNotificationManager.getInstance(context).showDownloadingNotification();
				}

				DataEyeManager.getInstance().downloadInterrupt(task.downloadBean, dataEyePauseReason);
			}

		};
		return new DownloadTask(context, downloadBean, taskListener);
	}

	private class TaskQueue {
		private Queue<DownloadTask> taskQueue;

		public TaskQueue() {

			taskQueue = new LinkedList<DownloadTask>();
		}

		public synchronized void offer(DownloadTask task) {

			taskQueue.offer(task);//Inserts the specified element into this queue 

		}

		public DownloadTask poll() {

			DownloadTask task = null;
			while (mDownloadingTasks.size() >= MAX_DOWNLOAD_THREAD_COUNT || (task = taskQueue.poll()) == null) {
				SystemClock.sleep(30);
			}
			return task;
		}

		public DownloadTask get(int position) {

			if (position >= size()) {
				return null;
			}
			return ((LinkedList<DownloadTask>) taskQueue).get(position);
		}

		public int size() {
			return taskQueue.size();
		}

		@SuppressWarnings("unused")
		public boolean remove(int position) {

			return taskQueue.remove(get(position));
		}

		public boolean remove(DownloadTask task) {

			return taskQueue.remove(task);
		}

		public void removeAll() {
			taskQueue.clear();
		}

		public void removeAllMediaTask() {
			ArrayList<DownloadTask> deleteList = new ArrayList<DownloadTask>();
			for (DownloadTask task : taskQueue) {
				if (MediaType.IMAGE.equals(task.downloadBean.getMediaType())
						|| MediaType.MUSIC.equals(task.downloadBean.getMediaType()))
					deleteList.add(task);
			}
			try {
				taskQueue.removeAll(deleteList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public boolean contains(DownloadTask task) {
			return taskQueue.contains(task);
		}
	}

}
