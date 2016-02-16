package com.reportforms.model;

import java.sql.Timestamp;

import com.reportforms.util.DateUtil;

/**
 * @2013/12/23
 * @author lisong.lan
 * @version 1.0
 * 与HP的对账类
 * 对应t_hp_account table
 *
 */
public class HpAccount extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7739406409001879172L;
	
	private Integer id;
	
	private Integer userId;//用户编号
	
	private String userName;//用户账号
	
	private Integer cpId;//厂商编号
	
	private Integer appId;//产品编号
	
	private Integer channelId;//渠道编号
	
	private String country;//国家
	
	private String orderId;//订单号-CP
	
	private String trxId;//mas
	
	private String cardNo;//卡号
	
	private String rechargeType;//充值类型
	
	private Float exchangeAValue;//充值金额
	
	private Float rechargeValue;//充值面额
	
	private Timestamp rechargeTime;//充值时间
	
	private String rechargeTimeString;
	
	private String remark;//备注
	
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}

	public Float getExchangeAValue() {
		return exchangeAValue;
	}

	public void setExchangeAValue(Float exchangeAValue) {
		this.exchangeAValue = exchangeAValue;
	}

	public Float getRechargeValue() {
		return rechargeValue;
	}

	public void setRechargeValue(Float rechargeValue) {
		this.rechargeValue = rechargeValue;
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
