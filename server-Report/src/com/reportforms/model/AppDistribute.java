package com.reportforms.model;

import java.io.Serializable;
import java.util.Date;

import com.reportforms.util.DateUtil;
import com.reportforms.util.FileUtil;

public class AppDistribute implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * appid
     */
    private Integer appId;

    /**
     * app应用名称
     */
    private String appName;

    /**
     * 类型编号
     */
    private Integer categoryId;

    /**
     * 类型名称
     */
    private String categoryName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 0公共资源1平台2自运营
     */
    private Integer free;

    /**
     * 图标
     */
    private String logo;

    private String bigLogo;

    /**
     * 描述
     */
    private String brief;

    /**
     * 长的介绍
     */
    private String description;

    /**
     * 星级
     */
    private Integer stars;

    /**
     * 初始下载数（初始5万到10万）
     */
    private Integer initDowdload;

    /**
     * 真正下载数
     */
    private Integer realDowdload;

    /**
     * 客户端浏览的次数
     */
    private Integer pageOpen;

    /**
     * 更新次数
     */
    private Integer updateNum;

    /**
     * 游戏编号
     */
    private Integer apkId;

    /**
     * 大小
     */
    private String fileSize;

    /**
     * 民名
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
     * 下载地址
     */
    private String url;

    /**
     * Home recommend 最高排名
     */
    private Integer hrHigerank;

    /**
     * Home recommend 最低排名
     */
    private Integer hrLowrank;

    /**
     * Home new 最高排名
     */
    private Integer hnHigerank;

    /**
     * Home new 最低排名
     */
    private Integer hnLowrank;

    /**
     * Home top 最高排名
     */
    private Integer htHigerank;

    /**
     * Home top 最低排名
     */
    private Integer htLowrank;

    /**
     * Home POPULAR 最高排名
     */
    private Integer hpHigerank;

    /**
     * Home POPULAR 最低排名
     */
    private Integer hpLowrank;

    /**
     * App HOT 最高排名
     */
    private Integer ahHigerank;

    /**
     * App HOT 最低排名
     */
    private Integer ahLowrank;

    /**
     * App TOP 最高排名
     */
    private Integer atHigerank;

    /**
     * App TOP 最低排名
     */
    private Integer atLowrank;

    /**
     * App NEW 最高排名
     */
    private Integer anHigerank;

    /**
     * App NEW 最低排名
     */
    private Integer anLowrank;

    /**
     * Game HOT 最高排名
     */
    private Integer ghHigerank;

    /**
     * Game HOT 最低排名
     */
    private Integer ghLowrank;

    /**
     * Game TOP 最高排名
     */
    private Integer gtHigerank;

    /**
     * Game TOP 最低排名
     */
    private Integer gtLowrank;

    /**
     * Game NEW 最高排名
     */
    private Integer gnHigerank;

    /**
     * Game NEW 最低排名
     */
    private Integer gnLowrank;

    private Date createTime;

    private Date updateTime;
    
    private String createTimeString;

    private String updateTimeString;
    
    /**
     * 国家Id
     */
	private int raveId;

	/**
	 * 国家
	 */
	private Country country; 
	
	
	private String countryName;
    /**
     * @return appid
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * @param appid 
	 *            appid
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
     * @return 类型编号
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryid 
	 *            类型编号
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
     * @return 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 
	 *            排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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

    /**
     * @return 描述
     */
    public String getBrief() {
        return brief;
    }

    /**
     * @param brief 
	 *            描述
     */
    public void setBrief(String brief) {
        this.brief = brief;
    }

    /**
     * @return 长的介绍
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 
	 *            长的介绍
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 星级
     */
    public Integer getStars() {
        return stars;
    }

    /**
     * @param stars 
	 *            星级
     */
    public void setStars(Integer stars) {
        this.stars = stars;
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
     * @return 客户端浏览的次数
     */
    public Integer getPageOpen() {
        return pageOpen;
    }

    /**
     * @param pageopen 
	 *            客户端浏览的次数
     */
    public void setPageOpen(Integer pageOpen) {
        this.pageOpen = pageOpen;
    }

    /**
     * @return 更新次数
     */
    public Integer getUpdateNum() {
        return updateNum;
    }

    /**
     * @param updatenum 
	 *            更新次数
     */
    public void setUpdateNum(Integer updateNum) {
        this.updateNum = updateNum;
    }

    /**
     * @return 游戏编号
     */
    public Integer getApkId() {
        return apkId;
    }

    /**
     * @param apkid 
	 *            游戏编号
     */
    public void setApkId(Integer apkId) {
        this.apkId = apkId;
    }
    
    /**
     * @return 民名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packagename 
	 *            民名
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
     * @return 下载地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url 
	 *            下载地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Home recommend 最高排名
     */
    public Integer getHrHigerank() {
        return hrHigerank;
    }

    /**
     * @param hrhigerank 
	 *            Home recommend 最高排名
     */
    public void setHrHigerank(Integer hrHigerank) {
        this.hrHigerank = hrHigerank;
    }

    /**
     * @return Home recommend 最低排名
     */
    public Integer getHrLowrank() {
        return hrLowrank;
    }

    /**
     * @param hrlowrank 
	 *            Home recommend 最低排名
     */
    public void setHrLowrank(Integer hrLowrank) {
        this.hrLowrank = hrLowrank;
    }

    /**
     * @return Home new 最高排名
     */
    public Integer getHnHigerank() {
        return hnHigerank;
    }

    /**
     * @param hnhigerank 
	 *            Home new 最高排名
     */
    public void setHnHigerank(Integer hnHigerank) {
        this.hnHigerank = hnHigerank;
    }

    /**
     * @return Home new 最低排名
     */
    public Integer getHnLowrank() {
        return hnLowrank;
    }

    /**
     * @param hnlowrank 
	 *            Home new 最低排名
     */
    public void setHnLowrank(Integer hnLowrank) {
        this.hnLowrank = hnLowrank;
    }

    /**
     * @return Home top 最高排名
     */
    public Integer getHtHigerank() {
        return htHigerank;
    }

    /**
     * @param hthigerank 
	 *            Home top 最高排名
     */
    public void setHtHigerank(Integer htHigerank) {
        this.htHigerank = htHigerank;
    }

    /**
     * @return Home top 最低排名
     */
    public Integer getHtLowrank() {
        return htLowrank;
    }

    /**
     * @param htlowrank 
	 *            Home top 最低排名
     */
    public void setHtLowrank(Integer htLowrank) {
        this.htLowrank = htLowrank;
    }

    /**
     * @return Home POPULAR 最高排名
     */
    public Integer getHpHigerank() {
        return hpHigerank;
    }

    /**
     * @param hphigerank 
	 *            Home POPULAR 最高排名
     */
    public void setHpHigerank(Integer hpHigerank) {
        this.hpHigerank = hpHigerank;
    }

    /**
     * @return Home POPULAR 最低排名
     */
    public Integer getHpLowrank() {
        return hpLowrank;
    }

    /**
     * @param hplowrank 
	 *            Home POPULAR 最低排名
     */
    public void setHpLowrank(Integer hpLowrank) {
        this.hpLowrank = hpLowrank;
    }

    /**
     * @return App HOT 最高排名
     */
    public Integer getAhHigerank() {
        return ahHigerank;
    }

    /**
     * @param ahhigerank 
	 *            App HOT 最高排名
     */
    public void setAhHigerank(Integer ahHigerank) {
        this.ahHigerank = ahHigerank;
    }

    /**
     * @return App HOT 最低排名
     */
    public Integer getAhLowrank() {
        return ahLowrank;
    }

    /**
     * @param ahlowrank 
	 *            App HOT 最低排名
     */
    public void setAhLowrank(Integer ahLowrank) {
        this.ahLowrank = ahLowrank;
    }

    /**
     * @return App TOP 最高排名
     */
    public Integer getAtHigerank() {
        return atHigerank;
    }

    /**
     * @param athigerank 
	 *            App TOP 最高排名
     */
    public void setAtHigerank(Integer atHigerank) {
        this.atHigerank = atHigerank;
    }

    /**
     * @return App TOP 最低排名
     */
    public Integer getAtLowrank() {
        return atLowrank;
    }

    /**
     * @param atlowrank 
	 *            App TOP 最低排名
     */
    public void setAtLowrank(Integer atLowrank) {
        this.atLowrank = atLowrank;
    }

    /**
     * @return App NEW 最高排名
     */
    public Integer getAnHigerank() {
        return anHigerank;
    }

    /**
     * @param anhigerank 
	 *            App NEW 最高排名
     */
    public void setAnHigerank(Integer anHigerank) {
        this.anHigerank = anHigerank;
    }

    /**
     * @return App NEW 最低排名
     */
    public Integer getAnLowrank() {
        return anLowrank;
    }

    /**
     * @param anlowrank 
	 *            App NEW 最低排名
     */
    public void setAnLowrank(Integer anLowrank) {
        this.anLowrank = anLowrank;
    }

    /**
     * @return Game HOT 最高排名
     */
    public Integer getGhHigerank() {
        return ghHigerank;
    }

    /**
     * @param ghhigerank 
	 *            Game HOT 最高排名
     */
    public void setGhHigerank(Integer ghHigerank) {
        this.ghHigerank = ghHigerank;
    }

    /**
     * @return Game HOT 最低排名
     */
    public Integer getGhLowrank() {
        return ghLowrank;
    }

    /**
     * @param ghlowrank 
	 *            Game HOT 最低排名
     */
    public void setGhLowrank(Integer ghLowrank) {
        this.ghLowrank = ghLowrank;
    }

    /**
     * @return Game TOP 最高排名
     */
    public Integer getGtHigerank() {
        return gtHigerank;
    }

    /**
     * @param gthigerank 
	 *            Game TOP 最高排名
     */
    public void setGtHigerank(Integer gtHigerank) {
        this.gtHigerank = gtHigerank;
    }

    /**
     * @return Game TOP 最低排名
     */
    public Integer getGtLowrank() {
        return gtLowrank;
    }

    /**
     * @param gtlowrank 
	 *            Game TOP 最低排名
     */
    public void setGtLowrank(Integer gtLowrank) {
        this.gtLowrank = gtLowrank;
    }

    /**
     * @return Game NEW 最高排名
     */
    public Integer getGnHigerank() {
        return gnHigerank;
    }

    /**
     * @param gnhigerank 
	 *            Game NEW 最高排名
     */
    public void setGnHigerank(Integer gnHigerank) {
        this.gnHigerank = gnHigerank;
    }

    /**
     * @return Game NEW 最低排名
     */
    public Integer getGnLowrank() {
        return gnLowrank;
    }

    /**
     * @param gnlowrank 
	 *            Game NEW 最低排名
     */
    public void setGnLowrank(Integer gnLowrank) {
        this.gnLowrank = gnLowrank;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        this.createTimeString = DateUtil.getTimestampToString(createTime);
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        this.updateTimeString = DateUtil.getTimestampToString(updateTime);
    }
    /**
     * @return 大小
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * @param filesize 
	 *            大小
     */
    public void setFileSize(Integer fileSize) {
    	this.fileSize = FileUtil.sizeFormat(fileSize);
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
		this.setCountryName(country.getName());
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCreateTimeString() {
		return createTimeString;
	}

	public String getUpdateTimeString() {
		return updateTimeString;
	}

    
}