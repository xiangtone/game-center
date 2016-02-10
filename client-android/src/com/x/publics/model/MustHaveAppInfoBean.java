/**   
* @Title: MustHaveAppInfoBean.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-9-4 下午2:52:06
* @version V1.0   
*/


package com.x.publics.model;


/**
* @ClassName: MustHaveAppInfoBean
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-9-4 下午2:52:06
* 
*/

public class MustHaveAppInfoBean extends AppInfoBean{

	private String catalogName;
	private int catalogId;

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public int getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}
	
}
