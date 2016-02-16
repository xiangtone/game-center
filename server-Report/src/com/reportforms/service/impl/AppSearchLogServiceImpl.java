package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.AppSearchLogMapper;
import com.reportforms.model.AppSearchLog;
import com.reportforms.service.AppSearchLogService;

@Service
public class AppSearchLogServiceImpl extends BaseServiceImpl<AppSearchLog> implements
		AppSearchLogService<AppSearchLog> {
	
	@Autowired
	private AppSearchLogMapper<AppSearchLog> appSearchLogMapper;
	
	@Override
	public List<AppSearchLog> query(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return appSearchLogMapper.query(paramsBean,new RowBounds(paramsBean.getStart(), paramsBean.getLimit()));
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return appSearchLogMapper.queryAllCounts(paramsBean);
	}
	
	@Override
	public List<AppSearchLog> queryByGroupBy(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return appSearchLogMapper.queryByGroupBy(paramsBean, new RowBounds(paramsBean.getStart(), paramsBean.getLimit()));
	}
	
	@Override
	public Integer queryByGroupByCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return appSearchLogMapper.queryByGroupByCounts(paginationBean);
	}
	
	@Override
	public List<AppSearchLog> queryByGroupByContent(
			PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return appSearchLogMapper.queryByGroupByContent(paramsBean, new RowBounds(paramsBean.getStart(), paramsBean.getLimit()));
	}
	
	@Override
	public Integer queryByGroupByContentCounts(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return appSearchLogMapper.queryByGroupByContentCounts(paramsBean);
	}
}
