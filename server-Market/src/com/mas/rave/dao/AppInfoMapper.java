package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppInfo;

/**
 * app数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppInfoMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(AppInfo example);

	/**
	 * 根据id删除对应app信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加app信息
	 */
	int insert(AppInfo record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(AppInfo record);

	/**
	 * 分页查询app信息
	 */
	List<AppInfo> selectByExample(AppInfo example, RowBounds rowBounds);

	/**
	 * 分页查询app信息
	 */
	List<AppInfo> selectByExample(AppInfo example);

	List<AppInfo> selectByExample1(AppInfo example);

	/**
	 * 根据id查看app信息
	 */
	AppInfo selectByPrimaryKey(long id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppInfo record);

	int updateNum(int id);

	/**
	 * 按fatherChannelId和name查询app应用
	 * 
	 * @param appInfo
	 * @return
	 */
	AppInfo selectByFatherChannelIdAndName(AppInfo appInfo);

	AppInfo selectByName(String name);

	public List<AppInfo> getAllAppInfos();

	int getAppInfoCountByCategory(int categoryId);

	// 检测cp是存在
	public int checkCp(Integer cpId);

	public void upFree(AppInfo appInfo);
	List<AppInfo> selectADayAgoInfo();
}