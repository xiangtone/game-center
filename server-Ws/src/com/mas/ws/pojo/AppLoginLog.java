package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class AppLoginLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 客户端编号
     */
    private Integer clientId;

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 帐号
     */
    private String userName;

    /**
     * 呢称
     */
    private String nickName;

    private String userPwd;

    private Integer appId;

    private String apkKey;

    private Integer cpId;

    private Integer serverId;

    private Integer channelId;

    private String sessionId;

    /**
     * 登陆入口 1启动激活登陆，2注册登陆，3正常登陆
     */
    private Integer entrance;

    private Date createTime;

    /**
     * 登陆时IP
     */
    private String IP;

    /**
     * 登陆时国家
     */
    private String country;

    /**
     * 登陆时省份
     */
    private String province;

    /**
     * 登陆时城市
     */
    private String city;

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
     * @return 用户编号
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userid 
	 *            用户编号
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return 帐号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param username 
	 *            帐号
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return 呢称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * @param nickname 
	 *            呢称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getApkKey() {
        return apkKey;
    }

    public void setApkKey(String apkKey) {
        this.apkKey = apkKey;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return 登陆入口 1启动激活登陆，2注册登陆，3正常登陆
     */
    public Integer getEntrance() {
        return entrance;
    }

    /**
     * @param entrance 
	 *            登陆入口 1启动激活登陆，2注册登陆，3正常登陆
     */
    public void setEntrance(Integer entrance) {
        this.entrance = entrance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 登陆时IP
     */
    public String getIP() {
        return IP;
    }

    /**
     * @param ip 
	 *            登陆时IP
     */
    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * @return 登陆时国家
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country 
	 *            登陆时国家
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return 登陆时省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province 
	 *            登陆时省份
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return 登陆时城市
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city 
	 *            登陆时城市
     */
    public void setCity(String city) {
        this.city = city;
    }
}