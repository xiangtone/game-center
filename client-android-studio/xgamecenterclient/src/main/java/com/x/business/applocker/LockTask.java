/**   
* @Title: LockTask.java
* @Package com.x.business.applocker
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-10-9 下午1:22:06
* @version V1.0   
*/

package com.x.business.applocker;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import android.app.ActivityManager;
import android.app.ActivityManager.AppTask;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.x.ui.activity.applocker.ApplockerPasswordActivity;

/**
* @ClassName: LockTask
* @Description: TODO(执行程序锁任务，插入密码输入activity)

* @date 2014-10-9 下午1:22:06
* 
*/

public class LockTask extends Thread {
	private Context context;
	private ActivityManager activityManager;
	public static boolean isUpdateList = true;
	public static boolean isIncomingCall = false;
	public static String TAG = "AppLocker";
	private List<String> lockedList = null;
	public static final String UNLOCK_ACTIVITY = "com.x.ui.activity.applocker.ApplockerPasswordActivity";
	public static String INCOMING_CALL;
	private static final List<String> PHONES = new ArrayList<String>();
	public LockTask(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PHONES.add("com.android.phone");
//		PHONES.add("com.android.dialer");
	}

	public static LockTask lockTask;

	public static LockTask getInstance(Context context) {
		if (lockTask == null)
			lockTask = new LockTask(context);
		return lockTask;
	}

	/**
	 * above android 5.0 api 21
	* @Title: getAppProcessInfo 
	* @Description: TODO 
	* @param @return    
	* @return RunningAppProcessInfo
	 */
	private RunningAppProcessInfo getAppProcessInfo() {
		final int PROCESS_STATE_TOP = 2;
		RunningAppProcessInfo currentInfo = null;
		Field field = null;

		try {
			field = RunningAppProcessInfo.class.getDeclaredField("processState");
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<RunningAppProcessInfo> appList = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo app : appList) {
			if (app.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && app.importanceReasonCode == 0) {
				Integer state = null;

				try {
					state = field.getInt(app);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (state != null && state == PROCESS_STATE_TOP) {
					currentInfo = app;
					break;
				}
			}
		}
		return currentInfo;
	}

	/**
	 * below android 5.0 <api21
	* @Title: getComponentName 
	* @Description: TODO 
	* @param @return    
	* @return ComponentName
	 */
	private ComponentName getComponentName() {
		// TODO Auto-generated method stub
		ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;//获取栈顶运行的程序
		return topActivity;
	}

	public void run() {
		// TODO Auto-generated method stub
		String packageName;
		String className;
		if (Build.VERSION.SDK_INT >= 21) {
			ComponentName topActivity = getComponentName();
			RunningAppProcessInfo appProcessInfo = getAppProcessInfo();
			packageName = appProcessInfo.pkgList[0];//获取栈顶运行程序的包名
			className = topActivity.getClassName();//获取栈顶运行程序当前运行的类名
			//incoming call listener
			INCOMING_CALL = appProcessInfo.processName;
		} else {
			ComponentName topActivity = getComponentName();
			packageName = topActivity.getPackageName();
			className = topActivity.getClassName();
		}

		startLocker(packageName, className);
	}

	/**
	 * get main lock switch's lock status
	* @Title: startLocker 
	* @Description: TODO 
	* @param @param packageName
	* @param @param className    
	* @return void
	 */
	private void startLocker(String packageName, String className) {
		if (LockManager.getInstance(context).getLockStatus()) {
			if (isIncomingCall == false && !PHONES.contains(packageName)) {
				lockPackage(packageName, className);
			} else if (isIncomingCall == true) {
				if (LockHelper.getInstance(context).isLockActivity(INCOMING_CALL))
					lockPackage(packageName, INCOMING_CALL);
			}
		}
	}

	/**
	 * execute lock the app which package name is given
	* @Title: lockPackage 
	* @Description: TODO 
	* @param @param packageName    
	* @return void
	 */
	private void lockPackage(String packageName, String className) {
		String currentLockPackageName = LockManager.getInstance(context).getCurrentLockPackageName();
		if (isBtwLockTime()) {
			if (isLockedApp(packageName) && !packageName.equals(currentLockPackageName)) {
				//				Log.d("applocker", "packageName=" + packageName);
				//				Log.d("applocker", "currentLockPackageName =" + currentLockPackageName);
				LockManager.getInstance(context).saveCurrentLockPackageName(packageName);
				ApplockerPasswordActivity.flag = false;
				context.startActivity(new Intent(context, ApplockerPasswordActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			}
			currentLockPackageName = LockManager.getInstance(context).getCurrentLockPackageName();
			if (!TextUtils.isEmpty(packageName) && !packageName.equals(currentLockPackageName)
					&& !UNLOCK_ACTIVITY.equals(className)) {
				LockManager.getInstance(context).saveCurrentLockPackageName("");
			}
		}
	}

	/**
	 * Determine whether is in locking time
	* @Title: isBtwLockTime 
	* @Description: TODO 
	* @param @return    
	* @return boolean
	 */
	private boolean isBtwLockTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
		Date currentDate = new Date(System.currentTimeMillis());
		String currentHour = dateFormat.format(currentDate);
		//		Log.d(TAG, "---current Hour---" + currentHour);
		String startLockTime = LockManager.getInstance(context).getStartLockTime();
		String stopLockTime = LockManager.getInstance(context).getStopLockTime();
		int cur = Integer.valueOf(currentHour);
		int start = Integer.valueOf(startLockTime);
		int stop = Integer.valueOf(stopLockTime) - 1;
		if (cur >= start && cur <= stop) {
			return true;
		}
		return false;
	}

	/**
	 * Determine whether is locked
	* @Title: isLockedApp 
	* @Description: TODO 
	* @param @param packageName
	* @param @return    
	* @return boolean
	 */
	private boolean isLockedApp(String packageName) {

		if (isUpdateList) {
			lockedList = LockManager.getInstance(context).findLockeredApp();
			isUpdateList = false;
		}

		if (lockedList != null && !lockedList.isEmpty()) {
			for (int i = 0; i < lockedList.size(); i++) {
				if (packageName.equals(lockedList.get(i)))
					return true;
			}
		}
		return false;
	}

}
