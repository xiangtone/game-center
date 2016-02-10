/**   
* @Title: DownloadEntityManager.java
* @Package com.x.db
* @Description: TODO 

* @date 2013-12-17 上午11:29:30
* @version V1.0   
*/

package com.x.db;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.x.business.statistic.DataEyeManager;
import com.x.db.dao.DownloadInfoEntity;
import com.x.publics.download.DownloadTask;
import com.x.publics.model.DownloadBean;
import com.x.publics.utils.Constan.MediaType;

/**
* @ClassName: DownloadEntityManager
* @Description: 下载信息数据库管理 

* @date 2013-12-17 上午11:29:30
* 
*/

public class DownloadEntityManager extends ModeManagerBase {
	private static DownloadEntityManager AInstance;

	private DownloadEntityManager() {

	}

	public static DownloadEntityManager getInstance() {
		if (AInstance == null)
			AInstance = new DownloadEntityManager();

		return AInstance;
	}

	public void close() {
		_db.close();
	}

	/** 
	* @Title: save 
	* @Description: 保存正在下载记录 
	* @param @param downloadBean     
	* @return void    
	*/
	public void save(DownloadBean downloadBean) {
		if (!isExitUrl(downloadBean.getUrl())) {
			try {
				_db.insert(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, null, getValues(downloadBean));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	private ContentValues getValues(DownloadBean downloadBean) {
		ContentValues values = new ContentValues();
		values.put(DownloadInfoEntity.APP_NAME, downloadBean.getName());
		values.put(DownloadInfoEntity.RESOURCE, downloadBean.getResource());
		values.put(DownloadInfoEntity.ICON_URL, downloadBean.getIconUrl());
		values.put(DownloadInfoEntity.DOWNLOAD_PATH, downloadBean.getUrl());
		values.put(DownloadInfoEntity.DOWNLOAD_LENGTH, "" + downloadBean.getCurrentBytes());
		values.put(DownloadInfoEntity.TOTAL_SIZE, "" + downloadBean.getTotalBytes());
		values.put(DownloadInfoEntity.MEDIA_TYPE, downloadBean.getMediaType());
		values.put(DownloadInfoEntity.RESOURCE_ID, downloadBean.getResourceId());
		values.put(DownloadInfoEntity.VERSION, downloadBean.getVersionName());
		values.put(DownloadInfoEntity.LOCAL_PATH, downloadBean.getLocalPath());
		values.put(DownloadInfoEntity.APP_STAUS, downloadBean.getStatus());
		values.put(DownloadInfoEntity.APP_PACKAGENAME, downloadBean.getPackageName());
		values.put(DownloadInfoEntity.CREATE_TIME, downloadBean.getCreateTime());
		values.put(DownloadInfoEntity.FINISHED_TIME, downloadBean.getFinishedTime());
		values.put(DownloadInfoEntity.DOWNLOAD_SPEED, downloadBean.getSpeed());
		values.put(DownloadInfoEntity.APP_ID, downloadBean.getAppId());
		values.put(DownloadInfoEntity.CATEGORY_ID, downloadBean.getCategoryId());
		values.put(DownloadInfoEntity.STARTS, downloadBean.getStars());
		values.put(DownloadInfoEntity.VERSION_CODE, downloadBean.getVersionCode());
		values.put(DownloadInfoEntity.ORIGINAL_URL, downloadBean.getOriginalUrl());
		return values;
	}

	/** 
	* @Title: isExitUrl 
	* @Description: 是否存在下载记录 
	* @param @param path
	* @param @return     
	* @return boolean    
	*/

	public synchronized boolean isExitUrl(String path) {
		boolean result = false;
		Cursor cursor = null;
		try {
			cursor = _db.query(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, new String[] { DownloadInfoEntity.THREAD_ID,
					DownloadInfoEntity.DOWNLOAD_LENGTH }, DownloadInfoEntity.DOWNLOAD_PATH + "=?",
					new String[] { path }, null, null, null);

			while (cursor.moveToNext()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return result;
	}

	/** 
	* @Title: findDownloadBean 
	* @Description: 查询下载 
	* @param @param selection
	* @param @param selectionArgs
	* @param @return     
	* @return DownloadBean    
	*/

	private synchronized DownloadBean findDownloadBean(String selection, String[] selectionArgs) {
		DownloadBean bean = null;
		Cursor cursor = null;
		try {
			cursor = _db.query(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, new String[] { DownloadInfoEntity.RESOURCE,
					DownloadInfoEntity.ICON_URL, DownloadInfoEntity.TOTAL_SIZE, DownloadInfoEntity.DOWNLOAD_PATH,
					DownloadInfoEntity.DOWNLOAD_LENGTH, DownloadInfoEntity.LOCAL_PATH, DownloadInfoEntity.RESOURCE_ID,
					DownloadInfoEntity.MEDIA_TYPE, DownloadInfoEntity.APP_NAME, DownloadInfoEntity.APP_STAUS,
					DownloadInfoEntity.APP_PACKAGENAME, DownloadInfoEntity.CREATE_TIME,
					DownloadInfoEntity.FINISHED_TIME, DownloadInfoEntity.VERSION, DownloadInfoEntity.DOWNLOAD_SPEED,
					DownloadInfoEntity.APP_ID, DownloadInfoEntity.CATEGORY_ID, DownloadInfoEntity.STARTS,
					DownloadInfoEntity.VERSION_CODE, DownloadInfoEntity.ORIGINAL_URL }, selection, selectionArgs, null,
					null, null);
			while (cursor.moveToNext()) {
				bean = new DownloadBean();
				bean.setResource((cursor.getString(0)));
				bean.setIconUrl(cursor.getString(1));
				bean.setTotalBytes(Long.valueOf(cursor.getString(2)));
				bean.setFileSize(bean.getTotalBytes());
				bean.setUrl(cursor.getString(3));
				bean.setCurrentBytes(Long.valueOf(cursor.getString(4)));
				bean.setLocalPath(cursor.getString(5));
				bean.setResourceId(cursor.getInt(6));
				bean.setMediaType(cursor.getString(7));
				bean.setName(cursor.getString(8));
				bean.setStatus(cursor.getInt(9));
				bean.setPackageName(cursor.getString(10));
				bean.setCreateTime(cursor.getString(11));
				bean.setFinishedTime(cursor.getString(12));
				bean.setVersionName(cursor.getString(13));
				bean.setSpeed(cursor.getString(14));
				bean.setAppId(cursor.getInt(15));
				bean.setCategoryId(cursor.getInt(16));
				bean.setStars(cursor.getInt(17));
				bean.setVersionCode(cursor.getInt(18));
				bean.setOriginalUrl(cursor.getString(19));

				if (!TextUtils.isEmpty(bean.getOriginalUrl()) & !TextUtils.isEmpty(bean.getUrl())
						&& !bean.getUrl().equals(bean.getOriginalUrl())) {
					bean.setPatch(true);
				}

				bean.setFileType(DataEyeManager.getInstance().getFileType(bean.getMediaType()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return bean;

	}

	/** 
	* @Title: getDownloadBeanByResId 
	* @Description: 根据资源id获取DownloadBean 
	* @param @param resId
	* @param @return     
	* @return DownloadBean    
	*/

	public synchronized DownloadBean getDownloadBeanByResId(String resId) {
		return findDownloadBean(DownloadInfoEntity.RESOURCE_ID + "=? ", new String[] { resId });
	}

	/** 
	* @Title: getDownloadBeanByResId 
	* @Description: 根据资源id , versionCode获取DownloadBean  
	* @param @param resId
	* @param @param versionCode
	* @param @return     
	* @return DownloadBean    
	*/

	public synchronized DownloadBean getDownloadBeanByResId(String resId, String versionCode) {
		return findDownloadBean(DownloadInfoEntity.RESOURCE_ID + "=? AND " + DownloadInfoEntity.VERSION_CODE + " =? ",
				new String[] { resId, versionCode });
	}

	/** 
	* @Title: getDownloadBeanByPkgName 
	* @Description:  根据包名获取DownloadBean
	* @param @param pkgName
	* @param @return     
	* @return DownloadBean    
	*/

	public synchronized DownloadBean getDownloadBeanByPkgName(String pkgName) {
		return findDownloadBean(DownloadInfoEntity.APP_PACKAGENAME + "=? ", new String[] { pkgName });
	}

	/** 
	* @Title: getDownloadBeanByPkgName 
	* @Description: 根据包名,版本号获取DownloadBean 
	* @param @param pkgName
	* @param @param versionCode
	* @param @return     
	* @return DownloadBean    
	*/

	public synchronized DownloadBean getDownloadBeanByPkgName(String pkgName, String versionCode) {
		return findDownloadBean(DownloadInfoEntity.APP_PACKAGENAME + "=? AND " + DownloadInfoEntity.VERSION_CODE
				+ " =? ", new String[] { pkgName, versionCode });
	}

	public synchronized String getOriginalUrlByLocalPath(String localPath) {
		DownloadBean downloadBean = findDownloadBean(DownloadInfoEntity.LOCAL_PATH + " =? ", new String[] { localPath });
		if (downloadBean != null)
			return downloadBean.getOriginalUrl();
		return "";
	}

	/** 
	* @Title: update 
	* @Description: 更新下载 
	* @param @param downloadBean     
	* @return void    
	*/

	public synchronized void update(DownloadBean downloadBean) {
		try {
			_db.update(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, getValues(downloadBean),
					DownloadInfoEntity.DOWNLOAD_PATH + "=?", new String[] { downloadBean.getUrl() });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/** 
	* @Title: updateAllPause 
	* @Description: TODO 
	* @param      
	* @return void    
	*/

	public synchronized void updateAllPause() {
		ContentValues cv = new ContentValues();
		cv.put(DownloadInfoEntity.APP_STAUS, DownloadTask.TASK_PAUSE);
		try {
			_db.update(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, cv, DownloadInfoEntity.APP_STAUS + "!=? and "
					+ DownloadInfoEntity.APP_STAUS + "!=? ", new String[] { "" + DownloadTask.TASK_FINISH,
					"" + DownloadTask.TASK_LAUNCH });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public synchronized void updateDownloadInstallingStatus(String path) {
		ContentValues cv = new ContentValues();
		cv.put(DownloadInfoEntity.APP_STAUS, DownloadTask.TASK_INSTALLING);
		try {
			_db.update(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, cv, DownloadInfoEntity.LOCAL_PATH + "=? ",
					new String[] { path });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public synchronized void updateDownloadInstallStatus(String originalUrl) {
		ContentValues cv = new ContentValues();
		cv.put(DownloadInfoEntity.APP_STAUS, DownloadTask.TASK_FINISH);
		try {
			_db.update(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, cv, DownloadInfoEntity.ORIGINAL_URL + "=? ",
					new String[] { originalUrl });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/** 
	* @Title: deleteByUrl 
	* @Description: 根据url删除下载记录 
	* @param @param url     
	* @return void    
	*/

	public synchronized void deleteByUrl(String url) {
		_db.delete(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, DownloadInfoEntity.DOWNLOAD_PATH + "=?",
				new String[] { url });
	}

	/** 
	* @Title: deleteAllFinished 
	* @Description: 删除所有下载历史 
	* @param      
	* @return void    
	*/

	public synchronized void deleteAllFinished() {
		_db.delete(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, DownloadInfoEntity.APP_STAUS + "=? OR "
				+ DownloadInfoEntity.APP_STAUS + "=?", new String[] { "" + DownloadTask.TASK_FINISH,
				"" + DownloadTask.TASK_LAUNCH });
	}

	public synchronized void deleteUnfinishedMedia() {
		_db.delete(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, "(" + DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR "
				+ DownloadInfoEntity.MEDIA_TYPE + "=? )" + " AND " + DownloadInfoEntity.APP_STAUS + "!=?",
				new String[] { MediaType.IMAGE, MediaType.MUSIC, "" + DownloadTask.TASK_FINISH });
	}

	/** 
	* @Title: deleteAllUnFinished 
	* @Description: 删除所有下载未完成任务 
	* @param      
	* @return void    
	*/

	public synchronized void deleteAllUnFinished() {
		_db.delete(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, DownloadInfoEntity.APP_STAUS + "!=? and "
				+ DownloadInfoEntity.APP_STAUS + "!=? ", new String[] { "" + DownloadTask.TASK_FINISH,
				"" + DownloadTask.TASK_LAUNCH });
	}

	/** 
	* @Title: deleteByPkgName 
	* @Description: 根据packageName删除下载记录  
	* @param @param packageName     
	* @return void    
	*/

	public synchronized void deleteByPkgName(String packageName, String versionCode) {
		_db.delete(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, DownloadInfoEntity.APP_PACKAGENAME + "=? AND "
				+ DownloadInfoEntity.VERSION_CODE + " =? ", new String[] { packageName, versionCode });
	}

	/** 
	* @Title: findDownloads 
	* @Description: 查询多个下载 
	* @param @param selection
	* @param @param selectionArgs
	* @param @return     
	* @return ArrayList<DownloadBean>    
	*/

	private ArrayList<DownloadBean> findDownloads(String selection, String[] selectionArgs) {
		ArrayList<DownloadBean> result = new ArrayList<DownloadBean>();
		Cursor cursor = null;
		try {
			cursor = _db.query(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, new String[] { DownloadInfoEntity.RESOURCE,
					DownloadInfoEntity.ICON_URL, DownloadInfoEntity.TOTAL_SIZE, DownloadInfoEntity.DOWNLOAD_PATH,
					DownloadInfoEntity.DOWNLOAD_LENGTH, DownloadInfoEntity.LOCAL_PATH, DownloadInfoEntity.RESOURCE_ID,
					DownloadInfoEntity.MEDIA_TYPE, DownloadInfoEntity.APP_NAME, DownloadInfoEntity.APP_STAUS,
					DownloadInfoEntity.APP_PACKAGENAME, DownloadInfoEntity.CREATE_TIME,
					DownloadInfoEntity.FINISHED_TIME, DownloadInfoEntity.VERSION, DownloadInfoEntity.DOWNLOAD_SPEED,
					DownloadInfoEntity.APP_ID, DownloadInfoEntity.CATEGORY_ID, DownloadInfoEntity.STARTS,
					DownloadInfoEntity.VERSION_CODE, DownloadInfoEntity.ORIGINAL_URL }, selection, selectionArgs, null,
					null, null);
			while (cursor.moveToNext()) {
				DownloadBean bean = new DownloadBean();
				bean.setResource((cursor.getString(0)));
				bean.setIconUrl(cursor.getString(1));
				bean.setTotalBytes(Long.valueOf(cursor.getString(2)));
				bean.setFileSize(bean.getTotalBytes());
				bean.setUrl(cursor.getString(3));
				bean.setCurrentBytes(Long.valueOf(cursor.getString(4)));
				bean.setLocalPath(cursor.getString(5));
				bean.setResourceId(cursor.getInt(6));
				bean.setMediaType(cursor.getString(7));
				bean.setName(cursor.getString(8));
				bean.setStatus(cursor.getInt(9));
				bean.setPackageName(cursor.getString(10));
				bean.setCreateTime(cursor.getString(11));
				bean.setFinishedTime(cursor.getString(12));
				bean.setVersionName(cursor.getString(13));
				bean.setSpeed(cursor.getString(14));
				bean.setAppId(cursor.getInt(15));
				bean.setCategoryId(cursor.getInt(16));
				bean.setStars(cursor.getInt(17));
				bean.setVersionCode(cursor.getInt(18));
				bean.setOriginalUrl(cursor.getString(19));
				if (!TextUtils.isEmpty(bean.getOriginalUrl()) & !TextUtils.isEmpty(bean.getUrl())//判断是否增量更新
						&& !bean.getUrl().equals(bean.getOriginalUrl())) {
					bean.setPatch(true);
				}
				bean.setFileType(DataEyeManager.getInstance().getFileType(bean.getMediaType()));
				result.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return result;
	}

	private int findDownloadCount(String selection, String[] selectionArgs) {
		Cursor cursor = null;
		int count = 0;
		try {
			cursor = _db.query(DownloadInfoEntity.DOWNLOAD_TABLE_NAME, new String[] { DownloadInfoEntity.RESOURCE,
					DownloadInfoEntity.ICON_URL, DownloadInfoEntity.TOTAL_SIZE, DownloadInfoEntity.DOWNLOAD_PATH,
					DownloadInfoEntity.DOWNLOAD_LENGTH, DownloadInfoEntity.LOCAL_PATH, DownloadInfoEntity.RESOURCE_ID,
					DownloadInfoEntity.MEDIA_TYPE, DownloadInfoEntity.APP_NAME, DownloadInfoEntity.APP_STAUS,
					DownloadInfoEntity.APP_PACKAGENAME, DownloadInfoEntity.CREATE_TIME,
					DownloadInfoEntity.FINISHED_TIME, DownloadInfoEntity.VERSION, DownloadInfoEntity.DOWNLOAD_SPEED,
					DownloadInfoEntity.APP_ID, DownloadInfoEntity.CATEGORY_ID, DownloadInfoEntity.STARTS,
					DownloadInfoEntity.VERSION_CODE, DownloadInfoEntity.ORIGINAL_URL }, selection, selectionArgs, null,
					null, null);
			while (cursor.moveToNext()) {
				count = cursor.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return count;
	}

	/** 
	* @Title: getAllDownload 
	* @Description: 获取所有下载记录 
	* @param @return     
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllDownload() {
		return findDownloads(null, null);
	}

	/** 
	* @Title: getAppDownloadByPackageName 
	* @Description: 根据包名获取所有下载完成未安装记录
	* @param @param PackageName
	* @param @return    
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAppDownloadFinishedByPackageName(String PackageName) {
		return findDownloads(DownloadInfoEntity.APP_PACKAGENAME + "=? AND " + DownloadInfoEntity.APP_STAUS + " =?",
				new String[] { PackageName, "" + DownloadTask.TASK_FINISH });
	}

	/** 
	* @Title: getAllAppDownload 
	* @Description: 获取所有应用下载记录 
	* @param @return    
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllAppDownload() {
		return findDownloads(DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR " + DownloadInfoEntity.MEDIA_TYPE + "=? ",
				new String[] { MediaType.APP, MediaType.GAME });
	}

	public HashMap<String, DownloadBean> getAllAppDownloadMap() {
		HashMap<String, DownloadBean> result = new HashMap<String, DownloadBean>();
		ArrayList<DownloadBean> downloadList = getAllAppDownload();
		for (DownloadBean downloadBean : downloadList) {
			result.put(downloadBean.getPackageName(), downloadBean);
		}
		return result;
	}

	/** 
	 * @Title: getAllWallpaperDownload 
	 * @Description: 获取壁纸所有下载记录 
	 * @param @return     
	 * @return ArrayList<DownloadBean>    
	 */

	public ArrayList<DownloadBean> getAllWallpaperDownload() {
		return findDownloads(DownloadInfoEntity.MEDIA_TYPE + "=?", new String[] { "" + MediaType.IMAGE + "" });
	}

	public ArrayList<DownloadBean> getAllThemeDownload() {
		return findDownloads(DownloadInfoEntity.MEDIA_TYPE + "=?", new String[] { "" + MediaType.THEME + "" });
	}

	public HashMap<String, DownloadBean> getAllDownloadThemeMap() {
		HashMap<String, DownloadBean> result = new HashMap<String, DownloadBean>();
		ArrayList<DownloadBean> downloadList = getAllThemeDownload();
		for (DownloadBean downloadBean : downloadList) {
			result.put("" + downloadBean.getResourceId(), downloadBean);
		}
		return result;
	}

	/** 
	* @Title: getAllDownloading 
	* @Description: 获取所有下载中记录
	* @param @return     
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllDownloading() {
		return findDownloads(DownloadInfoEntity.APP_STAUS + "=? OR " + DownloadInfoEntity.APP_STAUS + "=? OR "
				+ DownloadInfoEntity.APP_STAUS + "=?", new String[] { "" + DownloadTask.TASK_DOWNLOADING,
				"" + DownloadTask.TASK_CONNECTING, "" + DownloadTask.TASK_WAITING });
	}

	/** 
	* @Title: getAllAppPaused 
	* @Description: 获取所有暂停中的app下载记录
	* @param @return    
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllAppPaused() {
		return findDownloads("(" + DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR " + DownloadInfoEntity.MEDIA_TYPE
				+ "=? )" + " AND " + DownloadInfoEntity.APP_STAUS + "=?", new String[] { MediaType.APP, MediaType.GAME,
				"" + DownloadTask.TASK_PAUSE });
	}

	/** 
	* @Title: getAllAppDownloading 
	* @Description: 获取所有进入下载状态的app下载记录
	* @param @return    
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllAppDownloading() {
		return findDownloads("(" + DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR " + DownloadInfoEntity.MEDIA_TYPE
				+ "=? )" + " AND " + "(" + DownloadInfoEntity.APP_STAUS + "=? OR " + DownloadInfoEntity.APP_STAUS
				+ "=? OR " + DownloadInfoEntity.APP_STAUS + "=?)", new String[] { MediaType.APP, MediaType.GAME,
				"" + DownloadTask.TASK_DOWNLOADING, "" + DownloadTask.TASK_CONNECTING, "" + DownloadTask.TASK_WAITING });
	}


	/** 
	* @Title: getAllUninstall 
	* @Description: 获取所有下载完成未安装的记录
	* @param @return     
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllUninstall() {
		return findDownloads(DownloadInfoEntity.APP_STAUS + "=?", new String[] { "" + DownloadTask.TASK_FINISH });
	}

	/** 
	* @Title: getAllUnfinishedAppsDownloadList 
	* @Description: 获取所有未安装的应用记录
	* @param @return    
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllUnInstallAppsDownloadList() {
		return findDownloads("(" + DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR " + DownloadInfoEntity.MEDIA_TYPE
				+ "=? )" + " AND " + DownloadInfoEntity.APP_STAUS + "=?", new String[] { MediaType.APP, MediaType.GAME,
				"" + DownloadTask.TASK_FINISH });
	}

	/** 
	* @Title: getAllUnInstallAppsDownloadCount 
	* @Description: 获取所有未安装的应用记录数
	* @param @return    
	* @return int    
	*/

	public int getAllUnInstallAppsDownloadCount() {
		return findDownloadCount("(" + DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR " + DownloadInfoEntity.MEDIA_TYPE
				+ "=? )" + " AND (" + DownloadInfoEntity.APP_STAUS + "=? OR " + DownloadInfoEntity.APP_STAUS + "=? )",
				new String[] { MediaType.APP, MediaType.GAME, "" + DownloadTask.TASK_FINISH,
						"" + DownloadTask.TASK_INSTALLING });
	}

	/** 
	* @Title: getAllUnfinishedAppsDownloadList 
	* @Description: 获取所有未下载完成的应用记录
	* @param @return    
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllUnFinishedAppsDownloadList() {
		return findDownloads("(" + DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR " + DownloadInfoEntity.MEDIA_TYPE
				+ "=? )" + " AND " + DownloadInfoEntity.APP_STAUS + "!=?" + "AND " + DownloadInfoEntity.APP_STAUS
				+ "!=?", new String[] { MediaType.APP, MediaType.GAME, "" + DownloadTask.TASK_FINISH,
				"" + DownloadTask.TASK_LAUNCH });
	}

	/** 
	* @Title: getAllUnCompleteAppsDownloadCount 
	* @Description: 获取所有除已安装的下载应用记录数
	* @param @return    
	* @return int    
	*/

	public int getAllUnCompleteAppsDownloadCount() {
		return findDownloadCount("(" + DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR " + DownloadInfoEntity.MEDIA_TYPE
				+ "=? )" + " AND " + DownloadInfoEntity.APP_STAUS + "!=?", new String[] { MediaType.APP,
				MediaType.GAME, "" + DownloadTask.TASK_LAUNCH });
	}

	/** 
	* @Title: getAllUnFinishedAppsDownloadCount 
	* @Description: 获取所有未下载完成的应用记录数
	* @param @return    
	* @return int    
	*/

	public int getAllUnFinishedAppsDownloadCount() {
		return findDownloadCount("(" + DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR " + DownloadInfoEntity.MEDIA_TYPE
				+ "=? )" + " AND " + DownloadInfoEntity.APP_STAUS + "!=?" + "AND " + DownloadInfoEntity.APP_STAUS
				+ "!=?", new String[] { MediaType.APP, MediaType.GAME, "" + DownloadTask.TASK_FINISH,
				"" + DownloadTask.TASK_LAUNCH });
	}

	/** 
	* @Title: getAllUnfinishedMediaDownloadList 
	* @Description: 获取所有未下载完成的铃声/壁纸记录
	* @param @return    
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllUnfinishedMediaDownloadList() {
		return findDownloads("(" + DownloadInfoEntity.MEDIA_TYPE + "=?" + " OR " + DownloadInfoEntity.MEDIA_TYPE
				+ "=? )" + " AND " + DownloadInfoEntity.APP_STAUS + "!=?", new String[] { MediaType.IMAGE,
				MediaType.MUSIC, "" + DownloadTask.TASK_FINISH });
	}

	/** 
	* @Title: getAllDownloadMap 
	* @Description: 获取所有下载记录   HashMap<packageName, DownloadBean>
	* @param @return     
	* @return HashMap<String,DownloadBean>    
	*/

	public HashMap<String, DownloadBean> getAllDownloadMap() {
		HashMap<String, DownloadBean> result = new HashMap<String, DownloadBean>();
		ArrayList<DownloadBean> downloadList = getAllDownload();
		for (DownloadBean downloadBean : downloadList) {
			result.put(downloadBean.getPackageName(), downloadBean);
		}
		return result;
	}

	public HashMap<String, DownloadBean> getAllDownloadResourceIdMap() {
		HashMap<String, DownloadBean> result = new HashMap<String, DownloadBean>();
		ArrayList<DownloadBean> downloadList = getAllDownload();
		for (DownloadBean downloadBean : downloadList) {
			result.put("" + downloadBean.getResourceId(), downloadBean);
		}
		return result;
	}

	/** 
	* @Title: getAllDownloading 
	* @Description: 获取所有未完成下载
	* @param @return     
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllUnfinishedDownload() {
		return findDownloads(DownloadInfoEntity.APP_STAUS + "!=? and " + DownloadInfoEntity.APP_STAUS + "!=? ",
				new String[] { "" + DownloadTask.TASK_FINISH, "" + DownloadTask.TASK_LAUNCH });
	}

	/** 
	* @Title: getAllUnfinishedDownloadCount 
	* @Description: 获取所有未完成下载条目数
	* @param @return    
	* @return int    
	*/

	public int getAllUnfinishedDownloadCount() {
		return findDownloadCount(DownloadInfoEntity.APP_STAUS + "!=? and " + DownloadInfoEntity.APP_STAUS + "!=? ",
				new String[] { "" + DownloadTask.TASK_FINISH, "" + DownloadTask.TASK_LAUNCH });
	}

	/** 
	* @Title: getAllDownloading 
	* @Description: 获取所有完成下载
	* @param @return     
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllFinishedDownload() {
		return findDownloads(DownloadInfoEntity.APP_STAUS + "=? OR " + DownloadInfoEntity.APP_STAUS + "=?",
				new String[] { "" + DownloadTask.TASK_FINISH, "" + DownloadTask.TASK_LAUNCH });
	}

	/** 
	* @Title: getAllFinishedDownloadCount 
	* @Description: 获取所有完成下载条目数 
	* @param @return    
	* @return int    
	*/

	public int getAllFinishedDownloadCount() {
		return findDownloadCount(DownloadInfoEntity.APP_STAUS + "=? OR " + DownloadInfoEntity.APP_STAUS + "=?",
				new String[] { "" + DownloadTask.TASK_FINISH, "" + DownloadTask.TASK_LAUNCH });
	}

	/** 
	* @Title: getAllLaunchDownloadByPackageName 
	* @Description: 根据报名获取所有可运行下载 
	* @param @param packageName
	* @param @return     
	* @return ArrayList<DownloadBean>    
	*/

	public ArrayList<DownloadBean> getAllLaunchDownloadByPackageName(String packageName) {
		return findDownloads(DownloadInfoEntity.APP_STAUS + "=? AND " + DownloadInfoEntity.APP_PACKAGENAME + "=?",
				new String[] { "" + DownloadTask.TASK_LAUNCH, packageName });
	}
}
