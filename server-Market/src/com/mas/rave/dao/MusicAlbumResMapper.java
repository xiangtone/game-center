package com.mas.rave.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.main.vo.MusicAlbumRes;

public interface MusicAlbumResMapper {
	
	public List<MusicAlbumRes> query(PaginationBean params,RowBounds rowBounds);
	
	public Integer queryCounts(PaginationBean params);
	
	public void deleteById(PaginationBean params);
	
	public void deleteByMusicId(int musicId);
	
	public void updateById(MusicAlbumRes musicAlbumRes);
	
	public void insert(Map<String, Object> map);
	/**
	 * 根据musicId查询  add by jieding
	 * @param musicId
	 * @return
	 */
	public List<MusicAlbumRes> selectByMusicId(int musicId);
	/**
	 * 根据themeId查询 add by jieding
	 * @param themeId
	 * @return
	 */
	public List<MusicAlbumRes> selectByThemeId(int themeId);
	
	public void updateByPrimaryKey(MusicAlbumRes musicAlbumRes);
	
}
