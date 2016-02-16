package com.mas.rave.dao;

import java.util.List;

import com.mas.rave.main.vo.AppannieInfoBase;

public interface AppannieInfoBaseMapper {


	
	int deleteByName(AppannieInfoBase appannieInfo);	
	int insert(AppannieInfoBase appannieInfo);
	List<AppannieInfoBase> selectByName(AppannieInfoBase appannieInfo);
	

}