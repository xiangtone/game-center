package com.x.publics.download.upgrade;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class SystemInfo {

	// WIFI
	public static final String NETWORK_TYPE_WIFI = "WIFI";
	// 移动
	public static final String NETWORK_TYPE_CMWAP = "cmwap";
	public static final String NETWORK_TYPE_CMNET = "cmnet";
	// 联通
	public static final String NETWORK_TYPE_3GWAP = "3gwap";
	public static final String NETWORK_TYPE_3GNET = "3gnet";
	public static final String NETWORK_TYPE_UNIWAP = "uniwap";
	public static final String NETWORK_TYPE_UNINET = "uninet";
	// 电信
	public static final String NETWORK_TYPE_CTWAP = "ctwap";
	public static final String NETWORK_TYPE_CTNET = "ctnet";
	// 其他扩展类型
	public static final String NETWORK_TYPE_MOBILE_DUN = "dun";
	public static final String NETWORK_TYPE_MOBILE_HIPRI = "hipri";
	public static final String NETWORK_TYPE_MOBILE_MMS = "mms";
	public static final String NETWORK_TYPE_MOBILE_SUPL = "supl";
	public static final String NETWORK_TYPE_WIMAX = "wimax";
	// 无网络
	public static final String NETWORK_TYPE_NONE = "unknow";
	// SIM卡类型
	public static final String CHINA_MOBILE = "mobile";// "移动";
	public static final String CHINA_UNICOM = "unicom";// "联通";
	public static final String CHINA_TELECOM = "telecom";// "电信";
	public static final String UNKNOW_TYPE = "unknow";// "未知";
	// 当前正在使用的APN
	private static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");
	private static Uri PREFERRED_APN_URI2 = Uri
			.parse("content://telephony/carriers/preferapn2");

	public static boolean ENCRYPT_UPLOAD_DATA = true;// 是否加密上传数据
	public static boolean DECRYPT_DOWNLOAD_DATA = true;// 是否解密下载数据
	public static boolean NEED_INSTALL_ASSET_VPS = true;// 是否需要安装VPS
	public static boolean USE_SPEAK_CACHE_ENABLE = true;// 是否使用缓存
	public static boolean USE_SPEAK_CACHE_FIRST = false;// 是否优先使用缓存

	/**
	 * 获取当前网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkInfo(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		String mNetworkInfo = NETWORK_TYPE_NONE;
		if (network != null && network.isAvailable())// && network.isConnected()
														// //&&
														// network.isAvailable()
		{
			int type = network.getType();
			switch (type) {
			case ConnectivityManager.TYPE_WIFI:
				mNetworkInfo = NETWORK_TYPE_WIFI;
				break;

			case ConnectivityManager.TYPE_MOBILE:
			case 15:// 兼容马维尔平台方案
				String apnName = getCurrentAPN(context);
				if (apnName != null) {
					mNetworkInfo = apnName.toLowerCase();
				}
				break;

			case ConnectivityManager.TYPE_MOBILE_DUN:
				mNetworkInfo = NETWORK_TYPE_MOBILE_DUN;
				break;

			case ConnectivityManager.TYPE_MOBILE_HIPRI:
				mNetworkInfo = NETWORK_TYPE_MOBILE_HIPRI;
				break;

			case ConnectivityManager.TYPE_MOBILE_MMS:
				mNetworkInfo = NETWORK_TYPE_MOBILE_MMS;
				break;

			case ConnectivityManager.TYPE_MOBILE_SUPL:
				mNetworkInfo = NETWORK_TYPE_MOBILE_SUPL;
				break;

			case ConnectivityManager.TYPE_WIMAX:
				mNetworkInfo = NETWORK_TYPE_WIMAX;
				break;

			default:
				mNetworkInfo = NETWORK_TYPE_NONE;
				break;
			}
		}
		return mNetworkInfo;
	}

	/**
	 * 判断当前是否为wap网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isCurrentWapNetwork(Context context) {
		boolean iswap = false;
		String networkInfo = getNetworkInfo(context);
		if (networkInfo.equals(NETWORK_TYPE_CMWAP)
				|| networkInfo.equals(NETWORK_TYPE_3GWAP)
				|| networkInfo.equals(NETWORK_TYPE_UNIWAP)
				|| networkInfo.equals(NETWORK_TYPE_CTWAP)) {
			iswap = true;
		}
		return iswap;
	}

	/**
	 * 获取当前正在使用的APN
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentAPN(Context context) {
		String apn = null;
		try {
			Cursor cursor = context.getContentResolver().query(
					PREFERRED_APN_URI, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				apn = cursor.getString(cursor.getColumnIndex("apn"));
				cursor.close();
				cursor = null;
			} else {
				cursor = context.getContentResolver().query(PREFERRED_APN_URI2,
						null, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					apn = cursor.getString(cursor.getColumnIndex("apn"));
					cursor.close();
					cursor = null;
				}
			}
		} catch (Exception e) {

		}
		return apn;
	}

	/**
	 * 检测当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean available = false;
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null && network.isAvailable() && network.isConnected())// 当前网络可用并且已经连接
		{
			available = true;
		}
		return available;
	}

}
