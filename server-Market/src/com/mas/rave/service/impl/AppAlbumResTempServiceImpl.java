package com.mas.rave.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppAlbumColumnMapper;
import com.mas.rave.dao.AppAlbumMapper;
import com.mas.rave.dao.AppAlbumResTempMapper;
import com.mas.rave.dao.AppFileMapper;
import com.mas.rave.dao.AppInfoMapper;
import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppAlbumResExample;
import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppInfo;
import com.mas.rave.service.AppAlbumResTempService;

/**
 * 分发
 * 
 * @author jieding
 * 
 */
@Service
public class AppAlbumResTempServiceImpl implements AppAlbumResTempService {

	@Autowired
	private AppAlbumResTempMapper appAlbumResTempMapper;

	@Autowired
	private AppAlbumColumnMapper appAlbumColumnMapper;

	@Autowired
	private AppAlbumMapper appAlbumMapper;

	@Autowired
	private AppInfoMapper appInfoMapper;

	@Autowired
	private AppFileMapper appFileMapper;

	public PaginationVo<AppAlbumRes> searchAlbumResTemps(AppAlbumResTempCriteria criteria, int currentPage, int pageSize) {
		AppAlbumResExample example = new AppAlbumResExample();
		AppAlbumResExample.Criteria criteria1 = example.createCriteria();
		Map<Integer, Object> params = criteria.getParams();
		Map<String, Object> map = new HashMap<String, Object>();

		if (params != null && params.size() > 0) {
			if (StringUtils.isNotEmpty(params.get(1).toString()) && params.get(2) != null) {
				// 根据名字查看
				criteria1 = criteria1.andNameLike(params.get(2).toString(), Integer.parseInt(params.get(1).toString()));
				map.put("appName", params.get(2).toString().trim());
				if (params.get(3) != null) {
					// 根据分发类型查询
					criteria1.andScourceEqual((int) params.get(3));
					if ((int) params.get(3) == 0) {// 手动分发排序
						example.setOrderByClause("ranking asc,createTime desc");
					} else {
						example.setOrderByClause("sort desc,source asc");
					}
				}
				if (params.get(4) != null) {
					// 根据国家ID查询
					criteria1.andRaveIdEqual((int) params.get(4),(int)params.get(5));
					if (example.getOrderByClause() == null || example.getOrderByClause().equals("")) {
						example.setOrderByClause("sort desc,source asc");
					}
				}
			} else if (StringUtils.isNotEmpty(params.get(1).toString())) {
				criteria1 = criteria1.columnId(Integer.parseInt(params.get(1).toString()));
				// 根据id查看
				if (params.get(3) != null) {
					// 根据分发类型查询
					criteria1.andScourceEqual((int) params.get(3));
					// 手动分发排序
					if ((int) params.get(3) == 0) {
						example.setOrderByClause("ranking asc,createTime desc");
					} else {
						example.setOrderByClause("sort desc,source asc");
					}
				}
				if (params.get(4) != null) {
					// 根据国家ID查询
					criteria1.andRaveIdEqual((int) params.get(4),(int)params.get(5));
					if (example.getOrderByClause() == null || example.getOrderByClause().equals("")) {
						example.setOrderByClause("sort desc,source asc");
					}
				}
			}
		}
		example.setMapOrderByClause(map);
		//
		List<AppAlbumRes> data = appAlbumResTempMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = appAlbumResTempMapper.countByExample(example);
		PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(data, recordCount, pageSize, currentPage);
		return result;
	}

	// 按条件查看app分发信息
	public List<AppAlbumRes> getAppAlbumResTemp(AppAlbumResTempCriteria criteria) {
		AppAlbumResExample example = new AppAlbumResExample();
		AppAlbumResExample.Criteria criteria1 = example.createCriteria();
		Map<Integer, Object> params = criteria.getParams();
		Map<String, Object> map = new HashMap<String, Object>();
		if (params != null && params.size() > 0) {
			if (StringUtils.isNotEmpty(params.get(1).toString()) && params.get(2) != null) {
				// 根据名字查看
				criteria1 = criteria1.andNameLike(params.get(2).toString(), Integer.parseInt(params.get(1).toString()));
				map.put("appName", params.get(2).toString().trim());
				if (params.get(3) != null) {
					// 根据分发类型查询
					criteria1.andScourceEqual((int) params.get(3));
					if ((int) params.get(3) == 0) {// 手动分发排序
						example.setOrderByClause("ranking asc,createTime desc");
					} else {
						example.setOrderByClause("sort desc,source asc");
					}
				}
				if (params.get(4) != null) {
					// 根据国家ID查询
					criteria1.andRaveIdEqual((int) params.get(4),0);
					if (example.getOrderByClause() == null || example.getOrderByClause().equals("")) {
						example.setOrderByClause("sort desc,source asc");
					}
				}
			} else if (StringUtils.isNotEmpty(params.get(1).toString())) {
				criteria1 = criteria1.columnId(Integer.parseInt(params.get(1).toString()));
				// 根据id查看
				if (params.get(3) != null) {
					// 根据分发类型查询
					criteria1.andScourceEqual((int) params.get(3));
					// 手动分发排序
					if ((int) params.get(3) == 0) {
						example.setOrderByClause("ranking asc,createTime desc");
					} else {
						example.setOrderByClause("sort desc,source asc");
					}
				}
				if (params.get(4) != null) {
					// 根据国家ID查询
					criteria1.andRaveIdEqual((int) params.get(4),0);
					if (example.getOrderByClause() == null || example.getOrderByClause().equals("")) {
						example.setOrderByClause("sort desc,source asc");
					}
				}
			}
			example.setMapOrderByClause(map);
		}
		return appAlbumResTempMapper.selectByExample(example);
	}

	@Override
	public PaginationVo<AppAlbumRes> getSelectAppFiles(HashMap<String, Object> map, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		map.put("index", (currentPage - 1) * pageSize);
		map.put("pageSize", pageSize);
		Integer count = appAlbumResTempMapper.getSelectAppFilesCount(map);
		List<AppAlbumRes> apks = appAlbumResTempMapper.getSelectAppFiles(map);
		return new PaginationVo<AppAlbumRes>(apks, count, pageSize, currentPage);
	}

	// app分发信息
	public List<AppAlbumRes> getAppAlbumResTemp() {
		AppAlbumResExample example = new AppAlbumResExample();
		return appAlbumResTempMapper.selectByExample(example);
	}

	// 查看单个app分发信息
	public AppAlbumRes getAppAlbumResTemp(int id) {
		return appAlbumResTempMapper.selectByPrimaryKey(id);
	}

	// 增加app分发信息
	public void addAppAlbumResTemp(AppAlbumRes record) {
		appAlbumResTempMapper.insert(record);
	}

	// 更新app分发信息
	public void upAppAlbumResTemp(AppAlbumRes record) {
		appAlbumResTempMapper.updateByPrimaryKey(record);
	}

	// 删除app分发信息
	public void delAppAlbumResTemp(int id) {
		appAlbumResTempMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id删除对应app内容信息
	 */
	public int deleteByApp(Integer appId, Integer apkId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("apkId", apkId);
		return appAlbumResTempMapper.deleteByApp(map);
	}

	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppAlbumResTemp(id);
		}
	}

	// 批量更新
	public void resetAppAlbumResTemp(Integer raveId, Integer columnId, Integer albumId, Integer source, List<Integer> ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("columnId", columnId);
		map.put("albumId", albumId);
		map.put("source", source);
		map.put("raveId", raveId);
		map.put("list", ids);
		appAlbumResTempMapper.insertSelectApps(map);
	}

	// 获取app信息
	public AppAlbumRes getAppAlbumResTemp(int id, int type) {
		AppAlbumResExample example = new AppAlbumResExample();
		if (type == 1) {
			// 获取对应app信息
			example.createCriteria().andAppId(id);
		} else if (type == 2) {
			example.createCriteria().andApkId(id);
		}
		List<AppAlbumRes> res = appAlbumResTempMapper.selectByExample(example);
		if (res != null && res.size() > 0) {
			return res.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void updateSortByPrimarykey(AppAlbumRes entity) {
		// TODO Auto-generated method stub
		appAlbumResTempMapper.updateSortByPrimarykey(entity);
	}

	@Override
	public void doOperant(AppAlbumRes appAlbumRes) {
		appAlbumResTempMapper.insertOperant(appAlbumRes);
		appAlbumResTempMapper.deleteOperant(appAlbumRes);
		appAlbumResTempMapper.updateOperant(appAlbumRes);
	}

	@Override
	public void doBatchOperant(Integer[] ids, Integer columnId) {

		AppAlbumColumn appAlbumColumn = appAlbumColumnMapper.selectByPrimaryKey(columnId);
		for (Integer id : ids) {
			AppAlbumRes appAlbumRes = appAlbumResTempMapper.selectByPrimaryKey(id);
			AppAlbumRes appAlbumRes1 = new AppAlbumRes();
			appAlbumRes1.setAppFile(appAlbumRes.getAppFile());
			appAlbumRes1.setAppAlbumColumn(appAlbumColumn);
			doOperant(appAlbumRes1);
		}

	}

	@Override
	public List<AppAlbumRes> getByFileColumnAndSource(AppInfo appInfo, AppAlbumColumn appAlbumColumn, Integer source, Integer raveId) {
		AppAlbumRes appAlbumRes = new AppAlbumRes();
		appAlbumRes.setAppInfo(appInfo);
		appAlbumRes.setAppAlbumColumn(appAlbumColumn);
		appAlbumRes.setSource(source);
		appAlbumRes.setRaveId(raveId);
		return appAlbumResTempMapper.getByFileColumnAndSource(appAlbumRes);
	}

	@Override
	public void deleteBySource(Integer albumId, Integer columnId, int raveId, int source) {
		AppAlbumRes appAlbumRes = new AppAlbumRes();
		if (albumId != null && albumId != 0) {
			appAlbumRes.setAppAlbum(appAlbumMapper.selectByPrimaryKey(albumId));
		}
		if (columnId != null && columnId != 0) {
			appAlbumRes.setAppAlbumColumn(appAlbumColumnMapper.selectByPrimaryKey(columnId));
		}
		appAlbumRes.setSource(source);
		appAlbumRes.setRaveId(raveId);
		appAlbumResTempMapper.deleteBySource(appAlbumRes);

	}

	@Override
	public List<Integer> getids() {
		// TODO Auto-generated method stub
		return appAlbumResTempMapper.getids();
	}

	@Override
	public void updateRankingByPrimarykey(AppAlbumRes entity) {
		appAlbumResTempMapper.updateRankingByPrimarykey(entity);

	}

	/**
	 * 获取已经存在的列表
	 * 
	 * @param criteria
	 * @return
	 */
	public AppAlbumRes getApkRes(Integer raveId, Integer columnId, Integer apkId) {
		AppAlbumResExample example = new AppAlbumResExample();
		AppAlbumResExample.Criteria criteria = example.createCriteria();
		if (raveId != null) {
			criteria.andRaveIdEqual(raveId,0);
		}
		if (columnId != null) {
			criteria.columnId(columnId);
		}
		if (apkId != null) {
			criteria.andApkId(apkId);
		}
		List<AppAlbumRes> res = appAlbumResTempMapper.selectByExample(example);
		if (res != null && res.size() > 0) {
			return res.get(0);
		} else {
			return null;
		}
	}

}
