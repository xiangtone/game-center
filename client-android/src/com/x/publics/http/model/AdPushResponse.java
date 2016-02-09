/**   
* @Title: AdPushResponse.java
* @Package com.mas.amineappstore.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-8-4 下午1:50:40
* @version V1.0   
*/

package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.publics.model.PushMessageBean;

/**
* @ClassName: AdPushResponse
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-8-4 下午1:50:40
* 
*/

public class AdPushResponse extends CommentResponse {

	public ArrayList<PushMessageBean> messageList = new ArrayList<PushMessageBean>();
	public int listSize;

	public ArrayList<PushMessageBean> getMessageList() {
		return messageList;
	}

	public void setMessageList(ArrayList<PushMessageBean> messageList) {
		this.messageList = messageList;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}
}
