package com.mas.rave.service;

import java.io.Serializable;
import java.util.List;

import com.mas.rave.common.page.PaginationBean;

public abstract interface BaseService<T,PK extends Serializable> {
	
	public List<T> queryToExport(PaginationBean paginationBean);
	
	public List<T> query(PaginationBean paginationBean);
	
	public Integer queryAllCounts(PaginationBean paginationBean);
	
	public List<T> queryByGroupBy(PaginationBean paginationBean);
	
	public Integer queryByGroupByCounts(PaginationBean paginationBean);

}
