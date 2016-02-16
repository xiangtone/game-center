package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.publics.model.ThemeBean;

/**
 * @ClassName: ThemeResponse
 * @Desciption: 图片专辑，数据响应
 
 * @Date: 2014-3-25 上午10:15:08
 */

public class ThemeResponse extends CommonResponse {

	public int themeNum;
	public boolean isLast;
	public ArrayList<ThemeBean> themelist = new ArrayList<ThemeBean>();

	public int getThemeNum() {
		return themeNum;
	}

	public void setThemeNum(int themeNum) {
		this.themeNum = themeNum;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public ArrayList<ThemeBean> getThemelist() {
		return themelist;
	}

	public void setThemelist(ArrayList<ThemeBean> themelist) {
		this.themelist = themelist;
	}

}
