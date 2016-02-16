package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 类型编号(命名规则需要整理成文档
     */
    private Integer id;

    /**
     * 对应平台
     */
    private Integer marketInfoId;

    /**
     * 类型名
     */
    private String name;

    /**
     * 类型中文名
     */
    private String categoryCn;

    /**
     * 父类编号(默认0)
     */
    private Integer fatherId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 大图标
     */
    private String bigicon;

    /**
     * 推荐的应用
     */
    private String recommend;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 状态
     */
    private Boolean state;

    private Date createTime;

    private Date updateTime;

    //二级分类新增
    private Integer level;
    
    public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
     * @return 类型编号(命名规则需要整理成文档
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id 
	 *            类型编号(命名规则需要整理成文档
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 对应平台
     */
    public Integer getMarketInfoId() {
        return marketInfoId;
    }

    /**
     * @param marketinfoid 
	 *            对应平台
     */
    public void setMarketInfoId(Integer marketInfoId) {
        this.marketInfoId = marketInfoId;
    }

    /**
     * @return 类型名
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 
	 *            类型名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 类型中文名
     */
    public String getCategoryCn() {
        return categoryCn;
    }

    /**
     * @param categorycn 
	 *            类型中文名
     */
    public void setCategoryCn(String categoryCn) {
        this.categoryCn = categoryCn;
    }

    /**
     * @return 父类编号(默认0)
     */
    public Integer getFatherId() {
        return fatherId;
    }

    /**
     * @param fatherid 
	 *            父类编号(默认0)
     */
    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    /**
     * @return 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon 
	 *            图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return 大图标
     */
    public String getBigicon() {
        return bigicon;
    }

    /**
     * @param bigicon 
	 *            大图标
     */
    public void setBigicon(String bigicon) {
        this.bigicon = bigicon;
    }

    /**
     * @return 推荐的应用
     */
    public String getRecommend() {
        return recommend;
    }

    /**
     * @param recommend 
	 *            推荐的应用
     */
    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    /**
     * @return 排序值
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 
	 *            排序值
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return 状态
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            状态
     */
    public void setState(Boolean state) {
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
}