package com.x.publics.http.model;

import com.x.publics.utils.Constan;


public class LoginRequest extends CommonRequest {

	public LoginData data;

	public LoginRequest() {
		super(Constan.Rc.ACCCOUNT_LOGIN, Constan.SIGN);
	}

	public static class LoginData {
		
			public String apkKey ;
			public int appId ;
			public int channelId ;
			public int cpId ;
			public int serverId ;
			public int clientId ;
			public String userName ;
			public String userPwd ;
			
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
			public int getClientId() {
				return clientId;
			}
			public void setClientId(int clientId) {
				this.clientId = clientId;
			}
	}

	public LoginData getData() {
		return data;
	}

	public void setData(LoginData data) {
		this.data = data;
	}

	
	
}
