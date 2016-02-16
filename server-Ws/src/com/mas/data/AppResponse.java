package com.mas.data;

import javax.xml.bind.annotation.XmlRootElement;

import com.mas.ws.pojo.MasUser;

@XmlRootElement
public class AppResponse extends BaseResponse{
	
	private Boolean islogin;
	
	private MasUser masUser;
	
	private Data data;

	private Boolean isUpgrade;
	
	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Boolean getIsUpgrade() {
		return isUpgrade;
	}

	public void setIsUpgrade(Boolean isUpgrade) {
		this.isUpgrade = isUpgrade;
	}

	public Boolean getIslogin() {
		return islogin;
	}

	public void setIslogin(Boolean islogin) {
		this.islogin = islogin;
	}
	
}