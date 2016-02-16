package com.mas.rave.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.dao.MusicAlbumResMapper;
import com.mas.rave.main.vo.MusicAlbumRes;
import com.mas.rave.service.MusicAlbumResService;

@Service
public class MusicAlbumResServiceImpl implements MusicAlbumResService {
	
	@Autowired
	private MusicAlbumResMapper ringtonesMapper;

	@Override
	public List<MusicAlbumRes> query(PaginationBean params) {
		// TODO Auto-generated method stub
		return ringtonesMapper.query(params,new RowBounds(params.getStart(), params.getLimit()));
	}

	@Override
	public Integer queryCounts(PaginationBean params) {
		// TODO Auto-generated method stub
		return ringtonesMapper.queryCounts(params);
	}
	
	@Override
	public void deleteById(PaginationBean params) {
		// TODO Auto-generated method stub
		ringtonesMapper.deleteById(params);
	}
	
	@Override
	public void updateById(MusicAlbumRes musicAlbumRes) {
		// TODO Auto-generated method stub
		ringtonesMapper.updateById(musicAlbumRes);
	}
	
	@Override
	public void insert(Map<String, Object> map) {
		// TODO Auto-generated method stub
		ringtonesMapper.insert(map);
	}
}
