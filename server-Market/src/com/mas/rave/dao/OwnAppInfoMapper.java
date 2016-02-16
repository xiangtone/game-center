package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.OwnAppInfo;

/**
 * app数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface OwnAppInfoMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(OwnAppInfo example);

	/**
	 * 根据id删除对应app信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加app信息
	 */
	int insert(OwnAppInfo record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(OwnAppInfo record);

	/**
	 * 分页查询app信息
	 */
	List<OwnAppInfo> selectByExample(OwnAppInfo example, RowBounds rowBounds);

	/**
	 * 分页查询app信息
	 */
	List<OwnAppInfo> selectByExample(OwnAppInfo example);

	List<OwnAppInfo> queryAll(OwnAppInfo example);

	/**
	 * 根据id查看app信息
	 */
	OwnAppInfo selectByPrimaryKey(long id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(OwnAppInfo record);

	OwnAppInfo selectByName(String name);
}