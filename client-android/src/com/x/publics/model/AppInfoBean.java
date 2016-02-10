/**   
 * @Title: AppInfoBean.java
 * @Package com.x.model
 * @Description: TODO 
 
 * @date 2013-12-17 下午03:24:20
 * @version V1.0   
 */

package com.x.publics.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: AppInfoBean
 * @Description: 首页应用实体类
 
 * @date 2013-12-17 下午03:24:20
 * 
 */

public class AppInfoBean implements Parcelable {

	private long fileSize;
	private String logo;
	private int apkId;// 数据库id
	private int appId;// 应用id
	private int categoryId;// 分类id
	private String brief;// 简介
	private int fileType;// 文件类型 1:应用，2:游戏
	private String appName;
	private String packageName;
	private int stars;
	private String url;
	private int versionCode;
	private String versionName = "";
	private String publishTime;
	private long totalBytes;
	private long currentBytes;
	private int status; // 应用状态 0正常 1下载中 2暂停 3可升级 4安装 5启动

	private String patchUrl;
	private boolean isPatch;
	private long patchFileSize;

	// 新增字段
	private String issuer;// 开发商
	private String appSource; // 应用来源

	public class Status {
		public static final int NORMAL = 0;
		public static final int DOWNLOADING = 1;
		public static final int PAUSED = 2;
		public static final int CANUPGRADE = 3;
		public static final int CANINSTALL = 4;
		public static final int CANLAUNCH = 5;
		public static final int WAITING = 6;
		public static final int CONNECTING = 7;
		public static final int INSTALLING = 8;
		public static final int ERROR = 9;;
	}

	public AppInfoBean(Parcel source) {
		fileSize = source.readLong();
		logo = source.readString();
		apkId = source.readInt();
		appId = source.readInt();
		categoryId = source.readInt();
		brief = source.readString();
		appName = source.readString();
		packageName = source.readString();
		status = source.readInt();
		stars = source.readInt();
		url = source.readString();
		versionCode = source.readInt();
		versionName = source.readString();
		totalBytes = source.readLong();
		currentBytes = source.readLong();
		patchUrl = source.readString();
		patchFileSize = source.readLong();
		fileType = source.readInt();
		issuer = source.readString();
		appSource = source.readString();
	}

	public static Parcelable.Creator<AppInfoBean> getCreator() {
		return CREATOR;
	}

	public static final Parcelable.Creator<AppInfoBean> CREATOR = new Parcelable.Creator<AppInfoBean>() {

		@Override
		public AppInfoBean createFromParcel(Parcel source) {
			return new AppInfoBean(source);
		}

		@Override
		public AppInfoBean[] newArray(int size) {
			return new AppInfoBean[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(fileSize);
		dest.writeString(logo);
		dest.writeInt(apkId);
		dest.writeInt(appId);
		dest.writeInt(categoryId);
		dest.writeString(brief);
		dest.writeString(appName);
		dest.writeString(packageName);
		dest.writeInt(status);
		dest.writeInt(stars);
		dest.writeString(url);
		dest.writeInt(versionCode);
		dest.writeString(versionName);
		dest.writeLong(totalBytes);
		dest.writeLong(currentBytes);
		dest.writeString(patchUrl);
		dest.writeLong(patchFileSize);
		dest.writeInt(fileType);
		dest.writeString(issuer);
		dest.writeString(appSource);
	}

	public static final String NONE_VALUE = "-1";

	public AppInfoBean() {

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
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

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getApkId() {
		return apkId;
	}

	public void setApkId(int apkId) {
		this.apkId = apkId;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public boolean isPatch() {
		return isPatch;
	}

	public void setPatch(boolean isPatch) {
		this.isPatch = isPatch;
	}

	public long getPatchFileSize() {
		return patchFileSize;
	}

	public void setPatchFileSize(long patchFileSize) {
		this.patchFileSize = patchFileSize;
	}

	public String getPatchUrl() {
		return patchUrl;
	}

	public void setPatchUrl(String patchUrl) {
		this.patchUrl = patchUrl;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getAppSource() {
		return appSource;
	}

	public void setAppSource(String appSource) {
		this.appSource = appSource;
	}

}
