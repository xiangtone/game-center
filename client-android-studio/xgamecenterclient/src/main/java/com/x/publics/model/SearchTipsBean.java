/**   
 * @Title: SearchTipsBean.java
 * @Package com.x.publics.model
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-7-30 下午2:49:54
 * @version V1.0   
 */

package com.x.publics.model;

import java.io.Serializable;

/**
 * @ClassName: SearchTipsBean
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-30 下午2:49:54
 * 
 */

public class SearchTipsBean implements Serializable {

	private static final long serialVersionUID = 4080012924626494398L;
	private String appName;
	private String musicName;
	private String imageName;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

}
