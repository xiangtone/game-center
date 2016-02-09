/**   
* @Title: MyAppsBean.java
* @Package com.mas.amineappstore.publics.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-7 下午2:43:11
* @version V1.0   
*/


package com.x.publics.model;

import android.graphics.drawable.Drawable;


/**
* @ClassName: MyAppsBean
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-7-7 下午2:43:11
* 
*/

public class MyAppsBean implements Comparable<MyAppsBean>{
	
	//已安装 更新
	private int id;
	private String url; //正在下载url
	private String appName;
	private String packageName;
	private String versionName;
	private String oldVersionName;
	private int versionCode;
	private int appId;
	private int apkId; //resourceId
	private int categoryId;
	private int stars;
	private String logo; //iconUrl
	private String originalUrl;//全量url
	private String publishTime;
	private String sortLetters; //显示数据拼音的首字母
	private String sourceDir;
	private long fileSize;
	private int transFlag;
	private int sysFlag;
	private Drawable icon;
	
	//下载
	private int status; // 应用状态  0正常 1下载中 2暂停 3可升级 4安装 5启动
	private long totalBytes;
	private long currentBytes;
	private String speed;
	private boolean isPatch; //是否增量升级
	private String urlPatch; //增量更新url
	private long patchSize; //增量包大小
	private int fileType;//文件类型 1:应用，2:游戏
	private String localPath;
	private String createTime;
	private String finishedTime;
	private String mediaType;
	private int installed; // 是否已经安装 0 未安装 1 已安装
	private boolean autoInstall = true;
	
	//收藏
	public int favoriteId;
	public String favoriteManualDownloadNetwork;
	public Long favoriteCreateTime;
	public Long favoriteUpdateTime;
	public String favoriteAttribute; // 追加属性fileType
	public String favoriteExtAttribute1;
	public int favoriteExtAttribute2;
	
	public int listType ;
	
	public class ListType {
		public static final int DOWNLOAD = 3;
		public static final int UNINSTALL = 2;
		public static final int UPDATE = 1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getOldVersionName() {
		return oldVersionName;
	}

	public void setOldVersionName(String oldVersionName) {
		this.oldVersionName = oldVersionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getApkId() {
		return apkId;
	}

	public void setApkId(int apkId) {
		this.apkId = apkId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getTransFlag() {
		return transFlag;
	}

	public void setTransFlag(int transFlag) {
		this.transFlag = transFlag;
	}

	public int getSysFlag() {
		return sysFlag;
	}

	public void setSysFlag(int sysFlag) {
		this.sysFlag = sysFlag;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public boolean getIsPatch() {
		return isPatch;
	}

	public void setIsPatch(boolean isPatch) {
		this.isPatch = isPatch;
	}

	public String getUrlPatch() {
		return urlPatch;
	}

	public void setUrlPatch(String urlPatch) {
		this.urlPatch = urlPatch;
	}

	public long getPatchSize() {
		return patchSize;
	}

	public void setPatchSize(long patchSize) {
		this.patchSize = patchSize;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(String finishedTime) {
		this.finishedTime = finishedTime;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public int getInstalled() {
		return installed;
	}

	public void setInstalled(int installed) {
		this.installed = installed;
	}

	public boolean isAutoInstall() {
		return autoInstall;
	}

	public void setAutoInstall(boolean autoInstall) {
		this.autoInstall = autoInstall;
	}

	public int getFavoriteId() {
		return favoriteId;
	}

	public void setFavoriteId(int favoriteId) {
		this.favoriteId = favoriteId;
	}

	public String getFavoriteManualDownloadNetwork() {
		return favoriteManualDownloadNetwork;
	}

	public void setFavoriteManualDownloadNetwork(String favoriteManualDownloadNetwork) {
		this.favoriteManualDownloadNetwork = favoriteManualDownloadNetwork;
	}

	public Long getFavoriteCreateTime() {
		return favoriteCreateTime;
	}

	public void setFavoriteCreateTime(Long favoriteCreateTime) {
		this.favoriteCreateTime = favoriteCreateTime;
	}

	public Long getFavoriteUpdateTime() {
		return favoriteUpdateTime;
	}

	public void setFavoriteUpdateTime(Long favoriteUpdateTime) {
		this.favoriteUpdateTime = favoriteUpdateTime;
	}

	public String getFavoriteAttribute() {
		return favoriteAttribute;
	}

	public void setFavoriteAttribute(String favoriteAttribute) {
		this.favoriteAttribute = favoriteAttribute;
	}

	public String getFavoriteExtAttribute1() {
		return favoriteExtAttribute1;
	}

	public void setFavoriteExtAttribute1(String favoriteExtAttribute1) {
		this.favoriteExtAttribute1 = favoriteExtAttribute1;
	}

	public int getFavoriteExtAttribute2() {
		return favoriteExtAttribute2;
	}

	public void setFavoriteExtAttribute2(int favoriteExtAttribute2) {
		this.favoriteExtAttribute2 = favoriteExtAttribute2;
	}

	public int getListType() {
		return listType;
	}

	public void setListType(int listType) {
		this.listType = listType;
	}

	
	
	@Override
	public int compareTo(MyAppsBean another) {
		if(another.getListType() > this.getListType())
			return 1;
		else if(another.getListType() == this.getListType())
			return 0;
		return -1;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
