package com.mas.rave.main.vo;

import java.util.Date;

public class AppFileZappUpdate {
	
	private Integer id;
	private String apkKey; //文件key
	private String versionName; //版本名
	private Integer versionCode; //版本号
	private Integer upgradeType; //1不升级，2升级，3强制升级
	private String updateInfo; //更新内容 
	private Date createTime;
	private Date updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getApkKey() {
		return apkKey;
	}
	public void setApkKey(String apkKey) {
		this.apkKey = apkKey;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public Integer getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	public Integer getUpgradeType() {
		return upgradeType;
	}
	public void setUpgradeType(Integer upgradeType) {
		this.upgradeType = upgradeType;
	}
	public String getUpdateInfo() {
		return updateInfo;
	}
	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
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
