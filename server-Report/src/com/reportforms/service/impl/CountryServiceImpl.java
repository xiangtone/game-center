package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.CountryMapper;
import com.reportforms.model.Country;
import com.reportforms.service.CountryService;

/**
 * country
 * 
 * @author lisong.lan
 * 
 */
@Service
public class CountryServiceImpl extends BaseServiceImpl<Country> implements CountryService<Country> {
	
	@Autowired
	private CountryMapper<Country> countryMapper;
	
	@Override
	public List<Country> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return countryMapper.query(paginationBean,new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return countryMapper.queryAllCounts(paginationBean);
	}

}
