package com.mas.rave.service;

import java.util.List;

import com.mas.rave.main.vo.OwnAppFilePatch;

/**
 * AppFilePatchPatch对应文件信息数据访问接口
 * 
 * @author lin.lu
 * 
 */
public interface OwnAppFilePatchService {

	// app对应所有文件信息
	public List<OwnAppFilePatch> getOwnAppFilePatchs(Integer apkId);

	// 增加app文件信息
	public int addOwnAppFilePatch(OwnAppFilePatch appFilePatch);

	// 删除app文件信息
	public void delOwnAppFilePatch(int id);

	void batchDelete(Integer[] ids);

	// 获取对应差异包
	public OwnAppFilePatch getByApkId(int apkId, int lowVersionCode);
}
