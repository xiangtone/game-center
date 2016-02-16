package com.mas.ws.pojo;

import java.io.Serializable;
import java.util.Date;

public class MusicDownloadLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 客户端编号
     */
    private Integer clientId;

    /**
     * 客户端国家ID
     */
    private Integer raveId;

    /**
     * ct= 栏目ID（同时约定  当应用是搜索进来的 ct=100、Homes广告位ct=101、Apps广告位ct=102、Games广告位ct=103、推荐ct=104、下载管理105、更新管理106、收藏107)。
     */
    private Integer columnId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 应用包名
     */
    private String masPackageName;

    private String masVersionName;

    private Integer masVersionCode;

    /**
     * musicId
     */
    private Integer musicId;

    /**
     * music名称
     */
    private String musicName;

    /**
     * 创建时间
     */
    private Date createTime;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 客户端编号
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientid 
	 *            客户端编号
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return 客户端国家ID
     */
    public Integer getRaveId() {
        return raveId;
    }

    /**
     * @param raveid 
	 *            客户端国家ID
     */
    public void setRaveId(Integer raveId) {
        this.raveId = raveId;
    }

    /**
     * @return ct= 栏目ID（同时约定  当应用是搜索进来的 ct=100、Homes广告位ct=101、Apps广告位ct=102、Games广告位ct=103、推荐ct=104、下载管理105、更新管理106、收藏107)。
     */
    public Integer getColumnId() {
        return columnId;
    }

    /**
     * @param columnid 
	 *            ct= 栏目ID（同时约定  当应用是搜索进来的 ct=100、Homes广告位ct=101、Apps广告位ct=102、Games广告位ct=103、推荐ct=104、下载管理105、更新管理106、收藏107)。
     */
    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    /**
     * @return 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userid 
	 *            用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param username 
	 *            用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return 应用包名
     */
    public String getMasPackageName() {
        return masPackageName;
    }

    /**
     * @param maspackagename 
	 *            应用包名
     */
    public void setMasPackageName(String masPackageName) {
        this.masPackageName = masPackageName;
    }

    public String getMasVersionName() {
        return masVersionName;
    }

    public void setMasVersionName(String masVersionName) {
        this.masVersionName = masVersionName;
    }

    public Integer getMasVersionCode() {
        return masVersionCode;
    }

    public void setMasVersionCode(Integer masVersionCode) {
        this.masVersionCode = masVersionCode;
    }

    /**
     * @return musicId
     */
    public Integer getMusicId() {
        return musicId;
    }

    /**
     * @param musicid 
	 *            musicId
     */
    public void setMusicId(Integer musicId) {
        this.musicId = musicId;
    }

    /**
     * @return music名称
     */
    public String getMusicName() {
        return musicName;
    }

    /**
     * @param musicname 
	 *            music名称
     */
    public void setMusicName(String musicName) {
        this.musicName = musicName;
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
}