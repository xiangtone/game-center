package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppCollection;
import com.mas.rave.main.vo.AppCollectionExample;

/**
 * app主题数据访问接口
 * 
 * @author jieding
 * 
 */
public interface AppCollectionService {
	static class AppCollectionCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppCollectionCriteria nameLike(String name) {
			params.put(1, name);
			return this;
		}
		public AppCollectionCriteria nameCnLike(String nameCn) {
			params.put(2, nameCn);
			return this;
		}
		public AppCollectionCriteria adnState(Integer state) {
			params.put(3, state);
			return this;
		}
		public AppCollectionCriteria raveIdEqual(Integer raveId) {
			params.put(4, raveId);
			return this;
		}
		public AppCollectionCriteria typeEqual(Integer type) {
			params.put(5, type);
			return this;
		}
		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	/**
	 * 分页显示app主题信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<AppCollection> searchAppCollections(AppCollectionCriteria criteria, int currentPage, int pageSize);
	
	/**
	 * 2015-06-16 liwei 修改排序
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<AppCollection> searchAppCollections1(AppCollectionCriteria criteria, int currentPage, int pageSize);


	public List<AppCollection> searchAppCollection(AppCollectionExample example);

	// 查看单个app主题信息
	public AppCollection getAppCollection(long id);
	/**
	 * 根据name查看app主题信息
	 */
	public List<AppCollection>  selectByName(AppCollection record);


	// 增加app主题信息
	public void addAppCollection(AppCollection appCollection);


	// 更新app主题信息
	public void upAppCollection(AppCollection appCollection);

	// 删除app主题信息
	public void delAppCollection(Integer id);

	void batchDelete(Integer[] ids);

	/**
	 * 根据id更新排序信息
	 * @param appCollection
	 */
	public void updateSortByPrimarykey(AppCollection appCollection);
	
}
