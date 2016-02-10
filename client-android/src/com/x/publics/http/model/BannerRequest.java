/**   
* @Title: BannerRequest.java
* @Package com.x.http.model
* @Description: TODO 

* @date 2014-2-13 下午02:59:24
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
* @ClassName: BannerRequest
* @Description: TODO 

* @date 2014-2-13 下午02:59:24
* 
*/

public class BannerRequest extends CommonRequest {

	public int ct;
	public int raveId;

	public BannerRequest() {
		super(Constan.Rc.GET_BANNER, Constan.SIGN);
		this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
	}

	public int getCt() {
		return ct;
	}

	public void setCt(int ct) {
		this.ct = ct;
	}

}
