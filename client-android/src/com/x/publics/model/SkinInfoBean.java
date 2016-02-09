/**   
* @Title: SkinInfoBean.java
* @Package com.mas.amineappstore.publics.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-12-3 下午2:10:01
* @version V1.0   
*/

package com.x.publics.model;

/**
* @ClassName: SkinInfoBean
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-12-3 下午2:10:01
* 
*/

public class SkinInfoBean {

	private int skinId;
	private String skinName;
	private String logo;
	private String description;
	private String packageName;
	private String versionName;
	private int versionCode;
	private String apkUrl;
	private long apkSize;
	private long currentBytes;
	private int status; // 应用状态  0正常 1下载中 2暂停 3可升级 4安装 5启动

	public long getCurrentBytes() {
		return currentBytes;
	}

	public void setCurrentBytes(long currentBytes) {
		this.currentBytes = currentBytes;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

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

	public long getApkSize() {
		return apkSize;
	}

	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}

}
