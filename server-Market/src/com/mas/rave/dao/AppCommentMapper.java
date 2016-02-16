package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;

import com.mas.rave.main.vo.AppComment;

/**
 * app对应评论
 * 
 * @author liwei.sz
 * 
 */
public interface AppCommentMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(HashMap<String, Object> map);

	/**
	 * 根据id删除对应app评论信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加app对应评论信息
	 */
	int insert(AppComment record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(AppComment record);

	/**
	 * 分页查询app评论信息
	 */
	List<AppComment> selectByExample(HashMap<String, Object> map);

	/**
	 * 根据id查看app评论信息
	 */
	AppComment selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppComment record);

	int deleteByAppId(int appId);

}