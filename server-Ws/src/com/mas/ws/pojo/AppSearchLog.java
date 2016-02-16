package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class AppSearchLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer clientId;

    private Integer userId;

    private String userName;

    /**
     * 搜索内容
     */
    private String content;

    /**
     * 搜索结果数
     */
    private Integer searchNum;

    /**
     * 是否显示评论０不显示 1显示
     */
    private Boolean state;

    /**
     * 时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return 搜索内容
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content 
	 *            搜索内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return 搜索结果数
     */
    public Integer getSearchNum() {
        return searchNum;
    }

    /**
     * @param searchnum 
	 *            搜索结果数
     */
    public void setSearchNum(Integer searchNum) {
        this.searchNum = searchNum;
    }

    /**
     * @return 是否显示评论０不显示 1显示
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            是否显示评论０不显示 1显示
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
}