package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.OurPartners;
import com.mas.rave.main.vo.OurPartnersExample;

/**
 * 广告全作
 * 
 * @author liwei.sz
 * 
 */
public interface OurPartnersMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(OurPartnersExample example);

	/**
	 * 根据id删除对应广告合作　
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加广告合作
	 */
	int insert(OurPartners record);

	/**
	 * 分页查询广告全作
	 */
	List<OurPartners> selectByExample(OurPartnersExample example, RowBounds rowBounds);

	/**
	 * 分页查询广告合作　
	 */
	List<OurPartners> selectByExample(OurPartnersExample example);

	/**
	 * 根据id查看广告全作
	 */
	OurPartners selectByPrimaryKey(long id);

	/**
	 * 根据name查看广告任凭
	 */
	List<OurPartners> selectByName(OurPartners record);

	/**
	 *更新
	 */
	int updateByPrimaryKey(OurPartners record);

	/**
	 * 更新排序
	 * 
	 * @param record
	 */
	void updateSortByPrimarykey(OurPartners record);
}