package com.x.db.favorite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.x.db.dao.FavoriteApp;
import com.x.publics.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 
 *
 */
public class FavoriteDBHelper {
	public DBOpenHelper openHelper;
	public static FavoriteDBHelper instance;

	/**
	 * 
	 * @param context
	 */
	public FavoriteDBHelper(Context context) {
		openHelper = DBOpenHelper.getInstance(context);
	}

	/**
	 * 构建实例
	 
	 * @param context
	 * @return
	 */
	public static FavoriteDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new FavoriteDBHelper(context.getApplicationContext());
		}
		return instance;
	}

	/**
	 * 插入数据库
	 
	 * @param ad
	 */
	public boolean insertFavoriteApp(FavoriteApp favoriteApp) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "insert into " + DBOpenHelper.TABLE_FAVORITE_APP + " ( resourceId," + " appId," + " categoryId,"
				+ " appName," + " packageName," + " versionCode," + " versionName," + " iconUrl," + " downloadUrl,"
				+ " starts," + " size," + " status," + " manualDownloadNetwork," + " createTime," + " updateTime,"
				+ " attribute," + " extAttribute1," + " extAttribute2"
				+ " ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			openHelper.OpenDB();
			db = openHelper.getWritableDatabase();
			db.execSQL(
					sql,
					new Object[] { favoriteApp.getFavoriteResourceId(), favoriteApp.getFavoriteAppId(),
							favoriteApp.getFavoriteCategoryId(), favoriteApp.getFavoriteAppName(),
							favoriteApp.getFavoritePackageName(), favoriteApp.getFavoriteVersionCode(),
							favoriteApp.getFavoriteVersionName(), favoriteApp.getFavoriteIconUrl(),
							favoriteApp.getFavoriteDownloadUrl(), favoriteApp.getFavoriteStarts(),
							favoriteApp.getFavoriteSize(), favoriteApp.getFavoriteStatus(),
							favoriteApp.getFavoriteManualDownloadNetwork(), favoriteApp.getFavoriteCreateTime(),
							favoriteApp.getFavoriteUpdateTime(), favoriteApp.getFavoriteAttribute(),
							favoriteApp.getFavoriteExtAttribute1(), favoriteApp.getFavoriteExtAttribute2() });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			openHelper.closeDB(db);
		}
		return true;

	}

	/**
	 * 根据ResoiurceId批量删除FavoriteApp
	 * 
	 
	 * @param List
	 *            <FavoriteApp>
	 */
	public void deleteFavoriteApp(List<FavoriteApp> favoriteAppList) {
		if (favoriteAppList != null && favoriteAppList.size() > 0) {
			for (int i = 0; i < favoriteAppList.size(); i++) {
				deleteFavoriteAppByResourceId(favoriteAppList.get(i).getFavoriteResourceId());
			}
		}

	}

	/**
	 * 删除FavoriteApp
	 * 
	 
	 * @param resourceId
	 */
	public boolean deleteFavoriteApp(String selection) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		if (TextUtils.isEmpty(selection)) {
			return false;
		}
		String sql = "delete from " + DBOpenHelper.TABLE_FAVORITE_APP + " where 1=1 and " + selection;
		try {
			openHelper.OpenDB();
			db = openHelper.getWritableDatabase();
			db.execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			LogUtil.getLogger().d("FavoriteDBHelper deleteFavoriteApp[ sql=" + sql + "]");
			openHelper.closeDB(db);
		}
		return true;
	}

	/**
	 * 根据ResoiurceId删除FavoriteApp
	 * 
	 
	 * @param resourceId
	 */
	public boolean deleteFavoriteAppByResourceId(int resourceId) {
		return deleteFavoriteApp(" resourceId =" + resourceId);
	}

	/**
	 * 根据ResoiurceId修改FavoriteApp的status
	 * 
	 
	 * @param
	 */
	public boolean updateStatusByResourceId(int resourceId, int updateStatus, Long updateTime) {
		String updateSet = "status=" + updateStatus + " , updateTime=" + updateTime;
		String updateCondition = "resourceId=" + resourceId;

		return updateFavoriteApp(updateSet, updateCondition);
	}

	/**
	 * 根据packageName更新downloadUrl,versionName,versionCode
	 * 
	 
	 * @param
	 */
	public boolean updateInfoByPackageName(String packageName, String downloadUrl, String versionName, int versionCode,
			Long updateTime) {
		String updateSet = " downloadUrl='" + downloadUrl + "',versionName ='" + versionName + "',versionCode="
				+ versionCode + ", updateTime=" + updateTime;
		String updateCondition = "packageName='" + packageName + "'";
		return updateFavoriteApp(updateSet, updateCondition);
	}

	/**
	 * 根据ResoiurceId修改FavoriteApp的status
	 * 
	 
	 * @param
	 */
	public boolean updateManualDownloadNetworkByResourceId(int resourceId, String manualDownloadNetwork, Long updateTime) {
		String updateSet = "";
		if (TextUtils.isEmpty(manualDownloadNetwork)) {
			updateSet = "manualDownloadNetwork= '' and updateTime=" + updateTime;
		} else {
			updateSet = "manualDownloadNetwork= '" + manualDownloadNetwork + "' , updateTime=" + updateTime;
		}
		String updateCondition = "resourceId=" + resourceId;

		return updateFavoriteApp(updateSet, updateCondition);
	}

	/**
	 * 根据ResoiurceId修改FavoriteApp的status
	 * 
	 
	 * @param
	 */
	public synchronized boolean updateFavoriteApp(String updateSet, String updateCondition) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update " + DBOpenHelper.TABLE_FAVORITE_APP + " set  " + updateSet + " where 1=1 and "
				+ updateCondition;
		try {
			openHelper.OpenDB();
			db = openHelper.getWritableDatabase();
			db.execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			LogUtil.getLogger().d("FavoriteDBHelper updateFavoriteApp[ sql=" + sql + "]");
			openHelper.closeDB(db);
		}
		return true;
	}

	/**
	 * 根据ResoiurceId查询FavoriteApp
	 * 
	 * @param resourceId
	 * @return
	 */
	public FavoriteApp findFavoriteAppByResourceId(int resourceId) {
		List<FavoriteApp> favoriteAppList = findFavoriteApp("resourceId =" + resourceId);
		if (favoriteAppList != null && favoriteAppList.size() > 0) {
			return favoriteAppList.get(0);
		}
		return null;

	}

	public FavoriteApp findFavoriteAppByPackageName(String packageName) {
		List<FavoriteApp> favoriteAppList = findFavoriteApp("packageName ='" + packageName + "'");
		if (favoriteAppList != null && favoriteAppList.size() > 0) {
			return favoriteAppList.get(0);
		}
		return null;

	}

	/**
	 * 根据ResoiurceId查询FavoriteApp
	 * 
	 * @param resourceId
	 * @return
	 */
	public List<FavoriteApp> findFavoriteAppByStatus(int[] status) {
		String selection = "";

		if (status.length < 0) {
			return null;
		}
		for (int i = 0; i < status.length; i++) {
			selection = selection + status[i] + ",";
			if (status.length - 1 == i) {
				selection = selection.substring(0, selection.length() - 1);
			}
		}

		List<FavoriteApp> favoriteAppList = findFavoriteApp("status in (" + selection + ")");

		return favoriteAppList;

	}

	/**
	 * 根据ResoiurceId查询FavoriteApp
	 * 
	 * @param resourceId
	 * @return
	 */
	public List<FavoriteApp> findAllFavoriteApp() {
		return findFavoriteApp(null);

	}

	/**
	 * 查询FavoriteApp
	 
	 * @param 
	 * @return
	 */
	public List<FavoriteApp> findFavoriteApp(String selection) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = null;
		List<FavoriteApp> favoriteAppList = null;
		if (TextUtils.isEmpty(selection)) {
			selection = " 1=1 ";
		}

		String sql = "select * from " + DBOpenHelper.TABLE_FAVORITE_APP + " where 1=1 and " + selection;
		try {
			openHelper.OpenDB();
			db = openHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[] {});
			favoriteAppList = new ArrayList<FavoriteApp>();
			while (cursor.moveToNext()) {
				FavoriteApp favoriteApp = new FavoriteApp();
				favoriteApp.setFavoriteId(cursor.getInt(cursor.getColumnIndex("id")));
				favoriteApp.setFavoriteResourceId(cursor.getInt(cursor.getColumnIndex("resourceId")));
				favoriteApp.setFavoriteAppId(cursor.getInt(cursor.getColumnIndex("appId")));
				favoriteApp.setFavoriteCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));
				favoriteApp.setFavoriteAppName(cursor.getString(cursor.getColumnIndex("appName")));
				favoriteApp.setFavoritePackageName(cursor.getString(cursor.getColumnIndex("packageName")));
				favoriteApp.setFavoriteVersionCode(cursor.getInt(cursor.getColumnIndex("versionCode")));
				favoriteApp.setFavoriteVersionName(cursor.getString(cursor.getColumnIndex("versionName")));
				favoriteApp.setFavoriteIconUrl(cursor.getString(cursor.getColumnIndex("iconUrl")));
				favoriteApp.setFavoriteDownloadUrl(cursor.getString(cursor.getColumnIndex("downloadUrl")));

				favoriteApp.setFavoriteStarts(cursor.getInt(cursor.getColumnIndex("starts")));
				favoriteApp.setFavoriteSize(cursor.getLong(cursor.getColumnIndex("size")));
				favoriteApp.setFavoriteStatus(cursor.getInt(cursor.getColumnIndex("status")));

				favoriteApp.setFavoriteManualDownloadNetwork(cursor.getString(cursor
						.getColumnIndex("manualDownloadNetwork")));

				favoriteApp.setFavoriteCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
				favoriteApp.setFavoriteUpdateTime(cursor.getLong(cursor.getColumnIndex("updateTime")));

				favoriteApp.setFavoriteAttribute(cursor.getString(cursor.getColumnIndex("attribute"))); // fileType
				favoriteApp.setFavoriteExtAttribute1(cursor.getString(cursor.getColumnIndex("extAttribute1")));
				favoriteApp.setFavoriteExtAttribute2(cursor.getInt(cursor.getColumnIndex("extAttribute2")));
				favoriteAppList.add(favoriteApp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LogUtil.getLogger().d("FavoriteDBHelper findFavoriteApp[ sql=" + sql + "]");
			if (cursor != null) {
				cursor.close();
			}
			openHelper.closeDB(db);
		}
		return favoriteAppList;
	}

	/**
	 * 查询FavoriteApp
	 
	 * @param 
	 * @return
	 */
	public int findFavoriteAppCount() {

		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = null;
		String sql = "select * from " + DBOpenHelper.TABLE_FAVORITE_APP;
		try {
			openHelper.OpenDB();
			db = openHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[] {});
			while (cursor.moveToNext()) {
				return cursor.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LogUtil.getLogger().d("FavoriteDBHelper findFavoriteAppCount[ sql=" + sql + "]");
			if (cursor != null) {
				cursor.close();
			}
			openHelper.closeDB(db);
		}
		return 0;
	}

}
