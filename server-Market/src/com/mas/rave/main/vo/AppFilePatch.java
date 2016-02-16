package com.mas.rave.main.vo;

import java.util.Date;

/**
 * app对应文件历史信息
 * 
 * @author liwei.sz
 * 
 */
public class AppFilePatch {
	private int id;
	private AppInfo appInfo;// appInfoId',
	private int apkId; // apkId
	private String appName; // app名字
	private Channel channel;
	private Cp cp;
	private String lowMD5;
	private String lowPackageName;
	private int lowVersionCode;// 版本号',
	private String lowVersionName;// 版本名',
	private String packageName;
	private int versionCode;// 版本号',
	private String versionName;// 版本名',
	private String url;// 下载地址
	private int fileSize;// 大小',
	private boolean state;// 有效值.0无效 1有效',
	private Date createTime;// 创建时间
	private Date updateTime; // 更新时间
	private int lowFileSize;
	
	
	public int getLowFileSize() {
		return lowFileSize;
	}
	public void setLowFileSize(int lowFileSize) {
		this.lowFileSize = lowFileSize;
	}
	public String getLowMD5() {
		return lowMD5;
	}
	public void setLowMD5(String lowMD5) {
		this.lowMD5 = lowMD5;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public AppInfo getAppInfo() {
		return appInfo;
	}
	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}
	public int getApkId() {
		return apkId;
	}
	public void setApkId(int apkId) {
		this.apkId = apkId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public Cp getCp() {
		return cp;
	}
	public void setCp(Cp cp) {
		this.cp = cp;
	}
	public String getLowPackageName() {
		return lowPackageName;
	}
	public void setLowPackageName(String lowPackageName) {
		this.lowPackageName = lowPackageName;
	}
	public int getLowVersionCode() {
		return lowVersionCode;
	}
	public void setLowVersionCode(int lowVersionCode) {
		this.lowVersionCode = lowVersionCode;
	}
	public String getLowVersionName() {
		return lowVersionName;
	}
	public void setLowVersionName(String lowVersionName) {
		this.lowVersionName = lowVersionName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	

}
