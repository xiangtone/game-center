package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TAppPicture implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 应用&游戏编号
     */
    private Integer appId;

    /**
     * 资源地址
     */
    private String url;

    /**
     * 绝对路径
     */
    private String path;

    /**
     * 图片长度
     */
    private Integer length;

    /**
     * 图片尺寸
     */
    private Integer width;

    /**
     * 图片大小(byte)
     */
    private Integer fileSize;

    /**
     * 缩略图地址
     */
    private String thumbnailUrl;

    /**
     * 标题
     */
    private String title;

    /**
     * 长文字描述
     */
    private String description;

    /**
     * 图片类型 1.jpg 2.gif
     */
    private Integer fileType;

    private Boolean state;

    /**
     * 排序值
     */
    private Integer sort;

    private Date updateTime;

    private String operator;

    private String remark;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 应用&游戏编号
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * @param appid 
	 *            应用&游戏编号
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * @return 资源地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url 
	 *            资源地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return 绝对路径
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path 
	 *            绝对路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return 图片长度
     */
    public Integer getLength() {
        return length;
    }

    /**
     * @param length 
	 *            图片长度
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * @return 图片尺寸
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width 
	 *            图片尺寸
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return 图片大小(byte)
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * @param filesize 
	 *            图片大小(byte)
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return 缩略图地址
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     * @param thumbnailurl 
	 *            缩略图地址
     */
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * @return 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title 
	 *            标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return 长文字描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 
	 *            长文字描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 图片类型 1.jpg 2.gif
     */
    public Integer getFileType() {
        return fileType;
    }

    /**
     * @param filetype 
	 *            图片类型 1.jpg 2.gif
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * @return 排序值
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 
	 *            排序值
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}