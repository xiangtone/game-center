package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.Channel;
import com.mas.rave.main.vo.ChannelExample;

/**
 * 渠道数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface ChannelMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(ChannelExample example);

	/**
	 * 根据id删除对应渠道信息
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * 增加渠道信息
	 */
	int insert(Channel record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(Channel record);

	/**
	 * 分页查询渠道信息
	 */
	List<Channel> selectByExample(ChannelExample example, RowBounds rowBounds);
	
	/**
	 * 分页查询渠道信息
	 */
	List<Channel> selectByExample(ChannelExample example);

	/**
	 * 查找所有一级或二级分类
	 * 
	 * @param fatherId
	 * @return
	 */
	List<Channel> getChannels(String fatherId);

	/**
	 * 根据id查看渠道信息
	 */
	Channel selectByPrimaryKey(Integer id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(Channel record);

	int updateSortByPrimarykey(Channel channel);
	
	int deletSecondChannel(int fatherId);
}