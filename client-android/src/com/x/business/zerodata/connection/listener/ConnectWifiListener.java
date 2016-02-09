package com.x.business.zerodata.connection.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.x.business.zerodata.connection.helper.WifiState;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.connection.manager.WifiApManager;

/**
 * 
 
 *
 */

public class ConnectWifiListener implements Runnable {

	public Context context;

	public boolean running = false;
	private long startTime = 0L;
	private Thread thread = null;
	private Handler handler;
	public WifiApManager wifiApManager; //Wifi管理类
	public Long detectWifiTimeOut = 5 * 1000L;

	public ConnectWifiListener(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		wifiApManager = WifiApManager.getInstance(context);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(this.detectWifiTimeOut);
				if (!running)
					break;
				Message msg = null;
				if (ConnectHotspotManage.getInstance(context).getWifiState() == WifiState.WIFI_STATE_DISABLED) {

					msg = this.handler.obtainMessage(ZeroDataConnectHelper.CLIENT_WIFI_STATE_DISABLED);
				} else if (ConnectHotspotManage.getInstance(context).getWifiState() == WifiState.WIFI_STATE_DISABLING) {

					msg = this.handler.obtainMessage(ZeroDataConnectHelper.CLIENT_WIFI_STATE_DISABLING);
				} else if (ConnectHotspotManage.getInstance(context).getWifiState() == WifiState.WIFI_STATE_ENABLING) {

					msg = this.handler.obtainMessage(ZeroDataConnectHelper.CLIENT_WIFI_STATE_ENABLING);
				} else if (ConnectHotspotManage.getInstance(context).getWifiState() == WifiState.WIFI_STATE_ENABLED) {
					msg = this.handler.obtainMessage(ZeroDataConnectHelper.CLIENT_WIFI_STATE_ENABLED);
				} else {
					msg = this.handler.obtainMessage(ZeroDataConnectHelper.CLIENT_WIFI_WIFI_STATE_UNKNOWN);
				}
				msg.obj = ConnectWifiListener.class.getName() ;
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
			e.printStackTrace();
		}
	}

	public Long getDetectWifiTimeOut() {
		return detectWifiTimeOut;
	}

	public void setDetectWifiTimeOut(Long detectWifiTimeOut) {
		this.detectWifiTimeOut = detectWifiTimeOut;
	}

}
