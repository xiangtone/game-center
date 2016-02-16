package com.mas.rave.main.vo;

import java.util.Date;

public class AppInfoRank {
	
	private int appId;
	private int raveId;
	private int hrHigerank;
	private int hrLowrank;
	private int hnHigerank;
	private int hnLowrank;
	private int htHigerank;
	private int htLowrank;
	private int hpHigerank;
	private int hpLowrank;
	private int ahHigerank;
	private int ahLowrank;
	private int atHigerank;
	private int atLowrank;
	private int anHigerank;
	private int anLowrank;
	private int ghHigerank;
	private int ghLowrank;
	private int gtHigerank;
	private int gtLowrank;
	private int gnHigerank;
	private int gnLowrank;
	private Date updateTime;// 更新时间',
	private Date createTime;
	private Country country;
	
	
	public int getRaveId() {
		return raveId;
	}

	public void setRaveId(int raveId) {
		this.raveId = raveId;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		if(country!=null){
			this.setRaveId(country.getId());
		}
		this.country = country;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getHrHigerank() {
		return hrHigerank;
	}

	public void setHrHigerank(int hrHigerank) {
		this.hrHigerank = hrHigerank;
	}

	public int getHrLowrank() {
		return hrLowrank;
	}

	public void setHrLowrank(int hrLowrank) {
		this.hrLowrank = hrLowrank;
	}

	public int getHnHigerank() {
		return hnHigerank;
	}

	public void setHnHigerank(int hnHigerank) {
		this.hnHigerank = hnHigerank;
	}

	public int getHnLowrank() {
		return hnLowrank;
	}

	public void setHnLowrank(int hnLowrank) {
		this.hnLowrank = hnLowrank;
	}

	public int getHtHigerank() {
		return htHigerank;
	}

	public void setHtHigerank(int htHigerank) {
		this.htHigerank = htHigerank;
	}

	public int getHtLowrank() {
		return htLowrank;
	}

	public void setHtLowrank(int htLowrank) {
		this.htLowrank = htLowrank;
	}

	public int getHpHigerank() {
		return hpHigerank;
	}

	public void setHpHigerank(int hpHigerank) {
		this.hpHigerank = hpHigerank;
	}

	public int getHpLowrank() {
		return hpLowrank;
	}

	public void setHpLowrank(int hpLowrank) {
		this.hpLowrank = hpLowrank;
	}

	public int getAhHigerank() {
		return ahHigerank;
	}

	public void setAhHigerank(int ahHigerank) {
		this.ahHigerank = ahHigerank;
	}

	public int getAhLowrank() {
		return ahLowrank;
	}

	public void setAhLowrank(int ahLowrank) {
		this.ahLowrank = ahLowrank;
	}

	public int getAtHigerank() {
		return atHigerank;
	}

	public void setAtHigerank(int atHigerank) {
		this.atHigerank = atHigerank;
	}

	public int getAtLowrank() {
		return atLowrank;
	}

	public void setAtLowrank(int atLowrank) {
		this.atLowrank = atLowrank;
	}

	public int getAnHigerank() {
		return anHigerank;
	}

	public void setAnHigerank(int anHigerank) {
		this.anHigerank = anHigerank;
	}

	public int getAnLowrank() {
		return anLowrank;
	}

	public void setAnLowrank(int anLowrank) {
		this.anLowrank = anLowrank;
	}

	public int getGhHigerank() {
		return ghHigerank;
	}

	public void setGhHigerank(int ghHigerank) {
		this.ghHigerank = ghHigerank;
	}

	public int getGhLowrank() {
		return ghLowrank;
	}

	public void setGhLowrank(int ghLowrank) {
		this.ghLowrank = ghLowrank;
	}

	public int getGtHigerank() {
		return gtHigerank;
	}

	public void setGtHigerank(int gtHigerank) {
		this.gtHigerank = gtHigerank;
	}

	public int getGtLowrank() {
		return gtLowrank;
	}

	public void setGtLowrank(int gtLowrank) {
		this.gtLowrank = gtLowrank;
	}

	public int getGnHigerank() {
		return gnHigerank;
	}

	public void setGnHigerank(int gnHigerank) {
		this.gnHigerank = gnHigerank;
	}

	public int getGnLowrank() {
		return gnLowrank;
	}

	public void setGnLowrank(int gnLowrank) {
		this.gnLowrank = gnLowrank;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
}
