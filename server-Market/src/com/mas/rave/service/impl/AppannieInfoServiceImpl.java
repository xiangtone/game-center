package com.mas.rave.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppannieInfoMapper;
import com.mas.rave.main.vo.AppannieInfo;
import com.mas.rave.service.AppannieInfoService;
@Service
public class AppannieInfoServiceImpl implements AppannieInfoService {

	@Autowired
	private AppannieInfoMapper appannieInfoMapper;

	@Override
	public PaginationVo<AppannieInfo> selectByExample(AppannieInfo criteria,
			int currentPage, int pageSize) {
		int recordCount = appannieInfoMapper.countByExample(criteria);
		criteria.setCurrentPage((currentPage - 1) * pageSize);
		criteria.setPageSize(pageSize);
		List<AppannieInfo> data = appannieInfoMapper.selectByExample(criteria);
		PaginationVo<AppannieInfo> result = new PaginationVo<AppannieInfo>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public AppannieInfo selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appannieInfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appannieInfoMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AppannieInfo criteria) {
		// TODO Auto-generated method stub
		return appannieInfoMapper.insert(criteria);
	}

	@Override
	public List<AppannieInfo> selectByCondition(AppannieInfo appannieInfo) {
		// TODO Auto-generated method stub
		return appannieInfoMapper.selectByCondition(appannieInfo);
	}

	@Override
	public List<AppannieInfo> selectByCreateTime(Date createTime) {
		// TODO Auto-generated method stub
		return appannieInfoMapper.selectByCreateTime(createTime);
	}

	@Override
	public int deleteByCondition(AppannieInfo appannieInfo) {
		// TODO Auto-generated method stub
		return appannieInfoMapper.deleteByCondition(appannieInfo);
	}

	@Override
	public void batchDel(Integer[] ids) {
		for(Integer id:ids){
			deleteByPrimaryKey(id);
		}
		
	}
	

}
