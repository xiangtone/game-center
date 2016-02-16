package com.mas.data;

import java.util.List;

import com.mas.market.pojo.TAppAlbumRes;

public class MustHaveData {
	
	private List<TAppAlbumRes> applist;
	
	private Integer appNum;
	
	private String name;
	private Integer id;

	public List<TAppAlbumRes> getApplist() {
		return applist;
	}

	public void setApplist(List<TAppAlbumRes> applist) {
		this.applist = applist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAppNum() {
		return appNum;
	}

	public void setAppNum(Integer appNum) {
		this.appNum = appNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
