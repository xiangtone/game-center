
package com.hykj.gamecenter.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.R;
import com.hykj.gamecenter.controller.ProtocolListener.ReqAppInfoListener;
import com.hykj.gamecenter.controller.ReqAppInfoController;
import com.hykj.gamecenter.data.SettingContent;
import com.hykj.gamecenter.download.DownloadTask.TaskState;
import com.hykj.gamecenter.mta.MtaUtils;
import com.hykj.gamecenter.protocol.Apps.AppInfo;
import com.hykj.gamecenter.statistic.MSG_CONSTANTS;
import com.hykj.gamecenter.statistic.ReportConstants;
import com.hykj.gamecenter.statistic.StatisticManager;
import com.hykj.gamecenter.ui.widget.CSToast;
import com.hykj.gamecenter.utils.DateUtil;
import com.hykj.gamecenter.utils.FileUtils;
import com.hykj.gamecenter.utils.Logger;
import com.hykj.gamecenter.utils.SilentInstallThreadTask;
import com.hykj.gamecenter.utils.StringUtils;
import com.hykj.gamecenter.utils.ThreadTask;
import com.hykj.gamecenter.utilscs.LogUtils;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.Executor;

//import com.lidroid.xutils.util.LogUtils;

/**
 * 这个类只会在 {@link DownloadService}中调用一次 oddshou
 */
public class ApkDownloadManager {
	private static final int MAX_DOWNLOAD_THREAD = 2;
	private final Executor executor = new PriorityExecutor(MAX_DOWNLOAD_THREAD);
	// Android Xutils 框架
//	private HttpUtils mHttpUtils = null;
	private final DownloadTaskManager mDownloadInfoManager;
	private boolean bFirstConnect;
	private boolean bDebug = false;
	public static final String TAG = "ApkDownloadManager";
	// store page where the app installing or updating from.
	private int mPagePostion = 0;

	/**
	 * 下载过程中各种状态的通知队列
	 */
	private final Vector<Handler> mRegisterListenerList = new Vector<Handler>();

	public ApkDownloadManager() {
		Logger.i(TAG, "ApkDownloadManager " + "init", "oddshou");
		bFirstConnect = true;
		mDownloadInfoManager = DownloadTaskManager.getInstance();
		mDownloadInfoManager.LoadAllTaskFromDB();//加载下载中 和已完成列表
		mDownloadInfoManager.clearNotFileTasks();

		PowerManager pm = (PowerManager) App.getAppContext()
				.getSystemService(Context.POWER_SERVICE);
//		mHttpUtils = new HttpUtils(pm);
		//设置当前请求的缓存时间
		//        mHttpUtils.configCurrentHttpCacheExpiry(0*1000);
		//设置默认请求的缓存时间
		//        mHttpUtils.configDefaultHttpCacheExpiry(0);

//		mHttpUtils.configRequestThreadPoolSize(MAX_DOWNLOAD_THREAD);
//		mHttpUtils.configResponseTextCharset("utf-8");

	}

	public void openDebugMode() {
		bDebug = true;
	}

	public void closeDebugMode() {
		bDebug = false;
	}

	public void registerListener(Handler handler) {
		// LogUtils.e("registerListener handler= " + handler);

		if (handler == null) {
			return;
		}
		if (!mRegisterListenerList.contains(handler)) {
			mRegisterListenerList.add(handler);
		}
	}

	public void unregisterListener(Handler handler) {
		// LogUtils.e("unregisterListener handler= " + handler);

		if (handler == null) {
			return;
		}

		if (mRegisterListenerList.contains(handler)) {
			mRegisterListenerList.remove(handler);
		}
	}

	public int getTaskCount() {
		return mDownloadInfoManager.getTaskList().size();
	}

	public int getDownloadingTaskCount() {
		return mDownloadInfoManager./*getDownloadingCount*/getDownloadingTaskCount();
	}

	public boolean isAppInTaskList(int nAppID) {
		return mDownloadInfoManager.isAppInList(nAppID);
	}

	public DownloadTask getDownloadTaskByPackageName(String packageName) {

		return mDownloadInfoManager.getTask(packageName);
	}

	public DownloadTask getDownloadTaskByAppId(int appId) {
		return mDownloadInfoManager.getTask(appId);
	}

	public List<DownloadTask> getTaskList() {
		return mDownloadInfoManager.getTaskList();
	}

	public List<DownloadTask> getDownloadedTaskList() {
		Logger.i(TAG, "getDownloadedInfo " + "size " + mDownloadInfoManager.getDownloadedTaskList().size(), "oddshou");
		return mDownloadInfoManager.getDownloadedTaskList();
	}

	public int getDownloadedTaskCount() {
		return mDownloadInfoManager.getDownloadedTaskList().size();
	}

	// 退出应用时，存储task信息
	public void saveAllTaskToDB() {
		if (mDownloadInfoManager == null)
			return;

		mDownloadInfoManager.saveAllTaskToDB();
	}

	// 升级界面开始下载任务独立出来
	public void startUpgradeDownload(AppInfo appInfo, int appPosition) {
		if (appInfo == null) {
			LogUtils.e("appinfo is null!");
			return;
		}

		// 创建下载task
		DownloadTask info = DownloadTaskManager.createTask(appInfo, appPosition);

		// 添加到下载队列中
		boolean bResult = mDownloadInfoManager.addNewTask(info);
		if (!bResult) {
			//CSToast.show( App.getAppContext( ) , App.getAppContext( ).getString( R.string.download_tip_same_id ) );
			LogUtils.e(App.getAppContext().getString(R.string.download_tip_same_id));
			LogUtils.w("appinfo is exist in download queue! | info=" + appInfo.toString() + "");
			return;
		}
		// 如果是新增的任务，需要将任务存储到本地
		mDownloadInfoManager.saveTaskToDB(info);
		// 请求详情数据
		doDownloadRequest(info);
		// 开始下载上报 2.0
		ReportConstants.reportUpgradeDownloadStart(info.appId, info.packId, info.nFromPos);
		MtaUtils.trackUpgradeRequest();
		MtaUtils.trackDownloadRequest("ApkDownloadManager");
		// mPostion = appPosition;

	}

	public void startUpgradeDownload(DownloadTask info) {
		if (info == null) {
			LogUtils.e("appinfo is null!");
			return;
		}

		// 添加到下载队列中
		boolean bResult = mDownloadInfoManager.addNewTask(info);
		if (!bResult) {
			//CSToast.show( App.getAppContext( ) , App.getAppContext( ).getString( R.string.download_tip_same_id ) );
			LogUtils.e(App.getAppContext().getString(R.string.download_tip_same_id));
			LogUtils.w("appinfo is exist in download queue! | info=" + info.toString() + "");
			return;
		}
		// 如果是新增的任务，需要将任务存储到本地
		mDownloadInfoManager.saveTaskToDB(info);
		// 请求详情数据
		doDownloadRequest(info);
		// 开始下载上报 2.0
		ReportConstants.reportUpgradeDownloadStart(info.appId, info.packId, info.nFromPos);
		MtaUtils.trackUpgradeRequest();
		MtaUtils.trackDownloadRequest("ApkDownloadManager");
		// mPostion = appPosition;

	}

	/**
	 * @param appInfo
	 * @param appPosition  统计具体位置
	 * @param pagePosition 这个参数弃用，使用appPosition 做统计
	 */
	public void startDownload(AppInfo appInfo, int appPosition, int pagePosition) {

		if (appInfo == null) {
			LogUtils.e("appinfo is null!");
			return;
		}

		// 创建下载task
		DownloadTask info = DownloadTaskManager.createTask(appInfo, appPosition);
		LogUtils.e(
				" startDownload info.appId = " + info.appId + " IsRealAppDownloadURL= "
						+ info.getIsRealAppDownloadURL());
		// 添加到下载队列中
		boolean bResult = mDownloadInfoManager.addNewTask(info);
		if (!bResult) {
			//	    CSToast.show( App.getAppContext( ) , App.getAppContext( ).getString( R.string.download_tip_same_id ) );
			LogUtils.e(App.getAppContext().getString(R.string.download_tip_same_id));
			LogUtils.w("appinfo is exist in download queue! | info=" + appInfo.toString() + "");
			return;
		}
		// 如果是新增的任务，需要将任务存储到本地
		mDownloadInfoManager.saveTaskToDB(info);
		// 请求详情数据
		doDownloadRequest(info);

		// 开始下载上报 2.0
		ReportConstants.reportDownloadStart(info.appId, info.packId, info.nFromPos);
		MtaUtils.trackDownloadRequest(ReportConstants.getPageFrom(mPagePostion));
	}

	//最后一个参数用于统计
	public void startDownload(AppInfo appInfo, int appPosition, int pagePosition, int flag,
							  int groupId) {

		if (appInfo == null) {
			LogUtils.e("appinfo is null!");
			return;
		}

		// 创建下载task
		DownloadTask info = DownloadTaskManager.createTask(appInfo, appPosition);
		info.setGroupId(groupId);
		Log.d(TAG,
				" startDownload info.appId = " + info.appId + " IsRealAppDownloadURL= "
						+ info.getIsRealAppDownloadURL());
		// 添加到下载队列中
		boolean bResult = mDownloadInfoManager.addNewTask(info);
		if (!bResult) {
			//      CSToast.show( App.getAppContext( ) , App.getAppContext( ).getString( R.string.download_tip_same_id ) );
			LogUtils.e(App.getAppContext().getString(R.string.download_tip_same_id));
			LogUtils.w("appinfo is exist in download queue! | info=" + appInfo.toString() + "");
			return;
		}
		// 如果是新增的任务，需要将任务存储到本地
		mDownloadInfoManager.saveTaskToDB(info);
		// 请求详情数据
		doDownloadRequest(info);

		// 开始下载上报 2.0
		ReportConstants.reportDownloadStart(info.appId, info.packId, info.nFromPos);
		MtaUtils.trackDownloadRequest(ReportConstants.getPageFrom(mPagePostion));
	}

	public void stopDownload(DownloadTask dinfo) {
//		if (!canStop(dinfo.getState())) {
//			LogUtils.e("state is error ! state=" + dinfo.getState().toString());
//			// return; 这里不能return ，否则不会执行  handler.stop( ); 删除数据库记录后，之前的请求下载有可能不成功，那么xutil可能重试，导致删除数据库记录后又建一条记录
//		}
//
		// preparing时dinfo中的handle为空，不能发送stop回调到上层ui
//		if (dinfo.getState() == TaskState.PREPARING) {
//			updateTaskState(dinfo, TaskState.STOPPED);
//			Log.d(TAG, "stopDownload  updateTaskState");
//			//return;这里不能return ，否则不会执行  handler.stop( ); 删除数据库记录后，之前的请求下载有可能不成功，那么xutil可能重试，导致删除数据库记录后又建一条记录
//		}
		//xutils 2.0
//		HttpHandler<File> handler = dinfo.getHttpHandler();
//		if (handler != null && !handler.isCancelled()) {
//			handler.cancel();
//			Log.d(TAG, "stopDownload  handler stop dinfo.appName =" + dinfo.appName);
//		} else {
//			LogUtils.e("handler = null");
//		}
		//xutils 3.0
		DownloadCallBack callBack = dinfo.getCallBack();
		if (callBack != null) {
			callBack.cancel();
			updateTaskState(dinfo, TaskState.STOPPED);
		}

		// 更新通知栏信息
		mDownloadInfoManager.sendDownloadUpdateNotification();

	}

	//这个方法的暂停将不改变cancel状态，为了不使cancel状态异常，这个方法弃用
	public void stopDownloadNoCallback(DownloadTask dinfo) {
		//xutils 3.0
		DownloadCallBack callBack = dinfo.getCallBack();
		if (callBack != null) {
			callBack.cancelNoCallback();
		updateTaskState(dinfo, TaskState.STOPPED);
		}

		// 更新通知栏信息
		mDownloadInfoManager.sendDownloadUpdateNotification();
	}

	public void stopAllDownload() {
		Logger.e(TAG, "stopAllDownload ", "oddshou");
		List<DownloadTask> downloadInfoList = mDownloadInfoManager.getTaskList();
		ArrayList<DownloadTask> loadingTasks = new ArrayList<DownloadTask>();
		for (DownloadTask downloadInfo : downloadInfoList) {
			//
			if (downloadInfo.getState() == TaskState.PREPARING
					|| downloadInfo.getState() == TaskState.WAITING) {
//					stopDownloadNoCallback(downloadInfo);
				stopDownload(downloadInfo);
				// 退出应用，此处暂停下载归为用户主动暂停
				ReportConstants.reportDownloadStop(downloadInfo.appId, downloadInfo.packId,
						downloadInfo.nFromPos,
						ReportConstants.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP, "退出应用归为主动暂停");
//				MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_USER_ACTIVE_STOP);
			}
			//正在下载的任务后暂停，否则会因为暂停开启排队任务，引发错误
			if (downloadInfo.getState() == TaskState.LOADING) {
				loadingTasks.add(downloadInfo);
			}
		}
		for (DownloadTask downloadInfo : loadingTasks){
			stopDownload(downloadInfo);
			// 退出应用，此处暂停下载归为用户主动暂停
			ReportConstants.reportDownloadStop(downloadInfo.appId, downloadInfo.packId,
					downloadInfo.nFromPos,
					ReportConstants.STAC_DOWNLOAD_APK_USER_ACTIVE_STOP, "退出应用归为主动暂停");
		}
		mDownloadInfoManager.sendDownloadUpdateNotification();
	}

	// 重头开始，重新下载
	public void restartDownload(DownloadTask dinfo) {
		Log.d(TAG, "restartDownload dinfo = " + dinfo);
		stopDownload(dinfo);
		deleteFile(dinfo);
		dinfo.reset();

		doDownloadRequest(dinfo);
	}

	public void stopDownloadForRemove(DownloadTask dinfo) {
//		if (!canStop(dinfo.getState())) {
//			LogUtils.e("state is error ! state=" + dinfo.getState().toString());
//			// return; 这里不能return ，否则不会执行  handler.stop( ); 删除数据库记录后，之前的请求下载有可能不成功，那么xutil可能重试，导致删除数据库记录后又建一条记录
//		}
//
		// preparing时dinfo中的handle为空，不能发送stop回调到上层ui
		if (dinfo.getState() == TaskState.PREPARING) {
			updateTaskState(dinfo, TaskState.DELETED);
			Log.d(TAG, "stopDownload  updateTaskState");
			//return;这里不能return ，否则不会执行  handler.stop( ); 删除数据库记录后，之前的请求下载有可能不成功，那么xutil可能重试，导致删除数据库记录后又建一条记录
		}

		DownloadCallBack callBack = dinfo.getCallBack();
		if (callBack != null) {
			updateTaskState(dinfo, TaskState.DELETED);
			callBack.cancel();
		}

//		HttpHandler<File> handler = dinfo.getHttpHandler();
//		if (handler != null && !handler.isCancelled()) {
//			updateTaskState(dinfo, TaskState.DELETED);
//
//			handler.cancel();
//			handler.cancel(true);
//			handler.setRequestCallBack(null);
//			//            handler.
//			dinfo.setHttpHandler(null);//add by firewang 防止回调OnStop时重新向数据库中添加已删除任务的记录
//			Log.d(TAG, "stopDownload  handler stop dinfo.appName =" + dinfo.appName);
//		} else {
//			LogUtils.e("handler = null");
//		}

		// 更新通知栏信息
		mDownloadInfoManager.sendDownloadUpdateNotification();

	}

	/**
	 * 下载过程中出错，删除下载任务
	 *
	 * @param dinfo
	 */
	public void removeDownload(DownloadTask dinfo) {
		stopDownloadForRemove(dinfo);   //停止下载
//		stopDownload(dinfo);
		mDownloadInfoManager.removeTask(dinfo);     //删除缓存和数据库

		Message msg = Message.obtain();
		msg.what = MSG_CONSTANTS.MSG_REFRESH_DOWNLOAD;
		sendMsgToUi(msg);

		deleteFile(dinfo);
		mDownloadInfoManager.sendDownloadUpdateNotification();
	}

	// 批量删除
	public void batchRemoveDownload(List<DownloadTask> list, ArrayList<DownloadTask> downloadTasks) {

		StringBuffer sbf = new StringBuffer("");
		for (DownloadTask dinfo : list) {
			removeDownload(dinfo);
		}

		int size = downloadTasks.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				DownloadTask task = downloadTasks.get(i);
//                int row = 0;
//                try {
				mDownloadInfoManager.removeLoadedTask(task);
//                    row = App.getAppContext().getContentResolver()
//                            .delete(CSACContentProvider.DOWNLOADEDINFO_CONTENT_URI,
//                                    DownloadInfoColumns.APP_ID + " = " + task.appId, null);
//                    Logger.e(TAG, "移除任务  dinfo.appId = " + task.appId
//                            + " dinfo.appName = "
//                            + task.appName);
//                    Logger.e(TAG, "移除任务  row === " + row);
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
			}
		}

		Message msg = Message.obtain();
		msg.what = MSG_CONSTANTS.MSG_BATCH_REFRESH_DOWNLOAD;
		msg.obj = sbf.toString();
		Log.d(TAG, "msg.obj===" + msg.obj);
		sendMsgToUi(msg);

	}

	/**
	 * 删除任务
	 *
	 * @param packName
	 */
	public void removeDownloadTask(String packName) {
		DownloadTask task = mDownloadInfoManager.getTask(packName);
		if (task != null) {
			mDownloadInfoManager.saveLoadedTaskToDB(task);
			mDownloadInfoManager.removeTask(task);
		}
	}

	//调用 removeDownload(DownloadTask dinfo) 删除任务
	public void removeDownload(String packageName) {
		Log.d(TAG, "removeDownload 1");
		DownloadTask di = mDownloadInfoManager.getTask(packageName);
		if (di == null)
			return;
		Log.d(TAG, "removeDownload 2");
		mDownloadInfoManager.saveLoadedTaskToDB(di);
		removeDownload(di);
	}

	public void removeLoadedTask(DownloadTask dinfo) {
		mDownloadInfoManager.removeLoadedTask(dinfo);
	}

	// 继续下载
	public void resumeDownload(DownloadTask dinfo) {
		// LogUtils.e("-----resumeDownload-----");
		Log.d(TAG, "-----resumeDownload-----");
		if (dinfo == null)
			return;
		// TODO: 2015/12/24 oddshou 修改
//		if (!canResume(dinfo.getState())) {
//			// LogUtils.e("state is error ! state=" +
//			// dinfo.getState().toString());
//			Log.d(TAG, "state is error ! state=" + dinfo.getState().toString());
//			return;
//		}
		// LogUtils.e("-----resumeDownload,doDownloadRequest-----");
		Log.d(TAG, "-----resumeDownload,doDownloadRequest-----");
		doDownloadRequest(dinfo);
	}

	// private final HashMap< Integer , Integer > installHashMap = new HashMap< Integer , Integer >( );

	public void installDownload(final DownloadTask dinfo) {
		if (dinfo == null || dinfo.fileSavePath == null) {
			return;
		}
		final File f = new File(dinfo.fileSavePath);
		if (!f.exists()) {
			//在非ui线程 弹 toast 会crash,但不是每次都会出现############### odddshou
			try {
				CSToast.show(App.getAppContext(),
						App.getAppContext().getString(R.string.download_tip_file_deleted));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dinfo.resetForDeleted();
			updateTaskState(dinfo, TaskState.DELETED);
			return;
		}

		// 更新安装状态

		DownloadService.getDownloadManager().onStartInstall(dinfo);
		LogUtils.i("正在安装：" + dinfo.appId + "|" + dinfo.appName);
		// 静默安装队列
		SilentInstallThreadTask.postTask(new Runnable() {
			@Override
			public void run() {
				// 安装游戏
				// LogUtils.e(
				// "FileUtils.install( dinfo.fileSavePath , f , dinfo ) = " +
				// FileUtils.install( dinfo.fileSavePath , f , dinfo ) );
				// installHashMap.clear( );
				// FileUtils.install( dinfo.fileSavePath , f , dinfo );
				// installHashMap.put( dinfo.appId , dinfo.appId );
				// if( !installHashMap.containsKey( dinfo.appId ) )
				// {
				LogUtils.i("安装dinfo.appName=" + dinfo.appName + "|dinfo.packageName="
						+ dinfo.packageName);
				FileUtils.install(dinfo.fileSavePath, f, dinfo);
				// }
			}
		});

	}

	public void setFirstConnect(boolean bFirstConnect) {
		this.bFirstConnect = bFirstConnect;
	}

	/***********************************************************/
	/* 网络监控和处理相关 */

	/***********************************************************/
	// 程序启动的第一次连接广播
	//private static final int MSG_DELAY_TIME = 3500;// 过滤频繁的切换事件

	// 网络切换监听 如果是从无连接切换到WIFI连接，则需要重启被中断的下载任务
	public void onNetworkChanged(Intent intent) {
		// LogUtils.d( "intent = " + intent + "|bFirstConnect=" + bFirstConnect
		// );
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			if (bFirstConnect) {
				bFirstConnect = false;
				return;
			}
			// 为了过滤频繁的间隔3秒内的切换事件，只留下最后一次稳定的状态即可
			Logger.d("resumeDownload", "onNetworkChanged MSG_RESUME_DOWNLOAD");
			mManagerHandler.removeMessages(MSG_RESUME_DOWNLOAD);
			Message msg = Message.obtain();
			msg.what = MSG_RESUME_DOWNLOAD;
			//  mManagerHandler.sendMessageDelayed( msg , MSG_DELAY_TIME ); 改为网络可用时不自动下载
		}
	}

	public void onInstallEnd(DownloadTask dinfo) {
		Logger.i(TAG, "onInstallEnd " + "succeeded", "oddshou");
		updateTaskState(dinfo, TaskState.SUCCEEDED);
	}

	public void onStartInstall(DownloadTask dinfo) {
		LogUtils.e("onStartInstall");
		updateTaskState(dinfo, TaskState.INSTALLING);

		// 安装开始更新note信息内容
		mDownloadInfoManager.sendDownloadUpdateNotification();
	}

	private boolean canResume(TaskState state) {
		return state == TaskState.STOPPED || state == TaskState.FAILED_NETWORK
				|| state == TaskState.FAILED_SERVER
				|| state == TaskState.FAILED_NOFREESPACE;

	}

	private boolean canStop(TaskState state) {
		return state == TaskState.PREPARING || state == TaskState.STARTED
				|| state == TaskState.LOADING
				|| state == TaskState.WAITING;

	}

	private final static int MSG_REQUEST_APPDETAIL = 0x01;
	private final static int MSG_REQUEST_DOWNLOAD = 0x02;
	private final static int MSG_UPDATE_DOWNLOADINFO = 0x03;
	private final static int MSG_RESUME_DOWNLOAD = 0x04;
	private final static int MSG_GET_91_REAL_PATH = 0x05;
	private final static int MSG_REQUEST_DOWNLOAD_FROM_GET_REAL_PATH = 0x06;

	@SuppressLint("HandlerLeak")
	private final Handler mManagerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_UPDATE_DOWNLOADINFO:

					DownloadTask info = mDownloadInfoManager.updateTask((AppInfo) msg.obj);
					// //更新下载任务后才上报数据
					// //用于统计
					// ReportInfo rinfo = new ReportInfo(
					// ReportLuachInfo.DOWNLOAD_APK , 0 , null , null , null );
					// ReportLaunchInfoController controller = new
					// ReportLaunchInfoController( rinfo , info );
					// controller.setClientPos( mPostion );
					// controller.doRequest( );
					// //开始下载上报 2.0
					// mStatisticManager.reportDownloadStart( info.appId ,
					// info.packId , info.nFromPos );

					if (info == null || info.getState() == TaskState.STOPPED)
						break;

					AppInfo appInfo = (AppInfo) msg.obj;
					LogUtils.e("appInfo.getPackUrl( )  === " + appInfo.packUrl);
					LogUtils.e("appInfo.getPackUrl2( ) === " + appInfo.packUrl2);
					if (appInfo.packUrl != null && !"".equals(appInfo.packUrl)) {
						LogUtils.e("-----请求真实链接开始  111-----");//run this 20150611 20:50 tom
						Req91DownloadPathController controller = new Req91DownloadPathController();
						controller.execute(appInfo.packUrl, info);
						LogUtils.e("-----请求真实链接结束  111-----");//run this 20150611 20:50 tom
					} else if (appInfo.packUrl2 != null && !"".equals(appInfo.packUrl2)) {
						LogUtils.e("-----请求真实链接开始   222-----");
						Req91DownloadPathController controller = new Req91DownloadPathController();
						controller.execute(appInfo.packUrl2, info);
						LogUtils.e("-----请求真实链接结束   222-----");
					} else {

						// 直接下载
						Message msgDownload = obtainMessage();
						msgDownload.what = MSG_REQUEST_DOWNLOAD;
						msgDownload.obj = info;//根据downloadUrl  info.getDownloadUrl( )
						sendMessage(msgDownload);
					}

					break;
				case MSG_REQUEST_DOWNLOAD_FROM_GET_REAL_PATH:

					DownloadTask dInfo = (DownloadTask) msg.obj;
					// //更新下载任务后才上报数据
					// //用于统计
					// ReportInfo rinfo = new ReportInfo(
					// ReportLuachInfo.DOWNLOAD_APK , 0 , null , null , null );
					// ReportLaunchInfoController controller = new
					// ReportLaunchInfoController( rinfo , info );
					// controller.setClientPos( mPostion );
					// controller.doRequest( );
					// //开始下载上报 2.0
					// mStatisticManager.reportDownloadStart( info.appId ,
					// info.packId , info.nFromPos );

					if (dInfo == null)
						break;

					LogUtils.e("请求真实链接开始");
					Log.d(TAG, "请求真实链接开始 MSG_REQUEST_DOWNLOAD_FROM_GET_REAL_PATH");
					Req91DownloadPathController controller = new Req91DownloadPathController();
					controller.execute(dInfo.getDownloadUrl(), dInfo);
					LogUtils.e("请求真实链接结束");

					break;
				case MSG_GET_91_REAL_PATH:
					LogUtils.e("appDownloadURL  ===" + ((DownloadTask) msg.obj).appDownloadURL);
					Message msgDownload = obtainMessage();
					msgDownload.what = MSG_REQUEST_DOWNLOAD;
					msgDownload.obj = msg.obj;
					sendMessage(msgDownload);
					break;
				case MSG_REQUEST_APPDETAIL:
					LogUtils.e("-----获取详情-----");
					sendAppDetailRequest((DownloadTask) msg.obj);
					break;
				case MSG_REQUEST_DOWNLOAD:
					doDownload((DownloadTask) msg.obj);
					break;
				case MSG_RESUME_DOWNLOAD:
					Logger.d("resumeDownload", "MSG_RESUME_DOWNLOAD");
					ConnectivityManager cm = (ConnectivityManager) App.getAppContext()
							.getSystemService(
									Context.CONNECTIVITY_SERVICE);
					NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					State wifi = null;
					if (wifiInfo != null) {
						wifi = wifiInfo.getState();
					}
					if (wifi != null && wifi == State.CONNECTED) {
						List<DownloadTask> result = mDownloadInfoManager.getTaskList();
						for (DownloadTask dinfo : result) {
							resumeDownload(dinfo);
						}
					} else {
						//wifi重连，这个函数应该执行不到
						stopAllDownload();
					}
					break;
			}
		}
	};

	private void doDownloadRequest(DownloadTask dinfo) {
		if (dinfo == null) {
			// LogUtils.e("info parameter is null !");
			Log.d(TAG, "info parameter is null !");
			return;
		}

		// 下载地址不存在，要获取
		/* 字符串比较不能用等号 */
		if (dinfo.appDownloadURL == null || dinfo.appDownloadURL.isEmpty()) {
			// LogUtils.e("doDownloadRequest,请求详情");
			LogUtils.e("doDownloadRequest,请求详情");
			Message msg = mManagerHandler.obtainMessage();
			msg.what = MSG_REQUEST_APPDETAIL;
			msg.obj = dinfo;
			mManagerHandler.sendMessage(msg);
			return;
		}

		LogUtils.e("doDownloadRequest,请求继续下载");
		Log.d(TAG, "doDownloadRequest,请求继续下载");
		Log.d(TAG,
				"dinfo.appId = " + dinfo.appId + " getIsRealAppDownloadURL= "
						+ dinfo.getIsRealAppDownloadURL());
		if (dinfo.getIsRealAppDownloadURL()) {
			Message msg = mManagerHandler.obtainMessage();
			msg.what = MSG_REQUEST_DOWNLOAD;
			msg.obj = dinfo;
			mManagerHandler.sendMessage(msg);
		} else {
			Message msg = mManagerHandler.obtainMessage();
			msg.what = /* MSG_REQUEST_DOWNLOAD */MSG_REQUEST_DOWNLOAD_FROM_GET_REAL_PATH;
			msg.obj = dinfo;
			mManagerHandler.sendMessage(msg);
		}
	}

	// 当下载地址不存在的情况下执行
	// GC2.0 详情中的数据转移到appInfo中
	private void sendAppDetailRequest(final DownloadTask dinfo) {
		updateTaskState(dinfo, TaskState.PREPARING);

		// packid ,scrtype 的值目前默认都为零
		ReqAppInfoController controller = new ReqAppInfoController(dinfo.appId, 0, 1,
				new ReqAppInfoListener() {
					@Override
					public void onNetError(int errCode, String errorMsg) {
						String errMsg = "errorCode=" + errCode + "|msg=" + errorMsg;
						LogUtils.e("errMsg=" + errMsg);
						updateTaskState(dinfo, TaskState.FAILED_NETWORK, errMsg);
						// 网络中断，任务暂停
						ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId,
								dinfo.nFromPos,
								ReportConstants.STAC_DOWNLOAD_APK_NETWORK_INTERRUPT_STOP,
								"errCode:" + errCode + "|errorMsg:"
										+ errorMsg);
						MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_NETWORK_INTERRUPT_STOP);
					}

					@Override
					public void onReqFailed(int statusCode, String errorMsg) {
						String errMsg = "errorCode=" + statusCode + "|msg=" + errorMsg;
						LogUtils.e("errMsg=" + errMsg);
						updateTaskState(dinfo, TaskState.FAILED_SERVER, errMsg);
						// 服务器繁忙等原因暂停
						ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId,
								dinfo.nFromPos,
								ReportConstants.STAC_DOWNLOAD_APK_SERVER_BUSY_STOP, "statusCode :"
										+ statusCode
										+ "|errorMsg:" + errorMsg);
						MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_SERVER_BUSY);
					}

					@Override
					public void onReqAppInfoSucceed(AppInfo info, String serverCacheVer) {
						// 未获取下载路径直接返回 不能下载
						if (info == null || TextUtils.isEmpty(info.packUrl)) {
							String errMsg = "appID=" + info.appId
									+ " cannot get appDetail info!";
							LogUtils.e("errMsg=" + errMsg);
							updateTaskState(dinfo, TaskState.FAILED_SERVER, errMsg);
							// 服务器繁忙等原因暂停
							ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId,
									dinfo.nFromPos,
									ReportConstants.STAC_DOWNLOAD_APK_SERVER_BUSY_STOP, errMsg);
							MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_SERVER_BUSY);
							return;
						}

						Message downlaodMSG = mManagerHandler.obtainMessage();
						downlaodMSG.what = MSG_UPDATE_DOWNLOADINFO;
						downlaodMSG.obj = info;
						mManagerHandler.sendMessage(downlaodMSG);
					}
				});

		controller.setClientPos(dinfo.nFromPos);
		controller.doRequest();
	}

	private boolean isDiskFreeSpaceAvailable(long nSize) {
		// 检查当前设备存储状况
		long size = FileUtils.getAvailableStorageSize();
		LogUtils.e("磁盘剩余空间,size=" + size);
		return size >= nSize + 1024 * 1024 * 10;

	}

	private synchronized void doDownload(DownloadTask dinfo) {

		if (dinfo == null) {
			LogUtils.e("di == null ");
			return;
		}

		if (dinfo.getDownloadUrl() == null) {
			LogUtils.e("di.getDownloadUrl( ) == null | di=" + dinfo);
			return;
		}

		// 下载前检测磁盘空间
		if (!isDiskFreeSpaceAvailable(dinfo.fileLength)) {
			LogUtils.e("Disk is full! diskFreeSpace = " + FileUtils.getAvailableStorageSize());
//			dinfo.setHttpHandler(null);
			updateTaskState(dinfo, TaskState.FAILED_NOFREESPACE);
			return;
		}
//		dinfo.fileSavePath = getTmpSaveFilePath(dinfo);// 下载文件的保存路径
		dinfo.fileSavePath = getFileNameByDownInfo(dinfo);// 下载文件的保存路径
		Logger.i(TAG, "downloadTask Info " + dinfo);
		Logger.i(TAG, "doDownload,dinfo.fileSavePath = " + dinfo.fileSavePath);
		Logger.i(TAG, "doDownload,dinfo.downloadUrl = " + dinfo.getDownloadUrl());
		//xutils 3.0
		DownloadCallBack dinfoCallBack = dinfo.getCallBack();
		if (dinfoCallBack != null) {
			Logger.e(TAG, "doDownload " + dinfoCallBack.isCancelled(), "oddshou");
//			dinfoCancelable.cancel();
			if (dinfoCallBack.isCancelled()) {
				dinfo.setTaskCallback(new TaskCallback(dinfo));
				// 设置状态
				updateTaskState(dinfo, TaskState.PREPARING);
				return;
			}else {
				dinfo.setTaskCallback(null);
			}
		}

		createHttp(dinfo);
		//xutils 2.0
//		HttpHandler<File> handler = mHttpUtils.download(dinfo.getDownloadUrl(), dinfo.fileSavePath,
//				true/* autoResume 如果目标文件存在，接着未完成的部分继续下载 */, false, /* 如果从请求返回信息中获取到文件名，下载完成后自动重命名 */
//				new DownloadRequestCallBack(dinfo));
//		handler.supportCancel();
//		dinfo.setHttpHandler(handler);

		// 设置状态
		updateTaskState(dinfo, TaskState.WAITING);

		// 更新通知栏信息
		mDownloadInfoManager.sendDownloadUpdateNotification();
	}

	private void createHttp(DownloadTask task) {
		DownloadCallBack callBack = new DownloadCallBack(task);
		RequestParams params = new RequestParams(task.getDownloadUrl());
		params.setAutoResume(true);
		params.setAutoRename(false);
		params.setSaveFilePath(task.fileSavePath);
		params.setExecutor(executor);
		params.setCancelFast(true);
		Callback.Cancelable cancelable = x.http().get(params, callBack);
		callBack.setCancelable(cancelable);
		task.setCallBack(callBack);
	}

	/**
	 * 解决在cancel还没有结束的时候立即又开始会异常的bug
	 * 解决方法，设置TaskCallback，cancel结束后调用
	 */
	public class TaskCallback{

		private final DownloadTask task;

		public TaskCallback(DownloadTask task) {
			this.task = task;
		}

		public void doDownload(){
			createHttp(task);
			// 设置状态
			updateTaskState(task, TaskState.WAITING);
			// 更新通知栏信息
			mDownloadInfoManager.sendDownloadUpdateNotification();
			task.setTaskCallback(null);
		}


	}


	// 删除已下载文件
	private void deleteFile(DownloadTask dinfo) {
		if (dinfo == null) {
			LogUtils.e("dinfo is null");
			return;
		}

		if (TextUtils.isEmpty(dinfo.getFileSavePath()))
			return;

		// IO操作，用子线程异步操作
		final String deleteFilePath = dinfo.getFileSavePath();// 已下载成功的文件

//		final String tmpFilePath = getTmpSaveFilePath(dinfo);// 下载过程中的临时文件
		final String tmpFilePath = getFileNameByDownInfo(dinfo);// 下载过程中的临时文件
		ThreadTask.postTask(new Runnable() {
			@Override
			public void run() {
				File file = new File(deleteFilePath);
				if (file.exists()) {
					file.delete();
				}

				File fileTmp = new File(tmpFilePath);
				if (fileTmp.exists()) {
					fileTmp.delete();
				}
			}
		});
	}

	// 得到下载临时文件保存路径
//	public String getTmpSaveFilePath(DownloadTask dinfo) {
//		// String url = dinfo.getDownloadUrl();
//		String path = getFileNameByDownInfo(dinfo) + ".tmp";
//		return path;
//	}

	/**
	 * 从下载链接中提取文件名，再转化为本地保存路径
	 *
	 * @param
	 * @return
	 */
	private static String getFileNameByDownInfo(DownloadTask dinfo) {
		Log.d(TAG, "getFileNameByDownInfo");
		String fileName = getFileNameByDownInfo(dinfo, ".apk");
		Log.d(TAG, "getFileNameByDownInfo fileName = " + fileName);
		return getDownloadDir() + fileName + ".apk";
	}

	// download fileUrl =
	// http://apps.wandoujia.com/redirect?signature=b468318&url=http%3A%2F%2Fapk.wandoujia.com%2F6%2F7f%2Feb5df5a8050bf04a2b5a6163d10d37f6.apk&pn=com.lingan.yunqi&md5=eb5df5a8050bf04a2b5a6163d10d37f6&apkid=11834275&vc=1&size=8736764&pos=open/allApps&tokenId=niuwan&appType=APP

	/**
	 * 从下载链接中提取文件名
	 *
	 * @param
	 * @return
	 */
	public static String getFileNameByDownInfo(DownloadTask dinfo, String suffix) {
		String fileUrl = dinfo.getDownloadUrl();
		// 这里如果fileUrl相同，目标文件名称必须相同，为了断点续传
		Log.d(TAG, "download fileUrl = " + fileUrl);

        /*
		 * String fileName = ""; if (fileUrl != null) { int idx =
         * fileUrl.indexOf("?"); if (idx > 0) {
         * 
         * fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, idx);
         * 
         * fileName = fileUrl.substring(idx + 1); } else { fileName = fileUrl; }
         * }
         * 
         * int index = fileName.indexOf(suffix); if (index > 0) { fileName =
         * fileName.substring(0, index); }
         */

		// 不能有中文名
		String fileName = /* dinfo.appName + */dinfo.appId /* + dinfo.fileLength */
				+ dinfo.packageName + dinfo.packId + dinfo.packMD5;
		// Log.v(TAG, "fileName:"+fileName);
		// 保证文件名的唯一性
		// fileName = fileName + System.currentTimeMillis();
		// 转换成英文小写
		return fileName.toLowerCase(Locale.getDefault());
	}

	/**
	 * 从下载链接中提取文件名，再转化为本地保存路径
	 *
	 * @param
	 * @return
	 */
	//    private static String getFileNameFromUrl( String fileUrl ) {
	//	String fileName = FileUtils.decodeUrl2FileName( fileUrl , ".apk" );
	//	return getDownloadDir( ) + fileName + ".apk";
	//    }
	private static String getDownloadDir() {
		Log.d(TAG, "getDownloadDir");
		String path = FileUtils.getStorePath(FileUtils.APK_DIR);

		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		return path;
	}

	private File MoveFileToDownloadPath(File srcFile, DownloadTask dinfo) {
		String newFileName = /*
							 * FileUtils.decodeUrl2FileName(
                             * dinfo.appDownloadURL, ".apk")
                             */getFileNameByDownInfo(dinfo);

		Log.d(TAG,
				"MoveFileToDownloadPath newFileName =" + newFileName + " srcFile = "
						+ srcFile.getAbsolutePath());
		File newFileFullPath = srcFile;
		if (!TextUtils.isEmpty(newFileName)) {
			if (!newFileName.endsWith(".apk")) {
				newFileName = newFileName + ".apk";
			}

			//注意这里的newFileName带路径
			newFileFullPath = new File(newFileName);

			//System.currentTimeMillis( )

			while (newFileFullPath.exists()) {
				int iSplitIndex = newFileName.lastIndexOf(".apk");
				if (iSplitIndex < 0) {
					newFileName = newFileName + ".apk";
				}
				iSplitIndex = newFileName.lastIndexOf(".apk");

				newFileName = newFileName.subSequence(0,
						iSplitIndex - 1 < 0 ? iSplitIndex : iSplitIndex - 1)
						.toString()
						+ System.currentTimeMillis() + ".apk";

				newFileFullPath = new File(newFileName);
			}

			Log.d(TAG, "1 newFileFullPath = " + newFileFullPath);
			boolean bRet = srcFile.renameTo(newFileFullPath);
			Log.d(TAG, "bRet =" + bRet);
			if (bRet) {
				//newFileFullPath = newFileFullPath;
			} else {

                /**/
				FileUtils.fileChannelCopy(srcFile, newFileFullPath); // 将旧文件拷贝给新文件
				Log.d(TAG, srcFile + " -> " + newFileFullPath);
				//清空旧文件
				//FileUtils.emptyFileContent( srcFile );
				//newFileFullPath = srcFile;
				if (srcFile != null && srcFile.exists()) {
					srcFile.delete();
				}
			}

		}
		//更新下载任务文件路径信息信息
		mDownloadInfoManager.updateTaskFileSavePath(dinfo);
		Log.d(TAG, "MoveFileToDownloadPath newFileFullPath =" + newFileFullPath);
		return newFileFullPath;
	}

	public class DownloadCallBack implements Callback.CommonCallback<File>,
			Callback.ProgressCallback<File>,
			Callback.Cancelable {

		private boolean cancelled = false;
		private Cancelable cancelable;

		private final DownloadTask dinfo;

		// for debug
		private long preLength = 0;
		private long preTime;
		private long startTime;
		private long partialLen = 0;

		// private long speedAvg = 0;

		MediaScannerConnection mConnection = null; //通知MediaProvider数据库改变

		public DownloadCallBack(DownloadTask downloadInfo) {
			this.dinfo = downloadInfo;
		}

		public void setCancelable(Cancelable cancelable) {
			this.cancelable = cancelable;
		}

		public void cancelNoCallback(){
			if (cancelable != null) {
//				cancelled = true;
				cancelable.cancel();
//				dinfo.setCancelable(null);

			}
		}

		@Override
		public void cancel() {
			if (cancelable != null) {
				cancelled = true;
				cancelable.cancel();
//				dinfo.setCancelable(null);
				dinfo.setCallBack(null);
			}
			Logger.e(TAG, "cancel " + " cancelable "+ cancelable.isCancelled() , "oddshou");
		}

		@Override
		public boolean isCancelled() {
			return cancelled;
		}

		@Override
		public void onFinished() {
			Logger.e(TAG, "onFinished " + "", "oddshou");
			cancelled = false;
//			dinfo.setCancelable(null);
		}

		@Override
		public void onWaiting() {
			Logger.e(TAG, "onWaiting " + "", "oddshou");

		}

		@Override
		public void onStarted() {
			Logger.e(TAG, "onStarted " + "", "oddshou");
			updateTaskState(dinfo, TaskState.LOADING);

			startTime = preTime = System.currentTimeMillis();
			preLength = 0;
			LogUtils.e("onStart ,dinfo.progress=" + dinfo.progress);
			if (dinfo.progress > 0) {
				partialLen = dinfo.progress;
			}
		}

		@Override
		public void onCancelled(CancelledException e) {
			Logger.e(TAG, "Handler onStopped dinfo.appName = " + dinfo.appName + " dinfo.state = "
					+ dinfo.getState());
			TaskCallback taskCallback = dinfo.getTaskCallback();
			if (taskCallback != null) {
				taskCallback.doDownload();
			}
		}

		@Override
		public void onLoading(long total, long current, boolean isDownloading) {
			if (!isDownloading) {
				return;
			}

			if (current < partialLen) {
				partialLen = current;
			}
			dinfo.progress = current;
			dinfo.fileLength = total;

			Message msg = Message.obtain();
			msg.what = MSG_CONSTANTS.MSG_UPDATE_PROGRESS;
			// msg.arg1 = (int)dinfo.fileLength;
			// msg.arg2 = (int)dinfo.progress;
			msg.obj = dinfo;// .packageName;

			// 计算瞬时速度
			long len = current - preLength;
			long nowTime = System.currentTimeMillis();
			long time = nowTime - preTime;
			long speed = (time != 0) ? (len * 1000 / time) : time;

			preLength = current;
			preTime = nowTime;

			// 计算平均速度
			long times = nowTime - startTime;
			long currentNet = current - partialLen;
			dinfo.avgSpeed = (times != 0) ? (currentNet * 1000 / times) : times;
			// LogUtils.d( "total: " + total + "  current : " + current +
			// "   partialLen: " + partialLen + "  times: " + times );
			// 计算剩余时间
			dinfo.remainSeconds = 0;
			if (dinfo.avgSpeed != 0) {
				dinfo.remainSeconds = (long) ((float) (total - current) / (float) dinfo.avgSpeed);
			}
			// String remainStr = DateUtil.remainTime( (long)remainSeconds );

			if (bDebug) {
				dinfo.stateMsg = "下载速度: " + StringUtils.byteToString(speed);
			}

			sendMsgToUi(msg);
		}

		@Override
		public void onSuccess(File file) {
			Logger.e(TAG, "onSuccess " + "", "oddshou");
//			dinfo.setHttpHandler(null);
			final long spendTime = System.currentTimeMillis() - startTime;
			String remainStr = DateUtil.remainTime(spendTime / 1000);
			if (bDebug) {
				dinfo.stateMsg = "总时长： " + remainStr + " |平均速度: "
						+ StringUtils.byteToString(dinfo.avgSpeed) + "/s";
			}

//			final File srcFile = responseInfo.result;
			final File srcFile = file;
			new Thread() {
				public void run() {
					doSuccessed(srcFile, spendTime + "", dinfo.avgSpeed + "");
				}

			}.start();

		}

		//oddshou############ 添加 synchronized 以防ANR
		private synchronized void doSuccessed(File srcFile, String spendTime, String speed) {
			//xutils 3.0 下载临时文件.tmp 下载完成后改回来
//			final File dstFile = MoveFileToDownloadPath(srcFile, dinfo);
			final File dstFile = srcFile;
			dinfo.setFileSavePath(dstFile.getPath());
			Log.d(TAG, "dinfo path = " + dinfo.fileSavePath);
			// LogUtils.e( "packMD5 =" + dinfo.packMD5 );
			// 判断apk文件是否损坏
			if (dinfo.packMD5 != null
					&& FileUtils.isApkFileBroken(dstFile.getPath(), dinfo.packMD5)) {
				String errMsg = "apk is broken! file=" + dstFile.getPath() + "| length="
						+ dstFile.length();
				LogUtils.e(errMsg);
				// TODO debug时先不删除
				// deleteFile( dinfo );
				updateTaskState(dinfo, TaskState.FAILED_BROKEN, errMsg);
				// 上报下载失败
				ReportConstants.reportDownloadFail(dinfo.appId, dinfo.packId, dinfo.nFromPos,
						StatisticManager.STAC_DOWNLOAD_APK_ERROR_FILE_BROKEN, "downloadinfo="
								+ dinfo.toString());
				MtaUtils.trackDownloadFailed(StatisticManager.ERROR_DOWNLOAD_FILE_BROKEN);
				return;
			}

			Logger.i(TAG, "doSuccessed " + "succeeded", "oddshou");
			updateTaskState(dinfo, TaskState.SUCCEEDED, dinfo.stateMsg);

			// 播放下载完成提示语音,gamecenter下载完成后去除
			//	    App.playPrompt( );

			//通知MediaProvider数据库改变
			//因为建立连接是一个异步过程，所以，在建立连接时需要加入一个监听器。这样，连接建立后就可以得到通知，并进行下一步

			Log.d(TAG, "dstFile AbsolutePath = " + dstFile.getAbsolutePath());

			mConnection = new MediaScannerConnection(App.getAppContext(),
					new MediaScannerConnection.MediaScannerConnectionClient() {
						@Override
						public void onMediaScannerConnected() {
							if (mConnection != null) {
								//更新媒体数据库  这也是一个异步过程，更新完毕后，监听器中得到通知，并断开连接
								mConnection.scanFile(dstFile.getAbsolutePath(), null /* mimeType */);
							}
						}

						@Override
						public void onScanCompleted(String path, Uri uri) {
							if (path.equals(dstFile.getAbsolutePath().toString())) {
								if (mConnection != null) {
									//断开连接
									mConnection.disconnect();
								}
							}
						}
					});
			if (mConnection != null) {
				mConnection.connect();
			}

			// 上报下载成功 ,带上总下载时长 和 速度
			LogUtils.e("上报下载数据,spendTime=" + spendTime + "|speed=" + speed);
			if (speed.isEmpty() || spendTime.isEmpty()) {
			} else {
				speed = (Long.parseLong(speed) / 1024) + "";
			}
			MtaUtils.trackDownloadSuccess(dinfo.appName);
			Logger.e("DownloadStateViewCustomization", "dinfo.nFromPos====" + dinfo.nFromPos);
			ReportConstants.reportDownloadSuccessed(dinfo.appId, dinfo.packId, dinfo.nFromPos,
					spendTime, speed, dinfo.groupId);
//			new ReportSpeedThread(dinfo.appName, speed).start();		//MTA report
			// 检查设置，看是否自动安装
			if (SettingContent.getInstance().getSettingData().bDownloadedToSetup) {
				LogUtils.w("自动安装开始！");
				installDownload(dinfo); //################ oddshou 如果找不到安装包会弹出 toast，但当前不在 ui线程中,会 crash
			}
			if (bDebug) {
				FileUtils.writeDebugLog("上报下载数据,spendTime=" + spendTime + "|speed=" + speed);
			}
		}

		private final static int HTTPEXCEPTION_CODE_CLIENT_PROTOCOL = 0; //add by firewang
		private final static int HTTPEXCEPTION_CODE_FILE_NOEXIST = 404;

		private final static int HTTPEXCEPTION_CODE_5XX_START = 500;
		private final static int HTTPEXCEPTION_CODE_5XX_END = 599;

		private final static int HTTPEXCEPTION_CODE_4XX_START = 400;
		private final static int HTTPEXCEPTION_CODE_4XX_END = 499;

		@Override
		public void onError(Throwable throwable, boolean isOnCallback) {
			Logger.e(TAG, "onError " + "isOnCallback = " + isOnCallback + throwable.toString(), "oddshou");
//			dinfo.setHttpHandler(null);
			if (throwable instanceof HttpException) {
				HttpException httpException = (HttpException) throwable;
				String errorMsg = "errorCode=" + httpException.getCode() + "|msg=" + httpException.getMessage();
				LogUtils.e("HttpException error|" + errorMsg);
				Log.d(TAG, "HttpException error|" + errorMsg);
				if (bDebug) {
					FileUtils.writeDebugLog(errorMsg);
				}

				int errorCode = httpException.getCode();
				if (errorCode == HTTPEXCEPTION_CODE_FILE_NOEXIST) {
					updateTaskState(dinfo, TaskState.FAILED_NOEXIST, errorMsg);
				} else if (errorCode >= HTTPEXCEPTION_CODE_4XX_START
						&& errorCode <= HTTPEXCEPTION_CODE_4XX_END
			/* || errorCode == HTTPEXCEPTION_CODE_CLIENT_PROTOCOL*/) { //firewang add errorCode == HTTPEXCEPTION_CODE_CLIENT_PROTOCOL
					// 当前ngix服务器，对于416的错误是返回400的，文件有可能刚好下载完成后出错，导致当成文件损坏来处理
					File desFile = new File(dinfo.getFileSavePath());
					if (desFile.length() == dinfo.getFileLength() && dinfo.getFileLength() > 0) {
						dinfo.progress = dinfo.getFileLength();
						doSuccessed(desFile, "", "");
						return;
					}

					updateTaskState(dinfo, TaskState.FAILED_BROKEN, errorMsg);
				} else if (errorCode == HTTPEXCEPTION_CODE_CLIENT_PROTOCOL) {//tomqian 支持服务器返回错误码=0时，改变task的状态
					//为stop状态，为支持恢复网络时断点续传，此处为隐藏风险点
					updateTaskState(dinfo, TaskState.STOPPED, errorMsg);

				} else if (errorCode >= HTTPEXCEPTION_CODE_5XX_START
						&& errorCode <= HTTPEXCEPTION_CODE_5XX_END) {
					updateTaskState(dinfo, TaskState.FAILED_SERVER, errorMsg);
					// 服务器繁忙等原因暂停
					ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId, dinfo.nFromPos,
							ReportConstants.STAC_DOWNLOAD_APK_SERVER_BUSY_STOP, errorCode + ":"
									+ errorMsg);
					MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_SERVER_BUSY);
				}
			} else {
				updateTaskState(dinfo, TaskState.FAILED_NETWORK, "error");

				LogUtils.e("TaskState.FAILED_NETWORK =" + dinfo.toString() + "|errorCode = "
						+ throwable);
				Log.d(TAG, "TaskState.FAILED_NETWORK =" + dinfo.toString() + "|errorCode = "
						+ throwable);
			}
			// 上报下载失败
			ReportConstants.reportDownloadFail(dinfo.appId, dinfo.packId, dinfo.nFromPos,
					StatisticManager.STAC_DOWNLOAD_APK_ERROR_NETWORK, "error " + throwable);
			MtaUtils.trackDownloadFailed(StatisticManager.ERROR_DOWNLOAD_NETWORK);


		}
	}

//	private class DownloadRequestCallBack extends RequestCallBack<File> {
//		private final DownloadTask dinfo;
//
//		// for debug
//		private long preLength = 0;
//		private long preTime;
//		private long startTime;
//		private long partialLen = 0;
//
//		// private long speedAvg = 0;
//
//		MediaScannerConnection mConnection = null; //通知MediaProvider数据库改变
//
//		private DownloadRequestCallBack(DownloadTask downloadInfo) {
//			this.dinfo = downloadInfo;
//		}
//
//		@Override
//		public void onStart() {
//			Logger.i(TAG, "onStart()");
//			updateTaskState(dinfo, TaskState.LOADING);
//
//			startTime = preTime = System.currentTimeMillis();
//			preLength = 0;
//			LogUtils.e("onStart ,dinfo.progress=" + dinfo.progress);
//			if (dinfo.progress > 0) {
//				partialLen = dinfo.progress;
//			}
//
//		}
//
//		;
//
//		@Override
//		public void onCancelled() {
//			Logger.e(TAG, "onCancelled()");
//			Logger.e(TAG, "Handler onStopped dinfo.appName = " + dinfo.appName + " dinfo.state = "
//					+ dinfo.getState());
//			dinfo.setHttpHandler(null);
//			if (dinfo.getState() != TaskState.DELETED) {
//				updateTaskState(dinfo, TaskState.STOPPED);
//			}
//			Logger.e(TAG, "Handler onStopped dinfo.appName = " + dinfo.appName + " dinfo.state = "
//					+ dinfo.getState());
//		}
//
//		// 下载中
//		@Override
//		public void onLoading(long total, long current, boolean isUploading) {
//			//            Logger.i(TAG, "onLoading()");
//			if (current < partialLen) {
//				partialLen = current;
//			}
//			dinfo.progress = current;
//			dinfo.fileLength = total;
//
//			Message msg = Message.obtain();
//			msg.what = MSG_CONSTANTS.MSG_UPDATE_PROGRESS;
//			// msg.arg1 = (int)dinfo.fileLength;
//			// msg.arg2 = (int)dinfo.progress;
//			msg.obj = dinfo;// .packageName;
//
//			// 计算瞬时速度
//			long len = current - preLength;
//			long nowTime = System.currentTimeMillis();
//			long time = nowTime - preTime;
//			long speed = (time != 0) ? (len * 1000 / time) : time;
//
//			preLength = current;
//			preTime = nowTime;
//
//			// 计算平均速度
//			long times = nowTime - startTime;
//			long currentNet = current - partialLen;
//			dinfo.avgSpeed = (times != 0) ? (currentNet * 1000 / times) : times;
//			// LogUtils.d( "total: " + total + "  current : " + current +
//			// "   partialLen: " + partialLen + "  times: " + times );
//			// 计算剩余时间
//			dinfo.remainSeconds = 0;
//			if (dinfo.avgSpeed != 0) {
//				dinfo.remainSeconds = (long) ((float) (total - current) / (float) dinfo.avgSpeed);
//			}
//			// String remainStr = DateUtil.remainTime( (long)remainSeconds );
//
//			if (bDebug) {
//				dinfo.stateMsg = "下载速度: " + StringUtils.byteToString(speed);
//			}
//
//			sendMsgToUi(msg);
//
//			// LogUtils.e( "msg =" + msg.obj.toString( ) );
//			// LogUtils.e( "dinfo=" + dinfo.toString( ) );
//
//			// LogUtils.d( "current = " + current + "|currentNet=" + currentNet
//			// + "|total=" + total + "|bDebug=" + bDebug + "|partialLen=" +
//			// partialLen );
//			// LogUtils.d( "dinfo.stateMsg = " + dinfo.stateMsg );
//		}
//
//		// 下载完成
//		@Override
//		public void onSuccess(ResponseInfo<File> responseInfo) {
//			Logger.i(TAG, "onSuccess()");
//			dinfo.setHttpHandler(null);
//			final long spendTime = System.currentTimeMillis() - startTime;
//			String remainStr = DateUtil.remainTime(spendTime / 1000);
//			if (bDebug) {
//				dinfo.stateMsg = "总时长： " + remainStr + " |平均速度: "
//						+ StringUtils.byteToString(dinfo.avgSpeed) + "/s";
//			}
//
//			final File srcFile = responseInfo.result;
//			new Thread() {
//				public void run() {
//					doSuccessed(srcFile, spendTime + "", dinfo.avgSpeed + "");
//				}
//
//				;
//			}.start();
//		}
//
//		//oddshou############ 添加 synchronized 以防ANR
//		private synchronized void doSuccessed(File srcFile, String spendTime, String speed) {
//			final File dstFile = MoveFileToDownloadPath(srcFile, dinfo);
//			dinfo.setFileSavePath(dstFile.getPath());
//			Log.d(TAG, "dinfo path = " + dinfo.fileSavePath);
//			// LogUtils.e( "packMD5 =" + dinfo.packMD5 );
//			// 判断apk文件是否损坏
//			if (dinfo.packMD5 != null
//					&& FileUtils.isApkFileBroken(dstFile.getPath(), dinfo.packMD5)) {
//				String errMsg = "apk is broken! file=" + dstFile.getPath() + "| length="
//						+ dstFile.length();
//				LogUtils.e(errMsg);
//				// TODO debug时先不删除
//				// deleteFile( dinfo );
//				updateTaskState(dinfo, TaskState.FAILED_BROKEN, errMsg);
//				// 上报下载失败
//				ReportConstants.reportDownloadFail(dinfo.appId, dinfo.packId, dinfo.nFromPos,
//						StatisticManager.STAC_DOWNLOAD_APK_ERROR_FILE_BROKEN, "downloadinfo="
//								+ dinfo.toString());
//				MtaUtils.trackDownloadFailed(StatisticManager.ERROR_DOWNLOAD_FILE_BROKEN);
//				return;
//			}
//
//			Logger.i(TAG, "doSuccessed " + "succeeded", "oddshou");
//			updateTaskState(dinfo, TaskState.SUCCEEDED, dinfo.stateMsg);
//
//			// 播放下载完成提示语音,gamecenter下载完成后去除
//			//	    App.playPrompt( );
//
//			//通知MediaProvider数据库改变
//			//因为建立连接是一个异步过程，所以，在建立连接时需要加入一个监听器。这样，连接建立后就可以得到通知，并进行下一步
//
//			Log.d(TAG, "dstFile AbsolutePath = " + dstFile.getAbsolutePath());
//
//			mConnection = new MediaScannerConnection(App.getAppContext(),
//					new MediaScannerConnection.MediaScannerConnectionClient() {
//						@Override
//						public void onMediaScannerConnected() {
//							if (mConnection != null) {
//								//更新媒体数据库  这也是一个异步过程，更新完毕后，监听器中得到通知，并断开连接
//								mConnection.scanFile(dstFile.getAbsolutePath(), null /* mimeType */);
//							}
//						}
//
//						@Override
//						public void onScanCompleted(String path, Uri uri) {
//							if (path.equals(dstFile.getAbsolutePath().toString())) {
//								if (mConnection != null) {
//									//断开连接
//									mConnection.disconnect();
//								}
//							}
//						}
//					});
//			if (mConnection != null) {
//				mConnection.connect();
//			}
//
//			// 上报下载成功 ,带上总下载时长 和 速度
//			LogUtils.e("上报下载数据,spendTime=" + spendTime + "|speed=" + speed);
//			if (speed.isEmpty() || spendTime.isEmpty()) {
//			} else {
//				speed = (Long.parseLong(speed) / 1024) + "";
//			}
//			MtaUtils.trackDownloadSuccess(dinfo.appName);
//			Logger.e("DownloadStateViewCustomization", "dinfo.nFromPos====" + dinfo.nFromPos);
//			ReportConstants.reportDownloadSuccessed(dinfo.appId, dinfo.packId, dinfo.nFromPos,
//					spendTime, speed, dinfo.groupId);
//			new ReportSpeedThread(dinfo.appName, speed).start();
//			// 检查设置，看是否自动安装
//			if (SettingContent.getInstance().getSettingData().bDownloadedToSetup) {
//				LogUtils.w("自动安装开始！");
//				installDownload(dinfo); //################ oddshou 如果找不到安装包会弹出 toast，但当前不在 ui线程中,会 crash
//			}
//			if (bDebug) {
//				FileUtils.writeDebugLog("上报下载数据,spendTime=" + spendTime + "|speed=" + speed);
//			}
//		}
//
//		private final static int HTTPEXCEPTION_CODE_CLIENT_PROTOCOL = 0; //add by firewang
//		private final static int HTTPEXCEPTION_CODE_FILE_NOEXIST = 404;
//
//		private final static int HTTPEXCEPTION_CODE_5XX_START = 500;
//		private final static int HTTPEXCEPTION_CODE_5XX_END = 599;
//
//		private final static int HTTPEXCEPTION_CODE_4XX_START = 400;
//		private final static int HTTPEXCEPTION_CODE_4XX_END = 499;
//
//		@Override
//		public void onFailure(HttpException error, String msg) {
//			Logger.i(TAG, "onFailure()");
//			dinfo.setHttpHandler(null);
//
//			String errorMsg = "errorCode=" + error.getExceptionCode() + "|msg=" + msg;
//			LogUtils.e("HttpException error|" + errorMsg);
//			Log.d(TAG, "HttpException error|" + errorMsg);
//			if (bDebug) {
//				FileUtils.writeDebugLog(errorMsg);
//			}
//
//			int errorCode = error.getExceptionCode();
//			if (errorCode == HTTPEXCEPTION_CODE_FILE_NOEXIST) {
//				updateTaskState(dinfo, TaskState.FAILED_NOEXIST, errorMsg);
//			} else if (errorCode >= HTTPEXCEPTION_CODE_4XX_START
//					&& errorCode <= HTTPEXCEPTION_CODE_4XX_END
//            /* || errorCode == HTTPEXCEPTION_CODE_CLIENT_PROTOCOL*/) { //firewang add errorCode == HTTPEXCEPTION_CODE_CLIENT_PROTOCOL
//				// 当前ngix服务器，对于416的错误是返回400的，文件有可能刚好下载完成后出错，导致当成文件损坏来处理
//				File desFile = new File(dinfo.getFileSavePath());
//				if (desFile.length() == dinfo.getFileLength() && dinfo.getFileLength() > 0) {
//					dinfo.progress = dinfo.getFileLength();
//					doSuccessed(desFile, "", "");
//					return;
//				}
//
//				updateTaskState(dinfo, TaskState.FAILED_BROKEN, errorMsg);
//			} else if (errorCode == HTTPEXCEPTION_CODE_CLIENT_PROTOCOL) {//tomqian 支持服务器返回错误码=0时，改变task的状态
//				//为stop状态，为支持恢复网络时断点续传，此处为隐藏风险点
//				updateTaskState(dinfo, TaskState.STOPPED, errorMsg);
//
//			} else if (errorCode >= HTTPEXCEPTION_CODE_5XX_START
//					&& errorCode <= HTTPEXCEPTION_CODE_5XX_END) {
//				updateTaskState(dinfo, TaskState.FAILED_SERVER, errorMsg);
//				// 服务器繁忙等原因暂停
//				ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId, dinfo.nFromPos,
//						ReportConstants.STAC_DOWNLOAD_APK_SERVER_BUSY_STOP, errorCode + ":"
//								+ errorMsg);
//				MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_SERVER_BUSY);
//			} else {
//				updateTaskState(dinfo, TaskState.FAILED_NETWORK, errorMsg);
//
//				LogUtils.e("TaskState.FAILED_NETWORK =" + dinfo.toString() + "|errorCode = "
//						+ errorCode);
//				Log.d(TAG, "TaskState.FAILED_NETWORK =" + dinfo.toString() + "|errorCode = "
//						+ errorCode);
//				// 网络中断，任务暂停
//				ReportConstants.reportDownloadStop(dinfo.appId, dinfo.packId, dinfo.nFromPos,
//						ReportConstants.STAC_DOWNLOAD_APK_NETWORK_INTERRUPT_STOP, errorCode + ":"
//								+ errorMsg);
//				MtaUtils.trackDownloadStop(StatisticManager.STOP_REASON_NETWORK_INTERRUPT_STOP);
//			}
//
//			// 上报下载失败
//			ReportConstants.reportDownloadFail(dinfo.appId, dinfo.packId, dinfo.nFromPos,
//					StatisticManager.STAC_DOWNLOAD_APK_ERROR_NETWORK, errorCode + ":" + errorMsg);
//			MtaUtils.trackDownloadFailed(StatisticManager.ERROR_DOWNLOAD_NETWORK);
//		}
//	}

	private void updateTaskState(DownloadTask info, TaskState state) {
		updateTaskState(info, state, info.stateMsg);
	}

	private void updateTaskState(DownloadTask info, TaskState state, String stateMsg) {
		Logger.i(TAG, "updateTaskState " + info.appName + " state " + state, "oddshou");

		info.setState(state);
		if (bDebug) {
			info.stateMsg = stateMsg;
		}

		// 避免下载过程中直接关机，下载状态未保存的问题
		if (info.getState() == TaskState.WAITING || info.getState() == TaskState.STOPPED
				|| info.getState() == TaskState.SUCCEEDED) {
			mDownloadInfoManager.saveTaskToDB(info);
		}

		Message msg = Message.obtain();
		msg.what = MSG_CONSTANTS.MSG_DOWNLOAD_STATE_CHANGE;
		msg.obj = info;
		sendMsgToUi(msg);
	}

	// TODO 循环遍历MSG的地方
	private void sendMsgToUi(Message msg) {
		for (int i = 0; i < mRegisterListenerList.size(); i++) {
			Handler handler = mRegisterListenerList.get(i);

			// Logger.i( "ccccc" , "handler= " + handler );

			if (handler instanceof DownloadTaskStateListener) {
				DownloadTaskStateListener downloadTaskStateListener = (DownloadTaskStateListener) handler;
				switch (msg.what) {
					case MSG_CONSTANTS.MSG_DOWNLOAD_STATE_CHANGE:
						downloadTaskStateListener.sendStateChangeMsg((DownloadTask) msg.obj);
//						Logger.i(TAG, "sendMsgToUi " + ((DownloadTask) msg.obj).getState(), "oddshou");
						break;
					case MSG_CONSTANTS.MSG_UPDATE_PROGRESS:
						downloadTaskStateListener.sendProgressChangeMsg((DownloadTask) msg.obj);
						break;
					case MSG_CONSTANTS.MSG_REFRESH_DOWNLOAD:
						downloadTaskStateListener.sendRefreshMsg();
						break;
					//
					case MSG_CONSTANTS.MSG_BATCH_REFRESH_DOWNLOAD:
						downloadTaskStateListener.sendBatchRefreshMsg(msg.obj);
						break;
					default:
						break;
				}
			} else {
				handler.sendMessage(msg);
			}
		}
	}

	// 用于请求安卓市场真实下载链接
	public class Req91DownloadPathController extends AsyncTask<Object, String, String> {

		private HttpURLConnection connection = null;

		@Override
		protected String doInBackground(Object... params) {

			DownloadTask task = (DownloadTask) params[1];



			try {
				// LogUtils.e( "params[0] = " + params[0] );
				URL uslUrl = new URL((String) params[0]);
				connection = (HttpURLConnection) uslUrl.openConnection();
				if (connection != null) {
					connection.setInstanceFollowRedirects(true);
				} else {
					return null;
				}
				connection.connect();
				String location = connection.getHeaderField("Location");
				// LogUtils.e( "location=" + location );

				if (connection != null)
					connection.disconnect();
				// LogUtils.e( "connection.getURL( )=" + connection.getURL(
				// ).toString( ) );
				// LogUtils.e( "connection.getOutputStream( )=" +
				// connection.getOutputStream( ).toString( ) );

				// 发消息开始下载
				if (location != null && !location.isEmpty()) {
					LogUtils.e("location=" + location);//location=http://apk.wdjcdn.com/7/4e/567562e9d162f10756128221b00124e7.apk
					Log.e(TAG, "appId =  " + task.appId + " location = " + location
							+ " bRealAppDownloadURL = true");
					task.appDownloadURL = location;
					task.bRealAppDownloadURL = true;
					Log.d(TAG, "" + task.appDownloadURL);
					Message msg = Message.obtain();
					msg.what = MSG_GET_91_REAL_PATH;
					msg.obj = task;
					mManagerHandler.sendMessage(msg);
				} else {
					LogUtils.e("location,未请求到");
					Log.e(TAG, "location,未请求到 ,说明是真实地址，直接请求");
					task.bRealAppDownloadURL = true;
					task.appDownloadURL = (String) params[0];
					Log.e(TAG, "appId =  " + task.appId
							+ " location,未请求到  bRealAppDownloadURL = true");
					Message msg = Message.obtain();
					msg.what = MSG_REQUEST_DOWNLOAD;
					msg.obj = task;
					mManagerHandler.sendMessage(msg);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (connection != null)
					connection.disconnect();
				task.bRealAppDownloadURL = true;
				Message msg = Message.obtain();
				msg.what = MSG_REQUEST_DOWNLOAD;
				msg.obj = task;
				mManagerHandler.sendMessage(msg);

				LogUtils.e("连接异常：" + e.getMessage());
				// e.printStackTrace( );
			}finally {
				Logger.e(TAG, "error");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}

	public int getmPagePostion() {
		return mPagePostion;
	}

	public void setmPagePostion(int mPagePostion) {
		this.mPagePostion = mPagePostion;
	}

}
