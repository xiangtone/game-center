/**   
 * @Title: CategoryBean.java
 * @Package com.x.publics.model
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2015-10-20 下午3:52:10
 * @version V1.0   
 */

package com.x.publics.model;

import java.util.List;

/**
 * @ClassName: CategoryBean
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2015-10-20 下午3:52:10
 * 
 */

public class CategoryBean {
	private String name;
	private String recommend;
	private int categoryId;
	private String icon;
	private String bigicon;
	private int secondaryCatListSize;
	private List<SecondaryCatBean> secondaryCatList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
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

	public int getSecondaryCatListSize() {
		return secondaryCatListSize;
	}

	public void setSecondaryCatListSize(int secondaryCatListSize) {
		this.secondaryCatListSize = secondaryCatListSize;
	}

	public List<SecondaryCatBean> getSecondaryCatList() {
		return secondaryCatList;
	}

	public void setSecondaryCatList(List<SecondaryCatBean> secondaryCatList) {
		this.secondaryCatList = secondaryCatList;
	}


	public static class SecondaryCatBean {
		private String name;
		private int categoryId;
		
		
		
		public SecondaryCatBean() {
			super();
		}
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
		
		
	}
}
