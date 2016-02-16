package com.mas.rave.main.vo;

import java.util.Date;

/**
 * 常用回馈 zapp版本
 * @author jieding
 *
 */
public class ClientFeedbackZappCode {

	private int id;
	private Integer zappcode; //常见问题版本号
	private Date createTime;  //时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getZappcode() {
		return zappcode;
	}
	public void setZappcode(Integer zappcode) {
		this.zappcode = zappcode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
