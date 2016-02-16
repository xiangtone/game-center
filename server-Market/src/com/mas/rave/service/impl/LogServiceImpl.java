package com.mas.rave.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.dao.LogMapper;
import com.mas.rave.main.vo.Log;
import com.mas.rave.service.LogService;
@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogMapper logMapper;
	
	@Override
	public PaginationVo<Log> searchLog(Log criteria, int currentPage,
			int pageSize) {
		int recordCount = logMapper.countByExample(criteria);
		criteria.setCurrentPage((currentPage - 1) * pageSize);
		criteria.setPageSize(pageSize);
		List<Log> data = logMapper.selectByExample(criteria);
		PaginationVo<Log> result = new PaginationVo<Log>(data, recordCount, pageSize, currentPage);
		return result;
	}


	@Override
	public Log getLog(long id) {
		// TODO Auto-generated method stub
		return logMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Log> getAllLogs() {
		// TODO Auto-generated method stub
		return logMapper.getAllLogs();
	}

	@Override
	public int addLog(Log log) {
		// TODO Auto-generated method stub
		return logMapper.insert(log);
	}


}
