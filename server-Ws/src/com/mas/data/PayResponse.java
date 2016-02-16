package com.mas.data;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.Indomog.pay.data.MogValueExchange;

@XmlRootElement
public class PayResponse extends BaseResponse{
	
	private List<MogValueExchange> exchange;
	
	private DataPay data;
	
	public List<MogValueExchange> getExchange() {
		return exchange;
	}

	public void setExchange(List<MogValueExchange> exchange) {
		this.exchange = exchange;
	}

	public DataPay getData() {
		return data;
	}

	public void setData(DataPay data) {
		this.data = data;
	}
	
}