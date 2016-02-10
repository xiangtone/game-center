/**   
* @Title: AppDownloadLogResponse.java
* @Package com.x.publics.http.model
* @Description: TODO(用一句话描述该文件做什么)

* @date 2015-3-20 上午10:11:13
* @version V1.0   
*/

package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.ui.view.dynamic.DynamicListViewItem;

/**
* @ClassName: AppDownloadLogResponse
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2015-3-20 上午10:11:13
* 
*/

public class AppDownloadLogResponse extends CommonResponse {

	public int listSize;
	public ArrayList<DynamicListViewItem> dataList;

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public ArrayList<DynamicListViewItem> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<DynamicListViewItem> dataList) {
		this.dataList = dataList;
	}

}
