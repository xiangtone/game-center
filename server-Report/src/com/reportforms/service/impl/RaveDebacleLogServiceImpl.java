package com.reportforms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.common.page.PaginationBean;
import com.reportforms.dao.RaveDebacleLogMapper;
import com.reportforms.model.RaveDebacleLog;
import com.reportforms.service.RaveDebacleLogService;

@Service
public class RaveDebacleLogServiceImpl extends BaseServiceImpl<RaveDebacleLog> implements
		RaveDebacleLogService<RaveDebacleLog> {
	
	@Autowired
	private RaveDebacleLogMapper raveDebacleLogMapper;
	
	@Override
	public List<RaveDebacleLog> query(PaginationBean paramsBean) {
		// TODO Auto-generated method stub
		return raveDebacleLogMapper.query(paramsBean,new RowBounds(paramsBean.getStart(), paramsBean.getLimit()));
	}
	
	@Override
	public Integer queryAllCounts(PaginationBean paginationBean) {
		// TODO Auto-generated method stub
		return raveDebacleLogMapper.queryAllCounts(paginationBean);
	}

}
