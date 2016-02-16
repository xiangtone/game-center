package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TPayIndomog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * mogValue面额
     */
    private Integer mogValue;

    /**
     * 渠道Id
     */
    private Integer channelId;

    /**
     * cpId
     */
    private Integer cpId;

    /**
     * appId
     */
    private Integer appId;

    /**
     * indomog充值赠送a币
     */
    private Integer aValuePresent;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Boolean state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 操作人
     */
    private String operator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return mogValue面额
     */
    public Integer getMogValue() {
        return mogValue;
    }

    /**
     * @param mogvalue 
	 *            mogValue面额
     */
    public void setMogValue(Integer mogValue) {
        this.mogValue = mogValue;
    }

    /**
     * @return 渠道Id
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelid 
	 *            渠道Id
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return cpId
     */
    public Integer getCpId() {
        return cpId;
    }

    /**
     * @param cpid 
	 *            cpId
     */
    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    /**
     * @return appId
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * @param appid 
	 *            appId
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * @return indomog充值赠送a币
     */
    public Integer getaValuePresent() {
        return aValuePresent;
    }

    /**
     * @param avaluepresent 
	 *            indomog充值赠送a币
     */
    public void setaValuePresent(Integer aValuePresent) {
        this.aValuePresent = aValuePresent;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark 
	 *            备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return 状态
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            状态
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * @return 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createtime 
	 *            创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updatetime 
	 *            更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return 操作人
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator 
	 *            操作人
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }
}