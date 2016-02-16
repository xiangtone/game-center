package com.mas.rave.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppInfoConfig;

/**
 * app配置信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppInfoConfigService {
	static class AppInfoConfigCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppInfoConfigCriteria nameLike(String name) {
			params.put(1, name);
			return this;
		}

		public AppInfoConfigCriteria typeEquaTo(Integer type) {
			params.put(2, type);
			return this;
		}

		public AppInfoConfigCriteria stateEquaTo(Integer state) {
			params.put(3, state);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	public PaginationVo<AppInfoConfig> searchAppInfoConfig(HashMap<String, Object> map, int currentPage, int pageSize);

	public List<AppInfoConfig> searchAppInfoConfig(AppInfoConfigCriteria criteria);

	// app配置信息
	public List<AppInfoConfig> getAppInfoConfig();

	// 查看单个app配置信息
	public AppInfoConfig getAppInfoConfig(int id);

	// 增加app配置信息
	public void addAppInfoConfig(AppInfoConfig record);

	// 更新app配置信息
	public void upAppInfoConfig(AppInfoConfig record);
	// appannie分发前修改status
	public void updateStatusBy(boolean status);
	
	// 删除app配置信息
	public void delAppInfoConfig(int id);

	
	public void deleteByTime(int appType,Date createTime);
	/**
	 * 根据名字删除对应app配置信息
	 */
	int deleteByName(String name);

	void batchDelete(Integer[] ids);

	public AppInfoConfig getAppConfig(String name, int type);

}
