package com.mas.rave.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mas.rave.main.vo.AppInfoConfig;
import com.mas.rave.main.vo.AppInfoConfigExample;

/**
 * appInfoConfig数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppInfoConfigMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(HashMap<String, Object> map);

	/**
	 * 根据id删除对应app配置信息
	 */
	int deleteByPrimaryKey(int id);

	/**
	 * 根据名字删除对应app配置信息
	 */
	int deleteByName(String name);
	
	int deleteByTime(HashMap<String, Object> map);

	/**
	 * 增加app配置信息
	 */
	int insert(AppInfoConfig record);

	/**
	 * 分页查询app配置信息
	 */
	List<AppInfoConfig> selectByExample(HashMap<String, Object> map);

	/**
	 * 分页查询app配置信息
	 */
	List<AppInfoConfig> selectByExample(AppInfoConfigExample example);

	/**
	 * 根据id查看app配置信息
	 */
	AppInfoConfig selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppInfoConfig record);
	int updateStatusBy(boolean status);
	public AppInfoConfig getAppConfig(HashMap<String, Object> map);

}