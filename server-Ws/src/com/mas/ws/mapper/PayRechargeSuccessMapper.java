package com.mas.ws.mapper;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.PayRechargeSuccess;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface PayRechargeSuccessMapper {
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
    int insert(PayRechargeSuccess record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(PayRechargeSuccess record);

    /**
     * 根据条件查询记录集
     */
    List<PayRechargeSuccess> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    PayRechargeSuccess selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") PayRechargeSuccess record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") PayRechargeSuccess record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(PayRechargeSuccess record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(PayRechargeSuccess record);
}