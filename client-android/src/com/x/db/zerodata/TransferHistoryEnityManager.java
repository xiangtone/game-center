/**   
* @Title: TransferHistoryEnityManager.java
* @Package com.mas.amineappstore.db.zerodata
* @Description: TODO 

* @date 2014-3-31 下午06:43:02
* @version V1.0   
*/

package com.x.db.zerodata;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.x.db.ModeManagerBase;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: TransferHistoryEnityManager
* @Description: TODO 

* @date 2014-3-31 下午06:43:02
* 
*/

public class TransferHistoryEnityManager extends ModeManagerBase {

	private static TransferHistoryEnityManager AInstance;

	private TransferHistoryEnityManager() {

	}

	public static TransferHistoryEnityManager getInstance() {
		if (AInstance == null)
			AInstance = new TransferHistoryEnityManager();

		return AInstance;
	}

	public void close() {
		_db.close();
	}

	private ContentValues getValues(TransferHistory transferHistory) {
		ContentValues values = new ContentValues();
		values.put(TransferHistoryInfoEntity.FILE_RAW_PATH, transferHistory.getFileRawPath());
		values.put(TransferHistoryInfoEntity.FILE_SAVE_PATH, transferHistory.getFileSavePath());
		values.put(TransferHistoryInfoEntity.FILE_TYPE, transferHistory.getFileType());
		values.put(TransferHistoryInfoEntity.FILE_SUFFIX, transferHistory.getFileSuffix());
		values.put(TransferHistoryInfoEntity.FILE_SIZE, transferHistory.getFileSize());
		values.put(TransferHistoryInfoEntity.FILE_NAME, transferHistory.getFileName());
		values.put(TransferHistoryInfoEntity.FILE_URL, transferHistory.getFileUrl());
		values.put(TransferHistoryInfoEntity.TRANSFER_TYPE, transferHistory.getTransferType());
		values.put(TransferHistoryInfoEntity.CREATE_TIME, transferHistory.getCreateTime());
		values.put(TransferHistoryInfoEntity.FINISH_TIME, transferHistory.getFinishTime());
		return values;
	}

	/** 
	* @Title: save 
	* @Description: 插入一条数据库 
	* @param @param transferHistory     
	* @return void    
	*/

	public synchronized long save(TransferHistory transferHistory) {
		try {
			return _db.insert(TransferHistoryInfoEntity.TRANSFER_HISTORY_TABLE_NAME, null, getValues(transferHistory));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/** 
	* @Title: deleteTransferHistoryBySavePath 
	* @Description: 根据保存地址删除一条记录 
	* @param @param savePath     
	* @return void    
	*/

	public synchronized int deleteTransferHistoryBySavePath(String savePath) {
		return delete(TransferHistoryInfoEntity.FILE_SAVE_PATH + "=?", new String[] { savePath });
	}

	/** 
	* @Title: deleteTransferHistoryByRawPath 
	* @Description: 根据文件原始路径 删除一条记录
	* @param @param rawPath
	* @param @return     
	* @return int    
	*/

	public synchronized int deleteTransferHistoryByRawPath(String rawPath) {
		return delete(TransferHistoryInfoEntity.FILE_RAW_PATH + "=?", new String[] { rawPath });
	}
	
	/** 
	* @Title: deleteAllTransferHistory 
	* @Description: 删除所有接受历史
	* @param @return    
	* @return int    
	*/ 
	
	public synchronized int deleteAllTransferHistory ( ) {
		return delete(null,null);
	}

	/** 
	* @Title: delete 
	* @Description: 删除一条记录 
	* @param @param whereClause
	* @param @param whereArgs     
	* @return void    
	*/

	private synchronized int delete(String whereClause, String[] whereArgs) {
		try {
			return _db.delete(TransferHistoryInfoEntity.TRANSFER_HISTORY_TABLE_NAME, whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/** 
	* @Title: findAllTransferList 
	* @Description: 获取所有收发记录 
	* @param @return     
	* @return List<TransferHistory>    
	*/

	public synchronized List<TransferHistory> findAllTransferList() {
		return findTransferList(null, null, null, null, null);
	}

	/** 
	* @Title: findAllReceiveTransferList 
	* @Description: 获取所有接受记录 
	* @param @return     
	* @return List<TransferHistory>    
	*/

	public synchronized List<TransferHistory> findAllReceiveTransferList() {
		return findTransferList(TransferHistoryInfoEntity.TRANSFER_TYPE + " =?", new String[] { ""
				+ TransferHistory.Type.RECEIVE }, null, null, null);
	}

	/** 
	* @Title: findAllShareTransferList 
	* @Description: 获取所有分享记录 
	* @param @return     
	* @return List<TransferHistory>    
	*/

	public synchronized List<TransferHistory> findAllShareTransferList() {
		return findTransferList(TransferHistoryInfoEntity.TRANSFER_TYPE + " =?", new String[] { ""
				+ TransferHistory.Type.SHARE }, null, null, null);
	}

	/** 
	* @Title: findAllTransferList 
	* @Description: 获取符合条件的所有记录
	* @param @param selection
	* @param @param selectionArgs
	* @param @param groupBy
	* @param @param having
	* @param @param orderBy
	* @param @return     
	* @return List<TransferHistory>    
	*/

	private synchronized List<TransferHistory> findTransferList(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		List<TransferHistory> result = new ArrayList<TransferHistory>();
		Cursor cursor = null;
		try {
			cursor = _db.query(TransferHistoryInfoEntity.TRANSFER_HISTORY_TABLE_NAME, new String[] {
					TransferHistoryInfoEntity.FILE_NAME, TransferHistoryInfoEntity.FILE_RAW_PATH,
					TransferHistoryInfoEntity.FILE_SAVE_PATH, TransferHistoryInfoEntity.FILE_SIZE,
					TransferHistoryInfoEntity.FILE_SUFFIX, TransferHistoryInfoEntity.FILE_TYPE,
					TransferHistoryInfoEntity.FILE_URL, TransferHistoryInfoEntity.CREATE_TIME,
					TransferHistoryInfoEntity.FINISH_TIME, TransferHistoryInfoEntity.TRANSFER_TYPE }, selection,
					selectionArgs, groupBy, having, orderBy);
			while (cursor.moveToNext()) {
				TransferHistory transferHistory = new TransferHistory();
				transferHistory.setFileName(cursor.getString(0));
				transferHistory.setFileRawPath(cursor.getString(1));
				transferHistory.setFileSavePath(cursor.getString(2));
				transferHistory.setFileSize(cursor.getLong(3));
				transferHistory.setFileSuffix(cursor.getString(4));
				transferHistory.setFileType(cursor.getInt(5));
				transferHistory.setFileUrl(cursor.getString(6));
				transferHistory.setCreateTime(cursor.getString(7));
				transferHistory.setFinishTime(cursor.getString(8));
				transferHistory.setTransferType(cursor.getInt(9));
				result.add(transferHistory);
			}
			if(cursor !=null )
			 {
				cursor.close() ;
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
