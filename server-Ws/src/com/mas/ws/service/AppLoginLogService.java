package com.mas.ws.service;

import com.mas.ws.pojo.AppLoginLog;
import com.mas.ws.pojo.Criteria;

import java.util.List;

public interface AppLoginLogService {
    int countByExample(Criteria example);

    AppLoginLog selectByPrimaryKey(Integer id);

    List<AppLoginLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(AppLoginLog record);

    int updateByPrimaryKey(AppLoginLog record);

    int insert(AppLoginLog record);

    int insertSelective(AppLoginLog record);
}