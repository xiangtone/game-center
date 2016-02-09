package com.x.publics.http.model;

import com.x.publics.utils.Constan;

/**
 * @ClassName: ThemeRequest
 * @Desciption: 图片专辑，数据请求
 
 * @Date: 2014-3-25 上午10:11:05
 */

public class ThemeRequest extends CommonRequest {

	public Pager pager;

	public ThemeRequest() {
		super(Constan.Rc.GET_IMAGE_THEME, Constan.SIGN);
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

}
