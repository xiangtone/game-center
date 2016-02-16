package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
 * @ClassName: CommentRequest
 * @Desciption: 应用评论，数据请求
 
 * @Date: 2014-1-24 上午8:41:07
 */

public class CommentRequest extends CommonRequest {

	public int ps;
	public int pn;
	public Pager page;
	public MasUser masUser;
	public CommentData data;

	public CommentRequest() {
		super(Constan.Rc.POST_APP_COMMENT, Constan.SIGN);
	}

	public static class CommentData {
		public int appId;
		public int stars;
		public int clientId;
		public String content;

		public String deviceModel;
		public String deviceVendor;
		public String osVersion;
		public String osVersionName;
		public int deviceType;

		public int getAppId() {
			return appId;
		}

		public void setAppId(int appId) {
			this.appId = appId;
		}

		public int getStars() {
			return stars;
		}

		public void setStars(int stars) {
			this.stars = stars;
		}

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

	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public CommentData getData() {
		return data;
	}

	public void setData(CommentData data) {
		this.data = data;
	}

	public Pager getPage() {
		return page;
	}

	public void setPage(Pager page) {
		this.page = page;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public int getPn() {
		return pn;
	}

	public void setPn(int pn) {
		this.pn = pn;
	}
}
