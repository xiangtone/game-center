package com.mas.rave.main.vo;

import java.util.Date;

/**
 * app分发分类
 * 
 * @author liwei.sz
 * 
 */
public class AppAlbum {
	private int id;
	private String name;// 专题名称',
	private String description;// 专题描述',
	private int sort;// 排序（按数字大小从大到小排序）',
	private boolean state;// 状态 0无效　1有效
	private Date createTime;// 创建时间',
	private String operator;// 后台操作人',

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
