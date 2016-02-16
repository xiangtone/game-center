package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.ClientUserMapper;
import com.reportforms.model.ClientUser;
import com.reportforms.service.ClientUserService;


@Service
@SuppressWarnings("unchecked")
public class ClientUserServiceImpl extends BaseServiceImpl<ClientUser> implements ClientUserService<ClientUser> {

	@Autowired
	private ClientUserMapper clientUserMapper;
	
	@Override
	public List<ClientUser> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return clientUserMapper.query(paginationBean, new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return clientUserMapper.queryAllCounts(paginationBean);
	}

}
