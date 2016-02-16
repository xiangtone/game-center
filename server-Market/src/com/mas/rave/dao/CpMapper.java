package com.mas.rave.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.mas.rave.main.vo.Cp;
import com.mas.rave.main.vo.CpExample;

/**
 * cp数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface CpMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(CpExample example);

	/**
	 * 根据id删除对应cp信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加cp信息
	 */
	int insert(Cp record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(Cp record);

	/**
	 * 分页查询cp信息
	 */
	List<Cp> selectByExample(CpExample example, RowBounds rowBounds);
	
	/**
	 * 分页查询cp信息
	 */
	List<Cp> selectByExample(CpExample example);


	/**
	 * 根据id查看cp信息
	 */
	Cp selectByPrimaryKey(long id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(Cp record);
	
	/**
	 * 获取cp状态
	 * @param cpState
	 * @return
	 */
	List<Cp> getCpStates(int cpState);

}