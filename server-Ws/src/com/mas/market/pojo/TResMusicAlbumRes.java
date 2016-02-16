package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TResMusicAlbumRes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 平台Id
     */
    private Integer raveId;

    /**
     * 音乐专辑Id
     */
    private Integer themeId;

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
     * 排序（按数字大小从大到小排序）
     */
    private Integer sort;

    /**
     * 对应的t_res_music的Id
     */
    private Integer musicId;

    /**
     * music名称
     */
    private String musicName;

    private String artist;

    /**
     * 0公共资源1平台2自运营
     */
    private Integer free;

    /**
     * 图标
     */
    private String logo;

    /**
     * 简介
     */
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
     * 大小
     */
    private Integer fileSize;

    /**
     * 时长
     */
    private Integer duration;

    /**
     * 下载地址
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
    
    //new榜单上线时间
    private String onlineTime;
    //hot榜单下载量
    private String downloadNum;

    public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
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
     * @return 音乐专辑Id
     */
    public Integer getThemeId() {
        return themeId;
    }

    /**
     * @param themeid 
	 *            音乐专辑Id
     */
    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
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
     * @return 排序（按数字大小从大到小排序）
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 
	 *            排序（按数字大小从大到小排序）
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return 对应的t_res_music的Id
     */
    public Integer getMusicId() {
        return musicId;
    }

    /**
     * @param musicid 
	 *            对应的t_res_music的Id
     */
    public void setMusicId(Integer musicId) {
        this.musicId = musicId;
    }

    /**
     * @return music名称
     */
    public String getMusicName() {
        return musicName;
    }

    /**
     * @param musicname 
	 *            music名称
     */
    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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

    /**
     * @return 简介
     */
    public String getBrief() {
        return brief;
    }

    /**
     * @param brief 
	 *            简介
     */
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
     * @return 大小
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * @param filesize 
	 *            大小
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return 时长
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param duration 
	 *            时长
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
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
}