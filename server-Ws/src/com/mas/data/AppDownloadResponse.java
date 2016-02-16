package com.mas.data;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AppDownloadResponse extends BaseResponse {

	private List<AppDownloadData> dataList;
	private Integer listSize;

	public Integer getListSize() {
		return listSize;
	}

	public void setListSize(Integer listSize) {
		this.listSize = listSize;
	}

	public List<AppDownloadData> getDataList() {
		return dataList;
	}

	public void setDataList(List<AppDownloadData> dataList) {
		this.dataList = dataList;
	}
	
	
}
