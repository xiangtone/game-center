package com.mas.rave.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppCollectionMapper;
import com.mas.rave.dao.AppCollectionResMapper;
import com.mas.rave.main.vo.AppCollection;
import com.mas.rave.main.vo.AppCollectionExample;
import com.mas.rave.main.vo.AppCollectionExample.Criteria;
import com.mas.rave.service.AppCollectionService;
import com.mas.rave.util.FileUtil;

@Service
public class AppCollectionServiceImpl implements AppCollectionService {

	@Autowired
	private AppCollectionMapper appCollectionMapper;

	@Autowired
	private AppCollectionResMapper appCollectionResMapper;

	@Override
	public PaginationVo<AppCollection> searchAppCollections(AppCollectionCriteria criteria, int currentPage, int pageSize) {
		AppCollectionExample example = new AppCollectionExample();
		Map<Integer, Object> params = criteria.getParams();
		Criteria criteria1 = example.createCriteria();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				criteria1.andNameLike(params.get(1).toString());
			} else if (key.equals(2)) {
				criteria1.andNameCnLike(params.get(2).toString());

			}
			if (key.equals(4)) {
				criteria1.andRaveIdEqual(Integer.parseInt(params.get(4).toString()));
			}
			if (key.equals(5)) {
				criteria1.andTypeEqual(Integer.parseInt(params.get(5).toString()));
			}
		}
		example.setOrderByClause("createTime desc");
		List<AppCollection> data = appCollectionMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = appCollectionMapper.countByExample(example);
		PaginationVo<AppCollection> result = new PaginationVo<AppCollection>(data, recordCount, pageSize, currentPage);
		return result;
	}

	//liwei 2015-06-16 修改排序
	@Override
	public PaginationVo<AppCollection> searchAppCollections1(AppCollectionCriteria criteria, int currentPage, int pageSize) {
		AppCollectionExample example = new AppCollectionExample();
		Map<Integer, Object> params = criteria.getParams();
		Criteria criteria1 = example.createCriteria();
		for (Integer key : params.keySet()) {
			if (key.equals(1)) {
				// 根据id查看
				criteria1.andNameLike(params.get(1).toString());
			} else if (key.equals(2)) {
				criteria1.andNameCnLike(params.get(2).toString());

			}
			if (key.equals(4)) {
				criteria1.andRaveIdEqual(Integer.parseInt(params.get(4).toString()));
			}
			if (key.equals(5)) {
				criteria1.andTypeEqual(Integer.parseInt(params.get(5).toString()));
			}
		}
		example.setOrderByClause("createTime desc");
		List<AppCollection> data = appCollectionMapper.selectByExample1(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = appCollectionMapper.countByExample(example);
		PaginationVo<AppCollection> result = new PaginationVo<AppCollection>(data, recordCount, pageSize, currentPage);
		return result;
	}

	@Override
	public List<AppCollection> searchAppCollection(AppCollectionExample example) {
		return appCollectionMapper.selectByExample(example);
	}

	@Override
	public AppCollection getAppCollection(long id) {
		return appCollectionMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<AppCollection> selectByName(AppCollection record) {
		return appCollectionMapper.selectByName(record);
	}

	@Override
	public void addAppCollection(AppCollection record) {
		appCollectionMapper.insert(record);
	}

	@Override
	public void upAppCollection(AppCollection record) {
		appCollectionMapper.updateByPrimaryKey(record);

	}

	@Override
	public void delAppCollection(Integer id) {

		AppCollection appCollection = appCollectionMapper.selectByPrimaryKey(id);
		String bigUrl = null;
		String smallUrl = null;
		if (appCollection != null) {
			// 删除分发数据
			appCollectionResMapper.deleteByCollectionId(appCollection.getCollectionId());
			if (appCollection.isState() == true) {
				appCollection.setState(false);
				appCollectionMapper.updateByPrimaryKey(appCollection);
			} else {
				// 设置删除文件路径

				bigUrl = appCollection.getBigicon();
				smallUrl = appCollection.getIcon();
				appCollectionMapper.deleteByPrimaryKey(id);
				try {
					// 删除对应文件
					FileUtil.deleteFile(bigUrl);
					FileUtil.deleteFile(smallUrl);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void batchDelete(Integer[] ids) {
		for (Integer id : ids) {
			delAppCollection(id);
		}

	}

	@Override
	public void updateSortByPrimarykey(AppCollection appCollection) {
		appCollectionMapper.updateSortByPrimarykey(appCollection);

	}

}
