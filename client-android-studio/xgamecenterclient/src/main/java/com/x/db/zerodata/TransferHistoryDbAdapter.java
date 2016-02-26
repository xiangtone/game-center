/**   
* @Title: TransferHistoryDbAdapter.java
* @Package com.x.db.zerodata
* @Description: TODO 

* @date 2014-3-31 下午06:42:38
* @version V1.0   
*/

package com.x.db.zerodata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.x.db.ModeManagerBase;
import com.x.db.dao.EntityBase;

import java.util.ArrayList;

/**
* @ClassName: TransferHistoryDbAdapter
* @Description: TODO 

* @date 2014-3-31 下午06:42:38
* 
*/

public class TransferHistoryDbAdapter {

	private static ArrayList<EntityBase> entityList = null;
	private Context context = null;
	private SQLiteDatabase db = null;
	private myDBHelper dbHelper = null;

	public TransferHistoryDbAdapter(Context context) {

		this.context = context;
		this.dbHelper = new myDBHelper(this.context, TransferHistoryConstant.DB_NAME, null,
				TransferHistoryConstant.DB_VERSION);
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
			if (TransferHistoryConstant.ENTITYS.length > 0) {
				entityList = new ArrayList<EntityBase>();
			}
			ClassLoader cl = myDBHelper.class.getClassLoader();
			for (int i = 0, j = TransferHistoryConstant.ENTITYS.length; i < j; i++) {
				try {
					Object entityobj = cl.loadClass(TransferHistoryConstant.ENTITYS[i].trim()).newInstance();
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
						_db.execSQL(updatesqls[i2]);
					}
				}
			}
		}
	}
}
