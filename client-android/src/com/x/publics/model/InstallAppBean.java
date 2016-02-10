/**   
* @Title: InstallAppBean.java
* @Package com.x.model
* @Description: TODO 

* @date 2013-12-17 上午10:06:05
* @version V1.0   
*/

package com.x.publics.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
* @ClassName: InstallAppBean
* @Description: 本地安装应用实体类 

* @date 2013-12-17 上午10:06:05
* 
*/

public class InstallAppBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private int appId;
	private int apkId;
	private int categoryId;
	private int stars;
	private String appName;
	private String packageName;
	private String versionName;
	private String oldVersionName;
	private int versionCode;
	private String sourceDir;
	private long fileSize;
	private int transFlag;
	private int sysFlag;
	private Drawable icon;
	private String logo;
	private String url;//全量url
	private String publishTime;
	private int status; // 应用状态  0正常 1下载中 2暂停 3可升级 4安装 5启动
	private long totalBytes;
	private long currentBytes;
	private String sortLetters; //显示数据拼音的首字母
	private String speed;
	private boolean isPatch; //是否增量升级
	private String urlPatch; //增量更新url
	private long patchSize; //增量包大小
	private int fileType; //
	public InstallAppBean() {

	}

	public InstallAppBean(int id, String name, String packageName, String versionName, int versionCode, String size,
			String sourceDir, int transFlag, int sysFlag, Drawable icon) {
		super();
		this.id = id;
		this.appName = name;
		this.packageName = packageName;
		this.versionName = versionName;
		this.versionCode = versionCode;
		this.sourceDir = sourceDir;
		this.fileSize = Long.valueOf(size);
		this.transFlag = transFlag;
		this.sysFlag = sysFlag;
		this.icon = icon;
	}

	@Override
	public String toString() {
		String msg = "name:" + appName + ",packageName:" + packageName + ",versionName:" + versionName
				+ ",versionCode:" + versionCode + ",sourceDir:" + sourceDir + ",size+" + fileSize + ",transFlag:"
				+ transFlag + ",sysFlag:" + sysFlag;
		return msg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
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

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
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

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
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

	public String getOldVersionName() {
		return oldVersionName;
	}

	public void setOldVersionName(String oldVersionName) {
		this.oldVersionName = oldVersionName;
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

}
