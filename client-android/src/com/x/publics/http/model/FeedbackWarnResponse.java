/**   
* @Title: FeedbackListResponse.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-8-12 下午4:31:13
* @version V1.0   
*/


package com.x.publics.http.model;

import java.util.List;

/**
* @ClassName: FeedbackWarnResponse
* @Description: TODO(回馈提醒)

* @date 2014-8-12 下午4:31:13
* 
*/

public class FeedbackWarnResponse  extends CommonResponse{

	public int feedbackCode;
	public boolean feedbackAttention;
	
	
	public int getFeedbackCode() {
		return feedbackCode;
	}
	public void setFeedbackCode(int feedbackCode) {
		this.feedbackCode = feedbackCode;
	}
	public boolean isFeedbackAttention() {
		return feedbackAttention;
	}
	public void setFeedbackAttention(boolean feedbackAttention) {
		this.feedbackAttention = feedbackAttention;
	}
	
	
}
