package com.x.db.applocker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.x.db.dao.CommonLockerApp;
import com.x.publics.utils.LogUtil;
import com.x.publics.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 
 * 
 */
public class AppLockerDBHelper {
	public DBOpenHelper openHelper;
	public static AppLockerDBHelper instance;

	/**
	 * 
	 * @param context
	 */
	public AppLockerDBHelper(Context context) {
		openHelper = DBOpenHelper.getInstance(context);
	}

	/**
	 * 构建实例
	 * 
	 
	 * @param context
	 * @return
	 */
	public static AppLockerDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new AppLockerDBHelper(context.getApplicationContext());
		}
		return instance;
	}

	/**
	 * 插入数据库
	 * 
	 
	 * @param ad
	 */
	public boolean insertCommonLocker(CommonLockerApp commonLockerApp) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "insert into " + DBOpenHelper.TABLE_COMMON_LOCKER
				+ " ( packageName," + "appName," + " status," + " sortType,"+"activityName,"
				+ " isLocked,"+"isGroupApp," + " isEnable," + " createTime," + " updateTime,"
				+ " attribute," + " extAttribute1," + " extAttribute2"
				+ " ) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			openHelper.OpenDB();
			db = openHelper.getWritableDatabase();
			db.execSQL(
					sql,
					new Object[] { commonLockerApp.getPackageName(),
							commonLockerApp.getAppName(),
							commonLockerApp.getStatus(),
							commonLockerApp.getSortType(),
							commonLockerApp.getActivityName(),
							commonLockerApp.isLocked() == true ? 1 : 0,
							commonLockerApp.isGroupApp() == true ? 1 : 0,
							commonLockerApp.isEnable() == true ? 1 : 0,
							System.currentTimeMillis(),
							commonLockerApp.getUpdateTime(),
							commonLockerApp.getAttribute(),
							commonLockerApp.getExtAttribute1(),
							commonLockerApp.getExtAttribute2() });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			openHelper.closeDB(db);
		}
		return true;

	}

	/**
	 * 根据ResoiurceId批量删除CommonLockerApp
	 * 
	 
	 * @param List
	 *            <CommonLockerApp>
	 */
	public void deleteCommonLockerApp(List<CommonLockerApp> commonLockerAppList) {
		if (commonLockerAppList != null && commonLockerAppList.size() > 0) {
			for (int i = 0; i < commonLockerAppList.size(); i++) {
				deleteCommonLockerAppByPackageName(commonLockerAppList.get(i)
						.getPackageName());
			}
		}

	}

	/**
	 * 根据ResoiurceId批量删除CommonLockerApp
	 * 
	 
	 * @param List
	 *            <CommonLockerApp>
	 */
	public void deleteLockerAppByPackageName(String packageName)
	{
		deleteCommonLockerAppByPackageName(packageName);
	}
	/**
	 * 删除CommonLockerApp
	 * 
	 
	 * @param resourceId
	 */
	public boolean deleteCommonLockerApp(String selection) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		if (TextUtils.isEmpty(selection)) {
			return false;
		}
		String sql = "delete from " + DBOpenHelper.TABLE_COMMON_LOCKER
				+ " where 1=1 and " + selection;
		try {
			openHelper.OpenDB();
			db = openHelper.getWritableDatabase();
			db.execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			LogUtil.getLogger()
					.d("AppLockerDBHelper deleteCommonLockerApp[ sql=" + sql
							+ "]");
			openHelper.closeDB(db);
		}
		return true;
	}

	/**
	 * 根据ResoiurceId删除CommonLockerApp
	 * 
	 
	 * @param resourceId
	 */
	public boolean deleteCommonLockerAppByPackageName(String packageName) {
		return deleteCommonLockerApp(" packageName ='" + packageName + "'");
	}


	public boolean updateSetLocked(String packageName) {
		String updateSet = "isLocked =1 , updateTime="
				+ System.currentTimeMillis();
		String updateCondition = "packageName='" + packageName + "'";
		return updateCommonLockerApp(updateSet, updateCondition);
	}

	public boolean updateSetUnLocked(String packageName) {
		String updateSet = "isLocked =0 , updateTime="
				+ System.currentTimeMillis();
		String updateCondition = "packageName='" + packageName + "'";
		return updateCommonLockerApp(updateSet, updateCondition);
	}
	
	/**
	 * 根据ResoiurceId修改CommonLockerApp的status
	 * 
	 
	 * @param
	 */
	public synchronized boolean updateCommonLockerApp(String updateSet,
			String updateCondition) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update " + DBOpenHelper.TABLE_COMMON_LOCKER + " set  "
				+ updateSet + " where 1=1 and " + updateCondition;
		try {
			openHelper.OpenDB();
			db = openHelper.getWritableDatabase();
			db.execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			LogUtil.getLogger()
					.d("AppLockerDBHelper updateCommonLockerApp[ sql=" + sql
							+ "]");
			openHelper.closeDB(db);
		}
		return true;
	}

	

	public CommonLockerApp findCommonLockerAppByPackageName(String packageName) {
		List<CommonLockerApp> CommonLockerAppList = findCommonLockerApp("packageName ='"
				+ packageName + "'");
		if (CommonLockerAppList != null && CommonLockerAppList.size() > 0) {
			return CommonLockerAppList.get(0);
		}
		return null;

	}

	

	/**
	 * 根据ResoiurceId查询CommonLockerApp
	 * 
	 * @param resourceId
	 * @return
	 */
	public List<CommonLockerApp> findAllCommonLockerApp() {
		return findCommonLockerApp(null);

	}

	/**
	 * 查询CommonLockerApp
	 * 
	 
	 * @param
	 * @return
	 */
	public List<CommonLockerApp> findCommonLockerApp(String selection) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = null;
		List<CommonLockerApp> commonLockerAppList = null;
		if (TextUtils.isEmpty(selection)) {
			selection = " 1=1 ";
		}
		String sql = "select * from " + DBOpenHelper.TABLE_COMMON_LOCKER
				+ " where 1=1 and " + selection;
		try {
			openHelper.OpenDB();
			db = openHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[] {});
			commonLockerAppList = new ArrayList<CommonLockerApp>();
			while (cursor.moveToNext()) {
				CommonLockerApp commonLockerApp = new CommonLockerApp();
				commonLockerApp
						.setId(cursor.getInt(cursor.getColumnIndex("id")));
				commonLockerApp.setPackageName(cursor.getString(cursor
						.getColumnIndex("packageName")));
				commonLockerApp.setAppName(cursor.getString(cursor
						.getColumnIndex("appName")));
				commonLockerApp.setStatus(cursor.getInt(cursor
						.getColumnIndex("status")));
				commonLockerApp.setSortType(cursor.getInt(cursor
						.getColumnIndex("sortType")));
				commonLockerApp.setActivityName(cursor.getString(cursor
						.getColumnIndex("activityName")));
				commonLockerApp.isLocked = cursor.getInt(cursor
						.getColumnIndex("isLocked")) == 1 ? true : false;
				commonLockerApp.isGroupApp = cursor.getInt(cursor
						.getColumnIndex("isGroupApp")) == 1 ? true : false;
				commonLockerApp.isEnable = cursor.getInt(cursor
						.getColumnIndex("isEnable")) == 1 ? true : false;
				commonLockerApp.setCreateTime(cursor.getLong(cursor
						.getColumnIndex("createTime")));
				commonLockerApp.setUpdateTime(cursor.getLong(cursor
						.getColumnIndex("updateTime")));
				commonLockerApp.setAttribute(cursor.getString(cursor
						.getColumnIndex("attribute")));
				commonLockerApp.setExtAttribute1(cursor.getString(cursor
						.getColumnIndex("extAttribute1")));
				commonLockerApp.setExtAttribute2(cursor.getInt(cursor
						.getColumnIndex("extAttribute2")));
				commonLockerAppList.add(commonLockerApp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LogUtil.getLogger().d(
					"AppLockerDBHelper findCommonLockerApp[ sql=" + sql + "]");
			if (cursor != null) {
				cursor.close();
			}
			openHelper.closeDB(db);
		}
		return commonLockerAppList;
	}

	/**
	 * 查询CommonLockerApp
	 * 
	 
	 * @param
	 * @return
	 */
	public int findCommonLockerAppCount() {

		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = null;
		String sql = "select * from " + DBOpenHelper.TABLE_COMMON_LOCKER
				+ " where isLocked = 1 ";
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
			LogUtil.getLogger().d(
					"AppLockerDBHelper findCommonLockerAppCount[ sql=" + sql
							+ "]");
			if (cursor != null) {
				cursor.close();
			}
			openHelper.closeDB(db);
		}
		return 0;
	}
	/**
	 * 查询CommonLockerApp
	 * 
	 
	 * @param
	 * @return
	 */
	public List<String> findLockeredApp() {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		Cursor cursor = null;
		List<String> lockeredAppList = null;
		
		String sql = "select * from " + DBOpenHelper.TABLE_COMMON_LOCKER
				+ " where 1=1 and isLocked = 1 ";
		try {
			openHelper.OpenDB();
			db = openHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[] {});
			lockeredAppList = new ArrayList<String>();
			while (cursor.moveToNext()) {
				String packageName = cursor.getString(cursor.getColumnIndex("packageName"));
				lockeredAppList.add(packageName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LogUtil.getLogger().d(
					"AppLockerDBHelper findLockeredApp[ sql=" + sql + "]");
			if (cursor != null) {
				cursor.close();
			}
			openHelper.closeDB(db);
		}
		return lockeredAppList;
	}
}
