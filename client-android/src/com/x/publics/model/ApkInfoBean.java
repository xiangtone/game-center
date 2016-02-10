/**   
* @Title: ApkInfoBean.java
* @Package com.x.model
* @Description: TODO 

* @date 2014-2-14 下午04:25:32
* @version V1.0   
*/

package com.x.publics.model;

import java.io.Serializable;

/**
* @ClassName: ApkInfoBean
* @Description: TODO 

* @date 2014-2-14 下午04:25:32
* 
*/

public class ApkInfoBean implements Serializable {

	private String appName;
	private String packageName;
	private int versionCode;
	private String versionName;
	private long fileSize;
	private String apkPath;
	private boolean isInstalled;

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

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getApkPath() {
		return apkPath;
	}

	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}

	public boolean isInstalled() {
		return isInstalled;
	}

	public void setInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

}
