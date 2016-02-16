package com.mas.rave.dao;

import java.util.List;

import com.mas.rave.main.vo.ClientFeedbackZappCode;

/**
 * clientfeedbackZappCode 数据访问接口
 * @author jieding
 *
 */
public interface ClientFeedbackZappCodeMapper {

	/**
	 * 插入
	 */
	int insert(ClientFeedbackZappCode record);

	/**
	 * 根据id查看
	 */
	ClientFeedbackZappCode selectByPrimaryKey(Integer id);
	
	/**
	 * 查询所有
	 * @return
	 */
	List<ClientFeedbackZappCode> getAllClientFeedbackZappCode();

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(ClientFeedbackZappCode record);
}
