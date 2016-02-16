/**   
* @Title: SkinDownloadRequest.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-12-3 下午2:21:33
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
* @ClassName: SkinDownloadRequest
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-12-3 下午2:21:33
* 
*/

public class SkinDownloadRequest extends CommonRequest {

	public SkinDownloadRequest() {
		super(Constan.Rc.SKIN_DOWNLOAD, Constan.SIGN);
	}

	public SkinData data;

	public static class SkinData {
		private int clientId;
		private int downOrUpdate;
		private String imei;
		private String packageName;
		private int skinId;
		private String skinName;
		private int versionCode;
		private String versionName;

		public int getClientId() {
			return clientId;
		}

		public void setClientId(int clientId) {
			this.clientId = clientId;
		}

		public int getDownOrUpdate() {
			return downOrUpdate;
		}

		public void setDownOrUpdate(int downOrUpdate) {
			this.downOrUpdate = downOrUpdate;
		}

		public String getImei() {
			return imei;
		}

		public void setImei(String imei) {
			this.imei = imei;
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public int getSkinId() {
			return skinId;
		}

		public void setSkinId(int skinId) {
			this.skinId = skinId;
		}

		public String getSkinName() {
			return skinName;
		}

		public void setSkinName(String skinName) {
			this.skinName = skinName;
		}

		public int getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

	}

	public SkinData getData() {
		return data;
	}

	public void setData(SkinData data) {
		this.data = data;
	}

}
