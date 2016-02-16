package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.ClientFeedbackZapp;

/**
 * clientfeedbackZapp Service接口
 * @author jieding
 *
 */
public interface ClientFeedbackZappService {
	static class ClientFeedbackZappCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public ClientFeedbackZappCriteria questionLikeTo(String str){
			params.put(1,str);
			return this;
		}
		public ClientFeedbackZappCriteria replyContentLikeTo(String str){
			params.put(2,str);
			return this;
		}
		public Map<Integer,Object> getParams(){
			return Collections.unmodifiableMap(params);
		}
	}
	/**
	 * 根据id删除
	 */
	int deleteByPrimaryKey(Integer id);

	void batchDelete(Integer[] ids);
	/**
	 * 插入
	 */
	int insert(ClientFeedbackZapp record);


	/**
	 * 分页查询
	 */
	PaginationVo<ClientFeedbackZapp> selectByExample(ClientFeedbackZappCriteria criteria, int currentPage, int pageSize);

	/**
	 * 根据id查看 
	 */
	ClientFeedbackZapp selectByPrimaryKey(Integer id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(ClientFeedbackZapp record);
}
