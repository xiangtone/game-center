/**   
* @Title: AppsUpgradeResponse.java
* @Package com.mas.amineappstore.http.model
* @Description: TODO 

* @date 2014-1-18 下午04:17:15
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.publics.model.InstallAppBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: AppsUpgradeResponse
* @Description: TODO 

* @date 2014-1-18 下午04:17:15
* 
*/

public class AppsUpgradeResponse extends CommonResponse implements Serializable {

	private static final long serialVersionUID = 6121778994562644192L;
	public int appNum;
	public List<InstallAppBean> applist = new ArrayList<InstallAppBean>();

	public int getAppNum() {
		return appNum;
	}

	public void setAppNum(int appNum) {
		this.appNum = appNum;
	}

	public List<InstallAppBean> getApplist() {
		return applist;
	}

	public void setApplist(List<InstallAppBean> applist) {
		this.applist = applist;
	}

}
