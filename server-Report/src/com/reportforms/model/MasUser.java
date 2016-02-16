package com.reportforms.model;

import java.sql.Date;


public class MasUser extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8844434175850469066L;
	
	private Integer userId;
	
	private String userName;
	
	private String userPwd;
	
	private Integer aValue;
	
	private Integer aValuePresent;
	
	private String nickName;
	
	private Integer sex;
	
	private String sexString;//性别(1=男,0=女)
	
	private String age;
	
	private Integer userState;
	
	private String userStateString;
	
	private Integer categoryId;
	
	private String categoryIdString;
	
	private Integer rechargeNum;
	
	private String phone;
	
	private String email;
	
	private byte state;
	
	private String stateString;//在线状态
	
	private String address;
	
	private String realName;
	
	private Date createTime;
	
	private Integer aValueAll;
	
	private Integer aValuePresentAll;

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

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
		if(sex.intValue() == 1){
			this.sexString = "男";
		}else{
			this.sexString = "女";
		}
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
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

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
		if(state == 1){
			this.stateString = "在线";
		}else{
			this.stateString = "离线";
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getaValue() {
		return aValue;
	}

	public void setaValue(Integer aValue) {
		this.aValue = aValue;
	}

	public Integer getaValuePresent() {
		return aValuePresent;
	}

	public void setaValuePresent(Integer aValuePresent) {
		this.aValuePresent = aValuePresent;
	}

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
		//帐号状态(0=新用户未设置密码,1=老用户已设置)
		if(userState.intValue() == 0){
			this.userStateString = "未设置密码";
		}else{
			this.userStateString = "已设置密码";
		}
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
		//类别(0-普通用户,1-客服,2-虚拟用户)
		if(categoryId.intValue() == 0){
			this.categoryIdString = "普通用户";
		}else if(categoryId.intValue() == 1){
			this.categoryIdString = "客服";
		}else{
			this.categoryIdString = "虚拟用户";
		}
	}

	public Integer getRechargeNum() {
		return rechargeNum;
	}

	public void setRechargeNum(Integer rechargeNum) {
		this.rechargeNum = rechargeNum;
	}

	public Integer getaValueAll() {
		return aValueAll;
	}

	public void setaValueAll(Integer aValueAll) {
		this.aValueAll = aValueAll;
	}

	public Integer getaValuePresentAll() {
		return aValuePresentAll;
	}

	public void setaValuePresentAll(Integer aValuePresentAll) {
		this.aValuePresentAll = aValuePresentAll;
	}

	public String getSexString() {
		return sexString;
	}

	public String getUserStateString() {
		return userStateString;
	}

	public String getCategoryIdString() {
		return categoryIdString;
	}

	public String getStateString() {
		return stateString;
	}

}
