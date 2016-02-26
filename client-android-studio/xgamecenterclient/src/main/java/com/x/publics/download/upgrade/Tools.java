package com.x.publics.download.upgrade;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

public class Tools {

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

	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获取文件的MD5值
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileMD5(File file) {
		try {
			InputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
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
	 * 判断目录是否存在且可读写
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean isFileExistsAndCanReadWrite(File dir) {
		return dir != null && dir.exists() && dir.canRead() && dir.canWrite();
	}

	/**
	 * 
	 * @Title: getOldApkPath
	 * @Description: 获取旧包的备份路径（只要安装了该应用、该路径永远存在）
	 * @param @param context
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getOldApkPath(Context context) {
		String strUrl = "";
		try {
			strUrl = context.getPackageManager().getApplicationInfo(
					context.getPackageName(),
					PackageManager.GET_UNINSTALLED_PACKAGES).sourceDir;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strUrl;
	}

	/**
	 * 
	 * @Title: getApkSize
	 * @Description: 获取已安装包文件大小
	 * @param @param context
	 * @param @return
	 * @return int
	 */
	public static int getApkSize(Context context) {
		String strUrl = "";
		try {
			strUrl = context.getPackageManager().getApplicationInfo(
					context.getPackageName(),
					PackageManager.GET_UNINSTALLED_PACKAGES).publicSourceDir;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int size = Integer.valueOf((int) new File(strUrl).length());
		return size;
	}

	/**
	 * 
	 * @Title: getFileName
	 * @Description: 获取文件名称
	 * @param @param url
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getFileName(String url) {
		String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
		return fileName;
	}
}
