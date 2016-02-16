package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.MasUserMapper;
import com.reportforms.model.MasUser;
import com.reportforms.service.MasUserService;

@Service
@SuppressWarnings("unchecked")
public class MasUserServiceImpl extends BaseServiceImpl<MasUser> implements
		MasUserService<MasUser> {
	
	@Autowired
	private MasUserMapper masUserMapper;
 	
	@Override
	public List<MasUser> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return masUserMapper.query(paginationBean,new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return masUserMapper.queryAllCounts(paginationBean);
	}

}
