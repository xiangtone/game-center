package com.mas.rave.main.vo;

import java.util.Date;
/**
 * 应用专辑管理
 * @author jieding
 *
 */
public class AppCollection {
	/**编号*/
	private int collectionId;
	/**平台国家Id'*/
	private Integer raveId;
	/**主题名称*/
	private String name; 
	/**主题中文名称*/
	private String nameCn;
	/**主题的小图标（列表显示）*/
	private String icon;
	/**主题大图（页签详情时要显示）*/
	private String bigicon;
	/** 主题描述*/
	private String description;
	/**排序（按数字大小从大到小排序）*/
	private int sort; 
	/** 状态 0否 1是*/
	private boolean state;
	/**创建时间'*/
	private Date createTime;
	/**更新时间*/
	private Date updateTime;
	/**后台操作人*/
	private String operator;

	private Country country;
	
	//add by dingjie 2014/09/01
	/**专辑的类别（1为collection、2为MUST HAVE的栏目)*/
	private int type;
	private String createTime1;

	public Integer getRaveId() {
		return raveId;
	}
	public void setRaveId(Integer raveId) {
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
	public int getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getBigicon() {
		return bigicon;
	}
	public void setBigicon(String bigicon) {
		this.bigicon = bigicon;
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
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCreateTime1() {
		return createTime1;
	}
	public void setCreateTime1(String createTime1) {
		this.createTime1 = createTime1;
	}
}
