package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.ClientFeedback;

/**
 * 客户端回馈信息数据访问接口
 * @author jieding
 *
 */
public interface ClientFeedbackService {

	static class ClientFeedbackCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public ClientFeedbackCriteria clientIdLikeTo(String str){
			params.put(1,str);
			return this;
		}
		public ClientFeedbackCriteria deviceModelLikeTo(String str){
			params.put(2,str);
			return this;
		}
		public ClientFeedbackCriteria deviceVendorLikeTo(String str){
			params.put(3,str);
			return this;
		}
		public Map<Integer,Object> getParams(){
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示 客户端回馈信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<ClientFeedback> searchClientFeedbacks(ClientFeedback criteria,
			int currentPage, int pageSize);


	/**
	 * 获取单个客户回馈信息
	 * @param id
	 * @return
	 */
	public ClientFeedback getClientFeedback(int id);
	/**
	 * 根据clientId 分页查询
	 * @param clientId
	 * @return
	 */
	public PaginationVo<ClientFeedback> selectByClientId(Integer clientId, int currentPage, int pageSize);

	/**
	 * 根据imei 分页查询
	 * @param clientId
	 * @return
	 */
	public PaginationVo<ClientFeedback> selectByImei(String imei, int currentPage, int pageSize);

	/**
	 * 添加客户回馈信息
	 * @param clientFeedback
	 * @param id
	 */
	public void addClientFeedback(ClientFeedback clientFeedback);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(ClientFeedback record);

	/**
	 * 更新客户回馈信息
	 * @param clientFeedback
	 * @param id
	 */
	public void upClientFeedback(ClientFeedback clientFeedback,Integer id);

	/**
	 * 删除客户回馈信息
	 * @param id
	 */
	public void delClientFeedback(Integer id);
 
	public void batchDelete(Integer[] ids);
	public void delClientId(Integer clientId);
	public void batchDeleteClientId(Integer[] clientIds);
	public List<ClientFeedback> selectByClientFeedback(ClientFeedback clientFeedback);
		
}
