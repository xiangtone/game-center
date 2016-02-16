package com.mas.rave.service;

import java.util.List;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.MessagePush;

/**
 * MessagePush信息数据访问接口
 * 
 * @author liwei
 * 
 */
public interface MessagePushService {

	/**
	 * 分页显示MessagePush信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<MessagePush> searchMessagePush(MessagePush criteria, int currentPage, int pageSize);

	/**
	 * 获取当前总页数
	 */
	int countByExample(MessagePush example);

	/**
	 * 根据id删除对应MessagePush信息
	 */
	int deleteByPrimaryKey(long id);

	void batchDelete(Integer[] ids);

	/**
	 * 增加MessagePush信息
	 */
	int insert(MessagePush record);

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
