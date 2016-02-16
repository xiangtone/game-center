/**   
* @Title: CategoryResponse.java
* @Package com.x.http.model
* @Description: TODO 

* @date 2014-2-14 上午09:37:34
* @version V1.0   
*/

package com.x.publics.http.model;

import com.x.publics.model.CategoryInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: CategoryResponse
* @Description: TODO 

* @date 2014-2-14 上午09:37:34
* 
*/

public class CategoryResponse extends CommonResponse {

	public int categoryNum;
	public List<CategoryInfoBean> categorylist = new ArrayList<CategoryInfoBean>();

	public int getCategoryNum() {
		return categoryNum;
	}

	public void setCategoryNum(int categoryNum) {
		this.categoryNum = categoryNum;
	}

	public List<CategoryInfoBean> getCategorylist() {
		return categorylist;
	}

	public void setCategorylist(List<CategoryInfoBean> categorylist) {
		this.categorylist = categorylist;
	}

}
