package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.publics.model.CategoryInfoBean;
import com.x.publics.model.WallpaperBean;

/**
 * @ClassName: WallpaperResponse
 * @Desciption: 壁纸栏目，数据响应
 
 * @Date: 2014-3-25 下午2:06:03
 */

public class WallpaperResponse extends CommonResponse {

	public int imageNum;
	public boolean isLast;
	public ArrayList<WallpaperBean> imagelist = new ArrayList<WallpaperBean>();
	public ArrayList<CategoryInfoBean> categorylist = new ArrayList<CategoryInfoBean>();
	
	public int getImageNum() {
		return imageNum;
	}

	public void setImageNum(int imageNum) {
		this.imageNum = imageNum;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public ArrayList<WallpaperBean> getImagelist() {
		return imagelist;
	}

	public void setImagelist(ArrayList<WallpaperBean> imagelist) {
		this.imagelist = imagelist;
	}

	public ArrayList<CategoryInfoBean> getCategorylist() {
		return categorylist;
	}

	public void setCategorylist(ArrayList<CategoryInfoBean> categorylist) {
		this.categorylist = categorylist;
	}

}
