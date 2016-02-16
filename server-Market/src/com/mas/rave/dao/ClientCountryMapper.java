package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;

import com.mas.rave.main.vo.ClientCountry;

/**
 * clientCountry数据访问接口
 * 
 * @author jieding
 * 
 */
public interface ClientCountryMapper {
	/**
	 * 获取当前总页数
	 */
	int countByExample(ClientCountry example);

	/**
	 * 增加国家中英对照表
	 */
	int insert(ClientCountry record);

	/**
	 * 分页查询国家中英对照表
	 */
	List<ClientCountry> selectByExample(ClientCountry example);

	/**
	 * 根据国家中文名查询
	 */
	ClientCountry selectByCountryCn(String countryCn);

	/**
	 * 根据国家中文名更新
	 */
	int updateByCountryCn(ClientCountry record);

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
	int deleteByCountryCn(String countryCn);
}