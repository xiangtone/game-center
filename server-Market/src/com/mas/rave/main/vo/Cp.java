package com.mas.rave.main.vo;

import java.util.Date;

/**
 * cp信息
 * 
 * @author liwei.sz
 * 
 */
public class Cp {
	private int id;
	private String name;// 游戏厂商名称',
	private String password;// 密码(６位加密数)',
	private String pwd;// 密码(明文
	private String description;// 描述',
	private String backendAccount;// 后台帐号',
	private String backendUrl;// cp后台地址',
	private String backendPassword;// cp后台密码',
	private String address;// 地址',
	private String phoneNum;// 联系电话',
	private String qq;
	private String email;// 邮箱地址',
	private String contact;// 联系人',
	private String remark;// 备注',
	private boolean state;// 是否有效
	private String payWay;// list对象，返回支付方式列表',
	private Date createTime;// 创建时间
	private Date updateTime;// 更新时间
	private String priKey;// 私钥',
	private String n;// '随机数',
	private String callbackUrl;// 回调地址
	private String pkey;// 公钥',
	private int userType;// 用户类型，0=个人，1=企业，2=其它
	private int cpState;// 0未提交，1审核中，2未通过审核 3通过审核
	private String countryName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBackendAccount() {
		return backendAccount;
	}

	public void setBackendAccount(String backendAccount) {
		this.backendAccount = backendAccount;
	}

	public String getBackendUrl() {
		return backendUrl;
	}

	public void setBackendUrl(String backendUrl) {
		this.backendUrl = backendUrl;
	}

	public String getBackendPassword() {
		return backendPassword;
	}

	public void setBackendPassword(String backendPassword) {
		this.backendPassword = backendPassword;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
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

	public String getPriKey() {
		return priKey;
	}

	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}

	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getCpState() {
		return cpState;
	}

	public void setCpState(int cpState) {
		this.cpState = cpState;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

}
