package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

import com.mas.market.pojo.TAppInfo;
import com.mas.market.pojo.TRaveCountry;

public class AppDownloadLogIcon implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 客户端国家ID
     */
    private Integer raveId;

    /**
     * appId
     */
    private Integer appId;

    /**
     * app应用名称
     */
    private String appName;
    /**
     * apkId
     */
    private Integer apkId;

    /**
     * 创建时间
     */
    private Date createTime;
    
    private String iconUrl;
    
    private String packageName;
    
    private String countryName;
    
    private String appIcon;
    
    
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRaveId() {
		return raveId;
	}

	public void setRaveId(Integer raveId) {
		this.raveId = raveId;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getApkId() {
		return apkId;
	}

	public void setApkId(Integer apkId) {
		this.apkId = apkId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
}