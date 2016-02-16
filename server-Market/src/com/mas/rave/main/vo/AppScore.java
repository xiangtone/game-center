package com.mas.rave.main.vo;

import java.util.Date;

/**
 * appScore
 * 
 * @author liwei.sz
 * 
 */
public class AppScore {
	private String scoreKey;
	private String scoreValue;
	private Date createTime;

	public String getScoreKey() {
		return scoreKey;
	}

	public void setScoreKey(String scoreKey) {
		this.scoreKey = scoreKey;
	}

	public String getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(String scoreValue) {
		this.scoreValue = scoreValue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
