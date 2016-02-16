package com.reportforms.service.impl;

import java.util.List;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T, Long> {

	@Override
	public List<T> queryToExport(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryByGroupBy(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer queryByGroupByCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public T queryByTotal(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
