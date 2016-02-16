package com.mas.rave.vo;

import java.util.HashMap;
import java.util.Map;

public class AppResourceVO {

	// 应用编号',
	// private int appId;

	// app名称
	private String appName;

	// 父级渠道编号
	private int fatherChannelId;

	// 渠道编号
	private int channelId;

	// cp编号
	private int cpId;

	// 资源类型(0、公共资源 ,1、平台 ,2、自运营,3、批量)
	private int free;

	// app分类
	private int categoryId;

	// 应用简介
	private String brief;

	// app描述
	// private String appDesc;

	// app小logo图
	private String logo;

	// app大logo图
	private String bigLogo;

	// 星级
	private int stars;

	// 状态,0:表示上传后暂不显示,1:表示上传成功后直接显示
	private int state;

	// app排序
	private int sort;

	// app排序(默认为1)
	private int serverId;

	// 升级方式(1不更新,2下拉框更新,3对话框更新)
	private int upgradeType;

	// apk资源库的相对地址
	private String apkUrl;

	// 操作系统类型(1,ios, 2.andriod )
	private int osType;

	// app更新信息
	// private String updateInfo;

	// 语言 (1,中文 2,英语 3,其他)
	private int language;

	// 更新描述
	private String updateRemark;

	// 虚假下载量
	private int initDowdload;

	/**
	 * app截图地址，多张截图用;号分割
	 */
	private Map<String, String> picUrls = new HashMap<String, String>();

	/**
	 * app截图标题
	 */
	private String picTitle;

	/**
	 * app截图描述
	 */
	private String picDesc;

	/**
	 * app包名
	 */
	private String packageName;

	private Integer versionCode;

	private String versionName;

	/**
	 * 资源分类目录
	 */
	private String type;

	private int upType;

	private int replaceType;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getFatherChannelId() {
		return fatherChannelId;
	}

	public void setFatherChannelId(int fatherChannelId) {
		this.fatherChannelId = fatherChannelId;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getCpId() {
		return cpId;
	}

	public void setCpId(int cpId) {
		this.cpId = cpId;
	}

	public int getFree() {
		return free;
	}

	public void setFree(int free) {
		this.free = free;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getUpgradeType() {
		return upgradeType;
	}

	public void setUpgradeType(int upgradeType) {
		this.upgradeType = upgradeType;
	}

	public int getOsType() {
		return osType;
	}

	public void setOsType(int osType) {
		this.osType = osType;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public String getUpdateRemark() {
		return updateRemark;
	}

	public void setUpdateRemark(String updateRemark) {
		this.updateRemark = updateRemark;
	}

	public int getInitDowdload() {
		return initDowdload;
	}

	public void setInitDowdload(int initDowdload) {
		this.initDowdload = initDowdload;
	}

	public String getPicTitle() {
		return picTitle;
	}

	public void setPicTitle(String picTitle) {
		this.picTitle = picTitle;
	}

	public String getPicDesc() {
		return picDesc;
	}

	public void setPicDesc(String picDesc) {
		this.picDesc = picDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBigLogo() {
		return bigLogo;
	}

	public void setBigLogo(String bigLogo) {
		this.bigLogo = bigLogo;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public Map<String, String> getPicUrls() {
		return picUrls;
	}

	public void setPicUrls(Map<String, String> picUrls) {
		this.picUrls = picUrls;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getUpType() {
		return upType;
	}

	public void setUpType(int upType) {
		this.upType = upType;
	}

	public int getReplaceType() {
		return replaceType;
	}

	public void setReplaceType(int replaceType) {
		this.replaceType = replaceType;
	}

}
