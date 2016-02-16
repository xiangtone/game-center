package com.mas.ws.service;

import com.mas.ws.pojo.AppChangeuserLog;
import com.mas.ws.pojo.Criteria;

import java.util.List;

public interface AppChangeuserLogService {
    int countByExample(Criteria example);

    AppChangeuserLog selectByPrimaryKey();

    List<AppChangeuserLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(AppChangeuserLog record);

    int updateByPrimaryKey(AppChangeuserLog record);

    int insert(AppChangeuserLog record);

    int insertSelective(AppChangeuserLog record);
}