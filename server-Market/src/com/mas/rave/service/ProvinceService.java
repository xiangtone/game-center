package com.mas.rave.service;

import java.util.List;

import com.mas.rave.main.vo.Province;

/**
 * 省份数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface ProvinceService {
	static class ProvinceCriteria {
		//private Map<Integer, Object> params = new HashMap<Integer, Object>();
	}


	//所有省份信息
	public List<Province> getProvinces();

	// 单个省份信息
	public Province getProvince(int id);

	// 增加省份信息
	public void addProvince(Province province);


	// 更新省份信息
	public void upProvince(Province province);

	// 删除省份信息
	public void delProvince(int id);

	void batchDelete(Integer[] ids);

}
