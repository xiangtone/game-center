package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.AppDownLoadMapper;
import com.reportforms.dao.CategoryMapper;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.AppDownLoad;
import com.reportforms.model.Category;
import com.reportforms.service.AppDownLoadService;

@Service
public class AppDownLoadServiceImpl extends BaseServiceImpl<AppDownLoad> implements
		AppDownLoadService<AppDownLoad> {
	
	@Autowired
	private AppDownLoadMapper<AppDownLoad> appDownLoadMapper;
	
	@Autowired
	private CategoryMapper<Category> categoryMapper;
	
	@Override
	public List<AppDownLoad> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		List<AppDownLoad> list = appDownLoadMapper.query(paginationBean,new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
		if(!CollectionUtils.isEmpty(list)){
			return format(list);
		}else{
			return list;
		}
	}
	
	@Override
	public List<AppDownLoad> queryByGroupBy(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		List<AppDownLoad> list = appDownLoadMapper.queryByGroupBy(paginationBean, new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
		if(!CollectionUtils.isEmpty(list)){
			return format(list);
		}else{
			return list;
		}
	}
	
	@Override
	public Integer queryByGroupByCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return appDownLoadMapper.queryByGroupByCounts(paginationBean);
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return appDownLoadMapper.queryAllCounts(paginationBean);
	}
	
	private List<AppDownLoad> format(List<AppDownLoad> list){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
		List<Category> categorys = categoryMapper.query(new PaginationBean());
		DataSourceSwitch.clearDataSourceType();
		if(null != categorys && !categorys.isEmpty()){
			for (AppDownLoad app : list) {
				for (Category category : categorys) {
					if(null != app.getCategoryId() && app.getCategoryId().intValue() == category.getId().intValue()){
						app.setCategoryName(category.getName());
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public AppDownLoad queryByTotal(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return appDownLoadMapper.queryByTotal(paramsBean);
	}

}
