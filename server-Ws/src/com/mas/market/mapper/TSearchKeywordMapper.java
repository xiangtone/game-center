package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TSearchKeyword;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TSearchKeywordMapper {
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
    int deleteByPrimaryKey(Integer searchId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TSearchKeyword record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TSearchKeyword record);

    /**
     * 根据条件查询记录集
     */
    List<TSearchKeyword> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TSearchKeyword selectByPrimaryKey(Integer searchId);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TSearchKeyword record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TSearchKeyword record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TSearchKeyword record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TSearchKeyword record);

	List<TSearchKeyword> selectKeywords(Criteria cr);

	void updateSearchNum(Integer searchId);
}