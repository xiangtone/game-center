/**   
* @Title: LockService.java
* @Package com.mas.amineappstore.business.applocker
* @Description: TODO(服务轮询栈顶的应用是否为被加锁应用)

* @date 2014-10-9 下午1:50:40
* @version V1.0   
*/

package com.x.business.applocker;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
* @ClassName: LockService
* @Description: TODO(轮询栈顶的应用是否为被加锁的应用)

* @date 2014-10-9 下午1:50:40
* 
*/

public class LockService extends Service {

//	private Timer timer;
	public static final int FOREGROUND_ID = 0;
//	private static final long REPEAT_TIME = 1000L;

	private LockHanlder lockHanlder = new LockHanlder(this) ;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		startForeground(FOREGROUND_ID, new Notification());//提升该服务优先级，保证后台运行。
	}

	/* (非 Javadoc) 
	* <p>Title: onBind</p> 
	* <p>Description: </p> 
	* @param arg0
	* @return 
	* @see android.app.Service#onBind(android.content.Intent) 
	*/

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	* @Title: startTimer 
	* @Description: TODO 轮询LockTask，查看当前运行于栈顶的程序是否为被加锁程序
	* @param     
	* @return void
	 */
	private void startTimer() {
//		if (timer == null || ApplockerAlarm.isCreatAlarm) {
//			timer = new Timer();
//			LockTask lockTask = new LockTask(this);
//			LockTask.isUpdateList = true;
//			timer.schedule(lockTask, 0L, REPEAT_TIME);
//			ApplockerAlarm.isCreatAlarm = false ;
//		}
		if(lockHanlder==null)
		{
			lockHanlder = new LockHanlder(this) ;
		}
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		startTimer();
		return START_STICKY;
		//		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * clean the timer
	 */
	public void onDestroy() {
		stopForeground(true);
//		timer.cancel();
//		timer.purge();
//		timer = null;
		super.onDestroy();
	}
}
