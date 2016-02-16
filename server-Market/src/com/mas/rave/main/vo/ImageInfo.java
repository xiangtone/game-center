package com.mas.rave.main.vo;

import java.util.Date;
import java.util.List;

/**
 * Image基本信息
 * 
 * @author jieding
 * 
 */
public class ImageInfo extends PagingBean {

	/** 编号 */
	private int id;
	/** 图片名称 */
	private String name;
	/** 拼音 */
	private String pinyin;
	/** 图标 */
	private String logo;
	/** 中图标 */
	private String biglogo;
	/** 资源下载 */
	private String url;
	/** 图片宽度 */
	private int width;
	/** 图片长度 */
	private int length;
	/** 图片大小 */
	private int fileSize;
	/** 资费 */
	private int free;
	/** 星级 */
	private int stars;
	/** 类型 */
	private Category category;
	/** 简介 */
	private String brief;
	/** 描述 */
	private String description;
	/** 初始化下载量 */
	private int initDowdload;
	/** 真实下载量 */
	private int realDowdload;
	/** 创建日期 */
	private Date createTime;
	/** 更新日期 */
	private Date updateTime;
	/** 状态 */
	private boolean state;
	/** 后台操作者 */
	private String operator;
	/** 父类菜单id */
	private int category_parent;

	private int category_parent1;

	private List<Integer> ids;
	private int searchkey;
	private ImageAlbumRes imageAlbumRes;

	private Integer raveId;// 平台国家Id',
	private Country country;

	private String imageAddress;// add by dingjie 2014/7/11 ftp地址
	private String uploadType;

	// add by dingjie 2014/7/29 资源的别名用于搜索
	private String anotherName;
	// add by dingjie 2014/8/02 录入的资源的来源
	private String source;

	public Integer getRaveId() {
		return raveId;
	}

	public void setRaveId(Integer raveId) {
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

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getFree() {
		return free;
	}

	public void setFree(int free) {
		this.free = free;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getBiglogo() {
		return biglogo;
	}

	public void setBiglogo(String biglogo) {
		this.biglogo = biglogo;
	}

	public int getCategory_parent() {
		return category_parent;
	}

	public void setCategory_parent(int category_parent) {
		this.category_parent = category_parent;
	}

	public ImageAlbumRes getImageAlbumRes() {
		return imageAlbumRes;
	}

	public void setImageAlbumRes(ImageAlbumRes imageAlbumRes) {
		this.imageAlbumRes = imageAlbumRes;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public int getSearchkey() {
		return searchkey;
	}

	public void setSearchkey(int searchkey) {
		this.searchkey = searchkey;
	}

	public String getImageAddress() {
		return imageAddress;
	}

	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public String getAnotherName() {
		return anotherName;
	}

	public void setAnotherName(String anotherName) {
		this.anotherName = anotherName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getCategory_parent1() {
		return category_parent1;
	}

	public void setCategory_parent1(int category_parent1) {
		this.category_parent1 = category_parent1;
	}

}
