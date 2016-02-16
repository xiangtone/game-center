package com.Indomog.pay.pojo;

import com.alibaba.fastjson.annotation.JSONField;

public class Request {

	private Data Data;
	
	private PayMode PayMode;
	
	@JSONField(name="Data")
	public Data getData() {
		return Data;
	}
	@JSONField(name="Data")
	public void setData(Data data) {
		Data = data;
	}
	
	@JSONField(name="PayMode")
	public PayMode getPayMode() {
		return PayMode;
	}
	@JSONField(name="PayMode")
	public void setPayMode(PayMode payMode) {
		PayMode = payMode;
	}

	@Override
	public String toString() {
		return "Request [Data=" + Data + ", PayMode=" + PayMode + "]";
	}
}
