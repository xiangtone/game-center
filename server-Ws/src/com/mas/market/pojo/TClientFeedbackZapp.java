package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TClientFeedbackZapp implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 国家Id
     */
    private Integer raveId;

    /**
     * 提问
     */
    private String question;

    /**
     * 回答内容
     */
    private String replyContent;

    /**
     * 是否有效
     */
    private Boolean state;

    /**
     * 时间
     */
    private Date createTime;
    private String sendTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 国家Id
     */
    public Integer getRaveId() {
        return raveId;
    }

    /**
     * @param raveid 
	 *            国家Id
     */
    public void setRaveId(Integer raveId) {
        this.raveId = raveId;
    }

    /**
     * @return 提问
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question 
	 *            提问
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return 回答内容
     */
    public String getReplyContent() {
        return replyContent;
    }

    /**
     * @param replycontent 
	 *            回答内容
     */
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    /**
     * @return 是否有效
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            是否有效
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * @return 时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createtime 
	 *            时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
}