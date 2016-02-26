package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
 * @ClassName: WallpaperRequest
 * @Desciption: 壁纸栏目，数据请求
 
 * @Date: 2014-3-25 下午2:03:27
 */

public class WallpaperRequest extends CommonRequest {

	public Pager pager;
	public int themeId;
	public String column;
    public int categoryId;
	public WallpaperRequestData data;

	public WallpaperRequest() {
		super(Constan.Rc.GET_WALLPAPER, Constan.SIGN);
	}

	public static class WallpaperRequestData {

		public WallpaperRequestData(int ct) {
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

	public WallpaperRequestData getData() {
		return data;
	}

	public void setData(WallpaperRequestData data) {
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
