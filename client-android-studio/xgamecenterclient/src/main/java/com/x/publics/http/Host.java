/**   
 * @Title: Host.java
 * @Package com.x.http
 * @Description: TODO 
 
 * @date 2014-1-15 下午04:16:37
 * @version V1.0   
 */

package com.x.publics.http;

import java.io.File;

import android.os.Environment;

/**
 * @ClassName: Host
 * @Description: TODO
 
 * @date 2014-1-15 下午04:16:37
 * 
 */

public class Host {
	public static String URL = "";
//	"http://d2-xin-li.hq.ta-mp.com:8080/aMineWs/",内网测试地址
	public static String[] host = {"http://121.40.137.42:8881/",// mas
			"http://121.40.137.42:8881/",// hongkong 
			"http://121.40.137.42:8881/" 
			};//singapore
//	public static String[] host = {"http://10.128.165.11:81/",// mas
//	  "http://203.90.239.11/",// hongkong 
//	  "http://client.uplay360.com/" 
//	};//singapore
	
	/**
	 * testin服务器配置
	 * */
	/*
	 * public static String[] host = { "http://10.128.165.11/",// mas
	 * "http://203.90.239.11/",// hongkong "http://203.90.239.11/" };//
	 * singapore
	 */

	// 配置本地测试服务器
	public static final String uplay_mas = "uplay_mas.bin";
	// 配置香港测试服务器
	public static final String uplay_hongkong = "uplay_hongkong.bin";

	/**
	 * SDK服务器配置标示
	 */
	public static final int DOMAIN_SERIAL_NUMBER_MAS = 0;
	public static final int DOMAIN_SERIAL_NUMBER_HONGKONG = 1;
	public static final int DOMAIN_SERIAL_NUMBER_SINGAPORE = 2;

	/**
	 * 配置主机地址
	 * 
	 * @return
	 */
	public static void initHost() {
		if (isCheckFileExists(uplay_mas)) {
			URL = host[0];
		} else if (isCheckFileExists(uplay_hongkong)) {
			URL = host[1];
		} else {
			URL = host[2];
		}

	}

	/**
	 * @Title: getZappDomain
	 * @Description: TODO(获取对应服务器序列)
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int getZappDomain() {
		if (URL.equals(host[0])) {
			return 0;
		} else if (URL.equals(host[1])) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 如果配置香港服务器则是测试模式
	 * 
	 * @return
	 */
	public static boolean isMasSdkDebug() {
		if (isCheckFileExists(uplay_hongkong)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取server hint
	 * 
	 * @return
	 */
	public static String getHostHint() {
		if (Host.URL.equals(host[0])) {
			return "[zapp-server:mas]";
		} else if (Host.URL.equals(host[1])) {
			return "[zapp-server:hongkong]";
		}
		return "";
	}

	// /**
	// * 获取 sdk server hint
	// *
	// * @return
	// */
	// public static String getSDKHostHint(Context context) {
	//
	// int serverNum = AccountManager.getInstance().getSDKDomain();
	// if (serverNum == DOMAIN_SERIAL_NUMBER_MAS) {
	// return " [sdk-server:mas]";
	// } else if (serverNum == DOMAIN_SERIAL_NUMBER_HONGKONG) {
	// return " [sdk-server:hongkong]";
	// }
	// return "";
	//
	// }

	/**
	 * 
	 * @param sdkDebugFileName
	 * @return
	 */
	public static boolean isCheckFileExists(String debugFileName) {
		try {
			File checkDebugFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator
					+ debugFileName);
			if (checkDebugFile.exists()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
