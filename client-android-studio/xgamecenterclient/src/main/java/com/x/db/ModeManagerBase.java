package com.x.db;

import android.database.sqlite.SQLiteDatabase;

public abstract class ModeManagerBase {
	protected SQLiteDatabase _db = null;

	public void setSQLiteDatabase(SQLiteDatabase db) {
		this._db = db;
	}
}
