/**   
* @Title: SettingModel.java
* @Package com.x.util.settings
* @Description: TODO 

* @date 2014-2-15 下午05:43:25
* @version V1.0   
*/

package com.x.business.settings;

/**
* @ClassName: SettingModel
* @Description: 应用设置 

* @date 2014-2-15 下午05:43:25
* 
*/

public class SettingModel {

	/*2G/3G 省流量模式开关*/
	private boolean gprsSavingMode = false;

	/*2G/3G 下载提示开关*/
	private boolean gprsDownloadPromt = false;

	/*只在wifi环境下载开关*/
	private boolean onlyWifiDownload = false;

	/*安装完成删除apk文件开关*/
	private boolean deleteApkFile = true;

	/*下载完成自动调用安装开关*/
	private boolean autoInstall = true;
	
	/*静默安装开关*/
	private boolean silentInstall = true;

	/*wifi环境下自动下载收藏*/
	private boolean autoDownloadFavInWifi = false;

	/*显示模式，默认列表列表*/
	private boolean ficheMode = false;

	/*wifi环境下自动下载可更新应用*/
	private boolean autoDownloadUpdateInWifi = false;
	
	/*悬浮窗开启or关闭*/
	private boolean floatMode = false;	

	/*音效开启or关闭*/
	private boolean soundEffect = false;	
	
	public boolean isGprsSavingMode() {
		return gprsSavingMode;
	}

	public void setGprsSavingMode(boolean gprsSavingMode) {
		this.gprsSavingMode = gprsSavingMode;
	}

	public boolean isGprsDownloadPromt() {
		return gprsDownloadPromt;
	}

	public void setGprsDownloadPromt(boolean gprsDownloadPromt) {
		this.gprsDownloadPromt = gprsDownloadPromt;
	}

	public boolean isOnlyWifiDownload() {
		return onlyWifiDownload;
	}

	public void setOnlyWifiDownload(boolean onlyWifiDownload) {
		this.onlyWifiDownload = onlyWifiDownload;
	}

	public boolean isDeleteApkFile() {
		return deleteApkFile;
	}

	public void setDeleteApkFile(boolean deleteApkFile) {
		this.deleteApkFile = deleteApkFile;
	}

	public boolean isAutoInstall() {
		return autoInstall;
	}

	public void setAutoInstall(boolean autoInstall) {
		this.autoInstall = autoInstall;
	}

	public boolean isAutoDownloadFavInWifi() {
		return autoDownloadFavInWifi;
	}

	public void setAutoDownloadFavInWifi(boolean autoDownloadFavInWifi) {
		this.autoDownloadFavInWifi = autoDownloadFavInWifi;
	}

	public boolean isAutoDownloadUpdateInWifi() {
		return autoDownloadUpdateInWifi;
	}

	public void setAutoDownloadUpdateInWifi(boolean autoDownloadUpdateInWifi) {
		this.autoDownloadUpdateInWifi = autoDownloadUpdateInWifi;
	}

	public boolean isFicheMode() {
		return ficheMode;
	}

	public void setFicheMode(boolean ficheMode) {
		this.ficheMode = ficheMode;
	}
	
	public boolean isFloatMode() {
		return floatMode;
	}

	public void setFloatMode(boolean floatMode) {
		this.floatMode = floatMode;
	}

	public boolean isSoundEffect() {
		return soundEffect;
	}

	public void setSoundEffect(boolean soundEffect) {
		this.soundEffect = soundEffect;
	}

	public boolean isSilentInstall() {
		return silentInstall;
	}

	public void setSilentInstall(boolean silentInstall) {
		this.silentInstall = silentInstall;
	}

 
}
