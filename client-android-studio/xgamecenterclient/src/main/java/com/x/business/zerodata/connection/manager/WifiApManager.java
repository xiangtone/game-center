package com.x.business.zerodata.connection.manager;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.x.business.zerodata.connection.helper.ZeroDataConnectHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 
 *
 */
public class WifiApManager {
	private static WifiApManager wifiApManager = null;
	public WifiConfiguration userWifiConfiguration;
	private List<WifiConfiguration> mWifiConfiguration; //无线网络配置信息类集合(网络连接列表)  
	private List<ScanResult> mWifiList; //检测到接入点信息类 集合

	//描述任何Wifi连接状态
	WifiManager.WifiLock mWifilock; //能够阻止wifi进入睡眠状态，使wifi一直处于活跃状态
	public WifiManager mWifiManager;

	/**
	 * 获取该类的实例（懒汉）
	 * @param context
	 * @return
	 */
	public static WifiApManager getInstance(Context context) {
		if (wifiApManager == null) {
			wifiApManager = new WifiApManager(context);
		}
		return wifiApManager;
	}

	public WifiApManager(Context context) {
		//获取系统Wifi服务   WIFI_SERVICE
		this.mWifiManager = (WifiManager) context.getSystemService("wifi");
		//获取连接信息
	}

	/**
	 * 是否存在网络信息
	 * @param str  热点名称
	 * @return
	 */
	private WifiConfiguration isExsits(String str) {
		if (this.mWifiManager.getConfiguredNetworks() == null)
			return null;
		Iterator localIterator = this.mWifiManager.getConfiguredNetworks().iterator();
		WifiConfiguration localWifiConfiguration;
		do {
			if (!localIterator.hasNext())
				return null;
			localWifiConfiguration = (WifiConfiguration) localIterator.next();
		} while (!localWifiConfiguration.SSID.contains(str));
		return localWifiConfiguration;
	}

	/**锁定WifiLock，当下载大文件时需要锁定 **/
	public void acquireWifiLock() {
		this.mWifilock.acquire();
	}

	/**创建一个WifiLock**/
	public void createWifiLock() {
		this.mWifilock = this.mWifiManager.createWifiLock(ZeroDataConnectHelper.WIFI_LOCK_NAME);
	}

	/**解锁WifiLock**/
	public void releaseWifilock() {
		if (mWifilock.isHeld()) { //判断时候锁定  
			mWifilock.acquire();
		}
	}

	/**打开Wifi**/
	public void openWifi() {
		if (!this.mWifiManager.isWifiEnabled()) { //当前wifi不可用
			this.mWifiManager.setWifiEnabled(true);
		}
	}

	/**关闭Wifi**/
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	/**端口指定id的wifi**/
	public void disconnectWifi(int paramInt) {
		this.mWifiManager.disableNetwork(paramInt);
	}

	/**添加指定网络**/
	public boolean addNetwork(WifiConfiguration paramWifiConfiguration) {
		int i = mWifiManager.addNetwork(paramWifiConfiguration);
		return mWifiManager.enableNetwork(i, true);
	}

	public boolean enableNetwork(int networkId) {
		return mWifiManager.enableNetwork(networkId, true);
	}

	/**
	 * 连接指定配置好的网络
	 * @param index 配置好网络的ID
	 */
	public void connectConfiguration(int index) {
		// 索引大于配置好的网络索引返回    
		if (index > mWifiConfiguration.size()) {
			return;
		}
		//连接配置好的指定ID的网络    
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
	}

	/**
	 * 根据wifi信息创建或关闭一个热点 
	 * @param paramWifiConfiguration
	 * @param paramBoolean 关闭标志
	 */
	public void createWifiAP(WifiConfiguration paramWifiConfiguration, boolean paramBoolean) {
		try {
			Class localClass = this.mWifiManager.getClass();
			Class[] arrayOfClass = new Class[2];
			arrayOfClass[0] = WifiConfiguration.class;
			arrayOfClass[1] = Boolean.TYPE;
			Method localMethod = localClass.getMethod("setWifiApEnabled", arrayOfClass);
			WifiManager localWifiManager = this.mWifiManager;
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = paramWifiConfiguration;
			arrayOfObject[1] = Boolean.valueOf(paramBoolean);
			localMethod.invoke(localWifiManager, arrayOfObject);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUserWifiConfiguration(Context context) {
		WifiConfiguration configuration = getWifiApConfiguration(context);
		if (!configuration.SSID.endsWith(CreatHotspotManager.getCreatHotSpotName(context))) {
			userWifiConfiguration = configuration;
		}
	}

	public WifiConfiguration getUserWifiConfiguration() {
		return userWifiConfiguration;
	}

	public WifiConfiguration getWifiApConfiguration(Context context) {
		final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		final Method m = getWifiManagerMethod("getWifiApConfiguration", wifiManager);
		if (m != null) {
			try {
				return (WifiConfiguration) m.invoke(wifiManager);
			} catch (Exception e) {
			}
		}
		return null;
	}

	private static Method getWifiManagerMethod(final String methodName, final WifiManager wifiManager) {
		final Method[] methods = wifiManager.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}

	/**
	 * 创建热点
	 * @param hotSpotName
	 */
	public void createHotspot(String hotSpotName) {
		WifiConfiguration wifiConfiguration = createWifiInfo(hotSpotName,
				ZeroDataConnectHelper.SERVER_HOTSPOT_PASSWORD, 3, ZeroDataConnectHelper.SERVER_HOTSPOT_TYPE);
		createWifiAP(wifiConfiguration, true);

	}
	/**
	 * 创建热点
	 * @param wifiConfiguration
	 */
	public void createHotspot(WifiConfiguration wifiConfiguration , boolean enable) {
		createWifiAP(wifiConfiguration, enable);

	}

	/**
	 * 关闭热点
	 */
	public void closeHotspot(String hotSpotName) {
		WifiConfiguration wifiConfiguration = createWifiInfo(hotSpotName,
				ZeroDataConnectHelper.SERVER_HOTSPOT_PASSWORD, 3, ZeroDataConnectHelper.SERVER_HOTSPOT_TYPE);
		createWifiAP(wifiConfiguration, false);

	}

	/**
	 * 创建热点
	 * @param hotSpotName
	 */
	public void createOpenHotspot(String hotSpotName) {
		WifiConfiguration wifiConfiguration = createWifiInfo(hotSpotName, null, 1,
				ZeroDataConnectHelper.SERVER_HOTSPOT_TYPE);
		createWifiAP(wifiConfiguration, true);

	}

	/**
	 * 关闭热点
	 */
	public void closeOpenHotspot(String hotSpotName) {
		WifiConfiguration wifiConfiguration = createWifiInfo(hotSpotName, null, 1,
				ZeroDataConnectHelper.SERVER_HOTSPOT_TYPE);
		createWifiAP(wifiConfiguration, false);

	}

	/** 
	 * 创建一个wifi信息 
	 * @param ssid 名称 
	 * @param passawrd 密码 
	 * @param paramInt 有3个参数，1是无密码，2是简单密码，3是wap加密 
	 * @param type 是"ap"还是"wifi" 
	 * @return 
	 */
	public WifiConfiguration createWifiInfo(String ssid, String passawrd, int paramInt, String type) {
		//配置网络信息类
		WifiConfiguration localWifiConfiguration1 = new WifiConfiguration();
		//设置配置网络属性
		localWifiConfiguration1.allowedAuthAlgorithms.clear();
		localWifiConfiguration1.allowedGroupCiphers.clear();
		localWifiConfiguration1.allowedKeyManagement.clear();
		localWifiConfiguration1.allowedPairwiseCiphers.clear();
		localWifiConfiguration1.allowedProtocols.clear();

		if (type.equals(ZeroDataConnectHelper.CLIENT_CONNECT_HOTSPOT_TYPE)) { //wifi连接
			localWifiConfiguration1.SSID = ("\"" + ssid + "\"");
			WifiConfiguration localWifiConfiguration2 = isExsits(ssid);
			if (localWifiConfiguration2 != null) {
				mWifiManager.removeNetwork(localWifiConfiguration2.networkId); //从列表中删除指定的网络配置网络
			}
			if (paramInt == 1) { //没有密码
				//				localWifiConfiguration1.wepKeys[0] = "";
				localWifiConfiguration1.allowedKeyManagement.set(0);
				//				localWifiConfiguration1.wepTxKeyIndex = 0;
			} else if (paramInt == 2) { //简单密码
				localWifiConfiguration1.hiddenSSID = true;
				localWifiConfiguration1.wepKeys[0] = ("\"" + passawrd + "\"");
			} else { //wap加密 
				localWifiConfiguration1.preSharedKey = ("\"" + passawrd + "\"");
				localWifiConfiguration1.hiddenSSID = true;
				localWifiConfiguration1.allowedAuthAlgorithms.set(0);
				localWifiConfiguration1.allowedGroupCiphers.set(2);
				localWifiConfiguration1.allowedKeyManagement.set(1);
				localWifiConfiguration1.allowedPairwiseCiphers.set(1);
				localWifiConfiguration1.allowedGroupCiphers.set(3);
				localWifiConfiguration1.allowedPairwiseCiphers.set(2);
			}
		} else {//"ap" wifi热点
			localWifiConfiguration1.SSID = ssid;
			localWifiConfiguration1.allowedAuthAlgorithms.set(1);
			localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			localWifiConfiguration1.allowedKeyManagement.set(0);
			localWifiConfiguration1.wepTxKeyIndex = 0;
			if (paramInt == 1) { //没有密码
				localWifiConfiguration1.wepKeys[0] = "";
				localWifiConfiguration1.allowedKeyManagement.set(0);
				localWifiConfiguration1.wepTxKeyIndex = 0;
			} else if (paramInt == 2) { //简单密码
				localWifiConfiguration1.hiddenSSID = true;//网络上不广播ssid
				localWifiConfiguration1.wepKeys[0] = passawrd;
			} else if (paramInt == 3) {//wap加密
				localWifiConfiguration1.preSharedKey = passawrd;
				localWifiConfiguration1.allowedAuthAlgorithms.set(0);
				localWifiConfiguration1.allowedProtocols.set(1);
				localWifiConfiguration1.allowedProtocols.set(0);
				localWifiConfiguration1.allowedKeyManagement.set(1);
				localWifiConfiguration1.allowedPairwiseCiphers.set(2);
				localWifiConfiguration1.allowedPairwiseCiphers.set(1);
			}
		}
		return localWifiConfiguration1;
	}

	/**获取热点名**/
	public String getApSSID() {
		try {
			Method localMethod = this.mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration", new Class[0]);
			if (localMethod == null)
				return null;
			Object localObject1 = localMethod.invoke(this.mWifiManager, new Object[0]);
			if (localObject1 == null)
				return null;
			WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
			if (localWifiConfiguration.SSID != null)
				return localWifiConfiguration.SSID;
			Field localField1 = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
			if (localField1 == null)
				return null;
			localField1.setAccessible(true);
			Object localObject2 = localField1.get(localWifiConfiguration);
			localField1.setAccessible(false);
			if (localObject2 == null)
				return null;
			Field localField2 = localObject2.getClass().getDeclaredField("SSID");
			localField2.setAccessible(true);
			Object localObject3 = localField2.get(localObject2);
			if (localObject3 == null)
				return null;
			localField2.setAccessible(false);
			String str = (String) localObject3;
			return str;
		} catch (Exception localException) {
		}
		return null;
	}

	/**得到配置好的网络 **/
	public List<WifiConfiguration> getConfiguration() {
		return this.mWifiConfiguration;
	}

	/**获取ip地址**/
	public int getIPAddress() {
		return (getWifiInfo() == null) ? 0 : getWifiInfo().getIpAddress();
	}

	/**获取物理地址(Mac)**/
	public String getMacAddress() {
		return (getWifiInfo() == null) ? "NULL" : getWifiInfo().getMacAddress();
	}

	/**获取网络id**/
	public int getNetworkId() {
		return (getWifiInfo() == null) ? 0 : getWifiInfo().getNetworkId();
	}

	/**获取热点创建状态**/
	public int getWifiApState() {
		try {
			int i = ((Integer) this.mWifiManager.getClass().getMethod("getWifiApState", new Class[0])
					.invoke(this.mWifiManager, new Object[0])).intValue();
			return i;
		} catch (Exception localException) {
		}
		return 4; //未知wifi网卡状态
	}

	/**获取wifi连接信息**/
	public WifiInfo getWifiInfo() {
		return this.mWifiManager.getConnectionInfo();
	}

	/** 得到网络列表**/
	public List<ScanResult> getWifiList() {
		return this.mWifiList;
	}

	/**查看扫描结果**/
	public StringBuilder lookUpScan() {
		StringBuilder localStringBuilder = new StringBuilder();
		for (int i = 0; i < mWifiList.size(); i++) {
			localStringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
			//将ScanResult信息转换成一个字符串包  
			//其中把包括：BSSID、SSID、capabilities、frequency、level  
			localStringBuilder.append((mWifiList.get(i)).toString());
			localStringBuilder.append("\n");
		}
		return localStringBuilder;
	}

	/** 设置wifi搜索结果 **/
	public void setWifiList() {
		this.mWifiList = this.mWifiManager.getScanResults();
	}

	/**开始搜索wifi**/
	public void startScan() {
		this.mWifiManager.startScan();
	}

	/**得到接入点的BSSID**/
	public String getBSSID() {
		return (getWifiInfo() == null) ? "" : getWifiInfo().getBSSID();
	}

	/**得到接入点的SSID**/
	public String getSSID() {
		return (getWifiInfo() == null) ? "" : getWifiInfo().getSSID();
	}

	/**删除指定的SSID**/
	public boolean deleteNetwork(String ssid) {

		if (this.mWifiManager.getConfiguredNetworks() == null)
			return false;
		Iterator localIterator = this.mWifiManager.getConfiguredNetworks().iterator();
		WifiConfiguration localWifiConfiguration;

		while (localIterator.hasNext() && (localWifiConfiguration = (WifiConfiguration) localIterator.next()) != null) {
			if (localWifiConfiguration.SSID.contains(ssid)) {
				mWifiManager.removeNetwork(localWifiConfiguration.networkId); //从列表中删除指定的网络配置网络	
			}

		}
		return true;
	}

	/**获取所有连接到本wifi热点的手机IP地址  **/
	private ArrayList<String> getConnectedIP() {
		ArrayList<String> connectedIP = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				Log.d("meng", line);
				String[] splitted = line.split(" +");
				if (splitted != null && splitted.length >= 4) {
					String ip = splitted[0];
					connectedIP.add(ip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return connectedIP;
	}

	public DhcpInfo getDhcpInfo() {
		return this.mWifiManager.getDhcpInfo();
	}

	public String getServerIp() {
		String serverIp = (getDhcpInfo() == null) ? null : ipIntToString(getDhcpInfo().gateway);
		return serverIp;
	}

	/**将int类型的IP转换成字符串形式的IP**/
	public String ipIntToString(int ip) {
		try {
			byte[] bytes = new byte[4];
			bytes[0] = (byte) (0xff & ip);
			bytes[1] = (byte) ((0xff00 & ip) >> 8);
			bytes[2] = (byte) ((0xff0000 & ip) >> 16);
			bytes[3] = (byte) ((0xff000000 & ip) >> 24);
			return Inet4Address.getByAddress(bytes).getHostAddress();
		} catch (Exception e) {
			return "";
		}
	}

	public void uninit() {
		wifiApManager = null;
	}

	/**
	 * 获取是否连接wifi
	 * @return
	 */
	public boolean isConnectWifi() {
		return this.mWifiManager.isWifiEnabled();
	}

}