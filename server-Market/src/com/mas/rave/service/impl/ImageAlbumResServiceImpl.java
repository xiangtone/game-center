package com.mas.rave.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationBean;
import com.mas.rave.dao.ImageAlbumResMapper;
import com.mas.rave.main.vo.ImageAlbumRes;
import com.mas.rave.service.ImageAlbumResService;

@Service
public class ImageAlbumResServiceImpl implements ImageAlbumResService<ImageAlbumRes> {
	
	@Autowired
	private ImageAlbumResMapper<ImageAlbumRes> imageAlbumResMapper;

	@Override
	public List<ImageAlbumRes> query(PaginationBean params) {
		// TODO Auto-generated method stub
		return imageAlbumResMapper.query(params, new RowBounds(params.getStart(), params.getLimit()));
	}

	@Override
	public Integer queryCounts(PaginationBean params) {
		// TODO Auto-generated method stub
		return imageAlbumResMapper.queryCounts(params);
	}

	@Override
	public void deleteById(PaginationBean params) {
		// TODO Auto-generated method stub
		imageAlbumResMapper.deleteById(params);
	}

	@Override
	public void updateById(ImageAlbumRes entity) {
		// TODO Auto-generated method stub
		imageAlbumResMapper.updateById(entity);
	}

	@Override
	public void insert(Map<String, Object> map) {
		// TODO Auto-generated method stub
		imageAlbumResMapper.insert(map);
	}

}
