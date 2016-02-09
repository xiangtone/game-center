/**   
* @Title: UserInfoBean.java
* @Package com.mas.amineappstore.model
* @Description: TODO 

* @date 2014-1-21 上午10:44:38
* @version V1.0   
*/

package com.x.publics.model;

/**
* @ClassName: UserInfoBean
* @Description: TODO 

* @date 2014-1-21 上午10:44:38
* 
*/

public class UserInfoBean {

	private String userName;
	private int clientId;
	private int userId;
	private String userPwd;
	private String avalue;
	private boolean isLogin;
	private String nickName;
	
	
	/** 
	 * @return nickName 
	 */
	
	public String getNickName() {
		return nickName;
	}

	/** 
	 * @param nickName 要设置的 nickName 
	 */
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getAvalue() {
		return avalue;
	}

	public void setAvalue(String avalue) {
		this.avalue = avalue;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
}
