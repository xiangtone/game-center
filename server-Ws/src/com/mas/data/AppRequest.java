package com.mas.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AppRequest extends BaseRequest
{
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return super.toString()+"-------AppRequest [data=" + data.toString() + "]";
	}

	
	
}
