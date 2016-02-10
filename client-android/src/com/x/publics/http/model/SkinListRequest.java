/**   
* @Title: SkinListRequest.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-12-3 下午2:00:14
* @version V1.0   
*/


package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;

/**
* @ClassName: SkinListRequest
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-12-3 下午2:00:14
* 
*/

public class SkinListRequest extends CommonRequest{

	public Pager pager;
	public int raveId;
	
	public SkinListRequest() {
		super(Constan.Rc.SKIN_LIST, Constan.SIGN);
	}


	public int getRaveId() {
		return raveId;
	}

	public void setRaveId(int raveId) {
		this.raveId = raveId;
	}


	public Pager getPager() {
		return pager;
	}


	public void setPager(Pager pager) {
		this.pager = pager;
	}
	

}
