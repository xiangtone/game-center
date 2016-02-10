/**   
* @Title: SkinListResponse.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-12-3 下午2:06:05
* @version V1.0   
*/

package com.x.publics.http.model;

import java.util.ArrayList;
import java.util.List;

import com.x.publics.model.SkinInfoBean;

/**
* @ClassName: SkinListResponse
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-12-3 下午2:06:05
* 
*/

public class SkinListResponse extends CommonResponse {

	public int skinNum;
	public List<SkinInfoBean> skinlist = new ArrayList<SkinInfoBean>();

	public List<SkinInfoBean> getSkinlist() {
		return skinlist;
	}

	public void setSkinlist(List<SkinInfoBean> skinlist) {
		this.skinlist = skinlist;
	}

}
