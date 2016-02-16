package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.MusicInfo;

/**
 * music信息数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface MusicInfoMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(MusicInfo example);
	/**
	 * 根据id删除对应music信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加music信息
	 */
	int insert(MusicInfo record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(MusicInfo record);

	/**
	 * 分页查询music信息
	 */
	List<MusicInfo> selectByExample(MusicInfo example, RowBounds rowBounds);

	/**
	 * 分页查询music信息
	 */
	List<MusicInfo> selectByExample(MusicInfo example);

	/**
	 * 根据id查看music信息
	 */
	MusicInfo selectByPrimaryKey(long id);
	/**
	 * 根据name查看music信息
	 */
	List<MusicInfo> selectByName(String name);
	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(MusicInfo record);

	/**
	 * 按fatherChannelId和name查询music应用
	 * 
	 * @param appInfo
	 * @return
	 */
	MusicInfo selectByFatherChannelIdAndName(MusicInfo musicInfo);

	public List<MusicInfo> getAllMusicInfos();
	
	int getMusicInfoCountByCategory(int categoryId);

}