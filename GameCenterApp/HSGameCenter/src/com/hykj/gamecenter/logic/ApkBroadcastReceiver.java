package com.hykj.gamecenter.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.utils.Logger;

/**
 * APK包的卸载安装的处理
 * 
 * @author froyohuang
 * @date 2013-8-5
 * 
 */
public class ApkBroadcastReceiver extends BroadcastReceiver {

	private final ApkInstalledManager mApkInstalledManager;

	public ApkBroadcastReceiver() {
		mApkInstalledManager = ApkInstalledManager.getInstance();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Logger.i("AppManageActivity","install action= " + action);
		if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
			mApkInstalledManager.onApkInstalled(context, intent);
			return;
		} else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
			mApkInstalledManager.onApkUnInstalled(context, intent);
		}
	}

	public void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);// 一个新应用包已经安装在设备上，数据包括包名（最新安装的包程序不能接收到这个广播）
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.setPriority(Integer.MAX_VALUE);
		filter.addDataScheme("package");
		App.getAppContext().registerReceiver(this, filter);
	}

	public void unregisterReceiver() {
		// fixbug
		// froyo 21:25:50
		// 嗯 这两个receiver是一起注册 取消的
		// AlwaysOnline 21:26:19
		// 去设置里切换字体时，回来就容易出现
		// froyo 21:29:08
		// 哦 知道了 我是在splashactivity里注册的 在homepage里取消的
		// 切换字体后homepage重新启动会走到destroy取消一次 稍后如果正常结束 又会取消一次
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): Caused by:
		// java.lang.IllegalArgumentException: Receiver not registered:
		// com.niuwan.gamecenter.logic.ApkBroadcastReceiver@41d586a0
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): at
		// android.app.LoadedApk.forgetReceiverDispatcher(LoadedApk.java:657)
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): at
		// android.app.ContextImpl.unregisterReceiver(ContextImpl.java:1339)
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): at
		// android.content.ContextWrapper.unregisterReceiver(ContextWrapper.java:445)
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): at
		// com.niuwan.gamecenter.logic.ApkBroadcastReceiver.unregisterReceiver(ApkBroadcastReceiver.java:44)
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): at
		// com.niuwan.gamecenter.logic.BroadcastManager.unregisterReceiveres(BroadcastManager.java:30)
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): at
		// com.niuwan.gamecenter.activity.HomePageActivity.onDestroy(HomePageActivity.java:191)
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): at
		// android.app.Activity.performDestroy(Activity.java:5273)
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): at
		// android.app.Instrumentation.callActivityOnDestroy(Instrumentation.java:1110)
		// 09-26 21:16:24.188: E/AndroidRuntime(6997): at
		// android.app.ActivityThread.performDestroyActivity(ActivityThread.java:3438)
		try {
			App.getAppContext().unregisterReceiver(this);
		} catch (java.lang.IllegalArgumentException e) {
			Logger.e("ApkBroadcastReceiver",
					"unregisterReceiver java.lang.IllegalArgumentException");
		}
	}
}
