package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TCategory;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TCategoryMapper {
    /**
     * 根据条件查询记录总数
     */
    int countByExample(Criteria example);

    /**
     * 根据条件删除记录
     */
    int deleteByExample(Criteria example);

    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TCategory record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TCategory record);

    /**
     * 根据条件查询记录集
     */
    List<TCategory> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TCategory selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TCategory record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TCategory record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TCategory record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TCategory record);

	List<TCategory> selectByCtRaveId(Criteria cr);
	
	List<TCategory> selectLevelCat(Criteria cr);
	
	public List<Integer> selectAllCatIds(Criteria cr);
	
	List<TCategory> selectByAllCatIds(Criteria cr);
}