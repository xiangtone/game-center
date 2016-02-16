package com.x.publics.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class StorageUtils {

	public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

	public static final String FILE_ROOT = SDCARD_ROOT + File.separator + "zApp";

	public static final String FILE_DOWNLOAD_APK_PATH = FILE_ROOT + File.separator + "Download" + File.separator
			+ "Apks" + File.separator;

	public static final String FILE_DOWNLOAD_MUSIC_PATH = FILE_ROOT + File.separator + "Download" + File.separator
			+ "Music" + File.separator;

	public static final String FILE_DOWNLOAD_WALLPAPER_PATH = FILE_ROOT + File.separator + "Download" + File.separator
			+ "zApp Picture" + File.separator;

	public static final String FILE_DOWNLOAD_PLATFORM_UPGRADE_APK_PATH = FILE_ROOT + File.separator + "Download"
			+ File.separator + "Upgrade" + File.separator;

	public static final String FILE_ZERO_SHARE_PATH = FILE_ROOT + File.separator + "zApp Receiver" + File.separator;

	public static final String FILE_DOWNLOAD_SKIN_PATH = FILE_ROOT + File.separator + "Download" + File.separator
			+ "Skin" + File.separator;

	private static final long LOW_STORAGE_THRESHOLD = 1024 * 1024 * 10;

	public static boolean isSdCardWrittenable() {

		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public static long getAvailableStorage() {

		String storageDirectory = null;
		storageDirectory = Environment.getExternalStorageDirectory().toString();

		try {
			StatFs stat = new StatFs(storageDirectory);
			long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
			return avaliableSize;
		} catch (RuntimeException ex) {
			return 0;
		}
	}

	public static long getUsableStorage() {
		String storageDirectory = null;
		storageDirectory = Environment.getExternalStorageDirectory().toString();

		try {
			StatFs stat = new StatFs(storageDirectory);
			long usableSize = ((long) stat.getBlockCount() * (long) stat.getBlockSize()) - getAvailableStorage();
			return usableSize;
		} catch (RuntimeException ex) {
			return 0;
		}
	}

	public static boolean checkAvailableStorage() {

		if (getAvailableStorage() < LOW_STORAGE_THRESHOLD) {
			return false;
		}

		return true;
	}

	public static boolean isSDCardPresent() {

		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static void mkdir() throws IOException {

		File file = new File(FILE_DOWNLOAD_APK_PATH);
		if (!file.exists() || !file.isDirectory())
			file.mkdirs();

		File musicFile = new File(FILE_DOWNLOAD_MUSIC_PATH);
		if (!musicFile.exists() || !musicFile.isDirectory())
			musicFile.mkdirs();

		File wallpaperFile = new File(FILE_DOWNLOAD_WALLPAPER_PATH);
		if (!wallpaperFile.exists() || !wallpaperFile.isDirectory())
			wallpaperFile.mkdirs();

		File upgradeFile = new File(FILE_DOWNLOAD_PLATFORM_UPGRADE_APK_PATH);
		if (!upgradeFile.exists() || !upgradeFile.isDirectory())
			upgradeFile.mkdirs();

		File receive = new File(FILE_ZERO_SHARE_PATH);
		if (!receive.exists() || !receive.isDirectory())
			receive.mkdirs();

		File skin = new File(FILE_DOWNLOAD_SKIN_PATH);
		if (!skin.exists() || !skin.isDirectory())
			skin.mkdirs();

	}

	public static Bitmap getLoacalBitmap(String url) {

		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String size2(long size) {
		float tmpSize = (float) (size) / (float) (1024 * 1024 * 1024);
		DecimalFormat df = new DecimalFormat("#.##");
		return "" + df.format(tmpSize) + "G";

	}

	public static String size(long size) {
		if (size / (1024 * 1024 * 1024) > 0) {
			float tmpSize = (float) (size) / (float) (1024 * 1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "G";
		} else if (size / (1024 * 1024) > 0) {
			float tmpSize = (float) (size) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		} else if (size / 1024 > 0) {
			return "" + (size / (1024)) + "KB";
		} else
			return "" + size + "B";
	}

	/**
	 * 转换文件大小
	 * @param fileS
	 * @return
	 */
	public static String sizeMB(long fileS) {
		if (fileS / (1024 * 1024 * 1024) > 0) {
			float tmpSize = (float) (fileS) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		} else if (fileS / (1024 * 1024) > 0) {
			float tmpSize = (float) (fileS) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		} else if (fileS / 1024 > 0) {
			float tmpSize = (float) (fileS) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		} else
			return "0.01MB";
	}

	public static void installAPK(Context context, final String url) {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		String fileName = FILE_ROOT + NetworkUtils.getFileNameFromUrl(url);
		intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.setClassName("com.android.packageinstaller", "com.android.packageinstaller.PackageInstallerActivity");
		context.startActivity(intent);
	}

	public static boolean delete(File path) {

		boolean result = true;
		if (path.exists()) {
			if (path.isDirectory()) {
				for (File child : path.listFiles()) {
					result &= delete(child);
				}
				result &= path.delete(); // Delete empty directory.
			}
			if (path.isFile()) {
				result &= path.delete();
			}
			if (!result) {
				Log.e(null, "Delete failed;");
			}
			return result;
		} else {
			Log.e(null, "File does not exist.");
			return false;
		}
	}

	public static boolean isFileExit(String path) {
		if (path == null || path.equals(""))
			return false;
		File file = new File(path);
		return file.exists();
	}

	public static void clearAppCache(Context context) {
		clearCacheFolder(context.getFilesDir(), System.currentTimeMillis());
		clearCacheFolder(context.getCacheDir(), System.currentTimeMillis());
		if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			clearCacheFolder(context.getExternalCacheDir(), System.currentTimeMillis());
		}
	}

	private static int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	public static long getCacheFileSize(Context context) {
		long fileSize = 0;
		File filesDir = context.getFilesDir();// /data/data/package_name/files  
		File cacheDir = context.getCacheDir();// /data/data/package_name/cache  
		fileSize += getDirSize(filesDir);
		fileSize += getDirSize(cacheDir);
		if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {

			File externalCacheDir = context.getExternalCacheDir();//"<sdcard>/Android/data/<package_name>/cache/"  
			fileSize += getDirSize(externalCacheDir);
		}
		return fileSize;
	}

	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计  
			}
		}
		return dirSize;
	}
}
