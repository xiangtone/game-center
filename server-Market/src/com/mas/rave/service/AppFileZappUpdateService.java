package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppFileZappUpdate;


public interface AppFileZappUpdateService {
	static class AppFileZappUpdateCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppFileZappUpdateCriteria versionNameLikeTo(String str){
			params.put(1,str);
			return this;
		}
		public AppFileZappUpdateCriteria upgradeTypeEqualTo(int upgradeType){
			params.put(2,upgradeType);
			return this;
		}
		public AppFileZappUpdateCriteria apkKeyEqualTo(String str){
			params.put(3,str);
			return this;
		}	
		public Map<Integer,Object> getParams(){
			return Collections.unmodifiableMap(params);
		}
	}
	public PaginationVo<AppFileZappUpdate> searchAppFileZappUpdates(AppFileZappUpdateCriteria criteria,
			int currentPage, int pageSize);
	
	public void addAppFileZappUpdate(AppFileZappUpdate appFileZappUpdate);
	
	public void delAppFileZappUpdate(Integer id);
	
	public void batchDelete(Integer[] ids);
	
	public void updateByPrimarykey(AppFileZappUpdate appFileZappUpdate);
	
	public AppFileZappUpdate selectByPrimaryKey(Integer id);
	/**
	 * 根据条件查询 现固定条件为 apkKey, versionName或versionCode
	 * @param appFileZappUpdate
	 * @return
	 */
	public List<AppFileZappUpdate> selectByCondition(AppFileZappUpdate appFileZappUpdate);
}
