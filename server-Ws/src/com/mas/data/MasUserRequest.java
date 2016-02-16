package com.mas.data;

import javax.xml.bind.annotation.XmlRootElement;

import com.mas.ws.pojo.MasUser;

@XmlRootElement
public class MasUserRequest extends BaseRequest
{
	private MasUser data;

	public MasUser getData() {
		return data;
	}

	public void setData(MasUser data) {
		this.data = data;
	}


}
