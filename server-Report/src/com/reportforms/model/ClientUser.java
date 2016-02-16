package com.reportforms.model;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.reportforms.util.DateUtil;

public class ClientUser extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8347123829195802501L;
	
	private Integer clientId;
	
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
	
	private String createTimeString;
	
	private String ip;
	
	private String country;
	
	private String province;
	
	private String city;
	
	private String area;
	
	private Integer activeNum;

	/*add by dingjie 2014-08-12*/
	private String appPackageNameFirst;
	private String appVersionNameFirst;
	private Integer appVersionCodeFirst;
	private String appPackageNameLast;
	private String appVersionNameLast;
	private Integer	appVersionCodeLast;
	
	private String zappVersionName;
	
	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
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

	public String getAppPackageNameLast() {
		return appPackageNameLast;
	}

	public void setAppPackageNameLast(String appPackageNameLast) {
		this.appPackageNameLast = appPackageNameLast;
	}

	public String getAppVersionNameLast() {
		return appVersionNameLast;
	}

	public void setAppVersionNameLast(String appVersionNameLast) {
		this.appVersionNameLast = appVersionNameLast;
	}

	public Integer getAppVersionCodeLast() {
		return appVersionCodeLast;
	}

	public void setAppVersionCodeLast(Integer appVersionCodeLast) {
		this.appVersionCodeLast = appVersionCodeLast;
	}

	public String getZappVersionName() {
		StringBuilder zappVersionName  = new StringBuilder();
		if(StringUtils.isNotBlank(this.appVersionNameFirst)){
			zappVersionName.append(this.appVersionNameFirst);
		}
		if(StringUtils.isNotBlank(this.appVersionNameLast)){
			zappVersionName.append("-");
			zappVersionName.append(this.appVersionNameLast);
		}
		return zappVersionName.toString();
	}

	public void setZappVersionName(String zappVersionName) {
		this.zappVersionName = zappVersionName;
	}

}
