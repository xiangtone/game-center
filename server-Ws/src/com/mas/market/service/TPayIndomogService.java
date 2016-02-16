package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TPayIndomog;

import java.util.List;

public interface TPayIndomogService {
    int countByExample(Criteria example);

    TPayIndomog selectByPrimaryKey(Integer id);

    List<TPayIndomog> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TPayIndomog record);

    int updateByPrimaryKey(TPayIndomog record);

    int insert(TPayIndomog record);

    int insertSelective(TPayIndomog record);

	List<TPayIndomog> selectByExchange(Criteria criteria);
}