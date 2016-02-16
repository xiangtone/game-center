package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TAppAlbumColumn implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer columnId;

    /**
     * 大类别Id
     */
    private Integer albumId;

    /**
     * 页签名称
     */
    private String name;

    /**
     * 页签中文名称
     */
    private String nameCn;

    /**
     * 页签的小图标（列表显示）
     */
    private String icon;

    /**
     * 专题大图（页签详情时要显示）
     */
    private String bigicon;

    /**
     * 页签描述
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
     * 标识0表示不用手动分发，1表示需要手动分发
     */
    private Integer flag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 后台操作人
     */
    private String operator;

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    /**
     * @return 大类别Id
     */
    public Integer getAlbumId() {
        return albumId;
    }

    /**
     * @param albumid 
	 *            大类别Id
     */
    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    /**
     * @return 页签名称
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 
	 *            页签名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 页签中文名称
     */
    public String getNameCn() {
        return nameCn;
    }

    /**
     * @param namecn 
	 *            页签中文名称
     */
    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    /**
     * @return 页签的小图标（列表显示）
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon 
	 *            页签的小图标（列表显示）
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return 专题大图（页签详情时要显示）
     */
    public String getBigicon() {
        return bigicon;
    }

    /**
     * @param bigicon 
	 *            专题大图（页签详情时要显示）
     */
    public void setBigicon(String bigicon) {
        this.bigicon = bigicon;
    }

    /**
     * @return 页签描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 
	 *            页签描述
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
     * @return 标识0表示不用手动分发，1表示需要手动分发
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * @param flag 
	 *            标识0表示不用手动分发，1表示需要手动分发
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
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
}