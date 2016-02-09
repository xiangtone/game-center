/**   
* @Title: FeedbackListRequest.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-8-12 下午4:36:25
* @version V1.0   
*/


package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
* @ClassName: FeedbackListRequest
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-8-12 下午4:36:25
* 
*/

public class FeedbackListRequest extends CommonRequest {

	public String ps;
	public String pn;

	public FeedbackListRequest() {
		super(Constan.Rc.GET_FEEDBACKLIST, Constan.SIGN);
	}

	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}


}
