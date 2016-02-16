

package com.x.publics.http.model;

import java.util.List;

/**
 * @ClassName: FeedbackDialogueResponse
 * @Description: TODO(提交反馈:从服务器拉回来的对话内容)
 
 * @date 2014-7-23 上午9:34:18
 * 
 */

public class FeedbackDialogueResponse  extends CommonResponse{

	public List<FeedbackDialogueBean> feedbackClientList;
	public boolean isLast;
	public int feedbackNum;
	
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
	public List<FeedbackDialogueBean> getFeedbackClientList() {
		return feedbackClientList;
	}
	public void setFeedbackClientList(List<FeedbackDialogueBean> feedbackClientList) {
		this.feedbackClientList = feedbackClientList;
	}
	
	
	
}
