package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TResMusic implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Integer id;

    /**
     * 国家ID
     */
    private Integer raveId;

    /**
     * 音乐名
     */
    private String name;

    /**
     * 歌唱者
     */
    private String artist;

    private String pinyin;

    /**
     * 图标
     */
    private String logo;

    /**
     * 资源下载
     */
    private String url;

    /**
     * 大小
     */
    private Integer fileSize;

    /**
     * 资费
     */
    private Integer free;

    /**
     * 星级
     */
    private Integer stars;

    /**
     * 时间长度
     */
    private Integer duration;

    /**
     * 类型（风格）
     */
    private Integer categoryId;

    /**
     * 类型描述
     */
    private String categoryName;

    private String brief;

    /**
     * 描述
     */
    private String description;

    private Integer initDowdload;

    private Integer realDowdload;

    private Date createTime;

    private Date updateTime;

    private Boolean state;

    private String operator;

    /**
     * @return 编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id 
	 *            编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 国家ID
     */
    public Integer getRaveId() {
        return raveId;
    }

    /**
     * @param raveid 
	 *            国家ID
     */
    public void setRaveId(Integer raveId) {
        this.raveId = raveId;
    }

    /**
     * @return 音乐名
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 
	 *            音乐名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 歌唱者
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @param artist 
	 *            歌唱者
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
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
     * @return 资源下载
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url 
	 *            资源下载
     */
    public void setUrl(String url) {
        this.url = url;
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
     * @return 资费
     */
    public Integer getFree() {
        return free;
    }

    /**
     * @param free 
	 *            资费
     */
    public void setFree(Integer free) {
        this.free = free;
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
     * @return 时间长度
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param duration 
	 *            时间长度
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * @return 类型（风格）
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryid 
	 *            类型（风格）
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return 类型描述
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryname 
	 *            类型描述
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    /**
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 
	 *            描述
     */
    public void setDescription(String description) {
        this.description = description;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}