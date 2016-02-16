package com.mas.rave.service;

import java.util.List;

import com.mas.rave.main.vo.AppannieInfoBase;

public interface AppannieInfoBaseService {
	/**
	 * 根据app名称删除
	 * @param appName
	 * @return
	 */
	public int deleteByName(String appName);	
	/**
	 * 根据app名称查找
	 * @param appannieInfo
	 * @return
	 */
	public void insert(AppannieInfoBase appannieInfoBase);
	/**
	 * 插入数据
	 * @param appName
	 * @return
	 */
	public List<AppannieInfoBase> selectByName(AppannieInfoBase appName);
	
}
