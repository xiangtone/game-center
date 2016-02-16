package com.x.publics.http.model;

import com.x.publics.utils.Constan;


public class RegisterRequest extends CommonRequest {

	public RegisterData data;

	public RegisterRequest() {
		super(Constan.Rc.ACCCOUNT_REGISTER, Constan.SIGN);
	}

	public static class RegisterData {
		
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

	public RegisterData getData() {
		return data;
	}

	public void setData(RegisterData data) {
		this.data = data;
	}

	
}
