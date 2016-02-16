package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TSearchKeyword implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer searchId;
    private Integer actionRc;
    private String action;

    /**
     * t_search_keyword_icon表的ID
     */
    private Integer iconId;

    /**
     * t_search_keyword_icon的url
     */
    private String iconUrl;

    /**
     * 国家ID
     */
    private Integer raveId;

    /**
     * 国家名称
     */
    private String raveName;

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 当flag为1时资源的logo
     */
    private String resLogo;

    /**
     * 资源ID(appId,musicId,imageId)
     */
    private Integer resId;

    /**
     * 标识0关键字1表示资源、2表示列表
     */
    private Integer flag;

    /**
     * 资源类型ID,2为App&Game,4为Ringtones,5为Wallpaper
     */
    private Integer albumId;

    /**
     * 排序(按数字大小从大到小排序)
     */
    private Integer sort;

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
     * 被手机客户端点击的次数
     */
    private Integer searchNum;

    public Integer getSearchId() {
        return searchId;
    }

    public void setSearchId(Integer searchId) {
        this.searchId = searchId;
    }

    /**
     * @return t_search_keyword_icon表的ID
     */
    public Integer getIconId() {
        return iconId;
    }

    /**
     * @param iconid 
	 *            t_search_keyword_icon表的ID
     */
    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    /**
     * @return t_search_keyword_icon的url
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * @param iconurl 
	 *            t_search_keyword_icon的url
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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
     * @return 国家名称
     */
    public String getRaveName() {
        return raveName;
    }

    /**
     * @param ravename 
	 *            国家名称
     */
    public void setRaveName(String raveName) {
        this.raveName = raveName;
    }

    /**
     * @return 搜索关键字
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword 
	 *            搜索关键字
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return 当flag为1时资源的logo
     */
    public String getResLogo() {
        return resLogo;
    }

    /**
     * @param reslogo 
	 *            当flag为1时资源的logo
     */
    public void setResLogo(String resLogo) {
        this.resLogo = resLogo;
    }

    /**
     * @return 资源ID(appId,musicId,imageId)
     */
    public Integer getResId() {
        return resId;
    }

    /**
     * @param resid 
	 *            资源ID(appId,musicId,imageId)
     */
    public void setResId(Integer resId) {
        this.resId = resId;
    }

    /**
     * @return 标识0关键字1表示资源、2表示列表
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * @param flag 
	 *            标识0关键字1表示资源、2表示列表
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * @return 资源类型ID,2为App&Game,4为Ringtones,5为Wallpaper
     */
    public Integer getAlbumId() {
        return albumId;
    }

    /**
     * @param albumid 
	 *            资源类型ID,2为App&Game,4为Ringtones,5为Wallpaper
     */
    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    /**
     * @return 排序(按数字大小从大到小排序)
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 
	 *            排序(按数字大小从大到小排序)
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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
     * @return 被手机客户端点击的次数
     */
    public Integer getSearchNum() {
        return searchNum;
    }

    /**
     * @param searchnum 
	 *            被手机客户端点击的次数
     */
    public void setSearchNum(Integer searchNum) {
        this.searchNum = searchNum;
    }

	public Integer getActionRc() {
		return actionRc;
	}

	public void setActionRc(Integer actionRc) {
		this.actionRc = actionRc;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}