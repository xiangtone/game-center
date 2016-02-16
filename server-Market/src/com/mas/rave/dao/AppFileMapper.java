package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppFile;
import com.mas.rave.main.vo.AppFileExample;

/**
 * app文件数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppFileMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(AppFileExample example);

	/**
	 * 根据id删除对应app信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 更新state
	 */
	int updateState(AppFile appFile);
	
	/**
	 * 增加app文件信息
	 */
	int insert(AppFile record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(AppFile record);

	/**
	 * 分页查询app文件信息
	 */
	List<AppFile> selectByExample(AppFileExample example, RowBounds rowBounds);

	/**
	 * 分页查询app文件信息
	 */
	List<AppFile> selectByExample(AppFileExample example);

	/**
	 * 根据id查看app文件信息
	 */
	AppFile selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppFile record);

	/**
	 * 根据app信息更新app对应文件
	 * 
	 * @param appId
	 * @return
	 */
	int upAppFileByAppId(HashMap<String, Object> map);

	List<AppFile> getAppFileByAppIdAndRaveIdAndChannel(HashMap<String, Integer> map);
	
	public AppFile getAppFileByAppIdAndRaveId(AppFile appFile);

	List<AppFile> getAppFileByPacs(HashMap<String, Object> map);
	List<AppFile> getAppFileByName(AppFile appFile);
	/**
	 * 根据packagename和channelId查找appfile
	 * 
	 * @param appFile
	 * @return
	 */
	public AppFile selectByChannelIdAndPack(AppFile appFile);
	
	/**
	 * 根据packagename和channelId查找appfile
	 * 
	 * @param appFile
	 * @return
	 */
	public AppFile selectByChannelIdAndName(AppFile appFile);
	// 获取app对应文件信息
	public List<AppFile> getAppFiles(int appId);
	
	// 获取app对应文件信息
	public List<AppFile> getAppFilesGroupBy(int appId);
	
	//获取未审核的应用
	public List<AppFile> getAllCpFile();
}