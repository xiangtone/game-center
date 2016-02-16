package com.mas.rave.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppAlbumResExample;

/**
 * app分发数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppAlbumResMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(AppAlbumResExample example);

	/**
	 * 根据id删除对应app内容信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 根据id删除对应app内容信息
	 */
	int deleteByApp(HashMap<String, Object> map);

	/**
	 * 增加app内容信息
	 */
	int insert(AppAlbumRes record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(AppAlbumRes record);

	/**
	 * 分页查询app内容信息
	 */
	List<AppAlbumRes> selectByExample(AppAlbumResExample example, RowBounds rowBounds);

	/**
	 * 分页查询app内容信息
	 */
	List<AppAlbumRes> selectByExample(AppAlbumResExample example);

	/**
	 * 根据id查看app内容信息
	 */
	AppAlbumRes selectByPrimaryKey(long id);
	
	
	/**
	 * 根据columnId查看app内容信息
	 */
	List<String> selectByColumnId(AppAlbumRes appAlbumRes);
	
	int deleteByCollectionId(Integer collectionId);
	
	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppAlbumRes record);

	int deleteByAlbumId(Integer albumId);

	int updateSortByPrimarykey(AppAlbumRes entity);

	List<AppAlbumRes> getSelectAppFiles(HashMap<String, Object> map, RowBounds rowBounds);

	List<AppAlbumRes> getSelectAppFiles(HashMap<String, Object> map);

	Integer getSelectAppFilesCount(HashMap<String, Object> map);

	void insertSelectApps(Map<String, Object> map);
	/**
	 * 根据app信息更新app对应名称
	 * @param appId
	 * @return
	 */
	int upAppAlbumResByAppId(HashMap<String, Object> map);
}