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
* @ClassName: FeedbackListResponse
* @Description: TODO(常见反馈列表)

* @date 2014-8-12 下午4:31:13
* 
*/

public class FeedbackListResponse  extends CommonResponse{

	public List<UserFeedbackBean> feedbackCommonList;
	public boolean isLast;
	public int feedbackNum;
	
	
	public List<UserFeedbackBean> getFeedbackCommonList() {
		return feedbackCommonList;
	}
	public void setFeedbackCommonList(List<UserFeedbackBean> feedbackCommonList) {
		this.feedbackCommonList = feedbackCommonList;
	}
	public boolean isLast() {
		return isLast;
	}
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	public int getFeedbackNum() {
		return feedbackNum;
	}
	public void setFeedbackNum(int feedbackNum) {
		this.feedbackNum = feedbackNum;
	}
	
	
}
