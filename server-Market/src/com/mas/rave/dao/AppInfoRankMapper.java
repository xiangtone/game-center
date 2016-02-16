package com.mas.rave.dao;

import com.mas.rave.main.vo.AppInfoRank;

/**
 * app_info_rank访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppInfoRankMapper {

	/**
	 * 增加
	 */
	int insert(AppInfoRank record);

	/**
	 * 根据id查看
	 */
	AppInfoRank selectByPrimaryKey(AppInfoRank record);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppInfoRank record);

}