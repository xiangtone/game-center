package com.mas.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CallbackRequest extends BaseRequest {
	
	private CallbackData data;

	public CallbackData getData() {
		return data;
	}

	public void setData(CallbackData data) {
		this.data = data;
	}
}
