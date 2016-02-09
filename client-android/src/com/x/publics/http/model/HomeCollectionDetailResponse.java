/**   
* @Title: HomeCollectionDetailResponse.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-3 下午4:56:29
* @version V1.0   
*/

package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.publics.model.AppInfoBean;

/**
* @ClassName: HomeCollectionDetailResponse
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-3 下午4:56:29
* 
*/

public class HomeCollectionDetailResponse extends CommonResponse {

	public int appNum;
	public boolean isLast;
	public ArrayList<AppInfoBean> applist = new ArrayList<AppInfoBean>();

	public int getAppNum() {
		return appNum;
	}

	public void setAppNum(int appNum) {
		this.appNum = appNum;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public ArrayList<AppInfoBean> getApplist() {
		return applist;
	}

	public void setApplist(ArrayList<AppInfoBean> applist) {
		this.applist = applist;
	}
}
