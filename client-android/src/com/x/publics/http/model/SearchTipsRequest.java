/**   
 * @Title: SearchTipsRequest.java
 * @Package com.mas.amineappstore.publics.http.model
 * @Description: TODO(用一句话描述该文件做什么)
 
 * @date 2014-7-30 下午4:09:52
 * @version V1.0   
 */

package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
 * @ClassName: SearchTipsRequest
 * @Description: TODO(这里用一句话描述这个类的作用)
 
 * @date 2014-7-30 下午4:09:52
 * 
 */

public class SearchTipsRequest extends CommonRequest {

	public Pager page;
	public SearchTipsData data;

	public SearchTipsRequest(int rc) {
		super(rc, Constan.SIGN);
	}

	public static class SearchTipsData {
		public int raveId;
		public String content;

		public SearchTipsData() {
			this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	public Pager getPage() {
		return page;
	}

	public void setPage(Pager page) {
		this.page = page;
	}

	public SearchTipsData getData() {
		return data;
	}

	public void setData(SearchTipsData data) {
		this.data = data;
	}

}
