package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.Pay;
import com.mas.rave.main.vo.PayExample;

/**
 * 支付活动数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface PayMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(PayExample example);

	/**
	 * 根据id删除对应支付活动信息
	 */
	int deleteByPrimaryKey(int id);

	/**
	 * 增加支付活动信息
	 */
	int insert(Pay record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(Pay record);

	/**
	 * 分页查询支付活动信息
	 */
	List<Pay> selectByExample(PayExample example, RowBounds rowBounds);

	/**
	 * 分页查询支付活动信息
	 */
	List<Pay> selectByExample(PayExample example);

	/**
	 * 根据id查看支付活动信息
	 */
	Pay selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(Pay record);

}