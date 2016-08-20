
package com.hykj.gamecenter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.hykj.gamecenter.utils.Logger;

public class CSACDatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "CSASDatabaseHelper";

	private static final String DB_NAME = "csappstore";

	// 初始版本
	private static final int DB_VERSION_1 = 1;
	// 增加搜索历史记录表
	private static final int DB_VERSION_2 = 2;
	// 增加配置信息
	private static final int DB_VERSION_3 = 3;
	// downloadInfo中增加pack_md5字段，用于验证下载包
	private static final int DB_VERSION_4 = 4;
	// 增加下载历史记录表
	private static final int DB_VERSION_5 = 5;
	// 修改下载记录表
	private static final int DB_VERSION_6 = 6;
	// 统计缓存
	private static final int DB_VERSION_7 = 7;
	//修改下载记录，下载完成记录表
	private static final int DB_VERSION_8 = 8;
	//当前数据版本
	private static final int DB_VERSION = DB_VERSION_8;

	public interface Tables {
		//下载记录表
		String DownloadInfoes = "download_infoes";
		//下载记录表临时表(应用于数据库升级)
		String tempDownloadInfos = "_download_infoes";
		//全局配置表
		String GroupInfo = "group_info";
		//热词表
		String HotWords = "hot_words";
		//已下载历史记录表
		String DownloadedInfoes = "downloaded_infoes";
		//统计缓存表
		String Report = "report_tmp";
	}

	// add 字段 pack_md5 at 20140429
	public interface DownloadInfoColumns {
		String APP_ID = "app_id";
		String LOCAL_PATH = "local_path";
		String APP_URL = "app_url";
		String PACAKGE_NAME = "package_name";
		String APP_NAME = "app_name";
		String APP_RATING = "app_rating";
		String TOTAL_SIZE = "total_size";
		String DOWNLOAD_SIZE = "donwload_size";
		String ICON_URL = "icon_url";
		String STATE = "state";
		String PACK_MD5 = "pack_md5";
		String MFROMPOS = "nFromPos";
		String IS_REAL_DOWNLOAD = "is_real_download";
	}

	public interface GroupInfoColumns {
		String GROUP_ID = "group_id";
		String GROUP_CLASS = "group_class"; //10=应用游戏, 11=应用分类，12=游戏分类，21=网游单机，31=专题，41=推荐，51=分发
		String GROUP_TYPE = "group_type";
		String ORDER_TYPE = "order_type";
		String ORDER_NO = "order_no";
		String RECOMM_WORD = "recomm_word";
		String GROUP_NAME = "group_name";
		String GROUP_DESC = "group_desc";
		String GROUP_PIC_URL = "group_pic_url";
		String START_TIME = "start_time";
		String END_TIME = "end_time";

	}

	public interface HotWortdsColumns {
		String WORDS = "wrods";
	}

	public interface ReportColumns {
		String START_ACTID = "start_act_id";
		String START_ACTID2 = "start_act_id2";
		String ACTION_TIME = "action_time";
		String EXT1 = "ext1";
		String EXT2 = "ext2";
		String EXT3 = "ext3";
		String EXT4 = "ext4";
		String EXT5 = "ext5";
	}

	private static final String CREATE_TABLE_REPORT = "CREATE TABLE IF NOT EXISTS "
			+ Tables.Report + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ ReportColumns.START_ACTID + " INTEGER," + ReportColumns.START_ACTID2 + " INTEGER,"
			+ ReportColumns.ACTION_TIME + " TEXT," + ReportColumns.EXT1 + " TEXT,"
			+ ReportColumns.EXT2 + " TEXT," + ReportColumns.EXT3 + " TEXT,"
			+ ReportColumns.EXT4 + " TEXT," + ReportColumns.EXT5 + ");";

	private static final String CREATE_TABLE_DOWNLOADINOES = "CREATE TABLE IF NOT EXISTS "
			+ Tables.DownloadInfoes + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ DownloadInfoColumns.APP_ID + " INTEGER," + DownloadInfoColumns.LOCAL_PATH + " TEXT,"
			+ DownloadInfoColumns.APP_URL + " TEXT," + DownloadInfoColumns.PACAKGE_NAME + " TEXT,"
			+ DownloadInfoColumns.APP_NAME + " TEXT," + DownloadInfoColumns.APP_RATING
			+ " INTEGER," + DownloadInfoColumns.TOTAL_SIZE + " INTEGER,"
			+ DownloadInfoColumns.DOWNLOAD_SIZE + " INTEGER,"
			+ DownloadInfoColumns.ICON_URL + " TEXT," + DownloadInfoColumns.PACK_MD5 + " TEXT,"
			+ DownloadInfoColumns.STATE + " INTEGER,"
			+ DownloadInfoColumns.IS_REAL_DOWNLOAD + " INTEGER,"
			+ DownloadInfoColumns.MFROMPOS + " INTEGER NOT NULL DEFAULT 0"
			+ ");";

	private static final String CREATE_TABLE_GROUPINFO = "CREATE TABLE IF NOT EXISTS "
			+ Tables.GroupInfo + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ GroupInfoColumns.GROUP_ID
			+ " INTEGER," + GroupInfoColumns.GROUP_CLASS + " INTEGER,"
			+ GroupInfoColumns.GROUP_TYPE + " INTEGER," + GroupInfoColumns.ORDER_TYPE + " INTEGER,"
			+ GroupInfoColumns.ORDER_NO
			+ " INTEGER," + GroupInfoColumns.RECOMM_WORD + " TEXT," + GroupInfoColumns.GROUP_NAME
			+ " TEXT," + GroupInfoColumns.GROUP_DESC + " TEXT," + GroupInfoColumns.GROUP_PIC_URL
			+ " TEXT,"
			+ GroupInfoColumns.START_TIME + " TEXT," + GroupInfoColumns.END_TIME + " TEXT" + ");";

	private static final String CREATE_TABLE_HOTWORDS = "CREATE TABLE IF NOT EXISTS "
			+ Tables.HotWords + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ HotWortdsColumns.WORDS + " TEXT" + ");";

	private static final String CREATE_TABLE_DOWNLOADEDINOES = "CREATE TABLE IF NOT EXISTS "
			+ Tables.DownloadedInfoes + "(" + BaseColumns._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ DownloadInfoColumns.APP_ID + " INTEGER UNIQUE," + DownloadInfoColumns.LOCAL_PATH + " TEXT,"
			+ DownloadInfoColumns.APP_URL + " TEXT," + DownloadInfoColumns.PACAKGE_NAME + " TEXT,"
			+ DownloadInfoColumns.APP_NAME + " TEXT," + DownloadInfoColumns.APP_RATING
			+ " INTEGER," + DownloadInfoColumns.TOTAL_SIZE + " INTEGER,"
			+ DownloadInfoColumns.DOWNLOAD_SIZE + " INTEGER,"
			+ DownloadInfoColumns.ICON_URL + " TEXT," + DownloadInfoColumns.PACK_MD5 + " TEXT,"
			+ DownloadInfoColumns.STATE + " INTEGER,"
			+ DownloadInfoColumns.IS_REAL_DOWNLOAD + " INTEGER,"
			+ DownloadInfoColumns.MFROMPOS + " INTEGER NOT NULL DEFAULT 0"
			+ ");";

	private static final String DELETE_DOWNLOAD_INFO_TABLE = "DROP TABLE IF EXISTS "
			+ Tables.DownloadInfoes + ";";

	private static final String DELETE_DOWNLOADED_INFO_TABLE = "DROP TABLE IF EXISTS "
			+ Tables.DownloadedInfoes + ";";

	private static final String DELETE_REPORT_TABLE = "DROP TABLE IF EXISTS "
			+ Tables.Report +";";
	private static final String DELETE_HOTWORDS_TABLE = "DROP TABLE IF EXISTS "
			+ Tables.HotWords +";";
	private static final String DELETE_GROUPINFO_TABLE = "DROP TABLE IF EXISTS "
			+ Tables.GroupInfo +";";

	public CSACDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Logger.i(TAG, "onCreate");
		db.execSQL(CREATE_TABLE_DOWNLOADINOES);
		db.execSQL(CREATE_TABLE_GROUPINFO);
		db.execSQL(CREATE_TABLE_HOTWORDS);
		db.execSQL(CREATE_TABLE_DOWNLOADEDINOES);
		db.execSQL(CREATE_TABLE_REPORT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//数据库修改：从1.0升级到2.0操作（增加配置信息（增加groupinfo和SplashImageInfo表），删除了搜索历史记录表）
		Logger.i(TAG, "oldVersion:" + oldVersion);
		Logger.i(TAG, "newVersion:" + newVersion);
		//newVersion == 7
		switch (oldVersion) {
			case DB_VERSION_1:
			case DB_VERSION_2:
				//to3
				db.execSQL(CREATE_TABLE_GROUPINFO); //创建配置表
				//to4
				db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
				//to5
//				db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加下载历史记录表
				//to6
//				db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
				db.execSQL(DELETE_DOWNLOADED_INFO_TABLE); //删除已下载任务记录表
//				db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加已下载历史记录表
				//to7
				db.execSQL(CREATE_TABLE_REPORT);
				//to8
				break;
			case DB_VERSION_3:
				//to4
				db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
				//to5
//				db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加下载历史记录表
//				db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
				db.execSQL(DELETE_DOWNLOADED_INFO_TABLE); //删除已下载任务记录表
//				db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加已下载历史记录表
				db.execSQL(CREATE_TABLE_HOTWORDS);
				//to7
				db.execSQL(CREATE_TABLE_REPORT);
				//to8
				break;
			case DB_VERSION_4:
				//to5
//				db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加下载历史记录表
				//to6
				db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
				db.execSQL(DELETE_DOWNLOADED_INFO_TABLE); //删除已下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加已下载历史记录表
				//新加hotword数据库
				db.execSQL(CREATE_TABLE_HOTWORDS);
				//to7
				db.execSQL(CREATE_TABLE_REPORT);
				break;
			case DB_VERSION_5:
				//to6
				db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
				db.execSQL(DELETE_DOWNLOADED_INFO_TABLE); //删除已下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加已下载历史记录表
				//新加hotword数据库
				db.execSQL(CREATE_TABLE_HOTWORDS);
				//to7
				db.execSQL(CREATE_TABLE_REPORT);
				break;
			case DB_VERSION_6:
				//to7
				db.execSQL(CREATE_TABLE_REPORT);
				//to8
				db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
				db.execSQL(DELETE_DOWNLOADED_INFO_TABLE); //删除已下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加已下载历史记录表
				break;
			case DB_VERSION_7:
				//to8
				db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
				db.execSQL(DELETE_DOWNLOADED_INFO_TABLE); //删除已下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
				db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加已下载历史记录表
				break;
		}

	}

	/**
	 * Called when the database needs to be downgraded. This is strictly similar to
	 * {@link #onUpgrade} method, but is called whenever current version is newer than requested one.
	 * However, this method is not abstract, so it is not mandatory for a customer to
	 * implement it. If not overridden, default implementation will reject downgrade and
	 * throws SQLiteException
	 * <p/>
	 * <p>
	 * This method executes within a transaction.  If an exception is thrown, all changes
	 * will automatically be rolled back.
	 * </p>
	 *
	 * @param db         The database.
	 * @param oldVersion The old database version.
	 * @param newVersion The new database version.
	 */
//	@Override
//	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		//oldVersion == 7
//		switch (newVersion) {
//			case DB_VERSION_1:
//			case DB_VERSION_2:
//				db.execSQL(DELETE_GROUPINFO_TABLE);
//			case DB_VERSION_3:
//				db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
//			case DB_VERSION_4:
//				db.execSQL(DELETE_DOWNLOADED_INFO_TABLE); //删除已下载任务记录表
//			case DB_VERSION_5:
//				db.execSQL(DELETE_HOTWORDS_TABLE);
//				db.execSQL(DELETE_DOWNLOADED_INFO_TABLE);
//			case DB_VERSION_6:
//				db.execSQL(DELETE_REPORT_TABLE);
//		}
//	}

	private void upgradeDBTo3(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_GROUPINFO); //创建配置表
	}

	// add at 20140429
	private void upgradeDBTo4(SQLiteDatabase db) {
		db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
		db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
	}

	// add at 20140429
	private void upgradeDBTo5(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加下载历史记录表
	}

	// add at 20151127
	private void upgradeDBTo6(SQLiteDatabase db) {
		db.execSQL(DELETE_DOWNLOAD_INFO_TABLE); //删除下载任务记录表
		db.execSQL(DELETE_DOWNLOADED_INFO_TABLE); //删除已下载任务记录表
		db.execSQL(CREATE_TABLE_DOWNLOADINOES);//创建下载任务记录表
		db.execSQL(CREATE_TABLE_DOWNLOADEDINOES); //增加已下载历史记录表
		//新加hotword数据库
		db.execSQL(CREATE_TABLE_HOTWORDS);
	}

	// add at 20151230
	private void upgradeDBTo7(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_REPORT);
	}

}
