package com.hykj.gamecenter.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.hykj.gamecenter.App;
import com.hykj.gamecenter.download.ApkDownloadManager;
import com.hykj.gamecenter.download.DownloadService;
import com.hykj.gamecenter.utils.Logger;

public class NetworkMonitorReceiver extends BroadcastReceiver
{

    private final ApkDownloadManager mApkDownloadManager;

    public NetworkMonitorReceiver()
    {
    	mApkDownloadManager = DownloadService.getDownloadManager( );//ApkDownloadManager.getInstance();
    }

    @Override
    public void onReceive( Context context , Intent intent )
    {
	Logger.d( "resumeDownload" , "onNetworkChanged onReceive" );
	mApkDownloadManager.onNetworkChanged( intent );
    }

    public void registerReceiver()
    {
	IntentFilter filter = new IntentFilter( );
	filter.addAction( ConnectivityManager.CONNECTIVITY_ACTION );
	App.getAppContext( ).registerReceiver( this , filter );

	Logger.d( "resumeDownload" , "NetworkMonitorReceiver registerReceiver" );
    }

    public void unregisterReceiver()
    {
	//fixbug
	//		froyo  21:25:50
	//		嗯 这两个receiver是一起注册 取消的
	//		AlwaysOnline  21:26:19
	//		去设置里切换字体时，回来就容易出现
	//		froyo  21:29:08
	//		哦 知道了 我是在splashactivity里注册的 在homepage里取消的 切换字体后homepage重新启动会走到destroy取消一次 稍后如果正常结束 又会取消一次
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): Caused by: java.lang.IllegalArgumentException: Receiver not registered: com.niuwan.gamecenter.logic.NetworkMonitorReceiver@42077268
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): 	at android.app.LoadedApk.forgetReceiverDispatcher(LoadedApk.java:657)
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): 	at android.app.ContextImpl.unregisterReceiver(ContextImpl.java:1339)
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): 	at android.content.ContextWrapper.unregisterReceiver(ContextWrapper.java:445)
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): 	at com.niuwan.gamecenter.logic.NetworkMonitorReceiver.unregisterReceiver(NetworkMonitorReceiver.java:32)
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): 	at com.niuwan.gamecenter.logic.BroadcastManager.unregisterReceiveres(BroadcastManager.java:31)
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): 	at com.niuwan.gamecenter.activity.HomePageActivity.onDestroy(HomePageActivity.java:191)
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): 	at android.app.Activity.performDestroy(Activity.java:5273)
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): 	at android.app.Instrumentation.callActivityOnDestroy(Instrumentation.java:1110)
	//		09-26 21:23:55.588: E/AndroidRuntime(29546): 	at android.app.ActivityThread.performDestroyActivity(ActivityThread.java:3438)
	try
	{
	    App.getAppContext( ).unregisterReceiver( this );
	}
	catch ( java.lang.IllegalArgumentException e )
	{
	    Logger.e( "NetworkMonitorReceiver" , "unregisterReceiver java.lang.IllegalArgumentException" );
	}
    }
}
