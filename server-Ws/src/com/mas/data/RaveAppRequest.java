package com.mas.data;

import javax.xml.bind.annotation.XmlRootElement;

import com.mas.ws.pojo.MasUser;

@XmlRootElement
public class RaveAppRequest extends BaseRequest {
	
	private RaveData data;
	
	private Page page;
	
	private MasUser masUser;
	private MasData masPlay;
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public RaveData getData() {
		return data;
	}

	public void setData(RaveData data) {
		this.data = data;
	}

	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public MasData getMasPlay() {
		return masPlay;
	}

	public void setMasPlay(MasData masPlay) {
		this.masPlay = masPlay;
	}
}
