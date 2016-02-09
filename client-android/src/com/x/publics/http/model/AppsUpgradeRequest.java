/**   
* @Title: AppsUpgradeRequest.java
* @Package com.mas.amineappstore.http.model
* @Description: TODO 

* @date 2014-1-18 下午03:25:51
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.publics.utils.Constan;

import java.util.List;

/**
* @ClassName: AppsUpgradeRequest
* @Description: TODO 

* @date 2014-1-18 下午03:25:51
* 
*/

public class AppsUpgradeRequest extends CommonRequest {

	public int appNum;
	public int clientId;
	public int raveId;
	List<RequestInstallBean> apps;

	public static class RequestInstallBean {

		private String appName;
		private String appPackageName;
		private String appVersionName;
		private int appVersionCode;
		private String md5;
		private long fileSize;
		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
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

		public int getAppVersionCode() {
			return appVersionCode;
		}

		public void setAppVersionCode(int appVersionCode) {
			this.appVersionCode = appVersionCode;
		}

		public String getMd5() {
			return md5;
		}

		public void setMd5(String md5) {
			this.md5 = md5;
		}

		public long getFileSize() {
			return fileSize;
		}

		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}


	}

	public AppsUpgradeRequest() {
		super(Constan.Rc.GET_APPS_UPGRADE, Constan.SIGN);
	}

	public int getAppNum() {
		return appNum;
	}

	public void setAppNum(int appNum) {
		this.appNum = appNum;
	}

	public List<RequestInstallBean> getApps() {
		return apps;
	}

	public void setApps(List<RequestInstallBean> apps) {
		this.apps = apps;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getRaveId() {
		return raveId;
	}

	public void setRaveId(int raveId) {
		this.raveId = raveId;
	}

}
