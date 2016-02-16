package com.x.business.zerodata.connection.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.x.business.zerodata.connection.helper.WifiState;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.listener.ScanHotspotListener;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.publics.utils.ToastUtil;

/**
 * 
 
 *
 */
public class CreatHotspotManager {

	public static CreatHotspotManager creatHotspotManager;
	public Context context;
	public ScanHotspotListener scanHotspotListener; //WiFi搜索进度条线程
	public WifiApManager wiFiAdmin; //Wifi管理类

	public CreatHotspotManager(Context context) {
		this.context = context;
		wiFiAdmin = WifiApManager.getInstance(context);
	}

	/**
	 * 单例
	 * @param context
	 * @return
	 */
	public static CreatHotspotManager getInstance(Context context) {
		if (creatHotspotManager == null) {
			creatHotspotManager = new CreatHotspotManager(context);
		}

		return creatHotspotManager;
	}

	/**
	 * 创建热点
	 */
	public void createHotspot() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//该设备是否支持热点
				if (wiFiAdmin.getWifiApState() == WifiState.WIFI_STATE_UNKNOWN) {
					handleConnectResult(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_UNKNOWN);
				} else {
					//关闭wifi
					wiFiAdmin.closeWifi();

					//创建WiFi热点
					wiFiAdmin.createHotspot(getCreatHotSpotName(context));
				}
			}
		}).start();

	}

	/**
	 * 创建热点
	 */
	private void createOpenHotspot() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				//该设备是否支持热点
				if (wiFiAdmin.getWifiApState() == WifiState.WIFI_STATE_UNKNOWN) {
					handleConnectResult(ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_UNKNOWN);
				} else {
					// 
					wiFiAdmin.setUserWifiConfiguration(context);
					doCreatOpenHotspot();
				}
			}
		}).start();

	}

	public void doCreatOpenHotspot() {
		//关闭wifi
		wiFiAdmin.closeWifi();
		//创建WiFi热点
		wiFiAdmin.createOpenHotspot(getCreatHotSpotName(context));
	}

	public void closeOpenHotspot() {
		wiFiAdmin.closeOpenHotspot(getCreatHotSpotName(context));
	}

	/**
	 * 重新创建开放热点
	 */
	public boolean reCreateOpenHotspot() {
		if (!ConnectHotspotManage.getInstance(context).isHotspotEnable()
				&& !ConnectHotspotManage.getInstance(context).isHotspotEnabling()) {
			boolean isOtherHostspotEnable = ConnectHotspotManage.getInstance(context).isOtherHotspotEnable();
			if (isOtherHostspotEnable) {//非zapp热点是否打开
				ConnectHotspotManage.getInstance(context).disableOtherHotspot();
			}

			createOpenHotspot();
			return true;
		}
		return false;
	}

	/**
	 * 重新创建热点
	 */
	public boolean reCreateHotspot() {
		if (!ConnectHotspotManage.getInstance(context).isHotspotEnable()
				&& !ConnectHotspotManage.getInstance(context).isHotspotEnabling()) {
			createHotspot();
			return true;
		}
		return false;
	}

	public String getServerApIp() {

		return wiFiAdmin.getServerIp();
	}

	/**
	 * 关闭热点
	 */
	public void closeZeroDataHotspot() {
		if ((wiFiAdmin.getWifiApState() == WifiState.WIFI_STATE_ENABLED)
				|| (wiFiAdmin.getWifiApState() == WifiState.WIFI_AP_STATE_ENABLED)
				&& (wiFiAdmin.getApSSID().startsWith(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER))) {//目前连接着自己指定的Wifi热点
			//关闭热点
			wiFiAdmin.closeHotspot(getCreatHotSpotName(context));

		}
	}

	/**
	 * 获取热点创建的状态
	 * @return
	 */
	public int getWifiApState() {
		return wiFiAdmin.getWifiApState();
	}

	/**
	 * 获取热点名称
	 * @return
	 */
	public static String getCreatHotSpotName(Context context) {
		String hotspotName = ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER +"_"+String.valueOf(ZeroDataResourceHelper.getSelfZerodataHeadPortraitValue(context))+ "_" + ZeroDataConnectHelper.getZeroShareNickName(context);
		if(hotspotName.length() > 32){
			hotspotName = hotspotName.substring(0, 32);
		}
		return hotspotName;
	}

	/**
	 * Handler
	 */
	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//该设备不支持创建热点
			case ZeroDataConnectHelper.SERVER_WIFI_AP_STATE_UNKNOWN:
				ToastUtil.show(context, "your devices nonsupport create hotspot", Toast.LENGTH_SHORT);
				break;
			}

		};
	};

	/**
	 * 发送连接结果消息
	 * @param connectResult
	 */
	public void handleConnectResult(int connectResult) {
		Message msg = mHandler.obtainMessage(connectResult);
		mHandler.sendMessage(msg);
	}

}
