package com.x.publics.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;

/**
 * Api工具类
 * 
 
 * 
 */
public class Tools {

	public static final String sdcardPath = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/**
	 * 按指定的时间和格式输出时间信息
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static String getCurrentDate(String format, Date date) {
		if (format == null) {
			format = "yyyy-MM-dd";
		}
		if (date == null) {
			date = new Date();
		}
		String dateStr = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			dateStr = formatter.format(date);
		} catch (NullPointerException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		}
		return dateStr;
	}

	/**
	 * 按指定的时间和格式输出时间信息
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static String getCurrentDate() {
		return getCurrentDate("yyyy-MM-dd HH:mm:ss", null);
	}

	/**
	 * 按指定的时间和格式构建对应的Date实例
	 * 
	 * @param format
	 * @param dateStr
	 * @return
	 */
	public static Date createFormatDate(String format, String dateStr) {
		Date date = null;
		if (format == null) {
			format = "yyyy-MM-dd";
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			date = formatter.parse(dateStr);
		} catch (java.text.ParseException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		} catch (NullPointerException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取安装包信息
	 * 
	 * @param context
	 * @return
	 */
	public static ApplicationInfo getApplicationInfo(Context context,
			String packageName) {
		ApplicationInfo info = null;
		if (packageName != null && packageName.length() > 0) {
			try {
				info = context.getPackageManager().getApplicationInfo(
						packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			} catch (NameNotFoundException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			}
		}
		return info;
	}

	/**
	 * 获取安装包信息
	 * 
	 * @param context
	 * @return
	 */
	public static ApplicationInfo getApplicationInfo(Context context,
			String packageName, int flags) {
		ApplicationInfo info = null;
		if (packageName != null && packageName.length() > 0) {
			try {
				info = context.getPackageManager().getApplicationInfo(
						packageName, flags);
			} catch (NameNotFoundException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			}
		}
		return info;
	}

	/**
	 * 根据包名获取应用的安装信息
	 * 
	 * @param packageName
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context, String packageName) {
		PackageInfo info = null;
		if (packageName != null && packageName.length() > 0) {
			try {
				info = context.getPackageManager().getPackageInfo(packageName,
						PackageManager.GET_UNINSTALLED_PACKAGES);
			} catch (NameNotFoundException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			}
		}
		return info;
	}

	/**
	 * 根据包名获取应用的安装信息
	 * 
	 * @param packageName
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context,
			String packageName, int flag) {
		PackageInfo info = null;
		if (packageName != null && packageName.length() > 0) {
			try {
				info = context.getPackageManager().getPackageInfo(packageName,
						flag);// PackageManager.GET_UNINSTALLED_PACKAGES
			} catch (NameNotFoundException e) {

			}
		}
		return info;
	}

	/**
	 * 获取安装包的包名信息等
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static PackageInfo getPackageInfoFromPath(Context context,
			String path) {
		PackageManager pm = context.getPackageManager();
		return pm.getPackageArchiveInfo(path,
				PackageManager.GET_UNINSTALLED_PACKAGES);// PackageManager.GET_ACTIVITIES
	}

	/**
	 * 获取应用签名
	 * 
	 * @param info
	 * @return
	 */
	public static String getPackageSignature(PackageInfo info) {
		String platform_x509_pem = null;
		if (info != null) {
			Signature[] signatures = info.signatures;
			if (signatures != null && signatures.length > 0) {
				byte[] signByteArray = signatures[0].toByteArray();
				platform_x509_pem = Base64.encodeToString(signByteArray,
						Base64.NO_WRAP);
			}
		}
		return platform_x509_pem;
	}

	/**
	 * 判断应用是否为系统签名的应用
	 * 
	 * @param info
	 * @return
	 */
	public static boolean getIsSystemSignature(Context context, PackageInfo info) {
		boolean isSystemSignature = false;
		if (info != null) {
			String sharedUserId = info.sharedUserId;
			LogUtil.getLogger().e(
					"Tools",
					"getIsSystemSignature() packageName=" + info.packageName
							+ " sharedUserId=" + info.sharedUserId);
			if (sharedUserId != null
					&& sharedUserId.equals("android.uid.system")) {
				isSystemSignature = true;
			} else {
				String channelInfoSignature = getPackageSignature(info);
				String systemSignature = null;
				PackageInfo systemInfo = Tools.getPackageInfo(context,
						"android", PackageManager.GET_SIGNATURES);
				if (systemInfo != null) {
					systemSignature = getPackageSignature(systemInfo);
				} else {
					systemInfo = getPackageInfo(context,
							"com.android.settings",
							PackageManager.GET_SIGNATURES);
					if (systemInfo != null) {
						systemSignature = getPackageSignature(systemInfo);
					}
				}
				if (channelInfoSignature != null && systemSignature != null
						&& channelInfoSignature.equals(systemSignature)) {
					isSystemSignature = true;
				}

			}
		}
		return isSystemSignature;
	}

	/**
	 * 判断应用是否为系统签名或内置在系统区的应用
	 * 
	 * @param info
	 * @return
	 */
	public static boolean checkSystemSignatureOrSystemImg(Context context,
			PackageInfo info) {
		boolean isSystemSignature = false;
		if (info != null) {
			isSystemSignature = getIsSystemSignature(context, info);// 是否系统签名
			if (!isSystemSignature) {
				int installType = SystemInfo.getInstallType(context,
						info.packageName);
				isSystemSignature = (installType == SystemInfo.INSTALL_TYPE_SYSTEM);// 是否系统区
			}
		}
		return isSystemSignature;
	}

	/**
	 * 查看SD卡剩余空间大小
	 * 
	 * @return
	 */
	public static long getSDCardAvailableSize() {
		StatFs statFs = new StatFs(sdcardPath);
		long sdcardAvailableSize = (statFs.getBlockSize() * (long) statFs
				.getAvailableBlocks());
		return sdcardAvailableSize;
	}

	/**
	 * 查看SD卡总空间大小
	 * 
	 * @return
	 */
	public static long getSDCardTotalSize() {
		StatFs statFs = new StatFs(sdcardPath);
		long sdcardTotalSize = (statFs.getBlockSize() * (long) statFs
				.getBlockCount());
		return sdcardTotalSize;
	}

	/**
	 * 查看手机剩余空间大小
	 * 
	 * @return
	 */
	public static long getPhoneAvailableSize(Context context) {
		long phoneAvailableSize = 0;
		if (context != null) {
			StatFs statFs = new StatFs(context.getFilesDir().getAbsolutePath());
			phoneAvailableSize = (statFs.getBlockSize() * (long) statFs
					.getAvailableBlocks());
		}
		return phoneAvailableSize;
	}

	/**
	 * 查看手机总空间大小
	 * 
	 * @return
	 */
	public static long getPhoneTotalSize(Context context) {
		long phoneTotalSize = 0;
		if (context != null) {
			StatFs statFs = new StatFs(context.getFilesDir().getAbsolutePath());
			phoneTotalSize = (statFs.getBlockSize() * (long) statFs
					.getBlockCount());
		}
		return phoneTotalSize;
	}

	/**
	 * 查看外部存储剩余空间大小
	 * 
	 * @return
	 */
	public static long getDirAvailableSize() {
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		long availableSpare = (statFs.getBlockSize() * (long) statFs
				.getAvailableBlocks());
		return availableSpare;
	}

	/**
	 * 智能单位换算
	 * 
	 * @param data
	 *            //数据源
	 * @param k
	 *            //基数
	 * @param dec
	 *            //保留小数位数
	 * @param unit
	 *            //单位
	 * @return
	 */
	public static String getUnit(long data, int k, int dec, String[] unit) {
		if (unit != null && unit.length > 0 && k > 0) {
			double d = data;
			for (int i = 0; i < unit.length; i++) {
				if (d < k) {
					if ((d - (long) (d)) < Math.pow(0.1, dec)) {
						dec = 0;
					}
					return String.format("%." + dec + "f", d) + unit[i];
				}
				d /= k;
			}
			if ((d - (long) (d)) < Math.pow(0.1, dec)) {
				dec = 0;
			}
			return String.format("%." + dec + "f", d) + unit[unit.length - 1];
		}
		return null;
	}

	/**
	 * 根据Intent过滤Activity列表
	 * 
	 * @param context
	 * @param intent
	 * @return
	 */
	public static List<ResolveInfo> queryIntentActivities(Context context,
			Intent intent) {
		if (context != null && intent != null) {
			return context.getPackageManager().queryIntentActivities(intent,
					PackageManager.GET_ACTIVITIES);
		}
		return null;
	}

	public static final String browsers = "com.google.browser,com.android.browser,com.uc.browser,com.opera.mini.android,com.tencent.mtt,com.baidu.browser.apps,com.qihoo.androidbrowser";

	/**
	 * 使用浏览器打开指定网址
	 * 
	 * @param context
	 * @param url
	 */
	public static void openURL(Context context, String url) {
		if (context != null && url != null && url.length() > 0) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			List<ResolveInfo> list = queryIntentActivities(context, intent);
			if (list != null) {
				int len = list.size();
				if (len > 0) {
					String packageName;
					ResolveInfo info;
					boolean set = false;
					for (int i = 0; i < len; i++) {
						info = list.get(i);
						if (info != null) {
							packageName = info.activityInfo.packageName;
							if (browsers.indexOf(',' + packageName) >= 0) {
								set = true;
								intent.setClassName(packageName,
										info.activityInfo.name);
								break;
							}
						}
					}
					if (!set) {
						info = list.get(0);
						intent.setClassName(info.activityInfo.packageName,
								info.activityInfo.name);
					}
				}
			}
			context.startActivity(intent);
		}
	}

	/***
	 * 让用户自己选择浏览器打开URL
	* @Title: openURL2 
	* @Description: TODO 
	* @param @param context
	* @param @param url    
	* @return void
	 */
	public static void openURL2(Context context, String url) {
		if (context != null && url != null && url.length() > 0) {
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				List<ResolveInfo> list = queryIntentActivities(context, intent);
				if(list.size()>0&&list != null){
					context.startActivity(intent);
				}else{
					//乱输入的网址，或者格式不规范的，用默认浏览器打开
			        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
					context.startActivity(intent);
				}
			} catch (Exception e) {
				Log.v("Exception", e.getMessage());
			}
			finally{
				
			}
		}
	}


	/**
	 * 求字符串的子串高效方法
	 * 
	 * @param source
	 * @param start
	 * @param startInc
	 * @param end
	 * @param endInc
	 * @return
	 */
	public static String subString(String source, String start,
			Integer startInc, String end, Integer endInc) {
		String result = null;
		if (source == null || start == null || end == null) {
			return result;
		}
		if (startInc == null) {
			startInc = start.length();
		}
		if (endInc == null) {
			endInc = end.length();
		}
		int index1 = source.indexOf(start);
		int index2 = 0;
		if (index1 >= 0) {
			index1 += startInc;
			index2 = source.indexOf(end, index1);
			index2 += endInc;
			int length = source.length();
			if (index1 >= 0 && index2 <= length && index2 > index1) {
				result = source.substring(index1, index2);
			} else if (index1 == index2) {
				result = "";
			}
		}
		return result;
	}

	/**
	 * 求URL的主机地址
	 * 
	 * @param url
	 * @return
	 */
	public static String getXHost(String url) {
		String xHost = null;
		if (url.toLowerCase().startsWith("http://")) {
			url = "http://" + url.substring(7);
			xHost = subString(url, "http://", null, "/", 0);
			if (xHost == null) {
				xHost = subString(url, "http://", 0, "?", 0);
				if (xHost == null) {
					xHost = url.substring(7);
				}
			}
		} else if (url.toLowerCase().startsWith("www.")) {
			url = "www." + url.substring(4);
			xHost = subString(url, "www.", 0, "/", 0);
			if (xHost == null) {
				xHost = subString(url, "www.", 0, "?", 0);
				if (xHost == null) {
					xHost = url.substring(4);
				}
			}
		}
		return xHost;
	}

	/**
	 * 获取字符串的hash值
	 * 
	 * @param str
	 * @return
	 */
	public static int getStringHashValue(String str) {
		int hashCode = 0;
		if (str != null && str.length() > 0) {
			hashCode = str.hashCode();
		}
		return hashCode;
	}

	/**
	 * 判断目录是否存在且可读写
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean isFileExistsAndCanReadWrite(File dir) {
		return dir != null && dir.exists() && dir.canRead() && dir.canWrite();
	}

	/**
	 * 判断剩余空间在minSize之上的SD卡是否存在
	 * 
	 * @param minSize
	 * @return
	 */
	private static final String sdcardDirArr[] = { "/sdcard", "/sdcard0",
			"/sdcard1", "/sdcard2", };

	public static String getSdcardPath(long minSize) {
		String path = null;
		File dir = Environment.getExternalStorageDirectory();
		if (isFileExistsAndCanReadWrite(dir)
				&& (minSize <= 0 || getDirAvailableSize() > minSize)) {
			path = dir.getAbsolutePath();
		} else {
			if (sdcardDirArr != null && sdcardDirArr.length > 0) {
				for (int i = 0; i < sdcardDirArr.length; i++) {
					dir = new File(sdcardDirArr[i]);
					if (isFileExistsAndCanReadWrite(dir)
							&& (minSize <= 0 || getDirAvailableSize() > minSize)) {
						path = dir.getAbsolutePath();
						break;
					}
				}
			}
		}
		return path;
	}

	/**
	 * 公用判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isStringNull(String str) {
		if (str != null && !"".equals(str) && !"null".equals(str)
				&& !"NULL".equals(str) && str.length() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 获取SharedPreferences数据库中的整型值
	 * 
	 * @param context
	 * @param dbname
	 * @param valuename
	 * @param defaultValue
	 * @return
	 */
	public static int getSaveData(Context context, String dbname,
			String valuename, int defaultValue) {
		SharedPreferences pre = context.getSharedPreferences(dbname,
				Context.MODE_PRIVATE);
		return pre.getInt(valuename, defaultValue);
	}

	/**
	 * 获取SharedPreferences数据库中的整型值
	 * 
	 * @param context
	 * @param dbname
	 * @param valuename
	 * @param defaultValue
	 * @return
	 */
	public static long getSaveData(Context context, String dbname,
			String valuename, long defaultValue) {
		SharedPreferences pre = context.getSharedPreferences(dbname,
				Context.MODE_PRIVATE);
		return pre.getLong(valuename, defaultValue);
	}

	/**
	 * 获取SharedPreferences数据库中的boolean型值
	 * 
	 * @param context
	 * @param dbname
	 * @param valuename
	 * @param defaultValue
	 * @return
	 */
	public static boolean getSaveData(Context context, String dbname,
			String valuename, boolean defaultValue) {
		SharedPreferences pre = context.getSharedPreferences(dbname,
				Context.MODE_PRIVATE);
		return pre.getBoolean(valuename, defaultValue);
	}

	/**
	 * 获取SharedPreferences数据库中的字符串值
	 * 
	 * @param context
	 * @param dbname
	 * @param valuename
	 * @param defaultValue
	 * @return
	 */
	public static String getSaveData(Context context, String dbname,
			String valuename, String defaultValue) {
		SharedPreferences pre = context.getSharedPreferences(dbname,
				Context.MODE_PRIVATE);
		return pre.getString(valuename, defaultValue);
	}

	/**
	 * 保存整型值到SharedPreferences数据库中
	 * 
	 * @param context
	 * @param dbname
	 * @param valuename
	 * @param value
	 */
	public static void saveData(Context context, String dbname,
			String valuename, int value) {
		SharedPreferences pre = context.getSharedPreferences(dbname,
				Context.MODE_PRIVATE);
		Editor edit = pre.edit();
		edit.putInt(valuename, value);
		edit.commit();
	}

	/**
	 * 保存整型值到SharedPreferences数据库中
	 * 
	 * @param context
	 * @param dbname
	 * @param valuename
	 * @param value
	 */
	public static void saveData(Context context, String dbname,
			String valuename, long value) {
		SharedPreferences pre = context.getSharedPreferences(dbname,
				Context.MODE_PRIVATE);
		Editor edit = pre.edit();
		edit.putLong(valuename, value);
		edit.commit();
	}

	/**
	 * 保存boolean型值到SharedPreferences数据库中
	 * 
	 * @param context
	 * @param dbname
	 * @param valuename
	 * @param value
	 */
	public static void saveData(Context context, String dbname,
			String valuename, boolean value) {
		SharedPreferences pre = context.getSharedPreferences(dbname,
				Context.MODE_PRIVATE);
		Editor edit = pre.edit();
		edit.putBoolean(valuename, value);
		edit.commit();
	}

	/**
	 * 保存字符串值到SharedPreferences数据库中
	 * 
	 * @param context
	 * @param dbname
	 * @param valuename
	 * @param value
	 */
	public static void saveData(Context context, String dbname,
			String valuename, String value) {
		SharedPreferences pre = context.getSharedPreferences(dbname,
				Context.MODE_PRIVATE);
		Editor edit = pre.edit();
		edit.putString(valuename, value);
		edit.commit();
	}

	/**
	 * 根据分隔符获取字符串列表
	 * 
	 * @param source
	 * @param split
	 * @return
	 */
	public static ArrayList<String> splitStr(String source, String split) {
		if (source == null || source.length() == 0 || split == null
				|| split.length() == 0) {
			return null;
		}
		ArrayList<String> list = new ArrayList<String>();
		int index = source.indexOf(split);
		while (index >= 0) {
			if (index == 0) {
				list.add("");
			} else {
				list.add(source.substring(0, index));
			}
			source = source.substring(index + 1);
			index = source.indexOf(split);
		}
		list.add(source);
		return list;
	}

	/**
	 * 合并字符串列表为字符串
	 * 
	 * @param list
	 * @param split
	 * @return
	 */
	public static String mergeStr(ArrayList<String> list, String split) {
		if (list == null || list.size() == 0 || split == null
				|| split.length() == 0) {
			return null;
		}
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			str += list.get(i);
			if (i < list.size() - 1) {
				str += split;
			}
		}
		return str;
	}

	/**
	 * 链表转数组
	 * 
	 * @param list
	 * @return
	 */
	public static String[] arrayListToArr(ArrayList<String> list) {
		String[] arr = null;
		if (list != null) {
			arr = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				arr[i] = list.get(i);
			}
		}
		return arr;
	}

	/**
	 * 打开网络设置
	 * 
	 * @param context
	 */
	public static void openNetWorkSettings(Context context) {
		String action = android.provider.Settings.ACTION_SETTINGS;
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			action = android.provider.Settings.ACTION_WIRELESS_SETTINGS;
		}
		Intent intent = new Intent(action);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			openSettings(context);
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		}
	}

	public static void openSettings(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClassName("com.android.settings",
				"com.android.settings.Settings");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 正则表达式匹配
	 * 
	 * @param source
	 * @param regex
	 * @return
	 */
	public static Matcher getMatcher(String source, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		return matcher;
	}

	/**
	 * 返回正则表达式匹配的结果
	 * 
	 * @param source
	 * @param regex
	 * @return
	 */
	public static boolean matcher(String source, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		return matcher.matches();
	}

	/**
	 * 返回正则表达式匹配到的对应字符串
	 * 
	 * @param source
	 * @param regex
	 * @param index
	 * @return
	 */
	public static String getMatcherGroup(String source, String regex, int index) {
		String result = null;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
			if (index <= matcher.groupCount()) {
				try {
					result = matcher.group(index);
				} catch (IndexOutOfBoundsException e) {
					LogUtil.getLogger().e("Tools", e.getMessage());
					e.printStackTrace();
				} catch (IllegalStateException e) {
					LogUtil.getLogger().e("Tools", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 读取文本文件
	 * 
	 * @param path
	 * @return
	 */
	public static String readFile(String path) {
		if (path != null && path.length() > 0) {
			try {
				InputStream in = new FileInputStream(path);
				if (in != null && in.available() > 0) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in, "UTF-8"));
					String line = null;
					StringBuilder sb = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					return sb.toString();
				}
			} catch (FileNotFoundException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 读取文本文件
	 * 
	 * @param path
	 * @return
	 */
	public static String readAssetsFile(Context context, String name) {
		if (name != null && name.length() > 0) {
			try {
				InputStream in = context.getResources().getAssets().open(name);
				if (in != null && in.available() > 0) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in, "UTF-8"));
					String line = null;
					StringBuilder sb = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					return sb.toString();
				}
			} catch (FileNotFoundException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			} catch (ParseException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 字符串格式化处理
	 * 
	 * @param speak
	 * @return
	 */
	public static String FormatSpeakString(String speak) {
		if (speak != null && speak.length() > 0) {
			String[] replace = { ",", "，", ".", "。", "!", "！", "?", "？", ":",
					"：", "'", "‘", "’", "\"", "“", "”", ";", "；" };
			speak = speak.trim();
			String str;
			for (int i = 0; i < replace.length; i++) {
				str = replace[i];
				speak = speak.replace(str, "");
			}
		}
		return speak;
	}

	/**
	 * 检测当前应用是否具有某项权限
	 * 
	 * @param context
	 * @param permission
	 * @param packageName
	 * @return
	 */
	public static boolean checkCurrentAppPermission(Context context,
			String permission) {
		boolean reslut = false;
		try {
			int resultCode = context.checkCallingOrSelfPermission(permission);
			if (resultCode == PackageManager.PERMISSION_GRANTED) {
				reslut = true;
				LogUtil.getLogger().e("Tools",
						"CurrentApp has Permission:" + permission);
			} else {
				LogUtil.getLogger().e("Tools",
						"CurrentApp has not Permission:" + permission);
			}
		} catch (NullPointerException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		}
		return reslut;
	}

	/**
	 * 检测当前应用是否具有某项权限
	 * 
	 * @param context
	 * @param permission
	 * @param packageName
	 * @return
	 */
	public static boolean checkAppPermission(Context context,
			String packageName, String permission) {
		boolean reslut = false;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					packageName, PackageManager.GET_PERMISSIONS);
			if (info != null) {
				String[] requestedPermissions = info.requestedPermissions;
				if (requestedPermissions != null
						&& requestedPermissions.length > 0) {
					for (int j = 0; j < requestedPermissions.length; j++) {
						if (requestedPermissions[j] != null
								&& requestedPermissions.equals(permission)) {
							return true;
						}
					}
				}
			}
		} catch (NullPointerException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		}
		return reslut;
	}

	/**
	 * 数据拷贝基本方法，从InputStream拷贝到OutputStream，每次拷贝大小不超过10K
	 * 
	 * @param ins
	 * @param outs
	 * @return
	 */
	private static boolean inputStreamToOutputStream(InputStream ins,
			OutputStream outs) {
		if (ins != null && outs != null) {
			try {
				int total = ins.available();
				if (total > 0) {
					int k = 10 * 1024;// 每次拷贝10K
					byte[] b = null;
					if (total > k) {
						b = new byte[k];
					} else {
						b = new byte[total];
					}
					int temp;
					while (total > 0) {
						temp = ins.read(b);
						outs.write(b, 0, temp);
						outs.flush();
						total -= temp;
					}
					outs.close();
					outs = null;
					ins.close();
					ins = null;
					return true;
				}
				outs.close();
				outs = null;
				ins.close();
				ins = null;
			} catch (IOException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 拷贝Assets里面的文件到手机私有files目录下
	 * 
	 * @param context
	 * @param fromFileName
	 * @param toFileName
	 * @return
	 */
	public static boolean copyAssetFileToPhone(Context context,
			String fromFileName, String toFileName) {
		if (context != null && fromFileName != null && toFileName != null) {
			try {
				InputStream ins = context.getAssets().open(fromFileName);
				File f = context.getFileStreamPath(toFileName);
				File ftemp = context.getFileStreamPath(toFileName + ".temp");
				if (ins != null) {
					int total = ins.available();
					if (total > 0) {
						if (f.exists() && total == f.length()) {
							return true;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
				f.delete();
				ftemp.delete();
				FileOutputStream outs = context.openFileOutput(toFileName
						+ ".temp", Context.MODE_WORLD_READABLE
						| Context.MODE_WORLD_WRITEABLE);
				return inputStreamToOutputStream(ins, outs)
						&& ftemp.renameTo(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 获取所有安装包信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<ApplicationInfo> getAllInstalledApplicationInfo(
			Context context, int flags) {
		return context.getPackageManager().getInstalledApplications(flags);
	}

	/**
	 * 判断该包名的应用是否被安装
	 * 
	 * @param packageName
	 * @return
	 */
	public static boolean isAppInstalled(Context context, String packageName) {
		if (packageName == null || packageName.trim().equals("")) {
			return false;
		}
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			if (info == null) {
				return false;
			}
			return true;
		} catch (NameNotFoundException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param f
	 * @return
	 */
	public static boolean deleteFile(File f) {
		if (f != null) {
			try {
				if (f.exists()) {
					return f.delete();
				}
			} catch (SecurityException e) {// 文件路径访问安全性异常
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param fromFilePath
	 * @param toFilePath
	 * @return
	 */
	public static boolean copyFileToFile(String fromFilePath, String toFilePath) {
		if (fromFilePath == null || toFilePath == null
				|| fromFilePath.trim().equals("")
				|| toFilePath.trim().equals("")) {
			return false;
		}
		try {
			FileInputStream fin = new FileInputStream(fromFilePath);
			FileOutputStream fout = new FileOutputStream(toFilePath, false);
			int length = fin.available();
			if (length > 0) {
				int k = 1024 * 10;
				byte data[] = new byte[k];
				while (length > k) {
					fin.read(data);
					fout.write(data);
					fout.flush();
					length -= k;
				}
				if (length > 0) {
					fin.read(data, 0, length);
					fout.write(data, 0, length);
					fout.flush();
				}
				data = null;
				fout.close();
				fin.close();
				fout = null;
				fin = null;
				return true;
			}
		} catch (FileNotFoundException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 拷贝文件到手机内存，并授予其它应用的访问权限
	 * 
	 * @param fromFilePath
	 * @param toFilePath
	 * @return
	 */
	public static boolean copyFileToPhone(Context context, String fromFilePath,
			String fileName, int mode) {
		if (context == null || fromFilePath == null || fileName == null
				|| fromFilePath.trim().equals("") || fileName.trim().equals("")) {
			return false;
		}
		try {
			FileInputStream fin = new FileInputStream(fromFilePath);
			FileOutputStream fout = context.openFileOutput(fileName, mode);
			int length = fin.available();
			if (length > 0) {
				int k = 1024 * 10;
				byte data[] = new byte[k];
				while (length > k) {
					fin.read(data);
					fout.write(data);
					fout.flush();
					length -= k;
				}
				if (length > 0) {
					fin.read(data, 0, length);
					fout.write(data, 0, length);
					fout.flush();

				}
				data = null;
				fout.close();
				fin.close();
				fout = null;
				fin = null;
				return true;
			}
		} catch (FileNotFoundException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断字符串中是否包含汉字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasChineseCharacter(String str) {
		if (str != null && !str.trim().equals("")) {
			if (str.getBytes().length > str.length()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取转换为ISO-8859-1编码的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getISOString(String str) {
		if (str != null && !str.trim().equals("")) {
			try {
				return new String(str.getBytes(), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 打印字符串链表
	 * 
	 * @param list
	 * @return
	 */
	public static String showArrayList(ArrayList<String> list) {
		if (list == null) {
			return "null";
		}
		String result = "[";
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i);
			if (i != (list.size() - 1)) {
				result += ",";
			}
		}
		result += "]";
		return result;
	}

	/**
	 * 字符串转换为JSONObject
	 * 
	 * @param jsonObjStr
	 * @return
	 */
	public static JSONObject parserJSONObject(String jsonObjStr) {
		JSONObject obj = null;
		if (TextUtils.isEmpty(jsonObjStr)) {
			return null;
		}
		try {
			obj = new JSONObject(jsonObjStr);
		} catch (JSONException e) {
			LogUtil.getLogger().e("Tools",
					"JSON协议解析异常！jsonArrStr=" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LogUtil.getLogger().e("Tools",
					"JSON协议解析异常！jsonArrStr=" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 字符串转换为JSONArray
	 * 
	 * @param jsonArrStr
	 * @return
	 */
	public static JSONArray parserJSONArray(String jsonArrStr) {
		JSONArray obj = null;
		if (TextUtils.isEmpty(jsonArrStr)) {
			return null;
		}
		try {
			obj = new JSONArray(jsonArrStr);
		} catch (JSONException e) {
			LogUtil.getLogger().e("Tools",
					"JSON协议解析异常！jsonArrStr=" + jsonArrStr);
		} catch (Exception e) {
			LogUtil.getLogger().e("Tools", "Exception :" + e.getMessage());
		}
		return obj;
	}

	/**
	 * 取唯一标示url
	 * 
	 * @param url
	 * @return
	 */
	public static String getUniqueString(String url) {
		if (url != null) {
			int len = url.length();
			if (len > 0) {
				String urlChange = url.substring((len - 1) / 4 + 1,
						(len - 1) / 2 + 1)
						+ url.substring(3 * (len - 1) / 4 + 1, len)
						+ url.substring(0, (len - 1) / 4 + 1)
						+ url.substring((len - 1) / 2 + 1,
								3 * (len - 1) / 4 + 1);
				url = len + "-" + url.hashCode() + "-" + urlChange.hashCode();
			}
		}
		return url;
	}

	// 判断手机格式是否正确
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	// 判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 启动Activity
	 * 
	 * @param context
	 * @param cls
	 */
	public static void startActivity(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 跳转至网络设置界面
	 */
	public static void gotoSetting(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("no_network_connection").setMessage("go_to_settings");

		builder.setNegativeButton("yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				try {
					String sdkVersion = android.os.Build.VERSION.SDK;
					if (Integer.valueOf(sdkVersion) > 10) {
						intent = new Intent(
								android.provider.Settings.ACTION_WIRELESS_SETTINGS);
					} else {
						intent = new Intent();
						ComponentName comp = new ComponentName(
								"com.android.settings",
								"com.android.settings.WirelessSettings");
						intent.setComponent(comp);
						intent.setAction("android.intent.action.VIEW");
					}
					context.startActivity(intent);
				} catch (Exception e) {
					LogUtil.getLogger().e("Tools", e.getMessage());
					e.printStackTrace();
				}
			}
		}).setPositiveButton("no", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}

	/**
	 * 读取ExternalStorage内容
	 * 
	 * @param context
	 * @param folderName
	 * @param fileName
	 * @return
	 */
	public static String readFromEnStorage(Context context, String folderName,
			String fileName) {

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			String foldername = Environment.getExternalStorageDirectory()
					.getPath() + File.separator + folderName;
			File folder = new File(foldername);
			if (!folder.exists()) {
				folder.mkdir();
			}
			File targetFile = new File(foldername + File.separator + fileName);
			String readedStr = "";
			InputStream inputStream = null;
			BufferedReader bufferedReader = null;
			try {
				if (!targetFile.exists()) {
					targetFile.createNewFile();
					return null;
				} else {
					inputStream = new BufferedInputStream(new FileInputStream(
							targetFile));
					bufferedReader = new BufferedReader(new InputStreamReader(
							inputStream, "UTF-8"));
					String tmp;
					while ((tmp = bufferedReader.readLine()) != null) {
						readedStr += tmp;
					}
					return readedStr;
				}
			} catch (Exception e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
				return null;
			} finally {
				try {
					if (bufferedReader != null) {
						bufferedReader.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (Exception e) {
					LogUtil.getLogger().e("Tools", e.getMessage());
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	/**
	 * ExternalStorage内容
	 * 
	 * @param context
	 * @param folderName
	 * @param fileName
	 * @param info
	 * @return
	 */
	public static boolean saveFroEnStorage(Context context, String folderName,
			String fileName, String info) {

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			String foldername = Environment.getExternalStorageDirectory()
					.getPath() + File.separator + folderName;
			File folder = new File(foldername);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File targetFile = new File(foldername + File.separator + fileName);
			OutputStreamWriter outputStreamWriter = null;
			try {
				if (!targetFile.exists()) {
					targetFile.createNewFile();
				}
				outputStreamWriter = new OutputStreamWriter(
						new FileOutputStream(targetFile), "utf-8");
				outputStreamWriter.write(info);
				outputStreamWriter.close();
				return true;
			} catch (Exception e) {
				LogUtil.getLogger().e("Tools", e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					if (outputStreamWriter != null) {
						outputStreamWriter.close();
					}
				} catch (Exception e) {
					LogUtil.getLogger().e("Tools", e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获取文件的MD5值
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileMD5(File f) {
		try {
			InputStream fis = new FileInputStream(f);
			byte[] buffer = new byte[1024];
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * 调用系统普通安装接口
	 * 
	 * @param context
	 * @param path
	 */
	public static void installAppCommon(Context context, String path) {
		if (context != null && path != null && path.length() > 0) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(new File(path)),
					"application/vnd.android.package-archive");
			if (context != null) {
				context.startActivity(intent);
			}
		}
	}

	/**
	 * MD5
	 * 
	 * @param src
	 * @return
	 */
	public static String MD5(String src) {
		char hexChars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
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
			LogUtil.getLogger().e("Tools", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 字符串去空格
	 * 
	 * @param str
	 * @return
	 */
	public static String stripSpace(String str) {
		if (str != null || str.length() >= 0) {
			str = str.replace(" ", "");
			return str;
		}
		return "";
	}

	/**
	 * 
	 * @param context
	 * @param imageSourceId
	 * @return
	 */
	public static ImageGetter getImageGetter(final Context context,
			final int imageSourceId) {
		ImageGetter imageGetter = new Html.ImageGetter() {

			@Override
			public Drawable getDrawable(String source) {
				Drawable drawable = context.getResources().getDrawable(
						imageSourceId);
				drawable.setBounds(0, 0, dip2px(context, 16),
						dip2px(context, 16));
				return drawable;
			}
		};
		return imageGetter;
	}

	/**
	 * 
	 * @param context
	 * @param imageSourceId
	 * @return
	 */
	public static ImageSpan getImageSpan(Context context, int imageSourceId) {
		Drawable drawable = context.getResources().getDrawable(imageSourceId);
		if (SystemInfo.PHONE == SystemInfo.isPads(context)) {
			drawable.setBounds(0, 0, dip2px(context, 16), dip2px(context, 16));
		} else {
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
		}
		ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
		return imageSpan;
	}

	/**
	 * dip 转 px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 
	 * @Title: getApplicationIcon
	 * @Description: 根据包名，获取应用icon，bitmap
	 * @param @param context
	 * @param @param packageName
	 * @param @return
	 * @return Bitmap
	 */
	public static Bitmap getApplicationIcon(Context context, String packageName) {
		Bitmap bitmap = null;
		Drawable drawable = null;
		PackageManager pm = context.getPackageManager();
		try {
			drawable = pm.getApplicationIcon(packageName);
			bitmap = drawableToBitmap(drawable);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 
	 * @Title: drawableToBitmap
	 * @Description: drawable → Bitmap
	 * @param @param drawable
	 * @param @return
	 * @return Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/***
	 * 
	 * @Description: 是否是Url
	 * @param @param str
	 * @param @return
	 * @return boolean
	 */
	public static boolean isUrl(String str) {
		String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		return matcher(regex, str);
	}
}
