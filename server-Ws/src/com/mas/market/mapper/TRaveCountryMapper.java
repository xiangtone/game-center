package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TRaveCountry;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface TRaveCountryMapper {
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
    int insert(TRaveCountry record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TRaveCountry record);

    /**
     * 根据条件查询记录集
     */
    List<TRaveCountry> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TRaveCountry selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TRaveCountry record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TRaveCountry record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TRaveCountry record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TRaveCountry record);
}