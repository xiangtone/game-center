package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.SearchKeywordIcon;
import com.mas.rave.main.vo.SearchKeywordIconExample;

/**
 * SearchKeywordIcon数据访问接口
 * 
 * @author jieding
 * 
 */
public interface SearchKeywordIconMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(SearchKeywordIconExample example);

	/**
	 * 根据id删除
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加信息
	 */
	int insert(SearchKeywordIcon record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(SearchKeywordIcon record);


	/**
	 * 分页查询国家文件信息
	 */
	List<SearchKeywordIcon> selectByExample(SearchKeywordIconExample example, RowBounds rowBounds);
	/**
	 * 根据id查看国家文件信息
	 */
	SearchKeywordIcon selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(SearchKeywordIcon record);
	
	/**
	 * 更新国家状态
	 */
	int updateState(HashMap<String, Object> map);
	
	/**
	 * 获取所有国家信息
	 * @return
	 */
	public List<SearchKeywordIcon> getSearchKeywordIcons();
}