/**   
* @Title: AppGamesCategoryResponse.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-10-20 下午3:41:29
* @version V1.0   
*/


package com.x.publics.http.model;

import java.util.List;

import com.x.publics.model.CategoryBean;

/**
 * @ClassName: AppGamesCategoryResponse
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2015-10-20 下午3:41:29
 * 
 */

public class AppGamesCategoryResponse extends CommonResponse {
	public List<CategoryBean> categorylist;
	public int categoryNum;
	public List<CategoryBean> getCategorylist() {
		return categorylist;
	}
	public void setCategorylist(List<CategoryBean> categorylist) {
		this.categorylist = categorylist;
	}
	public int getCategoryNum() {
		return categoryNum;
	}
	public void setCategoryNum(int categoryNum) {
		this.categoryNum = categoryNum;
	}
	
}
