package com.mas.data;

import com.mas.util.MessageDigestUtil;

public class CallbackData {
	
	private String orderId;
	
	private String cpOrderId;
	
    private Integer orderValue;
    
	private Integer userId;
	
	private String chargeType = "AVoucher";
	
	private Integer appId;
	
	private Integer serverId;
	
	private String timeNow;

	public String setSign(Integer cpId,String apkKey){
		return MessageDigestUtil.getMD5(cpId+"appId="+appId+"chargeType="+chargeType+"cpOrderId="+cpOrderId+"orderId="+orderId+"orderValue="+orderValue+"serverId="+serverId+"timeNow="+timeNow+"userId="+userId+apkKey);
	}
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCpOrderId() {
		return cpOrderId;
	}

	public void setCpOrderId(String cpOrderId) {
		this.cpOrderId = cpOrderId;
	}

	public Integer getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(Integer orderValue) {
		this.orderValue = orderValue;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public String getTimeNow() {
		return timeNow;
	}

	public void setTimeNow(String timeNow) {
		this.timeNow = timeNow;
	}
}
