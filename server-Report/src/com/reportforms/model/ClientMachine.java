package com.reportforms.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.reportforms.util.DateUtil;

public class ClientMachine extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7375119473005434863L;
	
	private Integer id;
	
	private Integer clientId;
	
	private String clientIdString;

	private String phone;
	
	private String imei;
	
	private String mac;
	
	private String imsi;
	
	private String deviceModel;
	
	private String deviceVendor;
	
	private Integer deviceType;
	
	private String deviceTypeString;
	
	private String netType;
	
	private String osVersion;
	
	private Timestamp createTime;
	
	private Timestamp updateTime;
	
	private String createTimeString;
	
	private String updateTimeString;
	
	private String ip;
	
	private String country;
	
	private String province;
	
	private String city;
	
	private String area;
	
	private Integer activeNum;
	
	private String appPackageName;
	
	private String appVersionName;
	
	private Integer appVersionCode;

	
	/*add by dingjie 2014-08-12*/
	private String appPackageNameFirst;
	private String appVersionNameFirst;
	private Integer appVersionCodeFirst;
	
	private String zappVersionName;
	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	
	public String getClientIdString() {
		if( null == this.clientId){
			this.clientIdString = "未激活";
		}else{
			this.clientIdString = "已激活";
		}
		return clientIdString;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
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

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
		if(deviceType.intValue() == 1){
			this.deviceTypeString = "手机";
		}else if(deviceType.intValue() == 2){
			this.deviceTypeString = "平板";
		}else{
			this.deviceTypeString = "其它";
		}
	}

	public String getDeviceTypeString() {
		return deviceTypeString;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
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

	public String getUpdateTimeString() {
		return updateTimeString;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
		this.updateTimeString = DateUtil.getTimestampToString(updateTime);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public Integer getActiveNum() {
		return activeNum;
	}

	public void setActiveNum(Integer activeNum) {
		this.activeNum = activeNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getAppPackageNameFirst() {
		return appPackageNameFirst;
	}

	public void setAppPackageNameFirst(String appPackageNameFirst) {
		this.appPackageNameFirst = appPackageNameFirst;
	}

	public String getAppVersionNameFirst() {
		return appVersionNameFirst;
	}

	public void setAppVersionNameFirst(String appVersionNameFirst) {
		this.appVersionNameFirst = appVersionNameFirst;
	}

	public Integer getAppVersionCodeFirst() {
		return appVersionCodeFirst;
	}

	public void setAppVersionCodeFirst(Integer appVersionCodeFirst) {
		this.appVersionCodeFirst = appVersionCodeFirst;
	}

	public String getZappVersionName() {
		StringBuilder zappVersionName  = new StringBuilder();
		if(StringUtils.isNotBlank(this.appVersionName)){
			zappVersionName.append(this.appVersionName);
		}
		if(StringUtils.isNotBlank(this.appVersionNameFirst)){
			zappVersionName.append("-");
			zappVersionName.append(this.appVersionNameFirst);
		}
		return zappVersionName.toString();
	}

	public void setZappVersionName(String zappVersionName) {
		this.zappVersionName = zappVersionName;
	}

}
