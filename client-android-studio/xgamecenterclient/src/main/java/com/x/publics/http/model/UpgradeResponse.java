package com.x.publics.http.model;

/**
 * @ClassName: UpgradeResponse
 * @Desciption: 平台升级，数据响应
 
 * @Date: 2014-3-18 下午4:14:17
 */
public class UpgradeResponse extends CommonResponse {

	public boolean isUpgrade;

	public UpgradeResponseData data;

	public static class UpgradeResponseData {
		public String url; // 全量路径      
		public String urlPatch; // 增量路径
		public String appName; // 应用名称
		public String updateInfo;// 更新信息
		public int versionCode; // 版本号
		public String versionName; // 版本名称
		public boolean isPatch; // true, 表示是增量更新，为false表示为正常的更新
		public int fileSize; // 正常大小
		public int patchSize;// 增量大小
		public int fileType;//文件类型 1:应用，2:游戏
		public int upgradeType;// 2-普通升级、3-强制升级.

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public String getUpdateInfo() {
			return updateInfo;
		}

		public void setUpdateInfo(String updateInfo) {
			this.updateInfo = updateInfo;
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

		public boolean isPatch() {
			return isPatch;
		}

		public void setPatch(boolean isPatch) {
			this.isPatch = isPatch;
		}

		public int getFileSize() {
			return fileSize;
		}

		public void setFileSize(int fileSize) {
			this.fileSize = fileSize;
		}

		public int getPatchSize() {
			return patchSize;
		}

		public void setPatchSize(int patchSize) {
			this.patchSize = patchSize;
		}

		public int getFileType() {
			return fileType;
		}

		public void setFileType(int fileType) {
			this.fileType = fileType;
		}

		public String getUrlPatch() {
			return urlPatch;
		}

		public void setUrlPatch(String urlPatch) {
			this.urlPatch = urlPatch;
		}

		public int getUpgradeType() {
			return upgradeType;
		}

		public void setUpgradeType(int upgradeType) {
			this.upgradeType = upgradeType;
		}

	}

	public boolean isUpgrade() {
		return isUpgrade;
	}

	public void setUpgrade(boolean isUpgrade) {
		this.isUpgrade = isUpgrade;
	}

	public UpgradeResponseData getData() {
		return data;
	}

	public void setData(UpgradeResponseData data) {
		this.data = data;
	}
}
