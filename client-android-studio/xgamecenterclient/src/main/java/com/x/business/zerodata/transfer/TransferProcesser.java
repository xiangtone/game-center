package com.x.business.zerodata.transfer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.x.R;
import com.x.business.zerodata.history.TransferHistoryManager;
import com.x.db.resource.NativeResourceDBHelper;
import com.x.db.resource.NativeResourceConstant.FileType;
import com.x.publics.download.BroadcastManager;
import com.x.publics.download.error.NoMemoryException;
import com.x.publics.download.error.ServerException;
import com.x.publics.http.volley.VolleyError;
import com.x.publics.http.volley.Response.ErrorListener;
import com.x.publics.http.volley.Response.Listener;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.StorageUtils;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;

public class TransferProcesser extends Thread {

	private static final int MAX_TASK_COUNT = 10000;
	private static final int MAX_DOWNLOAD_THREAD_COUNT = 1;

	private Context mContext;

	private TaskQueue mTaskQueue;
	private List<TransferTask> mDownloadingTasks;
	private List<TransferTask> mDownloadFinishTasks;
	private List<TransferTask> mPausingTasks;

	private Boolean isRunning = false;
	private boolean isAdding = false;
	public TransferProcesser(Context context) {

		mContext = context;
		mTaskQueue = new TaskQueue();
		mDownloadingTasks = new ArrayList<TransferTask>();
		mDownloadFinishTasks = new ArrayList<TransferTask>();
		mPausingTasks = new ArrayList<TransferTask>();
	}

	public synchronized void startManage() {

		isRunning = true;
		this.start();
	}

	public synchronized void stopManage() {
		isRunning = false;
		pauseAllTask(false);
	}

	public boolean isRunning() {

		return isRunning;
	}

	@Override
	public void run() {

		super.run();
		while (isRunning) {
			TransferTask task = mTaskQueue.poll();
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

	public void addDownloadHistoryTask(TransferBean transferBean) {
		try {
			if (transferBean.getFileStatus() == TransferTask.TASK_FINISH
					|| transferBean.getFileStatus() == TransferTask.TASK_LAUNCH) {
				mDownloadFinishTasks.add(newDownloadTask(transferBean));
			} else if (transferBean.getFileStatus() == TransferTask.TASK_PAUSE) {
				mPausingTasks.add(newDownloadTask(transferBean));
			} else {
				if (!TransferManager.getInstance().canDownload(mContext)) {
					mPausingTasks.add(newDownloadTask(transferBean));
				} else if (!hasTask(transferBean)) {
					addTask(newDownloadTask(transferBean));
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	* @Title: addTasks 
	* @Description: TODO 
	* @param @param transferBeanList    
	* @return void    
	*/ 
	
	public synchronized void addTasks(final ArrayList<TransferBean> transferBeanList) {
		isAdding = true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(TransferBean transferBean : transferBeanList ){
					if(isAdding && transferBean != null && !hasTask(transferBean)){
						addTask(transferBean);
					}
				}				
			}
		}).start();
	}

	/** 
	* @Title: addTask 
	* @Description: 添加下载任务 
	* @param @param downloadBean     
	* @return void    
	*/

	public synchronized void addTask(TransferBean transferBean) {
		if (getTotalTaskCount() >= MAX_TASK_COUNT) {
			ToastUtil.show(mContext, mContext.getResources().getString(R.string.download_queue_is_full),
					Toast.LENGTH_LONG);
			//			broadcastAddTaskError(transferBean, mContext.getResources().getString(R.string.download_queue_is_full));
			return;
		}
		try {
			StorageUtils.mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if(!isAdding)
				return ;
			addTask(newDownloadTask(transferBean));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			//			broadcastAddTaskError(transferBean, "wrong download url");
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

	private void broadcastAddTaskError(TransferBean transferBean, String msg) {
		Intent updateIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
		updateIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.PAUSE);
		updateIntent.putExtra(TransferIntent.PROCESS_SPEED,
				mContext.getResources().getString(R.string.download_status_paused));
		updateIntent.putExtra(TransferIntent.PROCESS_PROMOT, msg);
		updateIntent.putExtra(TransferIntent.PROCESS_PROGRESS, "0");
		updateIntent.putExtra(TransferIntent.URL, transferBean.getFileUrl());
		BroadcastManager.sendBroadcast(updateIntent);
	}

	/** 
	* @Title: addTask 
	* @Description: 添加下载任务到下载队列 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	private void addTask(TransferTask task) {
		if (!this.isAlive()) {
			this.startManage();
		}
		broadcastAddTask(task.getUrl());
		mTaskQueue.offer(task);
		try {
			Thread.sleep(50); // sleep
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (mDownloadingTasks.size() >= MAX_DOWNLOAD_THREAD_COUNT) {
			TransferTask dk = mDownloadingTasks.get(MAX_DOWNLOAD_THREAD_COUNT - 1);
			if (!dk.transferBean.getFileRawPath().equals(task.transferBean.getFileRawPath()))
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

	private void broadcastAddTask(String url) {

		broadcastAddTask(url, false);
	}

	private void broadcastAddTask(String url, boolean isInterrupt) {

		Intent nofityIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
		nofityIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.ADD);
		nofityIntent.putExtra(TransferIntent.URL, url);
		nofityIntent.putExtra(TransferIntent.IS_PAUSED, isInterrupt);
		BroadcastManager.sendBroadcast(nofityIntent);
	}

	public void reBroadcastAddAllTask() {

		TransferTask task;
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			broadcastAddTask(task.getUrl(), task.isInterrupt());
		}
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			broadcastAddTask(task.getUrl());
		}
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			broadcastAddTask(task.getUrl());
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

	public boolean hasTask(TransferBean bean) {

		TransferTask task;
		try {
			for (int i = 0; i < mDownloadingTasks.size(); i++) {
				task = mDownloadingTasks.get(i);
				if (task.getUrl().equals(bean.getFileUrl())) {
					return true;
				}
			}
			for (int i = 0; i < mTaskQueue.size(); i++) {
				task = mTaskQueue.get(i);
				if (task.getUrl().equals(bean.getFileUrl())) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	public TransferTask getTask(int position) {

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

		return getQueueTaskCount() + getDownloadingTaskCount() + getPausingTaskCount();
	}

	/** 
	* @Title: pauseTask 
	* @Description: 暂停任务 
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public synchronized void pauseTask(String url) {

		TransferTask task;
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
		TransferTask task;
		// pause the waiting task
		List<TransferTask> pauseList = new ArrayList<TransferTask>();
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && urls.contains(task.getUrl())) {
				pauseList.add(task);
				pauseTask(task, false, false);
			}
		}
		for (TransferTask transferTask : pauseList) {
			mTaskQueue.remove(transferTask);
		}
		pauseList = new ArrayList<TransferTask>();
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && urls.contains(task.getUrl())) {

				pauseList.add(task);
				pauseTask(task, false, false);
			}
		}
		for (TransferTask transferTask : pauseList) {
			mDownloadingTasks.remove(transferTask);
		}

	}

	/** 
	* @Title: pauseTask 
	* @Description: 暂停任务 
	* @param @param downloadBean     
	* @return void    
	* @throws 
	*/

	public synchronized void pauseTask(TransferBean transferBean) {

		TransferTask task;
		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && task.getUrl().equals(transferBean.getFileUrl())) {
				pauseTask(task, true, true);
				break;
			}
		}

		// pause the waiting task
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && task.getUrl().equals(transferBean.getFileUrl())) {
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
		isAdding = false; //停止添加任务
		TransferTask task;
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			LogUtil.getLogger().e("pause waitingTask " + task.transferBean.getFileName());
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
//		if (stop) {
//			Intent nofityIntent = new Intent(TransferIntent.INTENT_FINISH_ACTIVITY);
//			BroadcastManager.sendBroadcast(nofityIntent);
//		}
	}

	/** 
	* @Title: deleteTask 
	* @Description: 删除任务
	* @param @param url     
	* @return void    
	* @throws 
	*/

	public synchronized void deleteTask(String url) {

		TransferTask task = null;

		for (int i = 0; i < mDownloadFinishTasks.size(); i++) {
			task = mDownloadFinishTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				deleteTask(task);
				return;
			}
		}

		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				task.onCancelled();
				deleteTask(task);
				return;
			}
		}
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && task.getUrl().equals(url)) {
				mTaskQueue.remove(task);
				deleteTask(task);
				return;
			}
		}
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				mPausingTasks.remove(task);
				deleteTask(task);
				return;
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
		TransferTask task = null;

		for (int i = 0; i < mDownloadFinishTasks.size(); i++) {
			task = mDownloadFinishTasks.get(i);
			deleteFile(task);
		}
		mDownloadFinishTasks.clear();

		// notify list changed
		Intent nofityIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
		nofityIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.DELETE_ALL_HISTORY);
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
		isAdding = false; //停止添加任务
		TransferTask task = null;
		/*for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			deleteFile(task);
		}*/
		mTaskQueue.removeAll(); //清除等待中任务

		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			deleteFile(task);
		}
		mPausingTasks.clear();

		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			task.onCancelled();
			deleteFile(task);
		}
		mDownloadingTasks.clear();

		// notify list changed
		Intent nofityIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
		nofityIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.DELETE_ALL_DOWNLOADING);
		BroadcastManager.sendBroadcast(nofityIntent);
	}

	/** 
	* @Title: deleteTask 
	* @Description: 删除下载任务
	* @param @param downloadBean     
	* @return void    
	* @throws 
	*/

	public synchronized void deleteTask(TransferBean transferBean) {

		TransferTask task = null;

		for (int i = 0; i < mDownloadFinishTasks.size(); i++) {
			task = mDownloadFinishTasks.get(i);
			if (task != null && task.getUrl().equals(transferBean.getFileUrl())) {
				deleteTask(task);
				return;
			}
		}

		for (int i = 0; i < mDownloadingTasks.size(); i++) {
			task = mDownloadingTasks.get(i);
			if (task != null && task.getUrl().equals(transferBean.getFileUrl())) {
				task.onCancelled();
				deleteTask(task);
				return;
			}
		}
		for (int i = 0; i < mTaskQueue.size(); i++) {
			task = mTaskQueue.get(i);
			if (task != null && task.getUrl().equals(transferBean.getFileUrl())) {
				deleteTask(task);
				return;
			}
		}
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(transferBean.getFileUrl())) {
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

	public synchronized void continueTask(String url) {

		TransferTask task;
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(url)) {
				continueTask(task);
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

	public synchronized void continueTask(TransferBean transferBean) {

		TransferTask task;
		for (int i = 0; i < mPausingTasks.size(); i++) {
			task = mPausingTasks.get(i);
			if (task != null && task.getUrl().equals(transferBean.getFileUrl())) {
				continueTask(task);
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

	public synchronized void pauseTask(TransferTask task, boolean autoInstall, boolean removed) {

		if (task != null) {
			task.onCancelled();

			// move to pausing list
			;
			try {
				if (removed)
					mDownloadingTasks.remove(task);
				task = newDownloadTask(task.transferBean);
				mPausingTasks.add(task);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
	}

	public synchronized void pauseErrorTask(TransferTask task) {

		if (task != null) {
			// move to pausing list
			try {
				mDownloadingTasks.remove(task);
				task = newDownloadTask(task.transferBean);
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

	public synchronized void continueTask(TransferTask task) {

		if (task != null) {
			//			broadcastContinueTask(task);
			mPausingTasks.remove(task);
			mTaskQueue.offer(task);
			if (mDownloadingTasks.size() >= MAX_DOWNLOAD_THREAD_COUNT) {
				task.getListener().waitDownload(task);
			}
			LogUtil.getLogger().d("continue download task :" + task.transferBean.getFileName());
			if (!this.isAlive()) {
				this.startManage();
			}
		}
	}

	/** 
	* @Title: completeTask 
	* @Description: 下载完成
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public synchronized void completeTask(TransferTask task) {
		task.transferBean.setFileStatus(TransferTask.TASK_FINISH);
		task.transferBean.setCurrentBytes(task.transferBean.getFileSize());

		// 下载完成通知系统更新
		int type = task.transferBean.getFileType();
		if (type != FileType.APK) { 
			NativeResourceDBHelper.getInstance(mContext).notifyFileSystemChanged(task.transferBean.getFileSavePath());
		} 
				
		if (mDownloadingTasks.contains(task)) {
			mDownloadingTasks.remove(task);
			mDownloadFinishTasks.add(task);
		}
		// notify list changed
		Intent nofityIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
		nofityIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.COMPLETE);
		nofityIntent.putExtra(TransferIntent.DOWNLOADBEAN, task.transferBean);
		nofityIntent.putExtra(TransferIntent.URL, task.getUrl());
		BroadcastManager.sendBroadcast(nofityIntent);
		//		DownloadNotificationManager.getInstance(mContext).showCompleteNotification();
		//		DownloadNotificationManager.getInstance(mContext).showDownloadingNotification();
		// to install 
		//		if (Utils.getSettingModel(mContext).isAutoInstall() && task.downloadBean.isAutoInstall())
		//			Utils.installApk(mContext, task.downloadBean.getLocalPath());

		TransferHistoryManager.getInstance().saveTransferreceiveHistory(task.transferBean);
	}

	/** 
	* @Title: deleteTask 
	* @Description: 删除任务
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public synchronized void deleteTask(TransferTask task) {
		if (mDownloadingTasks.contains(task)) {
			mDownloadingTasks.remove(task);
		}
		if (mDownloadFinishTasks.contains(task)) {
			mDownloadFinishTasks.remove(task);
		}
		if (mPausingTasks.contains(task)) {
			mPausingTasks.remove(task);
		}
		if (mTaskQueue.contains(task)) {
			mTaskQueue.remove(task);
		}

		deleteFile(task);
		sendDeleteBroadcast(task);
	}

	private synchronized void deleteFile(TransferTask task) {
		File tempfile = task.getTempFile();
		if (tempfile != null)
			tempfile.delete();
	}
	

	private void sendDeleteBroadcast(TransferTask task){
		Intent nofityIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
		nofityIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.DELETE);
		nofityIntent.putExtra(TransferIntent.URL, task.getUrl());
		nofityIntent.putExtra(TransferIntent.DOWNLOADBEAN, task.transferBean);
		BroadcastManager.sendBroadcast(nofityIntent);
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

	private TransferTask newDownloadTask(TransferBean transferBean) throws MalformedURLException {

		TransferTaskListener taskListener = new TransferTaskListener() {

			/**
			 *等待回调
			 */
			@Override
			public void waitDownload(TransferTask task) {
				task.transferBean.setFileStatus(TransferTask.TASK_WAITING);

				Intent updateIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
				updateIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.WAIT);
				updateIntent.putExtra(TransferIntent.PROCESS_SPEED,
						mContext.getResources().getString(R.string.download_status_waiting));
				updateIntent.putExtra(TransferIntent.PROCESS_PROMOT, Utils.sizeFormat(task.getDownloadSize()) + " / "
						+ Utils.sizeFormat(task.getTotalSize()));
				updateIntent.putExtra(TransferIntent.PROCESS_PROGRESS, task.getDownloadPercent() + "");
				updateIntent.putExtra(TransferIntent.URL, task.getUrl());
				updateIntent.putExtra(TransferIntent.DOWNLOADBEAN, task.transferBean);
				BroadcastManager.sendBroadcast(updateIntent);
			
			}

			/**
			 *更新回调
			 */
			@Override
			public void updateProcess(TransferTask task) {
				task.transferBean.setFileStatus(TransferTask.TASK_DOWNLOADING);
				task.transferBean.setCurrentBytes(task.getDownloadSize());
				task.transferBean.setSpeed(Utils.sizeFormat2(task.getDownloadSpeed() * 1000) + "/s ");

				long downloadpercent = task.getDownloadPercent();
				Intent updateIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
				updateIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.PROCESS);
				updateIntent.putExtra(TransferIntent.PROCESS_SPEED, Utils.sizeFormat2(task.getDownloadSpeed() * 1000)
						+ "/s ");
				updateIntent.putExtra(TransferIntent.PROCESS_PROMOT, Utils.sizeFormat(task.getDownloadSize()) + " / "
						+ Utils.sizeFormat(task.getTotalSize()));
				updateIntent.putExtra(TransferIntent.PROCESS_PROGRESS, downloadpercent + "");
				updateIntent.putExtra(TransferIntent.URL, task.getUrl());
				updateIntent.putExtra(TransferIntent.DOWNLOADBEAN, task.transferBean);
				BroadcastManager.sendBroadcast(updateIntent);
			}

			/**
			 *暂停回调
			 */
			@Override
			public void pauseDownload(TransferTask task) {
				task.transferBean.setFileStatus(TransferTask.TASK_PAUSE);
				task.transferBean.setCurrentBytes(task.getDownloadSize());

				Intent updateIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
				updateIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.PAUSE);
				updateIntent.putExtra(TransferIntent.PROCESS_SPEED,
						mContext.getResources().getString(R.string.download_status_paused));
				updateIntent.putExtra(TransferIntent.PROCESS_PROMOT, Utils.sizeFormat(task.getDownloadSize()) + " / "
						+ Utils.sizeFormat(task.getTotalSize()));
				updateIntent.putExtra(TransferIntent.PROCESS_PROGRESS, task.getDownloadPercent() + "");
				updateIntent.putExtra(TransferIntent.URL, task.getUrl());
				updateIntent.putExtra(TransferIntent.DOWNLOADBEAN, task.transferBean);
				BroadcastManager.sendBroadcast(updateIntent);
				//				DownloadNotificationManager.getInstance(mContext).showDownloadingNotification();
			}

			/**
			 *连接中回调
			 */
			@Override
			public void preDownload(TransferTask task) {
				task.transferBean.setFileStatus(TransferTask.TASK_CONNECTING);

				Intent updateIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
				updateIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.PREDOWNLOAD);
				updateIntent.putExtra(TransferIntent.PROCESS_SPEED,
						mContext.getResources().getString(R.string.download_status_connecting));
				updateIntent.putExtra(TransferIntent.PROCESS_PROMOT, Utils.sizeFormat(task.getDownloadSize()) + " / "
						+ Utils.sizeFormat(task.getTotalSize()));
				updateIntent.putExtra(TransferIntent.PROCESS_PROGRESS, task.getDownloadPercent() + "");
				updateIntent.putExtra(TransferIntent.URL, task.getUrl());
				updateIntent.putExtra(TransferIntent.DOWNLOADBEAN, task.transferBean);
				BroadcastManager.sendBroadcast(updateIntent);
			}

			/**
			 *下载完成回调
			 */
			@Override
			public void finishDownload(TransferTask task) {
				completeTask(task);
			}

			/**
			 *下载失败回调
			 */
			@Override
			public void errorDownload(TransferTask task, Throwable error) {
				pauseErrorTask(task);

				task.transferBean.setFileStatus(TransferTask.TASK_PAUSE);
				task.transferBean.setCurrentBytes(task.getDownloadSize());

				String prompt = null;
				if (error != null) {
					if (error instanceof NetworkErrorException) {
						prompt = mContext.getResources().getString(R.string.download_status_network_error);
					} else if (error instanceof NoMemoryException) {
						prompt = mContext.getResources().getString(R.string.download_status_memory_error);
					} else if (error instanceof SocketException) {
						prompt = mContext.getResources().getString(R.string.download_status_network_error);
					} else if (error instanceof IOException) {
						prompt = mContext.getResources().getString(R.string.download_status_io_error);
					} else if (error instanceof IllegalArgumentException) {
						prompt = mContext.getResources().getString(R.string.download_status_illega_error);
					} else if (error instanceof ServerException) {
						prompt = mContext.getResources().getString(R.string.download_status_server_error);
					} else {
						prompt = mContext.getResources().getString(R.string.download_status_error);
					}
					//					ToastUtil.show(mContext, prompt + ",Download " + task.transferBean.getFileName() + " faild!",
					//							Toast.LENGTH_LONG);
				}
				Intent errorIntent = new Intent(TransferIntent.INTENT_UPDATE_UI);
				errorIntent.putExtra(TransferIntent.TYPE, TransferIntent.Types.ERROR);
				errorIntent.putExtra(TransferIntent.URL, task.getUrl());
				errorIntent.putExtra(TransferIntent.PROCESS_SPEED, prompt);
				errorIntent.putExtra(TransferIntent.PROCESS_PROMOT, Utils.sizeFormat(task.getDownloadSize()) + " / "
						+ Utils.sizeFormat(task.getTotalSize()));
				errorIntent.putExtra(TransferIntent.PROCESS_PROGRESS, task.getDownloadPercent() + "");
				errorIntent.putExtra(TransferIntent.URL, task.getUrl());
				errorIntent.putExtra(TransferIntent.DOWNLOADBEAN, task.transferBean);
				BroadcastManager.sendBroadcast(errorIntent);
			}

		};
		return new TransferTask(mContext, transferBean, taskListener);
	}

	private class TaskQueue {
		private Queue<TransferTask> taskQueue;

		public TaskQueue() {

			taskQueue = new LinkedList<TransferTask>();
		}

		public synchronized void offer(TransferTask task) {

			taskQueue.offer(task);//Inserts the specified element into this queue 

		}


		public TransferTask poll() {
			TransferTask task = null;
			while (mDownloadingTasks.size() >= MAX_DOWNLOAD_THREAD_COUNT || (task = taskQueue.poll()) == null) {

			}
			return task;
		}

		public TransferTask get(int position) {

			if (position >= size()) {
				return null;
			}
			return ((LinkedList<TransferTask>) taskQueue).get(position);
		}

		public int size() {
			return taskQueue.size();
		}

		@SuppressWarnings("unused")
		public boolean remove(int position) {

			return taskQueue.remove(get(position));
		}

		public boolean remove(TransferTask task) {

			return taskQueue.remove(task);
		}

		public void removeAll() {
			taskQueue.clear();
		}

		public boolean contains(TransferTask task) {
			return taskQueue.contains(task);
		}
	}

	/**
	 * 统计app下载，响应数据
	 */
	private Listener<JSONObject> myResponseListent = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {

			LogUtil.getLogger().d("response==>" + response.toString());
		}
	};

	/**
	 * 异常捕获
	 */
	private ErrorListener myErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			error.printStackTrace();
		}
	};

}
