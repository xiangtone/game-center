package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.Country;
import com.mas.rave.main.vo.CountryExample;

/**
 * country数据访问接口
 * 
 * @author jieding
 * 
 */
public interface CountryMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(CountryExample example);

	/**
	 * 根据id删除对应国家信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加国家文件信息
	 */
	int insert(Country record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(Country record);


	/**
	 * 分页查询国家文件信息
	 */
	List<Country> selectByExample(CountryExample example, RowBounds rowBounds);
	/**
	 * 根据条件查询
	 * @param example
	 * @return
	 */
	List<Country> selectByExample(CountryExample example);
	/**
	 * 根据id查看国家文件信息
	 */
	Country selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(Country record);
	
	/**
	 * 更新国家状态
	 */
	int updateState(HashMap<String, Object> map);
	
	/**
	 * 获取所有国家信息
	 * @return
	 */
	public List<Country> getCountrys();
}