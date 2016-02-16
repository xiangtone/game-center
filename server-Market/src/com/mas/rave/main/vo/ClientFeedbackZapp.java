package com.mas.rave.main.vo;

import java.util.Date;

/**
 * 常用回馈
 * @author jieding
 *
 */
public class ClientFeedbackZapp {

	private int id;
	/*提问*/
	private String question;
	/*回答内容*/
	private String replyContent;
	/*是否生效*/
	private boolean state;
	/*回复时间*/
	private Date createTime;
	
	private String createTime1;
	public String getCreateTime1() {
		return createTime1;
	}
	public void setCreateTime1(String createTime1) {
		this.createTime1 = createTime1;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
