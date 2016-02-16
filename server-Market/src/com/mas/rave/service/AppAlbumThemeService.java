package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppAlbumTheme;

/**
 * app主题信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppAlbumThemeService {
	static class AppAlbumThemeCriteria {
		private Map<Integer, Object> params = new HashMap<Integer, Object>();

		public AppAlbumThemeCriteria nameLike(String name) {
			params.put(1, name);
			return this;
		}

		public AppAlbumThemeCriteria andAlbumId(Integer value) {
			params.put(2, value);
			return this;
		}

		public AppAlbumThemeCriteria appAlbumColumnIdEqualTo(int appAlbumColumnId) {
			params.put(4, appAlbumColumnId);
			return this;
		}

		public AppAlbumThemeCriteria appAlbumIdEqualTo(int[] appAlbumColumnIds) {
			params.put(5, appAlbumColumnIds);
			return this;
		}

		public AppAlbumThemeCriteria raveIdEqual(Integer raveId) {
			params.put(6, raveId);
			return this;
		}

		public AppAlbumThemeCriteria flagEqual(Integer flag) {
			params.put(7, flag);
			return this;
		}

		public Map<Integer, Object> getParams() {
			return Collections.unmodifiableMap(params);
		}
	}

	public PaginationVo<AppAlbumTheme> searchAppAlbumTheme(AppAlbumThemeCriteria criteria, int currentPage, int pageSize,int flag);

	// app主题信息
	public List<AppAlbumTheme> getAppAlbumTheme();

	// 查看单个app主题信息
	public AppAlbumTheme getAppAlbumTheme(int id);

	/**
	 * 根据name查看app主题信息
	 */
	List<AppAlbumTheme> selectByThemeName(String name);

	/**
	 * 根据nameCn查看app主题信息
	 */
	List<AppAlbumTheme> selectByThemeNameCn(String nameCn);

	// 增加app主题信息
	public void addAppAlbumTheme(AppAlbumTheme record);

	// 更新app主题信息
	public void upAppAlbumTheme(AppAlbumTheme record);

	// 删除app主题信息
	public void delAppAlbumTheme(int id);

	void batchDelete(Integer[] ids);

	public void updateSortByPrimarykey(AppAlbumTheme appAlbumTheme);

	/**
	 * 获取所有开发者banner图上
	 * 
	 * @param falg
	 * @return
	 */
	public List<AppAlbumTheme> getBanners(int falg);
	
	/**
	 * 获取开者者banner图
	 * 
	 * @param id
	 * @return
	 */
	public AppAlbumTheme getappBanner(int id);
}
