package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.publics.model.AppInfoSubBean;
import com.x.publics.model.PictureBean;

/**
 * @ClassName: DetailResponse
 * @Desciption: 应用详情响应数据
 
 * @Date: 2014-1-16 下午5:13:07
 */

public class DetailResponse extends CommonResponse {

	public int picNum; // 图片数量
	public AppInfoSubBean app;// 应用对象
	public ArrayList<PictureBean> piclist; // 应用截图集合

	public int getPicNum() {
		return picNum;
	}

	public void setPicNum(int picNum) {
		this.picNum = picNum;
	}

	public AppInfoSubBean getApp() {
		return app;
	}

	public void setApp(AppInfoSubBean app) {
		this.app = app;
	}


	public ArrayList<PictureBean> getPiclist() {
		return piclist;
	}

	public void setPiclist(ArrayList<PictureBean> piclist) {
		this.piclist = piclist;
	}


}