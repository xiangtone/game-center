package com.mas.rave.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.dao.AppAlbumMapper;
import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumExample;
import com.mas.rave.service.AppAlbumService;

/**
 * 分发
 * @author liwei.sz
 *
 */
@Service
public class AppAlbumServiceImpl implements AppAlbumService {

	@Autowired
	private AppAlbumMapper appAlbumMapper;

	// app分发信息
	public List<AppAlbum> getAppAlbum() {
		AppAlbumExample example = new AppAlbumExample();
		return appAlbumMapper.selectByExample(example);
	}

	// 查看单个app分发信息
	public AppAlbum getAppAlbum(int id) {
		return appAlbumMapper.selectByPrimaryKey(id);
	}

	// 增加app分发信息
	public void addAppAlbum(AppAlbum record) {
		appAlbumMapper.insert(record);
	}

	// 更新app分发信息
	public void upAppAlbum(AppAlbum record) {
		appAlbumMapper.updateByPrimaryKey(record);
	}

	// 删除app分发信息
	public void delAppAlbum(int id) {
		appAlbumMapper.deleteByPrimaryKey(id);
	}

	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppAlbum(id);
		}
	}

}
