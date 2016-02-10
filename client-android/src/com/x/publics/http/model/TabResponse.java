/**   
 * @Title: TabResponse.java
 * @Package com.x.http.model
 * @Description: TODO 
 
 * @date 2014-1-15 上午10:16:53
 * @version V1.0   
 */

package com.x.publics.http.model;

import com.x.publics.model.AppInfoBean;
import com.x.publics.model.RingtonesBean;
import com.x.publics.model.WallpaperBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TabResponse
 * @Description: TODO
 
 * @date 2014-1-15 上午10:16:53
 * 
 */

public class TabResponse extends CommonResponse {

	public int appNum;
	public int imageNum;
	public int musicNum;
	public boolean isLast;
	public ArrayList<AppInfoBean> applist = new ArrayList<AppInfoBean>();
	public ArrayList<WallpaperBean> imagelist = new ArrayList<WallpaperBean>();
	public ArrayList<RingtonesBean> musiclist = new ArrayList<RingtonesBean>();

	public int getAppNum() {
		return appNum;
	}

	public void setAppNum(int appNum) {
		this.appNum = appNum;
	}

	public int getImageNum() {
		return imageNum;
	}

	public void setImageNum(int imageNum) {
		this.imageNum = imageNum;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public List<AppInfoBean> getApplist() {
		return applist;
	}

	public void setApplist(ArrayList<AppInfoBean> applist) {
		this.applist = applist;
	}

	public ArrayList<WallpaperBean> getImagelist() {
		return imagelist;
	}

	public void setImagelist(ArrayList<WallpaperBean> imagelist) {
		this.imagelist = imagelist;
	}

	public int getMusicNum() {
		return musicNum;
	}

	public void setMusicNum(int musicNum) {
		this.musicNum = musicNum;
	}

	public ArrayList<RingtonesBean> getMusiclist() {
		return musiclist;
	}

	public void setMusiclist(ArrayList<RingtonesBean> musiclist) {
		this.musiclist = musiclist;
	}

}
