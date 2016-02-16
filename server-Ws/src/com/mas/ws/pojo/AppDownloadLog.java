package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class AppDownloadLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 客户端编号
     */
    private Integer clientId;

    /**
     * 客户端国家ID
     */
    private Integer raveId;

    /**
     * ct= 栏目ID（同时约定  当应用是搜索进来的 ct=100、Homes广告位ct=101、Apps广告位ct=102、Games广告位ct=103、推荐ct=104、下载管理105、更新管理106、收藏107)。
     */
    private Integer columnId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 游戏中的用户名
     */
    private String userName;

    /**
     * 应用包名
     */
    private String masPackageName;

    private String masVersionName;

    private Integer masVersionCode;

    /**
     * appId
     */
    private Integer appId;

    /**
     * app应用名称
     */
    private String appName;

    /**
     * 应用包名
     */
    private String packageName;

    private String versionName;

    private Integer versionCode;

    /**
     * apkId
     */
    private Integer apkId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * IP
     */
    private String IP;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 1是下载、2是更新
     */
    private Integer downOrUpdate;

    /**
     * 0公共资源1平台2自运营3开发者后台CP上传
     */
    private Integer free;

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
     * @return 客户端国家ID
     */
    public Integer getRaveId() {
        return raveId;
    }

    /**
     * @param raveid 
	 *            客户端国家ID
     */
    public void setRaveId(Integer raveId) {
        this.raveId = raveId;
    }

    /**
     * @return ct= 栏目ID（同时约定  当应用是搜索进来的 ct=100、Homes广告位ct=101、Apps广告位ct=102、Games广告位ct=103、推荐ct=104、下载管理105、更新管理106、收藏107)。
     */
    public Integer getColumnId() {
        return columnId;
    }

    /**
     * @param columnid 
	 *            ct= 栏目ID（同时约定  当应用是搜索进来的 ct=100、Homes广告位ct=101、Apps广告位ct=102、Games广告位ct=103、推荐ct=104、下载管理105、更新管理106、收藏107)。
     */
    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    /**
     * @return 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userid 
	 *            用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return 游戏中的用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param username 
	 *            游戏中的用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return 应用包名
     */
    public String getMasPackageName() {
        return masPackageName;
    }

    /**
     * @param maspackagename 
	 *            应用包名
     */
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
     * @return 应用包名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packagename 
	 *            应用包名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return apkId
     */
    public Integer getApkId() {
        return apkId;
    }

    /**
     * @param apkid 
	 *            apkId
     */
    public void setApkId(Integer apkId) {
        this.apkId = apkId;
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
     * @return IP
     */
    public String getIP() {
        return IP;
    }

    /**
     * @param ip 
	 *            IP
     */
    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * @return 国家
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country 
	 *            国家
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province 
	 *            省份
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city 
	 *            城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return 1是下载、2是更新
     */
    public Integer getDownOrUpdate() {
        return downOrUpdate;
    }

    /**
     * @param downorupdate 
	 *            1是下载、2是更新
     */
    public void setDownOrUpdate(Integer downOrUpdate) {
        this.downOrUpdate = downOrUpdate;
    }

    /**
     * @return 0公共资源1平台2自运营3开发者后台CP上传
     */
    public Integer getFree() {
        return free;
    }

    /**
     * @param free 
	 *            0公共资源1平台2自运营3开发者后台CP上传
     */
    public void setFree(Integer free) {
        this.free = free;
    }
}