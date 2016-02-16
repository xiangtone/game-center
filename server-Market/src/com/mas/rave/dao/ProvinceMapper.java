package com.mas.rave.dao;

import java.util.List;

import com.mas.rave.main.vo.Province;
import com.mas.rave.main.vo.ProvinceExample;

/**
 * 省份数据访问接口
 * 
 * @author liwei.sz
 * 
 */
public interface ProvinceMapper {

	/**
	 * 获取当前总页数
	 */
	int countByExample(ProvinceExample example);

	/**
	 * 根据id删除对应省份信息
	 */
	int deleteByPrimaryKey(long id);

	/**
	 * 增加省份文件信息
	 */
	int insert(Province record);

	/**
	 * 根据参数增加
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(Province record);


	/**
	 * 分页查询省份文件信息
	 */
	List<Province> selectByExample(ProvinceExample example);

	/**
	 * 根据id查看省份文件信息
	 */
	Province selectByPrimaryKey(int id);

	/**
	 * 根据主键更新
	 */
	int updateByPrimaryKey(Province record);

}