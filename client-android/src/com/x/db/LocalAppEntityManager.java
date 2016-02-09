/**   
* @Title: LocalAppEntityManager.java
* @Package com.mas.amineappstore.db
* @Description: TODO 

* @date 2013-12-17 上午10:02:15
* @version V1.0   
*/

package com.x.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.x.business.localapp.sort.PinYin;
import com.x.db.dao.LocalAppInfoEntity;
import com.x.publics.model.InstallAppBean;
import com.x.publics.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* @ClassName: LocalAppEntityManager
* @Description: 本地应用数据库管理 

* @date 2013-12-17 上午10:02:15
* 
*/

public class LocalAppEntityManager extends ModeManagerBase {

	private static LocalAppEntityManager AInstance;

	private LocalAppEntityManager() {

	}

	public static LocalAppEntityManager getInstance() {
		if (AInstance == null)
			AInstance = new LocalAppEntityManager();

		return AInstance;
	}

	public void close() {
		_db.close();
	}

	private ContentValues getValues(InstallAppBean appInfo) {
		ContentValues values = new ContentValues();
		values.put(LocalAppInfoEntity.APP_NAME, appInfo.getAppName());
		values.put(LocalAppInfoEntity.APP_PACKAGE_NAME, appInfo.getPackageName());
		values.put(LocalAppInfoEntity.APP_SIZE, appInfo.getFileSize());
		values.put(LocalAppInfoEntity.APP_SOURCE_DIR, appInfo.getSourceDir());
		values.put(LocalAppInfoEntity.APP_VERSION_CODE, appInfo.getVersionCode());
		values.put(LocalAppInfoEntity.APP_VERSION_NAME,
				appInfo.getVersionName() == null ? "" : appInfo.getVersionName());
		values.put(LocalAppInfoEntity.APP_TARNS_FLAG, appInfo.getTransFlag());
		values.put(LocalAppInfoEntity.APP_SYSTEM_FALG, appInfo.getSysFlag());
		return values;
	}

	/** 
	* @Title: getAllApps 
	* @Description: 获取所有App列表
	* @param @return     
	* @return List<InstallAppBean>    
	* @throws 
	*/

	public List<InstallAppBean> getAllApps() {
		return findApps(null, null);
	}

	/** 
	* @Title: getAllAppsMap 
	* @Description: 获取所有本地App  HashMap<packageName, InstallAppBean>
	* @param @return     
	* @return HashMap<String,InstallAppBean>    
	* @throws 
	*/

	public HashMap<String, InstallAppBean> getAllAppsMap() {
		HashMap<String, InstallAppBean> result = new HashMap<String, InstallAppBean>();
		List<InstallAppBean> appList = getAllApps();
		for (InstallAppBean appBean : appList) {
			result.put(appBean.getPackageName(), appBean);
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	/** 
	* @Title: getAllLocalApps 
	* @Description: 获取所有本地非系统App 
	* @param @return     
	* @return List<InstallAppBean>    
	* @throws 
	*/

	public List<InstallAppBean> getAllLocalApps() {
		String selectionArgs[] = { "0" };
		String selection = LocalAppInfoEntity.APP_SYSTEM_FALG + " = ?" ;
		return findApps(selection, selectionArgs);
	}
	
	public List<InstallAppBean> findApps(String selection, String[] selectionArgs) {
		List<InstallAppBean> result = new ArrayList<InstallAppBean>();
		Cursor cursor = null;
		try {
			cursor = _db.query(LocalAppInfoEntity.APPINFO_TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
			while (cursor.moveToNext()) {
				InstallAppBean app = new InstallAppBean(cursor.getInt(cursor.getColumnIndex(LocalAppInfoEntity._ID)),
						cursor.getString(cursor.getColumnIndex(LocalAppInfoEntity.APP_NAME)), cursor.getString(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_PACKAGE_NAME)), cursor.getString(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_VERSION_NAME)), cursor.getInt(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_VERSION_CODE)), cursor.getString((cursor
								.getColumnIndex(LocalAppInfoEntity.APP_SIZE))), cursor.getString(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_SOURCE_DIR)), cursor.getInt(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_TARNS_FLAG)), cursor.getInt(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_SYSTEM_FALG)), Utils.getPictureDraw(cursor
								.getBlob(cursor.getColumnIndex(LocalAppInfoEntity.APP_ICON))));
				try {
					//汉字转换成拼音
					String pinyin = PinYin.getPinYin(app.getAppName());
					String sortString = pinyin.substring(0, 1);
					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						app.setSortLetters(sortString.toUpperCase());
					} else {
						app.setSortLetters("#");
					}
				} catch (Exception e) {
					e.printStackTrace();
					app.setSortLetters("#");
				}
				result.add(app);
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
	* @Title: getLocalAppBySourceDir 
	* @Description: 根据源路径查询App 
	* @param @param dir
	* @param @return     
	* @return InstallAppBean    
	*/

	public InstallAppBean getLocalAppBySourceDir(String dir) {
		final String selectionArgs[] = { dir };
		String selection = LocalAppInfoEntity.APP_SOURCE_DIR + " = ?";
		return findApp(selection, selectionArgs);
	}

	/** 
	* @Title: getLocalAppByPackageName 
	* @Description: 根据应用名查询App  
	* @param @param packageName
	* @param @return     
	* @return InstallAppBean    
	*/
	public InstallAppBean getLocalAppByAppName(String appName) {
		final String selectionArgs[] = { appName };
		String selection = LocalAppInfoEntity.APP_NAME + " = ?";
		return findApp(selection, selectionArgs);
	}

	/** 
	* @Title: getLocalAppByPackageName 
	* @Description: 根据包名查询App  
	* @param @param packageName
	* @param @return     
	* @return InstallAppBean    
	*/

	public InstallAppBean getLocalAppByPackageName(String packageName) {
		final String selectionArgs[] = { packageName };
		String selection = LocalAppInfoEntity.APP_PACKAGE_NAME + " = ?";
		return findApp(selection, selectionArgs);
	}
	
	/** 
	* @Title: isSystemApp 
	* @Description: 是否预装应用 
	* @param @param packageName
	* @param @return    
	* @return boolean    
	*/ 
	
	public boolean isSystemApp(String packageName){
		InstallAppBean installAppBean = getLocalAppByPackageName(packageName);
		if(installAppBean != null && installAppBean.getSysFlag() == 1)
			return true;
		return false;
	}

	public InstallAppBean findApp(String selection, String[] selectionArgs) {
		InstallAppBean installAppBean = null;
		Cursor cursor = null;
		try {
			cursor = _db.query(LocalAppInfoEntity.APPINFO_TABLE_NAME, null, selection, selectionArgs, null, null, null);
			while (cursor.moveToNext()) {
				installAppBean = new InstallAppBean(cursor.getInt(cursor.getColumnIndex(LocalAppInfoEntity._ID)),
						cursor.getString(cursor.getColumnIndex(LocalAppInfoEntity.APP_NAME)), cursor.getString(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_PACKAGE_NAME)), cursor.getString(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_VERSION_NAME)), cursor.getInt(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_VERSION_CODE)), cursor.getString((cursor
								.getColumnIndex(LocalAppInfoEntity.APP_SIZE))), cursor.getString(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_SOURCE_DIR)), cursor.getInt(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_TARNS_FLAG)), cursor.getInt(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_SYSTEM_FALG)), Utils.getPictureDraw(cursor
								.getBlob(cursor.getColumnIndex(LocalAppInfoEntity.APP_ICON))));
				
				try {
					//汉字转换成拼音
					String pinyin = PinYin.getPinYin(installAppBean.getAppName());
					String sortString = pinyin.substring(0, 1);
					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						installAppBean.setSortLetters(sortString.toUpperCase());
					} else {
						installAppBean.setSortLetters("#");
					}
				} catch (Exception e) {
					e.printStackTrace();
					installAppBean.setSortLetters("#");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return installAppBean;
	}

	/** 
	* @Title: getAllMovedApps 
	* @Description: 获取可移动app列表 
	* @param @return     
	* @return List<InstallAppBean>    
	* @throws 
	*/

	public List<InstallAppBean> getAllMovedApps() {
		List<InstallAppBean> result = new ArrayList<InstallAppBean>();
		final String selectArgs[] = { "0" };
		Cursor cursor = null;
		try {
			cursor = _db.query(LocalAppInfoEntity.APPINFO_TABLE_NAME, null, LocalAppInfoEntity.APP_TARNS_FLAG + " = ?",
					selectArgs, null, null, null);
			while (cursor.moveToNext()) {
				InstallAppBean app = new InstallAppBean(cursor.getInt(cursor.getColumnIndex(LocalAppInfoEntity._ID)),
						cursor.getString(cursor.getColumnIndex(LocalAppInfoEntity.APP_NAME)), cursor.getString(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_PACKAGE_NAME)), cursor.getString(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_VERSION_NAME)), cursor.getInt(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_VERSION_CODE)), cursor.getString(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_SOURCE_DIR)), cursor.getString((cursor
								.getColumnIndex(LocalAppInfoEntity.APP_SIZE))), cursor.getInt(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_TARNS_FLAG)), cursor.getInt(cursor
								.getColumnIndex(LocalAppInfoEntity.APP_SYSTEM_FALG)), Utils.getPictureDraw(cursor
								.getBlob(cursor.getColumnIndex(LocalAppInfoEntity.APP_ICON))));
				result.add(app);
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
	* @Title: addApp 
	* @Description: 保存app信息到本地数据 
	* @param @param appInfo
	* @param @return     
	* @return int    
	* @throws 
	*/

	public int addApp(InstallAppBean appInfo) {
		return (int) _db.insert(LocalAppInfoEntity.APPINFO_TABLE_NAME, null, getValues(appInfo));
	}

	/** 
	* @Title: deleteApp 
	* @Description: 根据包名删除app记录 
	* @param @param packageName
	* @param @return     
	* @return int    
	* @throws 
	*/

	public int deleteApp(String packageName) {
		return (int) _db.delete(LocalAppInfoEntity.APPINFO_TABLE_NAME, LocalAppInfoEntity.APP_PACKAGE_NAME + " = ?",
				new String[] { packageName });
	}

	/** 
	* @Title: updataAppTransflg 
	* @Description: TODO 
	* @param @param pckName
	* @param @param flags
	* @param @return     
	* @return int    
	* @throws 
	*/

	public int updataAppTransflg(String pckName, int flags) {
		ContentValues values = new ContentValues();
		values.put(LocalAppInfoEntity.APP_TARNS_FLAG, flags);
		return (int) _db.update(LocalAppInfoEntity.APPINFO_TABLE_NAME, values, LocalAppInfoEntity.APP_PACKAGE_NAME
				+ " = ?", new String[] { "" + pckName });
	}
	
	/** 
	* @Title: updataAppSysflg 
	* @Description: 更新是否系统应用  
	* @param @param pckName
	* @param @param flags 0安装应用 1 系统应用
	* @param @return    
	* @return int    
	*/ 
	
	public int updataAppSysflg(String pckName, int flags) {
		ContentValues values = new ContentValues();
		values.put(LocalAppInfoEntity.APP_SYSTEM_FALG, flags);
		return (int) _db.update(LocalAppInfoEntity.APPINFO_TABLE_NAME, values, LocalAppInfoEntity.APP_PACKAGE_NAME
				+ " = ?", new String[] { "" + pckName });
	}

	/** 
	* @Title: deleteAllApp 
	* @Description: 删除本地应用表 
	* @param      
	* @return void    
	* @throws 
	*/

	public void deleteAllApp() {
		_db.delete(LocalAppInfoEntity.APPINFO_TABLE_NAME, null, null);
	}

}
