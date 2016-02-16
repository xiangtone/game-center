package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
 * @ClassName: FeedbackRequest
 * @Desciption: 用户反馈的数据请求
 
 * @Date: 2014-2-17 下午5:04:41
 */

public class FeedbackRequest extends CommonRequest {

	public MasUser masUser;
	public MasPlay masPlay;
	public FeedbackData data;

	public FeedbackRequest() {
		super(Constan.Rc.POST_FEEDBACK, Constan.SIGN);
	}

	public static class FeedbackData {
		public int clientId;
		public String email;
		public String content;
		public String deviceModel;
		public String deviceVendor;
		public String osVersion;
		public String osVersionName;
		public int deviceType;
		public String imei;

		public String getImei() {
			return imei;
		}

		public void setImei(String imei) {
			this.imei = imei;
		}

		public int getClientId() {
			return clientId;
		}

		public void setClientId(int clientId) {
			this.clientId = clientId;
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

	}
	public static class MasPlay {
		public String masPackageName;
		public int masVersionCode;
		public String masVersionName;
		
		public String getMasPackageName() {
			return masPackageName;
		}
		public void setMasPackageName(String masPackageName) {
			this.masPackageName = masPackageName;
		}
		public int getMasVersionCode() {
			return masVersionCode;
		}
		public void setMasVersionCode(int masVersionCode) {
			this.masVersionCode = masVersionCode;
		}
		public String getMasVersionName() {
			return masVersionName;
		}
		public void setMasVersionName(String masVersionName) {
			this.masVersionName = masVersionName;
		}
		
	}
	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public FeedbackData getData() {
		return data;
	}

	public void setData(FeedbackData data) {
		this.data = data;
	}

	public MasPlay getMasPlay() {
		return masPlay;
	}

	public void setMasPlay(MasPlay masPlay) {
		this.masPlay = masPlay;
	}

}
