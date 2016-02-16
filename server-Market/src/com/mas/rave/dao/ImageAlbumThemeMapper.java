package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.ImageAlbumTheme;
import com.mas.rave.main.vo.ImageAlbumThemeExample;

/**
 *  image主题数据访问接口
 * @author jieding
 *
 */
public interface ImageAlbumThemeMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(ImageAlbumThemeExample example);
	/**
	 * 根据id删除对应image主题信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加image主题信息
	 */
	int insert(ImageAlbumTheme record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(ImageAlbumTheme record);

	/**
	 * 分页查询image主题信息
	 */
	List<ImageAlbumTheme> selectByExample(ImageAlbumThemeExample example, RowBounds rowBounds);

	/**
	 * 分页查询image主题信息
	 */
	List<ImageAlbumTheme> selectByExample(ImageAlbumThemeExample example);

	/**
	 * 根据id查看image主题信息
	 */
	ImageAlbumTheme selectByPrimaryKey(long id);
	
	/**
	 * 根据themeName查看image主题信息
	 */
	List<ImageAlbumTheme>  selectByThemeName(ImageAlbumTheme record);
	/**
	 * 根据themeName查看image主题信息
	 */
	List<ImageAlbumTheme>  selectByThemeNameCn(String nameCn);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(ImageAlbumTheme record);
	/**
	 * 更新排序
	 * @param record
	 */
    void updateSortByPrimarykey(ImageAlbumTheme record);
    
    
	public List<ImageAlbumTheme> getAllImageAlbumThemes();

}