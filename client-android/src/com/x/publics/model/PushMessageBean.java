/**   
* @Title: PushMessageBean.java
* @Package com.mas.amineappstore.publics.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-8-4 下午1:49:00
* @version V1.0   
*/

package com.x.publics.model;

/**
* @ClassName: PushMessageBean
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-8-4 下午1:49:00
* 
*/

public class PushMessageBean {

	public int id;
	public int mode;//返回0是文字类型的推送，返回1是图片类型的推送
	public String picUrl;
	public String title;
	public String content;
	public int action;//0代表后续动作是打开应用，1为打开链接
	public String url;
	public String icon;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
