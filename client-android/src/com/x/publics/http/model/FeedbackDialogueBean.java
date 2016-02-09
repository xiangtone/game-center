/**   
* @Title: FeedbackDialogueBean.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-8-12 下午3:52:06
* @version V1.0   
*/

package com.x.publics.http.model;

/**
* @ClassName: FeedbackDialogueBean
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-8-12 下午3:52:06
* 
*/

public class FeedbackDialogueBean {

	private String content;//对话内容
	private long sendTime;//用户或者运维人员的内容发送时间
	private int feedbackType;//区分消息来源，是用户的还是运维人员的
	private boolean isShow;//是否显示

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public int getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(int feedbackType) {
		this.feedbackType = feedbackType;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

}
