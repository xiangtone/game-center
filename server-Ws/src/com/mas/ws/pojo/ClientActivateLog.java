package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class ClientActivateLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 客户端编号
     */
    private Integer clientId;

    /**
     * imei机器序列号
     */
    private String imei;

    /**
     * 网卡(wifi)地址
     */
    private String mac;

    /**
     * cpID
     */
    private Integer cpId;

    /**
     * 应用编号
     */
    private Integer appId;

    private String apkKey;

    /**
     * 渠道编号
     */
    private Integer channelId;

    /**
     * 应用服务器ID
     */
    private Integer serverId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 游戏中的用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPwd;

    /**
     * ip地址
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
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 目标手机所在的蜂窝小区ID
     */
    private Integer cellId;

    /**
     * lac，location area code 位置区码
     */
    private Integer lac;

    /**
     * MCC(移动国家码)和 MNC(移动网络码)
     */
    private Integer mcc;

    /**
     * MCC(移动国家码)和 MNC(移动网络码)
     */
    private Integer mnc;

    /**
     * 值
     */
    private String sessionId;

    /**
     * 应用包名
     */
    private String appPackageName;

    private String appVersionName;

    private Integer appVersionCode;

    /**
     * masSDK包名
     */
    private String masPackageName;

    private String masVersionName;

    private Integer masVersionCode;

    private Date createTime;

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
     * @return imei机器序列号
     */
    public String getImei() {
        return imei;
    }

    /**
     * @param imei 
	 *            imei机器序列号
     */
    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * @return 网卡(wifi)地址
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac 
	 *            网卡(wifi)地址
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return cpID
     */
    public Integer getCpId() {
        return cpId;
    }

    /**
     * @param cpid 
	 *            cpID
     */
    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    /**
     * @return 应用编号
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * @param appid 
	 *            应用编号
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getApkKey() {
        return apkKey;
    }

    public void setApkKey(String apkKey) {
        this.apkKey = apkKey;
    }

    /**
     * @return 渠道编号
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelid 
	 *            渠道编号
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return 应用服务器ID
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * @param serverid 
	 *            应用服务器ID
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
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
     * @return 密码
     */
    public String getUserPwd() {
        return userPwd;
    }

    /**
     * @param userpwd 
	 *            密码
     */
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    /**
     * @return ip地址
     */
    public String getIP() {
        return IP;
    }

    /**
     * @param ip 
	 *            ip地址
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
     * @return 纬度
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude 
	 *            纬度
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return 经度
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude 
	 *            经度
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return 目标手机所在的蜂窝小区ID
     */
    public Integer getCellId() {
        return cellId;
    }

    /**
     * @param cellid 
	 *            目标手机所在的蜂窝小区ID
     */
    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    /**
     * @return lac，location area code 位置区码
     */
    public Integer getLac() {
        return lac;
    }

    /**
     * @param lac 
	 *            lac，location area code 位置区码
     */
    public void setLac(Integer lac) {
        this.lac = lac;
    }

    /**
     * @return MCC(移动国家码)和 MNC(移动网络码)
     */
    public Integer getMcc() {
        return mcc;
    }

    /**
     * @param mcc 
	 *            MCC(移动国家码)和 MNC(移动网络码)
     */
    public void setMcc(Integer mcc) {
        this.mcc = mcc;
    }

    /**
     * @return MCC(移动国家码)和 MNC(移动网络码)
     */
    public Integer getMnc() {
        return mnc;
    }

    /**
     * @param mnc 
	 *            MCC(移动国家码)和 MNC(移动网络码)
     */
    public void setMnc(Integer mnc) {
        this.mnc = mnc;
    }

    /**
     * @return 值
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionid 
	 *            值
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
     * @return masSDK包名
     */
    public String getMasPackageName() {
        return masPackageName;
    }

    /**
     * @param maspackagename 
	 *            masSDK包名
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}