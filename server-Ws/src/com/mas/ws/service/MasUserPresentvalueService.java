package com.mas.ws.service;

import com.mas.ws.pojo.Criteria;
import com.mas.ws.pojo.MasUserPresentvalue;

import java.util.List;

public interface MasUserPresentvalueService {
    int countByExample(Criteria example);

    MasUserPresentvalue selectByPrimaryKey(Integer id);

    List<MasUserPresentvalue> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(MasUserPresentvalue record);

    int updateByPrimaryKey(MasUserPresentvalue record);

    int insert(MasUserPresentvalue record);

    int insertSelective(MasUserPresentvalue record);
}