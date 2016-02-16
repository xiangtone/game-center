package com.mas.rave.service;

import java.util.HashMap;
import java.util.List;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppCountryScore;

public interface AppCountryScoreService {
	/**
	 * 分页查询
	 * @param appCountryScore
	 * @return
	 */
	public PaginationVo<AppCountryScore> selectByExample(AppCountryScore appCountryScore, int currentPage, int pageSize);
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	public AppCountryScore selectByPrimaryKey(Integer id);
	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 */
	public void deleteByPrimaryKey(Integer id);
	/**
	 * 根据条件查询
	 * @param appCountryScore
	 * @return
	 */
	public void deleteByCondition(AppCountryScore appCountryScore);
	/**
	 * 更新
	 * @param appCountryScore
	 * @return
	 */
	public void update(AppCountryScore appCountryScore);
	/**
	 * 修改状态
	 * @param appCountryScore
	 * @return
	 */
	public void updateByState(AppCountryScore appCountryScore);
	/**
	 * 插入
	 * @param appCountryScore
	 * @return
	 */
	public void insert(AppCountryScore appCountryScore);

	/**
	 * 根据
	 * @param appCountryScore
	 * @return
	 */
	public List<AppCountryScore> selectByCondition(AppCountryScore appCountryScore);
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void batchDelete(Integer[] ids);
	
	public PaginationVo<AppCountryScore> getSelectAppFiles(HashMap<String, Object> map, int currentPage, int pageSize);
	
	public void insertSelectApps(Integer raveId, List<Integer> ids);
	
	/**
	 * 插入
	 * @param raveId    国家id
	 * @param albumId   app专题id
	 * @param columnId  app页签id
	 * @param ids
	 */
	public void insertByApp(Integer raveId, Integer albumId, Integer columnId, List<Integer> ids);
}
