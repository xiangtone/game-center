package com.mas.rave.service;

import java.util.Date;
import java.util.List;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppannieInfo;

public interface AppannieInfoService {

	public PaginationVo<AppannieInfo> selectByExample(AppannieInfo criteria,int currentPage, int pageSize);
	
	public AppannieInfo selectByPrimaryKey(Integer id);
	
	public int deleteByPrimaryKey(Integer id);
	
	public int insert(AppannieInfo appannieInfo);
		
	public List<AppannieInfo> selectByCondition(AppannieInfo criteria);
	
	public List<AppannieInfo> selectByCreateTime(Date createTime);
	
	public int deleteByCondition(AppannieInfo criteria);
	
	public void batchDel(Integer[] ids);

}