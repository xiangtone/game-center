package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TAppAlbumTheme implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer themeId;

    /**
     * 栏目Id
     */
    private Integer columnId;

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
     * 标识1表示资源、2表示列表
     */
    private Integer flag;

    /**
     * flag为1时对应的t_app_file的Id，2时为空或0
     */
    private Integer apkId;

    /**
     * 应用类型ID
     */
    private Integer categoryId;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date updateTime;

    /**
     * 后台操作人
     */
    private String operator;

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    /**
     * @return 栏目Id
     */
    public Integer getColumnId() {
        return columnId;
    }

    /**
     * @param columnid 
	 *            栏目Id
     */
    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
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
     * @return 标识1表示资源、2表示列表
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * @param flag 
	 *            标识1表示资源、2表示列表
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * @return flag为1时对应的t_app_file的Id，2时为空或0
     */
    public Integer getApkId() {
        return apkId;
    }

    /**
     * @param apkid 
	 *            flag为1时对应的t_app_file的Id，2时为空或0
     */
    public void setApkId(Integer apkId) {
        this.apkId = apkId;
    }

    /**
     * @return 应用类型ID
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryid 
	 *            应用类型ID
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
}