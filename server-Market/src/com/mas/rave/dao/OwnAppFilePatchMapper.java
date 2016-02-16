package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;

import com.mas.rave.main.vo.OwnAppFilePatch;

/**
 * app文件数据访问接口
 * 
 * @author lin.lu
 * 
 */
public interface OwnAppFilePatchMapper {

	/**
	 * 增加app文件信息
	 */
	int insert(OwnAppFilePatch record);

	/**
	 * 根据id删除对应app信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 根据id查看app文件信息
	 */
	OwnAppFilePatch selectByPrimaryKey(int id);

	// 获取app对应文件信息
	public List<OwnAppFilePatch> getOwnAppFilePatchs(int apkId);

	// 获取对应差异包
	public OwnAppFilePatch getPtchByApkId(HashMap<String, Object> map);

	/**
	 * 根据app信息更新app对应名称
	 * 
	 * @param appId
	 * @return
	 */
	int upByAppId(HashMap<String, Object> map);
}