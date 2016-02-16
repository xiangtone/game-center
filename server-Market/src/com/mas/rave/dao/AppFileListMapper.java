package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppFileList;
import com.mas.rave.main.vo.AppFileListExample;

/**
 * app文件数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppFileListMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(AppFileListExample example);

	/**
	 * 根据id删除对应app信息
	 */
	int deleteByPrimaryKey(int id);

	/**
	 * 增加app文件信息
	 */
	int insert(AppFileList record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(AppFileList record);

	/**
	 * 分页查询app文件信息
	 */
	List<AppFileList> selectByExample(AppFileListExample example, RowBounds rowBounds);

	/**
	 * 分页查询app文件信息
	 */
	List<AppFileList> selectByExample(AppFileListExample example);

	/**
	 * 根据id查看app文件信息
	 */
	AppFileList selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppFileList record);
	/**
	 * 根据app信息更新app对应名称
	 * @param appId
	 * @return
	 */
	int upAppFileListByAppId(HashMap<String, Object> map);
	
	AppFileList checkFile(HashMap<String, Object> map);
}