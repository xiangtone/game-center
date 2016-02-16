package com.reportforms.model;

import java.sql.Timestamp;

public class RaveDebacleLog {
	
	private Integer id;
	
	private Integer clientId;
	
	private String masPackageName;
	
	private String masVersionName;
	
	private Integer masVersionCode;
	
	private String content;
	
	private String deviceModel;
	
	private String deviceVendor;
	
	private String osVersion;
	
	private String osVersionName;
	
	private Integer deviceType;
	
	private boolean state;
	
	private Timestamp createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
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

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
