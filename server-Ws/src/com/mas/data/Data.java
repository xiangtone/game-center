package com.mas.data;

public class Data {

	private Integer raveId;

	private Integer cpId;
	
	private Integer appId;
	
	private Integer channelId;
	
	private Integer serverId;
	
	private String apkKey;
	
	private String md5;
	private Boolean isPatch;
	private Integer patchSize;
	private Integer fileSize;
	
	private String appPackageName;
	
	private String appVersionName;
	
	private Integer appVersionCode;
	
	/*------start游戏启动激活时为空------*/
	private String appName;
	
	private String updateInfo;
	
	private String url;
	private String urlPatch;
	
	private String description;
	/**
     * 1不更新，2下拉框提示更新，3对话框强制更新
     */
    private Integer upgradeType;
	
	private Integer clientId;
	
	private Integer userId;
	private Integer versionCode;
	private String versionName;
    private String userName;
    private String userPwd;
    private String userPwdNew;
    private String nickName;
    
	public Integer getCpId() {
		return cpId;
	}

	public void setCpId(Integer cpId) {
		this.cpId = cpId;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
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

	public Integer getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(Integer appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

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


	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getUpgradeType() {
		return upgradeType;
	}

	public void setUpgradeType(Integer upgradeType) {
		this.upgradeType = upgradeType;
	}

	public String getApkKey() {
		return apkKey;
	}

	public void setApkKey(String apkKey) {
		this.apkKey = apkKey;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public Boolean getIsPatch() {
		return isPatch;
	}

	public void setIsPatch(Boolean isPatch) {
		this.isPatch = isPatch;
	}

	public String getUrlPatch() {
		return urlPatch;
	}

	public void setUrlPatch(String urlPatch) {
		this.urlPatch = urlPatch;
	}

	public Integer getPatchSize() {
		return patchSize;
	}

	public void setPatchSize(Integer patchSize) {
		this.patchSize = patchSize;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public Integer getRaveId() {
		return raveId;
	}

	public void setRaveId(Integer raveId) {
		this.raveId = raveId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserPwdNew() {
		return userPwdNew;
	}

	public void setUserPwdNew(String userPwdNew) {
		this.userPwdNew = userPwdNew;
	}
}
