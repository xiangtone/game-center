package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppFileZappUpdate;
import com.mas.rave.main.vo.AppFileZappUpdateExample;

public interface AppFileZappUpdateMapper {
	/**
	 * 按条件分页
	 * @param example
	 * @return
	 */
	List<AppFileZappUpdate> selectByExample(AppFileZappUpdateExample example,RowBounds rowBounds);

	/**
	 * 按条件获取总数
	 * @param example
	 * @return
	 */
	int countByExample(AppFileZappUpdateExample example);
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	AppFileZappUpdate selectByPrimaryKey(Integer id);
	
	int deleteByPrimaryKey (Integer id);
	/**
	 * 插入
	 * @param appFileZappUpdate
	 * @return
	 */
	int insert(AppFileZappUpdate appFileZappUpdate);
	/**
	 * 更新
	 * @param appFileZappUpdate
	 * @return
	 */
	int updateByPrimaryKey(AppFileZappUpdate appFileZappUpdate);
	
	
	List<AppFileZappUpdate> selectByCondition(AppFileZappUpdate appFileZappUpdate);
}
