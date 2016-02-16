package com.mas.rave.main.vo;

import java.util.Date;

public class ClientFeedback extends PagingBean{
	
	private int id;
	
	private String imei; //机器序列号
	
	private Integer clientId;
	
	private int userId;
	
	private String userName;
	
	private String email;
	
	private String content;//内容
	
	private String deviceModel;//手机机型
		
	private String deviceVendor;//手机厂商
	
	private String osVersion;//系统版本号
	
	private String osVersionName;//系统版本名字
	
	private int deviceType;//1手机，2平板
	
	private boolean state;//是否显示０不显示 1显示
	
	
	private String masPackageName; //zapp包名
	
	private String masVersionName;//版本名
	
	private int masVersionCode;//版本号
	
	private int feedbackType;  //1用户回馈、2运营回答
	
	private String IP;    //ip地址
	
	private String country; //国家
	
	private String province;//省份
	
	private String city; //城市
	
	private Date createTime;//回馈时间
	
	private Boolean lookOver;
	
	private Boolean replyOrNot;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceVendor() {
		return deviceVendor;
	}

	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOsVersionName() {
		return osVersionName;
	}

	public void setOsVersionName(String osVersionName) {
		this.osVersionName = osVersionName;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}



	public String getMasPackageName() {
		return masPackageName;
	}

	public void setMasPackageName(String masPackageName) {
		this.masPackageName = masPackageName;
	}


	public String getMasVersionName() {
		return masVersionName;
	}

	public void setMasVersionName(String masVersionName) {
		this.masVersionName = masVersionName;
	}


	public int getMasVersionCode() {
		return masVersionCode;
	}

	public void setMasVersionCode(int masVersionCode) {
		this.masVersionCode = masVersionCode;
	}

	public int getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(int feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Boolean getLookOver() {
		return lookOver;
	}

	public void setLookOver(Boolean lookOver) {
		this.lookOver = lookOver;
	}

	public Boolean getReplyOrNot() {
		return replyOrNot;
	}

	public void setReplyOrNot(Boolean replyOrNot) {
		this.replyOrNot = replyOrNot;
	}
	

	
}
