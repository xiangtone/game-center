package com.mas.rave.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppannieInfoCountryRankMapper;
import com.mas.rave.main.vo.AppannieInfoCountryRank;
import com.mas.rave.service.AppannieInfoCountryRankService;
@Service
public class AppannieInfoCountryRankServiceImpl implements AppannieInfoCountryRankService{

	@Autowired
	private AppannieInfoCountryRankMapper appannieInfoCountryRankMapper;
	
	@Override
	public PaginationVo<AppannieInfoCountryRank> selectByExample(
			AppannieInfoCountryRank criteria, int currentPage, int pageSize) {
		int recordCount = appannieInfoCountryRankMapper.countByExample(criteria);
		criteria.setCurrentPage((currentPage - 1) * pageSize);
		criteria.setPageSize(pageSize);
		List<AppannieInfoCountryRank> data = appannieInfoCountryRankMapper.selectByExample(criteria);
		PaginationVo<AppannieInfoCountryRank> result = new PaginationVo<AppannieInfoCountryRank>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public AppannieInfoCountryRank selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appannieInfoCountryRankMapper.selectByPrimaryKey(id);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appannieInfoCountryRankMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AppannieInfoCountryRank criteria) {
		// TODO Auto-generated method stub
		return appannieInfoCountryRankMapper.insert(criteria);
	}

	@Override
	public List<AppannieInfoCountryRank> selectByCondition(
			AppannieInfoCountryRank criteria) {
		// TODO Auto-generated method stub
		return appannieInfoCountryRankMapper.selectByCondition(criteria);
	}

	@Override
	public List<AppannieInfoCountryRank> selectByCreateTime(Date createTime) {
		// TODO Auto-generated method stub
		return appannieInfoCountryRankMapper.selectByCreateTime(createTime);
	}

	@Override
	public int deleteByCondition(AppannieInfoCountryRank criteria) {
		// TODO Auto-generated method stub
		return appannieInfoCountryRankMapper.deleteByCondition(criteria);
	}

	@Override
	public void batchDel(Integer[] ids) {
		for(Integer id:ids){
			deleteByPrimaryKey(id);
		}
		
	}

	@Override
	public int updateStatusBy(AppannieInfoCountryRank criteria) {
		// TODO Auto-generated method stub
		return appannieInfoCountryRankMapper.updateStatusBy(criteria);
	}

	@Override
	public int update(AppannieInfoCountryRank criteria) {
		// TODO Auto-generated method stub
		return appannieInfoCountryRankMapper.update(criteria);
	}

}
