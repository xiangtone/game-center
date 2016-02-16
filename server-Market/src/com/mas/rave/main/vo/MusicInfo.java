package com.mas.rave.main.vo;

import java.util.Date;
import java.util.List;

/**
 * music 基本信息
 * 
 * @author jieding
 * 
 */
public class MusicInfo extends PagingBean {
	/** 编号 */
	private int id;
	/** 音乐名 */
	private String name;
	/** 歌唱者 */
	private String artist;
	/** 拼音 */
	private String pinyin;
	/** 图标 */
	private String logo;
	/** 资源下载 */
	private String url;
	/** 大小 */
	private int fileSize;
	/** 资费 */
	private int free;
	/** 星级 */
	private int stars;
	/** 时间长度 */
	private int duration;
	/** 时间长度字符串 add by dingjie 2015-04-28 */
	private String durationString;
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

	private MusicAlbumRes musicAlbumRes;

	private int category_parent;
	private int category_parent1;

	private Integer raveId;// 平台国家Id',
	private Country country;

	private List<Integer> ids;
	private int searchkey;

	private String musicAddress;// add by dingjie 2014/7/10 ftp地址
	private String uploadType;
	// add by dingjie 2014/7/29 资源的别名用于搜索
	private String anotherName;

	// add by dingjie 2014/8/02 录入的资源的来源
	private String source;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
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

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		if (duration != 0) {
			if (duration > 3600) {
				this.durationString = duration / 3600 + ":" + (duration % 3600) / 60 + ":" + (duration % 3600) % 60;
			} else if (duration > 60 && duration < 3600) {
				if (duration % 60 >= 10) {
					this.durationString = "00:" + duration / 60 + ":" + (duration % 60);
				} else {
					this.durationString = "00:" + duration / 60 + ":0" + (duration % 60);
				}
			} else {
				this.durationString = "00:00:" + duration;
			}
		}
		this.duration = duration;
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

	public MusicAlbumRes getMusicAlbumRes() {
		return musicAlbumRes;
	}

	public void setMusicAlbumRes(MusicAlbumRes musicAlbumRes) {
		this.musicAlbumRes = musicAlbumRes;
	}

	public int getCategory_parent() {
		return category_parent;
	}

	public void setCategory_parent(int category_parent) {
		this.category_parent = category_parent;
	}

	public String getMusicAddress() {
		return musicAddress;
	}

	public void setMusicAddress(String musicAddress) {
		this.musicAddress = musicAddress;
	}

	public String getAnotherName() {
		return anotherName;
	}

	public void setAnotherName(String anotherName) {
		this.anotherName = anotherName;
	}

	public String getDurationString() {
		if (durationString == null || durationString.equals("")) {
			return "00:00:00";
		}
		return durationString;
	}

	public void setDurationString(String durationString) {
		this.durationString = durationString;
	}

	public int getCategory_parent1() {
		return category_parent1;
	}

	public void setCategory_parent1(int category_parent1) {
		this.category_parent1 = category_parent1;
	}

}
