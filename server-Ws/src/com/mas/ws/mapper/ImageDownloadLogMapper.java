package com.mas.ws.mapper;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.ImageDownloadLog;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ImageDownloadLogMapper {
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
    int insert(ImageDownloadLog record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(ImageDownloadLog record);

    /**
     * 根据条件查询记录集
     */
    List<ImageDownloadLog> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    ImageDownloadLog selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") ImageDownloadLog record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") ImageDownloadLog record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(ImageDownloadLog record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(ImageDownloadLog record);
}