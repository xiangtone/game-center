package com.mas.rave.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppCountryScoreMapper;
import com.mas.rave.main.vo.AppCountryScore;
import com.mas.rave.service.AppCountryScoreService;
@Service
public class AppCountryScoreServiceImpl implements AppCountryScoreService {

	@Autowired
	private AppCountryScoreMapper appCountryScoreMapper;
	@Override
	public PaginationVo<AppCountryScore> selectByExample(AppCountryScore appCountryScore, int currentPage, int pageSize) {
		appCountryScore.setCurrentPage((currentPage - 1) * pageSize);
		appCountryScore.setPageSize(pageSize);
		List<AppCountryScore> data = appCountryScoreMapper.selectByExample(appCountryScore);
		int recordCount = appCountryScoreMapper.countByExample(appCountryScore);
		PaginationVo<AppCountryScore> result = new PaginationVo<AppCountryScore>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public AppCountryScore selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appCountryScoreMapper.selectByPrimaryKey(id);
	}

	@Override
	public void deleteByPrimaryKey(Integer id) {
		appCountryScoreMapper.deleteByPrimaryKey(id);

	}

	@Override
	public void deleteByCondition(AppCountryScore appCountryScore) {
		appCountryScoreMapper.deleteByCondition(appCountryScore);
	}

	@Override
	public void update(AppCountryScore appCountryScore) {
		appCountryScoreMapper.update(appCountryScore);

	}

	@Override
	public void updateByState(AppCountryScore appCountryScore) {
		appCountryScoreMapper.updateByState(appCountryScore);

	}

	@Override
	public void insert(AppCountryScore appCountryScore) {
		appCountryScoreMapper.insert(appCountryScore);

	}

	@Override
	public List<AppCountryScore> selectByCondition(
			AppCountryScore appCountryScore) {
		// TODO Auto-generated method stub
		return appCountryScoreMapper.selectByCondition(appCountryScore);
	}

	@Override
	public void batchDelete(Integer[] ids) {
		for(Integer id:ids){
			deleteByPrimaryKey(id);
		}
		
	}

	@Override
	public PaginationVo<AppCountryScore> getSelectAppFiles(
			HashMap<String, Object> map, int currentPage, int pageSize) {
		map.put("index", (currentPage - 1) * pageSize);
		map.put("pageSize", pageSize);
		Integer count = appCountryScoreMapper.getSelectAppFilesCount(map);
		List<AppCountryScore> apks = appCountryScoreMapper.getSelectAppFiles(map);
		return new PaginationVo<AppCountryScore>(apks, count, pageSize, currentPage);
	}

	@Override
	public void insertSelectApps(Integer raveId, List<Integer> ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("raveId", raveId);
		map.put("list", ids);
		appCountryScoreMapper.insertSelectApps(map);
		
	}

	@Override
	public void insertByApp(Integer raveId, Integer albumId, Integer columnId, List<Integer> ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("raveId", raveId);
		map.put("albumId", albumId);
		map.put("columnId", columnId);
		map.put("list", ids);
		appCountryScoreMapper.insertByApp(map);
		
	}

}
