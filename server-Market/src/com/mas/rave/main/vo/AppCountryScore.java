package com.mas.rave.main.vo;

import java.util.Date;

public class AppCountryScore extends PagingBean{
	
	private Integer id;
	
	private Integer albumId;//大类别Id
	
	private Integer columnId;//页签Id
	
	private String albumName;//专题名称
	
	private String columnName;//页签名称
	
	private String columnNameCn;//页签中文名称
	
	private int raveId;
	
	private Country country;
	
	private int appId;
	
	private AppInfo appInfo;
	
	private String appName;
	/*强制排行的得分 默认10分*/
	private int score;
	/*衰减周期默认10天*/
	private int fadingDay;
	
	private Date initialReleaseDate;
	
	private int days;
	/*startDate*/
	private Date startDate;
	/*是否启用*/
	private boolean state;
	/*创建日期*/
	private Date createTime;
	
	private double enforceScore;
	
	private String startDate1;
	
	private String bigLogo;
	
	private int free;
	
	private Integer isCategory;//是否为category:0:否，1:是。默认为0

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getAlbumId() {
		return albumId;
	}
	public void setAlbumId(Integer albumId) {
		this.albumId = albumId;
	}
	public Integer getColumnId() {
		return columnId;
	}
	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}
	
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnNameCn() {
		return columnNameCn;
	}
	public void setColumnNameCn(String columnNameCn) {
		this.columnNameCn = columnNameCn;
	}
	public int getRaveId() {
		return raveId;
	}
	public void setRaveId(int raveId) {
		this.raveId = raveId;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		if(country!=null){
			this.setRaveId(country.getId());
		}		
		this.country = country;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public AppInfo getAppInfo() {
		return appInfo;
	}
	public void setAppInfo(AppInfo appInfo) {
		if(appInfo!=null){
			this.setAppId(appInfo.getId());
			this.setAppName(appInfo.getName());
		}
		this.appInfo = appInfo;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getFadingDay() {
		return fadingDay;
	}
	public void setFadingDay(int fadingDay) {
		this.fadingDay = fadingDay;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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
	public String getStartDate1() {
		return startDate1;
	}
	public void setStartDate1(String startDate1) {
		this.startDate1 = startDate1;
	}
	public String getBigLogo() {
		return bigLogo;
	}
	public void setBigLogo(String bigLogo) {
		this.bigLogo = bigLogo;
	}
	public int getFree() {
		return free;
	}
	public void setFree(int free) {
		this.free = free;
	}
	public double getEnforceScore() {
		return enforceScore;
	}
	public void setEnforceScore(double enforceScore) {
		this.enforceScore = enforceScore;
	}
	public Date getInitialReleaseDate() {
		return initialReleaseDate;
	}
	public void setInitialReleaseDate(Date initialReleaseDate) {
		this.initialReleaseDate = initialReleaseDate;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public Integer getIsCategory() {
		return isCategory;
	}
	public void setIsCategory(Integer isCategory) {
		this.isCategory = isCategory;
	}
	
}
