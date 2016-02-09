package com.x.publics.http.model;

import com.x.publics.utils.Constan;


public class ModifyUserNickNameRequest extends CommonRequest {

	ModifyNickNameUser data ;

	public ModifyUserNickNameRequest() {
		super(Constan.Rc.ACCCOUNT_CHANGEINFO, Constan.SIGN);
	}

	 public static class  ModifyNickNameUser{
		 
		 	public String userName ;
			public String nickName ;
			
			public String getUserName() {
				return userName;
			}
			public void setUserName(String userName) {
				this.userName = userName;
			}
			public String getNickName() {
				return nickName;
			}
			public void setNickName(String nickName) {
				this.nickName = nickName;
			}
	 }

	public ModifyNickNameUser getData() {
		return data;
	}

	public void setData(ModifyNickNameUser data) {
		this.data = data;
	}

}
