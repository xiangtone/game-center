package com.x.publics.http.model;

import java.util.ArrayList;
import java.util.List;

import com.x.publics.model.CountryBean;

/**
 * 
* @ClassName: CountryResponse
* @Description: 区分多国家，数据响应

* @date 2014-5-20 下午4:58:47
*
 */
public class CountryResponse extends CommonResponse {

	/** 自动获取ID */
	public int autoRaveId;

	/** 国家数量 */
	public int countryNum;

	/** 封装国家实体类信息集合 */
	public List<CountryBean> countryList = new ArrayList<CountryBean>();

	public int getAutoRaveId() {
		return autoRaveId;
	}

	public void setAutoRaveId(int autoRaveId) {
		this.autoRaveId = autoRaveId;
	}

	public int getCountryNum() {
		return countryNum;
	}

	public void setCountryNum(int countryNum) {
		this.countryNum = countryNum;
	}

	public List<CountryBean> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<CountryBean> countryList) {
		this.countryList = countryList;
	}

}
