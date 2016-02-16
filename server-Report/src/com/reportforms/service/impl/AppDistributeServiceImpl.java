package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.AppDistributeMapper;
import com.reportforms.model.AppDistribute;
import com.reportforms.service.AppDistributeService;

@Service
public class AppDistributeServiceImpl extends BaseServiceImpl<AppDistribute> implements
		AppDistributeService<AppDistribute> {
	
	@Autowired
	private AppDistributeMapper<AppDistribute> appDistributeMapper;
	
	@Override
	public List<AppDistribute> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return appDistributeMapper.query(paginationBean,new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return appDistributeMapper.queryAllCounts(paginationBean);
	}

}
