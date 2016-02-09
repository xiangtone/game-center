/**   
* @Title: HomeCollectionDetailRequest.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-3 下午4:56:05
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;

/**
* @ClassName: HomeCollectionDetailRequest
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-3 下午4:56:05
* 
*/

public class HomeCollectionDetailRequest extends CommonRequest {

	public Pager page;

	public HomeCollectionDetailRequestData data;

	public HomeCollectionDetailRequest() {
		super(Constan.Rc.GET_HOME_COLLECTION_DETAIL, Constan.SIGN);
	}

	public static class HomeCollectionDetailRequestData {

		public HomeCollectionDetailRequestData(int collectionId) {
			this.ct = Constan.Ct.HOME_COLLECTION;
			this.collectionId = collectionId;
		}

		public int ct;
		public int collectionId;

		public int getCt() {
			return ct;
		}

		public void setCt(int ct) {
			this.ct = ct;
		}

		public int getCollectionId() {
			return collectionId;
		}

		public void setCollectionId(int collectionId) {
			this.collectionId = collectionId;
		}

	}

	public Pager getPage() {
		return page;
	}

	public void setPage(Pager page) {
		this.page = page;
	}

	public HomeCollectionDetailRequestData getData() {
		return data;
	}

	public void setData(HomeCollectionDetailRequestData data) {
		this.data = data;
	}

}
