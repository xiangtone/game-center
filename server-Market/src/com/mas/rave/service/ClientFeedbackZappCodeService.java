package com.mas.rave.service;

import java.util.List;

import com.mas.rave.main.vo.ClientFeedbackZappCode;
/**
 * ClientFeedbackZappCode 服务层
 * @author jieding
 *
 */
public interface ClientFeedbackZappCodeService {

	/**
	 * 插入
	 */
	public void insert(ClientFeedbackZappCode record);

	/**
	 * 根据id查看
	 */
	public ClientFeedbackZappCode selectByPrimaryKey(Integer id);
	
	/**
	 * 查询所有
	 * @return
	 */
	public List<ClientFeedbackZappCode> getAllClientFeedbackZappCode();

	/**
	 * 根据主键更新
	 */
	public void updateByPrimaryKey(ClientFeedbackZappCode record);
}
