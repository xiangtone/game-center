package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TClientSkinCode implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * skin版本号
     */
    private Integer skincode;

    /**
     * 时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return skin版本号
     */
    public Integer getSkincode() {
        return skincode;
    }

    /**
     * @param skincode 
	 *            skin版本号
     */
    public void setSkincode(Integer skincode) {
        this.skincode = skincode;
    }

    /**
     * @return 时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createtime 
	 *            时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}