/**   
* @Title: HomeMustHaveResponse.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-4 上午11:03:33
* @version V1.0   
*/

package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.publics.model.AppInfoBean;

/**
* @ClassName: HomeMustHaveResponse
* @Description: MustHave应答类

* @date 2014-9-4 上午11:03:33
* 
*/

public class HomeMustHaveResponse extends CommonResponse {

	public int mustHaveNum;
	public boolean isLast;
	public ArrayList<MustHaveCategoryList> mustHavelist = new ArrayList<MustHaveCategoryList>();

	public class MustHaveCategoryList {

		public ArrayList<AppInfoBean> applist = new ArrayList<AppInfoBean>();
		public String name;
		public int appNum;
		public int id;

		public ArrayList<AppInfoBean> getApplist() {
			return applist;
		}

		public void setApplist(ArrayList<AppInfoBean> applist) {
			this.applist = applist;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAppNum() {
			return appNum;
		}

		public void setAppNum(int appNum) {
			this.appNum = appNum;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	}

	public int getMustHaveNum() {
		return mustHaveNum;
	}

	public void setMustHaveNum(int mustHaveNum) {
		this.mustHaveNum = mustHaveNum;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public ArrayList<MustHaveCategoryList> getMustHavelist() {
		return mustHavelist;
	}

	public void setMustHavelist(ArrayList<MustHaveCategoryList> mustHavelist) {
		this.mustHavelist = mustHavelist;
	}


}
