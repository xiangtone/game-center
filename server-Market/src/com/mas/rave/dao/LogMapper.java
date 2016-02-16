package com.mas.rave.dao;

import java.util.List;

import com.mas.rave.main.vo.Log;

/**
 * log日志数据访问接口
 * 
 * @author jieding
 * 
 */
public interface LogMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(Log example);
	
	/**
	 * 增加Log信息
	 */
	int insert(Log record);

	/**
	 * 分页查询Log信息
	 */
	List<Log> selectByExample(Log example);

	/**
	 * 根据id查看Log信息
	 */
	Log selectByPrimaryKey(long id);

	/**
	 * 获取所有的log日志
	 * @return
	 */
	public List<Log> getAllLogs();
	
}