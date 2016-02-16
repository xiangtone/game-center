package com.reportforms.service;

import java.util.List;

import com.reportforms.common.page.PaginationBean;

public interface AppSearchLogService<T> extends BaseService<T, Long> {
	
	public List<T> queryByGroupByContent(PaginationBean paginationBean);
	
	public Integer queryByGroupByContentCounts(PaginationBean paginationBean);

}
