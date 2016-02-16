package com.mas.rave.main.vo;

import java.util.Date;

/**
 * app评论
 * 
 * @author liwei.sz
 * 
 */
public class AppComment{
	private int id;
	private AppInfo appInfo;// 应用信息
	private int clientId;// 手机端id
	private int userId;// 手机用户　
	private String userName;// 用户名
	private int stars;// 星级',
	private String content;// 内容',
	private int sort;// 顺序',
	private boolean state;// 是否显示评论０不显示 1显示',
	private Date createTime;// 评论时间

	private String name;
	private int appId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AppInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

}
