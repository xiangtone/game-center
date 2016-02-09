/**   
* @Title: HomeMustHaveRequest.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-4 上午11:03:17
* @version V1.0   
*/


package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.http.model.HomeCollectionRequest.HomeCollectionRequestData;
import com.x.publics.utils.Constan;

/**
* @ClassName: HomeMustHaveRequest
* @Description: MustHave请求类

* @date 2014-9-4 上午11:03:17
* 
*/

public class HomeMustHaveRequest extends CommonRequest {

	public Pager page;

	public HomeMustHaveRequestData data;
	
	public HomeMustHaveRequest() {
		super(Constan.Rc.GET_HOME_MUST_HAVE, Constan.SIGN);
	}
	
	public static class HomeMustHaveRequestData {

		public HomeMustHaveRequestData() {
			this.ct = Constan.Ct.HOME_MUST_HAVE;
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

	public HomeMustHaveRequestData getData() {
		return data;
	}

	public void setData(HomeMustHaveRequestData data) {
		this.data = data;
	}

}
