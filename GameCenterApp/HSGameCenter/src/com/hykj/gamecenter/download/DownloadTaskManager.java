
package com.hykj.gamecenter.download;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.net.Uri;
import android.util.Log;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.db.CSACContentProvider;
import com.hykj.gamecenter.db.CSACDatabaseHelper.DownloadInfoColumns;
import com.hykj.gamecenter.db.DatabaseUtils;
import com.hykj.gamecenter.download.DownloadTask.TaskState;
import com.hykj.gamecenter.logic.NotificationCenter;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.utils.Interface.IDownloadTaskObserver;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.ThreadTask;
import com.hykj.gamecenter.utilscs.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 添加说明 单例，提供下载任务个数变化监听队列
 * <p/>
 * 提供所有与任务栈有关的操作，以及任务栈写入数据库和 数据库读入任务栈
 */
public class DownloadTaskManager {

	private static final String TAG = "DownloadTaskManager";

	private static DownloadTaskManager mDownloadTaskManager;

	/**
	 * appid和DownloadTask
	 */
	private final Map<Integer, DownloadTask> mAppid2DownloadInfoMap = new ConcurrentHashMap<Integer, DownloadTask>();

	/**
	 * 用于界面显示的列表，用map无法保证列表显示顺序
	 */
	private final ArrayList<DownloadTask> mDownloadInfoList = new ArrayList<DownloadTask>();

	/**
	 * 用于显示已下载应用的列表
	 */
	private final ArrayList<DownloadTask> mDownloadedList = new ArrayList<DownloadTask>();

	private final ArrayList<DownloadTask> mFileNotExistDownloadInfoList = new ArrayList<DownloadTask>();

	private final Object downloadInfoListlock = new Object();
	private final Object downloadedInfoListlock = new Object();

	/*
	 * 下载任务个数变化监听
	 */
	// 观察者列表
	private static final ArrayList<IDownloadTaskObserver> mDownloadTaskCountChangeListenerList = new ArrayList<IDownloadTaskObserver>();

	// modify at 20131128
	private void sendDownloadTaskCountChangeNotify() {
		for (IDownloadTaskObserver listener : mDownloadTaskCountChangeListenerList) {
			listener.onDownloadTaskCountChange();
		}
	}

	public static void addDownloadTaskCountChangeListener(IDownloadTaskObserver listener) {
		if (!mDownloadTaskCountChangeListenerList.contains(listener)) {
			mDownloadTaskCountChangeListenerList.add(listener);
		}
	}

	public static void removeDownloadTaskCountChangeListener(IDownloadTaskObserver listener) {
		if (!mDownloadTaskCountChangeListenerList.contains(listener)) {
			return;
		}
		mDownloadTaskCountChangeListenerList.remove(listener);
	}

	private DownloadTaskManager() {
	}

	// 操作同步锁，保证操作mPackageInfoMapLock的方法同时只有一个执行
	public static final Object mDownloadDBLock = new Object();
	public static final Object mDownloadedDBLock = new Object();

	public static synchronized DownloadTaskManager getInstance() {
		if (mDownloadTaskManager == null) {
			mDownloadTaskManager = new DownloadTaskManager();
		}
		return mDownloadTaskManager;
	}

	// appDetial to appInfo
	public static synchronized DownloadTask createTask(AppInfo appInfo, int nFromPos) {
		if (appInfo == null) {
			LogUtils.e("appInfo is null!");
			return null;
		}
		//这个处理 appinfo 生成 DownloadTask 可以放在 DownloadTask 中
		DownloadTask dinfo = new DownloadTask();

		dinfo.appName = appInfo.showName;
		dinfo.packageName = appInfo.packName;
		dinfo.appId = appInfo.appId;
		// dinfo.rating = appInfo.getRecommLevel( );
		dinfo.appIconURL = appInfo.iconUrl;
		dinfo.progress = 0;
		dinfo.setState(TaskState.PREPARING);
		dinfo.nFromPos = nFromPos;

		// AppDetail appdetail = appInfo.getAppDetail( );
		// if( appdetail != null )
		// {
		dinfo.appDownloadURL = appInfo.packUrl;
		dinfo.packId = appInfo.packId;
		dinfo.fileLength = appInfo.packSize;
		dinfo.packMD5 = appInfo.packMD5.isEmpty() ? null : appInfo.packMD5;
		dinfo.bRealAppDownloadURL = false;
		// }
		// else
		// LogUtils.e( "appdetail is still null!" );

		return dinfo;
	}

	/**
	 * 更新下载任务文件路径信息信息
	 *
	 * @param taskInfo
	 */
	public synchronized void updateTaskFileSavePath(DownloadTask taskInfo) {
		if (null == taskInfo) {
			//LogUtils.e( "taskInfo is null!" );
			Log.d(TAG, "taskInfo is null!");
			return;
		}

		DownloadTask dinfo = mAppid2DownloadInfoMap.get(taskInfo.appId);
		if (dinfo == null) {
			//LogUtils.e( " not find DownloadInfo appid=" + appInfo.getAppId( ) );
			Log.d(TAG, "taskInfo.appId is null!");
			return;
		}

		dinfo.fileSavePath = taskInfo.fileSavePath;

		// TODO: 2015/12/23 以下操作多余
		mAppid2DownloadInfoMap.put(taskInfo.appId, dinfo);
//
		//更新mDownloadInfoList,
		synchronized (downloadInfoListlock) {
			if (mDownloadInfoList.contains(taskInfo)) {
				mDownloadInfoList.remove(taskInfo);
			}
			mDownloadInfoList.add(dinfo);
		}
	}

	public synchronized DownloadTask updateTask(AppInfo appInfo) {
		if (appInfo == null) {
			LogUtils.e("appInfo is null!");
			return null;
		}

		DownloadTask dinfo = mAppid2DownloadInfoMap.get(appInfo.appId);
		if (dinfo == null) {
			LogUtils.e(" not find DownloadInfo appid=" + appInfo.appId);
			return null;
		}

		// dinfo.appId = appInfo.getAppId( );
		dinfo.appName = appInfo.showName;
		dinfo.packageName = appInfo.packName;
		// dinfo.rating = appInfo.getRecommLevel( );
		dinfo.appIconURL = appInfo.iconUrl;
		// dinfo.progress = 0;
		// dinfo.state = DownloadInfo.STATE_WAITTING;
		// dinfo.nFromPos = nFromPos;

		// AppDetail appdetail = appInfo.getAppDetail( );
		// if( appdetail != null )
		// {
		dinfo.appDownloadURL = appInfo.packUrl;
		dinfo.packId = appInfo.packId;
		dinfo.fileLength = appInfo.packSize;
		dinfo.packMD5 = appInfo.packMD5.isEmpty() ? null : appInfo.packMD5;
		dinfo.bRealAppDownloadURL = false;
		// }
		// else
		// LogUtils.e( "appdetail is still null!" );

		// LogUtils.d( "DownloadInfo=" + dinfo.toString( ) );

		return dinfo;
	}

	public synchronized boolean addNewTask(DownloadTask di) {
		if (di == null)
			return false;

		synchronized (downloadInfoListlock) {
			for (int i = 0; i < mDownloadInfoList.size(); i++) {
				// TODO :
				// 对于相同的处理，引起的可能性是更新版本下载时与之前的老版本冲突，因为appid一样，处理应该是删除老的，加入新的，老的删除时状态待处理
				if (Integer.valueOf(mDownloadInfoList.get(i).appId).equals(di.appId)) {
					return false;
				}
			}

			mDownloadInfoList.add(di);
			mAppid2DownloadInfoMap.put(Integer.valueOf(di.appId), di);
		}

		// sendDownloadUpdateNotification( );

		// 通知观察者
		sendDownloadTaskCountChangeNotify();
		return true;
	}

	/**
	 * 尽可能使用 appid 查找 Task
	 *
	 * @param packageName
	 * @return
	 */
	public synchronized DownloadTask getTask(String packageName) {
		for (DownloadTask dinfo : mAppid2DownloadInfoMap.values()) {
			if (dinfo.packageName.equals(packageName)) {
				return dinfo;
			}
		}
		return null;
	}

	public synchronized DownloadTask getTask(int appId) {
		for (DownloadTask dinfo : mAppid2DownloadInfoMap.values()) {
			if (dinfo.appId == appId) {
				return dinfo;
			}
		}
		return null;
	}

	// public synchronized void removeTask( String packageName )
	// {
	// DownloadTask dinfo = getTask( packageName );
	// if( dinfo == null )
	// return;
	// removeTask( dinfo );
	// }

	/**
	 * 删除任务包含三个方面
	 * <p/>
	 * 1.mDownloadInfoList 中删除任务
	 * <p/>
	 * 2.mAppid2DownloadInfoMap 中删除
	 * <p/>
	 * 3.下载数据库中删除
	 *
	 * @param dinfo
	 */
	public synchronized void removeTask(DownloadTask dinfo) {
		synchronized (downloadInfoListlock) {
			if (mDownloadInfoList.contains(dinfo)) {
				mDownloadInfoList.remove(dinfo);


			} else {
				Logger.e("ApkDownloadManager", "dinfo isnot in list=" + dinfo);
			}

			if (mAppid2DownloadInfoMap.containsKey(dinfo.appId)) {
				Logger.e("ApkDownloadManager", "移除任务" + dinfo.appName + "success!");
				mAppid2DownloadInfoMap.remove(dinfo.appId);
				synchronized (mDownloadDBLock) {

					try {
						App.getAppContext()
								.getContentResolver()
								.delete(CSACContentProvider.DOWNLOADINFO_CONTENT_URI,
										DownloadInfoColumns.APP_ID + " = " + dinfo.appId, null);
						Logger.e("ApkDownloadManager", "移除任务  dinfo.appId = " + dinfo.appId
								+ " dinfo.appName = "
								+ dinfo.appName);

					} catch (SQLiteDiskIOException e) {
						e.printStackTrace();
					}
				}
			} else {
				Logger.e("ApkDownloadManager", "dinfo isnot in map=" + dinfo);
			}

			// 通知观察者
			sendDownloadTaskCountChangeNotify();
			// sendDownloadUpdateNotification( );
		}
	}

	/**
	 * 删除已完成任务
	 * <p/>
	 * 1.mDownloadedList 中删除任务
	 * <p/>
	 * 2.下载已完成数据库中删除
	 *
	 * @param dinfo
	 */
	public synchronized void removeLoadedTask(DownloadTask dinfo) {
		synchronized (downloadedInfoListlock) {
			if (mDownloadedList.contains(dinfo)) {
				mDownloadedList.remove(dinfo);
			} else {
				Logger.e(TAG, "dinfo isnot in list=" + dinfo);
			}
			synchronized (mDownloadedDBLock) {

				try {
					App.getAppContext()
							.getContentResolver()
							.delete(CSACContentProvider.DOWNLOADEDINFO_CONTENT_URI,
									DownloadInfoColumns.APP_ID + " = " + dinfo.appId, null);
					Logger.e(TAG, "移除任务  dinfo.appId = " + dinfo.appId
							+ " dinfo.appName = "
							+ dinfo.appName);

				} catch (SQLiteDiskIOException e) {
					e.printStackTrace();
				}
			}


			// 通知观察者
//			saveDownloadedData();//保存已下载任务至数据库
//			sendDownloadTaskCountChangeNotify();
			// sendDownloadUpdateNotification( );
		}
	}

	public synchronized boolean addLoadedTask(DownloadTask di) {
		if (di == null)
			return false;

		synchronized (downloadedInfoListlock) {
			for (int i = 0; i < mDownloadedList.size(); i++) {
				// TODO :
				// 对于相同的处理，引起的可能性是更新版本下载时与之前的老版本冲突，因为appid一样，处理应该是删除老的，加入新的，老的删除时状态待处理
				if (Integer.valueOf(mDownloadedList.get(i).appId).equals(di.appId)) {
					return false;
				}
			}

			mDownloadedList.add(di);
		}

		sendDownloadTaskCountChangeNotify();

		// 通知观察者
		return true;
	}


	public synchronized boolean isAppInList(int appid) {
		boolean ret = false;
		for (DownloadTask downloadInfo : mAppid2DownloadInfoMap.values()) {
			if (downloadInfo.appId == appid) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public List<DownloadTask> getTaskList() {
		synchronized (downloadInfoListlock) {

			return mDownloadInfoList;
		}
	}

	/**
	 * 获取已完成列表
	 *
	 * @return
	 */
	public List<DownloadTask> getDownloadedTaskList() {
		synchronized (downloadedInfoListlock) {
			return mDownloadedList;
		}

	}

	// 正在下载中的任务
	public int getDownloadingTaskCount() {
		synchronized (downloadInfoListlock) {
			int i = 0;
			for (DownloadTask task : mDownloadInfoList) {
				if (task.getState() == TaskState.PREPARING || task.getState() == TaskState.WAITING || task.getState() == TaskState.STARTED || task.getState() == TaskState.LOADING) {
					i++;
				}
			}
			return i;
		}
	}

	/**
	 * 加载正在下载队列和已完成队列
	 */
	public synchronized void LoadAllTaskFromDB() {
		mFileNotExistDownloadInfoList.clear();
		synchronized (mDownloadDBLock) {
			try {
				Cursor cursor = App
						.getAppContext()
						.getContentResolver()
						.query(CSACContentProvider.DOWNLOADINFO_CONTENT_URI, null, null, null, null);
				Log.d(TAG, "查询任务 LoadAllTaskFromDB");
				if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
					while (!cursor.isAfterLast()) {
						DownloadTask dinfo = DatabaseUtils.createTaskFromCursor(cursor);

						// 判断下载成功的文件是否文件还存在
						if (dinfo.fileSavePath != null) {
							//xutils3.0 下载临时文件 默认 .tmp
							File file = null;
							if (dinfo.getState() == TaskState.SUCCEEDED) {
								file = new File(dinfo.fileSavePath);
							}else {
								file = new File(dinfo.fileSavePath + ".tmp");
							}
							if (dinfo.getState() == TaskState.SUCCEEDED && file.exists() == false) {
								dinfo.resetForDeleted();
								mFileNotExistDownloadInfoList.add(dinfo);
							} else {
								LogUtils.e("file.length( ) = " + file.length());
								Logger.i(TAG, "LoadAllTaskFromDB " + dinfo.fileSavePath + " file.length( ) " + file.length(), "oddshou");
								dinfo.setProgress(file.length());
							}
						}

						addNewTask(dinfo);
						cursor.moveToNext();
					}
				}

				if (cursor != null) {
					cursor.close();
				}
			} catch (SQLiteDiskIOException e) {
			}
		}
		//加载已完成队列
		synchronized (mDownloadedDBLock) {
			mDownloadedList.clear();
			Cursor cursor = null;
			try {
				cursor = App
						.getAppContext()
						.getContentResolver()
						.query(CSACContentProvider.DOWNLOADEDINFO_CONTENT_URI, null,
								null, null, null);
				if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
					while (!cursor.isAfterLast()) {
						DownloadTask task = DatabaseUtils.createTaskFromCursor(cursor);
						mDownloadedList.add(task);
						cursor.moveToNext();
					}
				}
			} catch (SQLiteDiskIOException e) {
				Log.e(TAG, e.toString());
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
			Logger.i(TAG, "LoadAllTaskFromDB " + "size " + mDownloadedList.size(), "oddshou");
		}


	}

	/**
	 * 清除文件不存在的任务 依赖于 {@link #LoadAllTaskFromDB()} 方法中找到文件不存在的任务
	 */
	public synchronized void clearNotFileTasks() {
		synchronized (mDownloadDBLock) {
			for (DownloadTask di : mFileNotExistDownloadInfoList) {
				try {
					App.getAppContext()
							.getContentResolver()
							.delete(CSACContentProvider.DOWNLOADINFO_CONTENT_URI,
									DownloadInfoColumns.APP_ID + " = " + di.appId, null);
					Log.d(TAG, "移除任务 clearNotFileTasks dinfo.appId = " + di.appId
							+ " dinfo.appName = " + di.appName);
				} catch (SQLiteDiskIOException e) {
					// TODO: handle exception
				}
			}
		}
	}

	/**
	 * @param info
	 */
	public synchronized void saveTaskToDB(final DownloadTask info) {
		ThreadTask.postTask(new Runnable() {
			@Override
			public void run() {
				synchronized (mDownloadDBLock) {
					if (isDownloadInfoExistInDB(info)) {

						try {
							App.getAppContext()
									.getContentResolver()
									.update(CSACContentProvider.DOWNLOADINFO_CONTENT_URI,
											DatabaseUtils.createContentValues(info),
											DownloadInfoColumns.APP_ID + " = " + info.appId, null);
							LogUtils.e("修改任务  saveTaskToDB  info.appId = " + info.appId
									+ " info.appName = "
									+ info.appName);
						} catch (SQLiteDiskIOException e) {
							// TODO: handle exception
						}
					} else {
						try {
							App.getAppContext()
									.getContentResolver()
									.insert(CSACContentProvider.DOWNLOADINFO_CONTENT_URI,
											DatabaseUtils.createContentValues(info));
							LogUtils.e("增加任务  saveTaskToDB  info.appId = " + info.appId
									+ " info.appName = "
									+ info.appName);
						} catch (SQLiteDiskIOException e) {//插入异常，说明在有线程读的时候写数据库，会抛出异常。
							// TODO: handle exception
						}
					}
				}
			}
		});
	}

	// 内部调用，在调用处加锁
	private synchronized static boolean isDownloadInfoExistInDB(DownloadTask dinfo) {
		boolean result = false;
		synchronized (mDownloadedDBLock) {
			Cursor cursor = null;
			try {
				cursor = App
						.getAppContext()
						.getContentResolver()
						.query(CSACContentProvider.DOWNLOADINFO_CONTENT_URI, null,
								DownloadInfoColumns.APP_ID + " = " + dinfo.appId, null, null);
				Log.d(TAG, "查询任务 isDownloadInfoExistInDB");
				if (cursor != null && cursor.getCount() > 0) {
					result = true;
				}
				if (cursor != null) {
					cursor.close();
				}

			} catch (SQLiteDiskIOException e) {

			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
			return result;
		}
	}

	/**
	 * @param info
	 */
	public synchronized void saveLoadedTaskToDB(final DownloadTask info) {
		if (mDownloadedList.contains(info)) {
			return;
		}
		//先使用数据库判断是否已经添加
		synchronized (mDownloadedDBLock) {

			try {
				Cursor cursor = App.getAppContext().getContentResolver().query(CSACContentProvider.DOWNLOADEDINFO_CONTENT_URI, null,
						DownloadInfoColumns.APP_ID + " = " + info.appId, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					return;
				}
				Uri uri = App.getAppContext()
						.getContentResolver()
						.insert(CSACContentProvider.DOWNLOADEDINFO_CONTENT_URI,
								DatabaseUtils.createContentValues(info));
				if (uri == null) {
					return;
				}
			} catch (SQLiteDiskIOException e) {//插入异常，说明在有线程读的时候写数据库，会抛出异常。
				// TODO: handle exception
			}
		}

		boolean reAdd = addLoadedTask(info);    //添加到队列
	}


	public void sendDownloadUpdateNotification() {
		final int count = getDownloadingCount();
		final int iProcess = getDownloadingTotalProgress();
		NotificationCenter.getInstance().sendDownloadNotification(count, iProcess);
	}

	// sendDownloadUpdateNotification后 更新进度条
	public void sendDownloadUpdateProgressNotification() {
		final int count = getDownloadingCount();
		final int iProcess = getDownloadingTotalProgress();
		NotificationCenter.getInstance().UpdateDownloadNotification(count, iProcess);
	}

	// 正在下载或者等待状态的数目
	public int getDownloadingCount() {
		synchronized (downloadInfoListlock) {
			int count = 0;
			for (DownloadTask info : mDownloadInfoList) {
				if (info.getState() == TaskState.LOADING || info.getState() == TaskState.PREPARING
						|| info.getState() == TaskState.WAITING
						|| info.getState() == TaskState.STARTED) {
					count++;
				}
			}
			return count;
		}
	}

	// 正在下载或者等待状态的任务的总进度
	private int getDownloadingTotalProgress() {
		synchronized (downloadInfoListlock) {

			double totalDownloadedSize = 0, totalFileSize = 0;
			for (DownloadTask info : mDownloadInfoList) {
				if (info.getState() == TaskState.LOADING || info.getState() == TaskState.PREPARING
						|| info.getState() == TaskState.WAITING
						|| info.getState() == TaskState.STARTED) {
					totalDownloadedSize += info.progress;
					totalFileSize += info.fileLength;
					/*
					 * Log.d(TAG, "info.progress = " + info.progress +
                     * " info.fileLength = " + info.fileLength);
                     */
				}
			}
			/*
			 * Log.d(TAG, "totalDownloadedSize = " + totalDownloadedSize +
             * " totalFileSize = " + totalFileSize);
             */
			if (totalFileSize != 0) {
				int percent = (int) ((totalDownloadedSize / totalFileSize) * 100);
				if (percent >= 100) {
					percent = 100;
				}
				return percent;
			}
			return 0;
		}
	}

	public synchronized void saveAllTaskToDB() {
		new Thread(new SaveDataToDBRunnable(mDownloadInfoList)).start();
//		new Thread(new SaveLoadedDataToDBRunnable(mDownloadedList)).start();
	}

	private class SaveDataToDBRunnable implements Runnable {

		private final List<DownloadTask> data;

		private SaveDataToDBRunnable(List<DownloadTask> data) {
			this.data = data;
		}

		@Override
		public void run() {
			synchronized (downloadInfoListlock) {
				if (data.size() <= 0) {
					LogUtils.e("no data to saveDataToDB");
					return;
				}

				for (DownloadTask info : data) {
					updateRecord(info);
				}
			}
		}

		private int updateRecord(DownloadTask info) {
			ContentResolver resolver = App.getAppContext().getContentResolver();
			synchronized (mDownloadDBLock) {
				try {
					int updateRow = resolver.update(CSACContentProvider.DOWNLOADINFO_CONTENT_URI,
							DatabaseUtils.createContentValues(info), DownloadInfoColumns.APP_ID + " = "
									+ info.appId, null);
					Log.d(TAG, "修改任务  saveTaskToDB  info.appId = " + info.appId
							+ " info.appName = " + info.appName);
					return updateRow;
				} catch (SQLiteDiskIOException e) {

				} catch (SQLiteReadOnlyDatabaseException e) {
					// TODO: handle exception
				}
			}
			return 0;
		}
	}

	public void removeTaskByPackageName(String packName) {
		DownloadTask task = getTask(packName);
		if (task != null) {
			// LogUtils.e( "removeTaskByPackageName,task.packName=" +
			// task.packageName );
			removeTask(task);
		}

	}
}
