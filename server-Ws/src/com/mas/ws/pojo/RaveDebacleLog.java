package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class RaveDebacleLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer clientId;

    private String masPackageName;

    private String masVersionName;

    private Integer masVersionCode;

    /**
     * 崩溃内容
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
     * 是否显示评论０不显示 1显示
     */
    private Boolean state;

    /**
     * 崩溃时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getMasPackageName() {
        return masPackageName;
    }

    public void setMasPackageName(String masPackageName) {
        this.masPackageName = masPackageName;
    }

    public String getMasVersionName() {
        return masVersionName;
    }

    public void setMasVersionName(String masVersionName) {
        this.masVersionName = masVersionName;
    }

    public Integer getMasVersionCode() {
        return masVersionCode;
    }

    public void setMasVersionCode(Integer masVersionCode) {
        this.masVersionCode = masVersionCode;
    }

    /**
     * @return 崩溃内容
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content 
	 *            崩溃内容
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
     * @return 崩溃时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createtime 
	 *            崩溃时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}