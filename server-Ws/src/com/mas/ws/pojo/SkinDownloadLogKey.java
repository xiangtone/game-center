package com.mas.ws.pojo;

import java.io.Serializable;

public class SkinDownloadLogKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer versionCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }
}