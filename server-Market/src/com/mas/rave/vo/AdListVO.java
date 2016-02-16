package com.mas.rave.vo;

import java.util.Date;
import java.util.List;

public class AdListVO {
	
	
	private String adVersion;
	
	private Date addTime;
	
	List<AdUrl> list;
	
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

	
	public List<AdUrl> getList() {
		return list;
	}

	public void setList(List<AdUrl> list) {
		this.list = list;
	}


	public class AdUrl{
		
		private String url;
		
		private String adUrl;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getAdUrl() {
			return adUrl;
		}

		public void setAdUrl(String adUrl) {
			this.adUrl = adUrl;
		}
		
		
	}

}
