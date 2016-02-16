package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.CategoryMapper;
import com.reportforms.dao.MusicDownLoadMapper;
import com.reportforms.datasource.DataSourceInstances;
import com.reportforms.datasource.DataSourceSwitch;
import com.reportforms.model.Category;
import com.reportforms.model.MusicDownLoad;
import com.reportforms.service.MusicDownLoadService;

@Service
public class MusicDownLoadServiceImpl extends BaseServiceImpl<MusicDownLoad> implements
		MusicDownLoadService<MusicDownLoad> {
	
	@Autowired
	private MusicDownLoadMapper<MusicDownLoad> musicDownLoadMapper;
	
	@Autowired
	private CategoryMapper<Category> categoryMapper;
	
	@Override
	public List<MusicDownLoad> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		List<MusicDownLoad> list = musicDownLoadMapper.query(paginationBean,new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
		if(!CollectionUtils.isEmpty(list)){
			return format(list);
		}else{
			return list;
		}
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return musicDownLoadMapper.queryAllCounts(paginationBean);
	}
	
	private List<MusicDownLoad> format(List<MusicDownLoad> list){
		DataSourceSwitch.setDataSourceType(DataSourceInstances.MYSQL3);
		List<Category> categorys = categoryMapper.query(new PaginationBean());
		DataSourceSwitch.clearDataSourceType();
		if(null != categorys && !categorys.isEmpty()){
			for (MusicDownLoad music : list) {
				for (Category category : categorys) {
					if(null != music.getCategoryId() && music.getCategoryId().intValue() == category.getId().intValue()){
						music.setCategoryName(category.getName());
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public List<MusicDownLoad> queryByGroupBy(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		List<MusicDownLoad> list = musicDownLoadMapper.queryByGroupBy(paginationBean, new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
		if(!CollectionUtils.isEmpty(list)){
			return format(list);
		}else{
			return list;
		}
	}
	
	@Override
	public Integer queryByGroupByCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return musicDownLoadMapper.queryByGroupByCounts(paginationBean);
	}
	
	@Override
	public MusicDownLoad queryByTotal(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return musicDownLoadMapper.queryByTotal(paramsBean);
	}

}
