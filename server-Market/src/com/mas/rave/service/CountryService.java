package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.CountryExample;
/**
 * country数据访问接口
 * 
 * @author jieding
 * 
 */
public interface CountryService {
	
	static class CountryCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();
		
		public CountryCriteria nameLike(String name){
			params.put(1,name);
			return this;
		}
		public Map<Integer,Object> getParams(){
			return Collections.unmodifiableMap(params);
		}
	}
	
	//所有国家信息
	public List<Country> getCountrys();
	public List<Country> getCountry(CountryExample example);
	public PaginationVo<Country> selectByExample(CountryCriteria criteria, int currentPage,
			int pageSize);
	// 单个国家信息
	public Country getCountry(int id);

	// 增加国家信息
	public void addCountry(Country country);


	// 更新国家信息
	public void upCountry(Country country);

	// 删除国家信息
	public void delCountry(int id);

	void batchDelete(Integer[] ids);
	
	
	void updateState(int id ,int state);

}
