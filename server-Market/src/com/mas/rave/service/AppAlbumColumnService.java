package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppAlbumColumn;

/**
 * app专辑信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppAlbumColumnService {
	static class AppAlbumColumnCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppAlbumColumnCriteria nameLike(String name) {
			params.put(1, name);
			return this;
		}

		public AppAlbumColumnCriteria albumIdEquaTo(Integer albumId) {
			params.put(2, albumId);
			return this;
		}

		public AppAlbumColumnCriteria flagEquaTo(Integer flag) {
			params.put(3, flag);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	public PaginationVo<AppAlbumColumn> searchAppAlbumColumn(AppAlbumColumnCriteria criteria, int currentPage, int pageSize);

	public List<AppAlbumColumn> searchAppAlbumColumn(AppAlbumColumnCriteria criteria);

	// app专辑信息
	public List<AppAlbumColumn> getAppAlbumColumn();

	// 查看单个app专辑信息
	public AppAlbumColumn getAppAlbumColumn(int id);

	// 增加app专辑信息
	public void addAppAlbumColumn(AppAlbumColumn record);

	// 更新app专辑信息
	public void upAppAlbumColumn(AppAlbumColumn record);

	// 只更新排序字段
	public void updateSortByPrimaryKey(AppAlbumColumn entity);

	// 删除app专辑信息
	public void delAppAlbumColumn(int id);

	void batchDelete(Integer[] ids);

	// 根据app顶级分类查找app专辑信息
	public List<AppAlbumColumn> getAppAlbumColumnsByAppAlbumId(int appAlbumId);


}
