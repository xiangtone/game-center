package com.mas.data;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RechargeResponse extends BaseResponse{
	
	private List<DataPay> rechargelist;
	
	private Integer rechargeNum;
	
	private Boolean isLast;
	
	public List<DataPay> getRechargelist() {
		return rechargelist;
	}

	public void setRechargelist(List<DataPay> rechargelist) {
		this.rechargelist = rechargelist;
	}

	public Integer getRechargeNum() {
		return rechargeNum;
	}

	public void setRechargeNum(Integer rechargeNum) {
		this.rechargeNum = rechargeNum;
	}

	public Boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(Boolean isLast) {
		this.isLast = isLast;
	}
}