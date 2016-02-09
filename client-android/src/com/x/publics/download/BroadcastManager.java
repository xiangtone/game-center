/**   
* @Title: BroadCastManager.java
* @Package com.mas.amineappstore.download
* @Description: TODO 

* @date 2014-1-9 下午03:27:07
* @version V1.0   
*/

package com.x.publics.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
* @ClassName: BroadCastManager
* @Description: 应用内广播管理类 

* @date 2014-1-9 下午03:27:07
* 
*/

public class BroadcastManager {

	private static LocalBroadcastManager broadcastManager;

	public static void init(Context context) {
		broadcastManager = LocalBroadcastManager.getInstance(context);
	}

	/** 
	* @Title: registerReceiver 
	* @Description: 注册广播 
	* @param @param receiver
	* @param @param filter     
	* @return void    
	* @throws 
	*/

	public static void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		broadcastManager.registerReceiver(receiver, filter);
	}

	/** 
	* @Title: unregisterReceiver 
	* @Description: 注销广播 
	* @param @param receiver     
	* @return void    
	* @throws 
	*/

	public static void unregisterReceiver(BroadcastReceiver receiver) {
		broadcastManager.unregisterReceiver(receiver);
	}

	/** 
	* @Title: sendBroadcast 
	* @Description: 发送广播 
	* @param @param intent     
	* @return void    
	* @throws 
	*/

	public static void sendBroadcast(Intent intent) {
		broadcastManager.sendBroadcast(intent);
	}

}
