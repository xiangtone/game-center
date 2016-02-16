package com.x.publics.http.model;

import com.x.publics.utils.Constan;

public class PlatFormInitRequest extends CommonRequest {

	public PlatFormData data;
	public ClientActive clientActive;
	public ClientInfo clientInfo;
	public PlatFormInitMasUser masUser;

	public PlatFormInitRequest() {
		super(Constan.Rc.ACCCOUNT_START, Constan.SIGN);
	}

	public static class PlatFormInitMasUser {
		private String userName;
		private String userPwd;
		private int userId;

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

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

	}

	public static class ClientActive {
		public int cellId;
		public int lac;
		public double latitude;
		public double longitude;
		public int mnc;
		public int mcc ;
		
		public int getCellId() {
			return cellId;
		}

		public void setCellId(int cellId) {
			this.cellId = cellId;
		}

		public int getLac() {
			return lac;
		}

		public void setLac(int lac) {
			this.lac = lac;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public int getMnc() {
			return mnc;
		}

		public void setMnc(int mnc) {
			this.mnc = mnc;
		}

		public int getMcc() {
			return mcc;
		}

		public void setMcc(int mcc) {
			this.mcc = mcc;
		}

	}

	public static class ClientInfo {
		public String deviceModel;
		public int deviceType;
		public String deviceVendor;
		public String imei;
		public String imsi;
		public String mac;
		public String netType;
		public String osAddtional;
		public int osVersion;
		public String osVersionName;
		public String phone;
		public float screenDensity;
		public int screenHeight;
		public int screenWidth;
		public String serviceSupplier;
		public String smsc;

		public String getDeviceModel() {
			return deviceModel;
		}

		public void setDeviceModel(String deviceModel) {
			this.deviceModel = deviceModel;
		}

		public int getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(int deviceType) {
			this.deviceType = deviceType;
		}

		public String getDeviceVendor() {
			return deviceVendor;
		}

		public void setDeviceVendor(String deviceVendor) {
			this.deviceVendor = deviceVendor;
		}

		public String getImei() {
			return imei;
		}

		public void setImei(String imei) {
			this.imei = imei;
		}

		public String getImsi() {
			return imsi;
		}

		public void setImsi(String imsi) {
			this.imsi = imsi;
		}

		public String getMac() {
			return mac;
		}

		public void setMac(String mac) {
			this.mac = mac;
		}

		public String getNetType() {
			return netType;
		}

		public void setNetType(String netType) {
			this.netType = netType;
		}

		public String getOsAddtional() {
			return osAddtional;
		}

		public void setOsAddtional(String osAddtional) {
			this.osAddtional = osAddtional;
		}

		public int getOsVersion() {
			return osVersion;
		}

		public void setOsVersion(int osVersion) {
			this.osVersion = osVersion;
		}

		public String getOsVersionName() {
			return osVersionName;
		}

		public void setOsVersionName(String osVersionName) {
			this.osVersionName = osVersionName;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public float getScreenDensity() {
			return screenDensity;
		}

		public void setScreenDensity(float screenDensity) {
			this.screenDensity = screenDensity;
		}

		public int getScreenHeight() {
			return screenHeight;
		}

		public void setScreenHeight(int screenHeight) {
			this.screenHeight = screenHeight;
		}

		public int getScreenWidth() {
			return screenWidth;
		}

		public void setScreenWidth(int screenWidth) {
			this.screenWidth = screenWidth;
		}

		public String getServiceSupplier() {
			return serviceSupplier;
		}

		public void setServiceSupplier(String serviceSupplier) {
			this.serviceSupplier = serviceSupplier;
		}

		public String getSmsc() {
			return smsc;
		}

		public void setSmsc(String smsc) {
			this.smsc = smsc;
		}

	}

	public static class PlatFormData {
		public int raveId;
		public String apkKey;
		public int appId;
		public int channelId;
		public int cpId;
		public int serverId;
		public String appPackageName;
		public int appVersionCode;
		public String appVersionName;

		public String getApkKey() {
			return apkKey;
		}

		public void setApkKey(String apkKey) {
			this.apkKey = apkKey;
		}

		public int getAppId() {
			return appId;
		}

		public void setAppId(int appId) {
			this.appId = appId;
		}

		public int getChannelId() {
			return channelId;
		}

		public void setChannelId(int channelId) {
			this.channelId = channelId;
		}

		public int getCpId() {
			return cpId;
		}

		public void setCpId(int cpId) {
			this.cpId = cpId;
		}

		public int getServerId() {
			return serverId;
		}

		public void setServerId(int serverId) {
			this.serverId = serverId;
		}

		public String getAppPackageName() {
			return appPackageName;
		}

		public void setAppPackageName(String appPackageName) {
			this.appPackageName = appPackageName;
		}

		public int getAppVersionCode() {
			return appVersionCode;
		}

		public void setAppVersionCode(int appVersionCode) {
			this.appVersionCode = appVersionCode;
		}

		public String getAppVersionName() {
			return appVersionName;
		}

		public void setAppVersionName(String appVersionName) {
			this.appVersionName = appVersionName;
		}

		public int getRaveId() {
			return raveId;
		}

		public void setRaveId(int raveId) {
			this.raveId = raveId;
		}
	}

	public ClientActive getClientActive() {
		return clientActive;
	}

	public void setClientActive(ClientActive clientActive) {
		this.clientActive = clientActive;
	}

	public ClientInfo getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}

	public PlatFormInitMasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(PlatFormInitMasUser masUser) {
		this.masUser = masUser;
	}

	public PlatFormData getData() {
		return data;
	}

	public void setData(PlatFormData data) {
		this.data = data;
	}

}
