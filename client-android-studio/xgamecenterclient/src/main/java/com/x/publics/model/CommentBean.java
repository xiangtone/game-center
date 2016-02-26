package com.x.publics.model;

import java.io.Serializable;

/**
 * @ClassName: CommentBean
 * @Desciption: 评论实体类
 
 * @Date: 2014-1-15 下午3:32:51
 */

public class CommentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private long stars;             // 评论星级
	private String content;         // 评论内容
	private String userName;        // 评论人名称
	private String publishTime;     // 评论时间

	private int deviceType;         // 设备类型 1=手机 2=平板
	private String osVersion;       // 系统版本号
	private String deviceModel;     // 设备机型
	private String deviceVendor;    // 设备厂商
	private String osVersionName;   // 系统版本名称
	
	
	public long getStars() {
		return stars;
	}
	public void setStars(long stars) {
		this.stars = stars;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getDeviceVendor() {
		return deviceVendor;
	}
	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}
	public String getOsVersionName() {
		return osVersionName;
	}
	public void setOsVersionName(String osVersionName) {
		this.osVersionName = osVersionName;
	}
}
