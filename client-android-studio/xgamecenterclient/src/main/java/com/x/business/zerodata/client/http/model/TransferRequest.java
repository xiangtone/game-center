/**   
* @Title: TransferRequest.java
* @Package com.x.business.zerodata.client.http.model
* @Description: TODO 

* @date 2014-3-20 上午11:14:34
* @version V1.0   
*/

package com.x.business.zerodata.client.http.model;

/**
* @ClassName: TransferRequest
* @Description: TODO 

* @date 2014-3-20 上午11:14:34
* 
*/

public class TransferRequest {

	public String nickName; //昵称
	public String mac;
	public String imei;
	public String imsi;
	public String deviceModel;//手机型号
	public int osVersion; //系统版本号
	public String osVersionName; //系统版本名字
	public String rawClientGetPramas ;//原始参数集 
	public int currentProgress ;//当前进度
	public int connectType ;
	public int headPortrait ;
	
	public int getConnectType() {
		return connectType;
	}
	public void setConnectType(int connectType) {
		this.connectType = connectType;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public int getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(int osVersion) {
		this.osVersion = osVersion;
	}
	public String getOsVersionName() {
		return osVersionName;
	}
	public void setOsVersionName(String osVersionName) {
		this.osVersionName = osVersionName;
	}
	public String getRawClientGetPramas() {
		return rawClientGetPramas;
	}
	public void setRawClientGetPramas(String rawClientGetPramas) {
		this.rawClientGetPramas = rawClientGetPramas;
	}
	public int getCurrentProgress() {
		return currentProgress;
	}
	public void setCurrentProgress(int currentProgress) {
		this.currentProgress = currentProgress;
	}
	public int getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(int headPortrait) {
		this.headPortrait = headPortrait;
	}
	
	
	
}
