package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.CategoryMapper;
import com.reportforms.dao.ImageDownLoadMapper;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.Category;
import com.reportforms.model.ImageDownLoad;
import com.reportforms.service.ImageDownLoadService;

@Service
public class ImageDownLoadServiceImpl extends BaseServiceImpl<ImageDownLoad> implements
		ImageDownLoadService<ImageDownLoad> {
	
	@Autowired
	private ImageDownLoadMapper<ImageDownLoad> imageDownLoadMapper;
	
	@Autowired
	private CategoryMapper<Category> categoryMapper;
	
	@Override
	public List<ImageDownLoad> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		List<ImageDownLoad> list = imageDownLoadMapper.query(paginationBean,new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
		if(!CollectionUtils.isEmpty(list)){
			return format(list);
		}else{
			return list;
		}
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return imageDownLoadMapper.queryAllCounts(paginationBean);
	}
	
	@Override
	public List<ImageDownLoad> queryByGroupBy(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		List<ImageDownLoad> list = imageDownLoadMapper.queryByGroupBy(paginationBean, new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
		if(!CollectionUtils.isEmpty(list)){
			return format(list);
		}else{
			return list;
		}
	}
	
	@Override
	public Integer queryByGroupByCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return imageDownLoadMapper.queryByGroupByCounts(paginationBean);
	}
	
	private List<ImageDownLoad> format(List<ImageDownLoad> list){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
		List<Category> categorys = categoryMapper.query(new PaginationBean());
		DataSourceSwitch.clearDataSourceType();
		if(null != categorys && !categorys.isEmpty()){
			for (ImageDownLoad image : list) {
				for (Category category : categorys) {
					if(null != image.getCategoryId() && image.getCategoryId().intValue() == category.getId().intValue()){
						image.setCategoryName(category.getName());
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public ImageDownLoad queryByTotal(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return imageDownLoadMapper.queryByTotal(paramsBean);
	}

}
