package com.mas.rave.service;

import java.util.List;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppAlbumStatistics;

public interface AppAlbumStatisticsService {

	/**
	 * 分页查询
	 */
	public PaginationVo<AppAlbumStatistics> selectByExample(AppAlbumStatistics appAlbumStatistics,int currentPage, int pageSize);

	/**
	 * 分页查询
	 */
	public List<AppAlbumStatistics> selectByExample(AppAlbumStatistics appAlbumStatistics);

	/**
	 * 根据添加查找
	 */
	public AppAlbumStatistics selectByCondition(AppAlbumStatistics appAlbumStatistics);	
	/**
	 * 增加
	 */
	public int insert(AppAlbumStatistics appAlbumStatistics);	
	/**
	 * 更新
	 */
	public int update(AppAlbumStatistics appAlbumStatistics);
	
	/**
	 * 根据条件删除
	 */
	public int deleteByCondition(AppAlbumStatistics appAlbumStatistics);



}
