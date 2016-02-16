package com.mas.ws.mapper;

import com.mas.ws.pojo.AppChangeuserLog;
import com.mas.ws.pojo.Criteria;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface AppChangeuserLogMapper {
    /**
     * 根据条件查询记录总数
     */
    int countByExample(Criteria example);

    /**
     * 根据条件删除记录
     */
    int deleteByExample(Criteria example);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(AppChangeuserLog record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(AppChangeuserLog record);

    /**
     * 根据条件查询记录集
     */
    List<AppChangeuserLog> selectByExample(Criteria example);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") AppChangeuserLog record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") AppChangeuserLog record, @Param("condition") Map<String, Object> condition);

	int updateByPrimaryKeySelective(AppChangeuserLog record);

	AppChangeuserLog selectByPrimaryKey();
}