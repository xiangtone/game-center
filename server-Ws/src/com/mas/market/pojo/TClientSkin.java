package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TClientSkin implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * skinId编号
     */
    private Integer skinId;

    /**
     * skinName应用名
     */
    private String skinName;

    /**
     * 图标
     */
    private String logo;

    /**
     * 描述
     */
    private String description;

    /**
     * 应用包名
     */
    private String packageName;

    /**
     * 版本名
     */
    private String versionName;

    /**
     * 版本号
     */
    private Integer versionCode;

    /**
     * 下载地址
     */
    private String apkUrl;

    /**
     * 大小
     */
    private Integer apkSize;

    /**
     * 下载次数
     */
    private Integer downLoadNum;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否启用
     */
    private Boolean state;

    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * @return skinId编号
     */
    public Integer getSkinId() {
        return skinId;
    }

    /**
     * @param skinid 
	 *            skinId编号
     */
    public void setSkinId(Integer skinId) {
        this.skinId = skinId;
    }

    /**
     * @return skinName应用名
     */
    public String getSkinName() {
        return skinName;
    }

    /**
     * @param skinname 
	 *            skinName应用名
     */
    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    /**
     * @return 图标
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param logo 
	 *            图标
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 
	 *            描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 应用包名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packagename 
	 *            应用包名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return 版本名
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * @param versionname 
	 *            版本名
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * @return 版本号
     */
    public Integer getVersionCode() {
        return versionCode;
    }

    /**
     * @param versioncode 
	 *            版本号
     */
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return 下载地址
     */
    public String getApkUrl() {
        return apkUrl;
    }

    /**
     * @param apkurl 
	 *            下载地址
     */
    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    /**
     * @return 大小
     */
    public Integer getApkSize() {
        return apkSize;
    }

    /**
     * @param apksize 
	 *            大小
     */
    public void setApkSize(Integer apkSize) {
        this.apkSize = apkSize;
    }

    /**
     * @return 下载次数
     */
    public Integer getDownLoadNum() {
        return downLoadNum;
    }

    /**
     * @param downloadnum 
	 *            下载次数
     */
    public void setDownLoadNum(Integer downLoadNum) {
        this.downLoadNum = downLoadNum;
    }

    /**
     * @return 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort 
	 *            排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return 是否启用
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            是否启用
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

    /**
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updatetime 
	 *            更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}