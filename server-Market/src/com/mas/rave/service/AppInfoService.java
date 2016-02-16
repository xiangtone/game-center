package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppInfo;

/**
 * app信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppInfoService {
	static class AppCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppCriteria nameLike(String name) {
			params.put(2, name);
			return this;
		}

		public AppCriteria adnState(Integer state) {
			params.put(3, state);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示app信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<AppInfo> searchApps(AppInfo criteria, int currentPage, int pageSize);

	public AppInfo searchApp(AppInfo criteria);

	public List<AppInfo> searchApps(AppInfo criteria);

	// 查看单个app信息
	public AppInfo getApp(long id);

	// 获取所有app信息
	public List<AppInfo> getAllAppInfos();

	// 增加app信息
	public int addApp(AppInfo app, Integer categoryId);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(AppInfo record);

	// 更新app信息
	public void upApp(AppInfo app, Integer categoryId);

	// 删除app信息
	public void delApp(Integer id);

	void batchDelete(Integer[] ids);
	// 更新app信息 t_appcollection_res add by dingjie
	public void upAppCollectionRes(AppInfo appInfo);
	// 更新app信息
	public void upAppAlbumRes(AppInfo appInfo);

	// 更新app分发信息temp表 add by dingjie
	public void upAppAlbumResTemp(AppInfo appInfo);

	public void upAppFile(AppInfo appInfo);

	public void upAppCountryScoreByAppId(AppInfo appInfo);


	/**
	 * 查找appinfo信息
	 * 
	 * @param fatherChannelId
	 *            父渠道名
	 * @param appName
	 *            应用名
	 * @return
	 */
	public AppInfo findAppInfo(int fatherChannelId, String appName);

	public int updateNum(int id);

	public int getAppInfoCountByCategory(int categoryId);

	// 检测cp是存在
	public int checkCp(Integer cpId);
	
	public void upFree(AppInfo appInfo);
	
	public List<AppInfo> selectADayAgoInfo();

}
