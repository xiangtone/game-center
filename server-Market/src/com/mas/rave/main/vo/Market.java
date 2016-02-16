package com.mas.rave.main.vo;

import java.util.Date;

/**
 * 平台信息
 * 
 * @author liwei.sz
 * 
 */
public class Market{
	private int id;// 游戏编号',
	private String name;// 应用名',
	private String password; // 密码
	private String pwd;// 密码(明文
	private Channel channel; // 渠道信息
	private int cpId;// 游戏的厂商ID',
	private String apKKey;// 为每个游戏分配的appkey',
	private String tags;// 标签可能会有多个，以|分割',
	private boolean isUpgrade;// 1不更新，2下拉框更新，3对话框更新
	private String pinyin;// pingyin
	private String free;// 是否免费 0免费　1收费',
	private String brief;// 描述',
	private String description;// 长的介绍',
	private String logo;// 图标',
	private String bigLogo;// 大图',
	private String path; // 绝对路径
	private int stars;// 星级',
	private String officalIcon;// 官网 游戏分类小图标',
	private double officalScore;// 游戏评分',;
	private String officalImg;// 官网 游戏列表配图',
	private String backgroundImg;// 微官网banner大图',
	private String md5Code;// md5加密
	private int osType;// 操作系统类型',
	private int machineType;// 机器类型(未确定)',
	private String keyword;// 关键字
	private String initial;// 首字母',
	private Integer hasSite;// 是否有微官网
	private Date updateTime;// 更新时间',
	private String operator;// 操作员',
	private String remark;// 备注',
	private boolean state;// 有效值.0无效 1有效',
	private Category category;// 类型编号',
	private Date createTime; // 创建时间
	private int sort;// 排序值

	private int tag;

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

	public String getApKKey() {
		return apKKey;
	}

	public void setApKKey(String apKKey) {
		this.apKKey = apKKey;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isUpgrade() {
		return isUpgrade;
	}

	public void setUpgrade(boolean isUpgrade) {
		this.isUpgrade = isUpgrade;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getFree() {
		return free;
	}

	public void setFree(String free) {
		this.free = free;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getOfficalIcon() {
		return officalIcon;
	}

	public void setOfficalIcon(String officalIcon) {
		this.officalIcon = officalIcon;
	}

	public double getOfficalScore() {
		return officalScore;
	}

	public void setOfficalScore(double officalScore) {
		this.officalScore = officalScore;
	}

	public String getOfficalImg() {
		return officalImg;
	}

	public void setOfficalImg(String officalImg) {
		this.officalImg = officalImg;
	}

	public String getBackgroundImg() {
		return backgroundImg;
	}

	public void setBackgroundImg(String backgroundImg) {
		this.backgroundImg = backgroundImg;
	}

	public String getMd5Code() {
		return md5Code;
	}

	public void setMd5Code(String md5Code) {
		this.md5Code = md5Code;
	}

	public int getOsType() {
		return osType;
	}

	public void setOsType(int osType) {
		this.osType = osType;
	}

	public int getMachineType() {
		return machineType;
	}

	public void setMachineType(int machineType) {
		this.machineType = machineType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public Integer getHasSite() {
		return hasSite;
	}

	public void setHasSite(Integer hasSite) {
		this.hasSite = hasSite;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getCpId() {
		return cpId;
	}

	public void setCpId(int cpId) {
		this.cpId = cpId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}