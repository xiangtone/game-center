package com.reportforms.model;

import java.sql.Timestamp;

import com.reportforms.util.DateUtil;

public class CpAccount extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1548622160043160365L;
	
	private Integer id;
	
	private Integer userId;
	
	private String userName;
	
	private String orderId;
	
	private String orderIdCp;
	
	private Integer cpId;
	
	private Integer appId;
	
	private Integer channelId;
	
	private Float orderValue;
	
	private Timestamp rechargeTime;
	
	private String rechargeTimeString;
	
	private String remark;
	
	private String channelName;//渠道名称
	
	private String cpName;//游戏厂商名称
	
	private String appName;//产品名称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Timestamp getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(Timestamp rechargeTime) {
		this.rechargeTime = rechargeTime;
		this.rechargeTimeString = DateUtil.getTimestampToString(rechargeTime);
	}

	public String getRechargeTimeString() {
		return rechargeTimeString;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderIdCp() {
		return orderIdCp;
	}

	public void setOrderIdCp(String orderIdCp) {
		this.orderIdCp = orderIdCp;
	}

	public Integer getCpId() {
		return cpId;
	}

	public void setCpId(Integer cpId) {
		this.cpId = cpId;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Float getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(Float orderValue) {
		this.orderValue = orderValue;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCpName() {
		return cpName;
	}

	public void setCpName(String cpName) {
		this.cpName = cpName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
