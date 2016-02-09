/**   
* @Title: DownloadDbAdapter
* @Package com.mas.amineappstore.db
* @Description: TODO 

* @date 2013-12-17 上午09:49:31
* @version V1.0   
*/

package com.x.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.x.db.dao.EntityBase;

import java.util.ArrayList;

/**
* @ClassName: DownloadDbAdapter
* @Description: TODO 

* @date 2013-12-17 上午09:49:31
* 
*/

public class DownloadDbAdapter {

	private static ArrayList<EntityBase> entityList = null;
	private Context context = null;
	private SQLiteDatabase db = null;
	private myDBHelper dbHelper = null;

	public DownloadDbAdapter(Context context) {

		this.context = context;
		this.dbHelper = new myDBHelper(this.context, DownloadDbConstant.DB_NAME, null, DownloadDbConstant.DB_VERSION);
	}

	public static Object forName(String className, ClassLoader classLoader) {
		try {
			return Class.forName(className, true, classLoader);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		} catch (LinkageError ex) {
			ex.printStackTrace();
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void openAndIntiModeManager(ModeManagerBase... mmbs) {
		try {
			this.db = dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			Log.w("TAG", "openAndIntiModeManager open error:" + e);
			this.db = dbHelper.getReadableDatabase();

		}
		for (ModeManagerBase mmb : mmbs) {
			mmb.setSQLiteDatabase(this.db);
		}

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}

	public void close() {
		this.db.close();
	}

	private void initEntityList() {
		if (entityList == null) {
			if (DownloadDbConstant.ENTITYS.length > 0) {
				entityList = new ArrayList<EntityBase>();
			}
			ClassLoader cl = myDBHelper.class.getClassLoader();
			for (int i = 0, j = DownloadDbConstant.ENTITYS.length; i < j; i++) {
				try {
					Object entityobj = cl.loadClass(DownloadDbConstant.ENTITYS[i].trim()).newInstance();
					entityList.add((EntityBase) entityobj);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class myDBHelper extends SQLiteOpenHelper {

		public myDBHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			initEntityList();
			for (int i = 0, j = entityList.size(); i < j; i++) {
				EntityBase eb = entityList.get(i);
				_db.execSQL(eb.getCreateTableSql());
				String[] initsqls = eb.getInitTableSql();
				if (initsqls == null) {
					continue;
				}
				for (int i2 = 0, j2 = initsqls.length; i2 < j2; i2++) {
					_db.execSQL(initsqls[i2]);
				}
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			if (newVersion > oldVersion) {
				initEntityList();
				for (int i = 0, j = entityList.size(); i < j; i++) {
					EntityBase eb = entityList.get(i);
					String[] updatesqls = eb.getUpdateTableSql();
					if (updatesqls == null) {
						continue;
					}
					for (int i2 = 0, j2 = updatesqls.length; i2 < j2; i2++) {
						try {
							_db.execSQL(updatesqls[i2]);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
