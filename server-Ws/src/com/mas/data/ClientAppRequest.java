package com.mas.data;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientAppRequest extends BaseRequest {
	
	private Integer clientId;
	
	private Integer raveId;
	
	private List<Data> apps;
	
	private Integer appNum;

	public List<Data> getApps() {
		return apps;
	}

	public void setApps(List<Data> apps) {
		this.apps = apps;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getAppNum() {
		return appNum;
	}

	public void setAppNum(Integer appNum) {
		this.appNum = appNum;
	}

	public Integer getRaveId() {
		return raveId;
	}

	public void setRaveId(Integer raveId) {
		this.raveId = raveId;
	}

}
