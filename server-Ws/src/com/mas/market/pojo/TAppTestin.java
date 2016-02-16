package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TAppTestin implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * testIn测试查询编号
     */
    private String adaptId;

    /**
     * testIn测试报告调用地址
     */
    private String reportUrl;

    /**
     * 对应t_app_file表的Id
     */
    private Integer apkId;

    /**
     * 对应的t_app_info的Id
     */
    private Integer appId;

    /**
     * app应用名称
     */
    private String appName;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 版本名
     */
    private String versionName;

    /**
     * 版本号
     */
    private Integer versionCode;

    private Date createTime;

    private Date updateTime;

    /**
     * @return testIn测试查询编号
     */
    public String getAdaptId() {
        return adaptId;
    }

    /**
     * @param adaptid 
	 *            testIn测试查询编号
     */
    public void setAdaptId(String adaptId) {
        this.adaptId = adaptId;
    }

    /**
     * @return testIn测试报告调用地址
     */
    public String getReportUrl() {
        return reportUrl;
    }

    /**
     * @param reporturl 
	 *            testIn测试报告调用地址
     */
    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    /**
     * @return 对应t_app_file表的Id
     */
    public Integer getApkId() {
        return apkId;
    }

    /**
     * @param apkid 
	 *            对应t_app_file表的Id
     */
    public void setApkId(Integer apkId) {
        this.apkId = apkId;
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
}