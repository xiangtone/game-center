package com.mas.rave.service;

import java.util.HashMap;
import java.util.List;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.ClientCountry;

/**
 * clientCountry数据访问接口
 * 
 * @author jieding
 * 
 */
public interface ClientCountryService {

	/**
	 * 增加国家中英对照表
	 */
	public void insert(ClientCountry record);

	/**
	 * 分页查询国家中英对照表
	 */
	public PaginationVo<ClientCountry> selectByExample(ClientCountry example, int currentPage, int pageSize);

	/**
	 * 根据国家中文名查询
	 */
	public ClientCountry selectByCountryCn(String countryCn);

	/**
	 * 根据国家中文名更新
	 */
	public void updateByCountryCn(ClientCountry record);

	/**
	 * 获取所有国家中英对照表
	 * 
	 * @return
	 */
	public List<ClientCountry> getClientCountrys();
	
	/**
	 * 参数查找
	 * @param map
	 * @return
	 */
	public ClientCountry getParament(HashMap<String, Object> map);
	
	/**
	 * 根据名字删除
	 */
	public int deleteByCountryCn(String countryCn);
}
