package com.x.business.zerodata.connection.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.manager.WifiApManager;

/**
 * 
 
 *
 */
public class ScanHotspotListener implements Runnable {

	public Context context;
	private Handler handler ;
	public WifiApManager m_wiFiAdmin; //Wifi管理类
	public static final int scanHotspotIntervalTime = 3*1000 ; 
	
	public ScanHotspotListener(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		m_wiFiAdmin = WifiApManager.getInstance(context) ;
	}

	public boolean running = false;
	private long startTime = 0L;
	private Thread thread = null;

	@Override
	public void run() {
		while (true) {
			if (!running)
				return;
			try {
				Message msg = handler.obtainMessage(ZeroDataConnectHelper.CLIENT_SCAN_WIFI_RESULT);
				handler.sendMessage(msg);
				Thread.sleep(scanHotspotIntervalTime);
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
			e.printStackTrace() ;
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

	public static int getScanhotspotintervaltime() {
		return scanHotspotIntervalTime;
	}
	
}
