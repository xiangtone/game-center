/**   
* @Title: CategoryDetailRequest.java
* @Package com.x.http.model
* @Description: TODO 

* @date 2014-2-14 上午10:46:55
* @version V1.0   
*/

package com.x.publics.http.model;

/**
* @ClassName: CategoryDetailRequest
* @Description: TODO 

* @date 2014-2-14 上午10:46:55
* 
*/

public class CategoryDetailRequest extends CategoryRequest {
	public int categoryId;
	public String type;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
