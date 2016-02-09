package com.x.publics.model;

/**
 * @ClassName: AppInfoSubBean
 * @Desciption: appInfoBean 子类
 
 * @Date: 2014-1-24 上午8:42:40
 */
public class AppInfoSubBean extends AppInfoBean {

	private String description; // 应用描述
	private int realDowdload; // 下载数量
	private int picNum; // 应用图片数量
	private float starsReal; // 星级平均分


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRealDowdload() {
		return realDowdload;
	}

	public void setRealDowdload(int realDowdload) {
		this.realDowdload = realDowdload;
	}

	public int getPicNum() {
		return picNum;
	}

	public void setPicNum(int picNum) {
		this.picNum = picNum;
	}

	public float getStarsReal() {
		return starsReal;
	}

	public void setStarsReal(float starsReal) {
		this.starsReal = starsReal;
	}

}
