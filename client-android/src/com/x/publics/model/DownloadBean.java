/**   
* @Title: DownloadBean.java
* @Package com.x.model
* @Description: TODO 

* @date 2013-12-17 上午11:09:51
* @version V1.0   
*/

package com.x.publics.model;

import com.x.business.statistic.DataEyeManager;
import com.x.publics.utils.Utils;

import java.io.Serializable;

/**
* @ClassName: DownloadBean
* @Description: 下载应用实体类 

* @date 2013-12-17 上午11:09:51
* 
*/

public class DownloadBean implements Serializable {

	private static final long serialVersionUID = 83359352967668539L;
	private String url;
	private String name;
	private String localPath;
	private long fileSize;
	private long totalBytes;
	private long currentBytes;
	private String createTime;
	private String finishedTime;
	private int status; // 下载状态 
	private String speed;
	private String mediaType;
	private String iconUrl;
	private int categoryId;
	private int resourceId;

	/*App*/
	private String resource; // 名称
	private String versionName; // 版本号 1.0.0
	private int versionCode;
	private int appId;
	private int installed; // 是否已经安装 0 未安装 1 已安装
	private String packageName;
	private boolean autoInstall = true;
	private int stars;
	private boolean isPatch; //是否增量升级
	private String originalUrl;//全量包url
	private int fileType;//文件类型 1:应用，2:游戏

	/*Wallpaper*/
	private boolean setWallPaper; // 是否自动设为壁纸

	/*Rington*/
	private String palyTime;

	public DownloadBean() {

	}

	/**
	* 壁纸下载
	*/

	public DownloadBean(String url, String iconUrl, String name, int totalBytes, int currentBytes, String mediaType,
			int status, long fileSize, boolean setWallPaper, int resourceId, int categoryId) {
		this.originalUrl = url;
		this.url = url;
		this.iconUrl = iconUrl;
		this.name = name;
		this.totalBytes = totalBytes;
		this.currentBytes = currentBytes;
		this.mediaType = mediaType;
		this.status = status;
		this.fileSize = fileSize;
		this.setWallPaper = setWallPaper;
		this.resourceId = resourceId;
		this.categoryId = categoryId;

		this.createTime = Utils.formatData(System.currentTimeMillis());
		this.packageName = "";
		this.versionName = "";
		this.resource = "";
		this.localPath = "";
		this.fileType = DataEyeManager.getInstance().getFileType(mediaType);
	}

	/**
	* 铃声下载
	*/

	public DownloadBean(String url, String iconUrl, String name, int totalBytes, int currentBytes, String mediaType,
			int status, long fileSize, int resourceId, int categoryId) {
		this.originalUrl = url;
		this.url = url;
		if(iconUrl == null){ //为null的话入库失败
			this.iconUrl = "";
		} else {
			this.iconUrl = iconUrl;
		}
		this.name = name;
		this.totalBytes = totalBytes;
		this.currentBytes = currentBytes;
		this.mediaType = mediaType;
		this.status = status;
		this.fileSize = fileSize;
		this.resourceId = resourceId;
		this.categoryId = categoryId;

		this.createTime = Utils.formatData(System.currentTimeMillis());
		this.packageName = "";
		this.versionName = "";
		this.resource = "";
		this.localPath = "";
		this.fileType = DataEyeManager.getInstance().getFileType(mediaType);
	}

	/**
	*应用下载
	*/

	public DownloadBean(String url, String appName, long totalBytes, long currentBytes, String iconUrl,
			String mediaType, int resourceId, String version, String packageName, int status, long fileSize,
			int versionCode, int appId, int categoryId, int stars, boolean isPatch, String originalUrl) {
		super();
		this.url = url;
		this.name = appName;
		this.totalBytes = totalBytes;
		this.currentBytes = currentBytes;
		this.iconUrl = iconUrl;
		this.mediaType = mediaType;
		this.resourceId = resourceId;
		this.installed = 0;
		this.versionName = version;
		this.packageName = packageName;
		this.status = status;
		this.fileSize = fileSize;
		this.versionCode = versionCode;
		this.appId = appId;
		this.categoryId = categoryId;
		this.stars = stars;
		this.resource = appName;
		this.isPatch = isPatch;
		this.originalUrl = originalUrl;
		this.localPath = "";
		this.createTime = Utils.formatData(System.currentTimeMillis());
		this.fileType = DataEyeManager.getInstance().getFileType(mediaType);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public long getTotalBytes() {
		return totalBytes;
	}

	public void setTotalBytes(long totalBytes) {
		this.totalBytes = totalBytes;
	}

	public long getCurrentBytes() {
		return currentBytes;
	}

	public void setCurrentBytes(long currentBytes) {
		this.currentBytes = currentBytes;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public int getInstalled() {
		return installed;
	}

	public void setInstalled(int installed) {
		this.installed = installed;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public boolean isSetWallPaper() {
		return setWallPaper;
	}

	public void setSetWallPaper(boolean setWallPaper) {
		this.setWallPaper = setWallPaper;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(String finishedTime) {
		this.finishedTime = finishedTime;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public boolean isAutoInstall() {
		return autoInstall;
	}

	public void setAutoInstall(boolean autoInstall) {
		this.autoInstall = autoInstall;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getPalyTime() {
		return palyTime;
	}

	public void setPalyTime(String palyTime) {
		this.palyTime = palyTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPatch() {
		return isPatch;
	}

	public void setPatch(boolean isPatch) {
		this.isPatch = isPatch;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

}
