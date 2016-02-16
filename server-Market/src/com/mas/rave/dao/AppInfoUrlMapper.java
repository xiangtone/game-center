package com.mas.rave.dao;

import com.mas.rave.main.vo.AppInfoUrl;



/**
 * appInfoUrl数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppInfoUrlMapper {

	AppInfoUrl selectByName(String name);
	/**
	 * 根据名字删除对应app配置url信息
	 */
	int deleteByName(String name);
	
	/**
	 * 增加app配置信息
	 */
	int insert(AppInfoUrl record);
	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppInfoUrl record);

}