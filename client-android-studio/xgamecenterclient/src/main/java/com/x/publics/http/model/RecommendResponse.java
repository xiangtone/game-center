package com.x.publics.http.model;

import com.x.publics.model.RecommendBean;

import java.util.ArrayList;

/**
 * @ClassName: RecommendResponse
 * @Desciption: 应用推荐响应response
 
 * @Date: 2014-1-28 下午3:21:54
 */

public class RecommendResponse extends CommonResponse {

	public int recommendNum; // 应用推荐数量
	public ArrayList<RecommendBean> recommendlist; // 应用推荐集合

	public int getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(int recommendNum) {
		this.recommendNum = recommendNum;
	}

	public ArrayList<RecommendBean> getRecommendlist() {
		return recommendlist;
	}

	public void setRecommendlist(ArrayList<RecommendBean> recommendlist) {
		this.recommendlist = recommendlist;
	}

}
