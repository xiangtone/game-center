/**   
* @Title: BannerInfoBean.java
* @Package com.x.model
* @Description: TODO 

* @date 2014-2-13 下午03:02:48
* @version V1.0   
*/

package com.x.publics.model;

/**
* @ClassName: BannerInfoBean
* @Description: TODO 

* @date 2014-2-13 下午03:02:48
* 
*/

public class BannerInfoBean {

	private String name;
	private int apkId;
	private String icon;
	private String bigicon;
	private String description;
	private int fileType;//文件类型 1:应用，2:游戏

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getApkId() {
		return apkId;
	}

	public void setApkId(int apkId) {
		this.apkId = apkId;
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

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
}
