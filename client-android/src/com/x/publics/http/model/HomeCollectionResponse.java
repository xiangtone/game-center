/**   
* @Title: HomeCollectionResponse.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-3 下午4:47:50
* @version V1.0   
*/

package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.publics.model.AppCollectionBean;

/**
* @ClassName: HomeCollectionResponse
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-3 下午4:47:50
* 
*/

public class HomeCollectionResponse extends CommonResponse {

	public int collectionNum;
	public boolean isLast;
	public ArrayList<AppCollectionBean> collectionList = new ArrayList<AppCollectionBean>();

	public int getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(int collectionNum) {
		this.collectionNum = collectionNum;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public ArrayList<AppCollectionBean> getCollectionList() {
		return collectionList;
	}

	public void setCollectionList(ArrayList<AppCollectionBean> collectionList) {
		this.collectionList = collectionList;
	}
}
