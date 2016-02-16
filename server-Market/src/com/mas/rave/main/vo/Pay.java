package com.mas.rave.main.vo;

import java.util.Date;

/**
 * indomog充值赠送a币
 * 
 * @author liwei.sz
 * 
 */
public class Pay {
	private int id;
	private int mogValue;// mogValue面额',
	private int channelId;// 渠道Id',
	private int cpId;// cpId',
	private int appId;// appId',
	private int aValuePresent;// indomog充值赠送a币',
	private String remark;// 备注',
	private boolean state;// '状态',
	private Date createTime;// 创建时间',
	private Date updateTime;// 更新时间',
	private String operator;// 操作人

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMogValue() {
		return mogValue;
	}

	public void setMogValue(int mogValue) {
		this.mogValue = mogValue;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getCpId() {
		return cpId;
	}

	public void setCpId(int cpId) {
		this.cpId = cpId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getaValuePresent() {
		return aValuePresent;
	}

	public void setaValuePresent(int aValuePresent) {
		this.aValuePresent = aValuePresent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
