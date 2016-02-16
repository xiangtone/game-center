/**   
* @Title: TransferHistoryInfoEntity.java
* @Package com.x.db.zerodata
* @Description: TODO 

* @date 2014-3-31 下午06:43:21
* @version V1.0   
*/

package com.x.db.zerodata;

import com.x.db.dao.EntityBase;

/**
* @ClassName: TransferHistoryInfoEntity
* @Description: TODO 

* @date 2014-3-31 下午06:43:21
* 
*/

public class TransferHistoryInfoEntity implements EntityBase {

	@Override
	public String getCreateTableSql() {
		String sql = "CREATE TABLE IF NOT EXISTS " + TRANSFER_HISTORY_TABLE_NAME + "(" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + FILE_RAW_PATH + " VARCHAR(100)," + FILE_SAVE_PATH
				+ " VARCHAR(100)," + FILE_TYPE + " INTEGER," + FILE_SUFFIX + " VARCHAR(10)," + FILE_SIZE + " LONG  ,"
				+ FILE_NAME + " VARCHAR(50)," + FILE_URL + " VARCHAR(50)," + TRANSFER_TYPE + " INTEGER," + CREATE_TIME
				+ " VARCHAR(20)," + FINISH_TIME + " VARCHAR(20)," + ATTRIBUTE + " INTEGER," + EXATTRIBUTE1
				+ " VARCHAR(100), " + EXATTRIBUTE2 + " VARCHAR(100)" + ")";
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
	* @Fields APPINFO_TABLE_NAME : table name *transfer_history* 
	*/
	public static final String TRANSFER_HISTORY_TABLE_NAME = "transfer_history";

	/**
	* @Fields ID : TODO 
	*/
	public static final String ID = "id";

	/**
	* @Fields FILE_RAW_PATH : 文件原始路径 
	*/
	public static final String FILE_RAW_PATH = "fileRawPath";
	/**
	* @Fields FILE_RAW_PATH : 文件保存路径 
	*/
	public static final String FILE_SAVE_PATH = "fileSavePath";
	/**
	* @Fields FILE_TYPE : 文件类型 
	*/
	public static final String FILE_TYPE = "fileType";
	/**
	* @Fields FILE_SUFFIX : 文件后缀 
	*/
	public static final String FILE_SUFFIX = "fileSuffix";
	/**
	* @Fields FILE_SIZE : 文件大小 
	*/
	public static final String FILE_SIZE = "fileSize";
	/**
	* @Fields FILE_NAME : 文件名 
	*/
	public static final String FILE_NAME = "fileName";
	/**
	* @Fields FILE_URL : 文件请求地址 
	*/
	public static final String FILE_URL = "fileUrl";
	/**
	* @Fields TRANSFER_TYPE : 传输类型 接受 /分享 
	*/
	public static final String TRANSFER_TYPE = "transferType";
	/**
	* @Fields CREATE_TIME : TODO 
	*/
	public static final String CREATE_TIME = "createTime";
	/**
	* @Fields FINISH_TIME : TODO 
	*/
	public static final String FINISH_TIME = "finishTime";

	public static final String ATTRIBUTE = "attribute";

	public static final String EXATTRIBUTE1 = "exattribute1";

	public static final String EXATTRIBUTE2 = "exattribute2";

}
