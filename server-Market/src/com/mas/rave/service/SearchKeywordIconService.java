package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.SearchKeywordIcon;
/**
 * SearchKeywordIcon数据访问接口
 * 
 * @author jieding
 * 
 */
public interface SearchKeywordIconService {
	
	static class SearchKeywordIconCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();
		
		public SearchKeywordIconCriteria andNameLike(String name){
			params.put(1,name);
			return this;
		}
		public SearchKeywordIconCriteria stateEqual(boolean state){
			params.put(2,state);
			return this;
		}
		public Map<Integer,Object> getParams(){
			return Collections.unmodifiableMap(params);
		}
	}
	
	//所有信息
	public List<SearchKeywordIcon> getSearchKeywordIcons();

	public PaginationVo<SearchKeywordIcon> selectByExample(SearchKeywordIconCriteria criteria, int currentPage,
			int pageSize);
	// 单个
	public SearchKeywordIcon getSearchKeywordIcon(int id);

	// 增加
	public void addSearchKeywordIcon(SearchKeywordIcon searchKeywordIcon);


	// 更新
	public void upSearchKeywordIcon(SearchKeywordIcon searchKeywordIcon);

	// 删除
	public void delSearchKeywordIcon(int id);

	void batchDelete(Integer[] ids);
	
	
	void updateState(int id ,int state);

}
