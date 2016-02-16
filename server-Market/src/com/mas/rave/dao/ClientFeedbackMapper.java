package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.ClientFeedback;

/**
 * clientfeedback 数据访问接口
 * @author jieding
 *
 */
public interface ClientFeedbackMapper {


	/**
	 * 获取当前总页数
	 */
	int countByExample(ClientFeedback criteria);

	/**
	 * 根据id删除对应客户端回馈信息信息 
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * 增加客户端回馈信息信息 
	 */
	int insert(ClientFeedback record);


	/**
	 * 分页查询客户端回馈信息信息 
	 */
	List<ClientFeedback> selectByExample(ClientFeedback criteria, RowBounds rowBounds);
	
	/**
	 * 分页查询渠道信息
	 */
	List<ClientFeedback> selectByExample(ClientFeedback criteria);

	/**
	 * 查找所有客户端回馈信息信息 
	 * 
	 * @param id
	 * @return
	 */
	List<ClientFeedback> getClientFeedbacks(String id);
	/**
	 * 根据clientFeedback 查询 分页
	 * @param clientId
	 * @return
	 */
	List<ClientFeedback> selectByCondition(ClientFeedback clientFeedback);

	/**
	 * 按条件获取总数
	 * @param clientFeedback
	 * @return
	 */
	int countByCondition(ClientFeedback clientFeedback);
	/**
	 * 根据id查看客户端回馈信息信息 
	 */
	ClientFeedback selectByPrimaryKey(Integer id);
	List<ClientFeedback> selectByClientFeedback(ClientFeedback clientFeedback);
	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(ClientFeedback record);
	
	int deleteByClientId(Integer clientId); 

}
