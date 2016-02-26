package com.x.business.zerodata.connection.listener;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import com.x.business.zerodata.connection.helper.WifiState;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.manager.WifiApManager;

/**
 * 
 
 *
 */

public class CreatHotspotListener implements Runnable {

	public Context context;

	public boolean running = false;
	private long startTime = 0L;
	private Thread thread = null;
	private Handler handler ;
	public WifiApManager wifiApManager; //Wifi管理类
	public static final int creatHotspotTimeOut = 5*1000 ;
	
	public CreatHotspotListener(Context context, Handler handler) {
		this.context = context;
		this.handler = handler ;
		wifiApManager = WifiApManager.getInstance(context) ;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(creatHotspotTimeOut);
				if (!running)
					break;
				Message msg = null ;
				
				if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
				{

					if (wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_DISABLING){
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_DISABLING);
						
					}else if (wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_DISABLED){
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_DISABLED);

					}else if (wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_ENABLING){
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_ENABLING);
						
					}else if (wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_ENABLED){
							msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_ENABLED);
							
					}else if (wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_FAILED){
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_FAILED);
						
					}else{
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_UNKNOWN);
					}
				}else {

					if (wifiApManager.getWifiApState() == WifiState.WIFI_STATE_DISABLING){
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_DISABLING);
						
					}else if (wifiApManager.getWifiApState() == WifiState.WIFI_STATE_DISABLED){
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_DISABLED);

					}else if (wifiApManager.getWifiApState() == WifiState.WIFI_STATE_ENABLING){
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_ENABLING);
						
					}else if (wifiApManager.getWifiApState() == WifiState.WIFI_STATE_ENABLED){
							msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_ENABLED);
							
					}else if (wifiApManager.getWifiApState() == WifiState.WIFI_STATE_UNKNOWN){
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_FAILED);
						
					}else{
						msg = this.handler.obtainMessage(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_UNKNOWN);
					}
				}
				
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		try {
			thread = new Thread(this);
			running = true;
			startTime = System.currentTimeMillis();
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			running = false;
			thread = null;
			startTime = 0L;
		} catch (Exception e) {
			e.printStackTrace() ;
		}
	}

}
