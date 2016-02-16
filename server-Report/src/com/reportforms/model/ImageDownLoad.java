package com.reportforms.model;

import java.sql.Timestamp;

import com.reportforms.util.DateUtil;

/**
 * 图片下载
 * @author lisong.lan
 *
 */
public class ImageDownLoad extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2489614940858432813L;
	
	private Integer appType;
	
	private String appTypeString;
	
	private Integer imageId;
	
	private String imageName;
	
	private String theDate;
	
	private Integer categoryId;
	
	private String categoryName;
	
	private String countryCn;
	
	private String country;
	
	private Long downloadNum;
	
	private Timestamp createTime;
	
	private String createTimeString;

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public String getAppTypeString() {
		if(null != this.imageId){
			this.appTypeString = "Wallpaper";
		}else {
			return "";
		}
		return appTypeString;
	}
	
	public String getTheDate() {
		return theDate;
	}

	public void setTheDate(String theDate) {
		this.theDate = theDate;
	}

	public String getCountryCn() {
		return countryCn;
	}

	public void setCountryCn(String countryCn) {
		this.countryCn = countryCn;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(Long downloadNum) {
		this.downloadNum = downloadNum;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
		this.createTimeString = DateUtil.getTimestampToString(createTime);
	}
	
	public String getCreateTimeString() {
		return createTimeString;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
