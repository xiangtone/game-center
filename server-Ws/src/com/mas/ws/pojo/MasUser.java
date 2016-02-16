package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class MasUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer userId;
    private Integer loginNum;
    private Integer findPwdNum;
    /**
     * 账号
     */
    private String userName;
    private String recommendName;
    /**
     * 登录密码
     */
    private String userPwdNew;
    private String userPwd;

    /**
     * a币 虚拟货币（monetary）
     */
    private Integer aValue;

    /**
     * a币 帐号全局赠送
     */
    private Integer aValuePresent;

    /**
     * 2=新用户,1=老用户
     */
    private Integer typeId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别(1=男,0=女)
     */
    private Integer sex;

    /**
     * 年龄
     */
    private String age;

    /**
     * 最后一次登陆ip
     */
    private String IP;

    /**
     * 最后一次登陆所在国家
     */
    private String country;

    /**
     * 最后一次登陆所在省份
     */
    private String province;

    /**
     * 最后一次登陆所在城市
     */
    private String city;


    /**
     * 头像
     */
    private String photo;

    /**
     * 帐号状态(0=新用户未设置密码,1=老用户已设置)
     */
    private Integer userState;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 类别(0-普通用户,1-客服,2-虚拟用户)
     */
    private Integer categoryId;

    /**
     * 在线状态
     */
    private Boolean state;

    /**
     * 地址
     */
    private String address;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后登陆时间
     */
    private Date updateTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 充值次数
     */
    private Integer rechargeNum;

    /**
     * 充值时间记录
     */
    private String rechargeTimes;

    /**
     * 一共充值A币
     */
    private Integer aValueAll;

    /**
     * 一共赠送的A币
     */
    private Integer aValuePresentAll;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return 账号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param username 
	 *            账号
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return 登录密码
     */
    public String getUserPwd() {
        return userPwd;
    }

    /**
     * @param userpwd 
	 *            登录密码
     */
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    /**
     * @return a币 虚拟货币（monetary）
     */
    public Integer getaValue() {
        return aValue;
    }

    /**
     * @param avalue 
	 *            a币 虚拟货币（monetary）
     */
    public void setaValue(Integer aValue) {
        this.aValue = aValue;
    }

    /**
     * @return a币 帐号全局赠送
     */
    public Integer getaValuePresent() {
        return aValuePresent;
    }

    /**
     * @param avaluepresent 
	 *            a币 帐号全局赠送
     */
    public void setaValuePresent(Integer aValuePresent) {
        this.aValuePresent = aValuePresent;
    }

    /**
     * @return 2=新用户,1=老用户
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeid 
	 *            2=新用户,1=老用户
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * @return 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * @param nickname 
	 *            昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * @return 性别(1=男,0=女)
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * @param sex 
	 *            性别(1=男,0=女)
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * @return 年龄
     */
    public String getAge() {
        return age;
    }

    /**
     * @param age 
	 *            年龄
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * @return 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province 
	 *            省份
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return 所在地名(市)
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city 
	 *            所在地名(市)
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return 头像
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo 
	 *            头像
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return 帐号状态(0=新用户未设置密码,1=老用户已设置)
     */
    public Integer getUserState() {
        return userState;
    }

    /**
     * @param userstate 
	 *            帐号状态(0=新用户未设置密码,1=老用户已设置)
     */
    public void setUserState(Integer userState) {
        this.userState = userState;
    }

    /**
     * @return 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone 
	 *            手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email 
	 *            邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return 类别(0-普通用户,1-客服,2-虚拟用户)
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryid 
	 *            类别(0-普通用户,1-客服,2-虚拟用户)
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return 在线状态
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            在线状态
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * @return 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address 
	 *            地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realname 
	 *            真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
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
     * @return 最后登陆时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updatetime 
	 *            最后登陆时间
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

    /**
     * @return 充值次数
     */
    public Integer getRechargeNum() {
        return rechargeNum;
    }

    /**
     * @param rechargenum 
	 *            充值次数
     */
    public void setRechargeNum(Integer rechargeNum) {
        this.rechargeNum = rechargeNum;
    }

    /**
     * @return 充值时间记录
     */
    public String getRechargeTimes() {
        return rechargeTimes;
    }

    /**
     * @param rechargetimes 
	 *            充值时间记录
     */
    public void setRechargeTimes(String rechargeTimes) {
        this.rechargeTimes = rechargeTimes;
    }

    /**
     * @return 一共充值A币
     */
    public Integer getaValueAll() {
        return aValueAll;
    }

    /**
     * @param avalueall 
	 *            一共充值A币
     */
    public void setaValueAll(Integer aValueAll) {
        this.aValueAll = aValueAll;
    }

    /**
     * @return 一共赠送的A币
     */
    public Integer getaValuePresentAll() {
        return aValuePresentAll;
    }

    /**
     * @param avaluepresentall 
	 *            一共赠送的A币
     */
    public void setaValuePresentAll(Integer aValuePresentAll) {
        this.aValuePresentAll = aValuePresentAll;
    }

	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	public Integer getFindPwdNum() {
		return findPwdNum;
	}

	public void setFindPwdNum(Integer findPwdNum) {
		this.findPwdNum = findPwdNum;
	}

	public String getUserPwdNew() {
		return userPwdNew;
	}

	public void setUserPwdNew(String userPwdNew) {
		this.userPwdNew = userPwdNew;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(Integer loginNum) {
		this.loginNum = loginNum;
	}
}