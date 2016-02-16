package com.reportforms.model;

import java.sql.Timestamp;

import com.reportforms.util.DateUtil;

/**
 * 应用下载
 * @author lisong.lan
 *
 */
public class AppDownLoad extends BaseDomain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4363359648701021285L;
	
	private Integer appId;
	
	private String appName;
	
	private Long openNum;
	
	private Integer appType;
	
	private String appTypeString;
	
	private String theDate;
	
	private Integer categoryId;
	
	private String categoryName;
	
	private Integer free;
	
	private String freeDesc;
	
	private String source;
	
	private String countryCn;
	
	private String country;
	
	private Long downloadNum;
	
	private Timestamp createTime;
	
	private String createTimeString;
	
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

	public Long getOpenNum() {
		return openNum;
	}

	public void setOpenNum(Long openNum) {
		this.openNum = openNum;
	}
	
	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
		if(null != appType){
			switch (appType.intValue()) {
			case 1:this.appTypeString = "Homes";break;
			case 2:this.appTypeString = "Apps";break;
			case 3:this.appTypeString = "Games";break;
			case 4:this.appTypeString = "Ringtones";break;
			case 5:this.appTypeString = "Wallpaper";break;
			default:
				break;
			}
		}
	}

	public String getAppTypeString() {
		return appTypeString;
	}
	
	public String getTheDate() {
		return theDate;
	}

	public void setTheDate(String theDate) {
		this.theDate = theDate;
	}

	public String getCountryCn() {
		return countryCn;
	}

	public void setCountryCn(String countryCn) {
		this.countryCn = countryCn;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(Long downloadNum) {
		this.downloadNum = downloadNum;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
		this.createTimeString = DateUtil.getTimestampToString(createTime);
	}
	
	public String getCreateTimeString() {
		return createTimeString;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getFree() {
		return free;
	}

	public void setFree(Integer free) {
		this.free = free;
		if(null != free){
			switch (free.intValue()) {
			case 0: this.freeDesc = "公共资源"; break;
			case 1: this.freeDesc = "平台"; break;
			case 2: this.freeDesc = "自运营"; break;
			case 3: this.freeDesc = "开发者后台"; break;
			default:
				break;
			}
		}
	}

	public String getFreeDesc() {
		return freeDesc;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
