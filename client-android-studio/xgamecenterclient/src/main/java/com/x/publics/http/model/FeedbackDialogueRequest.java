/**   
* @Title: FeedbackDialogueRequest.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-8-12 下午3:02:08
* @version V1.0   
*/


package com.x.publics.http.model;

import com.x.publics.http.model.FeedbackRequest.FeedbackData;
import com.x.publics.http.model.FeedbackRequest.MasPlay;
import com.x.publics.utils.Constan;

/**
* @ClassName: FeedbackDialogueRequest
* @Description: TODO(用户回馈记录接口（rc=30024),从服务端拉下的对话内容)

* @date 2014-8-12 下午3:02:08
* 
*/

public class FeedbackDialogueRequest extends CommonRequest {

	public String imei;
	public String ps;
	public String pn;

	public FeedbackDialogueRequest() {
		super(Constan.Rc.GET_FEEDBACKDIALOGUE, Constan.SIGN);
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
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
