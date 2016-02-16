package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.Market;

/**
 * 平台信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface MarketService {
	static class MarketCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();
		
		public MarketCriteria nameLike(String name) {
			params.put(1, name);
			return this;
		}
		
		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示平台信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<Market> searchMarkets(MarketCriteria criteria,
			int currentPage, int pageSize);

	// 查看单个平台信息
	public Market getMarket(int id);
	
	//根据包名查找
	public Market getMarketPac(String pac_name);
	
	//获取所有平台信息
	public List<Market> getAllMarkets();

	// 增加平台信息
	public void addMarket(Market market,Integer channelId);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(Market record);

	// 更新平台信息
	public void upMarket(Market market,Integer channelId) ;

	// 删除平台信息
	public void delMarket(Integer id);

	void batchDelete(Integer[] ids);
}
