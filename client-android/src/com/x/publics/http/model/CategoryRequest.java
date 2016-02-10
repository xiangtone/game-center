/**   
* @Title: CategoryRequest.java
* @Package com.x.http.model
* @Description: TODO 

* @date 2014-2-14 上午09:37:10
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
* @ClassName: CategoryRequest
* @Description: TODO 

* @date 2014-2-14 上午09:37:10
* 
*/

public class CategoryRequest extends CommonRequest {

	public Pager pager;
	public int ct;
	public int raveId;

	public CategoryRequest() {
		super(Constan.Rc.GET_CATEGORIES, Constan.SIGN);
		this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
	}

	public int getCt() {
		return ct;
	}

	public void setCt(int ct) {
		this.ct = ct;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

}
