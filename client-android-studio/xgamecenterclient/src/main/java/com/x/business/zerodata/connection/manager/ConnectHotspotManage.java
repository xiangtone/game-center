package com.x.business.zerodata.connection.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;

import com.x.business.zerodata.connection.helper.WifiState;
import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;
import com.x.business.zerodata.connection.listener.ConnectHotspotResultListener;
import com.x.business.zerodata.connection.listener.ScanHotspotListener;
import com.x.business.zerodata.helper.ZeroDataConstant;
import com.x.business.zerodata.helper.ZeroDataResourceHelper;
import com.x.publics.utils.NetworkUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 
 
 *
 */
public class ConnectHotspotManage {

	public static ConnectHotspotManage connectHotspotManage;
	public Context context;

	public ScanHotspotListener scanHotspotListener; //WiFi搜索进度条线程
	public WifiApManager wifiApManager; //Wifi管理类
//	public ConnectHotspotResultListener connectHotspotResultListener;

	/**
	 * 构造子
	 * @param context
	 */
	public ConnectHotspotManage(Context context) {
		this.context = context;
		wifiApManager = WifiApManager.getInstance(context);
	}

	/**
	 * 单例
	 * @param context
	 * @return
	 */
	public static ConnectHotspotManage getInstance(Context context) {
		if (connectHotspotManage == null) {
			connectHotspotManage = new ConnectHotspotManage(context);
		}
		return connectHotspotManage;
	}

	public void scanHotSpot() {
		// 重新启动
		scanHotspotListener.stop();
		// 关闭热点
		if (wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_ENABLED
				|| wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_ENABLING) {
			wifiApManager.closeHotspot(CreatHotspotManager.getCreatHotSpotName(context));
			deleteZerodateAp();// 移除热点
		}

		if (!wifiApManager.mWifiManager.isWifiEnabled()) {
			wifiApManager.openWifi();// 打开wifi
		}

		wifiApManager.startScan(); // 开始搜索wifi
		scanHotspotListener.start();
	}

	public void reStartScan() {
		// 开始搜索wifi
		wifiApManager.startScan();
	}

	public void initScanHotSpotListener(Handler mHandler) {
		//搜索Wifi
		scanHotspotListener = new ScanHotspotListener(this.context, mHandler);
//		connectHotspotResultListener = new ConnectHotspotResultListener(this.context, mHandler);

	}

	/**
	 * 连接SSID
	 * @param SSID
	 */
	public void doConnectHotSpot(final String SSID) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				//打开wifi
				if (WifiState.WIFI_STATE_ENABLED != wifiApManager.mWifiManager.getWifiState()) {
					wifiApManager.openWifi();
				}
				WifiConfiguration localWifiConfiguration = wifiApManager.createWifiInfo(SSID,
						ZeroDataConnectHelper.SERVER_HOTSPOT_PASSWORD, 3,
						ZeroDataConnectHelper.CLIENT_CONNECT_HOTSPOT_TYPE);
				wifiApManager.addNetwork(localWifiConfiguration);
			}
		}).start();

	}
	
	/**
	 * 连接SSID
	 * @param SSID
	 */
	public boolean doConnectOpenAp(String SSID) {

			//打开wifi
			if (WifiState.WIFI_STATE_ENABLED != wifiApManager.mWifiManager.getWifiState()) {
				wifiApManager.openWifi();
			}
			if(TextUtils.isEmpty(SSID))
			{
				return false;
			}
			WifiConfiguration localWifiConfiguration = wifiApManager.createWifiInfo(SSID,
					ZeroDataConnectHelper.SERVER_HOTSPOT_PASSWORD, 1,
					ZeroDataConnectHelper.CLIENT_CONNECT_HOTSPOT_TYPE);
			return wifiApManager.addNetwork(localWifiConfiguration);
	}


	/**
	 * 停止扫描
	 */
	public void stopScanHotSpotListener() {
		scanHotspotListener.stop();
	}

	/**
	 * 获取扫描数据
	 * @return
	 */
	public List<ScanResult> getScanResults() {
		return wifiApManager.mWifiManager.getScanResults();
	}

	/**
	 * 开始扫描附近AP
	 */
	public void startScan() {
		wifiApManager.startScan();
	}

	/**
	 * 获取当前连接SSID
	 * @return
	 */
	public String getSSID() {
		return wifiApManager.getSSID();
	}


	/**
	 * 
	 * @return
	 */
	public String getServerIp() {
		String serverIp = (wifiApManager.getDhcpInfo() == null) ? null : wifiApManager.ipIntToString(wifiApManager
				.getDhcpInfo().gateway);
		return serverIp;
	}

	public int getWifiState() {
		return wifiApManager.mWifiManager.getWifiState();
	}
	
	public int getCurrentWifiNetworkId() {
		return  wifiApManager.getNetworkId();
	}
	
	public boolean  enableNetwork(int networkId) {
		return  wifiApManager.enableNetwork(networkId);
	}

	public boolean getScanHotSpotListenerStatus() {
		return scanHotspotListener.running;
	}

	public void stopConnectHotSpot() {
		if (scanHotspotListener != null) {
			// 重新启动
			scanHotspotListener.stop();
			// 关闭热点
		}
	}

	public void uninit() {
		if (scanHotspotListener != null) {
			scanHotspotListener.stop();
		}
	}


	public boolean isConnectWifi() {
		if (NetworkUtils.isNetworkAvailable(context)
				&& NetworkUtils.getNetworkInfo(context).equals(NetworkUtils.NETWORK_TYPE_WIFI)) {
			return true;
		}
		return false;

	}

	public boolean isHotspotEnable() {
		if ((wifiApManager.getWifiApState() == WifiState.WIFI_STATE_ENABLED || wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_ENABLED)
				&& (wifiApManager.getApSSID().startsWith(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER))) {
			return true;
		}

		return false;
	}
	
	/**
	 * 非zapp热点是否打开
	 */
	public boolean isOtherHotspotEnable(){
		if(wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_ENABLED
				&& !wifiApManager.getApSSID().startsWith(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER)){
			return true;
		}
		return false;
	}
	
	/**
	 * 关闭当前非zapp热点
	 */
	public void disableOtherHotspot(){
		wifiApManager.createHotspot(wifiApManager.getWifiApConfiguration(context) , false);
	}

	public boolean isHotspotEnabling() {
		return wifiApManager.getWifiApState() == WifiState.WIFI_AP_STATE_ENABLING;
	}

	/** 
	 * 断开当前连接的网络 
	 */
	public void disconnectWifi() {
		int netId = wifiApManager.getNetworkId();
		wifiApManager.disconnectWifi(netId);
	}

	public boolean isConnectedServer(String ssid) {
		if (ssid == null)
			return false;
		return getSSID().equals(ssid);
	}

	public boolean isDisConnectSSID(String ssid) {
		return TextUtils.isEmpty(ssid) || ssid.toLowerCase().endsWith("0x");
	}

	public void openWifi() {
		wifiApManager.openWifi();
	}

	public void closeWifi() {
		wifiApManager.closeWifi();
	}

	public boolean isWifiEnable() {
		return wifiApManager.isConnectWifi();
	}

	/**
	 * 
	* @Title: reSumeNetwork 
	* @Description: TODO(恢复网络) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void resumeClientNetwork()
	{
		resumWifiNetwork() ;
	}
	
	public void resumeUserHostSpot()
	{
		boolean isApEnable  =  ZeroDataResourceHelper.getInstance(context).prefers.getBoolean(ZeroDataConstant.ZERO_DATA_HOTSPOT_STATE_KEY,false) ;
		WifiConfiguration configuration = wifiApManager.getUserWifiConfiguration();
		if(configuration != null)
		{
			wifiApManager.createHotspot(configuration , true); // 打开一次才能记住配置
			if(!isApEnable)
			{
				wifiApManager.createHotspot(configuration , false);
			}
		}
	}
	
	public void resumWifiNetwork()
	{
		try {
//			closeWifi() ;
			//恢复WIFI状态
			int wifiState = ZeroDataResourceHelper.getInstance(context).prefers.getInt(ZeroDataConstant.ZERO_DATA_NETWORK_STATE_WIFI_KEY,-1) ;
			int networkId  = ZeroDataResourceHelper.getInstance(context).prefers.getInt(ZeroDataConstant.ZERO_DATA_NETWORK_STATE_WIFI_NETWORKID_KEY,0);
//			deleteZerodateAp() ;
			disConnectionZappWifi() ;
			switch (wifiState) {
			case WifiState.WIFI_STATE_ENABLED:
			case WifiState.WIFI_STATE_ENABLING:
				openWifi() ;
				if(networkId!= 0)
					this.enableNetwork(networkId);
				break;
			case WifiState.WIFI_STATE_DISABLED:
			case WifiState.WIFI_STATE_DISABLING:
			case WifiState.WIFI_STATE_UNKNOWN:
			default:
				closeWifi() ;
			}
		} catch (Exception e) {
			e.printStackTrace() ;
		}
	}
	
	//如果当前网络是zapp则断开
	public void disConnectionZappWifi()
	{
		//如果当前网络是zapp则断开
		if(!TextUtils.isEmpty(this.getSSID()) && this.getSSID().contains(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER))
		{
			disconnectWifi() ;
			deleteZerodateAp() ;
		}
	}
	
	/**
	 * 
	* @Title: deleteNetwork 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	 public void deleteZerodateAp()
	{
	 wifiApManager.deleteNetwork(ZeroDataConnectHelper.SERVER_HOTSPOT_HEADER);
	}
	/**
	 * 
	* @Title: reSumeNetwork 
	* @Description: TODO(恢复网络) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void resumeServerNetwork()
	{
		resumeUserHostSpot();
		resumWifiNetwork() ;
		//恢复数据连接状态
		boolean isDataConnetionOpen = ZeroDataResourceHelper.getInstance(context).prefers.getBoolean(ZeroDataConstant.ZERO_DATA_NETWORK_STATE_MOBILE_KEY,false) ;
		setMobileDataEnabled(isDataConnetionOpen) ;
		
	}
	/**
	 * 获取热点状态
	 * @return
	 */
	public int getWifiApstate() {
		return wifiApManager.getWifiApState();
	}
	
	/**
	 * 
	* @Title: saveNetworkState 
	* @Description: TODO(保存网络状态) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void saveNetworkState(Context context)
	{
		ZeroDataResourceHelper.getInstance(context).prefers.edit().putInt(ZeroDataConstant.ZERO_DATA_NETWORK_STATE_WIFI_NETWORKID_KEY,ConnectHotspotManage.getInstance(context).getCurrentWifiNetworkId()).commit() ;
		ZeroDataResourceHelper.getInstance(context).prefers.edit().putInt(ZeroDataConstant.ZERO_DATA_NETWORK_STATE_WIFI_KEY,ConnectHotspotManage.getInstance(context).getWifiState()).commit() ;
		ZeroDataResourceHelper.getInstance(context).prefers.edit().putBoolean(ZeroDataConstant.ZERO_DATA_NETWORK_STATE_MOBILE_KEY,ConnectHotspotManage.isDataConnectionAvailable(context)).commit() ;
		saveHostspotState(context);
	}
	
	
	/** 
	* @Title: saveHostspotState 
	* @Description: 保存热点状态 
	* @param @param context    
	* @return void    
	*/ 
	
	public void saveHostspotState(Context context)
	{
		boolean isOtherHostspotEnable = ConnectHotspotManage.getInstance(context).isOtherHotspotEnable();
		ZeroDataResourceHelper.getInstance(context).prefers.edit().putBoolean(
				ZeroDataConstant.ZERO_DATA_HOTSPOT_STATE_KEY, isOtherHostspotEnable).commit();// 保存手机初始热点状态
	}
	
	
	/**
	 * 
	* @Title: setMobileDataEnabled 
	* @Description: TODO(激活和关闭数据业务) 
	* @param @param context
	* @param @param enabled    设定文件 
	* @return void    返回类型 
	* @throws
	 */
    public void setMobileDataEnabled(boolean enabled) {
   	 try {
       final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       final Class conmanClass = Class.forName(conman.getClass().getName());
       final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
       iConnectivityManagerField.setAccessible(true);
       final Object iConnectivityManager = iConnectivityManagerField.get(conman);
       final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
       final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
       setMobileDataEnabledMethod.setAccessible(true);

       setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
   } catch (Exception e) {
		e.printStackTrace() ;
	}
   }
    
    /**
	 * 
	* @Title: isDataConnectionAvailable 
	* @Description: TODO(数据连接是否打开) 
	* @param @param context
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	public static boolean isDataConnectionAvailable(Context context) {
		ConnectivityManager  connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		      Class cmClass       = connectivityManager.getClass();  
	        Class[] argClasses  = null;  
	        Object[] argObject  = null;  
	        Boolean isOpen = false;  
	        try  
	        {  
	            Method method = cmClass.getMethod("getMobileDataEnabled", argClasses);  
	            isOpen = (Boolean) method.invoke(connectivityManager, argObject);  
	        } catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }  
	  
	        return isOpen;  
	}
}
