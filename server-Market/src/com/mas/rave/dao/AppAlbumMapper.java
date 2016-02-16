package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppAlbum;
import com.mas.rave.main.vo.AppAlbumExample;

/**
 * app分发数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppAlbumMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(AppAlbumExample example);

	/**
	 * 根据id删除对应app内容信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加app内容信息
	 */
	int insert(AppAlbum record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(AppAlbum record);

	/**
	 * 分页查询app内容信息
	 */
	List<AppAlbum> selectByExample(AppAlbumExample example, RowBounds rowBounds);

	/**
	 * 分页查询app内容信息
	 */
	List<AppAlbum> selectByExample(AppAlbumExample example);

	/**
	 * 根据id查看app内容信息
	 */
	AppAlbum selectByPrimaryKey(long id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppAlbum record);

}