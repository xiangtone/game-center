package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.ImageInfo;

/**
 * image信息数据访问接口
 * 
 * @author jieding
 * 
 */
public interface ImageInfoMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(ImageInfo example);
	/**
	 * 根据id删除对应image信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加image信息
	 */
	int insert(ImageInfo record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(ImageInfo record);

	/**
	 * 分页查询image信息
	 */
	List<ImageInfo> selectByExample(ImageInfo example, RowBounds rowBounds);

	/**
	 * 分页查询image信息
	 */
	List<ImageInfo> selectByExample(ImageInfo example);

	/**
	 * 根据id查看image信息
	 */
	ImageInfo selectByPrimaryKey(long id);
	/**
	 * 根据name查看image信息
	 */
	List<ImageInfo> selectByName(String name);
	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(ImageInfo record);

	public List<ImageInfo> getAllImageInfos();
	
	int getImageInfoCountByCategory(int categoryId);

}