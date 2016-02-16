package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TRaveCountry;

import java.util.List;

public interface TRaveCountryService {
    int countByExample(Criteria example);

    TRaveCountry selectByPrimaryKey(Integer id);

    List<TRaveCountry> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TRaveCountry record);

    int updateByPrimaryKey(TRaveCountry record);

    int insert(TRaveCountry record);

    int insertSelective(TRaveCountry record);
}