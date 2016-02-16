package com.mas.rave.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppAlbumStatisticsMapper;
import com.mas.rave.main.vo.AppAlbumStatistics;
import com.mas.rave.service.AppAlbumStatisticsService;
@Service
public class AppAlbumStatisticsServiceImpl implements AppAlbumStatisticsService {

	@Autowired
	private AppAlbumStatisticsMapper appAlbumStatisticsMapper;
	@Override
	public PaginationVo<AppAlbumStatistics> selectByExample(
			AppAlbumStatistics appAlbumStatistics,int currentPage, int pageSize) {
		appAlbumStatistics.setCurrentPage(currentPage);
		appAlbumStatistics.setPageSize(pageSize);
		int recordCount = appAlbumStatisticsMapper.countByExample(appAlbumStatistics);
		List<AppAlbumStatistics> data = appAlbumStatisticsMapper.selectByExample(appAlbumStatistics);
		PaginationVo<AppAlbumStatistics> result = new PaginationVo<AppAlbumStatistics>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public List<AppAlbumStatistics> selectByExample(
			AppAlbumStatistics appAlbumStatistics) {
		// TODO Auto-generated method stub
		return appAlbumStatisticsMapper.selectByExample(appAlbumStatistics);
	}

	@Override
	public AppAlbumStatistics selectByCondition(
			AppAlbumStatistics appAlbumStatistics) {
		// TODO Auto-generated method stub
		return appAlbumStatisticsMapper.selectByCondition(appAlbumStatistics);
	}

	@Override
	public int insert(AppAlbumStatistics appAlbumStatistics) {
		// TODO Auto-generated method stub
		return appAlbumStatisticsMapper.insert(appAlbumStatistics);
	}

	@Override
	public int update(AppAlbumStatistics appAlbumStatistics) {
		// TODO Auto-generated method stub
		return appAlbumStatisticsMapper.update(appAlbumStatistics);
	}

	@Override
	public int deleteByCondition(AppAlbumStatistics appAlbumStatistics) {
		// TODO Auto-generated method stub
		return appAlbumStatisticsMapper.deleteByCondition(appAlbumStatistics);
	}

}
