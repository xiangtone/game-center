/**   
* @Title: LocalLocalAppInfoEntity.java
* @Package com.mas.amineappstore.db.dao
* @Description: TODO 

* @date 2013-12-17 上午09:52:17
* @version V1.0   
*/

package com.x.db.dao;

/**
* @ClassName: LocalLocalAppInfoEntity
* @Description: TODO 

* @date 2013-12-17 上午09:52:17
* 
*/

public class LocalAppInfoEntity implements EntityBase {

	@Override
	public String getCreateTableSql() {
		String sql = "CREATE TABLE IF NOT EXISTS  " + APPINFO_TABLE_NAME + " (" + LocalAppInfoEntity._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + LocalAppInfoEntity.APP_NAME + " VARCHAR(30) NOT NULL,"
				+ LocalAppInfoEntity.APP_ICON + " BLOB," + LocalAppInfoEntity.APP_PACKAGE_NAME
				+ " VARCHAR(500)  NOT NULL," + LocalAppInfoEntity.APP_VERSION_NAME + " VARCHAR(50)  NOT NULL,"
				+ LocalAppInfoEntity.APP_VERSION_CODE + " VARCHAR(50)  NOT NULL," + LocalAppInfoEntity.APP_SOURCE_DIR
				+ " VARCHAR(250)  ," + LocalAppInfoEntity.APP_SIZE + " VARCHAR(100)  NOT NULL,"
				+ LocalAppInfoEntity.APP_TARNS_FLAG + " INTEGER  DEFAULT '-1' NOT NULL,"
				+ LocalAppInfoEntity.APP_SYSTEM_FALG + " INTEGER  DEFAULT '0' NOT NULL" + " )";
		return sql;
	}

	@Override
	public String[] getInitTableSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getUpdateTableSql() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 */
	public static final String APPINFO_TABLE_NAME = "local_app_info";

	/**
	 */
	public static final String _ID = "_id";

	/**
	 */
	public static final String APP_NAME = "name";

	/**
	 */
	public static final String APP_PACKAGE_NAME = "package_name";

	/**
	 */
	public static final String APP_ICON = "icon";

	/**
	 */
	public static final String APP_VERSION_NAME = "version_name";

	/**
	 */
	public static final String APP_VERSION_CODE = "version_code";

	/**
	 */
	public static final String APP_SOURCE_DIR = "source_dir";

	/**
	 */
	public static final String APP_SIZE = "size";

	/**
	 */
	public static final String APP_TARNS_FLAG = "trans_flag";

	/**
	 */
	public static final String APP_SYSTEM_FALG = "system_falg";

}
