package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TClientAppInfo implements Serializable {
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
     * 客户端应用包名
     */
    private String appPackageName;

    /**
     * 客户端版本名
     */
    private String appVersionName;

    /**
     * 客户端版本号
     */
    private Integer appVersionCode;

    /**
     * 客户端应用MD5
     */
    private String appMD5;

    /**
     * 服务器端版本号
     */
    private Integer svAppVersionCode;

    /**
     * 服务器端版本名
     */
    private String svAppVersionName;

    /**
     * 有没有增加包
     */
    private Boolean isUpdate;

    /**
     * 有没有增量包
     */
    private Boolean isPatch;

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
     * @return 客户端应用包名
     */
    public String getAppPackageName() {
        return appPackageName;
    }

    /**
     * @param apppackagename 
	 *            客户端应用包名
     */
    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    /**
     * @return 客户端版本名
     */
    public String getAppVersionName() {
        return appVersionName;
    }

    /**
     * @param appversionname 
	 *            客户端版本名
     */
    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    /**
     * @return 客户端版本号
     */
    public Integer getAppVersionCode() {
        return appVersionCode;
    }

    /**
     * @param appversioncode 
	 *            客户端版本号
     */
    public void setAppVersionCode(Integer appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    /**
     * @return 客户端应用MD5
     */
    public String getAppMD5() {
        return appMD5;
    }

    /**
     * @param appmd5 
	 *            客户端应用MD5
     */
    public void setAppMD5(String appMD5) {
        this.appMD5 = appMD5;
    }

    /**
     * @return 服务器端版本号
     */
    public Integer getSvAppVersionCode() {
        return svAppVersionCode;
    }

    /**
     * @param svappversioncode 
	 *            服务器端版本号
     */
    public void setSvAppVersionCode(Integer svAppVersionCode) {
        this.svAppVersionCode = svAppVersionCode;
    }

    /**
     * @return 服务器端版本名
     */
    public String getSvAppVersionName() {
        return svAppVersionName;
    }

    /**
     * @param svappversionname 
	 *            服务器端版本名
     */
    public void setSvAppVersionName(String svAppVersionName) {
        this.svAppVersionName = svAppVersionName;
    }

    /**
     * @return 有没有增加包
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @param isupdate 
	 *            有没有增加包
     */
    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * @return 有没有增量包
     */
    public Boolean getIsPatch() {
        return isPatch;
    }

    /**
     * @param ispatch 
	 *            有没有增量包
     */
    public void setIsPatch(Boolean isPatch) {
        this.isPatch = isPatch;
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