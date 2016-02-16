package com.mas.rave.main.vo;

import java.util.Date;

import com.mas.rave.util.FileUtil;

/**
 * app分发对应内容
 * 
 * @author liwei.sz
 * 
 */
public class AppAlbumRes {
	private Integer id;
	private int raveId;// 平台Id',
	private AppAlbum appAlbum;// 大类别Id',
	private AppAlbumColumn appAlbumColumn;// 页签Id',
	private int categoryId; // 分类
	private double sort;// 排序（按数字大小从大到小排序）',
	private AppInfo appInfo;// 对应的t_app_info的Id',
	private String appName;// app应用名称',
	private int free;// '0公共资源1平台2自运营',
	private String logo;// 图标',
	private String bigLogo;// 大图标',
	private String brief;// 简介',
	private String description;// 详细长描述',
	private int stars;// 星星评级（1-10）每一个表示半颗星',
	private AppFile appFile;// 对应的t_app_file的Id',
	private int fileSize;// apk的文件大小',
	private String packageName;// 包名',
	private int versionCode;// 版本号',
	private String versionName;// 版本名',
	private String url;// apk下载地址',
	private int initDowdload;// 初始下载数（初始5万到10万）',
	private int realDowdload;// 真正下载数',
	private String operator;// 后台操作人',
	private Date createTime;// 创建时间',
	private int appId;
	private int apkId;
	private int channelId;
	private int source;
	/* 广告 */
	private String albumName;
	private String albumNameCn;

	private String categoryName;
	private String CategoryNameCn;

	private String sourceString;

	private String fileSizeString;
	private String freeString;// '0公共资源1平台2自运营',
	private boolean effective;

	private Country country;

	// add by dingjie 2014/8/20
	private Integer ranking;
	/* 页签 */
	private String appAlbumName;

	// 标识
	private int flag;

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}

	private int columnId;

	// add by dingjie 2014/08/28 应用专题Id
	private Integer collectionId;
	// 专题
	private AppCollection appCollection;
	// 专题名称
	private String collectionName;

	public Integer getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}

	public AppCollection getAppCollection() {
		return appCollection;
	}

	public void setAppCollection(AppCollection appCollection) {
		this.appCollection = appCollection;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getRaveId() {
		return raveId;
	}

	public void setRaveId(int raveId) {
		this.raveId = raveId;
	}

	public AppAlbum getAppAlbum() {
		return appAlbum;
	}

	public void setAppAlbum(AppAlbum appAlbum) {
		if (appAlbum != null) {
			this.setAppAlbumName(appAlbum.getName());
		}
		this.appAlbum = appAlbum;
	}

	public AppAlbumColumn getAppAlbumColumn() {
		return appAlbumColumn;
	}

	public void setAppAlbumColumn(AppAlbumColumn appAlbumColumn) {
		if (appAlbumColumn != null) {
			this.setAlbumName(appAlbumColumn.getName());
			this.setAlbumNameCn(appAlbumColumn.getNameCn());
		}
		this.appAlbumColumn = appAlbumColumn;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public double getSort() {
		return sort;
	}

	public void setSort(double sort) {
		this.sort = sort;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBigLogo() {
		return bigLogo;
	}

	public void setBigLogo(String bigLogo) {
		this.bigLogo = bigLogo;
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

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.setFileSizeString(FileUtil.sizeFormat(fileSize));
		this.fileSize = fileSize;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public AppInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
		if (appInfo != null) {
			Category category = appInfo.getCategory();
			if (category != null) {
				this.setCategoryName(category.getName());
				this.setCategoryNameCn(category.getCategoryCn());
			}
			this.setAppId(appInfo.getId());
		}
	}

	public AppFile getAppFile() {
		return appFile;
	}

	public void setAppFile(AppFile appFile) {
		if (appFile != null) {
			this.setApkId(appFile.getId());
		}
		this.appFile = appFile;
	}

	public int getInitDowdload() {
		return initDowdload;
	}

	public void setInitDowdload(int initDowdload) {
		this.initDowdload = initDowdload;
	}

	public int getRealDowdload() {
		return realDowdload;
	}

	public void setRealDowdload(int realDowdload) {
		this.realDowdload = realDowdload;
	}

	public int getFree() {
		return free;
	}

	public void setFree(int free) {
		this.free = free;
		if (free == 0) {
			this.setFreeString("公共资源");
		} else if (free == 1) {
			this.setFreeString("平台");
		} else {
			this.setFreeString("自运营");
		}
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		if (source == 1) {
			this.sourceString = "自动";
		} else {
			this.sourceString = "手动";
		}

		this.source = source;
	}

	public boolean getEffective() {
		return effective;
	}

	public void setEffective(boolean effective) {
		this.effective = effective;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumNameCn() {
		return albumNameCn;
	}

	public void setAlbumNameCn(String albumNameCn) {
		this.albumNameCn = albumNameCn;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryNameCn() {
		return CategoryNameCn;
	}

	public void setCategoryNameCn(String categoryNameCn) {
		CategoryNameCn = categoryNameCn;
	}

	public String getSourceString() {
		return sourceString;
	}

	public void setSourceString(String sourceString) {
		this.sourceString = sourceString;
	}

	public String getFileSizeString() {
		return fileSizeString;
	}

	public void setFileSizeString(String fileSizeString) {
		this.fileSizeString = fileSizeString;
	}

	public String getFreeString() {
		return freeString;
	}

	public void setFreeString(String freeString) {
		this.freeString = freeString;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		if (country != null) {
			this.setRaveId(country.getId());
		}
		this.country = country;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public String getAppAlbumName() {
		return appAlbumName;
	}

	public void setAppAlbumName(String appAlbumName) {
		this.appAlbumName = appAlbumName;
	}

	public int getApkId() {
		return apkId;
	}

	public void setApkId(int apkId) {
		this.apkId = apkId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
