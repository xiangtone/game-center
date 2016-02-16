/**   
* @Title: PlatFormInitResponse.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-6-6 上午10:42:40
* @version V1.0   
*/

package com.x.publics.http.model;

/**
* @ClassName: PlatFormInitResponse
* @Description: 平台初始化应答

* @date 2014-6-6 上午10:42:40
* 
*/

public class PlatFormInitResponse extends CommonResponse {

	public int autoRaveId;
	public int clientId;
	public boolean islogin;
	public String loginInfo ;
	public MasUser masUser ;
	
	
	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public boolean isIslogin() {
		return islogin;
	}

	public void setIslogin(boolean islogin) {
		this.islogin = islogin;
	}

	public String getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(String loginInfo) {
		this.loginInfo = loginInfo;
	}

	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public int getAutoRaveId() {
		return autoRaveId;
	}

	public void setAutoRaveId(int autoRaveId) {
		this.autoRaveId = autoRaveId;
	}
	
}
