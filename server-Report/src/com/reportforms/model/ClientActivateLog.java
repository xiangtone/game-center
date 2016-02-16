package com.reportforms.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.reportforms.util.DateUtil;

public class ClientActivateLog extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -20928305179966323L;
	
	private Integer id;
	
	private Integer cpId;
	
	private Integer appId;
	
	private String appName;
	
	private App app;
	
	private String apkKey;
	
	private Integer clientId;
	
	private Integer serverId;
	
	private Integer userId;
	
	private String userName;
	
	private String userPwd;
	
	private String ip;
	
	private String country;
	
	private String province;
	
	private String city;
	
	private String area;
	
	private String sessionId;
	
	private String appPackageName;
	
	private String appVersionName;
	
	private Integer appVersionCode;
	
	private String masPackageName;

	private String masVersionName;
	
	private Integer masVersionCode;
	
	private Timestamp createTime;
	
	private String createTimeString;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getApkKey() {
		return apkKey;
	}

	public void setApkKey(String apkKey) {
		this.apkKey = apkKey;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
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

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getArea() {
		StringBuilder area  = new StringBuilder();
		if(StringUtils.isNotBlank(this.country)){
			area.append(this.country);
			
		}
		if(StringUtils.isNotBlank(this.province)){
			area.append("-");
			area.append(this.province);
		}
		if(StringUtils.isNotBlank(this.city)){
			area.append("-");
			area.append(this.city);
		}
		return area.toString();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAppPackageName() {
		return appPackageName;
	}

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

	public Integer getMasVersionCode() {
		return masVersionCode;
	}

	public void setMasVersionCode(Integer masVersionCode) {
		this.masVersionCode = masVersionCode;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
		this.createTimeString = DateUtil.getTimestampToString(createTime);
	}

	public String getCreateTimeString() {
		return createTimeString;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
}
