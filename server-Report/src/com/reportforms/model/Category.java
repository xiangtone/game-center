package com.reportforms.model;

import java.util.Date;

/**
 * app对应分类信息
 * 
 * @author lisong.lan
 * 
 */
public class Category {
	
	private Integer id;// '类型编号(命名规则需要整理成文档',
	private Integer marketInfoId; // 对应平台
	private String name;// 类型名',
	private String categoryCn;// 类型中文名',
	private int fatherId;// 父类编号(默认0)',
	private String icon;// 图标',
	private String bigicon;// 大图标',
	private String recommend;// 备注
	private Integer sort;// 排序值',
	private Boolean state;// 状态',
	private Date createTime;// 创建时间
	private Date updateTime;// 更新时间

	private Integer flag;// 标记,用来标记是否查询平台下全部应用.

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryCn() {
		return categoryCn;
	}

	public void setCategoryCn(String categoryCn) {
		this.categoryCn = categoryCn;
	}

	public Integer getFatherId() {
		return fatherId;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
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

	public Integer getMarketInfoId() {
		return marketInfoId;
	}

	public void setMarketInfoId(Integer marketInfoId) {
		this.marketInfoId = marketInfoId;
	}

	public String getBigicon() {
		return bigicon;
	}

	public void setBigicon(String bigicon) {
		this.bigicon = bigicon;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
