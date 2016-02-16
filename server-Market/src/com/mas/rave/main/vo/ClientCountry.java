package com.mas.rave.main.vo;

import java.util.Date;

/**
 * 中英对照表
 * 
 * @author jieding
 * 
 */
public class ClientCountry extends PagingBean {

	// private int id;// 唯一标识

	private String country;// 国家名
	private String countryStr;

	private String countryCn;// 国家名中文
	private String countryCnStr;

	private Date createTime;// 创建时间'

	private String iconUrl;

	// public int getId() {
	// return id;
	// }
	//
	// public void setId(int id) {
	// this.id = id;
	// }

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryCn() {
		return countryCn;
	}

	public void setCountryCn(String countryCn) {
		this.countryCn = countryCn;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getCountryStr() {
		return countryStr;
	}

	public void setCountryStr(String countryStr) {
		this.countryStr = countryStr;
	}

	public String getCountryCnStr() {
		return countryCnStr;
	}

	public void setCountryCnStr(String countryCnStr) {
		this.countryCnStr = countryCnStr;
	}

}
