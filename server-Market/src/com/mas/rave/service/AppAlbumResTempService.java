package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.service.AppAlbumResService.AppAlbumResCriteria;

/**
 * app分发内容信息数据访问接口
 * 
 * @author jieding
 * 
 */
public interface AppAlbumResTempService {
	static class AppAlbumResTempCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppAlbumResTempCriteria columnIdEqualTo(Integer columnId) {
			params.put(1, columnId);
			return this;
		}

		public AppAlbumResTempCriteria nameLike(String name) {
			params.put(2, name);
			return this;
		}

		public AppAlbumResTempCriteria sourceEqualTo(int source) {
			params.put(3, source);
			return this;
		}

		public AppAlbumResTempCriteria raveIdEqualTo(int raveId) {
			params.put(4, raveId);
			return this;
		}
		
		public AppAlbumResTempCriteria flagEqualTo(int flag) {
			params.put(5, flag);
			return this;
		}
		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	public PaginationVo<AppAlbumRes> searchAlbumResTemps(AppAlbumResTempCriteria criteria, int currentPage, int pageSize);
	
	public List<AppAlbumRes> getAppAlbumResTemp(AppAlbumResTempCriteria criteria);

	// app分发信息
	public List<AppAlbumRes> getAppAlbumResTemp();

	// 查看单个app分发信息
	public AppAlbumRes getAppAlbumResTemp(int id);

	// 增加app分发信息
	public void addAppAlbumResTemp(AppAlbumRes record);

	// 更新app分发信息
	public void upAppAlbumResTemp(AppAlbumRes record);

	// 删除app分发信息
	public void delAppAlbumResTemp(int id);

	public void deleteBySource(Integer albumId, Integer columnId, int raveId, int source);

	void batchDelete(Integer[] ids);

	/**
	 * 根据id删除对应app内容信息
	 */
	public int deleteByApp(Integer appId, Integer apkId);

	// 批量更新
	public void resetAppAlbumResTemp(Integer raveId, Integer columnId, Integer albumId, Integer source, List<Integer> ids);

	// 更新排序
	public void updateSortByPrimarykey(AppAlbumRes entity);

	// 更新手动分发排名
	public void updateRankingByPrimarykey(AppAlbumRes entity);

	// 获取app信息
	public AppAlbumRes getAppAlbumResTemp(int id, int type);

	public void doOperant(AppAlbumRes appAlbumRes);

	public void doBatchOperant(Integer[] ids, Integer columnId);

	public List<AppAlbumRes> getByFileColumnAndSource(AppInfo appInfo, AppAlbumColumn appAlbumColumn, Integer source, Integer raveId);

	public PaginationVo<AppAlbumRes> getSelectAppFiles(HashMap<String, Object> map, int currentPage, int pageSize);

	public List<Integer> getids();

	/**
	 * 获取已经存在的列表
	 * 
	 * @param criteria
	 * @return
	 */
	public AppAlbumRes getApkRes(Integer raveId, Integer columnId, Integer apkId);
}
