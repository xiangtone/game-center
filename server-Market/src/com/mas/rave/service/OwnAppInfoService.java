package com.mas.rave.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mas.rave.common.page.PaginationVo;
import com.mas.rave.main.vo.OwnAppInfo;

/**
 * app信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface OwnAppInfoService {
	/**
	 * 分页显示app信息
	 * 
	 * @param criteria
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PaginationVo<OwnAppInfo> searchOwnApps(OwnAppInfo criteria, int currentPage, int pageSize);

	public OwnAppInfo searchOwnApp(OwnAppInfo criteria);

	public List<OwnAppInfo> searchOwnApps(OwnAppInfo criteria);

	// 查看单个app信息
	public OwnAppInfo getOwnApp(long id);

	// 获取所有app信息
	public List<OwnAppInfo> getAllOwnAppInfos();

	// 增加app信息
	public int addOwnApp(OwnAppInfo app);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	public int insertSelective(OwnAppInfo record);

	// 更新app信息
	public void upOwnApp(OwnAppInfo app);

	// 删除app信息
	public void delOwnApp(Integer id);

	void batchDelete(Integer[] ids);

	public void upOwnAppFile(OwnAppInfo appInfo);

	public OwnAppInfo findOwnAppInfo(String appName);
}
