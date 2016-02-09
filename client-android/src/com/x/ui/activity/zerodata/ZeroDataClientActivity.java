/**   
 * @Title: ZeroDataClientActivity.java
 * @Package com.mas.amineappstore.activity
 * @Description: TODO 
 
 * @date 2014-1-14 下午02:56:34
 * @version V1.0   
 */

package com.x.ui.activity.zerodata;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.x.R;
import com.x.business.audio.AudioEffectManager;
import com.x.business.skin.SkinConfigManager;
import com.x.business.skin.SkinConstan;
import com.x.business.zerodata.client.ClientManager;
import com.x.business.zerodata.connection.helper.ServerDeviceModel;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.listener.ConnectWifiListener;
import com.x.business.zerodata.connection.manager.ConnectHotspotManage;
import com.x.business.zerodata.helper.ServiceHelper;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.publics.download.BroadcastManager;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.ResourceUtil;
import com.x.publics.utils.SharedPrefsUtil;
import com.x.publics.utils.ToastUtil;
import com.x.publics.utils.Utils;
import com.x.ui.activity.base.BaseActivity;
import com.zbar.lib.CaptureActivity;

/**
 * @Description:点击零流量分享首页的【Receive】按钮进入的搜索好友分享的页面。
 */

public class ZeroDataClientActivity extends BaseActivity implements OnClickListener {

	ServiceHelper serviceHelper;
	List<ScanResult> scanResultList;
	private ImageView[] imageViews = new ImageView[5];
	private TextView[] deviceTextView = new TextView[5];
	private TextView[] connectedState = new TextView[5];// 显示连接状态的提示
	private ImageView[] deviceIvConnecting = new ImageView[5];// 显示正在连接中的提示
	private ImageView[] deviceChioced = new ImageView[5];
	private LinearLayout[] layouts = new LinearLayout[5];
	private TextView nickNameBottomTv;// 显示在底部的设备名称
	private static final int serverDevices[] = { 0, 1, 2, 3, 4 };
	List<ServerDeviceModel> sdmList = new ArrayList<ServerDeviceModel>();
	Integer[] currentDevicesLayout;
	int connectDevicesSerialNum;
	ConnectHotspotManage chsm;
	public String currentServerIp = null;
	public boolean isConnecting = true;
	private ClientReceiver clientReceiver;
	public ConnectWifiListener connectWifiListener; // WiFi搜索进度条线程
	private Context context = this;
	//雷达View
	private ImageView scan_o, scan_t, scan_s;
	private Animation animation_o, animation_t, animation_s;

	ServerDeviceModel willConnectSdm = null;
	WakeLock wakeLock;
	public static boolean isDownloadStop = false;
	private boolean isOnclick = false;
	//新手引导【头像对应的图标】	
	private boolean boo = true;
	private Dialog mDialog = null;
	private ImageView guideIv, imageView;
	private boolean zeroClienIsFirst = true;

	private ImageView mGobackIv, searchIv, qrIv;
	private TextView mTitleTv;
	private View mNavigationView, mTitleView, mTitlePendant;
	private LinearLayout searchLl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTabTitle(R.string.page_share_title);
		setContentView(R.layout.activity_search_sharer);
		initUi();
		chsm = new ConnectHotspotManage(this);
		chsm.initScanHotSpotListener(mHandler);
		connectWifiListener = new ConnectWifiListener(this, mHandler);
		registerReceiver();
		initNavigation();
	}

	/**
	* @Title: initNavigation 
	* @Description: 初始化导航栏 
	* @param     
	* @return void
	 */
	private void initNavigation() {
		mTitleView = findViewById(R.id.rl_title_bar);
		mTitlePendant = findViewById(R.id.title_pendant);
		mNavigationView = findViewById(R.id.mh_navigate_ll);
		mGobackIv = (ImageView) findViewById(R.id.mh_slidingpane_iv);
		mTitleTv = (TextView) findViewById(R.id.mh_navigate_title_tv);
		searchLl = (LinearLayout) findViewById(R.id.mh_search_ll);
		searchLl.setVisibility(View.VISIBLE);
		searchIv = (ImageView) findViewById(R.id.mh_search_icon_iv);
		searchIv.setVisibility(View.GONE);
		qrIv = (ImageView) findViewById(R.id.mh_qr_code_icon_iv);
		qrIv.setVisibility(View.VISIBLE);

		mGobackIv.setBackgroundResource(R.drawable.ic_back);
		mTitleTv.setText(R.string.page_share_title);
		mNavigationView.setOnClickListener(this);
		searchLl.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		setSkinTheme();
		acquireWakeLock();
		isDownloadStop = false;
		ConnectHotspotManage.getInstance(context).disConnectionZappWifi();//断开zapp网络
		connectWifiListener.start();
		resetAllDeviceLayout();
		doScanHotSpot();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (sdmList.isEmpty()) // 延迟500ms后 可能已经出来发送者，判断发送者为空才播放
					AudioEffectManager.getInstance().playBeginScanAudioEffect(context);
			}
		}, 500);
	}

	private void initUi() {
		imageView = (ImageView) findViewById(R.id.bg_imageView);
		nickNameBottomTv = (TextView) findViewById(R.id.zdca_fmNickName_tv);
		nickNameBottomTv.setText(ZeroDataConnectHelper.getZeroShareNickName(this));

		scan_o = (ImageView) findViewById(R.id.zdca_scan_o);
		scan_t = (ImageView) findViewById(R.id.zdca_scan_t);
		scan_s = (ImageView) findViewById(R.id.zdca_scan_s);
		animation_o = AnimationUtils.loadAnimation(this, R.anim.share_scan_reveice);
		animation_t = AnimationUtils.loadAnimation(this, R.anim.share_scan_reveice);
		animation_s = AnimationUtils.loadAnimation(this, R.anim.share_scan_reveice);
		scan_o.setAnimation(animation_o);
		animation_o.setStartOffset(0);
		scan_t.setAnimation(animation_t);
		animation_t.setStartOffset(1000);
		scan_s.setAnimation(animation_s);
		animation_s.setStartOffset(2500);

		// 显示自己头像
		findViewById(R.id.img_my_head).setBackgroundResource(
				ZeroDataResourceHelper.getSelfZerodataHeadPortrait(context));

		imageViews[0] = (ImageView) findViewById(R.id.img_1);
		imageViews[1] = (ImageView) findViewById(R.id.img_2);
		imageViews[2] = (ImageView) findViewById(R.id.img_3);
		imageViews[3] = (ImageView) findViewById(R.id.img_4);
		imageViews[4] = (ImageView) findViewById(R.id.img_5);

		layouts[0] = (LinearLayout) findViewById(R.id.zdca_receive_device_layout1);
		layouts[1] = (LinearLayout) findViewById(R.id.zdca_receive_device_layout2);
		layouts[2] = (LinearLayout) findViewById(R.id.zdca_receive_device_layout3);
		layouts[3] = (LinearLayout) findViewById(R.id.zdca_receive_device_layout4);
		layouts[4] = (LinearLayout) findViewById(R.id.zdca_receive_device_layout5);

		deviceTextView[0] = (TextView) findViewById(R.id.zdca_receive_device_tv1);
		deviceTextView[1] = (TextView) findViewById(R.id.zdca_receive_device_tv2);
		deviceTextView[2] = (TextView) findViewById(R.id.zdca_receive_device_tv3);
		deviceTextView[3] = (TextView) findViewById(R.id.zdca_receive_device_tv4);
		deviceTextView[4] = (TextView) findViewById(R.id.zdca_receive_device_tv5);

		deviceIvConnecting[0] = (ImageView) findViewById(R.id.zdca_receive_device_connecting_stateIv1);
		deviceIvConnecting[1] = (ImageView) findViewById(R.id.zdca_receive_device_connecting_stateIv2);
		deviceIvConnecting[2] = (ImageView) findViewById(R.id.zdca_receive_device_connecting_stateIv3);
		deviceIvConnecting[3] = (ImageView) findViewById(R.id.zdca_receive_device_connecting_stateIv4);
		deviceIvConnecting[4] = (ImageView) findViewById(R.id.zdca_receive_device_connecting_stateIv5);

		connectedState[0] = (TextView) findViewById(R.id.connection_stateTv1);
		connectedState[1] = (TextView) findViewById(R.id.connection_stateTv2);
		connectedState[2] = (TextView) findViewById(R.id.connection_stateTv3);
		connectedState[3] = (TextView) findViewById(R.id.connection_stateTv4);
		connectedState[4] = (TextView) findViewById(R.id.connection_stateTv5);

		deviceChioced[0] = (ImageView) findViewById(R.id.zdca_receive_chioceDevice_iv1);
		deviceChioced[1] = (ImageView) findViewById(R.id.zdca_receive_chioceDevice_iv2);
		deviceChioced[2] = (ImageView) findViewById(R.id.zdca_receive_chioceDevice_iv3);
		deviceChioced[3] = (ImageView) findViewById(R.id.zdca_receive_chioceDevice_iv4);
		deviceChioced[4] = (ImageView) findViewById(R.id.zdca_receive_chioceDevice_iv5);

		for (int i = 0; i < layouts.length; i++) {
			layouts[i].setOnClickListener(this);
		}

		// 设置背景图片
		Utils.setBackgroundResource(imageView, R.drawable.zapp_radar_background, context);
	}

	/**
	 * 启动动画
	 * 
	 * @param layout
	 *            所需显示的那个动画的layout
	 * @param witchReceiver
	 *            显示的是哪个layout的动画
	 */
	private void startCartoon(final LinearLayout[] layouts, final int witchReceive, String deviceName,
			int headPortraitValue) {
		AudioEffectManager.getInstance().playScanResultAudioEffect(this);//播放音效
		LinearInterpolator lip = new LinearInterpolator();
		Animation receiverAnim = AnimationUtils.loadAnimation(ZeroDataClientActivity.this, R.anim.shar_of_receiver);
		receiverAnim.setInterpolator(lip);
		deviceTextView[witchReceive].setText(deviceName);
		layouts[witchReceive].setVisibility(View.VISIBLE);
		layouts[witchReceive].startAnimation(receiverAnim);
		imageViews[witchReceive].setBackgroundResource(ZeroDataResourceHelper
				.getZerodataHeadPortraitResource(headPortraitValue)); // 显示别人头像

		receiverAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
			}
		});
		//显示引导
		if (boo == false)
			return;
		showGuideDialog();
		boo = false;
	}

	/**
	 * 动画消失
	 * 
	 * @param layout
	 *            所需消失的那个动画的layout\
	 * @param witchReceiver
	 *            消失的是哪个layout的动画
	 */
	private void stopCartoon(final LinearLayout[] layouts, final int witchReceive) {
		LinearInterpolator lip = new LinearInterpolator();
		Animation receiverAnim = AnimationUtils.loadAnimation(ZeroDataClientActivity.this,
				R.anim.shar_of_receiver_disappear);
		receiverAnim.setInterpolator(lip);
		layouts[witchReceive].startAnimation(receiverAnim);
		receiverAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				layouts[witchReceive].setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mh_navigate_ll:
			onBackPressed();
			break;
		case R.id.mh_search_ll:
			Intent guideIntent = new Intent(context, CaptureActivity.class);
			startActivity(guideIntent);
			break;
		case R.id.zdca_receive_device_layout1:
			connectServerDevices(serverDevices[0]);
			break;
		case R.id.zdca_receive_device_layout2:
			connectServerDevices(serverDevices[1]);
			break;
		case R.id.zdca_receive_device_layout3:
			connectServerDevices(serverDevices[2]);
			break;
		case R.id.zdca_receive_device_layout4:
			connectServerDevices(serverDevices[3]);
			break;
		case R.id.zdca_receive_device_layout5:
			connectServerDevices(serverDevices[4]);
			break;
		default:
			break;
		}
	}

	/**
	 * 启动扫描
	 */
	public void doScanHotSpot() {
		startScanView();
		chsm.scanHotSpot();
		connectWifiListener.setDetectWifiTimeOut(3000L);
	}

	/**
	 * 连接到服务器设备
	 * 
	 * @param devicesSerialNum
	 */
	private void connectServerDevices(int devicesSerialNum) {
		// 重置连接显示
		resetConnectingLayout();
		connectDevicesSerialNum = devicesSerialNum;

		if (sdmList.size() > 0) {
			for (int i = 0; i < sdmList.size(); i++) {
				if (sdmList.get(i).getAdapterLayout() == devicesSerialNum) {
					willConnectSdm = sdmList.get(i);
					break;
				}
			}
		}

		//no Devices
		if (sdmList.size() == 0) {
			return;
			//no SSID
		} else if (willConnectSdm == null || TextUtils.isEmpty(willConnectSdm.getSSID())) {
			return;
		}

		lockConnectingDevices(devicesSerialNum);// 锁定connecting
		isConnecting = false;
		// 如果当前连接着
		if (chsm.getSSID() != null && chsm.getSSID().contains(willConnectSdm.getSSID()) && chsm.isConnectWifi()) {
			hanlderConnectedResultsDisplay();
		} else if (TextUtils.isEmpty(currentServerIp)) {
			chsm.doConnectOpenAp(willConnectSdm.getSSID());
		} else {
			disConnectHostspot(currentServerIp, false, true);
		}
	}

	/**
	 * 重置连接
	 */
	public void resetConnectingLayout() {
		if (sdmList.size() > 0) {
			for (int i = 0; i < sdmList.size(); i++) {
				unlockConnectingDevices(sdmList.get(i).getAdapterLayout());
			}
		}
	}

	/**
	 * 断开连接
	 * 
	 * @Title: disConnectHostspot
	 * @Description: TODO(断开连接)
	 * @param @param currentServerIp 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public boolean disConnectHostspot(String currentServerIp, boolean isReSumeNetwork, boolean isReconnectAp) {
		if (!TextUtils.isEmpty(currentServerIp)) {
			ClientManager.getInstance(ZeroDataClientActivity.this).disconnectHostSpot(currentServerIp, isReSumeNetwork,
					isReconnectAp);
			currentServerIp = null;
			return true;
		}
		return false;
	}

	/**
	 * 解锁设备连接中状态
	 * 
	 * @param connectDevicesSerialNum
	 */
	public void unlockConnectingDevices(int connectDevicesSerialNum) {
		deviceChioced[connectDevicesSerialNum].setVisibility(View.INVISIBLE);
		deviceIvConnecting[connectDevicesSerialNum].setVisibility(View.INVISIBLE);
		layouts[connectDevicesSerialNum].setClickable(true);
	}

	/**
	 * 锁定设备连接中状态
	 * 
	 * @param connectDevicesSerialNum
	 */
	public void lockConnectingDevices(int connectDevicesSerialNum) {
		deviceChioced[connectDevicesSerialNum].setVisibility(View.VISIBLE);
		deviceIvConnecting[connectDevicesSerialNum].setVisibility(View.VISIBLE);
		layouts[connectDevicesSerialNum].setClickable(false);
	}

	/**
	 * Handler
	 */
	public Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ZeroDataConnectHelper.CLIENT_SCAN_WIFI_RESULT: // 扫描到结果
				if (chsm.getScanResults() != null) {
					hanlderScanResultsDisplay(chsm.getScanResults());
					chsm.reStartScan();
				}
				break;
			case ZeroDataConnectHelper.CLIENT_CONNECT_SERVER_NO_CONNECTION:
				disConnectHostspot(currentServerIp, false, false);
				unlockConnectingDevices(connectDevicesSerialNum);
				break;
			case ZeroDataConnectHelper.CLIENT_CONNECT_SERVER_TIMEOUT:
				ToastUtil.show(ZeroDataClientActivity.this,
						ResourceUtil.getString(context, R.string.connection_server_time_out), Toast.LENGTH_SHORT);
				disConnectHostspot(currentServerIp, false, false);
				unlockConnectingDevices(connectDevicesSerialNum);
				break;
			case ZeroDataConnectHelper.CLIENT_WIFI_STATE_DISABLED:
				chsm.openWifi();
			case ZeroDataConnectHelper.CLIENT_WIFI_STATE_ENABLED:
				if (TextUtils.isEmpty(chsm.getSSID())) {

				} else if (!chsm.isDisConnectSSID(chsm.getSSID())
						&& !chsm.getSSID().contains(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER)) {
					chsm.disconnectWifi();

				} else if (!chsm.isDisConnectSSID(chsm.getSSID())
						&& chsm.getSSID().contains(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER)) {
					if (!isConnecting && chsm.getServerIp().startsWith("192.168")) {
						isConnecting = true;
						hanlderConnectedResultsDisplay();
					}

				}
				break;
			case ZeroDataConnectHelper.CLIENT_CONNECT_DISCONNECT_SUCCESSFUL:
				boolean isReconnectAp = (Boolean) msg.obj;
				if (isReconnectAp) {
					chsm.doConnectOpenAp(willConnectSdm.getSSID());
				}

				break;
			default:
				break;
			}

		};
	};

	/**
	 * 处理扫描出来的热点
	 * 
	 * @param scanResultSystemList
	 */
	public void hanlderScanResultsDisplay(List<ScanResult> scanResultSystemList) {
		if (scanResultSystemList != null && scanResultSystemList.size() > 0) {
			scanResultList = new ArrayList<ScanResult>();
			List<ServerDeviceModel> scanResultSdmList = new ArrayList<ServerDeviceModel>();
			for (int i = 0; i < scanResultSystemList.size(); i++) {
				ScanResult scanResult = scanResultSystemList.get(i);
				// 和指定连接热点比较，将其他的过滤掉！
				if (!scanResult.SSID.startsWith(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER)) {
					continue;
				}
				ServerDeviceModel sdm = new ServerDeviceModel();

				sdm.setBSSID(scanResult.BSSID);
				sdm.setSSID(scanResult.SSID);

				scanResultSdmList.add(sdm);

			}
			adapterScanResult(scanResultSdmList);
		}
	}

	/**
	 * 处理扫描结果
	 * 
	 * @param scanResultSdmList
	 */
	public void adapterScanResult(List<ServerDeviceModel> scanResultSdmList) {
		if (scanResultSdmList != null && scanResultSdmList.size() > 0) {
			// 移除
			for (int i = 0; i < sdmList.size(); i++) {
				boolean isRemove = true;
				for (int j = 0; j < scanResultSdmList.size(); j++) {
					if (scanResultSdmList.get(j).equals(sdmList.get(i))) {
						isRemove = false;
						scanResultSdmList.remove(j);
					}
				}
				if (isRemove) {
					stopCartoon(layouts, sdmList.get(i).getAdapterLayout());
					unlockConnectingDevices(sdmList.get(i).getAdapterLayout());//解锁设备连接中状态
					LogUtil.getLogger().d("zerodata", "remove layout = " + sdmList.get(i).getAdapterLayout());
					sdmList.remove(i);
				}

			}

			// 新增
			for (int i = 0; i < scanResultSdmList.size(); i++) {
				// 超过阀值
				if (sdmList.size() >= 5) {
					break;
				}

				boolean isExist = false;
				for (int j = 0; j < sdmList.size(); j++) {
					if (scanResultSdmList.get(i).equals(sdmList.get(j))) {
						isExist = true;
					}
				}
				// 如果已经存在则跳过
				if (isExist) {
					continue;
				}

				int adapterLayout = ZeroDataConnectHelper.getDeviceLayout(sdmList);
				LogUtil.getLogger().d("zerodata", "add layout = " + adapterLayout);
				ServerDeviceModel sdm = new ServerDeviceModel();
				sdm.setBSSID(scanResultSdmList.get(i).getBSSID());
				sdm.setSSID(scanResultSdmList.get(i).getSSID());
				sdm.setAdapterLayout(adapterLayout);
				sdmList.add(sdm);
				String SSID = ZeroDataConnectHelper.getDevicesNameFromSSID(scanResultSdmList.get(i).getSSID());

				startCartoon(layouts, adapterLayout, SSID,
						ZeroDataResourceHelper.getHeadPortraitValueFromSSID(scanResultSdmList.get(i).getSSID()));

			}

		} else {
			for (int i = 0; i < sdmList.size(); i++) {
				stopCartoon(layouts, sdmList.get(i).getAdapterLayout());
				unlockConnectingDevices(sdmList.get(i).getAdapterLayout());//解锁设备连接中状态
				sdmList.remove(i);
				// 所有发送者消失重新播放扫描音效
				AudioEffectManager.getInstance().playBeginScanAudioEffect(this);
			}

		}
	}

	/**
	 * 停止扫描附近热点
	 */
	public void stopScanView() {
		scan_o.clearAnimation();
		scan_o.setVisibility(View.INVISIBLE);
		scan_t.clearAnimation();
		scan_t.setVisibility(View.INVISIBLE);
		scan_s.clearAnimation();
		scan_s.setVisibility(View.INVISIBLE);
	}

	/**
	 * 开始扫描动画
	 */
	public void startScanView() {
		if (animation_o != null) {
			scan_o.setVisibility(View.VISIBLE);
			scan_o.startAnimation(animation_o);
		}
		if (animation_t != null) {
			scan_t.setVisibility(View.VISIBLE);
			scan_t.startAnimation(animation_t);
		}
		if (animation_s != null) {
			scan_s.setVisibility(View.VISIBLE);
			scan_s.startAnimation(animation_s);
		}
	}

	/**
	 * 开始处理对服务器的请求
	 */
	public void hanlderConnectedResultsDisplay() {
		if (chsm.getSSID().contains(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER)) {
			currentServerIp = chsm.getServerIp();
			ClientManager.getInstance(ZeroDataClientActivity.this).startRequestServerThread(currentServerIp);
		}

	}

	/**
	 * 取消设备的显示
	 * 
	 * @param SSID
	 */
	public void cancelDevicesDisplay(String SSID) {
		if (scanResultList != null && !TextUtils.isEmpty(SSID)) {
			for (int i = 0; i < scanResultList.size(); i++) {
				if (scanResultList.get(i).SSID.equals(SSID)) {
					continue;
				}
				layouts[serverDevices[i]].setVisibility(View.INVISIBLE);
			}
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		AudioEffectManager.getInstance().releaseMusicPlayer();
	}

	@Override
	public void onStop() {
		super.onStop();
		releaseWakeLock();
		resetAllDeviceLayout();
		if (!isDownloadStop) {
			boolean isDisConnect = disConnectHostspot(currentServerIp, true, false);
			if (!isDisConnect) {
				ConnectHotspotManage.getInstance(context).resumeClientNetwork();
			}
		}
		connectWifiListener.stop();

	}

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		chsm.uninit();
		connectWifiListener.stop();
		unregistReceiver();
		Utils.recycleBackgroundResource(imageView);
	}

	/**
	 * 
	* @Title: resetAllDeviceLayout 
	* @Description: TODO(重置显示状态) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void resetAllDeviceLayout() {
		for (int i = 0; i < layouts.length; i++) {
			layouts[i].setVisibility(View.INVISIBLE);
			deviceTextView[i].setText("");
			deviceIvConnecting[i].setVisibility(View.INVISIBLE);
		}

		//		deviceChioced[connectDevicesSerialNum].setVisibility(View.VISIBLE);
		//		deviceIvConnecting[connectDevicesSerialNum].setVisibility(View.VISIBLE);
		//		layouts[connectDevicesSerialNum].setClickable(false);
		sdmList.clear();
	}

	/**
	 * 注册监听
	 */
	private void registerReceiver() {

		clientReceiver = new ClientReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ZeroDataConstant.ACTION_HTTP_REQUEST_NO_CONNECTION);
		filter.addAction(ZeroDataConstant.ACTION_HTTP_REQUEST_TIMEOUT);
		filter.addAction(ZeroDataConstant.ACTION_HTTP_REQUEST_FINSISH);
		filter.addAction(ZeroDataConstant.ACTION_HTTP_REQUEST_DISCONNECT);
		BroadcastManager.registerReceiver(clientReceiver, filter);
	}

	private void unregistReceiver() {
		BroadcastManager.unregisterReceiver(clientReceiver);
	}

	/**
	 * 监听回调
	 * 
	 
	 * 
	 */
	public class ClientReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent != null && ZeroDataConstant.ACTION_HTTP_REQUEST_NO_CONNECTION.equals(intent.getAction())) {
				Message msg = mHandler.obtainMessage(ZeroDataConnectHelper.CLIENT_CONNECT_SERVER_NO_CONNECTION);
				mHandler.sendMessage(msg);

			} else if (intent != null && ZeroDataConstant.ACTION_HTTP_REQUEST_TIMEOUT.equals(intent.getAction())) {
				Message msg = mHandler.obtainMessage(ZeroDataConnectHelper.CLIENT_CONNECT_SERVER_TIMEOUT);
				mHandler.sendMessage(msg);

			} else if (ZeroDataConstant.ACTION_HTTP_REQUEST_FINSISH.equals(intent.getAction())) {
				finish();

			} else if (ZeroDataConstant.ACTION_HTTP_REQUEST_DISCONNECT.equals(intent.getAction())) {
				Message msg = mHandler.obtainMessage(ZeroDataConnectHelper.CLIENT_CONNECT_DISCONNECT_SUCCESSFUL);
				boolean isReconnectAp = intent.getBooleanExtra("extra", false);
				msg.obj = isReconnectAp;
				mHandler.sendMessage(msg);

			}
		}
	}

	@Override
	public void onBackPressed() {
		exitZeroDataShare();
	}

	public void exitZeroDataShare() {

		Utils.showDialog(this, ResourceUtil.getString(this, R.string.warm_tips),
				ResourceUtil.getString(this, R.string.dialog_exit_zerodata_share),
				ResourceUtil.getString(this, R.string.confirm), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 断开连接
						startActivity(new Intent(context, ZeroDataShareActivity.class));
						ZeroDataClientActivity.this.finish();
					};
				}, ResourceUtil.getString(this, R.string.cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
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
		case R.id.ic_qr_code_scan:
			if (isOnclick) {
				return false;
			}
			isOnclick = true;
			disConnectHostspot(currentServerIp, false, false);
			Intent intent = new Intent(context, CaptureActivity.class);
			intent.putExtra("fromActivity", ZeroDataClientActivity.this.getClass().getName());
			startActivity(intent);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_receive_scan, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setGuideDialog() {
		if (mDialog != null)
			return;
		mDialog = new Dialog(this, R.style.guideDialog);
		mDialog.setContentView(R.layout.guide_zerodatashare_head);
		Window dialogWindow = mDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
		lp.y = (int) this.getResources().getDimension(R.dimen.guide_wifishare_marginTop); // 新位置Y坐标
		dialogWindow.setAttributes(lp);
		guideIv = (ImageView) mDialog.findViewById(R.id.guide_head_iv);
		guideIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
			}
		});
		mDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				SharedPrefsUtil.putValue(ZeroDataClientActivity.this, "zeroClienIsFirst", false);
			}
		});
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}

	/**
	* 新手引导
	 */
	private void showGuideDialog() {
		zeroClienIsFirst = SharedPrefsUtil.getValue(this, "zeroClienIsFirst", true);
		if (zeroClienIsFirst == false)
			return;
		setGuideDialog();
	}

	/** 
	* @Title: setSkinTheme 
	* @Description: TODO 
	* @return void    
	*/
	private void setSkinTheme() {
		SkinConfigManager.getInstance().setTitleSkin(context, mTitleView, mNavigationView, mTitlePendant, null);
		SkinConfigManager.getInstance().setViewBackground(context, searchLl, SkinConstan.ITEM_THEME_BG);
		for (int i = 0; i < deviceChioced.length; i++) {
			SkinConfigManager.getInstance().setViewBackground(context, deviceChioced[i],
					SkinConstan.SHARE_DEVICE_SELECTED);
		}
	}

}
