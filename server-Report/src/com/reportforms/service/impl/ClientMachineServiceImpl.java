package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.ClientMachineMapper;
import com.reportforms.model.ClientMachine;
import com.reportforms.service.ClientMachineService;

@Service
public class ClientMachineServiceImpl extends BaseServiceImpl<ClientMachine> implements
		ClientMachineService<ClientMachine> {

	@Autowired
	private ClientMachineMapper<ClientMachine> clientMachineMapper;
	
	@Override
	public List<ClientMachine> query(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return clientMachineMapper.query(paginationBean,new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return clientMachineMapper.queryAllCounts(paginationBean);
	}
	
	@Override
	public List<ClientMachine> queryToExport(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return clientMachineMapper.query(paginationBean,new RowBounds(paginationBean.getStart(), paginationBean.getLimit()));
	}
}
