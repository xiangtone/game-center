package com.mas.rave.vo;

import java.util.ArrayList;
import java.util.List;

import com.mas.rave.main.vo.AppAlbumRes;

public class AppAlbumResVO extends AppAlbumRes {
	private boolean displayable;

	private List<AppAlbumRes> appAlbumRes = new ArrayList<AppAlbumRes>();

	public List<AppAlbumRes> getAppAlbumRes() {
		return appAlbumRes;
	}

	public void setAppAlbumRes(List<AppAlbumRes> appAlbumRes) {
		this.appAlbumRes = appAlbumRes;
	}

	public boolean isDisplayable() {
		return displayable;
	}

	public void setDisplayable(boolean displayable) {
		this.displayable = displayable;
	}

}
