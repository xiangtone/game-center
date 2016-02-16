package com.mas.rave.main.vo;

import java.util.Date;

/**
 * t_search_keyword_icon
 * 
 * @author jieding
 * 
 */
public class SearchKeywordIcon {

	private int id;// 唯一标识

	private String name;// 国家名

	private String nameCn;// 国家名中文

	private Date createTime;// 创建时间'

	private String url;// 图标路径
	private boolean state;// 客户端是否显示此国家列表

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

}
