package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppCollection;
import com.mas.rave.main.vo.AppCollectionExample;

/**
 *  image主题数据访问接口
 * @author jieding
 *
 */
public interface AppCollectionMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(AppCollectionExample example);
	/**
	 * 根据id删除对应image主题信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加image主题信息
	 */
	int insert(AppCollection record);

	/**
	 * 分页查询image主题信息
	 */
	List<AppCollection> selectByExample(AppCollectionExample example, RowBounds rowBounds);
	
	
	/**
	 * 分页查询image主题信息
	 * liwei 2015-06-16
	 * 修改排序
	 */
	List<AppCollection> selectByExample1(AppCollectionExample example, RowBounds rowBounds);

	/**
	 * 分页查询image主题信息
	 */
	List<AppCollection> selectByExample(AppCollectionExample example);

	/**
	 * 根据id查看image主题信息
	 */
	AppCollection selectByPrimaryKey(long id);
	
	/**
	 * 根据themeName查看image主题信息
	 */
	List<AppCollection>  selectByName(AppCollection record);


	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppCollection record);
	/**
	 * 更新排序
	 * @param record
	 */
    void updateSortByPrimarykey(AppCollection record);

}