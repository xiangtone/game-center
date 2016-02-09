/**   
 * @Title: RaveCrashRequest.java
 * @Package com.mas.amineappstore.http.model
 * @Description: TODO 
 
 * @date 2014-1-25 下午03:34:21
 * @version V1.0   
 */

package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
 * @ClassName: RaveCrashRequest
 * @Description: TODO
 
 * @date 2014-1-25 下午03:34:21
 * 
 */

public class RaveCrashRequest extends CommonRequest {

	public MasPlay masPlay;
	public RaveCrashRequestData data;

	public RaveCrashRequest() {
		super(Constan.Rc.POST_RAVE_CRASH, Constan.SIGN);
	}

	public static class RaveCrashRequestData {
		private int clientId;
		private String content;
		private String deviceModel;
		private int deviceType;
		private String deviceVendor;
		private String osVersion;
		private String osVersionName;

		public int getClientId() {
			return clientId;
		}

		public void setClientId(int clientId) {
			this.clientId = clientId;
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
	}

	public RaveCrashRequestData getData() {
		return data;
	}

	public void setData(RaveCrashRequestData data) {
		this.data = data;
	}

	public MasPlay getMasPlay() {
		return masPlay;
	}

	public void setMasPlay(MasPlay masPlay) {
		this.masPlay = masPlay;
	}

}
