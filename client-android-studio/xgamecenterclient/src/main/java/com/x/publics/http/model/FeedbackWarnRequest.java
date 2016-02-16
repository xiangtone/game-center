/**   
* @Title: FeedbackWarnRequest.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-8-12 下午5:34:50
* @version V1.0   
*/


package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
* @ClassName: FeedbackWarnRequest
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-8-12 下午5:34:50
* 
*/

public class FeedbackWarnRequest extends CommonRequest {

	public String imei;

	public FeedbackWarnRequest() {
		super(Constan.Rc.GET_FEEDBACKWARN, Constan.SIGN);
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
}
