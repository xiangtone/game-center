package com.mas.rave.main.vo;

import java.util.Date;

/**
 * app分发分类
 * 
 * @author liwei.sz
 * 
 */
public class ClientSkin extends PagingBean {
	private int skinId;
	private String skinName;
	private String logo;
	private String description;
	private String packageName;
	private String versionName;
	private int versionCode;
	private String apkUrl;
	private int apkSize;
	private int downLoadNum;
	private Date createTime;
	private Date updateTime;
	private int sort;
	private boolean state;

	private String logoUrl;
	private String apkFileUrl;

	public int getSkinId() {
		return skinId;
	}

	public void setSkinId(int skinId) {
		this.skinId = skinId;
	}

	public String getSkinName() {
		return skinName;
	}

	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public int getApkSize() {
		return apkSize;
	}

	public void setApkSize(int apkSize) {
		this.apkSize = apkSize;
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

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getApkFileUrl() {
		return apkFileUrl;
	}

	public void setApkFileUrl(String apkFileUrl) {
		this.apkFileUrl = apkFileUrl;
	}

	public int getDownLoadNum() {
		return downLoadNum;
	}

	public void setDownLoadNum(int downLoadNum) {
		this.downLoadNum = downLoadNum;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

}
