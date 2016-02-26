package com.x.publics.utils;

import java.util.UUID;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.x.publics.model.DownloadBean;
import com.x.publics.utils.Constan.MediaType;

public class NetworkUtils {

	//WIFI
	public static final String NETWORK_TYPE_WIFI = "WIFI";
	//无网络
	public static final String NETWORK_TYPE_NONE = "unknow";
	public static final String NETWORK_TYPE_MOBILE = "mobile";
	//当前正在使用的APN
	private static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	private static Uri PREFERRED_APN_URI2 = Uri.parse("content://telephony/carriers/preferapn2");

	/**
	 * 获取当前网络类型
	 * @param context
	 * @return
	 */
	public static String getNetworkInfo(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		String mNetworkInfo = NETWORK_TYPE_NONE;
		if (network != null && network.isAvailable()) {
			int type = network.getType();
			switch (type) {
			case ConnectivityManager.TYPE_WIFI:
				mNetworkInfo = NETWORK_TYPE_WIFI;
				break;

			case ConnectivityManager.TYPE_MOBILE:
				mNetworkInfo = NETWORK_TYPE_MOBILE;
				break;
			default:
				mNetworkInfo = NETWORK_TYPE_NONE;
				break;
			}
		}
		return mNetworkInfo;
	}

	/**
	* 获取当前正在使用的APN
	* @param context
	* @return
	*/
	public static String getCurrentAPN(Context context) {
		String apn = null;
		try {
			Cursor cursor = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				apn = cursor.getString(cursor.getColumnIndex("apn"));
				cursor.close();
				cursor = null;
			} else {
				cursor = context.getContentResolver().query(PREFERRED_APN_URI2, null, null, null, null);
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

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED
							|| info[i].getState() == NetworkInfo.State.CONNECTING) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String getFileNameFromUrl(String url) {
		// 通过 ‘？’ 和 ‘/’ 判断文件名
		int index = url.lastIndexOf('?');
		String filename;
		if (index > 1) {
			filename = url.substring(url.lastIndexOf('/') + 1, index);
		} else {
			filename = url.substring(url.lastIndexOf('/') + 1);
		}

		if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
			filename = UUID.randomUUID() + ".apk";// 默认取一个文件名
		}
		return filename;
	}

	public static String getFileNameFromDownloadBean(DownloadBean bean) {
		String filename;
		String url = bean.getUrl();
		if (bean.getMediaType().equals(MediaType.MUSIC)) {
			int index = url.lastIndexOf(".");
			String suffix = url.substring(index, url.length());
			filename = bean.getName() + suffix;
		} else if (bean.getMediaType().equals(MediaType.GAME) || bean.getMediaType().equals(MediaType.APP)) {
			filename = bean.getPackageName() + "_" + bean.getVersionCode() + ".apk";
		} else {
			// 通过 ‘？’ 和 ‘/’ 判断文件名
			int index = url.lastIndexOf('?');

			if (index > 1) {
				filename = url.substring(url.lastIndexOf('/') + 1, index);
			} else {
				filename = url.substring(url.lastIndexOf('/') + 1);
			}

			if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
				filename = UUID.randomUUID() + ".apk";// 默认取一个文件名
			}
		}

		return filename;
	}

}
