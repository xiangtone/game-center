package com.mas.ws.service;

import com.mas.ws.pojo.ClientActivateLog;
import com.mas.ws.pojo.Criteria;

import java.util.List;

public interface ClientActivateLogService {
    int countByExample(Criteria example);

    ClientActivateLog selectByPrimaryKey(Integer id);

    List<ClientActivateLog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(ClientActivateLog record);

    int updateByPrimaryKey(ClientActivateLog record);

    int insert(ClientActivateLog record);

    int insertSelective(ClientActivateLog record);
}