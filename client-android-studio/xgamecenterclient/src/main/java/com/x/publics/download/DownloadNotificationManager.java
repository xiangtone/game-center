/**   
* @Title: DownloadNotificationManager.java
* @Package com.x.download
* @Description: TODO 

* @date 2013-12-19 下午05:12:43
* @version V1.0   
*/

package com.x.publics.download;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.SparseArray;

import com.x.R;
import com.x.db.DownloadEntityManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.ResourceUtil;
import com.x.ui.activity.myApps.MyAppsActivity;

/**
* @ClassName: DownloadNotificationManager
* @Description: 下载Notification管理类 

* @date 2013-12-19 下午05:12:43
* 
*/

public class DownloadNotificationManager {

	public static DownloadNotificationManager instance;
	private Context context;
	private NotificationManager notificationManager;

	private SparseArray<NotificationCompat.Builder> builderMap;

	public DownloadNotificationManager(Context context) {
		this.context = context;
		notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
		builderMap = new SparseArray<NotificationCompat.Builder>();
	}

	public static DownloadNotificationManager getInstance(Context context) {
		if (instance == null)
			instance = new DownloadNotificationManager(context);
		return instance;
	}

	/** 
	* @Title: showDownloadingNotification 
	* @Description: 显示下载中通知
	* @param      
	* @return void    
	* @throws 
	*/

	public void showDownloadingNotification() {
		int downloadingNum = DownloadEntityManager.getInstance().getAllUnFinishedAppsDownloadCount();
		if (downloadingNum > 0) {
			NotificationCompat.Builder builder = new Builder(context);
			builder.setContentTitle(ResourceUtil.getString(context, R.string.download_notification_title, ""
					+ downloadingNum));
			builder.setSmallIcon(R.drawable.tiker_bar_icon);
			builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.mas_ic_launcher));
			builder.setContentText(ResourceUtil.getString(context, R.string.download_notification_sub_content));
			builder.setAutoCancel(true);

			Intent intent = new Intent(context, MyAppsActivity.class);
			PendingIntent resultPendingIntent = PendingIntent.getActivity(context, Constan.Notification.DOWNLOAD_ID,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(resultPendingIntent);
			notificationManager.notify(Constan.Notification.DOWNLOAD_ID, builder.build());
			builderMap.put(Constan.Notification.DOWNLOAD_ID, builder);
		} else {
			hide(Constan.Notification.DOWNLOAD_ID);
		}
	}

	/** 
	* @Title: showCompleteNotification 
	* @Description: 显示下载完成通知
	* @param      
	* @return void    
	* @throws 
	*/

	public void showCompleteNotification() {
		int unInstallNum = DownloadEntityManager.getInstance().getAllUnInstallAppsDownloadCount();
		if (unInstallNum > 0) {
			NotificationCompat.Builder builder = new Builder(context);
			builder.setContentTitle(ResourceUtil.getString(context, R.string.install_notification_title, ""
					+ unInstallNum));
			builder.setSmallIcon(R.drawable.tiker_bar_icon);
			builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.mas_ic_launcher));
			builder.setContentText(ResourceUtil.getString(context, R.string.install_notification_sub_content));
			builder.setAutoCancel(true);

			Intent intent = new Intent(context, MyAppsActivity.class);
			intent.putExtra("tabNum", 1);
			PendingIntent resultPendingIntent = PendingIntent.getActivity(context, Constan.Notification.COMPLETE_ID,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(resultPendingIntent);
			notificationManager.notify(Constan.Notification.COMPLETE_ID, builder.build());
			builderMap.put(Constan.Notification.COMPLETE_ID, builder);
		} else {
			hide(Constan.Notification.COMPLETE_ID);
		}
	}

	/** 
	* @Title: hide 
	* @Description: 隐藏通知 
	* @param @param id     
	* @return void    
	* @throws 
	*/

	public void hide(int id) {
		notificationManager.cancel(id);
		builderMap.remove(id);
	}

	/** 
	* @Title: cancleAll 
	* @Description: 取消所有通知 
	* @param      
	* @return void    
	* @throws 
	*/

	public void cancleAll() {
		if (builderMap.size() != 0) {
			int size = builderMap.size();
			for (int i = 0; i < size; i++) {
				notificationManager.cancel(builderMap.keyAt(i));
			}
		}
	}
}
