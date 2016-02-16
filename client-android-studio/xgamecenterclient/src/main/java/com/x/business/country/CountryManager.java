/**   
* @Title: CountryManager.java
* @Package com.x.business.country
* @Description: TODO(用一句话描述该文件做什么)

* @date 2014-6-6 上午10:06:42
* @version V1.0   
*/

package com.x.business.country;

import android.content.Context;

import com.x.publics.utils.SharedPrefsUtil;

/**
* @ClassName: CountryManager
* @Description: TODO(这里用一句话描述这个类的作用)

* @date 2014-6-6 上午10:06:42
* 
*/

public class CountryManager {

	private static CountryManager countryManager;

	public static CountryManager getInstance() {
		if (countryManager == null) {
			countryManager = new CountryManager();
		}
		return countryManager;
	}

	public CountryManager() {

	}

	public int getCountryId(Context context) {
		return SharedPrefsUtil.getValue(context, "COUNTRY_ID", 1);
	}

	public void saveCountryId(Context context, int id, boolean auto) {
		SharedPrefsUtil.putValue(context, "COUNTRY_ID", id);
		SharedPrefsUtil.putValue(context, "AUTO_COUNTRY", auto);
	}

	public boolean isAutoCountry(Context context) {
		return SharedPrefsUtil.getValue(context, "AUTO_COUNTRY", true);
	}

}
