package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TAppComment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer amount;
    private String publishTime;
    private String nickName;
    /**
     * appId
     */
    private Integer appId;

    private Integer clientId;

    private Integer userId;

    private String userName;

    /**
     * 星级
     */
    private Integer stars;

    /**
     * 内容
     */
    private String content;

    /**
     * 手机机型
     */
    private String deviceModel;

    /**
     * 手机厂商
     */
    private String deviceVendor;

    /**
     * 系统版本号
     */
    private String osVersion;

    /**
     * 系统版本名字
     */
    private String osVersionName;

    /**
     * 1手机，2平板
     */
    private Integer deviceType;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 是否显示评论０不显示 1显示
     */
    private Boolean state;

    /**
     * 评论时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return appId
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * @param appid 
	 *            appId
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
     * @return 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content 
	 *            内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return 手机机型
     */
    public String getDeviceModel() {
        return deviceModel;
    }

    /**
     * @param devicemodel 
	 *            手机机型
     */
    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    /**
     * @return 手机厂商
     */
    public String getDeviceVendor() {
        return deviceVendor;
    }

    /**
     * @param devicevendor 
	 *            手机厂商
     */
    public void setDeviceVendor(String deviceVendor) {
        this.deviceVendor = deviceVendor;
    }

    /**
     * @return 系统版本号
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * @param osversion 
	 *            系统版本号
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * @return 系统版本名字
     */
    public String getOsVersionName() {
        return osVersionName;
    }

    /**
     * @param osversionname 
	 *            系统版本名字
     */
    public void setOsVersionName(String osVersionName) {
        this.osVersionName = osVersionName;
    }

    /**
     * @return 1手机，2平板
     */
    public Integer getDeviceType() {
        return deviceType;
    }

    /**
     * @param devicetype 
	 *            1手机，2平板
     */
    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return 顺序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 
	 *            顺序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return 是否显示评论０不显示 1显示
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            是否显示评论０不显示 1显示
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * @return 评论时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createtime 
	 *            评论时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}