package com.mas.data;

import javax.xml.bind.annotation.XmlRootElement;

import com.mas.ws.pojo.MasUser;

@XmlRootElement
public class RechargeRequest extends BaseRequest {
	
	private DataPay data;
	
	private MasUser masUser;
	
	private Page page;

	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public DataPay getData() {
		return data;
	}

	public void setData(DataPay data) {
		this.data = data;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
