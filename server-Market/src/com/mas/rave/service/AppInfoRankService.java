package com.mas.rave.service;

import com.mas.rave.main.vo.AppInfoRank;

/**
 * t_app_info_rank访问接口
 * 
 * @author lin.lu
 */
public interface AppInfoRankService {


	// 查看单个
	public AppInfoRank getAppInfoRank(int appId, int raveId);

	// 增加
	public void addAppInfoRank(AppInfoRank record);

	// 更新
	public void upAppInfoRank(AppInfoRank record);

	/**
	 * 更新排名
	 * @param columnId
	 */
	public void updateRank(Integer columnId,Integer raveId);
}
