package com.mas.rave.main.vo;

import java.util.Date;

public class AppannieInfoBase{


	private Integer id;
	/**应用名称*/
	private String appName;	

	/**APP在APPAnnie上的安装数量*/
	private Double annieInstallTotal;
	/**APP在APPAnnie上评分*/
	private Double annieRatings;
	/**APP在APPAnnie上最早版本上线时间，
	 * 如果是我们自己运营的使用我们录入时间。
	 * (版本上线日期到至今的时间)*/
	private Date initialReleaseDate;
	
	private String issuer;

	/**APP包的大小*/
	private Double size;
	
	private String url;
	
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Double getAnnieInstallTotal() {
		return annieInstallTotal;
	}

	public void setAnnieInstallTotal(Double annieInstallTotal) {
		this.annieInstallTotal = annieInstallTotal;
	}

	public Double getAnnieRatings() {
		return annieRatings;
	}

	public void setAnnieRatings(Double annieRatings) {
		this.annieRatings = annieRatings;
	}

	public Date getInitialReleaseDate() {
		return initialReleaseDate;
	}

	public void setInitialReleaseDate(Date initialReleaseDate) {
		this.initialReleaseDate = initialReleaseDate;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	

}
