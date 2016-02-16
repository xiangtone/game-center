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
import com.mas.rave.dao.AppCollectionResMapper;
import com.mas.rave.dao.AppFileMapper;
import com.mas.rave.dao.AppInfoMapper;
import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppAlbumResExample;
import com.mas.rave.service.AppCollectionResService;

/**
 * 分发
 * 
 * @author liwei.sz
 * 
 */
@Service
public class AppCollectionResServiceImpl implements AppCollectionResService {

	@Autowired
	private AppCollectionResMapper appCollectionResMapper;

	@Autowired
	private AppAlbumColumnMapper appAlbumColumnMapper;

	@Autowired
	private AppInfoMapper appInfoMapper;

	@Autowired
	private AppFileMapper appFileMapper;

	public PaginationVo<AppAlbumRes> searchAlbumRess(AppCollectionResCriteria criteria, int currentPage, int pageSize) {
		AppAlbumResExample example = new AppAlbumResExample();
		AppAlbumResExample.Criteria criteria1 = example.createCriteria();
		Map<Integer, Object> params = criteria.getParams();
		Map<String, Object> map = new HashMap<String, Object>();
		if (params != null && params.size() > 0) {
			//栏目分发 
			if (params.get(1) != null) {
				criteria1= criteria1.columnId((int)params.get(1));
				if (params.get(2) != null) {
					//根据名字查询
					criteria1.andNameLike1(params.get(2).toString());
					map.put("appName", params.get(2).toString().trim());
				}
				// 根据Scource查看
				if (params.get(3) != null) {
					//根据分发类型查询
					criteria1.andScourceEqual((int)params.get(3));
				}
				if (params.get(4) != null) {
					//根据国家ID查询
					criteria1.andRaveIdEqual((int)params.get(4),0);
				}
				if (params.get(5) != null) {
					//根据主题ID查询
					criteria1.andCollectionIdEqual((int)params.get(5));
				}
				//应用专题分发
			}else{				
				if (params.get(5) != null) {
					//根据主题ID查询
					criteria1.andCollectionIdEqual((int)params.get(5));
				}
				// 根据name查看
				if (params.get(2) != null) {
					//根据分发类型查询
					criteria1.andNameLike1(params.get(2).toString());
					map.put("appName", params.get(2).toString().trim());
				}
				if (params.get(4) != null) {
					//根据国家ID查询
					criteria1.andRaveIdEqual((int)params.get(4),0);
				}
			}
			
		}
		example.setMapOrderByClause(map);
//		example.setOrderByClause("realDowdload DESC,initDowdload DESC,createTime desc");
		List<AppAlbumRes> data = appCollectionResMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = appCollectionResMapper.countByExample(example);
		PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(data, recordCount, pageSize, currentPage);
		return result;
	}
	//liwei 2015-06-16 修改排序
	public PaginationVo<AppAlbumRes> searchAlbumRess1(AppCollectionResCriteria criteria, int currentPage, int pageSize) {
		AppAlbumResExample example = new AppAlbumResExample();
		AppAlbumResExample.Criteria criteria1 = example.createCriteria();
		Map<Integer, Object> params = criteria.getParams();
		Map<String, Object> map = new HashMap<String, Object>();
		if (params != null && params.size() > 0) {
			//栏目分发 
			if (params.get(1) != null) {
				criteria1= criteria1.columnId((int)params.get(1));
				if (params.get(2) != null) {
					//根据名字查询
					criteria1.andNameLike1(params.get(2).toString());
					map.put("appName", params.get(2).toString().trim());
				}
				// 根据Scource查看
				if (params.get(3) != null) {
					//根据分发类型查询
					criteria1.andScourceEqual((int)params.get(3));
				}
				if (params.get(4) != null) {
					//根据国家ID查询
					criteria1.andRaveIdEqual((int)params.get(4),0);
				}
				if (params.get(5) != null) {
					//根据主题ID查询
					criteria1.andCollectionIdEqual((int)params.get(5));
				}
				//应用专题分发
			}else{				
				if (params.get(5) != null) {
					//根据主题ID查询
					criteria1.andCollectionIdEqual((int)params.get(5));
				}
				// 根据name查看
				if (params.get(2) != null) {
					//根据分发类型查询
					criteria1.andNameLike1(params.get(2).toString());
					map.put("appName", params.get(2).toString().trim());
				}
				if (params.get(4) != null) {
					//根据国家ID查询
					criteria1.andRaveIdEqual((int)params.get(4),0);
				}
			}
			
		}
		example.setMapOrderByClause(map);
//		example.setOrderByClause("realDowdload DESC,initDowdload DESC,createTime desc");
		List<AppAlbumRes> data = appCollectionResMapper.selectByExample1(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = appCollectionResMapper.countByExample(example);
		PaginationVo<AppAlbumRes> result = new PaginationVo<AppAlbumRes>(data, recordCount, pageSize, currentPage);
		return result;
	}
	

	@Override
	public PaginationVo<AppAlbumRes> getSelectAppFiles(HashMap<String, Object> map, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		map.put("index", (currentPage - 1) * pageSize);
		map.put("pageSize", pageSize);
		Integer count = appCollectionResMapper.getSelectAppFilesCount(map);
		List<AppAlbumRes> apks = appCollectionResMapper.getSelectAppFiles(map);
		return new PaginationVo<AppAlbumRes>(apks, count, pageSize, currentPage);
	}

	// 按条件查看app分发信息
	public List<AppAlbumRes> getAppAlbumRes(AppCollectionResCriteria criteria) {
		AppAlbumResExample example = new AppAlbumResExample();
		AppAlbumResExample.Criteria criteria1 = example.createCriteria();
		Map<Integer, Object> params = criteria.getParams();
		Map<String, Object> map = new HashMap<String, Object>();
		if (params != null && params.size() > 0) {
			//栏目分发 
			if (StringUtils.isNotEmpty(params.get(1).toString())) {
				criteria1= criteria1.columnId(Integer.parseInt(params.get(1).toString()));
				if (params.get(2) != null) {
					//根据名字查询
					criteria1.andNameLike1(params.get(2).toString());
					map.put("appName", params.get(2).toString().trim());
				}
				// 根据Scource查看
				if (params.get(3) != null) {
					//根据分发类型查询
					criteria1.andScourceEqual((int)params.get(3));
				}
				if (params.get(4) != null) {
					//根据国家ID查询
					criteria1.andRaveIdEqual((int)params.get(4),0);
				}
				if (params.get(5) != null) {
					//根据主题ID查询
					criteria1.andCollectionIdEqual((int)params.get(5));
				}
				//应用专题分发
			}
			example.setMapOrderByClause(map);
		}
		return appCollectionResMapper.selectByExample(example);
	}
	// 查看所有app分发信息
	public List<AppAlbumRes> getAppAlbumRes() {
		AppAlbumResExample example = new AppAlbumResExample();
		return appCollectionResMapper.selectByExample(example);
	}

	// 查看单个app分发信息
	public List<AppAlbumRes> getAppAlbumResByColumn(int columnId) {
		AppAlbumResExample example = new AppAlbumResExample();
		// 根据id查看
		example.createCriteria().columnId(columnId);
		return appCollectionResMapper.selectByExample(example);
	}

	// 增加app分发信息
	public void addAppAlbumRes(AppAlbumRes record) {
		appCollectionResMapper.insert(record);
	}

	// 更新app分发信息
	public void upAppAlbumRes(AppAlbumRes record) {
		appCollectionResMapper.updateByPrimaryKey(record);
	}

	// 删除app分发信息
	public void delAppAlbumRes(int id) {
		appCollectionResMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id删除对应app内容信息
	 */
	public int deleteByApp(Integer appId, Integer apkId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("apkId", apkId);
		return appCollectionResMapper.deleteByApp(map);
	}

	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppAlbumRes(id);
		}
	}

	// 批量更新
	public void resetAppAlbumRes(Integer raveId, Integer collectionId,Integer columnId,Integer albumId, List<Integer> ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("raveId", raveId);
		map.put("columnId", columnId);
		map.put("albumId", albumId);
		map.put("collectionId", collectionId);
		map.put("list", ids);
		appCollectionResMapper.insertSelectApps(map);
	}

	// 获取app信息
	public AppAlbumRes getAppAlbumRes(int id, int type) {
		AppAlbumResExample example = new AppAlbumResExample();
		if (type == 1) {
			// 获取对应app信息
			example.createCriteria().andAppId(id);
		} else if (type == 2) {
			example.createCriteria().andApkId(id);
		}
		List<AppAlbumRes> res = appCollectionResMapper.selectByExample(example);
		if (res != null && res.size() > 0) {
			return res.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void updateSortByPrimarykey(AppAlbumRes entity) {
		// TODO Auto-generated method stub
		appCollectionResMapper.updateSortByPrimarykey(entity);
	}

	@Override
	public List<String> selectByColumnId(Integer columnId,Integer raveId) {
		// TODO Auto-generated method stub
		AppAlbumRes appAlbumRes = new AppAlbumRes();
		appAlbumRes.setColumnId(columnId);
		appAlbumRes.setRaveId(raveId);
		return appCollectionResMapper.selectByColumnId(appAlbumRes);
	}

	@Override
	public AppAlbumRes selectByPrimaryKey(long id) {
		// TODO Auto-generated method stub
		return appCollectionResMapper.selectByPrimaryKey(id);
	}
}
