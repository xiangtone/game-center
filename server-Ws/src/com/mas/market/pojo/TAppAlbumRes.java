package com.mas.market.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TAppAlbumRes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Boolean isPatch;
    private String urlPatch;
    private Integer patchSize;
    private String publishTime;
    private Integer fileType; 
    private Integer fileTypeName; 
    /**
     * 最终的评分
     */
    private Float starsReal;
    /**
     * 平台Id
     */
    private Integer raveId;

    /**
     * 大类别Id
     */
    private Integer albumId;

    /**
     * 页签Id
     */
    private Integer columnId;

    /**
     * 应用类型（目前用于推荐）
     */
    private Integer categoryId;

    /**
     * 类型名称
     */
    private String categoryName;

    /**
     * 权重值
     */
    private BigDecimal sort;

    /**
     * 对应的t_app_info的Id
     */
    private Integer appId;

    /**
     * app应用名称
     */
    private String appName;

    /**
     * 0公共资源1平台2自运营
     */
    private Integer free;

    /**
     * 图标
     */
    private String logo;

    private String bigLogo;

    private String brief;

    /**
     * 详细长描述
     */
    private String description;

    /**
     * 星星评级（1-10）每一个表示半颗星
     */
    private Integer stars;

    /**
     * 对应的t_app_file的Id
     */
    private Integer apkId;

    /**
     * apk的文件大小
     */
    private Integer fileSize;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 版本号
     */
    private Integer versionCode;

    /**
     * 版本名
     */
    private String versionName;

    /**
     * apk下载地址
     */
    private String url;

    /**
     * 初始下载数（初始5万到10万）
     */
    private Integer initDowdload;

    /**
     * 真正下载数
     */
    private Integer realDowdload;

    /**
     * 后台操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 来源，0手动，1自动
     */
    private Integer source;

    /**
     * 是否生效
     */
    private Boolean effective;
    
    /**
     * app annie上的发行商
     */
    private String issuer;
    
    /**
     * app来源，根据free的值取值
     * Free=0,公用     （google）
     * Free=2,自营       (MAS)
     */
    private String appSource;

	public String getAppSource() {
		return appSource;
	}

	public void setAppSource(String appSource) {
		this.appSource = appSource;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 平台Id
     */
    public Integer getRaveId() {
        return raveId;
    }

    /**
     * @param raveid 
	 *            平台Id
     */
    public void setRaveId(Integer raveId) {
        this.raveId = raveId;
    }

    /**
     * @return 大类别Id
     */
    public Integer getAlbumId() {
        return albumId;
    }

    /**
     * @param albumid 
	 *            大类别Id
     */
    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    /**
     * @return 页签Id
     */
    public Integer getColumnId() {
        return columnId;
    }

    /**
     * @param columnid 
	 *            页签Id
     */
    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    /**
     * @return 应用类型（目前用于推荐）
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryid 
	 *            应用类型（目前用于推荐）
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return 类型名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryname 
	 *            类型名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return 权重值
     */
    public BigDecimal getSort() {
        return sort;
    }

    /**
     * @param sort 
	 *            权重值
     */
    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    /**
     * @return 对应的t_app_info的Id
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * @param appid 
	 *            对应的t_app_info的Id
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * @return app应用名称
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appname 
	 *            app应用名称
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return 0公共资源1平台2自运营
     */
    public Integer getFree() {
        return free;
    }

    /**
     * @param free 
	 *            0公共资源1平台2自运营
     */
    public void setFree(Integer free) {
        this.free = free;
    }

    /**
     * @return 图标
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param logo 
	 *            图标
     */
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

    /**
     * @return 详细长描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 
	 *            详细长描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 星星评级（1-10）每一个表示半颗星
     */
    public Integer getStars() {
        return stars;
    }

    /**
     * @param stars 
	 *            星星评级（1-10）每一个表示半颗星
     */
    public void setStars(Integer stars) {
        this.stars = stars;
    }

    /**
     * @return 对应的t_app_file的Id
     */
    public Integer getApkId() {
        return apkId;
    }

    /**
     * @param apkid 
	 *            对应的t_app_file的Id
     */
    public void setApkId(Integer apkId) {
        this.apkId = apkId;
    }

    /**
     * @return apk的文件大小
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * @param filesize 
	 *            apk的文件大小
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return 包名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packagename 
	 *            包名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return 版本号
     */
    public Integer getVersionCode() {
        return versionCode;
    }

    /**
     * @param versioncode 
	 *            版本号
     */
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return 版本名
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * @param versionname 
	 *            版本名
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * @return apk下载地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url 
	 *            apk下载地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return 初始下载数（初始5万到10万）
     */
    public Integer getInitDowdload() {
        return initDowdload;
    }

    /**
     * @param initdowdload 
	 *            初始下载数（初始5万到10万）
     */
    public void setInitDowdload(Integer initDowdload) {
        this.initDowdload = initDowdload;
    }

    /**
     * @return 真正下载数
     */
    public Integer getRealDowdload() {
        return realDowdload;
    }

    /**
     * @param realdowdload 
	 *            真正下载数
     */
    public void setRealDowdload(Integer realDowdload) {
        this.realDowdload = realDowdload;
    }

    /**
     * @return 后台操作人
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator 
	 *            后台操作人
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createtime 
	 *            创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 来源，0手动，1自动
     */
    public Integer getSource() {
        return source;
    }

    /**
     * @param source 
	 *            来源，0手动，1自动
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * @return 是否生效
     */
    public Boolean getEffective() {
        return effective;
    }

    /**
     * @param effective 
	 *            是否生效
     */
    public void setEffective(Boolean effective) {
        this.effective = effective;
    }

	public Boolean getIsPatch() {
		return isPatch;
	}

	public void setIsPatch(Boolean isPatch) {
		this.isPatch = isPatch;
	}

	public String getUrlPatch() {
		return urlPatch;
	}

	public void setUrlPatch(String urlPatch) {
		this.urlPatch = urlPatch;
	}

	public Integer getPatchSize() {
		return patchSize;
	}

	public void setPatchSize(Integer patchSize) {
		this.patchSize = patchSize;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getFileTypeName() {
		return fileTypeName;
	}

	public void setFileTypeName(Integer fileTypeName) {
		this.fileTypeName = fileTypeName;
	}

	public Float getStarsReal() {
		return starsReal;
	}

	public void setStarsReal(Float starsReal) {
		this.starsReal = starsReal;
	}
}