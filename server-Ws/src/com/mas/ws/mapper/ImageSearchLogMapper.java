package com.mas.ws.mapper;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.ImageSearchLog;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ImageSearchLogMapper {
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
    int insert(ImageSearchLog record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(ImageSearchLog record);

    /**
     * 根据条件查询记录集
     */
    List<ImageSearchLog> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    ImageSearchLog selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") ImageSearchLog record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") ImageSearchLog record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(ImageSearchLog record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(ImageSearchLog record);
}