package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TCp implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 游戏厂商名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phoneNum;

    private String qq;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 备注
     */
    private String remark;

    private Boolean state;

    private Date createTime;

    private Date updateTime;

    private String callbackUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 游戏厂商名称
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 
	 *            游戏厂商名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description 
	 *            描述
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return 联系电话
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * @param phonenum 
	 *            联系电话
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * @return 邮箱地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email 
	 *            邮箱地址
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return 联系人
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact 
	 *            联系人
     */
    public void setContact(String contact) {
        this.contact = contact;
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

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
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

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}