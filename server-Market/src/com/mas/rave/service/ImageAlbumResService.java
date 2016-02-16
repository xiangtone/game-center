package com.mas.rave.service;

import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationBean;

public interface ImageAlbumResService<T> {
	
	public List<T> query(PaginationBean params);
	
	public Integer queryCounts(PaginationBean params);

	public void deleteById(PaginationBean params);
	
	public void updateById(T entity);
	
	public void insert(Map<String, Object> map);

}
