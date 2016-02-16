package com.x.publics.http.model;

import com.x.publics.utils.Constan;


public class ModifyUserPwdRequest extends CommonRequest {

	public ModifyPwdUser data ;

	public ModifyUserPwdRequest() {
		super(Constan.Rc.ACCCOUNT_CHANGEINFO, Constan.SIGN);
	}

	 public static class  ModifyPwdUser{
		 
		 	public String userName ;
			public String userPwd ;
			public String userPwdNew ;
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
			public String getUserPwdNew() {
				return userPwdNew;
			}
			public void setUserPwdNew(String userPwdNew) {
				this.userPwdNew = userPwdNew;
			}
			
	 }

	public ModifyPwdUser getData() {
		return data;
	}

	public void setData(ModifyPwdUser data) {
		this.data = data;
	}

}
