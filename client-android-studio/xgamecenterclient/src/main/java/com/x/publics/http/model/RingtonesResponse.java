package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.publics.model.CategoryInfoBean;
import com.x.publics.model.RingtonesBean;

/**
 * @ClassName: RingtonesResponse
 * @Desciption: 音乐栏目，数据响应
 
 */

public class RingtonesResponse extends CommonResponse {

	public int musicNum;
	public boolean isLast;
	public ArrayList<RingtonesBean> musiclist = new ArrayList<RingtonesBean>();
	public ArrayList<CategoryInfoBean> categorylist = new ArrayList<CategoryInfoBean>();
	
	public int getImageNum() {
		return musicNum;
	}

	public void setImageNum(int musicNum) {
		this.musicNum = musicNum;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public ArrayList<RingtonesBean> getImagelist() {
		return musiclist;
	}

	public void setImagelist(ArrayList<RingtonesBean> imagelist) {
		this.musiclist = imagelist;
	}

	public ArrayList<CategoryInfoBean> getCategorylist() {
		return categorylist;
	}

	public void setCategorylist(ArrayList<CategoryInfoBean> categorylist) {
		this.categorylist = categorylist;
	}

}
