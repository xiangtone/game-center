package com.x.business.zerodata.connection.helper;

import android.content.Context;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.x.business.account.AccountManager;
import com.x.business.zerodata.client.http.model.ClientEntity;
import com.x.publics.model.UserInfoBean;
import com.x.publics.utils.Utils;

public class ZeroDataConnectHelper {

	public static final int CLIENT_STATUS_CONNECTING = 0x00001 ;// 搜索超时
	public static final int CLIENT_STATUS_SUCCESSFUL = 0x00002 ;// 搜索到wifi返回结果
	public static final int CLIENT_STATUS_FAILURE = 0x00003 ;
	public static final int CLIENT_SEARCH_WIFI_TIMEOUT = 0x00004 ;// 搜索超时
	public static final int CLIENT_SCAN_WIFI_RESULT = 0x00005 ;// 搜索超时
	public static final int CLIENT_CONNECT_HOTSPOT_RESULT_TIMEOUT = 0x00006 ;// 搜索超时
	public static final int CLIENT_CONNECT_HOTSPOT_RESULT = 0x00007 ;// 连接上wifi热点
	public static final int CLIENT_CONNECT_SERVER_TIMEOUT = 0x00008 ;// 连接上wifi热点
	public static final int CLIENT_CONNECT_SERVER_NO_CONNECTION = 0x00009 ;// 连接上wifi热点
	public static final int CLIENT_CONNECT_DISCONNECT_SUCCESSFUL = 0x00010 ;// 连接上wifi热点
	
	
	
	public static final int CLIENT_WIFI_STATE_DISABLING = 0x00100 ;// wifi正在关闭
	public static final int CLIENT_WIFI_STATE_DISABLED = 0x00101 ;// 
	public static final int CLIENT_WIFI_STATE_ENABLING = 0x00102 ;// 创建热点成功
	public static final int CLIENT_WIFI_STATE_ENABLED = 0x00103 ;// 创建热点成功
	public static final int CLIENT_WIFI_WIFI_STATE_UNKNOWN = 0x00104 ;// 创建热点成功
	
	public static final int CLIENT_RETRY_CONNECT_AP = 0x00200 ;// 创建热点成功
	
	public static final int SERVER_WIFI_AP_STATE_DISABLING = 0x00300 ;// wifi正在关闭
	public static final int SERVER_WIFI_AP_STATE_DISABLED = 0x00301 ;// 
	public static final int SERVER_WIFI_AP_STATE_ENABLING = 0x00302 ;// 创建热点成功
	public static final int SERVER_WIFI_AP_STATE_ENABLED = 0x00303 ;// 创建热点成功
	public static final int SERVER_WIFI_AP_STATE_FAILED = 0x00304 ;// 创建热点成功
	public static final int SERVER_WIFI_AP_STATE_UNKNOWN = 0x00305 ;// 创建热点成功
	

	public static final int SERVER_ADD_DEVICE = 0x00400 ;// 新增设备
	public static final int SERVER_UPDATE_PROGRESS = 0x00401 ;// 更新进度
	public static final int SERVER_DISCONNECT = 0x00402 ;// 取消设备显示
	
	public static final int SERVER_SERVICE_START = 0x00500  ;
	
	public static final int SERVICE_SERVICE_INIT_START = 0x00501  ;
	public static final int SERVICE_SERVICE_RESTART = 0x00502  ;
	public static final int SERVICE_SERVICE_START_SUCCESSFUL = 0x00503  ;
	
	
	public static final int CLIENT_CONNECT_TYPE_AUTO = 0x00600 ;//客户端自动连接
	public static final int CLIENT_CONNECT_TYPE_HAND = 0x00601 ;//客户端手动连接
	
	public static final int CLIENT_RECONNECTION_COUNTER = 5 ;
	//一些常量
	public static final String SERVER_HOTSPOT_HEADER = "zApp";
	public static final String SERVER_HOTSPOT_PASSWORD = "mas12345";
	
	public static final String SERVER_HOTSPOT_TYPE = "ap";
	public static final String CLIENT_CONNECT_HOTSPOT_TYPE = "wt";

	public static final String WIFI_LOCK_NAME = "masLock";

	public static final int SERVER_MAX_CLIENTS = 3 ;
	
	/**
	 * 
	* @Title: getDevicesNameFromSSID 
	* @Description: TODO() 
	* @param @param SSID
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getDevicesNameFromSSID(String SSID) {
		if (!TextUtils.isEmpty(SSID)) {
			try {
				String devicesName = SSID.substring(SERVER_HOTSPOT_HEADER.length()+3, SSID.length()) ;
				return devicesName ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 
	* @Title: getDeviceLayout 
	* @Description: TODO() 
	* @param @param sdmList
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	public static Integer getDeviceLayout(List<ServerDeviceModel> sdmList){
		Integer[]  array = null ;
		if(sdmList!=null && sdmList.size()>0)
		{
			array = new Integer[sdmList.size()] ;
			for (int i = 0; i < sdmList.size(); i++) {
				array[i] = sdmList.get(i).getAdapterLayout() ;
			}
		}
		return getDeviceLayout(array) ;
	}
	
	public static Integer getServerDeviceLayout(List<ClientEntity> ceList){
		Integer[]  array = null ;
		if(ceList!=null && ceList.size()>0)
		{
			array = new Integer[ceList.size()] ;
			for (int i = 0; i < ceList.size(); i++) {
				array[i] = ceList.get(i).getDevicesLayout() ;
			}
		}
		return getDeviceLayout(array) ;
	}
	
	/**
	 * 获取显示位置
	 * @param array
	 * @return
	 */
	public static Integer getDeviceLayout(Integer[] array) {
		if (array == null || array.length == 0) {
			return 0;
		}
		Arrays.sort(array);
		if (array[0] == 0) {
			int size = array.length;
			for (int i = 0; i < size - 1; i++) {
				if ((array[i + 1] - array[i]) == 1) {
					continue;
				} else {
					return array[i] + 1;
				}
			}
			return array[size - 1] + 1;
		} else {
			return 0;
		}
	}

	public static Integer[] getCurrentDeviceLayout(Set set, int arraySize) {
		if (set == null || set.size() == 0)
			return null;

		Integer[] array = new Integer[arraySize];
		int i = 0;
		Iterator<ServerDeviceModel> iterator = set.iterator();
		while (iterator.hasNext()) {
			ServerDeviceModel sdm = iterator.next();
			if (sdm != null)
				;
			{
				array[i] = sdm.getAdapterLayout();
				i++;
			}
		}
		return array;
	}

	
	/**
	 * 获取用户昵称
	 * @param context
	 * @return
	 */
	public static String getZeroShareNickName(Context context)
	{
		String deviceModel = Utils.getDeviceModel() ;
		String mac = Utils.getMAC(context) ;
		UserInfoBean userInfo = AccountManager.getInstance().getUserInfo(context);
		String nickName = "" ;
		if(userInfo ==null || !userInfo.isLogin() ||TextUtils.isEmpty(userInfo.getNickName()))
		{
			if(!TextUtils.isEmpty(deviceModel))
			{
				nickName = deviceModel ;
				if(!TextUtils.isEmpty(mac))
				{
					nickName = deviceModel+"_"+mac.substring(mac.length()-2, mac.length()) ;
				}
			}
		}else
		{
			nickName = userInfo.getNickName() ;
		}
		return nickName ;
		
	}
	
}
