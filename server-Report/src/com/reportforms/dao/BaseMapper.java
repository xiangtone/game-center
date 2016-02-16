package com.reportforms.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.reportforms.common.page.PaginationBean;

public abstract interface BaseMapper<T,PK extends Serializable> {
	
	public List<T> query(PaginationBean paginationBean,RowBounds rowBounds);
	
	public List<T> query(PaginationBean paginationBean);
	
	public Integer queryAllCounts(PaginationBean paginationBean);
	
	public List<T> queryByGroupBy(PaginationBean paginationBean,RowBounds rowBounds);
	
	public Integer queryByGroupByCounts(PaginationBean paginationBean);
	
	public T queryByTotal(PaginationBean paramsBean);

}
