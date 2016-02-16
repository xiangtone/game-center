package com.mas.rave.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppInfoConfigMapper;
import com.mas.rave.dao.AppInfoUrlMapper;
import com.mas.rave.main.vo.AppInfoConfig;
import com.mas.rave.main.vo.AppInfoConfigExample;
import com.mas.rave.main.vo.AppInfoUrl;
import com.mas.rave.service.AppInfoConfigService;

/**
 * app对应配置
 * 
 * @author liwei.sz
 * 
 */
@Service
public class AppInfoConfigServiceImpl implements AppInfoConfigService {

	@Autowired
	private AppInfoConfigMapper appInfoConfigMapper;
	@Autowired
	private AppInfoUrlMapper appInfoUrlMapper;
	Logger log = Logger.getLogger(AppInfoConfigServiceImpl.class);

	public PaginationVo<AppInfoConfig> searchAppInfoConfig(HashMap<String, Object> map, int currentPage, int pageSize) {
		map.put("currentPage", (currentPage - 1) * pageSize);
		map.put("pageSize", pageSize);
		List<AppInfoConfig> data = appInfoConfigMapper.selectByExample(map);
		int recordCount = appInfoConfigMapper.countByExample(map);
		PaginationVo<AppInfoConfig> result = new PaginationVo<AppInfoConfig>(data, recordCount, pageSize, currentPage);
		return result;
	}

	public List<AppInfoConfig> searchAppInfoConfig(AppInfoConfigCriteria criteria) {
		AppInfoConfigExample example = new AppInfoConfigExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				example.createCriteria().andNameLike(params.get(1).toString());
			}
		}
		example.setOrderByClause("createTime desc");
		return appInfoConfigMapper.selectByExample(example);
	}

	// app配置信息
	public List<AppInfoConfig> getAppInfoConfig() {
		AppInfoConfigExample example = new AppInfoConfigExample();
		return appInfoConfigMapper.selectByExample(example);
	}

	// 查看单个app配置信息
	public AppInfoConfig getAppInfoConfig(int id) {
		return appInfoConfigMapper.selectByPrimaryKey(id);
	}

	// 增加app配置信息
	public void addAppInfoConfig(AppInfoConfig record) {
		if(record.getType()==3&&record.getAppUrl()!=null&&!record.getAppUrl().equals("")){
			AppInfoUrl appInfoUrl = appInfoUrlMapper.selectByName(record.getName());
			AppInfoUrl	appInfoUrl1 = new AppInfoUrl();
			appInfoUrl1.setName(record.getName());
			appInfoUrl1.setAppUrl(record.getAppUrl());
			if(appInfoUrl!=null){			
				appInfoUrlMapper.updateByPrimaryKey(appInfoUrl1);
			}else{
				appInfoUrlMapper.insert(appInfoUrl1);
			}	
		}
		appInfoConfigMapper.insert(record);
	}

	// 更新app配置信息
	public void upAppInfoConfig(AppInfoConfig record) {
		if(record.getType()==3&&record.getAppUrl()!=null&&!record.getAppUrl().equals("")){
			AppInfoUrl appInfoUrl = appInfoUrlMapper.selectByName(record.getName());
			AppInfoUrl	appInfoUrl1 = new AppInfoUrl();
			appInfoUrl1.setName(record.getName());
			appInfoUrl1.setAppUrl(record.getAppUrl());
			if(appInfoUrl!=null){			
				appInfoUrlMapper.updateByPrimaryKey(appInfoUrl1);
			}else{
				appInfoUrlMapper.insert(appInfoUrl1);
			}	
		}
		appInfoConfigMapper.updateByPrimaryKey(record);

	}

	// 删除app配置信息
	public void delAppInfoConfig(int id) {
		appInfoConfigMapper.deleteByPrimaryKey(id);
	}

	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppInfoConfig(id);
		}
	}

	public AppInfoConfig getAppConfig(String name, int type) {
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		map.put("name", name);
		map.put("type", type);
		return appInfoConfigMapper.getAppConfig(map);
	}

	/**
	 * 根据名字删除对应app配置信息
	 */
	public int deleteByName(String name) {
		return appInfoConfigMapper.deleteByName(name);
	}

	@Override
	public void deleteByTime(int appType,Date createTime) {
		
		log.error("AppBatchDistributionTask  appInfoConfigService.deleteByTime   "+ appType+ " " + createTime);
		HashMap<String, Object> map = new HashMap<String, Object>(2);
		map.put("type", appType);
		map.put("createTime", createTime);
		appInfoConfigMapper.deleteByTime(map);
		
	}
	public void updateStatusBy(boolean status){
		appInfoConfigMapper.updateStatusBy(status);
	}
}
