package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TAppFile implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 游戏编号
     */
    private Integer id;

    /**
     * appId
     */
    private Integer appId;

    /**
     * app应用名称
     */
    private String appName;

    /**
     * 子渠道id
     */
    private Integer channelId;

    /**
     * cpid
     */
    private Integer cpId;

    /**
     * 文件key
     */
    private String apkKey;

    /**
     * 服务器id
     */
    private Integer serverId;

    /**
     * 1不更新，2下拉框更新，3对话框更新
     */
    private Integer upgradeType;

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
     * 文件地址
     */
    private String path;

    /**
     * 操作系统类型 1,ios ２.andriod
     */
    private Integer osType;

    /**
     * 分辨率（默认all是支持所有的，如果有单独的就查询分该辨率，以|隔开，如果查不到就弹出框）
     */
    private String resolution;

    /**
     * 大小
     */
    private Integer fileSize;

    /**
     * 游戏厂商分配的游戏的渠道号
     */
    private String cpChannelCode;

    private String updateInfo;

    /**
     * 语言 1,中文 2,英语 3,其他
     */
    private Integer language;

    /**
     * 备注
     */
    private String remark;

    /**
     * 有效值.0无效 1有效
     */
    private Boolean state;

    private Date createTime;

    private Date updateTime;

    /**
     * 是否存在在历史版本
     */
    private Boolean haslist;

    /**
     * @return 游戏编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id 
	 *            游戏编号
     */
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
     * @return 子渠道id
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelid 
	 *            子渠道id
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return cpid
     */
    public Integer getCpId() {
        return cpId;
    }

    /**
     * @param cpid 
	 *            cpid
     */
    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    /**
     * @return 文件key
     */
    public String getApkKey() {
        return apkKey;
    }

    /**
     * @param apkkey 
	 *            文件key
     */
    public void setApkKey(String apkKey) {
        this.apkKey = apkKey;
    }

    /**
     * @return 服务器id
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * @param serverid 
	 *            服务器id
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    /**
     * @return 1不更新，2下拉框更新，3对话框更新
     */
    public Integer getUpgradeType() {
        return upgradeType;
    }

    /**
     * @param upgradetype 
	 *            1不更新，2下拉框更新，3对话框更新
     */
    public void setUpgradeType(Integer upgradeType) {
        this.upgradeType = upgradeType;
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
     * @return 文件地址
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path 
	 *            文件地址
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return 操作系统类型 1,ios ２.andriod
     */
    public Integer getOsType() {
        return osType;
    }

    /**
     * @param ostype 
	 *            操作系统类型 1,ios ２.andriod
     */
    public void setOsType(Integer osType) {
        this.osType = osType;
    }

    /**
     * @return 分辨率（默认all是支持所有的，如果有单独的就查询分该辨率，以|隔开，如果查不到就弹出框）
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * @param resolution 
	 *            分辨率（默认all是支持所有的，如果有单独的就查询分该辨率，以|隔开，如果查不到就弹出框）
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
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
     * @return 游戏厂商分配的游戏的渠道号
     */
    public String getCpChannelCode() {
        return cpChannelCode;
    }

    /**
     * @param cpchannelcode 
	 *            游戏厂商分配的游戏的渠道号
     */
    public void setCpChannelCode(String cpChannelCode) {
        this.cpChannelCode = cpChannelCode;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    /**
     * @return 语言 1,中文 2,英语 3,其他
     */
    public Integer getLanguage() {
        return language;
    }

    /**
     * @param language 
	 *            语言 1,中文 2,英语 3,其他
     */
    public void setLanguage(Integer language) {
        this.language = language;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark 
	 *            备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return 有效值.0无效 1有效
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            有效值.0无效 1有效
     */
    public void setState(Boolean state) {
        this.state = state;
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

    /**
     * @return 是否存在在历史版本
     */
    public Boolean getHaslist() {
        return haslist;
    }

    /**
     * @param haslist 
	 *            是否存在在历史版本
     */
    public void setHaslist(Boolean haslist) {
        this.haslist = haslist;
    }
}