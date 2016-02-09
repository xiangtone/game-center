package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
 * @ClassName: UpgradeRequest
 * @Desciption: 平台升级，数据请求
 
 * @Date: 2014-3-18 下午3:48:54
 */

public class UpgradeRequest extends CommonRequest {

	public UpgradeRequest() {
		super(Constan.Rc.PLATFORM_UPGRADE, Constan.SIGN);
	}

	public UpgradeRequestData data;

	public static class UpgradeRequestData {
		public int appId;
		public String apkKey;
		public int appVersionCode;
		public String appPackageName;
		public String appVersionName;
		public String md5;

		public int getAppId() {
			return appId;
		}

		public void setAppId(int appId) {
			this.appId = appId;
		}

		public String getApkKey() {
			return apkKey;
		}

		public void setApkKey(String apkKey) {
			this.apkKey = apkKey;
		}

		public int getAppVersionCode() {
			return appVersionCode;
		}

		public void setAppVersionCode(int appVersionCode) {
			this.appVersionCode = appVersionCode;
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

		public String getMd5() {
			return md5;
		}

		public void setMd5(String md5) {
			this.md5 = md5;
		}

	}

	public UpgradeRequestData getData() {
		return data;
	}

	public void setData(UpgradeRequestData data) {
		this.data = data;
	}

}
