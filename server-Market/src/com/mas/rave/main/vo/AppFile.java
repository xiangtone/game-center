package com.mas.rave.main.vo;

import java.util.Date;

/**
 * app对应文件信息
 * 
 * @author liwei.sz
 * 
 */
public class AppFile implements Cloneable {
	private int id;
	private AppInfo appInfo;// appInfoId',
	private String appName; // app名字
	private Channel channel;
	private Cp cp;
	private String apkKey;
	private int serverId;
	private int upgradeType;// '1不更新，2下拉框更新，3对话框更新',
	private String packageName;
	private int osType;// 操作系统类型 1,ios 2.andriod',
	private int versionCode;// 版本号',
	private String versionName;// 版本名',
	private String url;// 下载地址
	private String resolution;// 分辨率（默认all是支持所有的
	private int fileSize;// 大小',
	private String cpChannelCode;// 游戏厂商分配的游戏的渠道号',
	private String updateInfo;// 游戏更新信息',
	private String remark;// 备注',
	private boolean state;// 有效值.0无效 1有效',
	private Date createTime;// 创建时间
	private Date updateTime; // 更新时间
	private int language;// 语言 1,中文 2,英语 3,其他',
	private boolean haslist;
	private boolean checked;
	private int raveId; // 所属区id

	private int type;// 0=黑名单，1=自动更新，2=需要增量

	private int appId;
	private Country country;
	private int cpState;

	private String logo;
	private String raveNames; // contact_group(rave name)
	private String nameEn; // 国家英文名

	public String getRaveNames() {
		return raveNames;
	}

	public void setRaveNames(String raveNames) {
		this.raveNames = raveNames;
	}

	public int getCpState() {
		return cpState;
	}

	public void setCpState(int cpState) {
		this.cpState = cpState;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApkKey() {
		return apkKey;
	}

	public void setApkKey(String apkKey) {
		this.apkKey = apkKey;
	}

	public int getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getCpChannelCode() {
		return cpChannelCode;
	}

	public void setCpChannelCode(String cpChannelCode) {
		this.cpChannelCode = cpChannelCode;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public AppInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Cp getCp() {
		return cp;
	}

	public void setCp(Cp cp) {
		this.cp = cp;
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

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getOsType() {
		return osType;
	}

	public void setOsType(int osType) {
		this.osType = osType;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public boolean isHaslist() {
		return haslist;
	}

	public void setHaslist(boolean haslist) {
		this.haslist = haslist;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRaveId() {
		return raveId;
	}

	public void setRaveId(int raveId) {
		this.raveId = raveId;
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

	public Object clone() {
		AppFile o = null;
		try {
			o = (AppFile) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}


}
