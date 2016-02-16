package com.mas.rave.main.vo;

import java.util.Date;

/**
 * app基本信息
 * 
 * @author liwei.sz
 * 
 */
public class AppInfo extends PagingBean {
	private int id;// 游戏编号',
	private String name;// 应用名',
	private String password; // 密码
	private String pwd;// 密码(明文
	private Channel channel; // 渠道信息
	private Cp cp;// 游戏的厂商ID',
	private String apKKey;// 为每个游戏分配的appkey',
	private String tags;// 标签可能会有多个，以|分割',
	private boolean isUpgrade;// 1不更新，2下拉框更新，3对话框更新
	private String pinyin;// pingyin
	private int free;// 是否免费 0免费　1收费'
						// 0公共资源1平台2自运营，fatherChannelId=100000,channelId固定在100003,cpId=300001）
	private String brief;// 描述',
	private String description;// 长的介绍',
	private String logo;// 图标',
	private String bigLogo;// 大图',
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
	private int initDowdload;// 初始下载数（初始5万到10万）',
	private int realDowdload;// 真正下载数',
	private int updateNum; // 更新数
	private int cpState; // 开发者状态
	private String source; // 责任人

	private int pageOpen;// 客户端浏览的次数,

	private int fatherChannelId;

	private int cpId;

	private int category_parent;
	private int category_parent1;

	private boolean checked;

	private String packageName;
	private int countryId;

	private Date startTime;
	private Date endTime;

	// add by dingjie 2014/7/29 资源的别名用于搜索
	private String anotherName;
	// add by dingjie 2014/7/25 export report
	private String cpName;
	private String channelName;
	private int channelId;
	private String firstCategoryName;
	private String firstCategoryNameCn;
	private String secondCategoryName;
	private String secondCategoryNameCn;

	private String pdfUrl;

	private String issuer;

	private double starsInit;
	private double starsReal;

	private Date initialReleaseDate;
	private String initialReleaseDate1;

	private String categoryName;

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

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

	public int getFree() {
		return free;
	}

	public void setFree(int free) {
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

	public Cp getCp() {
		return cp;
	}

	public void setCp(Cp cp) {
		if (cp != null) {
			this.setCpId(cp.getId());
			this.setCpName(cp.getName());
		}
		this.cp = cp;
	}

	public Category getCategory() {
		
		return category;
	}

	public void setCategory(Category category) {
		if (category != null) {
			this.setSecondCategoryNameCn(category.getCategoryCn());
			this.setSecondCategoryName(category.getName());
			if (category.getFatherId() == 2) {
				this.setFirstCategoryNameCn("应用");
				this.setFirstCategoryName("Apps");
			} else if (category.getFatherId() == 3) {
				this.setFirstCategoryNameCn("游戏");
				this.setFirstCategoryName("Games");
			}
		}
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
		if (channel != null) {
			this.setChannelId(channel.getId());
			this.setChannelName(channel.getName());
		}
		this.channel = channel;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getFatherChannelId() {
		return fatherChannelId;
	}

	public void setFatherChannelId(int fatherChannelId) {
		this.fatherChannelId = fatherChannelId;
	}

	public int getCategory_parent() {
		return category_parent;
	}

	public void setCategory_parent(int category_parent) {
		this.category_parent = category_parent;
	}

	public int getCpId() {
		return cpId;
	}

	public void setCpId(int cpId) {
		this.cpId = cpId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getInitDowdload() {
		return initDowdload;
	}

	public void setInitDowdload(int initDowdload) {
		this.initDowdload = initDowdload;
	}

	public int getRealDowdload() {
		return realDowdload;
	}

	public void setRealDowdload(int realDowdload) {
		this.realDowdload = realDowdload;
	}

	public int getUpdateNum() {
		return updateNum;
	}

	public void setUpdateNum(int updateNum) {
		this.updateNum = updateNum;
	}

	public int getPageOpen() {
		return pageOpen;
	}

	public void setPageOpen(int pageOpen) {
		this.pageOpen = pageOpen;
	}

	public String getCpName() {
		return cpName;
	}

	public void setCpName(String cpName) {
		this.cpName = cpName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getFirstCategoryName() {
		return firstCategoryName;
	}

	public void setFirstCategoryName(String firstCategoryName) {
		this.firstCategoryName = firstCategoryName;
	}

	public String getFirstCategoryNameCn() {
		return firstCategoryNameCn;
	}

	public void setFirstCategoryNameCn(String firstCategoryNameCn) {
		this.firstCategoryNameCn = firstCategoryNameCn;
	}

	public String getSecondCategoryName() {
		return secondCategoryName;
	}

	public void setSecondCategoryName(String secondCategoryName) {
		this.secondCategoryName = secondCategoryName;
	}

	public String getSecondCategoryNameCn() {
		return secondCategoryNameCn;
	}

	public void setSecondCategoryNameCn(String secondCategoryNameCn) {
		this.secondCategoryNameCn = secondCategoryNameCn;
	}

	public String getAnotherName() {
		return anotherName;
	}

	public void setAnotherName(String anotherName) {
		this.anotherName = anotherName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getCpState() {
		return cpState;
	}

	public void setCpState(int cpState) {
		this.cpState = cpState;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public double getStarsInit() {
		return starsInit;
	}

	public void setStarsInit(double starsInit) {
		this.starsInit = starsInit;
	}

	public Date getInitialReleaseDate() {
		return initialReleaseDate;
	}

	public void setInitialReleaseDate(Date initialReleaseDate) {
		this.initialReleaseDate = initialReleaseDate;
	}

	public String getInitialReleaseDate1() {
		return initialReleaseDate1;
	}

	public void setInitialReleaseDate1(String initialReleaseDate1) {
		this.initialReleaseDate1 = initialReleaseDate1;
	}

	public double getStarsReal() {
		return starsReal;
	}

	public void setStarsReal(double starsReal) {
		this.starsReal = starsReal;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCategory_parent1() {
		return category_parent1;
	}

	public void setCategory_parent1(int category_parent1) {
		this.category_parent1 = category_parent1;
	}

}
