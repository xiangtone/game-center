package com.mas.rave.vo;

import java.util.Date;

/**
 * 广告类
 * @author kun.shen
 *
 */
public class AdVO {
	
	private String url;
	
	private String adVersion;
	
	private Date addTime;
	
	private String adUrl;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAdVersion() {
		return adVersion;
	}

	public void setAdVersion(String adVersion) {
		this.adVersion = adVersion;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

}
