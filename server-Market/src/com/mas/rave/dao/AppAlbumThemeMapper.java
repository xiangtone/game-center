package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.AppAlbumTheme;
import com.mas.rave.main.vo.AppAlbumThemeExample;

/**
 * app对应主题数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface AppAlbumThemeMapper {

	/**
	 * 分页查询app主题信息
	 */
	List<AppAlbumTheme> selectByExample(AppAlbumThemeExample example, RowBounds rowBounds);

	/**
	 * 获取当前总页数
	 */
	int countByExample(AppAlbumThemeExample example);

	/**
	 * 根据id删除对应app主题信息
	 */
	int deleteByPrimaryKey(int id);

	/**
	 * 增加app主题信息
	 */
	int insert(AppAlbumTheme record);

	/**
	 * 分页查询app主题信息
	 */
	List<AppAlbumTheme> selectByExample(AppAlbumThemeExample example);

	/**
	 * 根据id查看app主题信息
	 */
	AppAlbumTheme selectByPrimaryKey(long id);

	/**
	 * 根据themeName查看app主题信息
	 */
	List<AppAlbumTheme> selectByThemeName(String name);

	/**
	 * 根据themeName查看app主题信息
	 */
	List<AppAlbumTheme> selectByThemeNameCn(String nameCn);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(AppAlbumTheme record);

	public int updateSortByPrimarykey(AppAlbumTheme appAlbumTheme);

	/**
	 * 获取所有开发者banner图上
	 * 
	 * @param falg
	 * @return
	 */
	public List<AppAlbumTheme> getBanners(int falg);

	/**
	 * 获取开者者banner图
	 * 
	 * @param id
	 * @return
	 */
	AppAlbumTheme getappBanner(int id);

}