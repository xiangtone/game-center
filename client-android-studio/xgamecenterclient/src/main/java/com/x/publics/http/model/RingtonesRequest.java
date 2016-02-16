package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
 * @ClassName: RingtonesRequest
 * @Desciption: 音乐栏目，数据请求
 
 */

public class RingtonesRequest extends CommonRequest {

	public Pager pager;
	public int themeId;
	public String column;
    public int categoryId;
	public MusicRequestData data;

	public RingtonesRequest() {
		super(Constan.Rc.GET_MUSIC, Constan.SIGN);
	}

	public static class MusicRequestData {

		public MusicRequestData(int ct) {
			this.ct = ct;
			this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
		}

		public int ct;

		public int raveId;

		public int getCt() {
			return ct;
		}

		public void setCt(int ct) {
			this.ct = ct;
		}

		public int getRaveId() {
			return raveId;
		}

		public void setRaveId(int raveId) {
			this.raveId = raveId;
		}
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public MusicRequestData getData() {
		return data;
	}

	public void setData(MusicRequestData data) {
		this.data = data;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public int getThemeId() {
		return themeId;
	}

	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

}
