package com.mas.data;

//add by lixin
public class AppDownloadData {
	// app 名称
	private String appName;
	// app 图标
	private String appIcon;

	private String countryName;

	private String countryIcon;
	
//	private Integer appId;
	
//	private String packageName;
	
	private Integer apkId;

	public Integer getApkId() {
		return apkId;
	}

	public void setApkId(Integer apkId) {
		this.apkId = apkId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryIcon() {
		return countryIcon;
	}

	public void setCountryIcon(String countryIcon) {
		this.countryIcon = countryIcon;
	}

}
