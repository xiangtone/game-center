package com.x.publics.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 手机基本信息读取及当前应用相关信息获取类
 
 *
 */
public class SystemInfo {

	//WIFI
	public static final String NETWORK_TYPE_WIFI = "WIFI";
	//移动
	public static final String NETWORK_TYPE_CMWAP = "cmwap";
	public static final String NETWORK_TYPE_CMNET = "cmnet";
	//联通
	public static final String NETWORK_TYPE_3GWAP = "3gwap";
	public static final String NETWORK_TYPE_3GNET = "3gnet";
	public static final String NETWORK_TYPE_UNIWAP = "uniwap";
	public static final String NETWORK_TYPE_UNINET = "uninet";
	//电信
	public static final String NETWORK_TYPE_CTWAP = "ctwap";
	public static final String NETWORK_TYPE_CTNET = "ctnet";
	//其他扩展类型
	public static final String NETWORK_TYPE_MOBILE_DUN = "dun";
	public static final String NETWORK_TYPE_MOBILE_HIPRI = "hipri";
	public static final String NETWORK_TYPE_MOBILE_MMS = "mms";
	public static final String NETWORK_TYPE_MOBILE_SUPL = "supl";
	public static final String NETWORK_TYPE_WIMAX = "wimax";
	//无网络
	public static final String NETWORK_TYPE_NONE = "unknow";
	//SIM卡类型
	public static final String CHINA_MOBILE = "mobile";//"移动";
	public static final String CHINA_UNICOM = "unicom";//"联通";
	public static final String CHINA_TELECOM = "telecom";//"电信";
	public static final String UNKNOW_TYPE = "unknow";//"未知";
	//当前正在使用的APN
	private static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	private static Uri PREFERRED_APN_URI2 = Uri.parse("content://telephony/carriers/preferapn2");

	public static boolean ENCRYPT_UPLOAD_DATA = true;//是否加密上传数据
	public static boolean DECRYPT_DOWNLOAD_DATA = true;//是否解密下载数据
	public static boolean NEED_INSTALL_ASSET_VPS = true;//是否需要安装VPS
	public static boolean USE_SPEAK_CACHE_ENABLE = true;//是否使用缓存
	public static boolean USE_SPEAK_CACHE_FIRST = false;//是否优先使用缓存

	/**
	 * 获取代理
	 */
	public static Proxy getProxy(Context context) {
		Proxy proxy = null;
		try {
			if (!getNetworkInfo(context).equals(NETWORK_TYPE_WIFI)) {
				String proxyHost = android.net.Proxy.getDefaultHost();
				int proxyPort = android.net.Proxy.getDefaultPort();
				if (proxyHost != null && proxyHost.length() > 0 && proxyPort != -1) {
					proxy = new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
				}
			}
		} catch (Exception e) {
			LogUtil.getLogger().e("SystemInfo", e.getMessage());
			e.printStackTrace();
		}
		return proxy;
	}

	/**
	 * 检测当前网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean available = false;
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null && network.isAvailable() && network.isConnected())//当前网络可用并且已经连接
		{
			available = true;
		}
		return available;
	}

	/**
	 * 判断当前是否为wap网络
	 * @param context
	 * @return
	 */
	public static boolean isCurrentWapNetwork(Context context) {
		boolean iswap = false;
		String networkInfo = getNetworkInfo(context);
		if (networkInfo.equals(NETWORK_TYPE_CMWAP) || networkInfo.equals(NETWORK_TYPE_3GWAP)
				|| networkInfo.equals(NETWORK_TYPE_UNIWAP) || networkInfo.equals(NETWORK_TYPE_CTWAP)) {
			iswap = true;
		}
		return iswap;
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
			LogUtil.getLogger().e("SystemInfo", e.getMessage());
			e.printStackTrace();
		}
		return apn;
	}

	/**
	 * 获取当前网络类型
	 * @param context
	 * @return
	 */
	public static int getNetworkType(Context context) {
		int networkType = TelephonyManager.NETWORK_TYPE_UNKNOWN;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		networkType = tm.getNetworkType();
		return networkType;
	}

	/**
	 * 获取当前网络类型
	 * @param context
	 * @return
	 */
	public static String getNetworkInfo(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		String mNetworkInfo = NETWORK_TYPE_NONE;
		if (network != null && network.isAvailable())// && network.isConnected() //&& network.isAvailable() 
		{
			int type = network.getType();
			switch (type) {
			case ConnectivityManager.TYPE_WIFI:
				mNetworkInfo = NETWORK_TYPE_WIFI;
				break;

			case ConnectivityManager.TYPE_MOBILE:
			case 15://兼容马维尔平台方案
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
	 * 获取手机唯一编号
	 */
	private static String imei;//手机设备唯一编号

	public static String getIMEI(Context context) {
		if (imei == null && context != null) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();// 手机串号
			if (imei == null || imei.length() == 0) {
				imei = "000000000000000";
			}
		}
		return imei;
	}

	/**
	 * 获取SIM卡唯一编号
	 */
	private static String imsi;//SIM卡唯一编号

	public static String getIMSI(Context context) {
		if (imsi == null && context != null) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imsi = tm.getSubscriberId();
			if (imsi == null || imsi.length() == 0) {
				imsi = "000000000000000";
			}
		}
		return imsi;
	}

	/**
	 * 获取手机型号
	 */
	private static String deviceModel;//手机型号

	public static String getDeviceModel() {
		if (deviceModel == null) {
			deviceModel = android.os.Build.MODEL;
		}
		return deviceModel;
	}

	/**
	 * 获取手机型号
	 */
	private static String manufacturer;//设备厂商型号

	public static String getDeviceManufacturer() {
		if (manufacturer == null) {
			manufacturer = android.os.Build.MANUFACTURER;
		}
		return manufacturer;
	}

	/**
	 * 获取Android系统版本
	 */
	private static String androidRelease;//Android系统版本

	public static String getAndroidRelease() {
		if (androidRelease == null) {
			androidRelease = android.os.Build.VERSION.RELEASE;
		}
		return androidRelease;
	}

	/**
	 * 获取Android系统API版本号
	 */
	private static int androidSDKINT;//Android系统API版本号

	public static int getAndroidSDKINT() {
		if (androidSDKINT == 0) {
			androidSDKINT = android.os.Build.VERSION.SDK_INT;
		}
		return androidSDKINT;
	}

	/**
	 * 获取手机号码
	 */
	private static String phoneNumber;//手机号码

	public static String getPhoneNumber(Context context) {
		if (phoneNumber == null) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			phoneNumber = tm.getLine1Number();
		}
		return phoneNumber;
	}

	/**
	 * 获取运营商
	 */
	private static String cardType;//运营商

	public static String getCardType(Context context) {
		if (cardType == null) {
			String imsi = getIMSI(context);
			cardType = UNKNOW_TYPE;
			if (imsi != null) {
				if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
					cardType = CHINA_MOBILE;// 中国移动
				} else if (imsi.startsWith("46001")) {
					cardType = CHINA_UNICOM;// 中国联通
				} else if (imsi.startsWith("46003")) {
					cardType = CHINA_TELECOM;// 中国电信
				}
			}
		}
		return cardType;
	}

	/**
	 * 获取MAC地址
	 */
	private static String mac;//MAC地址

	public static String getMAC(Context context) {
		if (mac == null) {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			if (info != null) {
				mac = info.getMacAddress();
			}
		}
		return mac;
	}

	/**
	 * 获取屏幕宽度
	 */
	private static int mScreenWidthPixels;// 宽

	public static int getScreenWidth(Context context) {
		if (mScreenWidthPixels <= 0) {
			setScreenInfomation(context);
		}
		return mScreenWidthPixels;
	}

	/**
	 * 获取屏幕高度
	 */
	private static int mScreenHeightPixels;// 高

	public static int getScreenHeight(Context context) {
		if (mScreenHeightPixels <= 0) {
			setScreenInfomation(context);
		}
		return mScreenHeightPixels;
	}

	/**
	 * 获取屏幕分辨率
	 */
	private static String screen;//屏幕分辨率

	public static String getScreen(Context context) {
		if (screen == null) {
			screen = getScreenWidth(context) + "x" + getScreenHeight(context);
		}
		return screen;
	}

	/**
	 * 获取屏幕尺寸
	 */
	private static String screenSize;//屏幕分辨率

	public static String getScreenSize(Context context) {
		if (screenSize == null) {
			screenSize = String.format("%.1f",
					Math.sqrt((Math.pow(getScreenWidth(context), 2) + Math.pow(getScreenHeight(context), 2)))
							/ getDensityDpi(context));
		}
		return screenSize;
	}

	/**
	 * 获取屏幕密度
	 */
	private static float density;// 屏幕密度

	public static float getDensity(Context context) {
		if (density <= 0f) {
			setScreenInfomation(context);
		}
		return density;
	}

	/**
	 * 获取屏幕密度系数
	 */
	private static int densityDpi;// 屏幕密度系数

	public static int getDensityDpi(Context context) {
		if (densityDpi <= 0) {
			setScreenInfomation(context);
		}
		return densityDpi;
	}

	/**
	 * 设置当前屏幕分辨率
	 */
	public static void setScreenInfomation(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		mScreenWidthPixels = dm.widthPixels;
		mScreenHeightPixels = dm.heightPixels;
		if (mScreenHeightPixels > 0 && mScreenHeightPixels > 0) {
			screen = mScreenWidthPixels + "x" + mScreenHeightPixels;
		}
		density = dm.density;
		densityDpi = dm.densityDpi;
	}

	/**
	 * 获取当前SIM卡的状态
	 * @param context
	 * @return
	 */
	public static int getSimState(Context context) {
		int simState = TelephonyManager.SIM_STATE_UNKNOWN;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			simState = tm.getSimState();// sim卡当前状态
		} catch (Exception e) {
			LogUtil.getLogger().e("SystemInfo", e.getMessage());
			e.printStackTrace();
		}
		return simState;
	}

	/**
	  * 获取手机CellId
	  * @param context
	  * @return
	  */
	private static int cellId;

	public static int getCellId(Context context) {
		if (cellId == 0) {
			cellId = -1;
			if (context != null) {
				try {

					int ACCESS_FINE_LOCATION_PERMISSION = context
							.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
					int ACCESS_COARSE_LOCATION_PERMISSION = context
							.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
					if (ACCESS_FINE_LOCATION_PERMISSION == PackageManager.PERMISSION_GRANTED
							|| ACCESS_COARSE_LOCATION_PERMISSION == PackageManager.PERMISSION_GRANTED) {
						TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
						// 返回值MCC + MNC  
						String operator = tm.getNetworkOperator();
						if (operator != null && operator.length() >= 3) {
							mcc = Integer.parseInt(operator.substring(0, 3));
							mnc = Integer.parseInt(operator.substring(3));
						}
						CellLocation cellLocation = tm.getCellLocation();
						if (cellLocation != null) {
							if (cellLocation instanceof GsmCellLocation) {
								GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
								cellId = gsmCellLocation.getCid();
								lac = gsmCellLocation.getLac();
							} else if (cellLocation instanceof CdmaCellLocation) {
								CdmaCellLocation cdma = (CdmaCellLocation) cellLocation;
								cellId = cdma.getBaseStationId();
								lac = cdma.getNetworkId();
								mnc = cdma.getSystemId();
							}
						}
					}
				} catch (Exception e) {
					LogUtil.getLogger().e("SystemInfo", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return cellId;
	}

	/**
	  * 获取手机Lac
	  * @param context
	  * @return
	  */
	private static int lac;

	public static int getLac(Context context) {
		if (lac == 0) {
			lac = -1;
			if (context != null) {
				try {
					int ACCESS_FINE_LOCATION_PERMISSION = context
							.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
					int ACCESS_COARSE_LOCATION_PERMISSION = context
							.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
					if (ACCESS_FINE_LOCATION_PERMISSION == PackageManager.PERMISSION_GRANTED
							|| ACCESS_COARSE_LOCATION_PERMISSION == PackageManager.PERMISSION_GRANTED) {
						TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
						// 返回值MCC + MNC  
						String operator = tm.getNetworkOperator();
						if (operator != null && operator.length() >= 3) {
							mcc = Integer.parseInt(operator.substring(0, 3));
							mnc = Integer.parseInt(operator.substring(3));
						}
						CellLocation cellLocation = tm.getCellLocation();
						if (cellLocation != null) {
							if (cellLocation instanceof GsmCellLocation) {
								GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
								cellId = gsmCellLocation.getCid();
								lac = gsmCellLocation.getLac();
							} else if (cellLocation instanceof CdmaCellLocation) {
								CdmaCellLocation cdma = (CdmaCellLocation) cellLocation;
								cellId = cdma.getBaseStationId();
								lac = cdma.getNetworkId();
								mnc = cdma.getSystemId();
							}
						}
					}
				} catch (Exception e) {
					LogUtil.getLogger().e("SystemInfo", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return lac;
	}

	/**
	  * 获取手机mobileCountryCode
	  * @param context
	  * @return
	  */
	private static int mcc;

	public static int getMcc(Context context) {
		if (mcc == 0) {
			mcc = -1;
			if (context != null) {
				try {

					int ACCESS_FINE_LOCATION_PERMISSION = context
							.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
					int ACCESS_COARSE_LOCATION_PERMISSION = context
							.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
					if (ACCESS_FINE_LOCATION_PERMISSION == PackageManager.PERMISSION_GRANTED
							|| ACCESS_COARSE_LOCATION_PERMISSION == PackageManager.PERMISSION_GRANTED) {
						TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
						// 返回值MCC + MNC  
						String operator = tm.getNetworkOperator();
						if (operator != null && operator.length() >= 3) {
							mcc = Integer.parseInt(operator.substring(0, 3));
							mnc = Integer.parseInt(operator.substring(3));
						}
						CellLocation cellLocation = tm.getCellLocation();
						if (cellLocation != null) {
							if (cellLocation instanceof GsmCellLocation) {
								GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
								cellId = gsmCellLocation.getCid();
								lac = gsmCellLocation.getLac();
							} else if (cellLocation instanceof CdmaCellLocation) {
								CdmaCellLocation cdma = (CdmaCellLocation) cellLocation;
								cellId = cdma.getBaseStationId();
								lac = cdma.getNetworkId();
								mnc = cdma.getSystemId();
							}
						}
					}
				} catch (Exception e) {
					LogUtil.getLogger().e("SystemInfo", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return mcc;
	}

	/**
	  * 获取手机mobileNetworkCode
	  * @param context
	  * @return
	  */
	private static int mnc;

	public static int getMnc(Context context) {
		if (mnc == 0) {
			mnc = -1;
			if (context != null) {
				try {

					int ACCESS_FINE_LOCATION_PERMISSION = context
							.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
					int ACCESS_COARSE_LOCATION_PERMISSION = context
							.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
					if (ACCESS_FINE_LOCATION_PERMISSION == PackageManager.PERMISSION_GRANTED
							|| ACCESS_COARSE_LOCATION_PERMISSION == PackageManager.PERMISSION_GRANTED) {
						TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
						// 返回值MCC + MNC  
						String operator = tm.getNetworkOperator();
						if (operator != null && operator.length() >= 3) {
							mcc = Integer.parseInt(operator.substring(0, 3));
							mnc = Integer.parseInt(operator.substring(3));
						}
						CellLocation cellLocation = tm.getCellLocation();
						if (cellLocation != null) {
							if (cellLocation instanceof GsmCellLocation) {
								GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
								cellId = gsmCellLocation.getCid();
								lac = gsmCellLocation.getLac();
							} else if (cellLocation instanceof CdmaCellLocation) {
								CdmaCellLocation cdma = (CdmaCellLocation) cellLocation;
								cellId = cdma.getBaseStationId();
								lac = cdma.getNetworkId();
								mnc = cdma.getSystemId();
							}
						}
					}
				} catch (Exception e) {
					LogUtil.getLogger().e("SystemInfo", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return mnc;
	}

	/**
	 * 获取纬度
	 * @param context
	 * @return
	 */
	public static double getLatitude(Context context) {
		Location location = getLocation(context);

		if (location != null) {
			return location.getLatitude();
		}
		return 0;
	}

	/**
	 * 获取经度
	 * @param context
	 * @return
	 */
	public static double getLongitude(Context context) {
		Location location = getLocation(context);
		if (location != null) {
			return location.getLongitude();
		}
		return 0;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static Location getLocation(Context context) {
		LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}

	/**
	 * 获取当前应用包名
	 */
	private static String packageName;

	public static String getPackageName(Context context) {
		if (packageName == null && context != null) {
			packageName = context.getPackageName();
		}
		return packageName;
	}

	/**
	 * 获取当前应用版本名称
	 */
	private static String versionName;

	public static String getVersionName(Context context) {
		if (versionName == null) {
			PackageInfo info = Tools.getPackageInfo(context, getPackageName(context),
					PackageManager.GET_UNINSTALLED_PACKAGES);
			if (info != null) {
				versionName = info.versionName;
			}
		}
		return versionName;
	}

	/**
	 * 获取当前应用版本号
	 */
	private static int versionCode;

	public static int getVersionCode(Context context) {
		if (versionCode == 0) {
			PackageInfo info = Tools.getPackageInfo(context, getPackageName(context),
					PackageManager.GET_UNINSTALLED_PACKAGES);
			if (info != null) {
				versionCode = info.versionCode;
			}
		}
		return versionCode;
	}

	/**
	 * 获取mannifest中metaDate的值
	 * @param context
	 * @param packageName
	 * @param metaDateKey
	 * @return
	 */
	public static int getMetaDataIntValue(Context context, String metaDateKey) {
		int metaDateValue = 0;
		if (!TextUtils.isEmpty(metaDateKey)) {
			ApplicationInfo appInfo;
			try {
				appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA);
				if (appInfo != null) {
					Bundle metaData = appInfo.metaData;
					if (metaData != null) {
						if (metaData.containsKey(metaDateKey)) {
							int metaDateInt = metaData.getInt(metaDateKey, -1);
							if (metaDateInt != -1) {
								metaDateValue = metaDateInt;
							}
						}
					}
				}
			} catch (NameNotFoundException e) {
				LogUtil.getLogger().e("SystemInfo", e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.getLogger().e("SystemInfo", e.getMessage());
				e.printStackTrace();
			}
		}
		return metaDateValue;
	}

	/**
	 * 获取mannifest中metaDate的值
	 * @param context
	 * @param packageName
	 * @param metaDateKey
	 * @return
	 */
	public static String getMetaDataStringValue(Context context, String metaDateKey) {
		String metaDateValueStr = null;
		if (!TextUtils.isEmpty(metaDateKey)) {
			ApplicationInfo appInfo;
			try {
				appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA);
				if (appInfo != null) {
					Bundle metaData = appInfo.metaData;
					if (metaData != null) {
						if (metaData.containsKey(metaDateKey)) {
							metaDateValueStr = metaData.getString(metaDateKey);
						}
					}
				}
			} catch (NameNotFoundException e) {
				LogUtil.getLogger().e("SystemInfo", e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.getLogger().e("SystemInfo", e.getMessage());
				e.printStackTrace();
			}
		}
		return metaDateValueStr;
	}

	/**
	 * 获取渠道号
	 * @param context
	 * @return
	 */
	//    public static String CHANNEL_ID = "channelId" ;
	//    private static final String DEFAULT_EXCEPTION_CHANNEL_ID = "9999";//数据读取异常时的默认渠道号
	//    private static String channelId;
	//    public static String getChannelId(Context context) {
	//    	if(channelId==null)
	//    	{
	//    		String packageName = getPackageName(context);
	//    		channelId=Tools.getSaveData(context.getApplicationContext(), packageName, CHANNEL_ID, null);
	//    		if(channelId==null)
	//    		{
	//    			channelId = getMetaDateIntValue(context,CHANNEL_ID);//优先读取自己的AndroidManifest
	//				if(channelId!=null&&channelId.length()>0)
	//				{
	//					Tools.saveData(context.getApplicationContext(), packageName, CHANNEL_ID, channelId);
	//				}
	//				else
	//				{
	//					channelId=DEFAULT_EXCEPTION_CHANNEL_ID;
	//				}
	//    		}
	//    	}
	//    	return channelId;
	//    }

	/**
	 * 获取标准GET参数
	 * @param context
	 * @return
	 */
	//    public static String requestUploadGetParam(Context context) {
	//    	String getParam = "";
	//    	getParam+="channelId="+getChannelId(context);
	//    	getParam+="&imei="+getIMEI(context);
	//    	getParam+="&imsi="+getIMSI(context);
	////    	getParam+="&model="+getModel();
	//    	getParam+="&pkgName="+getPackageName(context);
	//    	getParam+="&verName="+getVersionName(context);
	//    	getParam+="&verCode="+getVersionCode(context);
	//    	getParam+="&check="+SystemInfo.MD5(context);
	//    	getParam+="&cellId="+String.valueOf(SystemInfo.getCellId(context));
	//    	getParam+="&lac="+String.valueOf(SystemInfo.getLac(context));
	//    	getParam+="&network="+SystemInfo.getNetworkInfo(context);
	//    	getParam+="&release="+SystemInfo.getAndroidRelease();
	//    	getParam+="&sdkInt="+String.valueOf(SystemInfo.getAndroidSDKINT());
	//    	getParam+="&cardType="+SystemInfo.getCardType(context);
	//    	getParam+="&width="+String.valueOf(SystemInfo.getScreenWidth(context));
	//    	getParam+="&height="+String.valueOf(SystemInfo.getScreenHeight(context));
	//    	getParam+="&phoneSize="+Tools.getPhoneAvailableSize(context)+"/"+Tools.getPhoneTotalSize(context);
	//    	getParam+="&sdcardSize="+String.valueOf(Tools.getSDCardAvailableSize()+"/"+Tools.getSDCardTotalSize());
	//    	getParam+="&internalType="+SystemInfo.getAppInternal(context, context.getPackageName());
	//    	getParam+="&internalPath="+SystemInfo.getAppSourcePath(context, context.getPackageName());
	//    	return getParam;
	//    }

	/** 无参数 */
	public static final int TYPE_PARAM_NONE = 0;
	/** 基本参数 */
	public static final int TYPE_PARAM_BASE = 1;
	/** 公用参数 */
	public static final int TYPE_PARAM_COMMON = 2;

	//    /**
	//     * 获取标准POST参数
	//     * @param context
	//     * @return
	//     */
	//    public static List<BasicNameValuePair> getBasePosttParam(Context context) {
	//    	List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
	//    	list.add(new BasicNameValuePair("channelId",getChannelId(context)));
	//    	list.add(new BasicNameValuePair("imei",getIMEI(context)));
	//    	list.add(new BasicNameValuePair("imsi",getIMSI(context)));
	////    	list.add(new BasicNameValuePair("model",getModel()));
	//    	list.add(new BasicNameValuePair("pkgName",getPackageName(context)));
	//    	list.add(new BasicNameValuePair("verName",getVersionName(context)));
	//    	list.add(new BasicNameValuePair("verCode",String.valueOf(getVersionCode(context))));
	//    	list.add(new BasicNameValuePair("check",SystemInfo.MD5(context)));
	//    	list.add(new BasicNameValuePair("cellId",String.valueOf(SystemInfo.getCellId(context))));
	//    	list.add(new BasicNameValuePair("lac",String.valueOf(SystemInfo.getLac(context))));
	//    	list.add(new BasicNameValuePair("network",SystemInfo.getNetworkInfo(context)));
	//    	list.add(new BasicNameValuePair("netType",String.valueOf(SystemInfo.getNetworkType(context))));
	//    	list.add(new BasicNameValuePair("release",SystemInfo.getAndroidRelease()));
	//    	list.add(new BasicNameValuePair("sdkInt",String.valueOf(SystemInfo.getAndroidSDKINT())));
	//    	list.add(new BasicNameValuePair("cardType",SystemInfo.getCardType(context)));
	//    	list.add(new BasicNameValuePair("width",String.valueOf(SystemInfo.getScreenWidth(context))));
	//    	list.add(new BasicNameValuePair("height",String.valueOf(SystemInfo.getScreenHeight(context))));
	//    	list.add(new BasicNameValuePair("phoneSize",Tools.getPhoneAvailableSize(context)+"/"+Tools.getPhoneTotalSize(context)));
	//    	list.add(new BasicNameValuePair("sdcardSize",String.valueOf(Tools.getSDCardAvailableSize()+"/"+Tools.getSDCardTotalSize())));
	//    	list.add(new BasicNameValuePair("internalType",SystemInfo.getAppInternal(context, context.getPackageName())));
	//    	list.add(new BasicNameValuePair("internalPath",SystemInfo.getAppSourcePath(context, context.getPackageName())));
	//    	return list;
	//    }
	//    
	//    /**
	//     * 获取公用上传信息
	//     * 
	//     */
	//    public static List<BasicNameValuePair> getCommonPostParam(Context context){
	//    	List<BasicNameValuePair> list = getBasePosttParam(context);
	//    	
	//    	if (list != null){
	//    		AppInfo info = getInstallAppInfo(context);
	//    		if (info != null){
	//    			list.add(new BasicNameValuePair("allAppName", info.getAppName()));
	//    			list.add(new BasicNameValuePair("allAppVersion", info.getVersionCode()));
	//    			list.add(new BasicNameValuePair("allAppPkgName", info.getPackageName()));
	//    		}
	//    	}    	
	//    	return list;
	//    }

	/**
	 * @desc 
	 * 		组装公用参数 
	 * @param context
	 * @param type
	 * 		类型
	 * @param list
	 * 		额外参数
	 */
	//    public static List<BasicNameValuePair> composePostParams(Context context, int type, List<BasicNameValuePair> list){
	//    	
	//    	List<BasicNameValuePair> nList = null;
	//    	if (context != null){
	//    		//..类型分别获取
	//    		if (type == TYPE_PARAM_BASE){
	//    			nList = getBasePosttParam(context);
	//    		}else if (type == TYPE_PARAM_COMMON){
	////    			nList = getCommonPostParam(context);
	//    		}
	//    		if (nList == null){
	//    			nList = new ArrayList<BasicNameValuePair>();
	//    		}
	//    		//..组装
	//    		if (nList != null && list != null && list.size() > 0){
	//    			for (int i = 0; i < list.size(); i++){
	//    				nList.add(list.get(i));
	//    			}
	//    		}
	//    	}
	//    	return nList;
	//    }

	/**
	 * 根据已知参数列表构建JSONObject
	 * @param list
	 * @return
	 */
	public static JSONObject createJSONObject(List<BasicNameValuePair> list) {
		JSONObject obj = null;
		if (list != null) {
			int len = list.size();
			if (len > 0) {
				BasicNameValuePair temp;
				for (int i = 0; i < len; i++) {
					temp = list.get(i);
					if (temp != null) {
						if (obj == null) {
							obj = new JSONObject();
						}
						try {
							obj.put(temp.getName(), temp.getValue());
						} catch (JSONException e) {
							LogUtil.getLogger().e("SystemInfo", e.getMessage());
							e.printStackTrace();
						} catch (Exception e) {
							LogUtil.getLogger().e("SystemInfo", e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
		}
		return obj;
	}

	/**
	 * 返回已知参数列表构建JSONObject的字符串结构
	 * @param list
	 * @return
	 */
	public static String createJSONObjectStr(List<BasicNameValuePair> list) {
		JSONObject obj = createJSONObject(list);
		if (obj != null) {
			return obj.toString();
		}
		return null;
	}

	public static List<BasicNameValuePair> createPostValuePair(List<BasicNameValuePair> value) {

		List<BasicNameValuePair> list = null;

		if (value != null) {
			list = new ArrayList<BasicNameValuePair>();
			String data = createJSONObjectStr(value);
			list.add(new BasicNameValuePair("data", (data != null) ? data : ""));
		}
		return list;
	}

	public static final String ENCRYPT_AND_DECRYPT_KEY = "a#p#p#c#@m#a#s";//加解密KEY
	public static final String ENCRYPT_AND_DECRYPT_CHARSET = "UTF-8";
	private static EncodeData encodeData;

	/**
	 * 加密字符串
	 * @param source
	 * @return
	 */
	public static String encryptData(String source) {
		String encryptStr = null;
		if (source != null && source.length() > 0) {
			if (encodeData == null) {
				encodeData = new EncodeData(ENCRYPT_AND_DECRYPT_KEY);
			}
			byte[] sourceByte = null;
			try {
				sourceByte = source.getBytes(ENCRYPT_AND_DECRYPT_CHARSET);
			} catch (UnsupportedEncodingException e) {
				LogUtil.getLogger().e("SystemInfo", e.getMessage());
				e.printStackTrace();
			}
			if (sourceByte != null && sourceByte.length > 0) {
				byte[] encryptByte = encodeData.EncodeAndBase64New(sourceByte);
				if (encryptByte != null && encryptByte.length > 0) {
					try {
						encryptStr = new String(encryptByte, ENCRYPT_AND_DECRYPT_CHARSET);
					} catch (UnsupportedEncodingException e) {
						LogUtil.getLogger().e("SystemInfo", e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		if (encryptStr == null) {
			encryptStr = source;
		}
		return encryptStr;
	}

	/**
	 * 解密字符串
	 * @param encryptStr
	 * @return
	 */
	public static String decryptData(String encryptStr) {
		String source = null;
		if (encryptStr != null && encryptStr.length() > 0) {
			if (encodeData == null) {
				encodeData = new EncodeData(ENCRYPT_AND_DECRYPT_KEY);
			}
			byte[] encryptByte = null;
			try {
				encryptByte = encryptStr.getBytes(ENCRYPT_AND_DECRYPT_CHARSET);
			} catch (UnsupportedEncodingException e) {
				LogUtil.getLogger().e("SystemInfo", e.getMessage());
				e.printStackTrace();

			}
			if (encryptByte != null && encryptByte.length > 0) {
				byte[] sourceByte = encodeData.DecodeAndBase64New(encryptByte);
				if (sourceByte != null && sourceByte.length > 0) {
					try {
						source = new String(sourceByte, ENCRYPT_AND_DECRYPT_CHARSET);
					} catch (UnsupportedEncodingException e) {
						LogUtil.getLogger().e("SystemInfo", e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		if (source == null) {
			source = encryptStr;
		}
		return source;
	}

	/**
	 * 获取应用安装状态
	 */
	public static final String INSTALL_SYSTEM_PATH = "/system/app/";//系统区安装路径
	public static final String INSTALL_SDCARD_PATH = "/mnt/asec/";//SD卡安装路径
	public static final int INSTALL_TYPE_SDCARD = 2;
	public static final int INSTALL_TYPE_SYSTEM = 1;
	public static final int INSTALL_TYPE_DATA = 0;
	public static final int INSTALL_TYPE_NULL = -1;

	public static int getInstallType(Context context, String packageName) {
		int installType = -2;
		ApplicationInfo info = Tools.getApplicationInfo(context, packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
		if (info != null) {
			String installPath = info.sourceDir;
			if (installPath != null) {
				if ((installPath.startsWith(INSTALL_SYSTEM_PATH)) || ((info.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
						|| ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) > 0)) {
					installType = INSTALL_TYPE_SYSTEM;
				} else if ((installPath.startsWith(INSTALL_SDCARD_PATH))
						|| ((info.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) > 0)) {
					installType = INSTALL_TYPE_SDCARD;
				} else {
					installType = INSTALL_TYPE_DATA;
				}
			}
		} else {
			installType = INSTALL_TYPE_NULL;
		}
		return installType;
	}

	/**
	 * MD5
	 * @param context
	 * @return
	 */
	public static String MD5(Context context) {
		return MD5(getIMEI(context) + getIMSI(context));
	}

	public static String MD5(String src) {
		char hexChars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] bytes = src.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			bytes = md.digest();
			int j = bytes.length;
			char[] chars = new char[j * 2];
			int k = 0;
			for (int i = 0; i < bytes.length; i++) {
				byte b = bytes[i];
				chars[k++] = hexChars[b >>> 4 & 0xf];
				chars[k++] = hexChars[b & 0xf];
			}
			return new String(chars);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取应用的安装路径（/data/data/<packageName>下或者/system/app/下）
	 * @param packageName
	 * @return
	 */
	public static String getAppSourcePath(Context context, String packageName) {
		if (packageName == null || packageName.trim().equals("")) {
			return null;
		}
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			if (info == null) {
				return null;
			}
			return info.sourceDir;
		} catch (NameNotFoundException e) {
			LogUtil.getLogger().e("SystemInfo", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断软件是否为内置安装
	 * @param context
	 * @param packageName
	 * @return 
	 */
	public static String getAppInternal(Context context, String packageName) {
		String installPath = getAppSourcePath(context, packageName);
		if (installPath != null) {
			if (installPath.startsWith("/system/app")) {
				return "1";
			} else {
				return "0";
			}
		} else {
			return "-1";
		}
	}

	/**
	* 判断软件是否为内置安装
	* @param context
	* @param packageName
	* @return 
	*/
	public static boolean isAppInternal(Context context, String packageName) {
		String installPath = getAppSourcePath(context, packageName);
		if (installPath != null) {
			if (installPath.startsWith("/system/app")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否为平板
	 * 
	 * @return
	 */
	public static final int PAD = 2;
	public static final int PHONE = 1;

	public static int isPad(Context context) {

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);

		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// 屏幕尺寸
		double screenInches = Math.sqrt(x + y);
		// 大于6尺寸则为Pad
		if (screenInches >= 6.0) {
			return PAD;
		}
		return PHONE;
	}

	/**
	 * 判断是否为平板
	 * 
	 * @return
	 */
	public static int isPads(Context context) {
		if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
			return PAD;
		}
		return PHONE;
	}

	public static String getInstallAppPackageName(Context context, boolean isFlagSystem) {
		String packageName = "";
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(
				PackageManager.GET_UNINSTALLED_PACKAGES);
		//循环获取用户手机上所安装的软件信息
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			//不是系统区应用
			if (!isFlagSystem) {
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					packageName += packageInfo.packageName;
					packageName += "_" + packageInfo.versionCode + ",";
				}
			}//系统区应用
			else {
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {

					if (!(packageInfo.packageName.startsWith("com.android")
							|| packageInfo.packageName.startsWith("com.mediatek")
							|| packageInfo.packageName.startsWith("com.google")
							|| packageInfo.packageName.startsWith("com.spreadst") || packageInfo.packageName
								.startsWith("com.spreadtrum"))) {
						packageName += packageInfo.packageName;
						packageName += "_" + packageInfo.versionCode + ",";
					}
				}
			}
		}
		if (packageName.endsWith(",")) {
			packageName = packageName.substring(0, packageName.length() - 1);
		}
		return packageName;
	}

	/**
	 * 获取安装信息
	 * 过滤以下信息
	 * com.android  (android系统)
		com.mediatek (MTK相关)
		com.google   (谷歌相关)
		com.spreadst  (展讯相关)
		com.spreadtrum (展讯相关)
	 * @param context
	 * @param isFlagSystem
	 * @return
	 */
	public static JSONArray getInstallAppInfo(Context context) {

		JSONArray jArray = new JSONArray();
		try {
			List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
			//循环获取用户手机上所安装的软件信息
			for (int i = 0; i < packages.size(); i++) {
				PackageInfo packageInfo = packages.get(i);
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					if (!(packageInfo.packageName.startsWith("com.android")
							|| packageInfo.packageName.startsWith("com.mediatek")
							|| packageInfo.packageName.startsWith("com.google")
							|| packageInfo.packageName.startsWith("com.spreadst") || packageInfo.packageName
								.startsWith("com.spreadtrum"))) {
						JSONObject jObject = new JSONObject();
						jObject.put("name", packageInfo.applicationInfo.loadLabel(context.getPackageManager())
								.toString());
						jObject.put("packageName", packageInfo.packageName);
						jObject.put("versionCode", packageInfo.versionCode);
						jObject.put("versionName", packageInfo.versionName);
						jArray.put(jObject);
					}
				}
			}
		} catch (JSONException e) {
			LogUtil.getLogger().e("SystemInfo", e.getMessage());
			e.printStackTrace();
		}
		return jArray;
	}

	/**
	 * 锁定屏幕
	 * 
	 * @param activity
	 */
	public static void setRequestedOrientations(Activity activity) {
		if (activity == null) {
			return;
		}
		try {
			if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			}
		} catch (Exception ex) {
			LogUtil.getLogger().e("SystemInfo", ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * 锁定屏幕
	 * 
	 * @param activity
	 */
	public static void releaseRequestedOrientations(Activity activity) {
		if (activity == null) {
			return;
		}
		try {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
					| ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} catch (Exception e) {
			LogUtil.getLogger().e("SystemInfo", e.getMessage());
			e.printStackTrace();
		}
	}

}
