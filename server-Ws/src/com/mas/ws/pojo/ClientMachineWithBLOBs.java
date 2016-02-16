package com.mas.ws.pojo;

import java.io.Serializable;

public class ClientMachineWithBLOBs extends ClientMachine implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 每次激活时间
     */
    private String activeCreateTime;

    /**
     * 每次上报时间
     */
    private String activeUpdateTime;

    /**
     * @return 每次激活时间
     */
    public String getActiveCreateTime() {
        return activeCreateTime;
    }

    /**
     * @param activecreatetime 
	 *            每次激活时间
     */
    public void setActiveCreateTime(String activeCreateTime) {
        this.activeCreateTime = activeCreateTime;
    }

    /**
     * @return 每次上报时间
     */
    public String getActiveUpdateTime() {
        return activeUpdateTime;
    }

    /**
     * @param activeupdatetime 
	 *            每次上报时间
     */
    public void setActiveUpdateTime(String activeUpdateTime) {
        this.activeUpdateTime = activeUpdateTime;
    }
}