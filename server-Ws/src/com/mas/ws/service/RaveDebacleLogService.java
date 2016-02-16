package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.RaveDebacleLog;

import java.util.List;

public interface RaveDebacleLogService {
    int countByExample(Criteria example);

    RaveDebacleLog selectByPrimaryKey(Integer id);

    List<RaveDebacleLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(RaveDebacleLog record);

    int updateByPrimaryKey(RaveDebacleLog record);

    int insert(RaveDebacleLog record);

    int insertSelective(RaveDebacleLog record);
}