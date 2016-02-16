package com.mas.rave.dao;

import java.util.Date;
import java.util.List;

import com.mas.rave.main.vo.AppannieInfo;

public interface AppannieInfoMapper {

	/**
	 * 分页查询
	 * @param appannieInfo
	 * @return
	 */
	List<AppannieInfo> selectByExample(AppannieInfo appannieInfo);
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	AppannieInfo selectByPrimaryKey(Integer id);
	
	int deleteByPrimaryKey(Integer id);
	
	int deleteByCondition(AppannieInfo appannieInfo);
	
	int insert(AppannieInfo appannieInfo);
	
	int countByExample(AppannieInfo appannieInfo);
	
	List<AppannieInfo> selectByCondition(AppannieInfo appannieInfo);
	
	List<AppannieInfo> selectByCreateTime(Date createTime);

}