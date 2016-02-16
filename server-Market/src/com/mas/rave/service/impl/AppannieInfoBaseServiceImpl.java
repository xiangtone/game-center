package com.mas.rave.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mas.rave.dao.AppannieInfoBaseMapper;
import com.mas.rave.main.vo.AppannieInfoBase;
import com.mas.rave.service.AppannieInfoBaseService;
@Service
public class AppannieInfoBaseServiceImpl implements AppannieInfoBaseService {

	@Autowired
	private AppannieInfoBaseMapper appannieInfoBaseMapper;
	@Override
	public int deleteByName(String appName) {
		AppannieInfoBase appannieInfo  =new AppannieInfoBase();
		appannieInfo.setAppName(appName);
		return appannieInfoBaseMapper.deleteByName(appannieInfo);
	}

	@Override
	public void insert(AppannieInfoBase appannieInfo) {
		// TODO Auto-generated method stub
		appannieInfoBaseMapper.insert(appannieInfo);
	}

	@Override
	public List<AppannieInfoBase> selectByName(AppannieInfoBase appannieInfo) {
		return appannieInfoBaseMapper.selectByName(appannieInfo);
	}

}
