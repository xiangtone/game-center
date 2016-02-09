package com.x.publics.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.x.R;
import com.x.business.audio.AudioEffectManager;

/**
 * 
* @ClassName: DevicesMountUtil
* @Description: 存储设备检测工具类

* @date 2014-4-17 下午5:28:02
*
 */
public class DevicesMountUtil implements IDev {
	public final String HEAD = "dev_mount";
	public final String LABEL = "<label>";
	public final String MOUNT_POINT = "<mount_point>";
	public final String PATH = "<part>";
	public final String SYSFS_PATH = "<sysfs_path1...>";

	private final int NLABEL = 1;
	private final int NPATH = 2;
	private final int NMOUNT_POINT = 3;
	private final int NSYSFS_PATH = 4;

	private ArrayList<String> cache = new ArrayList<String>();
	private static DevicesMountUtil dev;
	private DevicesInfo info;

	private final File VOLD_FSTAB = new File(Environment.getRootDirectory().getAbsoluteFile() + File.separator + "etc"
			+ File.separator + "vold.fstab");

	public static DevicesMountUtil getInstance() {
		if (null == dev)
			dev = new DevicesMountUtil();
		return dev;
	}

	private DevicesInfo getInfo(final int device) {
		if (null == info)
			info = new DevicesInfo();

		try {
			initVoldFstabToCache();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (device >= cache.size())
			return null;
		String[] sinfo = cache.get(device).split(" ");

		info.setLabel(sinfo[NLABEL]);
		info.setMountPoint(sinfo[NMOUNT_POINT]);
		info.setPath(sinfo[NPATH]);
		info.setSysfsPath(sinfo[NSYSFS_PATH]);

		return info;
	}

	/**
	 * init the words into the cache array
	 * @throws IOException
	 */
	private void initVoldFstabToCache() throws IOException {
		cache.clear();
		BufferedReader br = new BufferedReader(new FileReader(VOLD_FSTAB));
		String tmp = null;
		while ((tmp = br.readLine()) != null) {
			// the words startsWith "dev_mount" are the SD info
			if (tmp.startsWith(HEAD)) {
				cache.add(tmp);
			}
		}
		br.close();
		cache.trimToSize();
	}

	public class DevicesInfo {
		private String label, mount_point, path, sysfs_path;

		/**
		 * return the label name of the SD card
		 * @return
		 */
		public String getLabel() {
			return label;
		}

		private void setLabel(String label) {
			this.label = label;
		}

		/**
		 * the mount point of the SD card
		 * @return
		 */
		public String getMountPoint() {
			return mount_point;
		}

		private void setMountPoint(String mount_point) {
			this.mount_point = mount_point;
		}

		/**
		 * SD mount path
		 * @return
		 */
		public String getPath() {
			return path;
		}

		private void setPath(String path) {
			this.path = path;
		}

		/**
		 * "unknow"
		 * @return
		 */
		public String getSysfsPath() {
			return sysfs_path;
		}

		private void setSysfsPath(String sysfs_path) {
			this.sysfs_path = sysfs_path;
		}

	}

	/*
	 * 获取对应存储路径下的总大小
	 */
	private long getInternalOrSDcardAll(String path) {
		try {
			StatFs stat = new StatFs(path);
			long blockSize = stat.getBlockSize();
			long blockCount = stat.getBlockCount();
			long all = (blockCount * blockSize);
			return all;
		} catch (RuntimeException ex) {
			return 0;
		}
	}

	/*
	 * 获取对应存储路径下的使用量
	 */
	private long getInternalOrSDcardAlready(String path) {
		try {
			StatFs stat = new StatFs(path);
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			long blockCount = stat.getBlockCount();
			long available = (availableBlocks * blockSize);
			long all = (blockCount * blockSize);
			long already = all - available;
			return already;
		} catch (RuntimeException ex) {
			return 0;
		}
	}

	//	/*
	//	 * 获取对应存储路径下的使用百分比
	//	 */
	//	private int getInternalOrSDcardStorage(String path){
	//		try {
	//			StatFs stat = new StatFs(path);
	//			long blockSize = stat.getBlockSize(); 
	//			long availableBlocks = stat.getAvailableBlocks();
	//			long blockCount = stat.getBlockCount();
	//			long available = (availableBlocks * blockSize); 
	//			long all = (blockCount * blockSize); 
	//			long already = all - available;
	//			double value = ((double)already / (double)all);
	//			int per = (int) (value * 100);
	//			return per;
	//		} catch (RuntimeException ex) {
	//			return 0;
	//		}
	//	}	

	/*
	 * 获取系统RAM使用百分比
	 */
	@Override
	public int getDevicesRAM(Context con, long release) {
		ActivityManager am = (ActivityManager) con.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		long all = 0;
		if (Build.VERSION.SDK_INT >= 16) {
			try {
				all = mi.totalMem;
			} catch (NoSuchFieldError e) {
				all = getTotalMen(con);
			}
		} else {
			all = getTotalMen(con);
		}
		long avail = mi.availMem;
		long already = all - avail;
		double value = ((double) already / (double) all);
		int per = (int) (value * 100);
		//Toast
		if (release > 0) {
			long temp = avail - release;
			if (temp > 0) {
				ToastUtil.show(con, con.getResources().getString(R.string.storage_ram, Utils.sizeFormat(temp)), 1000);
			} else {
				ToastUtil.show(con, con.getResources().getString(R.string.storage_good), 1000);
			}
		}
		return per;
	}

	private long getTotalMen(Context con) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader burf = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/meminfo")));
			String strInfo = burf.readLine();
			char[] chInfo = strInfo.toCharArray();
			int size = chInfo.length;
			for (int i = 0; i < size; i++) {
				if (chInfo[i] <= '9' && chInfo[i] >= '0') {
					sb.append(chInfo[i]);
				}
			}
			burf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String totalMem = sb.toString();//单位是KB  
		return Long.parseLong(totalMem) * 1024;
	}

	/*
	 * 获取所有存储卡使用百分比
	 */
	@Override
	public int getStoragePercentage() {
		long ltAll = getInternalOrSDcardAll(Environment.getDataDirectory().getAbsolutePath());
		long sdAll = getInternalOrSDcardAll(Environment.getExternalStorageDirectory().getAbsolutePath());
		long all = ltAll + sdAll;
		long ltAlready = getInternalOrSDcardAlready(Environment.getDataDirectory().getAbsolutePath());
		long sdAlready = getInternalOrSDcardAlready(Environment.getExternalStorageDirectory().getAbsolutePath());
		long already = ltAlready + sdAlready;
		double value = ((double) already / (double) all);
		int per = (int) (value * 100);
		return per;
	}

	/*
	 *清理后台进程
	 */
	@Override
	public long killBackgroundProcesses(Context con) {
		ActivityManager am = (ActivityManager) con.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		long avail = mi.availMem;
		List<RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
		if (infoList != null) {
			for (int i = 0; i < infoList.size(); ++i) {
				RunningAppProcessInfo appProcessInfo = infoList.get(i);
				//过滤自己
				if (appProcessInfo.processName.equals(con.getPackageName())) {
					continue;
				}
				if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
					String[] pkgList = appProcessInfo.pkgList;
					for (int j = 0; j < pkgList.length; ++j) {
						am.killBackgroundProcesses(pkgList[j]);
					}
				}
			}
		}
		return avail;
	}

	/*
	 * 清除所有应用缓存
	 */
	@Override
	public void clearAllAppCache(final Context con, final ClearCache cache) {
		ThreadUtil.start(new Runnable() {

			@Override
			public void run() {
				List<PackageInfo> packages = con.getPackageManager().getInstalledPackages(
						PackageManager.GET_UNINSTALLED_PACKAGES);
				sum = 0;
				for (int i = 0; i < packages.size(); i++) {
					PackageInfo packageInfo = packages.get(i);
					String appName = packageInfo.packageName;
//					if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
						//忽略本应用
						if (appName.equals(con.getPackageName()))
							continue;
						try {

							clearAllCache(con.createPackageContext(appName, Context.CONTEXT_INCLUDE_CODE
									| Context.CONTEXT_IGNORE_SECURITY));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				cache.resultCache(sum);
			}
		});
	}

	private void clearAllCache(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			if (context.getExternalCacheDir() != null) {
				deleteFile(context.getExternalCacheDir());
			}
		}
	}

	long sum = 0;

	private void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				sum += file.length();
				System.out.println("delete file " + file.getPath() + file.delete());
			} else if (file.isDirectory()) {
				if (file.listFiles() != null) { //权限原因
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFile(files[i]);
					}
				}
			}
		}
	}

	public interface ClearCache {
		//返回清理数据
		void resultCache(long sum);
	}

	/**
	 * 获取APP缓存大小
	 * 
	 * 展示数据时使用！
	 */
	//	private void queryAppCacheSize(Context con, String pkgName) {
	//		if (!TextUtils.isEmpty(pkgName)) {
	//			PackageManager manager = con.getPackageManager();
	//			try {
	//				String strGetPackageSizeInfo = "getPackageSizeInfo";
	//				Method getPackageSizeInfo = manager.getClass().getMethod(
	//						strGetPackageSizeInfo, String.class,
	//								IPackageStatsObserver.class);
	//				getPackageSizeInfo.invoke(manager,
	//						pkgName, mStatsObserver);
	//			} catch (Exception ex) {
	//				ex.printStackTrace();
	//			}
	//		}
	//	}
	//	private IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
	//		@Override
	//		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
	//				throws RemoteException {
	//			if(pStats.cacheSize > 0){
	//				Log.i("Simple", "-------------pStats.page="+ pStats.packageName
	//						+",cachesize="+ pStats.cacheSize
	//						+",succeeded="+ succeeded);
	//			}
	//		}
	//	};
}

interface IDev {
	//所有存储卡使用百分比
	int getStoragePercentage();

	//RAM使用百分比
	int getDevicesRAM(Context con, long release);

	//清理后台进程
	long killBackgroundProcesses(Context con);

	//清理所有应用缓存
	void clearAllAppCache(Context con, DevicesMountUtil.ClearCache cache);
}
