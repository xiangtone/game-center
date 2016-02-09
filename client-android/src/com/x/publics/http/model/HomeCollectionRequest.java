/**   
* @Title: HomeCollectionRequest.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-3 下午4:36:41
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
* @ClassName: HomeCollectionRequest
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-3 下午4:36:41
* 
*/

public class HomeCollectionRequest extends CommonRequest {

	public Pager page;

	public HomeCollectionRequestData data;

	public HomeCollectionRequest() {
		super(Constan.Rc.GET_HOME_COLLECTION, Constan.SIGN);
	}

	public static class HomeCollectionRequestData {

		public HomeCollectionRequestData() {
			this.ct = Constan.Ct.HOME_COLLECTION;
			this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
		}

		public int ct;

		public int raveId;

		public int getCt() {
			return ct;
		}

		public void setCt(int ct) {
			this.ct = ct;
		}

		public int getRaveId() {
			return raveId;
		}

		public void setRaveId(int raveId) {
			this.raveId = raveId;
		}
	}

	public Pager getPage() {
		return page;
	}

	public void setPage(Pager page) {
		this.page = page;
	}

	public HomeCollectionRequestData getData() {
		return data;
	}

	public void setData(HomeCollectionRequestData data) {
		this.data = data;
	}
}
