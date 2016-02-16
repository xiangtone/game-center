package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TAppCollection implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer collectionId;
    private String publishTime;
    /**
     * 国家ID
     */
    private Integer raveId;

    /**
     * 主题名称
     */
    private String name;

    /**
     * 主题中文名称
     */
    private String nameCn;

    /**
     * 主题的小图标（列表显示）
     */
    private String icon;

    /**
     * 主题大图（页签详情时要显示）
     */
    private String bigicon;

    /**
     * 主题描述
     */
    private String description;

    /**
     * 排序（按数字大小从大到小排序）
     */
    private Integer sort;

    /**
     * 状态
     */
    private Boolean state;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date updateTime;

    /**
     * 后台操作人
     */
    private String operator;

    /**
     * 专辑的类别（1为collection、2为MUST HAVE的栏目)
     */
    private Integer type;

    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Integer collectionId) {
        this.collectionId = collectionId;
    }

    /**
     * @return 国家ID
     */
    public Integer getRaveId() {
        return raveId;
    }

    /**
     * @param raveid 
	 *            国家ID
     */
    public void setRaveId(Integer raveId) {
        this.raveId = raveId;
    }

    /**
     * @return 主题名称
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 
	 *            主题名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 主题中文名称
     */
    public String getNameCn() {
        return nameCn;
    }

    /**
     * @param namecn 
	 *            主题中文名称
     */
    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    /**
     * @return 主题的小图标（列表显示）
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon 
	 *            主题的小图标（列表显示）
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return 主题大图（页签详情时要显示）
     */
    public String getBigicon() {
        return bigicon;
    }

    /**
     * @param bigicon 
	 *            主题大图（页签详情时要显示）
     */
    public void setBigicon(String bigicon) {
        this.bigicon = bigicon;
    }

    /**
     * @return 主题描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 
	 *            主题描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 排序（按数字大小从大到小排序）
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 
	 *            排序（按数字大小从大到小排序）
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

    /**
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createtime 
	 *            创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return 后台操作人
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator 
	 *            后台操作人
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * @return 专辑的类别（1为collection、2为MUST HAVE的栏目)
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type 
	 *            专辑的类别（1为collection、2为MUST HAVE的栏目)
     */
    public void setType(Integer type) {
        this.type = type;
    }

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
}
