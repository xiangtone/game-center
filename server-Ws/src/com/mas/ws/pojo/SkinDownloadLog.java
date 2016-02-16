package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class SkinDownloadLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * clinetId
     */
    private Integer clinetId;

    /**
     * imei
     */
    private String imei;

    /**
     * skinId
     */
    private Integer skinId;

    /**
     * skin应用名称
     */
    private String skinName;

    /**
     * 应用包名
     */
    private String packageName;

    private String versionName;

    private Integer versionCode;

    /**
     * IP
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
     * 创建时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return clinetId
     */
    public Integer getClinetId() {
        return clinetId;
    }

    /**
     * @param clinetid 
	 *            clinetId
     */
    public void setClinetId(Integer clinetId) {
        this.clinetId = clinetId;
    }

    /**
     * @return imei
     */
    public String getImei() {
        return imei;
    }

    /**
     * @param imei 
	 *            imei
     */
    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * @return skinId
     */
    public Integer getSkinId() {
        return skinId;
    }

    /**
     * @param skinid 
	 *            skinId
     */
    public void setSkinId(Integer skinId) {
        this.skinId = skinId;
    }

    /**
     * @return skin应用名称
     */
    public String getSkinName() {
        return skinName;
    }

    /**
     * @param skinname 
	 *            skin应用名称
     */
    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    /**
     * @return 应用包名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packagename 
	 *            应用包名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return IP
     */
    public String getIP() {
        return IP;
    }

    /**
     * @param ip 
	 *            IP
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
}