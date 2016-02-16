package com.x.publics.download.upgrade;
/**
 * 文件下载监听
 
 *
 */
public interface DownloadProgress {
	
	/**
	 * 下载准备
	 * @param id
	 * @param url
	 * @param downloadInfo
	 */
	public void downloadReady(String url);
	
	/**
	 * 下载开始
	 * @param id
	 * @param url
	 * @param downloaded
	 * @param total
	 * @param downloadInfo
	 */
	public void downloadStart(String url,String savePath,boolean downloadPhone,long downloaded,long total);
	
	/**
	 * 下载中
	 * @param id
	 * @param url
	 * @param downloaded
	 * @param total
	 * @param downloadInfo
	 */
	public void downloading(String url,String savePath,boolean downloadPhone,long downloaded,long total);
	
	/**
	 * 下载成功
	 * @param url
	 * @param savePath
	 * @param total
	 * @param downloadInfo
	 */
	public void downloadSucess(String url,String savePath,boolean downloadPhone,long total,int type,String reason);
	
	/**
	 * 下载失败
	 * @param url
	 * @param savePath
	 * @param type
	 * @param reason
	 * @param downloadInfo
	 */
	public void downloadFail(String url,String savePath,boolean downloadPhone,int type,String reason);

}
