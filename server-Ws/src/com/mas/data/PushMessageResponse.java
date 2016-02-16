package com.mas.data;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PushMessageResponse extends BaseResponse {

	private List<PushMessageData> messageList;
	private Integer listSize;

	public Integer getListSize() {
		return listSize;
	}

	public void setListSize(Integer listSize) {
		this.listSize = listSize;
	}

	public List<PushMessageData> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<PushMessageData> messageList) {
		this.messageList = messageList;
	}

}
