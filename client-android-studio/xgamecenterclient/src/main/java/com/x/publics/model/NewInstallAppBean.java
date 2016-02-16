/**   
* @Title: NewInstallAppBean.java
* @Package com.x.publics.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-7 下午2:25:47
* @version V1.0   
*/


package com.x.publics.model;

import android.graphics.drawable.Drawable;

/**
* @ClassName: NewInstallAppBean
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-7-7 下午2:25:47
* 
*/

public class NewInstallAppBean extends DownloadBean{
	
	private int installId;
	private int installAppId;
	private int installApkId;
	private int installCategoryId;
	private int installStars;
	private String installAppName;
	private String installPackageName;
	private String installVersionName;
	private String installOldVersionName;
	private int installVersionCode;
	private String installSourceDir;
	private String installFileSize;
	private int installTransFlag;
	private int installSysFlag;
	private Drawable installIcon;
	private String installLogo;
	private String installPublishTime;
	private int installStatus; // 应用状态  0正常 1下载中 2暂停 3可升级 4安装 5启动
	private String installSortLetters; //显示数据拼音的首字母
	private int installFileType; //
	
	public NewInstallAppBean(int id, String name, String packageName, String versionName, int versionCode, String size,
			String sourceDir, int transFlag, int sysFlag, Drawable icon) {
		super();
		this.installId = id;
		this.installAppName = name;
		this.installPackageName = packageName;
		this.installVersionName = versionName;
		this.installVersionCode = versionCode;
		this.installSourceDir = sourceDir;
		this.installFileSize = size;
		this.installTransFlag = transFlag;
		this.installSysFlag = sysFlag;
		this.installIcon = icon;
	}
	
	@Override
	public String toString() {
		String msg = "name:" + installAppName + ",packageName:" + installPackageName + ",versionName:" + installVersionName
				+ ",versionCode:" + installVersionCode + ",sourceDir:" + installSourceDir + ",size+" + installFileSize + ",transFlag:"
				+ installTransFlag + ",sysFlag:" + installSysFlag;
		return msg;
	}

	public int getInstallId() {
		return installId;
	}

	public void setInstallId(int installId) {
		this.installId = installId;
	}

	public int getInstallAppId() {
		return installAppId;
	}

	public void setInstallAppId(int installAppId) {
		this.installAppId = installAppId;
	}

	public int getInstallApkId() {
		return installApkId;
	}

	public void setInstallApkId(int installApkId) {
		this.installApkId = installApkId;
	}

	public int getInstallCategoryId() {
		return installCategoryId;
	}

	public void setInstallCategoryId(int installCategoryId) {
		this.installCategoryId = installCategoryId;
	}

	public int getInstallStars() {
		return installStars;
	}

	public void setInstallStars(int installStars) {
		this.installStars = installStars;
	}

	public String getInstallAppName() {
		return installAppName;
	}

	public void setInstallAppName(String installAppName) {
		this.installAppName = installAppName;
	}

	public String getInstallPackageName() {
		return installPackageName;
	}

	public void setInstallPackageName(String installPackageName) {
		this.installPackageName = installPackageName;
	}

	public String getInstallVersionName() {
		return installVersionName;
	}

	public void setInstallVersionName(String installVersionName) {
		this.installVersionName = installVersionName;
	}

	public String getInstallOldVersionName() {
		return installOldVersionName;
	}

	public void setInstallOldVersionName(String installOldVersionName) {
		this.installOldVersionName = installOldVersionName;
	}

	public int getInstallVersionCode() {
		return installVersionCode;
	}

	public void setInstallVersionCode(int installVersionCode) {
		this.installVersionCode = installVersionCode;
	}

	public String getInstallSourceDir() {
		return installSourceDir;
	}

	public void setInstallSourceDir(String installSourceDir) {
		this.installSourceDir = installSourceDir;
	}

	public String getInstallFileSize() {
		return installFileSize;
	}

	public void setInstallFileSize(String installFileSize) {
		this.installFileSize = installFileSize;
	}

	public int getInstallTransFlag() {
		return installTransFlag;
	}

	public void setInstallTransFlag(int installTransFlag) {
		this.installTransFlag = installTransFlag;
	}

	public int getInstallSysFlag() {
		return installSysFlag;
	}

	public void setInstallSysFlag(int installSysFlag) {
		this.installSysFlag = installSysFlag;
	}

	public Drawable getInstallIcon() {
		return installIcon;
	}

	public void setInstallIcon(Drawable installIcon) {
		this.installIcon = installIcon;
	}

	public String getInstallLogo() {
		return installLogo;
	}

	public void setInstallLogo(String installLogo) {
		this.installLogo = installLogo;
	}

	public String getInstallPublishTime() {
		return installPublishTime;
	}

	public void setInstallPublishTime(String installPublishTime) {
		this.installPublishTime = installPublishTime;
	}

	public int getInstallStatus() {
		return installStatus;
	}

	public void setInstallStatus(int installStatus) {
		this.installStatus = installStatus;
	}

	public String getInstallSortLetters() {
		return installSortLetters;
	}

	public void setInstallSortLetters(String installSortLetters) {
		this.installSortLetters = installSortLetters;
	}

	public int getInstallFileType() {
		return installFileType;
	}

	public void setInstallFileType(int installFileType) {
		this.installFileType = installFileType;
	}

}
