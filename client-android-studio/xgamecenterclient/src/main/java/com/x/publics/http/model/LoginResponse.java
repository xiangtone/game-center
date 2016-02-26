package com.x.publics.http.model;

public class LoginResponse extends CommonResponse {

	public MasUser masUser ;
	public boolean islogin ;
	
	
	public boolean isIslogin() {
		return islogin;
	}

	public void setIslogin(boolean islogin) {
		this.islogin = islogin;
	}

	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}
	
}
