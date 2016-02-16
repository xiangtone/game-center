package com.mas.data;

import javax.xml.bind.annotation.XmlRootElement;

import com.mas.ws.pojo.MasUser;

@XmlRootElement
public class PayRequest extends BaseRequest {
	
	private DataPay data;
	
	private MasUser masUser;
	
	private PayMode payMode;
	
	private boolean purchase = true;

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

	public PayMode getPayMode() {
		return payMode;
	}

	public void setPayMode(PayMode payMode) {
		this.payMode = payMode;
	}

	public boolean isPurchase() {
		return purchase;
	}

	public void setPurchase(boolean purchase) {
		this.purchase = purchase;
	}

}
