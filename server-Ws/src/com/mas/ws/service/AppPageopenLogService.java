package com.mas.ws.service;

import com.mas.ws.pojo.AppPageopenLog;
import com.mas.ws.pojo.Criteria;

import java.util.List;

public interface AppPageopenLogService {
    int countByExample(Criteria example);

    AppPageopenLog selectByPrimaryKey(Integer id);

    List<AppPageopenLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(AppPageopenLog record);

    int updateByPrimaryKey(AppPageopenLog record);

    int insert(AppPageopenLog record);

    int insertSelective(AppPageopenLog record);
}