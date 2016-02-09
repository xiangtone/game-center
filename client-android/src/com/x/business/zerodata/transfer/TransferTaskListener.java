package com.x.business.zerodata.transfer;



public interface TransferTaskListener {

	/** 
	* @Title: updateProcess 
	* @Description: 通知更新进度 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void updateProcess(TransferTask task);

	/** 
	* @Title: finishDownload 
	* @Description: 通知下载完成 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void finishDownload(TransferTask task);

	/** 
	* @Title: preDownload 
	* @Description: 通知进入下载
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void preDownload(TransferTask task);

	/** 
	* @Title: pauseDownload 
	* @Description: 通知暂停下载 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void pauseDownload(TransferTask task);

	/** 
	* @Title: waitDownload 
	* @Description: 通知等待下载 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void waitDownload(TransferTask task);

	/** 
	* @Title: errorDownload 
	* @Description: 通知下载异常 
	* @param @param task
	* @param @param error     
	* @return void    
	* @throws 
	*/

	public void errorDownload(TransferTask task, Throwable error);
}
