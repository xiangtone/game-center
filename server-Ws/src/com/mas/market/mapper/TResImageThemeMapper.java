package com.mas.market.mapper;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TResImageTheme;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface TResImageThemeMapper {
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
    int insert(TResImageTheme record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TResImageTheme record);

    /**
     * 根据条件查询记录集
     */
    List<TResImageTheme> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    TResImageTheme selectByPrimaryKey(Integer themeId);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TResImageTheme record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TResImageTheme record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TResImageTheme record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TResImageTheme record);

	List<TResImageTheme> selectTheme(Criteria cr);
}