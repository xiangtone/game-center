package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppAlbumStatistics;

/**
 * 应用排行统计
 * 
 * @author jieding
 * 
 */
public interface AppAlbumStatisticsMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(AppAlbumStatistics appAlbumStatistics);

	/**
	 * 根据条件删除
	 */
	int deleteByCondition(AppAlbumStatistics appAlbumStatistics);

	/**
	 * 增加
	 */
	int insert(AppAlbumStatistics appAlbumStatistics);

	/**
	 * 分页查询
	 */
	List<AppAlbumStatistics> selectByExample(AppAlbumStatistics appAlbumStatistics, RowBounds rowBounds);

	/**
	 * 分页查询
	 */
	List<AppAlbumStatistics> selectByExample(AppAlbumStatistics appAlbumStatistics);

	/**
	 * 根据添加查找
	 */
	AppAlbumStatistics selectByCondition(AppAlbumStatistics appAlbumStatistics);	
		
	/**
	 * 更新
	 */
	int update(AppAlbumStatistics appAlbumStatistics);
}