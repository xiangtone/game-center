package com.mas.rave.service;

import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.main.vo.MusicAlbumRes;

public interface MusicAlbumResService {
	
	public List<MusicAlbumRes> query(PaginationBean params);
	
	public Integer queryCounts(PaginationBean params);

	public void deleteById(PaginationBean params);
	
	public void updateById(MusicAlbumRes musicAlbumRes);
	
	public void insert(Map<String, Object> map);
}
