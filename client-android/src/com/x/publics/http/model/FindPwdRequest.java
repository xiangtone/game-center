package com.x.publics.http.model;

import com.x.publics.utils.Constan;


public class FindPwdRequest extends CommonRequest {

	public UserInfo data ;

	public FindPwdRequest() {
		super(Constan.Rc.ACCCOUNT_FIND_PASSWORD, Constan.SIGN);
	}

	public static class UserInfo{
		public String userName;
		
		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

	}

	public UserInfo getData() {
		return data;
	}

	public void setData(UserInfo data) {
		this.data = data;
	}
	
	
	
	
}
