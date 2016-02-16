package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TRaveCountry implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 国家名
     */
    private String name;

    /**
     * 国家名中文
     */
    private String nameCn;

    /**
     * 图标路径
     */
    private String url;

    /**
     * 客户端是否显示此国家列表
     */
    private Boolean state;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 国家名
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 
	 *            国家名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 国家名中文
     */
    public String getNameCn() {
        return nameCn;
    }

    /**
     * @param namecn 
	 *            国家名中文
     */
    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    /**
     * @return 图标路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url 
	 *            图标路径
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return 客户端是否显示此国家列表
     */
    public Boolean getState() {
        return state;
    }

    /**
     * @param state 
	 *            客户端是否显示此国家列表
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}