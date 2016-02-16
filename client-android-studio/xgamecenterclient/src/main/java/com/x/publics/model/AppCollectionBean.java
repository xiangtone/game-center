/**   
* @Title: AppCollectionBean.java
* @Package com.x.publics.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-2 上午10:57:57
* @version V1.0   
*/

package com.x.publics.model;

import java.io.Serializable;

/**
* @ClassName: AppCollectionBean
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-2 上午10:57:57
* 
*/

public class AppCollectionBean implements Serializable {

	private static final long serialVersionUID = -8144714306041122425L;

	private int collectionId;
	private String name;
	private String icon;
	private String bigicon;
	private String description;
	private String publishTime;
	
	public int getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
}
