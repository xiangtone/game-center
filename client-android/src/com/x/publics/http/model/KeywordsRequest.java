package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
 * 
* @ClassName: KeywordsRequest
* @Description: 搜索关键字，数据请求

* @date 2014-7-7 下午2:50:19
*
 */
public class KeywordsRequest extends CommonRequest {

	public int raveId;
	public int albumId;
	public Pager pager;

	public KeywordsRequest(int rc) {
		super(rc, Constan.SIGN);
		this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

}
