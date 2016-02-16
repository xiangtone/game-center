package com.mas.data;


public class DataPay {
	
	private String orderId;
    
	private String orderIdCp;
	private Integer orderStatus;
    private String orderDesc;

    private Integer orderValue;
    private Integer aValue;
    private Integer aValuePresent;
    private Integer exchangeAValue;
    private Integer exchangeAValuePresent;
    
    private Integer carValue;
    private String rechargeTime;
	private String rechargeType;
    
    private Integer cpId;
	
	private Integer appId;
	
	private Integer channelId;
	
	private Integer serverId;
	
	private String apkKey;
	
	private Integer clientId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public Integer getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(Integer orderValue) {
		this.orderValue = orderValue;
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

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getOrderIdCp() {
		return orderIdCp;
	}

	public void setOrderIdCp(String orderIdCp) {
		this.orderIdCp = orderIdCp;
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

	public Integer getCarValue() {
		return carValue;
	}

	public void setCarValue(Integer carValue) {
		this.carValue = carValue;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getApkKey() {
		return apkKey;
	}

	public void setApkKey(String apkKey) {
		this.apkKey = apkKey;
	}

	public String getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(String rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public String getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}

	public Integer getExchangeAValue() {
		return exchangeAValue;
	}

	public void setExchangeAValue(Integer exchangeAValue) {
		this.exchangeAValue = exchangeAValue;
	}

	public Integer getExchangeAValuePresent() {
		return exchangeAValuePresent;
	}

	public void setExchangeAValuePresent(Integer exchangeAValuePresent) {
		this.exchangeAValuePresent = exchangeAValuePresent;
	}
}
