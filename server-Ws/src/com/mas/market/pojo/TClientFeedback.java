package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TClientFeedback implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 机器序列号
     */
    private String imei;

    private Integer clientId;

    private Integer userId;

    private String userName;
    private String nickName;


	private String email;

    /**
     * 内容
     */
    private String content;

    /**
     * 手机机型
     */
    private String deviceModel;

    /**
     * 手机厂商
     */
    private String deviceVendor;

    /**
     * 系统版本号
     */
    private String osVersion;

    /**
     * 系统版本名字
     */
    private String osVersionName;

    /**
     * 1手机，2平板
     */
    private Integer deviceType;

    /**
     * 是否显示０不显示 1显示
     */
    private Boolean state;

    /**
     * zapp包名
     */
    private String masPackageName;

    /**
     * 版本名
     */
    private String masVersionName;

    /**
     * 版本号
     */
    private Integer masVersionCode;

    /**
     * 回馈时间
     */
    private Date createTime;
    private Date sendTime;

    /**
     * 1用户回馈、2运营回答
     */
    private Integer feedbackType;

    /**
     * ip地址
     */
    private String IP;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 0用户还没看回复。1已看
     */
    private Boolean lookOver;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 机器序列号
     */
    public String getImei() {
        return imei;
    }

    /**
     * @param imei 
	 *            机器序列号
     */
    public void setImei(String imei) {
        this.imei = imei;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content 
	 *            内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return 手机机型
     */
    public String getDeviceModel() {
        return deviceModel;
    }

    /**
     * @param devicemodel 
	 *            手机机型
     */
    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    /**
     * @return 手机厂商
     */
    public String getDeviceVendor() {
        return deviceVendor;
    }

    /**
     * @param devicevendor 
	 *            手机厂商
     */
    public void setDeviceVendor(String deviceVendor) {
        this.deviceVendor = deviceVendor;
    }

    /**
     * @return 系统版本号
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * @param osversion 
	 *            系统版本号
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * @return 系统版本名字
     */
    public String getOsVersionName() {
        return osVersionName;
    }

    /**
     * @param osversionname 
	 *            系统版本名字
     */
    public void setOsVersionName(String osVersionName) {
        this.osVersionName = osVersionName;
    }

    /**
     * @return 1手机，2平板
     */
    public Integer getDeviceType() {
        return deviceType;
    }

    /**
     * @param devicetype 
	 *            1手机，2平板
     */
    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return 是否显示０不显示 1显示
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            是否显示０不显示 1显示
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * @return zapp包名
     */
    public String getMasPackageName() {
        return masPackageName;
    }

    /**
     * @param maspackagename 
	 *            zapp包名
     */
    public void setMasPackageName(String masPackageName) {
        this.masPackageName = masPackageName;
    }

    /**
     * @return 版本名
     */
    public String getMasVersionName() {
        return masVersionName;
    }

    /**
     * @param masversionname 
	 *            版本名
     */
    public void setMasVersionName(String masVersionName) {
        this.masVersionName = masVersionName;
    }

    /**
     * @return 版本号
     */
    public Integer getMasVersionCode() {
        return masVersionCode;
    }

    /**
     * @param masversioncode 
	 *            版本号
     */
    public void setMasVersionCode(Integer masVersionCode) {
        this.masVersionCode = masVersionCode;
    }

    /**
     * @return 回馈时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createtime 
	 *            回馈时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 1用户回馈、2运营回答
     */
    public Integer getFeedbackType() {
        return feedbackType;
    }

    /**
     * @param feedbacktype 
	 *            1用户回馈、2运营回答
     */
    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    /**
     * @return ip地址
     */
    public String getIP() {
        return IP;
    }

    /**
     * @param ip 
	 *            ip地址
     */
    public void setIP(String IP) {
        this.IP = IP;
    }

    /**
     * @return 国家
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country 
	 *            国家
     */
    public void setCountry(String country) {
        this.country = country;
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
     * @return 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city 
	 *            城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return 0用户还没看回复。1已看
     */
    public Boolean getLookOver() {
        return lookOver;
    }

    /**
     * @param lookover 
	 *            0用户还没看回复。1已看
     */
    public void setLookOver(Boolean lookOver) {
        this.lookOver = lookOver;
    }

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}