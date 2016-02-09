/**   
 * @Title: SearchResponse.java
 * @Package com.mas.amineappstore.http.model
 * @Description: TODO 
 
 * @date 2014-1-20 上午11:45:51
 * @version V1.0   
 */

package com.x.publics.http.model;

import java.util.ArrayList;

import com.x.publics.model.AppInfoBean;
import com.x.publics.model.RingtonesBean;
import com.x.publics.model.WallpaperBean;

/**
 * @ClassName: SearchResponse
 * @Description: TODO
 
 * @date 2014-1-20 上午11:45:51
 */

public class SearchResponse extends TabResponse {

	public ArrayList<AppInfoBean> recommendlist = new ArrayList<AppInfoBean>(); // app 搜索不到数据时，返回推荐的数据集合
	public ArrayList<RingtonesBean> recommendMusiclist = new ArrayList<RingtonesBean>(); // Ringtones 搜索不到数据时，返回推荐的数据集合
	public ArrayList<WallpaperBean> recommendImagelist = new ArrayList<WallpaperBean>(); // wallpaper 搜索不到数据时，返回推荐的数据集合

	public ArrayList<AppInfoBean> getRecommendlist() {
		return recommendlist;
	}

	public void setRecommendlist(ArrayList<AppInfoBean> recommendlist) {
		this.recommendlist = recommendlist;
	}

	public ArrayList<WallpaperBean> getRecommendImagelist() {
		return recommendImagelist;
	}

	public void setRecommendImagelist(ArrayList<WallpaperBean> recommendImagelist) {
		this.recommendImagelist = recommendImagelist;
	}

	public ArrayList<RingtonesBean> getRecommendMusiclist() {
		return recommendMusiclist;
	}

	public void setRecommendMusiclist(ArrayList<RingtonesBean> recommendMusiclist) {
		this.recommendMusiclist = recommendMusiclist;
	}

}
