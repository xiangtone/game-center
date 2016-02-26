package com.x.db.updateapp ;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库helper类
 
 *
 */
public class DBOpenHelper extends SQLiteOpenHelper {
	
	private static final String DBNAME = "updateapp.db";
	public static final String TABLE_UPDATE_APP = "updateApp";
	private static final int VERSION = 1;
	private static DBOpenHelper openHelper;
	
	
	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}
	
	/**
	 * 构建实例
	 
	 * @param context
	 * @return
	 */
	public static DBOpenHelper getInstance(Context context) {
		if(openHelper==null)
		{
			openHelper = new DBOpenHelper(context.getApplicationContext());
		}
		return openHelper;
	}

	/**
	 * 创建数据库表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_UPDATE_APP+" (id integer primary key autoincrement, " +
				"resourceId INTEGER," +
				"appId INTEGER," +
				"categoryId INTEGER," +
				"appName varchar(100)," +
				"packageName varchar(100)," +
				"versionCode INTEGER," +
				"versionName varchar(100)," +
				"oldVersionName varchar(100)," +
				"iconUrl varchar(100)," +
				"downloadUrl varchar(100)," +
				"starts varchar(100)," +
				"size INTEGER," +
				"manualDownloadNetwork varchar(100)," +
				"status INTEGER," +
				"createTime Long," +
				"updateTime Long," +
				"attribute varchar(100),"+
				"extAttribute1 varchar(100),"+
				"extAttribute2 INTEGER)");
	}

	/**
	 * 升级数据库
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_UPDATE_APP);
		onCreate(db);
	}
	
	int index = 0;
	public void OpenDB() {
		index++;
	}
	
	/**
	 * 关闭数据库
	 * @param db
	 */
	public void closeDB(SQLiteDatabase db) {
		if(index>1)
		{
			index--;
		}
		else
		{
			index = 0;
			if(db==null)
			{
				db = getWritableDatabase();
			}
			if(db.isOpen())
			{
				db.close();
			}
			db = null;
		}
	}
	
	/**
	 * 强制关闭数据库
	 */
	public void forceCloseDB() {
		index = 0;
		SQLiteDatabase db = getWritableDatabase();
		db.close();
		db = null;
	}

}
