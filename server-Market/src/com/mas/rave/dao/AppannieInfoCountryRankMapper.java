package com.mas.rave.dao;

import java.util.Date;
import java.util.List;

import com.mas.rave.main.vo.AppannieInfoCountryRank;

public interface AppannieInfoCountryRankMapper {

	/**
	 * 分页查询
	 * @param appannieInfo
	 * @return
	 */
	List<AppannieInfoCountryRank> selectByExample(AppannieInfoCountryRank appannieInfo);
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	AppannieInfoCountryRank selectByPrimaryKey(Integer id);
	
	int deleteByPrimaryKey(Integer id);
	
	int deleteByCondition(AppannieInfoCountryRank appannieInfo);
	
	int insert(AppannieInfoCountryRank appannieInfo);
	
	int countByExample(AppannieInfoCountryRank appannieInfo);
	
	List<AppannieInfoCountryRank> selectByCondition(AppannieInfoCountryRank appannieInfo);
	
	List<AppannieInfoCountryRank> selectByCreateTime(Date createTime);
	
	int updateStatusBy(AppannieInfoCountryRank criteria);
	int update(AppannieInfoCountryRank criteria);
}