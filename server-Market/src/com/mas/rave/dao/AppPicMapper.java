package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppAlbumRes;
import com.mas.rave.main.vo.AppPic;
import com.mas.rave.main.vo.AppPicExample;

/**
 * app对应截图数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppPicMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(AppPicExample example);

	/**
	 * 根据id删除对应app截图信息
	 */
	int deleteByPrimaryKey(int id);

	/**
	 * 增加app截图信息
	 */
	int insert(AppPic record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(AppPic record);

	/**
	 * 分页查询app截图信息
	 */
	List<AppPic> selectByExample(AppPicExample example, RowBounds rowBounds);

	/**
	 * 分页查询app截图信息
	 */
	List<AppPic> selectByExample(AppPicExample example);

	/**
	 * 根据id查看app截图信息
	 */
	AppPic selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppPic record);

	void updateSortByPrimarykey(AppPic entity);

}