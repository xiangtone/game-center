package com.x.publics.http.model;

import com.x.publics.utils.Constan;

public class MachineActivateRequest extends CommonRequest {

	public ClientMachine clientMachine;
	public Data data;
	
	public MachineActivateRequest( ) {
		super(Constan.Rc.MACHINE_ACTIVATE, Constan.SIGN);
	}

	public static class ClientMachine {

		private String deviceModel;
		private int deviceType;
		private String deviceVendor;
		private String imei;
		private String imsi;
		private String mac;
		private String netType;
		private String osAddtional;
		private String osVersion;
		private String osVersionName;
		private String phone;
		private float screenDensity;
		private int screenHeight;
		private int screenWidth;
		private String serviceSupplier;
		private String smsc;
		private long createTime;

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

		public long getCreateTime() {
			return createTime;
		}

		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}
	}

	public static class Data {
		private String appPackageName;
		private int appVersionCode;
		private String appVersionName;

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
	}

	public ClientMachine getClientMachine() {
		return clientMachine;
	}

	public void setClientMachine(ClientMachine clientMachine) {
		this.clientMachine = clientMachine;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}
