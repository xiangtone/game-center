package com.mas.ws.mapper;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.MasUser;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface MasUserMapper {
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
    int deleteByPrimaryKey(Integer userId);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(MasUser record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(MasUser record);

    /**
     * 根据条件查询记录集
     */
    List<MasUser> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    MasUser selectByPrimaryKey(Integer userId);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") MasUser record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") MasUser record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(MasUser record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(MasUser record);

	void updateByUserName(MasUser masUser);
}