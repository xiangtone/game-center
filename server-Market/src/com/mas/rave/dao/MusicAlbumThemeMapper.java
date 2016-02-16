package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.MusicAlbumTheme;
import com.mas.rave.main.vo.MusicAlbumThemeExample;

/**
 *music主题数据访问接口
 * 
 * @author jieding
 * 
 */
public interface MusicAlbumThemeMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(MusicAlbumThemeExample example);
	/**
	 * 根据id删除对应music主题信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加music主题信息
	 */
	int insert(MusicAlbumTheme record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(MusicAlbumTheme record);

	/**
	 * 分页查询music主题信息
	 */
	List<MusicAlbumTheme> selectByExample(MusicAlbumThemeExample example, RowBounds rowBounds);

	/**
	 * 分页查询music主题信息
	 */
	List<MusicAlbumTheme> selectByExample(MusicAlbumThemeExample example);

	/**
	 * 根据id查看music主题信息
	 */
	MusicAlbumTheme selectByPrimaryKey(long id);
	/**
	 * 根据themeName查看music主题信息
	 */
	List<MusicAlbumTheme>  selectByThemeName(MusicAlbumTheme criteria);
	/**
	 * 根据themeName查看music主题信息
	 */
	List<MusicAlbumTheme>  selectByThemeNameCn(String nameCn);
	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(MusicAlbumTheme record);


	public List<MusicAlbumTheme> getAllMusicAlbumThemes();

	public void updateSortByPrimarykey(MusicAlbumTheme musicAlbumTheme);
}