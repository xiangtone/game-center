package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.CategoryMapper;
import com.reportforms.model.Category;
import com.reportforms.service.CategoryService;

/**
 * app对应分类
 * 
 * @author lisong.lan
 * 
 */

@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements CategoryService<Category> {

	@Autowired
	private CategoryMapper<Category> categoryMapper;
	
	@Override
	public List<Category> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return categoryMapper.query(paginationBean);
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return categoryMapper.queryAllCounts(paginationBean);
	}

}
