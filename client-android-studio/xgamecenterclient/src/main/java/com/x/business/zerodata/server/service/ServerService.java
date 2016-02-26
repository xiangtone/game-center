package com.x.business.zerodata.server.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.x.business.zerodata.server.service.ServiceController;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.server.HttpRequestHandler;
import com.x.business.zerodata.server.service.ServerValues;
import com.x.business.zerodata.server.service.params.ServerParams;
import com.x.business.zerodata.shell.ShellCommands;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.LogUtil;

public class ServerService extends Service implements ServerValues {

	private static final String TAG = "ServerService";

	private static final int START_NOTIFICATION_ID = 1;

	private static final int VIBRATE_IDENTIFIER = 0x102;
	private static final int SERVER_STARTED_IDENTIFIER = 0x102 + 1;
	private static final int SERVER_STOPED_IDENTIFIER = 0x102 + 2;

	/**
	 * This is the default port opened when the user ask for opening a port
	 * under 1024. <br>
	 * The system will try to use iptables like this:<br>
	 * iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port
	 * DEFAULT_PORT_ON_ROOT
	 */
	public static final int DEFAULT_PORT_ON_ROOT = 65535 - 50;

	// This field is can only be setted if the server is started.
	private ServerParams mParams;
	private int mCurrentPort;
	private String mLogPort;
	private ServerSocket mServerSocket;
	private String mVersion;
	
	private static MainServerThread mServerThread;

	private volatile boolean mVibrate;


	private BroadcastReceiver wifiStateChangedReceiver;

	@Override
	public IBinder onBind(Intent intent) {

		if (getServerStatus() != STATUS_RUNNING) {
		}

		return new ServiceController.Stub() {
			@Override
			public boolean startService() throws RemoteException {
				
				return startServer();
			}

			@Override
			public boolean restartService() throws RemoteException {
				
				if (getStatus() == STATUS_RUNNING) {
					stopServer();
				}
				return startServer();
			}

			@Override
			public boolean stopService() throws RemoteException {
				return stopServer();
			}

			@Override
			public int getStatus() throws RemoteException {
				return getServerStatus();
			}

			@Override
			public void setVibrate(boolean vibrate) throws RemoteException {
				mVibrate = vibrate;
			}

			@Override
			public String getVersion() throws RemoteException {
				return mVersion;
			}


			@Override
			public int getDefaultPortOnRoot() throws RemoteException {
				return DEFAULT_PORT_ON_ROOT;
			}

		};
	}

	private int getServerStatus() {
		if (null == mServerThread) {
			return STATUS_STOPPED;
		} else if (mServerThread.isAlive()) {
			return STATUS_RUNNING;
		} else {
			return STATUS_STOPPED;
		}
	}

	


	@Override
	public void onCreate() {
		super.onCreate();

		mParams = new ServerParams("", "", 10, false, 8080, 5) ;
		wifiStateChangedReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_UNKNOWN);

				switch (extraWifiState) {
				case WifiManager.WIFI_STATE_DISABLED:
					// Logger.d(TAG, "WIFI STATE DISABLED");
					break;
				case WifiManager.WIFI_STATE_DISABLING:
//					if (mPreferenceHelper.isAutostopWifiEnabled()
//							&& 
							if (getServerStatus() == STATUS_RUNNING) {
//						addLog("", "", "", "Wifi connection down... Stopping server");
						stopServer();
					}
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					// Logger.d(TAG, "WIFI STATE ENABLED");
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					// Logger.d(TAG, "WIFI STATE ENABLING");
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					// Logger.d(TAG, "WIFI STATE UNKNOWN");
					break;
				}

			}
		};

		registerReceiver(wifiStateChangedReceiver, new IntentFilter(
				WifiManager.WIFI_STATE_CHANGED_ACTION));

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
//		Logger.d(TAG, "  Destroing ServDroid Service");
		stopServer();
		if (getServerStatus() != STATUS_RUNNING) {
//			clearRunningNotification();
		}
		super.onDestroy();
	}

	private boolean startServer() {
		if (null == mServerThread || !mServerThread.isAlive()) {
			mServerThread = new MainServerThread();
			mServerThread.start();
			return true;
		}
		return false;

	}

	private boolean stopServer() {

//		clearRunningNotification();
		if (mCurrentPort < 1024) {
//			ShellCommands.closeNatPorts();
		}
		if (null == mServerThread) {
//			addLog("", "", "", "ERROR stopping ServDroid.web server ");
			return false;
		}
		if (mServerThread.isAlive()) {
			mServerThread.stopThread();
			mServerThread = null;
//			addLog("", "", "", "ServDroid.web server stoped ");
			return true;
		}
//		addLog("", "", "", "ERROR stopping ServDroid.web server");
		mServerThread = null;
		return false;
	}


	/**
	 * Private class for the server thread
	 */
	private class MainServerThread extends Thread {

		private volatile boolean mRun;
		private WifiLock mWl;

		public MainServerThread() {
			mRun = true;
		}

		public synchronized void stopThread() {
			if (null != mWl && mWl.isHeld()) {
				mWl.release();
			}
			if (mRun == false) {
				return;
			}
			mRun = false;
			if (mServerSocket == null) {
				return;
			}
			try {
				mServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void run() {

			try {
				if (mWl == null || !mWl.isHeld()) {
					WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					mWl = manager.createWifiLock(WifiManager.WIFI_MODE_FULL, "servdroid_wifilock");
					mWl.setReferenceCounted(false);
					mWl.acquire();
				}
			} catch (Exception e) {
			}

			mCurrentPort = mParams.getPort();
			mLogPort = "" + mCurrentPort;

			try {
				if (mParams.getPort() < 1024) {
					if (!ShellCommands.isDeviceRooted()
							|| !ShellCommands.openNatPort(mParams.getPort(), DEFAULT_PORT_ON_ROOT)) {
						mLogPort = "" + DEFAULT_PORT_ON_ROOT;
						mCurrentPort = 8080;
						mLogPort = "" + mCurrentPort;
					} else {
						mCurrentPort = DEFAULT_PORT_ON_ROOT;
						mLogPort = mLogPort + " / " + DEFAULT_PORT_ON_ROOT;
					}

				}
				mServerSocket = new ServerSocket(mCurrentPort, mParams.getMaxClients());


			} catch (IOException e) {
				if (mRun) {
				}
				return;
			}
			//服务启动成功
			sendServerStartBroadcast() ;
			while (mRun) {
				Socket socket;
				try {
					socket = mServerSocket.accept();
					LogUtil.getLogger().i("zerodata","ServerService[ is running ]") ;
				} catch (IOException e1) {
					if (mRun) {
					}
					return;
				}
				try {
					HttpRequestHandler request = new HttpRequestHandler(socket, 
							mParams, mVersion, ServerService.this);
					Thread thread = new Thread(request);

					thread.start();

				} catch (Exception e) {
					e.printStackTrace() ;
				}

			}
		}
	}
	
	private void sendServerStartBroadcast() {
		Intent intent = new Intent(ZeroDataConstant.ACTION_SERVICE_START);
		BroadcastManager.sendBroadcast(intent);
	}
}
