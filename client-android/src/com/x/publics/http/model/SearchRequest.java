/**   
 * @Title: SearchRequest.java
 * @Package com.x.http.model
 * @Description: TODO 
 
 * @date 2014-1-20 上午11:27:45
 * @version V1.0   
 */

package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
 * @ClassName: SearchRequest
 * @Description: TODO
 
 * @date 2014-1-20 上午11:27:45
 * 
 */

public class SearchRequest extends CommonRequest {

	public Pager page;
	public String action;
	public MasUser masUser;
	public SearchData data;

	public SearchRequest(int rc) {
		super(rc, Constan.SIGN);
	}

	public static class SearchData {
		public int raveId;
		public int clientId;
		public String content;

		public SearchData() {
			this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
		}

		public int getClientId() {
			return clientId;
		}

		public void setClientId(int clientId) {
			this.clientId = clientId;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	public MasUser getMasUser() {
		return masUser;
	}

	public void setMasUser(MasUser masUser) {
		this.masUser = masUser;
	}

	public SearchData getData() {
		return data;
	}

	public void setData(SearchData data) {
		this.data = data;
	}

	public Pager getPage() {
		return page;
	}

	public void setPage(Pager page) {
		this.page = page;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
