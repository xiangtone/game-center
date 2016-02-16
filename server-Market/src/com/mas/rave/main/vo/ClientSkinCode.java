package com.mas.rave.main.vo;

import java.util.Date;

/**
 * skin版本信息
 * 
 * @author liwei.sz
 * 
 */
public class ClientSkinCode {
	private int id;
	private int skincode;
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSkincode() {
		return skincode;
	}

	public void setSkincode(int skincode) {
		this.skincode = skincode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
