package com.reportforms.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.reportforms.common.page.PaginationBean;

public interface AppSearchLogMapper<T> extends BaseMapper<T, Long> {

	public List<T> queryByGroupByContent(PaginationBean paginationBean,RowBounds rowBounds);
	
	public Integer queryByGroupByContentCounts(PaginationBean paginationBean);
}
