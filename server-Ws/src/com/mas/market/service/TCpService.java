package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TCp;

import java.util.List;

public interface TCpService {
    int countByExample(Criteria example);

    TCp selectByPrimaryKey(Integer id);

    List<TCp> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TCp record);

    int updateByPrimaryKey(TCp record);

    int insert(TCp record);

    int insertSelective(TCp record);
}