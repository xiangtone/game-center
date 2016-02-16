package com.mas.rave.main.vo;

import java.util.Date;

/**
 * app对应主题
 * 
 * @author liwei.sz
 * 
 */
public class AppAlbumTheme {
	private int themeId;
	private AppAlbumColumn appAlbumColumn;// 栏目Id
	private String name;// 主题名称',
	private String nameCn;// 主题中文名称',
	private String icon;// 主题的小图标（列表显示）',
	private String bigicon;// 主题大图（页签详情时要显示）',
	private String description;// 主题描述',
	private int sort;// 排序（按数字大小从大到小排序）',
	private boolean state;// 状态 0否 1是
	private int flag;// 标识1表示资源、2表示列表',
	private AppFile appFile;// flag为1时对应的t_app_file的Id，2时为空或0',
	private AppInfo appInfo;// 针对开发者特需处理
	private Date createTime;// 创建时间',
	private Date updateTime;
	private String operator;// 后台操作人
	private int raveId;
	private Country country;
	private int apkId;
	private int appId;

	public int getThemeId() {
		return themeId;
	}

	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

	public AppAlbumColumn getAppAlbumColumn() {
		return appAlbumColumn;
	}

	public void setAppAlbumColumn(AppAlbumColumn appAlbumColumn) {
		this.appAlbumColumn = appAlbumColumn;
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public AppFile getAppFile() {
		return appFile;
	}

	public void setAppFile(AppFile appFile) {
		this.appFile = appFile;
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		if (country != null) {
			this.setRaveId(country.getId());
		}
		this.country = country;
	}

	public int getRaveId() {
		return raveId;
	}

	public void setRaveId(int raveId) {
		this.raveId = raveId;
	}

	public int getApkId() {
		return apkId;
	}

	public void setApkId(int apkId) {
		this.apkId = apkId;
	}

	public AppInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

}
