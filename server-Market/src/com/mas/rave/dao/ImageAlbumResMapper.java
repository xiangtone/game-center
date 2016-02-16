package com.mas.rave.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.common.page.PaginationBean;

public interface ImageAlbumResMapper<T> {

	public List<T> query(PaginationBean params,RowBounds rowBounds);
	
	public Integer queryCounts(PaginationBean params);
	
	public void deleteById(PaginationBean params);
	
	public void deleteByImageId(int imageId);
	
	public void updateById(T entity);
	
	public void insert(Map<String, Object> map);
	/**
	 * 根据imageId查询  add by jieding
	 * @param imageId
	 * @return
	 */
	public List<T> selectByImageId(int imageId);
	/**
	 * 根据themeId 查询 add by jieding
	 * @param themeId
	 * @return
	 */
	public List<T>  selectByThemeId(int themeId);
	
	public void updateByPrimaryKey(T entity);
	
}
