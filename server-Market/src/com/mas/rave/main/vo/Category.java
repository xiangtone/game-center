package com.mas.rave.main.vo;

import java.util.Date;

/**
 * app对应分类信息
 * 
 * @author liwei.sz
 * 
 */
public class Category {
	private int id;// '类型编号(命名规则需要整理成文档',
	private Integer marketInfoId;
	private Market marketInfo; // 对应平台
	private String name;// 类型名',
	private String categoryCn;// 类型中文名',
	private int fatherId;// 父类编号(默认0)',
	private int level;// 分类级别，0=应用分类，1=第1级，2=第2级
	private String icon;// 图标',
	private String bigicon;// 大图标',
	private String recommend;// 备注
	private int sort;// 排序值',
	private boolean state;// 状态',
	private Date createTime;// 创建时间
	private Date updateTime;// 更新时间
	private int raveId;
	private String cateName;
	private String cateNameCn;

	private Integer flag;// 标记,用来标记是否查询平台下全部应用.

	private Integer firstId;// 一级分类的id

	private Integer secondId;

	private String firstName;// 一级分类的name

	private Integer firstFatherId;// 一级分类的父id,即应用分类

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

	public String getCategoryCn() {
		return categoryCn;
	}

	public void setCategoryCn(String categoryCn) {
		this.categoryCn = categoryCn;
	}

	public int getFatherId() {
		return fatherId;
	}

	public void setFatherId(int fatherId) {
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

	public Market getMarketInfo() {
		return marketInfo;
	}

	public void setMarketInfo(Market marketInfo) {
		this.marketInfo = marketInfo;
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

	public Integer getMarketInfoId() {
		return marketInfoId;
	}

	public void setMarketInfoId(Integer marketInfoId) {
		this.marketInfoId = marketInfoId;
	}

	public int getRaveId() {
		return raveId;
	}

	public void setRaveId(int raveId) {
		this.raveId = raveId;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getCateNameCn() {
		return cateNameCn;
	}

	public void setCateNameCn(String cateNameCn) {
		this.cateNameCn = cateNameCn;
	}

	public Integer getFirstId() {
		return firstId;
	}

	public void setFirstId(Integer firstId) {
		this.firstId = firstId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Integer getFirstFatherId() {
		return firstFatherId;
	}

	public void setFirstFatherId(Integer firstFatherId) {
		this.firstFatherId = firstFatherId;
	}

	/******************* liwei add level 2015-10-20 ******************************/
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Integer getSecondId() {
		return secondId;
	}

	public void setSecondId(Integer secondId) {
		this.secondId = secondId;
	}

}
