package com.mas.ws.service;

import com.mas.ws.pojo.AppLogoutLog;
import com.mas.ws.pojo.Criteria;

import java.util.List;

public interface AppLogoutLogService {
    int countByExample(Criteria example);

    AppLogoutLog selectByPrimaryKey(Integer id);

    List<AppLogoutLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(AppLogoutLog record);

    int updateByPrimaryKey(AppLogoutLog record);

    int insert(AppLogoutLog record);

    int insertSelective(AppLogoutLog record);
}