/**   
* @Title: MasUser.java
* @Package com.mas.amineappstore.http.model
* @Description: TODO 

* @date 2014-1-20 上午11:30:14
* @version V1.0   
*/

package com.x.publics.http.model;

/**
* @ClassName: MasUser
* @Description: TODO 

* @date 2014-1-20 上午11:30:14
* 
*/

public class MasUser {

	private int userId ;
	private String userName ;
	private String userPwd ;
	private String nickName ;
	
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
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

}
