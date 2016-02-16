package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.OwnAppFile;

/**
 * app文件数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface OwnAppFileMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(OwnAppFile example);

	/**
	 * 根据id删除对应app信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 更新state
	 */
	int updateState(OwnAppFile appFile);

	/**
	 * 增加app文件信息
	 */
	int insert(OwnAppFile record);

	/**
	 * 分页查询app文件信息
	 */
	List<OwnAppFile> selectByExample(OwnAppFile example, RowBounds rowBounds);

	/**
	 * 分页查询app文件信息
	 */
	List<OwnAppFile> selectByExample(OwnAppFile example);

	/**
	 * 根据id查看app文件信息
	 */
	OwnAppFile selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(OwnAppFile record);

	/**
	 * 根据app信息更新app对应文件
	 * 
	 * @param appId
	 * @return
	 */
	int upOwnAppFileByAppId(HashMap<String, Object> map);

	List<OwnAppFile> getByParameter(HashMap<String, Object> map);
	
	public List<OwnAppFile> getOwnAppFiles(Integer appId);

}