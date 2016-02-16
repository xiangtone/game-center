package com.mas.ws.service;

import com.mas.ws.pojo.AppSearchLog;
import com.mas.ws.pojo.Criteria;

import java.util.List;

public interface AppSearchLogService {
    int countByExample(Criteria example);

    AppSearchLog selectByPrimaryKey(Integer id);

    List<AppSearchLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(AppSearchLog record);

    int updateByPrimaryKey(AppSearchLog record);

    int insert(AppSearchLog record);

    int insertSelective(AppSearchLog record);
}