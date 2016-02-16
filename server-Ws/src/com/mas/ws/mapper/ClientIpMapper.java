package com.mas.ws.mapper;

import com.mas.ws.pojo.ClientIp;
import com.mas.ws.pojo.Criteria;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ClientIpMapper {
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
    int insert(ClientIp record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(ClientIp record);

    /**
     * 根据条件查询记录集
     */
    List<ClientIp> selectByExample(Criteria example);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") ClientIp record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") ClientIp record, @Param("condition") Map<String, Object> condition);
}