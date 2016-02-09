package com.x.ui.activity.zerodata;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.x.R;
import com.x.business.zerodata.client.ClientManager;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.listener.ConnectWifiListener;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;

/**
 * 连接hotspot
 
 *
 */
public class ZeroDataQrConnectingActivity extends BaseActivity {

	private AnimationDrawable anima;
	private TextView connectingTv;
	ConnectHotspotManage chsm;
	String SSID = null;
	ConnectWifiListener connectWifiListener;
	ClientReceiver clientReceiver;
	boolean isConnectServer = false;
	private boolean isConnectHotSpot = false;
	private Timer timer;
	private int count = 0;
	private boolean isInit = false;
	public Long initConnectApTime = 0L;
	public Dialog dialog;
	public static boolean isDownloadStop;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_share_title);
		setContentView(R.layout.activity_zero_data_qr_connecting);
		context = this;
		registerReceiver();
		initUi();
		SSID = this.getIntent().getStringExtra("SSID");
		chsm = new ConnectHotspotManage(this);
		chsm.initScanHotSpotListener(mHandler);
		initConnectWifiListener();
		ConnectHotspotManage.getInstance(this).saveNetworkState(this);
		isDownloadStop = false;
	}

	private void initUi() {
		connectingTv = (TextView) findViewById(R.id.zeroDataQrConnectingTV);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (isInit) {
					count++;
					mHandler.sendEmptyMessage(10);
				} else {
					isInit = true;
				}
			}
		}, 0, 500);
	}

	@Override
	public void onStop() {
		releaseWakeLock();
		connectWifiListener.stop();
		if (!isDownloadStop) {
			ConnectHotspotManage.getInstance(this).resumeClientNetwork();
		}

		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
		acquireWakeLock();
		initConnectApTime = System.currentTimeMillis();
		connectServerAp(SSID);
		connectWifiListener.start();
	}

	WakeLock wakeLock;

	private void acquireWakeLock() {
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
			wakeLock.acquire();
		}
	}

	private void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}

	/**
	 * 初始化监听
	 */
	private void initConnectWifiListener() {
		connectWifiListener = new ConnectWifiListener(this, mHandler);
		connectWifiListener.setDetectWifiTimeOut(3000L);

	}

	/**
	 * 连接服务端的热点
	 * @param SSID
	 */
	private void connectServerAp(String SSID) {
		//SSID is null
		if (TextUtils.isEmpty(SSID)) {
			return;
		}
		connectAp();//连接热点

	}

	/**
	 * Handler
	 */
	public Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			//处理连接热点超时
			switch (msg.what) {
			case ZeroDataConnectHelper.CLIENT_CONNECT_SERVER_TIMEOUT:
			case ZeroDataConnectHelper.CLIENT_CONNECT_SERVER_NO_CONNECTION:
				connectResultDialog(
						ZeroDataQrConnectingActivity.this.getString(R.string.dialog_zerodata_connect_error_tips), false);
				break;
			case ZeroDataConnectHelper.CLIENT_WIFI_STATE_DISABLED:
				chsm.openWifi();
				isConnectServer = false;
				isConnectHotSpot = false;
				break;
			case ZeroDataConnectHelper.CLIENT_WIFI_STATE_ENABLED:
				if (chsm.isDisConnectSSID(chsm.getSSID())) {
					connectAp();
				} else if (!chsm.getSSID().contains(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER)) {
					chsm.disconnectWifi();
					connectAp();
				} else if (chsm.isConnectWifi()) {
					if (!isConnectServer && chsm.getServerIp().startsWith("192.168")) {
						isConnectServer = true;
						requestServer();
					}
				}
				break;

			// 连接中..	
			case 10:
				switch (count % 3) {
				case 0:
					connectingTv.setText(ResourceUtil.getString(context, R.string.share_connecting1));
					break;
				case 1:
					connectingTv.setText(ResourceUtil.getString(context, R.string.share_connecting2));
					break;
				case 2:
					connectingTv.setText(ResourceUtil.getString(context, R.string.share_connecting3));
					break;
				}
				break;
			}
			//检测是否超时
			if (msg.obj != null && msg.obj instanceof String && ConnectWifiListener.class.getName().equals(msg.obj)) {
				connectApTimeOut();
			}
		};
	};

	/**
	 * 
	* @Title: connectApTimeOut 
	* @Description: TODO(处理连接超时) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void connectApTimeOut() {
		if (System.currentTimeMillis() - initConnectApTime > 15 * 1000 && (dialog == null || !dialog.isShowing())) {
			connectResultDialog(
					ZeroDataQrConnectingActivity.this.getString(R.string.dialog_zerodata_connect_error_tips), false);
		}
	}

	/**
	 * 
	* @Title: connectAp 
	* @Description: TODO(连接热点) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void connectAp() {
		if (!isConnectHotSpot) {
			chsm.doConnectOpenAp(SSID);
			isConnectHotSpot = true;
		}
	}

	/**
	 * 开始处理对服务器的请求
	 */
	public void requestServer() {
		if (chsm.getSSID().contains(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER)) {
			String serverIp = chsm.getServerIp();
			ClientManager.getInstance(ZeroDataQrConnectingActivity.this).startQrRequestServerThread(serverIp);
		}

	}

	/**
	 * 注册http回调
	 */
	private void registerReceiver() {

		clientReceiver = new ClientReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ZeroDataConstant.ACTION_HTTP_REQUEST_TIMEOUT);
		filter.addAction(ZeroDataConstant.ACTION_HTTP_REQUEST_NO_CONNECTION);
		filter.addAction(ZeroDataConstant.ACTION_HTTP_REQUEST_FINSISH);
		BroadcastManager.registerReceiver(clientReceiver, filter);
	}

	/**
	 * 监听http回调
	 
	 *
	 */
	public class ClientReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent != null && ZeroDataConstant.ACTION_HTTP_REQUEST_TIMEOUT.equals(intent.getAction())) {
				Message msg = mHandler.obtainMessage(ZeroDataConnectHelper.CLIENT_CONNECT_SERVER_TIMEOUT);
				mHandler.sendMessage(msg);

			} else if (intent != null && ZeroDataConstant.ACTION_HTTP_REQUEST_NO_CONNECTION.equals(intent.getAction())) {
				Message msg = mHandler.obtainMessage(ZeroDataConnectHelper.CLIENT_CONNECT_SERVER_TIMEOUT);
				mHandler.sendMessage(msg);
			} else if (ZeroDataConstant.ACTION_HTTP_REQUEST_FINSISH.equals(intent.getAction())) {
				finish();

			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BroadcastManager.unregisterReceiver(clientReceiver);
		connectWifiListener.stop();
	}

	/**
	 * 连接服务器结果对话框
	 * @param result
	 */
	public void connectResultDialog(String result, final boolean isConnectApTimeout) {
		isConnectServer = false;
		dialog = Utils.showDialog(this, ResourceUtil.getString(this, R.string.warm_tips), result,
				ResourceUtil.getString(this, R.string.retry), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						if (!isConnectApTimeout && chsm.isConnectWifi() && !TextUtils.isEmpty(chsm.getSSID())
								&& chsm.getSSID().contains(SSID) && chsm.getServerIp().startsWith("192.168")) {
							isConnectServer = true;
							requestServer();
						} else {
							initConnectApTime = System.currentTimeMillis();
							isConnectHotSpot = false;
							connectAp();

						}

						dialog.dismiss();
					};
				}, ResourceUtil.getString(this, R.string.exit), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						back();
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			onBackPressed();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		back();
	}

	/**
	* 
	* @Title: back 
	* @Description: TODO(返回) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void back() {
		startActivity(new Intent(ZeroDataQrConnectingActivity.this, ZeroDataShareActivity.class));
		ConnectHotspotManage.getInstance(this).resumeClientNetwork();
		ZeroDataQrConnectingActivity.this.finish();
		//		}
	}
}
