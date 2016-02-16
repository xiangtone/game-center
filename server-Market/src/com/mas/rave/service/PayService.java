package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.Pay;

;

/**
 * pay信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface PayService {
	static class PayCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public PayCriteria mogValueEqualTo(Integer mogValue) {
			params.put(1, mogValue);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示pay信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<Pay> searchPays(PayCriteria criteria, int currentPage, int pageSize);

	// 查看单个pay信息
	public Pay getPay(int id);

	// 增加pay信息
	public void addPay(Pay pay);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Pay record);

	// 更新pay信息
	public void upPay(Pay pay);

	// 删除pay信息
	public void delPay(int id);

	void batchDelete(Integer[] ids);
}
