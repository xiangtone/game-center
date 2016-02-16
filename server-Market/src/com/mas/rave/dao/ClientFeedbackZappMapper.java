package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.ClientFeedbackZapp;
import com.mas.rave.main.vo.ClientFeedbackZappExample;

/**
 * clientfeedbackZapp 数据访问接口
 * @author jieding
 *
 */
public interface ClientFeedbackZappMapper {


	/**
	 * 获取当前总页数
	 */
	int countByExample(ClientFeedbackZappExample example);

	/**
	 * 根据id删除
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * 插入
	 */
	int insert(ClientFeedbackZapp record);


	/**
	 * 分页查询
	 */
	List<ClientFeedbackZapp> selectByExample(ClientFeedbackZappExample example, RowBounds rowBounds);

	/**
	 * 根据id查看
	 */
	ClientFeedbackZapp selectByPrimaryKey(Integer id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(ClientFeedbackZapp record);
}
