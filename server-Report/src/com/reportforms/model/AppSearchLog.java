package com.reportforms.model;

import java.sql.Timestamp;

import com.reportforms.util.DateUtil;


public class AppSearchLog extends BaseDomain {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -371418723373705540L;

	private Integer id;
	
	private Integer clientId;
	
	private Integer userId;
	
	private String userName;
	
	private String content;
	
	private Integer searchNum;
	
	private Integer searchCount;
	
	private Boolean state;
	
	private Timestamp createTime;
	
	private String createTimeString;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSearchNum() {
		return searchNum;
	}

	public void setSearchNum(Integer searchNum) {
		this.searchNum = searchNum;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
		this.createTimeString = DateUtil.getTimestampToString(createTime);
	}

	public String getCreateTimeString() {
		return createTimeString;
	}

	public Integer getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}

}
