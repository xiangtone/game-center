/**   
* @Title: DownloadInfoEntity.java
* @Package com.mas.amineappstore.db.dao
* @Description: TODO 

* @date 2013-12-17 上午11:25:51
* @version V1.0   
*/

package com.x.db.dao;

/**
* @ClassName: DownloadInfoEntity
* @Description: TODO 

* @date 2013-12-17 上午11:25:51
* 
*/

public class DownloadInfoEntity implements EntityBase {

	@Override
	public String getCreateTableSql() {

		//		String sql = "CREATE TABLE IF NOT EXISTS " + DOWNLOAD_TABLE_NAME + "(" + ID
		//				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + APP_NAME + " VARCHAR(20) NOT NULL ," + RESOURCE
		//				+ " VARCHAR(30) NOT NULL ," + RESOURCE_ID + " INTEGER NOT NULL," + VERSION + " VARCHAR(50) NOT NULL,"
		//				+ ICON_URL + " VARCHAR(100) NOT NULL," + DOWNLOAD_PATH + " VARCHAR(100) NOT NULL," + LOCAL_PATH
		//				+ " VARCHAR(100) NOT NULL," + MEDIA_TYPE + " VARCHAR(10) NOT NULL," + THREAD_ID
		//				+ " INTEGER DEFAULT '1' NOT NULL," + DOWNLOAD_LENGTH + " VARCHAR(50) NOT NULL ," + TOTAL_SIZE
		//				+ " VARCHAR(50)," + APP_STAUS + " INTEGER DEFAULT '0' NOT NULL, " + APP_PACKAGENAME
		//				+ " VARCHAR(50) NOT NULL ," + CREATE_TIME + " VARCHAR(20) NOT NULL ," + FINISHED_TIME
		//				+ " VARCHAR(20) ," + APP_ID + " INTEGER NOT NULL," + CATEGORY_ID + " INTEGER NOT NULL ," + VERSION_CODE
		//				+ " VARCHAR(20) NOT NULL," + STARTS + " INTEGER NOT NULL," + DOWNLOAD_SPEED + " VARCHAR(20)" + ")";
		String sql = "CREATE TABLE IF NOT EXISTS " + DOWNLOAD_TABLE_NAME + "(" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + APP_NAME + " VARCHAR(20) NOT NULL ," + RESOURCE
				+ " VARCHAR(30) NOT NULL ," + RESOURCE_ID + " INTEGER NOT NULL," + VERSION + " VARCHAR(50) NOT NULL,"
				+ ICON_URL + " VARCHAR(100) NOT NULL," + DOWNLOAD_PATH + " VARCHAR(100) NOT NULL," + LOCAL_PATH
				+ " VARCHAR(100) NOT NULL," + MEDIA_TYPE + " VARCHAR(10) NOT NULL," + THREAD_ID
				+ " INTEGER DEFAULT '1' NOT NULL," + DOWNLOAD_LENGTH + " VARCHAR(50) NOT NULL ," + TOTAL_SIZE
				+ " VARCHAR(50)," + APP_STAUS + " INTEGER DEFAULT '0' NOT NULL, " + APP_PACKAGENAME
				+ " VARCHAR(50) NOT NULL ," + CREATE_TIME + " VARCHAR(20) NOT NULL ," + FINISHED_TIME
				+ " VARCHAR(20) ," + APP_ID + " INTEGER NOT NULL," + CATEGORY_ID + " INTEGER NOT NULL ," + VERSION_CODE
				+ " VARCHAR(20) NOT NULL," + STARTS + " INTEGER NOT NULL," + DOWNLOAD_SPEED + " VARCHAR(20) ,"
				+ ORIGINAL_URL + " VARCHAR(100)," + ATTRIBUTE + " VARCHAR(100)," + EXATTRIBUTE1 + " VARCHAR(100),"
				+ EXATTRIBUTE2 + " INTEGER )";
		return sql;
	}

	@Override
	public String[] getInitTableSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getUpdateTableSql() {
		String oneToTwoSql001 = "ALTER TABLE " + DOWNLOAD_TABLE_NAME + " ADD COLUMN " + ORIGINAL_URL + " VARCHAR(100) ";
		String oneToTwoSql002 = "ALTER TABLE " + DOWNLOAD_TABLE_NAME + " ADD COLUMN " + ATTRIBUTE + " VARCHAR(100) ";
		String oneToTwoSql003 = "ALTER TABLE " + DOWNLOAD_TABLE_NAME + " ADD COLUMN " + EXATTRIBUTE1 + " VARCHAR(100) ";
		String oneToTwoSql004 = "ALTER TABLE " + DOWNLOAD_TABLE_NAME + " ADD COLUMN " + EXATTRIBUTE2 + " INTEGER ";

		String updateSql[] = { oneToTwoSql001, oneToTwoSql002, oneToTwoSql003, oneToTwoSql004 };
		return updateSql;
	}

	/**
	* @Fields APPINFO_TABLE_NAME : table name *download_info* 
	*/
	public static final String DOWNLOAD_TABLE_NAME = "download_info";

	/**
	* @Fields ID : TODO 
	*/
	public static final String ID = "id";

	/**
	* @Fields APP_NAME : TODO 
	*/
	public static final String APP_NAME = "appName";

	/**
	* @Fields RESOURCE : TODO 
	*/
	public static final String RESOURCE = "resource";

	/**
	* @Fields RESOURCE_ID : TODO 
	*/
	public static final String RESOURCE_ID = "resourceId";

	/**
	* @Fields VERSION : TODO 
	*/
	public static final String VERSION = "version";

	/**
	* @Fields ICON_URL : TODO 
	*/
	public static final String ICON_URL = "iconUrl";

	/**
	* @Fields LOCAL_PATH : TODO 
	*/
	public static final String LOCAL_PATH = "localPath";

	/**
	* @Fields DOWNLOAD_PATH : TODO 
	*/
	public static final String DOWNLOAD_PATH = "downloadPath";

	/**
	* @Fields MEDIA_TYPE : TODO 
	*/
	public static final String MEDIA_TYPE = "mediaType";

	/**
	* @Fields THREAD_ID : TODO 
	*/
	public static final String THREAD_ID = "threadId";

	/**
	* @Fields DOWNLOAD_LENGTH : TODO 
	*/
	public static final String DOWNLOAD_LENGTH = "downloadLength";

	/**
	* @Fields TOTAL_SIZE : TODO 
	*/
	public static final String TOTAL_SIZE = "totalSize";

	/**
	* @Fields APP_STAUS : 应用下载状态  
	*/
	public static final String APP_STAUS = "appStasut";

	/**
	* @Fields APP_PACKAGENAME : TODO 
	*/
	public static final String APP_PACKAGENAME = "packageName";

	/**
	* @Fields CREATE_TIME : TODO 
	*/
	public static final String CREATE_TIME = "createTime";

	/**
	* @Fields FINISHED_TIME : TODO 
	*/
	public static final String FINISHED_TIME = "finishedTime";

	/**
	* @Fields DOWNLOAD_SPEED : TODO 
	*/
	public static final String DOWNLOAD_SPEED = "downloadSpeed";

	public static final String APP_ID = "appId";
	public static final String CATEGORY_ID = "categoryId";
	public static final String VERSION_CODE = "versionCode";
	public static final String STARTS = "starts";

	/**
	* @Fields PATCH_URL : 增量更新url 
	*/
	public static final String ORIGINAL_URL = "originalUrl";

	public static final String ATTRIBUTE = "attribute";

	public static final String EXATTRIBUTE1 = "exAttribute1";

	public static final String EXATTRIBUTE2 = "exAttribute2";
}
