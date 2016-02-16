/**   
* @Title: AppGamesCategoryRequest.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-10-20 上午11:29:23
* @version V1.0   
*/


package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;

/**
 * @ClassName: AppGamesCategoryRequest
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2015-10-20 上午11:29:23
 * 
 */

public class AppGamesCategoryRequest extends CommonRequest {

	public int raveId;
	public int ct;
	public Pager pager;
	
	
	public AppGamesCategoryRequest() {
		super(Constan.Rc.APP_GAMES_CATEGORY, Constan.SIGN);
		this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
	}

	public int getRaveId() {
		return raveId;
	}

	public void setRaveId(int raveId) {
		this.raveId = raveId;
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
