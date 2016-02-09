/**   
* @Title: CategoryInfoBean.java
* @Package com.mas.amineappstore.model
* @Description: TODO 

* @date 2014-2-13 下午08:55:00
* @version V1.0   
*/

package com.x.publics.model;

/**
* @ClassName: CategoryInfoBean
* @Description: TODO 

* @date 2014-2-13 下午08:55:00
* 
*/

public class CategoryInfoBean {

	private String name;
	private int categoryId;
	private String icon;
	private String bigicon;
	private int appNums;
	private String recommend;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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

	public int getAppNums() {
		return appNums;
	}

	public void setAppNums(int appNums) {
		this.appNums = appNums;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

}
