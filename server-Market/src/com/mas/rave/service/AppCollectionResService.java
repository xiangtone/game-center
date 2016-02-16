package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppAlbumRes;

/**
 * app分发内容信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppCollectionResService {
	static class AppCollectionResCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppCollectionResCriteria columnIdEqualTo(Integer columnId) {
			params.put(1, columnId);
			return this;
		}

		public AppCollectionResCriteria nameLike(String name) {
			params.put(2, name);
			return this;
		}

		public AppCollectionResCriteria sourceEqualTo(int source) {
			params.put(3, source);
			return this;
		}
		public AppCollectionResCriteria raveIdEqualTo(int raveId) {
			params.put(4, raveId);
			return this;
		}
		public AppCollectionResCriteria collectionIdEqualTo(int collectionId) {
			params.put(5, collectionId);
			return this;
		}
		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	public PaginationVo<AppAlbumRes> searchAlbumRess(AppCollectionResCriteria criteria, int currentPage, int pageSize);
	
	//liwe 2015-06-16 修改排序
	public PaginationVo<AppAlbumRes> searchAlbumRess1(AppCollectionResCriteria criteria, int currentPage, int pageSize);

	
	//获取所属类别生效数据
	public List<String> selectByColumnId(Integer columnId,Integer raveId);
	// app分发信息
	public List<AppAlbumRes> getAppAlbumRes();
	public List<AppAlbumRes> getAppAlbumRes(AppCollectionResCriteria criteria);
	// 根据columnId查看app分发信息
	public List<AppAlbumRes> getAppAlbumResByColumn(int columnId);

	public AppAlbumRes selectByPrimaryKey(long id);
	// 增加app分发信息
	public void addAppAlbumRes(AppAlbumRes record);

	// 更新app分发信息
	public void upAppAlbumRes(AppAlbumRes record);

	// 删除app分发信息
	public void delAppAlbumRes(int id);

	void batchDelete(Integer[] ids);

	/**
	 * 根据id删除对应app内容信息
	 */
	public int deleteByApp(Integer appId, Integer apkId);

	// 批量更新
	public void resetAppAlbumRes(Integer raveId, Integer collectionId,Integer columnId,Integer albumId, List<Integer> ids);

	// 更新排序
	public void updateSortByPrimarykey(AppAlbumRes entity);

	// 获取app信息
	public AppAlbumRes getAppAlbumRes(int id, int type);

	public PaginationVo<AppAlbumRes> getSelectAppFiles(HashMap<String, Object> map, int currentPage, int pageSize);
}
