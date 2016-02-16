package com.mas.rave.main.vo;

import java.sql.Timestamp;

public class ImageAlbumRes {
	
	private Integer id;
	private Integer raveId;// 平台国家Id',
	private Integer themeId;
	private ImageAlbumTheme imageAlbumTheme;
	private Integer albumId;
	private AppAlbum appAlbum;// 大类别Id',
	private Integer columnId;
	private AppAlbumColumn appAlbumColumn;// 页签Id',
	private Integer categoryId; // 分类
	private Category category;
	private Integer sort;// 排序（按数字大小从大到小排序）',
	private Integer imageId;
	private String imageName;
	private ImageInfo imageInfo;
	private Integer fileSize;
	private Integer free;// '0公共资源1平台2自运营',
	private String logo;// 图标',
	private String biglogo;// 图标',
	private String brief;// 简介',
	private Integer width;
	private Integer length;
	private String description;// 详细长描述',
	private Integer stars;// 星星评级（1-10）每一个表示半颗星',
	private String url;// apk下载地址',
	private Integer initDowdload;// 初始下载数（初始5万到10万）',
	private Integer realDowdload;// 真正下载数',
	private String operator;// 后台操作人',
	private Timestamp createTime;// 创建时间',
	
	
	
	private Country country;

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
	public AppAlbum getAppAlbum() {
		return appAlbum;
	}
	public void setAppAlbum(AppAlbum appAlbum) {
		this.appAlbum = appAlbum;
	}
	public AppAlbumColumn getAppAlbumColumn() {
		return appAlbumColumn;
	}
	public void setAppAlbumColumn(AppAlbumColumn appAlbumColumn) {
		this.appAlbumColumn = appAlbumColumn;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getThemeId() {
		return themeId;
	}
	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
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
	public Integer getFileSize() {
		return fileSize;
	}
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	public Integer getFree() {
		return free;
	}
	public void setFree(Integer free) {
		this.free = free;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStars() {
		return stars;
	}
	public void setStars(Integer stars) {
		this.stars = stars;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getInitDowdload() {
		return initDowdload;
	}
	public void setInitDowdload(Integer initDowdload) {
		this.initDowdload = initDowdload;
	}
	public Integer getRealDowdload() {
		return realDowdload;
	}
	public void setRealDowdload(Integer realDowdload) {
		this.realDowdload = realDowdload;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public ImageAlbumTheme getImageAlbumTheme() {
		return imageAlbumTheme;
	}
	public void setImageAlbumTheme(ImageAlbumTheme imageAlbumTheme) {
		this.imageAlbumTheme = imageAlbumTheme;
	}
	public Integer getImageId() {
		return imageId;
	}
	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public ImageInfo getImageInfo() {
		return imageInfo;
	}
	public void setImageInfo(ImageInfo imageInfo) {
		this.imageInfo = imageInfo;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public String getBiglogo() {
		return biglogo;
	}
	public void setBiglogo(String biglogo) {
		this.biglogo = biglogo;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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

}
