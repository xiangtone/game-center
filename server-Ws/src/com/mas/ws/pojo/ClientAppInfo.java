package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class ClientAppInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 客户端编号
     */
    private Integer clientId;

    /**
     * app应用名称
     */
    private String appName;

    /**
     * 应用包名
     */
    private String appPackageName;

    private String appVersionName;

    private Integer appVersionCode;

    /**
     * 最后更新时用户是否安装
     */
    private Boolean atUse;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 客户端编号
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientid 
	 *            客户端编号
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
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
     * @return 应用包名
     */
    public String getAppPackageName() {
        return appPackageName;
    }

    /**
     * @param apppackagename 
	 *            应用包名
     */
    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public Integer getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(Integer appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    /**
     * @return 最后更新时用户是否安装
     */
    public Boolean getAtUse() {
        return atUse;
    }

    /**
     * @param atuse 
	 *            最后更新时用户是否安装
     */
    public void setAtUse(Boolean atUse) {
        this.atUse = atUse;
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
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updatetime 
	 *            更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}