package com.x.publics.download;

/**
* @ClassName: DownloadTaskListener
* @Description: 下载状态回调接口 

* @date 2014-1-10 上午09:40:40
* 
*/

public interface DownloadTaskListener {

	/** 
	* @Title: updateProcess 
	* @Description: 通知更新进度 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void updateProcess(DownloadTask task);

	/** 
	* @Title: finishDownload 
	* @Description: 通知下载完成 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void finishDownload(DownloadTask task);

	/** 
	* @Title: preDownload 
	* @Description: 通知进入下载
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void preDownload(DownloadTask task);

	/** 
	* @Title: pauseDownload 
	* @Description: 通知暂停下载 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void pauseDownload(DownloadTask task);

	/** 
	* @Title: waitDownload 
	* @Description: 通知等待下载 
	* @param @param task     
	* @return void    
	* @throws 
	*/

	public void waitDownload(DownloadTask task);

	/** 
	* @Title: errorDownload 
	* @Description: 通知下载异常 
	* @param @param task
	* @param @param error     
	* @return void    
	* @throws 
	*/

	public void errorDownload(DownloadTask task, Throwable error);
}
