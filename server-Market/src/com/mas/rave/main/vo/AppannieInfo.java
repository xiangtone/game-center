package com.mas.rave.main.vo;

import java.util.Date;

public class AppannieInfo extends PagingBean{

	private Integer id;
	
	private Integer raveId;
	
	private Integer albumId;
	
	private Integer columnId;
	
	private Integer categoryId;
	
	private Integer appId;


	private String appName;	
	
	private AppInfo appInfo;
	
	private Integer apkId;

	private AppFile appFile;
	
	private AppAlbum appAlbum;// 大类别Id',
	private AppAlbumColumn appAlbumColumn;// 页签Id',
	private Country country;
	private Category category;
	/**APP在APPAnnie上的安装数量*/
	private double annieInstallTotal;
	/**APP在APPAnnie上评分*/
	private double annieRatings;
	/**APP在APPAnnie上最早版本上线时间，
	 * 如果是我们自己运营的使用我们录入时间。
	 * (版本上线日期到至今的时间)*/
	private Date initialReleaseDate;
	/**APP在APPAnnie上的排行名次*/
	private double annieRank;
	/**APP包的大小*/
	private double size;
	/**zapp真实下载量*/
	private double realDownload;
	/**zapp平均下载量*/
	private double downloadAverage;
	/**zapp平均下载量*/
	private double pageviews;
	private double ratings;
	
	private double score;
	
	private Date createTime;

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

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public double getAnnieInstallTotal() {
		return annieInstallTotal;
	}

	public void setAnnieInstallTotal(double annieInstallTotal) {
		this.annieInstallTotal = annieInstallTotal;
	}


	public double getAnnieRatings() {
		return annieRatings;
	}

	public void setAnnieRatings(double annieRatings) {
		this.annieRatings = annieRatings;
	}



	public Date getInitialReleaseDate() {
		return initialReleaseDate;
	}

	public void setInitialReleaseDate(Date initialReleaseDate) {
		this.initialReleaseDate = initialReleaseDate;
	}

	public double getAnnieRank() {
		return annieRank;
	}

	public void setAnnieRank(double annieRank) {
		this.annieRank = annieRank;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getRealDownload() {
		return realDownload;
	}

	public void setRealDownload(double realDownload) {
		this.realDownload = realDownload;
	}

	public double getDownloadAverage() {
		return downloadAverage;
	}

	public void setDownloadAverage(double downloadAverage) {
		this.downloadAverage = downloadAverage;
	}

	public double getPageviews() {
		return pageviews;
	}

	public void setPageviews(double pageviews) {
		this.pageviews = pageviews;
	}

	public double getRatings() {
		return ratings;
	}

	public void setRatings(double ratings) {
		this.ratings = ratings;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public AppAlbum getAppAlbum() {
		return appAlbum;
	}

	public void setAppAlbum(AppAlbum appAlbum) {
		if(appAlbum!=null){
			this.setAlbumId(appAlbum.getId());
		}
		this.appAlbum = appAlbum;
	}

	public AppAlbumColumn getAppAlbumColumn() {
		return appAlbumColumn;
	}

	public void setAppAlbumColumn(AppAlbumColumn appAlbumColumn) {
		if(appAlbumColumn!=null){
			this.setColumnId(appAlbumColumn.getColumnId());
		}
		this.appAlbumColumn = appAlbumColumn;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		if(category!=null){
			this.setCategoryId(category.getId());
		}
		this.category = category;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getApkId() {
		return apkId;
	}

	public void setApkId(Integer apkId) {
		this.apkId = apkId;
	}

	public AppFile getAppFile() {
		return appFile;
	}

	public void setAppFile(AppFile appFile) {
		if(appFile!=null){
			this.setApkId(appFile.getAppId());
		}
		this.appFile = appFile;
	}

}
