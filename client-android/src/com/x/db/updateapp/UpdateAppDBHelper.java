package com.x.db.updateapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.x.db.dao.UpdateApp;
import com.x.publics.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 
 *
 */
public class UpdateAppDBHelper {
	public DBOpenHelper openHelper;
	public static UpdateAppDBHelper instance;

	/**
	 * 
	 * @param context
	 */
	public UpdateAppDBHelper(Context context) {
		openHelper = DBOpenHelper.getInstance(context);
	}

	/**
	 * 构建实例
	 
	 * @param context
	 * @return
	 */
	public static UpdateAppDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new UpdateAppDBHelper(context.getApplicationContext());
		}
		return instance;
	}

	/**
	 * 插入数据库
	 
	 * @param ad
	 */
	public boolean insertUpdateApp(UpdateApp updateApp) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "insert into " + DBOpenHelper.TABLE_UPDATE_APP + " ( resourceId," + " appId," + " categoryId,"
				+ " appName," + " packageName," + " versionCode," + " versionName," + " oldVersionName," + " iconUrl,"
				+ " downloadUrl," + " starts," + " size," + " status," + " manualDownloadNetwork," + " createTime,"
				+ " updateTime," + " attribute," + " extAttribute1," + " extAttribute2"
				+ " ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			openHelper.OpenDB();
			db = openHelper.getWritableDatabase();
			db.execSQL(
					sql,
					new Object[] { updateApp.getUpdateResourceId(), updateApp.getUpdateAppId(),
							updateApp.getUpdateCategoryId(), updateApp.getUpdateAppName(),
							updateApp.getUpdatePackageName(), updateApp.getUpdateVersionCode(),
							updateApp.getUpdateVersionName(), updateApp.getUpdateOldVersionName(),
							updateApp.getUpdateIconUrl(), updateApp.getUpdateDownloadUrl(),
							updateApp.getUpdateStarts(), updateApp.getUpdateSize(), updateApp.getUpdateStatus(),
							updateApp.getUpdateManualDownloadNetwork(), updateApp.getUpdateCreateTime(),
							updateApp.getUpdateUpdateTime(), updateApp.getUpdateAttribute(),
							updateApp.getUpdateExtAttribute1(), updateApp.getUpdateExtAttribute2() });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			openHelper.closeDB(db);
		}
		return true;

	}

	/**
	 * 根据ResoiurceId批量删除UpdateApp
	 * 
	 
	 * @param List
	 *            <UpdateApp>
	 */
	public void deleteUpdateApp(List<UpdateApp> updateAppList) {
		if (updateAppList != null && updateAppList.size() > 0) {
			for (int i = 0; i < updateAppList.size(); i++) {
				deleteUpdateAppByResourceId(updateAppList.get(i).getUpdateResourceId());
			}
		}

	}

	public boolean deleteAllUpdateApp() {
		return deleteUpdateApp("1 = 1");
	}

	/**
	 * 删除UpdateApp
	 * 
	 
	 * @param resourceId
	 */
	public boolean deleteUpdateApp(String selection) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		if (TextUtils.isEmpty(selection)) {
			return false;
		}
		String sql = "delete from " + DBOpenHelper.TABLE_UPDATE_APP + " where 1=1 and " + selection;
		try {
			openHelper.OpenDB();
			db = openHelper.getWritableDatabase();
			db.execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			LogUtil.getLogger().d("UpdateDBHelper deleteUpdateApp[ sql=" + sql + "]");
			openHelper.closeDB(db);
		}
		return true;
	}

	/**
	 * 根据ResoiurceId删除UpdateApp
	 * 
	 
	 * @param resourceId
	 */
	public boolean deleteUpdateAppByResourceId(int resourceId) {
		return deleteUpdateApp(" resourceId =" + resourceId);
	}

	/**
	 * 根据ResoiurceId删除UpdateApp
	 * 
	 
	 * @param resourceId
	 */
	public boolean deleteUpdateAppByPackageName(String packageName) {
		return deleteUpdateApp(" packageName = '" + packageName + "'");
	}

	/**
	 * 根据ResoiurceId修改UpdateAppApp的status
	 * 
	 
	 * @param
	 */
	public boolean updateStatusByResourceId(int resourceId, int updateStatus, Long updateTime) {
		String updateSet = "status=" + updateStatus + " , updateTime=" + updateTime;
		String updateCondition = "resourceId=" + resourceId;

		return updateUpdateApp(resourceId, updateSet, updateCondition);
	}

	/**
	 * 根据ResoiurceId修改UpdateApp的status
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

		return updateUpdateApp(resourceId, updateSet, updateCondition);
	}

	/**
	 * 根据ResoiurceId修改UpdateApp的status
	 * 
	 
	 * @param
	 */
	public boolean updateUpdateApp(int resourceId, String updateSet, String updateCondition) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		String sql = "update " + DBOpenHelper.TABLE_UPDATE_APP + " set  " + updateSet + " where 1=1 and "
				+ updateCondition;
		try {
			openHelper.OpenDB();
			db = openHelper.getWritableDatabase();
			db.execSQL(sql, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			LogUtil.getLogger().d("UpdateDBHelper updateUpdateApp[ sql=" + sql + "]");
			openHelper.closeDB(db);
		}
		return true;
	}

	/**
	 * 根据ResoiurceId查询UpdateApp
	 * 
	 * @param resourceId
	 * @return
	 */
	public UpdateApp findUpdateAppByResourceId(int resourceId) {
		List<UpdateApp> updateAppList = findUpdateApp("resourceId =" + resourceId);
		if (updateAppList != null && updateAppList.size() > 0) {
			return updateAppList.get(0);
		}
		return null;

	}

	/**
	 * 根据packageName查询UpdateApp
	 * 
	 * @param resourceId
	 * @return
	 */
	public UpdateApp findUpdateAppByPackageName(String packageName) {
		List<UpdateApp> updateAppList = findUpdateApp("packageName ='" + packageName + "'");
		if (updateAppList != null && updateAppList.size() > 0) {
			return updateAppList.get(0);
		}
		return null;

	}
	
	public UpdateApp findUpdateApp(String packageName , int versionCode) {
		List<UpdateApp> updateAppList = findUpdateApp("packageName ='" + packageName + "' and versionCode ="+versionCode);
		if (updateAppList != null && updateAppList.size() > 0) {
			return updateAppList.get(0);
		}
		return null;

	}

	/**
	 * 根据ResoiurceId查询UpdateApp
	 * 
	 * @param resourceId
	 * @return
	 */
	public List<UpdateApp> findUpdateAppByStatus(int[] status) {
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

		List<UpdateApp> updateAppList = findUpdateApp("status in (" + selection + ")");

		return updateAppList;

	}

	/**
	 * 根据ResoiurceId查询UpdateApp
	 * 
	 * @param resourceId
	 * @return
	 */
	public List<UpdateApp> findAllUpdateApp() {
		return findUpdateApp(null);

	}

	/**
	 * 查询UpdateApp
	 
	 * @param 
	 * @return
	 */
	public List<UpdateApp> findUpdateApp(String selection) {
		SQLiteDatabase db = openHelper.getReadableDatabase();
		List<UpdateApp> updateAppList = null;
		Cursor cursor = null ;
		if (TextUtils.isEmpty(selection)) {
			selection = " 1=1 ";
		}

		String sql = "select * from " + DBOpenHelper.TABLE_UPDATE_APP + " where 1=1 and " + selection;
		try {
			openHelper.OpenDB();
			db = openHelper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[] {});
			updateAppList = new ArrayList<UpdateApp>();
			while (cursor.moveToNext()) {
				UpdateApp updateApp = new UpdateApp();
				updateApp.setUpdateId(cursor.getInt(cursor.getColumnIndex("id")));
				updateApp.setUpdateResourceId(cursor.getInt(cursor.getColumnIndex("resourceId")));
				updateApp.setUpdateAppId(cursor.getInt(cursor.getColumnIndex("appId")));
				updateApp.setUpdateCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));
				updateApp.setUpdateAppName(cursor.getString(cursor.getColumnIndex("appName")));
				updateApp.setUpdatePackageName(cursor.getString(cursor.getColumnIndex("packageName")));
				updateApp.setUpdateVersionCode(cursor.getInt(cursor.getColumnIndex("versionCode")));
				updateApp.setUpdateVersionName(cursor.getString(cursor.getColumnIndex("versionName")));
				updateApp.setUpdateOldVersionName(cursor.getString(cursor.getColumnIndex("oldVersionName")));
				updateApp.setUpdateIconUrl(cursor.getString(cursor.getColumnIndex("iconUrl")));
				updateApp.setUpdateDownloadUrl(cursor.getString(cursor.getColumnIndex("downloadUrl")));

				updateApp.setUpdateStarts(cursor.getInt(cursor.getColumnIndex("starts")));
				updateApp.setUpdateSize(cursor.getLong(cursor.getColumnIndex("size")));
				updateApp.setUpdateStatus(cursor.getInt(cursor.getColumnIndex("status")));

				updateApp.setUpdateManualDownloadNetwork(cursor.getString(cursor
						.getColumnIndex("manualDownloadNetwork")));

				updateApp.setUpdateCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
				updateApp.setUpdateUpdateTime(cursor.getLong(cursor.getColumnIndex("updateTime")));

				updateApp.setUpdateAttribute(cursor.getString(cursor.getColumnIndex("attribute")));// 增量更新url
				updateApp.setUpdateExtAttribute1(cursor.getString(cursor.getColumnIndex("extAttribute1")));//追加属性fileType
				updateApp.setUpdateExtAttribute2(cursor.getInt(cursor.getColumnIndex("extAttribute2")));
				updateAppList.add(updateApp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			LogUtil.getLogger().d("UpdateDBHelper findUpdateApp[ sql=" + sql + "]");

			if(cursor !=null )
			{
				cursor.close() ;
			}
			openHelper.closeDB(db);
		}
		return updateAppList;
	}

	/**
	 * 查询UpdateApp
	 
	 * @param 
	 * @return
	 */
	public int findUpdateAppCount() {

		SQLiteDatabase db = openHelper.getReadableDatabase();
		String sql = "select * from " + DBOpenHelper.TABLE_UPDATE_APP;
		Cursor cursor = null ;
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
			LogUtil.getLogger().d("UpdateDBHelper findUpdateAppCount[ sql=" + sql + "]");
			if(cursor !=null )
			 {
				cursor.close() ;
			 }
			openHelper.closeDB(db);
		}
		return 0;
	}

}
