package com.mas.rave.main.vo;

import java.util.Date;

/**
 * 渠道信息
 * 
 * @author liwei.sz
 * 
 */
public class Channel {
	private int id;// 渠道管理公司ID
	private String password; // 密码(６位加密数)
	private String pwd;// 密码(明文
	private int fatherId;// 父渠道管理公司ID
	private String fatherName;// 父渠道管理公司名
	private String name;// 渠道管理公司
	private int type;// 渠道类型（1：运营 2：市场）
	private String contacter;// 联系人
	private String phone;// 联系电话
	private String email;// 邮箱地址
	private String address;// 联系地址
	private String description;// 简介说明
	private String remark;// 备注
	private boolean state;// 是否有效
	private int sort;// 排序
	private Date createTime;// 创建时间
	private String operator;// 操作人
	private Province province; // 对应省份

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFatherId() {
		return fatherId;
	}

	public void setFatherId(int fatherId) {
		this.fatherId = fatherId;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContacter() {
		return contacter;
	}

	public void setContacter(String contacter) {
		this.contacter = contacter;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
