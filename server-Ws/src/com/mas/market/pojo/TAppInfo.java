package com.mas.market.pojo;

import java.io.Serializable;

public class TAppInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String logo;
	
	private String bigLogo;

	public String getBigLogo() {
		return bigLogo;
	}

	public void setBigLogo(String bigLogo) {
		this.bigLogo = bigLogo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	
	
}
