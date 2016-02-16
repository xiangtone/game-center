package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.ImageInfo;
import com.mas.rave.main.vo.MessagePush;

/**
 * message_push信息数据访问接口
 * 
 * @author liwei
 * 
 */
public interface MessagePushMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(MessagePush example);

	/**
	 * 根据id删除对应MessagePush信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加MessagePush信息
	 */
	int insert(MessagePush record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(MessagePush record);

	/**
	 * 分页查询MessagePush信息
	 */
	List<MessagePush> selectByExample(MessagePush example, RowBounds rowBounds);

	/**
	 * 分页查询MessagePush信息
	 */
	List<MessagePush> selectByExample(MessagePush example);

	/**
	 * 根据id查看MessagePush信息
	 */
	MessagePush selectByPrimaryKey(long id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(MessagePush record);

}