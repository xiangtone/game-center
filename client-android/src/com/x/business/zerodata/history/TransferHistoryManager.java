/**   
* @Title: TransferHistoryManager.java
* @Package com.mas.amineappstore.business.zerodata.history
* @Description: TODO 

* @date 2014-3-31 下午08:05:45
* @version V1.0   
*/

package com.x.business.zerodata.history;

import com.x.business.zerodata.client.http.model.Transfers;
import com.x.business.zerodata.transfer.TransferBean;
import com.x.db.zerodata.TransferHistory;
import com.x.db.zerodata.TransferHistoryEnityManager;
import com.x.publics.utils.Utils;

import java.util.List;

/**
* @ClassName: TransferHistoryManager
* @Description: TODO 

* @date 2014-3-31 下午08:05:45
* 
*/

public class TransferHistoryManager {

	private static TransferHistoryManager transferHistoryManager;

	public TransferHistoryManager() {

	}

	public static TransferHistoryManager getInstance() {
		if (transferHistoryManager == null)
			transferHistoryManager = new TransferHistoryManager();
		return transferHistoryManager;
	}

	/** 
	* @Title: saveTransferreceiveHistory 
	* @Description: 保存接受记录 
	* @param @param transferBean
	* @param @return     
	* @return long    
	*/

	public long saveTransferreceiveHistory(TransferBean transferBean) {
		if (transferBean == null)
			return -1;
		TransferHistory transferHistory = new TransferHistory();
		transferHistory.setFileName(transferBean.getFileName());
		transferHistory.setFileRawPath(transferBean.getFileRawPath());
		transferHistory.setFileSuffix(transferBean.getFileSuffix());
		transferHistory.setFileType(transferBean.getFileType());
		transferHistory.setFileUrl(transferBean.getFileUrl());
		transferHistory.setFileSize(transferBean.getFileSize());
		transferHistory.setFileSavePath(transferBean.getFileSavePath());
		transferHistory.setCreateTime(Utils.formatData(System.currentTimeMillis()));
		transferHistory.setFinishTime(Utils.formatData(System.currentTimeMillis()));
		transferHistory.setTransferType(TransferHistory.Type.RECEIVE);
		return TransferHistoryEnityManager.getInstance().save(transferHistory);
	}

	/** 
	* @Title: saveTransfershareHistory 
	* @Description: 保存分享记录 
	* @param @param transfers
	* @param @return     
	* @return long    
	*/

	public long saveTransfershareHistory(Transfers transfers) {
		if (transfers == null)
			return -1;
		TransferHistory transferHistory = new TransferHistory();
		transferHistory.setFileName(transfers.getFileName());
		transferHistory.setFileRawPath(transfers.getFileRawPath());
		transferHistory.setFileSuffix(transfers.getFileSuffix());
		transferHistory.setFileType(transfers.getFileType());
		transferHistory.setFileUrl(transfers.getFileUrl());
		transferHistory.setFileSize(transfers.getFileSize());
		transferHistory.setFileSavePath("");
		transferHistory.setCreateTime(Utils.formatData(System.currentTimeMillis()));
		transferHistory.setFinishTime(Utils.formatData(System.currentTimeMillis()));
		transferHistory.setTransferType(TransferHistory.Type.SHARE);
		return TransferHistoryEnityManager.getInstance().save(transferHistory);
	}

	/** 
	* @Title: findAllReceiveHistory 
	* @Description: 获取所有接收记录 
	* @param @return     
	* @return List<TransferHistory>    
	*/

	public List<TransferHistory> findAllReceiveHistory() {
		return TransferHistoryEnityManager.getInstance().findAllReceiveTransferList();
	}

	/** 
	* @Title: findAllShareHistory 
	* @Description: 获取所有发送记录
	* @param @return     
	* @return List<TransferHistory>    
	*/

	public List<TransferHistory> findAllShareHistory() {
		return TransferHistoryEnityManager.getInstance().findAllShareTransferList();
	}

	/** 
	* @Title: deleteReceiveHistoryBySavePath 
	* @Description: 根据保存地址删除一条记录 
	* @param @param savePath
	* @param @return     
	* @return int    
	*/

	public int deleteReceiveHistoryBySavePath(String savePath) {
		if (savePath == null)
			return -1;
		return TransferHistoryEnityManager.getInstance().deleteTransferHistoryBySavePath(savePath);
	}

	/** 
	* @Title: deleteReceiveHistoryByRawPath 
	* @Description: 根据原始地址删除一条记录 
	* @param @param rawPath
	* @param @return     
	* @return int    
	*/

	public int deleteReceiveHistoryByRawPath(String rawPath) {
		if (rawPath == null)
			return -1;
		return TransferHistoryEnityManager.getInstance().deleteTransferHistoryByRawPath(rawPath);
	}
	
	/** 
	* @Title: deleteAllReceiveHistory 
	* @Description: 删除所有接收历史 
	* @param @return    
	* @return int    
	*/ 
	
	public int deleteAllReceiveHistory( ) {
		return TransferHistoryEnityManager.getInstance().deleteAllTransferHistory();
	}

}
