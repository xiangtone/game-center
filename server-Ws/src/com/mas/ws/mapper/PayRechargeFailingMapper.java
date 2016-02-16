package com.mas.ws.mapper;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.PayRechargeFailing;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface PayRechargeFailingMapper {
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
    int insert(PayRechargeFailing record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(PayRechargeFailing record);

    /**
     * 根据条件查询记录集
     */
    List<PayRechargeFailing> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    PayRechargeFailing selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") PayRechargeFailing record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") PayRechargeFailing record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(PayRechargeFailing record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(PayRechargeFailing record);
}