package com.reportforms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reportforms.dao.TsummyMapper;
import com.reportforms.model.Tsummy;
import com.reportforms.service.TsummyService;

@Service
public class TsummyServiceImpl implements TsummyService {
	
	@Autowired
	private TsummyMapper tsummyMapper;

	@Override
	public List<Tsummy> selectAll() {
		// TODO Auto-generated method stub
		return tsummyMapper.selectAll();
	}

}
