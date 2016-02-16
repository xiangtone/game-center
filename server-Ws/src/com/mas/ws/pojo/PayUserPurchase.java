package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class PayUserPurchase implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * amine订单号
     */
    private String orderId;

    /**
     * cp订单号（无时为null）
     */
    private String orderIdCp;

    /**
     * 消费aValue
     */
    private Integer orderValue;

    private Integer aValue;

    private Integer aValuePresent;

    private Date createTime;

    private String IP;

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

    private String userPwd;

    private Integer appId;

    private String apkKey;

    private Integer cpId;

    private Integer serverId;

    private Integer channelId;

    /**
     * session值
     */
    private String sessionId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 回调cp是否成功
     */
    private Boolean callback;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return amine订单号
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderid 
	 *            amine订单号
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return cp订单号（无时为null）
     */
    public String getOrderIdCp() {
        return orderIdCp;
    }

    /**
     * @param orderidcp 
	 *            cp订单号（无时为null）
     */
    public void setOrderIdCp(String orderIdCp) {
        this.orderIdCp = orderIdCp;
    }

    /**
     * @return 消费aValue
     */
    public Integer getOrderValue() {
        return orderValue;
    }

    /**
     * @param ordervalue 
	 *            消费aValue
     */
    public void setOrderValue(Integer orderValue) {
        this.orderValue = orderValue;
    }

    public Integer getaValue() {
        return aValue;
    }

    public void setaValue(Integer aValue) {
        this.aValue = aValue;
    }

    public Integer getaValuePresent() {
        return aValuePresent;
    }

    public void setaValuePresent(Integer aValuePresent) {
        this.aValuePresent = aValuePresent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
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

    /**
     * @return session值
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionid 
	 *            session值
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
     * @return 回调cp是否成功
     */
    public Boolean getCallback() {
        return callback;
    }

    /**
     * @param callback 
	 *            回调cp是否成功
     */
    public void setCallback(Boolean callback) {
        this.callback = callback;
    }
}