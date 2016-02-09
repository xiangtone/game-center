/**   
* @Title: BannerResponse.java
* @Package com.mas.amineappstore.http.model
* @Description: TODO 

* @date 2014-2-13 下午03:01:57
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.publics.model.BannerInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: BannerResponse
* @Description: TODO 

* @date 2014-2-13 下午03:01:57
* 
*/

public class BannerResponse extends CommonResponse {
	public List<BannerInfoBean> themelist = new ArrayList<BannerInfoBean>();

	public List<BannerInfoBean> getThemelist() {
		return themelist;
	}

	public void setThemelist(List<BannerInfoBean> themelist) {
		this.themelist = themelist;
	}
}
