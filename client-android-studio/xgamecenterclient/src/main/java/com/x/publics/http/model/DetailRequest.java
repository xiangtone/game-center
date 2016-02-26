package com.x.publics.http.model;

import com.x.AmineApplication;
import com.x.business.country.CountryManager;
import com.x.publics.utils.Constan;
import com.x.publics.utils.SharedPrefsUtil;

/**
 * @ClassName: DetailRequest
 * @Desciption: 应用详情请求参数
 
 * @Date: 2014-1-16 下午3:48:49
 */

public class DetailRequest extends CommonRequest {

	public DetailRequest(int rc) {
		super(rc, Constan.SIGN);
		this.raveId = CountryManager.getInstance().getCountryId(AmineApplication.context);
	}

	public int apkId;
	public int appId;
	public int ps;
	public int pn;
	public int ct;
	public int raveId;
	public String packageName;
	public String actionUrl;

	public int getApkId() {
		return apkId;
	}

	public void setApkId(int apkId) {
		this.apkId = apkId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public int getPn() {
		return pn;
	}

	public void setPn(int pn) {
		this.pn = pn;
	}

	public int getCt() {
		return ct;
	}

	public void setCt(int ct) {
		this.ct = ct;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

}
