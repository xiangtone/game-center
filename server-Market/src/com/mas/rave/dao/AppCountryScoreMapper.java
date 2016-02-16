package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppCountryScore;

public interface AppCountryScoreMapper {

	/**
	 * 分页查询
	 * @param appCountryScore
	 * @return
	 */
	List<AppCountryScore> selectByExample(AppCountryScore appCountryScore);
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	AppCountryScore selectByPrimaryKey(Integer id);
	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Integer id);
	/**
	 * 根据条件查询
	 * @param appCountryScore
	 * @return
	 */
	int deleteByCondition(AppCountryScore appCountryScore);
	/**
	 * 更新
	 * @param appCountryScore
	 * @return
	 */
	int update(AppCountryScore appCountryScore);
	/**
	 * 修改状态
	 * @param appCountryScore
	 * @return
	 */
	int updateByState(AppCountryScore appCountryScore);
	/**
	 * 插入
	 * @param appCountryScore
	 * @return
	 */
	int insert(AppCountryScore appCountryScore);
	/**
	 * 根据条件获取总数
	 * @param appCountryScore
	 * @return
	 */
	int countByExample(AppCountryScore appCountryScore);
	/**
	 * 根据
	 * @param appCountryScore
	 * @return
	 */
	List<AppCountryScore> selectByCondition(AppCountryScore appCountryScore);
	
	List<AppCountryScore> getSelectAppFiles(HashMap<String, Object> map, RowBounds rowBounds);

	List<AppCountryScore> getSelectAppFiles(HashMap<String, Object> map);

	Integer getSelectAppFilesCount(HashMap<String, Object> map);

	void insertSelectApps(Map<String, Object> map);
	
	void insertByApp(Map<String, Object> map);
	
}