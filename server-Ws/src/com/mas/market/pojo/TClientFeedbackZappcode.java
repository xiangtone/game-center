package com.mas.market.pojo;

import java.io.Serializable;
import java.util.Date;

public class TClientFeedbackZappcode implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 常见问题版本号
     */
    private Integer zappcode;

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
     * @return 常见问题版本号
     */
    public Integer getZappcode() {
        return zappcode;
    }

    /**
     * @param zappcode 
	 *            常见问题版本号
     */
    public void setZappcode(Integer zappcode) {
        this.zappcode = zappcode;
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