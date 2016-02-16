package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.Market;
import com.mas.rave.main.vo.MarketExample;

/**
 * 平台数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface MarketMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(MarketExample example);

	/**
	 * 根据id删除对应平台信息
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * 增加平台信息
	 */
	int insert(Market record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(Market record);

	/**
	 * 分页查询平台信息
	 */
	List<Market> selectByExample(MarketExample example, RowBounds rowBounds);

	List<Market> selectByExample(MarketExample example);

	// 根据包名获取对象
	public Market getMarketPac(String pac_name);

	/**
	 * 根据id查看平台信息
	 */
	Market selectByPrimaryKey(Integer id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(Market record);

}