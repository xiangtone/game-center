package com.x.business.zerodata.connection.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.connection.manager.WifiApManager;

/**
 * 
 
 *
 */
public class ConnectHotspotResultListener implements Runnable {

	public Context context;
	private Handler handler ;
	
	public ConnectHotspotResultListener(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	public boolean running = false;
	private long startTime = 0L;
	private Thread thread = null;

	@Override
	public void run() {
		while (true) {
			if (!running)
				return;
			
			if (System.currentTimeMillis() - startTime >= 300000000L) {
				Message msg = handler.obtainMessage(ZeroDataConnectHelper.CLIENT_CONNECT_HOTSPOT_RESULT_TIMEOUT);
				handler.sendMessage(msg);
			} else  {
				Message msg = handler.obtainMessage(ZeroDataConnectHelper.CLIENT_CONNECT_HOTSPOT_RESULT);
				long runTimes = System.currentTimeMillis() - startTime ;
				msg.arg1 = (int) runTimes ;
				handler.sendMessage(msg);
			}
			try {
				Thread.sleep(2000L);
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
			// TODO: handle exception
		}
	}

	public void stop() {
		try {
			running = false;
			thread = null;
			startTime = 0L;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
