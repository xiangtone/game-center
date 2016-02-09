/**   
* @Title: UserFeedbackBean.java
* @Package com.mas.amineappstore.publics.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-7-23 上午9:34:18
* @version V1.0   
*/


package com.x.publics.http.model;

/**
 * @ClassName: UserFeedbackBean
 * @Description: TODO(用户反馈列表实体类)
 
 * @date 2014-7-23 上午9:34:18
 * 
 */

public class UserFeedbackBean {


	private String question;
	private String replyContent;
	private String createTime;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
