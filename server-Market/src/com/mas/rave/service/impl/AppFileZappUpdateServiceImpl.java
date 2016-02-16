package com.mas.rave.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.AppFileZappUpdateMapper;
import com.mas.rave.main.vo.AppFileZappUpdate;
import com.mas.rave.main.vo.AppFileZappUpdateExample;
import com.mas.rave.main.vo.AppFileZappUpdateExample.Criteria;
import com.mas.rave.service.AppFileZappUpdateService;
@Service
public class AppFileZappUpdateServiceImpl implements AppFileZappUpdateService {

	@Autowired
	private AppFileZappUpdateMapper appFileZappUpdateMapper;
	
	@Override
	public PaginationVo<AppFileZappUpdate> searchAppFileZappUpdates(
			AppFileZappUpdateCriteria criteria, int currentPage, int pageSize) {
		AppFileZappUpdateExample example = new AppFileZappUpdateExample();
		Map<Integer, Object> params = criteria.getParams();
		Criteria criteria1 = example.createCriteria();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Integer key : params.keySet()) {
			if(key.equals(1)){
				criteria1.andVersionNameLikeTo(params.get(1).toString().trim());
				map.put("versionName", params.get(1).toString().trim());
			}else if(key.equals(2))	{
				criteria1.andUpgradeTypeEqualTo((Integer)params.get(2));
			}else if(key.equals(3))	{
				criteria1.apkKeyEqualTo(params.get(3).toString());
			}
		}
		example.setMapOrderByClause(map);
		example.setOrderByClause(" versionCode desc");
		List<AppFileZappUpdate> data = appFileZappUpdateMapper.selectByExample(example, new RowBounds((currentPage - 1) * pageSize, pageSize));

		int recordCount = appFileZappUpdateMapper.countByExample(example);
		PaginationVo<AppFileZappUpdate> result = new PaginationVo<AppFileZappUpdate>(data, recordCount, pageSize, currentPage);
		
		
		return result;
	}

	@Override
	public void addAppFileZappUpdate(AppFileZappUpdate appFileZappUpdate) {
		appFileZappUpdateMapper.insert(appFileZappUpdate);

	}

	@Override
	public void delAppFileZappUpdate(Integer id) {
		appFileZappUpdateMapper.deleteByPrimaryKey(id);

	}

	@Override
	public void batchDelete(Integer[] ids) {
		for(Integer id:ids){
			delAppFileZappUpdate(id);
		}

	}

	@Override
	public void updateByPrimarykey(AppFileZappUpdate appFileZappUpdate) {
		appFileZappUpdateMapper.updateByPrimaryKey(appFileZappUpdate);

	}

	@Override
	public AppFileZappUpdate selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appFileZappUpdateMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<AppFileZappUpdate> selectByCondition(
			AppFileZappUpdate appFileZappUpdate) {
		// TODO Auto-generated method stub
		return appFileZappUpdateMapper.selectByCondition(appFileZappUpdate);
	}

	
}
