/**   
* @Title: TabRequest.java
* @Package com.mas.amineappstore.http.model
* @Description: TODO 

* @date 2014-1-15 上午09:50:54
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
* @ClassName: TabRequest
* @Description: TODO 

* @date 2014-1-15 上午09:50:54
* 
*/

public class TabRequest extends CommonRequest {

	public Pager page;

	public TabRequestData data;

	public TabRequest() {
		super(Constan.Rc.GET_TAB_RESOURCE, Constan.SIGN);
	}

	public static class TabRequestData {

		public TabRequestData(int ct) {
			this.ct = ct;
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

	public Pager getPager() {
		return page;
	}

	public void setPager(Pager pager) {
		this.page = pager;
	}

	public TabRequestData getData() {
		return data;
	}

	public void setData(TabRequestData data) {
		this.data = data;
	}

}
