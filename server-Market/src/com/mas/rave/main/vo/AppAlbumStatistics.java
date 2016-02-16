package com.mas.rave.main.vo;

import java.util.Date;

/**
 * t_app_album_statistics 映射
 * 
 * @author jieding
 * 
 */
public class AppAlbumStatistics extends PagingBean {

	private int appId;
	/* 0公共资源1平台2自运营3开发者后台CP上传 */
	private int free;

	/* appName应用名 */
	private int appType;

	/* app的显示图片 */
	private String appName;
	private AppInfo appInfo;
	/* 类型编号 */
	private int categoryId;
	/* 类型名 */
	private String categoryName;
	/* 国家Id */
	private int raveId;

	private Country country;

	private int albumId;

	private AppAlbum appAlbum;

	/* 专辑编号 */
	private int columnId;
	/* 页签Id */
	private AppAlbumColumn appAlbumColumn;// 页签Id',
	/* zapp真正下载数 */
	private int realDowdload;
	/* zapp客户端浏览的次数 */
	private int pageOpen;
	/* 一共上线多少天 */
	private int days;
	/* 最终的评分 */
	private float starsReal;
	/* 下载转换率 */
	private double dowdChange;
	/* 搜索关键次数 */
	private int searchAppRank;
	/* 统计时全部app总搜索量 */
	private int searchAppCount;

	/* 下载排名 */
	private int dowdRank;
	
	/* 统计时下载排名总数 */
	private int dowdAppCount;

	/* 强制得分 */
	private double enforceScore;
	/* appannie上排行幅度 */
	private int annieExtent;
	/* 最终得分 */
	private double finalScore;
	/* 创建日期 */
	private Date createTime;

	private Integer appId1;
	private Integer raveId1;
	private AppFile appFile;

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getFree() {
		return free;
	}

	public void setFree(int free) {
		this.free = free;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
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
		this.country = country;
	}

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}

	public AppAlbumColumn getAppAlbumColumn() {
		return appAlbumColumn;
	}

	public void setAppAlbumColumn(AppAlbumColumn appAlbumColumn) {
		this.appAlbumColumn = appAlbumColumn;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public float getStarsReal() {
		return starsReal;
	}

	public void setStarsReal(float starsReal) {
		this.starsReal = starsReal;
	}

	public double getDowdChange() {
		return dowdChange;
	}

	public void setDowdChange(double dowdChange) {
		this.dowdChange = dowdChange;
	}

	public double getEnforceScore() {
		return enforceScore;
	}

	public void setEnforceScore(double enforceScore) {
		this.enforceScore = enforceScore;
	}

	public int getAnnieExtent() {
		return annieExtent;
	}

	public void setAnnieExtent(int annieExtent) {
		this.annieExtent = annieExtent;
	}

	public double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(double finalScore) {
		this.finalScore = finalScore;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getRealDowdload() {
		return realDowdload;
	}

	public void setRealDowdload(int realDowdload) {
		this.realDowdload = realDowdload;
	}

	public int getPageOpen() {
		return pageOpen;
	}

	public void setPageOpen(int pageOpen) {
		this.pageOpen = pageOpen;
	}

	public int getSearchAppRank() {
		return searchAppRank;
	}

	public void setSearchAppRank(int searchAppRank) {
		this.searchAppRank = searchAppRank;
	}

	public int getSearchAppCount() {
		return searchAppCount;
	}

	public void setSearchAppCount(int searchAppCount) {
		this.searchAppCount = searchAppCount;
	}

	public AppInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}

	public AppAlbum getAppAlbum() {
		return appAlbum;
	}

	public void setAppAlbum(AppAlbum appAlbum) {
		this.appAlbum = appAlbum;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public Integer getAppId1() {
		return appId1;
	}

	public void setAppId1(Integer appId1) {
		this.appId1 = appId1;
	}

	public AppFile getAppFile() {
		return appFile;
	}

	public void setAppFile(AppFile appFile) {
		this.appFile = appFile;
	}

	public Integer getRaveId1() {
		return raveId1;
	}

	public void setRaveId1(Integer raveId1) {
		this.raveId1 = raveId1;
	}

	public int getDowdRank() {
		return dowdRank;
	}

	public void setDowdRank(int dowdRank) {
		this.dowdRank = dowdRank;
	}

	public int getDowdAppCount() {
		return dowdAppCount;
	}

	public void setDowdAppCount(int dowdAppCount) {
		this.dowdAppCount = dowdAppCount;
	}

}
