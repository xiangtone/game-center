package com.hykj.gamecenter.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	private static final String TAG = "Utils";

	// 得到当前正在运行的activity的包名
	public static String getCurrentRuningActivityPackageName(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		String shortClassName = info.topActivity.getShortClassName(); // 类名
		String className = info.topActivity.getClassName(); // 完整类名
		String packageName = info.topActivity.getPackageName(); // 包名

		return packageName;
	}

	// 判断某个Activity是否正在运行 (Android判断App是否在前台运行 )
	public static boolean IsActivityRunning(Context context, String sPackageName) {
		String packageName = getCurrentRuningActivityPackageName(context);
		return packageName != null && packageName.equals(sPackageName);
	}

	// 判断当前应用程序是否处于后台
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {

				Log.d(TAG,
						"context.getPackageName()= " + context.getPackageName());
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					Log.i("后台", appProcess.processName);
					return true;
				} else {
					Log.i("前台", appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	// 判断ActivityName是否在当前运行的栈中 (得到当前运行栈中Activity的name)
	public static boolean isActivityInRunningTasksStack(Context context,
			String ActivityName) {
		String topActivityName = null;
		ActivityManager activityManager = (ActivityManager) (context
				.getSystemService(android.content.Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = activityManager
				.getRunningTasks(Integer.MAX_VALUE);
		if (runningTaskInfos != null) {
			for (int i = 0; i < runningTaskInfos.size(); i++) {
				ComponentName f = runningTaskInfos.get(i).topActivity;
				String topActivityClassName = f.getClassName();
				String temp[] = topActivityClassName.split("\\.");
				topActivityName = temp[temp.length - 1];
				Log.e(TAG, "ActivityName=" + topActivityName);
				if (topActivityName.equals(ActivityName)) {
					return true;
				}
			}
		}
		return false;
	}

	// 判断当前的activity出栈后，ActivityName是否在是当期的栈顶
	public static boolean isActivityInRunningTasksTopStack(Context context,
			String ActivityName) {
		String topActivityName = null;
		ActivityManager activityManager = (ActivityManager) (context
				.getSystemService(android.content.Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = activityManager
				.getRunningTasks(Integer.MAX_VALUE);
		if (runningTaskInfos != null) {
			if (runningTaskInfos.size() < 2) {
				return false;
			}

			ComponentName f = runningTaskInfos.get(1).topActivity;
			String topActivityClassName = f.getClassName();
			String temp[] = topActivityClassName.split("\\.");
			topActivityName = temp[temp.length - 1];
			Log.e(TAG, "ActivityName=" + topActivityName);
			if (topActivityName.equals(ActivityName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 推荐角标下发位运算
	 * 推荐标签，编辑指定，1=官方 2=推荐 4=首发 8=免费 16=礼包 32=活动 64=内测 128=热门...位运算
	 */
	public static ArrayList<Integer> getRecommend(int flag){
		ArrayList<Integer> list = new ArrayList<Integer>();
		int array[]  = {1,2,4,8,16,32,64,128};
		int base = 0x1;
		for(int i =0; i < 8;i++){
			int flag1 = (flag >>> i) & base;
			int flag2 = flag1 << i;
			for(int j =0;j < array.length;j++){
				if(flag2 == array[j]){
					list.add(array[j]);
				}
			}
		}
		return list;
	}
}
