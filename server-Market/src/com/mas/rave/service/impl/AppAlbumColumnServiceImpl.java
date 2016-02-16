package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppAlbumColumnMapper;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumColumnExample;
import com.mas.rave.service.AppAlbumColumnService;
import com.mas.rave.util.FileUtil;

/**
 * app对应主题
 * 
 * @author liwei.sz
 * 
 */
@Service
public class AppAlbumColumnServiceImpl implements AppAlbumColumnService {

	@Autowired
	private AppAlbumColumnMapper appAlbumColumnMapper;

	public PaginationVo<AppAlbumColumn> searchAppAlbumColumn(AppAlbumColumnCriteria criteria, int currentPage, int pageSize) {
		AppAlbumColumnExample example = new AppAlbumColumnExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				example.createCriteria().andNameLike(params.get(1).toString(), 1);
			} else if (key.equals(2)) {
				example.createCriteria().andAlbumId(Integer.parseInt(params.get(2).toString()), 1);
			} else if (key.equals(3)) {
				// System.out.println(params.get(3));
				example.createCriteria().andFlag(Integer.parseInt(params.get(3).toString()));
			}
		}
		// example.setOrderByClause("createTime desc");
		List<AppAlbumColumn> data = appAlbumColumnMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = appAlbumColumnMapper.countByExample(example);
		PaginationVo<AppAlbumColumn> result = new PaginationVo<AppAlbumColumn>(data, recordCount, pageSize, currentPage);
		return result;
	}

	public List<AppAlbumColumn> searchAppAlbumColumn(AppAlbumColumnCriteria criteria) {
		AppAlbumColumnExample example = new AppAlbumColumnExample();
		Map<Integer, Object> params = criteria.getParams();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				example.createCriteria().andNameLike(params.get(1).toString(), 1);
			} else if (key.equals(2)) {
				example.createCriteria().andAlbumId(Integer.parseInt(params.get(2).toString()), 1);
			}
		}
		example.setOrderByClause("createTime desc");
		return appAlbumColumnMapper.selectByExample(example);
	}

	// app主题信息
	public List<AppAlbumColumn> getAppAlbumColumn() {
		AppAlbumColumnExample example = new AppAlbumColumnExample();
		return appAlbumColumnMapper.selectByExample(example);
	}

	// 查看单个app主题信息
	public AppAlbumColumn getAppAlbumColumn(int id) {
		return appAlbumColumnMapper.selectByPrimaryKey(id);
	}

	// 增加app主题信息
	public void addAppAlbumColumn(AppAlbumColumn record) {
		appAlbumColumnMapper.insert(record);
	}

	// 更新app主题信息
	public void upAppAlbumColumn(AppAlbumColumn record) {
		appAlbumColumnMapper.updateByPrimaryKey(record);
	}

	// 删除app主题信息
	public void delAppAlbumColumn(int id) {
		// 设置删除文件路径
		AppAlbumColumn appAlbumColumn = appAlbumColumnMapper.selectByPrimaryKey(id);
		String bigUrl = null;
		String smallUrl = null;
		if (appAlbumColumn != null) {
			bigUrl = appAlbumColumn.getBigicon();
			smallUrl = appAlbumColumn.getIcon();
		}
		appAlbumColumnMapper.deleteByPrimaryKey(id);
		try {
			// 删除对应文件
			FileUtil.deleteFile(bigUrl);
			FileUtil.deleteFile(smallUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppAlbumColumn(id);
		}
	}

	@Override
	public void updateSortByPrimaryKey(AppAlbumColumn entity) {
		// TODO Auto-generated method stub
		appAlbumColumnMapper.updateSortByPrimaryKey(entity);
	}

	@Override
	public List<AppAlbumColumn> getAppAlbumColumnsByAppAlbumId(int appAlbumId) {
		// TODO Auto-generated method stub
		return appAlbumColumnMapper.getAppAlbumColumnsByAppAlbumId(appAlbumId);
	}
}
