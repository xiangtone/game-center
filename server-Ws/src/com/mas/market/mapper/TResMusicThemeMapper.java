package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResMusicTheme;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface TResMusicThemeMapper {
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
    int deleteByPrimaryKey(Integer themeId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TResMusicTheme record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TResMusicTheme record);

    /**
     * 根据条件查询记录集
     */
    List<TResMusicTheme> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TResMusicTheme selectByPrimaryKey(Integer themeId);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TResMusicTheme record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TResMusicTheme record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TResMusicTheme record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TResMusicTheme record);

	List<TResMusicTheme> selectTheme(Criteria cr);
}