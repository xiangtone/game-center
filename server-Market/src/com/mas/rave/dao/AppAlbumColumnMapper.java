package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppAlbumColumn;
import com.mas.rave.main.vo.AppAlbumColumnExample;

/**
 * app对应专辑数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppAlbumColumnMapper {

	/**
	 * 分页查询app专辑信息
	 */
	List<AppAlbumColumn> selectByExample(AppAlbumColumnExample example, RowBounds rowBounds);
	/**
	 * 获取当前总页数
	 */
	int countByExample(AppAlbumColumnExample example);

	/**
	 * 根据id删除对应app专辑信息
	 */
	int deleteByPrimaryKey(int id);

	/**
	 * 增加app专辑信息
	 */
	int insert(AppAlbumColumn record);


	/**
	 * 分页查询app专辑信息
	 */
	List<AppAlbumColumn> selectByExample(AppAlbumColumnExample example);

	/**
	 * 根据id查看app专辑信息
	 */
	AppAlbumColumn selectByPrimaryKey(long id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppAlbumColumn record);
	
	int updateSortByPrimaryKey(AppAlbumColumn entity);
	
	 /**
	  * 根据app顶级分类查找app专辑信息
	  */
	List<AppAlbumColumn> getAppAlbumColumnsByAppAlbumId(int appAlbumId);

}