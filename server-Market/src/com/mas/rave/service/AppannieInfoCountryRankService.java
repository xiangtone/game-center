package com.mas.rave.service;

import java.util.Date;
import java.util.List;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.AppannieInfoCountryRank;

public interface AppannieInfoCountryRankService {

	public PaginationVo<AppannieInfoCountryRank> selectByExample(AppannieInfoCountryRank criteria,int currentPage, int pageSize);
	
	public AppannieInfoCountryRank selectByPrimaryKey(Integer id);
	
	public int deleteByPrimaryKey(Integer id);
	
	public int insert(AppannieInfoCountryRank criteria);
		
	public List<AppannieInfoCountryRank> selectByCondition(AppannieInfoCountryRank criteria);
	
	public List<AppannieInfoCountryRank> selectByCreateTime(Date createTime);
	
	public int deleteByCondition(AppannieInfoCountryRank criteria);
	
	public void batchDel(Integer[] ids);
	
	public int updateStatusBy(AppannieInfoCountryRank criteria);
	
	public int update(AppannieInfoCountryRank criteria);

}