package com.mas.market.service;

import com.mas.market.pojo.Criteria;
import com.mas.market.pojo.TAppTestin;

import java.util.List;

public interface TAppTestinService {
    int countByExample(Criteria example);

    TAppTestin selectByPrimaryKey(String adaptId);

    List<TAppTestin> selectByExample(Criteria example);

    int updateByPrimaryKeySelective(TAppTestin record);

    int updateByPrimaryKey(TAppTestin record);

    int insert(TAppTestin record);

    int insertSelective(TAppTestin record);

	void updateByAdaptId(TAppTestin tAppTestin);
}